package xujun.keepaccount.activity;

import xujun.keepaccount.R;
import xujun.keepaccount.calendar.CalendarView;
import xujun.keepaccount.dialog.CanlendarDialog;
import xujun.keepaccount.entity.AccountEnum;
import xujun.keepaccount.sql.DBHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

public class WriteAccount extends Activity 
{
	private static final int REQUEST_CALENDAR = 1;
	
	private CalendarView calendarView;
	private Spinner spinner;
	private EditText txtMoney;
	private ImageButton btnAddAccount;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_account_activity);
		//Spinner的数据源是ArrayAdapter对象,simple_spinner_item表示的是spinner样式
		ArrayAdapter<AccountEnum> adapter = new ArrayAdapter<AccountEnum>(this, android.R.layout.simple_spinner_item);
		adapter.add(AccountEnum.Daily);
		adapter.add(AccountEnum.Eatery);
		adapter.add(AccountEnum.Shirt);
		adapter.add(AccountEnum.Traffic);
		adapter.add(AccountEnum.Electricity);
		adapter.add(AccountEnum.Amusement);
		adapter.add(AccountEnum.Sport);
		adapter.add(AccountEnum.Company);
		adapter.add(AccountEnum.Other);


		//设置Item的样式
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//设置spinner的数据源
		spinner = (Spinner) findViewById(R.id.spinType);
		spinner.setAdapter(adapter);
		//给弹出的选择框加一个标题
		spinner.setPrompt("消费类型");
		
		calendarView = (CalendarView)findViewById(R.id.calendar_view);
		calendarView.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(WriteAccount.this,CanlendarDialog.class);
				startActivityForResult(intent, REQUEST_CALENDAR);
			}
		});
		
		txtMoney = (EditText)findViewById(R.id.txtMoney);

		btnAddAccount = (ImageButton)findViewById(R.id.btnAddAccount);
		btnAddAccount.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v)
			{
				AlertDialog.Builder dialog = new AlertDialog.Builder(WriteAccount.this);
				dialog.setTitle("添加账单");
				final AccountEnum accountEnum = (AccountEnum)spinner.getSelectedItem();
				Spanned spannable = Html.fromHtml("<a>"+calendarView.getDateFormatString()+"</a><br><a>"+accountEnum+"\t金额:"+txtMoney.getText()+"</a><br><a>亲爱的,你确定增加吗?</a>");
				dialog.setMessage(spannable);
				if(txtMoney.getText().length() > 0)
				{
					dialog.setPositiveButton("确定", new DialogInterface.OnClickListener(){
	
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							DBHelper dbHelper = new DBHelper(WriteAccount.this);
							dbHelper.open();
							ContentValues insertItem = new ContentValues();
							insertItem.put(DBHelper.COLUMN_TYPE, accountEnum.getValue());
							insertItem.put(DBHelper.COLUMN_MONEY, Float.parseFloat(txtMoney.getText().toString()));
							insertItem.put(DBHelper.COLUMN_DATE, calendarView.getDate());
							dbHelper.insert(insertItem);
							dbHelper.close();
							clearState();
						}
					});
				}
				dialog.setNegativeButton("取消", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						clearState();
					}
				});
				
				dialog.create().show();
			}
			
		});
	}
	private void clearState()
	{
		spinner.setSelection(0);
		txtMoney.setText("");
	}
	//处理startActivityForResult
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode == REQUEST_CALENDAR)
		{
			
			if(resultCode == RESULT_OK)
			{
				calendarView.setCalendar(data.getIntExtra("year", 1900), data.getIntExtra("month", 0), data.getIntExtra("day", 1));
			}
		}
	}
}
