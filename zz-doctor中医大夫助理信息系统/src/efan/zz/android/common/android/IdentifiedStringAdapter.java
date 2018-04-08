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
package efan.zz.android.common.android;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.ArrayAdapter;
import efan.zz.android.ZZ;
import efan.zz.android.common.IdentifiedString;
import efan.zz.android.util.ZzUtil;

public class IdentifiedStringAdapter extends ArrayAdapter<IdentifiedString>
{
  private boolean isReady = false;
  
  public IdentifiedStringAdapter(Context context, int textViewResourceId)
  {
    super(context, textViewResourceId);
  }

  public boolean isReady()
  {
    return isReady;
  }

  public void setReady(boolean isReady)
  {
    this.isReady = isReady;
    
    if (isReady)
      ZzUtil.doNotifyAll(this);
  }

  public void preLoadNameOrKeyAutoCompAdapter(final String sql)
  {
    // Create thread to load AutoComplete data for selection...
    Thread loadThread = new Thread()
    {
      public void run()
      {
        loadNameOrKeyAutoCompAdapter(sql);
      }
    };
    loadThread.start();
  }
  
  public synchronized void loadNameOrKeyAutoCompAdapter(final String sql)
  {
    this.setReady(false);
    
    Cursor cursor = ZZ.db.rawQuery(sql, null);
    try
    {
      while (cursor.moveToNext())
      {
        int id = cursor.getInt(0);
        String name = cursor.getString(1);
        this.add(new IdentifiedString(name, id));
  
        String alias = cursor.getString(2);
        if (alias != null && alias.trim().length()>0)
          this.add(new IdentifiedString(alias.trim(), id));
      }
      
      cursor.moveToPosition(-1);
      while (cursor.moveToNext())
      {
        String keyCodes = cursor.getString(3);
        if (keyCodes == null)
          continue;
        
        int id = cursor.getInt(0);
        String name = cursor.getString(1);
        String keyCode[] = keyCodes.split(",");
        int n = keyCode.length;
        for (int i=0; i<n; i++)
        {
          if (keyCode[i].trim().length() > 0)
            this.add(new IdentifiedString(keyCode[i].trim()+"-"+name, id));
        }
      }
    }
    catch (Throwable t)
    {
      Log.e(this.getClass().getName(), t.getLocalizedMessage(), t);
    }
    finally
    {
      cursor.close();
      
      this.setReady(true);
    }
  }
  
  public int findPosition(final int id)
  {
    int n = this.getCount();
    for (int i=0; i<n; i++)
    {
      if (this.getItem(i).id == id)
        return i;
    }
    
    return -1;
  }

  public int findPosition(final String value)
  {
    int n = this.getCount();
    for (int i=0; i<n; i++)
    {
      if (this.getItem(i).value.equals(value))
        return i;
    }
    
    return -1;
  }
}
