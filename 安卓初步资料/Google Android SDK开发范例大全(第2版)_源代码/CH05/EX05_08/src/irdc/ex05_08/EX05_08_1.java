package irdc.ex05_08; 
/* import相关class */
import android.app.Activity;
import android.os.Bundle; 
import android.widget.Toast; 
/* 当user点击Notification留言条时，会执行的Activity */
public class EX05_08_1 extends Activity 
{
  @Override 
  protected void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState);
    /* 发出Toast */
    Toast.makeText(EX05_08_1.this, "这是模拟MSN切换登录状态的程序", Toast.LENGTH_SHORT ).show(); 
    finish();
    }
  }