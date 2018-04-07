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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.Socket;

import uk.ac.cam.cl.dtg.android.tor.TorProxy.TorProxyControlService;
import TorJava.TCPStream;
import TorJava.TCPStreamProperties;
import TorJava.Tor;
import TorJava.TorKeeper;

class HTTPConnection extends Thread {
	Socket local;

	/** http header for CONNECT-calls */
	private static final String success200 = "HTTP/1.0 200 Connection Established\r\n\r\n";
	/** the error page including header */
	private static final String error500a = "HTTP/1.0 500 Internal Server Error\r\n"
			+ "Content-Type: text/html\r\n\r\n"
			+ "<HTML><HEAD><TITLE>Internal Server Error</TITLE></HEAD>\r\n"
			+ "<BODY><H1>Internal Server Error</H1>\r\n"
			+ "The request failed.<br>\r\n<pre>";
	private static final String error500b = "</pre>\r\n</BODY></HTML>\r\n";
	/** just some $arbitrarily chosen variable ;-) **/
	private static final int payload = 498; // maximum length of Tor-Cell
	// Payload

	private TorProxyControlService torService = null;

	public HTTPConnection(Socket local, TorProxyControlService torService) {
		// start connection
		this.local = local;
		this.torService = torService;
		this.setName("HTTPConnection");
		this.start();
	}

	private String FilterAndReplace(String input) {
		// if (!MainWindow.processHTTPHeaders()) return input+"\r\n";
		try {
			int posColon = input.indexOf(':');
			if (posColon < 0)
				throw new Exception("HTTP Header without colon...");
			String name = input.substring(0, posColon);
			String value = input.substring(posColon + 1);
			// filtered?
			boolean filtered = false;
			for (int i = 0; (i < TorJava.TorConfig.setFilteredHeaders.size())
					&& (!filtered); ++i) {
				String conf = ((String) TorJava.TorConfig.setFilteredHeaders
						.elementAt(i)).toLowerCase();
				int idx = name.toLowerCase().indexOf(conf);
				filtered = ((idx >= 0) && (idx <= 2)); // fuzzy matching
			}
			if (filtered) {
				// System.out.println("Removed header "+name);
				return "";
			}
			// filter out, if client resends cookie that we send to him!
			if (name.equalsIgnoreCase("Cookie")) {
				// value should be something like: Route="TorJava
				int idxTorJava = value.toLowerCase().indexOf("torjava");
				int idxRoute = value.toLowerCase().indexOf("route");
				if ((idxRoute + 6 <= idxTorJava)
						&& (idxRoute + 8 >= idxTorJava)) {
					int nextQM = value.indexOf("\"", idxTorJava);
					System.err.println("filter out: " + idxRoute + " "
							+ idxTorJava + " " + nextQM + " " + value);
					if (nextQM < 0) {
						value = value.substring(0, idxRoute - 1);
					} else {
						try {
							String before = value.substring(0, idxRoute - 1);
							String after = "";
							if (nextQM + 1 < value.length())
								after = value.substring(nextQM + 1);
							value = before + after;
							if (value.startsWith(";"))
								value = value.substring(1);
							if (value.startsWith(" "))
								value = value.substring(1);
						} catch (Exception e) {
							System.err.println(e.getMessage());
							e.printStackTrace();
						}
					}
					System.err.println("filtered out to: " + value);
				}
			}
			// substitute?
			for (int i = 0; i < TorJava.TorConfig.setReplaceHeaders.size(); ++i) {
				String[] replace = (String[]) TorJava.TorConfig.setReplaceHeaders
						.elementAt(i);
				if (name.equalsIgnoreCase(replace[0])) {
					// System.out.println("replacing > "+name+": "+value);
					value = replace[1];
				}
			}
			// System.out.println(input+" > "+name+": "+value);
			return name + ":" + value + "\r\n";
		} catch (Exception e) {
			// if ANY error occurs, just copy the header (i.e. 'error' in this
			// case is also the first line
			// of an http-response... so be careful here!
			// System.out.println("header "+input+" is strange");
			return input + "\r\n";
		}
	}

	public void run() {
		final int PROTOCOL_HTTP = 1;
		final int PROTOCOL_HTTPS = 2;
		TCPStream remoteA = null;
		Socket remoteS = null;
		int protocol = PROTOCOL_HTTP;
		try {
			Tor tor = TorKeeper.getTor();
			// prepare parsing of http-request
			BufferedReader br = new BufferedReader(new InputStreamReader(local
					.getInputStream()));
			StringBuffer request = new StringBuffer(5000);
			String host = null;
			int port = 80;
			int body_length = 0;
			// parsing first line
			String header = br.readLine().trim();
			this.setName("HTTPConnection " + header);
			// System.out.println("Parsing '"+header+"'");
			String[] parts = header.split("[ ]");
			if (parts[0].startsWith("CONNECT")) {
				protocol = PROTOCOL_HTTPS;
				// HTTPS (or whatever else)
				String[] tt = parts[1].split("[:]");
				host = tt[0].trim();
				port = Integer.parseInt(tt[1].trim());
			}
			// HTTP-Protocol
			request.append(parts[0] + " ");
			if (parts[1].startsWith("http://")) {
				String uri_without_protocol = parts[1].substring(7);
				int pos_next_slash = uri_without_protocol.indexOf('/');
				host = uri_without_protocol.substring(0, pos_next_slash - 1);
				if (host.indexOf(':') >= 0) {
					try {
						String[] tt = header.split("[:]");
						host = tt[0].trim();
						port = Integer.parseInt(tt[1].trim());
					} catch (Exception e) {
					}
				}
				request.append(uri_without_protocol.substring(pos_next_slash));
			} else if (protocol != PROTOCOL_HTTPS) {
				throw new Exception("unsupported protocol");
			}
			for (int i = 2; i < parts.length; ++i)
				request.append(" " + parts[i]);
			request.append("\r\n");
			// parse more header of http-request
			header = br.readLine().trim();
			while (header.length() > 0) {
				if (header.startsWith("Host: ")) {
					host = header.substring(5).trim();
					//System.out.println("Overwriting host with "+host+" from '"
					// +header+"'");
					if (host.indexOf(':') >= 0) {
						try {
							String[] tt = host.split("[:]");
							host = tt[0].trim();
							port = Integer.parseInt(tt[1].trim());
						} catch (Exception e) {
						}
					}
				} else if (header.startsWith("Content-Length: ")) {
					try {
						String[] len = header.split("[:]");
						body_length = Integer.parseInt(len[1].trim());
					} catch (Exception e) {
						body_length = 0;
					}
				}
				if (!header.startsWith("Connection:")) {
					//Log.w("HTTPConnection", header);
					request.append(FilterAndReplace(header));
				}
				header = br.readLine().trim();
			}
			request.append("Connection: close\r\n\r\n");
			// read request body
			char[] body = new char[body_length];
			br.read(body, 0, body_length);
			request.append(body);
			// remote connection
			OutputStream remoteOutput;
			InputStream remoteInput;
			String additionalHeader = "";
			if (torService.isTorProcessActive()) {
				//Log.w("HTTPConnection", "Host: " + host + "; Port: " + port);				
				remoteA = tor.connect(new TCPStreamProperties(host, port));
				//Log.w("HTTPConnection", "After Host: " + host + "; Port: " + port);
				remoteOutput = remoteA.getOutputStream();
				remoteInput = remoteA.getInputStream();
				String strRoute = remoteA.getRoute();
				// additionalHeader = "X-Anonymized: TorJava"++"\r\n";
				additionalHeader = "Set-Cookie: Route=\"TorJava" + strRoute
						+ "\" ; Max-Age=300; Discard\r\n";
			} else {
				remoteS = new Socket(host, port);
				remoteOutput = remoteS.getOutputStream();
				remoteInput = remoteS.getInputStream();
			}

			if (protocol == PROTOCOL_HTTP) {
				// write http-request
				DataOutputStream remote_write = new DataOutputStream(
						remoteOutput);
				remote_write.write(request.toString().getBytes());
				remote_write.flush();
				// relay http-response - HEADER
				BufferedWriter local_write = new BufferedWriter(
						new OutputStreamWriter(local.getOutputStream()));
				br = new BufferedReader(new InputStreamReader(remoteInput));
				header = br.readLine().trim();
				while (header.length() > 0) {
					local_write.write(FilterAndReplace(header));
					header = br.readLine().trim();
				}
				// add custom HTTP-Header
				local_write.write(additionalHeader);
				// finish HTTP-header
				local_write.write("\r\n");
				// relay http-response - BODY
				char[] data = new char[payload];
				int length;
				length = br.read(data, 0, payload);
				while (length > 0) {
					local_write.write(data, 0, length);
					length = br.read(data, 0, payload);
				}
				local_write.flush();
			} else if (protocol == PROTOCOL_HTTPS) {
				// send HTTP-success message
				DataOutputStream local_write = new DataOutputStream(local
						.getOutputStream());
				local_write.write(success200.getBytes());
				local_write.flush();
				// relay subsequent data
				SocksConnection.relay(local, new DataInputStream(remoteInput),
						new DataOutputStream(remoteOutput));
			}
		}
		// pretty print error and display to client
		catch (Exception e) {
			try {
				// System.err.println(e);
				// e.printStackTrace();
				DataOutputStream local_write = new DataOutputStream(local
						.getOutputStream());
				local_write.write(error500a.getBytes());
				local_write.flush();
				e.printStackTrace(new PrintStream(local.getOutputStream()));
				local_write.write(error500b.getBytes());
				local_write.flush();
			} catch (Exception e2) {
			}
		} finally {
			try {
				local.close();
			} catch (Exception e) {
			}
			;
			try {
				remoteA.close();
			} catch (Exception e) {
			}
			;
			try {
				remoteS.close();
			} catch (Exception e) {
			}
			;
		}
	}
}
