/**
 * TorProxy - Anonymous data communication for Android devices
 * Copyright (C) 2009 Connell Gauld
 * 
 * Thanks to University of Cambridge,
 * 		Alastair Beresford and Andrew Rice
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


package uk.ac.cam.cl.dtg.android.tor.TorProxyTests;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import uk.ac.cam.cl.dtg.android.tor.TorProxy.TorProxyControlService;
import uk.ac.cam.cl.dtg.android.tor.TorProxyLib.ITorProxyControl;
import uk.ac.cam.cl.dtg.android.tor.TorProxyLib.SocksProxy;
import uk.ac.cam.cl.dtg.android.tor.TorProxyLib.TorProxyLib;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.test.ServiceTestCase;

public class ControlServiceConnectivityTest extends
		ServiceTestCase<TorProxyControlService> {
	
	private static final String TEST1_HOST = "people.pwf.cam.ac.uk";
	private static final int TEST1_PORT = 80;
	private static final String TEST1_FILE = "/cmg47/tests/test1.txt";
	private static final String TEST1_EXPECTED = "This is test 1.";
	
	private ITorProxyControl mControlService = null;
	private SocksProxy mSocksProxy = null;

	public ControlServiceConnectivityTest() {
		super(TorProxyControlService.class);
	}
	
	public void testHttp() throws Exception {
		Thread.sleep(5000);
		performHttpTest(TEST1_HOST, TEST1_PORT, TEST1_FILE, TEST1_EXPECTED);
	}
	
	public void performHttpTest(String host, int port, String file, String expected) throws Exception {
		Socket s = mSocksProxy.connectSocksProxy(new Socket(), host, port, 0);
		OutputStreamWriter w = new OutputStreamWriter(s.getOutputStream());
		w.write("GET " + file + " HTTP/1.1\n");
		w.write("Host: " + host + "\n");
		w.write("Connection: close\n");
		w.write("\n");
		w.flush();
		
		BufferedReader r = new BufferedReader(new InputStreamReader(s.getInputStream()));
		
		// Move beyond headers
		String nextline = null;
		int contentLength = -1;
		do {
			nextline = r.readLine();
			if (nextline.toLowerCase().startsWith("content-length: ")) {
				String contentLengthStr = nextline.substring("content-length: ".length());
				contentLength = Integer.parseInt(contentLengthStr);
			}
		} while (!"".equals(nextline));
		
		// Read content
		StringBuilder returned = new StringBuilder();
		char[] buffer = new char[1024];
		int read = 0;
		while(true) {
			String currentLine = new String(buffer, 0, read);
			returned.append(currentLine);
			if (contentLength > -1) {
				contentLength -= read;
				if (contentLength == 0) break;
			}
			read = r.read(buffer);
			if (read == -1) break;
		}
		String got = returned.toString().trim();
		assertTrue(got.equals(expected));
	}
	
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		Intent startIntent = new Intent();
        startIntent.setComponent(new ComponentName(TorProxyLib.CONTROL_SERVICE_PACKAGE, TorProxyLib.CONTROL_SERVICE_CLASS));
        IBinder service = bindService(startIntent); 
		
        mControlService = ITorProxyControl.Stub.asInterface(service);
        assertNotNull(mControlService);
        
		mControlService.setProfile(TorProxyLib.PROFILE_OFF);
		waitFor(TorProxyLib.STATUS_UNAVAILABLE, 5000); // 5 seconds
		
		mControlService.setProfile(TorProxyLib.PROFILE_ON);
		waitFor(TorProxyLib.STATUS_CONNECTING, 5000);
		
		int remaining = mControlService.getEstimatedTimeRemaining();
		assertTrue(remaining > 0);
		assertTrue(remaining < 120); // 2 minutes
		int timeout = (int)(remaining * 15.0f) * 1000; // 1.5x
		waitFor(TorProxyLib.STATUS_ON, timeout);
		
		int port = mControlService.getSOCKSPort();
		assertTrue(port > 0);
		assertTrue(port < 65535);
        
		mSocksProxy = new SocksProxy("127.0.0.1", port);
		assertNotNull(mSocksProxy);
		
	}
	
	private void waitFor(int status, int timeout) throws Exception {
		int timeSoFar = 0;
		int currentStatus = -1;
		while(currentStatus != status) {
			
			currentStatus = mControlService.getStatus();
			
			Thread.sleep(200); // 200ms
			timeSoFar += 200;
			if (timeSoFar >= timeout) {
				throw new Exception("Timeout waiting for status");
			}
		}
	}
	
}
