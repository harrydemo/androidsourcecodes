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

/**
 * sends a BEGIN cell, needed to establish a tcp-stream
 * 
 * @author Lexi Pimenidis
 */

class CellRelayBegin extends CellRelay {
    /**
     * constructor to build a BEGIN-CELL
     * 
     * @param s
     *            the stream that will carry the cell and the following data
     * @param sp
     *            contains the host and port to connect to
     */
    CellRelayBegin(TCPStream s, TCPStreamProperties sp) {
        // init a new Relay-cell
        super(s, RELAY_BEGIN);

        // ADDRESS | ':' | PORT | [00]
        byte[] host;
        if (sp.addr_resolved) { // insert IP-adress in dotted-quad format, if
                                // resolved
            StringBuffer sb = new StringBuffer();
            byte[] a = sp.addr.getAddress();
            for (int i = 0; i < 4; ++i) {
                if (i > 0)
                    sb.append('.');
                sb.append((int) (a[i]) & 0xff);
            }
            ;
            host = sb.toString().getBytes();
        } else {
            host = sp.hostname.getBytes(); // otherwise let exit-point resolv
                                            // name itself
        }
        ;
        System.arraycopy(host, 0, data, 0, host.length);
        // set ':'
        data[host.length] = ':';
        // set port
        byte[] port = new Integer(sp.port).toString().getBytes();
        System.arraycopy(port, 0, data, host.length + 1, port.length);
        // set length
        length = host.length + 1 + port.length + 1;
    }
}
