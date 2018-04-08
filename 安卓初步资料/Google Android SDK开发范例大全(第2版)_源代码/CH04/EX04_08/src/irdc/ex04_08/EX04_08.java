package irdc.ex04_08; 
import android.app.Activity; 
import android.os.Bundle;
import android.view.MotionEvent; 
import android.view.View; 
import android.view.animation.Animation;
import android.view.animation.AnimationUtils; 
import android.widget.AdapterView; 
import android.widget.ArrayAdapter;
import android.widget.TextView; 
//import android.widget.ListView; 
import android.widget.Spinner; 
public class EX04_08 extends Activity 
{ 
  private static final String[] countriesStr = { "̨����", "̨����", "̨����", "������" };
  private TextView myTextView;
  private Spinner mySpinner; 
  private ArrayAdapter adapter;
  Animation myAnimation; 
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState);
    /* ����main.xml Layout */
    setContentView(R.layout.main); 
    /* ��findViewById()ȡ�ö��� */
    myTextView = (TextView) findViewById(R.id.myTextView);
    mySpinner = (Spinner) findViewById(R.id.mySpinner); 
    adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, countriesStr);
    /* myspinner_dropdownΪ�Զ��������˵���ʽ������res/layoutĿ¼�� */ 
    adapter.setDropDownViewResource(R.layout.myspinner_dropdown); 
    /* ��ArrayAdapter����Spinner������ */ mySpinner.setAdapter(adapter);
    /* ��mySpinner����OnItemSelectedListener */ 
    mySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() 
    { 
      @Override 
      public void onItemSelected(AdapterView arg0, View arg1, int arg2, long arg3) 
      {
        /* ����ѡmySpinner��ֵ����myTextView�� */
        myTextView.setText("ѡ�����" + countriesStr[arg2]);
        /* ��mySpinner��ʾ */ 
        arg0.setVisibility(View.VISIBLE); 
        } 
      @Override 
      public void onNothingSelected(AdapterView arg0) 
      { 
        // TODO Auto-generated method stub 
        }
       }); 
    /* ȡ��Animation������res/animĿ¼�� */
    myAnimation = AnimationUtils.loadAnimation(this, R.anim.my_anim); 
    /* ��mySpinner����OnTouchListener */ 
    mySpinner.setOnTouchListener(new Spinner.OnTouchListener() 
    { 
      @Override 
      public boolean onTouch(View v, MotionEvent event) 
      { 
        /* ��mySpinnerִ��Animation */
        v.startAnimation(myAnimation); 
        /* ��mySpinner���� */
        v.setVisibility(View.INVISIBLE); 
        return false; 
        } 
      }); 
    mySpinner.setOnFocusChangeListener(new Spinner.OnFocusChangeListener() 
    { 
      @Override 
      public void onFocusChange(View v, boolean hasFocus)
      { 
        // TODO Auto-generated method stub 
        }
      });
    } 
  }
  
    
  
    
     
    


