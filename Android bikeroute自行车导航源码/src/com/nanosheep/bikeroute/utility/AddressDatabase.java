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

import java.util.ArrayList;
import java.util.List;

/**
 * SQLite database helper class for storing and retrieving
 * addresses entered into the navigation boxes.
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
 * @version Jul 2, 2010
 */
public class AddressDatabase {
		private static final int DATABASE_VERSION = 6;
		private static final String ADDRESS_TABLE_NAME = "address";
		private static final String ADDRESS = "address_string";
		private static final String ADDRESS_TABLE_CREATE =
               "CREATE TABLE " + ADDRESS_TABLE_NAME + " (" + ADDRESS + " TEXT PRIMARY KEY);";
		private static final String DATABASE_NAME = "bikeroute_db";

	   private SQLiteDatabase db;

	   private static SQLiteStatement insertStmt;
	   private static final String INSERT = "insert or ignore into " 
	      + ADDRESS_TABLE_NAME + "(" + ADDRESS + ") values (?);";
	   private static final String LIKE_QUERY = ADDRESS + " LIKE ";
	   private AddressDatabaseHelper openHelper;

	   public AddressDatabase(Context context) {
	      openHelper = new AddressDatabaseHelper(context.getApplicationContext());
	      this.db = openHelper.getWritableDatabase();
	   }
	   
	   /**
	    * Add an address string to the database.
	    * @param name the address string to insert.
	    * @return
	    */

	   public void insert(String name) {
		   this.insertStmt.bindString(1, name);
		   this.insertStmt.executeInsert();
	   }

	   /**
	    * Delete all rows in the database
	    */
	   
	   public void deleteAll() {
		   this.db.delete(ADDRESS_TABLE_NAME, null, null);
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
	    * Query the database for strings like the one given.
	    * @param ch String to match against
	    * @return a list of strings
	    */
	   
	   public List<String> selectLike(CharSequence ch) {
		   List<String> output = new ArrayList<String>();
		   String query = "%" + ch + "%";
		   StringBuilder sb = new StringBuilder(LIKE_QUERY);
		   DatabaseUtils.appendEscapedSQLString(sb, query);
		   Cursor cursor = this.db.query(ADDRESS_TABLE_NAME, new String[] { ADDRESS }, 
			        sb.toString(), null, null, null, ADDRESS + " desc", "10");
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
	    * Get all addresses in the database.
	    * @return a list of all the addresses in the db.
	    */

	   public List<String> selectAll() {
	      List<String> list = new ArrayList<String>();
	      Cursor cursor = this.db.query(ADDRESS_TABLE_NAME, new String[] { ADDRESS }, 
	        null, null, null, null, ADDRESS + " desc");
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
	   
	   /**
	    * 
	    * @author jono@nanosheep.net
	    * @version Jul 2, 2010
	    */

	   public static class AddressDatabaseHelper extends SQLiteOpenHelper {

	       AddressDatabaseHelper(Context context) {
	           super(context, DATABASE_NAME, null, DATABASE_VERSION);
	       }

	       @Override
	       public void onCreate(SQLiteDatabase db) {
	           db.execSQL(ADDRESS_TABLE_CREATE);
	   		}
	       
	       @Override
	       public void onOpen(SQLiteDatabase db) {
	    	   insertStmt = db.compileStatement(INSERT);
	       }

	       /* (non-Javadoc)
	        * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	        */
	       @Override
	       public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	       }
	   }
}
