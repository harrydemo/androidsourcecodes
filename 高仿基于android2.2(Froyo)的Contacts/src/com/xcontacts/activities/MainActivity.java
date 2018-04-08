package com.xcontacts.activities;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.Groups;
import android.provider.ContactsContract.RawContacts;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.xcontacts.activities.R;
import com.xcontacts.adapter.MyCursorAdapter;
import com.xcontacts.utils.MyLog;

/**
 * 显示所有联系人
 * 
 * @author Lefter
 */
public class MainActivity extends Activity implements OnItemClickListener {
	/** Called when the activity is first created. */
	private Cursor mCursor;
	private MyCursorAdapter mAdapter;
	private ListView mListView;

	private static final int CONTEXT_MENU_ITEM_VIEW_CONTACT = Menu.FIRST;
	private static final int CONTEXT_MENU_ITEM_EDIT = Menu.FIRST + 1;
	private static final int CONTEXT_MENU_ITEM_DELETE = Menu.FIRST + 2;
	private static final int CONTEXT_MENU_ITEM_TOGGLE_STAR = Menu.FIRST + 3;
	// 将选中的联系人移进某一个分组
	private static final int CONTEXT_MENU_ITEM_MOVE_INTO_GROUP = Menu.FIRST + 4;
	private static final int OPTIONS_MENU_ITEM_ADD = Menu.FIRST + 5;
	// 显示所有分组
	private static final int OPTIONS_MENU_ITEM_GROUPS = Menu.FIRST + 6;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 取消标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.list_contacts);

		Uri uri = Contacts.CONTENT_URI;
		String[] projection = new String[] { Contacts._ID, // 0
				Contacts.DISPLAY_NAME, // 1
				Contacts.STARRED, // 2
				Contacts.TIMES_CONTACTED, // 3
				Contacts.CONTACT_PRESENCE, // 4
				Contacts.PHOTO_ID, // 5
				Contacts.LOOKUP_KEY, // 6
				Contacts.HAS_PHONE_NUMBER, // 7
				Contacts.IN_VISIBLE_GROUP, // 8
		};

		String sortOrder = Contacts.DISPLAY_NAME + "  COLLATE LOCALIZED ASC ";
		mCursor = getContentResolver().query(uri, projection, null, null,
				sortOrder);

		mAdapter = new MyCursorAdapter(this, mCursor, true);

		mListView = (ListView) findViewById(R.id.listView);
		mListView.setAdapter(mAdapter);
		// 添加上下文菜单
		registerForContextMenu(mListView);
		mListView.setOnItemClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, OPTIONS_MENU_ITEM_ADD, 0, R.string.addContactDescription);
		menu.add(0, OPTIONS_MENU_ITEM_GROUPS, 0, R.string.listAllGroups);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case OPTIONS_MENU_ITEM_ADD:
			final Intent intent = new Intent(Intent.ACTION_INSERT,
					Contacts.CONTENT_URI);
			startActivity(intent);
			return true;
		case OPTIONS_MENU_ITEM_GROUPS:// 跳转到显示所有分组页面
			final Intent groupIntent = new Intent(this, GroupsActivity.class);
			startActivity(groupIntent);
		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterView.AdapterContextMenuInfo info;
		try {
			info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		} catch (ClassCastException e) {
			MyLog.e("bad menuInfo : " + e.getMessage());
			return;
		}

		Cursor cursor = (Cursor) mAdapter.getItem(info.position);
		if (cursor == null) {
			// For some reason the requested item isn't available, do nothing
			return;
		}
		long id = info.id;
		// 使用lookupUri更好
		Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, id);
		long rawContactId = this.queryForRawContactId(getContentResolver(), id);
		Uri rawContactUri = ContentUris.withAppendedId(RawContacts.CONTENT_URI,
				rawContactId);
		// Setup the menu header
		menu.setHeaderTitle(cursor.getString(cursor
				.getColumnIndex(Contacts.DISPLAY_NAME)));

		// View contact details
		menu.add(0, CONTEXT_MENU_ITEM_VIEW_CONTACT, 0,
				R.string.viewContactDesription).setIntent(
				new Intent(Intent.ACTION_VIEW, contactUri));
		// Star toggling
		int starState = cursor.getInt(cursor.getColumnIndex(Contacts.STARRED));
		if (starState == 0) {
			menu.add(0, CONTEXT_MENU_ITEM_TOGGLE_STAR, 0,
					R.string.addToFavorites);
		} else {
			menu.add(0, CONTEXT_MENU_ITEM_TOGGLE_STAR, 0,
					R.string.removeFromFavorites);
		}
		// Contact editing
		menu.add(0, CONTEXT_MENU_ITEM_EDIT, 0, R.string.editContactDescription)
				.setIntent(new Intent(Intent.ACTION_EDIT, rawContactUri));
		// Contact deleting
		menu.add(0, CONTEXT_MENU_ITEM_DELETE, 0,
				R.string.deleteContactDesription);
		// move one contact into a group
		menu.add(0, CONTEXT_MENU_ITEM_MOVE_INTO_GROUP, 0,
				R.string.moveIntoGroup);
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
		Cursor cursor = (Cursor) mAdapter.getItem(info.position);

		switch (item.getItemId()) {
		case CONTEXT_MENU_ITEM_TOGGLE_STAR:
			// Toggle the star
			ContentValues values = new ContentValues(1);
			values.put(Contacts.STARRED, cursor.getInt(cursor
					.getColumnIndex(Contacts.STARRED)) == 0 ? 1 : 0);
			final Uri selectedUri = this.getContactUri(info.position);
			getContentResolver().update(selectedUri, values, null, null);
			return true;
		case CONTEXT_MENU_ITEM_DELETE:
			deleteContact(getContactUri(info.position));
			return true;
		case CONTEXT_MENU_ITEM_MOVE_INTO_GROUP:
			MyLog.i("contactId： " + info.id);
			moveIntoGroup(info.id);
		default:
			return super.onContextItemSelected(item);
		}
	}

	/**
	 * 把id为contactId的联系人移进一个分组
	 * 
	 * @param contactId
	 */
	private void moveIntoGroup(final long contactId) {
		// 查询Groups中所有的分组
		final Cursor groupCursor = getContentResolver().query(
				Groups.CONTENT_URI, null, Groups.DELETED + " = 0", null, null);
		int countOfGroups = groupCursor.getCount();
		MyLog.i("count of groups :" + countOfGroups);
		// 存放Group的名称
		String[] groups = new String[countOfGroups];
		// HashMap,用于保存名称数组中各个名称对应的_id
		HashMap<Integer, Long> groupIds = new HashMap<Integer, Long>();
		// 循环，保存分组的名称和主键_id
		for (int i = 0; i < countOfGroups; i++) {
			groupCursor.moveToPosition(i);
			String groupTitle = groupCursor.getString(groupCursor
					.getColumnIndex(Groups.TITLE));
			MyLog.d("第 " + i + " 个分组的名称：" + groupTitle);
			groups[i] = groupTitle;
			long groupId = groupCursor.getLong(groupCursor
					.getColumnIndex(Groups._ID));
			MyLog.i("第 " + i + " 个分组的ID：" + groupId);
			groupIds.put(i, groupId);
		}
		final HashMap<Integer, Long> Ids = groupIds;
		// 弹Dialog，供用户选择要移进哪一个分组
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.listAllGroups);
		builder.setItems(groups, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				long groupId = Ids.get(which);
				MyLog.i("选择的group的Id :　" + groupId);
				ContentValues values = new ContentValues();
				values.put(Data.RAW_CONTACT_ID,
						queryForRawContactId(getContentResolver(), contactId));
				values.put(Data.MIMETYPE, GroupMembership.CONTENT_ITEM_TYPE);
				values.put(GroupMembership.GROUP_ROW_ID, groupId);
				getContentResolver().insert(Data.CONTENT_URI, values);
			}
		});
		builder.create().show();
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

	/**
	 * 根据参数中的uri对Contacts表进行删除
	 */
	private void deleteContact(Uri uri) {
		if (uri != null) {
			// 删除时应该弹出Dialog让用户确认操作
			getContentResolver().delete(uri, null, null);
			mAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * @return 根据参数得到这个position的联系人的lookupUri
	 */
	private Uri getContactUri(int position) {
		if (position == ListView.INVALID_POSITION) {
			throw new IllegalArgumentException("Position not in list bounds");
		}

		final Cursor cursor = (Cursor) mAdapter.getItem(position);
		if (cursor == null) {
			return null;
		}
		// Build and return soft, lookup reference
		final long contactId = cursor.getLong(cursor
				.getColumnIndex(Contacts._ID));
		final String lookupKey = cursor.getString(cursor
				.getColumnIndex(Contacts.LOOKUP_KEY));
		return Contacts.getLookupUri(contactId, lookupKey);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		MyLog.d("MainActivity==>onItemClick()");
		this.hideSoftKeyboard();
		Uri uri = getContactUri(position);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
		finish();
	}

	/**
	 * 隐藏键盘
	 */
	private void hideSoftKeyboard() {
		// Hide soft keyboard, if visible
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(mListView.getWindowToken(),
				0);
	}
}