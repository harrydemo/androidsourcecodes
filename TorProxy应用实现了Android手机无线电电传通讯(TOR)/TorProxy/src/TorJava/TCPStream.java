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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.Date;

import TorJava.Common.Queue;
import TorJava.Common.TorException;
import TorJava.Common.TorNoAnswerException;

/**
 * handles the features of single TCP streams on top of circuits through the tor
 * network. provides functionality to send and receive data by this streams and
 * is publicly visible.
 * 
 * @author Lexi Pimenidis
 * @author Tobias Koelsch
 * @author Michael Koellejan
 * @version unstable
 */

public class TCPStream {

    int queue_timeout = TorConfig.queueTimeoutStreamBuildup; // wait x seconds for answer

    Circuit circ;

    int ID;

    Queue queue; // receives incoming data

    public InetAddress resolvedAddress;

    public TCPStreamProperties sp;

    public boolean established;

    public boolean closed;

    int closed_for_reason; // set by CellRelay. descriptive Strings are in
                            // CellRelay.reason_to_string

    //TCPStreamThreadTor2Java tor2java;
    //TCPStreamThreadJava2Tor java2tor;
    QueueTor2JavaHandler qhT2J;
    QueueFlowControlHandler qhFC;
    TCPStreamOutputStream outputStream;

    Date created;

    Date last_action; // last time, a cell was send that was not a padding
                        // cell

    Date last_cell; // last time, a cell was send

    int streamLevelFlowControl = 500;
    final int streamLevelFlowControlIncrement = 50;

    /**
     * creates a stream on top of a existing circuit. users and programmers
     * should never call this function, but Tor.connect() instead.
     * 
     * @param c
     *            the circuit to build the stream through
     * @param sp
     *            the host etc. to connect to
     * @see Tor
     * @see Circuit
     * @see TCPStreamProperties
     */
    TCPStream(Circuit c, TCPStreamProperties sp) throws IOException,
            TorException, TorNoAnswerException {
        this.sp = sp;
        established = false;
        created = new Date();
        last_action = created;
        last_cell = created;
        int setupDuration; // stream establishment duration
        long startSetupTime; 

        // attach stream to circuit
        circ = c;
        ID = circ.assign_streamID(this);
        queue = new Queue(queue_timeout);
        closed = false;
        closed_for_reason = 0;
        Logger.logStream(Logger.VERBOSE, "TCPStream: building new stream " + print());

        startSetupTime = System.currentTimeMillis();
        // send RELAY-BEGIN
        send_cell(new CellRelayBegin(this, sp));

        // wait for RELAY_CONNECTED
        CellRelay relay = null;
        try {
            Logger.logStream(Logger.VERBOSE, "TCPStream: Waiting for Relay-Connected Cell...");
            relay = queue.receiveRelayCell(CellRelay.RELAY_CONNECTED);
            Logger.logStream(Logger.VERBOSE, "TCPStream: Got Relay-Connected Cell");
        } catch (TorException e) {
            if (!closed)  // only msg, if closing was unintentionally
              Logger.logStream(Logger.WARNING, "TCPStream: Closed:" + print() + " due to TorException:" + e.getMessage());
            closed = true;
            // MRK: when the circuit does not work at this point: close it
            // Lexi: please do it soft! there might be other streams
            //       working on this circuit...
            //c.close(false);
            // Lexi: even better: increase only a counter for this circuit
            //       otherwise circuits will close on an average after 3 or 4 
            //       streams. this is nothing we'd like to happen
            c.reportStreamFailure(this);
            throw e;
        } catch (IOException e) {
            closed = true;
            Logger.logStream(Logger.WARNING, "TCPStream: Closed:" + print() + " due to IOException:" + e.getMessage());
            throw e;
        }

        setupDuration = (int) (System.currentTimeMillis() - startSetupTime);

        // store resolved IP in TCPStreamProperties
        byte[] ip = new byte[relay.length];
        System.arraycopy(relay.data, 0, ip, 0, ip.length);
        try {
            sp.addr = InetAddress.getByAddress(ip);
            sp.addr_resolved = true;
            resolvedAddress = sp.addr;
            Logger.logStream(Logger.RAW_DATA, "TCPStream: storing resolved IP " + sp.addr.toString());
        } catch (IOException e) {
        }
        // create reading threads to relay between user-side and tor-side
        //tor2java = new TCPStreamThreadTor2Java(this);
        //java2tor = new TCPStreamThreadJava2Tor(this);
        qhFC = new QueueFlowControlHandler(this,streamLevelFlowControl,streamLevelFlowControlIncrement);
        this.queue.addHandler(qhFC);
        qhT2J = new QueueTor2JavaHandler(this);
        this.queue.addHandler(qhT2J);
        outputStream = new TCPStreamOutputStream(this);

        Logger.logStream(Logger.INFO, "TCPStream: build stream "
                + print() + " within " + setupDuration + " ms");
        // attach stream to history
        circ.registerStream(sp, setupDuration);
        established = true;
        // Tor.lastSuccessfulConnection = new Date(System.currentTimeMillis());
        circ.tor.fireEvent(new TorEvent(TorEvent.STREAM_BUILD,this,"Stream build: "+print()));
    }

    /** called from derived ResolveStream */
    TCPStream(Circuit c) {
        circ = c;
    }

    void send_cell(Cell c) throws IOException {
        // update 'action'-timestamp, if not padding cell
        last_cell = new Date();
        if (!c.isTypePadding())
            last_action = last_cell;
        // send cell
        try{
          circ.send_cell(c);
        }
        catch(IOException e) {
          // if there's an error in sending a cell, close this stream
          this.circ.reportStreamFailure(this);
          close(false);
          throw e;
        }
    }

    /** send a stream-layer dummy */
    void sendKeepAlive() {
        try {
            send_cell(new CellRelayDrop(this));
        } catch (IOException e) {
        }
    }

    /** for application interaction */
    public void close() {
        // gracefully close stream
        close(false);
        // remove from circuit
        Logger.logStream(Logger.RAW_DATA,
                "TCPStream.close(): removing stream " + print());
        circ.streams.remove(new Integer(ID));
    }

    /**
     * for internal usage
     * 
     * @param force
     *            if set to true, just destroy the object, without sending
     *            END-CELLs and stuff
     */
    void close(boolean force) {
        Logger.logStream(Logger.VERBOSE, "TCPStream.close(): closing stream " + print());
        circ.tor.fireEvent(new TorEvent(TorEvent.STREAM_CLOSED,this,"Stream closed: "+print()));

        // if stream is not closed, send a RELAY-END-CELL
        if (!(closed || force)) {
            try {
                send_cell(new CellRelayEnd(this, (byte) 6)); // send cell with 'DONE'
            } catch (IOException e) {
            }
        }
        // terminate threads gracefully
        closed = true;
        /*if (!force) {
            try {
                this.wait(3);
            } catch (Exception e) {
            }
        }*/
        // terminate threads if they are still alive
        if (outputStream != null) {
          try { outputStream.close(); 
          }catch(Exception e){}
        };
        // close queue (also removes handlers)
        queue.close();
        // remove from circuit
        circ.streams.remove(new Integer(ID));
    }

    /**
     * use this to receive data by the anonymous data stream
     * 
     * @return a standard Java-Inputstream
     */
    public InputStream getInputStream() {
        return qhT2J.sin;
    }

    /**
     * use this to transmit data through the Tor-network
     * 
     * @return a standard Java-Outputstream
     */
    public OutputStream getOutputStream() {
        return outputStream;
    }

    /** used for proxy and UI */
    public String getRoute() {
      StringBuffer sb = new StringBuffer();
      for(int i=0; i< circ.route_established ; ++i) {
        Server s = circ.route[i].server;
        sb.append(", ");
        sb.append(s.nickname+" ("+s.countryCode+")");
      }
      return sb.toString();
    }

    /** for debugging */
    String print() {
        if (sp == null)
            return ID + " on circuit " + circ.print() + " to nowhere";
        else {
          if (closed) {
            return ID + " on circuit " + circ.print() + " to " + sp.hostname + ":" + sp.port + " (closed)";
          } else {
            return ID + " on circuit " + circ.print() + " to " + sp.hostname + ":" + sp.port;
          }
        }
    }
}

// vim: et
