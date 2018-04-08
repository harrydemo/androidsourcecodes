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

import uk.ac.cam.cl.dtg.android.tor.TorProxy.TorProxyControlService;
import uk.ac.cam.cl.dtg.android.tor.TorProxyLib.ITorProxyControl;
import uk.ac.cam.cl.dtg.android.tor.TorProxyLib.TorProxyLib;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.test.ServiceTestCase;

public class ControlServiceInterfaceTest extends ServiceTestCase<TorProxyControlService> {

	private ITorProxyControl mControlService = null;
	
	public ControlServiceInterfaceTest() {
		super(TorProxyControlService.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		Intent startIntent = new Intent();
        startIntent.setComponent(new ComponentName(TorProxyLib.CONTROL_SERVICE_PACKAGE, TorProxyLib.CONTROL_SERVICE_CLASS));
        IBinder service = bindService(startIntent); 
		
        mControlService = ITorProxyControl.Stub.asInterface(service);
        assertNotNull(mControlService);
        
	}
	
	public void testOnDemandOff() throws Exception {
		
		mControlService.setProfile(TorProxyLib.PROFILE_OFF);
		waitFor(TorProxyLib.STATUS_UNAVAILABLE, 5000); // 5 seconds
		
		mControlService.setProfile(TorProxyLib.PROFILE_ONDEMAND);
		waitFor(TorProxyLib.STATUS_REQUIRES_DEMAND, 5000);
		
		mControlService.registerDemand();
		waitFor(TorProxyLib.STATUS_CONNECTING, 5000);
		
		int remaining = mControlService.getEstimatedTimeRemaining();
		assertTrue(remaining > 0);
		assertTrue(remaining < 120); // 2 minutes
		int timeout = (int)(remaining * 1.5f) * 1000; // 1.5x
		waitFor(TorProxyLib.STATUS_ON, timeout);
		
		mControlService.setProfile(TorProxyLib.PROFILE_OFF);
		waitFor(TorProxyLib.STATUS_UNAVAILABLE, 5000); // 5 seconds
	}
	
	public void testSwitchOnOff() throws Exception {
		
		mControlService.setProfile(TorProxyLib.PROFILE_OFF);
		waitFor(TorProxyLib.STATUS_UNAVAILABLE, 5000); // 5 seconds
		
		mControlService.setProfile(TorProxyLib.PROFILE_ON);
		waitFor(TorProxyLib.STATUS_CONNECTING, 5000);
		
		int remaining = mControlService.getEstimatedTimeRemaining();
		assertTrue(remaining > 0);
		assertTrue(remaining < 120); // 2 minutes
		int timeout = (int)(remaining * 1.5f) * 1000; // 1.5x
		waitFor(TorProxyLib.STATUS_ON, timeout);
		
		mControlService.setProfile(TorProxyLib.PROFILE_OFF);
		waitFor(TorProxyLib.STATUS_UNAVAILABLE, 5000); // 5 seconds
	}
	
	public void testSocksPort() throws Exception {
		
		mControlService.setProfile(TorProxyLib.PROFILE_OFF);
		waitFor(TorProxyLib.STATUS_UNAVAILABLE, 5000); // 5 seconds
		
		mControlService.setProfile(TorProxyLib.PROFILE_ON);
		waitFor(TorProxyLib.STATUS_CONNECTING, 5000);
		
		int remaining = mControlService.getEstimatedTimeRemaining();
		assertTrue(remaining > 0);
		assertTrue(remaining < 120); // 2 minutes
		int timeout = (int)(remaining * 1.5f) * 1000; // 1.5x
		waitFor(TorProxyLib.STATUS_ON, timeout);
		
		int port = mControlService.getSOCKSPort();
		assertTrue(port > 0);
		assertTrue(port < 65535);
		
		mControlService.setProfile(TorProxyLib.PROFILE_OFF);
		waitFor(TorProxyLib.STATUS_UNAVAILABLE, 5000); // 5 seconds
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
