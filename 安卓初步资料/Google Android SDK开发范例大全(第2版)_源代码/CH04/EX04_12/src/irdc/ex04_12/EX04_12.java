package irdc.ex04_12; 
import android.app.Activity;
import android.os.Bundle; 
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView; 
public class EX04_12 extends Activity 
{ 
  TextView myTextView; 
  ImageButton myImageButton_1; 
  ImageButton myImageButton_2; 
  /** Called when the activity is first created. */
  @Override 
  public void onCreate(Bundle savedInstanceState)
  { 
    super.onCreate(savedInstanceState); 
    /* 加载main.xml Layout */ 
    setContentView(R.layout.main); 
    /* 以findViewById()取得TextView及ImageButton对象 */
    myTextView = (TextView) findViewById(R.id.myTextView);
    myImageButton_1 = (ImageButton) findViewById(R.id.myImageButton_1);
    myImageButton_2 = (ImageButton) findViewById(R.id.myImageButton_2);
    /* myImageButton_1加入OnClickListener */ 
    myImageButton_1.setOnClickListener(new Button.OnClickListener() 
    { 
      public void onClick(View v) 
      { 
        myTextView.setText("你按下的是myImageButton_1"); 
        /* 按下myImageButton_1时将myImageButton_1图片置换成p3图片 */
        myImageButton_1.setImageDrawable(getResources().getDrawable( R.drawable.p3)); 
        /* 按下myImageButton_1时将myImageButton_2图片置换成p2图片 */
        myImageButton_2.setImageDrawable(getResources().getDrawable( R.drawable.p2)); 
        } 
      }); 
    /* myImageButton_2加入OnClickListener */ 
    myImageButton_2.setOnClickListener(new Button.OnClickListener() 
    { 
      public void onClick(View v)
    {
      myTextView.setText("你按下的是myImageButton_2"); 
      /* 按下myImageButton_2时将myImageButton_1图片置换成p1图片 */
      myImageButton_1.setImageDrawable(getResources().getDrawable( R.drawable.p1));
      /* 按下myImageButton_2时将myImageButton_2图片置换成p3图片 */ 
      myImageButton_2.setImageDrawable(getResources().getDrawable( R.drawable.p3));
      }
      }); 
    } 
  }