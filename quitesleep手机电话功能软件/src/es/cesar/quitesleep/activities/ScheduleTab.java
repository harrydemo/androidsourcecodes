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

package es.cesar.quitesleep.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import es.cesar.quitesleep.R;
import es.cesar.quitesleep.ddbb.ClientDDBB;
import es.cesar.quitesleep.ddbb.Schedule;
import es.cesar.quitesleep.dialogs.EndTimeDialog;
import es.cesar.quitesleep.dialogs.StartTimeDialog;
import es.cesar.quitesleep.interfaces.IDialogs;
import es.cesar.quitesleep.utils.ExceptionUtils;
import es.cesar.quitesleep.utils.QSLog;
import es.cesar.quitesleep.utils.QSToast;

/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class ScheduleTab extends Activity implements OnClickListener {		
	
	private final String CLASS_NAME = getClass().getName();
	private final int START_TIME_DIALOG 	= 0;
	private final int END_TIME_DIALOG 		= 1;
	private final int ABOUT_DIALOG 			= 2;
	private final int HELP_DIALOG			= 3;
	
	//Ids for the button widgets
	private final int startTimeButtonId = R.id.schedule_button_startTime;
	private final int endTimeButtonId = R.id.schedule_button_endTime;
	private final int daysWeekButtonId = R.id.schedule_button_daysweek; 
	
	//Ids for thextViews widgets
	private final int startTimeLabelId = R.id.schedule_textview_startTime; 
	private final int endTimeLabelId = R.id.schedule_textview_endTime;
	
	//Days of the week checkboxes Ids
	private final int mondayCheckId = R.id.schedule_checkbox_monday;
	private final int tuesdayCheckId = R.id.schedule_checkbox_tuesday;
	private final int wednesdayCheckId = R.id.schedule_checkbox_wednesday;
	private final int thursdayCheckId = R.id.schedule_checkbox_thursday;
	private final int fridayCheckId = R.id.schedule_checkbox_friday;
	private final int saturdayCheckId = R.id.schedule_checkbox_saturday;
	private final int sundayCheckId = R.id.schedule_checkbox_sunday;				
	
	//Used dialogs in this activity
	private StartTimeDialog startTimeDialog; 
	private EndTimeDialog endTimeDialog;
	
	//labels for start and end times
	private TextView startTimeLabel; 
	private TextView endTimeLabel; 
	
	//Days of the week checkboxes
	private CheckBox mondayCheck;
	private CheckBox tuesdayCheck;
	private CheckBox wednesdayCheck;
	private CheckBox thursdayCheck;
	private CheckBox fridayCheck;
	private CheckBox saturdayCheck;
	private CheckBox sundayCheck;
	
	//Ids for option menu
	final int aboutMenuId = R.id.menu_information_about;
	final int helpMenuId = R.id.menu_information_help;
	
	final private String CALCULATOR_FONT = "fonts/calculator_script_mt.ttf";
			
		
	@Override
	public void onCreate (Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scheduletab);
			
		startTimeDialog = new StartTimeDialog(this);
		endTimeDialog = new EndTimeDialog(this);
		
		Button startTimeButton = (Button)findViewById(startTimeButtonId);				
		Button endTimeButton = (Button)findViewById(endTimeButtonId);
		Button daysWeekButton = (Button)findViewById(daysWeekButtonId);
		
		//---------		Define our own text style used a custom font	------//
		startTimeLabel = (TextView)findViewById(startTimeLabelId);
		endTimeLabel = (TextView)findViewById(endTimeLabelId);
		
		Typeface face = Typeface.createFromAsset(getAssets(), CALCULATOR_FONT);
		
		startTimeLabel.setTypeface(face);
		endTimeLabel.setTypeface(face);
		//-------------------------------------------------------------------//
		
		//Sets the timeLabes into start and end time dialogs for update the changes		
		startTimeDialog.setStartTimeLabel(startTimeLabel);
		endTimeDialog.setEndTimeLabel(endTimeLabel);
		
		
		//Instantiate the days of the week checkboxes
		mondayCheck = (CheckBox)findViewById(mondayCheckId);
		tuesdayCheck = (CheckBox)findViewById(tuesdayCheckId);
		wednesdayCheck = (CheckBox)findViewById(wednesdayCheckId);
		thursdayCheck = (CheckBox)findViewById(thursdayCheckId);
		fridayCheck = (CheckBox)findViewById(fridayCheckId);
		saturdayCheck = (CheckBox)findViewById(saturdayCheckId);
		sundayCheck = (CheckBox)findViewById(sundayCheckId);
		
		//Define the onClick listeners
		startTimeButton.setOnClickListener(this);
		endTimeButton.setOnClickListener(this);
		daysWeekButton.setOnClickListener(this);
		
		/* Set the default saved data when this activity is run for first time
		 * (not for first time globally, else for all times that we run
		 * the application and show this activity for first time)
		 */
		setDefaultSavedData();
		
					
	}
	
	
	/**
	 * Listener for all buttons in this Activity
	 */
	public void onClick (View view) {
		
		int viewId = view.getId();
				
		switch (viewId) {
		
			case startTimeButtonId:				
				showDialog(START_TIME_DIALOG);				
				break;
				
			case endTimeButtonId:
				showDialog(END_TIME_DIALOG);				
				break;			
				
			case daysWeekButtonId:
				saveDayWeeksSelection();
				break;
		}						
	}
	
	/**
	 * For the first time that create the dialogs
	 */
	@Override
	protected Dialog onCreateDialog (int id) {
		
		Dialog dialog;
		
		switch (id) {
			case START_TIME_DIALOG:
				if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "Create the StartTimeDialog for 1st time");
				dialog = startTimeDialog.getTimePickerDialog();				
				break;
			case END_TIME_DIALOG:
				if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "Create the EndTimeDialog for 1st time");				
				dialog = endTimeDialog.getTimePickerDialog();
				break;
			case ABOUT_DIALOG:
				dialog = showWebviewDialog(IDialogs.ABOUT_URI);
				break;	
			case HELP_DIALOG:
				dialog = showWebviewDialog(IDialogs.HELP_SCHEDULE_URI);
				break;	
			default:
				dialog = null;
		}
		
		return dialog;	
	}
	
	/**
	 * Create the webview dialog using the file (uri) specified to show the information.
	 * 
	 * @return
	 */
	public Dialog showWebviewDialog(String uri) {
		
		try {
			  View contentView = getLayoutInflater().inflate(R.layout.webview_dialog, null, false);
              WebView webView = (WebView) contentView.findViewById(R.id.webview_content);
              webView.getSettings().setJavaScriptEnabled(true);              
              
              webView.loadUrl(uri);

              return new AlertDialog.Builder(this)
                  .setCustomTitle(null)
                  .setPositiveButton(android.R.string.ok, null)
                  .setView(contentView)
                  .create();
              
		}catch (Exception e) {
			if (QSLog.DEBUG_E) QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
			return null;
		}
	}
	
	/**
	 * This function prepare the dalogs every time to call for some of this
	 * 
	 *  @param int
	 *  @param dialog
	 */
	@Override
	protected void onPrepareDialog (int idDialog, Dialog dialog) {
		
		try {
			
			switch (idDialog) {
				case START_TIME_DIALOG:				
					break;
				case END_TIME_DIALOG:
					break;
				default:
					break;
			}
			
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));			
		}
	}

	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data) {
		
		//TODO
		if (QSLog.DEBUG_D)QSLog.d(CLASS_NAME, "en onActiviryResult");	
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
		
		try {							
			MenuInflater menuInflater = getMenuInflater();
			menuInflater.inflate(R.menu.informationmenu, menu);
			
			return true;
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
			return false;
		}		
	}
	
	/**
	 * @param 		item
	 * @return 		boolean
	 */
	@Override
	public boolean onOptionsItemSelected (MenuItem item) {
		
		try {
			
			switch (item.getItemId()) {
				case aboutMenuId:
					showDialog(ABOUT_DIALOG);
					break;
				case helpMenuId:
					showDialog(HELP_DIALOG);
					break;
				default:
					break;
			}
			return false;
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
			return false;			
		}
	}
	
	/**
	 * Save the days of the weeks checkboxes state into a Schedule object from the
	 * database. If the Schedule object isn't create, we don't do nothing, not save the
	 * checkboxes state.
	 * 
	 * @return			true or false if the schedule save was good or not
	 * @see				boolean
	 */
	private boolean saveDayWeeksSelection () {
		
		try {
			
			ClientDDBB clientDDBB = new ClientDDBB();
			Schedule schedule = clientDDBB.getSelects().selectSchedule();
			if (schedule != null) {
				
				schedule.setMonday(mondayCheck.isChecked());
				schedule.setTuesday(tuesdayCheck.isChecked());
				schedule.setWednesday(wednesdayCheck.isChecked());
				schedule.setThursday(thursdayCheck.isChecked());
				schedule.setFriday(fridayCheck.isChecked());
				schedule.setSaturday(saturdayCheck.isChecked());
				schedule.setSunday(sundayCheck.isChecked());
				
				clientDDBB.getInserts().insertSchedule(schedule);
				
				clientDDBB.commit();
				clientDDBB.close();
				
				if (QSToast.RELEASE) QSToast.r(
                		this,
                		this.getString(
                				R.string.schedule_toast_daysweek_ok),
                		Toast.LENGTH_SHORT);
				
				return true;
				
			}else {
				if (QSToast.RELEASE) QSToast.r(
                		this,
                		this.getString(
                				R.string.schedule_toast_daysweek_ko),
                		Toast.LENGTH_SHORT);
				
				return false;
			}
				
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
			throw new Error(e.toString());
		}
	}
	
	/**
	 * Set the default saved data in the start and end time labels and
	 * all checkboxes associated to a day of the week.
	 */
	private void setDefaultSavedData () {
		
		try {
			
			ClientDDBB clientDDBB = new ClientDDBB();
			
			Schedule schedule = clientDDBB.getSelects().selectSchedule();
			if (schedule != null) {
				if (schedule.getStartFormatTime()!= null && 
						!schedule.getStartFormatTime().equals(""))
					startTimeLabel.setText(schedule.getStartFormatTime());
				
				if (schedule.getEndFormatTime() != null &&
						!schedule.getEndFormatTime().equals(""))
					endTimeLabel.setText(schedule.getEndFormatTime());
				
				mondayCheck.setChecked(schedule.isMonday());
				tuesdayCheck.setChecked(schedule.isTuesday());
				wednesdayCheck.setChecked(schedule.isWednesday());
				thursdayCheck.setChecked(schedule.isThursday());
				fridayCheck.setChecked(schedule.isFriday());
				saturdayCheck.setChecked(schedule.isSaturday());
				sundayCheck.setChecked(schedule.isSunday());
			}
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
		}
	}
		
}
