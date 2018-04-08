package irdc.ex06_23; 

import java.io.File; 
import java.util.ArrayList; 
import android.app.Activity; 
import android.content.ContentResolver;
import android.content.Context; 
import android.content.Intent;
import android.database.Cursor;
import android.gesture.Gesture; 
import android.gesture.GestureLibraries; 
import android.gesture.GestureLibrary; 
import android.gesture.GestureOverlayView; 
import android.gesture.Prediction; 
import android.gesture.GestureOverlayView.OnGesturePerformedListener; 
import android.net.Uri;
import android.os.Bundle; 
import android.os.Environment; 
import android.provider.ContactsContract;
import android.widget.Toast;

public class EX06_23 extends Activity 
{ 
  private GestureLibrary gesLib; 
  private GestureOverlayView gesOverlay; 
  private String gesPath; 
   
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState);
    /* 加载main.xml Layout */
    setContentView(R.layout.main); 
    
    /* 查看SDCard是否存在 */ 
    if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) 
    { 
      /* SD卡不存在，显示Toast信息 */
      Toast.makeText(EX06_23.this,"SD卡不存在!程序无法运行",
                     Toast.LENGTH_LONG).show(); 
      finish(); 
    }
    
    /* 取得GestureLibrary的文件路径 */
    gesPath = new File
    (
      Environment.getExternalStorageDirectory(),"gestures" 
    ).getAbsolutePath();
     
    File file = new File(gesPath); 
    if(!file.exists()) 
    { 
      /* gestures文件不存在，显示Toast信息 */
      Toast.makeText(EX06_23.this,"gestures文件不存在!程序无法运行",
                     Toast.LENGTH_LONG).show(); 
      finish();
    }
    
    /* 初始化GestureLibrary */
    gesLib = GestureLibraries.fromFile(gesPath);  
    if (!gesLib.load()) 
    { 
      /* 读取失败，显示Toast信息 */
      Toast.makeText(EX06_23.this,"gestures文件读取失败!程序无法运行",
                     Toast.LENGTH_LONG).show(); 
      finish();
    }
    /* GestureOverlayView初始化 */
    gesOverlay = (GestureOverlayView) findViewById(R.id.myGestures1); 
    gesOverlay.addOnGesturePerformedListener(new MyListener(this)); 
  } 
  
  /* 告定义OnGesturePerformedListener */
  public class MyListener implements OnGesturePerformedListener 
  { 
    private Context context; 
    
    public MyListener(Context context) 
    { 
      this.context = context; 
    } 
     
    @Override 
    public void onGesturePerformed(GestureOverlayView overlay,
                                   Gesture gesture) 
    {  
      /* 手势比对 */
      ArrayList<Prediction> predictions = gesLib.recognize(gesture); 
      if (predictions.size() > 0) 
      { 
        /* 取最接近的手势 */
        Prediction prediction = predictions.get(0); 
        /* 取得预测值吹少己于1.0 */ 
        if (prediction.score > 1.0) 
        { 
          /* 从通信录中取得联系人电话 */
          String phone=getContactPeople(prediction.name);
          /* 拨打电话给联系人  */
          if(phone!="")
          {
            Intent intent=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phone));
            startActivity(intent);
          }
          else
          {
        	  /* 比对不到，显示Toast */
              Toast.makeText(this.context,"phone number not found!",
                             Toast.LENGTH_SHORT).show();
          }
        }
        else
        {
          /* 比对不到，显示Toast */
          Toast.makeText(this.context,"gesture no match!",
                         Toast.LENGTH_SHORT).show(); 
        }
      }
      else
      {
        /* 比对不到，显示Toast */
        Toast.makeText(this.context,"gesture no match!",
                       Toast.LENGTH_SHORT).show();
      } 
    } 
  }
  
  /* 以联系人名称搜索通信录中联系人的电话的method */
  private String getContactPeople(String name)
  {
    String result="";
    ContentResolver contentResolver = 
      EX06_23.this.getContentResolver();
    Cursor cursor = null;

    /* 要搜索的字段名称 */
    String[] projection = new String[]
    { ContactsContract.CommonDataKinds.Phone.NUMBER };

    /* 以联系人的名字去找该联系人的电话 */
    cursor = contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection,
        ContactsContract.Contacts.DISPLAY_NAME + "=?", new String[]
        { name }, "");

    if (cursor.getCount() != 0)
    {
      cursor.moveToFirst();
      /* 取得联系人电话 */
      result = cursor.getString(0);
    }
    return result;
  }
} 

