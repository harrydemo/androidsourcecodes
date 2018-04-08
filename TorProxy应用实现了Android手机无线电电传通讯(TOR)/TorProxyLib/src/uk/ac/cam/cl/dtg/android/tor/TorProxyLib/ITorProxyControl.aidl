/**
 * TorProxyLib - Anonymous data communication for Android devices
 * 			   - Tools for application developers
 * Copyright (C) 2009 Connell Gauld
 * 
 *  Thanks to University of Cambridge,
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

package uk.ac.cam.cl.dtg.android.tor.TorProxyLib;

import uk.ac.cam.cl.dtg.android.tor.TorProxyLib.HiddenServiceDescriptor;

interface ITorProxyControl {

    int getStartProgress();
    int getSOCKSPort();
    int getHTTPPort();
    
    void setProfile(in int profile);
    
    
    int getProfile();
    int getStatus();
    int getEstimatedTimeRemaining();
    
    void registerDemand();
    
    HiddenServiceDescriptor createHiddenService();
    boolean startHiddenService(in int port, in HiddenServiceDescriptor h);
    int getHiddenServiceStatusPercent(String url);
}