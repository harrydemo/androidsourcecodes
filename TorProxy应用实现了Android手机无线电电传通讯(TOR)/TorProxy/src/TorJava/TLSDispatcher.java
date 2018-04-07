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

import java.io.DataInputStream;
import java.io.IOException;

import TorJava.Common.TorException;

/**
 * reads data arriving at the TLS connection and dispatches it to the
 * appropriate circuit or stream that it belongs to.
 * 
 * @author Lexi Pimenidis
 */
class TLSDispatcher extends Thread {
    
    DataInputStream sin;

    TLSConnection tls;

    boolean stopped;

    TLSDispatcher(TLSConnection tls, DataInputStream sin) {
        this.tls = tls;
        this.sin = sin;
		this.setName("TLSDispatcher for "+tls.server.nickname);
        this.start();
    }

    public void close() {
        this.stopped = true;
        this.interrupt();
    }

    public void run() {
        boolean dispatched = false;
        while(!stopped) {
            // read next data-packet
            Cell cell = null;
            try {
                cell = new Cell(sin);
            } catch (IOException e) {
                Logger.logTLS(Logger.INFO,"TLSDispatcher.run: connection error: "+e.getMessage());
                stopped = true;
                break;
            }
            // padding cell?
            if (cell.isTypePadding()) {
                Logger.logCell(Logger.INFO, "TLSDispatcher.run: padding cell from from " + tls.server.nickname);
            } else {
                dispatched = false;
                int cell_circID = cell.circID;
                // dispatch according to circID
                if (tls.circuits.containsKey(new Integer(cell_circID))) {
                    Circuit circ = (Circuit) tls.circuits.get(new Integer(cell_circID));
                    try { // admitted: this was not the original intent for queue handlers... but maye I'll find a better solution sometimes in the future
                        if (circ.qhFC!=null) circ.qhFC.handleCell(cell);
                    }catch(TorException e) {}
                    // check for destination in circuit
                    if (cell.isTypeRelay()) {
                        CellRelay relay = null;
                        try {
                            // found a relay-cell! Try to strip off
                            // symmetric encryption and check the content
                            relay = new CellRelay(circ, cell);
                            // dispatch to stream, if a stream-ID is given
                            if (relay.isTypeBegin()) {
                            	//System.out.println("CELL BEGIN!");
                            	circ.handleBegin(relay);
                            } else if (relay.streamID != 0) {
                                if (circ.streams.containsKey(new Integer(relay.streamID))) {
                                    TCPStream stream = (TCPStream) circ.streams
                                        .get(new Integer(relay.streamID));
                                    dispatched = true;
                                    Logger.logCell( Logger.VERBOSE, "TLSDispatcher.run: data from " + tls.server.nickname + " dispatched to circuit " + circ.ID + "/stream " + stream.ID);
                                    stream.queue.add(relay);
                                }
                            } else {
                                // relay cell for stream id 0: dispatch to
                                // circuit
                                if (relay.isTypeIntroduce2()) {
                                    if (circ.myHSProperties != null) {
                                        Logger.logCell( Logger.VERBOSE, "TLSDispatcher.run: introduce2 from " + tls.server.nickname + " dispatched to circuit " + circ.ID + " (stream ID=0)");
                                        try{
                                            dispatched = circ.handleIntroduce2(relay);
                                        }
                                        catch(IOException e) {
                                            Logger.logCell( Logger.INFO, "TLSDispatcher.run: error handling intro2-cell: "+e.getMessage());
                                        }
                                    }
                                } else {
                                    dispatched = true;
                                    Logger.logCell( Logger.VERBOSE, "TLSDispatcher.run: data from " + tls.server.nickname + " dispatched to circuit " + circ.ID + " (stream ID=0)");
                                    circ.queue.add(relay);
                                }
                            }
                        } catch (TorException e) {
                            Logger.logCell(Logger.WARNING, "TLSDispatcher.run: TorException " + e.getMessage() + " during dispatching cell");
                            //e.printStackTrace();
                            /*
                             * if (relay != null)
                             * Logger.logCell(Logger.WARNING,"TLSDispatcher.run:
                             * "+relay.toString()); else
                             * Logger.logCell(Logger.WARNING,"TLSDispatcher.run:
                             * "+cell.toString());
                             */
                        }
                    } else {
                        // no relay cell: cell is there to control circuit
                        if (cell.isTypeDestroy()) {
                            dispatched = true;
                            Logger.logCell(Logger.VERBOSE, "TLSDispatcher.run: received DESTROY-cell from " + tls.server.nickname + " for circuit " + circ.ID);
                            circ.close(true);
                        } else {
                            dispatched = true; Logger.logCell(Logger.VERBOSE, "TLSDispatcher.run: data from " + tls.server.nickname + " dispatched to circuit " + circ.ID);
                            circ.queue.add(cell);
                        }
                    }
                } else {
                    Logger.logCircuit(Logger.INFO, "TLSDispatcher.run: received cell for circuit " + cell_circID + " from " + tls.server.nickname + ". But no such circuit exists.");
                }
            }
            if (!dispatched) {
                // used to be WARNING, but is given too often to be of $REAL value, like a warning should
                Logger.logCell(Logger.VERBOSE, "TLSDispatcher.run: data from " + tls.server.nickname + " could not get dispatched");
                Logger.logCell(Logger.RAW_DATA, "TLSDispatcher.run: " + cell.print());
            }
        }
    }
}
