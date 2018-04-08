package org.zyj.zyjdeweather;

import java.util.List;

import org.ksoap2.serialization.SoapObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class zyjdeweatherActivity extends Activity {
	private Button city_btn;
	private static final int CITY = 0x11;
	private static final String TAG = "zyjdeweatherActivity";
	private String city_str;
	private TextView city_text;
	private Spinner province_spinner;
	private Spinner city_spinner;
	private List<String> provinces;
	private List<String> citys;
	private SharedPreferences preference;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.tianqiyubaobeijing);
		// 获得SharedPreferences对象
		preference = getSharedPreferences("weather", MODE_PRIVATE);
		// 获取默认城市，青岛
		city_str = readSharpPreference();
		city_text = (TextView) findViewById(R.id.city);
		city_text.setText(city_str);
		city_btn = (Button) findViewById(R.id.city_button);
		city_btn.setOnClickListener(new ClickEvent());
		findViewById(R.id.content_today_layout).getBackground().setAlpha(120);
		findViewById(R.id.content_small_bg1).getBackground().setAlpha(120);
		findViewById(R.id.content_small_bg2).getBackground().setAlpha(120);
		findViewById(R.id.content_small_bg3).getBackground().setAlpha(120);
		refresh(city_str);
	}

	class ClickEvent implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.city_button:
				show_dialog(CITY);
				break;
			default:
				break;
			}
		}
	}


	public void show_dialog(int cityId) {
		switch (cityId) {
		case CITY:
			
			try{
			// 取得city_layout.xml中的视图
			final View view = LayoutInflater.from(this).inflate(
					R.layout.city_layout, null);
			// 省份Spinner
			province_spinner = (Spinner) view
					.findViewById(R.id.province_spinner);
			// 城市Spinner
			city_spinner = (Spinner) view.findViewById(R.id.city_spinner);
			// 省份列表
			provinces = WebServiceUtil.getProvinceList();
			@SuppressWarnings({ "rawtypes", "unchecked" })
			ArrayAdapter adapter = new ArrayAdapter(this,
					android.R.layout.simple_spinner_item, provinces);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			province_spinner.setAdapter(adapter);
			
			// 省份Spinner监听器
			province_spinner
					.setOnItemSelectedListener(new OnItemSelectedListener() {
						@SuppressWarnings("rawtypes")
						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int position, long arg3) {
							citys = WebServiceUtil
									.getCityListByProvince(provinces
											.get(position));
							@SuppressWarnings("unchecked")
							ArrayAdapter adapter1 = new ArrayAdapter(
									zyjdeweatherActivity.this,
									android.R.layout.simple_spinner_item, citys);
							adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							city_spinner.setAdapter(adapter1);
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
						}
					});

			// 城市Spinner监听器
			city_spinner
					.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int position, long arg3) {
							city_str = citys.get(position);
						}
						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
						}
					});

			// 选择城市对话框
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("请选择所属城市");
			dialog.setView(view);
			dialog.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							city_text.setText(city_str);
							writeSharpPreference(city_str);
							refresh(city_str);
						}
					});
			dialog.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			dialog.show();
			
			}catch (Exception e) {
				Toast.makeText(zyjdeweatherActivity.this, "请检查网络连接！", Toast.LENGTH_LONG).show();
			}
			break;
		default:
			break;
		}
	}

	protected void refresh(String city_str) {
		SoapObject detail = WebServiceUtil.getWeatherByCity(city_str);
		Log.i(TAG, detail.toString());
		try {
			// 取得<string>10月13日 中雨转小雨</string>中的数据
			String date = detail.getProperty(7).toString();
			Log.i(TAG, "date----------->" + date);
			// 将"10月13日 中雨转小雨"拆分成两个数组
			String[] date_array = date.split(" ");
			Log.i(TAG, "date_array----------->" + date_array);

			TextView today_text = (TextView) findViewById(R.id.today);
			today_text.setText(date_array[0]);

			// 取得<string>江苏 无锡</string>中的数据
			TextView city_text = (TextView) findViewById(R.id.city_text);
			city_text.setText(detail.getProperty(1).toString());

			TextView today_weather = (TextView) findViewById(R.id.today_weather);
			today_weather.setText(date_array[1]);

			// 取得<string>15℃/21℃</string>中的数据
			TextView qiweng_text = (TextView) findViewById(R.id.qiweng);
			qiweng_text.setText(detail.getProperty(8).toString());

			// 取得<string>今日天气实况：气温：20℃；风向/风力：东南风
			// 2级；湿度：79%</string>中的数据,并通过":"拆分成数组
			TextView shidu_text = (TextView) findViewById(R.id.shidu);
			String date1 = detail.getProperty(4).toString();
			shidu_text.setText(date1.split("：")[4]);

			// 取得<string>东北风3-4级</string>中的数据
			TextView fengli_text = (TextView) findViewById(R.id.fengli);
			fengli_text.setText(detail.getProperty(9).toString());

			// 取得<string>空气质量：良；紫外线强度：最弱</string>中的数据,并通过";"拆分,再通过":"拆分,拆分两次,取得我们需要的数据
			String date2 = detail.getProperty(5).toString();
			String[] date2_array = date2.split("；");
			TextView kongqi_text = (TextView) findViewById(R.id.kongqi);
			kongqi_text.setText(date2_array[0].split("：")[1]);

			TextView zhiwai_text = (TextView) findViewById(R.id.zhiwai);
			zhiwai_text.setText(date2_array[1].split("：")[1]);

			// 设置小贴士数据
			// <string>穿衣指数：较凉爽，建议着长袖衬衫加单裤等春秋过渡装。年老体弱者宜着针织长袖衬衫、马甲和长裤。感冒指数：虽然温度适宜但风力较大，仍较易发生感冒，体质较弱的朋友请注意适当防护。
			// 运动指数：阴天，较适宜开展各种户内外运动。洗车指数：较不宜洗车，路面少量积水，如果执意擦洗汽车，要做好溅上泥水的心理准备。晾晒指数：天气阴沉，不利于水分的迅速蒸发，不太适宜晾晒。若需要晾晒，请尽量选择通风的地点。
			// 旅游指数：阴天，风稍大，但温度适宜，总体来说还是好天气。这样的天气很适宜旅游，您可以尽情享受大自然的风光。路况指数：阴天，路面比较干燥，路况较好。舒适度指数：温度适宜，风力不大，您在这样的天气条件下，会感到比较清爽和舒适。
			// 空气污染指数：气象条件有利于空气污染物稀释、扩散和清除，可在室外正常活动。紫外线指数：属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。</string>
			String[] xiaotieshi = detail.getProperty(6).toString().split("\n");
			TextView xiaotieshi_text = (TextView) findViewById(R.id.xiaotieshi);
			xiaotieshi_text.setText(xiaotieshi[0]);

			// 设置当日图片
			ImageView image = (ImageView) findViewById(R.id.imageView1);
			int icon = parseIcon(detail.getProperty(10).toString());
			image.setImageResource(icon);

			// 取得第二天的天气情况
			String[] date_str = detail.getProperty(12).toString().split(" ");
			TextView tomorrow_date = (TextView) findViewById(R.id.tomorrow_date);
			tomorrow_date.setText(date_str[0]);

			TextView tomorrow_qiweng = (TextView) findViewById(R.id.tomorrow_qiweng);
			tomorrow_qiweng.setText(detail.getProperty(13).toString());

			TextView tomorrow_tianqi = (TextView) findViewById(R.id.tomorrow_tianqi);
			tomorrow_tianqi.setText(date_str[1]);

			ImageView tomorrow_image = (ImageView) findViewById(R.id.tomorrow_image);
			int icon1 = parseIcon(detail.getProperty(15).toString());
			tomorrow_image.setImageResource(icon1);

			// 取得第三天的天气情况
			String[] date_str1 = detail.getProperty(17).toString().split(" ");
			TextView afterday_date = (TextView) findViewById(R.id.afterday_date);
			afterday_date.setText(date_str1[0]);

			TextView afterday_qiweng = (TextView) findViewById(R.id.afterday_qiweng);
			afterday_qiweng.setText(detail.getProperty(18).toString());

			TextView afterday_tianqi = (TextView) findViewById(R.id.afterday_tianqi);
			afterday_tianqi.setText(date_str1[1]);

			ImageView afterday_image = (ImageView) findViewById(R.id.afterday_image);
			int icon2 = parseIcon(detail.getProperty(20).toString());
			afterday_image.setImageResource(icon2);

			// 取得第四天的天气情况
			String[] date_str3 = detail.getProperty(22).toString().split(" ");
			TextView nextday_date = (TextView) findViewById(R.id.nextday_date);
			nextday_date.setText(date_str3[0]);

			TextView nextday_qiweng = (TextView) findViewById(R.id.nextday_qiweng);
			nextday_qiweng.setText(detail.getProperty(23).toString());

			TextView nextday_tianqi = (TextView) findViewById(R.id.nextday_tianqi);
			nextday_tianqi.setText(date_str3[1]);

			ImageView nextday_image = (ImageView) findViewById(R.id.nextday_image);
			int icon3 = parseIcon(detail.getProperty(25).toString());
			nextday_image.setImageResource(icon3);

		} catch (Exception e) {
			Toast.makeText(zyjdeweatherActivity.this, "请检查网络连接！", Toast.LENGTH_LONG).show();
		}
	}

	// 工具方法，该方法负责把返回的天气图标字符串，转换为程序的图片资源ID。
	private int parseIcon(String strIcon) {
		if (strIcon == null)
			return -1;
		if ("0.gif".equals(strIcon))
			return R.drawable.a_0;
		if ("1.gif".equals(strIcon))
			return R.drawable.a_1;
		if ("2.gif".equals(strIcon))
			return R.drawable.a_2;
		if ("3.gif".equals(strIcon))
			return R.drawable.a_3;
		if ("4.gif".equals(strIcon))
			return R.drawable.a_4;
		if ("5.gif".equals(strIcon))
			return R.drawable.a_5;
		if ("6.gif".equals(strIcon))
			return R.drawable.a_6;
		if ("7.gif".equals(strIcon))
			return R.drawable.a_7;
		if ("8.gif".equals(strIcon))
			return R.drawable.a_8;
		if ("9.gif".equals(strIcon))
			return R.drawable.a_9;
		if ("10.gif".equals(strIcon))
			return R.drawable.a_10;
		if ("11.gif".equals(strIcon))
			return R.drawable.a_11;
		if ("12.gif".equals(strIcon))
			return R.drawable.a_12;
		if ("13.gif".equals(strIcon))
			return R.drawable.a_13;
		if ("14.gif".equals(strIcon))
			return R.drawable.a_14;
		if ("15.gif".equals(strIcon))
			return R.drawable.a_15;
		if ("16.gif".equals(strIcon))
			return R.drawable.a_16;
		if ("17.gif".equals(strIcon))
			return R.drawable.a_17;
		if ("18.gif".equals(strIcon))
			return R.drawable.a_18;
		if ("19.gif".equals(strIcon))
			return R.drawable.a_19;
		if ("20.gif".equals(strIcon))
			return R.drawable.a_20;
		if ("21.gif".equals(strIcon))
			return R.drawable.a_21;
		if ("22.gif".equals(strIcon))
			return R.drawable.a_22;
		if ("23.gif".equals(strIcon))
			return R.drawable.a_23;
		if ("24.gif".equals(strIcon))
			return R.drawable.a_24;
		if ("25.gif".equals(strIcon))
			return R.drawable.a_25;
		if ("26.gif".equals(strIcon))
			return R.drawable.a_26;
		if ("27.gif".equals(strIcon))
			return R.drawable.a_27;
		if ("28.gif".equals(strIcon))
			return R.drawable.a_28;
		if ("29.gif".equals(strIcon))
			return R.drawable.a_29;
		if ("30.gif".equals(strIcon))
			return R.drawable.a_30;
		if ("31.gif".equals(strIcon))
			return R.drawable.a_31;
		return 0;
	}

	public void writeSharpPreference(String string) {

		SharedPreferences.Editor editor = preference.edit();
		editor.putString("city", string);
		editor.commit();
	}

	public String readSharpPreference() {
		String city = preference.getString("city", "青岛");
		return city;
	}
}