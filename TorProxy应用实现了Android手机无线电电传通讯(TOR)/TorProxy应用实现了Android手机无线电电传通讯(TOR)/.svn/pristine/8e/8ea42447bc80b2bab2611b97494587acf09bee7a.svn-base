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
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import TorJava.Common.StreamsAndHTTP;

/**
 * main class for directory server functionality
 * 
 * @author Lexi Pimenidis
 * @version unstable
 */

class DirectoryServer extends Thread {
    Directory dir;

    HashMap<String,HashMap<String,Object>> serviceDescriptors;

    ServerSocket dir_server;

    /**
     * creates the server socket and installs a dispatcher for incoming data.
     * 
     * @param dir_port
     *            the port to open for directory services
     * @exception IOException
     */
    DirectoryServer(Directory dir, int dir_port) throws IOException {
        if (dir_port < 1)
            throw new IOException("invalid port given");
        if (dir_port > 0xffff)
            throw new IOException("invalid port given");
        this.dir = dir;
        serviceDescriptors = new HashMap<String,HashMap<String,Object>>();

        Logger.logGeneral(Logger.INFO,"DirectoryServer: starting directory server on port " + dir_port);
        dir_server = new ServerSocket(dir_port);
        this.start();
    }

    public void run() {
        try {
            while (true) {
                // receive new connection
                Socket client = dir_server.accept();
                String descr = client.getInetAddress().getHostAddress() + ":" + client.getPort();
                Logger.logDirectory(Logger.VERBOSE,"Incoming connection to directory from " + descr);
                // handle connection
                new DirectoryServerThread(dir, client, serviceDescriptors);
                // close connection
            }
        } catch (IOException e) {
        }
    }

    /**
     * close down the directory server
     */
    void close() {
        Logger.logDirectory(Logger.VERBOSE, "DirectoryServer.close(): Closing directory server");

        // close connections
        try {
            dir_server.close();
        } catch (IOException e) {
        }
    }
}

class DirectoryServerThread extends Thread {
    Directory dir;

    HashMap<String,HashMap<String,Object>> sd;

    Socket client;

    public DirectoryServerThread(Directory dir, Socket client,
            HashMap<String,HashMap<String,Object>> serviceDescriptoren) {
        this.dir = dir;
        this.client = client;
        this.sd = serviceDescriptoren;
        this.start();
    }

    public void run() {
        try {
            // read request
            String query = StreamsAndHTTP.readHTTPFromStream(client.getInputStream());
            PrintStream sout = new PrintStream(client.getOutputStream());
            // parse request
            Pattern p = Pattern.compile(
                    "^(GET|POST) ([^ ]+) HTTP.*?\r?\n\r?\n(.*)", Pattern.DOTALL
                            + Pattern.MULTILINE + Pattern.CASE_INSENSITIVE
                            + Pattern.UNIX_LINES);
            Matcher m = p.matcher(query);
            if (!m.find())
                return;
            String method = m.group(1);
            String uri = m.group(2);
            String body = m.group(3);
            // main location to handle requests
            String answer = null;
            if (method.equals("GET")) {
                if (uri.equals("/")) { // provide a directory listing
                    answer = dir.renderDirectory();
                }
                ;
                if (uri.startsWith("/tor/rendezvous/")) { // send a service
                                                            // descriptor
                    // - extract 'z'
                    String z = uri.substring(16);
                    // - check if stored service descriptor for 'z'
                    if (this.sd.containsKey(z))
                        answer = (String)(((HashMap<String,Object>) this.sd.get(z))
                                .get("original"));
                }
            }
            ;
            if (method.equals("POST")) {
                if (uri.equals("/tor/rendezvous/publish")) { // service
                                                                // descriptor is
                                                                // uploaded
                    try {
                        HashMap<String,Object> sd_ = new HashMap<String,Object>(); // store body
                        sd_.put("original", body);
                        // - parse body to ServiceDescriptor and store in
                        // HashMap
                        ServiceDescriptor serv_desc = new ServiceDescriptor("",
                                body.getBytes(), dir);
                        sd_.put("parsed", serv_desc);
                        // extract z and store sd_ in this.sd
                        String z = serv_desc.getURL();
                        this.sd.put(z, sd_);
                        // success
                        answer = "";
                    } catch (Exception e) {
                        Logger.logDirectory(Logger.WARNING,"strange data format detected or stuff");
                        answer = null;
                    }
                }
                ;
            }
            ;
            // http-status-code
            StringBuffer http;
            if (answer != null) {
                http = new StringBuffer("HTTP/1.0 200 yoo, peace man!\r\n");
            } else {
                http = new StringBuffer("HTTP/1.0 404 no way, man!\r\n");
                answer = "";
            }
            ;
            // build answer
            http.append("Content-Length: " + answer.length() + "\r\n");
            http.append("\r\n");
            http.append(answer);
            // send answer
            sout.print(http.toString());
        } catch (IOException e) {
        } finally {
            try {
                client.close();
            } catch (IOException e) {
            }
        }
    }
}
