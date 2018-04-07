package com.xcontacts.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.AggregationExceptions;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.xcontacts.utils.MyLog;

/**
 * 显示某一个分组下的所有联系人
 * 
 * @author Lefter
 * 
 */
public class ContactsInOneGroup extends Activity {
	// ContextMenu,将被点击的联系人移出当前分组
	private static final int CONTEXT_MENU_REMOVE = Menu.FIRST;

	private SimpleCursorAdapter mAdapter;
	private ListView mListView;
	private long groupId;
	private Cursor mCursor;
	private StringBuilder sb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.list_groups);
		setupListView();
		registerForContextMenu(mListView);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == ListView.INVALID_POSITION) {
					throw new IllegalArgumentException(
							"Position not in list bounds");
				}
				final Cursor cursor = (Cursor) mAdapter.getItem(position);
				if (cursor != null) {
					// Build and return soft, lookup reference
					final long contactId = cursor.getLong(cursor
							.getColumnIndex(Contacts._ID));
					final String lookupKey = cursor.getString(cursor
							.getColumnIndex(Contacts.LOOKUP_KEY));
					Intent intent = new Intent(Intent.ACTION_VIEW, Contacts
							.getLookupUri(contactId, lookupKey));
					startActivity(intent);
					finish();
				}
			}
		});
	}

	/**
	 * 初始化ListView、Adapter、Cursor
	 */
	private void setupListView() {
		MyLog.i("***setupListView***");
		Intent intent = getIntent();
		Uri uri = intent.getData();
		// 取得被点击的分组的ID
		groupId = ContentUris.parseId(uri);
		MyLog.i("被点击的分组的id: " + groupId);
		// 查询Data中与该group相关的信息
		String groupSelection = Data.MIMETYPE + " = ?" + " AND "
				+ GroupMembership.GROUP_ROW_ID + " = ?";
		String[] groupSelectionArgs = new String[] {
				GroupMembership.CONTENT_ITEM_TYPE, String.valueOf(groupId) };
		Cursor groupCursor = getContentResolver().query(Data.CONTENT_URI, null,
				groupSelection, groupSelectionArgs, null);
		int count = groupCursor.getCount();
		MyLog.d("count of rawcontacts in this group:" + count);
		sb = new StringBuilder();
		long rawContactId;
		long contactId;
		for (int i = 0; i < count; i++) {
			groupCursor.moveToPosition(i);
			rawContactId = groupCursor.getLong(groupCursor
					.getColumnIndex(GroupMembership.RAW_CONTACT_ID));
			contactId = queryForContactId(getContentResolver(), rawContactId);
			sb.append(contactId);
			if (i != count - 1) {
				sb.append(',');
			}
			MyLog.i("	******			");
			MyLog.i("	RawContactId: " + rawContactId);
			MyLog.i("	ContactId: " + contactId);
		}
		groupCursor.close();
		MyLog.i("所有的ContactId: " + sb.toString());
		// 构造查询条件
		String selection = Contacts._ID + " in ( " + sb.toString() + " )";
		String sortOrder = Contacts.DISPLAY_NAME + "  COLLATE LOCALIZED ASC ";
		mCursor = getContentResolver().query(Contacts.CONTENT_URI, null,
				selection, null, sortOrder);
		mAdapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_1, mCursor,
				new String[] { Contacts.DISPLAY_NAME },
				new int[] { android.R.id.text1 });
		mListView = (ListView) findViewById(R.id.groupsListView);
		mListView.setAdapter(mAdapter);
	}

	/**
	 * 查询RawContacts中_id等于rawContactId的记录的contact_id字段的值
	 */
	public long queryForContactId(ContentResolver cr, long rawContactId) {
		Cursor contactIdCursor = null;
		long contactId = -1;
		try {
			contactIdCursor = cr.query(RawContacts.CONTENT_URI,
					new String[] { RawContacts.CONTACT_ID }, RawContacts._ID
							+ "=" + rawContactId, null, null);
			if (contactIdCursor != null && contactIdCursor.moveToFirst()) {
				contactId = contactIdCursor.getLong(0);
			}
		} finally {
			if (contactIdCursor != null) {
				contactIdCursor.close();
			}
		}
		return contactId;
	}

	/**
	 * @return 根据参数中的contactId,查询在raw_contacts表中谁属于参数中的contactId
	 */
	private long queryForRawContactId(ContentResolver cr, long contactId) {
		Cursor rawContactIdCursor = null;
		long rawContactId = -1;
		try {
			rawContactIdCursor = cr.query(RawContacts.CONTENT_URI,
					new String[] { RawContacts._ID }, RawContacts.CONTACT_ID
							+ "=" + contactId, null, null);
			if (rawContactIdCursor != null && rawContactIdCursor.moveToFirst()) {
				// Just return the first one.
				rawContactId = rawContactIdCursor.getLong(0);
			}
		} finally {
			if (rawContactIdCursor != null) {
				rawContactIdCursor.close();
			}
		}
		return rawContactId;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.add(0, CONTEXT_MENU_REMOVE, 0, R.string.moveOutOfGroup);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info;
		try {
			info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		} catch (ClassCastException e) {
			MyLog.e("bad menuInfo : " + e.getMessage());
			return false;
		}
		switch (item.getItemId()) {
		case CONTEXT_MENU_REMOVE:
			MyLog.d("onContextItemSelected-->contactId : " + info.id);
			moveOutOfGroup(info.id);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private void moveOutOfGroup(long contactId) {
		long rawContactId = queryForRawContactId(getContentResolver(),
				contactId);
		MyLog.d("moveOutOfGroup-->rawContactId: " + rawContactId);
		Cursor aggregationCursor = getContentResolver().query(
				AggregationExceptions.CONTENT_URI,
				null,
				AggregationExceptions.RAW_CONTACT_ID1 + " = ? or "
						+ AggregationExceptions.RAW_CONTACT_ID2 + " = ? ",
				new String[] { String.valueOf(rawContactId),
						String.valueOf(rawContactId) }, null);
		int count = aggregationCursor.getCount();
		String where = null;
		String[] selectionArgs = null;
		if (count > 0) {// 该机录与其他记录有聚合(Aggregation)
			// 由于在queryForRawContactId()方法中我们是根据contactId查rawContactId,且只返回了第一个
			MyLog.w("存在聚合现象！");
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < count; i++) {
				aggregationCursor.moveToPosition(i);
				sb.append(aggregationCursor.getLong(aggregationCursor
						.getColumnIndex(AggregationExceptions.RAW_CONTACT_ID2)));
				sb.append(',');
			}
			sb.append(rawContactId);
			MyLog.w("所有的RawContactId: " + sb.toString());
			//
			where = GroupMembership.GROUP_ROW_ID + " = ? " + " AND "
					+ Data.MIMETYPE + " = ? " + " AND " + Data.RAW_CONTACT_ID
					+ " in ( " + sb.toString() + " ) ";
			selectionArgs = new String[] { String.valueOf(groupId),
					GroupMembership.CONTENT_ITEM_TYPE };
		} else {// 该机录没有聚合(Aggregation)
			MyLog.w("不存在聚合现象！");
			where = Data.RAW_CONTACT_ID + " = ? " + " AND " + Data.MIMETYPE
					+ " = ? " + " AND " + GroupMembership.GROUP_ROW_ID
					+ " = ? ";
			selectionArgs = new String[] { String.valueOf(rawContactId),
					GroupMembership.CONTENT_ITEM_TYPE, String.valueOf(groupId) };
		}
		getContentResolver().delete(Data.CONTENT_URI, where, selectionArgs);
		// 刷新
		setupListView();
	}
}