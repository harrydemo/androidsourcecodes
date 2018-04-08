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
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import javax.net.SocketFactory;

/**
 * this factory creates TorSocket's for other applications.
 * 
 * @author Michael Koellejan
 * @version unstable
 */
public class TorSocketFactory extends SocketFactory {

    Tor tor;

    static TorSocketFactory defaultFactory;

    public TorSocketFactory(Tor varTor) {
        super();
        this.tor = varTor;
        if (TorSocketFactory.defaultFactory == null) {
            TorSocketFactory.defaultFactory = this;
        }
    }

    public static SocketFactory getDefault() {
        return TorSocketFactory.defaultFactory;
    }

    public Socket createSocket() throws SocketException {
        return new TorSocket(this.tor);
    }

    public Socket createSocket(InetAddress address, int port)
            throws SocketException, IOException {
        return new TorSocket(address, port, tor);
    }

    public Socket createSocket(InetAddress address, int port,
            InetAddress localAddress, int localPort) throws SocketException,
            IOException {
        return new TorSocket(address, port, localAddress, localPort, tor);
    }

    public Socket createSocket(String host, int port) throws SocketException,
            IOException {
        return new TorSocket(host, port, tor);
    }

    public Socket createSocket(String host, int port, InetAddress localAddress,
            int localPort) throws SocketException, IOException {
        return new TorSocket(host, port, localAddress, localPort, tor);
    }
}
