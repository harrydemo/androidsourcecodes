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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import TorJava.Common.TorException;

/**
 * functionality for the TLS connections bridging the gap to the first nodes in
 * the routes.
 * 
 * @author Lexi Pimenidis
 * @author Vinh Pham
 * @version unstable
 */

class TLSConnection {
    // pointer to the server
    Server server;

    // the physical connection (if any) to the node
    SSLSocket tls;

    // boolean handshake_finished = false;

    boolean closed = false;

    TLSDispatcher dispatcher;

    DataOutputStream sout;

    ConcurrentHashMap<Integer,Circuit> circuits;

    static String[] enabledSuites = { /*"SSL_DHE_RSA_WITH_3DES_EDE_CBC_SHA",
            "TLS_DHE_RSA_WITH_AES_128_CBC_SHA".*/
    		"DHE-RSA-AES128-SHA"};

    static String filenameKeyStore = "/tmp";

    /**
     * creates the TLS connection and installs a dispatcher for incoming data.
     * 
     * @param server
     *            the server to connect to
     * @see TLSDispatcher
     * @exception IOException
     * @exception SSLPeerUnverifiedException
     */
    TLSConnection(Server server, PrivateKeyHandler pkh) throws IOException,
            SSLPeerUnverifiedException, SSLException {
        if (server == null)
            throw new IOException("TLSConnection: server variable is NULL");
        this.server = server;
        circuits = new ConcurrentHashMap<Integer,Circuit>();

        // create new certificates and use them ad-hoc
//        KeyManager kms[] = new KeyManager[1];
        
        // XXX: Leave out the PrivateKeyHandler, should be needed for 
        // server operation and hidden services only
        //kms[0] = pkh;

        // use the keys and certs from above to connect to Tor-network
        try {
            TrustManager[] tms = { new TorX509TrustManager() };
            SSLContext context = SSLContext.getInstance("TLS");
             context.init(null, tms, null);
            //Log.i("TorTLS", "Here");
            //context.init(kms, tms, null);
            //Log.i("TorTLS", "Here2");
            SSLSocketFactory factory = (SSLSocketFactory) context.getSocketFactory();

            tls = (SSLSocket) factory.createSocket(server.hostname, server.orPort);

            // FIXME: check certificates received in TLS
            //        (note: not an important security bug, since it only affects hop2hop-encryption, real 
            //               data is encrypted anyway on top of TLS)

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
             * e.getPeerCertificates(); System.out.println("Received cert-chain
             * of length "+chain.length); for(int i=0;i<chain.length;++i)
             * System.out.println(" cert "+i+": "+chain[i].toString()); }
             * catch(Exception ex) {} } };
             * tls.addHandshakeCompletedListener(hscl);
             */

            tls.setEnabledCipherSuites(enabledSuites);
            //String[] strs = tls.getEnabledCipherSuites();
                        
            // create object to write data to stream
            sout = new DataOutputStream(tls.getOutputStream());
            // start listening for incoming data
            this.dispatcher = new TLSDispatcher(this, new DataInputStream(tls.getInputStream()));
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
     * constructor for incoming TLS-Connections
     */
    TLSConnection(SSLSocket ssl) throws IOException,
            SSLPeerUnverifiedException, SSLException {
        server = null;
        tls = ssl;
        // create object to write data to stream
        sout = new DataOutputStream(tls.getOutputStream());
        // start listening for incoming data
        this.dispatcher = new TLSDispatcher(this, new DataInputStream(tls.getInputStream()));
    }

    /**
     * converts a cell to bytes and transmitts it over the line. received data
     * is dispatched by the class TLSDispatcher
     * 
     * @param c
     *            the cell to send
     * @exception IOException
     * @see TLSDispatcher
     */
    synchronized void send_cell(Cell c) throws IOException {
			try{
        sout.write(c.toByteArray());
			}
			catch(IOException e) {
				// force to close the connection
				close(true);
				// rethrow error
				throw e;
			}
    }

    /**
     * returns a free circID and save that it points to "c", save it to "c",
     * too. Throws an exception, if no more free IDs are available, or the TLS
     * connection is marked as closed.<br>
     * FIXME: replace this code with something more beautiful
     * 
     * @param c
     *            the circuit that is going to be build through this
     *            TLS-Connection
     * @return an identifier for this new circuit
     * @exception TorException
     */
    synchronized int assign_circID(Circuit c) throws TorException {
        if (closed)
            throw new TorException("TLSConnection.assign_circID(): Connection to "+server.nickname+" is closed for new circuits");
        // find a free number (other than zero)
        int ID;
        int j = 0;
        do {
            if (++j > 1000) throw new TorException("TLSConnection.assign_circID(): no more free IDs");
            
            // Deprecated: 16 bit unsigned Integers with MSB set
            // ID = FirstNodeHandler.rnd.nextInt() & 0xffff | 0x8000;
            
            // XXX: Since the PrivateKeyHandler is gone, we don't need to consider 
            // the MSB as long as we are in client mode (see main-tor-spec.txt, Section 5.1) 
            ID = FirstNodeHandler.rnd.nextInt() & 0xffff; // & 0x7fff;
            
            if (circuits.containsKey(new Integer(ID)))
                ID = 0;
        } while (ID == 0);
        // assign ID to circuit, memorize circuit
        c.ID = ID;
        circuits.put(new Integer(ID), c);
        return ID;
    }

    /**
     * marks as closed. closes if no more data or forced closed on real close:
     * kill dispatcher
     * 
     * @param force
     *            set to TRUE if established circuits shall be cut and
     *            terminated.
     */
    void close(boolean force) {
        Logger.logTLS(Logger.VERBOSE, "TLSConnection.close(): Closing TLS to "
                + server.nickname);

        closed = true;
        // FIXME: a problem with (!force) is, that circuits, that are currently
        // still build up
        // are not killed. their build-up should be stopped
        // close circuits, if forced
        Iterator<Integer> i = circuits.keySet().iterator();
        while (i.hasNext()) {
            Object nick = i.next();
            Circuit onlist = (Circuit) circuits.get(nick);
            if (onlist.close(force))
                i.remove();
        }        
        if (!(force || circuits.isEmpty()))
            return;
        // kill dispatcher
        dispatcher.close();
        // close TLS connection
        try {
            sout.close();
            tls.close();
        } catch (IOException e) {
        }
    }
}
