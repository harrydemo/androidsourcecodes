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
    /* ����main.xml Layout */ 
    setContentView(R.layout.main); 
    /* new ArrayAdapter���󲢽�autoStr�ַ������鴫�� */
    ArrayAdapter adapter = 
      new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, autoStr);
    /* ��findViewById()ȡ��AutoCompleteTextView���� */ 
    AutoCompleteTextView myAutoCompleteTextView = 
      (AutoCompleteTextView) findViewById(R.id.myAutoCompleteTextView);
    /* ��ArrayAdapter����AutoCompleteTextView������ */
    myAutoCompleteTextView.setAdapter(adapter); 
    }
  }