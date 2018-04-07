package efan.zz.android;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

//������ ȫ��
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
    	//�������ݿ�
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
	  //�˳����� �ر����ݿ�
    if (db != null)
      db.close();
  }
}
