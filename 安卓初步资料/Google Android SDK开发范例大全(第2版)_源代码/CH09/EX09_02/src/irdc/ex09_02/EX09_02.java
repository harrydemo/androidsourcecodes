package irdc.ex09_02;

import android.app.Activity;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;

public class EX09_02 extends Activity
{

  private AutoCompleteTextView myAutoCompleteTextView1;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    myAutoCompleteTextView1 = (AutoCompleteTextView) findViewById(R.id.myAutoCompleteTextView1);

    /* new一个自己实现做的BaseAdapter */
    MyAdapter adapter = new MyAdapter(this);

    myAutoCompleteTextView1.setAdapter(adapter);
  }
}

