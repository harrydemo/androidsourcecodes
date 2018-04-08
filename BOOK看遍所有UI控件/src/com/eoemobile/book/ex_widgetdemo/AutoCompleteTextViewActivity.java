package com.eoemobile.book.ex_widgetdemo;

//Download by http://www.codefans.net
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class AutoCompleteTextViewActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.autocomplete);
		setTitle("AutoCompleteTextViewActivity");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, COUNTRIES);
		AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.auto_complete);
		textView.setAdapter(adapter);
	}

	static final String[] COUNTRIES = new String[]
	{ "China", "Russia", "Germany", "Ukraine", "Belarus", "USA", "China1", "China12", "Germany", "Russia2", "Belarus", "USA" };

}
