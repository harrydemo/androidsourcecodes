package irdc.ex04_27;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SlidingDrawer;

public class EX04_27 extends Activity
{
  private GridView gv;
  private SlidingDrawer sd;
  private ImageView im;
  private int[] icons={R.drawable.alarm,R.drawable.calendar,
                       R.drawable.camera,R.drawable.clock,
                       R.drawable.music,R.drawable.tv};
  private String[] items={"Alarm","Calendar","Camera","Clock","Music","TV"};
    
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    /* 加载main.xml Layout */
    setContentView(R.layout.main);
    /* 初始化对象 */
    gv = (GridView)findViewById(R.id.myContent1); 
    sd = (SlidingDrawer)findViewById(R.id.drawer1);
    im=(ImageView)findViewById(R.id.myImage1);
    
    /* 使用告定义的MyGridViewAdapter设置GridView里面的item内容 */
    MyGridViewAdapter adapter=new MyGridViewAdapter(this,items,icons);
    gv.setAdapter(adapter);
    
    /* 设定SlidingDrawer被打开的事件处理 */
    sd.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener()
    {
      @Override
      public void onDrawerOpened()
      {
        im.setImageResource(R.drawable.close);
      }
    });
    /* 设置SlidingDrawer被关闭的事件处理 */
    sd.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener()
    {
      @Override
      public void onDrawerClosed()
      {
        im.setImageResource(R.drawable.open);
      }
    });
  }
}

