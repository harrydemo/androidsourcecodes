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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.RSAPublicKeySpec;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.RSAPrivateKeyStructure;
import org.bouncycastle.asn1.x509.RSAPublicKeyStructure;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.encodings.OAEPEncoding;
import org.bouncycastle.crypto.encodings.PKCS1Encoding;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.jce.provider.JCERSAPrivateKey;
import org.bouncycastle.jce.provider.JCERSAPublicKey;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.util.encoders.Base64;

import TorJava.Logger;

/**
 * this class contains utility functions concerning encryption
 * 
 * @author Lexi Pimenidis
 * @author Andriy Panchenko
 * @author Michael Koellejan
 */
public class Encryption {

    /**
     * returns the hash of the input
     * 
     * 
     */
    public static byte[] getHash(byte[] input) {
    
        SHA1Digest sha1 = new SHA1Digest();
        sha1.reset();
        sha1.update(input, 0, input.length);
    
        byte[] hash = new byte[sha1.getDigestSize()];
        sha1.doFinal(hash, 0);
        return hash;
    
    }

    /**
     * checks signature of PKCS1-padded SHA1 hash of the input
     * 
     * @param signature
     *            signature to check
     * @param signingKey
     *            public key from signing
     * @param input
     *            byte array, signature is made over
     * 
     * @return true, if the signature is correct
     * 
     */
    public static boolean verifySignature(byte[] signature,
            RSAPublicKeyStructure signingKey, byte[] input) {
    
        byte[] hash = getHash(input);
    
        try {
            RSAKeyParameters myRSAKeyParameters = new RSAKeyParameters(false,
                    signingKey.getModulus(), signingKey.getPublicExponent());
    
            PKCS1Encoding pkcs_alg = new PKCS1Encoding(new RSAEngine());
            pkcs_alg.init(false, myRSAKeyParameters);
    
            byte[] decrypted_signature = pkcs_alg.processBlock(signature, 0,
                    signature.length);
    
            return Encoding.arraysEqual(hash, decrypted_signature);
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return false;
    
    }

    /**
     * checks row signature
     * 
     * @param signature
     *            signature to check
     * @param signingKey
     *            public key from signing
     * @param input
     *            byte array, signature is made over
     * 
     * @return true, if the signature is correct
     * 
     */
    public static boolean verifySignatureXXXX(byte[] signature,
            RSAPublicKeyStructure signingKey, byte[] input) {
    
        byte[] hash = getHash(input);
        try {
            Signature sig = Signature.getInstance("SHA1withRSA");
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(signingKey
                    .getModulus(), signingKey.getPublicExponent());
            PublicKey pubKey = keyFactory.generatePublic(keySpec);
            sig.initVerify(pubKey);
            sig.update(input);
            System.out.println("");
            System.out.println(" HERE -> " + sig.verify(signature));
    
            RSAKeyParameters myRSAKeyParameters = new RSAKeyParameters(false,
                    signingKey.getModulus(), signingKey.getPublicExponent());
            RSAEngine rsa_alg = new RSAEngine();
            rsa_alg.init(false, myRSAKeyParameters);
            byte[] decrypted_signature = rsa_alg.processBlock(signature, 0,
                    signature.length);
            System.out.println(" inpu = " + Encoding.toHexString(input));
            System.out.println(" hash = " + Encoding.toHexString(hash));
            System.out.println("");
            System.out.println(" sign = " + Encoding.toHexString(signature));
            System.out.println(" decr = "
                    + Encoding.toHexString(decrypted_signature));
    
            return Encoding.arraysEqual(hash, decrypted_signature);
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return false;
    
    }

    /**
     * sign some data using a private kjey and PKCS#1 v1.5 padding
     * 
     * @param data
     *            the data to be signed
     * @param signingKey
     *            the key to sign the data
     * @return a signature
     */
    public static byte[] signData(byte[] data, RSAKeyParameters signingKey) {
        try {
            byte[] hash = Encryption.getHash(data);
            PKCS1Encoding pkcs1 = new PKCS1Encoding(new RSAEngine());
            pkcs1.init(true, signingKey);
            return pkcs1.processBlock(hash, 0, hash.length);
        } catch (InvalidCipherTextException e) {
            Logger.logGeneral(Logger.ERROR, "Common.signData(): "
                    + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /** used to encode a signature in PEM */
    public static String binarySignatureToPEM(byte[] signature) {
        String sigB64 = Encoding.toBase64(signature);
        StringBuffer sig = new StringBuffer();
    
        sig.append("-----BEGIN SIGNATURE-----\n");
        while (sigB64.length() > 64) {
            sig.append(sigB64.substring(0, 64) + "\n");
            sigB64 = sigB64.substring(64);
        }
        sig.append(sigB64 + "\n");
        sig.append("-----END SIGNATURE-----\n");
        return sig.toString();
    }

    /**
     * makes RSA public key from string
     * 
     * @param s
     *            string that contais the key
     * @return
     * @see JCERSAPublicKey
     */
    public static RSAPublicKeyStructure extractRSAKey(String s) {
    
        PEMReader reader = new PEMReader(new StringReader(s));
        JCERSAPublicKey JCEKey;
        RSAPublicKeyStructure theKey;
    
        try {
            Object o = reader.readObject();
            if (!(o instanceof JCERSAPublicKey))
                throw new IOException(
                        "Common.extractRSAKey: no public key found for signing key in string '"
                                + s + "' type " + o.getClass().getName());
            JCEKey = (JCERSAPublicKey) o;
            theKey = new RSAPublicKeyStructure(JCEKey.getModulus(), JCEKey
                    .getPublicExponent());
    
        } catch (IOException e) {
            Logger.logDirectory(Logger.WARNING,
                    "Common.extractRSAKey: Caught exception:" + e.getMessage());
            theKey = null;
        }
    
        return theKey;
    }

    /**
     * makes RSA public key from bin byte array
     * 
     * @param s
     *            string that contais the key
     * @return
     * @see JCERSAPublicKey
     */
    public static RSAPublicKeyStructure extractBinaryRSAKey(byte[] b) {
    
        RSAPublicKeyStructure theKey;
    
        try {
            ASN1InputStream ais = new ASN1InputStream(b);
            Object asnObject = ais.readObject();
            ASN1Sequence sequence = (ASN1Sequence) asnObject;
            theKey = new RSAPublicKeyStructure(sequence);
    
        } catch (IOException e) {
            Logger.logDirectory(Logger.WARNING, "Caught exception:"
                    + e.getMessage());
            theKey = null;
        }
    
        return theKey;
    }

    /**
     * copy from one format to another
     */
    public static RSAPublicKeyStructure getRSAPublicKeyStructureFromJCERSAPublicKey(
            JCERSAPublicKey jpub) {
        return new RSAPublicKeyStructure(jpub.getModulus(), jpub
                .getPublicExponent());
    }

    /**
     * converts a JCERSAPublicKey into PKCS1-encoding
     * 
     * @param rsaPublicKey
     * @see JCERSAPublicKey
     * @return PKCS1-encoded RSA PUBLIC KEY
     */
    public static byte[] getPKCS1EncodingFromRSAPublicKey(
            RSAPublicKeyStructure pubKeyStruct) {
        try {
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            ASN1OutputStream aOut = new ASN1OutputStream(bOut);
            aOut.writeObject(pubKeyStruct.toASN1Object());
            return bOut.toByteArray();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * converts a JCERSAPublicKey into PEM/PKCS1-encoding
     * 
     * @param rsaPublicKey
     * @see RSAPublicKeyStructure
     * @return PEM-encoded RSA PUBLIC KEY
     */
    public static String getPEMStringFromRSAPublicKey(RSAPublicKeyStructure rsaPublicKey) {
    
        // mrk: this was awful to program. Remeber: There are two entirely
        // different
        // standard formats for rsa public keys. Bouncy castle does only support
        // the
        // one we can't use for TOR directories.
    
        StringBuffer tmpDirSigningKey = new StringBuffer();
    
        try {
    
            tmpDirSigningKey.append("-----BEGIN RSA PUBLIC KEY-----\n");
    
            byte[] base64Encoding = Base64
                    .encode(getPKCS1EncodingFromRSAPublicKey(rsaPublicKey));
            for (int i = 0; i < base64Encoding.length; i++) {
                tmpDirSigningKey.append((char) base64Encoding[i]);
                if (((i + 1) % 64) == 0)
                    tmpDirSigningKey.append("\n");
            }
            tmpDirSigningKey.append("\n");
    
            tmpDirSigningKey.append("-----END RSA PUBLIC KEY-----\n");
        } catch (Exception e) {
            return null;
        }
    
        return tmpDirSigningKey.toString();
    }

    /**
     * makes RSA private key from base64 encoded string
     * 
     * @param s
     *            string that contais the key
     * @return
     * @see JCERSAPPrivateKey
     */
    public static JCERSAPrivateKey getRSAPrivateKeyFromPEMString(String s) {
    
        PEMReader reader = new PEMReader(new StringReader(s));
        JCERSAPrivateKey theKey;
    
        try {
            Object o = reader.readObject();
            if (o instanceof JCERSAPrivateKey) {
            	theKey = (JCERSAPrivateKey) o;
            } else if (o instanceof KeyPair) {
            	PrivateKey k = ((KeyPair)o).getPrivate();
            	if (k instanceof JCERSAPrivateKey) {
            		theKey = (JCERSAPrivateKey)k;
            	} else {
            		throw new IOException("key found is not RSA");
            	}
            } else {
                throw new IOException(
                        "no private key found for signing key in string '" + s
                                + "'");
            }
        } catch (IOException e) {
            Logger.logDirectory(Logger.WARNING, "Caught exception:"
                    + e.getMessage());
            theKey = null;
        }
    
        return theKey;
    }

    /**
     * makes RSA public key from base64 encoded string
     * 
     * @param s
     *            string that contais the key
     * @return
     * @see JCERSAPublicKey
     */
    public static JCERSAPublicKey getRSAPublicKeyFromPEMString(String s) {
    
        PEMReader reader = new PEMReader(new StringReader(s));
        JCERSAPublicKey theKey;
    
        try {
            Object o = reader.readObject();
            if (!(o instanceof JCERSAPublicKey))
                throw new IOException(
                        "no public key found for signing key in string '" + s
                                + "'");
            theKey = (JCERSAPublicKey) o;
        } catch (IOException e) {
            Logger.logDirectory(Logger.WARNING, "Caught exception:"
                    + e.getMessage());
            theKey = null;
        }
    
        return theKey;
    }

    /**
     * encrypt data with asymmetric key. create asymmetrically encrypted data:<br>
     * <ul>
     * <li>OAEP padding [42 bytes] (RSA-encrypted)
     * <li>Symmetric key [16 bytes]
     * <li>First part of data [70 bytes]
     * <li>Second part of data [x-70 bytes] (Symmetrically encrypted)
     * <ul>
     * encrypt and store in result
     * 
     * @param priv
     *            key to use for decryption
     * @param data
     *            to be decrypted, needs currently to be at least 70 bytes long
     * @return raw data
     */
    public static byte[] asym_decrypt(RSAKeyParameters priv, byte[] data)
            throws TorException {
    
        if (data == null)
            throw new NullPointerException("can't encrypt NULL data");
        if (data.length < 70)
            throw new TorException("input array too short");
    
        try {
            int encrypted_bytes = 0;
    
            // init OAEP
            OAEPEncoding oaep = new OAEPEncoding(new RSAEngine());
            oaep.init(false, priv);
            // apply RSA+OAEP
            encrypted_bytes = oaep.getInputBlockSize();
            byte[] oaep_input = new byte[encrypted_bytes];
            System.arraycopy(data, 0, oaep_input, 0, encrypted_bytes);
            byte[] part1 = oaep.decodeBlock(oaep_input, 0, encrypted_bytes);
    
            // extract symmetric key
            byte[] symmetric_key = new byte[16];
            System.arraycopy(part1, 0, symmetric_key, 0, 16);
            // init AES
            AESCounterMode aes = new AESCounterMode(true, symmetric_key);
            // apply AES
            byte[] aes_input = new byte[data.length - encrypted_bytes];
            System.arraycopy(data, encrypted_bytes, aes_input, 0, aes_input.length);
            byte part2[] = aes.processStream(aes_input);
    
            // replace unencrypted data
            byte[] result = new byte[part1.length - 16 + part2.length];
            System.arraycopy(part1, 16, result, 0, part1.length - 16);
            System.arraycopy(part2, 0, result, part1.length - 16, part2.length);

            return result;
    
        } catch (InvalidCipherTextException e) {
            Logger.logCell(Logger.ERROR,
                    "CommonEncryption.asym_decrypt(): can't decrypt cipher text:"
                            + e.getMessage());
            throw new TorException("CommonEncryption.asym_decrypt(): InvalidCipherTextException:"
                    + e.getMessage());
        }
    
    }

    /**
     * converts a PEM-encoded private key back into JCERSAPublicKey
     * 
     * @param rsaPrivateKey
     * @see JCERSAPrivateKey
     * @return PEM-encoded RSA PRIVATE KEY
     */
    public static String getPEMStringFromRSAPrivateKey(JCERSAPrivateKey rsaPrivateKey) {
    
        StringWriter pemStrWriter = new StringWriter();
        PEMWriter pemWriter = new PEMWriter(pemStrWriter);
    
        try {
            pemWriter.writeObject(rsaPrivateKey);
            pemWriter.close();
            pemStrWriter.flush();
        } catch (IOException e) {
            Logger.logDirectory(Logger.WARNING, "Caught exception:"
                    + e.getMessage());
            return "";
        }
    
        return pemStrWriter.toString();
    }

    public static RSAPrivateKeyStructure getRSAPrivateKeyStructureFromJCERSAPrivateKey(JCERSAPrivateKey key,JCERSAPublicKey pubkey) {
        return new RSAPrivateKeyStructure(key.getModulus(), pubkey.getPublicExponent(), 
                key.getPrivateExponent(), null, null, null, null, null);
    }

}
