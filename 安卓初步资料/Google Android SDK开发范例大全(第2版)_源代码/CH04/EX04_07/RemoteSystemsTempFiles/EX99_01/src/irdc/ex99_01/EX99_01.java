package irdc.ex99_01;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EX99_01 extends Activity {
    /** Called when the activity is first created. */
  
  public Button mButton1;
  public TextView mTextView1;
  
  
  
  
  @Override
    public void onCreate(Bundle savedInstanceState) 
{
     super.onCreate(savedInstanceState);
     setContentView(R.layout.main);
  
     mTextView1 = (TextView) findViewById(R.id.mTextView1);
     mButton1 = (Button) findViewById(R.id.mButton1);
     
     mButton1.setOnClickListener(new Button.OnClickListener()
  {  
     @Override
     public void onClick(View v)
    {
       mTextView1.setText("HI~~~");
       
    }
  });
  }
}