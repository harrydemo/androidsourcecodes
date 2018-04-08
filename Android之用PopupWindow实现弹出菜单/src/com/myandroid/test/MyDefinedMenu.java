package com.myandroid.test;

import java.util.List;

import com.myandroid.test.MyMenu.ItemClickEvent;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;



public class MyDefinedMenu extends PopupWindow { 
	
	private LinearLayout layout;	//总的布局
	private GridView gv_title;		//菜单栏
	private GridView gv_body;		//选项视图
	private BodyAdatper[] bodyAdapter;	//选项适配器
	private TitleAdatper titleAdapter;	//标题适配器
	private Context context;			//上下文
	private int titleIndex;				//菜单序号
	public int currentState;			//对话框状态：0--显示中、1--已消失、2--失去焦点
	
	
	
	public MyDefinedMenu(Context context, List<String> titles, 
			List<List<String>> item_names, List<List<Integer>> item_images,
			ItemClickEvent itemClickEvent) {
		
		super(context);
		this.context = context;
		currentState = 1;
		
		//布局框架
		layout = new LinearLayout(context);		
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		
		//菜单栏
	    titleIndex = 0;
		gv_title = new GridView(context);
		titleAdapter = new TitleAdatper(context, titles);
		gv_title.setAdapter(titleAdapter);
		gv_title.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		gv_title.setNumColumns(titles.size());	//菜单个数
		gv_title.setBackgroundColor(Color.WHITE);
		
		//选项视图
		bodyAdapter = new BodyAdatper[item_names.size()];	//各个视图适配器
		for (int i = 0; i < item_names.size(); i++) {
			bodyAdapter[i] = new BodyAdatper(context, item_names.get(i), item_images.get(i));
		}
		gv_body = new GridView(context);
		gv_body.setNumColumns(4);	//每行显示4个选项
		gv_body.setBackgroundColor(Color.TRANSPARENT);
		gv_body.setAdapter(bodyAdapter[0]);	//设置适配器
		
		//菜单项切换
		gv_title.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				titleIndex = arg2;	//记录当前选中菜单项序号
				titleAdapter.setFocus(arg2);
				gv_body.setAdapter(bodyAdapter[arg2]);	//改变选项视图

			}
		});
		
		//设置选项点击事件
		gv_body.setOnItemClickListener(itemClickEvent);
		
		//添加标题栏和选项
		layout.addView(gv_title);
		layout.addView(gv_body);
		
		// 添加菜单视图
		this.setContentView(layout);
		this.setWidth(LayoutParams.FILL_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);// menu菜单获得焦点 如果没有获得焦点menu菜单中的控件事件无法响应  
		
	}
	
	/**
	 * 获取当前选中菜单项
	 * @return	菜单项序号
	 */
	public int getTitleIndex() {
		
		return titleIndex;
	}
	
	

	
}
