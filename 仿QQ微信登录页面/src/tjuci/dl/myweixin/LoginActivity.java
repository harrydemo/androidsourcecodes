package tjuci.dl.myweixin;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
/**
 * ����  �ǶԲ���main.xml�� �ؼ��Ĳ���
 * @author dl
 *
 */
public class LoginActivity extends Activity implements OnClickListener{
	Button rebackBtn,loginBtn,forgetPasswdBtn;
	EditText userEdit,passwdEdit;
	PopupWindow popup ;
	RelativeLayout loginLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        rebackBtn = (Button)findViewById(R.id.login_reback_btn);
        rebackBtn.setOnClickListener(this);//ע�������  һ��������
        loginBtn = (Button)findViewById(R.id.login_login_btn);
        loginBtn.setOnClickListener(this);//ע�������  һ��������
        passwdEdit = (EditText)findViewById(R.id.login_passwd_edit);
        userEdit = (EditText)findViewById(R.id.login_user_edit);
        forgetPasswdBtn = (Button)findViewById(R.id.forget_passwd);
        forgetPasswdBtn.setOnClickListener(this);
        loginLayout = (RelativeLayout)findViewById(R.id.login_layout);
    }
	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		switch (viewId) {
		case R.id.login_reback_btn://���ذ�ť
			LoginActivity.this.finish();//�ر����Activity  ������һ��Activity
			break;
		case R.id.login_login_btn://�����¼��ť   �����ж�  �û����������Ƿ�Ϊ��
			String userEditStr = userEdit.getText().toString().trim();
			String passwdEditStr = passwdEdit.getText().toString().trim();
			if(("".equals(userEditStr) || null == userEditStr) || 
					("".equals(passwdEditStr) || null == passwdEditStr)){//ֻҪ�û�����������һ��Ϊ��
				new AlertDialog.Builder(LoginActivity.this)
				.setIcon(getResources().getDrawable(R.drawable.login_error_icon))
				.setTitle("��¼ʧ��")
				.setMessage("΢���˺Ż����벻��Ϊ�գ�������΢���˺Ż�����")
				.create().show();
			}
			break;
		case R.id.forget_passwd://���  ���������롱 ����ı�
			forgetPasswdBtn.setTextColor(Color.RED);//�ı���ɺ�ɫ
			View view = LayoutInflater.from(LoginActivity.this).inflate(R.layout.login_dialog, null);
			popup = new PopupWindow(view, AbsListView.LayoutParams.FILL_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
			popup.showAsDropDown(forgetPasswdBtn);
			popup.setFocusable(false);
			popup.setOutsideTouchable(true);
			popup.showAtLocation(forgetPasswdBtn, Gravity.CENTER, 0, 0);
			popup.update();
			loginLayout.setBackgroundColor(Color.GRAY);
			forgetPasswdBtn.setBackgroundColor(Color.GRAY);
			forgetPasswdBtn.setEnabled(false);
			break;
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(popup!= null && popup.isShowing()){
			popup.dismiss();
			loginLayout.setBackgroundColor(Color.WHITE);
			forgetPasswdBtn.setBackgroundColor(Color.WHITE);
			forgetPasswdBtn.setEnabled(true);
		}
		return super.onTouchEvent(event);
	}
}