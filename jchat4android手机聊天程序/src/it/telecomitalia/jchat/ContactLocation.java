/*****************************************************************
 jChat is a  chat application for Android based on JADE
  Copyright (C) 2008 Telecomitalia S.p.A. 
 
 GNU Lesser General Public License

 This is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation, 
 version 2.1 of the License. 

 This software is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this software; if not, write to the
 Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 Boston, MA  02111-1307, USA.
 *****************************************************************/

package it.telecomitalia.jchat;

import android.location.Location;

/**
 * Specialization of {@link Location} that also contains a flag that checks if the location has moved
 * <p> 
 * Used by the agent to store all modifications to the contacts lists that should be done also to the GUI
 * <p>
 * This object is immutable by design.
 * 
 * @author Cristina Cucè
 * @author Marco Ughetti 
 * @author Stefano Semeria
 * @author Tiziana Trucco
 * @version 1.0 
 */
public class ContactLocation extends Location {

	/**
	 * true if contact location has changed, false otherwise
	 */
	private boolean hasMoved;

	/**
	 * Instantiates a new empty contact location.
	 * Each component is initialized to positive infinity
	 * @param providerName name of the location provider (gps/network)
	 */
	public ContactLocation(String providerName){
		super(providerName);
		hasMoved = false;
		setLatitude(Double.POSITIVE_INFINITY);
		setLongitude(Double.POSITIVE_INFINITY);
		setAltitude(Double.POSITIVE_INFINITY);
	
	}
	
	/**
	 * Creates a new copy of contact location.
	 * 
	 * @param toBeCopied the to be copied
	 */
	public ContactLocation( ContactLocation toBeCopied){
		this(toBeCopied, toBeCopied.hasMoved);
	}
	
	/**
	 * Instantiates a new contact location given location and boolean flag
	 * 
	 * @param loc the contact location
	 * @param moved the initial value of the boolean flag
	 */
	private ContactLocation(Location loc, boolean moved){
		super(loc);
		hasMoved = moved;
	}
	
	/**
	 * Changes the location of this contact and sets its internal state to moving
	 * 
	 * @param loc the new contact location
	 */
	public void changeLocation(Location loc)
	{   this.hasMoved = !this.equals(loc);
		if(hasMoved){
		    setLatitude(loc.getLatitude());
		    setLongitude(loc.getLongitude());
		    setAltitude(loc.getAltitude());
	    }
	}
	
	/**
	 * Checks if the {@link ContactLocation} is changed.
	 * 
	 * @return true, if successful
	 */
	public boolean hasMoved(){
		return hasMoved;
	}
	
	/**
	 * Two Contact location are the same if they match their altitude, longitude and latitude
	 * @param l location we have to compare
	 * @return true if equals, false otherwise
	 */
	public boolean equals(Location l){
		return ((this.getAltitude()==l.getAltitude()) && (this.getLatitude()==l.getLatitude()) && (this.getLongitude()==l.getLongitude()));
	}
	
}
