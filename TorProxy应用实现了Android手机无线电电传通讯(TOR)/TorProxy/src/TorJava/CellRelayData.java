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
 * this cell is used to transmit data over a tcp stream
 * 
 * @author Lexi Pimenidis
 * @version unstable
 */

class CellRelayData extends CellRelay {
    /**
     * creates an empty DATA-CELL that can later be used to insert and transmit
     * data with it
     * 
     * @param s
     *            the stream where the data is send on
     */
    CellRelayData(TCPStream s) {
        super(s, CellRelay.RELAY_DATA);
    }
}
