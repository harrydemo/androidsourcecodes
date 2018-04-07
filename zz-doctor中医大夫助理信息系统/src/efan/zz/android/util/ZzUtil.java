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
package efan.zz.android.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import efan.zz.android.ZZ;
import efan.zz.android.activity.MedicineDetail;
import efan.zz.android.activity.MedicineQuery;
import efan.zz.android.activity.WelcomeRxRecipeQuery;

public class ZzUtil
{
  public static void doWait(Object syncObj)
  {
    synchronized(syncObj)
    {
      try
      {
        syncObj.wait();
      } catch (InterruptedException e)
      {
        Log.d("", "", e);
      }
    }
  }
  
  public static void doNotifyAll(Object syncObj)
  {
    synchronized(syncObj)
    {
      syncObj.notifyAll();
    }
  }
  
  public static void goHome(Activity caller)
  {
    Intent nextAct = new Intent();
    nextAct.setAction(Intent.ACTION_MAIN);
    nextAct.setClass(caller, WelcomeRxRecipeQuery.class);
    caller.startActivity(nextAct);
  }
  
  public static void gotoMedicine(Activity caller)
  {
    Intent nextAct = new Intent();
    nextAct.setAction(Intent.ACTION_VIEW);
    nextAct.setClass(caller, MedicineQuery.class);
    caller.startActivity(nextAct);
  }
  
  // http://developer.android.com/guide/publishing/publishing.html#marketintent
  public static void searchMarket(Activity caller, String paramtype, String value)
  {
    Uri uri = Uri.parse("market://search?q=" + paramtype + ":" + value);
    Intent intent = new Intent();
    intent.setData(uri);
    intent.setAction(Intent.ACTION_VIEW);
    caller.startActivity(intent);
  }
  
  public static void youngGirlWarning()
  {
//    toastMessage("Hi, I am still growing! Try me later. Sorry lah...", false);    // for pre-1.0 beta version only
  }
  
  public static void toastMessage(String msg, boolean wannaShowLong)
  {
    int length = Toast.LENGTH_SHORT;
    if (wannaShowLong)
      length = Toast.LENGTH_LONG;
    
    Toast t = Toast.makeText(ZZ.ctx, msg, length);
    t.show();
  }

}
