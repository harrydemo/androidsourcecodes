package com.appspot.hexagon.tests;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import com.appspot.hexagon.R;
import android.app.Activity;
import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.appspot.hexagon.dbo.MapCell;
import com.appspot.hexagon.dbo.WorldMap;
import com.appspot.hexagon.util.CSVUtil;
import com.appspot.hexagon.util.HexagonDBHelper;

public class SQLiteTestActivity extends Activity {
	
	private HexagonDBHelper dbHelper = null;
	private static String TAG = "SQLiteTest";

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		// Set fullscreen
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.main2);
		
		dbHelper = new HexagonDBHelper(this);
		try {
			// populate the world with data on text file			
            
			populateWorld(R.raw.map_vector);
		} catch (Exception ex) {
			// ignore exception(s)
			Log.e(TAG, "Failed to read data", ex);
		} finally {
			dbHelper.close();
		}
	}

	private void populateWorld(int fileIndex) {
		SQLiteDatabase db = null;
		ContentValues worldMapValues = null;
		ContentValues mapCellValues = null;

		// initialization
		db = dbHelper.getWritableDatabase();
		Resources res = getResources();
		InputStream is = res.openRawResource(fileIndex);

		// create world
		worldMapValues = new ContentValues();
		worldMapValues.put(WorldMap.Constants.DESC, "Level 1 Map");
		worldMapValues.put(WorldMap.Constants.NAME, "");
		worldMapValues.put(WorldMap.Constants.WIDTH, 512);
		worldMapValues.put(WorldMap.Constants.HEIGHT, 512);
		// insert new row
		db.insertOrThrow(WorldMap.Constants.TABLE, null, worldMapValues);

		int parentMapId = -1;
		ArrayList<String[]> cellRawData = CSVUtil.readFromFile(is);
		Iterator<String[]> cellDataIter = cellRawData.iterator();

		// mapping parameter(s) with value(s)
		for (int row = 0; cellDataIter.hasNext(); row++) {
			String[] cellColumns = cellDataIter.next();
			for (int col = 0; col < cellColumns.length; col++) {
				StringBuffer name = new StringBuffer();
				name.append(row).append('|').append(col);
				MapCell cellData = new MapCell(name.toString(), null, Integer.parseInt(cellColumns[col]), parentMapId, row,
						col);
				mapCellValues = new ContentValues();
				mapCellValues.put(MapCell.Constants.DESC, cellData.getDesc());
				mapCellValues.put(MapCell.Constants.NAME, cellData.getName());
				mapCellValues.put(MapCell.Constants.PARENT_MAP, new Integer(cellData.getParentMapId()));
				mapCellValues.put(MapCell.Constants.MAP_ROW, cellData.getMapRow());
				mapCellValues.put(MapCell.Constants.MAP_COLUMN, cellData.getMapColumn());
				mapCellValues.put(MapCell.Constants.IMAGE, new Integer(cellData.getImage()));
				// insert new row
				Log.v(TAG, "inserting values["+mapCellValues+"]");
				db.insertOrThrow(MapCell.Constants.TABLE, null, mapCellValues);
			}
		}
	}

	private Cursor findMapCellsOnScreen(int startX, int startY, int width,
			int height) {
		Cursor cursor = null;
		SQLiteDatabase db = null;
		StringBuilder sb = null;

		// initialization
		db = dbHelper.getReadableDatabase();
		sb = new StringBuilder();

		cursor = db.query(WorldMap.Constants.TABLE, new String[] {
				WorldMap.Constants.NAME, WorldMap.Constants.WIDTH,
				WorldMap.Constants.HEIGHT }, sb
				.append(WorldMap.Constants.WIDTH).append(" BETWEEN (").append(
						startX).append(" AND ").append(startX + width).append(
						") ").append(WorldMap.Constants.HEIGHT).append(
						" BETWEEN (").append(startY).append(" AND ").append(
						startY + height).append(") ").toString(), null, // selection
																		// arguments
				null, // group by
				null, // having
				null); // order by
		startManagingCursor(cursor);

		return cursor;
	}
	
	@Override
    protected void onPause() {
        super.onPause();
    }
	
	@Override
    protected void onSaveInstanceState(Bundle state) {
        // just have the View's thread save its state into our Bundle
        super.onSaveInstanceState(state);
        Log.w(this.getClass().getName(), "saving game ...");
    }
}
