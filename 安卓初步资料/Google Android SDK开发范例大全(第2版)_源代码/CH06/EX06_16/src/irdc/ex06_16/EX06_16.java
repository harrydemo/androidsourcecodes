package irdc.ex06_16;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class EX06_16 extends Activity
{
  /* ������ֻ������һ�Σ��ͻ�?�պ󿪻�ʱ�Զ����� */
  private TextView mTextView01; 
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    /* Ϊ�˿���ʾ�⣬�����һ��ӭ��TextView������Ϊ��ʾ */
    mTextView01 = (TextView)findViewById(R.id.myTextView1);
    mTextView01.setText(R.string.str_welcome);
  }
}

