/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.superdeskclock;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

/**
 * AlarmClock application.
 */
public class AlarmClock extends Activity implements OnItemClickListener {

    static final String PREFERENCES = "AlarmClock";

    /** This must be false for production.  If true, turns on logging,
        test code, etc. */
    public static final boolean DEBUG = false;

    private SharedPreferences mPrefs;
    private LayoutInflater mFactory;
    private ListView mAlarmsList;
    private Cursor mCursor;
    //����
    private ProgressDialog myDialog = null;
   
    private void updateIndicatorAndAlarm(boolean enabled, ImageView bar,
            Alarm alarm) {
        bar.setImageResource(enabled ? R.drawable.ic_indicator_on
                : R.drawable.ic_indicator_off);
        //�޸�
        Alarms.enableAlarm(this, alarm.id, enabled);
        if (enabled) {
            SetAlarm.popAlarmSetToast(this, alarm.hour, alarm.minutes, alarm.daysOfWeek);
        }
    }

    private class AlarmTimeAdapter extends CursorAdapter {
    	//����
    	private Context context;
        public AlarmTimeAdapter(Context context, Cursor cursor) {
            super(context, cursor);
        }

        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View ret = mFactory.inflate(R.layout.alarm_time, parent, false);
            DigitalClock digitalClock = (DigitalClock) ret.findViewById(R.id.digitalClock);
            digitalClock.setLive(false);
            return ret;
        }

        public void bindView(View view, Context context, Cursor cursor) {
            final Alarm alarm = new Alarm(cursor);
            
            View indicator = view.findViewById(R.id.indicator);

            // Set the initial resource for the bar image.
            final ImageView barOnOff = (ImageView) indicator.findViewById(R.id.bar_onoff);
            barOnOff.setImageResource(alarm.enabled ? R.drawable.ic_indicator_on : R.drawable.ic_indicator_off);

            // Set the initial state of the clock "checkbox"
            final CheckBox clockOnOff =(CheckBox) indicator.findViewById(R.id.clock_onoff);
            clockOnOff.setChecked(alarm.enabled);

            //����
            this.context=context;
            // Clicking outside the "checkbox" should also change the state.
            indicator.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                    	myDialog = ProgressDialog.show(AlarmTimeAdapter.this.context,"��ʾ","���Ժ�",true);
						new Thread(){ 
							public void run(){ 
								try{ 
									sleep(800);
								}catch (Exception e){
									e.printStackTrace();
								}finally{
									// ж����������myDialog����
									myDialog.dismiss();
								}
							}
						}.start();
                    	try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
                        clockOnOff.toggle();
                        updateIndicatorAndAlarm(clockOnOff.isChecked(),barOnOff, alarm);
                    }
            });

            DigitalClock digitalClock =(DigitalClock) view.findViewById(R.id.digitalClock);

            // set the alarm text
            final Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, alarm.hour);
            c.set(Calendar.MINUTE, alarm.minutes);
            digitalClock.updateTime(c);
            digitalClock.setTypeface(Typeface.DEFAULT);

            // Set the repeat text or leave it blank if it does not repeat.
            TextView daysOfWeekView = (TextView) digitalClock.findViewById(R.id.daysOfWeek);
            final String daysOfWeekStr = alarm.daysOfWeek.toString(AlarmClock.this, false);
            if (daysOfWeekStr != null && daysOfWeekStr.length() != 0) {
                daysOfWeekView.setText(daysOfWeekStr);
                daysOfWeekView.setVisibility(View.VISIBLE);
            } else {
                daysOfWeekView.setVisibility(View.GONE);
            }

            // Display the label
            TextView labelView = (TextView) view.findViewById(R.id.label);
            if (alarm.label != null && alarm.label.length() != 0) {
                labelView.setText(alarm.label);
                labelView.setVisibility(View.VISIBLE);
            } else {
                labelView.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        final int id = (int) info.id;
        switch (item.getItemId()) {
            case R.id.delete_alarm:
                // Confirm that the alarm will be deleted.
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.delete_alarm))
                        .setMessage(getString(R.string.delete_alarm_confirm))
                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface d, int w) {
                                        Alarms.deleteAlarm(AlarmClock.this, id);
                                    }
                                })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
                return true;

            case R.id.enable_alarm:
                final Cursor c = (Cursor) mAlarmsList.getAdapter().getItem(info.position);
                final Alarm alarm = new Alarm(c);
                //�޸�
                Alarms.enableAlarm(this, alarm.id, !alarm.enabled);
                if (!alarm.enabled) {
                    SetAlarm.popAlarmSetToast(this, alarm.hour, alarm.minutes, alarm.daysOfWeek);
                }
                return true;

            case R.id.edit_alarm:
                Intent intent = new Intent(this, SetAlarm.class);
                intent.putExtra(Alarms.ALARM_ID, id);
                startActivity(intent);
                return true;

            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        mFactory = LayoutInflater.from(this);
        mPrefs = getSharedPreferences(PREFERENCES, 0);
        mCursor = Alarms.getAlarmsCursor(getContentResolver());

        updateLayout();
    }

    private void updateLayout() {
        setContentView(R.layout.alarm_clock);
        mAlarmsList = (ListView) findViewById(R.id.alarms_list);
        AlarmTimeAdapter adapter = new AlarmTimeAdapter(this, mCursor);
        mAlarmsList.setAdapter(adapter);
        mAlarmsList.setVerticalScrollBarEnabled(true);
        mAlarmsList.setOnItemClickListener(this);
        mAlarmsList.setOnCreateContextMenuListener(this);

        View addAlarm = findViewById(R.id.add_alarm);
        addAlarm.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    addNewAlarm();
                }
            });
        // Make the entire view selected when focused.
        addAlarm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    v.setSelected(hasFocus);
                }
        });

        ImageButton deskClock =
                (ImageButton) findViewById(R.id.desk_clock_button);
        deskClock.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startActivity(new Intent(AlarmClock.this, DeskClock.class));
                }
        });
    }

    private void addNewAlarm() {
        startActivity(new Intent(this, SetAlarm.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ToastMaster.cancelToast();
        mCursor.deactivate();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
            ContextMenuInfo menuInfo) {
        // Inflate the menu from xml.
        getMenuInflater().inflate(R.menu.context_menu, menu);

        // Use the current item to create a custom view for the header.
        final AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
        final Cursor c =
                (Cursor) mAlarmsList.getAdapter().getItem((int) info.position);
        final Alarm alarm = new Alarm(c);

        // Construct the Calendar to compute the time.
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, alarm.hour);
        cal.set(Calendar.MINUTE, alarm.minutes);
        final String time = Alarms.formatTime(this, cal);

        // Inflate the custom view and set each TextView's text.
        final View v = mFactory.inflate(R.layout.context_menu_header, null);
        TextView textView = (TextView) v.findViewById(R.id.header_time);
        textView.setText(time);
        textView = (TextView) v.findViewById(R.id.header_label);
        textView.setText(alarm.label);

        // Set the custom view on the menu.
        menu.setHeaderView(v);
        // Change the text based on the state of the alarm.
        if (alarm.enabled) {
            menu.findItem(R.id.enable_alarm).setTitle(R.string.disable_alarm);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.menu_item_desk_clock:
                startActivity(new Intent(this, DeskClock.class));
                return true;
            case R.id.menu_item_add_alarm:
                addNewAlarm();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.alarm_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void onItemClick(AdapterView parent, View v, int pos, long id) {
        Intent intent = new Intent(this, SetAlarm.class);
        intent.putExtra(Alarms.ALARM_ID, (int) id);
        startActivity(intent);
    }
}
