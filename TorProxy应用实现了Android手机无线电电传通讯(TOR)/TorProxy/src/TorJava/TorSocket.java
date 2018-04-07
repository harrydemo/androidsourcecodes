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
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * a socket class for TorJava. Actual functionality is implemented in
 * TorSocketImpl
 * 
 * @author Michael Koellejan
 * @version unstable
 */
public class TorSocket extends TorSocketTmpClass {

    Tor tor;

    public TorSocket(Tor varTor) throws SocketException {
        super(new TorSocketImpl(varTor));

        this.tor = varTor;
    }

    public TorSocket(InetAddress address, int port, Tor varTor)
            throws SocketException, IOException {
        // Local name and port are ignored by the current implementation of TOR.
        // They are used here to stick with the signatures of Socket's methods.
        this(address, port, InetAddress.getByName("127.0.0.1"), 6543, varTor);
    }

    public TorSocket(InetAddress address, int port, InetAddress localAddress,
            int localPort, Tor varTor) throws SocketException, IOException {
        this(varTor);
        this.bind(new InetSocketAddress(localAddress, localPort));
        this.connect(new InetSocketAddress(address, port));
    }

    public TorSocket(String host, int port, Tor varTor) throws SocketException,
            UnknownHostException, IOException {
        this(host, port, InetAddress.getByName("127.0.0.1"), 6543, varTor);
    }

    public TorSocket(String host, int port, InetAddress localAddress,
            int localPort, Tor varTor) throws SocketException,
            UnknownHostException, IOException {
        this(varTor);
        this.bind(new InetSocketAddress(localAddress, localPort));
        this.connect(new InetSocketAddress(host, port));
    }

    public void markAsDefunct() {
        this.socketImpl.markAsDefunct();
    }
}
