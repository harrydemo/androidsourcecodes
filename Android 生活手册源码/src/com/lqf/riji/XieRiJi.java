package com.lqf.riji;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lqf.gerenriji.R;
import com.lqf.sqlite.DBhelper;

public class XieRiJi extends Activity {
	//定义所需要的控件
	@SuppressWarnings("unused")
	private EditText title,riqi,neirong;
	private Button baocun,qingchu;
	//定义数据库实例化对象
	private DBhelper helper;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xieriji);
		
		//获取控件
		title = (EditText) findViewById(R.id.title);
		this.riqi = (EditText) findViewById(R.id.riqi);
		neirong = (EditText) findViewById(R.id.neirong);
		baocun =  (Button) findViewById(R.id.baocun);
		qingchu = (Button) findViewById(R.id.qingkong);
		//获取数据库实例化对象
		helper = new DBhelper(this);
		//点击baocun的触发事件
		baocun.setOnClickListener(new OnClickListener() {		
			public void onClick(View v) {
				//调用增加的方法
				Insert();
				Toast.makeText(XieRiJi.this, "保存成功", 20).show();
				
				Intent intent = new Intent(XieRiJi.this,RiJi.class);
				startActivity(intent);
			}
		});
		//点击qingchu的触发事件
		qingchu.setOnClickListener(new OnClickListener() {		
			public void onClick(View v) {
				title.setText("");
				neirong.setText("");
			}
		});
		
	}
	//增加日记的方法
	public void Insert(){
		//定义日期为空
    	SimpleDateFormat sdf1 = null;
    	SimpleDateFormat sdf2 = null;
    	//定义日期与时间的格式
    	sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
    	sdf2 = new SimpleDateFormat("HH:mm:ss");
    	//转换为字符串
    	String a = sdf1.format(new Date());
    	String b = sdf2.format(new Date());
		//获取文本框传入的值
		String ltitle = title.getText().toString();
		String lriqi = a+"	"+b;
		String lneirong = neirong.getText().toString();
		//传值
		helper.rijiinsert(ltitle, lriqi, lneirong);
	}
}

