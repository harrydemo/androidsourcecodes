package irdc.ex09_08;

/* import相关class */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EX09_08 extends Activity
{
  private Button mButton;
  private EditText mEditText1;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    /* 设定layout为main.xml */
    setContentView(R.layout.main);
    
    /* 初始化对象 */
    mEditText1=(EditText) findViewById(R.id.myEdit1);
    mButton=(Button) findViewById(R.id.myButton);
    
    /* 设定Button的onClick事件 */
    mButton.setOnClickListener(new Button.OnClickListener() 
    { 
      @Override 
      public void onClick(View v) 
      { 
        /* 输入账号的空白检查 */
        String userId=mEditText1.getText().toString();
        if(userId.equals(""))
        {
          showDialog("账号不可为空白!");
        }
        else
        {
          /* 把账号放入Bundle传给下一个Activity */
          Intent intent = new Intent();
          intent.setClass(EX09_08.this,EX09_08_1.class);
          Bundle bundle = new Bundle();
          bundle.putString("userId",userId);
          intent.putExtras(bundle);
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
    new AlertDialog.Builder(EX09_08.this).setTitle("Message")
     .setMessage(mess)
     .setNegativeButton("确定",new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface dialog, int which)
        {          
        }
      })
      .show();
    }
}

