package irdc.test_01;

import irdc.test_01.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class TEST_01 extends Activity 
{
    /** Called when the activity is first created. */
  
  public TextView mTextView;
  public CheckBox mCheckBox1;
  public CheckBox mCheckBox2;
  public CheckBox mCheckBox3;
    
  @Override
    public void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
   
    mTextView = (TextView) findViewById(R.id.mTextView);
    mCheckBox1 = (CheckBox) findViewById(R.id.mCheckBox1);
    mCheckBox2 = (CheckBox) findViewById(R.id.mCheckBox2);
    mCheckBox3 = (CheckBox) findViewById(R.id.mCheckBox3);
    
    
    mCheckBox1.setOnClickListener(new CheckBox.OnClickListener()
    {  
     @Override
      public void onClick(View v)
     {
       String s="�A�����:";          
       if(mCheckBox1.isChecked()||mCheckBox2.isChecked()||mCheckBox3.isChecked()){
         if(mCheckBox1.isChecked())
         {
           s+="�q���B";
         }
         if(mCheckBox2.isChecked())
         {
           s+="����B";
         }
         if(mCheckBox3.isChecked())
         {
           s+="�H�Υd";
         }
       }else{
         s="�����!!";    
       }
              
       mTextView.setText(s);
     }
    }); 
    
  mCheckBox2.setOnClickListener(new CheckBox.OnClickListener()
  {  
   @Override
    public void onClick(View v)
   {
     String s="�A�����:";          
     if(mCheckBox1.isChecked()||mCheckBox2.isChecked()||mCheckBox3.isChecked()){
       if(mCheckBox1.isChecked())
       {
         s+="�q���B";
       }
       if(mCheckBox2.isChecked())
       {
         s+="����B";
       }
       if(mCheckBox3.isChecked())
       {
         s+="�H�Υd";
       }
     }else{
       s="�����!!";    
     }       
     mTextView.setText(s);
   }
  });  
  
  mCheckBox3.setOnClickListener(new CheckBox.OnClickListener()
  {  
   @Override
    public void onClick(View v)
   {
     String s="�A�����:";          
     if(mCheckBox1.isChecked()||mCheckBox2.isChecked()||mCheckBox3.isChecked()){
       if(mCheckBox1.isChecked())
       {
         s+="�q���B";
       }
       if(mCheckBox2.isChecked())
       {
         s+="����B";
       } 
       if(mCheckBox3.isChecked())
       {
         s+="�H�Υd";
       }
     }else{
       s="�����!!";    
     }       
     mTextView.setText(s);
   }
  });  
  }
}