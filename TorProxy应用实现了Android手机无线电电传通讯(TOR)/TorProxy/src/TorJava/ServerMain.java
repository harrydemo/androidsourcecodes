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
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;

/**
 * main class for server functionality
 * 
 * @author Lexi Pimenidis
 * @version unstable
 */

class ServerMain {
		Tor tor;

    Thread or_listener;

    SSLServerSocket or_server;

    ConcurrentHashMap<String,TLSConnection> tls_connection; // pointer to tls-connections in FirstNodeHandler!!

    static String[] enabledSuites = { /*"SSL_DHE_RSA_WITH_3DES_EDE_CBC_SHA",*/
            "TLS_DHE_RSA_WITH_AES_128_CBC_SHA" };

    static String filenameKeyStore = "/tmp";

    /**
     * creates the TLS server socket and installs a dispatcher for incoming
     * data.
     * 
     * @param or_port
     *            the port to open for or-connections
     * @param dir_port
     *            the port to open for directory services
     * @exception IOException
     * @exception SSLPeerUnverifiedException
     */
    ServerMain(Tor tor, int or_port, int dir_port)
            throws IOException, SSLPeerUnverifiedException, SSLException 
		{
				this.tor = tor;
			
        if (or_port < 1)
            throw new IOException("invalid port given");
        if (dir_port < 1)
            throw new IOException("invalid port given");
        if (or_port > 0xffff)
            throw new IOException("invalid port given");
        if (dir_port > 0xffff)
            throw new IOException("invalid port given");
        tls_connection = tor.fnh.tls;

        KeyManager kms[] = new KeyManager[1];
        kms[0] = tor.privateKeyHandler;

        // use the keys and certs from above to connect to Tor-network
        try {
            TrustManager[] tms = { new TorX509TrustManager() };
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(kms, tms, null);
            SSLServerSocketFactory factory = (SSLServerSocketFactory) context
                    .getServerSocketFactory();

            or_server = (SSLServerSocket) factory.createServerSocket(or_port);

            // FIXME: check certificates received in TLS

            /*
             * // for debugging purposes
             * javax.net.ssl.HandshakeCompletedListener hscl = new
             * javax.net.ssl.HandshakeCompletedListener() { public void
             * handshakeCompleted(HandshakeCompletedEvent e) { try {
             * System.out.println("Cipher: "+e.getCipherSuite());
             * java.security.cert.Certificate[] chain =
             * e.getLocalCertificates(); System.out.println("Send cert-chain of
             * length "+chain.length); for(int i=0;i<chain.length;++i)
             * System.out.println(" cert "+i+": "+chain[i].toString()); chain =
             * e.getPeerCertificates(); System.out.println("Recieved cert-chain
             * of length "+chain.length); for(int i=0;i<chain.length;++i)
             * System.out.println(" cert "+i+": "+chain[i].toString()); }
             * catch(Exception ex) {} } };
             * tls.addHandshakeCompletedListener(hscl);
             */
            // TODO: Re-enable this
            //or_server.setEnabledCipherSuites(enabledSuites);

            // start listening for incoming data
            or_listener = new Thread() {
                public void run() {
                    try {
                        while (true) {
                            try {
                                SSLSocket ssl = (SSLSocket) (or_server.accept());
                                ssl.setEnabledCipherSuites(enabledSuites);
                                ssl.startHandshake();
                                TLSConnection tls = new TLSConnection(ssl);
                                // add connection to array
                                String descr = ssl.getInetAddress()
                                        .getHostAddress()
                                        + ":" + ssl.getPort();
                                Logger.logTLS(Logger.VERBOSE,"Incoming TLS connection from " + descr);
                                tls_connection.put(descr, tls);
                            } catch (SecurityException e) {
                            }
                        }
                    } catch (IOException e) {
                    }
                }
            };
            or_listener.start();
        /*} catch (NoSuchProviderException e) {
            SSLException e2 = new SSLException(e.getMessage());
            e2.setStackTrace(e.getStackTrace());
            throw e2;*/
        } catch (NoSuchAlgorithmException e) {
            SSLException e2 = new SSLException(e.getMessage());
            e2.setStackTrace(e.getStackTrace());
            throw e2;
        } catch (KeyManagementException e) {
            SSLException e2 = new SSLException(e.getMessage());
            e2.setStackTrace(e.getStackTrace());
            throw e2;
        }
    }

    /**
     * @param force
     *            set to TRUE if close anyway as fast as possible
     */
    void close(boolean force) {
        Logger.logTLS(Logger.VERBOSE,"ServerMain.close(): Closing TLS server");

        // tls-connections are handled by FirstNodeHandler, no need to close
        // form here and there
        // close connections
        try {
            or_server.close();
        } catch (IOException e) {
        }
        // join thread listening on server port
        try {
            or_listener.join();
        } catch (InterruptedException e) {
        }
    }
}
