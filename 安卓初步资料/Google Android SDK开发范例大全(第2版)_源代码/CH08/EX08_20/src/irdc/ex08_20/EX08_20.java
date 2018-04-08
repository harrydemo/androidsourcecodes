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

/* 实现Runnable Interface */
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
    /* 加载main.xml Layout */
    setContentView(R.layout.main);
    /* 对象初始化 */
    tv = (TextView) findViewById(R.id.text1);
    b1 = (Button) findViewById(R.id.button1);
    
    b1.setOnClickListener(new OnClickListener()
    {
      public void onClick(View v)
      {
        tv.setText("");
        /* 弹出ProgressDialog */
        d=new ProgressDialog(EX08_20.this);
        d.setMessage("文件下载中..");
        d.show();
        /* 启动另一个Thread，运行run() */
        Thread thread = new Thread(EX08_20.this);
        thread.start();
      }
    });
    
  }
  
  /* Handler构造之后，会监听传来的信息代码 */
  private Handler handler = new Handler()
  {
    @Override 
    public void handleMessage(Message msg)
    { 
      d.dismiss();
      tv.setText("下载完成!");
    }
  };

  @Override
  public void run()
  {
    /* 模拟文件下载耗时约10秒 */
    try
    {
      for(int i=0;i<10;i++)
      {
        /* 每执行几次循环，即暂停1秒 */
        Thread.sleep(1000);
      }
      /* 下载完成并传空的Message */
      handler.sendEmptyMessage(0);
    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
    }
  }
}

