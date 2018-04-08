package irdc.ex03_16;

import irdc.ex03_16.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EX03_16 extends Activity {
    /** Called when the activity is first created. */
	
	public Button mButton2;
	public Button mButton3;
	public Button mButton4;
	public Button mButton5;
	public EditText mEditText1;
	public EditText mEditText2;
	public TextView mTextView2;
	public TextView mTextView4;
   
	@Override
    public void onCreate(Bundle savedInstanceState) 
{
     super.onCreate(savedInstanceState);
     setContentView(R.layout.main);
  
     mTextView2 = (TextView) findViewById(R.id.mTextView2);
     mTextView4 = (TextView) findViewById(R.id.mTextView4);
     mButton2 = (Button) findViewById(R.id.mButton2);
     mButton3 = (Button) findViewById(R.id.mButton3);
     mButton4 = (Button) findViewById(R.id.mButton4);
     mButton5 = (Button) findViewById(R.id.mButton5);
     mEditText1 = (EditText) findViewById(R.id.mText1);
     mEditText2 = (EditText) findViewById(R.id.mText2);
     
     mButton2.setOnClickListener(new Button.OnClickListener()
  {  
     @Override
     public void onClick(View v)
    {
       mTextView2.setText("+");
       String strRet = Float.toString( Float.parseFloat(mEditText1.
    		   getText().toString())+ Float.parseFloat
    		   (mEditText2.getText().toString()) ); 
       mTextView4.setText(strRet);
       
    }
    });
     
     mButton3.setOnClickListener(new Button.OnClickListener()
  {  
        @Override
        public void onClick(View v)
       {
          mTextView2.setText("-");
          String strRet = Float.toString( Float.parseFloat(mEditText1.
       		   getText().toString())- Float.parseFloat
       		   (mEditText2.getText().toString()) ); 
          mTextView4.setText(strRet);
          
       }
  });
     
     mButton4.setOnClickListener(new Button.OnClickListener()
  {  
        @Override
        public void onClick(View v)
       {
          mTextView2.setText("*");
          String strRet = Float.toString( Float.parseFloat(mEditText1.
       		   getText().toString())* Float.parseFloat
       		   (mEditText2.getText().toString()) ); 
          mTextView4.setText(strRet);
          
       }
  });
     
     mButton5.setOnClickListener(new Button.OnClickListener()
  {  
        @Override
        public void onClick(View v)
       {
          mTextView2.setText("/");
          String strRet = Float.toString( Float.parseFloat(mEditText1.
       		   getText().toString())/ Float.parseFloat
       		   (mEditText2.getText().toString()) ); 
          mTextView4.setText(strRet);
          
       }
  });   
     
    
  }
}	