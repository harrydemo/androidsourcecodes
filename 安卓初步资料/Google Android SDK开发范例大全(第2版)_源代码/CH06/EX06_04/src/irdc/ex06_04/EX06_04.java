package irdc.ex06_04;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EX06_04 extends Activity
{
  private Button mButton01,mButton02;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    mButton01 = (Button)findViewById(R.id.myButton1);
    
    /* 开始启动系统服务按钮事件 */
    mButton01.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        // TODO Auto-generated method stub
        
        /* 建构Intent对象，指定开启对象为mService1服务 */
        Intent i = new Intent( EX06_04.this, mService1.class );
        
        /* 设定新TASK的方式 */
        i.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
        
        /* 以startService方法启动Intent */
        startService(i); 
      }
    });
    
    mButton02 = (Button)findViewById(R.id.myButton2);
    
    /* 关闭系统服务按钮事件 */
    mButton02.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        // TODO Auto-generated method stub
        
        /* 构造Intent对象，指定欲关闭的对象为mService1服务 */
        Intent i = new Intent( EX06_04.this, mService1.class );
        
        /* 以stopService方法关闭Intent */
        stopService(i);
      }
    });
  }
}

