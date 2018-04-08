package com.milifan.portal;

import com.milifan.R;
import com.milifan.util.SharedPreferencesHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Settings extends Activity {
	SharedPreferencesHelper sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.setting);
		sp = new SharedPreferencesHelper(this, "callTimeRecorder");
		
		EditText edit = (EditText) this.findViewById(R.id.edittext_setting);
		edit.setText(String.valueOf(sp.getValue("month_total")));
		Button btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				putCallMonthTotal();
			}
		});
		Button btn_cancal = (Button) findViewById(R.id.btn_cancal);
		btn_cancal.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void putCallMonthTotal() {
		EditText edit = (EditText) this.findViewById(R.id.edittext_setting);
		String ret = edit.getText().toString();
		sp = new SharedPreferencesHelper(this, "callTimeRecorder");

		if (!ret.equals("") && ret != null) {
			sp.putValue("month_total", Integer.parseInt(ret));
			this.finish();
		} else {
			Toast.makeText(this, R.string.warning, Toast.LENGTH_LONG).show();
		}

	}

}
