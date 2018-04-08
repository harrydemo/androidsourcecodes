package com.airport.main;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;

public class CheckAirportTabActivity extends TabActivity {
	private Spinner gospinner, setofffspinner, airportcompany;//出发，目的的下拉列表,航空公司
	private Button selectbtn;//查询按钮
	private String leaveCity, arriveCity, leavedate;//出发城市，目的城市，日期的String类型，用于传数据

	private String[] city = { "上海", "北京", "广州", "大连", "沈阳", "福州" ,"南京"};
	private String[] company = { "中国航空", "东方航空", "上海航空", "海南航空", "南方航空",
			"厦门航空", "深圳航空" };
	private EditText leaveDateEdt, returnBackDateEdt;//出发城市，返回城市编辑框
	private Button leaveDateBtn, returnDateBtn;
	private int m_year, m_month, m_day;
	private Calendar c;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_airport_tab);
		TabHost th = getTabHost();
		th.addTab(th.newTabSpec("single").setIndicator("单程")
				.setContent(R.id.tab1));
		th.addTab(th.newTabSpec("return").setIndicator("返程")
				.setContent(R.id.tab2));
		gospinner = (Spinner) findViewById(R.id.go_selectcity);
		setofffspinner = (Spinner) findViewById(R.id.setoffcity);
		airportcompany = (Spinner) findViewById(R.id.airport_company);
		leaveDateEdt = (EditText) findViewById(R.id.setoff_date);
		leaveDateBtn = (Button) findViewById(R.id.setoffdate_btn);
		returnDateBtn = (Button) findViewById(R.id.return_date_btn);
		returnBackDateEdt = (EditText) findViewById(R.id.setoff_date);
		selectbtn = (Button) findViewById(R.id.select_btn);
		c = Calendar.getInstance();//获取日历的实例
		m_year = c.get(Calendar.YEAR);//年
		m_month = c.get(Calendar.MONTH);//月
		m_day = c.get(Calendar.DAY_OF_MONTH);//日
		// CreateDataBase();
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, company);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		airportcompany.setAdapter(adapter);//将航空公司数组装入适配器
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, city);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		gospinner.setAdapter(adapter1);
		setofffspinner.setAdapter(adapter1);
		leaveDateBtn.setOnClickListener(new leaveDateClick());
		returnDateBtn.setOnClickListener(new returnDateClick());
		gospinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		setofffspinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		//查询按钮事件
		selectbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(CheckAirportTabActivity.this,
						SingleListActivity.class);
				leaveCity = gospinner.getSelectedItem().toString();//获取出发城市选择的选项并转换成文字
				arriveCity = setofffspinner.getSelectedItem().toString();//同理
				leavedate = leaveDateEdt.getText().toString();//同理
				Bundle bundle = new Bundle();//Bundle存取数据，如出发城市，到达城市。出发日期等
				bundle.putString("leaveCity", leaveCity);
				bundle.putString("arriveCity", arriveCity);
				bundle.putString("leavedate", leavedate);
				intent.putExtras(bundle);//向查询界面传送数据
				startActivity(intent);
				finish();

			}
		});
	}

	class leaveDateClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			showDialog(0);
		}

	}

	class returnDateClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			showDialog(1);

		}

	}

	/*
	 * private void CreateDataBase() { DBHelper helper = new
	 * DBHelper(CheckAirportTabActivity.this, "cities_db"); Cursor cursor =
	 * helper.query("cities", new String[] { "_id", "cityname" }, null, null,
	 * null, null, null); if (cursor.getCount() == 0) {
	 * helper.exeecSQL("insert into cities(cityname) values('上海')");
	 * helper.exeecSQL("insert into cities(cityname) values('北京')");
	 * helper.exeecSQL("insert into cities(cityname) values('广州')");
	 * helper.exeecSQL("insert into cities(cityname) values('大连')");
	 * helper.exeecSQL("insert into cities(cityname) values('沈阳')");
	 * helper.exeecSQL("insert into cities(cityname) values('重庆')");
	 * helper.exeecSQL("insert into cities(cityname) values('福州')");
	 * helper.exeecSQL("insert into cities(cityname) values('南昌')");
	 * helper.exeecSQL("insert into cities(cityname) values('鞍山')");
	 * helper.exeecSQL("insert into cities(cityname) values('昆明')");
	 * helper.exeecSQL("insert into cities(cityname) values('武汉')");
	 * helper.exeecSQL("insert into cities(cityname) values('海口')");
	 * helper.exeecSQL("insert into cities(cityname) values('重庆')");
	 * helper.exeecSQL("insert into cities(cityname) values('长沙')");
	 * helper.exeecSQL("insert into cities(cityname) values('赤峰')");
	 * 
	 * } SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
	 * android.R.layout.simple_spinner_item, cursor, new String[] { "cityname"
	 * }, new int[] { android.R.id.text1 });
	 * adapter.setDropDownViewResource(android
	 * .R.layout.simple_spinner_dropdown_item); gospinner.setAdapter(adapter);
	 * setofffspinner.setAdapter(adapter); }
	 */

	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == 0) {
			return new DatePickerDialog(this, l1, m_year, m_month, m_day);
		} else if (id == 1) {
			return new DatePickerDialog(this, l2, m_year, m_month, m_day);
		} else if (id == 2) {
			return new DatePickerDialog(this, l3, m_year, m_month, m_day);
		}
		return null;
	}

	private OnDateSetListener l1 = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			m_year = year;
			m_month = monthOfYear;
			m_day = dayOfMonth;
			leaveDateEdt.setText(m_year + "-" + (m_month + 1) + "-" + m_day);

		}
	};
	private OnDateSetListener l2 = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			m_year = year;
			m_month = monthOfYear;
			m_day = dayOfMonth;
			returnBackDateEdt.setText(m_year + "-" + (m_month + 1) + "-"
					+ m_day);

		}
	};
	private OnDateSetListener l3 = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			m_year = year;
			m_month = monthOfYear;
			m_day = dayOfMonth;
			returnBackDateEdt.setText(m_year + "-" + (m_month + 1) + "-"
					+ m_day);

		}
	};
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode==KeyEvent.KEYCODE_BACK) {
			System.exit(0);
		}
		return true;
	}
}
