package irdc.ex04_19;
import android.app.Activity;
import android.os.Bundle; 
import android.view.View; 
import android.widget.AdapterView;
import android.widget.ArrayAdapter; 
import android.widget.LinearLayout; 
import android.widget.ListView; 
import android.widget.TextView; 
public class EX04_19 extends Activity 
{ 
  private static final String[] array = { "sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday" }; 
  LinearLayout myLinearLayout; 
  TextView myTextView; 
  ListView myListView;
  /** Called when the activity is first created. */
  @Override 
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState);
    /* ����LinearLayout */ 
    myLinearLayout = new LinearLayout(this); 
    myLinearLayout.setOrientation(LinearLayout.VERTICAL);
    myLinearLayout.setBackgroundColor(android.graphics.Color.WHITE);
    /* ����TextView */
    myTextView = new TextView(this); 
    LinearLayout.LayoutParams param1 = 
      new LinearLayout.LayoutParams( LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    myTextView.setText(R.string.title);
    myTextView.setTextColor(getResources().getColor(R.drawable.blue)); 
    /* ��TextView�ӵ�myLinearLayout */ 
    myLinearLayout.addView(myTextView, param1); 
    /* ����ListView */ 
    myListView = new ListView(this);
    LinearLayout.LayoutParams param2 = 
      new LinearLayout.LayoutParams( LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    myListView.setBackgroundColor(getResources().getColor(R.drawable.ltgray));
    /* ��ListView�ӵ�myLinearLayout */
    myLinearLayout.addView(myListView, param2); 
    /* ��LinearLayout�ӵ�ContentView */
    setContentView(myLinearLayout); 
    /* new ArrayAdapter���󲢽�array�ַ������鴫�� */ 
    ArrayAdapter adapter =
      new ArrayAdapter(this, R.layout.my_simple_list_item, array); 
    /* ��ArrayAdapter����ListView������ */
    myListView.setAdapter(adapter); 
    /* myListView����OnItemSelectedListener */ 
    myListView .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
    { 
      @Override 
      public void onItemSelected(AdapterView arg0, View arg1, int arg2, long arg3) 
      { 
        /* ʹ��getSelectedItem()��ѡȡ��ֵ����myTextView�� */ 
        myTextView.setText("��ѡ����" + arg0.getSelectedItem().toString()); 
        }
      @Override 
      public void onNothingSelected(AdapterView arg0) 
      { 
        // TODO Auto-generated method stub 
        } 
      }); 
    /* myListView����OnItemClickListener */ 
    myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() 
    { 
      @Override 
      public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) 
      { 
        /* ʹ��String[index]��arg2Ϊ��ѡ��ListView��index������ֵ����myTextView�� */ 
        myTextView.setText("��ѡ����" + array[arg2]); 
        }
      }); 
    } 
  }
      
      
    

