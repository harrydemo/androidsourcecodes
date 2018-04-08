package com.xcontacts.activities;

import java.util.List;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.RawContactsEntity;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.xcontacts.activities.R;
import com.xcontacts.adapter.MyCursorAdapter;
import com.xcontacts.utils.MyLog;

/**
 * 显示一个联系人的详细信息
 * 
 * @author Lefter
 */
public class ViewContactActivity extends Activity implements OnClickListener {
	private Uri mLookupUri;
	// 显示联系人头像
	private QuickContactBadge mQuickContactBadge;
	// 显示联系人姓名
	private TextView mContactName;
	// 显示联系人是否被添加到了favorite
	private CheckBox mFavorite;
	// 显示联系人的电话、Email等信息
	private ListView mListView;

	private MyCursorAdapter mAdapter;

	private Cursor mCursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Intent intent = getIntent();
		Uri data = intent.getData();
		String authority = data.getAuthority();
		if (ContactsContract.AUTHORITY.equals(authority)) {
			mLookupUri = data;
		}
		// 取消标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.view_contact);
		// 初始化控件
		mQuickContactBadge = (QuickContactBadge) findViewById(R.id.viewContactQuickContactBadge);
		mContactName = (TextView) findViewById(R.id.tvViewContactContactName);
		mFavorite = (CheckBox) findViewById(R.id.checkBoxFavorite);
		mFavorite.setOnClickListener(this);

		mListView = (ListView) findViewById(R.id.viewContactListView);
		setupAdapter();
		// 设置QuickContactBadge和姓名
		setupQuickContactBadgeAndName();
		// 是否添加到了Favorite
		mCursor.moveToFirst();
		boolean isFavorite = mCursor.getInt(mCursor
				.getColumnIndex(Contacts.STARRED)) == 1;
		mFavorite.setChecked(isFavorite);
		mListView.setAdapter(mAdapter);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.checkBoxFavorite:
			// Toggle "starred" state
			// Make sure there is a contact
			if (mLookupUri != null) {
				final ContentValues values = new ContentValues(1);
				values.put(Contacts.STARRED, mFavorite.isChecked());
				getContentResolver().update(mLookupUri, values, null, null);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 初始化Adapter
	 */
	private void setupAdapter() {
		closeCursor();

		// Interprete mLookupUri
		mCursor = setupContactCursor(mLookupUri);

		// If mCursor is null now we did not succeed in using the Uri's Id (or
		// it didn't contain
		// a Uri). Instead we now have to use the lookup key to find the record

		if (mCursor == null) {// 使用上下文菜单时传递的Uri并不是lookupUri
			mLookupUri = Contacts
					.getLookupUri(getContentResolver(), mLookupUri);
			mCursor = setupContactCursor(mLookupUri);
		}

		// If mCursor is still null, we were unsuccessful in finding the record
		if (mCursor == null) {
			// TODO either figure out a way to prevent a flash of black
			// background or
			// use some other UI than a toast
			MyLog.e("invalid contact uri: " + mLookupUri);
			finish();
			return;
		}

		final long contactId = ContentUris.parseId(mLookupUri);
		/**
		 * 由于在ListView中显示了Photo和Name的信息，所以有空行。我们可以在查询的时候加入查询条件
		 */
		// 注意一个Contact对应多个RawContacts的情况
		// final Cursor entityCursor = getContentResolver().query(
		// RawContactsEntity.CONTENT_URI, null,
		// RawContacts.CONTACT_ID + "=?",
		// new String[] { String.valueOf(contactId) }, null);
		final Cursor entityCursor = getContentResolver().query(
				RawContactsEntity.CONTENT_URI,
				null,
				RawContacts.CONTACT_ID + " = ? " + " AND "
						+ RawContactsEntity.MIMETYPE + " != ? " + " AND "
						+ RawContactsEntity.MIMETYPE + " != ? ",
				new String[] { String.valueOf(contactId),
						Photo.CONTENT_ITEM_TYPE,
						StructuredName.CONTENT_ITEM_TYPE, }, null);
		startManagingCursor(entityCursor);
		mAdapter = new MyCursorAdapter(this, entityCursor, false);
		MyLog.d(contactId + " count: " + entityCursor.getCount());
	}

	private void closeCursor() {
		if (mCursor != null) {
			mCursor.close();
			mCursor = null;
		}
	}

	/**
	 * 初始化QuickContactBadge和姓名
	 */
	private void setupQuickContactBadgeAndName() {
		int photoIndex = mCursor.getColumnIndex(Contacts.PHOTO_ID);
		if (!mCursor.isNull(photoIndex)) { // 判断是否设置了头像
			MyLog.d("		设置用户自定义的头像");
			long photoId = mCursor.getLong(photoIndex);
			MyLog.d("		photo Id : " + photoId);

			String[] projection = new String[] { Photo.PHOTO };
			String selection = Data._ID + " = ? ";
			String[] selectionArgs = new String[] { String.valueOf(photoId) };
			// 查询头像
			Cursor photoCursor = getContentResolver().query(Data.CONTENT_URI,
					projection, selection, selectionArgs, null);
			if (photoCursor.moveToFirst()) {
				byte[] photo = photoCursor.getBlob(0);
				Bitmap bitmapPhoto = BitmapFactory.decodeByteArray(photo, 0,
						photo.length);
				mQuickContactBadge.setImageBitmap(bitmapPhoto);
			}
			photoCursor.close();
		} else {
			MyLog.d("		使用默认的头像");
			mQuickContactBadge
					.setImageResource(R.drawable.ic_contact_list_picture);
		}
		// 设置QuickContactBadge
		int idIndex = mCursor.getColumnIndex(Contacts._ID);
		long contactId = mCursor.getLong(idIndex);

		int lookupIndex = mCursor.getColumnIndex(Contacts.LOOKUP_KEY);
		String lookupKey = mCursor.getString(lookupIndex);
		mQuickContactBadge.assignContactUri(Contacts.getLookupUri(contactId,
				lookupKey));
		// 设置显示姓名
		int nameIndex = mCursor.getColumnIndex(Contacts.DISPLAY_NAME);
		if (!mCursor.isNull(nameIndex)) {
			String name = mCursor.getString(nameIndex);
			mContactName.setText(name);
		}
	}

	/**
	 * 根据参数中的Uri查询联系人在contacts表中的详细信息
	 */
	private Cursor setupContactCursor(Uri lookupUri) {
		if (lookupUri == null) {
			return null;
		}
		final List<String> segments = lookupUri.getPathSegments();
		if (segments.size() != 4) {
			return null;
		}

		// Contains an Id.
		final long uriContactId = Long.parseLong(segments.get(3));
		final String uriLookupKey = Uri.encode(segments.get(2));
		final Uri dataUri = Uri.withAppendedPath(
				ContentUris.withAppendedId(Contacts.CONTENT_URI, uriContactId),
				Contacts.Data.CONTENT_DIRECTORY);

		// This cursor has several purposes:
		// - Fetch RAW_CONTACT_ID
		// - Fetch the lookup-key to ensure we are looking at the right record
		Cursor cursor = getContentResolver()
				.query(dataUri,
						new String[] { Contacts.Data.RAW_CONTACT_ID,
								Contacts.LOOKUP_KEY, Contacts.STARRED,
								Contacts.PHOTO_ID, Contacts._ID,
								Contacts.DISPLAY_NAME }, null, null, null);
		// 对查询到的cursor进行验证,以确保我没们使用的Uri时正确的
		if (cursor.moveToFirst()) {
			String lookupKey = cursor.getString(cursor
					.getColumnIndex(Contacts.LOOKUP_KEY));
			if (!lookupKey.equals(uriLookupKey)) {
				// ID and lookup key do not match
				cursor.close();
				return null;
			}
			return cursor;
		} else {
			cursor.close();
			return null;
		}
	}
}