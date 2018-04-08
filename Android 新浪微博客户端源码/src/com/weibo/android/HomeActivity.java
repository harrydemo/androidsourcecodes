package com.weibo.android;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.weibo.android.logic.AsyncBitmapLoader;
import com.weibo.android.logic.MainService;
import com.weibo.android.logic.Task;
import com.weibo.net.Weibo;
import com.weibo.net.WeiboException;

public class HomeActivity extends Activity implements IWeiboActivity,
		OnScrollListener {

	private Map<Integer, View> m = new HashMap<Integer, View>();
	private ListView listView;
	private View loadMoreView;
	private Button head_new;
	private Button head_refresh;
	private JsonAdapter adapter;
	private ProgressDialog p;
	private TextView loadMoreButton;
	private Weibo weibo = Weibo.getInstance();
	private static int id = 2;// 默认从第二页开始加载
	String returnResult;// 返回的json
	JSONObject jsonobject;// 构造jsonobject
	JSONArray jsonarray;// 构造jsonarray
	private int visibleLastIndex = 0; // 最后的可视项索引
	private int visibleItemCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		if (MainService.isRun == true) {

		} else {
			Intent service = new Intent(this, MainService.class);
			startService(service);
		}

		MainService.addActivity(HomeActivity.this);
		MainService.newTask(new Task(Task.HOMEREFRESH, null,this));
		listView = (ListView) findViewById(R.id.home_list);
		head_new = (Button) this.findViewById(R.id.head_new);
		head_refresh = (Button) this.findViewById(R.id.head_refresh);
		loadMoreView = getLayoutInflater().inflate(R.layout.load_more, null);
		loadMoreButton = (TextView) loadMoreView
				.findViewById(R.id.loadMoreButton);
		loadMoreButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				WeiBoAsyncTask weiboTask = new WeiBoAsyncTask(HomeActivity.this);
				weiboTask.execute("");
			}
		});

		head_new.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HomeActivity.this,
						NewWeiboActivity.class);
				startActivity(intent);
			}
		});

		head_refresh.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				p = ProgressDialog.show(HomeActivity.this, "",
						"正在加载，请稍候  . . .  ", true);
				p.show();
				MainService.newTask(new Task(Task.HOMEREFRESH, null,HomeActivity.this));

			}
		});

		listView.addFooterView(loadMoreView); // 设置列表底部视图
		listView.setOnScrollListener(this); // 添加滑动监听
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				String itemid = String.valueOf(adapter.getItemId(position));
				Intent i = new Intent(HomeActivity.this,ViewWeiBoActivity.class);
				i.putExtra("key", itemid);
				startActivity(i);
			}
		});
		p = ProgressDialog.show(this, "", "正在加载，请稍候  . . .  ", true);
	}

	private class JsonAdapter extends BaseAdapter {
		private Context ctx;
		private JSONArray jsonarray;

		public JsonAdapter(Context ctx, JSONArray jsonarray) {
			this.ctx = ctx;
			this.jsonarray = jsonarray;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return jsonarray.length();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return jsonarray.optJSONObject(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub

			try {
				JSONObject jsonobject = (JSONObject) getItem(position);
				return jsonobject.getLong("id");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			convertView = LayoutInflater.from(ctx).inflate(R.layout.home_item,
					null);
			ImageView imageIcon = (ImageView) convertView
					.findViewById(R.id.home_image);
			ImageView content_image = (ImageView) convertView
					.findViewById(R.id.content_image);
			ImageView retween_image = (ImageView) convertView
					.findViewById(R.id.retweeted_image);
			try {
				// 异步加载图片

				new AsyncBitmapLoader().execute(imageIcon,
						((JSONObject) getItem(position)).getJSONObject("user")
								.getString("profile_image_url"));
				if (((JSONObject) getItem(position)).has("thumbnail_pic")) {
					new AsyncBitmapLoader().execute(content_image,
							((JSONObject) getItem(position))
									.getString("thumbnail_pic"));
				}
				/*
				 * imageIcon.setBackgroundDrawable(NetUtil.getNetImage(status
				 * .getUser().getProfileImageURL()));
				 */
				TextView textView = (TextView) convertView
						.findViewById(R.id.home_title);
				textView.setText(((JSONObject) getItem(position))
						.getJSONObject("user").getString("screen_name")
						+ "说：");
				TextView textContent = (TextView) convertView
						.findViewById(R.id.home_content);
				textContent.setText(((JSONObject) getItem(position))
						.getString("text"));
				TextView textTime = (TextView) convertView
						.findViewById(R.id.home_timeout);
				textTime.setTextSize(10);
				textTime.setText(((JSONObject) getItem(position))
						.getString("created_at"));

				TextView home_item_swich = (TextView) convertView
						.findViewById(R.id.home_item_swich);

				if (((JSONObject) getItem(position)).has("retweeted_status")) {
					home_item_swich.setText(((JSONObject) getItem(position))
							.getJSONObject("retweeted_status").getJSONObject(
									"user").getString("screen_name")
							+ ":"
							+ ((JSONObject) getItem(position)).getJSONObject(
									"retweeted_status").getString("text"));
					if ((((JSONObject) getItem(position))
							.getJSONObject("retweeted_status"))
							.has("thumbnail_pic")) {

						new AsyncBitmapLoader().execute(retween_image,
								(((JSONObject) getItem(position))
										.getJSONObject("retweeted_status"))
										.getString("thumbnail_pic"));
					}

				} else {
					LinearLayout layout = (LinearLayout) convertView
							.findViewById(R.id.home_item_layout);
					layout.setVisibility(View.GONE);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return convertView;
		}

		public void addItem(JSONObject jsonobject) {
			jsonarray.put(jsonobject);
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {/*
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("你确定退出吗？").setCancelable(false)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// 停止服务
									// Context.stopService();
									Intent it = new Intent(HomeActivity.this,
											MainService.class);
									HomeActivity.this.stopService(it);
									HomeActivity.this.finish();
									android.os.Process
											.killProcess(android.os.Process
													.myPid());
									android.os.Process
											.killProcess(android.os.Process
													.myTid());
									android.os.Process
											.killProcess(android.os.Process
													.myUid());
								}
							}).setNegativeButton("返回",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
			return true;
		}

		return super.onKeyDown(keyCode, event);
		
	*/
		return true;
	}

	@Override
	public void init() {

	}

	@Override
	public void refresh(Object... params) {
		try {
			returnResult = (String) params[0];
			jsonobject = new JSONObject(returnResult);
			jsonarray = jsonobject.getJSONArray("statuses");
			Log.i("refresh json", returnResult);
			adapter = new JsonAdapter(this, jsonarray);
			listView.setAdapter(adapter);
			p.dismiss();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		this.visibleItemCount = visibleItemCount;
		visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
	}

	// 滑动状态改变时被调用
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		int itemsLastIndex = adapter.getCount() - 1; // 数据集最后一项的索引
		int lastIndex = itemsLastIndex + 1; // 加上底部的loadMoreView项
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& visibleLastIndex == lastIndex) {
			// 如果是自动加载,可以在这里放置异步加载数据的代码
			Log.i("LOADMORE", "loading...");
		}
	}

	class WeiBoAsyncTask extends AsyncTask<String, Integer, String> {
		ProgressDialog pdialog;

		public WeiBoAsyncTask(Context context) {
			pdialog = new ProgressDialog(context);
			pdialog.setMessage("正在加载微博信息,请稍后");
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				Paging p = new Paging(id, 10);// 每次加载20条微博
				String loadmoreresult = weibo.getPublicTimeline(
						HomeActivity.this, p);
				Log.v("loadmore", loadmoreresult);
				JSONArray loadmoerejson = new JSONObject(loadmoreresult)
						.getJSONArray("statuses");
				for (int index = 0; index < loadmoerejson.length(); index++) {
					adapter.addItem(loadmoerejson.optJSONObject(index));
				}

			} catch (WeiboException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			} catch (JSONException jsonException) {
				jsonException.printStackTrace();
			}
			return null;

		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			adapter.notifyDataSetChanged(); // 数据集变化后,通知adapter
			listView.setSelection(visibleLastIndex - visibleItemCount + 1); // 设置选中项

			id += 1;// 页数加1
			pdialog.dismiss();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pdialog.show();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

	}

}
