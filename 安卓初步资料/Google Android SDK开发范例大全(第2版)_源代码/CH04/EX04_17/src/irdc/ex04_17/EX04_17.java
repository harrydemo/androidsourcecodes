package irdc.ex04_17; 
import android.app.Activity; 
import android.os.Bundle; 
import android.os.Handler; 
import android.os.Message;
import android.view.View;
import android.widget.Button; 
import android.widget.ProgressBar; 
import android.widget.TextView;
public class EX04_17 extends Activity 
{ 
  private TextView mTextView01;
  private Button mButton01; 
  private ProgressBar mProgressBar01; 
  public int intCounter=0; 
  /* 自定义Handler讯息代码，用以作为识别事件处理 */ 
  protected static final int GUI_STOP_NOTIFIER = 0x108; 
  protected static final int GUI_THREADING_NOTIFIER = 0x109;
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
{ 
    super.onCreate(savedInstanceState); 
    setContentView(R.layout.main); 
    mButton01 = (Button)findViewById(R.id.myButton1);
    mTextView01 = (TextView)findViewById(R.id.myTextView1); 
    /* 设定ProgressBar widget对象 */ 
    mProgressBar01 = (ProgressBar)findViewById(R.id.myProgressBar1); 
    /* 调用setIndeterminate方法指派indeterminate模式为false */
    mProgressBar01.setIndeterminate(false); 
    /* 当按下按钮后，开始线程工作 */ 
    mButton01.setOnClickListener(new Button.OnClickListener()
    {
      
    @Override
    public void onClick(View v) 
    { 
      // TODO Auto-generated method stub 
      /* 按下按钮让ProgressBar显示 */ 
      mTextView01.setText(R.string.str_progress_start);
      /* 将隐藏的ProgressBar显示出来 */ 
      mProgressBar01.setVisibility(View.VISIBLE);
      /* 指定Progress为最多100 */ 
      mProgressBar01.setMax(100); 
      /* 初始Progress为0 */
      mProgressBar01.setProgress(0); 
      /* 起始一个线程 */ 
      new Thread(new Runnable() 
      { 
        public void run()
        { 
          /* 预设0至9，共执行10次的循环叙述 */ 
          for (int i=0;i<10;i++) 
          { 
            try 
            {
              /* 成员变量，用以识别加载进度 */ 
              intCounter = (i+1)*20; 
              /* 每执行一次循环，即暂停1秒 */ 
              Thread.sleep(1000); 
              /* 当Thread执行5秒后显示执行结束 */ 
              if(i==4)
              {
                /* 以Message对象，传递参数给Handler */ 
                Message m = new Message(); 
                /* 以what属性指定User自定义 */ 
                m.what = EX04_17.GUI_STOP_NOTIFIER;
                EX04_17.this.myMessageHandler.sendMessage(m); 
                break; 
                } 
              else 
              {
                Message m = new Message(); 
                m.what = EX04_17.GUI_THREADING_NOTIFIER;
                EX04_17.this.myMessageHandler.sendMessage(m); 
                }
              } 
            catch(Exception e)
            { 
              e.printStackTrace(); 
              }
            } 
          }
        }
      ).start(); 
      } 
    }); 
    } 
  /* Handler建构之后，会聆听传来的讯息代码 */ 
  Handler myMessageHandler = new Handler() 
  { 
    // @Override
    public void handleMessage(Message msg)
    {
      switch (msg.what) 
      { 
      /* 当取得识别为 离开线程时所取得的讯息 */ 
      case EX04_17.GUI_STOP_NOTIFIER:
        /* 显示执行终了 */ 
        mTextView01.setText(R.string.str_progress_done); 
        /* 设定ProgressBar Widget为隐藏 */ 
        mProgressBar01.setVisibility(View.GONE); 
        Thread.currentThread().interrupt();
        break;
        /* 当取得识别为 持续在线程当中时所取得的讯息 */
        case EX04_17.GUI_THREADING_NOTIFIER:
          if(!Thread.currentThread().isInterrupted()) 
          {
            mProgressBar01.setProgress(intCounter); 
            /* 将显示进度显示于TextView当中 */ 
            mTextView01.setText ( getResources().getText(R.string.str_progress_start)+
                "("+Integer.toString(intCounter)+"%)\n"+
                "Progress:"+
                Integer.toString(mProgressBar01.getProgress())+
                "\n"+"Indeterminate:"+
                Boolean.toString(mProgressBar01.isIndeterminate()) ); 
            } 
          break;
          } 
      super.handleMessage(msg);
      } 
    }; 
    }

           
            
           

        
      
      
  
