package irdc.ex08_16;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class EX08_16_02 extends Activity
{
  private TextView mTextView03;
  /* 中文字的间距 */
  private int intShiftPadding = 14;
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    setContentView(R.layout.loginok);
    
    DisplayMetrics dm = new DisplayMetrics(); 
    getWindowManager().getDefaultDisplay().getMetrics(dm);
    
    
    mTextView03 = (TextView)findViewById(R.id.myTextView3);
    
    mTextView03.setLayoutParams
    (
      new AbsoluteLayout.LayoutParams(intShiftPadding*mTextView03.getText().toString().length(),18,(dm.widthPixels-(intShiftPadding*mTextView03.getText().toString().length()))-10,0)
    );
    
    mTextView03.setOnClickListener(new TextView.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        // TODO Auto-generated method stub
        Intent i = new Intent();
        i.setClass(EX08_16_02.this, EX08_16.class);
        startActivity(i);
        finish();
      }
    });
  }
}

