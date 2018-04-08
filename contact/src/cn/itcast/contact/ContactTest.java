package cn.itcast.contact;

import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts.Data;
import android.test.AndroidTestCase;
import android.util.Log;

public class ContactTest extends AndroidTestCase {
	private static final String TAG = "ContactTest";
	
	//获取通信录中所有的联系人
	public void testGetContacts() throws Throwable{
		ContentResolver contentResolver = this.getContext().getContentResolver();
		Uri uri = Uri.parse("content://com.android.contacts/contacts");
		Cursor cursor = contentResolver.query(uri, null, null, null, null);
		while(cursor.moveToNext()){
			StringBuilder sb = new StringBuilder();
			String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));  
			String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)); 
			sb.append("contactId=").append(contactId).append(",name=").append(name);
			
			Cursor phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,  
			        null,  
			        ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,  
			        null, null);  
			while(phones.moveToNext()){
				String phone = phones.getString(phones.getColumnIndex("data1")); 
				sb.append(",phone=").append(phone);
			}
			Cursor emails = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,  
				       null,  
				       ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId,  
				       null, null); 
			while(emails.moveToNext()){
				String email = emails.getString(emails.getColumnIndex("data1")); 
				sb.append(",email=").append(email);
			}
			Log.i(TAG, sb.toString());
		}
	}
	
	/**
	 * 首先向RawContacts.CONTENT_URI执行一个空值插入，目的是获取系统返回的rawContactId 
	 * 这时后面插入data表的依据，只有执行空值插入，才能使插入的联系人在通讯录里面可见
	 */
	public void testInsert() {
		ContentValues values = new ContentValues();
		//首先向RawContacts.CONTENT_URI执行一个空值插入，目的是获取系统返回的rawContactId 
		Uri rawContactUri = this.getContext().getContentResolver().insert(RawContacts.CONTENT_URI, values);
		long rawContactId = ContentUris.parseId(rawContactUri);
		//往data表入姓名数据
		values.clear();
		values.put(Data.RAW_CONTACT_ID, rawContactId); 
		values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);//内容类型
		values.put(StructuredName.GIVEN_NAME, "李天山");
		this.getContext().getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
		//往data表入电话数据
		values.clear();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
		values.put(Phone.NUMBER, "13921009789");
		values.put(Phone.TYPE, Phone.TYPE_MOBILE);
		this.getContext().getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
		//往data表入Email数据
		values.clear();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
		values.put(Email.DATA, "liming@itcast.cn");
		values.put(Email.TYPE, Email.TYPE_WORK);
		this.getContext().getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
	}

	public void testSave() throws Throwable{
		//文档位置：reference\android\provider\ContactsContract.RawContacts.html
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		int rawContactInsertIndex = 0;
		ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
				.withValue(RawContacts.ACCOUNT_TYPE, null)
				.withValue(RawContacts.ACCOUNT_NAME, null)
				.build());
		//文档位置：reference\android\provider\ContactsContract.Data.html
		ops.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI)
				.withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex)
				.withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
				.withValue(StructuredName.GIVEN_NAME, "赵薇")
				.build());
		ops.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI)
				 .withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex)
		         .withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
		         .withValue(Phone.NUMBER, "13671323809")
		         .withValue(Phone.TYPE, Phone.TYPE_MOBILE)
		         .withValue(Phone.LABEL, "手机号")
		         .build());
		ops.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI)
				 .withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex)
		         .withValue(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
		         .withValue(Email.DATA, "liming@itcast.cn")
		         .withValue(Email.TYPE, Email.TYPE_WORK)
		         .build());
		ContentProviderResult[] results = this.getContext().getContentResolver()
			.applyBatch(ContactsContract.AUTHORITY, ops);
		for(ContentProviderResult result : results){
			Log.i(TAG, result.uri.toString());
		}
	}

}
