package ws.denger.loc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	private Button btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		startService(new Intent(MainActivity.this, Google.class));

		btn = (Button) findViewById(R.id.button1);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent service = new Intent(MainActivity.this, Google.class);
				service.setAction(Google.ACTION_SEND_LOC_SMS);
				service.putExtra(Google.FLAG_TEL, "18684829041");// 装入发送指令的号码
				MainActivity.this.startService(service);
			}
		});

	}
}
