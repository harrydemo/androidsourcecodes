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
    /* ����main.xml Layout */
    setContentView(R.layout.main); 
    
    /* �鿴SDCard�Ƿ���� */ 
    if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) 
    { 
      /* SD�������ڣ���ʾToast��Ϣ */
      Toast.makeText(EX06_23.this,"SD��������!�����޷�����",
                     Toast.LENGTH_LONG).show(); 
      finish(); 
    }
    
    /* ȡ��GestureLibrary���ļ�·�� */
    gesPath = new File
    (
      Environment.getExternalStorageDirectory(),"gestures" 
    ).getAbsolutePath();
     
    File file = new File(gesPath); 
    if(!file.exists()) 
    { 
      /* gestures�ļ������ڣ���ʾToast��Ϣ */
      Toast.makeText(EX06_23.this,"gestures�ļ�������!�����޷�����",
                     Toast.LENGTH_LONG).show(); 
      finish();
    }
    
    /* ��ʼ��GestureLibrary */
    gesLib = GestureLibraries.fromFile(gesPath);  
    if (!gesLib.load()) 
    { 
      /* ��ȡʧ�ܣ���ʾToast��Ϣ */
      Toast.makeText(EX06_23.this,"gestures�ļ���ȡʧ��!�����޷�����",
                     Toast.LENGTH_LONG).show(); 
      finish();
    }
    /* GestureOverlayView��ʼ�� */
    gesOverlay = (GestureOverlayView) findViewById(R.id.myGestures1); 
    gesOverlay.addOnGesturePerformedListener(new MyListener(this)); 
  } 
  
  /* �涨��OnGesturePerformedListener */
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
      /* ���Ʊȶ� */
      ArrayList<Prediction> predictions = gesLib.recognize(gesture); 
      if (predictions.size() > 0) 
      { 
        /* ȡ��ӽ������� */
        Prediction prediction = predictions.get(0); 
        /* ȡ��Ԥ��ֵ���ټ���1.0 */ 
        if (prediction.score > 1.0) 
        { 
          /* ��ͨ��¼��ȡ����ϵ�˵绰 */
          String phone=getContactPeople(prediction.name);
          /* ����绰����ϵ��  */
          if(phone!="")
          {
            Intent intent=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phone));
            startActivity(intent);
          }
          else
          {
        	  /* �ȶԲ�������ʾToast */
              Toast.makeText(this.context,"phone number not found!",
                             Toast.LENGTH_SHORT).show();
          }
        }
        else
        {
          /* �ȶԲ�������ʾToast */
          Toast.makeText(this.context,"gesture no match!",
                         Toast.LENGTH_SHORT).show(); 
        }
      }
      else
      {
        /* �ȶԲ�������ʾToast */
        Toast.makeText(this.context,"gesture no match!",
                       Toast.LENGTH_SHORT).show();
      } 
    } 
  }
  
  /* ����ϵ����������ͨ��¼����ϵ�˵ĵ绰��method */
  private String getContactPeople(String name)
  {
    String result="";
    ContentResolver contentResolver = 
      EX06_23.this.getContentResolver();
    Cursor cursor = null;

    /* Ҫ�������ֶ����� */
    String[] projection = new String[]
    { ContactsContract.CommonDataKinds.Phone.NUMBER };

    /* ����ϵ�˵�����ȥ�Ҹ���ϵ�˵ĵ绰 */
    cursor = contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection,
        ContactsContract.Contacts.DISPLAY_NAME + "=?", new String[]
        { name }, "");

    if (cursor.getCount() != 0)
    {
      cursor.moveToFirst();
      /* ȡ����ϵ�˵绰 */
      result = cursor.getString(0);
    }
    return result;
  }
} 

