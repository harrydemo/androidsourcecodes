/**
 * 
 */
package com.ty.shortcall;

import android.app.Activity;
import android.app.TabActivity;
import android.appwidget.AppWidgetManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Contacts;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.TabSpec;

/**
 * 作用:配置快速拨号联系人信息
 * 
 * @author TANG YONG
 * @version 1.0 2010-06-21
 * 
 */
public class ConfiguresActivity extends TabActivity implements OnItemClickListener, OnClickListener {	
	private static final String TAG = "ConfiguresActivity";
	public final static String PRE_NAME = "Configure";	
	
	private static final String[] CONTACT_PROJ = new String[]{
		Contacts.People._ID,
		Contacts.People.NAME,
		Contacts.People.NUMBER
	};
	
	private Cursor cursor;
	
	private TabHost tabHost;
	private ListView contactList;
	private EditText nameEdit;
	private EditText numberEdit;
	private Button saveBtn;
	
	private int widgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	//private String name, number;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);		
		
		tabHost = getTabHost();
		LayoutInflater.from(this).inflate(R.layout.configures, tabHost.getTabContentView(), true);
		
		TabSpec tsContact = tabHost.newTabSpec("contact");		
		tsContact.setIndicator(getString(R.string.contactTabText), getResources().getDrawable(R.drawable.globe));
		tsContact.setContent(R.id.contactList);
		tabHost.addTab(tsContact);
		
		TabSpec tsInput = tabHost.newTabSpec("input");		
		tsInput.setIndicator(getString(R.string.inputTabText), getResources().getDrawable(R.drawable.tag));
		tsInput.setContent(R.id.inputLinear);
		tabHost.addTab(tsInput);
		
		tabHost.setCurrentTab(0);
		contactList = (ListView)findViewById(R.id.contactList);
		
		// 处理获取联系人列表
		dealContactList(contactList);
		
		setContentView(tabHost);
        
		// 获取控件信息;
		nameEdit = (EditText)findViewById(R.id.cfgContactEdit);
		numberEdit = (EditText)findViewById(R.id.cfgPhoneEdit);
		
		saveBtn = (Button)findViewById(R.id.saveBtn);
		contactList.setOnItemClickListener(this);
		saveBtn.setOnClickListener(this);
		
		// 获取widgetId信息
		widgetId = getIntent().getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
		setConfigureResult(Activity.RESULT_CANCELED);
        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
        
        if(savedInstanceState != null){
        	nameEdit.setText(savedInstanceState.getString("name"));
        	numberEdit.setText(savedInstanceState.getString("number"));
        }
	}

	private void dealContactList(ListView lv) {
		// TODO Auto-generated method stub
		Log.d(TAG, "deal with contact list");
		ContentResolver cr = getContentResolver();
		cursor = cr.query(Contacts.People.CONTENT_URI, CONTACT_PROJ, Contacts.People.NUMBER + " is not null", null, Contacts.People.DEFAULT_SORT_ORDER);
		cursor.moveToFirst();
		
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.contactitems, cursor, new String[]{Contacts.People.NAME, Contacts.People.NUMBER}, new int[]{R.id.itemNameText, R.id.itemNumberText});
		
		lv.setAdapter(adapter);
	}

	public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
		// TODO Auto-generated method stub
		TextView nameText = (TextView)v.findViewById(R.id.itemNameText);
		TextView numberText = (TextView)v.findViewById(R.id.itemNumberText);
		
		String name = nameText.getText().toString();
		String number = numberText.getText().toString();
		
		if (number != null && number.length() > 0){
			saveResult(name, number);
		}
	}

	/**
	 *
	 */
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.saveBtn){
			
			String name = nameEdit.getText().toString();
			String number = numberEdit.getText().toString();
			
			if (number != null && number.length() > 0){
				saveResult(name, number);
			}
		}
	}
	
	/**
	 *  保存数据
	 * @param contactName
	 * @param contactNumber
	 */
	private void saveResult(String contactName, String contactNumber) {
		// TODO Auto-generated method stub
		AppWidgetManager manager = AppWidgetManager.getInstance(this);
		
		// 存储信息至本地
		SharedPreferences sp = getSharedPreferences(PRE_NAME + widgetId, 0);
		Editor editor = sp.edit();
		editor.putString("title", contactName);
		editor.putString("phoneNumber", contactNumber);
		editor.commit();		
		
		setConfigureResult(Activity.RESULT_OK);
        finish();
        
        ShortCallWidget.updateAppWidget(this, manager, widgetId);
	}

	/**
     * Convenience method to always include {@link #widgetId} when setting
     * the result {@link Intent}.
     */
    public void setConfigureResult(int resultCode) {
        final Intent data = new Intent();
        data.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        setResult(resultCode, data);
    }
}
