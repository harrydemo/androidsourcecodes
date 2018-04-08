package irdc.ex03_09; 
/* import���class */
import android.app.Activity;
import android.os.Bundle; 
import android.view.View;
import android.widget.Button;
import android.content.Intent;
public class EX03_09 extends Activity 
{
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState)
  { 
    super.onCreate(savedInstanceState);
    /* ����main.xml Layout */ 
    setContentView(R.layout.main);
    /* ��findViewById()ȡ��Button���󣬲�����onClickListener */
    Button b1 = (Button) findViewById(R.id.button1);
    b1.setOnClickListener(new Button.OnClickListener()
    { 
      public void onClick(View v) 
      {
        /* newһ��Intent���󣬲�ָ��Ҫ������class */ 
        Intent intent = new Intent(); 
        intent.setClass(EX03_09.this, EX03_09_1.class); 
        /* ����һ���µ�Activity */ startActivity(intent);
        /* �ر�ԭ����Activity */ EX03_09.this.finish();
        } 
      });
    } 
  }