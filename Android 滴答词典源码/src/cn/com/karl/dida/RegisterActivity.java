package cn.com.karl.dida;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	private EditText registerUserName;
	private EditText registerPassWord;
	private EditText email;
	private ImageView agreementCheckBox;
	private static Boolean isPlay=false;
	private ImageButton backBtn;
	private ImageButton registerBtn02;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register);
		
		registerUserName=(EditText) this.findViewById(R.id.registerUserName);
		registerPassWord=(EditText) this.findViewById(R.id.registerPassWord);
		email=(EditText) this.findViewById(R.id.email);
		agreementCheckBox=(ImageView) this.findViewById(R.id.agreementCheckBox);
		backBtn=(ImageButton) this.findViewById(R.id.backBtn);
		registerBtn02=(ImageButton) this.findViewById(R.id.registerBtn02);
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		agreementCheckBox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isPlay==false){
				agreementCheckBox.setImageResource(R.drawable.checkbox_n);
				isPlay=true;
				}else{
					agreementCheckBox.setImageResource(R.drawable.checkbox_s);
					isPlay=false;	
				}
			}
		});
		registerBtn02.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(registerUserName.getText().toString().trim().equals("")){
					Toast.makeText(getApplicationContext(), "账号不能为空", 1).show();
				}else if(registerPassWord.getText().toString().trim().equals("")){
					Toast.makeText(getApplicationContext(), "密码不能为空", 1).show();
				}else if(email.getText().toString().trim().equals("")){
					Toast.makeText(getApplicationContext(), "邮箱不能为空", 1).show();
				}else{
					Toast.makeText(getApplicationContext(), "对不起此版块暂未开通", 1).show();
					registerUserName.setText("");
					registerPassWord.setText("");
					email.setText("");
				}
			}
		});
		
		
		
	}
}
