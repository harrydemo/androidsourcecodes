package com.cmw.android.widgets.examples;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cmw.android.widgets.R;
/**
 * 相对布局复杂示例    ---   仿 TabHost 
 * @author chengmingwei
 *
 */
public class RelativeLayoutComplexActivity extends Activity {
	private LinearLayout task = null;
	private LinearLayout accounts = null;
	private LinearLayout sended = null;
	private LinearLayout content = null;
	
	private TextView taskText = null;
	private TextView accountsText = null;
	private TextView sendedText = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.relativelayout_complex_example);
		init();
	}
	
	private void init(){
		task = (LinearLayout)findViewById(R.id.task);
		accounts = (LinearLayout)findViewById(R.id.accounts);
		sended = (LinearLayout)findViewById(R.id.sended);
		content = (LinearLayout)findViewById(R.id.content);
		
		taskText = (TextView)findViewById(R.id.taskText);
		accountsText = (TextView)findViewById(R.id.accountsText);
		sendedText = (TextView)findViewById(R.id.sendedText);
		
		LayoutInflater factory = LayoutInflater.from(this);
		View taskView = factory.inflate(R.layout.task, null);
		View accountsView = factory.inflate(R.layout.accounts, null);
		View sendedView = factory.inflate(R.layout.sended, null);
		
		content.addView(taskView);
		//-------> 为Tab 添加单击事件
		task.setOnClickListener(new TabListener(task, taskText, taskView));
		accounts.setOnClickListener(new TabListener(accounts, accountsText, accountsView));
		sended.setOnClickListener(new TabListener(sended, sendedText, sendedView));
	}
	/**
	 * TabPanel 
	 * @author chengmingwei
	 *
	 */
	class TabListener implements OnClickListener{
		LinearLayout currLayout;
		TextView tv;
		View subView;
		
		
		
		public TabListener(LinearLayout currLayout, TextView tv, View subView) {
			this.currLayout = currLayout;
			this.tv = tv;
			this.subView = subView;
		}

		@Override
		public void onClick(View v) {
			reset();
			active();
		}

		private void reset(){
			LinearLayout[] layouts = {task,accounts,sended};
			TextView[] tvs = {taskText,accountsText,sendedText};
			for(int i=0; i<3; i++){
				layouts[i].setBackgroundDrawable(null);
				tvs[i].setTextColor(getResources().getColor(R.color.green));
			}
			content.removeAllViews();
		}
		
		private void active(){
			currLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.textback));
			tv.setTextColor(getResources().getColor(R.color.white));
			content.addView(subView);
		}
	}
}
