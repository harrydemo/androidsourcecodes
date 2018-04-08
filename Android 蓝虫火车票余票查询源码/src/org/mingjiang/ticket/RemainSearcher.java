package org.mingjiang.ticket;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import trainApp.TicketSearch;
import trainApp.common.HttpHelper;
import trainApp.common.TrainInfo;
import trainApp.http.PostDataProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RemainSearcher extends Activity implements OnClickListener {
	TextView startDataLabel = null;
	Button setStartDataButton = null;
	TextView startStationLabel = null;
	TextView arriveStationLabel = null;
	Button setStartArriveStationButton = null;
	EditText trainCodeText = null;
	Button setTrainCodeButton = null;
	TextView trainTypeLabel = null;
	Button setTrainTypeButton = null;
	// Spinner searcherSpinner = null;
	Button searcherButton = null;

	EditText startStationText = null;
	EditText arriveStationText = null;

	Calendar c;
	ProgressDialog progressDialog;

	ListView trainTypeList = null;
	String resultStr = "";
	String refreshTime = "";
	String trainCodes = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.remainticket);
		c = Calendar.getInstance();

		startDataLabel = (TextView) this.findViewById(R.id.startData_label);
		startDataLabel.setText(c.get(Calendar.YEAR) + "-"
				+ (c.get(Calendar.MONDAY) + 1) + "-"
				+ c.get(Calendar.DAY_OF_MONTH));
		setStartDataButton = (Button) this.findViewById(R.id.startData_button);
		setStartDataButton.setOnClickListener(this);
		startStationLabel = (TextView) this
				.findViewById(R.id.startStation_label);
		arriveStationLabel = (TextView) this
				.findViewById(R.id.arriveStation_label);
		setStartArriveStationButton = (Button) this
				.findViewById(R.id.start_arrive_button);
		setStartArriveStationButton.setOnClickListener(this);
		trainCodeText = (EditText) this.findViewById(R.id.trainCode_editText);
		setTrainCodeButton = (Button) this.findViewById(R.id.trainCode_button);
		setTrainCodeButton.setOnClickListener(this);
		trainTypeLabel = (TextView) this.findViewById(R.id.traintype_label);
		setTrainTypeButton = (Button) this.findViewById(R.id.traintype_button);
		setTrainTypeButton.setOnClickListener(this);
		// searcherSpinner = (Spinner) this.findViewById(R.id.lx_Search);
		searcherButton = (Button) this.findViewById(R.id.ticketSearch_button);
		searcherButton.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.startData_button:
			new DatePickerDialog(RemainSearcher.this,
					new DatePickerDialog.OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							// TODO Auto-generated method stub
							String startData = year + "-" + (monthOfYear + 1)
									+ "-" + dayOfMonth;
							RemainSearcher.this.startDataLabel
									.setText(startData);
						}
					}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
							.get(Calendar.DAY_OF_MONTH)).show();
			break;
		case R.id.start_arrive_button:
			LayoutInflater factory = LayoutInflater.from(RemainSearcher.this);
			View myView = factory.inflate(R.layout.start_arrive_station, null);
			startStationText = (EditText) myView
					.findViewById(R.id.startStationEditText);
			arriveStationText = (EditText) myView
					.findViewById(R.id.arriveStationEditText);
			final AlertDialog dialog = new AlertDialog.Builder(this).setTitle(
					"始发终到站").setView(myView).setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							String start = startStationText.getText()
									.toString();
							String arrive = arriveStationText.getText()
									.toString();
							if (start.trim().equals("")
									|| start.trim().equals("")) {
								displayText("请正确输入始发终到站");
							} else {
								RemainSearcher.this.startStationLabel
										.setText(start);
								RemainSearcher.this.arriveStationLabel
										.setText(arrive);
							}
						}

					}).setNegativeButton("取消",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							displayText("我取消这样选择了");
							dialog.cancel();
						}
					}).create();
			dialog.show();
			break;
		case R.id.trainCode_button:
			RemainSearcher.this.progressDialog = new ProgressDialog(
					RemainSearcher.this);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setTitle("请稍候");
			progressDialog.setMessage("正在查询数据……");
			progressDialog.setCancelable(true);
			progressDialog.show();
			Thread trainCodeThread = new Thread() {
				public void run() {
					// 获取时间
					final String[] startData = RemainSearcher.this.startDataLabel
							.getText().toString().trim().split("-");
					final String startStation = RemainSearcher.this.startStationLabel
							.getText().toString().trim();
					final String arriveStation = RemainSearcher.this.arriveStationLabel
							.getText().toString().trim();
					final String trainCode = "";
					// 选择车型列表
					final String[] trainTypes = RemainSearcher.this.trainTypeLabel
							.getText().toString().trim().split(";");
					try {
						TicketSearch trainSearch = new TicketSearch(
								"http://dynamic.12306.cn/TrainQuery/iframeLeftTicketByStation.jsp");
						trainSearch.setMonth(TrainHelper
								.parseNumStr(startData[1]));
						trainSearch.setDay(TrainHelper
								.parseNumStr(startData[2]));
						trainSearch.setStartStation(startStation);
						trainSearch.setArriveStation(arriveStation);
						trainSearch.setTrainCode(trainCode);
						// 这里筛选路过车或始发终到等车，默认是所有
						trainSearch.setRFlag(PostDataProvider.RFLAG_ALL_VALUE);
						String aa = trainTypes[0];
						if (trainTypes.length == 1
								&& aa.equals(TrainHelper.Train_AllType)) {
							trainSearch
									.setName_CheckAll(PostDataProvider.NAME_CKBALL_VALUE);

						} else if (trainTypes.length >= 1) {
							List<String> tFlags = new ArrayList<String>();
							for (int i = 0; i < trainTypes.length; i++) {
								if (trainTypes[i]
										.equals(TrainHelper.Train_DCType_Text)) {
									tFlags.add("DC");
								}
								if (trainTypes[i]
										.equals(TrainHelper.Train_ZType_Text)) {
									tFlags.add("Z");
								}
								if (trainTypes[i]
										.equals(TrainHelper.Train_TType_Text)) {
									tFlags.add("T");
								}
								if (trainTypes[i]
										.equals(TrainHelper.Train_KType_Text)) {
									tFlags.add("K");
								}
								if (trainTypes[i]
										.equals(TrainHelper.Train_PKType_Text)) {
									tFlags.add("PK");
								}
								if (trainTypes[i]
										.equals(TrainHelper.Train_PKEType_Text)) {
									tFlags.add("PKE");
								}
								if (trainTypes[i]
										.equals(TrainHelper.Train_LKType_Text)) {
									tFlags.add("LK");
								}
							}
							trainSearch.setTrainFlag(tFlags);
						}

						int a = 2;
						resultStr = trainSearch
								.post(PostDataProvider.LX_VALUE_00);// postBack信息
						resultStr = HttpHelper.parse(resultStr);
						refreshTime = HttpHelper.getRefreshTime(resultStr);// 服务器更新时间

						if (resultStr != "") {
							Intent intent = new Intent();
							Bundle mBundle = new Bundle();
							// 传递数据
							mBundle.putString("data", resultStr);
							mBundle.putString("start_arriveStation", "\""
									+ RemainSearcher.this.startStationLabel
											.getText().toString()
									+ "\""
									+ "到\""
									+ RemainSearcher.this.arriveStationLabel
											.getText().toString() + "\"");
							mBundle.putString("refreshTime", refreshTime);

							intent.putExtras(mBundle);
							intent.setClass(RemainSearcher.this,
									TrainCodes.class);
							RemainSearcher.this.startActivityForResult(intent, 1);
						}
						RemainSearcher.this.progressDialog.cancel();

					} catch (Exception e) {
						RemainSearcher.this.progressDialog.cancel();
						displayText("提示：查询错误！");
					}
				}
			};
			trainCodeThread.start();
			break;
		case R.id.traintype_button:
			Intent intent = new Intent();
			Bundle mBundle = new Bundle();
			mBundle.putString("data", "");
			intent.putExtras(mBundle);
			intent.setClass(RemainSearcher.this, TrainTypes.class);
			// RemainSearcher.this.startActivity(intent);

			// 能够返回结果
			RemainSearcher.this.startActivityForResult(intent, 1);
			break;
		case R.id.ticketSearch_button:
			RemainSearcher.this.progressDialog = new ProgressDialog(
					RemainSearcher.this);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setTitle("请稍候");
			progressDialog.setMessage("正在查询数据……");
			progressDialog.setCancelable(true);
			progressDialog.show();

			Thread th = new Thread() {
				public void run() {
					// 获取时间
					final String[] startData = RemainSearcher.this.startDataLabel
							.getText().toString().trim().split("-");
					final String startStation = RemainSearcher.this.startStationLabel
							.getText().toString().trim();
					final String arriveStation = RemainSearcher.this.arriveStationLabel
							.getText().toString().trim();
					final String trainCode = RemainSearcher.this.trainCodeText
							.getText().toString().trim();
					// 选择车型列表
					final String[] trainTypes = RemainSearcher.this.trainTypeLabel
							.getText().toString().trim().split(";");
					try {
						TicketSearch trainSearch = new TicketSearch(
								"http://dynamic.12306.cn/TrainQuery/iframeLeftTicketByStation.jsp");
						trainSearch.setMonth(TrainHelper
								.parseNumStr(startData[1]));
						trainSearch.setDay(TrainHelper
								.parseNumStr(startData[2]));
						trainSearch.setStartStation(startStation);
						trainSearch.setArriveStation(arriveStation);
						trainSearch.setTrainCode(trainCode);
						// 这里筛选路过车或始发终到等车，默认是所有
						trainSearch.setRFlag(PostDataProvider.RFLAG_ALL_VALUE);
						String aa = trainTypes[0];
						if (trainTypes.length == 1
								&& aa.equals(TrainHelper.Train_AllType)) {
							trainSearch
									.setName_CheckAll(PostDataProvider.NAME_CKBALL_VALUE);

						} else if (trainTypes.length >= 1) {
							List<String> tFlags = new ArrayList<String>();
							for (int i = 0; i < trainTypes.length; i++) {
								if (trainTypes[i]
										.equals(TrainHelper.Train_DCType_Text)) {
									tFlags.add("DC");
								}
								if (trainTypes[i]
										.equals(TrainHelper.Train_ZType_Text)) {
									tFlags.add("Z");
								}
								if (trainTypes[i]
										.equals(TrainHelper.Train_TType_Text)) {
									tFlags.add("T");
								}
								if (trainTypes[i]
										.equals(TrainHelper.Train_KType_Text)) {
									tFlags.add("K");
								}
								if (trainTypes[i]
										.equals(TrainHelper.Train_PKType_Text)) {
									tFlags.add("PK");
								}
								if (trainTypes[i]
										.equals(TrainHelper.Train_PKEType_Text)) {
									tFlags.add("PKE");
								}
								if (trainTypes[i]
										.equals(TrainHelper.Train_LKType_Text)) {
									tFlags.add("LK");
								}
							}
							trainSearch.setTrainFlag(tFlags);
						}

						int a = 2;
						resultStr = trainSearch
								.post(PostDataProvider.LX_VALUE_00);// postBack信息
						resultStr = HttpHelper.parse(resultStr);
						refreshTime = HttpHelper.getRefreshTime(resultStr);// 服务器更新时间

						if (resultStr != "") {
							Intent intent = new Intent();
							Bundle mBundle = new Bundle();
							// 传递数据
							mBundle.putString("data", resultStr);
							mBundle.putString("start_arriveStation", "\""
									+ RemainSearcher.this.startStationLabel
											.getText().toString()
									+ "\""
									+ "到\""
									+ RemainSearcher.this.arriveStationLabel
											.getText().toString() + "\"");
							mBundle.putString("refreshTime", refreshTime);

							intent.putExtras(mBundle);
							intent.setClass(RemainSearcher.this,
									RemainTicketsList.class);
							RemainSearcher.this.startActivity(intent);
						}
						RemainSearcher.this.progressDialog.cancel();

					} catch (Exception e) {
						RemainSearcher.this.progressDialog.cancel();
						displayText("提示：查询错误！");
					}
				}
			};
			th.start();
			break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			this.trainTypeLabel.setText(data.getExtras().getString("trainTypes"));
			break;
		case 1111111:
			this.trainCodeText.setText(data.getExtras().getString("trainCode"));
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, "网络");
		//menu.add(0, 1, 0, "帮助");
		menu.add(0, 2, 0, "关于");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case 0:
			intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
			startActivity(intent);
			return true;
		case 1:
			displayText("1");
			return true;
		case 2:

			intent = new Intent();
			intent.setClass(RemainSearcher.this, About.class);
			RemainSearcher.this.startActivity(intent);
			return true;
		}
		return false;
	}

	protected void displayText(String string) {
		Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
	}
}
