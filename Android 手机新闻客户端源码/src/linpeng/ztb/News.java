package linpeng.ztb;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RemoteViews.ActionException;
import android.widget.TextView;
import android.widget.Toast;

public class News extends Activity {

	private TextView news_details_text, news_details_title, news_details_time;
	private ProgressBar news_details_progress;
	private int flag = 1, arg2, text_size;// flag=1表示从网络获取数据=0表示从数据库获取数据
	private String kind, url;
	private String newsdetailstext = "", downloadtext = "",
			downloadaddress = "", type;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.arg1) {
			case 1:
				if (newsdetailstext.length() > 3) {
					putData();
				} else {
					Toast.makeText(News.this, "网络不通，请稍后再试", Toast.LENGTH_SHORT)
							.show();
				}
				news_details_text.setText(newsdetailstext);
				news_details_progress.setVisibility(8);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.news);
		Intent intent = getIntent();

		SharedPreferences s = getSharedPreferences("text_size", 0);
		text_size = s.getInt("text_size", 20);

		url = intent.getStringExtra("newsurl");
		type = intent.getStringExtra("type");
		String newstitle = intent.getStringExtra("newstitle");
		String newstime = intent.getStringExtra("newstime");

		news_details_text = (TextView) findViewById(R.id.news_details_text);
		news_details_title = (TextView) findViewById(R.id.news_details_title);
		news_details_time = (TextView) findViewById(R.id.news_details_time);
		news_details_progress = (ProgressBar) findViewById(R.id.news_details_progress);
		news_details_progress.setVisibility(0);
		news_details_title.setText(newstitle);
		news_details_time.setText(newstime);
		news_details_text.setTextSize(text_size);

		flag = 0;

		DataBaseHelper dbh = new DataBaseHelper(this,
				new IntToStrong().TypeToType(type), null, 1);
		SQLiteDatabase sql = dbh.getReadableDatabase();
		Cursor cursor = sql.query(new IntToStrong().TypeToType(type),
				new String[] { "newsurl", "newstitle", "newstime",
						"newsdetails", "newsdownloadtext1", "newsdownloadurl1",
						"newsdownloadtext2", "newsdownloadurl2" }, "newsurl=?",
				new String[] { url }, null, null, null);
		cursor.moveToNext();
		if (cursor.getCount() == 0) {
			new NewsThread().start();
		} else {
			news_details_text.setText(cursor.getString(cursor
					.getColumnIndex("newsdetails")));
			news_details_progress.setVisibility(8);
		}
		if (sql != null) {
			sql.close();
		}
	}

	protected void putData() {
		DataBaseHelper dbh = new DataBaseHelper(this,
				new IntToStrong().TypeToType(type), null, 1);
		SQLiteDatabase sql = dbh.getWritableDatabase();
		dbh.addetails(url, news_details_title.getText().toString(),
				news_details_time.getText().toString(), newsdetailstext, null,
				null, null, null);
		if (dbh != null) {
			dbh.close();
			sql.close();
		}
	}

	private void getNews() {
		try {
			String host = android.net.Proxy.getDefaultHost();
			int port = android.net.Proxy.getDefaultPort();
			Document doc = Jsoup.connect(url).get();
			Element ele = doc.getElementById("TDContent");
			int j = 1;
			if (ele.children().text().length() < 2) {
				System.out.println(1);
				newsdetailstext = newsdetailstext + ele.text();
			}
			for (Element ele2 : ele.children()) {
				j++;
				newsdetailstext = newsdetailstext + ele2.text();
				newsdetailstext = newsdetailstext + "\n";
				newsdetailstext = newsdetailstext + "\n";
			}
			if (j == 2) {
				newsdetailstext = "";
				for (Element ele2 : ele.children()) {
					for (Element ele3 : ele2.children()) {
						System.out.println(3);
						newsdetailstext = newsdetailstext + ele3.text();
						newsdetailstext = newsdetailstext + "\n";
						newsdetailstext = newsdetailstext + "\n";

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class NewsThread extends Thread {

		@Override
		public void run() {
			getNews();
			Message msg = handler.obtainMessage();
			msg.arg1 = 1;
			handler.sendMessage(msg);

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(menu.NONE, 1, 1, "字体大小");
		menu.add(menu.NONE, 2, 1, "使用浏览器打开此页面");
		menu.add(menu.NONE, 3, 1, "拨打此页面中电话");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			Builder builder = new Builder(this);
			builder.setTitle("字体大小");
			builder.setSingleChoiceItems(new String[] { "大", "中", "小" },
					new IntToStrong().text_size_to_order(text_size),
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {

							if (which == 0) {
								news_details_text.setTextSize(24);
								SharedPreferences sp = getSharedPreferences(
										"text_size", 0);
								sp.edit().putInt("text_size", 24).commit();
								dialog.cancel();
							} else if (which == 1) {
								news_details_text.setTextSize(20);
								SharedPreferences sp = getSharedPreferences(
										"text_size", 0);
								sp.edit().putInt("text_size", 20).commit();
								dialog.cancel();
							} else if (which == 2) {
								news_details_text.setTextSize(16);
								SharedPreferences sp = getSharedPreferences(
										"text_size", 0);
								sp.edit().putInt("text_size", 16).commit();
								dialog.cancel();
							}
						}
					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {

						}
					});
			builder.show();
			break;
		case 2:
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			startActivity(intent);
			break;
		case 3:
			Builder builder2=new Builder(this);
			Map telephone=getTelePhone();
			//LayoutInflater inflater=getLayoutInflater();
			//View layout=inflater.inflate(R.layout.telephone,(ViewGroup)findViewById(R.id.news_tele));
			
			Intent intent1 = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+ telephone.get("tele"+1)));
			startActivity(intent1);
			//builder2.setView(layout);
			//builder2.show();
			break;
		default:
			break;
		}
		return true;
	}

	private Map getTelePhone() {
		Map telephone = new HashMap<String, String>();
		if (news_details_text.length() > 5) {
			int temp_index_telephone=1;
			String text = news_details_text.getText().toString();
			for (int i = 1; i <= text.length() - 1; i++) {
				if (text.charAt(i) == '电' && text.charAt(i + 1) == '话'
						|| text.charAt(i) == '电'
						&& text.charAt(i + 2) == '话') {

					int k = 3;
					if(text.charAt(i+2)=='话'){
						k=4;
					}
					String temp_telephone="";
					while (text.charAt(i + k) == '1'
							|| text.charAt(i + k) == '2'
							|| text.charAt(i + k) == '3'
							|| text.charAt(i + k) == '4'
							|| text.charAt(i + k) == '5'
							|| text.charAt(i + k) == '6'
							|| text.charAt(i + k) == '7'
							|| text.charAt(i + k) == '8'
							|| text.charAt(i + k) == '9'
							|| text.charAt(i + k) == '-'
							|| text.charAt(i + k) == '0') {
						temp_telephone=temp_telephone+text.charAt(i+k);
						k++;
					}
					if(temp_telephone.length()>5){
						temp_telephone=temp_telephone.replace("-","");
						telephone.put("tele"+temp_index_telephone, temp_telephone);
						Log.i("sasaa", "" + temp_telephone);
						temp_index_telephone++;
						temp_telephone="";
					}
				}
			}
		}
		return telephone;
	}

}
