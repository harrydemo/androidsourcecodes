package irdc.ex05_08; 
/* import���class */
import android.app.Activity;
import android.os.Bundle; 
import android.widget.Toast; 
/* ��user���Notification������ʱ����ִ�е�Activity */
public class EX05_08_1 extends Activity 
{
  @Override 
  protected void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState);
    /* ����Toast */
    Toast.makeText(EX05_08_1.this, "����ģ��MSN�л���¼״̬�ĳ���", Toast.LENGTH_SHORT ).show(); 
    finish();
    }
  }