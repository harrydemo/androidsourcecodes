package irdc.ex03_01; 
import android.app.Activity; 
import android.os.Bundle;
/*��������widget.TextView�����ڳ���������TextView����*/
import android.widget.TextView;
public class EX03_01 extends Activity 
{ 
  /*��������widget.TextView�����ڳ���������TextView����*/ 
  private TextView mTextView01; 
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState); 
    /*����main.xml Layout����ʱmyTextView01:textΪstr_1*/
    setContentView(R.layout.main);
    mTextView01 = (TextView) findViewById(R.id.myTextView01); 
    String str_2 = "��ӭ����Android��TextView����..."; 
    mTextView01.setText(str_2); 
  } 
}