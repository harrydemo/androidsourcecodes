package com.worldchip.apk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

//相册确定登录界面
public class StoreLogin extends Activity{
	SharedPreferences shared = null;
	Editor editor = null;
	private EditText password = null;//登陆密码
	private EditText twoedit = null;//旧密码
	private EditText threeedit = null;//新密码
	private EditText fouredit = null;//确认新密码
	String password01;//默认密码
	View DialogView = null;
	View dialogView = null;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main1);
        shared = getSharedPreferences("passwordshared", Activity.MODE_PRIVATE);
        temp();  
        Toast.makeText(StoreLogin.this, "注意：相册的初始密码为admin!", Toast.LENGTH_SHORT).show();
    }
	
	public void temp(){//登陆页面
		editor = shared.edit();//打开指定shared文件，如果 不存在则创建他
		password01 = shared.getString("password", "admin");
		LayoutInflater factory = LayoutInflater.from(this);//得到自定义对话框
        DialogView = factory.inflate(R.layout.main3, null);//基于xml布局文件创建一个对话框样式预备使用
        password = (EditText)DialogView.findViewById(R.id.password);
		AlertDialog dlg = new AlertDialog.Builder(this)
        .setTitle("请输入相册密码")
        .setIcon(R.drawable.dl_icon) 
        .setView(DialogView)
        .setPositiveButton("登录", new DialogInterfaceOnClick2())  //设置确定按钮并且绑定监听事件
        .setNeutralButton("修改", new DialogInterfaceOnClick3())   //设置修改按钮并且绑定监听事件
        .setNegativeButton("退出", new DialogInterfaceOnClick6())  //设置取消按钮并且绑定监听事件
        .create();//设置取消按钮并创建
        dlg.show();//显示
	}
	
	public void alter(){//修改页面
		LayoutInflater factory = LayoutInflater.from(StoreLogin.this);//得到自定义对话框
		dialogView = factory.inflate(R.layout.main2, null);
		AlertDialog dlg = new AlertDialog.Builder(StoreLogin.this)
		.setTitle("请修改相册密码")
		.setView(dialogView)
		.setPositiveButton("确定", new DialogInterfaceOnClick5())
		.setNegativeButton("取消", new DialogInterfaceOnClick4())
		.create();
		dlg.show();
		
	}
    
    class DialogInterfaceOnClick2 implements DialogInterface.OnClickListener{//登陆

		
		public void onClick(DialogInterface dialog, int whichButton) {
			String pass = password.getText().toString();//拿到用户输入的值
			if(pass.equals(password01)){
				Toast.makeText(StoreLogin.this, "登录成功", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.setClass(StoreLogin.this, DreamerViewActivity.class);
				StoreLogin.this.startActivity(intent);
				finish();
			}else{
				Toast.makeText(StoreLogin.this, "密码错误！", Toast.LENGTH_SHORT).show();
				temp();
			}
		}
    }
    
    class DialogInterfaceOnClick3 implements DialogInterface.OnClickListener{//修改

		public void onClick(DialogInterface dialog, int whicButton) {
			alter();
			twoedit = (EditText)dialogView.findViewById(R.id.twoedit);
			threeedit = (EditText)dialogView.findViewById(R.id.threeedit);
			fouredit = (EditText)dialogView.findViewById(R.id.fouredit);
		}
    	
    }
    class DialogInterfaceOnClick4 implements DialogInterface.OnClickListener{//取消

		
		public void onClick(DialogInterface dialog, int whichButton) {
			temp();
		}
    }
    class DialogInterfaceOnClick6 implements DialogInterface.OnClickListener{//退出

		
		public void onClick(DialogInterface dialog, int whichButton) {
			StoreLogin.this.finish();//点击退出按钮之后调用finish方法结束应用程序			
		}
    	
    }
    
    class DialogInterfaceOnClick5 implements DialogInterface.OnClickListener{//修改密码确定

	
		public void onClick(DialogInterface dialog, int whichButton) {
		String two = twoedit.getText().toString();//旧密码
		String three = threeedit.getText().toString();//新密码
		String four = fouredit.getText().toString();//确认新密码
			if(two.equals(password01)&&four.equals(three)){
//				editor = shared.edit();  //打开指定shared文件，如果不存在则创建他
				editor.putString("password", three);
				editor.commit();//提交	
				temp();
				Toast.makeText(StoreLogin.this, "修改成功", Toast.LENGTH_SHORT).show();
			}else if(!two.equals(password01)){
					Toast.makeText(StoreLogin.this, "旧密码输入错误", Toast.LENGTH_SHORT).show();
					temp();
			}else if(!four.equals(three)){
					Toast.makeText(StoreLogin.this, "两次新密码输入不一致", Toast.LENGTH_SHORT).show();
					temp();
			}
		}
    	
    }
}