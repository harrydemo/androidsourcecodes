package irdc.ex03_10;
/* import相关class */ 
import android.app.Activity; 
import android.content.Intent; 
import android.os.Bundle; 
import android.view.View; 
import android.widget.Button;
import android.widget.EditText; 
import android.widget.RadioButton;
public class EX03_10 extends Activity 
{ 
  /** Called when the activity is first created. */ 
  @Override
  public void onCreate(Bundle savedInstanceState)
  { 
    super.onCreate(savedInstanceState); 
    /* 载入main.xml Layout */
    setContentView(R.layout.main); 
    /* 以findViewById()取得Button对象，并加入onClickListener */ 
    Button b1 = (Button) findViewById(R.id.button1);
    b1.setOnClickListener(new Button.OnClickListener() 
    {
      public void onClick(View v) 
      {
        /*取得输入的身高*/ 
        EditText et = (EditText) findViewById(R.id.height);
        double height=Double.parseDouble(et.getText().toString()); 
        /*取得选择的性别*/ String sex=""; 
        RadioButton rb1 = (RadioButton) findViewById(R.id.sex1);
        if(rb1.isChecked()) { sex="M"; }
        else{ sex="F"; } 
        /*new一个Intent对象，并指定class*/
        Intent intent = new Intent(); 
        intent.setClass(EX03_10.this,EX03_10_1.class); 
        /*new一个Bundle对象，并将要传递的数据传入*/
        Bundle bundle = new Bundle();
        bundle.putDouble("height",height);
        bundle.putString("sex",sex); 
        /*将Bundle对象assign给Intent*/ 
        intent.putExtras(bundle); 
        /*调用Activity EX03_10_1*/ 
        startActivity(intent); 
        } 
      }); 
    }
  }