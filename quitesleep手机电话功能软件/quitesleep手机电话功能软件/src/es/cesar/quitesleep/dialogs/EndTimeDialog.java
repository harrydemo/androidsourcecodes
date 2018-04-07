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

package es.cesar.quitesleep.dialogs;

import java.text.DateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.util.Log;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import es.cesar.quitesleep.R;
import es.cesar.quitesleep.ddbb.ClientDDBB;
import es.cesar.quitesleep.ddbb.Schedule;
import es.cesar.quitesleep.utils.ExceptionUtils;
import es.cesar.quitesleep.utils.QSLog;
import es.cesar.quitesleep.utils.QSToast;

/**
 * 
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class EndTimeDialog {
	
	private String CLASS_NAME = getClass().getName();	
	
	DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);		
	Calendar dateAndTime = Calendar.getInstance();
		
	private TimePickerDialog timePickerDialog;
	
	private TextView endTimeLabel;
	
	private Activity activity;
					
	
	//--------------		Getters & Setters		-----------------------//
	public TimePickerDialog getTimePickerDialog() {
		return timePickerDialog;
	}
	public void setTimePickerDialog(TimePickerDialog timePickerDialog) {
		this.timePickerDialog = timePickerDialog;
	}
	
	public TextView getEndTimeLabel() {
		return endTimeLabel;
	}
	public void setEndTimeLabel(TextView endTimeLabel) {
		this.endTimeLabel = endTimeLabel;
	}
	//-----------------------------------------------------------------------//

			
	TimePickerDialog.OnTimeSetListener timerPickerEnd = 
		new TimePickerDialog.OnTimeSetListener() {
		
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						
			view.setIs24HourView(true);
			dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
			dateAndTime.set(Calendar.MINUTE, minute);			
			
			//Update operations
			updateSchedule();
			updateEndTimeLabel();
			
		}
	};

	/**
	 * Constructor
	 * 
	 * @param activity
	 */
	public EndTimeDialog (Activity activity) {					
		
		timePickerDialog = new TimePickerDialog(
				activity,
				timerPickerEnd,
				dateAndTime.get(Calendar.HOUR_OF_DAY),
				dateAndTime.get(Calendar.MINUTE),
				true);	
		
		this.activity = activity;
		
		//If we have redefine the time picker dialog title
		//timePickerDialog.setTitle(R.string.endtimedialog_message_label);		
	}				
	
	/**
	 * Update the endTimeLabel located in the activity ScheduleTab, with the
	 * selected by user endTime.
	 */
	private void updateEndTimeLabel () {
		
		try {
						
			if (endTimeLabel != null)
				endTimeLabel.setText(timeFormat.format(dateAndTime.getTime()));
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));			
		}
	}
	
	/**
	 * Update the Schedule object from the database with the end time objects
	 * that have been used in the dialog and specified by the user.
	 * 
	 * @throws Exception
	 */
	private void updateSchedule () {
				
		try {
			
			ClientDDBB clientDDBB = new ClientDDBB();
			
			Schedule schedule = clientDDBB.getSelects().selectSchedule();
			
			/* If the Schedule object is null, then never have been created before
			 * so, we have to create here.
			 */			
			if (schedule == null)
				schedule = new Schedule();
			
			schedule.setAllEndTime(
					dateAndTime.getTime(), 
					timeFormat.format(dateAndTime.getTime()));
			
			clientDDBB.getUpdates().insertSchedule(schedule);
			
			clientDDBB.commit();			
			clientDDBB.close();
			
			if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "Schedule saved with the end time!!");
			
			if (QSToast.RELEASE) QSToast.r(
            		activity,
            		activity.getString(
            				R.string.schedule_toast_endTime),
            		Toast.LENGTH_SHORT);	
						
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
			throw new Error(e.toString());
		}
	}

}
	