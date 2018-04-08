package com.oauthTest.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.oauthTest.R;
import com.oauthTest.utils.ConfigUtil;

/**
 * 登录选择界面
 *		1.根据不同的按钮点击初始化数据 		
 * @author bywyu
 */
public class MainAct extends Activity implements OnClickListener{
	private final String LOGTAG = "MainAct";

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button sinaBtn = (Button) findViewById(R.id.sinaBtn);
        sinaBtn.setOnClickListener(this);
        sinaBtn.setTag(ConfigUtil.SINAW);
        
        Button qqBtn = (Button) findViewById(R.id.qqBtn);
        qqBtn.setOnClickListener(this);
        qqBtn.setTag(ConfigUtil.QQW);
        
        Button sohuBtn = (Button) findViewById(R.id.sohuBtn);
        sohuBtn.setOnClickListener(this);
        sohuBtn.setTag(ConfigUtil.SOHUW);
        
        Button wangyiBtn = (Button) findViewById(R.id.wangyiBtn);
        wangyiBtn.setOnClickListener(this);
        wangyiBtn.setTag(ConfigUtil.WANGYIW);
    }

	@Override
    public void onClick(View v) {
		
		//初始化ConfigUtil信息
		ConfigUtil conf = ConfigUtil.getInstance();
		String curWeibo = String.valueOf(v.getTag());
		conf.setCurWeibo(curWeibo);
		
		if(curWeibo.equals(ConfigUtil.SINAW)){
			conf.initSinaData();
		}else if(curWeibo.equals(ConfigUtil.QQW)){
			conf.initQqData();
		}else if(curWeibo.equals(ConfigUtil.SOHUW)){
			conf.initSohuData();
		}else if(curWeibo.equals(ConfigUtil.WANGYIW)){
			conf.initWangyiData();
		}
		
		Intent intent = new Intent(MainAct.this,AuthorizationAct.class);
		startActivity(intent);
    }
}