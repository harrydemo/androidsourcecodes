package com.appspot.hexagon.util;

import java.io.InputStream;
import java.util.Scanner;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.appspot.hexagon.R;
import com.appspot.hexagon.dbo.MapCell;
import com.appspot.hexagon.dbo.WorldMap;

public class HexagonDBHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "hexagon.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TAG = "HexagonDBHelper";
	private Context ctx = null;
	
	public HexagonDBHelper(Context ctx) {
		/**
		 * context, database_name, factory, database_version
		 */
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
		this.ctx = ctx;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//exeSQLFile(db, R.raw.hexagon_db_create);
		//exeSQLFile(db, R.raw.hexagon_db_insert);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//exeSQLFile(db, R.raw.hexagon_db_drop);
		//exeSQLFile(db, R.raw.hexagon_db_create);
	}
	
	private void exeSQLFile(SQLiteDatabase db, int sqlFileIndex) {
		Resources res = null;
		Scanner sc = null;

		// initialization
		res = ctx.getResources();
		InputStream is = res.openRawResource(sqlFileIndex);
		sc = new Scanner(is);
		
		while(sc.hasNextLine()){
			String sqlStmt = sc.nextLine();
			// ignore sql comments
			if(!sqlStmt.matches("---\\ .+\\ ---")){
				db.execSQL(sqlStmt);
				Log.i(TAG, sqlStmt);
			}
		}
	}

}
