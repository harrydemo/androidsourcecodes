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

import TorJava.Common.Encoding;
import TorJava.Common.TorException;

/**
 * this cell helps extending existing circuits
 * 
 * @author Lexi Pimenidis
 * @version unstable
 */

class CellRelayExtend extends CellRelay {
    /**
     * build an EXTEND-cell<br>
     * <ul>
     * <li>address (4 bytes)
     * <li>port (2 bytes)
     * <li>onion skin (186 bytes)
     * <li>hash (20 bytes)
     * </ul>
     * 
     * @param c
     *            the circuit that needs to be extended
     * @param next
     *            the node to which the circuit shall be extended
     * @throws IOException
     */
    CellRelayExtend(Circuit c, Node next) throws IOException, TorException {
        // init a new RELAY-cell
        super(c, CellRelay.RELAY_EXTEND);

        // Address [4 bytes] next.server.addr
        byte[] address = next.server.address.getAddress();
        // Port [2 bytes] next.server.port
        byte[] or_port = Encoding.intToNByteArray(next.server.orPort, 2);
        // Onion skin [186 bytes]
        byte[] onion_raw = new byte[144];
        System.arraycopy(next.symmetric_key_for_create, 0, onion_raw, 0, 16);
        System.arraycopy(next.dh_x_bytes, 0, onion_raw, 16, 128);
        byte[] onion_skin = next.asym_encrypt(onion_raw);
        // Public key hash [20 bytes]
        // (SHA1-hash of the PKCS#1 ASN1-encoding of the next OR's signing key)
        byte[] key_hash = next.server.fingerprint;

        // save everything in payload
        length = 4 + 2 + 186 + 20;
        System.arraycopy(address, 0, data, 0, 4);
        System.arraycopy(or_port, 0, data, 4, 2);
        System.arraycopy(onion_skin, 0, data, 6, 186);
        System.arraycopy(key_hash, 0, data, 192, 20);
    }
}
