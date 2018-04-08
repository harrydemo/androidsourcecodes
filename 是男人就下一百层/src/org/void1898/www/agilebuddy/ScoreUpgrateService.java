package org.void1898.www.agilebuddy;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import org.void1898.www.agilebuddy.util.ConstantInfo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Binder;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class ScoreUpgrateService extends Service implements
		OnSharedPreferenceChangeListener {

	@Override
	public void onCreate() {
		super.onCreate();
		SharedPreferences rankingInfo = getSharedPreferences(
				ConstantInfo.PREFERENCE_RANKING_INFO, 0);
		if ("".equals(rankingInfo.getString(
				ConstantInfo.PREFERENCE_KEY_RANKING_UID, ""))) {
			String uuid = null;
			try {
				// 获取IMEI作为唯一标识
				uuid = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE))
						.getDeviceId();
			} catch (Exception ex) {
			}
			if (uuid == null) {
				// 如果IMEI获取失败, 随机生成UUID作为标识
				uuid = UUID.randomUUID().toString();
			}
			rankingInfo.edit().putString(
					ConstantInfo.PREFERENCE_KEY_RANKING_UID, uuid).putInt(
					ConstantInfo.PREFERENCE_KEY_RANKING_SCORE, 0).commit();
		}
		rankingInfo.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return binder;
	}

	public synchronized void onSharedPreferenceChanged(
			SharedPreferences sharedPreferences, String key) {
		if (key.equals(ConstantInfo.PREFERENCE_KEY_RANKING_FLAG)) {
			// 分数低于100不提交到服务器
			if (sharedPreferences.getInt(
					ConstantInfo.PREFERENCE_KEY_RANKING_SCORE, 0) < 100) {
				showToast(R.string.options_toast_upload_success);
				return;
			}
			if (updateRecord(
					sharedPreferences.getString(
							ConstantInfo.PREFERENCE_KEY_RANKING_UID, ""),
					sharedPreferences.getString(
							ConstantInfo.PREFERENCE_KEY_RANKING_NAME, "NONAME"),
					sharedPreferences.getInt(
							ConstantInfo.PREFERENCE_KEY_RANKING_SCORE, 0))) {
				showToast(R.string.options_toast_upload_success);
			} else {
				showToast(R.string.options_toast_upload_failure);
			}
		}
	}

	private boolean updateRecord(String uid, String name, int score) {
		HttpURLConnection conn = null;
		try {
			URL url = new URL(ConstantInfo.APP_SERVER_URL);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			dos.writeUTF("UPDATE");
			dos.writeUTF(uid);
			dos.writeUTF(name);
			dos.writeUTF(String.valueOf(score));
			dos.flush();
			dos.close();
			DataInputStream dis = new DataInputStream(conn.getInputStream());
			String resultInfo = dis.readUTF();
			if (resultInfo != null && !"ERROR".equals(resultInfo)) {
				return true;
			}
			dis.close();
		} catch (Exception e) {
			Log.e("", "", e);
		} finally {
			if (conn != null)
				conn.disconnect();
		}
		return false;
	}

	private void showToast(int strId) {
		Toast toast = Toast.makeText(this, strId, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP, 0, 220);
		toast.show();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	@Override
	public void onDestroy() {
		SharedPreferences rankingInfo = getSharedPreferences(
				ConstantInfo.PREFERENCE_RANKING_INFO, 0);
		rankingInfo.unregisterOnSharedPreferenceChangeListener(this);
	}

	private final IBinder binder = new ScoreUpgrateServiceBinder();

	public class ScoreUpgrateServiceBinder extends Binder {
		public ScoreUpgrateService getService() {
			return ScoreUpgrateService.this;
		}
	}

}
