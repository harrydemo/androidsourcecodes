package com.chenqi.activity;

import com.chenqi.domain.User;
import com.chenqi.service.UserService;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class RegisterActivity extends Activity
{
	Button registerBtn;
	Button exitBtn;
	EditText userText;
	EditText pwdText;
	RadioGroup sexradioGroup;
	EditText ageText;
	EditText telephoneText;
	UserService userService;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		registerBtn=(Button)this.findViewById(R.id.register);
		exitBtn=(Button)this.findViewById(R.id.registerexit);
		userText=(EditText)this.findViewById(R.id.registerusername);
		pwdText=(EditText)this.findViewById(R.id.registerpwd);
		sexradioGroup=(RadioGroup)this.findViewById(R.id.rgGender);
		ageText=(EditText)this.findViewById(R.id.age);
		telephoneText=(EditText)this.findViewById(R.id.telephone);
		registerBtn.setOnClickListener(new buttonOnClickListener());
		exitBtn.setOnClickListener(new buttonOnClickListener());
		userService=new UserService(this);
	}
	private class buttonOnClickListener implements View.OnClickListener{
		
		@Override
		public void onClick(View arg0)
		{
			switch (((Button)arg0).getId())
			{
			case R.id.register:
			String username=userText.getText().toString();
			String pwd=pwdText.getText().toString();
			String sex="ÄÐ";
			if(sexradioGroup.getId()==R.id.rbGirl)
				sex="Å®";
			Integer age=Integer.valueOf(ageText.getText().toString());
			String telephone=telephoneText.getText().toString();
			User user=new User(username, pwd, sex, age, telephone);
			userService.save(user);	
			Toast.makeText(RegisterActivity.this, R.string.success, Toast.LENGTH_LONG).show();
			break;

			case R.id.registerexit:
				finish();
				break;
			}
			
		}
	}

}
