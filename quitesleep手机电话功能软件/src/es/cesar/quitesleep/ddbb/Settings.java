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
 */
public class Settings extends Id {
	
	public final String CLASS_NAME = getClass().getName();
	
	//Attribute Settings QuiteSleep service 
	private boolean quiteSleepServiceState = false;
	
	//Attribute Mail Settings	
	private String user;
	private String passwd;
	private String subject;
	private String body;
	private boolean mailService = false;
	
	//Attribute SMS Settings
	private String smsText;
	private boolean smsService = false;
	
	//---------------		Getters & Setters		--------------------------//
	
	//@@@@@@@@@		QuiteSleep service		@@@@@@@@@@@@@@@//
	public boolean isQuiteSleepServiceState() {
		return quiteSleepServiceState;
	}
	public void setQuiteSleepServiceState(boolean quiteSleepServiceState) {
		this.quiteSleepServiceState = quiteSleepServiceState;
	}
	
	//@@@@@@@@@@	Mail Service		@@@@@@@@@@@@@@@@@@//
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}

	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}

	public boolean isMailService() {
		return mailService;
	}
	public void setMailService(boolean mailService) {
		this.mailService = mailService;
	}
	
	//@@@@@@@@@@@@		SMS service		@@@@@@@@@@@@@@@@@@@@//
	public boolean isSmsService() {
		return smsService;
	}
	public void setSmsService(boolean smsService) {
		this.smsService = smsService;
	}
	
	public String getSmsText() {
		return smsText;
	}
	public void setSmsText(String smsText) {
		this.smsText = smsText;
	}	
	//------------------------------------------------------------------------//
		
	
	/**
	 * Constructor without parameters
	 */
	public Settings () {
		super();		
	}
	
	
	/**
	 * Constructor with the basic paramters
	 * @param quiteSleepServiceState
	 */
	public Settings (boolean quiteSleepServiceState) {
		
		super();
		this.quiteSleepServiceState = quiteSleepServiceState;
	}
	
	
	@Override
	public String toString () {
		return "Settings: \tquiteSleepServiceState: " + quiteSleepServiceState;
	}
	
	

}
