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
import uk.ac.cam.cl.dtg.android.tor.TorProxyLib.TorProxyLib;
import android.content.ComponentName;
import android.content.Intent;
import android.test.ServiceTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;

public class ControlServiceConnectionTest extends ServiceTestCase<TorProxyControlService> {

	public ControlServiceConnectionTest() {
		super(TorProxyControlService.class);
	}
	
	public ControlServiceConnectionTest(Class<TorProxyControlService> serviceClass) {
		super(serviceClass);
	}

	@SmallTest
	public void testStartup() {
        Intent startIntent = new Intent();
        startIntent.setComponent(new ComponentName(TorProxyLib.CONTROL_SERVICE_PACKAGE, TorProxyLib.CONTROL_SERVICE_CLASS));
        startService(startIntent); 
	}
	
    @MediumTest
    public void testBindable() {
        Intent startIntent = new Intent();
        startIntent.setComponent(new ComponentName(TorProxyLib.CONTROL_SERVICE_PACKAGE, TorProxyLib.CONTROL_SERVICE_CLASS));
//        IBinder service = 
        	bindService(startIntent); 
    }
}
