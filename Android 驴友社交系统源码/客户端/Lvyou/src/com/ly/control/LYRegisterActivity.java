package com.ly.control;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ly.control.R;
import com.ly.bean.LYRegisterBean;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.util.Linkify;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class LYRegisterActivity extends Activity {
	
	private Button bt1,bt2;
	private ImageView iv1,iv2,iv3,iv4;

	private TextView tv0,tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv10,tv11,tv12,tv13;
	private EditText et1,et2,et3,et4,et5,et6,et8,et9,et10;
	private RadioButton rb1,rb2;
	private CheckBox cb1,cb2,cb3;
	private String e1,e2,e3,e4,e5,e6,e8,e9,e10;
	private String url;
	private ImageView img,img2,img3;
	private RadioGroup rg;
	public int i,n1,n2,n3;
	private String str="";
	private ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		iv1=(ImageView)findViewById(R.id.ImageView01);
		iv2=(ImageView)findViewById(R.id.ImageView02);
	    iv4=(ImageView)findViewById(R.id.ImageView04);
	    et1=(EditText)findViewById(R.id.EditText01);
		et2=(EditText)findViewById(R.id.EditText02);
		et3=(EditText)findViewById(R.id.EditText03);
		et4=(EditText)findViewById(R.id.EditText04);
		et5=(EditText)findViewById(R.id.EditText05);
		et6=(EditText)findViewById(R.id.EditText06);
		et8=(EditText)findViewById(R.id.EditText08);
		et9=(EditText)findViewById(R.id.EditText09);
		et10=(EditText)findViewById(R.id.EditText10);
		
		
		rb1=(RadioButton) findViewById(R.id.RadioButton01);
		rb2=(RadioButton) findViewById(R.id.RadioButton02);
		rb1.setOnClickListener(l);
		rb2.setOnClickListener(l);
		
		cb1=(CheckBox) findViewById(R.id.CheckBox01);
		cb2=(CheckBox) findViewById(R.id.CheckBox02);
		cb3=(CheckBox) findViewById(R.id.CheckBox03);
		img=(ImageView) findViewById(R.id.ImageView05);
		img2=(ImageView) findViewById(R.id.ImageView02);
		bt1=(Button)findViewById(R.id.Button01);
		bt2=(Button)findViewById(R.id.Button02);
		bt1.setOnClickListener(l);
		bt2.setOnClickListener(l);
		pd = new ProgressDialog(this); 
		
	}
	private OnClickListener l = new OnClickListener() {
		

		public void onClick(View v) {
			// TODO Auto-generated method stu	
			
			
		if(v==bt1){
			e1=et1.getText().toString();
			e2=et2.getText().toString();
			e3=et3.getText().toString();
			e4=et4.getText().toString();
			e5=et5.getText().toString();
			e6=et6.getText().toString();
			e8=et8.getText().toString();
			e9=et9.getText().toString();
			e10=et10.getText().toString();
			if(rb1.isChecked())
			{				
				i=1;				
			}
			else
			{			
				i=0;				
			}
			if(cb1.isChecked())
			{				
				n1=1;				
			}
			else
			{				
				n1=0;				
			}
			if(cb2.isChecked())
			{				
				n2=1;				
			}
			else
			{				
				n2=0;				
			}
			if(cb3.isChecked())
			{				
				n3=1;				
			}
			else
			{				
				n3=0;				
			}

			if (!e1.equals("") && !e2.equals("") && !e3.equals("") && !e4.equals("") && !e5.equals("") && !e6.equals("") && !e8.equals("") && !e9.equals("") && (url != null && !url.equals("")) && (n1!=0||n2!=0||n3!=0)) 
			{
				if(e2.equals(e3))
				{
					if(Linkify.addLinks(et4, Linkify.EMAIL_ADDRESSES)){
															
					Thread t=new Thread(r);
					t.start();
				
					}
					else{
						Toast.makeText(LYRegisterActivity.this, "请输入正确的email格式！", Toast.LENGTH_LONG).show();																														
					}
					
				}
				else
				{					
					Toast.makeText(LYRegisterActivity.this, "请确认两次密码输入一致！ ",
							Toast.LENGTH_LONG).show();										
				}
							
			} 
			else {
					Toast.makeText(LYRegisterActivity.this, "请填写完整资料！ ",
							Toast.LENGTH_LONG).show();
			}	
			//pd.show();
		}	
		if(v==bt2){
			Intent intent = new Intent(LYRegisterActivity.this,LYGalleryActivity.class);
			startActivityForResult(intent, 3);
		}		
		}
	};
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		 url=data.getStringExtra("url");
		 Bitmap bm = BitmapFactory.decodeFile("/sdcard/"+url);
		 img.setImageBitmap(bm);
		 super.onActivityResult(requestCode, resultCode, data);
	 };
	 Runnable r = new Runnable(){

		
			public void run() {
				// TODO Auto-generated method stub								
				StringBuilder sb = new StringBuilder();
				sb.append("<files>");
				sb.append("<file>");
				sb.append("<username>");
				sb.append(e1);
				sb.append("</username>");
				
				sb.append("<password>");
				sb.append(e2);
				sb.append("</password>");
				
				sb.append("<email>");
				sb.append(e4);
				sb.append("</email>");
				sb.append("<name>");
				sb.append(e5);
				sb.append("</name>");
				
				sb.append("<sex>");
				sb.append(i);
				sb.append("</sex>");
				sb.append("<phone>");
				sb.append(e6);
				sb.append("</phone>");
				sb.append("<headname>");
				sb.append(url);
				sb.append("</headname>");
				sb.append("<head>");
				ByteArrayOutputStream bo = new ByteArrayOutputStream();
				
				try{
					FileInputStream f = new FileInputStream("/sdcard/"+url);
				byte []image = new byte [1024];
				int len = 0;
				while ((len=f.read(image))!=-1){
					bo.write(image, 0, len);
				}
				byte re[] = bo.toByteArray();
				String encond = Base64.encodeToString(re, Base64.DEFAULT);
			
				bo.close();
				f.close();
				sb.append(encond);	
				}
				catch(Exception e){
					e.printStackTrace();
				}		
				sb.append("</head>");
				sb.append("<job>");
				sb.append(e8);
				sb.append("</job>");
				sb.append("<address>");
				sb.append(e9);
				sb.append("</address>");
				sb.append("<circle>");
				sb.append(e10);
				sb.append("</circle>");
				sb.append("<guanzhu>");
				sb.append(n1+""+n2+""+n3);
				sb.append("</guanzhu>");
				sb.append("</file>");
				sb.append("</files>");
				byte content[] = sb.toString().getBytes();
				try {
					URL u = new URL("http://10.0.2.2:8080/Lvyou/LYRegisterServlet");
					HttpURLConnection huc = (HttpURLConnection) u.openConnection();
					huc.setDoInput(true);
					huc.setDoOutput(true);
					huc.setRequestMethod("POST");
					huc.setRequestProperty("Content-Type", "mutipart/form-data");
					huc.setRequestProperty("Content-Length", content.length+"");
					huc.getOutputStream().write(content);
					String str = "";
					if(huc.getResponseCode()==HttpURLConnection.HTTP_OK){
						InputStream in =huc.getInputStream();
				
						LYRegisterBean rb = new LYRegisterBean();
						String reg=rb.register(in);
						Message msg = new Message();
						msg.obj=reg;
						hb.sendMessage(msg);


					}}
					 catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					 }
					 
				pd.cancel();
			}
			  
		  };
		  Handler hb=new Handler (){
				public void handleMessage(Message msg){
					
					 
				     String p=(String) msg.obj;
				     
				     if(p.equals("0")){
				    	 Toast.makeText(LYRegisterActivity.this,"已经存在的用户，请重新填写用户信息！", Toast.LENGTH_LONG).show();
				     }
				     else if(p.equals("1")){
				    	 Toast.makeText(LYRegisterActivity.this,"注册成功！", Toast.LENGTH_LONG).show();
				    	 Intent intent=new Intent(LYRegisterActivity.this,LYLoginActivity.class);
						 startActivity(intent);
							
				     }
			}
		 };

}
