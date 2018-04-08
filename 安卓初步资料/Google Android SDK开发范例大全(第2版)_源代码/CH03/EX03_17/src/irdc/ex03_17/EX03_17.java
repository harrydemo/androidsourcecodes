package irdc.ex03_17;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class EX03_17 extends Activity
{
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
  }
  
  public boolean onCreateOptionsMenu(Menu menu)
  {
    menu.add(0, 0, 0, R.string.app_about);
    menu.add(0, 1, 1, R.string.str_exit);
    return super.onCreateOptionsMenu(menu);
  }
  
  public boolean onOptionsItemSelected(MenuItem item)
  {
    super.onOptionsItemSelected(item);
    switch(item.getItemId())
    {
      case 0:
        openOptionsDialog();
        break;
      case 1:
        finish();
        break;
    }
    return true;
  }
  
  private void openOptionsDialog()
  {
    new AlertDialog.Builder(this)
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
}