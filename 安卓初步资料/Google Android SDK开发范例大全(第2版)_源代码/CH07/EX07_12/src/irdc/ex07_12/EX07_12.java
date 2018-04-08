package irdc.ex07_12;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class EX07_12 extends Activity
{

  private TextView TextView01;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    TextView01 = (TextView) findViewById(R.id.TextView01);

    /* 取得短信传来的bundle */
    Bundle bunde = this.getIntent().getExtras();
    if (bunde != null)
    {
      String str = bunde.getString("TextView_Text");
      TextView01.setText(str);
    } else
    {
      finish();
    }

  }
}

