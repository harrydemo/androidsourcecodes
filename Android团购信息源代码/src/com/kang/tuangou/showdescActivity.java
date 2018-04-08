package com.kang.tuangou;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kang.database.DataIUDS;
import com.kang.meituan.meituan;

public class showdescActivity extends Activity implements OnClickListener
{
	private static final String TAG = "showdescActivity";
	private TextView deal_title, website, deal_desc, price, value, rebate,
			sales_num, shop_addr, shop_name, shop_tel, start_time, end_time,
			last_time;

	private Button back_bt, collect_bt;
	private ImageView image;
	private meituan mei;

	private Button buy, fav;

	private String addr, name, tel;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.showdescription);

		Bundle bundle = getIntent().getExtras();
		mei = (meituan) bundle.getSerializable("mei");

		System.out.println(mei.getUrl());

		deal_title = (TextView) findViewById(R.id.deal_title);
		deal_title.setText(mei.getDeal_title());

		website = (TextView) findViewById(R.id.website);
		website.setText("【" + mei.getWebsite() + "】");

		deal_desc = (TextView) findViewById(R.id.deal_desc);
		deal_desc.setText(mei.getDeal_desc());

		// 图片处理
		image = (ImageView) findViewById(R.id.image);
		image.setImageBitmap(getBitmap(mei.getDeal_img()));

		start_time = (TextView) findViewById(R.id.start_time);
		start_time.setText(formatTime(mei.getStart_time()));

		end_time = (TextView) findViewById(R.id.end_time);
		end_time.setText(formatTime(mei.getEnd_time()));

		last_time = (TextView) findViewById(R.id.last_time);
		last_time.setText(getTime(System.currentTimeMillis() / 1000,
				mei.getEnd_time()));

		price = (TextView) findViewById(R.id.price);
		price.setText("现价 : ¥ " + mei.getPrice());

		value = (TextView) findViewById(R.id.value);
		value.setText("原价 : ¥ " + mei.getValue());
		value.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); // 设置中划线

		rebate = (TextView) findViewById(R.id.rebate);
		rebate.setText("折扣 : " + mei.getRebate());

		sales_num = (TextView) findViewById(R.id.sales_num);
		sales_num.setText("已经有     " + mei.getSales_num() + "  人购买");

		if (mei.getShop_addr() == null)
		{
			addr = "未知";
		} else
		{
			addr = mei.getShop_addr();
		}

		shop_addr = (TextView) findViewById(R.id.shop_addr);
		shop_addr.setText("地址 : " + addr);

		if (mei.getShop_name() == null)
		{
			name = "未知";
		} else
		{
			name = mei.getShop_name();
		}
		shop_name = (TextView) findViewById(R.id.shop_name);
		shop_name.setText("商家名称 : " + name);

		if (mei.getShop_tel() == null)
		{
			tel = "未知";
		} else
		{
			tel = mei.getShop_tel();
		}
		shop_tel = (TextView) findViewById(R.id.shop_tel);
		shop_tel.setText("商家电话 : " + tel);

		fav = (Button) findViewById(R.id.fav);
		fav.setOnClickListener(this);

		back_bt = (Button) findViewById(R.id.back);
		back_bt.setOnClickListener(this);

		collect_bt = (Button) findViewById(R.id.collect);
		collect_bt.setOnClickListener(this);

		buy = (Button) findViewById(R.id.buy);
		buy.setOnClickListener(this);
	}

	/**
	 * 按钮事件
	 */
	public void onClick(View v)
	{
		if (v == fav)
		{

			try
			{
				DataIUDS data = new DataIUDS(showdescActivity.this);
				data.save(mei); // 数据保存到数据库
				Toast.makeText(showdescActivity.this, "收藏添加成功", 1).show();
			} catch (Exception e)
			{
				Log.e(TAG, e.toString());
				Toast.makeText(showdescActivity.this, "收藏添加失败，请重新添加", 1).show();
			}

		} else if (v == back_bt)
		{

			this.finish();

		} else if (v == collect_bt)
		{
			
			Intent intent = new Intent();
			intent.setAction("com.kang.mycollectActivity");
			startActivity(intent);
			
		} else if (v == buy)
		{
			 String url = mei.getUrl();   
			 Intent intent = new Intent(Intent.ACTION_VIEW);   
			 intent.setData(Uri.parse(url));   
			 startActivity(intent);  
		}

	}

	/**
	 * 将长整型的时间1324322343格式转成2011-8-29
	 * 
	 * @param time
	 *            长整型时间
	 * @return
	 */
	private String formatTime(long time)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(time * 1000);
		String data = sdf.format(date);
		return data;
	}

	/**
	 * 通过时间计算还有多少天多少小时
	 * 
	 * @param start_time
	 *            开始时间
	 * @param end_time
	 *            结束时间
	 * @return
	 */
	private String getTime(long start_time, long end_time)
	{
		long last_time = end_time - start_time;
		long day = last_time / (24 * 60 * 60);
		long hour = (last_time / (60 * 60) - day * 24);

		String str = "还有" + day + "天" + hour + "小时";
		return str;
	}

	/**
	 * 从网络地址取得图片
	 * 
	 * @param path
	 *            网络地址
	 * @return
	 */

	private Bitmap getBitmap(String path)
	{
		try
		{
			URL url = new URL(path);
			HttpURLConnection httpConn = (HttpURLConnection) url
					.openConnection();

			InputStream input = httpConn.getInputStream();
			Bitmap image = BitmapFactory.decodeStream(input);

			return image;

		} catch (Exception e)
		{
			Log.i(TAG, e.toString());
		}

		return null;
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
								Toast.makeText(showdescActivity.this,
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
