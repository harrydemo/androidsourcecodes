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

package es.cesar.quitesleep.mailmessages;

import java.util.Properties;

import android.util.Log;
import es.cesar.quitesleep.utils.ExceptionUtils;
import es.cesar.quitesleep.utils.QSLog;

/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class MailConfig {
	
	private static String CLASS_NAME = "es.cesar.quitesleep.sendmessages.MailConfig";
	
	private static Properties properties = null;	

	
	//-------------		Getters & Setters		------------------------------//
	public static Properties getProperties() {
		return properties;
	}

	public static void setProperties(Properties properties) {
		MailConfig.properties = properties;
	}
	//------------------------------------------------------------------------//
	 
	public static void initProperties () {
		
		try {
			properties = new Properties();
			properties.setProperty("mail.transport.protocol", "smtp");
			properties.setProperty("mail.host", "smtp.gmail.com");
			properties.put("mail.smtp.auth", "true");
			properties.put("mail.smpt.port", "465");
			properties.put("mail.smtp.socketFactory.port", "465");
			properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			properties.put("mail.smtp.socketFactory.fallback", "false");
			properties.setProperty("mail.smtp.quitwait", "false");						
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
		}
	}
	
	
	

}
