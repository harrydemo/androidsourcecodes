package irdc.EX04_05;
import android.app.Activity; 
import android.os.Bundle; 
import android.widget.CheckBox; 
import android.widget.CompoundButton; 
import android.widget.TextView; 
public class EX04_05 extends Activity 
{ 
  /*声明对象变量*/ 
  private TextView mTextView1;
  private CheckBox mCheckBox1; 
  private CheckBox mCheckBox2;
  private CheckBox mCheckBox3;
  /** Called when the activity is first created. */
  @Override 
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main); 
    /*透过findViewById取得TextView对象并调整文字内容*/
    mTextView1 = (TextView) findViewById(R.id.myTextView1);
    mTextView1.setText("你所选择的项目有: ");
    /*透过findViewById取得三个CheckBox对象*/ 
    mCheckBox1=(CheckBox)findViewById(R.id.myCheckBox1); 
    mCheckBox2=(CheckBox)findViewById(R.id.myCheckBox2); 
    mCheckBox3=(CheckBox)findViewById(R.id.myCheckBox3);
    /*设定OnCheckedChangeListener给三个CheckBox对象*/
    mCheckBox1.setOnCheckedChangeListener(mCheckBoxChanged); 
    mCheckBox2.setOnCheckedChangeListener(mCheckBoxChanged); 
    mCheckBox3.setOnCheckedChangeListener(mCheckBoxChanged);
    }
  /*声明并建构onCheckedChangeListener对象*/ 
  private CheckBox.OnCheckedChangeListener mCheckBoxChanged = 
    new CheckBox.OnCheckedChangeListener() 
  { 
    /*implement onCheckedChanged方法*/
    @Override 
    public void onCheckedChanged( 
        CompoundButton buttonView, boolean isChecked) {
      // TODO Auto-generated method stub 
      /*透过getString()取得CheckBox的文字字符串*/ 
      String str0="所选的项目为: "; 
      String str1=getString(R.string.str_checkbox1); 
      String str2=getString(R.string.str_checkbox2); 
      String str3=getString(R.string.str_checkbox3);
      String plus=";";
      String result="但是超过预算啰!!"; 
      String result2="还可以再多买几本喔!!"; 
      /*任一CheckBox被勾选后,该CheckBox的文字会改变TextView的文字内容 * 三个对象总共八种情境*/
      if(mCheckBox1.isChecked()==true & mCheckBox2.isChecked()==true & mCheckBox3.isChecked()==true) 
      {
        mTextView1.setText(str0+str1+plus+str2+plus+str3+result);
        } 
      else if(mCheckBox1.isChecked()==false & mCheckBox2.isChecked()==true & mCheckBox3.isChecked()==true) 
        {
          mTextView1.setText(str0+str2+plus+str3+result);
          } 
        else if(mCheckBox1.isChecked()==true & mCheckBox2.isChecked()==false & mCheckBox3.isChecked()==true) 
          { 
            mTextView1.setText(str0+str1+plus+str3+result); 
            } 
          else  if(mCheckBox1.isChecked()==true & mCheckBox2.isChecked()==true & mCheckBox3.isChecked()==false) 
            { 
              mTextView1.setText(str0+str1+plus+str2+result); } 
            else if(mCheckBox1.isChecked()==false & mCheckBox2.isChecked()==false & mCheckBox3.isChecked()==true) 
              {
                mTextView1.setText(str0+str3+plus+result2); 
                } 
              else if(mCheckBox1.isChecked()==false & mCheckBox2.isChecked()==true & mCheckBox3.isChecked()==false) 
                { 
                  mTextView1.setText(str0+str2); 
                  } 
                else if(mCheckBox1.isChecked()==true & mCheckBox2.isChecked()==false & mCheckBox3.isChecked()==false) 
                  { mTextView1.setText(str0+str1); } 
                  else if(mCheckBox1.isChecked()==false & mCheckBox2.isChecked()==false & mCheckBox3.isChecked()==false) 
                  { mTextView1.setText(str0); 
                  }
      }
      };
      }






