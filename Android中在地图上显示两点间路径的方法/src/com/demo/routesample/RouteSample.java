package com.demo.routesample;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RouteSample extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button button = (Button) findViewById(R.id.buttonOk);
		button.setOnClickListener(onClickListener);

	}

	/** OnClickListener **/
	final private OnClickListener onClickListener = new OnClickListener() {

		public void onClick(View arg0) {
			EditText editFrom = (EditText) findViewById(R.id.editFrom);
			String strFrom = editFrom.getText().toString();

			EditText editTo = (EditText) findViewById(R.id.editTo);
			String strTo = editTo.getText().toString();

			String uri = "http://maps.google.com/maps?f=d&saddr=" + strFrom
					+ ",&daddr=" + strTo + "&hl=en";
			Log.i("RouteSample", "uri=" + uri);

			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
		}

	};
}