package irdc.ex06_02; 

/* import���class */
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
  /* �������� */
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
      /* �����׽����action��ACTION_BATTERY_CHANGED��
       * ��ִ��onBatteryInfoReceiver() */
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
    /* ����main.xml Layout */
    setContentView(R.layout.main); 
    
    /* ��ʼ��Button�����趨���º�Ĳ��� */
    mButton01 = (Button)findViewById(R.id.myButton1);  
    mButton01.setOnClickListener(new Button.OnClickListener() 
    { 
      @Override 
      public void onClick(View v) 
      { 
        /* ע�Ἰ��ϵͳ BroadcastReceiver����Ϊ���ʵ�ؼ���֮�� */ 
        registerReceiver 
        ( 
          mBatInfoReceiver, 
          new IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        ); 
      } 
    }); 
  }
  
  /* ���ص�ACTION_BATTERY_CHANGEDʱҪִ�е�method */
  public void onBatteryInfoReceiver(int intLevel, int intScale) 
  {
    /* create �����ĶԻ����� */
    d = new AlertDialog.Builder(EX06_02.this).create();
    d.setTitle(R.string.str_dialog_title); 
    /* ��ȡ�õĵ���������ʾ��Dialog�� */
    d.setMessage(getResources().getString(R.string.str_dialog_body)+ 
                 String.valueOf(intLevel * 100 / intScale) + "%");
    /* �趨�����ӻ���İ�ť */
    d.setButton(getResources().getString(R.string.str_button2),
                new DialogInterface.OnClickListener() 
      { 
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
          /* ��ע��Receiver�����رնԻ����� */ 
          unregisterReceiver(mBatInfoReceiver); 
          d.dismiss(); 
        } 
      }); 
    d.show(); 
  }
}

