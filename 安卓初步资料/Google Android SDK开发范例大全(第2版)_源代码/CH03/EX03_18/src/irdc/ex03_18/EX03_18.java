package irdc.ex03_18; 
import android.app.Activity; 
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View; 
import android.widget.Button; 
import android.widget.TextView;
public class EX03_18 extends Activity
{
  private Button mButton1; 
  private TextView mTextView1; 
  public ProgressDialog myDialog = null; 
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    mButton1 =(Button) findViewById(R.id.myButton1);
    mTextView1 = (TextView) findViewById(R.id.myTextView1);
    mButton1.setOnClickListener(myShowProgressBar); 
  } 
  Button.OnClickListener myShowProgressBar = 
    new Button.OnClickListener() 
  { 
    public void onClick(View arg0) 
    { 
      final CharSequence strDialogTitle = 
        getString(R.string.str_dialog_title);
      final CharSequence strDialogBody = 
        getString(R.string.str_dialog_body); 
      // 显示Progress对话框 
      myDialog = ProgressDialog.show 
      ( EX03_18.this, 
          strDialogTitle, 
          strDialogBody, 
          true ); 
      mTextView1.setText(strDialogBody); 
      new Thread() 
      { 
        public void run() 
        { try 
        { 
          /* 在这里写上要背景执行的程序片段 */
          /* 为了明显看见效果，以暂停3秒作为示范 */
          sleep(3000); 
          }
        catch (Exception e) 
        {
          e.printStackTrace(); 
        } finally
        { // 卸除所建立的myDialog对象。
          myDialog.dismiss();
          }
        }
        }.start(); /* 开始执行线程 */ 
        }
    /*End: public void onClick(View arg0)*/ 
  };
}