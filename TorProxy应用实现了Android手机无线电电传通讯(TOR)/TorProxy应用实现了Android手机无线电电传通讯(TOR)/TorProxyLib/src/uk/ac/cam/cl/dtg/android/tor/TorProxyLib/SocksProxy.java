/**
 * TorProxyLib - Anonymous data communication for Android devices
 * 			   - Tools for application developers
 * Copyright (C) 2009 Connell Gauld
 * 
 *  Thanks to University of Cambridge,
 * 		Alastair Beresford and Andrew Rice
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

package uk.ac.cam.cl.dtg.android.tor.TorProxyLib;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Provides SOCKS proxy client functionality.
 * @author cmg47
 *
 */
public class SocksProxy {
	
	// Corresponds to version 5, 1 auth method, anonymous
	private final static byte[] SOCKS5_HELLO = {0x05, 0x01, 0x00};
	
	InetSocketAddress mProxy = null;
	
	/**
	 * Construct a SocksProxy that uses the provided proxy port with localhost
	 * @param proxyPort the port of the proxy
	 */
	public SocksProxy(int proxyPort) {
		mProxy = new InetSocketAddress("127.0.0.1", proxyPort);
	}
	
	/**
	 * Construct a SocksProxy that uses the provided proxy details
	 * @param proxyHost the IP address of the proxy
	 * @param proxyPort the port of the proxy
	 */
	public SocksProxy(String proxyHost, int proxyPort) {
		mProxy = new InetSocketAddress(proxyHost, proxyPort);
	}
	
	/**
	 * Connect to a host:port tunnelled through this SOCKS proxy 
	 * @param sock Socket over which to perform the SOCKS connection. Will create new Socket if null.
	 * @param destHost host to connect to
	 * @param destPort destination host port
	 * @param timeout socket timeout
	 * @return Socket ready for send and receive with destination
	 */
	public Socket connectSocksProxy(Socket insock, String destHost, int destPort, int timeout) throws IOException, UnknownHostException {
		
		Socket sock = insock;
		
		if (sock == null) sock = new Socket();
		
		sock.connect(mProxy, timeout);
        InputStream in = sock.getInputStream();
        OutputStream out = sock.getOutputStream();
        
        // Send SOCKS5 greeting
        out.write(SOCKS5_HELLO);
        out.flush();
        
        byte[] serverResponse = new byte[2];
        in.read(serverResponse);
        
        if (serverResponse[0] != 0x05) throw new IOException("SOCKS server returned invalid version number");
        if (serverResponse[1] != 0x00) throw new IOException("No common authentication method with SOCKS server");
        
        byte[] hostname = destHost.getBytes("utf-8");
        
        byte[] request = new byte[7 + hostname.length];
        request[0] = 0x05; // Version
        request[1] = 0x01; // Command (TCP stream)
        request[2] = 0x00; // Reserved
        request[3] = 0x03; // Domain name
        request[4] = intToNByteArray(hostname.length, 1)[0]; // Length of name
        System.arraycopy(hostname, 0, request, 5, hostname.length); // Name
        byte[] portArray = intToNByteArray(destPort, 2);
        System.arraycopy(portArray, 0, request, 5+hostname.length, 2); // Port
        
        out.write(request);
        out.flush();
        
        serverResponse = new byte[4];
        in.read(serverResponse);
        
        if (serverResponse[0] != 0x05) throw new IOException("SOCKS server returns invalid version number");
        switch (serverResponse[1]) {
        case 0x00:
        	// Success, just pass on
        	break;
        case 0x04:
        	throw new UnknownHostException();
        default:
        	throw new IOException("Error from SOCKs proxy: status " + serverResponse[1]);
        }
        if (serverResponse[2] != 0x00) throw new IOException("Invalid response from SOCKs proxy");
        
        switch (serverResponse[3]) {
        case 0x01: // IPv4 address
        	byte[] ipaddressv4 = new byte[4];
        	in.read(ipaddressv4);
        	break;
        case 0x03:
        	byte[] hostnameLengthArray = new byte[1];
        	in.read(hostnameLengthArray);
        	int hostnameLength = byteArrayToInt(hostnameLengthArray, 0, 1);
        	byte[] hostnameBack = new byte[hostnameLength];
        	in.read(hostnameBack);
        	break;
        case 0x04:
        	byte[] ipaddressv6 = new byte[16];
        	in.read(ipaddressv6);
        	break;
        }
        byte[] portBack = new byte[2];
        in.read(portBack);
        
        // Finished SOCKS handshake
        
        return sock;
	}
	
	/**
     * Convert int to the array of bytes. Network order (I think).
     * 
     * @param myInt
     *            integer to convert
     * @param n
     *            size of the byte array
     * @return byte array of size n
     * 
     */
    private static byte[] intToNByteArray(int myInt, int n) {
    
        byte[] myBytes = new byte[n];
    
        for (int i = 0; i < n; ++i) {
            myBytes[i] = (byte) ((myInt >> ((n - i - 1) * 8)) & 0xff);
        }
        return myBytes;
    }

    /**
     * Convert the byte array to an int starting from the given offset.
     * 
     * @param b
     *            byte array
     * @param offset
     *            array offset
     * @param length
     *            number of bytes to convert
     * @return integer
     */
    private static int byteArrayToInt(byte[] b, int offset, int length) {
        int value = 0;
        int numbersToConvert = b.length - offset;
    
        int n = Math.min(length, 4); // 4 bytes is max int size (2^32)
        n = Math.min(n, numbersToConvert); // make sure we are not out of array
                                            // bounds
    
        // if (numbersToConvert > 4)
        // offset = b.length - 4; // warning: offset has been changed
        // in order to convert LSB
    
        for (int i = 0; i < n; i++) {
            int shift = (n - 1 - i) * 8;
            value += (b[i + offset] & 0xff) << shift;
        }
        return value;
    }
}
