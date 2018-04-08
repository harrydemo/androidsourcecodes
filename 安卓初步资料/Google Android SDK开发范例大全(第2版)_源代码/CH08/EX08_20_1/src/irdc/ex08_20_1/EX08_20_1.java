package irdc.ex08_20_1;

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
public class EX08_20_1 extends Activity implements Runnable
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
        d=new ProgressDialog(EX08_20_1.this);
        /* ����ProgressDialog��styleΪ��״ */
        d.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        /* �趨���λ */
        d.setMax(100);
        /* �趨��ʼֵ */
        d.setProgress(0);
        d.setMessage("�ļ�������..");
        d.show();
        /* ������һ��Thread������run() */
        Thread thread = new Thread(EX08_20_1.this);
        thread.start();
      }
    });
    
  }
  
  /* Handler����֮�󣬻�����������ѶϢռ��  */
  private Handler handler = new Handler()
  {
    @Override 
    public void handleMessage(Message msg)
    { 
      /* ȡ�ò����Ľ���ֵ */
      int p=msg.getData().getInt("percent");
      if(p==100)
      {
        /* أ����ɹر�ProgressDialog */
        tv.setText("أ�����!");
        d.dismiss();
      }
      else
      {
        /* أ����UPDATE������ֵ */
        d.setProgress(p);
      }
    }
  };

  @Override
  public void run()
  {
    /* ģ�⵵��أ�غ�ʱԼ10�� */
    try
    {
      for(int i=0;i<10;i++)
      {
        /* ÿִ�м���ѭ����?��ͣ1�� */
        Thread.sleep(1000);
        
        /* ѭ��ÿִ�м��ν�����10% */
        int percent=(i+1)*10;
        Message m=new Message();
        Bundle data=m.getData();
        /* �����ȷ���Message�� */
        data.putInt("percent",percent);
        m.setData(data);
        /* send Message */
        handler.sendMessage(m);
      }
    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
    }
  }
}

