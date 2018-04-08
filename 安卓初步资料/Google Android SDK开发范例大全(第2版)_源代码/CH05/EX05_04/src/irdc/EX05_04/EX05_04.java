package irdc.EX05_04;
import java.util.regex.Matcher;
import java.util.regex.Pattern; 
import android.app.Activity;
/*必需引用content.Intent类来开启email client*/ 
import android.content.Intent;
import android.os.Bundle; 
import android.view.KeyEvent;
import android.view.View; 
import android.widget.Button;
import android.widget.EditText;
public class EX05_04 extends Activity 
{ 
  /*声明四个EditText一个Button以及四个String变量*/ 
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
    /*透过findViewById建构子来建构Button对象*/ 
    mButton01 = (Button)findViewById(R.id.myButton1); 
    /*将Button预设设为Disable的状态*/ 
    mButton01.setEnabled(false); 
    /*透过findViewById建构子来建构所有EditText对象*/
    mEditText01 = (EditText)findViewById(R.id.myEditText1);
    mEditText02 = (EditText)findViewById(R.id.myEditText2);
    mEditText03 = (EditText)findViewById(R.id.myEditText3);
    mEditText04 = (EditText)findViewById(R.id.myEditText4);
    /*设定OnKeyListener,当key事件发生时进行反应*/
    mEditText01.setOnKeyListener(new EditText.OnKeyListener()
    {
      
    @Override 
    public boolean onKey(View v, int keyCode, KeyEvent event) 
    { 
      // TODO Auto-generated method stub 
      /*若使用者键入为正规email文字,则enable 按钮 反之则disable 按钮*/ 
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
    /*设定onClickListener 让使用者点选Button时送出email*/ 
    mButton01.setOnClickListener(new Button.OnClickListener() 
    {
      
    @Override 
    public void onClick(View v) 
    {
      // TODO Auto-generated method stub 
      /*透过Intent来发送邮件*/
      Intent mEmailIntent = 
        new Intent(android.content.Intent.ACTION_SEND); 
      /*设定邮件格式为plain/text*/
      mEmailIntent.setType("plain/text");
      /*取得EditText01,02,03,04的值作为收件人地址,附件,主题,内容*/
      strEmailReciver = new String[]{mEditText01.getText().toString()};
      strEmailCc = new String[]{mEditText02.getText().toString()};
      strEmailSubject = mEditText03.getText().toString();
      strEmailBody = mEditText04.getText().toString(); 
      /*将取得的字符串放入mEmailIntent中*/
      mEmailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, strEmailReciver); 
      mEmailIntent.putExtra(android.content.Intent.EXTRA_CC, strEmailCc);
      mEmailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, strEmailSubject);
      mEmailIntent.putExtra(android.content.Intent.EXTRA_TEXT, strEmailBody); 
      /*开启Gmail 并将相关参数传入*/ 
      startActivity(Intent.createChooser(mEmailIntent, getResources().getString(R.string.str_message))); 
      } 
    });
    } 
  /*确认字符串是否为email格式并回传true or false*/ 
  public static boolean isEmail(String strEmail) 
  {
    String strPattern = "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
    Pattern p = Pattern.compile(strPattern);
    Matcher m = p.matcher(strEmail);
    return m.matches(); 
    } 
  }
  
    
     
   
 
