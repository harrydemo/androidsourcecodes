package xujun.keepaccount.tabbar;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import xujun.keepaccount.R;
import android.app.ActivityGroup;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * @author 徐骏
 * @data 2010-11-1
 */
public class TabBarActivity extends ActivityGroup implements
		OnCheckedChangeListener
{
	private int btnWidth = 64;// 满屏情况下，每个按钮宽度64
	private LinearLayout contentViewLayout;// 子页面的容器
	private RadioGroup tabBar;// 工具条
	private List<TabBarButton> buttonList;// 工具条的按钮集合
	private RadioGroup.LayoutParams buttonLayoutParams;// 工具条按钮的布局对象，就是设置widht和height
	
	public static final String ACTION_CHANGE_TAB = "xujun.android.view.tabbar.changeTab";
	private ChangeTabBroadcastReceiver receiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabbar);
		// 获得子页面的容器
		contentViewLayout = (LinearLayout) findViewById(R.id.contentViewLayout);
		tabBar = (RadioGroup) findViewById(R.id.tabBar);
		tabBar.setOnCheckedChangeListener(this);
		buttonList = new ArrayList<TabBarButton>();
	}
	
	@Override
	protected void onResume()
	{
		//在Resume状态下注册一个Broadcast
		IntentFilter filter = new IntentFilter(ACTION_CHANGE_TAB);
		receiver = new ChangeTabBroadcastReceiver();
		registerReceiver(receiver, filter);
		
		super.onResume();
	}
	@Override
	public void onPause()
    {
    	unregisterReceiver(receiver);
    	super.onPause();
    }
	public void addTabButton(String label, int imageId, Intent intent)
	{
		TabBarButton btn = new TabBarButton(this);
		btn.setState(imageId, label, intent);
		buttonList.add(btn);
	}
	public void commit()
	{
		tabBar.removeAllViews();
		// 获得设备的屏幕宽度，计算一屏5个的话，每个按钮的尺寸
		WindowManager windowManager = getWindowManager();
		int windowWidth = windowManager.getDefaultDisplay().getWidth();
		// 每个图标宽度是64，计算一屏可以容纳多少个图标,这是为了下面代码的判断
		int btnNum = windowWidth / 64;
		// 如果本身的操作按钮少于一屏的个数(每个64的宽度，如320的屏幕，就是5个)，那么要重新计算每个图标的width，否则不好看
		if (buttonList.size() < btnNum)
		{
			btnWidth = windowWidth / buttonList.size();
		}
		ButtonStateDrawable.WIDTH = btnWidth;
		buttonLayoutParams = new RadioGroup.LayoutParams(btnWidth,
				LayoutParams.WRAP_CONTENT);
		// 加载按钮到Group上，并设置每个按钮的ID，这样点击时就能区分了
		for (int i = 0; i < buttonList.size(); i++)
		{
			TabBarButton btn = buttonList.get(i);
			btn.setId(i);// ID和Index一致，这样就可以在onCheckedChanged中方便使用了
			tabBar.addView(btn, i, buttonLayoutParams);
		}
		setCurrentTab(0);
	}
	public void setCurrentTab(int index)
	{
		// 设置RadioGroup选中状态，更新UI
		tabBar.check(index);
		// 先清空内容区域，再启动该Button对应的Activity
		contentViewLayout.removeAllViews();
		TabBarButton btn = (TabBarButton) tabBar.getChildAt(index);
		//根据SDK文档说明，startActivity对同一个ID的会缓存，在编写内部Activity要注意，每次刷新的处理，在OnResume中处理
		View tabView = getLocalActivityManager().startActivity(btn.getLabel(),
				btn.getIntent()).getDecorView();
		
		contentViewLayout.addView(tabView, new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	}
	// 在contentViewLayout中切换按钮对应的Intent
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId)
	{
		setCurrentTab(checkedId);
	}
	//提供一个外部Activity触发的入口
	class ChangeTabBroadcastReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			int curIndex = intent.getExtras().getInt("CurIndex");
			setCurrentTab(curIndex);
		}
		
	}
}
