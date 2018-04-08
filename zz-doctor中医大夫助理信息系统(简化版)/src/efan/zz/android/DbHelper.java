package efan.zz.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//数据库管理
public class DbHelper extends SQLiteOpenHelper
{
	private static final String VERSION = "1.00";
	private static final int DB_VERSION = (int) (Float.parseFloat(VERSION) * 100);
	private static final String DB_PATH = "ZZ.db";

	private static DbHelper dbHelper;

	private Context ctx;

	private DbHelper(Context ctx)
	{
		super(ctx, DB_PATH, null, DB_VERSION);

		this.ctx = ctx;
	}

	public static DbHelper getInstance(Context ctx)
	{
		if (dbHelper == null)
		{
			dbHelper = new DbHelper(ctx);
		}

		return dbHelper;
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		try
		{
			db.beginTransaction();

			// create db ZZ
			applySQLs(db, R.raw.db_create);

			// Initialise / load db ZZ
			applySQLs(db, R.raw.zz_data_subject);
			applySQLs(db, R.raw.zz_data_medicine);
			applySQLs(db, R.raw.zz_data_recipe);
			applySQLs(db, R.raw.zz_data_recipe_medicine_map);
			db.setTransactionSuccessful();
		}
		catch (Exception e)
		{
			Log.e(this.getClass().getName(), "", e);
			throw new RuntimeException("Database create error! Please contact the support or developer.", e);
		} 
		finally
		{
			db.endTransaction();
		}
	}

	//执行命令
	private void applySQLs(SQLiteDatabase db, int sqlResourceId) throws IOException
	{
		InputStream tmpIS = ctx.getResources().openRawResource(sqlResourceId);
		InputStreamReader tmpReader = new InputStreamReader(tmpIS);
		BufferedReader tmpBuf = new BufferedReader(tmpReader);

		StringBuffer sql = new StringBuffer();
		String tmpStr = null;
		while ((tmpStr = tmpBuf.readLine()) != null)
		{
			sql.append(tmpStr);
			sql.append('\n');
			
			if (tmpStr.trim().endsWith(";"))
			{
				db.execSQL(sql.toString());
				sql = new StringBuffer();
			}
		}

		tmpBuf.close();
		tmpReader.close();
		tmpIS.close();
	}

	//升级
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		try
		{
			/***
			 * NO upgrade before 1.0 *** // upgrade db ZZ applySQLs(db,
			 * R.raw.db_upgrade);
			 */

			db.beginTransaction();
			applySQLs(db, R.raw.db_clean);
			onCreate(db);
			db.setTransactionSuccessful();
		} catch (Exception e)
		{
			Log.e(this.getClass().getName(), "", e);
			throw new RuntimeException("Database upgrade error! Please contact the support or developer.", e);
		} finally
		{
			db.endTransaction();
		}
	}
}
