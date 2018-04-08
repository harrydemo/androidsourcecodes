package com.ldci.t56.mobile.safe;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;

import com.ldci.t56.mobile.handler.PhoneNumberXml;
import com.ldci.t56.mobile.tool.BroadCastTool;

public class MainActivity extends Activity implements OnClickListener {
	/** ��Ա���� */
	public static boolean[] isCheckBoxChecked = new boolean[9];
	public static boolean[] isRadioButtonChecked = new boolean[4];
	public static String mUndisturbedContent = "�ر�";
	public static String mStartTime = "23:00";
	public static String mEndTime = "07:00";
	private ImageButton mMenu_Message, mMenu_Call, mMenu_Phone, mMenu_State,
			mMenu_Smart, mMenu_Set;
	private SlidingDrawer mSlidingDrawer;
	private ImageView mImageView;
	private Intent mIntent;
	private Intent mServiceIntent;
	public static final String MESSAGE_RUBBISH_TABLE_NAME = "message_rubbish_table";
	EditText mInputPhoneNumber;
	String result;
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mEditor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("MainActivity", "onCreate");
		mServiceIntent = new Intent(BroadCastTool.AUTO_START_SERVICE);
		confirmPw();
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d("MainActivity", "onStart");
	}

	@Override
	protected void onResume() {
		Log.d("MainActivity", "onResume");
		super.onResume();
	}

	//���Ӽ��̵ķ��ؼ�
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_BACK){
			new AlertDialog.Builder(MainActivity.this).setTitle("��ʾ").setMessage(
			"��ȷ��Ҫ�˳���?").setPositiveButton("ȷ��",
			new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface arg0, int arg1) {
					Intent mIntent = new Intent(BroadCastTool.AUTO_START_SERVICE);
					stopService(mIntent);
					MainActivity.this.finish();
				}

			}).setNegativeButton("ȡ��", null).show();
			return false;
		}else{
			return super.onKeyDown(keyCode, event);
		}
	}
	
	@Override
	protected void onStop() {
		Log.d("MainActivity", "onStop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d("MainActivity", "onDestroy");
		startService(mServiceIntent);
	}

	/**���������뱣��*/
	private void confirmPw(){
		mSharedPreferences = this.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
		boolean isPasswordProtected = mSharedPreferences.getBoolean("isPasswordProtected", false);
		if(isPasswordProtected){
			LayoutInflater mLI = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final RelativeLayout mRL = (RelativeLayout)mLI.inflate(R.layout.protected_password_confirm, null);
			final EditText mEnterPw = (EditText)mRL.findViewById(R.id.protected_enter_pw);
			final TextView mForgetPw = (TextView)mRL.findViewById(R.id.protected_forget_pw);
			new AlertDialog.Builder(MainActivity.this).setTitle("���뱣��").setView(mRL)
			.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface arg0, int arg1) {
					mSharedPreferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
					String mPw = mSharedPreferences.getString("mPW", "");
					if(!mPw.equals(mEnterPw.getText().toString())){
						Toast.makeText(MainActivity.this, "�������!", Toast.LENGTH_LONG).show();
						confirmPw();
					}else{
						initContentView();
					}
				}
			}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface arg0, int arg1) {
					MainActivity.this.finish();
				}
			}).setOnCancelListener(new OnCancelListener(){

				public void onCancel(DialogInterface arg0) {
					MainActivity.this.finish();
				}
				
			}).show();	
			mForgetPw.setOnClickListener(new OnClickListener(){//�ԡ��������롱�ļ���

				public void onClick(View arg0) {
					getBackPwByQuestion();
				}
				
			});
		}else{
			initContentView();
		}
	}
	
	/**��������ȷ������δ�������뱣��ʱ�ĳ�ʼ������*/
	private void initContentView(){
		startService(mServiceIntent);
		setContentView(R.layout.main);
		initView();
		initListener();
		initSlidingListener();
		stopService(mServiceIntent);
	}
	
	/**���ܱ�����ȡ������*/
	private void getBackPwByQuestion(){
		LayoutInflater mLI = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout mLL = (LinearLayout)mLI.inflate(R.layout.getback_pw_by_question, null);
		final EditText mQuestion = (EditText)mLL.findViewById(R.id.getback_pw_question);
		final EditText mAnswer = (EditText)mLL.findViewById(R.id.getback_pw_answer);
		new AlertDialog.Builder(MainActivity.this).setTitle("ȡ������").setView(mLL)
		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface arg0, int arg1) {
				mSharedPreferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
				String mQuestionStr = mSharedPreferences.getString("mPW_Question", "");
				String mAnswerStr = mSharedPreferences.getString("mPW_Answer", "");
				if(mQuestionStr.equals(mQuestion.getText().toString())&&mAnswerStr.equals(mAnswer.getText().toString())){
					new AlertDialog.Builder(MainActivity.this).setMessage("��ϲ�㣬���������� "+mSharedPreferences.getString("mPW", "")+" ��סŶ!").show();
				}else{
					new AlertDialog.Builder(MainActivity.this).setMessage("����������!").show();
				}
			}
		}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface arg0, int arg1) {
				
			}
		}).show();	
	}
	
	/** ��ʼ��������� */
	private void initView() {
		mMenu_Message = (ImageButton) findViewById(R.id.ImageView01);
		mMenu_Call = (ImageButton) findViewById(R.id.ImageView02);
		mMenu_Phone = (ImageButton) findViewById(R.id.ImageView03);
		mMenu_State = (ImageButton) findViewById(R.id.ImageView04);
		mMenu_Smart = (ImageButton) findViewById(R.id.ImageView05);
		mMenu_Set = (ImageButton) findViewById(R.id.ImageView06);
		mSlidingDrawer = (SlidingDrawer) findViewById(R.id.menu_sliding);
		mImageView = (ImageView) findViewById(R.id.sliding_image);
		// mTextView = (TextView) findViewById(R.id.menu_title);
	}

	/** ע������� */
	private void initListener() {
		mMenu_Message.setOnClickListener(this);
		mMenu_Call.setOnClickListener(this);
		mMenu_Phone.setOnClickListener(this);
		mMenu_State.setOnClickListener(this);
		mMenu_Smart.setOnClickListener(this);
		mMenu_Set.setOnClickListener(this);
	}

	/** SlidingDrawer�Ŀ����¼����� */
	private void initSlidingListener() {
		mSlidingDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			public void onDrawerOpened() {
				mImageView.setImageResource(R.drawable.spliding_close);
			}
		});
		mSlidingDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {
			public void onDrawerClosed() {
				mImageView.setImageResource(R.drawable.spliding_open);
			}
		});
	}

	/** ����¼� */
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ImageView01:
			mIntent = new Intent(MainActivity.this, MessageActivity.class);
			MainActivity.this.startActivity(mIntent);
			break;
		case R.id.ImageView02:
			mIntent = new Intent(MainActivity.this, CallActivity.class);
			MainActivity.this.startActivity(mIntent);
			break;
		case R.id.ImageView03:
			phoneAera();
			break;
		case R.id.ImageView04:
			initChangeAudioDialog();
			break;
		case R.id.ImageView05:
			smartSet();
			break;
		case R.id.ImageView06:
			mIntent = new Intent(MainActivity.this, SetActivity.class);
			MainActivity.this.startActivity(mIntent);
			break;
		}

	}

	/**��������ز�ѯ*/
	private void phoneAera(){
		LinearLayout mInputNumber = (LinearLayout) getLayoutInflater().inflate(R.layout.searchphone, null);
		mInputPhoneNumber = (EditText)mInputNumber.findViewById(R.id.phonenumber);
		new AlertDialog.Builder(MainActivity.this).setTitle("�����ѯ")
		.setIcon(getResources().getDrawable(R.drawable.menu_phone_press)).setView(mInputNumber)
			.setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
							HttpResponse mHttpResponse = null;
							try {
								HttpGet mHttpGet = new HttpGet("http://www.webxml.com.cn/WebServices/MobileCodeWS.asmx/getMobileCodeInfo?mobileCode="+mInputPhoneNumber.getText().toString()+"&userID=");
								mHttpResponse = new DefaultHttpClient().execute(mHttpGet);
								new AlertDialog.Builder(MainActivity.this).setMessage("StatusCode is "+mHttpResponse.getStatusLine().getStatusCode()).show();
								if(mHttpResponse.getStatusLine().getStatusCode()==200){
									result = EntityUtils.toString(mHttpResponse.getEntity());
									Log.d("phonenumber","result is "+result);
									SAXParserFactory mSAXParserFactory = SAXParserFactory.newInstance();
									SAXParser mSAXParser = mSAXParserFactory.newSAXParser();
									XMLReader mXMLReader = mSAXParser.getXMLReader();
									PhoneNumberXml mPhoneNumberXml = new PhoneNumberXml();
									mXMLReader.setContentHandler(mPhoneNumberXml);
									StringReader mStringReader = new StringReader(result);
									InputSource mInputSource = new InputSource(mStringReader);
									mXMLReader.parse(mInputSource);
									new AlertDialog.Builder(MainActivity.this).setMessage(PhoneNumberXml.mPhoneNumberStr).create().show();
								}else{
									Toast.makeText(MainActivity.this, "�������Ӵ���!", Toast.LENGTH_LONG).show();
								}
								
							} catch (ClientProtocolException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							} catch (ParserConfigurationException e) {
								e.printStackTrace();
							} catch (SAXException e) {
								e.printStackTrace();
							}
					}

				}).setNegativeButton("ȡ��",null).show();
	}
	
	/** �����շ����� */
	private void smartSet() {
		RelativeLayout intellectLayout = (RelativeLayout) getLayoutInflater()
				.inflate(R.layout.intellect, null);
		final SharedPreferences settings = getSharedPreferences("DEMO",
				MODE_PRIVATE);
		// ������ý�������
		mCheckbox01 = (CheckBox) intellectLayout
				.findViewById(R.id.intellect_checbox_1);
		mCheckbox02 = (CheckBox) intellectLayout
				.findViewById(R.id.intellect_checbox_2);
		mCheckbox03 = (CheckBox) intellectLayout
				.findViewById(R.id.intellect_checbox_3);
		mEditText01 = (EditText) intellectLayout
				.findViewById(R.id.intellect_edittext01);
		mEditText02 = (EditText) intellectLayout
				.findViewById(R.id.intellect_edittext02);
		// ���ؽ����ԭʼ����
		if (settings.getBoolean("bool01", false)) {
			mCheckbox01.setChecked(true);
			mEditText01.setVisibility(View.VISIBLE);
			mEditText01.setText(settings.getString("smartE01", ""));
		}
		if (settings.getBoolean("bool02", false)) {
			mCheckbox02.setChecked(true);
			mEditText02.setVisibility(View.VISIBLE);
			mEditText02.setText(settings.getString("smartE02", ""));
		}
		if (settings.getBoolean("bool03", false)) {
			mCheckbox03.setChecked(true);
		}
		// ��checkbox�Ӽ���
		mCheckbox01.setOnCheckedChangeListener(new Checklistenner());
		mCheckbox02.setOnCheckedChangeListener(new Checklistenner());
		mCheckbox03.setOnCheckedChangeListener(new Checklistenner());

		new AlertDialog.Builder(this)
				.setTitle("�����շ�����")
				.setIcon(getResources().getDrawable(R.drawable.menu_smart_press))
				.setView(intellectLayout)
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						SharedPreferences.Editor sedit = settings.edit();
						// String use = settings.getString("username", "");
						// String passw = settings.getString("password", "");
						if (mCheckbox01.isChecked()) {
							boolean bool01 = true;
							sedit.putBoolean("bool01", bool01).commit();
							sedit.putString("smartE01",
									mEditText01.getText().toString()).commit();
						} else {
							sedit.remove("bool01").commit();
							sedit.remove("smartE01").commit();
						}
						if (mCheckbox02.isChecked()) {
							boolean bool02 = true;
							sedit.putBoolean("bool02", bool02).commit();
							sedit.putString("smartE02",
									mEditText02.getText().toString()).commit();
						} else {
							sedit.remove("bool02").commit();
							sedit.remove("smartE02").commit();
						}
						if (mCheckbox03.isChecked()) {
							boolean bool03 = true;
							sedit.putBoolean("bool03", bool03).commit();
						} else {
							sedit.remove("bool03").commit();
						}
					}
				}).setNegativeButton("ȡ��", null).show();
	}

	private CheckBox mCheckbox01;
	private CheckBox mCheckbox02;
	private CheckBox mCheckbox03;
	private EditText mEditText01;
	private EditText mEditText02;

	/**
	 * �����շ�checkbox�ļ�����
	 * 
	 * @author yuli
	 */
	class Checklistenner implements OnCheckedChangeListener {
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (!mCheckbox01.isChecked()) {
				mEditText01.setVisibility(View.GONE);
			} else {
				mEditText01.setVisibility(View.VISIBLE);
			}
			if (!mCheckbox02.isChecked()) {
				mEditText02.setVisibility(View.GONE);
			} else {
				mEditText02.setVisibility(View.VISIBLE);
			}
			if (!mCheckbox03.isChecked()) {
				Toast.makeText(MainActivity.this, "�ر��˵绰����", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(MainActivity.this, "�����˵绰����", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	/**�����龰ģʽ�ĵ�����*/
	private void initChangeAudioDialog(){
		LinearLayout mLL = (LinearLayout) getLayoutInflater().inflate(R.layout.main_audio_change, null);
		final EditText mQJ_MS = (EditText)mLL.findViewById(R.id.edit_qingjing_ms);
		final EditText mQJ_JY = (EditText)mLL.findViewById(R.id.edit_qingjing_jy);
		final EditText mQJ_ZD = (EditText)mLL.findViewById(R.id.edit_qingjing_zd);
		final EditText mQJ_XL = (EditText)mLL.findViewById(R.id.edit_qingjing_xl);
		mSharedPreferences = getSharedPreferences("QJMO",MODE_PRIVATE);
		mQJ_MS.setText(mSharedPreferences.getString("mQJ_MS", ""));
		mQJ_JY.setText(mSharedPreferences.getString("mQJ_JY", ""));
		mQJ_ZD.setText(mSharedPreferences.getString("mQJ_ZD", ""));
		mQJ_XL.setText(mSharedPreferences.getString("mQJ_XL", ""));
		new AlertDialog.Builder(this)
		.setTitle("�龰ģʽ����")
		.setIcon(getResources().getDrawable(R.drawable.menu_state_press))
		.setMessage("���͸�ʽ���龰ָ��+��Ӧָ����龰ָ��Ϊqjzl������ָ��Ϊjy�����͸�ʽΪqjzl+jy")
		.setView(mLL)
		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String mMS = mQJ_MS.getText().toString();
				String mJY = mQJ_JY.getText().toString();
				String mZD = mQJ_ZD.getText().toString();
				String mXL = mQJ_XL.getText().toString();
				if(mMS.length() == 0||mJY.length() == 0||mZD.length() == 0||mXL.length() == 0){
					Toast.makeText(getApplicationContext(), "��������ע���κ�һ���Ϊ��", Toast.LENGTH_SHORT).show();
				}else{
					mSharedPreferences = getSharedPreferences("QJMO",MODE_PRIVATE);
					mEditor = mSharedPreferences.edit();
					mEditor.putString("mQJ_MS", mMS);
					mEditor.putString("mQJ_JY", mJY);
					mEditor.putString("mQJ_ZD", mZD);
					mEditor.putString("mQJ_XL", mXL);
					mEditor.commit();
				}
			}
			
		}).setNegativeButton("ȡ��", null).show();
	}

}