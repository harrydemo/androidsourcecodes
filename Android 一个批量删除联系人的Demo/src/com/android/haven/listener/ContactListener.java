package com.android.haven.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.haven.adapter.GridAdapter;
import com.android.haven.contact.ContactManagerActvity;
import com.android.haven.contact.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

/**
 * @auther hujh
 * @version 2010-10-31 ÏÂÎç05:28:32
 */

public class ContactListener {
	
	public static ContactListener myListener;
	private ContactListener(){};
	
	public Dialog dialog;
	public static ContactListener instance(){
		if(myListener == null)
			myListener = new ContactListener();
		return myListener;
	}
	
	public class MyListItemOnClickListener implements  AdapterView.OnItemClickListener{

		private ContactManagerActvity activity;
		public MyListItemOnClickListener(ContactManagerActvity activity){
			this.activity = activity;
		}
		
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			dialog = new AlertDialog.Builder(activity).create();
			LayoutInflater li = LayoutInflater.from(activity);
			View view = li.inflate(R.layout.gridview_menu, null);
			dialog.show();
			dialog.getWindow().setContentView(view);
			GridView gridView = (GridView)view.findViewById(R.id.gridViewMenu);
			List<Map<String,Integer>> list = new ArrayList<Map<String,Integer>>();
			Map<String,Integer> map;
			
			map =  new HashMap<String,Integer>();
			map.put("ID",R.drawable.call_popup);
			map.put("MSG",  R.string.call);
			list.add(map);
			
			map =  new HashMap<String,Integer>();
			map.put("ID",R.drawable.sms_popup);
			map.put("MSG",  R.string.sms);
			list.add(map);
			
			map =  new HashMap<String,Integer>();
			map.put("ID",R.drawable.delete);
			map.put("MSG",  R.string.delete);
			list.add(map);
			
			map =  new HashMap<String,Integer>();
			map.put("ID",R.drawable.cancel_popup);
			map.put("MSG",  R.string.cancel);
			list.add(map);
			gridView.setAdapter(new GridAdapter(list,activity));
			gridView.setOnItemClickListener(ContactListener.instance().new MyGridItemOnClickListener(activity,arg2));
		}
	}
	
	public class MyGridItemOnClickListener implements AdapterView.OnItemClickListener{

		private ContactManagerActvity activity;
		private int listPosition;
		public MyGridItemOnClickListener(ContactManagerActvity activity,int listPosition){
			this.activity = activity;
			this.listPosition = listPosition;
		}
		
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Message msg = new Message();
			msg.what = arg2+10;
			msg.arg1 = listPosition;
			activity.myHandler.sendMessage(msg);
		}
	}
}
