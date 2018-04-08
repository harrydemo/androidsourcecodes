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
package TorJava.Common;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import TorJava.TCPStream;

/**
 * this class contains general utility functions
 * 
 * @author Lexi Pimenidis
 * @author Andriy Panchenko
 * @author Michael Koellejan
 * @author Connell Gauld
 */
public class StreamsAndHTTP {
    /**
     * read http-request/response from input stream
     */
    public static String readHTTPFromStream(InputStream in) {
        // DataInputStream.readLine() is depreacated
        BufferedReader sin = new BufferedReader(new InputStreamReader(in), 16384);
       
        StringBuffer header = new StringBuffer();
        StringBuffer body = new StringBuffer();
        try {
            // read header
            String str = sin.readLine();
            while ((str != null) && (str.length() > 0)) {
                header.append(str + "\n");
                str = sin.readLine();
            }
            ;
            header.append("\n");
            // check length of body
            str = header.toString();
            if (str.startsWith("HTTP/") || str.startsWith("POST ")) {
                int content_length_start = str.indexOf("Content-Length: ") + 16;
                int content_length_end = str.indexOf("\n", content_length_start);
                // System.out.println("Parsing HTTP:\n"+str+"Start
                // "+content_length_start+" End "+content_length_end);
                // parse body only, if legitimate HTTP
                if (content_length_start >= 16) {
                    int content_length = Integer.parseInt(str.substring(
                            content_length_start, content_length_end));
                    // System.out.println("Length "+content_length);
                    // read body
                    char[] body_bytes = new char[content_length];
                    int filled = 0;
									  int t=0;
                    do {
                        t = sin.read(body_bytes, filled, content_length - filled);
												filled += t;
                    } while ((filled < content_length)&&(t>=0)); // until buffer filled or EOF
										body.append(body_bytes);
								} else {
									// if no content-length is given, just read all
									try{
										char[] body_bytes = new char[512];
										int filled = 0;
										do {
											filled = sin.read(body_bytes, 0, body_bytes.length);
											if(filled>0) body.append(body_bytes,0,filled);
										} while (filled>=0);   // detect EOF
									} catch (IOException e) { /* eof, reset, ... */
									}
               }
						}
        } catch (IOException e) { /* eof, reset, ... */
        } catch (NumberFormatException e) { /*
                                             * throwed by Integer.parseInt() in
                                             * case some practical prank is
                                             * played on us
                                             */
        }
        return header.toString() + body.toString();
    }

    /**
     * reads all data from an inputstream
     */
    public static String readAllFromStream(InputStream in) {

        // DataInputStream.readLine() is depreacated
        BufferedReader sin = new BufferedReader(new InputStreamReader(in), 16384);
        
        StringBuffer buf = new StringBuffer();
        try {
            String str = sin.readLine();
            while (str != null) {
                buf.append(str);
                buf.append("\n");
                str = sin.readLine();
            }
            ;
        } catch (IOException e) { /* eof, reset, ... */
        }
        return buf.toString();
    }

    /** does a HTTP-request */
    public static String HTTPRequest(OutputStream out, InputStream in, String req) {
        PrintStream sout = new PrintStream(out);
        sout.print(req);
				sout.flush();
        String data = readHTTPFromStream(in);
        sout.close();
        return data;
    }

    /** does a HTTP-request */
		public static String HTTPRequest(String host, int port, String req) 
			throws UnknownHostException,IOException
		{
			Socket s = new Socket(host,port);
			String response = HTTPRequest(s.getOutputStream(),s.getInputStream(),req);
			s.close();
			return response;
		}

    /** does a HTTP-request, timeout is in milliseconds? */
		public static String HTTPRequest(String host, int port, String req, int timeout) 
			throws UnknownHostException,IOException
		{
			Socket s = new Socket();
			s.connect(new InetSocketAddress(host,port),timeout);
			String response = HTTPRequest(s.getOutputStream(),s.getInputStream(),req);
			s.close();
			return response;
		}

    /**
     * gets service descriptor of the hidden service
     */
    public static byte[] retrieveServiceDescriptor(TCPStream stream, String z)
            throws IOException {
        byte[] answer = BinaryHTTPRequest(stream.getOutputStream(), stream
                .getInputStream(), "GET /tor/rendezvous/" + z
                + " HTTP/1.0\r\n\r\n");
        stream.close();
        return answer;
    }
    
    /** does a HTTP-request */
    public static String HTTPBinaryRequest(OutputStream out, InputStream in, byte[] req) throws IOException {
        out.write(req);
        out.flush();
        String data = readHTTPFromStream(in);
        return data;
    }
    
    
    /// ADDED TO FIX HIDDEN SERVICES
    /**
     * Read all of the data available
     * @param in the input to read from
     * @return the data read
     */
    public static byte[] binaryReadHTTPFromStream(InputStream in) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
                        // Read all in
                        int read = 0;
                        byte[] buffer = new byte[1024];
                        while(read > -1) {
                                out.write(buffer, 0, read);
                                read = in.read(buffer);
                        }
                        return out.toByteArray();
        } catch (IOException e) {
                // Just return an empty array
                return new byte[0];
        }
    }

    /**
     * Performs an HTTP request and returns binary data (including headers)
     * @param out Outgoing stream
     * @param in Incoming stream
     * @param req The HTTP request line (e.g. "GET ...")
     * @return the data received (including headers)
     */
    public static byte[] BinaryHTTPRequest(OutputStream out, InputStream in, String req) {
        PrintStream sout = new PrintStream(out);
        sout.print(req);
                sout.flush();
        byte[] data = binaryReadHTTPFromStream(in);
        sout.close();
        return data;
    }


}
