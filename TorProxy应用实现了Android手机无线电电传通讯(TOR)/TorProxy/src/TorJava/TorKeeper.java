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

import java.io.File;
import java.io.IOException;

import javax.net.SocketFactory;

/**
 * this class holds the single instantiation of the Tor-client. This is too
 * avoid that multiple instances are started and thus ressources are wasted and
 * QoS goes down.
 * 
 * CG - 07/07/09 - Altered to allow closedown of Tor
 * 
 * @author Lexi Pimenidis
 * @author Connell Gauld
 * @version unstable
 */
public class TorKeeper {
	
	public final static int CONSENSUS_PARSED = 0;
	public final static int DESCRIPTORS_FETCHED = 1;
	public final static int CIRCUITS_AVAILABLE = 2;
	private final static int NUM_PROGRESS_PARTS = 3;
	
	private static boolean complete[] = {false, false, false}; 
	
	private static Tor tor;
	private static TorSocketFactory socketFactory;
	private static String configFile;
	private static String configPath = "";
	private static TorStatusChange controller = null;
	
	/**
	 * use tor with all defaults (not a bad choice!)
	 */
	TorKeeper() {
		configFile = null;
		tor = null;
	}
	
	public static void setTorStatusChange(TorStatusChange controller) {
		TorKeeper.controller = controller;
	}

	/**
	 * use a specific configuration file for Tor
	 * 
	 * @param configFile
	 *            points to a torrc-file
	 */
	TorKeeper(String configFile) {
		TorKeeper.configFile = configFile;
		tor = null;
	}

	public static synchronized Tor getTor() throws IOException {
		return getTor(false);
	}
	
	public static synchronized Tor getTor(String filedir, boolean fastStartup) throws IOException {
		configPath = filedir;
		return getTor(fastStartup, fastStartup);
	}
	
	/**
	 * returns the single instance of the Tor-client. If not already
	 * instantiated, do so.
	 */
	public static synchronized Tor getTor(boolean fastStartup) throws IOException {
		if (tor == null) {
			if (configFile == null)
					tor = new Tor(fastStartup);
			else {
				tor = new Tor(configFile, fastStartup);	
				//Debug.startMethodTracing("torproxy");
			}
		}
		;
		return tor;
	}

	public static synchronized Tor getTor(boolean noLocalFileSystemAccess, boolean fastStartup)
			throws IOException {
		if (tor == null) {
			tor = new Tor(noLocalFileSystemAccess, fastStartup);
		}
		;
		return tor;
	}

	
	public static synchronized void setComplete(int component, boolean isComplete) {
		complete[component] = isComplete;
		if (controller != null) controller.statusChanged();
	}

	public static synchronized int getProgressPerCent() {
		int p = 0;
		int inc = (100 / NUM_PROGRESS_PARTS);
		
		for (int i=0; i<NUM_PROGRESS_PARTS; i++) {
			if (complete[i]) p += inc;
		}
		return p;
	}

	public static synchronized boolean isTorReady() {
		boolean ready = true;
		for (int i=0; i<NUM_PROGRESS_PARTS; i++) {
			if (complete[i] == false) ready = false;
		}
		return ready;
	}
	
	public static synchronized void closeTor() {
		if (tor != null) {
			tor.close();
			tor = null;
			for (int i=0; i<NUM_PROGRESS_PARTS; i++) {
				complete[i] = false;
			}
		}
	}
	
	public static String getConfigPath() {
		return configPath;
	}

	/**
	 * returns the single instance of the TorSocketFactory. If not already
	 * instantiated, do so.
	 */
	public static synchronized SocketFactory getSocketFactory()
			throws IOException {
		if (tor == null)
			getTor(false);
		if (socketFactory == null)
			socketFactory = new TorSocketFactory(tor);
		return (SocketFactory) socketFactory;
	}
	
	public static int getStartupTimeEstimate(boolean fastStart) {
		if (!fastStart) return 60;
		
		int estimate = 34;
		File consensusCache = new File(TorConfig.getConfigDir() + Directory.CONSENSUS_CACHE_FILENAME);
		File descriptorCache = new File(TorConfig.getConfigDir() + DescriptorCache.DESCRIPTOR_CACHE_FILENAME);
		File keyCache = new File(TorConfig.getConfigDir() + PrivateKeyHandler.CACHE_FILENAME);
		
		if (consensusCache.exists()) {
			if (!descriptorCache.exists()) estimate += 10;
		} else {
			estimate += 20;
		}
		
		if (!keyCache.exists()) estimate += 6;
		
		return estimate;
	}
}
