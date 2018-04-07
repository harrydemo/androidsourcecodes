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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.util.Date;

import javax.net.ssl.X509KeyManager;

/**
 * manages private keys for:
 * <ul>
 * <li>identity
 * <li>onion routing
 * <li>hidden services
 * </ul>
 * 
 * @author Lexi Pimenidis
 */
class PrivateKeyHandler implements X509KeyManager {
	
	public static final String CACHE_FILENAME = "keycache.dat";
	
    KeyPair keypair = null;
    private boolean createdNewCachedPair = false;

    PrivateKeyHandler(boolean fastStart) {
        // generates a new random key pair on every start. read one from file,
        // if neccessary
    	if (fastStart) {
    		FileInputStream inf = null;

    		try {
				inf = new FileInputStream(TorConfig.getConfigDir() + CACHE_FILENAME);
				ObjectInputStream in = new ObjectInputStream(inf);
				
				keypair = (KeyPair)in.readObject();
				Logger.logTLS(Logger.INFO, "Restored keypair from cache");
			} catch (Exception e) {
				// Just generate one instead
			} finally {
				if (inf != null) {
					try {
						inf.close();
					} catch (IOException e) {}
				}
			}
			
			// Never use a keypair more than once so delete cache
			File f = new File(TorConfig.getConfigDir() + CACHE_FILENAME);
			f.delete();
    	}
    	
    	if (keypair != null) return;
    	
        try {
            
            keypair = generateKeyPair();
            
        } catch (Exception e) {
            Logger.logTLS(Logger.ERROR,"PrivateKeyHandler: Caught exception: " + e.getMessage());
        }
    }
    
    private KeyPair generateKeyPair() throws Exception {
    	KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
        generator.initialize(1024, new SecureRandom());
        return generator.generateKeyPair();
    }

    KeyPair getIdentity() {
        return keypair;
    }

    public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket) {
        return "TorJava";
    }

    public PrivateKey getPrivateKey(String alias) {
        return keypair.getPrivate();
    }

    public String[] getClientAliases(String keyType, Principal[] issuers) {
        String[] s = new String[1];
        s[0] = "TorJava";
        return s;
    }

    public java.security.cert.X509Certificate[] getCertificateChain(String alias) {
        try {
            org.bouncycastle.x509.X509V3CertificateGenerator generator = new org.bouncycastle.x509.X509V3CertificateGenerator();
            generator.reset();
            generator.setSerialNumber(BigInteger.valueOf(42));
            generator.setNotBefore(new Date( System.currentTimeMillis() - 24L * 3600 * 1000));
            generator.setNotAfter(new Date(System.currentTimeMillis() + 365L * 24 * 3600 * 1000));
            /*generator.setIssuerDN(new org.bouncycastle.asn1.x509.X509Name( "CN=TorJava, O=TOR"));
            generator.setSubjectDN(new org.bouncycastle.asn1.x509.X509Name( "CN=TorJava, O=TOR"));*/
            generator.setIssuerDN(new org.bouncycastle.asn1.x509.X509Name( "CN=TorJava"));
            generator.setSubjectDN(new org.bouncycastle.asn1.x509.X509Name( "CN=TorJava"));
            generator.setPublicKey(keypair.getPublic());
            generator.setSignatureAlgorithm("SHA1WITHRSA");
            java.security.cert.X509Certificate x509 = generator.generateX509Certificate(keypair.getPrivate());
            java.security.cert.X509Certificate[] x509s = new java.security.cert.X509Certificate[2];
            // send the same certificate twice works fine with the default implementation of tor!
            x509s[0] = x509;  // myself
            x509s[1] = x509;  // a certificate for myself
            return x509s;
        } catch (Exception e) {
            Logger.logTLS(Logger.ERROR, "Caught exception: " + e.getMessage());
        }
        return null;
    }

    public String chooseServerAlias(String keyType, Principal[] issuers,
            Socket socket) {
        return null;
    }

    public String[] getServerAliases(String keyType, Principal[] issuers) {
        return null;
    }
    
    /**
     * Create a new KeyPair and place it in the cache for the next time the
     * program runs. Only actually does this once.
     */
    public void prepareCachedKeyPairIfNeeded() {
    	if (createdNewCachedPair) return;
    	Logger.logTLS(Logger.INFO,"PrivateKeyHandler: Creating new cached pair");
    	try {
	    	KeyPair pairForCache = generateKeyPair();
	    	
	        FileOutputStream outf = null;
			try {
				outf = new FileOutputStream(TorConfig.getConfigDir() + CACHE_FILENAME);
				ObjectOutputStream out = new ObjectOutputStream(outf);
				out.writeObject(pairForCache);
				Logger.logTLS(Logger.INFO, "Cached keypair");
				createdNewCachedPair = true;
			} catch (Exception e) {
			} finally {
				if (outf != null) {
					try {
						outf.close();
					} catch (IOException m) {}
				}
			}
    	} catch (Exception e) {
    		Logger.logTLS(Logger.WARNING,"PrivateKeyHandler: Unable to create cached pair: " + e.getMessage());
    	}
    }
}
