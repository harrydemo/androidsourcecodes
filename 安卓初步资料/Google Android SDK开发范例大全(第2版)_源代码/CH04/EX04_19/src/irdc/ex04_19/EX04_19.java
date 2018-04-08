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
    /* 新增LinearLayout */ 
    myLinearLayout = new LinearLayout(this); 
    myLinearLayout.setOrientation(LinearLayout.VERTICAL);
    myLinearLayout.setBackgroundColor(android.graphics.Color.WHITE);
    /* 新增TextView */
    myTextView = new TextView(this); 
    LinearLayout.LayoutParams param1 = 
      new LinearLayout.LayoutParams( LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    myTextView.setText(R.string.title);
    myTextView.setTextColor(getResources().getColor(R.drawable.blue)); 
    /* 将TextView加到myLinearLayout */ 
    myLinearLayout.addView(myTextView, param1); 
    /* 新增ListView */ 
    myListView = new ListView(this);
    LinearLayout.LayoutParams param2 = 
      new LinearLayout.LayoutParams( LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    myListView.setBackgroundColor(getResources().getColor(R.drawable.ltgray));
    /* 将ListView加到myLinearLayout */
    myLinearLayout.addView(myListView, param2); 
    /* 将LinearLayout加到ContentView */
    setContentView(myLinearLayout); 
    /* new ArrayAdapter对象并将array字符串数组传入 */ 
    ArrayAdapter adapter =
      new ArrayAdapter(this, R.layout.my_simple_list_item, array); 
    /* 将ArrayAdapter加入ListView对象中 */
    myListView.setAdapter(adapter); 
    /* myListView加入OnItemSelectedListener */ 
    myListView .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
    { 
      @Override 
      public void onItemSelected(AdapterView arg0, View arg1, int arg2, long arg3) 
      { 
        /* 使用getSelectedItem()将选取的值带入myTextView中 */ 
        myTextView.setText("你选的是" + arg0.getSelectedItem().toString()); 
        }
      @Override 
      public void onNothingSelected(AdapterView arg0) 
      { 
        // TODO Auto-generated method stub 
        } 
      }); 
    /* myListView加入OnItemClickListener */ 
    myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() 
    { 
      @Override 
      public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) 
      { 
        /* 使用String[index]，arg2为点选到ListView的index，并将值带入myTextView中 */ 
        myTextView.setText("你选的是" + array[arg2]); 
        }
      }); 
    } 
  }
      
      
    

