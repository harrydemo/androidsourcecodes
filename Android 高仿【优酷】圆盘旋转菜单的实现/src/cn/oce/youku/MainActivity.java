package cn.oce.youku;

import cn.itcast.youku.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

	private ImageButton home;
	private ImageButton menu;
	private RelativeLayout level2;
	private RelativeLayout level3;
	
	private boolean isLevel2Show = true;
	private boolean isLevel3Show = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		home = (ImageButton) findViewById(R.id.home);
		menu = (ImageButton) findViewById(R.id.menu);

		level2 = (RelativeLayout) findViewById(R.id.level2);
		level3 = (RelativeLayout) findViewById(R.id.level3);

		menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(isLevel3Show){
					//隐藏3级导航菜单
					MyAnimation.startAnimationOUT(level3, 500, 0);
				}else {
					//显示3级导航菜单
					MyAnimation.startAnimationIN(level3, 500);
				}
				
				isLevel3Show = !isLevel3Show;
			}
		});

		home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!isLevel2Show){
					//显示2级导航菜单
					MyAnimation.startAnimationIN(level2, 500);
				} else {
					if(isLevel3Show){
						//隐藏3级导航菜单
						MyAnimation.startAnimationOUT(level3, 500, 0);
						//隐藏2级导航菜单
						MyAnimation.startAnimationOUT(level2, 500, 500);
						isLevel3Show = !isLevel3Show;
					}
					else {
						//隐藏2级导航菜单
						MyAnimation.startAnimationOUT(level2, 500, 0);
					}
				}
				isLevel2Show = !isLevel2Show;
			}
		});

	}

}
