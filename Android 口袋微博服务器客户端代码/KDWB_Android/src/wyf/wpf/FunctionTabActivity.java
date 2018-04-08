package wyf.wpf;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

public class FunctionTabActivity extends TabActivity{
	static final int MENU_SEARCH = 0;
	static final int MENU_EXIT = 1;
	String uno = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();		//获得启动该Activity的Intent
		uno = intent.getStringExtra("uno");
		final TabHost tabHost = getTabHost();
		Intent intentPublish = new Intent(this,wyf.wpf.PublishActivity.class);
		intentPublish.putExtra("uno", uno);
		Intent intent1 = new Intent(this,wyf.wpf.ContactsActivity.class);
		intent1.putExtra("type", 0);			//type为1表示好友列表
		intent1.putExtra("uno", uno);
		Intent intent2 = new Intent(this,wyf.wpf.ContactsActivity.class);
		intent2.putExtra("type", 1);			//type为2表示最近访客
		intent2.putExtra("uno", uno);
		Intent iDiary = new Intent(this,wyf.wpf.MyDiaryActivity.class);
		iDiary.putExtra("uno", uno);		//为将用户id设置为Intent的Extra
		Intent iAlbum = new Intent(this,wyf.wpf.MyAlbumListActivity.class);
		iAlbum.putExtra("uno", uno);
		
		tabHost.addTab(tabHost.newTabSpec("tab1")
				.setIndicator("快速发布", getResources().getDrawable(R.drawable.publish))
				.setContent(intentPublish)
				);
		tabHost.addTab(tabHost.newTabSpec("tab2")
				.setIndicator("我的好友", getResources().getDrawable(R.drawable.friend))
				.setContent(intent1)
				);
		tabHost.addTab(tabHost.newTabSpec("tab3")
				.setIndicator("最近访客", getResources().getDrawable(R.drawable.visitor))
				.setContent(intent2)
				);	
		tabHost.addTab(tabHost.newTabSpec("tab4")
				.setIndicator("日志列表",getResources().getDrawable(R.drawable.diary))
				.setContent(iDiary)
				);
		tabHost.addTab(tabHost.newTabSpec("tab5")
				.setIndicator("相册列表", getResources().getDrawable(R.drawable.album))
				.setContent(iAlbum)
				);
		String tab = intent.getStringExtra("tab");
		if(tab != null){
			tabHost.setCurrentTabByTag(tab);
		}
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_SEARCH, 0, "搜索")
			.setIcon(R.drawable.search);
		menu.add(0, MENU_EXIT, 0, "退出")
			.setIcon(R.drawable.exit);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case MENU_SEARCH:				//按下搜索菜单选项
			Intent intent = new Intent(this,SearchActivity.class);		//创建Intent
			intent.putExtra("visitor", uno);
			startActivity(intent);
			break;
		case MENU_EXIT:					//按下退出菜单选项
			new AlertDialog.Builder(this)
			.setTitle("提示")
			.setMessage("确认退出吗？")
			.setIcon(R.drawable.alert_icon)
			.setPositiveButton(
					"确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							android.os.Process.killProcess(android.os.Process.myPid());		//结束进程
						}
					})
			.setNegativeButton(
					"取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {}
					})
			.show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
}