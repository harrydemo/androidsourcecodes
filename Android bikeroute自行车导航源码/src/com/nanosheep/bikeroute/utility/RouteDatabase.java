/**
 * 
 */
package com.nanosheep.bikeroute.utility;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;
import com.nanosheep.bikeroute.utility.route.PGeoPoint;
import com.nanosheep.bikeroute.utility.route.Route;
import com.nanosheep.bikeroute.utility.route.Segment;

import java.util.ArrayList;
import java.util.List;

/**
 * SQLite database helper class for storing and retrieving
 * favourite routes.
 * 
 * This file is part of BikeRoute.
 * 
 * Copyright (C) 2011  Jonathan Gray
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 * @author jono@nanosheep.net
 * @version Jan 8, 2011
 */
public class RouteDatabase {
		private static final int DATABASE_VERSION = 6;
		private static final String ROUTE_TABLE_NAME = "route";
		public static final String NAME = "name_string";
		public static final String FRIENDLY_NAME =  "friendly_name_string";
		private static final String CRIGHT = "copyright_string";
		private static final String WARN = "warning_string";
		private static final String CC = "country_code_string";
		private static final String POLY = "polyline_string";
		private static final String ITIN = "itenerary_id";
		public static final String ROUTER = "router";
		
		
		private static final String POINTS_TABLE_NAME = "points";
		private static final String LAT = "latitude_e6_int";
		private static final String LNG = "longitute_e6_int";
		private static final String SEG_ID = "segid";
		
		private static final String SEGMENT_TABLE_NAME = "segments";
		private static final String INSTRUCTION = "turn_string";
		private static final String ROUTE_ID = "routeid";
		public static final String LENGTH = "length_int";
		private static final String DIST = "distance_dbl";
		
		private static final String ELEVATION_TABLE_NAME = "elevations";
		private static final String ELEV = "elevation_int";
		private static final String DIST_M = "distance_int";
		
		private static final String ROUTE_TABLE_CREATE =
               "CREATE TABLE " + ROUTE_TABLE_NAME + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY, " + NAME + " TEXT, " +
               CRIGHT + " TEXT, " + WARN + " TEXT, " + CC + " TEXT, " + POLY + " TEXT, " + ITIN + " INTEGER, "
               + LENGTH + " INTEGER, " + ROUTER + " TEXT, " + FRIENDLY_NAME +" TEXT);";
		
		private static final String POINTS_TABLE_CREATE =
			"CREATE TABLE " + POINTS_TABLE_NAME + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY, " + SEG_ID + " INTEGER , " + LAT + 
			" INTEGER, " + LNG + " INTEGER, " + ROUTE_ID + " INTEGER);";
		
		private static final String SEGMENT_TABLE_CREATE = 
			"CREATE TABLE " + SEGMENT_TABLE_NAME + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY, " + NAME + " TEXT, " + INSTRUCTION + " TEXT, " +
					ROUTE_ID + " INTEGER, " + DIST + " DOUBLE, " + LENGTH + " INTEGER);";
		
		private static final String ELEVATION_TABLE_CREATE = 
			"CREATE TABLE " + ELEVATION_TABLE_NAME + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY, " + ELEV + " INTEGER, " + DIST_M +
			" INTEGER, " + ROUTE_ID + " INTEGER);";
		
		
		private static final String DATABASE_NAME = "bikeroute_routes_db";

	   private SQLiteDatabase db;

	   private static List<SQLiteStatement> insertStmt;
	   
	   private static final String LIKE_QUERY = NAME + " LIKE ";
	   
	   private static RouteDatabaseHelper openHelper;

	   public RouteDatabase(Context context) {
	      openHelper = new RouteDatabaseHelper(context.getApplicationContext());
	      this.db = openHelper.getWritableDatabase();
	   }
	   
	   /**
	    * Add route to the database.
	    * @param route the route to store.
	    * @return
	    */

	   public void insert(final String name, final Route route) {
		   db.beginTransaction();
		   try {
			   //Insert the route and get the " + BaseColumns._ID + " back
			   RouteDatabase.insertStmt.get(0).bindString(1, route.getName()); 
			   RouteDatabase.insertStmt.get(0).bindString(2, route.getCopyright());
			   RouteDatabase.insertStmt.get(0).bindString(3, route.getWarning());
			   RouteDatabase.insertStmt.get(0).bindString(4, route.getCountry());
			   RouteDatabase.insertStmt.get(0).bindString(5, route.getPolyline()); 
			   RouteDatabase.insertStmt.get(0).bindLong(6, route.getItineraryId()); 
			   RouteDatabase.insertStmt.get(0).bindLong(7, route.getLength()); 
			   RouteDatabase.insertStmt.get(0).bindString(8, route.getRouter());
			   RouteDatabase.insertStmt.get(0).bindString(9, name);
			   final long routeId = RouteDatabase.insertStmt.get(0).executeInsert();
		   
			   //Insert elevation set
			   for(int i = 0; i < route.getElevationSeries().getItemCount(); i++) {
				   RouteDatabase.insertStmt.get(3).clearBindings();
				   //Elevation
				   RouteDatabase.insertStmt.get(3).bindDouble(1, route.getElevationSeries().getY(i));
				   //Distance
				   RouteDatabase.insertStmt.get(3).bindDouble(2, route.getElevationSeries().getX(i));
				   RouteDatabase.insertStmt.get(3).bindLong(3, routeId);
				   RouteDatabase.insertStmt.get(3).executeInsert();
			   }
		   
			   //Insert each segment with a ref to the route
			   for(Segment s : route.getSegments()) {
				   RouteDatabase.insertStmt.get(1).clearBindings();
				   RouteDatabase.insertStmt.get(1).bindString(1, s.getName());
				   RouteDatabase.insertStmt.get(1).bindString(2, s.getInstruction());
				   RouteDatabase.insertStmt.get(1).bindLong(3, routeId);
				   RouteDatabase.insertStmt.get(1).bindDouble(4, s.getDistance());
				   RouteDatabase.insertStmt.get(1).bindLong(5, s.getLength());
				   final long segId = RouteDatabase.insertStmt.get(1).executeInsert();
				   //And each point with a ref to the segment
				   for (PGeoPoint p : s.getPoints()) {
					   RouteDatabase.insertStmt.get(2).clearBindings();
					   RouteDatabase.insertStmt.get(2).bindLong(1, segId);
					   RouteDatabase.insertStmt.get(2).bindLong(2, p.getLatitudeE6());
					   RouteDatabase.insertStmt.get(2).bindLong(3, p.getLongitudeE6());
					   RouteDatabase.insertStmt.get(2).bindLong(4, routeId);
					   RouteDatabase.insertStmt.get(2).executeInsert();
				   }
		   		}
			   	db.setTransactionSuccessful();
		   	} finally {
		   		db.endTransaction();
		   	}
	   }

	   /**
	    * Delete all rows in the database
	    */
	   
	   public void deleteAll() {
	      this.db.delete(ROUTE_TABLE_NAME, null, null);
	      this.db.delete(SEGMENT_TABLE_NAME, null, null);
	      this.db.delete(POINTS_TABLE_NAME, null, null);
	      this.db.delete(ELEVATION_TABLE_NAME, null, null);
	   }
	   
	   /** Delete a given route and associated data.
	    * 
	    */
	   
	   public void delete(final long id) {
		   db.delete(ROUTE_TABLE_NAME, BaseColumns._ID + " = '" + id + "'", null);
		   db.delete(POINTS_TABLE_NAME, ROUTE_ID + " = " + id, null);
		   db.delete(SEGMENT_TABLE_NAME, ROUTE_ID + " = " + id, null);
		   db.delete(ELEVATION_TABLE_NAME, ROUTE_ID + " = " + id, null);
	   }
	   
	   public void open() {
		   if (!this.db.isOpen()) {
			   this.db = openHelper.getWritableDatabase();
		   }
	   }
	   
	   public void close() {
		   if (this.db.isOpen()) {
			   this.db.close();
		   }
	   }
	   
	   /**
	    * Extract a route previously stored in the database.
	    * 
	    * @param id The row " + BaseColumns._ID + " of the route to restore.
	    * @return a new route object.
	    */
	   public Route getRoute(final long id) {
		   Route r = new Route();
		   //Get the route
		   Cursor cursor = this.db.query(ROUTE_TABLE_NAME, new String[] { NAME, CRIGHT, WARN, CC, POLY, ITIN, LENGTH, ROUTER}, 
			        "" + BaseColumns._ID + " = '" + id + "'", null, null, null, NAME + " desc", "10");
		   if (cursor.moveToFirst()) {
			   r.setName(cursor.getString(0));
			   r.setCopyright(cursor.getString(1));
			   r.setWarning(cursor.getString(2));
			   r.setCountry(cursor.getString(3));
			   r.setPolyline(cursor.getString(4));
			   r.setItineraryId(cursor.getInt(5));
			   r.setLength(cursor.getInt(6));
			   r.setRouter(cursor.getString(7));
		   }
		   cursor.close();	
		   
		 //Get elevation map for this segment
		   cursor = this.db.query(ELEVATION_TABLE_NAME, new String[] { ELEV, DIST_M}, 
			        ROUTE_ID + " = '" + id + "'", null, null, null, "" + BaseColumns._ID + " ASC", null);
		  
		   if (cursor.moveToFirst()) {
			   do {
				   r.addElevation(cursor.getDouble(0), cursor.getDouble(1));
			   } while (cursor.moveToNext());
		   }
		   cursor.close();
		   
		   //Get segments for the route
		   cursor = this.db.query(SEGMENT_TABLE_NAME, new String[] { "" + BaseColumns._ID + "", NAME, INSTRUCTION, DIST, LENGTH}, 
			        ROUTE_ID + " = '" + id + "'", null, null, null, "" + BaseColumns._ID + " asc", null);
		   
		   if (cursor.moveToFirst()) {
			   Segment s = new Segment();
			   do {
				   s.clearPoints();
				   s.setName(cursor.getString(1));
				   s.setInstruction(cursor.getString(2));
				   s.setDistance(cursor.getDouble(3));
				   s.setLength(cursor.getInt(4));
				   //Get points for this segment
				   Cursor pointsCursor = this.db.query(POINTS_TABLE_NAME, new String[] { LAT, LNG}, 
					        SEG_ID + " = '" + cursor.getInt(0) + "'", null, null, null, "" + BaseColumns._ID + " ASC", null);
				  
				   if (pointsCursor.moveToFirst()) {
					   PGeoPoint p;
					   do {
						   //Add point to segment & route
						   p = new PGeoPoint(pointsCursor.getInt(0), pointsCursor.getInt(1));
						   s.addPoint(p);
						   r.addPoint(p);
					   } while (pointsCursor.moveToNext());
					   pointsCursor.close();
				   }
				   r.addSegment(s.copy());
			   } while (cursor.moveToNext());
			   cursor.close();
		   }
		   r.buildTree();
		   return r;
	   }
	   
	   /**
	    * Query the database for strings like the one given.
	    * @param ch String to match against
	    * @return a list of strings
	    */
	   
	   public List<String> selectLike(CharSequence ch) {
		   List<String> output = new ArrayList<String>();
		   String query = "%" + ch + "%";
		   StringBuilder sb = new StringBuilder(LIKE_QUERY);
		   DatabaseUtils.appendEscapedSQLString(sb, query);
		   Cursor cursor = this.db.query(ROUTE_TABLE_NAME, new String[] { NAME }, 
			        sb.toString(), null, null, null, NAME + " desc", "10");
		   if (cursor.moveToFirst()) {
			   do {
				   output.add(cursor.getString(0)); 
			   } while (cursor.moveToNext());
		   }
		   if (cursor != null && !cursor.isClosed()) {
			   cursor.close();
		   }
		   return output;
	   }
	   
	   /**
	    * Get all routes in the database.
	    * @return a list of all the routes in the db.
	    */

	   public List<String> selectAll() {
	      List<String> list = new ArrayList<String>();
	      Cursor cursor = this.db.query(ROUTE_TABLE_NAME, new String[] { NAME }, 
	        null, null, null, null, NAME + " desc");
	      if (cursor.moveToFirst()) {
	         do {
	            list.add(cursor.getString(0)); 
	         } while (cursor.moveToNext());
	      }
	      if (cursor != null && !cursor.isClosed()) {
	         cursor.close();
	      }
	      return list;
	   }
	   
	   public Cursor getRoutes() {
		   return this.db.query(ROUTE_TABLE_NAME, new String[] { BaseColumns._ID, FRIENDLY_NAME, LENGTH, ROUTER },
			        null, null, null, null, NAME + " desc");
	   }
	   
	   /**
	    * 
	    * @author jono@nanosheep.net
	    * @version Jul 2, 2010
	    */

	   private static class RouteDatabaseHelper extends SQLiteOpenHelper {
		   private static final String INSERT_ROUTE = "insert into " 
			      + ROUTE_TABLE_NAME + " values (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		   private static final String INSERT_SEGMENT = "insert into "
				   + SEGMENT_TABLE_NAME + " values (NULL, ?, ?, ?, ?, ?);";
		   private static final String INSERT_POINT = "insert into "
				   + POINTS_TABLE_NAME + " values (NULL, ?, ?, ?, ?);";
		   private static final String INSERT_ELEV = "insert into "
				   + ELEVATION_TABLE_NAME + " values(null, ?, ?, ?);";
		   
	       RouteDatabaseHelper(Context context) {
	           super(context, DATABASE_NAME, null, DATABASE_VERSION);
	       }

	       @Override
	       public void onCreate(SQLiteDatabase db) {
	           db.execSQL(ROUTE_TABLE_CREATE);
	           db.execSQL(POINTS_TABLE_CREATE);
	           db.execSQL(SEGMENT_TABLE_CREATE);
	           db.execSQL(ELEVATION_TABLE_CREATE);
	   		}
	       
	       @Override
	       public void onOpen(SQLiteDatabase db) {
	    	   insertStmt = new ArrayList<SQLiteStatement>(4);
			   insertStmt.add(db.compileStatement(INSERT_ROUTE));
			   insertStmt.add(db.compileStatement(INSERT_SEGMENT));
			   insertStmt.add(db.compileStatement(INSERT_POINT));
			   insertStmt.add(db.compileStatement(INSERT_ELEV));
	       }

	       /* (non-Javadoc)
	        * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	        */
	       @Override
	       public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    	   db.execSQL("DROP TABLE IF EXISTS " + ROUTE_TABLE_NAME);
	    	   db.execSQL("DROP TABLE IF EXISTS " + POINTS_TABLE_NAME);
	    	   db.execSQL("DROP TABLE IF EXISTS " + SEGMENT_TABLE_NAME);
	    	   db.execSQL("DROP TABLE IF EXISTS " + ELEVATION_TABLE_NAME);
	    	   onCreate(db);
	       }
	   }
}
