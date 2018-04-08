package irdc.ex07_20;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

public class EX07_20 extends Activity
{
  private TextView myText1;
  private TextView myText2;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    /* 加载main.xml Layout */
    setContentView(R.layout.main);
    /* TextView初始化 */
    myText1=(TextView)findViewById(R.id.text1);
    myText2=(TextView)findViewById(R.id.text2);
  }
  
  /* 重写onTouchEvent() */  
  @Override
  public boolean onTouchEvent(MotionEvent event) 
  {   
    /* 显示触碰点的数量 */
    myText2.setText(""+event.getPointerCount());
    
    /* event的Action?断 */
    switch(event.getAction())
    {
      /* 触碰事件发生 */
      case MotionEvent.ACTION_DOWN:
        myText1.setText(getResources().getString(R.string.act1));
        break;
      /* 触碰事件结束 */
      case MotionEvent.ACTION_UP:
        myText1.setText(getResources().getString(R.string.act2));
        /* 显示点数为0 */
        myText2.setText("0");
        break;
      /* 第几个触碰点被按下 */
      case MotionEvent.ACTION_POINTER_1_DOWN:
        myText1.setText(getResources().getString(R.string.act3));
        break;
      /* 第几个触碰点被移除 */
      case MotionEvent.ACTION_POINTER_1_UP:
        myText1.setText(getResources().getString(R.string.act4));
        break;
      /* 第又个触碰点被按下 */
      case MotionEvent.ACTION_POINTER_2_DOWN:
        myText1.setText(getResources().getString(R.string.act5));
        break;
      /* 第又个触碰点被移除 */
      case MotionEvent.ACTION_POINTER_2_UP:
        myText1.setText(getResources().getString(R.string.act6));
        break;
      default:
        break;
    }
    return super.onTouchEvent(event);
  }
}

