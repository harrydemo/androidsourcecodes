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
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * 
 * @author Lexi Pimenidis
 * @author Andriy Panchenko
 * @version unstable
 */

class TCPStreamThreadJava2Tor extends Thread {
    TCPStream stream;

    PipedOutputStream sout; // read from this, and forward to tor

    PipedInputStream fromjava; // private end of this pipe

    boolean stopped; // as stop() is depreacated we use this toggle variable
    
    TCPStreamThreadJava2Tor(TCPStream stream) {
        this.stream = stream;
        try {
            sout = new PipedOutputStream();
            fromjava = new PipedInputStream(sout);
        } catch (IOException e) {
            Logger.logStream(Logger.ERROR,
                    "TCPStreamThreadJava2Tor: caught IOException "
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
            try {
                int read_bytes = fromjava.available();
                while (read_bytes > 0 && !this.stopped) {
                    Logger.logStream(Logger.RAW_DATA,
                            "TCPStreamThreadJava2Tor.run(): read " + read_bytes
                                    + " bytes from application");
                    CellRelayData cell = new CellRelayData(stream);
                    cell.length = read_bytes;
                    if (cell.length > cell.data.length)
                        cell.length = cell.data.length;
                    int b0 = fromjava.read(cell.data, 0, cell.length);
                    read_bytes -= b0;
                    if (b0 < cell.length) {
                        Logger.logStream(Logger.WARNING,
                                "TCPStreamThreadJava2Tor.run(): read " + b0
                                        + " bytes where " + cell.length
                                        + " were advetised");
                        cell.length = b0;
                    }
                    ;
                    if (cell.length > 0)
                        stream.send_cell(cell);
                }
                ;
                Thread.yield();
            } catch (IOException e) {
                Logger.logStream(Logger.WARNING,
                        "TCPStreamThreadJava2Tor.run(): caught IOException "
                                + e.getMessage());
            }
        }
    }
}
