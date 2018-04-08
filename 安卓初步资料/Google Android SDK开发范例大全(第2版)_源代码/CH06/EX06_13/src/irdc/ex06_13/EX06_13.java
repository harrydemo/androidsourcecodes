package irdc.ex06_13;

/* import相关class */
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EX06_13 extends Activity
{
  /* 声明设定图档的几个Button及启动与终止的两个Button */
  private Button mButton1;
  private Button mButton2;
  private Button setButton1;
  private Button setButton2;
  private Button setButton3;
  private Button setButton4;
  private Button setButton5;
  private Button setButton6;
  private Button setButton7;
  /* 声明显示图文件名称的几个TextView */
  private TextView mySet1;
  private TextView mySet2;
  private TextView mySet3;
  private TextView mySet4;
  private TextView mySet5;
  private TextView mySet6;
  private TextView mySet7;
  /* 声明定义的数据库变量DailyBgDB */
  private DailyBgDB db;
  /* 声明存放设定值的Map */
  private Map<Integer,Integer> map;
  private LayoutInflater inflater;
  private int tmpWhich=0;
  /* 声明存放图片id的阵在bg与存放图片名称的数组bgName */
  private final int[] bg =
  {R.drawable.b01,R.drawable.b02,R.drawable.b03,R.drawable.b04,
  R.drawable.b05,R.drawable.b06,R.drawable.b07};
  private final String[] bgName ={"b01.png","b02.png","b03.png",
                       "b04.png","b05.png","b06.png","b07.png"};
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    /* 加载main.xml Layout */
    setContentView(R.layout.main);
    inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    
    /* 将数据库存放的设定值放入map中 */
    initSettingData();   
    /* 初始化TextView对象 */
    mySet1=(TextView) findViewById(R.id.mySet1);
    mySet2=(TextView) findViewById(R.id.mySet2);
    mySet3=(TextView) findViewById(R.id.mySet3);
    mySet4=(TextView) findViewById(R.id.mySet4);
    mySet5=(TextView) findViewById(R.id.mySet5);
    mySet6=(TextView) findViewById(R.id.mySet6);
    mySet7=(TextView) findViewById(R.id.mySet7);
    
    /* 设定显示的图文件名称 */
    if(!map.get(0).equals(99))
    {
      mySet1.setText(bgName[map.get(0)]);
    }
    if(!map.get(1).equals(99))
    {
      mySet2.setText(bgName[map.get(1)]);
    }
    if(!map.get(2).equals(99))
    {
      mySet3.setText(bgName[map.get(2)]);
    }
    if(!map.get(3).equals(99))
    {
      mySet4.setText(bgName[map.get(3)]);
    }
    if(!map.get(4).equals(99))
    {
      mySet5.setText(bgName[map.get(4)]);
    }
    if(!map.get(5).equals(99))
    {
      mySet6.setText(bgName[map.get(5)]);
    }
    if(!map.get(6).equals(99))
    {
      mySet7.setText(bgName[map.get(6)]);
    }
    
    /* 初始化Button对象 */
    setButton1=(Button) findViewById(R.id.setButton1);
    setButton2=(Button) findViewById(R.id.setButton2);
    setButton3=(Button) findViewById(R.id.setButton3);
    setButton4=(Button) findViewById(R.id.setButton4);
    setButton5=(Button) findViewById(R.id.setButton5);
    setButton6=(Button) findViewById(R.id.setButton6);
    setButton7=(Button) findViewById(R.id.setButton7);
    /* 以initButton()来设定OnClickListener */
    setButton1=initButton(setButton1,mySet1,0);
    setButton2=initButton(setButton2,mySet2,1);
    setButton3=initButton(setButton3,mySet3,2);
    setButton4=initButton(setButton4,mySet4,3);
    setButton5=initButton(setButton5,mySet5,4);
    setButton6=initButton(setButton6,mySet6,5);
    setButton7=initButton(setButton7,mySet7,6);
    
    /* 设定启动服务的Button */
    mButton1=(Button)findViewById(R.id.myButton1);
    mButton1.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View v)
      {        
        /* 取得服务启动后几天的0点0分0秒的millsTime */
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DATE,1);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        long startTime=calendar.getTimeInMillis();
        /* 重复执行的间隔时间 */
        long repeatTime=24*60*60*1000;
        /* 将更换桌布的排程叵丈AlarmManager中 */
        Intent intent = new Intent(EX06_13.this,MyReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(EX06_13.this,
            0, intent, 0);
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        /* setRepeating()让排程重复执行
           startTime为开始执行时间
           repeatTime为重复执行间隔
           AlarmManager.RTC?使服务休眠时仍然会执行 */
        am.setRepeating(AlarmManager.RTC,startTime,repeatTime,sender);
        /* 北Toast提示已启动 */
        Toast.makeText(EX06_13.this,"服务已启动",Toast.LENGTH_SHORT)
          .show();
        /* 启动后马勺因执行几次换桌布的程序以更换今天的桌布 */
        Intent i = new Intent(EX06_13.this,ChangeBgImage.class);
        startActivity(i);
      }
    });
    
    /* 设定终止服务的Button */
    mButton2=(Button) findViewById(R.id.myButton2);
    mButton2.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View v)
      {
        Intent intent = new Intent(EX06_13.this,MyReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(EX06_13.this,
            0, intent, 0);
        /* 由AlarmManager中移除排程 */
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        am.cancel(sender);
        /* 以Toast提示已终止 */
        Toast.makeText(EX06_13.this,"服务已终止",Toast.LENGTH_SHORT)
          .show();
      }
    });
  }
  
  /* 由数据库中取得设定值的method */
  private void initSettingData()
  {
    map=new LinkedHashMap<Integer,Integer>();
    db=new DailyBgDB(EX06_13.this);
    Cursor cur=db.select();
    while(cur.moveToNext()){
      map.put(cur.getInt(0),cur.getInt(1));
    }
    cur.close();
    db.close();
  }
  
  /* 设定Button的OnClickListener的method */
  private Button initButton(Button b,final TextView t,final int id)
  {
    b.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View v)
      {        
        /* 设定按下Button后弹出的选择图片的Dialog */
        new AlertDialog.Builder(EX06_13.this)
          .setIcon(R.drawable.pic_icon)
          .setTitle("请选择图档！")
          .setSingleChoiceItems(bgName,map.get(id),
           new DialogInterface.OnClickListener()
           {
             public void onClick(DialogInterface dialog,int which)
             {
               tmpWhich=which;
               /* 选择图片后弹出预览图文件的窗口 */
               View view=inflater.inflate(R.layout.preview, null);
               TextView message=(TextView) view.findViewById(R.id.bgName);
               /* 设定预览画面的文件名与ImageView */
               message.setText(bgName[which]);
               ImageView mView01 = (ImageView)view.findViewById(R.id.bgImage);
               mView01.setImageResource(bg[which]);
               
               Toast toast=Toast.makeText(EX06_13.this,"",Toast.LENGTH_SHORT);    
               toast.setView(view);    
               toast.show(); 
             }
           })
          .setPositiveButton("确定",new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface dialog,int which1)
            {      
              /* 改变画面显示的设定图文件文件宅 */
              t.setText(bgName[tmpWhich]);
              /* 改变map里的值 */
              map.put(id,tmpWhich);
              /* 将变更的设定存入数据库 */
              saveData(id,tmpWhich);  
            }
          })
          .setNegativeButton("取消",new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface dialog,int which)
            {
            }
          }).show();
      }
    });
    return b;
  }
  
  /* 储存设定值吹DB的method */
  private void saveData(int id,int value){
    db=new DailyBgDB(EX06_13.this);
    db.update(id,value);
    db.close();
  }
}

