package com.example.phoneui;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CallLog.Calls;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

public class Notic extends Activity {
	private ArrayList<HashMap<String,String>> list;
	private ListView listview;
	private boolean isnew;
	private boolean status;
	protected void onNewIntent(Intent intent){
		super.onNewIntent(intent);
		setIntent(intent);
	}
	protected void onResume(){
		super.onResume();
		status = true;
		Task read = new Task();
		read.execute("1");
	}
	protected void onPause(){
		super.onPause();
		status = false;
	}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notic);
        listview = (ListView) findViewById(R.id.listview);
        listview.setOnItemClickListener(new itemshort());
    }
    public void onClick(View v){
    	Task set = new Task();
    	set.execute("2");
    	finish();
    }
    private class itemshort implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
			tocall(list.get(arg2).get(Calls.NUMBER));
			finish();
		}
    }
    void tocall(String number){
    	Uri uri = Uri.parse("tel:"+number);
    	Intent intent = new Intent(Intent.ACTION_CALL, uri);
    	startActivity(intent);
    }
    class Task extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... params) {
			// TODO 自动生成的方法存根
			if(params[0] == "1" && !this.isCancelled())
				readnew();
			else if(params[0] == "2")
				setOld();
			return params[0];
		}
		protected void onPostExecute(String result){
			super.onPostExecute(result);
			if(result == "1" && status)
			{
				if(!isnew)
					finish();
				else
					show();
			}
		}
    }
    void readnew(){
    	list = new ArrayList<HashMap<String,String>>();
    	HashMap<String,String> map;
    	Cursor cursor = getApplicationContext().getContentResolver().query(Calls.CONTENT_URI,new String[]{Calls.CACHED_NAME,Calls.NUMBER,Calls.DATE,Calls.TYPE},Calls.NEW+"=?"+" AND "+Calls.TYPE+"=?",new String[]{"1",String.valueOf(Calls.MISSED_TYPE)},Calls.DEFAULT_SORT_ORDER);
    	while(status && cursor.moveToNext())
    	{
    		map = new HashMap<String,String>();
			String name = cursor.getString(0);
			if(name == null || name.length() == 0)
				name = cursor.getString(1);
    		map.put(Calls.CACHED_NAME, name);
    		map.put(Calls.NUMBER, cursor.getString(1));
    		String date = getDate(cursor.getLong(2));
    		map.put(Calls.DATE, date);
    		String type = getType(cursor.getInt(3));
    		map.put(Calls.TYPE, type);
    		list.add(map);
    	}
    	if(list.size() > 0)
    		isnew = true;
    	else
    		isnew = false;
    	cursor.close();
    }
    void setOld(){
    	ContentValues values = new ContentValues();
    	values.put(Calls.NEW, 0);
    	getApplicationContext().getContentResolver().update(Calls.CONTENT_URI, values,Calls.NEW+"=?"+" AND "+Calls.TYPE+"=?",new String[]{"1",String.valueOf(Calls.MISSED_TYPE)});
    }
    void show(){
    	SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), list, R.layout.loglist,new String[]{Calls.CACHED_NAME,Calls.DATE}, new int[]{R.id.name,R.id.date});
    	listview.setAdapter(adapter);
    }
    String getDate(long date){
    	String mFormat = "";
    	String newdate = null;
    	Date logdate;
    	logdate = new Date(date);
    	if(isToday(logdate))
    		mFormat = "a"+"\n"+"h:mm";
    	else
    		mFormat = "MM-dd";
    	newdate = DateFormat.format(mFormat, logdate).toString();
    	return newdate;
    }
	boolean isToday(Date date){
    	Date now = new Date(System.currentTimeMillis());
    	if(now.getYear()==date.getYear() && now.getMonth()==date.getMonth() && now.getDate()== date.getDate())
    		return true;
		return false;
    }
	String getType(int num){
    	String type = null;
    	switch(num)
    	{
    	case Calls.INCOMING_TYPE:type = "已接";break;
    	case Calls.MISSED_TYPE:type = "未接";break;
    	case Calls.OUTGOING_TYPE:type = "已拨";break;
    	default:type = "未接通";break;
    	}
    	return type;
    }
	boolean get24HourMode(Context context) {
        return android.text.format.DateFormat.is24HourFormat(context);
    }
    /*public boolean dispatchKeyEvent(KeyEvent e){
    	if(e.getKeyCode() == KeyEvent.KEYCODE_BACK)
    		return true;
    	return super.dispatchKeyEvent(e);
    } */
}
