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
 * sends an END cell, needed to close a tcp-stream
 * 
 * @author Lexi Pimenidis
 */

class CellRelayEnd extends CellRelay {
    /**
     * constructor to build a ENDCELL
     * 
     * @param s
     *            the stream that shall be closed
     * @param reason
     *            a reason
     */
    CellRelayEnd(TCPStream s, byte reason) {
        // init a new Relay-cell
        super(s, CellRelay.RELAY_END);

        // set length
        length = 1;
        data[0] = reason;
    }
}
