package com.jacinter;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

//用来执行和显示查询姓名等的类
public class Search extends ListActivity {
    	MySQLiteHelper myHelper;  

	   /** Called when the activity is first created. */
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.search);
			Intent intent=new Intent();
			intent=this.getIntent();
			Bundle bundle = intent.getExtras();  
			String username = bundle.getString("searchName");  
			//Toast.makeText(getApplicationContext(), username, Toast.LENGTH_LONG).show();
			
			ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

			//开始
            myHelper = new MySQLiteHelper(this, "my.db", null, 2); 
            SQLiteDatabase db = myHelper.getReadableDatabase();  
            Cursor cursor = db.rawQuery("SELECT name,telephone FROM jacnamelist where (finditems like ? and department>-1)", new String[]{"%"+username+"%"});  

            if( cursor != null ){
               if( cursor.moveToFirst() ){
                   do{
               			HashMap<String, String> map = new HashMap<String, String>();

                	    map.put("user_name", cursor.getString(0));
   			        	map.put("user_ip", cursor.getString(1) );
   			        	list.add(map);
                        }while( cursor.moveToNext());
                  }
             }else{
     			Toast.makeText(getApplicationContext(), "对不起，没找到匹配的结果", Toast.LENGTH_LONG).show();

            	 
             }
            cursor.close();
            db.close();

			
			
			
			
			MyAdapter listAdapter = new MyAdapter(this, list,
					R.layout.user, new String[] { "user_name", "user_ip" },
					new int[] { R.id.user_name,R.id.user_ip});
			setListAdapter(listAdapter);
		}
	 

}
