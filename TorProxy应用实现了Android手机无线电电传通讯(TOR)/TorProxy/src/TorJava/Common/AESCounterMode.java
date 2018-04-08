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
package TorJava.Common;

import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.params.KeyParameter;

/**
 * implements AES in Countermode. This special mode turn the block cipher into a
 * stream cipher. we thus have to create a key stream and take care that no byte
 * of it gets lost.
 * 
 * @author Lexi Pimenidis
 */

public class AESCounterMode {
    int block_size;

    byte[] counter;

    AESEngine aes;

    byte[] stream_buffer;

    int stream_next;

    /**
     * init the AES-Engine
     * 
     * @param encrypt
     *            is the key-stream created with encryption or decryption? In
     *            case of doubt: set to TRUE
     * @param key
     *            the symmetric key for the algorithm
     */
    public AESCounterMode(boolean encrypt, byte[] key) {
				if (!encrypt)
					System.err.println("AESCounterMode.<iinit>: WARNING! neve use Counter-mode in TOR with 'decryption'");
			
        // init cipher
        aes = new AESEngine();
        aes.init(encrypt, new KeyParameter(key));
        block_size = aes.getBlockSize();
        // init counter
        counter = new byte[block_size];
        // JAVA already sets the array to all zeroes
        // for(int i=0;i<block_size;++i)
        // counter[i]=0;
        stream_buffer = new byte[block_size];
        stream_next = block_size;
    }

    /**
     * reads the next key of the key stream from the buffer. if the buffer is
     * not filled, generates the next few bytes in the buffer.
     * 
     * @return the next byte of the key stream
     */
    private byte nextStreamByte() {
        ++stream_next;
        // are there still unused bytes in the buffer?
        if (stream_next >= block_size) {
            // fill stream-buffer
            aes.processBlock(counter, 0, stream_buffer, 0);
            stream_next = 0;
            // increase counter
            int j = block_size - 1;
            do {
                ++counter[j];
                --j;
            } while ((counter[j + 1] == 0) && (j >= 0));
        }
        ;
        return stream_buffer[stream_next];
    }

    /**
     * encrypts or decrypts an array of arbitrary length. since counter mode is
     * used as a stream cipher, the cipher is symmetric, i.e. encryption and
     * decryption is the same.
     * 
     * @param in
     *            input the plain text, or the cipher text
     * @return receive the result
     */
    public byte[] processStream(byte[] in) {
        byte[] out = new byte[in.length];
        for (int i = 0; i < in.length; ++i) {
            byte cipher = nextStreamByte();
            out[i] = (byte) (((int) in[i] + 256) ^ ((int) cipher + 256));
        }
        ;
        return out;
    }

}
