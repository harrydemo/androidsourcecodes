package com.genius.demo;

import android.R.bool;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FriendInfoActivity extends Activity{

	private TextView mChangeCommentTextView;
	
	private EditText mCommentEditText;
	
	private boolean  mBCommentEditable;
	
	private InputMethodManager mKeyBorad;
	
	private String mCommentTmpString;			
	
	
	
	private LinearLayout mQzone;
	private LinearLayout mMaiLayout;
	private LinearLayout mChat;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.info_friend);
		
		init();
	}

	
	private void init()
	{		
		
		mChangeCommentTextView = (TextView) findViewById(R.id.change_comment);
		mChangeCommentTextView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switchCommentState();
			}
		});
		
		mCommentEditText = (EditText) findViewById(R.id.edit_comment);
		
		mBCommentEditable = false;

		mKeyBorad = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);  

		
		View view1 = findViewById(R.id.info_qzone_mail);
		mQzone = (LinearLayout) view1.findViewById(R.id.qzone);
		mQzone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(FriendInfoActivity.this, "You Press QZONE", Toast.LENGTH_SHORT).show();
			}
		});
		
		mMaiLayout = (LinearLayout) view1.findViewById(R.id.mail);
		mMaiLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(FriendInfoActivity.this, "You Press Main", Toast.LENGTH_SHORT).show();
			}
		});
		
		mChat = (LinearLayout) view1.findViewById(R.id.chat);
		mChat.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(FriendInfoActivity.this, "You Press Chat", Toast.LENGTH_SHORT).show();				
			}
		});
	}
	
	
	



	private void switchCommentState()
	{
		
		
		if (mBCommentEditable)
		{
			mBCommentEditable = false;
			mCommentEditText.setFocusableInTouchMode(false);
			mCommentEditText.setFocusable(false);
			
			
			mChangeCommentTextView.setText("修改");   


			boolean ret = mKeyBorad.hideSoftInputFromWindow(mCommentEditText.getWindowToken(), 0);
			if (ret == false)	
			{
				mCommentEditText.setText(mCommentTmpString);
			}
		}else{			
			mBCommentEditable = true;			
			mCommentEditText.setFocusableInTouchMode(true);
			mCommentEditText.setFocusable(true);		
		
			mChangeCommentTextView.setText("确定");
	
			mKeyBorad.showSoftInput(mCommentEditText, 0);
			
			mCommentTmpString = mCommentEditText.getText().toString();
		}
	}
}
