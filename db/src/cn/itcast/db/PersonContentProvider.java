package cn.itcast.db;

import cn.itcast.service.DBOpenHelper;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class PersonContentProvider extends ContentProvider {
	private static final UriMatcher sMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	private static final int PERSONS = 1;
	private static final int PERSON = 2;
	private DBOpenHelper dbOpenHelper;
	
	static{
		sMatcher.addURI("cn.itcast.provides.personprovider", "person", PERSONS);
		sMatcher.addURI("cn.itcast.provides.personprovider", "person/#", PERSON);
	}
	//   content://cn.itcast.provides.personprovider/person
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		switch (sMatcher.match(uri)) {
		case PERSONS:
			long rowid = db.insert("person", "name", values);
			getContext().getContentResolver().notifyChange(uri, null);
			return ContentUris.withAppendedId(uri, rowid);
		default:
			throw new IllegalArgumentException("Unknown Uri:"+ uri);
		}
	}
	//  content://cn.itcast.provides.personprovider/person 删除表中的所有记录
	//  content://cn.itcast.provides.personprovider/person/10 删除表中指定id的记录
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		int num = 0;
		switch (sMatcher.match(uri)) {
		case PERSONS:
			num = db.delete("person", selection, selectionArgs);
			break;
		case PERSON:
			long personid = ContentUris.parseId(uri);
			String where = "personid="+ personid;
			if(selection!=null && !"".equals(selection)){
				where = where + " and "+ selection;
			}
			num = db.delete("person", where, selectionArgs);
			break;	
		default:
			throw new IllegalArgumentException("Unknown Uri:"+ uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return num;
	}

	@Override
	public String getType(Uri uri) {// gif image/gif   text/plain
		switch (sMatcher.match(uri)) {
		case PERSONS:
			return "vnd.android.cursor.dir/person";
		case PERSON:
			return "vnd.android.cursor.item/person";
		default:
			throw new IllegalArgumentException("Unknown Uri:"+ uri);
		}
	}


	@Override
	public boolean onCreate() {
		this.dbOpenHelper = new DBOpenHelper(this.getContext());
		return true;
	}

	//  content://cn.itcast.provides.personprovider/person 获取表中的所有记录
	//  content://cn.itcast.provides.personprovider/person/10 获取表中指定id的记录
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		switch (sMatcher.match(uri)) {
		case PERSONS:
			return db.query("person", projection, selection, selectionArgs, null, null, sortOrder);
			
		case PERSON:
			long personid = ContentUris.parseId(uri);
			String where = "personid="+ personid;
			if(selection!=null && !"".equals(selection)){
				where = where + " and "+ selection;
			}
			return db.query("person", projection, where, selectionArgs, null, null, sortOrder);
			
		default:
			throw new IllegalArgumentException("Unknown Uri:"+ uri);
		}
	}
	//  content://cn.itcast.provides.personprovider/person 更新表中的所有记录
	//  content://cn.itcast.provides.personprovider/person/10 更新表中指定id的记录
	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		int num = 0;
		switch (sMatcher.match(uri)) {
		case PERSONS:
			num = db.update("person", values, selection, selectionArgs);
			break;
		case PERSON:
			long personid = ContentUris.parseId(uri);
			String where = "personid="+ personid;
			if(selection!=null && !"".equals(selection)){
				where = where + " and "+ selection;
			}
			num = db.update("person", values, where, selectionArgs);
			break;	
		default:
			throw new IllegalArgumentException("Unknown Uri:"+ uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return num;
	}

}
