package irdc.ex06_03;

import android.app.Activity; 
import android.app.PendingIntent;
import android.content.Intent; 
import android.database.Cursor; 
import android.net.Uri; 
import android.os.Bundle; 
import android.provider.ContactsContract;
//import android.provider.Contacts.People; 
import android.telephony.SmsManager;
//import android.util.Log;
import android.view.View; 
import android.widget.Button; 
import android.widget.TextView; 
import android.widget.Toast;

public class EX06_03 extends Activity 
{ 
  private TextView mTextView01;
  private TextView mTextView3;
  private TextView mTextView5;
  private Button mButton01;
  private Button mButton02;
  /*先宣告strMessage為String*/
  String strMessage;
  //private String TAG;
   
  private static final int PICK_CONTACT_SUBACTIVITY = 2; 
   
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState); 
    setContentView(R.layout.main); 
     
    mTextView01 = (TextView)findViewById(R.id.myTextView1);
    mButton01 = (Button)findViewById(R.id.myButton1);
    mTextView3 = (TextView)findViewById(R.id.myTextView3);
    mButton02 = (Button)findViewById(R.id.myButton2);
    mTextView5= (TextView)findViewById(R.id.myTextView5);
    
    /*設定第一個Button事件*/
    mButton01.setOnClickListener(new Button.OnClickListener() 
    { 
      @Override 
      public void onClick(View v) 
      { 
        // TODO Auto-generated method stub 
        
        Uri uri = Uri.parse("content://contacts/people"); 
        Intent intent = new Intent(Intent.ACTION_PICK, uri);
        /*去擷取mTextView3裡的內容*/
        strMessage = mTextView3.getText().toString();
        
        startActivityForResult(intent, PICK_CONTACT_SUBACTIVITY); 
      }      
    }); 
    
    /*設定第二個Button事件*/
    mButton02.setOnClickListener(new Button.OnClickListener() 
    { 
      @Override 
      public void onClick(View v) 
      { 
        // TODO Auto-generated method stub 
        Uri uri = Uri.parse("content://contacts/people"); 
        Intent intent = new Intent(Intent.ACTION_PICK, uri);
        /*去擷取mTextView5裡的內容*/
        strMessage = mTextView5.getText().toString();
        
        startActivityForResult(intent, PICK_CONTACT_SUBACTIVITY); 
      }      
    }); 
  } 

  @Override 
  protected void onActivityResult 
  (int requestCode, int resultCode, Intent data) 
  { 
    // TODO Auto-generated method stub 
    switch (requestCode) 
    {  
      case PICK_CONTACT_SUBACTIVITY: 
        final Uri uriRet = data.getData(); 
        if(uriRet != null) 
        { 
          try 
          {
           Cursor c = managedQuery(uriRet, null, null, null, null);
           c.moveToFirst();
           String strName = "";
           String strPhone = "";
           
           /* 取得_id這個欄位得值 */
           int contactId = c.
             getInt(c.getColumnIndex(ContactsContract.Contacts._ID));
           
           /*用_id去查詢電話的Cursor */
           Cursor curContacts = getContentResolver().
             query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                 null,ContactsContract.CommonDataKinds.Phone.
                   CONTACT_ID +" = "+ contactId,null, null);

           if(curContacts.getCount()>0)
           {
             /* 2.0可以允許User設定多組電話號碼，我們只要撈出第一組電話 */
             curContacts.moveToFirst();
             /* 取得通訊錄名子*/
             strName = curContacts.getString(curContacts.
                 getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
             /* 取得通訊錄電話*/
             strPhone = curContacts.getString(curContacts.
                 getColumnIndex(ContactsContract.CommonDataKinds.
                                  Phone.NUMBER));
           }
           else
           {
             // nothing selected
           }
           
           /*設定要寄給通訊錄裡的電話*/
           String strDestAddress = strPhone; 
           System.out.println(strMessage);
           
           SmsManager smsManager = SmsManager.getDefault();
           
           PendingIntent mPI = PendingIntent.getBroadcast(EX06_03.
               this, 0, new Intent(), 0); 
           
           /*寄出簡訊*/
           smsManager.sendTextMessage(strDestAddress, null, 
               strMessage, mPI, null); 
           
           /*用Toast顯示傳送中*/
           Toast.makeText(EX06_03.this, getString(R.string.str_msg)+
               strName,Toast.LENGTH_SHORT).show();
           
           mTextView01.setText(strName+":"+strPhone);
           
          } 
          catch(Exception e) 
          {             
            mTextView01.setText(e.toString()); 
            e.printStackTrace(); 
          } 
        } 
        break; 
    }   
    super.onActivityResult(requestCode, resultCode, data);    
  } 
}
