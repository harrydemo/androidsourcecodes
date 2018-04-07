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

import java.net.InetAddress;

/**
 * Compound data structure.
 * 
 * @author Lexi Pimenidis
 * @author Andriy Panchenko
 * @version unstable
 */
public class TCPStreamProperties {
    String hostname;

    InetAddress addr;

    boolean addr_resolved; // set to true, if hostname is resolved into addr

    boolean allowUntrustedExit = true; // allow exit servers to be untrusted

		boolean allowNonGuardEntry = true; // allow entry node to be non Guard (dirv2)

    boolean exitPolicyRequired = true;

    int port;

    int route_min_length;

    int route_max_length;

    int connect_retries;
    
    float p; // [0..1] 0 -> select hosts completely randomly

    // 1 -> select hosts with good uptime/bandwidth with higher prob.

    String[] route;

    /**
     * preset the data structure with all necessary attributes
     * 
     * @param host
     *            give a hostname
     * @param port
     *            connect to this port
     */
    public TCPStreamProperties(String host, int port) {
        this.hostname = host;
        this.port = port;
        addr_resolved = false;

        init();
    }
    
    public TCPStreamProperties(InetAddress addr, int port) {
        this.hostname = addr.getHostAddress();
        this.addr = addr;
        this.port = port;
        addr_resolved = true;

        init();
    }

    public TCPStreamProperties() {
        this.hostname = null;
        this.addr = null;
        this.port = 0;
        addr_resolved = false;

        init();
    }
    
    /** Default initialization of member variables **/
    void init() {
        route_min_length = TorConfig.route_min_length;
        route_max_length = TorConfig.route_max_length;
        p = 1;
        connect_retries = TorConfig.retriesStreamBuildup;
    }

    /**
     * set minimum route length
     * 
     * @param min
     *            minimum route length
     */
    public void setMinRouteLength(int min) {
        if (min >= 0)
            route_min_length = min;
    }

    /**
     * set maximum route length
     * 
     * @param max
     *            maximum route length
     */
    public void setMaxRouteLength(int max) {
        if (max >= 0)
            route_max_length = max;
    }

    /**
     * get minimum route length
     * 
     * @return minimum route length
     */
    public int getMinRouteLength() {
        return route_min_length;
    }

    /**
     * get maximum route length
     * 
     * @return maximum route length
     */
    public int getMaxRouteLength() {
        return route_max_length;
    }

    /**
     * sets predefined route
     * 
     * @param route
     *            custom route
     */
    public void setCustomRoute(String[] route) {
        this.route = route;
    }

    /** sets this node as a predefined exit-point 
     * 
     * @param node
     */
    public void setCustomExitpoint(String node) {
        if (route == null) {
            this.route_min_length = this.route_max_length;
            route = new String[this.route_max_length];
        }
        route[route.length-1] = node;
    }
    
    /**
     * @return predefined route
     * 
     */
    public String[] getProposedRoute() {
        return route;
    }

    public float getRankingInfluenceIndex() {
        return p;
    }

    public void setRankingInfluenceIndex(float p) {
        this.p = p;
    }

		/**
		 * returns hostname if set, in another case the IP
		 *
		 */
		public String getDestination(){
						if (hostname.length() > 0)
										return hostname;
						return addr.getHostAddress();
		}

}
