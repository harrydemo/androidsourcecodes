package irdc.ex04_29_1;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EX04_29_1 extends Activity
{
  private Button Button01;
  private EditText EditText01;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    EditText01 = (EditText) findViewById(R.id.EditText01);
    Button01 = (Button) findViewById(R.id.Button01);
    Button01.setOnClickListener(new Button.OnClickListener()
    {

      @Override
      public void onClick(View arg0)
      {
        // TODO Auto-generated method stub
        String searchStr = "" + EditText01.getText();
        /* 取得网页搜寻的intent */
        Intent search = new Intent(Intent.ACTION_WEB_SEARCH);
        search.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        /* 放丈欲搜寻的文? */
        search.putExtra(SearchManager.QUERY, searchStr);

        final Bundle appData = getIntent().getBundleExtra(
            SearchManager.APP_DATA);
        if (appData != null)
        {
          search.putExtra(SearchManager.APP_DATA, appData);
        }
        startActivity(search);
      }

    });
  }
}

