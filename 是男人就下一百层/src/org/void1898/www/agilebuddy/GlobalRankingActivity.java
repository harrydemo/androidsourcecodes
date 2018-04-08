package org.void1898.www.agilebuddy;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.stringtree.json.ExceptionErrorListener;
import org.stringtree.json.JSONReader;
import org.stringtree.json.JSONValidatingReader;
import org.void1898.www.agilebuddy.util.ConstantInfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * activity for global ranking
 * 
 * @author void1898@gamil.com
 * 
 */
public class GlobalRankingActivity extends Activity {

	private static int HANDLE_MESSAGE_UI_UPDATE = 1;

	private ProgressDialog mProgressDialog;

	private LinkedList<TableRow> mRankingDatas = new LinkedList<TableRow>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.ranking);

		mProgressDialog = ProgressDialog.show(this, "", getResources()
				.getString(R.string.ranking_title_loading_info), true, true,
				new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface arg0) {
						GlobalRankingActivity.this.finish();
					}
				});

		new RankingDataThread(this, new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what == GlobalRankingActivity.HANDLE_MESSAGE_UI_UPDATE) {
					TableLayout table = (TableLayout) GlobalRankingActivity.this
							.findViewById(R.id.ranking_table);
					for (TableRow row : mRankingDatas) {
						if (table.equals(row.getParent())) {
							table.removeView(row);
						}
						table.addView(row);
					}
					table.invalidate();
				}
			}
		}).start();
	}

	class RankingDataThread extends Thread {

		private Handler mHandler;

		public RankingDataThread(Context context, Handler handler) {
			this.mHandler = handler;
		}

		public void run() {
			mRankingDatas.clear();
			SharedPreferences settings = getSharedPreferences(
					ConstantInfo.PREFERENCE_RANKING_INFO, 0);
			String uid = settings.getString(
					ConstantInfo.PREFERENCE_KEY_RANKING_UID, "");

			Map myRecord = new HashMap();
			myRecord.put("index", "N/A");
			myRecord.put("name", settings.getString(
					ConstantInfo.PREFERENCE_KEY_RANKING_NAME, "Unknow"));
			myRecord.put("score", settings.getInt(
					ConstantInfo.PREFERENCE_KEY_RANKING_SCORE, 0));
			myRecord.put("bizDate", settings.getString(
					ConstantInfo.PREFERENCE_KEY_RANKING_DATE, "Unknow"));

			Map curObj = null;
			List rankList = getRankingInfo(uid);
			if (rankList != null && rankList.size() > 0) {
				boolean isInTopFlag = false;
				for (Object obj : rankList) {
					curObj = (Map) obj;
					if (uid.equals(curObj.get("uid"))) {
						isInTopFlag = true;
						break;
					}
				}
				// 不在前25名单里
				if (!isInTopFlag) {
					int rankLength = rankList.size();
					curObj = (Map) rankList.get(rankLength - 1);
					if ((Long) curObj.get("score") >= (Integer) myRecord
							.get("score")) {
						myRecord.put("index", ">100");
						rankList.add(myRecord);
					} else {
						for (int i = 0; i < rankLength; i++) {
							curObj = (Map) rankList.get(i);
							if ((Long) curObj.get("score") < (Integer) myRecord
									.get("score")) {
								rankList.add(i, myRecord);
								break;
							}
						}
					}
				}
			} else {
				rankList.add(myRecord);
			}

			// 表头
			TableRow tableRow = null;
			TextView tableView = null;
			tableRow = new TableRow(GlobalRankingActivity.this);
			tableView = new TextView(GlobalRankingActivity.this);
			tableView.setText(R.string.ranking_title_no);
			tableView.setWidth(55);
			tableView.setPadding(10, 10, 0, 0);
			tableView.setTextColor(0xFF88C426);
			tableRow.addView(tableView);
			tableView = new TextView(GlobalRankingActivity.this);
			tableView.setText(R.string.ranking_title_name);
			tableView.setWidth(90);
			tableView.setPadding(10, 10, 0, 0);
			tableView.setTextColor(0xFFF28530);
			tableRow.addView(tableView);
			tableView = new TextView(GlobalRankingActivity.this);
			tableView.setText(R.string.ranking_title_score);
			tableView.setWidth(60);
			tableView.setPadding(10, 10, 0, 0);
			tableView.setTextColor(0xFFA7DBD7);
			tableRow.addView(tableView);
			tableView = new TextView(GlobalRankingActivity.this);
			tableView.setText(R.string.ranking_title_date);
			tableView.setWidth(85);
			tableView.setPadding(10, 10, 0, 0);
			tableView.setTextColor(0xFFFFEA21);
			tableRow.addView(tableView);
			mRankingDatas.add(tableRow);
			for (Object obj : rankList) {
				curObj = (Map) obj;
				tableRow = new TableRow(GlobalRankingActivity.this);
				tableView = new TextView(GlobalRankingActivity.this);
				tableView.setText(String.valueOf(curObj.get("index")));
				tableView.setWidth(55);
				tableView.setPadding(10, 10, 0, 0);
				tableView.setTextColor(0xFF88C426);
				tableRow.addView(tableView);
				tableView = new TextView(GlobalRankingActivity.this);
				tableView.setText(String.valueOf(curObj.get("name")));
				tableView.setWidth(90);
				tableView.setPadding(10, 10, 0, 0);
				tableView.setTextColor(0xFFF28530);
				tableRow.addView(tableView);
				tableView = new TextView(GlobalRankingActivity.this);
				tableView.setText(String.valueOf(curObj.get("score")));
				tableView.setWidth(60);
				tableView.setPadding(10, 10, 0, 0);
				tableView.setTextColor(0xFFA7DBD7);
				tableRow.addView(tableView);
				tableView = new TextView(GlobalRankingActivity.this);
				tableView.setText(String.valueOf(curObj.get("bizDate")));
				tableView.setWidth(85);
				tableView.setPadding(10, 10, 0, 0);
				tableView.setTextColor(0xFFFFEA21);
				tableRow.addView(tableView);
				mRankingDatas.add(tableRow);
			}
			Message message = new Message();
			message.what = GlobalRankingActivity.HANDLE_MESSAGE_UI_UPDATE;
			mHandler.sendMessage(message);
			mProgressDialog.dismiss();
		}
	}

	/**
	 * 获取排名信息(前25+当前用户)
	 * 
	 * @param uuid
	 *            用户唯一标识
	 * @return 排名列表
	 */
	public List getRankingInfo(String uuid) {
		HttpURLConnection conn = null;
		try {
			URL url = new URL(ConstantInfo.APP_SERVER_URL);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			dos.writeUTF("GET");
			dos.writeUTF(uuid);
			dos.flush();
			dos.close();
			DataInputStream dis = new DataInputStream(conn.getInputStream());
			String resultInfo = dis.readUTF();
			if (resultInfo != null && !"ERROR".equals(resultInfo)) {
				JSONReader reader = new JSONValidatingReader(
						new ExceptionErrorListener());
				return (List) reader.read(resultInfo);
			}
			dis.close();
		} catch (Exception e) {
			Log.e("", "", e);
		} finally {
			if (conn != null)
				conn.disconnect();
		}
		return new ArrayList();
	}
}
