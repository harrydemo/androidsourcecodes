package irdc.ex08_13;

/* import相关class */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EX08_13 extends Activity
{
  /* 变量声明 */
  private Button mButton;
  private EditText mEditText;
  
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    /* 初始化对象 */
    mEditText=(EditText) findViewById(R.id.myEdit);
    mButton=(Button) findViewById(R.id.myButton);
    /* 设定Button的onClick事件 */
    mButton.setOnClickListener(new Button.OnClickListener() 
    { 
      @Override 
      public void onClick(View v) 
      { 
        String path=mEditText.getText().toString();
        if(path.equals(""))
        {
          showDialog("网址不可为空白!");
        }
        else
        {
          /* new一个Intent对象，并指定class */
          Intent intent = new Intent();
          intent.setClass(EX08_13.this,EX08_13_1.class);
          
          /* new一个Bundle对象，并将要传递的数据传入 */
          Bundle bundle = new Bundle();
          bundle.putString("path",path);
          /* 将Bundle对象assign给Intent */
          intent.putExtras(bundle);
          /* 调用Activity EX08_13_1 */
          startActivityForResult(intent,0);
        }
      } 
    });
  }
  
  /* 重写 onActivityResult()*/
  @Override
  protected void onActivityResult(int requestCode,int resultCode,
                                  Intent data)
  {
    switch (resultCode)
    { 
      case 99:
        /* 回传错误时以Dialog显示 */
        Bundle bunde = data.getExtras();
        String error = bunde.getString("error");
        showDialog(error);
        break;       
      default: 
        break; 
     } 
   } 
  
  /* 显示Dialog的method */
  private void showDialog(String mess){
    new AlertDialog.Builder(EX08_13.this).setTitle("Message")
    .setMessage(mess)
    .setNegativeButton("确定", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface dialog, int which)
      {          
      }
    })
    .show();
  }
}

