package irdc.EX09_01;

import android.app.Activity; 
import android.app.AlertDialog; 
import android.content.DialogInterface; 
import android.content.Intent; 
import android.os.Bundle; 
/*必需引用util.DisplayMetrics类别来取得屏幕分辨率*/
import android.util.DisplayMetrics; 
import android.util.Log; 
import android.view.LayoutInflater; 
import android.view.View; 
import android.widget.AbsoluteLayout; 
import android.widget.EditText; 
import android.widget.TextView; 

@SuppressWarnings("deprecation")
public class EX09_01 extends Activity 
{ 
  /*声明变量*/
  private TextView mTextView01; 
  private LayoutInflater mInflater01; 
  private View mView01; 
  private EditText mEditText01,mEditText02; 
  private String TAG = "HIPPO_DEBUG"; 
  /* 中文字的间距 */ 
  private int intShiftPadding = 14; 
   
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState); 
    setContentView(R.layout.main); 
     
    /* 建立DisplayMetrics对象，取得屏幕分辨率 */ 
    DisplayMetrics dm = new DisplayMetrics();  
    getWindowManager().getDefaultDisplay().getMetrics(dm); 
     
    mTextView01 = (TextView)findViewById(R.id.myTextView1); 
     
    /* 将文字Label放在屏幕右上方 */ 
    mTextView01.setLayoutParams 
    ( 
      new AbsoluteLayout.LayoutParams(intShiftPadding*mTextView01.getText().toString().length(),18,(dm.widthPixels-(intShiftPadding*mTextView01.getText().toString().length()))-10,0) 
    ); 
     
    /* 处理用户点击TextView文字的事件处理 -登录*/ 
    mTextView01.setOnClickListener(new TextView.OnClickListener() 
    { 
      @Override 
      public void onClick(View v) 
      { 
        // TODO Auto-generated method stub 
         
        /* 显示登录对话框 */ 
        showLoginForm(); 
      } 
    }); 
  } 
   
  /* 自定义登录对话框函数 */ 
  private void showLoginForm() 
  { 
    try 
    { 
      /* 以LayoutInflater取得主Activity的context */ 
      mInflater01 = LayoutInflater.from(EX09_01.this); 
      /* 设定建立的View所要使用的Layout Resource */ 
      mView01 = mInflater01.inflate(R.layout.login, null); 
       
      /* 账号EditText */ 
      mEditText01 = (EditText) mView01.findViewById(R.id.myEditText1); 
       
      /* 密码EditText */ 
      mEditText02 = (EditText) mView01.findViewById(R.id.myEditText2); 
      
      /*建立AlertDialog窗口来取得使用者账号密码*/ 
      new AlertDialog.Builder(this) 
      .setView(mView01) 
      .setPositiveButton("OK", 
      new DialogInterface.OnClickListener() 
      { 
        /*重写onClick()来触发取得Token事件与完成登录事件*/
        public void onClick(DialogInterface dialog, int whichButton) 
        { 
          if(processGoogleLogin(mEditText01.getText().toString(), mEditText02.getText().toString())) 
          { 
            Intent i = new Intent(); 
            /*登录后调用注销程序(EX09_01_02.java)*/
            i.setClass(EX09_01.this, EX09_01_02.class); 
            startActivity(i); 
            finish(); 
          } 
        } 
      }).show(); 
    } 
    catch(Exception e) 
    { 
      e.printStackTrace(); 
    } 
  } 
   /*调用GoogleAuthSub来取的Google账号的Authentication Token*/
  private boolean processGoogleLogin(String strUID, String strUPW) 
  { 
    try 
    { 
      /*建构自定义的GoogtleAuthSub对象*/
      GoogleAuthSub gas = new GoogleAuthSub(strUID, strUPW); 
      /*取得Google Token*/
      String strAuth =  gas.getAuthSubToken(); 
      if(strAuth!="")
      {
        /*将取回的Google Token写入log中*/
        Log.i(TAG, strAuth);
        return true;
      }
      else
      {
        return false;
      }
    } 
    catch (Exception e) 
    { 
      e.printStackTrace();
      return false;
    } 
     
  } 
} 

