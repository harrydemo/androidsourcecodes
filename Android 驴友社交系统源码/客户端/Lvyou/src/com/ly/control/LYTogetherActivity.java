package com.ly.control;

import java.io.InputStream;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.ly.control.R;
import com.ly.bean.GetTogetherBean;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class LYTogetherActivity extends Activity {
	private  ArrayList<String[]> list = new ArrayList<String[]>();
	private ListView listtogether;
	private String s1,s2,s3,s4;
	private String id;
	private Button bt1,bt2;
	private int count = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lytogether);
		listtogether = (ListView) findViewById(R.id.FXListView01);
		bt1 = (Button) findViewById(R.id.btnLeft);
		bt2 = (Button) findViewById(R.id.btnRight);
		id=getIntent().getStringExtra("result");
		 checkButton();
		Thread t = new Thread(r);
		t.start();
		bt1.setOnClickListener(l);
		bt2.setOnClickListener(l);
	}
	 private void checkButton() { 
		  //索引值小于等于0，表示不能向前翻页了，已经到了第一页了。
		  //将向前翻页的按钮设为不可用。
		  if(count <=0){
		    bt1.setEnabled(false);
		    bt2.setEnabled(true);
		  }
		   /**值的长度减去前几页的长度，剩下的就是这一页的长度，
		    * 如果这一页的长度比View_Count小，
		    * 表示这是最后的一页了，后面在没有了。*/
		   //将向后翻页的按钮设为不可用。
		  else if(list.size()<=5){
		     bt2.setEnabled(false);
		     bt1.setEnabled(true);
		  }
		  //否则将2个按钮都设为可用的。
		   else {
		    bt1.setEnabled(true);
		    bt2.setEnabled(true);
		   }
		 } 
	private OnClickListener l = new OnClickListener() {
		
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v==bt1){		
				count--;
				 Thread t = new Thread(r);
				t.start();
				  checkButton();
			}
			if(v==bt2){
				count++;
				Thread t = new Thread(r);
				t.start();
			 checkButton();
			}
		}
	};
	Runnable r = new Runnable(){
		


		public void run() {
			// TODO Auto-generated method stub
//			XMLHandler xh = new XMLHandler();
			try {
				URL url = new URL("http://10.0.2.2:8080/Lvyou/ReceiveServlet");
				HttpURLConnection htc = (HttpURLConnection) url.openConnection();
				htc.setRequestMethod("POST");
				htc.setDoInput(true);
				htc.setDoOutput(true);
				OutputStream out = htc.getOutputStream();
				StringBuilder sb = new StringBuilder();
				sb.append("<user>");
				sb.append("<flag>");
				sb.append(count);
				sb.append("</flag>");
				sb.append("</user>");
				byte userXml[] = sb.toString().getBytes();
				out.write(userXml);
				if(htc.getResponseCode()==HttpURLConnection.HTTP_OK){
					InputStream in = htc.getInputStream();
					GetTogetherBean gt = new GetTogetherBean();
					list =gt.gettogether(in);
					Message msg = new Message();
					msg.obj=list;
					h.sendMessage(msg);
					
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	};
	Handler  h=new  Handler()
	{
		  public void handleMessage(android.os.Message msg) {
			 
			  list=(ArrayList<String[]>) msg.obj;
			  listtogether.setAdapter(new myadapter(LYTogetherActivity.this , list));
			  listtogether.setOnItemClickListener(new OnItemClickListener() {

		
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					s1=list.get(arg2)[4];
					s2=list.get(arg2)[3];
					s3=list.get(arg2)[2];
					
					Intent intent =new Intent(LYTogetherActivity.this,TravelActivity.class);
					intent.putExtra("id", id);
					intent.putExtra("title", s1);
					intent.putExtra("time", s2);
					intent.putExtra("content", s3);
					
					startActivity(intent);
				}
			});
			  
		  };
	};
	private class myadapter extends BaseAdapter{
		private Context c;
		private ArrayList<String[]> list;
		public myadapter(Context c, ArrayList<String[]> list){
			this.c=c;
			this.list=list;
		}
		
			public int getCount() {
				// TODO Auto-generated method stub
				return list.size();
			}

		
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return list.get(position);
			}

			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

	
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				View v = LayoutInflater.from(c).inflate(R.layout.togetheritem, null);
				ImageView iv = (ImageView) v.findViewById(R.id.fImg);
				TextView tv = (TextView) v.findViewById(R.id.uname);
				TextView tv1 = (TextView) v.findViewById(R.id.title1);
				TextView tv2 = (TextView) v.findViewById(R.id.address1);
				TextView tv3 = (TextView) v.findViewById(R.id.content1);
				TextView tv4 = (TextView) v.findViewById(R.id.content2);
				String uname = list.get(position)[0];
				String content = list.get(position)[2];
				String pic = list.get(position)[1];
				String time = list.get(position)[3];
				String title = list.get(position)[4];
				String gtime = list.get(position)[5];
				tv3.setText(content);
				tv1.setText(title);
				tv2.setText(time);
				tv.setText(uname);
				tv4.setText(gtime);
				try {
					URL url = new URL("http://10.0.2.2:8080/Lvyou/pic/"+pic);
					HttpURLConnection htc = (HttpURLConnection) url.openConnection();
					InputStream in = htc.getInputStream();
					Bitmap bit = BitmapFactory.decodeStream(in);
					iv.setImageBitmap(bit);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				return v;
			}
			
		}
}
