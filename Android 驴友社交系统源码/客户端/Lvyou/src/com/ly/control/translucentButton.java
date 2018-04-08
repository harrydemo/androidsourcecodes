package com.ly.control;


import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.ly.control.R;
import com.ly.bean.LYAddAttentionBean;
import com.ly.bean.LYAddFriendsBean;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class translucentButton extends Activity {
	int m_nSreenHeight = 0;
	Button m_menu1,menu2,menu3;
	private ProgressDialog pd;
	private String hostid,otherid;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.translucent_button);
        pd = new ProgressDialog(this);
        hostid=getIntent().getStringExtra("hostid");
		otherid=getIntent().getStringExtra("otherid");
        
        m_menu1 = (Button)findViewById(R.id.menu1);
        menu2 = (Button)findViewById(R.id.menu2);
        menu3 = (Button)findViewById(R.id.menu3);
       // Button menu4 = (Button)findViewById(R.id.menu4);
        
        //menu2.setBackgroundColor(R.drawable.hert);
        
        m_menu1.setOnClickListener(menu1ClickListener);
        menu2.setOnClickListener(menu2ClickListener);
        menu3.setOnClickListener(menu3ClickListener);
        //menu4.setOnClickListener(menu4ClickListener);
        
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        m_nSreenHeight = dm.heightPixels;
        
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_MENU) {
    		this.finish();
    	}
    	return super.onKeyUp(keyCode, event);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	if (event.getX() < m_nSreenHeight - m_menu1.getHeight()) {
    		finish();
    	}
        return false;
    }
    private OnClickListener menu1ClickListener = new OnClickListener() {
    	public void onClick(View v) {
    		if(otherid.equals(hostid)||hostid==null)			
				{
				Toast.makeText(translucentButton.this, "ÃÌº” ß∞‹£¨Œ¥µ«¬ºªÚ’ﬂID≥ÂÕª«ÎºÏ≤È", Toast.LENGTH_LONG).show();
				}
				else
				{
				pd.show();
				Thread t = new Thread(r1);
				t.start();
				
				}
    		//Toast.makeText(translucentButton.this, "menu1 clicked!", Toast.LENGTH_SHORT).show();
    	}
    };
    private OnClickListener menu2ClickListener = new OnClickListener() {
    	public void onClick(View v) {
    		if(otherid.equals(hostid)||hostid==null)			
			{
			Toast.makeText(translucentButton.this, "ÃÌº” ß∞‹£¨Œ¥µ«¬ºªÚ’ﬂID≥ÂÕª«ÎºÏ≤È", Toast.LENGTH_LONG).show();
			}
			else
			{
			pd.show();
			Thread t = new Thread(r);
			t.start();
			
			}
    		//Toast.makeText(translucentButton.this, "menu2 clicked!", Toast.LENGTH_SHORT).show();
    	}
    };
    private OnClickListener menu3ClickListener = new OnClickListener() {
    	public void onClick(View v) {
    		Intent intent = new Intent(translucentButton.this,LYFriendsInfoActivity.class);
    		intent.putExtra("uid",otherid);
    		startActivity(intent);
    		//Toast.makeText(translucentButton.this, "menu3 clicked!", Toast.LENGTH_SHORT).show();
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
					Toast.makeText(translucentButton.this, "ÃÌº” ß∞‹£¨Œ¥µ«¬ºªÚ’ﬂID≥ÂÕª«ÎºÏ≤È", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(translucentButton.this, "ÃÌº”≥…π¶", Toast.LENGTH_LONG).show();
					
				}
			  };
		  };
		  
		  
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
						Toast.makeText(translucentButton.this, "ÃÌº” ß∞‹£¨Œ¥µ«¬ºªÚ’ﬂID≥ÂÕª«ÎºÏ≤È", Toast.LENGTH_LONG).show();
					}else{
						Toast.makeText(translucentButton.this, "—È÷§–≈œ¢“—æ≠∑¢ÀÕ", Toast.LENGTH_LONG).show();
					}
				  };
			  };
}