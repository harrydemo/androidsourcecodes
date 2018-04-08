package cn.com.mythos.android;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.ColorFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TabHost;
import cn.com.mythos.touhoucartoonreader.R;

public class TabMainActivity extends TabActivity {
	private String str1 = "打开文件";
	private String str2 = "历史记录";
	private String picPath;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TabHost tabHost = this.getTabHost();
		LayoutInflater.from(this).inflate(R.layout.layout_tabmain,tabHost);
		Intent intent1 = new Intent(TabMainActivity.this, SDcardActivity.class);
		Intent intent2 = new Intent(TabMainActivity.this, SDcardHistoryActivity.class);
		
		picPath=getPicPath();
		Bundle bundle = new Bundle();
		bundle.putString("picPath", picPath);
		if(picPath != null) {
			intent1.putExtras(bundle);
			intent2.putExtras(bundle);
		}
		
		tabHost.addTab(tabHost.newTabSpec(str1).setIndicator(str1,getResources().getDrawable(R.drawable.fileopen)).setContent(intent1));
		tabHost.addTab(tabHost.newTabSpec(str2).setIndicator(str2,getResources().getDrawable(R.drawable.history)).setContent(intent2));
		tabHost.setBackgroundResource(R.color.lucid);
	}
	
	//获取MainActivity中当前图片的路径信息
	private String getPicPath() {
		Bundle bundle = getIntent().getExtras();
		if(bundle != null) {
			return bundle.getString("picPath");
		}
		return null;
	}
}
