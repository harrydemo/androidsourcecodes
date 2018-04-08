 /*************************************************************************** 
 *              Copyright (C) 2009 Andrico Team                             * 
 *              http://code.google.com/p/andrico/                           *
 *                             												*
 * Licensed under the Apache License, Version 2.0 (the "License");			*
 * you may not use this file except in compliance with the License.			*
 * 																			*	
 * You may obtain a copy of the License at 									*
 * http://www.apache.org/licenses/LICENSE-2.0								*
 *																			*
 * Unless required by applicable law or agreed to in writing, software		*
 * distributed under the License is distributed on an "AS IS" BASIS,		*
 *																			*
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.	*
 * See the License for the specific language governing permissions and		*
 * limitations under the License.											*
 ****************************************************************************/

package org.andrico.andrico.content;


import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;


public class DBHelper extends SQLiteOpenHelper {


        private static final String TAG = "MobiLoggerDBHelper";
        private static final String CREATE_CONTACT_TABLE = 
			"CREATE TABLE IF NOT EXISTS CONTACTS ("+
			"id INTEGER PRIMARY KEY AUTOINCREMENT,"+
			"name TEXT," +
			"second_name TEXT,"+
			"date_of_birth TEXT,"+
			"adress TEXT, "+
			"page TEXT, "+
			"fb_id TEXT," +
			"small_pic Text," +
			"image BLOB" +
			");";
        
        
        boolean dbIsOpen;
        
        public DBHelper(Context context, String name, CursorFactory factory, int version) 
        {
                super(context, name, factory, version);
                Log.d(TAG,"DBHelper constructor called, called super()");
                // TODO Auto-generated constructor stub
        }


        @Override
        public void onCreate(SQLiteDatabase db) 
        {
                Log.d(TAG, "onCreate called!");
                try 
                {   
                        Log.d(TAG, "Create CONTACT table...");
                        
                        db.execSQL(CREATE_CONTACT_TABLE);
                        this.dbIsOpen = true;
                        this.dbIsOpen = false;
                } 
                catch (SQLException e) 
                {
                        Log.e(TAG, "Failed to create CONTACTS tables.");
                }
        }
        
        
        

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
        {
                // TODO Auto-generated method stub
                Log.d(TAG, "onUpgrade called!");
        }
}