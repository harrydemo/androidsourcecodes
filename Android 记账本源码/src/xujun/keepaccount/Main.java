package xujun.keepaccount;


import xujun.keepaccount.R;
import xujun.keepaccount.activity.QueryAccount;
import xujun.keepaccount.activity.TypeAccount;

import xujun.keepaccount.activity.WriteAccount;

import xujun.keepaccount.tabbar.ButtonDemo;
import xujun.keepaccount.tabbar.TabBarActivity;
import android.content.Intent;
import android.os.Bundle;

import android.view.Window;

/**
 * @author 徐骏
 * @data   2010-11-1
 */
public class Main extends TabBarActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		//第一句位置
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		
		addTabButton("记录账单", R.drawable.tabbar_pen, new Intent(this,WriteAccount.class));	
		addTabButton("账单查询", R.drawable.tabbar_query_account, new Intent(this,QueryAccount.class));
		addTabButton("分类查询", R.drawable.tabbar_account_type, new Intent(this,TypeAccount.class));

		addTabButton("统计图表", R.drawable.tabbar_chart, new Intent(this,ButtonDemo.class));
		addTabButton("收入", R.drawable.tabbar_flower, new Intent(this,ButtonDemo.class));
		addTabButton("定期储蓄", R.drawable.tabbar_clock, new Intent(this,ButtonDemo.class));
		addTabButton("我的奖励", R.drawable.tabbar_toy, new Intent(this,ButtonDemo.class));
		commit();
	
	}
}
