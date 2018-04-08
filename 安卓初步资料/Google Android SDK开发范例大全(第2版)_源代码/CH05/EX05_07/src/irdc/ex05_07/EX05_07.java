package irdc.ex05_07; 
import android.app.Activity; 
import android.os.Bundle; 
import android.text.util.Linkify; 
import android.view.View; 
import android.widget.Button; 
import android.widget.ImageView; 
import android.widget.LinearLayout;
import android.widget.TextView; 
import android.widget.Toast; 
public class EX05_07 extends Activity 
{ 
  private Button mButton01; 
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState); 
    setContentView(R.layout.main);
    mButton01 = (Button)findViewById(R.id.myButton1); 
    /*�趨Button��OnClickListener�����¼�*/
    mButton01.setOnClickListener(new Button.OnClickListener()
    { 
      @Override public void onClick(View v) 
      {
        // TODO Auto-generated method stub 
        ImageView mView01 = new ImageView(EX05_07.this); 
        TextView mTextView = new TextView(EX05_07.this);
        LinearLayout lay = new LinearLayout(EX05_07.this);
        /*�趨mTextViewȥץȡstringֵ*/ 
        mTextView.setText(R.string.app_url); 
        /*�ж�mTextView������Ϊ�Σ�����ϵͳ������*/
        Linkify.addLinks(mTextView,Linkify.WEB_URLS| Linkify.EMAIL_ADDRESSES|Linkify.PHONE_NUMBERS); 
        /*��Toast��ʽ��ʾ*/ 
        Toast toast = Toast.makeText(EX05_07.this, mTextView .getText(), Toast.LENGTH_LONG); 
        View textView = toast.getView(); lay.setOrientation(LinearLayout.HORIZONTAL);
        /*��Toast�����ͼƬ*/
        mView01.setImageResource(R.drawable.icon); 
        /*��Toast����ʾͼƬ*/ lay.addView(mView01); 
        /*��Toast����ʾ����*/ lay.addView(textView); 
        toast.setView(lay);
        toast.show(); 
        } 
      });
    } 
  }
    
   
 
