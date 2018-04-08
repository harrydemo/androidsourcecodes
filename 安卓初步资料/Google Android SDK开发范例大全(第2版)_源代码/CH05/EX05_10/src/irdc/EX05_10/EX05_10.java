package irdc.EX05_10;
import android.app.Activity; 
import android.content.Intent; 
import android.os.Bundle; 
/*必需引用database.Cursor,Contacts.People与 net.uri等类来使用联系人数据*/ 
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
  /*声明四个UI变量与一个常数作为Activity接收回传值用*/ 
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
    /*透过findViewById建构子来建构一个TextView,两个EditText,一个Button对象**/ 
    mTextView01 = (TextView)findViewById(R.id.myTextView1); 
    mEditText01 = (EditText)findViewById(R.id.myEditText01);
    mEditText02 = (EditText)findViewById(R.id.myEditText02);
    mButton01 = (Button)findViewById(R.id.myButton1); 
    /*设定onClickListener 让使用者点选Button时搜寻联系人*/ 
    mButton01.setOnClickListener(new Button.OnClickListener() 
    {
      
    @Override 
    public void onClick(View v) 
    { 
      // TODO Auto-generated method stub 
      /*建构Uri来取得联络人的资源位置*/ 
      // Uri uri = Uri.parse("content://contacts/people/"); 
      /*透过Intent来取得联络人数据并回传所选的值*/
      //Intent intent = new Intent(Intent.ACTION_PICK, uri);
      /*开启新的Activity并期望该Activity回传值*/ 
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
              /* 必须要有android.permission.READ_CONTACTS权限 */
              Cursor c = managedQuery(uriRet, null, null, null, null);
              /*将Cursor移到资料最前端*/
              c.moveToFirst(); 
              /*取得联络人的姓名*/ 
              String strName = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
              /*将姓名写入EditText01中*/
              mEditText01.setText(strName); 
              /*取得联络人的电话*/ 
              int contactId = c.getInt(c.getColumnIndex(ContactsContract.Contacts._ID)); 
              Cursor phones = getContentResolver().query ( ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId, null, null ); 
              StringBuffer sb = new StringBuffer(); 
              int typePhone, resType; 
              String numPhone; 
              if (phones.getCount() > 0) 
              { 
                phones.moveToFirst(); 
                /* 2.0可以允许User设定多组电话号码，但本范例只捞一组电话号码作示范 */
                typePhone = phones.getInt ( phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE) );
                numPhone = phones.getString ( phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER) ); 
                resType = ContactsContract.CommonDataKinds.Phone.getTypeLabelResource(typePhone);
                sb.append(getString(resType) +": "+ numPhone +"\n");
                /*将电话写入EditText02中*/
                mEditText02.setText(numPhone);
                } 
              else
              { 
                sb.append("no Phone number found");
                }
              /*Toast是否读取到完整的电话种类与电话号码*/
              Toast.makeText(this, sb.toString(), Toast.LENGTH_SHORT).show(); 
              } 
            catch(Exception e)
            { 
              /*将错误信息在TextView中显示*/
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
  
    
  
