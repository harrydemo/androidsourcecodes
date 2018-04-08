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

package es.cesar.quitesleep.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import es.cesar.quitesleep.staticValues.ConfigAppValues;
import es.cesar.quitesleep.utils.ExceptionUtils;
import es.cesar.quitesleep.utils.QSLog;
import es.cesar.quitesleep.utils.QSToast;


/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 * 
 * Class that implements the sms delivery action for listen the send action message 
 *
 */
public class DeliveredActionSMSReceiver extends BroadcastReceiver {
	
	private String CLASS_NAME = getClass().getName();
	
	final String DELIVERED_SMS_ACTION 		= 	"DELIVERED_SMS_ACTION";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "The user have been receive the SMS message!!");
		if (QSToast.DEBUG) QSToast.d(
        		ConfigAppValues.getContext(),
        		"The user have been receive the SMS message!!",
        		Toast.LENGTH_SHORT);;
	}			

}
