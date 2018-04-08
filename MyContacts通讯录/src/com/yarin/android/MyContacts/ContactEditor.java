package com.yarin.android.MyContacts;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ContactEditor extends Activity
{
	private static final String TAG = "ContactEditor";
	
	
	private static final int STATE_EDIT = 0;
    private static final int STATE_INSERT = 1;
    
    private static final int REVERT_ID = Menu.FIRST;
    private static final int DISCARD_ID = Menu.FIRST + 1;
    private static final int DELETE_ID = Menu.FIRST + 2;
    
    private Cursor mCursor;
    private int mState;
    private Uri mUri;
    private EditText nameText;
    private EditText mobileText;
    private EditText homeText;
    private EditText addressText;
    private EditText emailText;
    private EditText blogText;
    
    private Button okButton;
    private Button cancelButton;
    
	public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);

		final Intent intent = getIntent();
		final String action = intent.getAction();
		Log.e(TAG + ":onCreate", action);
		//根据action的不同进行不同的操作
		//编辑联系人
		if (Intent.ACTION_EDIT.equals(action))
		{
			mState = STATE_EDIT;
			mUri = intent.getData();
		}
		else if (Intent.ACTION_INSERT.equals(action))
		{
			//添加新联系人
			mState = STATE_INSERT;
			mUri = getContentResolver().insert(intent.getData(), null);

			if (mUri == null)
			{
				Log.e(TAG + ":onCreate", "Failed to insert new Contact into " + getIntent().getData());
				finish();
				return;
			}
			setResult(RESULT_OK, (new Intent()).setAction(mUri.toString()));

		}
		else
		{
			Log.e(TAG + ":onCreate", " unknown action");
			finish();
			return;
		}
        
        setContentView(R.layout.editorcontacts);
        
        nameText = (EditText) findViewById(R.id.EditText01);
        mobileText = (EditText) findViewById(R.id.EditText02);
        homeText = (EditText) findViewById(R.id.EditText03);
        addressText = (EditText) findViewById(R.id.EditText04);
        emailText = (EditText) findViewById(R.id.EditText05);
        blogText = (EditText) findViewById(R.id.EditText06);
        
        okButton = (Button)findViewById(R.id.Button01);
        cancelButton = (Button)findViewById(R.id.Button02);
        
        okButton.setOnClickListener(new OnClickListener()
        {
			public void onClick(View v) 
			{
				String text = nameText.getText().toString();
				if(text.length()==0)
				{
					//如果没有输入东西，则不添加记录
					setResult(RESULT_CANCELED);
					deleteContact();
					finish();
				}
				else
				{
					//添加一条数据
					updateContact();
				}
			}
        	
        });
        cancelButton.setOnClickListener(new OnClickListener()
        {
			public void onClick(View v) 
			{
				if(mState == STATE_INSERT)
				{
					//不添加记录
					setResult(RESULT_CANCELED);
					deleteContact();
					finish();
				}
				else
				{
					//恢复到编辑前的状态
					backupContact();
				}
			}
        });
        
        
        Log.e(TAG+":onCreate", mUri.toString());
        // 获得并保存原始联系人信息
        mCursor = managedQuery(mUri, ContactColumn.PROJECTION, null, null, null);
        mCursor.moveToFirst();
        Log.e(TAG, "end of onCreate()");
	}
	
	
    protected void onResume()
	{
		super.onResume();
		if (mCursor != null)
		{
			Log.e(TAG + ":onResume", "count:" + mCursor.getColumnCount());
			// 读取并显示联系人信息
			mCursor.moveToFirst();
			if (mState == STATE_EDIT)
			{
				setTitle(getText(R.string.editor_user));
			}
			else if (mState == STATE_INSERT)
			{
				setTitle(getText(R.string.add_user));
			}
			String name = mCursor.getString(ContactColumn.NAME_COLUMN);
			String moblie = mCursor.getString(ContactColumn.MOBILENUM_COLUMN);
			String home = mCursor.getString(ContactColumn.HOMENUM_COLUMN);
			String address = mCursor.getString(ContactColumn.ADDRESS_COLUMN);
			String email = mCursor.getString(ContactColumn.EMAIL_COLUMN);
			String blog = mCursor.getString(ContactColumn.BLOG_COLUMN);
			
			nameText.setText(name);
			mobileText.setText(moblie);
			homeText.setText(home);
			addressText.setText(address);
			emailText.setText(email);
			blogText.setText(blog);
		}
		else
		{
			setTitle("错误信息");
		}
	}
    
    protected void onPause()
	{
		super.onPause();
		if (mCursor != null)
		{
			String text = nameText.getText().toString();
			if (text.length() == 0)
			{
				Log.e(TAG + ":onPause", "nameText is null ");
				setResult(RESULT_CANCELED);
				deleteContact();
				// 更新信息
			}
			else
			{
				ContentValues values = new ContentValues();
				values.put(ContactColumn.NAME, nameText.getText().toString());
				values.put(ContactColumn.MOBILENUM, mobileText.getText().toString());
				values.put(ContactColumn.HOMENUM, homeText.getText().toString());
				values.put(ContactColumn.ADDRESS, addressText.getText().toString());
				values.put(ContactColumn.EMAIL, emailText.getText().toString());
				values.put(ContactColumn.BLOG, blogText.getText().toString());
				
				Log.e(TAG + ":onPause", mUri.toString());
				Log.e(TAG + ":onPause", values.toString());
				getContentResolver().update(mUri, values, null, null);
			}
		}
	}
    
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        super.onCreateOptionsMenu(menu);

        if (mState == STATE_EDIT) 
        {
            menu.add(0, REVERT_ID, 0, R.string.revert)
                    .setShortcut('0', 'r')
                    .setIcon(R.drawable.listuser);
            menu.add(0, DELETE_ID, 0, R.string.delete_user)
            .setShortcut('0', 'f')
            .setIcon(R.drawable.remove);
        } 
        else 
        {
            menu.add(0, DISCARD_ID, 0, R.string.revert)
                    .setShortcut('0', 'd')
                    .setIcon(R.drawable.listuser);
        }
        return true;
    }
    //菜单处理
	@Override
    public boolean onOptionsItemSelected(MenuItem item) 
	{
        switch (item.getItemId()) 
        {
        case DELETE_ID:
        	deleteContact();
            finish();
            break;
        case DISCARD_ID:
        	cancelContact();
            break;
        case REVERT_ID:
        	backupContact();
            break;
        }
        return super.onOptionsItemSelected(item);
    }
	
	//删除联系人信息
	private void deleteContact() 
	{
		if (mCursor != null) 
		{
            mCursor.close();
            mCursor = null;
            getContentResolver().delete(mUri, null, null);
            nameText.setText("");
        }
	}
	//丢弃信息
	private void cancelContact() 
	{
		if (mCursor != null) 
		{
			deleteContact();
        }
        setResult(RESULT_CANCELED);
        finish();
	}
	//更新 变更的信息
	private void updateContact() 
	{
		if (mCursor != null) 
		{
			mCursor.close();
            mCursor = null;
            ContentValues values = new ContentValues();
			values.put(ContactColumn.NAME, nameText.getText().toString());
			values.put(ContactColumn.MOBILENUM, mobileText.getText().toString());
			values.put(ContactColumn.HOMENUM, homeText.getText().toString());
			values.put(ContactColumn.ADDRESS, addressText.getText().toString());
			values.put(ContactColumn.EMAIL, emailText.getText().toString());
			values.put(ContactColumn.BLOG, blogText.getText().toString());
            Log.e(TAG+":onPause",mUri.toString());
            Log.e(TAG+":onPause",values.toString());
            getContentResolver().update(mUri, values, null, null);
        }
        setResult(RESULT_CANCELED);
        finish();
	}
	//取消用，回退到最初的信息
	private void backupContact() 
	{
		if (mCursor != null) 
		{
			mCursor.close();
            mCursor = null;
            ContentValues values = new ContentValues();
			values.put(ContactColumn.NAME, nameText.getText().toString());
			values.put(ContactColumn.MOBILENUM, mobileText.getText().toString());
			values.put(ContactColumn.HOMENUM, homeText.getText().toString());
			values.put(ContactColumn.ADDRESS, addressText.getText().toString());
			values.put(ContactColumn.EMAIL, emailText.getText().toString());
			values.put(ContactColumn.BLOG, blogText.getText().toString());
            Log.e(TAG+":onPause",mUri.toString());
            Log.e(TAG+":onPause",values.toString());
            getContentResolver().update(mUri, values, null, null);
        }
        setResult(RESULT_CANCELED);
        finish();
	}
}

