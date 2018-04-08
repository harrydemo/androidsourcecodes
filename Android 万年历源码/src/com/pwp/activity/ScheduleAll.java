package com.pwp.activity;

import java.util.ArrayList;

import com.pwp.borderText.BorderTextView;
import com.pwp.constant.CalendarConstant;
import com.pwp.dao.ScheduleDAO;
import com.pwp.vo.ScheduleVO;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.AbsListView.LayoutParams;

/**
 * 显示/修改所有日程的activity
 * @author jack_peng
 *
 */
public class ScheduleAll extends Activity {

	private ScrollView sv = null;
	private LinearLayout layout = null;
	private BorderTextView textTop = null;
	private ScheduleDAO dao = null;
	private ScheduleVO scheduleVO = null;
	private ArrayList<ScheduleVO> schList = new ArrayList<ScheduleVO>();
	private String scheduleInfo = "";
	private final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
	
	private int scheduleID = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		dao = new ScheduleDAO(this);
		sv = new ScrollView(this);
		
		params.setMargins(0, 5, 0, 0);
		layout = new LinearLayout(this); // 实例化布局对象
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setBackgroundResource(R.drawable.schedule_bk);
		layout.setLayoutParams(params);
		
		textTop = new BorderTextView(this, null);
		textTop.setTextColor(Color.BLACK); 
		textTop.setBackgroundResource(R.drawable.top_day);
		textTop.setText("所有日程");
		textTop.setHeight(47);
		textTop.setGravity(Gravity.CENTER);
		
		layout.addView(textTop);
		sv.addView(layout);
		
		setContentView(sv);
		
		getScheduleAll();
	}
	
	/**
	 * 得到所有的日程信息
	 */
	public void getScheduleAll(){
		schList = dao.getAllSchedule();
		if(schList != null){
			for (ScheduleVO vo : schList) {
				String content = vo.getScheduleContent();
				int startLine = content.indexOf("\n");
				if(startLine > 0){
					content = content.substring(0, startLine)+"...";
				}else if(content.length() > 30){
					content = content.substring(0, 30)+"...";
				}
				scheduleInfo = CalendarConstant.sch_type[vo.getScheduleTypeID()]+"\n"+vo.getScheduleDate()+"\n"+content;
				scheduleID = vo.getScheduleID();
				createInfotext(scheduleInfo, scheduleID);
			}
		}else{
			scheduleInfo = "没有日程";
			createInfotext(scheduleInfo,-1);
		}
	}
	
	/**
	 * 创建放日程信息的textview
	 */
	public void createInfotext(String scheduleInfo, int scheduleID){
		final BorderTextView info = new BorderTextView(this, null);
		info.setText(scheduleInfo);
		info.setTextColor(Color.BLACK); 
		info.setBackgroundColor(Color.WHITE);
		info.setLayoutParams(params);
		info.setGravity(Gravity.CENTER_VERTICAL);
		info.setPadding(10, 5, 10, 5);
		info.setTag(scheduleID);
		layout.addView(info);
		
		//点击每一个textview就跳转到shceduleInfoView中显示详细信息
		info.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String schID = String.valueOf(v.getTag());
				String scheduleIDs[] = new String[]{schID};
				Intent intent = new Intent();
				intent.setClass(ScheduleAll.this, ScheduleInfoView.class);
				intent.putExtra("scheduleID", scheduleIDs);
				startActivity(intent);
			}
		});
		
		
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(1, menu.FIRST, menu.FIRST, "返回日历");
		menu.add(1, menu.FIRST+1, menu.FIRST+1, "添加日程");
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId()){
		case Menu.FIRST:
			Intent intent = new Intent();
			intent.setClass(ScheduleAll.this, CalendarActivity.class);
			startActivity(intent);
			break;
		case Menu.FIRST+1:
			Intent intent1 = new Intent();
			intent1.setClass(ScheduleAll.this, ScheduleView.class);
			startActivity(intent1);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
