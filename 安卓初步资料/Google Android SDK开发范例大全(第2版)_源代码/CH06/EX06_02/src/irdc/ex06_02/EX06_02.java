package irdc.ex06_02; 

/* import相关class */
import android.app.Activity; 
import android.app.AlertDialog;
import android.content.BroadcastReceiver; 
import android.content.Context; 
import android.content.DialogInterface;
import android.content.Intent; 
import android.content.IntentFilter; 
import android.os.Bundle; 
import android.view.View; 
import android.widget.Button; 

public class EX06_02 extends Activity 
{ 
  /* 变量声明 */
  private int intLevel;
  private int intScale; 
  private Button mButton01;
  private AlertDialog d;
  
  /* create BroadcastReceiver */
  private BroadcastReceiver mBatInfoReceiver=new BroadcastReceiver()
  {  
    public void onReceive(Context context, Intent intent) 
    { 
      String action = intent.getAction();  
      /* 如果捕捉到的action是ACTION_BATTERY_CHANGED，
       * 就执行onBatteryInfoReceiver() */
      if (Intent.ACTION_BATTERY_CHANGED.equals(action)) 
      { 
        intLevel = intent.getIntExtra("level", 0);  
        intScale = intent.getIntExtra("scale", 100); 
        onBatteryInfoReceiver(intLevel,intScale);
      }  
    } 
  };
   
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState); 
    /* 加载main.xml Layout */
    setContentView(R.layout.main); 
    
    /* 初始化Button，并设定按下后的操作 */
    mButton01 = (Button)findViewById(R.id.myButton1);  
    mButton01.setOnClickListener(new Button.OnClickListener() 
    { 
      @Override 
      public void onClick(View v) 
      { 
        /* 注册几个系统 BroadcastReceiver，作为访问电池计量之用 */ 
        registerReceiver 
        ( 
          mBatInfoReceiver, 
          new IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        ); 
      } 
    }); 
  }
  
  /* 拦截到ACTION_BATTERY_CHANGED时要执行的method */
  public void onBatteryInfoReceiver(int intLevel, int intScale) 
  {
    /* create 弹出的对话窗口 */
    d = new AlertDialog.Builder(EX06_02.this).create();
    d.setTitle(R.string.str_dialog_title); 
    /* 将取得的电吕计量显示于Dialog中 */
    d.setMessage(getResources().getString(R.string.str_dialog_body)+ 
                 String.valueOf(intLevel * 100 / intScale) + "%");
    /* 设定返并加画面的按钮 */
    d.setButton(getResources().getString(R.string.str_button2),
                new DialogInterface.OnClickListener() 
      { 
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
          /* 反注册Receiver，并关闭对话窗口 */ 
          unregisterReceiver(mBatInfoReceiver); 
          d.dismiss(); 
        } 
      }); 
    d.show(); 
  }
}

