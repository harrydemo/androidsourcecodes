package com.betterman.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.betterman.util.Constant;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.R;

public class LoginActivity extends Activity
{
	EditText email ;
//  EditText ip;
	Button register;
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
      // TODO Auto-generated method stub
      super.onCreate(savedInstanceState);
      setContentView(R.layout.register);
      
      email = (EditText)findViewById(R.id.email);
//      ip = (EditText)findViewById(R.id.ip);
      register = (Button)findViewById(R.id.ok);
      
      register.setOnClickListener(new OnClickListener()
      {
          
          @Override
          public void onClick(View v)
          {
//              Constant.SERVER_IP = ip.getText().toString();
              Constant.EMAIL = email.getText().toString();
              Intent intent = new Intent(LoginActivity.this,CaptureActivity.class);
              LoginActivity.this.startActivity(intent);
              LoginActivity.this.finish();
              
          }
      });
      
  }

}
