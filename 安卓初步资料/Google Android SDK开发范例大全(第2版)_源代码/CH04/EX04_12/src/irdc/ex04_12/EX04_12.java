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
    /* ����main.xml Layout */ 
    setContentView(R.layout.main); 
    /* ��findViewById()ȡ��TextView��ImageButton���� */
    myTextView = (TextView) findViewById(R.id.myTextView);
    myImageButton_1 = (ImageButton) findViewById(R.id.myImageButton_1);
    myImageButton_2 = (ImageButton) findViewById(R.id.myImageButton_2);
    /* myImageButton_1����OnClickListener */ 
    myImageButton_1.setOnClickListener(new Button.OnClickListener() 
    { 
      public void onClick(View v) 
      { 
        myTextView.setText("�㰴�µ���myImageButton_1"); 
        /* ����myImageButton_1ʱ��myImageButton_1ͼƬ�û���p3ͼƬ */
        myImageButton_1.setImageDrawable(getResources().getDrawable( R.drawable.p3)); 
        /* ����myImageButton_1ʱ��myImageButton_2ͼƬ�û���p2ͼƬ */
        myImageButton_2.setImageDrawable(getResources().getDrawable( R.drawable.p2)); 
        } 
      }); 
    /* myImageButton_2����OnClickListener */ 
    myImageButton_2.setOnClickListener(new Button.OnClickListener() 
    { 
      public void onClick(View v)
    {
      myTextView.setText("�㰴�µ���myImageButton_2"); 
      /* ����myImageButton_2ʱ��myImageButton_1ͼƬ�û���p1ͼƬ */
      myImageButton_1.setImageDrawable(getResources().getDrawable( R.drawable.p1));
      /* ����myImageButton_2ʱ��myImageButton_2ͼƬ�û���p3ͼƬ */ 
      myImageButton_2.setImageDrawable(getResources().getDrawable( R.drawable.p3));
      }
      }); 
    } 
  }