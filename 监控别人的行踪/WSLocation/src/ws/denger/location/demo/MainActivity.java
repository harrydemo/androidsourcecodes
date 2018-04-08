package ws.denger.location.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * @author Denger
 * 
 */
public class MainActivity extends Activity implements BDLocationListener,
		OnClickListener {
	private Button btnToggleAuto;
	private TextView tvShow;
	private EditText etTime;
	private boolean isAuto = false;// 是否处于自动获取中
	private LocationClient client;// 度娘屌丝端，用这神器进行各种

	private void loadWidget() {
		btnToggleAuto = (Button) findViewById(R.id.btnToggleAuto);
		etTime = (EditText) findViewById(R.id.etTime);
		tvShow = (TextView) findViewById(R.id.tvShow);
		btnToggleAuto.setOnClickListener(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		loadWidget();
		client = new LocationClient(this); // 初始化屌丝端
		client.registerLocationListener(this);// 注册监听
		setOption();// 设置参数
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if (client.isStarted()) {
			client.stop();
		}
		super.onPause();
	}

	/**
	 * 设置度娘屌丝端的参数
	 */
	private void setOption() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 1
		option.setCoorType("bd09ll"); // 2
		option.setPriority(LocationClientOption.GpsFirst); // 3 设置优先
		option.setProdName("wsLocation"); // 4 设置产品线名称
		String time = etTime.getText().toString();
		if (time != null && time.length() > 0)// 5
			option.setScanSpan(Integer.parseInt(time));
		else
			option.setScanSpan(4000);
		client.setLocOption(option);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnToggleAuto:
			System.out.println("btnAuto...");
			if (isAuto) {
				client.stop();
				btnToggleAuto.setText("开始自动获取");
			} else {
				client.start();
				btnToggleAuto.setText("关闭自动获取");
			}
			isAuto = !isAuto;
			break;
		}
	}

	@Override
	public void onReceiveLocation(BDLocation location) {
		StringBuilder sb = new StringBuilder();
		sb.append("时间: ");
		sb.append(location.getTime());
		sb.append("\nerror code: ");
		sb.append(location.getLocType());
		sb.append("\n纬度: ");
		sb.append(location.getLatitude());
		sb.append("\n经度: ");
		sb.append(location.getLongitude());
		sb.append("\n误差径: ");
		sb.append(location.getRadius());
		if (location.getLocType() == BDLocation.TypeGpsLocation) {
			sb.append("\n速度: ");
			sb.append(location.getSpeed());
			sb.append("\n卫星: ");
			sb.append(location.getSatelliteNumber());
		} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
			sb.append("\n地址: ");
			sb.append(location.getAddrStr());
		}
		System.out.println("---->" + sb.toString());
		tvShow.setText(sb.toString());
	}
}