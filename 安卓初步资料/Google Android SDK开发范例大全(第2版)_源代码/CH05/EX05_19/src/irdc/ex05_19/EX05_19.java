package irdc.ex05_19;
import android.app.Activity; 
import android.content.Intent;
import android.os.Bundle; 
import android.view.View;
import android.widget.ImageButton; 
public class EX05_19 extends Activity
{
  private ImageButton myImageButton; 
  /** Called when the activity is first created. */
  @Override 
  public void onCreate(Bundle savedInstanceState)
  { 
    super.onCreate(savedInstanceState); 
    setContentView(R.layout.main);
    myImageButton = (ImageButton) findViewById(R.id.myImageButton);
    myImageButton.setOnClickListener(new ImageButton.OnClickListener()
{ 
      @Override 
      public void onClick(View v)
      { 
        /* 调用拨号的画面 */ 
        Intent myIntentDial = new Intent("android.intent.action.CALL_BUTTON"); 
        startActivity(myIntentDial);
        }
      });
    } 
  }