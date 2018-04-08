package com.eoemobile.book.ex_widgetdemo;

//Download by http://www.codefans.net
import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

public class ViewTextActivity extends Activity
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setTitle("ViewTextActivity");
		setContentView(R.layout.text);
		find_and_modify_text_view();
	}

	private void find_and_modify_text_view()
	{
		TextView text_view = (TextView) findViewById(R.id.text_view);
		CharSequence text_view_old = text_view.getText();
		text_view.setText("修改前是：" + text_view_old + "\n修改为:TextView的值也是可以动态修改的.");
	}

}