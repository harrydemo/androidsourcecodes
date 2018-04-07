package wyf.wpf;

import static wyf.wpf.ConstantUtil.SERVER_ADDRESS;
import static wyf.wpf.ConstantUtil.SERVER_PORT;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class RegActivity extends Activity{
	MyConnector mc = null;			//声明MyConnector对象
	String uno = "";				//记录用户的ID
	ProgressDialog pd= null;		//声明进度对话框
	Handler myHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:		
				View linearLayout = (LinearLayout)findViewById(R.id.regResult);	//获得线性布局
				linearLayout.setVisibility(View.VISIBLE);		//设置可见性
				EditText etUno = (EditText)findViewById(R.id.etUno);
				etUno.setText(uno);		
				break;
			}
			super.handleMessage(msg);
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reg);							//设置当前屏幕
		Button btnReg = (Button)findViewById(R.id.btnReg);		//获得注册Button按钮对象
		btnReg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pd = ProgressDialog.show(RegActivity.this, "请稍候...", "正在连接服务器...", false);
				register();
			}
		});
		Button btnBack = (Button)findViewById(R.id.btnBack);				//获得返回按钮对象
		btnBack.setOnClickListener(new View.OnClickListener() {				//为返回按钮添加监听器
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RegActivity.this,LoginActivity.class);	//创建Intent对象
				startActivity(intent);										//启动Activity
				finish();
			}
		});
		Button btnEnter = (Button)findViewById(R.id.btnEnter);				//获得进入功能面板按钮对象
		btnEnter.setOnClickListener(new View.OnClickListener() {			//为进入功能面板的按钮添加监听器
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RegActivity.this,FunctionTabActivity.class);	//创建Intent
				intent.putExtra("uno", uno);		//设置Extra字段
				startActivity(intent);				//启动FunctionTab
				finish();							//关闭该Activity
			}
		});
	}
	//方法：连接服务器，进行注册
	public void register(){
		new Thread(){
			public void run(){
				Looper.prepare();
				//获得用户输入的数据并进行验证
				EditText etName = (EditText)findViewById(R.id.etName);			//获得昵称EditText对象
				EditText etPwd1 = (EditText)findViewById(R.id.etPwd1);			//获得密码EditText对象
				EditText etPwd2 = (EditText)findViewById(R.id.etPwd2);			//获得确认密码EditText对象
				EditText etEmail = (EditText)findViewById(R.id.etEmail);		//获得邮箱EditText对象
				EditText etStatus = (EditText)findViewById(R.id.etStatus);		//获得心情EditText对象
				String name = etName.getEditableText().toString().trim();		//获得昵称
				String pwd1 = etPwd1.getEditableText().toString().trim();		//获得密码
				String pwd2 = etPwd2.getEditableText().toString().trim();		//获得确认密码
				String email = etEmail.getEditableText().toString().trim();		//获得邮箱
				String status = etStatus.getEditableText().toString().trim();	//获得状态
				if(name.equals("") || pwd1.equals("") || pwd2.equals("") || email.equals("") || status.equals("")){
					Toast.makeText(RegActivity.this, "请将注册信息填写完整", Toast.LENGTH_LONG).show();
					return;
				}
				if(!pwd1.equals(pwd2)){				//判断两次输入的密码是否一致
					Toast.makeText(RegActivity.this, "两次输入的密码不一致！", Toast.LENGTH_LONG).show();
					return;
				}
				try{
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
					String regInfo = "<#REGISTER#>"+name+"|"+pwd1+"|"+email+"|"+status;
					mc.dout.writeUTF(regInfo);
					String result = mc.din.readUTF();
					pd.dismiss();
					if(result.startsWith("<#REG_SUCCESS#>")){		//返回信息为注册成功
						result= result.substring(15);		//去掉信息头
						uno = result;				//记录用户的ID
						myHandler.sendEmptyMessage(0);				//发出Handler消息
						Toast.makeText(RegActivity.this, "注册成功！", Toast.LENGTH_LONG).show();
					}
					else{		//注册失败
						Toast.makeText(RegActivity.this, "注册失败！请重试！", Toast.LENGTH_LONG).show();
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}.start();
	}
	@Override
	protected void onDestroy() {
		if(mc != null){
			mc.sayBye();
		}
		super.onDestroy();
	}
}