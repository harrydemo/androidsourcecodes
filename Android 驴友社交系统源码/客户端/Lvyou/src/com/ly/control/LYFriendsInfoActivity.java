package com.ly.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.ly.control.R;
import com.ly.bean.ShowMemoryBean;
import com.ly.bean.ShowRegisterBean;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LYFriendsInfoActivity extends Activity {
	private String id;
	//private ArrayList<String[]> list=null;
	private String result;
	private ImageView iv02;
	private TextView tv03,tv05,tv07,tv15,tv17,tv21,tv23,tv25;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.friendinfo);
	id=getIntent().getStringExtra("uid");
	System.out.println("FXFriendsInfoActivity:"+id);
	iv02=(ImageView) findViewById(R.id.ImageView02);
	tv03=(TextView) findViewById(R.id.TextView03);
	tv05=(TextView) findViewById(R.id.TextView05);
	tv07=(TextView) findViewById(R.id.TextView07);
	tv15=(TextView) findViewById(R.id.TextView15);
	tv17=(TextView) findViewById(R.id.TextView17);
	tv21=(TextView) findViewById(R.id.TextView21);
	tv23=(TextView) findViewById(R.id.TextView23);
	tv25=(TextView) findViewById(R.id.TextView25);
	
	Thread t=new Thread(r);
	t.start();
}
Runnable r = new Runnable(){
	


	public void run() {
		// TODO Auto-generated method stub
//		XMLHandler xh = new XMLHandler();
		try {
			URL url = new URL("http://10.0.2.2:8080/Lvyou/ShowRegisterServlet");
			HttpURLConnection htc = (HttpURLConnection) url.openConnection();
			htc.setRequestMethod("POST");
			htc.setDoInput(true);
			htc.setDoOutput(true);
			OutputStream out = htc.getOutputStream();
			StringBuilder sb = new StringBuilder();
			sb.append("<user>");
			sb.append("<id>");
			sb.append(id);
			sb.append("</id>");
			sb.append("</user>");
			byte userXml[] = sb.toString().getBytes();
			out.write(userXml);
			if(htc.getResponseCode()==HttpURLConnection.HTTP_OK){
				InputStream in = htc.getInputStream();
				ShowRegisterBean srb = new ShowRegisterBean();
				result =srb.showregister(in);
				Message msg = new Message();
				msg.obj=result;
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
		   result= (String) msg.obj;
		 if(result.equals("error")){
			 Toast.makeText(LYFriendsInfoActivity.this, "Ò³Ãæ´íÎó", Toast.LENGTH_LONG).show();
		 }else{
			String [] re = result.split(",");
			tv03.setText(re[1]);
			tv05.setText(re[2]);
			tv07.setText(re[3]);
			tv15.setText(re[4]);
			tv17.setText(re[5]);
			tv21.setText(re[6]);
			tv23.setText(re[7]);
			tv25.setText(re[8]);
			String pic=re[0];
			try {
				URL url = new URL("http://10.0.2.2:8080/Lvyou/pic/"+pic);
				HttpURLConnection htc = (HttpURLConnection) url.openConnection();
				InputStream in = htc.getInputStream();
				Bitmap bit = BitmapFactory.decodeStream(in);
				iv02.setImageBitmap(bit);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		
	  };
};
}
