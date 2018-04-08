package com.parabola.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	public Button add;
	public EditText username;
	public EditText usersex;
	public EditText userage;
	public EditText useraddress;
 
	String name;
	String sex;
	String age;
	String address;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        init();
        
        add.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View v) {
				checkData();
			}
        	
        });
    }
    
    public void init(){
    	username = (EditText)findViewById(R.id.username);  
  	    usersex = (EditText)findViewById(R.id.sex);  
  	    userage = (EditText)findViewById(R.id.age); 
  	    useraddress = (EditText)findViewById(R.id.address); 
  	    add = (Button) this.findViewById(R.id.add);
    }
  
    /**
     * 接收数据，并且验证是否正确
     */
	public void checkData(){   
	    name = username.getText().toString().trim();
	    sex = usersex.getText().toString().trim();
	    age = userage.getText().toString().trim();
	    address = useraddress.getText().toString().trim();
	    
	    User user = new User();  
	    user.setName(name);  
	    user.setSex(sex);
	    user.setAge(age);
	    user.setAddress(address);
	    
	    LoginProtocol login = new LoginProtocol();  
	    boolean result = login.checkLogin(user);  
	      
	    if(result){                
	    	//SpiderCache.getInstance().setUserSession(user);  
	        Toast.makeText(getApplicationContext(), "登录成功", 1000).show();  
	        Intent intent = new Intent ();  
	        intent.setClass(LoginActivity.this,WelcomeActivity.class);  
	        startActivity(intent);  
	    }else{              
	    	Toast.makeText(LoginActivity.this,"密码或用户名不匹配，请重新输入！",1000).show();  
	    }  
	}
}  