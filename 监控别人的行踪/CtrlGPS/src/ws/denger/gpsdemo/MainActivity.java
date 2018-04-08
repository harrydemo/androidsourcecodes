package ws.denger.gpsdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * @author Denger
 * 
 */
public class MainActivity extends Activity implements OnClickListener {
	private Button btnOpen, btnQuery;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		btnOpen = (Button) findViewById(R.id.button1);
		btnQuery = (Button) findViewById(R.id.button2);
		btnOpen.setOnClickListener(this);
		btnQuery.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			GPSHelper.toggleGPS(this);
			break;

		case R.id.button2:
			boolean isOpen = GPSHelper.isGPSEnable(this);
			Toast.makeText(this, "" + isOpen, 500).show();
			break;
		}
	}

}