package irdc.ex03_05; 
import android.app.Activity; 
import android.os.Bundle; 
import android.util.DisplayMetrics; 
import android.widget.TextView;
public class EX03_05 extends Activity 
{
  private TextView mTextView01; 
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState); 
    setContentView(R.layout.main);
    /* �������� android.util.DisplayMetrics */
    DisplayMetrics dm = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(dm); 
    String strOpt = "�ֻ���Ļ�ֱ���Ϊ��" + dm.widthPixels + " �� " + dm.heightPixels; 
    mTextView01 = (TextView) findViewById(R.id.myTextView01); 
    mTextView01.setText(strOpt);
    } 
  }