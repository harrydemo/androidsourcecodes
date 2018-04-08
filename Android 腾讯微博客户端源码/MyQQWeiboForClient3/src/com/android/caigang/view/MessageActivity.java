package com.android.caigang.view;

import com.android.caigang.R;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

//私信
public class MessageActivity extends ListActivity implements OnItemClickListener,OnItemLongClickListener{
	
	private ListView listView;
	private View top_panel;
	private Button top_btn_left;
	private Button top_btn_right;
	private TextView top_title;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message);
		
		setUpViews();
		setUpListeners();
		
		top_title.setText("私信");//顶部工具栏名称
	}
	
	private void setUpViews(){
		listView = getListView();
		top_panel = (View)findViewById(R.id.message_top);
		top_btn_left = (Button)top_panel.findViewById(R.id.top_btn_left);
		top_btn_right = (Button)top_panel.findViewById(R.id.top_btn_right);
		top_title = (TextView)top_panel.findViewById(R.id.top_title);
	}
	
	private void setUpListeners(){
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

}
