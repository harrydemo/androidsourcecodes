package org.loon.framework.android.game.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.loon.framework.android.game.core.LRelease;
import org.loon.framework.android.game.core.input.LKey;
import org.loon.framework.android.game.core.input.LTouch;
import org.loon.framework.android.game.core.store.Session;


/**
 * Copyright 2008 - 2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * @project loonframework
 * @author chenpeng
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
/**
 * 0.3.2版新增类，用以实现简单的游戏服务器端数据处理
 */
public class NetworkServer implements LRelease {

	private String name;

	private ServerSocket server;

	private Thread serverListener;

	private NetworkListener listener;

	private boolean running, daemon;

	private ArrayList<NetworkClient> clients;

	public NetworkServer(String name, int port) throws IOException {
		this.running = true;
		this.daemon = false;
		this.clients = new ArrayList<NetworkClient>();
		this.name = name;
		this.server = new ServerSocket(port);
	}

	public void start() {
		if (serverListener == null) {
			serverListener = new Thread(new Runnable() {
				public void run() {
					for (; isRunning();) {
						try {
							Socket s = server.accept();
							addClient(s);
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
				}
			});
			serverListener.setDaemon(daemon);
			serverListener.start();
		}
	}

	public void stop() {
		for (int i = 0; i < countClients(); i++) {
			getClient(i).close();
		}
		running = false;
		try {
			server.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public synchronized void addClient(String host, int port) throws UnknownHostException, IOException {
		addClient(new NetworkClient(host, port));
	}
	
	public synchronized void addClient(NetworkClient client) {
		clients.add(client);
		if (listener != null) {
			listener.connected(client);
		}
	}

	public synchronized void addClient(Socket s) {
		addClient(new NetworkClient(s, this));
	}

	public String getName() {
		return name;
	}

	public boolean isRunning() {
		return running;
	}

	public void sendMessage(Session session) {
		sendMessage(NetworkListener.SESSION, session.encode());
	}

	public void sendMessage(LTouch touch) {
		sendMessage(NetworkListener.TOUCH, new String(touch.out()));
	}

	public void sendMessage(LKey key) {
		sendMessage(NetworkListener.KEY, new String(key.out()));
	}

	public void sendMessage(NetworkMessage message) {
		for (int i = 0; i < clients.size(); i++) {
			((NetworkClient) clients.get(i)).sendMessage(message);
		}
	}

	public void sendMessage(String name, String message) {
		NetworkMessage tmpmsg = new NetworkMessage();
		tmpmsg.name = name;
		tmpmsg.message = message;
		sendMessage(tmpmsg);
	}

	public synchronized int countClients() {
		return clients.size();
	}

	public synchronized NetworkClient getClient(int number) {
		return (NetworkClient) clients.get(number);
	}

	public synchronized void removeClient(int number) {
		clients.remove(number);
	}

	public synchronized void removeClient(NetworkClient client) {
		clients.remove(client);
	}

	public ServerSocket getServerSocket() {
		return server;
	}

	public NetworkListener getListener() {
		return listener;
	}

	public void setListener(NetworkListener listener) {
		this.listener = listener;
	}

	public boolean isDaemon() {
		return daemon;
	}

	public void setDaemon(boolean daemon) {
		this.daemon = daemon;
	}

	public void dispose() {
		stop();
	}

}
