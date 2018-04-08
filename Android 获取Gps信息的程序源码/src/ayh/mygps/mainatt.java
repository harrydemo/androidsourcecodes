package ayh.mygps;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
//import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class mainatt extends Activity implements OnClickListener
{
	private LocationManager lm;
	private Location loc;
	private Criteria ct;
	private String provider;

	private TextView tvLatitude;
	private TextView tvLongitude;
	private TextView tvHigh;
	private TextView tvDirection;
	private TextView tvSpeed;
	private TextView tvGpsTime;
	private TextView tvInfoType;
	private EditText etSetTimeSpace;
	private Button btnmanual;
	private Button btnsettimespace;
	private Button btnexit;

	private DBGps dbgps = new DBGps(this);

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		tvLatitude = (TextView) findViewById(R.id.tvlatitude);
		tvLongitude = (TextView) findViewById(R.id.tvlongitude);
		tvHigh = (TextView) findViewById(R.id.tvhigh);
		tvDirection = (TextView) findViewById(R.id.tvdirection);
		tvSpeed = (TextView) findViewById(R.id.tvspeed);
		tvGpsTime = (TextView) findViewById(R.id.tvgpstime);
		tvInfoType = (TextView) findViewById(R.id.tvinfotype);
		etSetTimeSpace = (EditText) findViewById(R.id.ettimespace);
		btnmanual = (Button) findViewById(R.id.btnmanual);
		btnmanual.setOnClickListener(this);
		btnsettimespace = (Button) findViewById(R.id.btnsettimespace);
		btnsettimespace.setOnClickListener(this);
		btnexit = (Button) findViewById(R.id.btnexit);
		btnexit.setOnClickListener(this);

		dbgps.openDB();
		initLocation();
	}

	private final LocationListener locationListener = new LocationListener()
	{

		@Override
		public void onLocationChanged(Location arg0)
		{
			showInfo(getLastPosition(), 2);
		}

		@Override
		public void onProviderDisabled(String arg0)
		{
			showInfo(null, -1);
		}

		@Override
		public void onProviderEnabled(String arg0)
		{
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2)
		{
		}

	};

	private void initLocation()
	{
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
		{
			ct = new Criteria();
			ct.setAccuracy(Criteria.ACCURACY_FINE);// 高精度
			ct.setAltitudeRequired(true);// 显示海拔
			ct.setBearingRequired(true);// 显示方向
			ct.setSpeedRequired(true);// 显示速度
			ct.setCostAllowed(false);// 不允许有花费
			ct.setPowerRequirement(Criteria.POWER_LOW);// 低功耗
			provider = lm.getBestProvider(ct, true);
			// 位置变化监听,默认5秒一次,距离10米以上
			lm.requestLocationUpdates(provider, 5000, 10, locationListener);
		} else
			showInfo(null, -1);
	}

	private gpsdata getLastPosition()
	{
		gpsdata result = new gpsdata();
		loc = lm.getLastKnownLocation(provider);
		if (loc != null)
		{
			result.Latitude = (int) (loc.getLatitude() * 1E6);
			result.Longitude = (int) (loc.getLongitude() * 1E6);
			result.High = loc.getAltitude();
			result.Direct = loc.getBearing();
			result.Speed = loc.getSpeed();
			Date d = new Date();
			d.setTime(loc.getTime() + 28800000);// UTC时间,转北京时间+8小时
			result.GpsTime = DateFormat.format("yyyy-MM-dd kk:mm:ss", d).toString();
			d = null;
		}
		return result;
	}

	private void showInfo(gpsdata cdata, int infotype)
	{
		if (cdata == null)
		{
			if (infotype == -1)
			{
				tvLatitude.setText("GPS功能已关闭");
				tvLongitude.setText("");
				tvHigh.setText("");
				tvDirection.setText("");
				tvSpeed.setText("");
				tvGpsTime.setText("");
				tvInfoType.setText("");
				btnmanual.setEnabled(false);
				btnsettimespace.setEnabled(false);
				etSetTimeSpace.setEnabled(false);
			}
		} else
		{
			tvLatitude.setText(String.format("纬度:%d", cdata.Latitude));
			tvLongitude.setText(String.format("经度:%d", cdata.Longitude));
			tvHigh.setText(String.format("海拔:%f", cdata.High));
			tvDirection.setText(String.format("方向:%f", cdata.Direct));
			tvSpeed.setText(String.format("速度:%f", cdata.Speed));
			tvGpsTime.setText(String.format("GPS时间:%s", cdata.GpsTime));
			cdata.InfoType = infotype;
			switch (infotype)
			{
			case 1:
				tvInfoType.setText("信息来源状态:手动获取更新");
				break;
			case 2:
				tvInfoType.setText("信息来源状态:位置改变更新");
				break;
			/*
			 * case 3: tvInfoType.setText("信息来源状态:位置改变更新"); break;
			 */
			}

			dbgps.addGpsData(cdata);
		}

	}

	@Override
	public void onClick(View v)
	{
		if (v.equals(btnmanual))
		{
			showInfo(getLastPosition(), 1);
		}
		if (v.equals(btnsettimespace))
		{
			if (TextUtils.isEmpty(etSetTimeSpace.getText().toString()))
			{
				Toast.makeText(this, "请输入更新时间间隔", Toast.LENGTH_LONG).show();
				etSetTimeSpace.requestFocus();
				return;
			}

			int timespace = Integer.valueOf(etSetTimeSpace.getText().toString()) * 1000;
			if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
				lm.requestLocationUpdates(provider, timespace, 10, locationListener);
		}
		if (v.equals(btnexit))
			android.os.Process.killProcess(android.os.Process.myPid());
	}

	@Override
	protected void onDestroy()
	{
		if (dbgps != null)
		{
			dbgps.closeDB();
			dbgps = null;
		}
		super.onDestroy();
	}

}