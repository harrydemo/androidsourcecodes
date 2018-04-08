package irdc.EX05_02;
import android.app.Activity;
import android.content.Intent;
/*必需引用Uri类才能使用Uri.parse()*/
import android.net.Uri; 
import android.os.Bundle; 
import android.view.View;
/*必需引用 widget.Button才能声明使用Button对象*/ 
import android.widget.Button;
import android.widget.Toast; 
/*必需引用 widget.EditText才能声明使用EditText对象*/ 
import android.widget.EditText;
/*必需引用 java.util.regex才能使用Regular Expression*/ 
import java.util.regex.Matcher; 
import java.util.regex.Pattern;
public class EX05_02 extends Activity 
{ 
	/*声明Button与EditText对象名称*/
	private Button mButton1;
	private EditText mEditText1; 
	/** Called when the activity is first created. */
	@Override 
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main); 
		/*透过findViewById建构子来建构EditText与Button对象*/ 
		mEditText1 = (EditText) findViewById(R.id.myEditText1); 
		mButton1 = (Button) findViewById(R.id.myButton1);
		/*设定Button对象的OnClickListener来聆听OnClick事件*/
		mButton1.setOnClickListener(new Button.OnClickListener() 
		{
			@Override 
			public void onClick(View v) 
			{ 
				try 
				{ 
					/*取得EditText中用户输入的字符串*/ 
					String strInput = mEditText1.getText().toString(); if (isPhoneNumberValid(strInput)==true)
					{ 
						/*建构一个新的Intent并执行action.CALL的常数与透过Uri将字符串带入*/
						Intent myIntentDial = new Intent("android.intent.action.CALL",Uri.parse("tel:"+strInput)); 
						/*在startActivity()方法中带入自定义的Intent对象以执行拨打电话的工作*/ 
						startActivity(myIntentDial); mEditText1.setText("");
						} 
					else
					{
						mEditText1.setText(""); 
						Toast.makeText( EX05_02.this, "输入的电话格式不符", Toast.LENGTH_LONG).show();
						}
					} 
				catch(Exception e)
				{
					e.printStackTrace();
					} 
				} 
			});
		} 
	/*检查字符串是否为电话号码的方法,并回传true or false的判断值*/ 
	public static boolean isPhoneNumberValid(String phoneNumber) 
	{
		boolean isValid = false;
		/* 可接受的电话格式有: * ^\\(? : 
		 * 可以使用 "(" 作为开头 * (\\d{3}): 紧接着三个数字 * \\)? : 
		 * 可以使用")"接续 * [- ]? : 在上述格式后可以使用具选择性的 "-". * (\\d{3}) :
		 *  再紧接着三个数字 * [- ]? : 可以使用具选择性的 "-" 接续. * (\\d{4})$: 
		 *  以四个数字结束. * 可以比对下列数字格式:
		 *   * (123)456-7890, 123-456-7890, 1234567890, (123)-456-7890 */
		String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$"; 
		String expression2 ="^\\(?(\\d{2})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$"; 
		CharSequence inputStr = phoneNumber;
		/*建立Pattern*/ 
		Pattern pattern = Pattern.compile(expression); 
		/*将Pattern 以参数传入Matcher作Regular expression*/
		Matcher matcher = pattern.matcher(inputStr);
		/*建立Pattern2*/
		Pattern pattern2 =Pattern.compile(expression2);
		/*将Pattern2 以参数传入Matcher2作Regular expression*/
		Matcher matcher2= pattern2.matcher(inputStr); 
		if(matcher.matches()||matcher2.matches()) 
		{ 
			isValid = true;
			}
		return isValid;
		}
	}