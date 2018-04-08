package irdc.ex06_13;

/* import相关class */
import java.io.IOException;
import java.util.Calendar;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

/* 实际运行更换桌面背景的Activity */
public class ChangeBgImage extends Activity
{
  /* 声明存放图片id的数组bg */
  private static final int[] bg =
    {R.drawable.b01,R.drawable.b02,R.drawable.b03,R.drawable.b04,
    R.drawable.b05,R.drawable.b06,R.drawable.b07};
  
  @Override
  protected void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    /* 加载progress.xml Layout */
    setContentView(R.layout.progress);
    /* 取得今天是星期几 */
    Calendar ca=Calendar.getInstance();
    int dayOfWeek=ca.get(Calendar.DAY_OF_WEEK)-1;
    
    /* 从数据库中取得今天应该换哪几张背景 */
    int DailyBg=0;
    String selection = "DailyId=?";   
    String[] selectionArgs = new String[]{""+dayOfWeek};
    DailyBgDB db=new DailyBgDB(ChangeBgImage.this);
    Cursor cur=db.select(selection,selectionArgs);
    while(cur.moveToNext())
    {
      DailyBg=cur.getInt(0);
    }
    cur.close();
    db.close();

    /* 如果DailyBg==99表示没设定，所以不运行 */
    if(DailyBg!=99)
    {
      Bitmap bmp=BitmapFactory.decodeResource(getResources(),bg[DailyBg]);
      try
      {
        super.setWallpaper(bmp);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    finish();
  }
}

