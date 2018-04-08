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
  /*���ŧistrMessage��String*/
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
    
    /*�]�w�Ĥ@��Button�ƥ�*/
    mButton01.setOnClickListener(new Button.OnClickListener() 
    { 
      @Override 
      public void onClick(View v) 
      { 
        // TODO Auto-generated method stub 
        
        Uri uri = Uri.parse("content://contacts/people"); 
        Intent intent = new Intent(Intent.ACTION_PICK, uri);
        /*�h�^��mTextView3�̪����e*/
        strMessage = mTextView3.getText().toString();
        
        startActivityForResult(intent, PICK_CONTACT_SUBACTIVITY); 
      }      
    }); 
    
    /*�]�w�ĤG��Button�ƥ�*/
    mButton02.setOnClickListener(new Button.OnClickListener() 
    { 
      @Override 
      public void onClick(View v) 
      { 
        // TODO Auto-generated method stub 
        Uri uri = Uri.parse("content://contacts/people"); 
        Intent intent = new Intent(Intent.ACTION_PICK, uri);
        /*�h�^��mTextView5�̪����e*/
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
           
           /* ���o_id�o�����o�� */
           int contactId = c.
             getInt(c.getColumnIndex(ContactsContract.Contacts._ID));
           
           /*��_id�h�d�߹q�ܪ�Cursor */
           Cursor curContacts = getContentResolver().
             query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                 null,ContactsContract.CommonDataKinds.Phone.
                   CONTACT_ID +" = "+ contactId,null, null);

           if(curContacts.getCount()>0)
           {
             /* 2.0�i�H���\User�]�w�h�չq�ܸ��X�A�ڭ̥u�n���X�Ĥ@�չq�� */
             curContacts.moveToFirst();
             /* ���o�q�T���W�l*/
             strName = curContacts.getString(curContacts.
                 getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
             /* ���o�q�T���q��*/
             strPhone = curContacts.getString(curContacts.
                 getColumnIndex(ContactsContract.CommonDataKinds.
                                  Phone.NUMBER));
           }
           else
           {
             // nothing selected
           }
           
           /*�]�w�n�H���q�T���̪��q��*/
           String strDestAddress = strPhone; 
           System.out.println(strMessage);
           
           SmsManager smsManager = SmsManager.getDefault();
           
           PendingIntent mPI = PendingIntent.getBroadcast(EX06_03.
               this, 0, new Intent(), 0); 
           
           /*�H�X²�T*/
           smsManager.sendTextMessage(strDestAddress, null, 
               strMessage, mPI, null); 
           
           /*��Toast��ܶǰe��*/
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
