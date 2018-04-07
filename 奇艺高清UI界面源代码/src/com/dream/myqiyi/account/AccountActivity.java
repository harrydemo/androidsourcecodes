package com.dream.myqiyi.account;


import com.dream.myqiyi.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AccountActivity extends Activity {
	TextView mTitleView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_activity);
		prepareView();
		mTitleView.setText(R.string.category_account);
	}

	private void prepareView() {
		mTitleView = (TextView) findViewById(R.id.title_text);
	}
}
