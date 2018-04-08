package linpeng.ztb;

import java.sql.Blob;
import java.sql.Date;
import java.sql.Time;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {

	private String DATABASENAME;

	public DataBaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		DATABASENAME = name;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (DATABASENAME == "title1") {
			db.execSQL("CREATE TABLE IF NOT EXISTS title1(newsclass integer,newstitle varchar,newstime varchar,"
					+ "isread varchar,url varchar)");
		} else if (DATABASENAME == "title2") {
			db.execSQL("CREATE TABLE IF NOT EXISTS title2(newsclass integer,newstitle varchar,newstime varchar,"
					+ "isread varchar,url varchar)");
		} else if (DATABASENAME == "title3") {
			db.execSQL("CREATE TABLE IF NOT EXISTS title3(newsclass integer,newstitle varchar,newstime varchar,"
					+ "isread varchar,url varchar)");
		} else if (DATABASENAME == "title4") {
			db.execSQL("CREATE TABLE IF NOT EXISTS title4(newsclass integer,newstitle varchar,newstime varchar,"
					+ "isread varchar,url varchar)");
		} else if (DATABASENAME == "title5") {
			db.execSQL("CREATE TABLE IF NOT EXISTS title5(newsclass integer,newstitle varchar,newstime varchar,"
					+ "isread varchar,url varchar)");
		} else if (DATABASENAME == "title6") {
			db.execSQL("CREATE TABLE IF NOT EXISTS title6(newsclass integer,newstitle varchar,newstime varchar,"
					+ "isread varchar,url varchar)");
		} else if (DATABASENAME == "title7") {
			db.execSQL("CREATE TABLE IF NOT EXISTS title7(newsclass integer,newstitle varchar,newstime varchar,"
					+ "isread varchar,url varchar)");
		} else if (DATABASENAME == "newsdetails1") {
			db.execSQL("CREATE TABLE IF NOT EXISTS newsdetails1(newsurl varchar,newstitle nvarchar,newstime varchar,newsdetails nvarchar,newsdownloadtext1 nvarchar,newsdownloadurl1 varchar,newsdownloadtext2 nvarchar,newsdownloadurl2 varchar)");
		} else if (DATABASENAME == "newsdetails2") {
			db.execSQL("CREATE TABLE IF NOT EXISTS newsdetails2(newsurl varchar,newstitle nvarchar,newstime varchar,newsdetails nvarchar,newsdownloadtext1 nvarchar,newsdownloadurl1 varchar,newsdownloadtext2 nvarchar,newsdownloadurl2 varchar)");
		} else if (DATABASENAME == "newsdetails3") {
			db.execSQL("CREATE TABLE IF NOT EXISTS newsdetails3(newsurl varchar,newstitle nvarchar,newstime varchar,newsdetails nvarchar,newsdownloadtext1 nvarchar,newsdownloadurl1 varchar,newsdownloadtext2 nvarchar,newsdownloadurl2 varchar)");
		} else if (DATABASENAME == "newsdetails4") {
			db.execSQL("CREATE TABLE IF NOT EXISTS newsdetails4(newsurl varchar,newstitle nvarchar,newstime varchar,newsdetails nvarchar,newsdownloadtext1 nvarchar,newsdownloadurl1 varchar,newsdownloadtext2 nvarchar,newsdownloadurl2 varchar)");
		} else if (DATABASENAME == "newsdetails5") {
			db.execSQL("CREATE TABLE IF NOT EXISTS newsdetails5(newsurl varchar,newstitle nvarchar,newstime varchar,newsdetails nvarchar,newsdownloadtext1 nvarchar,newsdownloadurl1 varchar,newsdownloadtext2 nvarchar,newsdownloadurl2 varchar)");
		} else if (DATABASENAME == "newsdetails6") {
			db.execSQL("CREATE TABLE IF NOT EXISTS newsdetails6(newsurl varchar,newstitle nvarchar,newstime varchar,newsdetails nvarchar,newsdownloadtext1 nvarchar,newsdownloadurl1 varchar,newsdownloadtext2 nvarchar,newsdownloadurl2 varchar)");
		} else if (DATABASENAME == "newsdetails7") {
			db.execSQL("CREATE TABLE IF NOT EXISTS newsdetails7(newsurl varchar,newstitle nvarchar,newstime varchar,newsdetails nvarchar,newsdownloadtext1 nvarchar,newsdownloadurl1 varchar,newsdownloadtext2 nvarchar,newsdownloadurl2 varchar)");
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public void addnewslist(int newsclass, String newstitle, String newstime,
			String isread, String url) {
		int i = 0;
		ContentValues value = new ContentValues();
		SQLiteDatabase sq = this.getReadableDatabase();
		Cursor cursor = sq.query(DATABASENAME, new String[] { "newsclass",
				"newstitle", "newstime", "isread", "url" }, "newsclass=?",
				new String[] { newsclass + "" }, null, null, null);
		cursor.moveToNext();
		sq.close();
		if (cursor.getCount() == 0) {
			i = 1;
			value.put("newsclass", newsclass);
			value.put("newstitle", newstitle);
			value.put("newstime", newstime);
			value.put("isread", isread);
			value.put("url", url);
			this.getWritableDatabase().insert(DATABASENAME, null, value);
			this.getWritableDatabase().close();
		} else {
			Log.i("data", "ÒÑ´æÔÚ");
		}
	}

	public void addetails(String newsurl, String newstitle, String newstime,
			String newsdetails, String newsdownloadtext1,
			String newsdownloadtext2, String newsdownloadurl1,
			String newsdownloadurl2) {
		ContentValues value = new ContentValues();
		SQLiteDatabase sq = this.getWritableDatabase();
		Cursor cursor = sq.query(DATABASENAME, new String[] { "newsurl",
				"newstitle", "newstime", "newsdetails", "newsdownloadtext1",
				"newsdownloadurl1", "newsdownloadtext2", "newsdownloadurl2" },
				"newsurl=?", new String[] { newsurl }, null, null, null);
		cursor.moveToNext();
		if (cursor.getCount() == 0) {
			value.put("newsurl", newsurl);
			value.put("newsdetails", newsdetails);
			value.put("newstitle", newstitle);
			value.put("newstime", newstime);
			value.put("newsdownloadtext1", newsdownloadtext1);
			value.put("newsdownloadtext2", newsdownloadtext2);
			value.put("newsdownloadurl1", newsdownloadurl1);
			value.put("newsdownloadurl2", newsdownloadurl2);
			this.getWritableDatabase().insert(DATABASENAME, null, value);
		}
	}

	public void dellAll() {
		this.getWritableDatabase().delete(DATABASENAME, null, null);
		this.getWritableDatabase().close();
	}
}
