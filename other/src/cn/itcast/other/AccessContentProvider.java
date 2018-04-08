package cn.itcast.other;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

public class AccessContentProvider extends AndroidTestCase {
	private static final String TAG = "AccessContentProvider";

	public void testSave() throws Throwable{
		ContentResolver contentResolver = this.getContext().getContentResolver();
		Uri insertUri = Uri.parse("content://cn.itcast.provides.personprovider/person");
		ContentValues values = new ContentValues();
		values.put("name", "laozhang");
		values.put("phone", "1350000009");
		values.put("amount", "30000000000");
		Uri uri = contentResolver.insert(insertUri, values);
		Log.i(TAG, uri.toString());
	}
	
	public void testUpdate() throws Throwable{
		ContentResolver contentResolver = this.getContext().getContentResolver();
		Uri updateUri = Uri.parse("content://cn.itcast.provides.personprovider/person/5");
		ContentValues values = new ContentValues();
		values.put("amount", "300");
		contentResolver.update(updateUri, values, null, null);
	}
	
	public void testFind() throws Throwable{
		ContentResolver contentResolver = this.getContext().getContentResolver();
		Uri uri = Uri.parse("content://cn.itcast.provides.personprovider/person");
		Cursor cursor = contentResolver.query(uri, null, null, null, "personid asc");
		while(cursor.moveToNext()){
			int personid = cursor.getInt(cursor.getColumnIndex("personid"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String phone = cursor.getString(cursor.getColumnIndex("phone"));
			int amount = cursor.getInt(cursor.getColumnIndex("amount"));
			Log.i(TAG, "personid="+ personid + ",name="+ name+ ",phone="+ phone+ ",amount="+ amount);
		}
		cursor.close();
	}
	
	public void testDelete() throws Throwable{
		ContentResolver contentResolver = this.getContext().getContentResolver();
		Uri uri = Uri.parse("content://cn.itcast.provides.personprovider/person/5");
		contentResolver.delete(uri, null, null);
	}
}
