package irdc.ex03_09;
/* import相关class */
import android.app.Activity; 
import android.os.Bundle;
import android.content.Intent; 
import android.view.View; 
import android.widget.Button; 
public class EX03_09_1 extends Activity 
{ 
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState); 
    /* 载入mylayout.xml Layout */ 
    setContentView(R.layout.mylayout); 
    /* 以findViewById()取得Button对象，并加入onClickListener */
    Button b2 = (Button) findViewById(R.id.button2);
    b2.setOnClickListener(new Button.OnClickListener()
    { 
      public void onClick(View v)
      { 
        /* new一个Intent对象，并指定要启动的class */ 
        Intent intent = new Intent(); 
        intent.setClass(EX03_09_1.this, EX03_09.class); 
        /* 调用一个新的Activity */ startActivity(intent); 
        /* 关闭原本的Activity */ 
        EX03_09_1.this.finish();
        } 
      });
    }
  }