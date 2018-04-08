package com.ly.control;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ly.control.R;
import com.ly.bean.LYAddAttentionBean;
import com.ly.bean.LYAddFriendsBean;
import com.ly.bean.ShowReplyBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RatingBar.OnRatingBarChangeListener;

public class LYMemory2Activity extends Activity {
	private RatingBar ratbar;

	private  String ITEM1="将作者添加为好友";
	private  String ITEM2="将作者添加为关注者";
	private  String ITEM3="查看作者详情";


	private final int ORDER1= Menu.FIRST;
	private final int ORDER2= Menu.FIRST+1;
	private final int ORDER3= Menu.FIRST+2;
	
	private String t2,a2,c2,m2,u2,ti2,te2,tt2;
	private TextView tv03,tv05,tv07,tv11,tv13,tv15;
	private Button bt1,bt2,bt3,btnsendmsg;
	private ProgressDialog pd;
	private String hostid,otherid;
	private EditText etmsg;
	private ArrayList<String []> list;
	private ListView lvreply;
	private String mmid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lymemory2);
		ratbar=(RatingBar) findViewById(R.id.RatingBar01);
		//ratbar.setOnRatingBarChangeListener(listener);
		t2=getIntent().getStringExtra("title");
		a2=getIntent().getStringExtra("address");
		c2=getIntent().getStringExtra("content");
		m2=getIntent().getStringExtra("mid");
		u2=getIntent().getStringExtra("uid");
		ti2=getIntent().getStringExtra("tagtitle");
		te2=getIntent().getStringExtra("tagtype");
		tt2=getIntent().getStringExtra("tagcontent");

		
		bt1=(Button) findViewById(R.id.Button01);
		bt2=(Button) findViewById(R.id.Button02);
		bt3=(Button) findViewById(R.id.Button03);
		bt1.setOnClickListener(l);
		bt2.setOnClickListener(l);
		bt3.setOnClickListener(l);
		
		lvreply=(ListView) findViewById(R.id.Lvreply01);
		etmsg=(EditText) findViewById(R.id.EtSendInfo);
		btnsendmsg=(Button) findViewById(R.id.BtnSendInfo);
		btnsendmsg.setOnClickListener(l);
		pd = new ProgressDialog(this);
		tv03=(TextView) findViewById(R.id.TextView03);
		tv05=(TextView) findViewById(R.id.TextView05);
		tv07=(TextView) findViewById(R.id.TextView07);
		tv11=(TextView) findViewById(R.id.TextView11);
		tv13=(TextView) findViewById(R.id.TextView13);
		tv15=(TextView) findViewById(R.id.TextView15);
		tv03.setText(t2);
		tv05.setText(a2);
		tv07.setText(c2);
		tv11.setText(ti2);
		tv13.setText(te2);
		tv15.setText(tt2);

		
		hostid=getIntent().getStringExtra("hostid");
		otherid=getIntent().getStringExtra("uid");
		
		//System.out.println(hostid+"::::"+otherid);
		
		Thread t = new Thread(r2);
		t.start();
		
		
	}
	 public boolean onKeyUp(int keyCode, KeyEvent event) {
	    	if (keyCode == KeyEvent.KEYCODE_MENU) {
	    		Intent intent=new Intent(LYMemory2Activity.this, translucentButton.class);
	    		intent.putExtra("hostid", hostid);
	    		intent.putExtra("otherid", otherid);
	    		startActivity(intent);
	    		overridePendingTransition(R.anim.fade, R.anim.hold);
	    	}
	    	return super.onKeyUp(keyCode, event);
	    }
	private OnClickListener l = new OnClickListener() {
		
	
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v==bt2){
				if(otherid.equals(hostid)||hostid==null)					
				{
				Toast.makeText(LYMemory2Activity.this, "添加失败，未登录或者ID冲突请检查", Toast.LENGTH_LONG).show();
				}
				else
				{
				pd.show();
				Thread t = new Thread(r1);
				t.start();
				
				}
			}
			if(v == bt3){
				
				if(otherid.equals(hostid)||hostid==null)					
				{
				Toast.makeText(LYMemory2Activity.this, "添加失败，未登录或者ID冲突请检查", Toast.LENGTH_LONG).show();
				}
				else
				{
				pd.show();
				Thread t = new Thread(r);
				t.start();
				
				}
			}
			if(v == btnsendmsg)
			{	
				if(hostid==null)					
				{
				Toast.makeText(LYMemory2Activity.this, "未登录，请登录后再回复", Toast.LENGTH_LONG).show();
				}
				else
				{
				pd.show();
				Thread t = new Thread(r2);
				t.start();
				Toast.makeText(LYMemory2Activity.this, "回复成功", Toast.LENGTH_LONG).show();
				}
			}
		}
	};
	
	private OnRatingBarChangeListener listener =new OnRatingBarChangeListener() {
		

		public void onRatingChanged(RatingBar ratingBar, float rating,
				boolean fromUser) {
			// TODO Auto-generated method stub
			
		}
	};

	 
	 Runnable r = new Runnable() {
			
	
			public void run() {
				//LoginUserHandler luh = new LoginUserHandler();
				try {
					URL url = new URL("http://10.0.2.2:8080/Lvyou/LYAddAttentionServlet");
					HttpURLConnection htc = (HttpURLConnection) url.openConnection();
					htc.setDoInput(true);
					htc.setDoOutput(true);
					htc.setRequestMethod("POST");
					
					OutputStream out= htc.getOutputStream();
					
					StringBuilder sb = new StringBuilder();
					sb.append("<atts>");
					sb.append("<att>");
					sb.append("<hostid>");					
					sb.append(hostid);
					sb.append("</hostid>");
					sb.append("<otherid>");					
					sb.append(""+otherid);
					sb.append("</otherid>");
					sb.append("</att>");
					sb.append("</atts>");
					byte userXML[] = sb.toString().getBytes();
					out.write(userXML);
					
					if(htc.getResponseCode()==HttpURLConnection.HTTP_OK)
					{
						InputStream in =htc.getInputStream();
						LYAddAttentionBean fab = new LYAddAttentionBean();
						String result = fab.addattention(in);
						Message msg = new Message();
						msg.obj=result;
						ha.sendMessage(msg);
						
						
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pd.cancel();
			}
		};
		Handler  ha=new  Handler()
		  {
			  public void handleMessage(android.os.Message msg) {
				String result=(String) msg.obj;
				if(result.equals("error")){
					Toast.makeText(LYMemory2Activity.this, "添加失败，未登录或者ID冲突请检查", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(LYMemory2Activity.this, "添加成功", Toast.LENGTH_LONG).show();
					
				}
			  };
		  };
		 
		 Runnable r2 = new Runnable() {
				
		
				public void run() {
					//LoginUserHandler luh = new LoginUserHandler();
					try {
						URL url = new URL("http://10.0.2.2:8080/Lvyou/LYReplyMemoryServlet");
						HttpURLConnection htc = (HttpURLConnection) url.openConnection();
						htc.setDoInput(true);
						htc.setDoOutput(true);
						htc.setRequestMethod("POST");
						
						OutputStream out= htc.getOutputStream();
						
						StringBuilder sb = new StringBuilder();
						sb.append("<atts>");
						sb.append("<att>");
						sb.append("<memoryid>");					
						sb.append(m2);
						sb.append("</memoryid>");
						sb.append("<hostid>");					
						sb.append(hostid);
						sb.append("</hostid>");
						sb.append("<content>");					
						sb.append(""+etmsg.getText().toString());
						sb.append("</content>");
						sb.append("</att>");
						sb.append("</atts>");
						byte userXML[] = sb.toString().getBytes();
						out.write(userXML);
						
						if(htc.getResponseCode()==HttpURLConnection.HTTP_OK)
						{
							InputStream in =htc.getInputStream();
							ShowReplyBean srb = new ShowReplyBean();
							list = srb.showreply(in);
							Message msg = new Message();
							msg.obj=list;
							h.sendMessage(msg);
							
							
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					pd.cancel();
				}
			};
			
			 Handler  h=new  Handler()
			  {
				  public void handleMessage(android.os.Message msg) {
					   list=(ArrayList<String[]>) msg.obj;
					   //System.out.println(list.get(2));
					   lvreply.setAdapter(new myadapter(LYMemory2Activity.this , list));
					
				  };
			  };
			  private class myadapter extends BaseAdapter{
					private Context c;
					private ArrayList<String[]> list;
					
					public myadapter(Context c,ArrayList<String[]> l){
						
						this.c=c;
						this.list=l;
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
						View v=LayoutInflater.from(c).inflate(R.layout.lyreplyitem2, null);
							mmid=list.get(position)[2];
							System.out.println(mmid);
							TextView tv1 = (TextView) v.findViewById(R.id.uname22);
							String name =list.get(position)[3];
							tv1.setText("【"+name+"】");
							TextView tv2 = (TextView)v.findViewById(R.id.content22);
							String content=list.get(position)[0];
							tv2.setText(content);
//											
							return v;
					}
					
				}
			  
  Runnable r1 = new Runnable() {
					
		
					public void run() {
						//LoginUserHandler luh = new LoginUserHandler();
						try {
							URL url = new URL("http://10.0.2.2:8080/Lvyou/LYAddFriendsServlet");
							HttpURLConnection htc = (HttpURLConnection) url.openConnection();
							htc.setDoInput(true);
							htc.setDoOutput(true);
							htc.setRequestMethod("POST");
							
							OutputStream out= htc.getOutputStream();
							
							StringBuilder sb = new StringBuilder();
							sb.append("<atts>");
							sb.append("<att>");
							sb.append("<hostid>");					
							sb.append(hostid);
							sb.append("</hostid>");
							sb.append("<otherid>");					
							sb.append(""+otherid);
							sb.append("</otherid>");
							sb.append("</att>");
							sb.append("</atts>");
							byte userXML[] = sb.toString().getBytes();
							out.write(userXML);
							
							if(htc.getResponseCode()==HttpURLConnection.HTTP_OK)
							{
								InputStream in =htc.getInputStream();
								LYAddFriendsBean fab = new LYAddFriendsBean();
								String result = fab.addfriends(in);
								Message msg = new Message();
								msg.obj=result;
								ha1.sendMessage(msg);
								
								
							}

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						pd.cancel();
					}
				};
				Handler  ha1=new  Handler()
				  {
					  public void handleMessage(android.os.Message msg) {
						String result=(String) msg.obj;
						if(result.equals("error")){
							Toast.makeText(LYMemory2Activity.this, "添加失败，未登录或者ID冲突请检查", Toast.LENGTH_LONG).show();
						}else{
							Toast.makeText(LYMemory2Activity.this, "验证信息已经发送", Toast.LENGTH_LONG).show();
						}
					  };
				  };
}
