package irdc.ex04_06; 
import android.app.Activity; 
import android.os.Bundle; 
import android.widget.ArrayAdapter; 
import android.widget.RadioButton;
import android.widget.RadioGroup; 
import android.widget.TextView; 
public class EX04_06 extends Activity 
{ 
  public TextView mTextView1; 
  public RadioGroup mRadioGroup1;
  public RadioButton mRadio1,mRadio2; 
  ArrayAdapter adapter2; 
  /** Called when the activity is first created. */
  @Override 
  public void onCreate(Bundle savedInstanceState)
  { 
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main); 
    /*ȡ�� TextView��RadioGroup��RadioButton����*/
    mTextView1 = (TextView) findViewById(R.id.myTextView); 
    mRadioGroup1 = (RadioGroup) findViewById(R.id.myRadioGroup); 
    mRadio1 = (RadioButton) findViewById(R.id.myRadioButton1);
    mRadio2 = (RadioButton) findViewById(R.id.myRadioButton2);
    /*RadioGroup��OnCheckedChangeListener��ִ��*/ 
    mRadioGroup1.setOnCheckedChangeListener(mChangeRadio);
    } 
  private RadioGroup.OnCheckedChangeListener mChangeRadio =
    new RadioGroup.OnCheckedChangeListener() 
  { 
    @Override public void onCheckedChanged(RadioGroup group, int checkedId)
    { 
      // TODO Auto-generated method stub 
      if(checkedId==mRadio1.getId()) 
      { 
        /*��mRadio1�����ݴ���mTextView1*/
        mTextView1.setText(mRadio1.getText());
        } 
      else if(checkedId==mRadio2.getId())
      {
        /*��mRadio2�����ݴ���mTextView1*/ 
        mTextView1.setText(mRadio2.getText());
        }
      } 
    };
    }
  
 
 
