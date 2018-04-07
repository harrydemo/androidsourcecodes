/**
 * OnionCoffee - Anonymous Communication through TOR Network
 * Copyright (C) 2005-2007 RWTH Aachen University, Informatik IV
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2 as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 */
package TorJava;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * Management thread
 * 
 * @author Lexi Pimenidis
 * @author Michael Koellejan
 * @version unstable
 */

class TorBackgroundMgmt extends Thread {
    static final int MILLISEC = 1000; // general multiplicator for time

    static int INITIAL_INTERVAL = 3; // time to sleep until first actions
    static int INTERVAL = 10; // time to wait inbetween working loads

    static int CIRCUITS_KEEP_ALIVE_INTERVAL = 30; // interval of padding messages on circuits
    static int STREAMS_KEEP_ALIVE_INTERVAL = 30; // interval of padding messages on streams

    static int MAX_NUMBER_THREADS = 4;
    
    Tor tor; // pointer to main class

    int number_of_circuits; // at least this amount of circuits should always be
                            // available

    // upper bound is (number_of_circuits+tor.config.circuitsMaximumNumber)

    private long now_long; // store the current time


    private ArrayList<Thread> brtList; // List of background threads (for graceful
                                // close)

    boolean stopped = false; // As stop() is depreceated we follow the Sun
                                // recomm.

    DirectoryManager dirman = null;
    
    TorBackgroundMgmt(Tor tor, int number_of_circuits, boolean fastStartup) {
        this.brtList = new ArrayList<Thread>(number_of_circuits);
        this.tor = tor;
        this.number_of_circuits = number_of_circuits;
        this.setName("TorBackgroundMgmt");
        now_long = new Date().getTime();
        spawn_idle_circuits(number_of_circuits);
        this.dirman = new DirectoryManager(tor, fastStartup);
        this.start();
    }

    /** create some empty circuits to have at hand - does so in the background */
    private void spawn_idle_circuits(int amount) {
        // Don't create circuits until not at least a certain fraction of the routers is known
        if (tor.dir.nodeCounter > 0) {
          int minDescriptors = Math.min(Math.round(TorConfig.min_percentage*tor.dir.nodeCounter), TorConfig.min_descriptors);
          if (tor.dir.torServers.size() < Math.max(minDescriptors, TorConfig.route_min_length)) {
            Logger.logGeneral(Logger.INFO, "Not yet spawning circuits ("+
                      tor.dir.torServers.size()+"/"+tor.dir.nodeCounter+" descriptors present)");  
            return;
          }
          else Logger.logGeneral(Logger.INFO, "TorBackgroundMgmt.spawn_idle_circuits: Spawn "+amount+" new circuits");          
        } else {
          return;
        }
        
        TorKeeper.setComplete(TorKeeper.DESCRIPTORS_FETCHED, true);
        
        // Cleanup our background thread list
        ListIterator<Thread> brtIterator = brtList.listIterator();
        while (brtIterator.hasNext()) {
            Thread brt = brtIterator.next();
            if (!brt.isAlive())
                brtIterator.remove();
        }
        
        if ((amount + brtList.size())>MAX_NUMBER_THREADS) {
        	amount = MAX_NUMBER_THREADS - brtList.size();
        	if (amount < 0) amount = 0;
    	}

        // Spawn new background threads
        for (int i = 0; i < amount; ++i) {
            Thread brt = new Thread() {
                public void run() {
                    try {
                        // idle threadds should at least allow using port 80
                        TCPStreamProperties sp = new TCPStreamProperties();
                        sp.port = 80;
                        new Circuit(tor, tor.fnh, tor.dir, sp);
                    } catch (Exception e) {
                        Logger.logCircuit(Logger.VERBOSE,"TorBackgroundMgmt.spawn_idle_circuits: "+e.getMessage());
                    }
                    // Common.listAllRunningThreads();
                }
            };
            Logger.logGeneral(Logger.RAW_DATA, "TorBackgroundMgmt.spawn_idle_circuits: Circuit-Spawning thread started.");
            brt.setName("Spawn Idle Thread");
            brt.start();
            // Common.listAllRunningThreads();
            brtList.add(brt);
        }
    }

    /**
     * sends keep-alive data on circuits
     */
    private void send_keep_alive_packets() {
        Iterator<String> i = tor.fnh.tls.keySet().iterator();
        while (i.hasNext()) {
            TLSConnection tls = (TLSConnection) tor.fnh.tls.get(i.next());
            Iterator<Integer> i2 = tls.circuits.keySet().iterator();
            while (i2.hasNext()) {
                // check if this circuit needs a keep-alive-packet
                Circuit c = (Circuit) tls.circuits.get(i2.next());
                if ((c.established) && (now_long - c.last_cell.getTime() > CIRCUITS_KEEP_ALIVE_INTERVAL * MILLISEC)) {
                    Logger.logGeneral(Logger.RAW_DATA, "TorBackgroundMgmt.send_keep_alive_packets(): Circuit " + c.print());
                    c.sendKeepAlive();
                }
                // check streams in circuit
                Iterator<Integer> i3 = c.streams.keySet().iterator();
                while (i3.hasNext()) {
                    TCPStream stream = (TCPStream) c.streams.get(i3.next());
                    if ((stream.established) && (!stream.closed) && (now_long - stream.last_cell.getTime() > STREAMS_KEEP_ALIVE_INTERVAL * MILLISEC)) {
                        Logger.logGeneral(Logger.RAW_DATA, "TorBackgroundMgmt.send_keep_alive_packets(): Stream " + stream.print());
                        stream.sendKeepAlive();
                    }
                }
            }
        }
    }

    /**
     * used to determine which (old) circuits can be torn down because there are
     * enough new circuits. or builds up new circuits, if there are not enough.
     */
    private void idle_circuits() {
        // count circuits
        int circuits_total = 0; // all circuits
        int circuits_alive = 0; // circuits that are building up, or that are established
        int circuits_established = 0; // established, but not already closed
        int circuits_closed = 0; // closing down

        Iterator<String> i = tor.fnh.tls.keySet().iterator();
        while (i.hasNext()) {
            TLSConnection tls = (TLSConnection) tor.fnh.tls.get(i.next());
            Iterator<Integer> i2 = tls.circuits.keySet().iterator();
            while (i2.hasNext()) {
                Circuit c = (Circuit) tls.circuits.get(i2.next());
                String flag = "";
                ++circuits_total;
                if (c.closed) {
                    flag = "C";
                    ++circuits_closed;
                } else {
                    flag = "B";
                    ++circuits_alive;
                    if (c.established) {
                        flag = "E";
                        ++circuits_established;
                    };
                }
                Logger.logGeneral(Logger.RAW_DATA,"TorBackgroundMgmt.idle_circuits(): "+flag+" rank "+c.ranking+ " fails "+c.stream_fails+" of "+c.stream_counter+ " TLS "+tls.server.nickname+"/"+c.print());
            }
        }
        Logger.logGeneral(Logger.RAW_DATA, "TorBackgroundMgmt.idle_circuits(): circuit counts: " + (circuits_alive - circuits_established) + " building, " + circuits_established + " established + " + circuits_closed + " closed = " + circuits_total);
        // check if enough 'alive' circuits are there
        if (circuits_established >= 1) TorKeeper.setComplete(TorKeeper.CIRCUITS_AVAILABLE, true);
        
        if (circuits_alive < number_of_circuits) {
            //if (tor.dir.torServers.size()>1) 
            //Logger.logGeneral(Logger.INFO, "TorBackgroundMgmt.idle_circuits(): spawn " + (number_of_circuits - circuits_alive) + " new circuits");
            spawn_idle_circuits( (number_of_circuits - circuits_alive) * 3 / 2 );
        } else if (circuits_established > number_of_circuits + TorConfig.circuitsMaximumNumber) {
            // TODO: if for some reason there are too many established circuits. close the oldest ones
            Logger.logGeneral(Logger.VERBOSE, "TorBackgroundMgmt.idle_circuits(): kill " + (number_of_circuits + TorConfig.circuitsMaximumNumber - circuits_alive) + "new circuits (FIXME)");
        }
    }

    /**
     * used to close circuits that are marked for closing, but are still alive.
     * They are closed, if no more streams are contained.
     */
    private void tear_down_closed_circuits() {
        Iterator<String> i = tor.fnh.tls.keySet().iterator();
        while (i.hasNext()) {
            TLSConnection tls = (TLSConnection) tor.fnh.tls.get(i.next());
            Iterator<Integer> i2 = tls.circuits.keySet().iterator();
            while (i2.hasNext()) {
                Circuit c = (Circuit) tls.circuits.get(i2.next());
                // check if stream is establishing but doesn't had any action for a longer period of time
                Iterator<Integer> i3 = c.streams.keySet().iterator();
                while (i3.hasNext()) {
                  TCPStream s = (TCPStream) c.streams.get(i3.next());
                  long diff = (now_long - s.last_action.getTime()) / MILLISEC;
                  if ((!s.established) || s.closed) {
                    if (diff > (2*TorConfig.queueTimeoutStreamBuildup)) {
                      //System.out.println("close "+diff+" "+s.print());
                      Logger.logGeneral(Logger.VERBOSE, "TorBackgroundMgmt.tear_down_closed_circuits(): closing stream (too long building) " + s.print());
                      s.close(true);
                    } else {
                      //System.out.println("Checked "+diff+" "+s.print());
                    }
                  } else {
                    //System.out.println("OK "+diff+" "+s.print());
                  }
                }
                // check if circuit is establishing but doesn't had any action for a longer period of time
                if ((!c.established) && (!c.closed)) {
                  if ((now_long - c.last_action.getTime()) / MILLISEC > (2*TorConfig.queueTimeoutCircuit)) {
                    Logger.logGeneral(Logger.VERBOSE, "TorBackgroundMgmt.tear_down_closed_circuits(): closing (too long building) " + c.print());
                    c.close(false);
                  }
                }
                // check if this circuit should not accept more streams
                if (c.established_streams > TorConfig.streamsPerCircuit) {
                    Logger.logGeneral(Logger.VERBOSE, "TorBackgroundMgmt.tear_down_closed_circuits(): closing (maximum streams) " + c.print());
                    c.close(false);
                }
                // if closed, recall close() again and again to do garbage collection and stuff
                if (c.closed) c.close(false);
                // check if this circuit can be removed from the set of circuits
                if (c.destruct) {
                    Logger.logGeneral(Logger.VERBOSE, "TorBackgroundMgmt.tear_down_closed_circuits(): destructing circuit " + c.print());
                    i2.remove();
                }
            }
        }
    }
    
    public void close_all_circuits() {
    	TorKeeper.setComplete(TorKeeper.CIRCUITS_AVAILABLE, false);
    	Iterator<String> i = tor.fnh.tls.keySet().iterator();
        while (i.hasNext()) {
        	TLSConnection tls = (TLSConnection) tor.fnh.tls.get(i.next());
            Iterator<Integer> i2 = tls.circuits.keySet().iterator();
            while (i2.hasNext()) {
            	Circuit c = (Circuit) tls.circuits.get(i2.next());
            	c.close(true);
            	if (c.destruct) i2.remove();
            }
        }
        this.interrupt();
    }

    public void close() {
        // stop sub-thread
        dirman.stopped = true;
        dirman.interrupt();
        // stop this thread
        this.stopped = true;
        this.interrupt();
    }

    public void cleanup() {
        ListIterator<Thread> brtIterator = brtList.listIterator();
        while (brtIterator.hasNext()) {
            Thread brt = brtIterator.next();
            if (brt.isAlive())
                brt.interrupt();
            brtIterator.remove();
        }
    }

    public void run() {
        try {
            sleep(INITIAL_INTERVAL * MILLISEC);
        } catch (InterruptedException e) {
        }
        // run until killed
        outerWhile: while (!stopped) {
            try {
                now_long = new Date().getTime();
                // do work
                idle_circuits();
                tear_down_closed_circuits();
                send_keep_alive_packets();
                // wait
                sleep(INTERVAL * MILLISEC);
            } catch (InterruptedException e) {
                break outerWhile;
            } catch (Exception e) {
                break outerWhile;
            }
        }
        cleanup();
    }
}

/**
 * Directory-Manager Class. This class is done in a separate thread
 * to avoid stalling the other management tasks: updating a directory
 * can take quite an amount of time :-/
 */
class DirectoryManager extends Thread {
  boolean stopped = false;
  private Tor tor;
  private long now_long;
  private long dir_next_update; // timestamp
  boolean fastStartup;

  DirectoryManager(Tor tor, boolean fastStartup) {
	this.fastStartup = fastStartup;
    this.tor = tor;
    dir_next_update = now_long; // + TorConfig.intervalDirectoryRefresh*60* MILLISEC;
    this.start();
  }

  /**
   * keep up to date with the directory informations
   */
  private void update_directory() {
    if ((now_long > dir_next_update) || (tor.dir.torServers.size()<1)) {
    	
      Logger.logGeneral(Logger.INFO, "TorBackgroundMgmt.update_directory: updating directory");
      if (fastStartup) this.setPriority(8);
      int updateVer = tor.dir.refreshListOfServers(fastStartup);
      if (fastStartup) fastStartup = false;
      if (updateVer==1) 
        dir_next_update = now_long + TorConfig.intervalDirectoryV1Refresh* 60* TorBackgroundMgmt.MILLISEC;
      else
        dir_next_update = now_long + TorConfig.intervalDirectoryRefresh* 60* TorBackgroundMgmt.MILLISEC;
      if (tor.dir.torServers.size()>1) {
        //tor.dir.writeDirectoryToFile(TorConfig.getCacheFilename());
    	  this.setPriority(3); // Lower priority for future updates
      } else {
        Logger.logGeneral(Logger.WARNING, "TorBackgroundMgmt.update_directory: no directory available");
      }
      
	  	// Create a new KeyPair if needed
	  	tor.privateKeyHandler.prepareCachedKeyPairIfNeeded();
    }
  }

  public void run() {
    // run until killed
    while (!stopped) {
      try {
        now_long = new Date().getTime();
        // do work
        update_directory();
        // wait
        sleep(TorBackgroundMgmt.INTERVAL * TorBackgroundMgmt.MILLISEC);
      } catch (Exception e) {
        stopped = true;
      }
    }
  }

}
// vim: et
