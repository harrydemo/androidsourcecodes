package irdc.ex04_13; 
import android.app.Activity;
import android.os.Bundle; 
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
public class EX04_13 extends Activity 
{
  private static final String[] autoStr = new String[] { "a", "abc", "abcd", "abcde" }; 
  /** Called when the activity is first created. */
  @Override 
  public void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState); 
    /* 载入main.xml Layout */ 
    setContentView(R.layout.main); 
    /* new ArrayAdapter对象并将autoStr字符串数组传入 */
    ArrayAdapter adapter = 
      new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, autoStr);
    /* 以findViewById()取得AutoCompleteTextView对象 */ 
    AutoCompleteTextView myAutoCompleteTextView = 
      (AutoCompleteTextView) findViewById(R.id.myAutoCompleteTextView);
    /* 将ArrayAdapter加入AutoCompleteTextView对象中 */
    myAutoCompleteTextView.setAdapter(adapter); 
    }
  }