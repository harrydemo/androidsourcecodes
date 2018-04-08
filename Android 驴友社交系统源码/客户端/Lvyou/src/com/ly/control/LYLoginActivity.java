package com.ly.control;





import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.ly.control.R;
import com.ly.bean.LoginBean;
import com.ly.db.DBHelper;
import com.ly.handler.LoginHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LYLoginActivity extends Activity {
    /** Called when the activity is first created. */
	private EditText et,et2;
	private Button btn;
	private CheckBox cb;
	public static String f;
	public static String name;
	public String s,s1;
	 private LoginHandler lh;
	 private int count;
	 private ProgressDialog pd;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        btn = (Button) findViewById(R.id.Button01);
        et = (EditText) findViewById(R.id.EditText01);
        et2 = (EditText) findViewById(R.id.EditText03);
        cb = (CheckBox) findViewById(R.id.CheckBox01);
        pd = new ProgressDialog(this);
        DBHelper db = new DBHelper(LYLoginActivity.this);
		SQLiteDatabase sd = db.getReadableDatabase();
		Cursor c = sd.rawQuery("select user_id _id,user_name,user_pswd from "+DBHelper.table_name+"", null);
		while(c.moveToNext()){
			int id = c.getInt(0);
			name = c.getString(1);
			f = c.getString(2);
			cb.setChecked(true);
			
		}
		et.setText(name);
		et2.setText(f);
		c.close();
		sd.close();
		db.close();
		
		 lh = new LoginHandler();
        btn.setOnClickListener(l);
    }
    
    private OnClickListener l = new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			if(cb.isChecked()&&v==btn){
				count++;
				pd.show();
				Thread t = new Thread(r);
				t.start();
				
				
			s = et.getText().toString();
			s1 = et2.getText().toString();
			DBHelper db = new DBHelper(LYLoginActivity.this);
			SQLiteDatabase sd = db.getWritableDatabase();
			sd.execSQL("insert into "+DBHelper.table_name+" values(null,?,?)",new String[]{s,s1});
			sd.close();
		
			db.close();
			}
			if(v==btn&&!cb.isChecked()){
				count++;
				pd.show();

				Thread t = new Thread(r);
				t.start();
				
			}
		}
	};
Runnable r = new Runnable(){
		

	
		public void run() {
			// TODO Auto-generated method stub
			try {
				URL url = new URL("http://10.0.2.2:8080/Lvyou/LoginServlet");
				HttpURLConnection htc = (HttpURLConnection) url.openConnection();
	
				htc.setRequestMethod("POST");
				htc.setDoInput(true);
				htc.setDoOutput(true);
				OutputStream out = htc.getOutputStream();
				StringBuilder sb = new StringBuilder();
				sb.append("<user>");
				sb.append("<name>");
				sb.append(et.getText()+"");
				sb.append("</name>");
				sb.append("<pswd>");
				sb.append(et2.getText()+"");
				sb.append("</pswd>");
				sb.append("<flag>");
				sb.append(count);
				sb.append("</flag>");
				sb.append("</user>");		
				System.out.println("++++++++++++");
				byte userXml[] = sb.toString().getBytes();
				out.write(userXml);
				if(htc.getResponseCode()==HttpURLConnection.HTTP_OK){
					InputStream in = htc.getInputStream();
					LoginBean xp = new LoginBean();
					String result = xp.password(in);
					Message msg = new Message();
					msg.obj = result;
					h.sendMessage(msg);
					
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pd.cancel();
		}
  };
  private String result;
  Handler h = new Handler(){
 	 public void handleMessage(Message msg) {
 		 String re = msg.obj+"";
 		 if(re.equals("error")){
 			 Toast.makeText(LYLoginActivity.this, "登录失败,请检查用户名密码是否正确", Toast.LENGTH_LONG).show();
 			if(lh.getNo()!=null){
 				
 				Intent intent = new Intent(LYLoginActivity.this,YanzhengActivity.class);
 				
 				startActivity(intent);
 				count=0;
 			}

 		 }else{
 			 result =re;
 			 String ss[] = result.split(",");
 			Toast.makeText(LYLoginActivity.this, "欢迎"+ss[2]+"进入驴友天下行", Toast.LENGTH_LONG).show();
 			 Intent intent = new Intent(LYLoginActivity.this,LYTabHostActivity.class);
 			 intent.putExtra("result", ss[0]);
 				startActivity(intent);
 				
 				
 		 }
 	 };
  };
}