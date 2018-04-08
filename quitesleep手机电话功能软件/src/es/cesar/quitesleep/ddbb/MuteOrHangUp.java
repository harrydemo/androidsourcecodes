/*  
 	Copyright 2011 Cesar Valiente Gordo
 
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

public class MuteOrHangUp extends Id {
	
	private boolean mute = false;
	private boolean hangUp = false;
	
	//Getters and Setters
	public boolean isMute() {
		return mute;
	}
	public void setMute(boolean mute) {		
		this.mute = mute;		
		this.hangUp = !mute;
	}
	public boolean isHangUp() {
		return hangUp;
	}
	public void setHangUp(boolean hangUp) {
		this.hangUp = hangUp;
		this.mute = !hangUp;
	}
	
	/**
	 * Empty constructor
	 */
	public MuteOrHangUp () {
		super();
		this.mute = true;
		this.hangUp = false;		
	}
	
	/**
	 * 
	 * @param mute
	 * @param hangUp
	 */
	public MuteOrHangUp (boolean mute, boolean hangUp) {
		super();
		this.mute = mute;
		this.hangUp = hangUp;
	}
	
	@Override
	public String toString () {		
		return "Mute: " + mute + "\tHang Up: " + hangUp;
	}
	

}
