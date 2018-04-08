package irdc.EX04_02; 
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
/*ʹ��OnClickListener��OnFocusChangeListener��������ť��״̬*/ 
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener; 
import android.widget.Button; 
import android.widget.ImageButton; 
import android.widget.TextView; 
public class EX04_02 extends Activity
{ 
  /*���������������(ͼƬ��ť,��ť,��TextView)*/ 
  private ImageButton mImageButton1;
  private Button mButton1;
  private TextView mTextView1;
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState)
  { 
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main); 
    /*͸��findViewById������������*/ 
    mImageButton1 =(ImageButton) findViewById(R.id.myImageButton1); 
    mButton1=(Button)findViewById(R.id.myButton1); 
    mTextView1 = (TextView) findViewById(R.id.myTextView1);
    /*͸��OnFocusChangeListener����ӦImageButton��onFous�¼�*/ 
    mImageButton1.setOnFocusChangeListener(new OnFocusChangeListener()
    { 
      public void onFocusChange(View arg0, boolean isFocused) 
      { 
        // TODO Auto-generated method stub
        /*��ImageButton״̬ΪonFocus�ı�ImageButton��ͼƬ * ���ı�textView������*/ 
        if (isFocused==true) 
        { 
          mTextView1.setText("ͼƬ��ť״̬Ϊ:Got Focus"); 
          mImageButton1.setImageResource(R.drawable.iconfull);
          } 
        /*��ImageButton״̬ΪoffFocus�ı�ImageButton��ͼƬ *���ı�textView������*/ 
        else
        { 
          mTextView1.setText("ͼƬ��ť״̬Ϊ:Lost Focus"); 
          mImageButton1.setImageResource(R.drawable.iconempty); 
          } 
        } 
      }
    ); 
    /*͸��onClickListener����ӦImageButton��onClick�¼�*/ 
    mImageButton1.setOnClickListener(new OnClickListener()
    { 
      public void onClick(View v) 
      {
        // TODO Auto-generated method stub 
        /*��ImageButton״̬ΪonClick�ı�ImageButton��ͼƬ * ���ı�textView������*/ 
        mTextView1.setText("ͼƬ��ť״̬Ϊ:Got Click");
        mImageButton1.setImageResource(R.drawable.iconfull);
        }
      } 
    );
    /*͸��onClickListener����ӦButton��onClick�¼�*/ 
    mButton1.setOnClickListener(new OnClickListener() 
    {
      

    public void onClick(View v)
    { 
      // TODO Auto-generated method stub 
      /*��Button״̬ΪonClick�ı�ImageButton��ͼƬ * ���ı�textView������*/
      mTextView1.setText("ͼƬ��ť״̬Ϊ:Lost Focus");
      mImageButton1.setImageResource(R.drawable.iconempty); 
      }
    } 
    ); 
    } 
  }



   
    
 


