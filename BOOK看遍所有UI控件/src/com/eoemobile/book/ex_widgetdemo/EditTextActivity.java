package com.eoemobile.book.ex_widgetdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class EditTextActivity extends Activity
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setTitle("EditTextActivity");
		setContentView(R.layout.editview);
		find_and_modify_text_view();
	}

	private void find_and_modify_text_view()
	{
		Button get_edit_view_button = (Button) findViewById(R.id.get_edit_view_button);
		get_edit_view_button.setOnClickListener(get_edit_view_button_listener);
	}

	private Button.OnClickListener get_edit_view_button_listener = new Button.OnClickListener()
	{
		public void onClick(View v)
		{
			EditText edit_text = (EditText) findViewById(R.id.edit_text);
			CharSequence edit_text_value = edit_text.getText();
			setTitle("EditTextµÄÖµ:" + edit_text_value);
		}
	};

}