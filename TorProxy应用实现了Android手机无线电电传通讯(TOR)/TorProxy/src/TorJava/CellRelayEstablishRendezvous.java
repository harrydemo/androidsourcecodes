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

import TorJava.Common.TorException;

/**
 * this cell is used to establish rendezvous point
 * 
 * @author Andriy Panchenko
 * @version unstable
 */

class CellRelayEstablishRendezvous extends CellRelay {
    CellRelayEstablishRendezvous(Circuit c, byte[] cookie)
        throws TorException
    {
        super(c, CELL_RELAY_ESTABLISH_RENDEZVOUS);
        // check whether the cookie size suits into data and is at least 20 bytes
        if (cookie.length<20)
            throw new TorException("CellRelayEstablishRendezvous: rendevouz-cookie is too small");
        if (cookie.length>data.length)
            throw new TorException("CellRelayEstablishRendezvous: rendevouz-cookie is too large");
        // copy cookie
        System.arraycopy(cookie, 0, data, 0, cookie.length);
        length = cookie.length;

    }
}
