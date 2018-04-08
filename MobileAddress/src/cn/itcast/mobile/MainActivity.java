package cn.itcast.mobile;

import cn.itcast.service.MobileService;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
	private EditText mobileText;
    private TextView resultView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mobileText = (EditText) this.findViewById(R.id.mobile);
        resultView = (TextView) this.findViewById(R.id.result);
        Button button = (Button) this.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				String mobile = mobileText.getText().toString();
				try {
					String address = MobileService.getMobileAddress(mobile);
					resultView.setText(address);
				} catch (Exception e) {
					Log.e(TAG, e.toString());
					Toast.makeText(MainActivity.this, R.string.error, 1).show();
				}
			}
		});
    }
}