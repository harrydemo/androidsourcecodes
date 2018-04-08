package com.feicong.qqmsglook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class QQMainListActivity extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
	private Button buttonFriends;
	private Button buttonTroops;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_list_layout);        
        buttonFriends = (Button) findViewById(R.id.friends);
        buttonFriends.setOnClickListener(this);
        buttonTroops = (Button) findViewById(R.id.troops);
        buttonTroops.setOnClickListener(this);     
    }

	public void onClick(View v)
	{
		Intent intent = new Intent();
		Button button = (Button) v;
		String qqUin = getIntent().getStringExtra("qqUin");
		intent.putExtra("qqUin", qqUin);
		switch (button.getId()) {
			case R.id.friends:
				intent.setClass(QQMainListActivity.this, QQFriendListActivity.class);
				break;
			case R.id.troops:
				intent.setClass(QQMainListActivity.this, QQTroopListActivity.class);
				break;
		}
		QQMainListActivity.this.startActivity(intent);
	}
}