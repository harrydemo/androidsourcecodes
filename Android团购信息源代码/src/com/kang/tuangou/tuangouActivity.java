package com.kang.tuangou;
//Download by http://www.codefans.net
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.kang.meituan.meituan;
import com.kang.meituan.tuan_handler;

public class tuangouActivity extends Activity implements OnClickListener,
		OnItemClickListener
{
	private static final String TAG = "tuangouActivity";
	private ListView listView;
	private List<meituan> meituans;
	private Button meituan, lashou, nuomi, ftuan, city_bt;
	private TextView city_text;
	private ProgressDialog Dialog;
	private SimpleAdapter adapter;
	private String[] citys_china;
	private String[] citys;
	private int city_position;
	private String city_str;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏

		setContentView(R.layout.test);

		citys = getResources().getStringArray(R.array.citys);
		citys_china = getResources().getStringArray(R.array.citys_china);

		ReadSharedPreferences();
		city_str = citys_china[city_position];
		city_text = (TextView) findViewById(R.id.city_text);

		Dialog = new ProgressDialog(this);
		Dialog.setTitle("团购网站");
		Dialog.setMessage("正在载入,请稍候...");

		String path = "http://open.client.lashou.com/api/detail/city/"
				+ URLEncoder.encode(citys_china[city_position]) + "/p/1/r/10";
		progressshow(path, tuan_handler.LASHOU);

		// 美团按钮事件
		meituan = (Button) findViewById(R.id.meituan);
		meituan.setOnClickListener(this);

		// 拉手按钮事件
		lashou = (Button) findViewById(R.id.lashou);
		lashou.setOnClickListener(this);

		// F团按钮事件
		ftuan = (Button) findViewById(R.id.ftuan);
		ftuan.setOnClickListener(this);

		// 糯米按钮事件
		nuomi = (Button) findViewById(R.id.nuomi);
		nuomi.setOnClickListener(this);

		// 城市选择按钮
		city_bt = (Button) findViewById(R.id.city);
		city_bt.setOnClickListener(this);

		// listViw的点击事件
		listView = (ListView) findViewById(R.id.listview);
		listView.setOnItemClickListener(this);

	}

	// 按钮点击事件处理
	public void onClick(View v)
	{
		if (v == meituan)
		{
			String path = "http://www.meituan.com/api/v2/"
					+ citys[city_position] + "/deals";
			progressshow(path, tuan_handler.MEITUAN);

		} else if (v == lashou)
		{

			String path = "http://open.client.lashou.com/api/detail/city/"
					+ URLEncoder.encode(citys_china[city_position])
					+ "/p/1/r/10";
			progressshow(path, tuan_handler.LASHOU);

		} else if (v == ftuan)
		{

			String path = "http://newapi.ftuan.com/api/v2.aspx?city="
					+ citys[city_position];
			progressshow(path, tuan_handler.FTUAN);

		} else if (v == nuomi)
		{

			String path = "http://www.nuomi.com/api/dailydeal?version=v1&city="
					+ citys[city_position];
			progressshow(path, tuan_handler.NUOMI);

		} else if (v == city_bt)
		{
			AlertDialog.Builder city_dialog = new AlertDialog.Builder(this);
			city_dialog.setTitle("请选择您需要查看的城市");
			city_dialog.setSingleChoiceItems(citys_china, -1,
					new DialogInterface.OnClickListener()
					{

						public void onClick(DialogInterface dialog, int which)
						{
							city_position = which;
							city_str = citys_china[city_position];
							Toast.makeText(tuangouActivity.this,
									"您现在选择查看的城市为" + citys_china[city_position],
									1).show();
							String path = "http://open.client.lashou.com/api/detail/city/"
									+ URLEncoder
											.encode(citys_china[city_position])
									+ "/p/1/r/10";
							progressshow(path, tuan_handler.LASHOU);

							dialog.dismiss();
						}
					});

			city_dialog.show();

		}

	}

	// 通过线程来加载网络数据
	private void progressshow(final String path, final int web)
	{
		Dialog.show();

		final Handler mHandler = new Handler()
		{

			// 处理来自线程的消息,并将线程中的数据设置入listview
			@Override
			public void handleMessage(Message msg)
			{
				if (msg.what == 1)
				{
					Dialog.cancel();
					city_text.setText(city_str);
					listView.setAdapter(adapter);
				}
			}
		};

		new Thread()
		{

			public void run()
			{
				// 子线程的循环标志位
				Looper.prepare();

				try
				{
					Thread.sleep(500);

				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}

				// 处理网页数据
				urlconn(path, web);

				// 给handle发送的消息
				Message m = new Message();
				m.what = 1;
				mHandler.sendMessage(m);

				Looper.loop();
			};

		}.start();

	}

	/**
	 * 处理网页XML数据
	 * 
	 * @param path
	 *            网页地址
	 * @param website
	 *            网站类型 0为美团,1为拉手,2为F团,3为糯米
	 */
	private void urlconn(String path, int website)
	{

		try
		{
			URL url = new URL(path);
			HttpURLConnection httpConn = (HttpURLConnection) url
					.openConnection();

			int responseCode = httpConn.getResponseCode();

			if (responseCode == 200)
			{
				InputStream input = httpConn.getInputStream();

				meituans = tuan_handler.getListMeituan(input, website);

				setInListView(); // 将数据加入listview的adapter中

				input.close();
				httpConn.disconnect();

			}
		} catch (Exception e)
		{
			Log.e(TAG, e.toString());
			Toast.makeText(this, "您的网络连接出错，请确认你的网络已打开", 1).show();
		}

	}

	/**
	 * 将数据绑定到ListView上
	 */
	private void setInListView()
	{
		List<Map<String, String>> lists = new ArrayList<Map<String, String>>();

		for (meituan mei : meituans)
		{
			Map<String, String> map = new HashMap<String, String>();
			map.put("Website", "【" + mei.getWebsite() + "】");
			map.put("Deal_title", mei.getDeal_title());
			map.put("Price", "现价 : ¥ " + mei.getPrice());
			map.put("Value", "原价 : ¥ " + mei.getValue());
			map.put("Rebate", "折扣 : " + mei.getRebate());

			lists.add(map);

		}

		adapter = new SimpleAdapter(tuangouActivity.this, lists, R.layout.item,
				new String[] { "Website", "Deal_title", "Price", "Value",
						"Rebate" }, new int[] { R.id.website, R.id.deal_title,
						R.id.price, R.id.value, R.id.rebate });

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// return super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.back:
			this.finish();
			break;

		case R.id.mycollect:
			Intent intent = new Intent();
			intent.setAction("com.kang.mycollectActivity");
			startActivity(intent);
			break;

		case R.id.about:
			AlertDialog.Builder dialog = new Builder(this);
			dialog.setTitle(R.string.author);
			// 装载/res/layout/author.xml
			final LinearLayout author = (LinearLayout) getLayoutInflater().inflate(
					R.layout.author, null);
			

			dialog.setView(author);
			dialog.setPositiveButton("确定",
					new DialogInterface.OnClickListener()
					{

						public void onClick(DialogInterface dialog, int which)
						{
							
							EditText edit_suggest = (EditText)author.findViewById(R.id.edit_suggest);
							String suggest = edit_suggest.getText().toString();
							try
							{
								final Intent emailIntent = new Intent(
										android.content.Intent.ACTION_SEND);
								emailIntent.setType("plain/text");
								emailIntent.putExtra(
										android.content.Intent.EXTRA_EMAIL,
										"kangkangz4@126.com");
								emailIntent.putExtra(
										android.content.Intent.EXTRA_SUBJECT,
										"团购客户端意见");
								emailIntent.putExtra(
										android.content.Intent.EXTRA_TEXT,
										suggest);
								startActivity(Intent.createChooser(emailIntent,
										"邮件发送中...."));
							} catch (Exception e)
							{
								Log.e(TAG, e.toString());
								Toast.makeText(tuangouActivity.this,
										"请设置您手机里的邮件地址", 1).show();
							}

						}
					});
			dialog.setNegativeButton("取消",
					new DialogInterface.OnClickListener()
					{

						public void onClick(DialogInterface dialog, int which)
						{
							dialog.dismiss();

						}
					});
			dialog.show();

			break;

		case R.id.eixt:

			this.finish();

			break;

		}

		return true;
	}

	// listViw的点击事件
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3)
	{
		// 取得当前点的数据
		meituan mei = meituans.get(position);

		// 通赤bundle将mei的序列化数据给intent;
		Bundle bundle = new Bundle();
		bundle.putSerializable("mei", mei);

		Intent intent = new Intent();
		intent.putExtras(bundle);

		intent.setAction("com.kang.showdescActivity"); // 通过隐式intent来打开shwodescActivity
		// intent.setClass(tuangouActivity.this,
		// showdescActivity.class);
		startActivity(intent);

	}

	/**
	 * 读取存储在SharedPreferences的city_postiotn的数据
	 */
	private void ReadSharedPreferences()
	{
		SharedPreferences city_info = getSharedPreferences("city_info", 0);
		city_position = city_info.getInt("CITY_POSITION", 0);
	}

	/**
	 * 将city_position的数据存储在SharedPreferences里
	 * 
	 * @param city_position
	 *            城市位置编号
	 */
	private void WriteSharedPreferences(int city_position)
	{
		SharedPreferences city_info = getSharedPreferences("city_info", 0);
		SharedPreferences.Editor edit = city_info.edit();
		edit.putInt("CITY_POSITION", city_position);
		edit.commit();
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		WriteSharedPreferences(city_position);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		WriteSharedPreferences(city_position);
	}

}
