package com.wang;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditTextTest extends Activity {
 Button clearbtn;
 EditText et;
 TextView tv;
 final int MAX_LENGTH = 50;
 int Rest_Length = MAX_LENGTH;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        tv =(TextView)findViewById(R.id.tv);
        et = (EditText)findViewById(R.id.et);

        et.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(Rest_Length>0){
					Rest_Length = MAX_LENGTH - et.getText().length();
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				tv.setText("还可以输入"+Rest_Length+"个字");
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				tv.setText("还可以输入"+Rest_Length+"个字");
			}
		});

        clearbtn = (Button)findViewById(R.id.btn);
        clearbtn.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				et.setText("");
				Rest_Length = MAX_LENGTH;
			}
		});
    }
}