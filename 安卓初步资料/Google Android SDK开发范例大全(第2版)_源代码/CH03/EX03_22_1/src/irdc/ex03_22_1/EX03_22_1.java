package irdc.ex03_22_1;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;

public class EX03_22_1 extends Activity {
  private EditText et;
  private CheckBox cb;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    /* 加载main.xml Layout */
    setContentView(R.layout.main);
    /* findViewById()取得对象 */
    et=(EditText)findViewById(R.id.mPassword);
    cb=(CheckBox)findViewById(R.id.mCheck);
    /* 设定CheckBox的OnCheckedChangeListener */
    cb.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener()
    {
      @Override
      public void onCheckedChanged(CompoundButton arg0, boolean arg1)
      {
        if(cb.isChecked())
        {
          /* 设定EditText的内容为可见的 */
          et.setTransformationMethod(
              HideReturnsTransformationMethod.getInstance());
        }
        else
        {
          /* 设定EditText的内容为隐藏的 */
          et.setTransformationMethod(
              PasswordTransformationMethod.getInstance());
        }
      }
    });
  }
}

