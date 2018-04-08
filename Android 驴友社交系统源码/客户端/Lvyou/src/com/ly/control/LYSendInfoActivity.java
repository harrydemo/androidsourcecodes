package com.ly.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ly.control.R;
import com.ly.bean.FriendListBean;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LYSendInfoActivity extends Activity {
    /** Called when the activity is first created. */
	private String time,uid,ueid,content;
	private EditText et;
	private Button bt;
	private ProgressDialog pd;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sendinfo);
        et = (EditText) findViewById(R.id.EtSendInfo);
        bt = (Button) findViewById(R.id.BtnSendInfo);
        pd = new ProgressDialog(this);
        uid = getIntent().getStringExtra("result");
        ueid = getIntent().getStringExtra("ueid");
        SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy-MM-dd  HH:mm:ss");  
        Date   curDate   =   new   Date(System.currentTimeMillis());//获取当前时间             
        time  =   formatter.format(curDate);  
        bt.setOnClickListener(l);
    }
    private OnClickListener l = new OnClickListener() {
		
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			content = et.getText().toString();
			pd.show();
			Thread t = new Thread(r);
			t.start();
			Intent intent = new Intent(LYSendInfoActivity.this,LYTabHostActivity.class);
			intent.putExtra("result", uid);
			startActivity(intent);
			Toast.makeText(LYSendInfoActivity.this, "发送成功！", Toast.LENGTH_LONG).show();
		}
	};
	Runnable r = new Runnable() {
	

	public void run() {
		
		try {
			URL url = new URL("http://10.0.2.2:8080/Lvyou/LYAddMsgServlet");
			HttpURLConnection htc = (HttpURLConnection) url.openConnection();
			htc.setDoInput(true);
			htc.setDoOutput(true);
			htc.setRequestMethod("POST");
			
			
			OutputStream out= htc.getOutputStream();
			StringBuilder sb = new StringBuilder();
			
			sb.append("<user>");
			sb.append("<hostid>");					
			sb.append(uid);
			sb.append("</hostid>");
			sb.append("<otherid>");					
			sb.append(ueid);
			sb.append("</otherid>");
			sb.append("<time>");					
			sb.append(time);
			sb.append("</time>");
			sb.append("<content>");					
			sb.append(content);
			sb.append("</content>");
			sb.append("</user>");
			
			byte userXML[] = sb.toString().getBytes();
			out.write(userXML);
			if(htc.getResponseCode()==HttpURLConnection.HTTP_OK)
			{
				InputStream in =htc.getInputStream();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pd.cancel();
	}
};
}