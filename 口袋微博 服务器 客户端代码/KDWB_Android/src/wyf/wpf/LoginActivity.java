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
				pd = ProgressDialog.show(LoginActivity.this, "请稍候", "正在连接服务器...", true, true);
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
				android.os.Process.killProcess(android.os.Process.myPid());		//结束进程，退出程序
			}
		});
    }
    //方法：连接服务器进行登录
    public void login(){
    	new Thread(){
    		public void run(){
    			Looper.prepare();
				try{
					if(mc == null){
						mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
					}
					EditText etUid = (EditText)findViewById(R.id.etUid);	//获得帐号EditText
					EditText etPwd = (EditText)findViewById(R.id.etPwd);	//获得密码EditText
					String uid = etUid.getEditableText().toString().trim();	//获得输入的帐号
					String pwd = etPwd.getEditableText().toString().trim();	//获得输入的密码
					if(uid.equals("") || pwd.equals("")){		//判断输入是否为空
						Toast.makeText(LoginActivity.this, "请输入帐号或密码!", Toast.LENGTH_SHORT).show();//输出提示消息
						return;
					}
					String msg = "<#LOGIN#>"+uid+"|"+pwd;					//组织要返回的字符串
					mc.dout.writeUTF(msg);										//发出消息
					String receivedMsg = mc.din.readUTF();		//读取服务器发来的消息
					pd.dismiss();
					if(receivedMsg.startsWith("<#LOGIN_SUCCESS#>")){	//收到的消息为登录成功消息
						receivedMsg = receivedMsg.substring(17);
						String [] sa = receivedMsg.split("\\|");
						CheckBox cb = (CheckBox)findViewById(R.id.cbRemember);		//获得CheckBox对象
						if(cb.isChecked()){
							rememberMe(uid,pwd);
						}
						//转到功能面板
						Intent intent = new Intent(LoginActivity.this,FunctionTabActivity.class);
						intent.putExtra("uno", sa[0]);
						startActivity(intent);						//启动功能Activity
						finish();
					}
					else if(msg.startsWith("<#LOGIN_FAIL#>")){					//收到的消息为登录失败
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
    //方法：将用户的id和密码存入Preferences
    public void rememberMe(String uid,String pwd){
    	SharedPreferences sp = getPreferences(MODE_PRIVATE);	//获得Preferences
    	SharedPreferences.Editor editor = sp.edit();			//获得Editor
    	editor.putString("uid", uid);							//将用户名存入Preferences
    	editor.putString("pwd", pwd);							//将密码存入Preferences
    	editor.commit();
    }
    //方法：从Preferences中读取用户名和密码
    public void checkIfRemember(){
    	SharedPreferences sp = getPreferences(MODE_PRIVATE);	//获得Preferences
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