package ayh.mygps;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DBGps
{
	private static final String dbname = "gpsdata.db";
	private final Context ct;
	private SQLiteDatabase db;
	private SQLiteDB sdb;
	
	private static class SQLiteDB extends SQLiteOpenHelper
	{
		public SQLiteDB(Context context)
		{
			super(context, dbname, null, 1);
		}

		@Override
		public void onCreate(SQLiteDatabase sdb)
		{
			//建表
			sdb.execSQL("create table tab_gps (infotype integer,latitude integer,longitude integer,high double,direct double,speed double,gpstime date);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase sdb, int oldVersion, int newVersion)
		{
			sdb.execSQL("drop table if exists tab_gps");
			onCreate(sdb);
		}
	}

	//初始化数据库
	public DBGps(Context context)
	{
		ct = context;
		sdb = new SQLiteDB(ct);
	}
	
	//打开数据库
	public void openDB()
	{
		db = sdb.getWritableDatabase();
	}
	
	public void closeDB()
	{
		sdb.close();
	}
	
	public boolean addGpsData(gpsdata cdata)
	{
		boolean result = true;
		try
		{
			String StrSql = String.format("insert into tab_gps (infotype,latitude,longitude,high,direct,speed,gpstime) values (%d,%d,%d,%.1f,%.1f,%.1f,'%s')",
					cdata.InfoType,cdata.Latitude,cdata.Longitude,cdata.High,cdata.Direct,cdata.Speed,cdata.GpsTime);
			db.execSQL(StrSql);
			result = true;
		}catch(Exception e)
		{
			result = false;
			Toast.makeText(ct, "保存GPS数据失败:" + e.getMessage(), Toast.LENGTH_LONG).show();
		}
		return result;
	}
}
