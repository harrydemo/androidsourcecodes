package com.ldci.t56.mobile.safe;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class SensitiveActivity extends ListActivity {
	
	private List<String> mList = new ArrayList<String>();
	private StringBuilder mStringBuilder = new StringBuilder();
	private SharedPreferences mSharedPreferences;
	public final int SENSITIVE_ADD = Menu.FIRST;
	public final int SENSITIVE_DELETE = Menu.FIRST+1;
	public final int SENSITIVE_MULTIPLE = Menu.FIRST+2;
	public static int single_or_multiple = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSharedPreferences = this.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
		Log.d("debug", "mSharedPreferences is "+mSharedPreferences.getString("sensitive", ""));
		String xmlInfo = mSharedPreferences.getString("sensitive", "");
		if(xmlInfo.length() != 0){
			String[] mArray = xmlInfo.substring(0, xmlInfo.length()-1).split(",");
			for(int i = 0;i != mArray.length;i++){
				Log.d("debug", "mArray["+i+"] is "+mArray[i]);
				mList.add(mArray[i]);
			}
			singleMode(mArray);
		}
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		for(int i = 0; i != mList.size();i++){
			mStringBuilder.append(mList.get(i));
			mStringBuilder.append(",");
		}
		mSharedPreferences = this.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
		SharedPreferences.Editor mEditor = mSharedPreferences.edit();
		mEditor.putString("sensitive", mStringBuilder.toString());
		mEditor.commit();
	}
	
	private void initListData(){
		
		String[] mArray = new String[mList.size()];
		for(int i = 0; i != mList.size();i++){
			mArray[i] = mList.get(i);
		}
		if(single_or_multiple == 1){
			singleMode(mArray);
		}else if(single_or_multiple == 2){
			multipleMode(mArray);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.addSubMenu(0, SENSITIVE_ADD, 0, "添加").setIcon(android.R.drawable.ic_menu_add);
		return super.onCreateOptionsMenu(menu);
	}

	boolean isCreateOptionsMenu = false;
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if(getListView().getCount() > 0){
			if(isCreateOptionsMenu == false){
				menu.addSubMenu(0, SENSITIVE_DELETE, 0, "删除").setIcon(android.R.drawable.ic_menu_delete);
				menu.addSubMenu(0, SENSITIVE_MULTIPLE, 0, "多选").setIcon(android.R.drawable.ic_menu_manage);
				isCreateOptionsMenu = true;
			}
		}else{
			menu.removeItem(SENSITIVE_DELETE);
			menu.removeItem(SENSITIVE_MULTIPLE);
			isCreateOptionsMenu = false;
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case SENSITIVE_ADD:
			single_or_multiple = 1;
			addSensitive();
			break;
		case SENSITIVE_DELETE:
			if(single_or_multiple == 1){
				int position = getListView().getSelectedItemPosition();
				if(position != -1){
					mList.remove(position);
					initListData();
				}
			}else{
				
				
				
				
				
				
//-------------------------------------------------------------------------------多选的删除：开始----------------------------------------------------------------			
				Log.d("SensitiveActivity", "mList size is"+mList.size());
				Log.d("SensitiveActivity", "checked item size is "+getListView().getCheckedItemPositions().size());
				int m = mList.size();
				for(int i = 0;i != m;i++){
					if(getListView().getCheckedItemPositions().get(i)){
						Log.d("SensitiveActivity", "item checked info is inner :"+i+" is "+getListView().getCheckedItemPositions().get(i));
						Log.d("SensitiveActivity", "item content info is :"+i+" is "+getListView().getAdapter().getItem(i));
						if(mList.contains(getListView().getAdapter().getItem(i))){
							mList.remove(getListView().getAdapter().getItem(i));
						}
					}
				}
				Log.d("SensitiveActivity", "initListData start");
				initListData();
				Log.d("SensitiveActivity", "initListData end");
//--------------------------------------------------------------------------------多选的删除：结束----------------------------------------------------------------				
				
				
				
				
				
				
				
				
			}
			break;
		case SENSITIVE_MULTIPLE:
			single_or_multiple = 2;
			initListData();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	/**单选模式*/
	private void singleMode(String[] mArray){
		ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_checked,mArray);
		getListView().setAdapter(mAdapter);
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}
	
	/**多选模式*/
	private void multipleMode(String[] mArray){
		ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, mArray);
		getListView().setAdapter(mAdapter);
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}
	
	/**添加新敏感词*/
	private void addSensitive(){
		LayoutInflater mLI = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout mLL = (LinearLayout)mLI.inflate(R.layout.set_sensitive_add, null);
		final EditText mNewSensitive = (EditText)mLL.findViewById(R.id.set_sensitive_add);
		new AlertDialog.Builder(SensitiveActivity.this).setTitle("添加新敏感词").setMessage("请填写不多于5个汉字或5个字母，数字的组合").setView(mLL)
		.setPositiveButton("确定", new OnClickListener(){
			public void onClick(DialogInterface arg0, int arg1) {
				//数据库的操作
				if(mNewSensitive.getText().toString().length() == 0||mNewSensitive.getText().toString().length() > 5){
					Toast.makeText(SensitiveActivity.this, "输入错误!", Toast.LENGTH_SHORT).show();
				}else{
					Log.d("debug", "the sensitive is "+mNewSensitive.getText().toString()+" and the length is "+mNewSensitive.getText().toString().length());
					mList.add(mNewSensitive.getText().toString());
					initListData();
				}
			}
		}).setNegativeButton("取消", null).show();
	}
}
