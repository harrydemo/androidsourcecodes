package cn.com.karl.dida;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class TestsActivity extends Activity {
    private EditText userName;
    private EditText password;
    private ImageButton registerBtn;
    private ImageButton loginBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tests);
		
		userName=(EditText) this.findViewById(R.id.userName);
		password=(EditText) this.findViewById(R.id.passWord);
		registerBtn=(ImageButton) this.findViewById(R.id.registerBtn01);
		loginBtn=(ImageButton) this.findViewById(R.id.loginBtn);
		registerBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				userName.setText("");
				password.setText("");
				Intent intent=new Intent(TestsActivity.this,RegisterActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.fade, R.anim.hold);
				
			}
		});
		loginBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(userName==null || userName.getText().toString().trim().equals("")){
					Toast.makeText(getApplicationContext(), "�û�������Ϊ��", 1).show();
				}else{
					if(password==null || password.getText().toString().trim().equals("")){
						Toast.makeText(getApplicationContext(), "���벻��Ϊ��", 1).show();
					}else{
						Toast.makeText(getApplicationContext(), "�Բ���˰����δ��ͨ", 1).show();
						userName.setText("");
						password.setText("");
						
					}
				}
			}
		});
		
		
		
		
		
	}
	
}
