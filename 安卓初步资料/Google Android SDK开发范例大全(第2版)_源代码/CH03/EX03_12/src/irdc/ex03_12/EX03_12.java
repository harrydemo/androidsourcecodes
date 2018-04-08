package irdc.ex03_12;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EX03_12 extends Activity 
{
  private Button mButton1;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    mButton1 = (Button) findViewById(R.id.myButton1);
    mButton1.setOnClickListener(new Button.OnClickListener()
    {

      @Override
      public void onClick(View v)
      {
        // TODO Auto-generated method stub
        new AlertDialog.Builder(EX03_12.this)
        .setTitle(R.string.app_about)
        .setMessage(R.string.app_about_msg)
        .setPositiveButton(R.string.str_ok,
           new DialogInterface.OnClickListener()
            {
           public void onClick(DialogInterface dialoginterface, int i)
             {
             }
             }
            )
        .show();
      }      
    });
  }
}