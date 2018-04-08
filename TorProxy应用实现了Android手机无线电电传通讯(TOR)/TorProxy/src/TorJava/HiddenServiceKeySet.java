package TorJava;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.jce.provider.JCERSAPrivateKey;
import org.bouncycastle.jce.provider.JCERSAPublicKey;

import TorJava.Common.Encoding;
import TorJava.Common.Encryption;

public class HiddenServiceKeySet {
	public JCERSAPublicKey jpub;
	public JCERSAPrivateKey jpriv;
	public RSAKeyParameters pub, priv; // keys
	byte[] pubKeyHash;
	private String url;

	public HiddenServiceKeySet() {
		// Generate
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA",
					"BC");

			generator.initialize(1024, new SecureRandom());
			KeyPair keypair = generator.generateKeyPair();
			jpub = (JCERSAPublicKey) keypair.getPublic();
			jpriv = (JCERSAPrivateKey) keypair.getPrivate();
			// generate service descriptor
		} catch (NoSuchProviderException e) {
			Logger.logTLS(Logger.ERROR,
					"HiddenServiceProperties: Caught exception: "
							+ e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			Logger.logTLS(Logger.ERROR,
					"HiddenServiceProperties: Caught exception: "
							+ e.getMessage());
		}
		init();
	}

	public HiddenServiceKeySet(String publicKey, String privateKey) {
		jpub = Encryption.getRSAPublicKeyFromPEMString(publicKey);
		jpriv = Encryption.getRSAPrivateKeyFromPEMString(privateKey);
		init();
	}

	private void init() {
		pub = new RSAKeyParameters(false, jpub.getModulus(), jpub
				.getPublicExponent());
		priv = new RSAKeyParameters(true, jpriv.getModulus(), jpriv
				.getPrivateExponent());

		pubKeyHash = new byte[20];
		pubKeyHash = Encryption.getHash(Encryption
				.getPKCS1EncodingFromRSAPublicKey(Encryption
						.getRSAPublicKeyStructureFromJCERSAPublicKey(jpub)));
		updateURL();
	}
	
    private void updateURL() {
        try {
            // create hash of public key
            byte[] hash = Encryption.getHash(Encryption.getPKCS1EncodingFromRSAPublicKey(Encryption.getRSAPublicKeyStructureFromJCERSAPublicKey(jpub)));
            // take top 80-bits and convert to biginteger
            byte[] h1 = new byte[10];
            System.arraycopy(hash, 0, h1, 0, 10);
            // return encoding
            this.url = Encoding.toBase32(h1);
        } catch (Exception e) {
            Logger.logGeneral(Logger.ERROR, "ServiceDescriptor.updateURL(): "
                    + e.getMessage());
            e.printStackTrace();
            this.url = null;
        }
    }
	
	public String getPublicKey() {
		return Encryption.getPEMStringFromRSAPublicKey(Encryption.getRSAPublicKeyStructureFromJCERSAPublicKey(jpub));
	}
	
	public String getPrivateKey() {
		String str = Encryption.getPEMStringFromRSAPrivateKey(jpriv); 
		return str;
	}
	
	public String getUrl() {
		return url;
	}
}
