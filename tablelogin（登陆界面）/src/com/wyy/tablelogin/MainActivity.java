package com.wyy.tablelogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final EditText unameEt=(EditText)findViewById(R.id.yhm);
        final EditText upassEt=(EditText)findViewById(R.id.passw);
        Button btn=(Button)findViewById(R.id.dl);
        btn.setOnClickListener(
        		new View.OnClickListener() {
					
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String uname=unameEt.getText().toString();
						String password=upassEt.getText().toString();
						SharedPreferences references=getSharedPreferences("account",Context.MODE_WORLD_READABLE+Context.MODE_WORLD_WRITEABLE);
						String name=references.getString("username","");
						String psw=references.getString("password","");
						if(name.equals(uname)&&psw.equals(password))
						{
				
							Intent intentq=new Intent(MainActivity.this,OtherActivity.class);
							startActivity(intentq);
							//String sb="登陆成功";
							//Toast.makeText(MainActivity.this,sb.toString(),1000).show();
						}
						
						else
						{	
							String sb="请输入正确的用户名和密码";
							Toast.makeText(MainActivity.this,sb.toString(),1000).show();
						} 
						
					}
				
					}
				
        );
        Button button=(Button)findViewById(R.id.zcbutton);
        button.setOnClickListener(
        		new View.OnClickListener() {
					
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent=new Intent(MainActivity.this, RegisterActivity.class);
							startActivity(intent);
					}
				}
        );
    }
}