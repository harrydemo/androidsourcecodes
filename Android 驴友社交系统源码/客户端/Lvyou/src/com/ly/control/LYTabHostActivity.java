package com.ly.control;


import java.io.InputStream;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ly.control.R;
import com.ly.bean.FriendListBean;
import com.ly.bean.RMsgBean;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class LYTabHostActivity extends TabActivity {
	private String uid;
	private ArrayList<String[]> list;
	//private ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    uid = getIntent().getStringExtra("result");

		TabHost  tabhost = getTabHost();
		Drawable home = getResources().getDrawable(R.drawable.memory);
		Drawable friends = getResources().getDrawable(R.drawable.together);
	    Drawable photos = getResources().getDrawable(R.drawable.shan);
		Drawable mypage = getResources().getDrawable(R.drawable.other);
		
		TabHost.TabSpec  tabspec1 = tabhost.newTabSpec("旅游记忆");
		tabspec1.setIndicator("旅游记忆",home);
		Intent g_intent = new Intent(LYTabHostActivity.this,LYMemoryActivity.class);
		String id = getIntent().getStringExtra("result");
		g_intent.putExtra("result", id);
		tabspec1.setContent(g_intent);
		tabhost.addTab(tabspec1);
		
		TabHost.TabSpec  tabspec2 = tabhost.newTabSpec("结伴游");
		tabspec2.setIndicator("结伴游",friends);
		Intent g_intent1 = new Intent(LYTabHostActivity.this,LYTogetherActivity.class);
		String id2 = getIntent().getStringExtra("result");
		g_intent1.putExtra("result", id2);
		tabspec2.setContent(g_intent1);
		tabhost.addTab(tabspec2);
	
		
		TabHost.TabSpec  tabspec4 = tabhost.newTabSpec("好友/关注者");
		tabspec4.setIndicator("好友/关注者",photos);
		Intent g_intent3 = new Intent(LYTabHostActivity.this,LYFriendsActivity.class);
		String id3 = getIntent().getStringExtra("result");
		g_intent3.putExtra("result", id3);
		tabspec4.setContent(g_intent3);
		tabhost.addTab(tabspec4);
		
		TabHost.TabSpec  tabspec5 = tabhost.newTabSpec("其他功能");
		tabspec5.setIndicator("其他功能",mypage);
		Intent g_intent4 = new Intent(LYTabHostActivity.this,LYOtherActivity.class);
		String s = getIntent().getStringExtra("result");
		g_intent4.putExtra("result", s);
		tabspec5.setContent(g_intent4);
		tabhost.addTab(tabspec5);
		if(uid!=null){
			msg();
		}
	}
		private void delmsg(){
			
			try {
				URL url = new URL("http://10.0.2.2:8080/Lvyou/DelMsgServlet");
				HttpURLConnection htc = (HttpURLConnection) url.openConnection();
				htc.setDoInput(true);
				htc.setDoOutput(true);
				htc.setRequestMethod("POST");
				
				OutputStream out= htc.getOutputStream();
				StringBuilder sb = new StringBuilder();
				
				sb.append("<user>");
				sb.append("<uid>");					
				sb.append(uid);
				sb.append("</uid>");
				sb.append("</user>");
				
				byte userXML[] = sb.toString().getBytes();
				out.write(userXML);
				
				if(htc.getResponseCode()==HttpURLConnection.HTTP_OK)
				{
//					InputStream in =htc.getInputStream();
//					RMsgBean fmb= new RMsgBean();
//					list = fmb.rmsg(in);
//					
//					Message msg = new Message();
//					msg.obj=list;
//					h.sendMessage(msg);
				
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//pd.cancel();
		}
		

		private  void msg() {
			//LoginUserHandler luh = new LoginUserHandler();
			
			try {
				URL url = new URL("http://10.0.2.2:8080/Lvyou/RMsgServlet");
				HttpURLConnection htc = (HttpURLConnection) url.openConnection();
				htc.setDoInput(true);
				htc.setDoOutput(true);
				htc.setRequestMethod("POST");
				
				OutputStream out= htc.getOutputStream();
				StringBuilder sb = new StringBuilder();
				
				sb.append("<user>");
				sb.append("<uid>");					
				sb.append(uid);
				sb.append("</uid>");
				sb.append("</user>");
				
				byte userXML[] = sb.toString().getBytes();
				out.write(userXML);
				
				if(htc.getResponseCode()==HttpURLConnection.HTTP_OK)
				{
					InputStream in =htc.getInputStream();
					RMsgBean fmb= new RMsgBean();
					list = fmb.rmsg(in);
					
					Message msg = new Message();
					msg.obj=list;
					h.sendMessage(msg);
				
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	
	Handler  h=new  Handler()
	  {
		 public void handleMessage(android.os.Message msg) {
			
			 list =(ArrayList<String[]>) msg.obj;
			 
			 Map<String, String> childdata =null;
			 
			 if(list.size()==0){
				 //Toast.makeText(getApplicationContext(), "未登录", Toast.LENGTH_SHORT).show();
			 }else{
				
				 showmsg();
			 }
			 
			
			
			
			// System.out.println(list.size()+":handler...");
		  };
		 
	  };
	private void showmsg(){
		  AlertDialog.Builder up = new AlertDialog.Builder(LYTabHostActivity.this);
		up.setIcon(R.drawable.info);
		up.setTitle("未读消息");
		View vv = LayoutInflater.from(LYTabHostActivity.this).inflate(R.layout.msg, null);
		ListView landmark=(ListView)vv.findViewById(R.id.ListView01);
		landmark.setAdapter(new myadapter(LYTabHostActivity.this , list));
		//landmark.setAdapter(sa);
		up.setView(vv);
		up.setNegativeButton("已读", new DialogInterface.OnClickListener() {
			
		public void onClick(DialogInterface dialog, int which) {
					
					//pd.show();
					delmsg();
					dialog.cancel();
							
			}
						
			});
		
			up.setCancelable(false);
			up.show();
	    		 
	     }   
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
				View v = LayoutInflater.from(c).inflate(R.layout.msgitem, null);
				
				
				TextView tv2 = (TextView) v.findViewById(R.id.title1);
				TextView tv3 = (TextView) v.findViewById(R.id.address1);
				TextView tv4 = (TextView) v.findViewById(R.id.content2);
				
				
			
				String name = list.get(position)[4];	
				String time = list.get(position)[5];
				String content=list.get(position)[6];
				
				
				
				tv2.setText(name);
				tv3.setText(content);
				tv4.setText(time);
				
				
			
				return v;
			}
			
		}

}
