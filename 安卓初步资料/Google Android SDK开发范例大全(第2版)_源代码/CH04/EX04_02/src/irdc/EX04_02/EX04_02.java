package irdc.EX04_02; 
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
/*使用OnClickListener与OnFocusChangeListener来区隔按钮的状态*/ 
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener; 
import android.widget.Button; 
import android.widget.ImageButton; 
import android.widget.TextView; 
public class EX04_02 extends Activity
{ 
  /*宣告三个对象变量(图片按钮,按钮,与TextView)*/ 
  private ImageButton mImageButton1;
  private Button mButton1;
  private TextView mTextView1;
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState)
  { 
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main); 
    /*透过findViewById建构三个对象*/ 
    mImageButton1 =(ImageButton) findViewById(R.id.myImageButton1); 
    mButton1=(Button)findViewById(R.id.myButton1); 
    mTextView1 = (TextView) findViewById(R.id.myTextView1);
    /*透过OnFocusChangeListener来响应ImageButton的onFous事件*/ 
    mImageButton1.setOnFocusChangeListener(new OnFocusChangeListener()
    { 
      public void onFocusChange(View arg0, boolean isFocused) 
      { 
        // TODO Auto-generated method stub
        /*若ImageButton状态为onFocus改变ImageButton的图片 * 并改变textView的文字*/ 
        if (isFocused==true) 
        { 
          mTextView1.setText("图片按钮状态为:Got Focus"); 
          mImageButton1.setImageResource(R.drawable.iconfull);
          } 
        /*若ImageButton状态为offFocus改变ImageButton的图片 *并改变textView的文字*/ 
        else
        { 
          mTextView1.setText("图片按钮状态为:Lost Focus"); 
          mImageButton1.setImageResource(R.drawable.iconempty); 
          } 
        } 
      }
    ); 
    /*透过onClickListener来响应ImageButton的onClick事件*/ 
    mImageButton1.setOnClickListener(new OnClickListener()
    { 
      public void onClick(View v) 
      {
        // TODO Auto-generated method stub 
        /*若ImageButton状态为onClick改变ImageButton的图片 * 并改变textView的文字*/ 
        mTextView1.setText("图片按钮状态为:Got Click");
        mImageButton1.setImageResource(R.drawable.iconfull);
        }
      } 
    );
    /*透过onClickListener来响应Button的onClick事件*/ 
    mButton1.setOnClickListener(new OnClickListener() 
    {
      

    public void onClick(View v)
    { 
      // TODO Auto-generated method stub 
      /*若Button状态为onClick改变ImageButton的图片 * 并改变textView的文字*/
      mTextView1.setText("图片按钮状态为:Lost Focus");
      mImageButton1.setImageResource(R.drawable.iconempty); 
      }
    } 
    ); 
    } 
  }



   
    
 


