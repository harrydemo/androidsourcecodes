package irdc.ex03_11;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EX03_11 extends Activity
{
  private Button mButton1;
  private TextView mTextView1;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    mTextView1 = (TextView) findViewById(R.id.myTextView1);
    mButton1 = (Button) findViewById(R.id.myButton1);
    
    mButton1.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        mTextView1.setText(R.string.str_hi);
      }
    });
    
    
    
  }
}