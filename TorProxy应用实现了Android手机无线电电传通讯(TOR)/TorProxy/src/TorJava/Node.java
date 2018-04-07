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

import java.math.BigInteger;
import java.util.Random;

import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.encodings.OAEPEncoding;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.RSAKeyParameters;

import TorJava.Common.AESCounterMode;
import TorJava.Common.Encoding;
import TorJava.Common.TorException;

/**
 * represents a server as part of a specific circuit. Stores the additional data
 * and contains all of the complete crypto-routines.
 * 
 * @author Lexi Pimenidis
 * @author Tobias Koelsch
 */
class Node {
    Server server;

    byte[] symmetric_key_for_create; // used to encrypt a part of the
                                        // diffie-hellman key-exchange

    BigInteger dh_private; // data for the diffie-hellman key-exchange

    BigInteger dh_x;

    byte[] dh_x_bytes;

    byte[] dh_y_bytes;

    byte[] kh; // the derived key data

    byte[] forward_digest; // digest for all data send to this node

    byte[] backward_digest; // digest for all data received from this node

    byte[] kf; // symmetric key for sending data

    byte[] kb; // symmetric key for receiving data

    AESCounterMode aes_encrypt;

    AESCounterMode aes_decrypt;

    SHA1Digest sha1_forward;

    SHA1Digest sha1_backward;

    // The SKIP 1024 bit modulus
    static final BigInteger dh_p = new BigInteger(
            "00FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD129024E08"
                    + "8A67CC74020BBEA63B139B22514A08798E3404DDEF9519B3CD3A431B"
                    + "302B0A6DF25F14374FE1356D6D51C245E485B576625E7EC6F44C42E9"
                    + "A637ED6B0BFF5CB6F406B7EDEE386BFB5A899FA5AE9F24117C4B1FE6"
                    + "49286651ECE65381FFFFFFFFFFFFFFFF", 16);

    // The base used with the SKIP 1024 bit modulus
    static final BigInteger dh_g = new BigInteger("2");


    /** constructor for server-side.  */
    Node(Server init,byte[] dh_x_bytes) {
        if (init == null)
            throw new NullPointerException("can't init node on NULL server");
        // save a pointer to the server's data
        this.server = init;
        Random rnd = new Random();
        // do Diffie-Hellmann
        dh_x = new BigInteger(1,dh_x_bytes);
        dh_private = new BigInteger(dh_p.bitLength() - 1, rnd);
        BigInteger dh_xy = dh_x.modPow(dh_private, dh_p);
        byte[] dh_xy_bytes = BigIntegerTo128Bytes(dh_xy);

        // return dh_y-Bytes
        BigInteger dh_y = dh_g.modPow(dh_private, dh_p);
        dh_y_bytes = BigIntegerTo128Bytes(dh_y);
        // derive key-material
        SHA1Digest sha1 = new SHA1Digest();
        byte[] k = new byte[100];
        byte[] sha1_input = new byte[dh_xy_bytes.length + 1];
        System.arraycopy(dh_xy_bytes, 0, sha1_input, 0, dh_xy_bytes.length);
        for (int i = 0; i < 5; ++i) {
            sha1.reset();
            sha1_input[sha1_input.length - 1] = (byte) i;
            sha1.update(sha1_input, 0, sha1_input.length);
            sha1.doFinal(k, i * 20);
        };
        // DEBUGGING OUTPUT -- BEGIN
        Logger.logCrypto(Logger.VERBOSE, "Node.<init>: dh_x = \n"
                + Encoding.toHexString(dh_x_bytes, 100) + "\n" + "dh_y = \n"
                + Encoding.toHexString(dh_y_bytes, 100) + "\n" + "dh_xy = keymaterial:\n"
                + Encoding.toHexString(dh_xy_bytes, 100) + "\n" + "Key Data:\n"
                + Encoding.toHexString(k, 100));
        // DEBUGGING OUTPUT -- END

        
        // derived key info is correct - save to final destination
        // handshake
        kh = new byte[20];
        System.arraycopy(k, 0, kh, 0, 20);
        // forward digest
        forward_digest = new byte[20];
        System.arraycopy(k, 40, forward_digest, 0, 20);
        sha1_forward = new SHA1Digest();
        sha1_forward.update(forward_digest, 0, 20);
        // backward digest
        backward_digest = new byte[20];
        System.arraycopy(k, 20, backward_digest, 0, 20);
        sha1_backward = new SHA1Digest();
        sha1_backward.update(backward_digest, 0, 20);
        // secret key for sending data
        kf = new byte[16];
        System.arraycopy(k, 76, kf, 0, 16);
        aes_encrypt = new AESCounterMode(true, kf);
        // secret key for receiving data
        kb = new byte[16];
        System.arraycopy(k, 60, kb, 0, 16);
        aes_decrypt = new AESCounterMode(true, kb);
    }
    
    /** constructor for client-side */
    Node(Server init) {
        if (init == null)
            throw new NullPointerException("can't init node on NULL server");
        // save a pointer to the server's data
        this.server = init;
        Random rnd = new Random();

        // Diffie-Hellman: generate our secret
        dh_private = new BigInteger(dh_p.bitLength() - 1, rnd);
        // Diffie-Hellman: generate g^x
        dh_x = dh_g.modPow(dh_private, dh_p);
        dh_x_bytes = BigIntegerTo128Bytes(dh_x);

        // generate symmetric key for circuit creation
        symmetric_key_for_create = new byte[16];
        rnd.nextBytes(symmetric_key_for_create);
    }

    /**
     * encrypt data with asymmetric key. create asymmetricla encrypted data:<br>
     * <ul>
     * <li>OAEP padding [42 bytes] (RSA-encrypted)
     * <li>Symmetric key [16 bytes]                   FIXME: we assume that we ALWAYS need this 
     * <li>First part of data [70 bytes]
     * <li>Second part of data [x-70 bytes] (Symmetrically encrypted)
     * <ul>
     * encrypt and store in result
     * 
     * @param data
     *            to be encrypted, needs currently to be at least 70 bytes long
     * @return the first half of the key exchange, ready to be send to the other
     *         partner
     */
    byte[] asym_encrypt(byte[] data) throws TorException {
        if (data == null)
            throw new NullPointerException("can't encrypt NULL data");
        if (data.length < 70)
            throw new TorException("input array too short");

        try {
            int encrypted_bytes = 0;

            // init OAEP
            OAEPEncoding oaep = new OAEPEncoding(new RSAEngine());
            oaep.init(true, new RSAKeyParameters(false, server.onionKey.getModulus(), server.onionKey.getPublicExponent()));
            // apply RSA+OAEP
            encrypted_bytes = oaep.getInputBlockSize();
            byte[] oaep_input = new byte[encrypted_bytes];
            System.arraycopy(data, 0, oaep_input, 0, encrypted_bytes);
            byte[] part1 = oaep.encodeBlock(oaep_input, 0, encrypted_bytes);

            // init AES
            AESCounterMode aes = new AESCounterMode(true,symmetric_key_for_create);
            // apply AES
            byte[] aes_input = new byte[data.length - encrypted_bytes];
            System.arraycopy(data, encrypted_bytes, aes_input, 0, aes_input.length);
            byte part2[] = aes.processStream(aes_input);

            // replace unencrypted data
            byte[] result = new byte[part1.length + part2.length];
            System.arraycopy(part1, 0, result, 0, part1.length);
            System.arraycopy(part2, 0, result, part1.length, part2.length);

            return result;
        } catch (InvalidCipherTextException e) {
            Logger.logCell(Logger.ERROR,"Node.asym_encrypt(): can't encrypt cipher text:" + e.getMessage());
            throw new TorException("InvalidCipherTextException:" + e.getMessage());
        }
    }

    /**
     * called after receiving created or extended cell: finished DH-key
     * exchange. Expects the first 148 bytes of the data array to be filled
     * with:<br>
     * <ul>
     * <li>128 bytes of DH-data (g^y)
     * <li>20 bytes of derivated key data (KH) (see chapter 4.2 of torspec)
     * </ul>
     * 
     * @param data
     *            expects the received second half of the DH-key exchange
     */
    void finish_dh(byte[] data) throws TorException {
        // calculate g^xy
        // - fix some undocument stuff: all numbers are 128-bytes only!
        // - add a leading zero to all numbers
        dh_y_bytes = new byte[128];
        System.arraycopy(data, 0, dh_y_bytes, 0, 128);
        BigInteger dh_y = new BigInteger(1,dh_y_bytes);
        BigInteger dh_xy = dh_y.modPow(dh_private, dh_p);
        byte[] dh_xy_bytes = BigIntegerTo128Bytes(dh_xy);

        // derivate key material
        SHA1Digest sha1 = new SHA1Digest();
        byte[] k = new byte[100];
        byte[] sha1_input = new byte[dh_xy_bytes.length + 1];
        System.arraycopy(dh_xy_bytes, 0, sha1_input, 0, dh_xy_bytes.length);
        for (int i = 0; i < 5; ++i) {
            sha1.reset();
            sha1_input[sha1_input.length - 1] = (byte) i;
            sha1.update(sha1_input, 0, sha1_input.length);
            sha1.doFinal(k, i * 20);
        };

        // DEBUGGING OUTPUT -- BEGIN
        Logger.logCrypto(Logger.VERBOSE, "Node.finish_dh: dh_x = \n"
                + Encoding.toHexString(dh_x_bytes, 100) + "\n" + "dh_y = \n"
                + Encoding.toHexString(dh_y_bytes, 100) + "\n" + "dh_xy = keymaterial:\n"
                + Encoding.toHexString(dh_xy_bytes, 100) + "\n" + "Key Data:\n"
                + Encoding.toHexString(k, 100) + "\n" + "Data:\n"
                + Encoding.toHexString(data, 100));
        // DEBUGGING OUTPUT -- END

        // check if derived key data is equal to bytes 128-147 of data[]
        boolean equal = true;
        for (int i = 0; equal && (i < 20); ++i)
            equal = (k[i] == data[128 + i]);
        // is there some error in the key data?
        if (!equal)
            throw new TorException("derived key material is wrong!");

        // derived key info is correct - save to final destination
        // handshake
        kh = new byte[20];
        System.arraycopy(k, 0, kh, 0, 20);
        // forward digest
        forward_digest = new byte[20];
        System.arraycopy(k, 20, forward_digest, 0, 20);
        sha1_forward = new SHA1Digest();
        sha1_forward.update(forward_digest, 0, 20);
        // backward digest
        backward_digest = new byte[20];
        System.arraycopy(k, 40, backward_digest, 0, 20);
        sha1_backward = new SHA1Digest();
        sha1_backward.update(backward_digest, 0, 20);
        // secret key for sending data
        kf = new byte[16];
        System.arraycopy(k, 60, kf, 0, 16);
        aes_encrypt = new AESCounterMode(true, kf);
        // secret key for receiving data
        kb = new byte[16];
        System.arraycopy(k, 76, kb, 0, 16);
        aes_decrypt = new AESCounterMode(true, kb);
    }

    /**
     * calculate the forward digest
     * 
     * @param data
     * @return a four-byte array containing the digest
     */
    byte[] calc_forward_digest(byte[] data) {
        Logger.logCrypto(Logger.RAW_DATA, "Node.calc_forward_digest() on:\n"
                + Encoding.toHexString(data, 100));
        sha1_forward.update(data, 0, data.length);
        byte[] digest = new byte[20];
        SHA1Digest copyOldState = new SHA1Digest(sha1_forward); // ugly fix
                                                                // around
                                                                // bouncy-castle's
                                                                // behaviour on
                                                                // hashes
        sha1_forward.doFinal(digest, 0);
        sha1_forward = copyOldState;
        Logger.logCrypto(Logger.VERBOSE, " result:\n"
                + Encoding.toHexString(digest, 100));
        byte[] four_bytes = new byte[4];
        System.arraycopy(digest, 0, four_bytes, 0, 4);
        return four_bytes;
    }

    /**
     * calculate the backward digest
     * 
     * @param data
     * @return a four-byte array containing the digest
     */
    byte[] calc_backward_digest(byte[] data) {
        Logger.logCrypto(Logger.RAW_DATA, "Node.calc_backward_digest() on:\n"
                + Encoding.toHexString(data, 100));
        sha1_backward.update(data, 0, data.length);
        byte[] digest = new byte[20];
        SHA1Digest copyOldState = new SHA1Digest(sha1_backward); // ugly fix
                                                                    // around
                                                                    // bouncy-castle's
                                                                    // behaviour
                                                                    // on hashes
        sha1_backward.doFinal(digest, 0);
        sha1_backward = copyOldState;
        Logger.logCrypto(Logger.RAW_DATA, " result:\n"
                + Encoding.toHexString(digest, 100));
        byte[] four_bytes = new byte[4];
        System.arraycopy(digest, 0, four_bytes, 0, 4);
        return four_bytes;
    }

    /**
     * encrypt data with symmetric key
     * 
     * @param data
     *            is used for input and output.
     */
    void sym_encrypt(byte[] data) {
        Logger.logCrypto(Logger.VERBOSE, "Node.sym_encrypt for node "
                + server.nickname);
        //Logger.logCrypto(Logger.RAW_DATA, "Node.sym_encrypt in:\n"
         //       + Encoding.toHexString(data, 100));
        // encrypt data
        byte[] encrypted = aes_encrypt.processStream(data);
        // copy to output
        if (encrypted.length > data.length)
            System.arraycopy(encrypted, 0, data, 0, data.length);
        else
            System.arraycopy(encrypted, 0, data, 0, encrypted.length);
        // DEBUG: output
        Logger.logCrypto(Logger.RAW_DATA, "Node.sym_encrypt out:\n"
                + Encoding.toHexString(data, 100));
    }

    /**
     * decrypt data with symmetric key
     * 
     * @param data
     *            is used for input and output.
     */
    void sym_decrypt(byte[] data) {
        Logger.logCrypto(Logger.VERBOSE, "Node.sym_decrypt for node "
                + server.nickname);
        /*Logger.logCrypto(Logger.RAW_DATA, "Node.sym_decrypt in:\n" + Encoding.toHexString(data, 100)); */
        // decrypt data
        byte[] decrypted = aes_decrypt.processStream(data);
        // copy to output
        if (decrypted.length > data.length)
            System.arraycopy(decrypted, 0, data, 0, data.length);
        else
            System.arraycopy(decrypted, 0, data, 0, decrypted.length);
        // DEBUG: output
        /*Logger.logCrypto(Logger.RAW_DATA, "Node.sym_decrypt out:\n" + Encoding.toHexString(data, 100)); */
    }

    /** helper function to convert a bigInteger to a fixed-sized array for TOR-Usage */
    private byte[] BigIntegerTo128Bytes(BigInteger a) {
      byte[] temp = a.toByteArray();
      byte[] result = new byte[128];
      if (temp.length > 128)
        System.arraycopy(temp, temp.length - 128, result, 0, 128);
      else
        System.arraycopy(temp, 0, result, 128 - temp.length, temp.length);
      return result;
    }
}
