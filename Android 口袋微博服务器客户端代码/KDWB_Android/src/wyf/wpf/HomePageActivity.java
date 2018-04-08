package wyf.wpf;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class HomePageActivity extends TabActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		String uno = intent.getStringExtra("uno");
		String visitor = intent.getStringExtra("visitor");
		final TabHost tabHost = getTabHost();
		Intent diaryIntent = new Intent(this,DiaryActivity.class);
		diaryIntent.putExtra("uno", uno);
		diaryIntent.putExtra("visitor", visitor);
		Intent albumIntent = new Intent(this,AlbumListActivity.class);
		albumIntent.putExtra("uno", uno);
		albumIntent.putExtra("visitor", visitor);
		
		tabHost.addTab(tabHost.newTabSpec("tab1")
				.setIndicator("日志列表", getResources().getDrawable(R.drawable.diary))
				.setContent(diaryIntent)
				);
		tabHost.addTab(tabHost.newTabSpec("tab2")
				.setIndicator("相册列表", getResources().getDrawable(R.drawable.album))
				.setContent(albumIntent)
				);
		String tab = intent.getStringExtra("tab");
		if(tab != null){
			tabHost.setCurrentTabByTag(tab);
		}
	}
	
}