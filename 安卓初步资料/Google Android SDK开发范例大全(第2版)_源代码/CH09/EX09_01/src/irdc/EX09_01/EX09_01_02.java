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
  /* �����ֵļ�� */ 
  private int intShiftPadding = 14; 
   
  @Override 
  protected void onCreate(Bundle savedInstanceState) 
  { 
    // TODO Auto-generated method stub 
    super.onCreate(savedInstanceState); 
    setContentView(R.layout.loginok); 
    
    /* ����DisplayMetrics����ȡ����Ļ�ֱ��� */
    DisplayMetrics dm = new DisplayMetrics();  
    getWindowManager().getDefaultDisplay().getMetrics(dm); 
     
    /*͸�� findViewById()��ȡ��TextView����*/ 
    mTextView03 = (TextView)findViewById(R.id.myTextView3); 
    
    /* ������Label������Ļ���Ϸ� */
    mTextView03.setLayoutParams 
    ( 
      new AbsoluteLayout.LayoutParams(intShiftPadding*mTextView03.getText().toString().length(),18,(dm.widthPixels-(intShiftPadding*mTextView03.getText().toString().length()))-10,0) 
    ); 
    
    /* �����û����TextView���ֵ��¼�����-ע�� */
    mTextView03.setOnClickListener(new TextView.OnClickListener() 
    { 
      /*��дonClick()�¼�*/
      @Override 
      public void onClick(View v) 
      { 
        // TODO Auto-generated method stub 
        Intent i = new Intent();
        /*ע������õ�¼����(EX09_01.java)*/
        i.setClass(EX09_01_02.this, EX09_01.class); 
        startActivity(i); 
        finish(); 
      } 
    }); 
  } 
} 



