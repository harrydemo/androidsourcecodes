/*
 * Copyright 2009 eFANsoftware
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package efan.zz.android;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

public class ZZ extends Application
{
  public static SQLiteDatabase db;
  public static DbHelper dbHelper;
  
  public static Context ctx;
  public static InputMethodManager inputMethodMgr;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate()
  {
    try
    {
      // Initialize roDB instance. Create the roDB at the first time
      ctx = this.getApplicationContext();
      dbHelper = DbHelper.getInstance(ctx);
      db = dbHelper.getWritableDatabase();
      
      inputMethodMgr = (InputMethodManager) ctx.getSystemService(INPUT_METHOD_SERVICE);
    }
    catch (Throwable e)
    {
      Log.e(this.getClass().getName(), "", e);
    }
  }
  
  @Override
  public void onLowMemory()
  {
    // TODO: performance tuning
    // close roDB & reopen
  }
  
  @Override
  public void onTerminate()
  { 
    if (db != null)
      db.close();
  }
}
