package com.ldci.t56.mobile.safe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SetActivity extends ListActivity {
	
	private Intent mIntent;
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mEditor;
	Calendar mCalendar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("debug", "onCreate");
		mCalendar = Calendar.getInstance();
		/*��ȡ��SharedPreferences����������ļ�����*/
		mSharedPreferences = this.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
		MainActivity.mUndisturbedContent = mSharedPreferences.getString("UndisturbedMode", MainActivity.mUndisturbedContent);
		
		mCalendar.setTimeInMillis(mSharedPreferences.getLong("UndisturbedStartTime", System.currentTimeMillis()));
		int hourS = mCalendar.get(Calendar.HOUR_OF_DAY);
		int minuteS = mCalendar.get(Calendar.MINUTE);
		MainActivity.mStartTime = (hourS < 10?"0"+hourS:hourS)+":"+(minuteS < 10?"0"+minuteS:minuteS);
		
		mCalendar.setTimeInMillis(mSharedPreferences.getLong("UndisturbedEndTime", System.currentTimeMillis()+60000000L));
		int hourE = mCalendar.get(Calendar.HOUR_OF_DAY);
		int minuteE = mCalendar.get(Calendar.MINUTE);
		MainActivity.mEndTime = (hourE < 10?"0"+hourE:hourE)+":"+(minuteE < 10?"0"+minuteE:minuteE);
		
		MainActivity.isCheckBoxChecked[3] = mSharedPreferences.getBoolean("isReceiveSMS", false);
		MainActivity.isCheckBoxChecked[4] = mSharedPreferences.getBoolean("isReceiveCall", false);
		MainActivity.isCheckBoxChecked[5] = mSharedPreferences.getBoolean("isPasswordProtected", false);
		MainActivity.isCheckBoxChecked[6] = mSharedPreferences.getBoolean("isAutoStartWithPhone", false);
		/*��ȡ������ʼ�����б�����*/
		setListAdapter(new SetAdapter(SetActivity.this));
		initListViewListener();
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		mSharedPreferences = this.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
		mEditor.putString("UndisturbedMode", MainActivity.mUndisturbedContent);
		
		int hourStart = Integer.valueOf(MainActivity.mStartTime.split(":")[0]);
		int minuteStart = Integer.valueOf(MainActivity.mStartTime.split(":")[1]);
		int hourEnd = Integer.valueOf(MainActivity.mEndTime.split(":")[0]);
		int minuteEnd = Integer.valueOf(MainActivity.mEndTime.split(":")[1]);
		
		mCalendar.setTimeInMillis(System.currentTimeMillis());
		mCalendar.set(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), hourStart, minuteStart);
		mEditor.putLong("UndisturbedStartTime", mCalendar.getTimeInMillis());
		Log.d("yejian", "UndisturbedStartTime:"+mCalendar.getTimeInMillis());
		if((hourStart == hourEnd&&minuteStart > minuteEnd)||hourStart > hourEnd){
			mCalendar.set(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)+1, hourEnd, minuteEnd);
			Log.d("yejian", "1-------"+mCalendar.getTimeInMillis());
		}else{
			mCalendar.set(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), hourEnd, minuteEnd);
			Log.d("yejian", "2-------"+mCalendar.getTimeInMillis());
		}
		mEditor.putLong("UndisturbedEndTime", mCalendar.getTimeInMillis());
		Log.d("yejian", "UndisturbedEndTime:"+mCalendar.getTimeInMillis());
		
		mEditor.putBoolean("isReceiveSMS", MainActivity.isCheckBoxChecked[3]);
		mEditor.putBoolean("isReceiveCall", MainActivity.isCheckBoxChecked[4]);
		mEditor.putBoolean("isPasswordProtected", MainActivity.isCheckBoxChecked[5]);
		mEditor.putBoolean("isAutoStartWithPhone", MainActivity.isCheckBoxChecked[6]);
		mEditor.commit();
	}
	
	
	/**ListView�ĵ���¼�����*/
	private void initListViewListener(){
		getListView().setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				CheckBox mCheckBox = (CheckBox) view.findViewById(R.id.set_checkbox);
				if(!mCheckBox.isChecked()){
					MainActivity.isCheckBoxChecked[position] = true;
					mCheckBox.setChecked(true);
				}else{
					MainActivity.isCheckBoxChecked[position] = false;
					mCheckBox.setChecked(false);
				}
				switch(position){
					case 0:
						initUnDisturbedDialog();
						break;
					case 1:
						String mStartTime[] = MainActivity.mStartTime.split(":");
						setUndisturbedTime(1,mStartTime[0],mStartTime[1]);
						break;
					case 2:
						String mEndTime[] = MainActivity.mEndTime.split(":");
						setUndisturbedTime(2,mEndTime[0],mEndTime[1]);
						break;
					case 5:
						protectDialog(mCheckBox);
						break;
					case 7:
						
						break;
					case 8:
						mIntent = new Intent(SetActivity.this, SensitiveActivity.class);
						SetActivity.this.startActivity(mIntent);
						break;
				}
			}
			
		});
	}
	
	/**���뱣��*/
	private void protectDialog(CheckBox checkbox){
		if(checkbox.isChecked()){
			LayoutInflater mLI = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final LinearLayout mLL = (LinearLayout)mLI.inflate(R.layout.set_protected, null);
			final EditText mPW_Enter = (EditText)mLL.findViewById(R.id.protected_pw_enter);
			final EditText mPW_Confirm = (EditText)mLL.findViewById(R.id.protected_pw_confirm);
			final EditText mPW_Question = (EditText)mLL.findViewById(R.id.protected_pw_question);
			final EditText mPW_Answer = (EditText)mLL.findViewById(R.id.protected_pw_answer);
			new AlertDialog.Builder(SetActivity.this).setTitle("���뱣������").setView(mLL)
			.setPositiveButton("ȷ��", new OnClickListener(){
				public void onClick(DialogInterface arg0, int arg1) {
					String pw_enter = mPW_Enter.getText().toString();
					String pw_confirm = mPW_Confirm.getText().toString();
					String pw_question = mPW_Question.getText().toString();
					String pw_answer = mPW_Answer.getText().toString();
					if(mPW_Enter.length()!=0&&mPW_Confirm.length()!=0&&mPW_Question.length()!=0&&mPW_Answer.length()!=0&&pw_enter.equals(pw_confirm)){
						mSharedPreferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
						SharedPreferences.Editor mEditor = mSharedPreferences.edit();
						mEditor.putString("mPW", pw_enter);
						mEditor.putString("mPW_Question", pw_question);
						mEditor.putString("mPW_Answer", pw_answer);
						mEditor.commit();
					}else{
						Toast.makeText(SetActivity.this, "�������!", Toast.LENGTH_LONG).show();
						canclePwProtected();
					}
				}
			}).setNegativeButton("ȡ��", new OnClickListener(){
				public void onClick(DialogInterface arg0, int arg1) {
					canclePwProtected();
				}
			}).show();	
		}
	}
	
	/**ȡ�����뱣��ѡ��״̬*/
	private void canclePwProtected(){
		MainActivity.isCheckBoxChecked[5] = false;
		setListAdapter(new SetAdapter(SetActivity.this));
	}
	
	String hourStr;
	String minuteStr;
	/**ʱ������*/
	private void setUndisturbedTime(final int start_or_end,String hour,String minute){
		
		new TimePickerDialog(SetActivity.this, new TimePickerDialog.OnTimeSetListener(){

			public void onTimeSet(TimePicker view, int hour,
					int minute) {
				String mStartTime[] = MainActivity.mStartTime.split(":");
				String mEndTime[] = MainActivity.mEndTime.split(":");
				if(start_or_end == 1){
					hourStr = (hour<10?"0"+hour:""+hour);
					minuteStr = (minute<10?"0"+minute:""+minute);
					if(hourStr.equals(mEndTime[0])&&minuteStr.equals(mEndTime[1])){
						Toast.makeText(getApplicationContext(), "��������һ����ʱ��!", Toast.LENGTH_SHORT).show();
						setUndisturbedTime(1,mStartTime[0],mStartTime[1]);
					}else{
						MainActivity.mStartTime = hourStr+":"+minuteStr;
					}
				}else{
					hourStr = (hour<10?"0"+hour:""+hour);
					minuteStr = (minute<10?"0"+minute:""+minute);
					if(mStartTime[0].equals(hourStr)&&mStartTime[1].equals(minuteStr)){
						Toast.makeText(getApplicationContext(), "��������һ����ʱ��!", Toast.LENGTH_SHORT).show();
						setUndisturbedTime(2,mEndTime[0],mEndTime[1]);
					}else{
						MainActivity.mEndTime = hourStr+":"+minuteStr;
					}
				}
				setListAdapter(new SetAdapter(SetActivity.this));
			}
			
		},Integer.valueOf(hour),Integer.valueOf(minute),true).show();
	}

	/**ҹ������ģʽѡ��򣬰������ض��ţ����ص绰�����ض��ź͵绰�Լ��ر�*/
	private void initUnDisturbedDialog(){
		LayoutInflater mLI = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout mLL = (LinearLayout)mLI.inflate(R.layout.set_undisturbed, null);
		final RadioGroup mRG = (RadioGroup)mLL.findViewById(R.id.RadioGroup01);
		final RadioButton mRB1 = (RadioButton)mLL.findViewById(R.id.RadioButton01);
		final RadioButton mRB2 = (RadioButton)mLL.findViewById(R.id.RadioButton02);
		final RadioButton mRB3 = (RadioButton)mLL.findViewById(R.id.RadioButton03);
		final RadioButton mRB4 = (RadioButton)mLL.findViewById(R.id.RadioButton04);
		RadioGroup.OnCheckedChangeListener mOCCL = new RadioGroup.OnCheckedChangeListener(){

			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				if(arg1 == mRB1.getId()){
					MainActivity.mUndisturbedContent = "���ض���";
				}else if(arg1 == mRB2.getId()){
					MainActivity.mUndisturbedContent = "���ص绰";
				}else if(arg1 == mRB3.getId()){
					MainActivity.mUndisturbedContent = "���ض��ź͵绰";
				}else if(arg1 == mRB4.getId()){
					MainActivity.mUndisturbedContent = "�ر�";
				}
			}
			
		};
		mRG.setOnCheckedChangeListener(mOCCL);
		
		new AlertDialog.Builder(this).setTitle("��ѡ��!").setView(mLL)
				.setPositiveButton("ȷ��", new OnClickListener(){
					public void onClick(DialogInterface arg0, int arg1) {
						setListAdapter(new SetAdapter(SetActivity.this));
					}
				}).show();
	}
	
	/**�Զ���������*/
	class SetAdapter extends BaseAdapter{
		
		private ArrayList<Map<String, String>> mList = new  ArrayList<Map<String, String>>();
		private Map<String, String> mMap;
		private String[] mTitle = {"ҹ������ģʽ","��ʼʱ������","����ʱ������","���ն���","�ܽӵ绰","���뱣��","��������","�Զ�IP����","���д�����"};
		private String[] mContent = {MainActivity.mUndisturbedContent,MainActivity.mStartTime,MainActivity.mEndTime,"����һ�ж���","�������κ��˵�ɧ��","�����������ȫ","��������������","IP����С����","�Զ����������дʿ�"};
		
		public SetAdapter(Context context){
			Log.d("debug", "SetAdapter");
			initListData();
		}
		
		public void initListData(){
			for(int i = 0; i != 9; i++){
				mMap = new HashMap<String, String>();
				mMap.put("set_title", mTitle[i]);
				mMap.put("set_content", mContent[i]);
				mList.add(mMap);
			}
		}
		
		public int getCount() {
			return mList.size();
		}

		public Object getItem(int position) {
			return mList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View view, ViewGroup viewgroup) {
			LayoutInflater mLI = (LayoutInflater)SetActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			RelativeLayout mLL = (RelativeLayout)mLI.inflate(R.layout.set_listview, null);
			TextView mTitle = (TextView)mLL.findViewById(R.id.set_title);
			TextView mContent = (TextView)mLL.findViewById(R.id.set_content);
			final CheckBox mCheckBox = (CheckBox)mLL.findViewById(R.id.set_checkbox);
			mTitle.setText(mList.get(position).get("set_title"));
			mContent.setText(mList.get(position).get("set_content"));
			mCheckBox.setFocusable(false);//ȡ��CheckBox�Ľ���
			mCheckBox.setEnabled(false);
			if(MainActivity.isCheckBoxChecked[position] == true){
				mCheckBox.setChecked(true);
			}else{
				mCheckBox.setChecked(false);
			}
			if(position == 0||position == 1||position == 2||position == 7||position == 8){
				mCheckBox.setVisibility(View.GONE);
			}
			return mLL;
		}
	}
	
}
