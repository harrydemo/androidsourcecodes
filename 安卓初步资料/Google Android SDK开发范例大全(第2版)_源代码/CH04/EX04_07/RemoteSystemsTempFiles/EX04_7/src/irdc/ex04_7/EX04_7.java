package irdc.ex04_7;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class EX04_7 extends Activity
{
  private ImageView mImageView01;
  private Button mButton01;
  private Button mButton02;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    mImageView01 = (ImageView)findViewById(R.id.myImageView1);
    mButton01 = (Button) findViewById(R.id.myButton1);
    mButton02 = (Button) findViewById(R.id.myButton2);
    
    mImageView01.setImageDrawable(getResources().getDrawable(R.drawable.right)); 
    
    mButton01.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View v)
     {
      mImageView01.setImageDrawable(getResources().getDrawable(R.drawable.right));
     }
   });
    
    mButton02.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View v)
     {
      mImageView01.setImageDrawable(getResources().getDrawable(R.drawable.left));
     }
   });
    
  }
}