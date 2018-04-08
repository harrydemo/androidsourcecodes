package com.bao;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
    /** Called when the activity is first created. */
	
	private EditText username;
	private EditText password;
	private Button login;
	private Button reset;
	UserManager userManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.login);
        reset = (Button)findViewById(R.id.reset);
        userManager = new UserManager();
        login.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean isSuccess = userManager.isRightUser(username.getText().toString(), password.getText().toString());
				if(isSuccess == true){
					Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(LoginActivity.this, "密码或用户名错误", Toast.LENGTH_LONG).show();
					
				}
			}
		});
        
        reset.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				username.setText("");
				password.setText("");
			}
		});
    
    }
}