package com.eoemobile.book.ex_widgetdemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class TimePickerActivity extends Activity
{

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setTitle("TimePickerActivity");
		setContentView(R.layout.time_picker);
		TimePicker tp = (TimePicker) this.findViewById(R.id.time_picker);
		tp.setIs24HourView(true);
	}

}