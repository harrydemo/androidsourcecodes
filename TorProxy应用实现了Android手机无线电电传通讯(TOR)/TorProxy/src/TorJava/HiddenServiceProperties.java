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
import java.util.HashSet;

import TorJava.Common.TorException;

/**
 * contains all properties for a hidden service
 */
public class HiddenServiceProperties {
	
	public static final int STATUS_STARTED = 0;
	public static final int STATUS_INTROPOINTS = 1;
	public static final int STATUS_ADVERTISED = 2;
	public static final int STATUS_TOTAL = 3;
	
	private boolean status[] = new boolean[STATUS_TOTAL];
	
	private HiddenServiceKeySet keys;
	public HiddenServiceRequestHandler handler;
	private OnHiddenServiceStatusChange onChange;

	int port;

	HashSet<IntroductionPoint> introPoints;
	int minimum_number_of_intro_points;

	ServiceDescriptor sd_v0;
	

	/** constructor for first initialization or use-once-services */
	public HiddenServiceProperties(int port, HiddenServiceRequestHandler handler, OnHiddenServiceStatusChange onChange)
			throws TorException {
		this.keys = new HiddenServiceKeySet();
		this.onChange = onChange;
		init(port, handler, new HashSet<IntroductionPoint>());
	}


	public HiddenServiceProperties(int port,
			HiddenServiceRequestHandler handler, HiddenServiceKeySet keys, OnHiddenServiceStatusChange onChange) throws TorException {
		this.keys = keys;
		this.onChange = onChange;
		init(port, handler, new HashSet<IntroductionPoint>());
	}


	/** initializes hidden service and service descriptor */
	private void init(int port, HiddenServiceRequestHandler handler,
			HashSet<IntroductionPoint> introPoints) throws TorException {
		this.port = port;
		this.handler = handler;
		this.introPoints = introPoints;
		minimum_number_of_intro_points = 3;
		
		sd_v0 = new ServiceDescriptor(0, keys.pub, keys.priv, introPoints);
		// precalc-hash of public key
		

	}

	/** add another intro point */
	void addIntroPoint(String server, Directory dir) throws TorException {
		Logger.logHiddenService(Logger.RAW_DATA,
				"HiddenServiceProperties.addIntroPoint: adding " + server);
		introPoints.add(new IntroductionPoint(server, dir));
		// generate new service descriptor
		sd_v0 = new ServiceDescriptor(0, keys.pub, keys.priv, introPoints);
	}

	/** constructor for saved configuration of hidden services */
	public HiddenServiceProperties(String filename,
			HiddenServiceRequestHandler handler) throws IOException {
		// FIXME: implement
		throw new IOException("not implemented yet");
	}

	/**
	 * returns the base32-encoded url of the service
	 */
	public String getName() {
		return sd_v0.getURL();
	}
	
	public HiddenServiceKeySet getKeys() {
		return keys;
	}
	
	/** writes all informations to a file */
	void writeToFile(String filename) throws IOException {
		// FIXME: implement
		throw new IOException("not implemented yet");
	}
	
	/**
	 * Switch this hidden service handler to the specified one
	 * @param newHandler
	 */
	public void reassignHandler(HiddenServiceRequestHandler newHandler) {
		HiddenServiceRequestHandler old = handler;
		handler = newHandler;
		old.close();
	}
	
	/**
	 * get the current status of this hidden service startup
	 * @return percentage started
	 */
	public int getStatusPercent() {
		int totalSoFar = 0;
		int increment = (int)(((float)100) / STATUS_TOTAL);
		
		for (int i=0; i<STATUS_TOTAL; i++) {
			if (status[i]) totalSoFar += increment;
		}

		// Integer rounding fix:
		if (totalSoFar > 97) totalSoFar = 100;
		
		return totalSoFar;
	}
	
	/**
	 * update the status of a particular element of hidden service
	 * startup
	 * @param element the status element that has changed
	 * @param complete
	 */
	public void setStatus(int element, boolean complete) {
		status[element] = complete;
		if (onChange != null) {
			onChange.onHiddenServiceStatusChange(getName(), getStatusPercent());
		}
	}
}
