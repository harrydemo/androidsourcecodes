package irdc.ex03_19; 
import android.app.Activity; 
import android.app.ProgressDialog; 
import android.os.Bundle;
import android.view.View; 
import android.widget.Button; 
public class EX03_19 extends Activity 
{ 
  /*建立一个全局的类成员变量，类型为ProgressDialog对象*/ 
  public ProgressDialog myDialog = null; 
  @Override 
  public void onCreate(Bundle icicle)
  { 
    super.onCreate(icicle);
    /* 建立一个按钮对象 */ 
    Button btnButton1 = new Button(this); 
    this.setContentView(btnButton1);
    btnButton1.setText(R.string.str_btn1);
    /* 为建立好的按钮对象，指定OnClicklistener * 亦即按下按钮会执行的事件 * 并在事件处理函数中显示ProgressBar */ 
    btnButton1.setOnClickListener(myShowProgressBar); 
    } 
  /** 按下按钮执行的OnClickListener事件函数 */
  Button.OnClickListener myShowProgressBar =
    new Button.OnClickListener() 
  { 
    // @Override 
    public void onClick(View arg0) 
    { 
      CharSequence strDialogTitle = getString(R.string.str_dialog_title);
      CharSequence strDialogBody = getString(R.string.str_dialog_body); 
      // 显示Progress对话框
      myDialog = ProgressDialog.show 
      ( 
          EX03_19.this, strDialogTitle, strDialogBody, true );
      new Thread()
      {
        public void run()
        {
          try
          {
            /* 在这里写上要执行的程序片段 */
            /* 为了明显看见效果，以暂停3秒作为示范 */ 
            sleep(3000);
            }
          catch (Exception e)
          { }
          // 卸除所建立的myDialog对象。
          myDialog.dismiss();
          }
        }.start();
        /* 开始执行线程 */ 
        }
    };
    }
  
