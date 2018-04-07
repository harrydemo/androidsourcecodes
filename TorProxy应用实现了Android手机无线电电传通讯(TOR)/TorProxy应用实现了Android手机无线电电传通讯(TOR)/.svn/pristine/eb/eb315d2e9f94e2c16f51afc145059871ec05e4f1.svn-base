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
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * this class contains a backgroundthread that waits for incoming cells in a
 * TCPStream and makes them available to the Java-Application.
 * 
 * @author Lexi Pimenidis
 * @author Andriy Panchenko
 * @version unstable
 * @see TCPStreamThreadJava2Tor
 */

class TCPStreamThreadTor2Java extends Thread {
    TCPStream stream;

    PipedInputStream sin; // read from tor and output to this stream

    PipedOutputStream fromtor; // private end of this pipe

    boolean stopped; // as stop() is depreacated we use this toggle variable
    
    TCPStreamThreadTor2Java(TCPStream stream) {
        this.stream = stream;
        try {
            sin = (PipedInputStream) new SafePipedInputStream();
            fromtor = new PipedOutputStream(sin);
        } catch (IOException e) {
            Logger.logStream(Logger.ERROR,
                    "TCPStreamThreadTor2Java: caught IOException "
                            + e.getMessage());
        }
        this.start();
    }

    public void close() {
        this.stopped = true;
        this.interrupt();
    }
    
    public void run() {
        while (!stream.closed && !this.stopped) {
            Cell cell = (Cell) stream.queue.get();
            if (cell != null) {
                if (!cell.isTypeRelay()) {
                    Logger.logStream(Logger.ERROR,
                            "TCPStreamThreadTor2Java.run(): stream "
                                    + stream.ID + " received NON-RELAY cell:\n"
                                    + cell.print());
                } else {
                    CellRelay relay = (CellRelay) cell;
                    if (relay.isTypeData()) {
                        Logger.logStream(Logger.RAW_DATA,
                                "TCPStreamThreadTor2Java.run(): stream "
                                        + stream.ID + " received data");
                        try {
                            fromtor.write(relay.data, 0, relay.length);
                        } catch (IOException e) {
                            Logger.logStream(Logger.ERROR,
                                    "TCPStreamThreadTor2Java.run(): caught IOException "
                                            + e.getMessage());
                        }
                    } else if (relay.isTypeEnd()) {
                        Logger.logStream(Logger.RAW_DATA,
                                "TCPStreamThreadTor2Java.run(): stream "
                                        + stream.ID + " is closed: "
                                        + relay.reasonForClosing());
                        stream.closed_for_reason = (int) (relay.payload[0]) & 0xff;
                        stream.closed = true;
                        stream.close(true);
                    } else {
                        Logger.logStream(Logger.ERROR,
                                "TCPStreamThreadTor2Java.run(): stream "
                                        + stream.ID
                                        + " received strange cell:\n"
                                        + relay.print());
                    }
                }
            }
        }
    }
}

/**
 * this class is meant to simulate the behaviour of a standard stream. It's
 * necessary because at some point the PipedInputStream returns a IOException,
 * if the connection has been closed by the remote side, where a InputStream
 * would only return a 'null'.
 * 
 * @author Lexi Pimenidis
 * @see PipedInputStream
 * @see InputStream
 */
class SafePipedInputStream extends PipedInputStream {

    public int read() throws IOException {
        try {
            return super.read();
        } catch (IOException e) {
            // catch only if the connected PipeOutputStream is closed. otherwise
            // rethrow the error
						String msg = e.getMessage();
            if (msg!=null && msg.equals("Write end dead"))
                return -1;
            else
                throw e;
        }
    }

    public int read(byte[] b, int off, int len) throws IOException {
        try {
            return super.read(b, off, len);
        } catch (IOException e) {
            // catch only if the connected PipeOutputStream is closed. otherwise
            // rethrow the error
						String msg = e.getMessage();
            if (msg!=null && (msg.equals("Write end dead") || msg.equals("Pipe closed"))) {
                b = null;
                return 0;
            } else
                throw e;
        }
    }
}
