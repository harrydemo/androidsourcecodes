package irdc.ex04_09; 
import android.app.Activity; 
import android.os.Bundle; 
import android.view.View; 
import android.widget.AdapterView; 
import android.widget.ArrayAdapter; 
import android.widget.Button; 
import android.widget.EditText; 
import android.widget.Spinner; 
import android.widget.TextView;
import java.util.ArrayList; 
import java.util.List; 
public class EX04_09 extends Activity 
{ 
  private static final String[] countriesStr = { "台北市", "台北县", "台中市", "高雄市" };
  private TextView myTextView;
  private EditText myEditText;
  private Button myButton_add;
  private Button myButton_remove; 
  private Spinner mySpinner; 
  private ArrayAdapter adapter;
  private List allCountries;
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState);
    /* 载入main.xml Layout */
    setContentView(R.layout.main); allCountries = new ArrayList(); 
    for (int i = 0; i < countriesStr.length; i++)
    { 
      allCountries.add(countriesStr[i]);
      } 
    /* new ArrayAdapter物件并将allCountries传入 */ 
    adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, allCountries);
    adapter .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    /* 以findViewById()取得对象 */
    myTextView = (TextView) findViewById(R.id.myTextView);
    myEditText = (EditText) findViewById(R.id.myEditText);
    myButton_add = (Button) findViewById(R.id.myButton_add); 
    myButton_remove = (Button) findViewById(R.id.myButton_remove);
    mySpinner = (Spinner) findViewById(R.id.mySpinner); 
    /* 将ArrayAdapter加入Spinner对象中 */ 
    mySpinner.setAdapter(adapter); 
    /* 将myButton_add加入OnClickListener */ 
    myButton_add.setOnClickListener(new Button.OnClickListener() 
    { 
      @Override 
      public void onClick(View arg0) 
      { 
        String newCountry = myEditText.getText().toString(); 
        /* 先比对新增的值是否已存在，不存在才可新增 */ 
        for (int i = 0; i < adapter.getCount(); i++)
        { 
          if (newCountry.equals(adapter.getItem(i))) 
          {
            return; 
            } 
          }
        if (!newCountry.equals(""))
        {
          /* 将值新增至adapter */ adapter.add(newCountry);
          /* 取得新增的值的位置 */
          int position = adapter.getPosition(newCountry); 
          /* 将Spinner选取在新增的值的位置 */ 
          mySpinner.setSelection(position); 
          /* 将myEditText清空 */ 
          myEditText.setText(""); 
          } 
        }
      }); 
    /* 将myButton_remove加入OnClickListener */ 
    myButton_remove.setOnClickListener(new Button.OnClickListener()
    { 
      @Override 
      public void onClick(View arg0) 
      { 
        if (mySpinner.getSelectedItem() != null) 
        {
          /* 移除mySpinner的值 */ 
          adapter.remove(mySpinner.getSelectedItem().toString()); 
          /* 将myEditText清空 */
          myEditText.setText("");
          if (adapter.getCount() == 0)
          { 
            /* 将myTextView清空 */ 
            myTextView.setText(""); 
            }
          }
        } 
      }); 
    /* 将mySpinner加入OnItemSelectedListener */ 
    mySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
    { 
      @Override
      public void onItemSelected(AdapterView arg0, View arg1, int arg2, long arg3)
      { 
        /* 将所选mySpinner的值带入myTextView中 */ 
        myTextView.setText(arg0.getSelectedItem().toString());
        }
      @Override 
      public void onNothingSelected(AdapterView arg0)
      {
        
      } 
      }); 
    } 
  }