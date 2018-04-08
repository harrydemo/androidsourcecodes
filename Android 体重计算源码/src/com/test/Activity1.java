package com.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class Activity1 extends Activity {

    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity1);
        
        Button b1 = (Button) findViewById(R.id.button1);
        b1.setOnClickListener(new Button.OnClickListener()
        {
          public void onClick(View v)
          {
            //取得输入的身高
            EditText et = (EditText) findViewById(R.id.height);
            double height=Double.parseDouble(et.getText().toString());
            //取得选择的性别
            String sex="";
            RadioButton rb1 = (RadioButton)findViewById(R.id.sex1);
            if(rb1.isChecked())
            {
              sex="M";
            }
            else
            {
              sex="F";
            }
         
            Intent intent = new Intent();
            intent.setClass(Activity1.this,Activity2.class);
            
            //new一个Bundle对象，并将要传递的数据传入
            Bundle bundle = new Bundle();
            bundle.putDouble("height",height);
            bundle.putString("sex",sex);
          
            //将Bundle对象assign给Intent
            intent.putExtras(bundle);
          
            //调用Activity2
            startActivity(intent);
          }
        });
    }
    
    
}