package com.eoemobile.book.ex_widgetdemo;

//Download by http://www.codefans.net
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class CheckBoxActivity extends Activity
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
		setTitle("CheckBoxActivity");
		setContentView(R.layout.check_box);
		find_and_modify_text_view();
	}

	private void find_and_modify_text_view()
	{
		plain_cb = (CheckBox) findViewById(R.id.plain_cb);
		serif_cb = (CheckBox) findViewById(R.id.serif_cb);
		italic_cb = (CheckBox) findViewById(R.id.italic_cb);
		bold_cb = (CheckBox) findViewById(R.id.bold_cb);
		Button get_view_button = (Button) findViewById(R.id.get_view_button);
		get_view_button.setOnClickListener(get_view_button_listener);
	}

	private Button.OnClickListener get_view_button_listener = new Button.OnClickListener()
	{
		public void onClick(View v)
		{
			String r = "";
			if (plain_cb.isChecked())
			{
				r = r + "," + plain_cb.getText();
			}
			if (serif_cb.isChecked())
			{
				r = r + "," + serif_cb.getText();
			}
			if (italic_cb.isChecked())
			{
				r = r + "," + italic_cb.getText();
			}
			if (bold_cb.isChecked())
			{
				r = r + "," + bold_cb.getText();
			}
			setTitle("Checked: " + r);
		}
	};
}