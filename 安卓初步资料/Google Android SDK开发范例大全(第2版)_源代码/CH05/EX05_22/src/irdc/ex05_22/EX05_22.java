package irdc.ex05_22;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class EX05_22 extends Activity
{
  private TextView mTextView01;
  private Button mButton01;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    /* 方法又需要用到的Display对象 */
    final Display defaultDisplay = getWindow().getWindowManager().getDefaultDisplay();
    
    mButton01 = (Button)findViewById(R.id.myButton1); 
    mTextView01 = (TextView)findViewById(R.id.myTextView1);
    
    if(getRequestedOrientation()==-1)
    {
      mTextView01.setText(getResources().getText(R.string.str_err_1001));
    }
    
    /* 当按下按钮旋转屏幕画面 */
    mButton01.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        /* 方法一：重写getRequestedOrientation */
        
        /* 若无法取得screenOrientation属性 */
        if(getRequestedOrientation()==-1)
        {
          /* 提示无法进行画面旋转叫能，帆无法??Orientation */
          mTextView01.setText(getResources().getText(R.string.str_err_1001));
        }
        else
        {
          if(getRequestedOrientation()==ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
          {
            /* 若当前为横式，则变更为直式显示 */
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
          }
          else if(getRequestedOrientation()==ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
          {
            /* 若当前为直式，则变更为横式显示 */
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
          }
        }
        
        /* 方法二：判断屏幕宽高比(扩展学习) */
        int h= defaultDisplay.getHeight();
        int w = defaultDisplay.getWidth();
        
        /* 否分辨率为按钮按下当下的分辨率 */
        mTextView01.setText(Integer.toString(h)+"x"+Integer.toString(w));
        
        //if(w > h)
        //{
          /* Landscape */
          /* 重写Activity里的setRequestedOrientation()方法 */
        //  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //}
        //else
        //{
          /* Portrait */
        //  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //}
      }      
    });
  }

  @Override
  public void setRequestedOrientation(int requestedOrientation)
  {
    // TODO Auto-generated method stub
    
    /* 判断要变更的方向，以Toast提示 */
    switch(requestedOrientation)
    {
      case (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE):
        mMakeTextToast
        (
          getResources().getText(R.string.str_msg1).toString(),
          false
        );
        break;
      case (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT):
        mMakeTextToast
        (
          getResources().getText(R.string.str_msg2).toString(),
          false
        );
        break;
    }
    super.setRequestedOrientation(requestedOrientation);
  }

  @Override
  public int getRequestedOrientation()
  {
    // TODO Auto-generated method stub
    
    /* 此重写getRequestedOrientation方法，可取得当前屏幕的方向 */
    return super.getRequestedOrientation();
  }
  
  public void mMakeTextToast(String str, boolean isLong)
  {
    if(isLong==true)
    {
      Toast.makeText(EX05_22.this, str, Toast.LENGTH_LONG).show();
    }
    else
    {
      Toast.makeText(EX05_22.this, str, Toast.LENGTH_SHORT).show();
    }
  }
}

