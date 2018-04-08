package irdc.EX06_01;

import android.app.Activity; 
import android.os.Bundle; 
import android.widget.TextView; 

public class EX06_01 extends Activity 
{ 
  private TextView mTextView1; 
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState); 
    setContentView(R.layout.main); 
    /*透过findViewById建构巳建立TextView对象*/ 
    mTextView1 = (TextView) findViewById(R.id.myTextView1); 
    mTextView1.setText("等待接收短信..."); 
  }
}