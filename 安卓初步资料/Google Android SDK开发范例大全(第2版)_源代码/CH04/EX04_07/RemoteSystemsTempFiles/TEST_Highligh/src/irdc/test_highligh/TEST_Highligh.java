package irdc.test_highligh;


import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class TEST_Highligh extends Activity {
  
  private OnCheckedChangeListener BasicCheckListener;  
  
  
  
    /** Called when the activity is first created. */
    @Override
  public void onCreate(Bundle savedInstanceState)
    {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    CheckBox[] setOfCheckBoxes = new CheckBox[]
     {
        (CheckBox)findViewById(R.id.mCheckBox1),
        (CheckBox)findViewById(R.id.mCheckBox2)
     };
    
      
    BasicCheckListener = new OnCheckedChangeListener()
    {
     
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
      {
        // TODO Auto-generated method stub
        if(isChecked)
        {
          buttonView.setBackgroundResource(R.color.highlightedColor);
        } else {
          buttonView.setBackgroundColor(android.R.drawable.screen_background_dark);
        }
      }      
    };
    for(int i = 0; i < setOfCheckBoxes.length; i++){
      setOfCheckBoxes[i].setOnCheckedChangeListener(BasicCheckListener);
    }     
 }
}