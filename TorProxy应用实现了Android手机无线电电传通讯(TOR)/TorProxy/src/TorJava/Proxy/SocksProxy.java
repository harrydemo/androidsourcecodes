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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import uk.ac.cam.cl.dtg.android.tor.TorProxy.TorProxyControlService;

public class SocksProxy extends Thread {
	
	private int port;
	private ServerSocket ss = null;
	private boolean stop = false;
	private ArrayList<SocksConnection> connections = new ArrayList<SocksConnection>();
	
	private TorProxyControlService torService = null;
	
	public SocksProxy(TorProxyControlService torService) {
		this.setName("SocksProxy");
		this.torService = torService;
		this.start();
	}
	public void run() {
		try{
			ss = new ServerSocket(0);
			port = ss.getLocalPort();
			//Log.w("SocksProxy", "Listening on port " + port);
			while(!stop) {
				Socket s = ss.accept();
				connections.add(new SocksConnection(s, torService));
			}
		}
		catch(Exception e) {
			//Log.i("SocksProxy", "Exception");
			System.err.println(e.toString());
			e.printStackTrace();
		}
	}
	
	public int getPort() {
		return port;
	}
	
	public void close() {
		//Log.i("SocksProxy", "Closing...");
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
		int noThreads = connections.size();
		for (int i=0; i<noThreads; i++) {
			connections.get(i).close();
		}
		connections.clear();
	}
}


