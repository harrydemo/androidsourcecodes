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
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import TorJava.Common.Encoding;
import TorJava.Common.Encryption;
import TorJava.Common.Queue;
import TorJava.Common.TorException;

/**
 * handles the functionality of creating circuits, given a certain route and
 * buidling tcp-streams on top of them.
 * 
 * @author Lexi Pimenidis
 * @author Tobias Koelsch
 * @author Andriy Panchenko
 * @author Michael Koellejan
 * @version unstable
 */

public class Circuit {
    TLSConnection tls; // a pointer to the TLS-layer

    Node[] route; // stores the route

    int route_established; // nodes in the route, where the keys have been
                            // established

    Queue queue; // used to receive incoming data

    public HashMap<Integer, TCPStream> streams; // a list of all tcp-streams relayed through this
                        // circuit

    HashSet<Object> streamHistory; // contains URLs, InetAddresse or z-part of HS URL
                            // of hosts used to make contact to (or for DNS query) with this Circuit
                            
    int established_streams = 0; // counts the number of established streams

    ServiceDescriptor sd; // service descriptor in case if used for rendezvous
                            // point

    HiddenServiceProperties myHSProperties; // pointer to hidden service
                                            // properties, if circuit used for
                                            // communication with introduction
                                            // point

    public int ID;

    public boolean established; // set to true, if route is established

    public boolean closed; // set to true, if no new streams are allowed

    boolean destruct; // set to true, if circuit is closed and inactive and
                        // may be removed from all sets

    Date created;

    Date last_action; // last time, a cell was send that was not a padding
                        // cell

    Date last_cell; // last time, a cell was send

    int setupDuration; // time in ms it took to establish the circuit

    int ranking;  // ranking index of the circuit

    int sum_streams_setup_delays; // duration of all streams' setup times 

    int stream_counter;  // overall number of streams relayed thrue the circ

    int stream_fails;    // overall counter of failures in streams in this circuit

    final int circuitLevelFlowControl = 1000;
    final int circuitLevelFlowControlIncrement = 100;
    QueueFlowControlHandler qhFC;

    Directory dir;
    FirstNodeHandler fnh;
    Tor tor;

    /**
     * initiates a circuit. tries to rebuild the circuit for a limited number of
     * times, if first attempt fails.
     * 
     * @param fnh
     *            a pointer to the TLS-Connection to the first node
     * @param dir
     *            a pointer to the directory, in case an alternative route is
     *            necessary
     * @param sp
     *            some properties for the stream that is the reason for building
     *            the circuit (needed if the circuit is needed to ask the
     *            directory for a new route)
     * 
     * @exception TorException
     * @exception IOException
     */
    Circuit(Tor tor,FirstNodeHandler fnh, Directory dir, TCPStreamProperties sp)
            throws IOException, TorException, InterruptedException 
    {
        // init variables
        this.dir = dir;
        this.fnh = fnh;
        this.tor = tor;
        closed = false;
        established = false;
        destruct = false;
        sum_streams_setup_delays = 0;
        stream_counter = 0;
        stream_fails = 0;
        ranking = -1; // unused circs have highest priority for selection
        streams = new HashMap<Integer,TCPStream>();
        streamHistory = new HashSet<Object>();
        created = new Date();
        last_action = created;
        last_cell = created;
        // get a new route
        Server[] init = dir.createNewRoute(sp);
        if (init==null) throw new TorException("Circuit: could not build route");
        // try to build a circuit
        long startSetupTime = System.currentTimeMillis();
        for (int misses = 1;; ++misses) {
            if (Thread.interrupted())
                throw new InterruptedException();
            try {
                // attach circuit to TLS
                Logger.logCircuit(Logger.VERBOSE, "Circuit: connecting to " + init[0].nickname + " (" + init[0].countryCode +  ") over tls");
                tls = fnh.get_connection(init[0]);
                Logger.logCircuit(Logger.VERBOSE,
                        "Circuit: creating queue");
                queue = new Queue(TorConfig.queueTimeoutCircuit);
                // FIXME: Addition to circuits-list is quite hidden here.
                Logger.logCircuit(Logger.VERBOSE,
                		"Circuit: assigning circId");
                ID = tls.assign_circID(this);
                route_established = 0;
                // connect to entry point = init[0]
                Logger.logCircuit(Logger.VERBOSE,
                        "Circuit: sending create cell to " + init[0].nickname);
                route = new Node[init.length];
                create(init[0]);
                route_established = 1;
                // extend route
                for (int i = 1; i < init.length; ++i) {
                    Logger.logCircuit(Logger.VERBOSE, "Circuit: " + print() + " extending to " + init[i].nickname + " (" + init[i].countryCode + ")");
                    extend(i, init[i]);
                    route_established += 1;
                }
                // finished - success
                break;
            } catch (IOException e) { // some error occured during the
                                        // creating of the circuit
                if (closed)
                    throw new IOException("Circuit: " + print()
                            + " closing during buildup");
                Logger.logCircuit(Logger.INFO, "Circuit: " + print() + " IOException " + misses + " :" + e.getMessage());
                e.printStackTrace();
                if (misses >= TorConfig.reconnectCircuit)
                    throw e; // enough retries, exit
                // build a new route over the hosts that are known to be
                // working, punish failing host
                init = dir.restoreCircuit(sp, init, route_established);
            } catch (TorException e) { // some error occured during the
                                        // creating of the circuit
                if (closed)
                    throw new IOException("Circuit: " + print()
                            + " closing during buildup");
                Logger.logCircuit(Logger.INFO, "Circuit: " + print() + " TorException " + misses + " :" + e.getMessage());
                e.printStackTrace();
                if (misses >= TorConfig.reconnectCircuit)
                    throw e; // enough retries, exit
                // build a new route over the hosts that are known to be
                // working, punish failing host
                init = dir.restoreCircuit(sp, init, route_established);
            }
        };
        setupDuration = (int) (System.currentTimeMillis() - startSetupTime);
        established = true;
        Logger.logCircuit(Logger.INFO, "Circuit: " + print() + " established within " + setupDuration + " ms");
        qhFC = new QueueFlowControlHandler(this,circuitLevelFlowControl,circuitLevelFlowControlIncrement);
        queue.addHandler(qhFC);
        // fire event
        tor.fireEvent(new TorEvent(TorEvent.CIRCUIT_BUILD,this,"Circuit build " + print()));
    }

    /**
     * does exactly that: - check introduce2 for validity and connect to
     * rendevous-point
     */
    boolean handleIntroduce2(CellRelay cell) throws TorException, IOException {
        // parse intro-cell
        if (cell.length<20) 
          throw new TorException("Circuit.handleIntroduce2: cannot parse content, cell is too short");
        byte[] identifier = new byte[20];
        System.arraycopy(cell.data,0,identifier,0,20);
        if (!Encoding.arraysEqual(identifier,myHSProperties.getKeys().pubKeyHash))
          throw new TorException("Circuit.handleIntroduce2: onion is for unknown key-pair");
        byte[] onionData = new byte[cell.length-20];
        System.arraycopy(cell.data,20,onionData,0,cell.length-20);
        
        byte[] plain_intro2 = Encryption.asym_decrypt(myHSProperties.getKeys().priv, onionData);

        // TODO: deal with introduce2 version 1 - 3
        if (plain_intro2.length != 168) {
            Logger.logCell( Logger.ERROR, "Circuit.handleIntroduce2: cannot parse content, possibly version is not yet supported");
            return false;
        }
        Logger.logHiddenService(Logger.INFO,"Circuit.handleIntroduce2: received Intro2-Cell");

        // extract content from decoded Intro2
        byte[] rendezvous_bytes = new byte[20];
        byte[] cookie = new byte[20];
        byte[] dh_x = new byte[128];

        System.arraycopy(plain_intro2, 0, rendezvous_bytes, 0, 20);
        System.arraycopy(plain_intro2, 20, cookie, 0, 20);
        System.arraycopy(plain_intro2, 40, dh_x, 0, 128);

        // cut the padding NULs
        int rendezvousLen = 0;
        do{
          if (rendezvous_bytes[rendezvousLen]==0) break;
          ++rendezvousLen;
        } while(rendezvousLen<=20);
        byte[] rendezvous_byte_cropped = new byte[rendezvousLen];
        System.arraycopy(rendezvous_bytes,0,rendezvous_byte_cropped,0,rendezvousLen);
        String rendezvous_str = new String(rendezvous_byte_cropped);
        Server rendezvousServer = dir.getByName(rendezvous_str);
        if (rendezvousServer == null) 
          throw new TorException("Circuit.handleIntroduce2: unknown rendezvous-point '"+rendezvous_str+"'");

        // build circuit to rendezvous
        TCPStreamProperties sp = new TCPStreamProperties();
        sp.exitPolicyRequired = false;
        sp.setCustomExitpoint(rendezvous_str);

        // make new circ where the last node is intro point
        for(int i=0;i<sp.connect_retries;++i) {
          Circuit c2rendezvous = fnh.provideSuitableNewCircuit(sp);
          // send dh_y
          Node virtualNode = new Node(rendezvousServer,dh_x);
          c2rendezvous.send_cell(new CellRelayRendevous1(c2rendezvous,cookie,virtualNode.dh_y_bytes,virtualNode.kh));
          Logger.logHiddenService(Logger.INFO,"Circuit.handleIntroduce2: connected to rendezvous '"+rendezvous_str+"' over "+c2rendezvous.print());
          // extend circuit to 'virtual' next point AFTER doing the rendezvous
          c2rendezvous.myHSProperties = myHSProperties;
          c2rendezvous.addNode(virtualNode);
          return true;
        }
        return false;
    }

    /**
     * sends a cell on this circuit. Incoming data is received by the class
     * TLSDispatcher and then put in the queue.
     * 
     * @param c
     *            the cell
     * @exception IOException
     * @see TLSDispatcher
     */
    void send_cell(Cell c) throws IOException {
        // update 'action'-timestamp, if not padding cell
        last_cell = new Date();
        if (!c.isTypePadding())
            last_action = last_cell;
        // send cell
        try{
          tls.send_cell(c);
        }
        catch(IOException e) {
          // if there's an error in sending it can only mean that the
          // circuit or the TLS-connection has severe problems. better close it
          if (!closed) close(false);
          throw e;
        }
    }

    /** creates and send a padding-cell down the circuit */
    void sendKeepAlive() {
        try {
            send_cell(new CellPadding(this));
        } catch (IOException e) {
        	Logger.logCircuit(Logger.INFO, "Send KeepAlive IO error");
        }
    }

    /**
     * initiates circuit, sends CREATE-cell. throws an error, if something went
     * wrong
     */
    private void create(Server init) throws IOException, TorException {
        // save starting point
        route[0] = new Node(init);
        // send create cell, set circID
        send_cell(new CellCreate(this));
        // wait for answer
        Cell created = queue.receiveCell(Cell.CELL_CREATED);
        // finish DH-exchange
        route[0].finish_dh(created.payload);
    }

    /**
     * Extends the existing circuit one more hop. sends an EXTEND-cell.
     */
    private void extend(int i, Server next) throws IOException, TorException {
        // save next node
        route[i] = new Node(next);
        // send extend cell
        send_cell(new CellRelayExtend(this, route[i]));
        // wait for extended-cell
        CellRelay relay = queue.receiveRelayCell(CellRelay.RELAY_EXTENDED);
        // finish DH-exchange
        route[i].finish_dh(relay.data);
    }

    /**
     * adds node as the last one in the route
     * 
     * @param n
     *            new node that is appended to the existing route
     */
    void addNode(Node n) {
        // create a new array for route that is one entry larger
        Node[] newRoute = new Node[route_established + 1];
        System.arraycopy(route, 0, newRoute, 0, route_established);
        // add new node
        newRoute[route_established] = n;
        ++route_established;
        // route to set new array
        route = newRoute;
    }

    /** used to report that this stream cause some trouble (either by itself,
     *  or the remote server, or what ever)
     */
    void reportStreamFailure(TCPStream stream) {
      ++stream_fails;
      // if it's just too much, 'soft'-close this circuit
      if ((stream_fails>TorConfig.circuitClosesOnFailures)&&(stream_fails > stream_counter*3/2)) {
        if (!closed)
          Logger.logCircuit(Logger.INFO,"Circuit.reportStreamFailure: closing due to failures "+print());
        close(false);
      }
      // include in ranking
      updateRanking();
    }

    /**
     * find a free stream ID, other than zero
     */
    private synchronized int getFreeStreamID() throws TorException {
        for (int nr=1; nr<0x10000 ; ++nr) {
          int id = (nr+stream_counter) & 0xffff;
          if (id!=0) 
            if (!streams.containsKey(new Integer(id)))
                return id;
        }
        throw new TorException("Circuit.getFreeStreamID: " + print()
                  + " has no free stream-IDs");
    }

    /**
     * find a free stream-id.
     */
    int assign_streamID(TCPStream s) throws TorException {
        if (closed)
            throw new TorException("Circuit.assign_streamID: " + print()
                    + "is closed");
        // assign stream ID and memorize stream
        s.ID = getFreeStreamID();
        streams.put(new Integer(s.ID), s);
        return s.ID;
    }

    /**
     * registers a stream in the history to allow bundeling streams to the same
     * connection in one circuit
     */
    void registerStream(TCPStreamProperties sp) throws TorException {
        ++established_streams;
        if (sp.addr != null)
            streamHistory.add(sp.addr);
        if (sp.hostname != null)
            streamHistory.add(sp.hostname);
    }

    /**
     * registers a stream in the history to allow bundeling streams to the same
     * connection in one circuit, wrapped for setting stream creation time
     */
    void registerStream(TCPStreamProperties sp, long streamSetupDuration) throws TorException {

        sum_streams_setup_delays += streamSetupDuration;
        stream_counter++;  
        updateRanking();
        registerStream(sp);
    }


    /**
     * updates the ranking of the circuit. takes into account: setup time of circuit and
     * streams. but also number of stream-failures on this circuit;
     *
     */
    private void updateRanking(){
        // do a weighted average of all setups. weighten the setup-time of the circuit more
        // then those of the single streams. thus streams will be rather unimportant at the
        // beginning, but play a more important role afterwards.
        ranking = (TorConfig.CIRCUIT_ESTABLISHMENT_TIME_IMPACT * setupDuration + sum_streams_setup_delays)/
                        (stream_counter + TorConfig.CIRCUIT_ESTABLISHMENT_TIME_IMPACT);
        // take into account number of stream-failures on this circuit
        // DEPRECATED: just scale this up linearly
        //         ranking *= 1 + stream_fails;
        // NEW: be cruel! there should be something severe for 3 or 4 errors!
        ranking *= Math.exp(stream_fails);
    }


    /**
     * closes the circuit. either soft (remaining connections are kept, no new
     * one allowed) or hard (everything is closed immediatly, e.g. if a destroy
     * cell is received)
     */
    boolean close(boolean force) {
        if (!closed){
          Logger.logCircuit(Logger.INFO, "Circuit.close(): closing " + print());
//          int numberOfNodeOccurances;
           // remove servers from list of currently used nodes
          for(int i=0;i<route_established;++i){
            //numberOfNodeOccurances = ((Integer) FirstNodeHandler.currentlyUsedNodes.get(route[i].server.nickname)).intValue();
            //FirstNodeHandler.currentlyUsedNodes.put(route[i].server.nickname, Math.max(0, --numberOfNodeOccurances));
          }
        }
        tor.fireEvent(new TorEvent(TorEvent.CIRCUIT_CLOSED,this,"Circuit: closing "+print()));

        // mark circuit closed. do nothing more, is soft close and streams are
        // left
        closed = true;
        established = false;
        // close all streams, removed closed streams
        Iterator<Integer> si = streams.keySet().iterator();
        while (si.hasNext()) {
          try{
            Object nick = si.next();
            TCPStream stream = (TCPStream) streams.get(nick);
            // check if stream is still allive
            if (!stream.closed) {
                if (force)
                    stream.close(force);
                else {
                    // check if we can time-out the stream?
                    if (System.currentTimeMillis() - stream.last_cell.getTime() > 10 * TorConfig.queueTimeoutStreamBuildup * 1000) {
                        // ok, fsck it!
                        Logger.logCircuit(Logger.INFO, "Circuit.close(): forcing timeout on stream");
                        stream.close(true);
                    } else {
                        // no way...
                        Logger.logCircuit(Logger.VERBOSE, "Circuit.close(): can't close due to " + stream.print());
                    }
                }
            }
            if (stream.closed) si.remove();
          }
          catch(Exception e) {}
        }
        // 
        if ((!force) && (!streams.isEmpty()))
            return false;
        // gracefully kill circuit with DESTROY-cell or so
        if (!force) {
            if (route_established > 0) {
                // send a destroy-cell to the first hop in the circuit only
                Logger.logCircuit(Logger.VERBOSE, "Circuit.close(): destroying " + print());
                route_established = 1;
                try {
                    send_cell(new CellDestroy(this));
                } catch (IOException e) {
                }
            }
            ;
        }
        ;
        // close circuit (also removes handlers)
        queue.close();
        // tls.circuits.remove(new Integer(ID));
        destruct = true;
        // closed
        return true;
    }

    /** returns the route of the circuit. used to display route on a map or the like */
    public Server[] getRoute() {
      Server[] s = new Server[route_established];
      for(int i=0;i<route_established;++i)
        s[i] = route[i].server;
      return s;
    }

    /** used for description */
    public String print() {
        if (tls != null && tls.server != null) {
            StringBuffer sb = new StringBuffer(ID + " [" + tls.server.nickname + " (" + tls.server.countryCode + ")");
            for (int i = 1; i < route_established; ++i)
                sb.append(" " + route[i].server.nickname + " (" + route[i].server.countryCode + ")");
            sb.append("]");
            if (closed) { 
              sb.append(" (closed)");
            } else {
              if (!established) sb.append(" (establishing)");
            }
            return sb.toString();
        } else
            return "<empty>";
    }
    
    void registerEstablishedStream(int id, TCPStream s) {
    	streams.put(id, s);
    }

    
    public void handleBegin(CellRelay relay) throws TorException {
    	if (myHSProperties == null) throw new TorException("Begin received on non-service circuit");
		try {
			TCPStreamExit exit = new TCPStreamExit(this, relay);
			this.myHSProperties.handler.accept(exit);
		} catch (IOException e) {
			throw new TorException(e.getMessage());
		}
    }
}

// vim: et
