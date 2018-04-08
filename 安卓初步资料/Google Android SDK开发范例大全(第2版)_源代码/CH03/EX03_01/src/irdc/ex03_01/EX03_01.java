package irdc.ex03_01; 
import android.app.Activity; 
import android.os.Bundle;
/*必须引用widget.TextView才能在程序里声明TextView对象*/
import android.widget.TextView;
public class EX03_01 extends Activity 
{ 
  /*必须引用widget.TextView才能在程序里声明TextView对象*/ 
  private TextView mTextView01; 
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState); 
    /*加载main.xml Layout，此时myTextView01:text为str_1*/
    setContentView(R.layout.main);
    mTextView01 = (TextView) findViewById(R.id.myTextView01); 
    String str_2 = "欢迎来到Android的TextView世界..."; 
    mTextView01.setText(str_2); 
  } 
}