package com.ly.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import com.ly.control.R;
import com.ly.db.AddPlaceHelper;
import com.ly.db.DBHelper2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class LYAddMemoryActivity extends Activity {
	private String [] str00;
	private String [] str01;
	private String str02[][]={
			{"请选择"},
			{"哈尔滨","佳木斯","大庆"},
			{"长春","吉林","辽宁"},
			{"沈阳","大连","鞍山"},
			{"广州","厦门","深圳"}
	};
	private String []str03;
	private Spinner sp1,sp2,sp3;
	private EditText txtName,txtContent;
	private ProgressDialog pd;
	private Button btnsend,btnadd,btnlook,btnsave;
	private String title,content,sp11,sp22,sp33;
	private String id,time,title1,content1,address1;
	private String nll;
	private int sp111,sp221,sp331;
	private ListView lv,lv02;
	int position;
	private TextView tv,tv5,tv6,tv7;
	private String tagtitle,tagcontent,tagtype;
	private List<String> list,list1,list2;
	private String tv55,tv66,tv77;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fxaddmemory);
		btnsend=(Button) findViewById(R.id.Button011);
		btnadd=(Button) findViewById(R.id.Button03);
		btnlook=(Button) findViewById(R.id.Button09);
		btnsave=(Button) findViewById(R.id.Button02);
		txtName=(EditText) findViewById(R.id.EditText01);
		txtContent=(EditText) findViewById(R.id.EditText02);
		
		tv5=(TextView) findViewById(R.id.TextView05);
		tv6=(TextView) findViewById(R.id.TextView06);
		tv7=(TextView) findViewById(R.id.TextView07);
		lv=(ListView)findViewById(R.id.ListView01);
		//lv02=(ListView)findViewById(R.id.ListView02);
		
		tagtitle=getIntent().getStringExtra("tagtitle");
		tagcontent=getIntent().getStringExtra("tagcontent");
		tagtype=getIntent().getStringExtra("tagtype");

		
		SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy-MM-dd  HH:mm:ss");  
        Date   curDate   =   new   Date(System.currentTimeMillis());//获取当前时间             
        time  =   formatter.format(curDate);  
        nll = getIntent().getStringExtra("nll");
		pd = new ProgressDialog(this);
		id=getIntent().getStringExtra("result");
		
		AddPlaceHelper db=new AddPlaceHelper(LYAddMemoryActivity.this);
		SQLiteDatabase sd=db.getReadableDatabase();
		Cursor c=sd.rawQuery("select user_id _id,title,type,content from "+AddPlaceHelper.table_name+" where id= "+id+"", null);
		 list=new ArrayList<String>();
		 list1=new ArrayList<String>();
		 list2=new ArrayList<String>();
		while(c.moveToNext()){
		String title=c.getString(c.getColumnIndex("title"));
		list.add(title);
		String type=c.getString(c.getColumnIndex("type"));
		list1.add(type);
		String content=c.getString(c.getColumnIndex("content"));
		list2.add(content);
		}
		SimpleCursorAdapter sa=new SimpleCursorAdapter(LYAddMemoryActivity.this,android.R.layout.simple_expandable_list_item_2,c,new String[]{"title","type"},new int[]{android.R.id.text1,android.R.id.text2});
		lv.setAdapter(sa);
		lv.setOnItemClickListener(new OnItemClickListener() {

		
			
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				tv5.setText(list.get(arg2));
				tv6.setText(list1.get(arg2));
				tv7.setText(list2.get(arg2));
			}
		});

		
		content1 = getIntent().getStringExtra("content");
		title1 = getIntent().getStringExtra("title");
		address1 = getIntent().getStringExtra("address");
		
		
		btnsend.setOnClickListener(l);
		btnadd.setOnClickListener(l);
		btnlook.setOnClickListener(l);
		btnsave.setOnClickListener(l);
		sp1=(Spinner) findViewById(R.id.Spinner01);
	    sp2=(Spinner) findViewById(R.id.Spinner02);
	    sp3=(Spinner) findViewById(R.id.Spinner03);
		
	    str00=new String[]{"中国"};
	    
	    SpinnerAdapter adapter00= new SpinnerAdapter(this,android.R.layout.simple_spinner_item,str00);
	    adapter00.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    sp1.setAdapter(adapter00);	
	    
	    
	    str01=new String[]{"请选择","黑龙江","吉林省","辽宁省","广东省"};
	    SpinnerAdapter adapter01=new SpinnerAdapter(this,android.R.layout.simple_spinner_item,str01);
		adapter01.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp2.setAdapter(adapter01); 

	    sp2.setOnItemSelectedListener(new OnItemSelectedListener(){

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				position=(int)arg3;
				str03=new String[str02[position].length];
				for(int i=0;i<str02[position].length;i++){  
                    str03[i]=str02[position][i];  
                }
				
				
			
		SpinnerAdapter adapter02=new SpinnerAdapter(LYAddMemoryActivity.this,android.R.layout.simple_spinner_item,str03);  
        adapter02.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
        sp3.setAdapter(adapter02);  
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			} 
});
	    
	    
		if(nll!=null){
			String[] s = address1.split(",");
			
			Integer[] orderids = new Integer[s.length];

			for(int i = 0;i<s.length;i++){

			orderids[i] = Integer.parseInt(s[i]);
			
			}
			
			
			txtName.setText(title1);
			txtContent.setText(content1);
			sp1.setSelection(orderids[0]);
			sp2.setSelection(orderids[1]);
			sp3.setSelection(orderids[2]);
			
		   
		}

		
	}
	private class SpinnerAdapter extends ArrayAdapter<String> {  
	    Context context;  
	    String[] items = new String[] {};  
	  
	    public SpinnerAdapter(final Context context,final int textViewResourceId, final String[] objects) {  
	        super(context, textViewResourceId, objects);  
	        this.items = objects;  
	        this.context = context;  
	    } 
	  
	    @Override  
	    public View getView(int position, View convertView, ViewGroup parent) {  
	        if (convertView == null) {  
	            LayoutInflater inflater = LayoutInflater.from(context);  
	            convertView = inflater.inflate(  
	                    android.R.layout.simple_spinner_item, parent, false);  
	        }  
	  
	        // android.R.id.text1 is default text view in resource of the android.  
	        // android.R.layout.simple_spinner_item is default layout in resources of android.  
	  
	        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);  
	        tv.setText(items[position]);  
	        tv.setTextColor(Color.BLACK);  
	        tv.setTextSize(12);  
	        return convertView;  
	    }
	}
	private OnClickListener l = new OnClickListener() {
		
	
		public void onClick(View v) {
			if(v == btnsend){
				title=txtName.getText().toString();
				content=txtContent.getText().toString();
				sp11=sp1.getSelectedItem().toString();
				sp22=sp2.getSelectedItem().toString();
				sp33=sp3.getSelectedItem().toString();
				tv55=tv5.getText().toString();
				tv66=tv6.getText().toString();
				tv77=tv7.getText().toString();
				pd.show();
				Thread t =new  Thread(r);
				t.start();
				Intent intent = new Intent(LYAddMemoryActivity.this,LYTabHostActivity.class);
				intent.putExtra("result", id);
				startActivity(intent);
			}
			if(v == btnadd){
				Intent intent = new Intent(LYAddMemoryActivity.this,AddPlaceActivity.class);
				intent.putExtra("result", id);
				startActivity(intent);
			}
			if(v == btnlook){
				Intent intent = new Intent(LYAddMemoryActivity.this,GoogleMapActivity.class);
				startActivity(intent);
			}
			if(v == btnsave){
				title=txtName.getText().toString();
				content=txtContent.getText().toString();
				sp111=sp1.getSelectedItemPosition();
				sp221=sp2.getSelectedItemPosition();
				sp331=sp3.getSelectedItemPosition();
				String s = sp111+","+sp221+","+sp331;
				
				DBHelper2 db1 = new DBHelper2(LYAddMemoryActivity.this);
				SQLiteDatabase sd1 = db1.getWritableDatabase();
				sd1.execSQL("insert into "+DBHelper2.table_name+" values(null,?,?,?,?,?,?,?,?)",new String[]{title,content,s,time,id,tagtitle,tagtype,tagcontent});
				
				
				sd1.close();
				db1.close();
				
				Intent intent = new Intent(LYAddMemoryActivity.this,StoreMemoryActivity.class);
				intent.putExtra("result", id);
				startActivity(intent);
				Toast.makeText(LYAddMemoryActivity.this, "保存记忆到本地成功！", Toast.LENGTH_LONG).show();

			}
		}
	};
	
	Runnable r = new Runnable() {
		

		public void run() {
			//LoginUserHandler luh = new LoginUserHandler();
			try {
				URL url = new URL("http://10.0.2.2:8080/Lvyou/LYBuildMemoryServlet");
				HttpURLConnection htc = (HttpURLConnection) url.openConnection();
				htc.setDoInput(true);
				htc.setDoOutput(true);
				htc.setRequestMethod("POST");
				
				OutputStream out= htc.getOutputStream();
				
				StringBuilder sb = new StringBuilder();
				sb.append("<users>");
				sb.append("<user>");
				sb.append("<uid>");					
				sb.append(id);
				sb.append("</uid>");
				sb.append("<title>");					
				sb.append(""+title);
				sb.append("</title>");
				sb.append("<address>");					
				sb.append(sp11+","+sp22+","+sp33);
				sb.append("</address>");
				sb.append("<content>");
				sb.append(""+content);
				sb.append("</content>");
				sb.append("<time>");
				sb.append(time);
				sb.append("</time>");
				sb.append("<tagtitle>");
				sb.append(tv55);
				sb.append("</tagtitle>");
				sb.append("<tagtype>");
				sb.append(tv66);
				sb.append("</tagtype>");
				sb.append("<tagcontent>");
				sb.append(tv77);
				sb.append("</tagcontent>");
				sb.append("</user>");
				sb.append("</users>");
				byte userXML[] = sb.toString().getBytes();
				out.write(userXML);
				
				if(htc.getResponseCode()==HttpURLConnection.HTTP_OK)
				{
					InputStream in =htc.getInputStream();
//					LoginXMLBean lxb = new LoginXMLBean();
//					String result = lxb.isLogin(in);
//					Message msg = new Message();
//					msg.obj=result;
//					h.sendMessage(msg);
//					System.out.println(result+":uupptt");
					
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
			  String re = msg.obj+"";
			  if(re.equals("error"))
			  {
				  Toast.makeText(LYAddMemoryActivity.this, "发布失败", Toast.LENGTH_LONG).show();
				 
			  }
			  else
			  {				
				  Toast.makeText(LYAddMemoryActivity.this, "发布成功", Toast.LENGTH_LONG).show();
				  
			  }
		  };
	  };
}
