package irdc.ex08_17;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EX08_17 extends Activity
{
  private Button mButton01,mButton02;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    mButton01 = (Button)findViewById(R.id.myButton1);
    
    /* 开始访问气象局地震服务 */
    mButton01.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        // TODO Auto-generated method stub
        Intent i = new Intent( EX08_17.this, mService1.class );
        i.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
        startService(i); 
      }
    });
    
    mButton02 = (Button)findViewById(R.id.myButton2);
    
    /* 停止访问气象局地震服务 */
    mButton02.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        // TODO Auto-generated method stub
        Intent i = new Intent( EX08_17.this, mService1.class );
        stopService(i);
      }
    });
  }

  @Override
  protected void onPause()
  {
    // TODO Auto-generated method stub
    
    /* 离开程序前关闭之前开启的服务，如果有开启的话 */
    Intent i = new Intent( EX08_17.this, mService1.class );
    stopService(i);
    super.onPause();
  }
}

