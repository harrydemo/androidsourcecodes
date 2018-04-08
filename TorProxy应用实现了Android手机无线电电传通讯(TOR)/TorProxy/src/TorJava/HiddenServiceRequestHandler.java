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

/**
 * implement to have a callback-mechanism for incoming connections on a hidden
 * service
 * 
 * @author Lexi Pimenidis
 */
public interface HiddenServiceRequestHandler {
    /**
     * is called whenever a incoming connection is detected
     * 
     * @param incomingConnection
     * @return if connection was handled true, false, if connection was rejected
     */
    boolean accept(TCPStream incomingConnection);
    
    /**
     * is called to close this handler down
     */
    void close();
    
}
