package irdc.ex04_01; 
import android.app.Activity; 
import android.os.Bundle; 
import android.view.KeyEvent; 
import android.view.View; 
import android.widget.EditText; 
import android.widget.TextView;
public class EX04_01 extends Activity 
{
  /*声明 TextView、EditText对象*/ 
  private TextView mTextView01;
  private EditText mEditText01; 
  /** Called when the activity is first created. */ 
  @Override
  public void onCreate(Bundle savedInstanceState)
  { 
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main); 
    /*取得TextView、EditText*/
    mTextView01 = (TextView)findViewById(R.id.myTextView); 
    mEditText01 = (EditText)findViewById(R.id.myEditText);
    /*设定EditText用OnKeyListener事件来启动*/
    mEditText01.setOnKeyListener(new EditText.OnKeyListener()
    { 
      @Override 
      public boolean onKey(View arg0, int arg1, KeyEvent arg2)
      { 
        // TODO Auto-generated method stub
        /*设定TextView显示EditText所输入的内容*/ 
        mTextView01.setText(mEditText01.getText());
        return false;
        } 
      }
    );
    }
  }


