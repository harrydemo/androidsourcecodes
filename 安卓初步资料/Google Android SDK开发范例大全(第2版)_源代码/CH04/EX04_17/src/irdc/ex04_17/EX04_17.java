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
  /* �Զ���HandlerѶϢ���룬������Ϊʶ���¼����� */ 
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
    /* �趨ProgressBar widget���� */ 
    mProgressBar01 = (ProgressBar)findViewById(R.id.myProgressBar1); 
    /* ����setIndeterminate����ָ��indeterminateģʽΪfalse */
    mProgressBar01.setIndeterminate(false); 
    /* �����°�ť�󣬿�ʼ�̹߳��� */ 
    mButton01.setOnClickListener(new Button.OnClickListener()
    {
      
    @Override
    public void onClick(View v) 
    { 
      // TODO Auto-generated method stub 
      /* ���°�ť��ProgressBar��ʾ */ 
      mTextView01.setText(R.string.str_progress_start);
      /* �����ص�ProgressBar��ʾ���� */ 
      mProgressBar01.setVisibility(View.VISIBLE);
      /* ָ��ProgressΪ���100 */ 
      mProgressBar01.setMax(100); 
      /* ��ʼProgressΪ0 */
      mProgressBar01.setProgress(0); 
      /* ��ʼһ���߳� */ 
      new Thread(new Runnable() 
      { 
        public void run()
        { 
          /* Ԥ��0��9����ִ��10�ε�ѭ������ */ 
          for (int i=0;i<10;i++) 
          { 
            try 
            {
              /* ��Ա����������ʶ����ؽ��� */ 
              intCounter = (i+1)*20; 
              /* ÿִ��һ��ѭ��������ͣ1�� */ 
              Thread.sleep(1000); 
              /* ��Threadִ��5�����ʾִ�н��� */ 
              if(i==4)
              {
                /* ��Message���󣬴��ݲ�����Handler */ 
                Message m = new Message(); 
                /* ��what����ָ��User�Զ��� */ 
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
  /* Handler����֮�󣬻�����������ѶϢ���� */ 
  Handler myMessageHandler = new Handler() 
  { 
    // @Override
    public void handleMessage(Message msg)
    {
      switch (msg.what) 
      { 
      /* ��ȡ��ʶ��Ϊ �뿪�߳�ʱ��ȡ�õ�ѶϢ */ 
      case EX04_17.GUI_STOP_NOTIFIER:
        /* ��ʾִ������ */ 
        mTextView01.setText(R.string.str_progress_done); 
        /* �趨ProgressBar WidgetΪ���� */ 
        mProgressBar01.setVisibility(View.GONE); 
        Thread.currentThread().interrupt();
        break;
        /* ��ȡ��ʶ��Ϊ �������̵߳���ʱ��ȡ�õ�ѶϢ */
        case EX04_17.GUI_THREADING_NOTIFIER:
          if(!Thread.currentThread().isInterrupted()) 
          {
            mProgressBar01.setProgress(intCounter); 
            /* ����ʾ������ʾ��TextView���� */ 
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

           
            
           

        
      
      
  
