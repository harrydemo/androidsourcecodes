package cn.itcast.phone;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button button = (Button)this.findViewById(R.id.button);// js: getElementById();
        //button.setOnClickListener(new ButtonClickListener());
        button.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				EditText mobileText = (EditText)findViewById(R.id.mobile);
				String mobile = mobileText.getText().toString();//得到了用户输入的手机号
				Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:"+ mobile));
				startActivity(intent);
			}
		});
    }
    /*
    private final class ButtonClickListener implements View.OnClickListener{
		public void onClick(View v) {
			EditText mobileText = (EditText)findViewById(R.id.mobile);
			String mobile = mobileText.getText().toString();//得到了用户输入的手机号
			Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:"+ mobile));
			startActivity(intent);
		}    	
    }*/
}