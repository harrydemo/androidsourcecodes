package tjuci.dl.myweixin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
/**
 * 此类  是对布局main.xml上 控件的操作
 * @author dl
 *
 */
public class MyWeiXinActivity extends Activity implements OnClickListener{
	Button loginBtn,registBtn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        loginBtn = (Button)findViewById(R.id.main_login_btn);
        registBtn = (Button)findViewById(R.id.main_regist_btn);
        loginBtn.setOnClickListener(this);
        registBtn.setOnClickListener(this);
    }
	@Override
	public void onClick(View v) {
		int btnId = v.getId();
		switch (btnId) {//判断点击的按钮
		case R.id.main_login_btn://登录按钮
			Intent intent = new Intent(MyWeiXinActivity.this, LoginActivity.class);
			startActivity(intent);//启动对应的Activity  此处为硬编码  不介意这么写  写成action形式 最好
			Log.i("-------------", "------------------");
			break;

		case R.id.main_regist_btn://注册按钮
			
			break;
		}
	}
}