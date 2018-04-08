package irdc.ex05_06; 
import android.app.Activity; 
import android.app.Service;
import android.os.Bundle; 
import android.os.Vibrator; 
import android.view.View; 
import android.view.View.OnClickListener; 
import android.widget.Toast; 
import android.widget.ToggleButton;
public class EX05_06 extends Activity 
{ 
  private Vibrator mVibrator01;
  /** Called when the activity is first created. */ 
  @Override public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    /*设定ToggleButton的对象*/
    mVibrator01 = ( Vibrator )getApplication().getSystemService( Service .VIBRATOR_SERVICE ); 
    final ToggleButton mtogglebutton1 = (ToggleButton) findViewById(R.id .myTogglebutton1); 
    final ToggleButton mtogglebutton2 = (ToggleButton) findViewById(R.id .myTogglebutton2); 
    final ToggleButton mtogglebutton3 = (ToggleButton) findViewById(R.id .myTogglebutton3); 
    /*设定ToggleButton使用OnClickListener来启动事件*/
    mtogglebutton1.setOnClickListener(new OnClickListener()
    { 
      
      public void onClick(View v) 
      { 
        if (mtogglebutton1.isChecked()) 
        { 
          /*设定震动的周期*/
          mVibrator01.vibrate( new long[]{100,10,100,1000},-1); 
          /*用Toast显示震动启动*/ 
          Toast.makeText(EX05_06.this, getString(R.string.str_ok) ,Toast.LENGTH_SHORT).show();
          }
        else
        { 
          /*取消震动*/ mVibrator01.cancel(); 
          /*用Toast显示震动取消*/ 
          Toast.makeText(EX05_06.this, getString(R.string.str_end) ,Toast.LENGTH_SHORT).show();
          } 
        } 
      });
    /*设定ToggleButton使用OnClickListener来启动事件*/ 
    mtogglebutton2.setOnClickListener(new OnClickListener()
    { 
      public void onClick(View v) 
      { 
        if (mtogglebutton2.isChecked())
        {
/*设定震动的周期*/ mVibrator01.vibrate( new long[]{100,100,100,1000},0);
/*用Toast显示震动启动*/
Toast.makeText(EX05_06.this, getString(R.string.str_ok) ,Toast.LENGTH_SHORT).show(); 
}
        else
        { 
          /*取消震动*/
          mVibrator01.cancel(); 
          /*用Toast显示震动取消*/ 
          Toast.makeText(EX05_06.this, getString(R.string.str_end) ,Toast.LENGTH_SHORT).show();
          }
        } 
      });
    mtogglebutton3.setOnClickListener(new OnClickListener()
    { 
      public void onClick(View v) 
      { 
        if (mtogglebutton3.isChecked()) 
        {
          /*设定震动的周期*/
          mVibrator01.vibrate( new long[]{1000,50,1000,50,1000},0); 
          /*用Toast显示震动启动*/
          Toast.makeText(EX05_06.this, getString(R.string.str_ok) ,Toast.LENGTH_SHORT).show();
          }
        else
        { 
          /*取消震动*/ 
          mVibrator01.cancel(); 
          /*用Toast显示震动取消*/
          Toast.makeText(EX05_06.this, getString(R.string.str_end) ,Toast.LENGTH_SHORT).show();
          }
        } 
      }); 
    } 
  }