package com.chuguangming;


import com.chuguangming.KickFlybug.Until.ActivityUtil;
import com.chuguangming.KickFlybug.Until.GameObjData;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class GameModeActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 全屏并且没有Title
		ActivityUtil.fullLandScapeScreen(this);
		setContentView(R.layout.options);

//		btn_mode_100c = (Button) findViewById(R.id.btn_mode_100c);
//		btn_mode_100s = (Button) findViewById(R.id.btn_mode_100s);
//
//		btn_mode_100c.setOnClickListener(this);
//		btn_mode_100s.setOnClickListener(this);

	}

	Button btn_mode_100s;
	Button btn_mode_100c;
	public void onClick(View v) {
		if (v.equals(btn_mode_100c)) {
			GameObjData.CURRENT_GAME_MODE = GameObjData.MODE_100C;
		} else if (v.equals(btn_mode_100s)) {
			GameObjData.CURRENT_GAME_MODE = GameObjData.MODE_100S;
		}
		startActivity(new Intent(GameModeActivity.this, KickFly.class));
	}

}
