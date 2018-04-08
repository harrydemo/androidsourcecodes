package irdc.test_02;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class TEST_02 extends Activity {
  
  private TextView mTextView1; 
  private CheckBox mCheckBox1;
  private CheckBox mCheckBox2; 

      /** Called when the activity is first created. */
      @Override
  public void onCreate(Bundle savedInstanceState)
  {
   super.onCreate(savedInstanceState);
   setContentView(R.layout.main);
   
   mTextView1 = (TextView) findViewById(R.id.mTextView1); 
   mCheckBox1 = (CheckBox) findViewById(R.id.mCheckBox1); 
   mCheckBox2 = (CheckBox) findViewById(R.id.mCheckBox2); 
   
   mCheckBox1.setOnCheckedChangeListener(mCheckBoxChanged);
   mCheckBox2.setOnCheckedChangeListener(mCheckBoxChanged);
   
  }
  private CheckBox.OnCheckedChangeListener  
   mCheckBoxChanged = new CheckBox.OnCheckedChangeListener() 
  { 
      @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
      {
        // TODO Auto-generated method stub
   if(mCheckBox1.isChecked()||mCheckBox2.isChecked()==true) 
   { 
     mTextView1.setText(Boolean.toString(mCheckBox1.isChecked()||mCheckBox2.isChecked())); 
   }else if(mCheckBox1.isChecked()||mCheckBox2.isChecked()==false)
     { 
      mTextView1.setText(Boolean.toString(mCheckBox1.isChecked()||mCheckBox2.isChecked())); 
     }
   }
  };
}
    

