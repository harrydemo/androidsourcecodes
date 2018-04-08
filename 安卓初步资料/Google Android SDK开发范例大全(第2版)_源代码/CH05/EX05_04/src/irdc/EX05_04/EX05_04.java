package irdc.EX05_04;
import java.util.regex.Matcher;
import java.util.regex.Pattern; 
import android.app.Activity;
/*��������content.Intent��������email client*/ 
import android.content.Intent;
import android.os.Bundle; 
import android.view.KeyEvent;
import android.view.View; 
import android.widget.Button;
import android.widget.EditText;
public class EX05_04 extends Activity 
{ 
  /*�����ĸ�EditTextһ��Button�Լ��ĸ�String����*/ 
  private EditText mEditText01; 
  private EditText mEditText02;
  private EditText mEditText03; 
  private EditText mEditText04;
  private Button mButton01; 
  private String[] strEmailReciver; 
  private String strEmailSubject;
  private String[] strEmailCc;
  private String strEmailBody ;
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState)
  { 
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main); 
    /*͸��findViewById������������Button����*/ 
    mButton01 = (Button)findViewById(R.id.myButton1); 
    /*��ButtonԤ����ΪDisable��״̬*/ 
    mButton01.setEnabled(false); 
    /*͸��findViewById����������������EditText����*/
    mEditText01 = (EditText)findViewById(R.id.myEditText1);
    mEditText02 = (EditText)findViewById(R.id.myEditText2);
    mEditText03 = (EditText)findViewById(R.id.myEditText3);
    mEditText04 = (EditText)findViewById(R.id.myEditText4);
    /*�趨OnKeyListener,��key�¼�����ʱ���з�Ӧ*/
    mEditText01.setOnKeyListener(new EditText.OnKeyListener()
    {
      
    @Override 
    public boolean onKey(View v, int keyCode, KeyEvent event) 
    { 
      // TODO Auto-generated method stub 
      /*��ʹ���߼���Ϊ����email����,��enable ��ť ��֮��disable ��ť*/ 
      if(isEmail(mEditText01.getText().toString()))
      { 
        mButton01.setEnabled(true);
        } 
      else 
      {
        mButton01.setEnabled(false);
        }
      return false;
      } 
    });
    /*�趨onClickListener ��ʹ���ߵ�ѡButtonʱ�ͳ�email*/ 
    mButton01.setOnClickListener(new Button.OnClickListener() 
    {
      
    @Override 
    public void onClick(View v) 
    {
      // TODO Auto-generated method stub 
      /*͸��Intent�������ʼ�*/
      Intent mEmailIntent = 
        new Intent(android.content.Intent.ACTION_SEND); 
      /*�趨�ʼ���ʽΪplain/text*/
      mEmailIntent.setType("plain/text");
      /*ȡ��EditText01,02,03,04��ֵ��Ϊ�ռ��˵�ַ,����,����,����*/
      strEmailReciver = new String[]{mEditText01.getText().toString()};
      strEmailCc = new String[]{mEditText02.getText().toString()};
      strEmailSubject = mEditText03.getText().toString();
      strEmailBody = mEditText04.getText().toString(); 
      /*��ȡ�õ��ַ�������mEmailIntent��*/
      mEmailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, strEmailReciver); 
      mEmailIntent.putExtra(android.content.Intent.EXTRA_CC, strEmailCc);
      mEmailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, strEmailSubject);
      mEmailIntent.putExtra(android.content.Intent.EXTRA_TEXT, strEmailBody); 
      /*����Gmail ������ز�������*/ 
      startActivity(Intent.createChooser(mEmailIntent, getResources().getString(R.string.str_message))); 
      } 
    });
    } 
  /*ȷ���ַ����Ƿ�Ϊemail��ʽ���ش�true or false*/ 
  public static boolean isEmail(String strEmail) 
  {
    String strPattern = "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
    Pattern p = Pattern.compile(strPattern);
    Matcher m = p.matcher(strEmail);
    return m.matches(); 
    } 
  }
  
    
     
   
 
