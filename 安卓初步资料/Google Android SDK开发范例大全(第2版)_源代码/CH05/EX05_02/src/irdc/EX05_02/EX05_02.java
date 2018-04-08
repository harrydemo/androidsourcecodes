package irdc.EX05_02;
import android.app.Activity;
import android.content.Intent;
/*��������Uri�����ʹ��Uri.parse()*/
import android.net.Uri; 
import android.os.Bundle; 
import android.view.View;
/*�������� widget.Button��������ʹ��Button����*/ 
import android.widget.Button;
import android.widget.Toast; 
/*�������� widget.EditText��������ʹ��EditText����*/ 
import android.widget.EditText;
/*�������� java.util.regex����ʹ��Regular Expression*/ 
import java.util.regex.Matcher; 
import java.util.regex.Pattern;
public class EX05_02 extends Activity 
{ 
	/*����Button��EditText��������*/
	private Button mButton1;
	private EditText mEditText1; 
	/** Called when the activity is first created. */
	@Override 
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main); 
		/*͸��findViewById������������EditText��Button����*/ 
		mEditText1 = (EditText) findViewById(R.id.myEditText1); 
		mButton1 = (Button) findViewById(R.id.myButton1);
		/*�趨Button�����OnClickListener������OnClick�¼�*/
		mButton1.setOnClickListener(new Button.OnClickListener() 
		{
			@Override 
			public void onClick(View v) 
			{ 
				try 
				{ 
					/*ȡ��EditText���û�������ַ���*/ 
					String strInput = mEditText1.getText().toString(); if (isPhoneNumberValid(strInput)==true)
					{ 
						/*����һ���µ�Intent��ִ��action.CALL�ĳ�����͸��Uri���ַ�������*/
						Intent myIntentDial = new Intent("android.intent.action.CALL",Uri.parse("tel:"+strInput)); 
						/*��startActivity()�����д����Զ����Intent������ִ�в���绰�Ĺ���*/ 
						startActivity(myIntentDial); mEditText1.setText("");
						} 
					else
					{
						mEditText1.setText(""); 
						Toast.makeText( EX05_02.this, "����ĵ绰��ʽ����", Toast.LENGTH_LONG).show();
						}
					} 
				catch(Exception e)
				{
					e.printStackTrace();
					} 
				} 
			});
		} 
	/*����ַ����Ƿ�Ϊ�绰����ķ���,���ش�true or false���ж�ֵ*/ 
	public static boolean isPhoneNumberValid(String phoneNumber) 
	{
		boolean isValid = false;
		/* �ɽ��ܵĵ绰��ʽ��: * ^\\(? : 
		 * ����ʹ�� "(" ��Ϊ��ͷ * (\\d{3}): �������������� * \\)? : 
		 * ����ʹ��")"���� * [- ]? : ��������ʽ�����ʹ�þ�ѡ���Ե� "-". * (\\d{3}) :
		 *  �ٽ������������� * [- ]? : ����ʹ�þ�ѡ���Ե� "-" ����. * (\\d{4})$: 
		 *  ���ĸ����ֽ���. * ���Աȶ��������ָ�ʽ:
		 *   * (123)456-7890, 123-456-7890, 1234567890, (123)-456-7890 */
		String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$"; 
		String expression2 ="^\\(?(\\d{2})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$"; 
		CharSequence inputStr = phoneNumber;
		/*����Pattern*/ 
		Pattern pattern = Pattern.compile(expression); 
		/*��Pattern �Բ�������Matcher��Regular expression*/
		Matcher matcher = pattern.matcher(inputStr);
		/*����Pattern2*/
		Pattern pattern2 =Pattern.compile(expression2);
		/*��Pattern2 �Բ�������Matcher2��Regular expression*/
		Matcher matcher2= pattern2.matcher(inputStr); 
		if(matcher.matches()||matcher2.matches()) 
		{ 
			isValid = true;
			}
		return isValid;
		}
	}