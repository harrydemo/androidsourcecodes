package com.php.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.php.Json2Lv;
import com.php.R;

public class Register extends Activity {

	private String userName;
	private String email;
	private String password;
	private String strResult;
	
	private EditText view_userName;
	private EditText view_email;
	private EditText view_password;
	private EditText view_passwordConfirm;
	private Button view_submit;
	private Button view_clearAll;
	private static final int MENU_EXIT = Menu.FIRST - 1;
	private static final int MENU_ABOUT = Menu.FIRST;
	StringBuilder suggest;
	/** 用来操作SharePreferences的标识 */
	private final String SHARE_LOGIN_TAG = "MAP_SHARE_LOGIN_TAG";

	/** 如果登录成功后,用于保存用户名到SharedPreferences,以便下次不再输入 */
	private String SHARE_LOGIN_USERNAME = "MAP_LOGIN_USERNAME";

	/** 如果登录成功后,用于保存PASSWORD到SharedPreferences,以便下次不再输入 */
	private String SHARE_LOGIN_PASSWORD = "MAP_LOGIN_PASSWORD";

	/** 如果登陆失败,这个可以给用户确切的消息显示,true是网络连接失败,false是用户名和密码错误 */
	private boolean isNetError;
	/** 注册loading提示框 */
	private ProgressDialog proDialog;
	
	/** 登录后台通知更新UI线程,主要用于登录失败,通知UI线程更新界面 */
	Handler registerHandler = new Handler() {
		public void handleMessage(Message msg) {
			isNetError = msg.getData().getBoolean("isNetError");
			if (proDialog != null) {
				proDialog.dismiss();
			}
			else if (isNetError) {
				Toast.makeText(Register.this, "注册失败:\n1.请检查您网络连接.\n2.请联系我们.!",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		findViews();
		setListener();
	}

	/** 1.初始化注册view组件 */
	private void findViews() {
		view_userName = (EditText) findViewById(R.id.registerUserName);
		view_email = (EditText) findViewById(R.id.registerEmail);
		view_password = (EditText) findViewById(R.id.registerPassword);
		view_passwordConfirm = (EditText) findViewById(R.id.registerPasswordConfirm);
		view_submit = (Button) findViewById(R.id.registerSubmit);
		view_clearAll = (Button) findViewById(R.id.registerClear);
	}

	private void setListener() {
		view_submit.setOnClickListener(submitListener);
		view_clearAll.setOnClickListener(clearListener);
		/** 验证邮箱格式 */
		view_email.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				String strPattern = "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
				String email = view_email.getText().toString();
				Pattern p = Pattern.compile(strPattern);
				Matcher m = p.matcher(email);
				if(!m.matches()){
					Toast.makeText(Register.this, "邮箱格式错误，请重输！",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	/** 监听注册确定按钮 */
	private OnClickListener submitListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String userName = view_userName.getText().toString();
			String email = view_email.getText().toString();
			String password = view_password.getText().toString();
			String passwordConfirm = view_passwordConfirm.getText().toString();
			validateForm(userName,email,password, passwordConfirm);
			if(suggest.length()==0){
				proDialog = ProgressDialog.show(Register.this, "注册中..",
						"连接中..请稍后....", true, true);
				// 开一个线程进行注册,主要是用于失败,成功可以直接通过startAcitivity(Intent)转向
				Thread registerThread = new Thread(new RegisterHandler());
				registerThread.start();
			}
		}
	};

	/** 清空监听按钮 */
	private OnClickListener clearListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			clearForm();
		}
	};

	/** 验证注册表单 */
	private void validateForm(String userName, String email,String password, String password2) {
		suggest = new StringBuilder();
		Log.d(this.toString(), "validate");
		if (userName.length() < 1) {
			suggest.append(getText(R.string.suggust_userName) + "\n");
		}
		if (email.length() < 1) {
			suggest.append(getText(R.string.suggust_email) + "\n");
		}
		if (password.length() < 1 || password2.length() < 1) {
			suggest.append(getText(R.string.suggust_passwordNotEmpty) + "\n");
		}
		if (!password.equals(password2)) {
			suggest.append(getText(R.string.suggest_passwordNotSame));
		}
		if (suggest.length() > 0) {
			//sub是
			Toast.makeText(this, suggest.subSequence(0, suggest.length() - 1),
					Toast.LENGTH_SHORT).show();
		}
	}

	/** 清空表单 */
	private void clearForm() {
		view_userName.setText("");
		view_password.setText("");
		view_passwordConfirm.setText("");

		view_userName.requestFocus();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_EXIT, 0, R.string.MENU_EXIT);
		menu.add(0, MENU_ABOUT, 0, R.string.MENU_ABOUT);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		super.onMenuItemSelected(featureId, item);
		switch (item.getItemId()) {
		case MENU_EXIT:
			finish();
			break;
		case MENU_ABOUT:
			alertAbout();
			break;
		}
		;
		return true;
	}

	/** 弹出关于对话�?*/
	private void alertAbout() {
		new AlertDialog.Builder(Register.this).setTitle(R.string.MENU_ABOUT)
				.setMessage(R.string.aboutInfo).setPositiveButton(
						R.string.ok_label,
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {
							}
						}).show();
	}
	
	private boolean validateLocalRegister(String userName, String email, String password,
			String validateUrl) {
		System.out.println("username:"+userName);
		System.out.println("password:"+password);
		// 用于标记登陆状态
		boolean registerState = false;

		HttpPost httpRequest =new HttpPost(validateUrl);
		//Post运作传送变数必须用NameValuePair[]阵列储存

		//传参数 服务端获取的方法为request.getParameter("name")

		List params=new ArrayList();
		params.add(new BasicNameValuePair("username",userName));
		params.add(new BasicNameValuePair("password",password));	
		params.add(new BasicNameValuePair("email",email));	
		
		try{

			//发出HTTP request

			httpRequest.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));

			//取得HTTP response
			

			HttpResponse httpResponse=new DefaultHttpClient().execute(httpRequest);
			System.out.println("确认状态码"+httpResponse.getStatusLine().getStatusCode());
			
			 //若状态码为200 ok
  
			if(httpResponse.getStatusLine().getStatusCode()==200){
			
			//取出回应字串
				
			strResult=EntityUtils.toString(httpResponse.getEntity());
			
			System.out.println("strResult: "+strResult);
			}
		}catch(Exception e){
			e.printStackTrace();}

          if(strResult.equals("<script>window.location.href='index.php'</script>")){
        	  registerState = true;
          }
		
		// 注册成功
		if (registerState) {
			
				saveSharePreferences(true, true);
		} 
		return registerState;
	}

	/**
	 * 如果成功注册,则将登陆用户名和密码记录在SharePreferences
	 * 
	 * @param saveUserName
	 *            是否将用户名保存到SharePreferences
	 * @param savePassword
	 *            是否将密码保存到SharePreferences
	 * */
	private void saveSharePreferences(boolean saveUserName, boolean savePassword) {
		SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		if (saveUserName) {
			Log.d(this.toString(), "saveUserName="
					+ view_userName.getText().toString());
			share.edit().putString(SHARE_LOGIN_USERNAME,
					view_userName.getText().toString()).commit();
		}
		if (savePassword) {
			share.edit().putString(SHARE_LOGIN_PASSWORD,
					view_password.getText().toString()).commit();
		}
		share = null;
	}
	
	class RegisterHandler implements Runnable {
		@Override
		public void run() {
			userName = view_userName.getText().toString();
			email = view_email.getText().toString();
			password = view_password.getText().toString();
			//这里换成你的验证地址
			String validateURL="http://10.0.2.2/jqueryRegister02/user.php";
			boolean registerState = validateLocalRegister(userName,email, password,
					validateURL);
			Log.d(this.toString(), "validateRegister");

			// 登陆成功
			if (registerState) {
				// 需要传输数据到登陆后的界面,
				Intent intent = new Intent();
				intent.setClass(Register.this, Json2Lv.class);
				Bundle bundle = new Bundle();
				bundle.putString("MAP_USERNAME", userName);
				intent.putExtras(bundle);
				// 转向登陆后的页面
				startActivity(intent);
				proDialog.dismiss();
			} else {
				// 通过调用handler来通知UI主线程更新UI,
				Message message = new Message();
				Bundle bundle = new Bundle();
				bundle.putBoolean("isNetError", isNetError);
				message.setData(bundle);
				registerHandler.sendMessage(message);
			}
		}
	}
}
