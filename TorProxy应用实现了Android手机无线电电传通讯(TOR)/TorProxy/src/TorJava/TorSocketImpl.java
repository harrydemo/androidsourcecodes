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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketImpl;


/**
 * implements the actual functionality of TorSocket sockets.
 * 
 * @author Michael Koellejan
 * @version unstable
 */
public class TorSocketImpl extends SocketImpl implements TorSocketOptions {

    Tor tor;

    TCPStream stream;

    int retriesConnect;
    int maximumRouteLen;

    public TorSocketImpl(Tor varTor) {
        super();
        tor = varTor;
        retriesConnect = TorConfig.retriesConnect;
        maximumRouteLen = 3;
        // Semantics of SocketImpl ask for create() being called for
        // initialization.
    }

    protected void accept(SocketImpl s) {
        // Server mode. Not yet implemented.
    }

    protected int available() throws java.io.IOException {
        if (stream == null)
            throw new java.io.IOException();
        return stream.getInputStream().available();
    }

    protected void bind(InetAddress host, int port) throws java.io.IOException {
        // Server mode. Not yet implemented.
    }

    protected void close() throws java.io.IOException {
        stream.close();
    }

    protected void connect(String host, int port) throws java.io.IOException {
        TCPStreamProperties tcpProp = new TCPStreamProperties(host, port);
        tcpProp.setMaxRouteLength(maximumRouteLen);

        stream = tor.connect(tcpProp);

        this.address = tcpProp.addr;
        this.port = port;
    }

    protected void connect(InetAddress address, int port)
    throws java.io.IOException {
        TCPStreamProperties tcpProp = new TCPStreamProperties(address, port);
        tcpProp.setMaxRouteLength(maximumRouteLen);

        stream = tor.connect(tcpProp);

        this.address = tcpProp.addr;
        this.port = port;      
    }

    protected void connect(SocketAddress address, int timeout)
    throws java.io.IOException {

        try {
            if (address.getClass().equals(
                    Class.forName("java.net.InetSocketAddress")) == false) {
                throw new java.io.IOException();
            }
        } catch (ClassNotFoundException e) {
            throw new java.io.IOException();
        }

        InetSocketAddress inetSockAdr = (InetSocketAddress) address;
        this.connect(inetSockAdr.getAddress(), inetSockAdr.getPort());
    }

    protected void create(boolean stream) throws java.io.IOException {

        if (!stream)
            throw new IOException();
    }

    protected InputStream getInputStream() throws java.io.IOException {
        return stream.getInputStream();
    }

    protected OutputStream getOutputStream() throws java.io.IOException {
        return stream.getOutputStream();
    }

    protected void listen(int backlog) throws java.io.IOException {
        // Server mode. Not yet implemented.
    }

    protected void sendUrgentData(int data) throws java.io.IOException {
        // We don't support urgend data send, yet.
    }

    protected boolean supportsUrgendData() {
        return false;
    }

    public Object getOption(int optID) throws SocketException {
        if (optID == TOR_RETRIES_CONNECT_OPT) {
            return new Integer(retriesConnect);
        } else if (optID ==TOR_MAXIMUM_ROUTE_LEN ) {
            return new Integer(maximumRouteLen);
        }

        return null;
    }

    public void setOption(int optID, Object value) throws SocketException {
        if (optID == TOR_RETRIES_CONNECT_OPT) {
            retriesConnect = ((Integer)value).intValue();
        } else if (optID == TOR_MAXIMUM_ROUTE_LEN ) {
            maximumRouteLen = ((Integer)value).intValue();
        }
        // TODO: Possibly change TorConfig with this.
    }

    public void markAsDefunct() {
        // stream.close();
        // stream.circ.close(false);
    }
}
