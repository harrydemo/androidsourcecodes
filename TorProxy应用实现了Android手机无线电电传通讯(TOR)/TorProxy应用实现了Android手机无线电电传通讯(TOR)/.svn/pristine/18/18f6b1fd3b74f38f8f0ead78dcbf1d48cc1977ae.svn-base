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

import TorJava.Common.Encryption;
import TorJava.Common.TorException;

/**
 * this cell is used to establish connection to the intro point
 * 
 * @author Andriy Panchenko
 * @version unstable
 */

class CellRelayIntroduce1 extends CellRelay {
    CellRelayIntroduce1(Circuit c, byte[] cookie, ServiceDescriptor sd,
            String rendezvousName, Node n) throws TorException {
        super(c, CELL_RELAY_INTRODUCE1);

        // plain text part
        // System.arraycopy(Common.getHash(sd.bytesKey), 0, data, 0, 20);
        System.arraycopy(Encryption.getHash(Encryption
                .getPKCS1EncodingFromRSAPublicKey(sd.publicKey)), 0, data, 0,
                20);

        // System.out.println(" bin hash -> " +
        // Common.toHexString(Common.getHash(sd.bytesKey)));
        // System.out.println( "pkcs1 hash -> " +
        // Common.toHexString(Common.getHash(Common.getPKCS1EncodingFromRSAPublicKey(sd.publicKey))));

        // compose encrypted part

        byte[] plain_data = new byte[16 + 20 + 20 + 128];
        byte[] name_bytes = rendezvousName.getBytes();

        System.arraycopy(n.symmetric_key_for_create, 0, plain_data, 0, n.symmetric_key_for_create.length);
        System.arraycopy(name_bytes, 0, plain_data, 16, name_bytes.length); // nickname
        System.arraycopy(cookie, 0, plain_data, 36, cookie.length); // 20 octets
        System.arraycopy(n.dh_x_bytes, 0, plain_data, 56, n.dh_x_bytes.length); // 128 octets

        byte[] encrypted_data = n.asym_encrypt(plain_data);

        // set encrypted part
        System.arraycopy(encrypted_data, 0, data, 20, encrypted_data.length);
        length = 20 + encrypted_data.length;

    }
}
