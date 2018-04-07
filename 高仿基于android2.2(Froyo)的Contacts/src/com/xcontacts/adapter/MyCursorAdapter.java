package com.xcontacts.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.CommonDataKinds.Nickname;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.CommonDataKinds.Website;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.Groups;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.RawContactsEntity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.xcontacts.activities.R;
import com.xcontacts.utils.MyLog;

public class MyCursorAdapter extends CursorAdapter {
	private LayoutInflater mListContainer;
	// 自定义视图
	private ContactItem mContactItem = null;

	// 用于判断显示所有联系人还是显示一个联系人
	private boolean isListContacts = false;

	public MyCursorAdapter(Context context, Cursor c, boolean isListContacts) {
		super(context, c);
		this.isListContacts = isListContacts;
		mListContainer = LayoutInflater.from(context);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		MyLog.d("MyCursorAdapter==>newView()");
		View convertView = null;
		mContactItem = new ContactItem();
		if (isListContacts) {// 显示所有联系人
			// 将显示所有联系人时listview中每一行的布局文件转换为View对象
			convertView = mListContainer.inflate(R.layout.list_contacts_item,
					null);
			// 获取控件对象
			mContactItem.mLinearLayout = (LinearLayout) convertView
					.findViewById(R.id.contactItemLinearLayout);
			mContactItem.mQuickContactBadge = (QuickContactBadge) convertView
					.findViewById(R.id.quickContactBadge);
			mContactItem.mTextView1 = (TextView) convertView
					.findViewById(R.id.tvContactName);
		} else {// 查看单个联系人的详细信息
			convertView = mListContainer.inflate(R.layout.view_contact_item,
					null);
			// 获取控件对象
			mContactItem.mLinearLayout = (LinearLayout) convertView
					.findViewById(R.id.viewContactItemLinearLayout);
			mContactItem.mTextView1 = (TextView) convertView
					.findViewById(R.id.tvViewContact1);
			mContactItem.mTextView2 = (TextView) convertView
					.findViewById(R.id.tvViewContact2);
		}
		// 设置控件集到convertView
		convertView.setTag(mContactItem);
		return convertView;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		if (view != null) {
			mContactItem = (ContactItem) view.getTag();
			if (isListContacts) {// 显示联系人列表
				MyLog.d("MyCursorAdapter==>bindView()" + " contact_id : "
						+ cursor.getLong(0));
				int photoIndex = cursor.getColumnIndex(Contacts.PHOTO_ID);
				if (!cursor.isNull(photoIndex)) { // 判断是否设置了头像
					MyLog.d("		设置用户自定义的头像");
					long photoId = cursor.getLong(photoIndex);
					MyLog.d("		photo Id : " + photoId);

					String[] projection = new String[] { Photo.PHOTO };
					String selection = Data._ID + " = ? ";
					String[] selectionArgs = new String[] { String
							.valueOf(photoId) };

					Cursor photoCursor = context.getContentResolver().query(
							Data.CONTENT_URI, projection, selection,
							selectionArgs, null);
					if (photoCursor.moveToFirst()) {
						byte[] photo = photoCursor.getBlob(0);
						Bitmap bitmapPhoto = BitmapFactory.decodeByteArray(
								photo, 0, photo.length);
						mContactItem.mQuickContactBadge
								.setImageBitmap(bitmapPhoto);
					}
					photoCursor.close();
				} else {
					MyLog.d("		使用默认的头像");
					mContactItem.mQuickContactBadge
							.setImageResource(R.drawable.ic_contact_list_picture);
				}
				// 设置QuickContactBadge
				int idIndex = cursor.getColumnIndex(Contacts._ID);
				long contactId = cursor.getLong(idIndex);

				int lookupIndex = cursor.getColumnIndex(Contacts.LOOKUP_KEY);
				String lookupKey = cursor.getString(lookupIndex);
				mContactItem.mQuickContactBadge.assignContactUri(Contacts
						.getLookupUri(contactId, lookupKey));
				// 设置显示姓名
				int nameIndex = cursor.getColumnIndex(Contacts.DISPLAY_NAME);
				if (!cursor.isNull(nameIndex)) {
					String name = cursor.getString(nameIndex);
					mContactItem.mTextView1.setText(name);
				}
			} else { // 显示一个联系人的详细信
				MyLog.d("MyCursorAdapter==>bindView()"
						+ " raw_contact_id : "
						+ cursor.getLong(cursor.getColumnIndex(RawContacts._ID)));
				MyLog.d("MyCursorAdapter==>bindView()"
						+ " data_id : "
						+ cursor.getLong(cursor
								.getColumnIndex(RawContactsEntity.DATA_ID)));
				// 查询某一个联系人的详细信息
				final String mimeType = cursor.getString(cursor
						.getColumnIndex(Data.MIMETYPE));
				MyLog.d("bindView()-->mimetype:" + mimeType);
				if (mimeType != null) {// 数据类型不为空
					if (Phone.CONTENT_ITEM_TYPE.equals(mimeType)) {
						// 获得Phone的Type
						int type = cursor.getInt(cursor
								.getColumnIndex(Phone.TYPE));
						// 查询Phone的号码
						String strPhoneNumber = cursor.getString(cursor
								.getColumnIndex(Phone.NUMBER));
						// 根据不同的Type设置ListView中第一行的文字
						if (type != Phone.TYPE_ASSISTANT
								&& type != Phone.TYPE_CUSTOM) {
							mContactItem.mTextView1
									.setText(getStringFromPhoneType(type));
						} else {// 处理用户自定义Lable的情况
							String strPhoneType = cursor.getString(cursor
									.getColumnIndex(Phone.LABEL));
							mContactItem.mTextView1.setText(strPhoneType);
						}
						// 如果号码不为空则显示
						if (!TextUtils.isEmpty(strPhoneNumber)) {
							mContactItem.mTextView2.setText(strPhoneNumber);
						}
					} else if (Email.CONTENT_ITEM_TYPE.equals(mimeType)) {
						// 取得Email的Type
						int type = cursor.getInt(cursor
								.getColumnIndex(Email.TYPE));
						// 查询Email的地址.
						// 注意这里用的是Data.DATA1，而其他的查询则不是。其实Phone.NUMBER实质上也是Data.DATA1
						// 只不过Google针对不同的情况，如Phone、Email，给我们提供了Data表对应的Alias
						String strEmailAddre = cursor.getString(cursor
								.getColumnIndex(Data.DATA1));
						// 同样，根据不同的Type，对ListView中的第一行进行设置
						if (type != Email.TYPE_CUSTOM) {
							mContactItem.mTextView1
									.setText(getStringFromEmailType(type));
						} else {// 用户自定义的Type，存储在Email.LABEL
							String strEmailType = cursor.getString(cursor
									.getColumnIndex(Email.LABEL));
							mContactItem.mTextView1.setText(strEmailType);
						}
						if (!TextUtils.isEmpty(strEmailAddre)) {// 不为空，则显示
							mContactItem.mTextView2.setText(strEmailAddre);
						}
					} else if (Im.CONTENT_ITEM_TYPE.equals(mimeType)) {
						// 取得Im的Protocol，根据Protocol进行区分，相当于Phone、Email的Type
						// Im的Api允许定义不同的Type，但是Android自带的Contacts未提供设置入口。
						// Im默认使用了Type_Other
						int protocol = cursor.getInt(cursor
								.getColumnIndex(Im.PROTOCOL));
						// 取得Im的内容，如QQ号码
						String strImNumber = cursor.getString(cursor
								.getColumnIndex(Im.DATA));
						// Im.PROTOCOL_NETMEETING与Im.PROTOCOL_CUSTOM
						if (protocol != Im.PROTOCOL_CUSTOM
								&& protocol != Im.PROTOCOL_NETMEETING) {
							mContactItem.mTextView1
									.setText(getStringFromImType(protocol));
						} else {
							String strImProtocol = cursor.getString(cursor
									.getColumnIndex(Im.CUSTOM_PROTOCOL));
							mContactItem.mTextView1.setText(strImProtocol);
						}
						if (!TextUtils.isEmpty(strImNumber)) {
							mContactItem.mTextView2.setText(strImNumber);
						}
					} else if (StructuredPostal.CONTENT_ITEM_TYPE
							.equals(mimeType)) {
						// 取得StructuredPostal的Type，以便根据不同的Type设置不同的文字
						int type = cursor.getInt(cursor
								.getColumnIndex(StructuredPostal.TYPE));
						// 取得完整的地址
						String strStructuredPostal = cursor
								.getString(cursor
										.getColumnIndex(StructuredPostal.FORMATTED_ADDRESS));
						if (type != StructuredPostal.TYPE_CUSTOM) {
							mContactItem.mTextView1
									.setText(getStringFromPostalType(type));
						} else {
							String strPostalType = cursor.getString(cursor
									.getColumnIndex(StructuredPostal.LABEL));
							mContactItem.mTextView1.setText(strPostalType);
						}
						if (!TextUtils.isEmpty(strStructuredPostal)) {
							mContactItem.mTextView2
									.setText(strStructuredPostal);
						}
					} else if (Organization.CONTENT_ITEM_TYPE.equals(mimeType)) {
						// Organization在显示的时候没有区分Type。但是新建和编辑联系人时是可以设置Type的
						String strOrganizationCompany = cursor.getString(cursor
								.getColumnIndex(Organization.COMPANY));
						String strOrganizationTitle = cursor.getString(cursor
								.getColumnIndex(Organization.TITLE));
						mContactItem.mTextView1.setText(strOrganizationCompany);
						mContactItem.mTextView2.setText(strOrganizationTitle);
					} else if (Nickname.CONTENT_ITEM_TYPE.equals(mimeType)) {
						mContactItem.mTextView1.setText(R.string.nickName);
						String strNickName = cursor.getString(cursor
								.getColumnIndex(Nickname.NAME));
						mContactItem.mTextView2.setText(strNickName);
					} else if (Website.CONTENT_ITEM_TYPE.equals(mimeType)) {
						mContactItem.mTextView1.setText(R.string.webSite);
						// 从Api可以看出，Website是支持设置type的.但是Google没有在自带的Contacts没有提供入口
						// Google默认使用的type是TYPE_OTHER
						String strWebsite = cursor.getString(cursor
								.getColumnIndex(Website.URL));
						mContactItem.mTextView2.setText(strWebsite);
					} else if (Note.CONTENT_ITEM_TYPE.equals(mimeType)) {
						mContactItem.mTextView1.setText(R.string.notes);
						// 不支持Type,直接取Data表中data1字段的值
						String strNote = cursor.getString(cursor
								.getColumnIndex(Note.NOTE));
						mContactItem.mTextView2.setText(strNote);
					} else if (GroupMembership.CONTENT_ITEM_TYPE
							.equals(mimeType)) {
						// 分组相关的信息
						mContactItem.mTextView1.setText(R.string.belongtoGroup);
						long groupId = cursor.getLong(cursor
								.getColumnIndex(GroupMembership.GROUP_ROW_ID));
						// 构造Uri
						Uri groupUri = ContentUris.withAppendedId(
								Groups.CONTENT_URI, groupId);
						// 查询Group表
						Cursor groupCursor = context.getContentResolver()
								.query(groupUri, null, null, null, null);
						groupCursor.moveToFirst();
						// 取得分组名称
						String groupName = groupCursor.getString(groupCursor
								.getColumnIndex(Groups.TITLE));
						mContactItem.mTextView2.setText(groupName);
					}
				}
			}
		}
	}

	/**
	 * 根据数据的类型返回对应的字符串
	 */
	private int getStringFromPhoneType(int type) {
		switch (type) {
		case Phone.TYPE_CUSTOM:// 特殊情况
			break;
		case Phone.TYPE_ASSISTANT:// 特殊情况
			break;
		// 如果是上述两种情况，则用户自定义Lable(即data3字段)
		case Phone.TYPE_HOME:
			type = R.string.call_home;
			break;
		case Phone.TYPE_CALLBACK:
			type = R.string.call_callback;
			break;
		case Phone.TYPE_CAR:
			type = R.string.call_car;
			break;
		case Phone.TYPE_COMPANY_MAIN:
			type = R.string.call_company_main;
			break;
		case Phone.TYPE_FAX_HOME:
			type = R.string.call_fax_home;
			break;
		case Phone.TYPE_FAX_WORK:
			type = R.string.call_fax_work;
			break;
		case Phone.TYPE_ISDN:
			type = R.string.call_isdn;
			break;
		case Phone.TYPE_MAIN:
			type = R.string.call_main;
			break;
		case Phone.TYPE_MMS:
			type = R.string.call_mms;
			break;
		case Phone.TYPE_MOBILE:
			type = R.string.call_mobile;
			break;
		case Phone.TYPE_OTHER:
			type = R.string.call_other;
			break;
		case Phone.TYPE_OTHER_FAX:
			type = R.string.call_other_fax;
			break;
		case Phone.TYPE_PAGER:
			type = R.string.call_pager;
			break;
		case Phone.TYPE_RADIO:
			type = R.string.call_radio;
			break;
		case Phone.TYPE_TELEX:
			type = R.string.call_telex;
			break;
		case Phone.TYPE_TTY_TDD:
			type = R.string.call_tty_tdd;
			break;
		case Phone.TYPE_WORK:
			type = R.string.call_work;
			break;
		case Phone.TYPE_WORK_MOBILE:
			type = R.string.call_work_mobile;
			break;
		case Phone.TYPE_WORK_PAGER:
			type = R.string.call_work_pager;
			break;
		}
		return type;
	}

	/**
	 * 根据数据的类型返回对应的字符串
	 */
	private int getStringFromEmailType(int type) {
		switch (type) {
		case Email.TYPE_CUSTOM:// 特殊情况
			break;
		// 如果是上述情况，则用户自定义Lable(即data3字段)
		case Email.TYPE_HOME:
			type = R.string.email_home;
			break;
		case Email.TYPE_WORK:
			type = R.string.email_work;
			break;
		case Email.TYPE_MOBILE:
			type = R.string.email_mobile;
			break;
		case Email.TYPE_OTHER:
			type = R.string.email_other;
			break;
		}
		return type;
	}

	/**
	 * 根据数据的类型返回对应的字符串
	 */
	private int getStringFromImType(int protocol) {
		switch (protocol) {
		// Im.PROTOCOL_NETMEETING与Im.PROTOCOL_CUSTOM
		case Im.PROTOCOL_CUSTOM:// 特殊情况
			break;
		// 如果是上述情况，则用户自定义Custom_Protocol(即data6字段)
		case Im.PROTOCOL_AIM:
			protocol = R.string.chat_aim;
			break;
		case Im.PROTOCOL_GOOGLE_TALK:
			protocol = R.string.chat_gtalk;
			break;
		case Im.PROTOCOL_ICQ:
			protocol = R.string.chat_icq;
			break;
		case Im.PROTOCOL_JABBER:
			protocol = R.string.chat_jabber;
			break;
		case Im.PROTOCOL_MSN:
			protocol = R.string.chat_msn;
			break;
		case Im.PROTOCOL_QQ:
			protocol = R.string.chat_qq;
			break;
		case Im.PROTOCOL_SKYPE:
			protocol = R.string.chat_skype;
			break;
		case Im.PROTOCOL_YAHOO:
			protocol = R.string.chat_yahoo;
			break;
		}
		return protocol;
	}

	/**
	 * 根据数据的类型返回对应的字符串
	 */
	private int getStringFromPostalType(int type) {
		switch (type) {
		case StructuredPostal.TYPE_CUSTOM:// 特殊情况
			break;
		// 如果是上述情况，则用户自定义Lable(即data3字段)
		case StructuredPostal.TYPE_HOME:
			type = R.string.map_home;
			break;
		case StructuredPostal.TYPE_WORK:
			type = R.string.map_work;
			break;
		case StructuredPostal.TYPE_OTHER:
			type = R.string.map_other;
			break;
		}
		return type;
	}
}