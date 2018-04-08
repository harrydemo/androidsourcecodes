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
    
    /* ��ʼ����ϵͳ����ť�¼� */
    mButton01.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        // TODO Auto-generated method stub
        
        /* ����Intent����ָ����������ΪmService1���� */
        Intent i = new Intent( EX06_04.this, mService1.class );
        
        /* �趨��TASK�ķ�ʽ */
        i.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
        
        /* ��startService��������Intent */
        startService(i); 
      }
    });
    
    mButton02 = (Button)findViewById(R.id.myButton2);
    
    /* �ر�ϵͳ����ť�¼� */
    mButton02.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        // TODO Auto-generated method stub
        
        /* ����Intent����ָ�����رյĶ���ΪmService1���� */
        Intent i = new Intent( EX06_04.this, mService1.class );
        
        /* ��stopService�����ر�Intent */
        stopService(i);
      }
    });
  }
}

