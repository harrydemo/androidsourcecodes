package com.pop;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PopupWindows extends Activity implements OnClickListener {

	Button om,om1;
	PopWin pw;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		om = (Button) findViewById(R.id.om);
		om.setOnClickListener(this);
		om1 = (Button) findViewById(R.id.om1);
		om1.setOnClickListener(this);
		pw = new PopWin(PopupWindows.this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.om) {
			pw.ShowWin(this);
		}else
		if (v.getId() == R.id.om1) {
			pw.dismiss();
		}
	}
}