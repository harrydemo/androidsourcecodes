package xujun.keepaccount.activity;

import xujun.keepaccount.R;
import xujun.keepaccount.dialog.DateSelectorDialog;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

public class QueryAccount extends TabActivity implements OnTabChangeListener
{
	private TabHost tabHost;
	private static final int MENU_DELETE = 1001;
	private static final int REQUEST_QUERYDATE = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tabHost = getTabHost();
		tabHost.addTab(tabHost.newTabSpec("today").setIndicator("今日账单",getResources().getDrawable(R.drawable.tab_today))
				.setContent(new Intent(this,TodayAccount.class)
				//Tab页每次更换当前选择都会刷新，当日账单每次都应该刷新，因为如果零点可能还在使用
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
		tabHost.addTab(tabHost.newTabSpec("history").setIndicator("历史账单查询",getResources().getDrawable(R.drawable.tab_history))
				.setContent(new Intent(this,HistoryAccount.class)));
		tabHost.setOnTabChangedListener(this);
				
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE,MENU_DELETE,0,"删除所选记录").setIcon(android.R.drawable.ic_menu_delete);  
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_DELETE:
			new AlertDialog.Builder(this).setTitle("确认").setMessage("确定删除所选择的记录吗?")
				.setPositiveButton("确认", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						if(tabHost.getCurrentTab() == 0)
						{
							Intent deleteTodayAccountIntent = new Intent();
							deleteTodayAccountIntent.setAction(TodayAccount.ACTION_DELETE_TODAY_ACCOUNT);
							sendBroadcast(deleteTodayAccountIntent);
						}
						else if(tabHost.getCurrentTab() == 1)
						{
							Intent deleteHisAccountIntent = new Intent();
							deleteHisAccountIntent.setAction(HistoryAccount.DELELTE_HISTORY_ACCOUNT);
							sendBroadcast(deleteHisAccountIntent);
						}
					}
					
				}).create().show();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	//如果是选择查询页，就先弹出一个日期选择对话框
	@Override
	public void onTabChanged(String tabId) {
		if(tabId.equals("history"))
		{
			Intent intent = new Intent(this,DateSelectorDialog.class);
			startActivityForResult(intent, REQUEST_QUERYDATE);
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_QUERYDATE)
		{
			if(resultCode == RESULT_OK)
			{
				String startDate = data.getCharSequenceExtra("startDate").toString();
				String endDate = data.getCharSequenceExtra("endDate").toString();
				
				Intent queryIntent = new Intent();
				queryIntent.setAction(HistoryAccount.QUERY_HISTORY_ACCOUNT);
				queryIntent.putExtra("startDate", startDate);
				queryIntent.putExtra("endDate", endDate);
				sendBroadcast(queryIntent);
			}
		}
	}
}
