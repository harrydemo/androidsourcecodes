package com.yarin.android.MyContacts;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ContactView extends Activity
{
	private TextView mTextViewName;
	private TextView mTextViewMobile;
	private TextView mTextViewHome;
	private TextView mTextViewAddress;
	private TextView mTextViewEmail;
	private TextView mTextViewBlog;
	
    private Cursor mCursor;
    private Uri mUri;
    
    private static final int REVERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private static final int EDITOR_ID = Menu.FIRST + 2;
    private static final int CALL_ID = Menu.FIRST + 3;
    private static final int SENDSMS_ID = Menu.FIRST + 4;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		mUri = getIntent().getData();
		
		this.setContentView(R.layout.viewuser);
		
		mTextViewName = (TextView) findViewById(R.id.TextView_Name);
		mTextViewMobile = (TextView) findViewById(R.id.TextView_Mobile);
		mTextViewHome = (TextView) findViewById(R.id.TextView_Home);
		mTextViewAddress = (TextView) findViewById(R.id.TextView_Address);
		mTextViewEmail = (TextView) findViewById(R.id.TextView_Email);
		mTextViewBlog = (TextView) findViewById(R.id.TextView_Blog);
		
	    // 获得并保存原始联系人信息
        mCursor = managedQuery(mUri, ContactColumn.PROJECTION, null, null, null);
        mCursor.moveToFirst();
	}
	
    protected void onResume()
	{
		super.onResume();
		if (mCursor != null)
		{
			// 读取并显示联系人信息
			mCursor.moveToFirst();
			
			mTextViewName.setText(mCursor.getString(ContactColumn.NAME_COLUMN));
			mTextViewMobile.setText(mCursor.getString(ContactColumn.MOBILENUM_COLUMN));
			mTextViewHome.setText(mCursor.getString(ContactColumn.HOMENUM_COLUMN));
			mTextViewAddress.setText(mCursor.getString(ContactColumn.ADDRESS_COLUMN));
			mTextViewEmail.setText(mCursor.getString(ContactColumn.EMAIL_COLUMN));
			mTextViewBlog.setText(mCursor.getString(ContactColumn.BLOG_COLUMN));
		}
		else
		{
			setTitle("错误信息");
		}
	}
    
    public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		//添加菜单
		menu.add(0, REVERT_ID, 0, R.string.revert).setShortcut('0', 'r').setIcon(R.drawable.listuser);
		menu.add(0, DELETE_ID, 0, R.string.delete_user).setShortcut('0', 'd').setIcon(R.drawable.remove);
		menu.add(0, EDITOR_ID, 0, R.string.editor_user).setShortcut('0', 'd').setIcon(R.drawable.edituser);
		menu.add(0, CALL_ID, 0, R.string.call_user).setShortcut('0', 'd').setIcon(R.drawable.calluser)
				.setTitle(this.getResources().getString(R.string.call_user)+mTextViewName.getText());
		menu.add(0, SENDSMS_ID, 0, R.string.sendsms_user).setShortcut('0', 'd').setIcon(R.drawable.sendsms)
		.setTitle(this.getResources().getString(R.string.sendsms_user)+mTextViewName.getText());
		return true;
	}
    
    public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			//删除
			case DELETE_ID:
				deleteContact();
				finish();
				break;
			//返回列表
			case REVERT_ID:
				setResult(RESULT_CANCELED);
				finish();
				break;
			case EDITOR_ID:
			//编辑联系人
				startActivity(new Intent(Intent.ACTION_EDIT, mUri)); 
				break;
			case CALL_ID:
			//呼叫联系人
		        Intent call = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+mTextViewMobile.getText()));
		        startActivity(call);
				break;
			case SENDSMS_ID:
			//发短信给联系人
		        Intent sms = new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:"+mTextViewMobile.getText()));
		        startActivity(sms);
				break;
		}
		return super.onOptionsItemSelected(item);
	}


	// 删除联系人信息
	private void deleteContact()
	{
		if (mCursor != null)
		{
			mCursor.close();
			mCursor = null;
			getContentResolver().delete(mUri, null, null);
			setResult(RESULT_CANCELED);
		}
	}
}

