package com.eoemobile.book.ex_widgetdemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.DatePicker;

public class CopyOfTimePickerActivity extends Activity
{

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setTitle("CheckBoxActivity");
		setContentView(R.layout.date_picker);
		DatePicker dp = (DatePicker) this.findViewById(R.id.date_picker);
		dp.init(2009, 5, 17, null);
	}

}