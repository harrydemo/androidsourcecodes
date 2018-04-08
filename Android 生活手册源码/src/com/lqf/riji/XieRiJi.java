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
	//��������Ҫ�Ŀؼ�
	@SuppressWarnings("unused")
	private EditText title,riqi,neirong;
	private Button baocun,qingchu;
	//�������ݿ�ʵ��������
	private DBhelper helper;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xieriji);
		
		//��ȡ�ؼ�
		title = (EditText) findViewById(R.id.title);
		this.riqi = (EditText) findViewById(R.id.riqi);
		neirong = (EditText) findViewById(R.id.neirong);
		baocun =  (Button) findViewById(R.id.baocun);
		qingchu = (Button) findViewById(R.id.qingkong);
		//��ȡ���ݿ�ʵ��������
		helper = new DBhelper(this);
		//���baocun�Ĵ����¼�
		baocun.setOnClickListener(new OnClickListener() {		
			public void onClick(View v) {
				//�������ӵķ���
				Insert();
				Toast.makeText(XieRiJi.this, "����ɹ�", 20).show();
				
				Intent intent = new Intent(XieRiJi.this,RiJi.class);
				startActivity(intent);
			}
		});
		//���qingchu�Ĵ����¼�
		qingchu.setOnClickListener(new OnClickListener() {		
			public void onClick(View v) {
				title.setText("");
				neirong.setText("");
			}
		});
		
	}
	//�����ռǵķ���
	public void Insert(){
		//��������Ϊ��
    	SimpleDateFormat sdf1 = null;
    	SimpleDateFormat sdf2 = null;
    	//����������ʱ��ĸ�ʽ
    	sdf1 = new SimpleDateFormat("yyyy��MM��dd��");
    	sdf2 = new SimpleDateFormat("HH:mm:ss");
    	//ת��Ϊ�ַ���
    	String a = sdf1.format(new Date());
    	String b = sdf2.format(new Date());
		//��ȡ�ı������ֵ
		String ltitle = title.getText().toString();
		String lriqi = a+"	"+b;
		String lneirong = neirong.getText().toString();
		//��ֵ
		helper.rijiinsert(ltitle, lriqi, lneirong);
	}
}

