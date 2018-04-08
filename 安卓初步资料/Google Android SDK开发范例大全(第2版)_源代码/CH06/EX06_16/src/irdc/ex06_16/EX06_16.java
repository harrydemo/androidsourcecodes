package irdc.ex06_16;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class EX06_16 extends Activity
{
  /* 本程序只需运行一次，就会?日后开机时自动运行 */
  private TextView mTextView01; 
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    /* 为了快速示意，程序仅一欢迎的TextView文字作为演示 */
    mTextView01 = (TextView)findViewById(R.id.myTextView1);
    mTextView01.setText(R.string.str_welcome);
  }
}

