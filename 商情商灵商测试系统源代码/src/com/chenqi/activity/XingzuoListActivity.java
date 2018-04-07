package com.chenqi.activity;

import com.chenqi.service.UserService;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class XingzuoListActivity extends Activity
{
	ListView lvPerson;	
	@Override
		protected void onCreate(Bundle savedInstanceState)
		{
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.xingzuolist);
			UserService userService = new UserService(this);
			Cursor cursor = userService.getXingzuoCursor();
			lvPerson = (ListView) findViewById(R.id.lvXingzuo);
//			SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.person_item, cursor, new String[]
			SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.xingzuolist_item, cursor, 
											new String[]{ "_id", "xingzuoname", "timerange" }, 
											new int[]{ R.id.tvid, R.id.tvname, R.id.tvTimerange });
			lvPerson.setAdapter(adapter);
			
			lvPerson.setOnItemClickListener(new OnItemClickListener()
			{

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
				{

				}
			});
			
		}
}
