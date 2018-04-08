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

import java.net.Socket;
import java.net.SocketException;

/**
 * a socket class for TorJava. Actual functionality is implemented in
 * TorSocketImpl
 * 
 * @author Michael Koellejan
 * @version unstable
 */
public class TorSocketTmpClass extends Socket {

    TorSocketImpl socketImpl;

    public TorSocketTmpClass(TorSocketImpl argSocketImpl)
            throws SocketException {
        super(argSocketImpl);

        this.socketImpl = argSocketImpl;
    }
}
