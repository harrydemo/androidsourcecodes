package irdc.ex04_04; 
import android.app.Activity; 
//import android.graphics.Color; 
import android.os.Bundle; 
import android.view.View; 
import android.widget.Button; 
import android.widget.CheckBox;
import android.widget.TextView; 
public class EX04_04 extends Activity 
{ 
  /** Called when the activity is first created. */ 


  /*声明 TextView、CheckBox、Button对象*/
  public TextView myTextView1; 
  public TextView myTextView2;
  public CheckBox myCheckBox; 
  public Button myButton; 
  
  @Override 
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState); 
    setContentView(R.layout.main); 
    /*取得TextView、CheckBox、Button*/ 
    myTextView1 = (TextView) findViewById(R.id.myTextView1); 
    myTextView2 = (TextView) findViewById(R.id.myTextView2); 
    myCheckBox = (CheckBox) findViewById(R.id.myCheckBox);
    myButton = (Button) findViewById(R.id.myButton); 
    /*将CheckBox、Button预设为未选取状态*/ 
    myCheckBox.setChecked(false);
    myButton.setEnabled(false);
    myCheckBox.setOnClickListener(new CheckBox.OnClickListener()
    { 
      @Override public void onClick(View v) 
      { 
        // TODO Auto-generated method stub 
        if(myCheckBox.isChecked())
        { 
          myButton.setEnabled(true);
          myTextView2.setText("");
          } 
        else 
        { 
          myButton.setEnabled(false); 
          myTextView1.setText(R.string.text1);
          /*在TextView2里显示出"请勾选我同意"*/
          myTextView2.setText(R.string.no);
        }
      }
      });
   myButton.setOnClickListener(new Button.OnClickListener()
    { 
     @Override 
     public void onClick(View v) 
    { 
      // TODO Auto-generated method stub 
      if(myCheckBox.isChecked()) 
      {
        myTextView1.setText(R.string.ok); 
      }
      else
      {   
      }
    } 
    });
    } 
  }











