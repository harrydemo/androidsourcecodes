package siwi.map.android;

import java.io.DataInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Login extends Activity {

	/** Called when the activity is first created. */
	// 1.Զ�̵�¼(�û��������볬��3λ������ɹ�,�ڷ����������Լ����ô���)
	// 2.����loadingЧ��(���̴߳���,ʱ�·�������)
	// 3.��¼���ɹ��ܹ���ʾ����ԭ��
	// 4.�����䛺����Ϣ(���Ñ������ܴa,�����Լ��O��)
	// ע��:
	//1.��Ҫ����androidManifest�ļ�
	// <activity android:name=".IndexPage" android:label="��½���ҳ��"
	// />
	// <activity android:name=".Register" android:label="ע��"
	// />
	// </application>
	// <uses-permission android:name="android.permission.INTERNET" />
	//2.��Ҫ��дһ��������servlet����
//	String userName = request.getParameter("userName");
//	String password = request.getParameter("password");
//	System.out.println(userName+"-"+password);
//	DataOutputStream dos = new DataOutputStream(response.getOutputStream());
//	if (userName != null && password != null) {
//		if (userName.length() > 3 && password.length() > 3) {
//			dos.writeInt(16);
//		}else{
//			dos.writeInt(0);
//		}
//	} else {
//		dos.writeInt(0);
//	}
//	dos.flush();
//	dos.close();
	//3.����������web.xml(�����jsp����Բ�������)
	//4.��Ҫ��Login.class�е��ڲ���LoginFailureHandler��run�����е�validateURL
	private String userName;
	private String password;

	/** ������UI */
	private EditText view_userName;
	private EditText view_password;
	private CheckBox view_rememberMe;
	private Button view_loginSubmit;
	private Button view_loginRegister;
	private static final int MENU_EXIT = Menu.FIRST - 1;
	private static final int MENU_ABOUT = Menu.FIRST;
	/** ��������SharePreferences�ı�ʶ */
	private final String SHARE_LOGIN_TAG = "MAP_SHARE_LOGIN_TAG";

	/** �����¼�ɹ���,���ڱ����û�����SharedPreferences,�Ա��´β������� */
	private String SHARE_LOGIN_USERNAME = "MAP_LOGIN_USERNAME";

	/** �����¼�ɹ���,���ڱ���PASSWORD��SharedPreferences,�Ա��´β������� */
	private String SHARE_LOGIN_PASSWORD = "MAP_LOGIN_PASSWORD";

	/** �����½ʧ��,������Ը��û�ȷ�е���Ϣ��ʾ,true����������ʧ��,false���û������������ */
	private boolean isNetError;

	/** ��¼loading��ʾ�� */
	private ProgressDialog proDialog;

	/** ��¼��̨֪ͨ����UI�߳�,��Ҫ���ڵ�¼ʧ��,֪ͨUI�̸߳��½��� */
	Handler loginHandler = new Handler() {
		public void handleMessage(Message msg) {
			isNetError = msg.getData().getBoolean("isNetError");
			if (proDialog != null) {
				proDialog.dismiss();
			}
			if (isNetError) {
				Toast.makeText(Login.this, "��½ʧ��:\n1.��������������.\n2.����ϵ����.!",
						Toast.LENGTH_SHORT).show();
			}
			// �û������������
			else {
				Toast.makeText(Login.this, "��½ʧ��,��������ȷ���û���������!",
						Toast.LENGTH_SHORT).show();
				// �����ǰ��SharePreferences����
				clearSharePassword();
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		findViewsById();
		initView(false);
		// ��ҪȥsubmitListener��������URL
		setListener();
	}

	/** ��ʼ��ע��View��� */
	private void findViewsById() {
		view_userName = (EditText) findViewById(R.id.loginUserNameEdit);
		view_password = (EditText) findViewById(R.id.loginPasswordEdit);
		view_rememberMe = (CheckBox) findViewById(R.id.loginRememberMeCheckBox);
		view_loginSubmit = (Button) findViewById(R.id.loginSubmit);
		view_loginRegister = (Button) findViewById(R.id.loginRegister);
	}

	/**
	 * ��ʼ������
	 * 
	 * @param isRememberMe
	 *            �����ʱ�����RememberMe,���ҵ�½�ɹ���һ��,��saveSharePreferences(true,ture)��,��ֱ�ӽ���
	 * */
	private void initView(boolean isRememberMe) {
		SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		String userName = share.getString(SHARE_LOGIN_USERNAME, "");
		String password = share.getString(SHARE_LOGIN_PASSWORD, "");
		Log
				.d(this.toString(), "userName=" + userName + " password="
						+ password);
		if (!"".equals(userName)) {
			view_userName.setText(userName);
		}
		if (!"".equals(password)) {
			view_password.setText(password);
			view_rememberMe.setChecked(true);
		}
		// �������Ҳ������,��ֱ���õ�½��ť��ȡ����
		if (view_password.getText().toString().length() > 0) {
			// view_loginSubmit.requestFocus();
			// view_password.requestFocus();
		}
		share = null;
	}

	/**
	 * ����û���½,������ͨ��DataOutputStream��dos.writeInt(int);���ж��Ƿ��¼�ɹ�(
	 * ����������int>0��½�ɹ�,����ʧ��),��½�ɹ������isRememberMe���ж��Ƿ�������(�û����ǻᱣ����),
	 * ������ӷ���������5��,Ҳ������ʧ��.
	 * 
	 * @param userName
	 *            �û���
	 * @param password
	 *            ����
	 * @param validateUrl
	 *            ����½�ĵ�ַ
	 * */
	private boolean validateLocalLogin(String userName, String password,
			String validateUrl) {
		// ���ڱ�ǵ�½״̬
		boolean loginState = false;
		HttpURLConnection conn = null;
		DataInputStream dis = null;
		try {
			URL url = new URL(validateUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			conn.connect();
			dis = new DataInputStream(conn.getInputStream());
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				Log.d(this.toString(),
						"getResponseCode() not HttpURLConnection.HTTP_OK");
				isNetError = true;
				return false;
			}
			// ��ȡ�������ĵ�¼״̬��
			int loginStateInt = dis.readInt();
			if (loginStateInt > 0) {
				loginState = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			isNetError = true;
			Log.d(this.toString(), e.getMessage() + "  127 line");
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		// ��½�ɹ�
		if (loginState) {
			if (isRememberMe()) {
				saveSharePreferences(true, true);
			} else {
				saveSharePreferences(true, false);
			}
		} else {
			// ��������������
			if (!isNetError) {
				clearSharePassword();
			}
		}
		if (!view_rememberMe.isChecked()) {
			clearSharePassword();
		}
		return loginState;
	}

	/**
	 * �����¼�ɹ���,�򽫵�½�û����������¼��SharePreferences
	 * 
	 * @param saveUserName
	 *            �Ƿ��û������浽SharePreferences
	 * @param savePassword
	 *            �Ƿ����뱣�浽SharePreferences
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

	/** ��ס�ҵ�ѡ���Ƿ�ѡ */
	private boolean isRememberMe() {
		if (view_rememberMe.isChecked()) {
			return true;
		}
		return false;
	}

	/** ��¼Button Listener */
	private OnClickListener submitListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			proDialog = ProgressDialog.show(Login.this, "������..",
					"������..���Ժ�....", true, true);
			// ��һ���߳̽��е�¼��֤,��Ҫ������ʧ��,�ɹ�����ֱ��ͨ��startAcitivity(Intent)ת��
			Thread loginThread = new Thread(new LoginFailureHandler());
			loginThread.start();
		}
	};

	// .start();
	// }
	// };

	/** ��ס��checkBoxListener */
	private OnCheckedChangeListener rememberMeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (view_rememberMe.isChecked()) {
				Toast.makeText(Login.this, "�����¼�ɹ�,�Ժ��˺ź�������Զ�����!",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	/** ע��Listener */
	private OnClickListener registerLstener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(Login.this, Register.class);
			// ת��ע��ҳ��
			startActivity(intent);
		}
	};

	/** ���ü����� */
	private void setListener() {
		view_loginSubmit.setOnClickListener(submitListener);
		view_loginRegister.setOnClickListener(registerLstener);
		view_rememberMe.setOnCheckedChangeListener(rememberMeListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_EXIT, 0, getResources().getText(R.string.MENU_EXIT));
		menu.add(0, MENU_ABOUT, 0, getResources().getText(R.string.MENU_ABOUT));
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
		return true;
	}

	/** �������ڶԻ��� */
	private void alertAbout() {
		new AlertDialog.Builder(Login.this).setTitle(R.string.MENU_ABOUT)
				.setMessage(R.string.aboutInfo).setPositiveButton(
						R.string.ok_label,
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {
							}
						}).show();
	}

	/** ������� */
	private void clearSharePassword() {
		SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		share.edit().putString(SHARE_LOGIN_PASSWORD, "").commit();
		share = null;
	}

	class LoginFailureHandler implements Runnable {
		@Override
		public void run() {
			userName = view_userName.getText().toString();
			password = view_password.getText().toString();
			//���ﻻ�������֤��ַ
			String validateURL="http://192.168.1.229:8080/android_server/LoginValidate?userName="
				+ userName + "&password=" + password;
			boolean loginState = validateLocalLogin(userName, password,
					validateURL);
			Log.d(this.toString(), "validateLogin");

			// ��½�ɹ�
			if (loginState) {
				// ��Ҫ�������ݵ���½��Ľ���,
				Intent intent = new Intent();
				intent.setClass(Login.this, IndexPage.class);
				Bundle bundle = new Bundle();
				bundle.putString("MAP_USERNAME", userName);
				intent.putExtras(bundle);
				// ת���½���ҳ��
				startActivity(intent);
				proDialog.dismiss();
			} else {
				// ͨ������handler��֪ͨUI���̸߳���UI,
				Message message = new Message();
				Bundle bundle = new Bundle();
				bundle.putBoolean("isNetError", isNetError);
				message.setData(bundle);
				loginHandler.sendMessage(message);
			}
		}

	}
}
