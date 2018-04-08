package wyf.wpf;
import static wyf.wpf.ConstantUtil.SERVER_ADDRESS;
import static wyf.wpf.ConstantUtil.SERVER_PORT;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class LoginActivity extends Activity {
	MyConnector mc = null;
	ProgressDialog pd;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        checkIfRemember();
        Button btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pd = ProgressDialog.show(LoginActivity.this, "���Ժ�", "�������ӷ�����...", true, true);
				login();
			}
		});
        Button btnReg = (Button)findViewById(R.id.btnReg);
        btnReg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, wyf.wpf.RegActivity.class);
				startActivity(intent);
				finish();
			}
		});
        ImageButton ibExit = (ImageButton)findViewById(R.id.ibExit);
        ibExit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				android.os.Process.killProcess(android.os.Process.myPid());		//�������̣��˳�����
			}
		});
    }
    //���������ӷ��������е�¼
    public void login(){
    	new Thread(){
    		public void run(){
    			Looper.prepare();
				try{
					if(mc == null){
						mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
					}
					EditText etUid = (EditText)findViewById(R.id.etUid);	//����ʺ�EditText
					EditText etPwd = (EditText)findViewById(R.id.etPwd);	//�������EditText
					String uid = etUid.getEditableText().toString().trim();	//���������ʺ�
					String pwd = etPwd.getEditableText().toString().trim();	//������������
					if(uid.equals("") || pwd.equals("")){		//�ж������Ƿ�Ϊ��
						Toast.makeText(LoginActivity.this, "�������ʺŻ�����!", Toast.LENGTH_SHORT).show();//�����ʾ��Ϣ
						return;
					}
					String msg = "<#LOGIN#>"+uid+"|"+pwd;					//��֯Ҫ���ص��ַ���
					mc.dout.writeUTF(msg);										//������Ϣ
					String receivedMsg = mc.din.readUTF();		//��ȡ��������������Ϣ
					pd.dismiss();
					if(receivedMsg.startsWith("<#LOGIN_SUCCESS#>")){	//�յ�����ϢΪ��¼�ɹ���Ϣ
						receivedMsg = receivedMsg.substring(17);
						String [] sa = receivedMsg.split("\\|");
						CheckBox cb = (CheckBox)findViewById(R.id.cbRemember);		//���CheckBox����
						if(cb.isChecked()){
							rememberMe(uid,pwd);
						}
						//ת���������
						Intent intent = new Intent(LoginActivity.this,FunctionTabActivity.class);
						intent.putExtra("uno", sa[0]);
						startActivity(intent);						//��������Activity
						finish();
					}
					else if(msg.startsWith("<#LOGIN_FAIL#>")){					//�յ�����ϢΪ��¼ʧ��
						Toast.makeText(LoginActivity.this, msg.substring(14), Toast.LENGTH_LONG).show();
						Looper.loop();
						Looper.myLooper().quit();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
    		}
    	}.start();
    }
    //���������û���id���������Preferences
    public void rememberMe(String uid,String pwd){
    	SharedPreferences sp = getPreferences(MODE_PRIVATE);	//���Preferences
    	SharedPreferences.Editor editor = sp.edit();			//���Editor
    	editor.putString("uid", uid);							//���û�������Preferences
    	editor.putString("pwd", pwd);							//���������Preferences
    	editor.commit();
    }
    //��������Preferences�ж�ȡ�û���������
    public void checkIfRemember(){
    	SharedPreferences sp = getPreferences(MODE_PRIVATE);	//���Preferences
    	String uid = sp.getString("uid", null);
    	String pwd = sp.getString("pwd", null);
    	if(uid != null && pwd!= null){
    		EditText etUid = (EditText)findViewById(R.id.etUid);
    		EditText etPwd = (EditText)findViewById(R.id.etPwd);
    		CheckBox cbRemember = (CheckBox)findViewById(R.id.cbRemember);
    		etUid.setText(uid);
    		etPwd.setText(pwd);
    		cbRemember.setChecked(true);
    	}
    }
	@Override
	protected void onDestroy() {
		if(mc != null){
			mc.sayBye();
		}
		super.onDestroy();
	}
}