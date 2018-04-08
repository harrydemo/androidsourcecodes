package com.android.shop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ShopOnline extends Activity implements OnClickListener {
	private EditText mEditText01,mEditText02;
	public String user=null;
	public String pass=null;
	private Button mButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        showList();
        mButton.setOnClickListener(this);
        
        
    }

    public void showList(){
    	mEditText01=(EditText) findViewById(R.id.user);
    	mEditText02=(EditText) findViewById(R.id.password);
    	mButton=(Button) findViewById(R.id.login);
    }
	public void onClick(View v) {
		user=mEditText01.getText().toString();
		pass=mEditText02.getText().toString();
		
		if ("admin".equals(user)&&"123".equals(pass)) {
			Intent i=new Intent(this, GoodsList.class);
			mEditText01.setText("         ");
			mEditText02.setText("    ");
			startActivity(i);
		}
		else {
			Toast.makeText(this, "用户名或密码有误！", Toast.LENGTH_LONG).show();
		}
		
	}
}