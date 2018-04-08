package irdc.ex05_20;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class EX05_20 extends Activity
{
  private Button mButton01;
  private int intWidth, intHeight, intButtonX, intButtonY;
  
  /* 保存屏幕的分辨率 */
  private int intScreenX, intScreenY;
  
  /* 按钮位移的平移量 */
  private int intShift = 2;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    DisplayMetrics dm = new DisplayMetrics(); 
    getWindowManager().getDefaultDisplay().getMetrics(dm);
    
    /* 取得屏幕解析像素 */
    intScreenX = dm.widthPixels;
    intScreenY = dm.heightPixels;
    
    /* 定义按钮的宽x高 */
    intWidth = 80;
    intHeight = 40;
    
    mButton01 =(Button) findViewById(R.id.myButton1);
    
    /* 初始化按钮位置置中 */
    RestoreButton();
    
    /* 当按下按钮，还原初始位置 */
    mButton01.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        // TODO Auto-generated method stub
        RestoreButton();
      }
    });
  }
  
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event)
  {
    // TODO Auto-generated method stub
    switch(keyCode)
    {
      /* 中间按键 */
      case KeyEvent.KEYCODE_DPAD_CENTER:
        /* keyCode=23 */
        RestoreButton();
        break;
      /* 上按键 */
      case KeyEvent.KEYCODE_DPAD_UP:
        /* keyCode=19 */
        MoveButtonUp();
        break;
      /* 下按键 */
      case KeyEvent.KEYCODE_DPAD_DOWN:
        /* keyCode=20 */
        MoveButtonDown();
        break;
      /* 左按键 */
      case KeyEvent.KEYCODE_DPAD_LEFT:
        /* keyCode=21 */
        MoveButtonLeft();
        break;
      /* 右按键 */
      case KeyEvent.KEYCODE_DPAD_RIGHT:
        /* keyCode=22 */
        MoveButtonRight();
        break;
    }
    return super.onKeyDown(keyCode, event);
  }
  
  /* 还原按钮位置的事件处理 */
  public void RestoreButton()
  {
    intButtonX = ((intScreenX-intWidth)/2);
    intButtonY = ((intScreenY-intHeight)/2);
    mMakeTextToast("("+Integer.toString(intButtonX)+","+Integer.toString(intButtonY)+")",true);
    mButton01.setLayoutParams(new AbsoluteLayout.LayoutParams(intWidth,intHeight,intButtonX,intButtonY));
  }
  
  /* 按下DPAD上按键时事件处理 */
  public void MoveButtonUp()
  {
    intButtonY = intButtonY-intShift;
    if(intButtonY<0)
    {
      intButtonY = 0;
    }
    mButton01.setLayoutParams(new AbsoluteLayout.LayoutParams(intWidth,intHeight,intButtonX,intButtonY));
  }
  
  /* 按下DPAD下按键时事件处理 */
  public void MoveButtonDown()
  {
    intButtonY = intButtonY+intShift;
    if(intButtonY>(intScreenY-intHeight))
    {
      intButtonY = intScreenY-intHeight;
    }
    mButton01.setLayoutParams(new AbsoluteLayout.LayoutParams(intWidth,intHeight,intButtonX,intButtonY));
  }
  
  /* 按下DPAD左按键时事件处理 */
  public void MoveButtonLeft()
  {
    intButtonX = intButtonX-intShift;
    if(intButtonX<0)
    {
      intButtonX = 0;
    }
    mButton01.setLayoutParams(new AbsoluteLayout.LayoutParams(intWidth,intHeight,intButtonX,intButtonY));
  }
  
  /* 按下DPAD右按键时事件处理 */
  public void MoveButtonRight()
  {
    intButtonX = intButtonX+intShift;
    if(intButtonX>(intScreenX-intWidth))
    {
      intButtonX = intScreenX-intWidth;
    }
    mButton01.setLayoutParams(new AbsoluteLayout.LayoutParams(intWidth,intHeight,intButtonX,intButtonY));
  }
  
  public void mMakeTextToast(String str, boolean isLong)
  {
    if(isLong==true)
    {
      Toast.makeText(EX05_20.this, str, Toast.LENGTH_LONG).show();
    }
    else
    {
      Toast.makeText(EX05_20.this, str, Toast.LENGTH_SHORT).show();
    }
  }
}

