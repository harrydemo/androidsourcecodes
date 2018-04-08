package com.eoemobile.book.ex_widgetdemo;

//Download by http://www.codefans.net
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class ProgressBarActivity extends Activity
{
	CheckBox plain_cb;
	CheckBox serif_cb;
	CheckBox italic_cb;
	CheckBox bold_cb;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setTitle("ProgressBarActivity");
		setContentView(R.layout.progress_bar);
		// find_and_modify_text_view();
	}

}