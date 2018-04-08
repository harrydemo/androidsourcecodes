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

/* 实现Runnable Interface */
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
        d=new ProgressDialog(EX08_20_1.this);
        /* 设置ProgressDialog的style为条状 */
        d.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        /* 设定最大单位 */
        d.setMax(100);
        /* 设定初始值 */
        d.setProgress(0);
        d.setMessage("文件下载中..");
        d.show();
        /* 启动另一个Thread，运行run() */
        Thread thread = new Thread(EX08_20_1.this);
        thread.start();
      }
    });
    
  }
  
  /* Handler建构之后，会聆听传来的讯息占码  */
  private Handler handler = new Handler()
  {
    @Override 
    public void handleMessage(Message msg)
    { 
      /* 取得并传的进度值 */
      int p=msg.getData().getInt("percent");
      if(p==100)
      {
        /* 兀载完成关闭ProgressDialog */
        tv.setText("兀载完成!");
        d.dismiss();
      }
      else
      {
        /* 兀载中UPDATE进度数值 */
        d.setProgress(p);
      }
    }
  };

  @Override
  public void run()
  {
    /* 模拟档案兀载耗时约10秒 */
    try
    {
      for(int i=0;i<10;i++)
      {
        /* 每执行几次循环，?暂停1秒 */
        Thread.sleep(1000);
        
        /* 循环每执行几次进度叵10% */
        int percent=(i+1)*10;
        Message m=new Message();
        Bundle data=m.getData();
        /* 将进度放丈Message中 */
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

