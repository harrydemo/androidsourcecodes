package com.samg_checkinapp;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ListActivity extends android.app.ListActivity {
	private void dialog(String msg){       
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(msg);
        builder.show();
}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_view);
		this.getIntent();
		DatabaseHelper dbh = new DatabaseHelper(ListActivity.this,"SamG_Checkin");
	    SQLiteDatabase sd = dbh.getReadableDatabase();
	    Cursor cursor=sd.query("CheckinTable", new String[]{"name","number"}, null, null, null, null, null);
	    ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
	    while(cursor.moveToNext()){
	    	for(int i=0;i<cursor.getCount();i++){
	    		cursor.moveToPosition(i);
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String number = cursor.getString(cursor.getColumnIndex("number"));				
			//String name=_intent.getStringExtra("name");
			//String number=_intent.getStringExtra("number");
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("getname", name);
			map.put("getnumber", number);
			list.add(map);
	    	}
	    }
		SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.user, new String[]{"getname","getnumber"}, new int[]{R.id.txt1,R.id.txt2});
		setListAdapter(adapter);	    
	    cursor.close();
	    Button btn1 = (Button)findViewById(R.id.button2);
	    btn1.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				DatabaseHelper dbh = new DatabaseHelper(ListActivity.this,"SamG_Checkin");
			    SQLiteDatabase sd = dbh.getReadableDatabase();
				sd.delete("CheckinTable", null, null);
				System.out.println("已清除数据库！");
			}
	    	
	    });
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		System.out.println("id--------"+id);
		System.out.println("position--------"+position);
	}
	public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add("退出");
    	menu.add("关于我们");
        return true;
    }
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getTitle().equals("退出"))
			finish();
		else if(item.getTitle().equals("关于我们"));
			dialog("SamG工作室出品");
		return super.onMenuItemSelected(featureId, item);
		
	}
	
}
