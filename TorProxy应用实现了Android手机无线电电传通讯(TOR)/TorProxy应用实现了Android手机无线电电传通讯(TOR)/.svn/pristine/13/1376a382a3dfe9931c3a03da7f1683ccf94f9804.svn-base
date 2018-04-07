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
package TorJava.Proxy;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import uk.ac.cam.cl.dtg.android.tor.TorProxy.TorProxyControlService;

public class HTTPProxy extends Thread {
	
	private int port = 0;
	private TorProxyControlService torService = null;
	private boolean stop = false;
	private ServerSocket ss = null;
	
	/**
	 * Start an HTTP proxy on an empty port
	 * @param torService the Tor control service
	 */
	public HTTPProxy(TorProxyControlService torService) {
		this.setName("HTTPProxy");
		this.torService = torService;
		this.start();
	}
	public void run() {
		try{
			ss = new ServerSocket(0);
			this.port = ss.getLocalPort();
			while(!stop) {
				Socket s = ss.accept();
				//Log.e("TorProxy", "Accepted connection");
				new HTTPConnection(s, torService);
			}
		} catch (BindException e) {
			
		} catch(Exception e) {
			//Log.e("TorProxy", e.toString());
			//System.err.println(e.toString());
			//e.printStackTrace();
		}
	}
	
	public int getPort() {
		return port;
	}
	
	public void close() {
		stop = true;
		if (ss != null) {
			try {
				ss.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.interrupt();
	}
}

