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

import java.io.IOException;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import TorJava.Common.TorException;

/**
 * maintains the list of active TLS-connections to Tor nodes.
 * 
 * @author Lexi Pimenidis
 * @author Andriy Panchenko
 * @version unstable
 */

class FirstNodeHandler {
    
    ConcurrentHashMap<String,TLSConnection> tls;

    static Random rnd;

	// nicknames of currently used nodes in circuits as key, # of cirs - value
    static ConcurrentHashMap<String,Integer> currentlyUsedNodes;

    Tor tor;

    /**
     * initialize Handler of TLSConnections
     */
    FirstNodeHandler(Tor tor) throws IOException {
        tls = new ConcurrentHashMap<String,TLSConnection>();
        rnd = new Random();
		currentlyUsedNodes = new ConcurrentHashMap<String,Integer>();
        this.tor = tor;
    }

    /**
     * return a pointer to a TLS-connection to a certain node. if there is
     * none, it is created and returned.
     * 
     * @param server
     *            the node to connect to
     * @return the TLS connection
     */
    TLSConnection get_connection(Server server) throws IOException,
            TorException {
        if (server == null)
            throw new TorException("FirstNodeHandler: server is NULL");
        // check if TLS-connections to node established
        if (tls.containsKey(server.nickname)) {
            return (TLSConnection) tls.get(server.nickname);
        } else {
            // build otherwise
            Logger.logTLS(Logger.VERBOSE,"FirstNodeHandler: TLS connection to " + server.nickname);
            TLSConnection t = new TLSConnection(server, tor.privateKeyHandler);
            Logger.logTLS(Logger.VERBOSE,"FirstNodeHandler: Adding to TLS: " + server.nickname);
            tls.put(server.nickname, t);
            return t;
        }
    }

    /**
     * closes all TLS connections
     * 
     * @param force
     *            set to false, if circuits shall be terminated gracefully
     */
    void close(boolean force) {
        Iterator<String> i = tls.keySet().iterator();
        while (i.hasNext()) {
            TLSConnection t = (TLSConnection) tls.get(i.next());
            t.close(force);
        }
    }

    Circuit provideSuitableNewCircuit(TCPStreamProperties sp)
            throws IOException 
    {
      for (int retries = 0; retries < TorConfig.retriesConnect; ++retries) {
        try {
          return new Circuit(tor,this, tor.dir, sp);
        } 
        catch (InterruptedException e) { /* no, do nothing */ }
        catch(TorException e) { /* continue trying */ }
      }
      return null;
    }
    
   /**
     * used to return a number of circuits to a target. established a new circuit or uses an existing one
     *
     * @param sp gives some basic restrains
     * @param forHiddenService if set to true, use circuit that is unused and don't regard exit-policies
     * @param force_new create new circuit anyway
     */
    Circuit[] provideSuitableCircuits(TCPStreamProperties sp, boolean forHiddenService) throws 
            IOException {
        Logger.logCircuit(Logger.VERBOSE, "FirstNodeHandler.provideSuitableCircuits: called for " + sp.hostname);
        // list all suiting circuits in a vector
        int numberOfExistingCircuits = 0;
        Vector<Circuit> allCircs = new Vector<Circuit>(10,10);
        int rankingSum = 0;
        Iterator<String> it = this.tls.keySet().iterator();
        Logger.logCircuit(Logger.VERBOSE, "FirstNodeHandler.provideSuitableCircuits: " + this.tls.size() + " circuits");
        while (it.hasNext()) {
          TLSConnection tls = (TLSConnection) this.tls.get(it.next());
          Iterator<Integer> i2 = tls.circuits.keySet().iterator();
          while (i2.hasNext()) {
            try {
              Circuit circuit = tls.circuits.get(i2.next());
              ++numberOfExistingCircuits;
              if (circuit.established && (!circuit.closed) && tor.dir.isCompatible(circuit, sp, forHiddenService)){
                allCircs.add(circuit);
                rankingSum += circuit.ranking;
              }
            } catch (TorException e) { /* do nothing, just try next circuit */}
          }
        }
        // sort circuits (straight selection... O(n^2)) by
        // - wether they contained a stream to the specific address
        // - ranking (stochastically!)
        //   - implicit: wether they haven't had a stream at all
        for(int i=0;i<allCircs.size()-1;++i) {
          Circuit c1 = (Circuit)allCircs.get(i);
          int min=i;
          int min_ranking = c1.ranking;
          if (min_ranking==0) min_ranking=1;
          boolean min_points_to_addr = c1.streamHistory.contains(sp.hostname);
          for(int j=i+1;j<allCircs.size();++j) {
            Circuit thisCirc = (Circuit)allCircs.get(j);
            int this_ranking = thisCirc.ranking;
            if (this_ranking==0) this_ranking=1;
            boolean this_points_to_addr = thisCirc.streamHistory.contains(sp.hostname);
            float ranking_quota = this_ranking / min_ranking;
            if ((this_points_to_addr && !min_points_to_addr) ||
                (rnd.nextFloat() > Math.exp(-ranking_quota))) { // sort stochastically
              min = j;
              min_ranking = this_ranking;
            }
          }
          if (min>i) {
            Circuit temp = allCircs.set(i,allCircs.get(min));
            allCircs.set(min,temp);
          }
        }
        // return number of circuits suiting to number of stream-connect retries!
        int return_values = sp.connect_retries;
        if (allCircs.size()<return_values) return_values=allCircs.size();
        if ((return_values==1)&&(numberOfExistingCircuits < TorConfig.circuitsMaximumNumber)) {
          // spawn new circuit IN BACKGROUND, unless maximum number of circuits reached
          Logger.logCircuit(Logger.VERBOSE, "FirstNodeHandler.provideSuitableCircuits: spawning circuit to " + sp.hostname + " in background");
          final TCPStreamProperties spFinal = sp;
          Thread spawnInBackground = new Thread() {
            public void run() {
              try{ new Circuit(tor, tor.fnh, tor.dir, spFinal); }
              catch(Exception e){}
            }
          };
          spawnInBackground.setName("FirstNodeHandler.provideSuitableCircuits");
          spawnInBackground.start();
        } else if ((return_values==0)&&(numberOfExistingCircuits < TorConfig.circuitsMaximumNumber)) {
          // spawn new circuit, unless maximum number of circuits reached
          Logger.logCircuit(Logger.VERBOSE, "FirstNodeHandler.provideSuitableCircuits: spawning circuit to " + sp.hostname );
          Circuit single = provideSuitableNewCircuit(sp);
          if (single!=null) {
            return_values=1;
            allCircs.add(single);
          }
        }
        // copy values
        Circuit[] results = new Circuit[return_values];
        for(int i=0;i<return_values;++i) {
          results[i] = (Circuit)allCircs.get(i);
          Logger.logCircuit(Logger.VERBOSE, "FirstNodeHandler.provideSuitableCircuits: Choose Circuit ranking "+results[i].ranking+":"+results[i].print());
        }
        return results;
    }

}

// vim: et
