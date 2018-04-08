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

package es.cesar.quitesleep.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import es.cesar.quitesleep.R;
import es.cesar.quitesleep.activities.Main;
import es.cesar.quitesleep.staticValues.ConfigAppValues;
import es.cesar.quitesleep.utils.ExceptionUtils;
import es.cesar.quitesleep.utils.QSLog;

/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class QuiteSleepNotification {

	private final static String CLASS_NAME = "es.cesar.quitesleep.notifications.QuiteSleepNotification";
	
	//Notification
	private static NotificationManager notificationManager = null;
	final private static int notificationId = R.layout.quitesleep_notification;
	
	/**
	 * Create the intent that is used when the user touch the QuiteSleep
	 * notification, and then go to the Main class application.
	 * 
	 * @return		the PendingIntent
	 * @see			PendingIntent
	 */
	private static PendingIntent notificationIntent (Context context) {
        // The PendingIntent to launch our activity if the user selects this
        // notification.  Note the use of FLAG_UPDATE_CURRENT so that if there
        // is already an active matching pending intent, we will update its
        // extras to be the ones passed in here.
        PendingIntent contentIntent = PendingIntent.getActivity(
        		context, 
        		0,
                new Intent(
                		context, 
                		Main.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK),                        
                PendingIntent.FLAG_UPDATE_CURRENT);
        return contentIntent;
    }
	
	
	/**
	 * Show the QuiteSleep service running notification. If is the stop
	 * action, cancel the notification and hide.
	 */
	public static void showNotification (Context context, boolean showNotification) {
		
		try {
			if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "notifiactionId: " + notificationId);
			
			if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "showNotification: " + showNotification);
			
			if (showNotification) {
				
				//Get the notification manager service
				if (notificationManager == null)
					notificationManager = 
						(NotificationManager)context.
							getSystemService(Context.NOTIFICATION_SERVICE);

				
				CharSequence title = context.getText(R.string.app_name);
				
				CharSequence message;
				
				message = ConfigAppValues.getContext().getText(
						R.string.quitesleep_notification_start_message);
				
				Notification notification = new Notification(
						R.drawable.quitesleep_statusbar,
						message, 
						System.currentTimeMillis());
				
				notification.setLatestEventInfo(
						context, 
						title, 
						message, 
						notificationIntent(context));
				
				/* For the led flash custom
				notification.ledARGB = 0xff00ff00;
				notification.ledOnMS = 300;
				notification.ledOffMS = 1000;
				notification.flags |= Notification.FLAG_SHOW_LIGHTS;
				*/
				
				notificationManager.notify(notificationId, notification);
				
			}else  if (notificationManager != null){
				if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "Cancelando la notificacion");
				notificationManager.cancel(notificationId);
			}
			
							
																		
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
		}
	}
}
