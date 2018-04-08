package irdc.EX05_03; 
import android.app.Activity; 
/*��������PendingIntent�����ʹ��getBrocast()*/ 
import android.app.PendingIntent; 
import android.content.Intent; 
import android.os.Bundle; 
/*��������telephony.gsm.SmsManager�����ʹ��sendTextMessage()*/
import android.telephony.SmsManager;
import android.view.View; 
import android.widget.Button;
import android.widget.EditText; 
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern; 
public class EX05_03 extends Activity
{ 
  /*��������һ��Button������EditText*/
  private Button mButton1; 
  private EditText mEditText1; 
  private EditText mEditText2; 
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState); 
    setContentView(R.layout.main);
    /*͸��findViewById������������EditText1,EditText2��Button����*/
    mEditText1 = (EditText) findViewById(R.id.myEditText1);
    mEditText2 = (EditText) findViewById(R.id.myEditText2);
    mButton1 = (Button) findViewById(R.id.myButton1);
    /*��Ĭ�����ּ���EditText��*/ 
    mEditText1.setText("������绰����"); 
    mEditText2.setText("�������������!!"); 
    /*�趨onClickListener ��ʹ���ߵ�ѡEditTextʱ������Ӧ*/
    mEditText1.setOnClickListener(new EditText.OnClickListener()
    { 
      public void onClick(View v) 
      {
        /*��ѡEditTextʱ�������*/
        mEditText1.setText("");
        } 
      });
    /*�趨onClickListener ��ʹ���ߵ�ѡEditTextʱ������Ӧ*/ 
    mEditText2.setOnClickListener(new EditText.OnClickListener()
    { 
      public void onClick(View v) 
      { 
        /*��ѡEditTextʱ�������*/ 
        mEditText2.setText("");
        }
      } ); 
    /*�趨onClickListener ��ʹ���ߵ�ѡButtonʱ������Ӧ*/
    mButton1.setOnClickListener(new Button.OnClickListener() 
    {
      
   @Override 
   public void onClick(View v) 
   { 
     /*��EditText1ȡ�ü�Ѷ�ռ��˵绰*/ 
     String strDestAddress = mEditText1.getText().toString();
     /*��EditText2ȡ�ü�Ѷ��������*/ 
     String strMessage = mEditText2.getText().toString(); 
     /*����һȡ��default instance�� SmsManager���� */ 
     SmsManager smsManager = SmsManager.getDefault(); 
     // TODO Auto-generated method stub
     /*����ռ��˵绰��ʽ���Ѷ�����Ƿ񳬹�70�ַ�*/ 
     if(isPhoneNumberValid(strDestAddress)==true && iswithin70(strMessage)==true)
     { 
       try 
       { 
         /*�������������ͨ���������,���ͼ�Ѷ * 
          * �Ƚ���һPendingIntent����ʹ��getBroadcast()��������Broadcast * 
          * ��PendingIntent,�绰,��Ѷ���ֵȲ�������sendTextMessage()�������ͼ�Ѷ*/ 
         PendingIntent mPI = PendingIntent.getBroadcast(EX05_03.this, 0, new Intent(), 0); 
         smsManager.sendTextMessage(strDestAddress, null, strMessage, mPI, null);
         } 
       catch(Exception e) 
       {
         e.printStackTrace();
         }
       Toast.makeText(EX05_03.this, "�ͳ��ɹ�!!" , Toast.LENGTH_SHORT).show();
       mEditText1.setText("");
       mEditText2.setText(""); 
       } 
     /*�绰��ʽ���Ѷ���ֲ���������ʱ,ʹ��Toast��֪�û����*/ 
     else
     {
       /*�绰��ʽ����*/ 
       if (isPhoneNumberValid(strDestAddress)==false) 
       { 
         /*����������70�ַ�*/
         if(iswithin70(strMessage)==false) 
         { 
           Toast.makeText(EX05_03.this, "�绰�����ʽ����+�������ݳ���70��,����!!", Toast.LENGTH_SHORT).show();
           } 
         else 
         {
           Toast.makeText(EX05_03.this, "�绰�����ʽ����,����!!" , Toast.LENGTH_SHORT).show();
           }
         } 
       /*��������70�ַ�*/
       else if (iswithin70(strMessage)==false) 
       { 
         Toast.makeText(EX05_03.this, "�������ݳ���70��,��ɾ����������!!", Toast.LENGTH_SHORT).show();
         }
       } 
     }
   });
    } 
  /*����ַ����Ƿ�Ϊ�绰����ķ���,���ش�true or false���ж�ֵ*/
  public static boolean isPhoneNumberValid(String phoneNumber)
  {
    boolean isValid = false; 
    /* �ɽ��ܵĵ绰��ʽ��: * ^\\(? : ����ʹ�� "(" ��Ϊ��ͷ * (\\d{3}):
     *  �������������� * \\)? : ����ʹ��")"���� * [- ]? : 
     *  ��������ʽ�����ʹ�þ�ѡ���Ե� "-". * (\\d{3}) :
     *   �ٽ������������� * [- ]? : 
     *   ����ʹ�þ�ѡ���Ե� "-" ����. * (\\d{4})$: 
     *   ���ĸ����ֽ���. * ���Աȶ��������ָ�ʽ:
     *    * (123)456-7890, 123-456-7890, 1234567890, (123)-456-7890 */ 
    String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";
    /* �ɽ��ܵĵ绰��ʽ��: * ^\\(? :
     *  ����ʹ�� "(" ��Ϊ��ͷ * (\\d{2}): 
     *  �������������� * \\)? : ����ʹ��")"���� * [- ]? : 
     *  ��������ʽ�����ʹ�þ�ѡ���Ե� "-". * (\\d{4}) : 
     *  �ٽ������ĸ����� * [- ]? : 
     *  ����ʹ�þ�ѡ���Ե� "-" ����. * (\\d{4})$: 
     *  ���ĸ����ֽ���. * ���Աȶ��������ָ�ʽ: * 
     *  (123)456-7890, 123-456-7890, 1234567890, (123)-456-7890 */
    String expression2 ="^\\(?(\\d{2})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
    CharSequence inputStr = phoneNumber;
    /*����Pattern*/ Pattern pattern = Pattern.compile(expression);
    /*��Pattern �Բ�������Matcher��Regular expression*/ 
    Matcher matcher = pattern.matcher(inputStr); 
    /*����Pattern2*/ Pattern pattern2 =Pattern.compile(expression2);
    /*��Pattern2 �Բ�������Matcher2��Regular expression*/ 
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
  
     
   
    
   

