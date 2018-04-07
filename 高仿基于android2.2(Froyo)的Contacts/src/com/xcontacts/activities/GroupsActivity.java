package com.xcontacts.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Groups;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.xcontacts.utils.MyLog;

/**
 * 
 * @author Lefter
 * 
 */
public class GroupsActivity extends Activity {
	private static final int OPTIONS_MENU_ADD_GROUP = Menu.FIRST;
	// 修改分组名称
	private static final int CONTEXT_MENU_EDIT_GROUP = Menu.FIRST + 1;
	// 删除分组
	private static final int CONTEXT_MENU_DELETE_GROUP = Menu.FIRST + 2;

	private ListView mListView;
	private SimpleCursorAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.list_groups);

		String[] projection = new String[] { Groups._ID, Groups.TITLE };
		// 查询未被删除的记录
		String selection = Groups.DELETED + "=?";
		String[] selectionArgs = new String[] { String.valueOf(0) };
		Cursor c = getContentResolver().query(Groups.CONTENT_URI, projection,
				selection, selectionArgs, null);
		MyLog.i("分组的数量： " + c.getCount());
		String[] from = new String[] { Groups.TITLE };
		int[] to = new int[] { android.R.id.text1 };
		mAdapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_1, c, from, to);
		mListView = (ListView) findViewById(R.id.groupsListView);
		mListView.setAdapter(mAdapter);
		// 注册ContextMenu
		registerForContextMenu(mListView);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Uri uri = ContentUris.withAppendedId(Groups.CONTENT_URI, id);
				MyLog.i("OnItemClick-->Uri : " + uri);
				Intent intent = new Intent();
				intent.setClass(GroupsActivity.this, ContactsInOneGroup.class);
				intent.setData(uri);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, OPTIONS_MENU_ADD_GROUP, 0, R.string.addGroup);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case OPTIONS_MENU_ADD_GROUP:
			showDialog(false, null);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.add(0, CONTEXT_MENU_EDIT_GROUP, 0, R.string.editGroup);
		menu.add(0, CONTEXT_MENU_DELETE_GROUP, 0, R.string.deleteGroup);
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
		int position = info.position;
		Uri uri = getGroupUri(position);
		switch (item.getItemId()) {
		case CONTEXT_MENU_EDIT_GROUP:
			showDialog(true, uri);
			return true;
		case CONTEXT_MENU_DELETE_GROUP:
			deleteGroup(uri);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private void showDialog(final boolean isEdit, final Uri uri) {
		// 弹出Dialog,供用户输入分组的名称，点击确定后保存分组
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("请输入分组名称：");
		final EditText etGroupName = new EditText(this);
		etGroupName.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		builder.setView(etGroupName);
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 保存分组，并刷新页面显示刚添加的分组
						String newGroupName = etGroupName.getText().toString();
						if (!TextUtils.isEmpty(newGroupName)) {
							if (isEdit) {// 修改分组名称
								updateGroupName(newGroupName, uri);
							} else {// 新建分组
								saveNewGroup(newGroupName);
							}
							dialog.dismiss();
						} else {// 分组名称为空
							Toast.makeText(GroupsActivity.this,
									R.string.toast_Content_is_null,
									Toast.LENGTH_SHORT).show();
						}
					}
				});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		// 显示Dialog
		builder.create().show();
	}

	/**
	 * 添加一个分组不区分是否重名
	 */
	private void saveNewGroup(String newGroupName) {
		ContentValues values = new ContentValues();
		values.put(Groups.TITLE, newGroupName);
		getContentResolver().insert(Groups.CONTENT_URI, values);
		// 刷新
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * 删除指定Uri的分组
	 * 
	 * @param uri
	 *            待删除的分组的Uri
	 */
	private void deleteGroup(Uri uri) {
		getContentResolver().delete(uri, null, null);
		// 刷新
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * 
	 * @param newGroupName
	 *            新的分组名称
	 * @param uri
	 *            被更新的记录的Uri
	 */
	private void updateGroupName(String newGroupName, Uri uri) {
		ContentValues values = new ContentValues();
		values.put(Groups.TITLE, newGroupName);
		getContentResolver().update(uri, values, null, null);
		// 刷新
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * 返回用户长按的分组的Uri。
	 * 
	 * @param position
	 *            用户长按的分组的position
	 */
	private Uri getGroupUri(int position) {
		if (position == ListView.INVALID_POSITION) {
			throw new IllegalArgumentException("Position not in list bounds");
		}
		final Cursor cursor = (Cursor) mAdapter.getItem(position);
		if (cursor == null) {
			return null;
		}
		final long groupId = cursor.getLong(cursor.getColumnIndex(Groups._ID));
		Uri uri = ContentUris.withAppendedId(Groups.CONTENT_URI, groupId);
		MyLog.i("用户长按的分组的Uri: " + uri);
		return uri;
	}
}