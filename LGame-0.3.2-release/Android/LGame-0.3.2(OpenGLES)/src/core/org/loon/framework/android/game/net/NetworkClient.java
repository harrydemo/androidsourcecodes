package org.loon.framework.android.game.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
 * 0.3.2版新增类，用以进行简单的游戏客户端数据处理
 */
public class NetworkClient implements LRelease {

	private static String delimiter = "&";

	private static boolean hiPerformance = false;

	private ArrayList<NetworkMessage> messageIn;

	private ArrayList<NetworkMessage> messageOut;

	private Socket client;

	private NetworkServer server;

	private Thread threadListen;

	private Thread threadSender;

	private NetworkListener listener;

	private boolean isRunning, daemon;

	private int pingtime;

	public NetworkClient(String host, int port) throws UnknownHostException,
			IOException {
		this.isRunning = true;
		this.daemon = false;
		this.messageIn = new ArrayList<NetworkMessage>();
		this.messageOut = new ArrayList<NetworkMessage>();
		this.pingtime = -1;
		this.client = new Socket(host, port);
		this.startClient();
	}

	public NetworkClient(Socket s, NetworkServer server) {
		this.isRunning = true;
		this.daemon = true;
		this.messageIn = new ArrayList<NetworkMessage>();
		this.messageOut = new ArrayList<NetworkMessage>();
		this.pingtime = -1;
		this.client = s;
		this.server = server;
		this.startClient();
	}

	private void startClient() {
		if (threadListen == null) {

			threadListen = new Thread(new Runnable() {

				BufferedReader in;

				String line;

				NetworkMessage tmpmsg;

				public void run() {
					try {
						in = new BufferedReader(new InputStreamReader(client
								.getInputStream()));
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					while (isRunning) {
						line = null;
						try {
							line = in.readLine();
							String splits[] = line.split(NetworkClient
									.getDelimiter());
							tmpmsg = new NetworkMessage();
							tmpmsg.name = splits[0];
							tmpmsg.message = splits[1];
							if (tmpmsg.name.startsWith(NetworkListener.FLAG)) {
								if (tmpmsg.name.equals(NetworkListener.PING)) {
									tmpmsg.name = NetworkListener.REPING;
									sendMessage(tmpmsg);
								}
								if (tmpmsg.name.equals(NetworkListener.REPING)) {
									pingtime = (int) (System.nanoTime() - Long
											.parseLong(tmpmsg.message) / 1000000);
								}
							} else {
								addInMessage(tmpmsg);
							}
						} catch (Exception ex) {
							close();
						}
						if (!NetworkClient.hiPerformance) {
							try {
								Thread.sleep(10L);
							} catch (InterruptedException ex) {
							}
						} else {
							int i = 0;
							while (i < 10) {
								Thread.yield();
								i++;
							}
						}
					}
				}

			});
			threadListen.setDaemon(daemon);
			threadListen.start();
		}
		if (threadSender == null) {
			threadSender = new Thread(new Runnable() {

				BufferedWriter out;

				NetworkMessage tmpmsg;

				public void run() {
					try {
						out = new BufferedWriter(new OutputStreamWriter(client
								.getOutputStream()));
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					while (isRunning) {
						if (messageOut.size() > 0) {
							tmpmsg = getNextOutMessage();
							try {
								out.write((new StringBuilder()).append(
										tmpmsg.name).append(
										NetworkClient.getDelimiter()).append(
										tmpmsg.message).toString());
								out.newLine();
								out.flush();
							} catch (IOException ex) {
								close();
							}
						}
						if (!NetworkClient.hiPerformance) {
							try {
								Thread.sleep(10L);
							} catch (InterruptedException ex) {
							}
						} else {
							int i = 0;
							while (i < 10) {
								Thread.yield();
								i++;
							}
						}
					}
				}

			});
			threadSender.setDaemon(daemon);
			threadSender.start();
		}
	}

	private synchronized void addInMessage(NetworkMessage message) {
		messageIn.add(message);
		if (server == null) {
			if (listener != null) {
				listener.getMessage(this);
			}
		} else if (server.getListener() != null) {
			server.getListener().getMessage(this);
		}
	}

	private synchronized void addOutMessage(NetworkMessage message) {
		messageOut.add(message);
	}

	private synchronized NetworkMessage getNextOutMessage() {
		if (messageOut.size() > 0) {
			return (NetworkMessage) messageOut.remove(0);
		} else {
			return null;
		}
	}

	private synchronized NetworkMessage getNextInMessage() {
		if (messageIn.size() > 0) {
			return (NetworkMessage) messageIn.remove(0);
		} else {
			return null;
		}
	}

	public synchronized ArrayList<Session> findSession() {
		ArrayList<NetworkMessage> messages = findMessage(NetworkListener.SESSION);
		if (messages != null) {
			ArrayList<Session> result = new ArrayList<Session>(messages.size());
			for (NetworkMessage mes : messages) {
				if (mes.message != null) {
					result.add(Session.loadStringSession(mes.message));
				}
			}
			return result;
		}
		return null;
	}

	public synchronized ArrayList<LKey> findKey() {
		ArrayList<NetworkMessage> messages = findMessage(NetworkListener.KEY);
		if (messages != null) {
			ArrayList<LKey> result = new ArrayList<LKey>(messages.size());
			for (NetworkMessage mes : messages) {
				if (mes.message != null) {
					result.add(new LKey(mes.message.getBytes()));
				}
			}
			return result;
		}
		return null;
	}

	public synchronized ArrayList<LTouch> findTouch() {
		ArrayList<NetworkMessage> messages = findMessage(NetworkListener.TOUCH);
		if (messages != null) {
			ArrayList<LTouch> result = new ArrayList<LTouch>(messages.size());
			for (NetworkMessage mes : messages) {
				if (mes.message != null) {
					result.add(new LTouch(mes.message.getBytes()));
				}
			}
			return result;
		}
		return null;
	}

	public synchronized ArrayList<NetworkMessage> findMessage(String name) {
		if (name == null) {
			return null;
		}
		if (messageIn != null) {
			ArrayList<NetworkMessage> messages = new ArrayList<NetworkMessage>(
					10);
			for (NetworkMessage mes : messageIn) {
				if (mes.name.equals(name)) {
					messages.add(mes);
				}
			}
			return messages;
		} else {
			return null;
		}
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
		addOutMessage(message);
	}

	public void sendMessage(String name, String message) {
		NetworkMessage tmpmsg = new NetworkMessage();
		tmpmsg.name = name;
		tmpmsg.message = message;
		sendMessage(tmpmsg);
	}

	public NetworkMessage getNextMessage() {
		return getNextInMessage();
	}

	public Socket getSocket() {
		return client;
	}

	public void close() {
		if (!client.isClosed())
			try {
				if (server == null) {
					if (listener != null)
						listener.discconnected(this);
				} else {
					server.removeClient(this);
					if (server.getListener() != null) {
						server.getListener().discconnected(this);
					}
				}
				client.close();
				isRunning = false;
			} catch (IOException ex) {
			}
	}

	public static String getDelimiter() {
		return delimiter;
	}

	public static void setDelimiter(String aDelimiter) {
		delimiter = aDelimiter;
	}

	public NetworkListener getListener() {
		return listener;
	}

	public void setListener(NetworkListener listener) {
		this.listener = listener;
	}

	public void ping() {
		sendMessage(NetworkListener.PING, Long.toString(System.nanoTime()));
	}

	public boolean isDaemon() {
		return daemon;
	}

	public void setDaemon(boolean daemon) {
		this.daemon = daemon;
	}

	public int getPingtime() {
		return pingtime;
	}

	public static void useHiPerformance(boolean b) {
		hiPerformance = b;
	}

	public void dispose() {
		close();
	}

}
