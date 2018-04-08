package com.pwp.activity;

import com.pwp.borderText.BorderEditText;
import com.pwp.borderText.BorderTextView;
import com.pwp.constant.CalendarConstant;
import com.pwp.dao.ScheduleDAO;
import com.pwp.vo.ScheduleVO;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.AbsListView.LayoutParams;

public class ScheduleInfoView extends Activity {

	private LinearLayout layout = null;
	private BorderTextView textTop = null;
	private BorderTextView info = null;
	private BorderTextView date = null;
	private BorderTextView type = null;
	private BorderEditText editInfo = null;
	private ScheduleDAO dao = null;
	private ScheduleVO scheduleVO = null;
	
	private String scheduleInfo = "";    //�ճ���Ϣ���޸�ǰ������
	private String scheduleChangeInfo = "";  //�ճ���Ϣ���޸�֮�������
	private final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		dao = new ScheduleDAO(this);
		
        //final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 5, 0, 0);
		layout = new LinearLayout(this); // ʵ�������ֶ���
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setBackgroundResource(R.drawable.schedule_bk);
		layout.setLayoutParams(params);
		
		textTop = new BorderTextView(this, null);
		textTop.setTextColor(Color.BLACK); 
		textTop.setBackgroundResource(R.drawable.top_day);
		textTop.setText("�ճ�����");
		textTop.setHeight(47);
		textTop.setGravity(Gravity.CENTER);
		
			
		editInfo = new BorderEditText(ScheduleInfoView.this, null);
		editInfo.setTextColor(Color.BLACK); 
		editInfo.setBackgroundColor(Color.WHITE);
		editInfo.setHeight(200);
		editInfo.setGravity(Gravity.TOP);
		editInfo.setLayoutParams(params);
		editInfo.setPadding(10, 5, 10, 5);
		
		layout.addView(textTop);
		
		
		Intent intent = getIntent();
		//scheduleID = Integer.parseInt(intent.getStringExtra("scheduleID"));
		//һ�����ڿ��ܶ�Ӧ�������ճ�(scheduleID)
		String[] scheduleIDs = intent.getStringArrayExtra("scheduleID");
		//��ʾ�ճ���ϸ��Ϣ
		for(int i = 0; i< scheduleIDs.length; i++){
			handlerInfo(Integer.parseInt(scheduleIDs[i]));
		}
		setContentView(layout);
		
				
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(1, menu.FIRST, menu.FIRST, "�����ճ�");
		menu.add(1, menu.FIRST+1, menu.FIRST+1,"����ճ�");
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case Menu.FIRST:
			Intent intent = new Intent();
			intent.setClass(ScheduleInfoView.this, ScheduleAll.class);
			startActivity(intent);
			break;
        case Menu.FIRST+1:
    	    
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
	/**
	 * ��ʾ�ճ�������Ϣ
	 */
	public void handlerInfo(int scheduleID){
		BorderTextView date = new BorderTextView(this, null);
		date.setTextColor(Color.BLACK); 
		date.setBackgroundColor(Color.WHITE);
		date.setLayoutParams(params);
		date.setGravity(Gravity.CENTER_VERTICAL);
		date.setHeight(40);
		date.setPadding(10, 0, 10, 0);
		
		BorderTextView type = new BorderTextView(this, null);
		type.setTextColor(Color.BLACK); 
		type.setBackgroundColor(Color.WHITE);
		type.setLayoutParams(params);
		type.setGravity(Gravity.CENTER);
		type.setHeight(40);
		type.setPadding(10, 0, 10, 0);
		type.setTag(scheduleID);
		
		final BorderTextView info = new BorderTextView(this, null);
		info.setTextColor(Color.BLACK); 
		info.setBackgroundColor(Color.WHITE);
		info.setGravity(Gravity.CENTER_VERTICAL);
		info.setLayoutParams(params);
		info.setPadding(10, 5, 10, 5);
		
		
		layout.addView(type);
		layout.addView(date);
		layout.addView(info);
		/*Intent intent = getIntent();
		int scheduleID = Integer.parseInt(intent.getStringExtra("scheduleID"));*/
		scheduleVO = dao.getScheduleByID(scheduleID);
		date.setText(scheduleVO.getScheduleDate());
		type.setText(CalendarConstant.sch_type[scheduleVO.getScheduleTypeID()]);
		info.setText(scheduleVO.getScheduleContent());
		
		
		
		//��ʱ�䰴ס�ճ�����textview����ʾ�Ƿ�ɾ���ճ���Ϣ
		type.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {

				final String scheduleID = String.valueOf(v.getTag());
				
				new AlertDialog.Builder(ScheduleInfoView.this).setTitle("ɾ���ճ�").setMessage("ȷ��ɾ��").setPositiveButton("ȷ��", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {

						dao.delete(Integer.parseInt(scheduleID));
						Intent intent1 = new Intent();
						intent1.setClass(ScheduleInfoView.this, ScheduleAll.class);
						startActivity(intent1);
					}
				}).setNegativeButton("ȡ��", null).show();
				
				return true;
			}
		});
		
		
	}
}
