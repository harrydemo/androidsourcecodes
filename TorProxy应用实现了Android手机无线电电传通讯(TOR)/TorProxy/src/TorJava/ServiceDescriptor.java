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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.bouncycastle.asn1.x509.RSAPublicKeyStructure;
import org.bouncycastle.crypto.params.RSAKeyParameters;

import TorJava.Common.Encoding;
import TorJava.Common.Encryption;
import TorJava.Common.StreamsAndHTTP;
import TorJava.Common.TorException;

/**
 * class that represents the Service Descriptor
 * 
 * @see http://tor.eff.org/cvs/tor/doc/tor-hidden-service.html
 * 
 * @author Andriy
 * @author Lexi
 * @author Connell Gauld
 */

class ServiceDescriptor {

	// public key
	RSAPublicKeyStructure publicKey;

	RSAKeyParameters privateKey;

	byte[] signature;

	int timestamp, numberOfIntroPoints, version;

	String url;

	// introduction points
	HashSet<IntroductionPoint> introPoints;

	byte[] bytesIntroPoints;

	/**
	 * constructor for creating a service descriptor
	 */
	ServiceDescriptor(int version, RSAKeyParameters publicKey,
			RSAKeyParameters privateKey,
			HashSet<IntroductionPoint> given_introPoints) throws TorException {
		if (version != 0)
			throw new TorException("not implemented"); // FIXME: service
														// descriptors of
														// version != 0 are not
														// supported, yet
		this.version = version;
		this.timestamp = (int) (System.currentTimeMillis() / 1000L);
		this.publicKey = new RSAPublicKeyStructure(publicKey.getModulus(),
				publicKey.getExponent());
		this.privateKey = privateKey;
		updateURL();
		// store intro-points
		introPoints = given_introPoints;
		numberOfIntroPoints = given_introPoints.size();
		byte[] temp = new byte[introPoints.size() * 100];
		int temp_fill = 0;
		Iterator<IntroductionPoint> i = introPoints.iterator();
		while (i.hasNext()) {
			byte[] s = i.next().getIdentityDigest().getBytes();
			System.arraycopy(s, 0, temp, temp_fill, s.length);
			temp_fill += s.length + 1;
		}
		this.bytesIntroPoints = new byte[temp_fill];
		System.arraycopy(temp, 0, bytesIntroPoints, 0, temp_fill);
	}

	/**
	 * needs to be called, in case of service descriptor is self-generated and
	 * shall be called with toByteArray()
	 */
	void updateSignature() throws TorException {
		signature = Encryption.signData(toByteArray(false), privateKey);
	}

	/**
	 * for sending the descriptor
	 */
	byte[] toByteArray() throws TorException {
		return toByteArray(true);
	}

	/**
	 * for sending the descriptor
	 */
	private byte[] toByteArray(boolean withSignature) throws TorException {
		// get PK
		byte[] publicKey_bytes;
		try {
			publicKey_bytes = Encryption
					.getPKCS1EncodingFromRSAPublicKey(publicKey);
		} catch (Exception e) {
			throw new TorException("ServiceDescriptor.toByteArray(): "
					+ e.getMessage());
		}
		byte[] kl = Encoding.intToNByteArray(publicKey_bytes.length, 2);
		byte[] ts = Encoding.intToNByteArray(timestamp, 4);
		byte[] ni = Encoding.intToNByteArray(numberOfIntroPoints, 2);
		if (numberOfIntroPoints < 1)
			Logger
					.logGeneral(Logger.WARNING,
							"ServiceDescriptor.toByteArray(): strange data, possibly wrong?");
		// concatenate all together
		byte[] result;
		if (withSignature) {
			if (signature == null)
				updateSignature();
			result = new byte[2 + publicKey_bytes.length + 4 + 2
					+ bytesIntroPoints.length + signature.length];
		} else
			result = new byte[2 + publicKey_bytes.length + 4 + 2
					+ bytesIntroPoints.length];
		System.arraycopy(kl, 0, result, 0, 2);
		System.arraycopy(publicKey_bytes, 0, result, 2, publicKey_bytes.length);
		System.arraycopy(ts, 0, result, 2 + publicKey_bytes.length, 4);
		System.arraycopy(ni, 0, result, 6 + publicKey_bytes.length, 2);
		System.arraycopy(bytesIntroPoints, 0, result,
				8 + publicKey_bytes.length, bytesIntroPoints.length);
		if (withSignature)
			System.arraycopy(signature, 0, result, 8 + publicKey_bytes.length
					+ bytesIntroPoints.length, signature.length);
		// return result
		return result;
	}

	/**
	 * constructor that is applied for data received from a dir-server. this one
	 * parses the data and sets all fields appropriately
	 * 
	 */
	ServiceDescriptor(String url, byte[] sd, Directory dir) throws IOException,
			TorException {
		if (sd == null)
			throw new IOException("ServiceDescriptor: given NULL-data");

		int offset = 0;

		if ((sd[0] == 255) && (sd[1] == 1)) {
			offset = 2; // V1 descriptor
			version = 1;
		} else {
			offset = 0; // V0 descriptor
			version = 0;
		}

		int keyLength = Encoding.byteArrayToInt(sd, offset, offset + 2);
		byte[] bytesKey = new byte[keyLength];
		System.arraycopy(sd, offset + 2, bytesKey, 0, keyLength);

		publicKey = Encryption.extractBinaryRSAKey(bytesKey);

		timestamp = Encoding.byteArrayToInt(sd, keyLength + offset + 2, 4);
		if (!checkTimeStampValidity())
			throw new TorException("ServiceDescriptor: TimeStamp is not valid");

		if (offset == 0) {

			// proceed V0: parse introduction points
			offset += keyLength + 6;
			numberOfIntroPoints = Encoding.byteArrayToInt(sd, offset, 2);
			offset += 2;
			int offset_intro_start = offset;

			introPoints = new HashSet<IntroductionPoint>(numberOfIntroPoints);

			for (int i = 0; i < numberOfIntroPoints; ++i) {
				String or = "";
				while (sd[offset] != 0) {
					or += (char) sd[offset];
					offset++;
				}
				introPoints.add(new IntroductionPoint(or, dir));
				offset++;
			}
			// save list of intro-points as they were
			bytesIntroPoints = new byte[offset - offset_intro_start];
			System.arraycopy(sd, offset_intro_start, bytesIntroPoints, 0,
					offset - offset_intro_start);

		} else {
			// TODO proceed V1 as in 0.1.1.6-alpha
			offset += keyLength + 8; // next 2 bytes PROTO, skip them at the
										// point

			int authNumber = sd[offset]; // NA number of auth mechanisms
											// accepted
			int i = 0;
			while (i < authNumber) {

				// AUTHT 2 octets

				// AUTHL 1 octet

				// AUTHD variable

				i++;
			}

			numberOfIntroPoints = Encoding.byteArrayToInt(sd, offset, 2);

			introPoints = new HashSet<IntroductionPoint>(numberOfIntroPoints);

			i = 0;
			while (i < numberOfIntroPoints) {

				// ATYPE 1 octets

				// ADDR 4 or 6 octets

				// PORT 2 octets

				// AUTHT 2 octets

				// AUTHL 1 octet

				// AUTHD variable

				// ID 20 octets
				// KLEN 2 octets
				// KEY [KLEN octets]

				i++;
			}

		}

		// offset points to begin of signature
		// now copy and verify the signature
		byte[] input = new byte[offset];
		signature = new byte[sd.length - offset];
		System.arraycopy(sd, offset, signature, 0, sd.length - offset);
		System.arraycopy(sd, 0, input, 0, offset);
		// DEBUGING-OUTPUT-BEGIN
		/*
		 * byte hash[] = Common.getHash(input); System.out.println("All
		 * Data:"+sd.length+" bytes\n"+Common.toHexString(sd));
		 * System.out.println("Hash :"+hash.length+"
		 * bytes\n"+Common.toHexString(hash));
		 * System.out.println("Signature:"+signature.length+"
		 * bytes\n"+Common.toHexString(signature)); System.out.println("Signed
		 * Data:"+input.length+" bytes\n"+Common.toHexString(input)); if (!
		 * Common.verifySignatureXXXX(signature, publicKey, input)) throw new
		 * TorException("ServiceDescriptor: signature verification failed");
		 */
		// DEBUGING-OUTPUT-END
		if (!Encryption.verifySignature(signature, publicKey, input))
			throw new TorException(
					"ServiceDescriptor: signature verification failed");

		// check base32-encoding of public-key to be equivalent to this.url
		updateURL();
		// if ( (url!=null) && (! this.url.equalsIgnoreCase(url)) )
		// throw new TorException("ServiceDescriptor: some error parsing the
		// service descriptor, received URL "+this.url+" instead of "+url);

	}

	/**
	 * loads a descriptor from the networks
	 * 
	 * @param z
	 *            the z-part of the address
	 */
	static ServiceDescriptor loadFromDirectory(String z, Tor tor)
			throws IOException {

		int i = 2; // Try twice
		while (i > 0) {

			// make a connection to the directory service
			HashMap<String, HashMap<String, Object>> directoryServers = tor.config.trustedServers;
			Iterator<String> it = directoryServers.keySet().iterator();

			while (it.hasNext()) {
				// fetch data for hidden service's rendezvous anonymously
				String dirServerNick = (String) it.next();
				HashMap<String, Object> dirServerEntry = tor.config.trustedServers
						.get(dirServerNick);

				String address = (String) dirServerEntry.get("ip");
				int port = ((Integer) dirServerEntry.get("port")).intValue();
				TCPStreamProperties sp = new TCPStreamProperties(address, port);
				TCPStream stream = null;

				Logger.logGeneral(Logger.INFO,
						"ServiceDescriptor.loadFromDirectory: fetching service descriptor for "
								+ z + " from " + dirServerNick + " (" + address
								+ ":" + port + ")");

				try {
					stream = tor.connect(sp);
				} catch (IOException e) {
					Logger.logGeneral(Logger.WARNING,
							"ServiceDescriptor.loadFromDirectory: unable to connect to directory server "
									+ address + "(" + e.getMessage() + ")");
					continue;
				}

				byte[] interm = StreamsAndHTTP.retrieveServiceDescriptor(
						stream, z);

				// Search for end of headers (CR, LF, CR, LF)
				int dataStart = 0;
				for (int m = 0; m < (interm.length - 3); m++) {
					if ((interm[m] == 13) && (interm[m + 1] == 10)
							&& (interm[m + 2] == 13) && (interm[m + 3] == 10)) {
						dataStart = m + 4;
						break;
					}
				}
				// Separate data and headers
				byte[] data = new byte[interm.length - dataStart];
				System.arraycopy(interm, dataStart, data, 0, data.length);
				byte[] headers = new byte[dataStart];
				System.arraycopy(interm, 0, headers, 0, dataStart);

				String str = new String(headers);
				// System.out.println("After retrieve:" + str);
				stream.close();

				if (!str.startsWith("HTTP/")) {
					Logger.logGeneral(Logger.ERROR,
							"ServiceDescriptor.loadFromDirectory: directory request failed: no http!\n"
									+ str);
				} else if (str.indexOf(" 400") != -1) {
					Logger.logGeneral(Logger.ERROR,
							"ServiceDescriptor.loadFromDirectory: hidden service "
									+ z + " directory request failed: 400");
				} else if (str.indexOf(" 200") != -1) {

					// str = Parsing.parseStringByRE(str, ".*?\n\n(.*)", "");
					try {
						return new ServiceDescriptor(z, data, tor.dir);
					} catch (TorException e) {
						Logger.logGeneral(Logger.WARNING,
								"ServiceDescriptor.loadFromDirectory: problem parsing Service Descriptor for "
										+ z);
						continue;
					}
				}
			}
			--i;
		}
		;
		Logger.logGeneral(Logger.WARNING,
				"ServiceDescriptor.loadFromDirectory: unable to fetch service descriptor for "
						+ z);
		throw new IOException(
				"ServiceDescriptor.loadFromDirectory: unable to fetch service descriptor for "
						+ z);
	}

	private void updateURL() {
		try {
			// create hash of public key
			byte[] hash = Encryption.getHash(Encryption
					.getPKCS1EncodingFromRSAPublicKey(publicKey));
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

	/**
	 * checks whether the timestamp is no older than 24h
	 * 
	 * @param timestamp
	 *            time stamp to check
	 * 
	 */
	boolean checkTimeStampValidity() {
		return (((int) (System.currentTimeMillis() / 1000)) - timestamp) < 86400;
	}

	/**
	 * returns the z-part of the url
	 */
	String getURL() {
		return url;
	}

}
