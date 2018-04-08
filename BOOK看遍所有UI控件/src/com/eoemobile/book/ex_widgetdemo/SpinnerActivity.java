package com.eoemobile.book.ex_widgetdemo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class SpinnerActivity extends Activity
{
	Spinner spinner_c;
	Spinner spinner_2;
	private ArrayAdapter<String> aspnCountries;
	private List<String> allcountries;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setTitle("SpinnerActivity");
		setContentView(R.layout.spinner);
		find_and_modify_view();
	}

	private static final String[] mCountries =
	{ "China", "Russia", "Germany", "Ukraine", "Belarus", "USA" };

	private void find_and_modify_view()
	{
		spinner_c = (Spinner) findViewById(R.id.spinner_1);
		allcountries = new ArrayList<String>();
		for (int i = 0; i < mCountries.length; i++)
		{
			allcountries.add(mCountries[i]);
		}
		aspnCountries = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, allcountries);
		aspnCountries.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_c.setAdapter(aspnCountries);

		spinner_2 = (Spinner) findViewById(R.id.spinner_2);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.countries, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_2.setAdapter(adapter);
	}

}