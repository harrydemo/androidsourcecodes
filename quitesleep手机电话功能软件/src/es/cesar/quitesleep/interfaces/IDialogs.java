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

package es.cesar.quitesleep.interfaces;

import android.app.Dialog;

public interface IDialogs {

	//About me uri
	public final String ABOUT_URI = "file:///android_asset/about.html";
	
	//Help files uris
	public final String HELP_CONTACT_URI = "file:///android_asset/helpContacts.html";
	public final String HELP_LOGS_URI = "file:///android_asset/helpLog.html";
	public final String HELP_SCHEDULE_URI = "file:///android_asset/helpSchedule.html";
	public final String HELP_SETTINGS_URI = "file:///android_asset/helpSettings.html";
	
	
	/**
	 * Builds and returns the webview dialog with the specified uri that includes
	 * the resource to show. 
	 * @return
	 */
	public Dialog showWebviewDialog (String uri);
				
}
