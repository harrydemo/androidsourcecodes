package com.ly.control;
import java.io.InputStream;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ly.control.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class LYOtherActivity extends Activity {
private String s;
	private ListView lvother;
	private String uname,pic;
	private	HashMap<String,Object> map;
	private ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
	private String []pname={"�û�ע��","��½","�½�����","����δ�����ļ���","��������"};
	private String []pcontent={"���û�����ݵ�¼����ϵͳ","���û�����ݵ�¼����ϵͳ","����һ���µ����μ���","��ԭ�������ļ�������ϼ������Ƽ�������","����һ���������Ϣ"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lyother);

		lvother=(ListView) findViewById(R.id.ListViewOther01);
		
		s = getIntent().getStringExtra("result");
	
		for(int i=0;i<=4;i++){
			map= new HashMap<String,Object>();
			map.put("uname", pname[i].toString());
			map.put("ucontent",pcontent[i].toString());
			list.add(map);
			
		}
		SimpleAdapter sa = new SimpleAdapter( this,list,R.layout.lyother2,new String []{"uname","ucontent"},new int []{R.id.TextView01,R.id.TextView02});
		
		lvother.setAdapter(sa);
		lvother.setOnItemClickListener(listener);

			
		
	}
	private OnItemClickListener listener = new OnItemClickListener() {

	
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			
			if(arg2==0)
			{
				Intent intent = new Intent(LYOtherActivity.this,LYRegisterActivity.class);
				startActivity(intent);
			}
			if(arg2==1&&s==null)
			{
				Intent intent = new Intent(LYOtherActivity.this,LYLoginActivity.class);
				startActivity(intent);
			}
			if(arg2==1&&s!=null)
			{
				Toast.makeText(LYOtherActivity.this, "�Ѿ���¼�������ٴε�¼��", Toast.LENGTH_LONG).show();
			}
			if(arg2==2&&s!=null)
			{
				
				Intent intent = new Intent(LYOtherActivity.this,LYAddMemoryActivity.class);
				intent.putExtra("result", s);
				startActivity(intent);
			}
			if(arg2==2&&s==null)
			{
				
				Toast.makeText(LYOtherActivity.this, "���½����ʹ�øù��ܣ�", Toast.LENGTH_LONG).show();
			}
			if(arg2==3&&s!=null){
				Intent intent = new Intent(LYOtherActivity.this,StoreMemoryActivity.class);
				intent.putExtra("result", s);
				startActivity(intent);
			}
			if(arg2==3&&s==null){
				
				Toast.makeText(LYOtherActivity.this, "���½����ʹ�øù��ܣ�", Toast.LENGTH_LONG).show();
			}
			if(arg2==4&&s!=null)
			{
				Intent intent = new Intent(LYOtherActivity.this,LYTogether2Activity.class);
				intent.putExtra("result", s);
				startActivity(intent);
			}
			if(arg2==4&&s==null)
			{
				
				Toast.makeText(LYOtherActivity.this, "���½����ʹ�øù��ܣ�", Toast.LENGTH_LONG).show();
			}
			
		}
	};


}
