package irdc.EX05_10;
import android.app.Activity; 
import android.content.Intent; 
import android.os.Bundle; 
/*��������database.Cursor,Contacts.People�� net.uri������ʹ����ϵ������*/ 
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract; 
import android.view.View;
import android.widget.Button; 
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
public class EX05_10 extends Activity 
{ 
  /*�����ĸ�UI������һ��������ΪActivity���ջش�ֵ��*/ 
  private TextView mTextView01; 
  private Button mButton01;
  private EditText mEditText01; 
  private EditText mEditText02; 
  private static final int PICK_CONTACT_SUBACTIVITY = 2;
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState); 
    setContentView(R.layout.main);
    /*͸��findViewById������������һ��TextView,����EditText,һ��Button����**/ 
    mTextView01 = (TextView)findViewById(R.id.myTextView1); 
    mEditText01 = (EditText)findViewById(R.id.myEditText01);
    mEditText02 = (EditText)findViewById(R.id.myEditText02);
    mButton01 = (Button)findViewById(R.id.myButton1); 
    /*�趨onClickListener ��ʹ���ߵ�ѡButtonʱ��Ѱ��ϵ��*/ 
    mButton01.setOnClickListener(new Button.OnClickListener() 
    {
      
    @Override 
    public void onClick(View v) 
    { 
      // TODO Auto-generated method stub 
      /*����Uri��ȡ�������˵���Դλ��*/ 
      // Uri uri = Uri.parse("content://contacts/people/"); 
      /*͸��Intent��ȡ�����������ݲ��ش���ѡ��ֵ*/
      //Intent intent = new Intent(Intent.ACTION_PICK, uri);
      /*�����µ�Activity��������Activity�ش�ֵ*/ 
      //startActivityForResult(intent, PICK_CONTACT_SUBACTIVITY);
      startActivityForResult
      (
          new Intent(Intent.ACTION_PICK, 
              android.provider.ContactsContract.Contacts.CONTENT_URI),
              PICK_CONTACT_SUBACTIVITY); 
      }
    }); 
    } 
  @Override 
  protected void onActivityResult (int requestCode, int resultCode, Intent data)
  { 
    // TODO Auto-generated method stub 
    try 
    { 
      switch (requestCode) 
      { 
        case PICK_CONTACT_SUBACTIVITY:
          final Uri uriRet = data.getData();
          if(uriRet != null) 
          { 
            try
            {
              /* ����Ҫ��android.permission.READ_CONTACTSȨ�� */
              Cursor c = managedQuery(uriRet, null, null, null, null);
              /*��Cursor�Ƶ�������ǰ��*/
              c.moveToFirst(); 
              /*ȡ�������˵�����*/ 
              String strName = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
              /*������д��EditText01��*/
              mEditText01.setText(strName); 
              /*ȡ�������˵ĵ绰*/ 
              int contactId = c.getInt(c.getColumnIndex(ContactsContract.Contacts._ID)); 
              Cursor phones = getContentResolver().query ( ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId, null, null ); 
              StringBuffer sb = new StringBuffer(); 
              int typePhone, resType; 
              String numPhone; 
              if (phones.getCount() > 0) 
              { 
                phones.moveToFirst(); 
                /* 2.0��������User�趨����绰���룬��������ֻ��һ��绰������ʾ�� */
                typePhone = phones.getInt ( phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE) );
                numPhone = phones.getString ( phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER) ); 
                resType = ContactsContract.CommonDataKinds.Phone.getTypeLabelResource(typePhone);
                sb.append(getString(resType) +": "+ numPhone +"\n");
                /*���绰д��EditText02��*/
                mEditText02.setText(numPhone);
                } 
              else
              { 
                sb.append("no Phone number found");
                }
              /*Toast�Ƿ��ȡ�������ĵ绰������绰����*/
              Toast.makeText(this, sb.toString(), Toast.LENGTH_SHORT).show(); 
              } 
            catch(Exception e)
            { 
              /*��������Ϣ��TextView����ʾ*/
              mTextView01.setText(e.toString());
              e.printStackTrace();
              }
            } 
          break;
          default: break; 
          }
      } 
    catch(Exception e) 
    { 
      e.printStackTrace(); 
      } 
    super.onActivityResult(requestCode, resultCode, data);
    }
  }
  
    
  
