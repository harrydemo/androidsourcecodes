package irdc.ex04_26;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EX04_26 extends Activity 
{
  /*声明 Button对象*/
  private Button mButton1;
  
    /** Called when the activity is first created. */
    @Override
  public void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    /*取得 Button对象*/
    mButton1 = (Button) findViewById(R.id.myButton1);
    mButton1.setOnClickListener(new Button.OnClickListener()
    {

    @Override
    public void onClick(View v)
    {
      // TODO Auto-generated method stub      
      new AlertDialog.Builder(EX04_26.this)
      /*弹出窗口的最上头文字*/
      .setTitle(R.string.app_about)
      /*设置弹出窗口的图式*/
      .setIcon(R.drawable.hot) 
      /*设置弹出窗口的信息*/
      .setMessage(R.string.app_about_msg)
      .setPositiveButton(R.string.str_ok,
      new DialogInterface.OnClickListener()
    {
     public void onClick(DialogInterface dialoginterface, int i)
     {           
      finish();/*关闭窗口*/
     }
     }
    )
      /*设置弹出窗口的返回事件*/
      .setNegativeButton(R.string.str_no,
       new DialogInterface.OnClickListener()
      {
     public void onClick(DialogInterface dialoginterface, int i)   
    {
    }
      })
    .show();
    }
    });
  }
  }

