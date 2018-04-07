package siwi.map.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity {

	private EditText view_userName;
	private EditText view_password;
	private EditText view_passwordConfirm;
	private Button view_submit;
	private Button view_clearAll;
	private static final int MENU_EXIT = Menu.FIRST - 1;
	private static final int MENU_ABOUT = Menu.FIRST;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		findViews();
		setListener();
		// 然后执行注册的事情,访问远程服务器注册用户
	}

	/** 1.初始化注册view组件 */
	private void findViews() {
		view_userName = (EditText) findViewById(R.id.registerUserName);
		view_password = (EditText) findViewById(R.id.registerPassword);
		view_passwordConfirm = (EditText) findViewById(R.id.registerPasswordConfirm);
		view_submit = (Button) findViewById(R.id.registerSubmit);
		view_clearAll = (Button) findViewById(R.id.registerClear);
	}

	private void setListener() {
		view_submit.setOnClickListener(submitListener);
		view_clearAll.setOnClickListener(clearListener);
	}

	/** 监听注册确定按钮 */
	private OnClickListener submitListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String userName = view_userName.getText().toString();
			String password = view_password.getText().toString();
			String passwordConfirm = view_passwordConfirm.getText().toString();
			validateForm(userName, password, passwordConfirm);
		}
	};

	/** 清空监听按钮 */
	private OnClickListener clearListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			clearForm();
		}
	};

	/** 检查注册表单 */
	private void validateForm(String userName, String password, String password2) {
		StringBuilder suggest = new StringBuilder();
		Log.d(this.toString(), "validate");
		if (userName.length() < 1) {
			suggest.append(getText(R.string.suggust_userName) + "\n");
		}
		if (password.length() < 1 || password2.length() < 1) {
			suggest.append(getText(R.string.suggust_passwordNotEmpty) + "\n");
		}
		if (!password.equals(password2)) {
			suggest.append(getText(R.string.suggest_passwordNotSame));
		}
		if (suggest.length() > 0) {
			//sub是因为要出去最后一个 \n换行符
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

	/** 弹出关于对话框 */
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
}
