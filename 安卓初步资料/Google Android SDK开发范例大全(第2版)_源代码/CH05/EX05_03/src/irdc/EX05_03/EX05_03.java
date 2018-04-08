package irdc.EX05_03; 
import android.app.Activity; 
/*必需引用PendingIntent类才能使用getBrocast()*/ 
import android.app.PendingIntent; 
import android.content.Intent; 
import android.os.Bundle; 
/*必需引用telephony.gsm.SmsManager类才能使用sendTextMessage()*/
import android.telephony.SmsManager;
import android.view.View; 
import android.widget.Button;
import android.widget.EditText; 
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern; 
public class EX05_03 extends Activity
{ 
  /*声明变量一个Button与两个EditText*/
  private Button mButton1; 
  private EditText mEditText1; 
  private EditText mEditText2; 
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState); 
    setContentView(R.layout.main);
    /*透过findViewById建构子来建构EditText1,EditText2与Button对象*/
    mEditText1 = (EditText) findViewById(R.id.myEditText1);
    mEditText2 = (EditText) findViewById(R.id.myEditText2);
    mButton1 = (Button) findViewById(R.id.myButton1);
    /*将默认文字加载EditText中*/ 
    mEditText1.setText("请输入电话号码"); 
    mEditText2.setText("请输入短信内容!!"); 
    /*设定onClickListener 让使用者点选EditText时做出反应*/
    mEditText1.setOnClickListener(new EditText.OnClickListener()
    { 
      public void onClick(View v) 
      {
        /*点选EditText时清空内文*/
        mEditText1.setText("");
        } 
      });
    /*设定onClickListener 让使用者点选EditText时做出反应*/ 
    mEditText2.setOnClickListener(new EditText.OnClickListener()
    { 
      public void onClick(View v) 
      { 
        /*点选EditText时清空内文*/ 
        mEditText2.setText("");
        }
      } ); 
    /*设定onClickListener 让使用者点选Button时做出反应*/
    mButton1.setOnClickListener(new Button.OnClickListener() 
    {
      
   @Override 
   public void onClick(View v) 
   { 
     /*由EditText1取得简讯收件人电话*/ 
     String strDestAddress = mEditText1.getText().toString();
     /*由EditText2取得简讯文字内容*/ 
     String strMessage = mEditText2.getText().toString(); 
     /*建构一取得default instance的 SmsManager对象 */ 
     SmsManager smsManager = SmsManager.getDefault(); 
     // TODO Auto-generated method stub
     /*检查收件人电话格式与简讯字数是否超过70字符*/ 
     if(isPhoneNumberValid(strDestAddress)==true && iswithin70(strMessage)==true)
     { 
       try 
       { 
         /*两个条件都检查通过的情况下,发送简讯 * 
          * 先建构一PendingIntent对象并使用getBroadcast()方法进行Broadcast * 
          * 将PendingIntent,电话,简讯文字等参数传入sendTextMessage()方法发送简讯*/ 
         PendingIntent mPI = PendingIntent.getBroadcast(EX05_03.this, 0, new Intent(), 0); 
         smsManager.sendTextMessage(strDestAddress, null, strMessage, mPI, null);
         } 
       catch(Exception e) 
       {
         e.printStackTrace();
         }
       Toast.makeText(EX05_03.this, "送出成功!!" , Toast.LENGTH_SHORT).show();
       mEditText1.setText("");
       mEditText2.setText(""); 
       } 
     /*电话格式与简讯文字不符合条件时,使用Toast告知用户检查*/ 
     else
     {
       /*电话格式不符*/ 
       if (isPhoneNumberValid(strDestAddress)==false) 
       { 
         /*且字数超过70字符*/
         if(iswithin70(strMessage)==false) 
         { 
           Toast.makeText(EX05_03.this, "电话号码格式错误+短信内容超过70字,请检查!!", Toast.LENGTH_SHORT).show();
           } 
         else 
         {
           Toast.makeText(EX05_03.this, "电话号码格式错误,请检查!!" , Toast.LENGTH_SHORT).show();
           }
         } 
       /*字数超过70字符*/
       else if (iswithin70(strMessage)==false) 
       { 
         Toast.makeText(EX05_03.this, "短信内容超过70字,请删除部分内容!!", Toast.LENGTH_SHORT).show();
         }
       } 
     }
   });
    } 
  /*检查字符串是否为电话号码的方法,并回传true or false的判断值*/
  public static boolean isPhoneNumberValid(String phoneNumber)
  {
    boolean isValid = false; 
    /* 可接受的电话格式有: * ^\\(? : 可以使用 "(" 作为开头 * (\\d{3}):
     *  紧接着三个数字 * \\)? : 可以使用")"接续 * [- ]? : 
     *  在上述格式后可以使用具选择性的 "-". * (\\d{3}) :
     *   再紧接着三个数字 * [- ]? : 
     *   可以使用具选择性的 "-" 接续. * (\\d{4})$: 
     *   以四个数字结束. * 可以比对下列数字格式:
     *    * (123)456-7890, 123-456-7890, 1234567890, (123)-456-7890 */ 
    String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";
    /* 可接受的电话格式有: * ^\\(? :
     *  可以使用 "(" 作为开头 * (\\d{2}): 
     *  紧接着两个数字 * \\)? : 可以使用")"接续 * [- ]? : 
     *  在上述格式后可以使用具选择性的 "-". * (\\d{4}) : 
     *  再紧接着四个数字 * [- ]? : 
     *  可以使用具选择性的 "-" 接续. * (\\d{4})$: 
     *  以四个数字结束. * 可以比对下列数字格式: * 
     *  (123)456-7890, 123-456-7890, 1234567890, (123)-456-7890 */
    String expression2 ="^\\(?(\\d{2})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
    CharSequence inputStr = phoneNumber;
    /*建立Pattern*/ Pattern pattern = Pattern.compile(expression);
    /*将Pattern 以参数传入Matcher作Regular expression*/ 
    Matcher matcher = pattern.matcher(inputStr); 
    /*建立Pattern2*/ Pattern pattern2 =Pattern.compile(expression2);
    /*将Pattern2 以参数传入Matcher2作Regular expression*/ 
    Matcher matcher2= pattern2.matcher(inputStr); 
    if(matcher.matches()||matcher2.matches()) 
    {
      isValid = true;
      } 
    return isValid;
    }
  public static boolean iswithin70(String text)
  {
    if (text.length()<= 70) 
      return true; 
    else
      return false; 
    } 
  }
  
     
   
    
   

