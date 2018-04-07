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

import java.text.DateFormat;
import java.util.Date;

/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class Schedule extends Id {
	
	//---	Time in Calendar format -----//
	private Date startTime;
	private Date endTime;
	
	
	//----	Time formatted	----------------//
	private String startFormatTime;
	private String endFormatTime;
	
	
	//----------		Week days	------------------//
	private boolean monday = false;
	private boolean tuesday = false;
	private boolean wednesday = false;
	private boolean thursday = false;
	private boolean friday = false;
	private boolean saturday = false;
	private boolean sunday = false;
	//--------------------------------------------------//
	
	
	//------	Getters & Setters		--------------//
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getStartFormatTime() {
		return startFormatTime;
	}
	public void setStartFormatTime(String startFormatTime) {
		this.startFormatTime = startFormatTime;
	}

	public String getEndFormatTime() {
		return endFormatTime;
	}
	public void setEndFormatTime(String endFormatTime) {
		this.endFormatTime = endFormatTime;
	}
	
	//----------------		Week days 		---------------------------//
	public boolean isMonday() {
		return monday;
	}
	public void setMonday(boolean monday) {
		this.monday = monday;
	}
	
	public boolean isTuesday() {
		return tuesday;
	}
	public void setTuesday(boolean tuesday) {
		this.tuesday = tuesday;
	}
	
	public boolean isWednesday() {
		return wednesday;
	}
	public void setWednesday(boolean wednesday) {
		this.wednesday = wednesday;
	}
	
	public boolean isThursday() {
		return thursday;
	}
	public void setThursday(boolean thursday) {
		this.thursday = thursday;
	}
	
	public boolean isFriday() {
		return friday;
	}
	public void setFriday(boolean friday) {
		this.friday = friday;
	}
	
	public boolean isSaturday() {
		return saturday;
	}
	public void setSaturday(boolean saturday) {
		this.saturday = saturday;
	}
	
	public boolean isSunday() {
		return sunday;
	}
	public void setSunday(boolean sunday) {
		this.sunday = sunday;
	}
	
	//-------------		StartTimes		----------------------------------//
	public void setAllStartTime (Date startTime, String startFormatTime) {
		this.startTime = startTime;
		this.startFormatTime = startFormatTime;		
	}
	
	public void setAllEndTime (Date endTime, String endFormatTime) {
		this.endTime = endTime;
		this.endFormatTime = endFormatTime;
	}
	//--------------------------------------------------------------------//
	
	
	
	/**
	 * Constructor without parameters
	 */
	public Schedule () {
		
		super();
	}
	
	
	/**
	 * Constructor with the basic parameters
	 * @param startTime
	 * @param endTime
	 */
	public Schedule (Date startTime, Date endTime) {
	
		super();
		this.startTime = startTime;
		this.endTime = endTime;
		
		//Convert all calendar schedules to format strings schedules
		DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
		this.startFormatTime = dateFormat.format(startTime);
		this.endFormatTime = dateFormat.format(endTime);
		
	}
	
	/**
	 * 
	 * @param startTime
	 * @param endTime
	 * @param startFormatTime
	 * @param endFormatTime
	 */
	public Schedule (
			Date startTime, 
			Date endTime, 
			String startFormatTime, 
			String endFormatTime) {
	
		super();
		this.startTime = startTime;
		this.endTime = endTime;
		this.startFormatTime = startFormatTime;
		this.endFormatTime = endFormatTime;		
	}
	
	@Override
	public String toString () {
		
		return "StartTime: " + startFormatTime + "\tEndTime: " + 
		endFormatTime + "\nMonday: " + monday + "\tTuesday: " + tuesday + 
		"\tWednesday: " + wednesday + "\tThursday:" + thursday + "\tFriday: " + 
		friday + "\tSaturday: " + saturday + "\tSunday: " + sunday; 
	}
	

}
