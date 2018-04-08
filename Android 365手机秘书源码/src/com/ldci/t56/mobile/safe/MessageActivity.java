package com.ldci.t56.mobile.safe;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.OperationApplicationException;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.Contacts.Phones;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TabHost.OnTabChangeListener;

import com.ldci.t56.mobile.db.DbAdapter;
import com.ldci.t56.mobile.info.Message_Forbid_Info;
import com.ldci.t56.mobile.tool.BroadCastTool;

public class MessageActivity extends TabActivity implements OnTabChangeListener {
	
	private DbAdapter mDbAdapter;
	private Cursor mCursorTab1;
	private Cursor mCursorTab2;
	private ListView mListViewTab1;
	private ListView mListViewTab2;
	private static final int MENU_RUBBISH_BACK = Menu.FIRST;
	private static final int MENU_RUBBISH_REPLY = Menu.FIRST+1;
	private static final int MENU_RUBBISH_CALL = Menu.FIRST+2;
	private static final int MENU_RUBBISH_DELETE = Menu.FIRST+3;
	private static final int MENU_RUBBISH_MULTIPLE = Menu.FIRST+4;
	
	private static final int MENU_FORBID_REPLY = Menu.FIRST+5;
	private static final int MENU_FORBID_CALL = Menu.FIRST+6;
	private static final int MENU_FORBID_DELETE = Menu.FIRST+7;
	private static final int MENU_FORBID_MULTIPLE = Menu.FIRST+8;
	private static final int MENU_FORBID_ADD = Menu.FIRST+9;
	private static final int MENU_FORBID_ADD_FIRST = Menu.FIRST+10;

	private static final int MENU_ADD_CANTACT = Menu.FIRST+11;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabhost_public);
		initFindViewById();
		TabHost mTabHost=getTabHost();
		mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("拦截记录",getResources().getDrawable(R.drawable.tab_icon_1)).setContent(R.id.tabhost_public_listview));
		mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("黑名单",getResources().getDrawable(R.drawable.tab_icon_2)).setContent(R.id.tabhost_public_listview_tab2));
		mTabHost.setOnTabChangedListener(this);
		mTabHost.setCurrentTab(0);

		mDbAdapter = new DbAdapter(MessageActivity.this);
		mDbAdapter.open();
		
		initTab1Data();
		initTabListViewListener(mListViewTab1);
	}
	
	private MyBroadCastReceiver mMBCR;
	private IntentFilter mIF;
	@Override
	protected void onResume(){
		super.onResume();
		mIF = new IntentFilter();
		mIF.addAction(BroadCastTool.SMS_RECEIVED_ACTION);
		mMBCR = new MyBroadCastReceiver();
		this.registerReceiver(mMBCR, mIF);
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		this.unregisterReceiver(mMBCR);
		mDbAdapter.close();
	}
	/** 打電話*/
	Intent mIntent;
	private void actionCALL(int position, Cursor c,String column){
		c.moveToPosition(position);
		Uri uri = Uri.parse("tel:"+c.getString(c.getColumnIndex(column)));
		mIntent = new Intent(Intent.ACTION_CALL, uri);   
		startActivity(mIntent);   
	}
	/**發短信 */
	private void actionSMS(int position, Cursor c,String column){
		c.moveToPosition(position);
		mIntent = new Intent(Intent.ACTION_VIEW);
		mIntent.putExtra("address", c.getString(c.getColumnIndex(column)));
		mIntent.setType("vnd.android-dir/mms-sms");
		startActivity(mIntent);   
	}
	/** 刪除標記*/
	private void delMarkItemData(ListView lv,Cursor c,String table){
		int mContant = lv.getCount();
		for(int i=0;i!=mContant;i++){
			if(lv.getCheckedItemPositions().get(i)){
				c.moveToPosition(i);
				long rowId = c.getLong(c.getColumnIndex(DbAdapter.TABLE_ID));
				mDbAdapter.getDel(rowId, table);
			}
		}
	}
	private void returnMessage(){
		for(int i=0;i!=mListViewTab1.getCount();i++){
			if(mListViewTab1.getCheckedItemPositions().get(i)){
				
				ContentValues cv = new ContentValues();    
				cv.put("type", "1"); 
				mCursorTab1.moveToPosition(i);
				cv.put("address", mCursorTab1.getString(mCursorTab1.getColumnIndex(DbAdapter.MESSAGE_RUBBISH_PHONE)));  
				String rowContent = mCursorTab1.getString(mCursorTab1.getColumnIndex(DbAdapter.MESSAGE_RUBBISH_CONTENT));
				cv.put("body", rowContent); 
				getContentResolver().insert(Uri.parse("content://sms/inbox"), cv); 
				long rowId = mCursorTab1.getLong(mCursorTab1.getColumnIndex(DbAdapter.TABLE_ID));
				mDbAdapter.getDel(rowId, DbAdapter.MESSAGE_RUBBISH_TABLE_NAME);
			}
		}
	}
	private String getPhoneNumber(int position,Cursor c){		
		c.moveToPosition(position);
		return c.getString(c.getColumnIndex(DbAdapter.MESSAGE_RUBBISH_PHONE)); 
	}
	EditText mCallName;
	EditText mCallNumber;
	private void addContact(String phone){
		LayoutInflater mLI = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout mLL = (LinearLayout)mLI.inflate(R.layout.set_call_record_name, null);
		mCallName = (EditText)mLL.findViewById(R.id.EditText01);
		mCallNumber = (EditText)mLL.findViewById(R.id.EditText02);
		mCallNumber.setText(phone);
		new AlertDialog.Builder(this).setTitle("加入联系人").
		setView(mLL).
		setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(mCallName.getText().toString().length() != 0&&mCallNumber.getText().toString().length() != 0){
					
					ContentValues cv = new ContentValues();
					ArrayList<ContentProviderOperation> operationList = new ArrayList<ContentProviderOperation>();
					ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(RawContacts.CONTENT_URI);
					builder.withValues(cv);
					operationList.add(builder.build());
					builder = ContentProviderOperation.newInsert(Data.CONTENT_URI);
					builder.withValueBackReference(StructuredName.RAW_CONTACT_ID, 0);
	                builder.withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
	                builder.withValue(StructuredName.DISPLAY_NAME, mCallName.getText().toString());
	                operationList.add(builder.build());
	                builder = ContentProviderOperation.newInsert(Data.CONTENT_URI);
	                builder.withValueBackReference(Phone.RAW_CONTACT_ID, 0);
	                builder.withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
	                builder.withValue(Phone.NUMBER, mCallNumber.getText().toString());
	                builder.withValue(Data.IS_PRIMARY, 1);
	                operationList.add(builder.build());
	                try {
	                	getContentResolver().applyBatch(ContactsContract.AUTHORITY, operationList);
					} catch (RemoteException e) {
						e.printStackTrace();
					} catch (OperationApplicationException e) {
						e.printStackTrace();
					}
					Toast.makeText(getApplicationContext(), "成功添加!", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(getApplicationContext(), "姓名或号码不能为空!", Toast.LENGTH_SHORT).show();
					addContact(mCallNumber.getText().toString());
				}
			}
		}).setNegativeButton("取消", null).create().show();
	}
	
	/** 多选时打电话只拨打第一个被选中的号码，发短信给第一个被选中的号码发送*/
	private void callMethod(ListView lv,String sms_call,Cursor c,String column,String addContact){
		if(lv.getCheckedItemPositions().size() >= 1){
			for(int i=0;i!=lv.getCount();i++){
				if(lv.getCheckedItemPositions().get(i)){
					c.moveToPosition(i);
					String phone = c.getString(c.getColumnIndex(column));
					if(sms_call.equals("sms")){
						if("addContact".equals(addContact)){
							addContact(phone);
						}else{
							mIntent = new Intent(Intent.ACTION_VIEW);
							mIntent.putExtra("address", phone);
							mIntent.setType("vnd.android-dir/mms-sms");
							startActivity(mIntent);  
						} 
					}else if(sms_call.equals("call")){
						if("addContact".equals(addContact)){
							addContact(phone);
						}else{
							Uri uri = Uri.parse("tel:"+phone);
							mIntent = new Intent(Intent.ACTION_DIAL, uri);   
							startActivity(mIntent);
						}
					}
					break;
				}
			}
		}
	}
	
	/**-------------------------------初始化基本组件--------------------------------------------------------------------------------------------------*/
	private void initFindViewById(){
		mListViewTab1 = (ListView)findViewById(R.id.tabhost_public_listview);
		mListViewTab2 = (ListView)findViewById(R.id.tabhost_public_listview_tab2);
	}
	
	/**-------------------------------点击不同标签时的事件监听 ----------------------------------------------------------------------------------*/
	private boolean isCheckedTab1OrTab2 = false;
	public void onTabChanged(String tabId) {
		if("tab1".equals(tabId)){
			isCheckedTab1OrTab2 = false;
			initTab1Data();
		}else if("tab2".equals(tabId)){
			isCheckedTab1OrTab2 = true;
			initTab2Data();
		}
	}
	
	private int i = 0;
	/**---------------------------------------------从数据库中获得并加载垃圾短信数据Start-------------------------------------------------*/
	
	private void initTab1Data(){
		mCursorTab1=mDbAdapter.getAll(DbAdapter.MESSAGE_RUBBISH_TABLE_NAME)	;
		this.stopManagingCursor(mCursorTab1);
		i = 0;
		int mCount = mCursorTab1.getCount();
		if(mCount >= 0){
			mArray = new String[mCount];
			if(mCursorTab1.moveToFirst()){
				do{
					mArray[i] = "垃圾号码:"+mCursorTab1.getString(mCursorTab1.getColumnIndex(DbAdapter.MESSAGE_RUBBISH_PHONE))+"\n"+ 
										 "收件时间:"+mCursorTab1.getString(mCursorTab1.getColumnIndex(DbAdapter.MESSAGE_RUBBISH_TIME))+"\n\n"+
										 "短信内容:\n"+mCursorTab1.getString(mCursorTab1.getColumnIndex(DbAdapter.MESSAGE_RUBBISH_CONTENT));
					i++;
				}while(mCursorTab1.moveToNext());
			}
			ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(MessageActivity.this,android.R.layout.simple_list_item_checked,mArray);
			mListViewTab1.setAdapter(mAdapter);
			mListViewTab1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		}
		
	}
	
	String[] mArray;
	private void initTab2Data(){
		mCursorTab2 = mDbAdapter.getAll(DbAdapter.MESSAGE_FORBID_TABLE_NAME);
		this.startManagingCursor(mCursorTab2);		
		i = 0;
		int mCount = mCursorTab2.getCount();
		if(mCount >= 0){
			mArray = new String[mCount];
			if(mCursorTab2.moveToFirst()){
				do{
					mArray[i] = mCursorTab2.getString(mCursorTab2.getColumnIndex(DbAdapter.MESSAGE_FORBID_PHONE));
					i++;
				}while(mCursorTab2.moveToNext());
			}
			ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(MessageActivity.this,android.R.layout.simple_list_item_checked,mArray);
			mListViewTab2.setAdapter(mAdapter);
			mListViewTab2.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		}
		
	}
	/*---------------------------------------------从数据库中获得并加载垃圾短信数据End----------------------------------------------------*/
	
	/**---------------------------------------------列表项的长按事件监听Start---------------------------------------------------------------------*/
	private void initTabListViewListener(final ListView listview){
//		listview.setOnItemClickListener(new OnItemClickListener(){//单击事件监听
//			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
//					long id) {
				
//			}
//		});
		listview.setOnItemLongClickListener(new OnItemLongClickListener(){//长按事件监听
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(MessageActivity.this, mArray[position], Toast.LENGTH_LONG).show();
				return false;
			}
		});
	}
	/*---------------------------------------------列表项的长按事件监听End---------------------------------------------------------------------*/
	
	/*--------------------------------------------------------------------------------------关于动态菜单Begin---------------------------------------*/
	boolean isCreatePreparedOptionMenu1 = true;
	boolean isCreatePreparedOptionMenu2 = false;
	boolean isCreatePreparedOptionMenu3 = false;
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if(isCheckedTab1OrTab2 == false){
			isCreatePreparedOptionMenu3 = false;
			if(mListViewTab1.getCount()>0){
				if(isCreatePreparedOptionMenu1){
					menu.add(0, MENU_RUBBISH_REPLY, 0, "回复").setIcon(android.R.drawable.ic_menu_send);
					menu.add(0, MENU_RUBBISH_CALL, 0, "拨打").setIcon(android.R.drawable.ic_menu_call);
					menu.add(0, MENU_RUBBISH_DELETE, 0, "删除").setIcon(android.R.drawable.ic_menu_close_clear_cancel);
					menu.add(0, MENU_RUBBISH_MULTIPLE, 0, "清空").setIcon(android.R.drawable.ic_menu_delete);
					menu.add(0, MENU_RUBBISH_BACK, 0, "恢复").setIcon(android.R.drawable.ic_menu_rotate);
					menu.add(0, MENU_ADD_CANTACT, 0, "加入联系人").setIcon(android.R.drawable.ic_menu_add);
					isCreatePreparedOptionMenu1 = false;
				}
			}else{
				menu.removeItem(MENU_RUBBISH_BACK);
				menu.removeItem(MENU_RUBBISH_REPLY);
				menu.removeItem(MENU_RUBBISH_CALL);
				menu.removeItem(MENU_RUBBISH_DELETE);
				menu.removeItem(MENU_RUBBISH_MULTIPLE);
				menu.removeItem(MENU_ADD_CANTACT);
				isCreatePreparedOptionMenu1 = true;
			}
		}else{
			menu.removeItem(MENU_RUBBISH_BACK);
			menu.removeItem(MENU_RUBBISH_REPLY);
			menu.removeItem(MENU_RUBBISH_CALL);
			menu.removeItem(MENU_RUBBISH_DELETE);
			menu.removeItem(MENU_RUBBISH_MULTIPLE);
			menu.removeItem(MENU_ADD_CANTACT);
			isCreatePreparedOptionMenu1 = true;
		}
		
		if(isCheckedTab1OrTab2 == true){
			if(mListViewTab2.getCount()>0){
				if(!isCreatePreparedOptionMenu2){
					menu.add(0, MENU_FORBID_ADD, 0, "添加").setIcon(android.R.drawable.ic_menu_add);
					menu.add(0, MENU_FORBID_REPLY, 0, "回复").setIcon(android.R.drawable.ic_menu_send);
					menu.add(0, MENU_FORBID_CALL, 0, "拨打").setIcon(android.R.drawable.ic_menu_call);
					menu.add(0, MENU_FORBID_DELETE, 0, "删除").setIcon(android.R.drawable.ic_menu_close_clear_cancel);
					menu.add(0, MENU_FORBID_MULTIPLE, 0, "清空").setIcon(android.R.drawable.ic_menu_delete);
					isCreatePreparedOptionMenu2 = true;
				}
				menu.removeItem(MENU_FORBID_ADD_FIRST);
				isCreatePreparedOptionMenu3 = false;
			}else{
				if(!isCreatePreparedOptionMenu3){
					menu.add(0, MENU_FORBID_ADD_FIRST, 0, "添加").setIcon(android.R.drawable.ic_menu_add);
					isCreatePreparedOptionMenu3 = true;
				}
				menu.removeItem(MENU_FORBID_REPLY);
				menu.removeItem(MENU_FORBID_CALL);
				menu.removeItem(MENU_FORBID_DELETE);
				menu.removeItem(MENU_FORBID_MULTIPLE);
				menu.removeItem(MENU_FORBID_ADD);
				isCreatePreparedOptionMenu2 = false;
			}
		}else{
			menu.removeItem(MENU_FORBID_REPLY);
			menu.removeItem(MENU_FORBID_CALL);
			menu.removeItem(MENU_FORBID_DELETE);
			menu.removeItem(MENU_FORBID_MULTIPLE);
			menu.removeItem(MENU_FORBID_ADD);
			menu.removeItem(MENU_FORBID_ADD_FIRST);
			isCreatePreparedOptionMenu2 = false;
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int positionTab1 = mListViewTab1.getSelectedItemPosition();
		int positionTab2 = mListViewTab2.getSelectedItemPosition();
		switch(item.getItemId()){
		case MENU_RUBBISH_REPLY:
			if(positionTab1 != -1){
				actionSMS(positionTab1,mCursorTab1,DbAdapter.MESSAGE_RUBBISH_PHONE);
			}else{
				callMethod(mListViewTab1,"sms",mCursorTab1,DbAdapter.MESSAGE_RUBBISH_PHONE,"");
			}
			break;
		case MENU_RUBBISH_CALL:
			if(positionTab1 != -1){
				actionCALL(positionTab1,mCursorTab1,DbAdapter.MESSAGE_RUBBISH_PHONE);
			}else{
				callMethod(mListViewTab1,"call",mCursorTab1,DbAdapter.MESSAGE_RUBBISH_PHONE,"");
			}
			break;
		case MENU_RUBBISH_DELETE:
			if(positionTab1 != -1){
				mCursorTab1.moveToPosition(positionTab1);
				long rowId = mCursorTab1.getLong(mCursorTab1.getColumnIndex(DbAdapter.TABLE_ID));
				mDbAdapter.getDel(rowId, DbAdapter.MESSAGE_RUBBISH_TABLE_NAME);
			}else{
				delMarkItemData(mListViewTab1,mCursorTab1,DbAdapter.MESSAGE_RUBBISH_TABLE_NAME);
			}
			initTab1Data();
			break;
		case MENU_RUBBISH_MULTIPLE:
			mDbAdapter.deleteTable(DbAdapter.MESSAGE_RUBBISH_TABLE_NAME);
			initTab1Data();
			break;
		case MENU_RUBBISH_BACK:
			returnMessage();
			initTab1Data();
			break;
		case MENU_FORBID_ADD:
		case MENU_FORBID_ADD_FIRST:
			addForbidPhone();
			break;
		case MENU_FORBID_REPLY:
			if(positionTab2 != -1){
				actionSMS(positionTab2,mCursorTab2,DbAdapter.MESSAGE_FORBID_PHONE);
			}else{
				callMethod(mListViewTab2,"sms",mCursorTab2,DbAdapter.MESSAGE_FORBID_PHONE,"");
			}
			break;
		case MENU_FORBID_CALL:
			if(positionTab2 != -1){
				actionCALL(positionTab2,mCursorTab2,DbAdapter.MESSAGE_FORBID_PHONE);
			}else{
				callMethod(mListViewTab2,"call",mCursorTab2,DbAdapter.MESSAGE_FORBID_PHONE,"");
			}
			break;
		case MENU_FORBID_DELETE:
			if(positionTab2 != -1){
				mCursorTab2.moveToPosition(positionTab2);
				long rowId = mCursorTab2.getLong(mCursorTab2.getColumnIndex(DbAdapter.TABLE_ID));
				mDbAdapter.getDel(rowId, DbAdapter.MESSAGE_FORBID_TABLE_NAME);
			}else{
				delMarkItemData(mListViewTab2,mCursorTab2,DbAdapter.MESSAGE_FORBID_TABLE_NAME);
			}
			initTab2Data();
			break;
		case MENU_FORBID_MULTIPLE:
			mDbAdapter.deleteTable(DbAdapter.MESSAGE_FORBID_TABLE_NAME);
			initTab2Data();
			break;
		case MENU_ADD_CANTACT:
			if(positionTab1 != -1){
				addContact(getPhoneNumber(positionTab1, mCursorTab1));
			}else{
				callMethod(mListViewTab1,"sms",mCursorTab1,DbAdapter.MESSAGE_RUBBISH_PHONE,"addContact");
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	/*--------------------------------------------------------------------------------------关于动态菜单End----------------------------------------------*/
	
	/*--------------------------------------------------------------------------------------接收到垃圾短信时的广播接收器Begin------------------*/
	class MyBroadCastReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(BroadCastTool.SMS_RECEIVED_ACTION)){
				initTab1Data();
			}
		}
	}
	/*--------------------------------------------------------------------------------------接收到垃圾短信时的广播接收器End---------------------*/
	
	/**---------------------------------------------------------------------------------------添加黑名单的Dialog-------------------------------------------*/
	EditText mForbidPhone ;
	private void addForbidPhone(){
		LayoutInflater mLI = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout mRL = (RelativeLayout)mLI.inflate(R.layout.message_forbid_add, null);
		mForbidPhone = (EditText)mRL.findViewById(R.id.new_forbid_phone);
		Button mButton=(Button)mRL.findViewById(R.id.get_system_contact);
		mButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showMulitAlertDialog();
			}
		});
		new AlertDialog.Builder(MessageActivity.this).setTitle("添加黑名单").setView(mRL)
		.setPositiveButton("确定", new OnClickListener(){
			public void onClick(DialogInterface arg0, int arg1) {
				
				String mNewForbidPhone = mForbidPhone.getText().toString();
				
				if(mNewForbidPhone.length() < 3){
					Toast.makeText(MessageActivity.this, "输入错误!", Toast.LENGTH_SHORT).show();
				}else{
					String mStr[] = mNewForbidPhone.split(";");
					for(int i = 0;i != mStr.length;i++){
						String phone = mStr[i].split(":").length == 1?mStr[i].split(":")[0]:mStr[i].split(":")[1];
						if(!(mDbAdapter.getPhone(phone, 2)).moveToFirst()){
							Message_Forbid_Info mMFI = new Message_Forbid_Info();
							mMFI.setMessage_forbid_phone(phone);
							mDbAdapter.getAdd(mMFI);
						}else{
							Toast.makeText(MessageActivity.this, "号码"+phone+"已存在!", Toast.LENGTH_SHORT).show();
						}
					}
					initTab2Data();
					Toast.makeText(MessageActivity.this, "添加成功!", Toast.LENGTH_SHORT).show();
				}
			}
		}).setNegativeButton("取消", null).show();
	}
	
	private ListView mLV;
	@SuppressWarnings("deprecation")
	private void showMulitAlertDialog(){
		Cursor mCursor=getContentResolver().query(Phones.CONTENT_URI, new String[]{"_id","name","number"}, null, null, "number desc");
		startManagingCursor(mCursor);
		if(mCursor.moveToFirst()){
			int m = 0;
			String mContant[] = new String[mCursor.getCount()];
			do{
				String phoneNumber = mCursor.getString(mCursor.getColumnIndex(Phones.NUMBER));
				mContant[m] = mCursor.getString(mCursor.getColumnIndex(Phones.NAME))+":"+(phoneNumber.length() == 13?phoneNumber.substring(2):phoneNumber);
				m++;
			}while(mCursor.moveToNext());
			initListData(mContant);
		}
	}
	
	String checkInfo;
	private void initListData(final String[] contant){
		AlertDialog mAlertDialog=new AlertDialog.Builder(this).setMultiChoiceItems(contant,
				new boolean[contant.length], 
				new DialogInterface.OnMultiChoiceClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which, boolean isChecked) {
						
					}
				}).setPositiveButton("确定", new DialogInterface.OnClickListener() {	
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						checkInfo = "";
						for(int i=0;i!=contant.length;i++){
							if(mLV.getCheckedItemPositions().get(i)){
								checkInfo += mLV.getAdapter().getItem(i).toString()+";";
							}
						}
						mForbidPhone.setText(checkInfo.substring(0, checkInfo.length()));
					}
				}).setNegativeButton("取消", null).create();
		mLV=mAlertDialog.getListView();
		mAlertDialog.show();
	}

}