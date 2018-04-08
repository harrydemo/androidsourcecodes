package com.kang.tuangou;
//Download by http://www.codefans.net
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.kang.database.DataIUDS;
import com.kang.meituan.meituan;

public class mycollectActivity extends Activity implements OnClickListener,
		OnItemClickListener, OnItemLongClickListener
{
	private final static String TAG = "mycollectActivity";
	private ListView listView;
	private Button back_bt, back_main_bt, delete_bt;
	private boolean noData;
	private SimpleAdapter adapter;
	private List<meituan> meituans;
	private DataIUDS data;
	private meituan mei;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.mycollect);

		listView = (ListView) findViewById(R.id.listview);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
		setInListView();
		registerForContextMenu(listView);

		back_bt = (Button) findViewById(R.id.back);
		back_bt.setOnClickListener(this);

		back_main_bt = (Button) findViewById(R.id.back_main);
		back_main_bt.setOnClickListener(this);

		delete_bt = (Button) findViewById(R.id.delete);
		delete_bt.setOnClickListener(this);
	}

	/**
	 * 将数据库中的数据取出，并放入listView的adapter中
	 */
	private void setInListView()
	{
		data = new DataIUDS(this);

		meituans = new ArrayList<meituan>();
		meituans = data.getListData(0, 20);
		noData = meituans.isEmpty() ? true : false;

		if (noData)
		{
			Toast.makeText(this, "您暂时还没有收藏，请重新添加", 1).show();
		} else
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

			adapter = new SimpleAdapter(this, lists, R.layout.item,
					new String[] { "Website", "Deal_title", "Price", "Value",
							"Rebate" }, new int[] { R.id.website,
							R.id.deal_title, R.id.price, R.id.value,
							R.id.rebate });

			listView.setAdapter(adapter);
		}

	}

	/**
	 * ListView的点击事件
	 */
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3)
	{
		start_showdescActivity(position);
	}

	/**
	 * 打开当前选择选项的具体数据
	 * 
	 * @param position
	 *            meituans列表的数据
	 */
	public void start_showdescActivity(int position)
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
	 * 按钮事件
	 */
	public void onClick(View v)
	{
		if (v == back_bt)
		{
			this.finish();
		} else if (v == back_main_bt)
		{
			Intent intent = new Intent();
			intent.setClass(this, tuangouActivity.class);
			startActivity(intent);
			this.finish();

		} else if (v == delete_bt)   //全部删除按钮事件
		{
			AlertDialog.Builder dialog = new Builder(this);
			dialog.setTitle("数据删除").setMessage("您确定要删除全部数据吗？");
			dialog.setPositiveButton("确定",
					new DialogInterface.OnClickListener()
					{

						public void onClick(DialogInterface dialog, int which)
						{
							data.deleteAllData();
							Toast.makeText(mycollectActivity.this, "数据已经全部删除",
									1).show();
							finish();
							Intent intent = new Intent();
							intent.setAction("com.kang.mycollectActivity");
							startActivity(intent);
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

		}

	}

	public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
			final int position, long arg3)
	{

		String[] string = { "打开", "删除当前" };
		// 取得当前点的数据
		mei = meituans.get(position);
		// Toast.makeText(this, "当前数据ID为" + mei.getId(), 1).show();

		AlertDialog.Builder dialog = new Builder(this);
		dialog.setTitle("请选择").setItems(string,
				new DialogInterface.OnClickListener()
				{

					public void onClick(DialogInterface dialog, int which)
					{

						// 删除当前选项所选择的数据
						if (which == 1)
						{
							new Builder(mycollectActivity.this)
									.setTitle("数据删除")
									.setMessage("您确定要删除当前数据吗？")
									.setPositiveButton(
											"确定",
											new DialogInterface.OnClickListener()
											{

												public void onClick(
														DialogInterface dialog,
														int which)
												{

													data.delete(mei.getId());
													finish();
													Intent intent = new Intent();
													intent.setAction("com.kang.mycollectActivity");
													startActivity(intent);
												}
											})
									.setNegativeButton(
											"取消",
											new DialogInterface.OnClickListener()
											{

												public void onClick(
														DialogInterface dialog,
														int which)
												{
													dialog.dismiss();
												}
											}).show();

						} else if (which == 0)   //打开事件处理
						{
							start_showdescActivity(position);
						}
					}
				});
		dialog.show();

		return false;
	}
	
	
	public boolean onCreateOptionsMenu(Menu menu)
	{
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
			
		case R.id.back_main:
			Intent intent2 = new Intent();
			intent2.setClass(this, tuangouActivity.class);
			startActivity(intent2);
			
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
								Toast.makeText(mycollectActivity.this,
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
			System.out.println("exit");
			// ActivityManager am = (ActivityManager)
			// getSystemService(ACTIVITY_SERVICE);
			// am.killBackgroundProcesses("com.kang.tuangou");
			Intent intent1 = new Intent(Intent.ACTION_MAIN);
			intent1.addCategory(Intent.CATEGORY_HOME);
			intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent1);

			break;

		}

		return true;
	}

}
