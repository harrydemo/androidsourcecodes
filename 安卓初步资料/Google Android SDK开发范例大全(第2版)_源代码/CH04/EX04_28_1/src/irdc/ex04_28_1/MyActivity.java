package irdc.ex04_28_1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MyActivity extends Activity
{
  EditText EditText01;
  Button Button02;
  public static String text = "";

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    // TODO Auto-generated method stub

    super.onCreate(savedInstanceState);
    setContentView(R.layout.config);
    EditText01 = (EditText) findViewById(R.id.EditText01);
    Button Button02 = (Button) findViewById(R.id.Button02);

    Button02.setOnClickListener(new Button.OnClickListener()
    {

      @Override
      public void onClick(View arg0)
      {
        // TODO Auto-generated method stub
        final Context context = MyActivity.this;
        context
            .startService(new Intent(context, EX04_28_1.UpdateService.class));
        text = "" + EditText01.getText();
        Intent resultValue = new Intent();
        setResult(RESULT_OK, resultValue);
        finish();
      }
    });
  }

}
