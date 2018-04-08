package irdc.EX09_01;

import android.app.Activity; 
import android.content.Intent; 
import android.os.Bundle; 
import android.util.DisplayMetrics; 
import android.view.View; 
import android.widget.AbsoluteLayout; 
import android.widget.TextView; 

@SuppressWarnings("deprecation")
public class EX09_01_02 extends Activity 
{ 
  private TextView mTextView03; 
  /* 中文字的间距 */ 
  private int intShiftPadding = 14; 
   
  @Override 
  protected void onCreate(Bundle savedInstanceState) 
  { 
    // TODO Auto-generated method stub 
    super.onCreate(savedInstanceState); 
    setContentView(R.layout.loginok); 
    
    /* 建立DisplayMetrics对象，取得屏幕分辨率 */
    DisplayMetrics dm = new DisplayMetrics();  
    getWindowManager().getDefaultDisplay().getMetrics(dm); 
     
    /*透过 findViewById()来取得TextView对象*/ 
    mTextView03 = (TextView)findViewById(R.id.myTextView3); 
    
    /* 将文字Label放在屏幕右上方 */
    mTextView03.setLayoutParams 
    ( 
      new AbsoluteLayout.LayoutParams(intShiftPadding*mTextView03.getText().toString().length(),18,(dm.widthPixels-(intShiftPadding*mTextView03.getText().toString().length()))-10,0) 
    ); 
    
    /* 处理用户点击TextView文字的事件处理-注销 */
    mTextView03.setOnClickListener(new TextView.OnClickListener() 
    { 
      /*重写onClick()事件*/
      @Override 
      public void onClick(View v) 
      { 
        // TODO Auto-generated method stub 
        Intent i = new Intent();
        /*注销后调用登录程序(EX09_01.java)*/
        i.setClass(EX09_01_02.this, EX09_01.class); 
        startActivity(i); 
        finish(); 
      } 
    }); 
  } 
} 



