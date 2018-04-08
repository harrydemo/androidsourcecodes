package com.android.superdeskclock.expand;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

import com.android.superdeskclock.Alarm;
import com.android.superdeskclock.Alarms;
import com.android.superdeskclock.R;
import com.android.superdeskclock.SetAlarm;

public class ChooseBellActivity extends ListActivity{
	
	/* 对象声明 
	items：存放显示的名称
	paths：存放文件路径
	rootPath：起始目录
	*/
	private List<String> items=null;
	private List<String> paths=null;
	private String rootPath="/sdcard";
	private TextView mPath;
	private int mId;
	private String resultFile;

	@Override
	protected void onCreate(Bundle icicle){
		super.onCreate(icicle);
		/* 载入main.xml Layout */
		setContentView(R.layout.music_list);
		Intent i=getIntent();
		mId=i.getIntExtra(Alarms.ALARM_ID, -1);
		int type=i.getIntExtra("TYPE", -1);
		if(type!=1)
			rootPath=Environment.getExternalStorageDirectory()+"/music/alarms";
		
		mPath=(TextView)findViewById(R.id.mPath);
		getFileDir(rootPath);
	}
	
	public LayoutParams getLayoutParams(int[][] rules){
		LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		for (int i = 0; i < rules.length; i++) {
			layoutParams.addRule(rules[i][0]);
		}
        return layoutParams;
	}
	
	
	/* 取得文件架构的method */
	private void getFileDir(String filePath){
		/* 设置目前所在路径 */
		mPath.setText(filePath);
		items=new ArrayList<String>();
		paths=new ArrayList<String>();  
		File f=new File(filePath);  
		File[] files=f.listFiles();
		int length=files==null?0:files.length;
		if(files==null||!filePath.equals(rootPath)){
			/* 第一笔设置为[回到根目录] */
			items.add("Back to "+rootPath);
			paths.add(rootPath);
			/* 第二笔设置为[回上层] */
			items.add("Back to ../");
			paths.add(f.getParent());
		}
		/* 将所有文件添加ArrayList中 */
		for(int i=0;i<length;i++){
			File file=files[i];
			items.add(file.getName());
			paths.add(file.getPath());
		}
		/* 声明一ArrayAdapter，使用file_row这个Layout，
		并将Adapter设置给此ListActivity */
		ArrayAdapter<String> fileList = new ArrayAdapter<String>(this,R.layout.music_row, items);
		setListAdapter(fileList);
	}
	
	@Override
	public void onBackPressed() {
		toSetAlarmActivity(false);
	}
	
	public void toSetAlarmActivity(boolean status){
		Intent intent = new Intent(this, SetAlarm.class);
		intent.putExtra(Alarms.ALARM_ID,mId);
		
		if(status)
			updateBell();
		
		startActivity(intent);
		finish();
	}
	
	public void updateBell(){
		Alarm alarm=com.android.superdeskclock.Alarms.getAlarm(getContentResolver(),mId);
		alarm.silent=false;
		alarm.alert=Uri.parse(resultFile);
		
		ContentValues values = com.android.superdeskclock.Alarms.createContentValues(alarm);
		ContentResolver resolver = ChooseBellActivity.this.getContentResolver();
		resolver.update(ContentUris.withAppendedId(Alarm.Columns.CONTENT_URI, alarm.id),values, null, null);
	}
	
	/* 设置ListItem被点击时要做的动作 */
	@Override
	protected void onListItemClick(ListView l,View v,int position,long id){
		File file = new File(paths.get(position));
			if (file.isDirectory()){
			/* 如果是文件夹就再进去捞一次 */
			getFileDir(paths.get(position));
		}
		else{
			resultFile=file.getPath();
			toSetAlarmActivity(true);       
		}
	}
}
