package irdc.ex08_20;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/* ʵ��Runnable Interface */
public class EX08_20 extends Activity implements Runnable
{
  private ProgressDialog d;
  private TextView tv;
  private Button b1;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    /* ����main.xml Layout */
    setContentView(R.layout.main);
    /* �����ʼ�� */
    tv = (TextView) findViewById(R.id.text1);
    b1 = (Button) findViewById(R.id.button1);
    
    b1.setOnClickListener(new OnClickListener()
    {
      public void onClick(View v)
      {
        tv.setText("");
        /* ����ProgressDialog */
        d=new ProgressDialog(EX08_20.this);
        d.setMessage("�ļ�������..");
        d.show();
        /* ������һ��Thread������run() */
        Thread thread = new Thread(EX08_20.this);
        thread.start();
      }
    });
    
  }
  
  /* Handler����֮�󣬻������������Ϣ���� */
  private Handler handler = new Handler()
  {
    @Override 
    public void handleMessage(Message msg)
    { 
      d.dismiss();
      tv.setText("�������!");
    }
  };

  @Override
  public void run()
  {
    /* ģ���ļ����غ�ʱԼ10�� */
    try
    {
      for(int i=0;i<10;i++)
      {
        /* ÿִ�м���ѭ��������ͣ1�� */
        Thread.sleep(1000);
      }
      /* ������ɲ����յ�Message */
      handler.sendEmptyMessage(0);
    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
    }
  }
}

