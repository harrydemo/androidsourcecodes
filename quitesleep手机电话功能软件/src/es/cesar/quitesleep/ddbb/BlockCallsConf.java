/*
 	Copyright 2010 Cesar Valiente Gordo
 
 	This file is part of QuiteSleep.

    QuiteSleep is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    QuiteSleep is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with QuiteSleep.  If not, see <http://www.gnu.org/licenses/>.
*/

package es.cesar.quitesleep.ddbb;

/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 * 
 * This class configure and set the block settings.
 *
 */
public class BlockCallsConf extends Id {
	
	private final String CLASS_NAME = this.getClass().getName();
	
	//Block options
	private boolean blockAll = false;
	private boolean blockBlockedContacts = false;		
	private boolean blockUnknown = false;
	private boolean blockUnknownAndBlockedContacts = false;
	
	//int constants regarding to the options
	private final int BLOCK_ALL  = 1;
	private final int BLOCK_BLOCKED_CONTACTS = 2;
	private final int BLOCK_UNKNOWN = 3;
	private final int BLOCK_UNKNOWN_AND_BLOCKED_CONTACTS = 4;
	
	
	//--------------	 Getters & Setters 	----------------------------------//
	public boolean isBlockAll() {
		return blockAll;
	}

	public void setBlockAll(boolean blockAll) {
		this.blockAll = blockAll;
		
		if (blockAll) {
			//Setting the rest to false			
			this.blockBlockedContacts = false;
			this.blockUnknown = false;
			this.blockUnknownAndBlockedContacts = false;
		}		
	}

	public boolean isBlockBlockedContacts() {
		return blockBlockedContacts;
	}

	public void setBlockBlockedContacts(boolean blockBlockedContacts) {
		
		this.blockBlockedContacts = blockBlockedContacts;
		
		if (blockBlockedContacts) {
			//Setting the rest to false
			this.blockAll = false;			
			this.blockUnknown = false;
			this.blockUnknownAndBlockedContacts = false;
		}
	}

	
	public boolean isBlockUnknown() {
		return blockUnknown;
	}

	public void setBlockUnknown(boolean blockUnknown) {
		
		this.blockUnknown = blockUnknown;
		
		if (blockUnknown) {
			//Setting the rest to false
			this.blockAll = false;	
			this.blockBlockedContacts = false;
			this.blockUnknownAndBlockedContacts = false;
		}
	}

	public boolean isBlockUnknownAndBlockedContacts() {
		return blockUnknownAndBlockedContacts;
	}

	public void setBlockUnknownAndBlockedContacts(
			boolean blockUnknownAndBlockedContacts) {
	
		this.blockUnknownAndBlockedContacts = blockUnknownAndBlockedContacts;
		
		if (blockUnknownAndBlockedContacts) {
			//Setting the rest to false
			this.blockAll = false;			
			this.blockBlockedContacts = false;
			this.blockUnknown = false;
		}
	}	
	//------------------------------------------------------------------------//
	
	/**
	 * Constructor without parameters. By default the option which block
	 * only to the blocked contacts is established
	 */
	public BlockCallsConf () {
		super();
		this.blockAll = false;
		this.blockBlockedContacts = true;	
		this.blockUnknown = false;
		this.blockUnknownAndBlockedContacts = false;
	}
	
	
	/**
	 * Constructor with all blocked options
	 * 
	 * @param blockAll
	 * @param blockBlockedContacts
	 * @param blockUnknown
	 * @param blockUnknownAndBlockedContacts
	 */
	public BlockCallsConf (
			boolean blockAll,
			boolean blockBlockedContacts,
			boolean blockUnknown,
			boolean blockUnknownAndBlockedContacts) {
		
		super();
		this.blockAll = blockAll;
		this.blockBlockedContacts = blockBlockedContacts;		
		this.blockUnknown = blockUnknown;
		this.blockUnknownAndBlockedContacts = blockUnknownAndBlockedContacts;
	}
	

	/**
	 * This function returns the integer which corresponds with the
	 * option established, or 0 if none option is set.
	 * @return
	 */
	public int whatIsInUse () {
		
		if (blockAll)
			return BLOCK_ALL;		
		else if (blockBlockedContacts)
			return BLOCK_BLOCKED_CONTACTS;
		else if (blockUnknown) 
			return BLOCK_UNKNOWN;
		else if (blockUnknownAndBlockedContacts)
			return BLOCK_UNKNOWN_AND_BLOCKED_CONTACTS;
		else
			return 0;
	}
	/**
	 * @return String
	 */
	public String toString () {
		
		return "BlockAll: " + blockAll + "\nBlockBlockedContacts: " + blockBlockedContacts +		
		"\nBlockUnknown: " + blockUnknown + 
		"\nBlockUnknownAndBlockedContacts: " + blockUnknownAndBlockedContacts;
	}

}
