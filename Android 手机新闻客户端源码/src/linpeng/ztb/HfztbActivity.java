package linpeng.ztb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class HfztbActivity extends Activity implements OnClickListener {
	private GridView xml_gridview;
	private ListView xml_newslist;
	private Button foot_loadmore, prepare_refresh, gotoright, up, down,
			gotoother, gotoother2, gotoother3, gotoother4, gotoother5;
	private TextView foot_text;
	private ProgressBar foot_progressbar;
	private HorizontalScrollView horizontalScrollView;
	private SimpleAdapter listview_adapter;
	private ProgressBar refresh;
	private String list_button = "title1";
	private int changenewslist = 1;
	private int[][] location = new int[5][2];
	private boolean fist_open_title1 = true, fist_open_title2 = true,
			fist_open_title3 = true, fist_open_title4 = true,
			fist_open_title5 = true, fist_open_title6 = true,
			fist_open_title7 = true, goto_other_visiably = false;
	private boolean is_first_open = true;
	private final int FLINGDIS = 900;
	private List<Map<String, Object>> newslist = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> tempnewslist = new ArrayList<Map<String, Object>>();
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.arg1 > -1) {
				prepare_refresh.setVisibility(0);
				refresh.setVisibility(8);
				Log.i("list_button", new IntToStrong().getname(msg.arg1)
						+ list_button);
				changeFirstOpen(msg.arg1);
				if (list_button.equals(new IntToStrong().getname(msg.arg1))) {
					getNewsList(msg.arg1, false);
				}
			}
			if (msg.arg1 == -1) {
				prepare_refresh.setVisibility(0);
				refresh.setVisibility(8);
				Toast.makeText(HfztbActivity.this, "网络不通，请稍候再试",
						Toast.LENGTH_SHORT).show();
			}
		}

		private void changeFirstOpen(int arg1) {
			if (arg1 == 0 && fist_open_title1) {
				fist_open_title1 = false;
			} else if (arg1 == 1 && fist_open_title2) {
				fist_open_title2 = false;
			} else if (arg1 == 2 && fist_open_title3) {
				fist_open_title3 = false;
			} else if (arg1 == 3 && fist_open_title4) {
				fist_open_title4 = false;
			} else if (arg1 == 4 && fist_open_title5) {
				fist_open_title5 = false;
			} else if (arg1 == 5 && fist_open_title6) {
				fist_open_title6 = false;
			} else if (arg1 == 6 && fist_open_title7) {
				fist_open_title7 = false;
			}
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		xml_gridview = (GridView) findViewById(R.id.xml_gridview);
		xml_gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		xml_newslist = (ListView) findViewById(R.id.newslist);
		prepare_refresh = (Button) findViewById(R.id.prepare_refresh);
		refresh = (ProgressBar) findViewById(R.id.refresh);
		gotoright = (Button) findViewById(R.id.gotoright);
		horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
		up = (Button) findViewById(R.id.up);
		down = (Button) findViewById(R.id.down);
		gotoother = (Button) findViewById(R.id.gotoother);
		gotoother2 = (Button) findViewById(R.id.gotoother2);
		gotoother3 = (Button) findViewById(R.id.gotoother3);
		gotoother4 = (Button) findViewById(R.id.gotoother4);
		gotoother5 = (Button) findViewById(R.id.gotoother5);

		LayoutInflater layoutInflater = getLayoutInflater();
		View footView = layoutInflater.inflate(R.layout.foot, null);
		xml_newslist.addFooterView(footView);

		foot_loadmore = (Button) findViewById(R.id.foot_loadmore);
		foot_text = (TextView) findViewById(R.id.foot_text);
		foot_progressbar = (ProgressBar) findViewById(R.id.foot_progressbar);
		foot_loadmore.setOnClickListener(this);
		prepare_refresh.setOnClickListener(this);
		gotoright.setOnClickListener(this);
		up.setOnClickListener(this);
		down.setOnClickListener(this);
		gotoother.setOnClickListener(this);
		gotoother2.setOnClickListener(this);
		gotoother3.setOnClickListener(this);
		gotoother4.setOnClickListener(this);
		gotoother5.setOnClickListener(this);

		getNewsList(0, false);

		SimpleAdapter gridview_adapter = new SimpleAdapter(this,
				new ListData().getGridviewdata(), R.layout.gridview,
				new String[] { "grid_title" }, new int[] { R.id.grid_title });

		listview_adapter = new SimpleAdapter(this, newslist,
				R.layout.newslistdetails, new String[] { "news_isend",
						"news_title", "news_time" }, new int[] {
						R.id.news_isend, R.id.news_title, R.id.news_time });

		is_first_open = false;

		xml_gridview.setAdapter(gridview_adapter);
		xml_gridview.setOnItemClickListener(new onGridViewItemClick());
		xml_newslist.setOnItemClickListener(new onListViewItemClick());
		xml_newslist.setAdapter(listview_adapter);
	}

	private void getloction() {
		gotoother.getLocationOnScreen(location[0]);
		gotoother2.getLocationOnScreen(location[1]);
		gotoother3.getLocationOnScreen(location[2]);
		gotoother4.getLocationOnScreen(location[3]);
		gotoother5.getLocationOnScreen(location[4]);
		Log.i("x", location[0][0] + " " + location[0][1]);
	}

	private void getNewsList(int table_number, boolean is_fresh) {
		boolean has_fresh = false;
		String table_name = new IntToStrong().getname(table_number);
		DataBaseHelper dbh = new DataBaseHelper(this, table_name, null, 1);
		SQLiteDatabase sql = dbh.getReadableDatabase();
		Cursor cursor = sql.query(table_name, new String[] { "newsclass",
				"newstitle", "newstime", "isread" }, "newsclass>?",
				new String[] { "-1" }, null, null, null);
		if (cursor.getCount() != 0 && is_fresh == false) {
			newslist.clear();
			getNewsListByDatabase(cursor);
			prepare_refresh.setVisibility(0);
			refresh.setVisibility(8);
			if (!is_first_open) {
				listview_adapter.notifyDataSetChanged();
				xml_newslist.setSelection(0);
				list_button = new IntToStrong().getname(table_number);
			}
		} else {
			list_button = new IntToStrong().getname(table_number);
			MyThread myThread = new MyThread(table_number, HfztbActivity.this);
			has_fresh = true;
			myThread.start();
		}
		if (!has_fresh) {
			reFresh(table_name, table_number);
		}
		if (sql != null) {
			sql.close();
		}
		if (dbh != null) {
			dbh.close();
		}
	}

	private void reFresh(String table_name, int table_number) {
		if (fist_open_title1 && table_name == "title1") {
			MyThread myThread = new MyThread(table_number, HfztbActivity.this);
			myThread.start();
		} else if (fist_open_title2 && table_name == "title2") {
			MyThread myThread = new MyThread(table_number, HfztbActivity.this);
			myThread.start();
		} else if (fist_open_title3 && table_name == "title3") {
			MyThread myThread = new MyThread(table_number, HfztbActivity.this);
			myThread.start();
		} else if (fist_open_title4 && table_name == "title4") {
			MyThread myThread = new MyThread(table_number, HfztbActivity.this);
			myThread.start();
		} else if (fist_open_title5 && table_name == "title5") {
			MyThread myThread = new MyThread(table_number, HfztbActivity.this);
			myThread.start();
		} else if (fist_open_title6 && table_name == "title6") {
			MyThread myThread = new MyThread(table_number, HfztbActivity.this);
			myThread.start();
		} else if (fist_open_title7 && table_name == "title7") {
			MyThread myThread = new MyThread(table_number, HfztbActivity.this);
			myThread.start();
		}
	}

	private void getNewsListByDatabase(Cursor cursor) {
		newslist.clear();
		while (cursor.moveToNext()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("news_title",
					cursor.getString(cursor.getColumnIndex("newstitle")));
			map.put("news_time",
					cursor.getString(cursor.getColumnIndex("newstime")));
			map.put("news_isend",
					cursor.getString(cursor.getColumnIndex("isread")));
			newslist.add(map);
		}
	}

	class onGridViewItemClick implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			int table_number = arg2;
			TextView gridviewback = (TextView) arg1;
			for (int i = 0; i < arg0.getCount(); i++) {
				TextView gridview_text_temp = (TextView) arg0.getChildAt(i);
				gridview_text_temp.setBackgroundDrawable(null);
				gridview_text_temp.setTextColor(getResources().getColor(
						R.color.grid_title_color));
			}
			gridviewback.setBackgroundResource(R.drawable.gridviewbackground);
			gridviewback.setTextColor(Color.WHITE);
			if (arg2 >= 0 && arg2 <= 6) {
				getNewsList(table_number, false);
				Log.i("after", "thread");
			}
		}
	}

	class onListViewItemClick implements OnItemClickListener {

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			DataBaseHelper dbh = new DataBaseHelper(HfztbActivity.this,
					list_button, null, 1);
			SQLiteDatabase sq = dbh.getReadableDatabase();
			Cursor cursor = sq.query(list_button, new String[] { "newsclass",
					"newstitle", "newstime", "isread", "url" }, "newsclass=?",
					new String[] { arg2 + "" }, null, null, null);
			cursor.moveToNext();
			Intent intent = new Intent(HfztbActivity.this, News.class);// 跳转到新闻页
			if (!cursor.isAfterLast()) {
				intent.putExtra("newsurl",
						cursor.getString(cursor.getColumnIndex("url")));
				intent.putExtra("newstitle",
						cursor.getString(cursor.getColumnIndex("newstitle")));
				intent.putExtra("newstime",
						cursor.getString(cursor.getColumnIndex("newstime")));
				intent.putExtra("type", list_button);
				startActivity(intent);
			}
			if (dbh != null) {
				dbh.close();
				sq.close();
			}
		}
	}

	public class MyThread extends Thread {
		private int table_number;
		private Context context;

		public MyThread(int table_number, Context context) {
			this.table_number = table_number;
			this.context = context;
		}

		@Override
		public void run() {
			getNewsList(table_number);
		}

		protected void getNewsList(int table_number) {
			try {
				Document doc;
				String url = "http://www.hfztb.cn/hfzbtb/jyxx/002002/00200200"
						+ (table_number + 1) + "/";
				doc = Jsoup.connect(url).get();
				new ListData().getListData(doc, table_number, context, url);

				Message msg = handler.obtainMessage();
				msg.arg1 = table_number;
				handler.sendMessage(msg);
			} catch (IOException e) {
				Log.i("tag", "error");
				Message msg = handler.obtainMessage();
				msg.arg1 = -1;
				handler.sendMessage(msg);
				e.printStackTrace();
			}
		}
	}

	public void onClick(View v) {
		if (v.getId() == foot_loadmore.getId()) {
			foot_loadmore.setVisibility(8);
			foot_text.setVisibility(0);
			foot_progressbar.setVisibility(0);
		}
		if (v.getId() == prepare_refresh.getId()) {
			prepare_refresh.setVisibility(8);
			refresh.setVisibility(0);
			getNewsList(new IntToStrong().table_name_to_int(list_button), true);
		}
		if (v.getId() == gotoright.getId()) {
			horizontalScrollView.fling(FLINGDIS);
			Log.i("" + horizontalScrollView.getScrollY(),
					horizontalScrollView.getScrollX() + "");
		}
		if (v.getId() == up.getId()) {
			xml_newslist.setSelection(0);
		}
		if (v.getId() == down.getId()) {
			Log.i("asff", xml_newslist.getScrollY() + "");
			xml_newslist.setSelection(1000);
		}
		if (v.getId() == gotoother.getId()) {
			getloction();
			if (!goto_other_visiably) {
				setVisiablyAndAnimation(false, -1);
			} else {
				Log.i("x", location[0][0] + " " + location[0][1]);
				setVisiablyAndAnimation(true, 0);
			}
		}
		if (v.getId() == gotoother2.getId()) {
			setVisiablyAndAnimation(false, 0);
		}
		if (v.getId() == gotoother3.getId()) {
			setVisiablyAndAnimation(false, 1);
		}
		if (v.getId() == gotoother4.getId()) {
			setVisiablyAndAnimation(false, 2);
		}
		if (v.getId() == gotoother5.getId()) {
			setVisiablyAndAnimation(false, 3);
		}
	}

	private void setVisiablyAndAnimation(boolean b, int which) {
		if (!b && which > -1) {
			ScaleAnimation[] scaleAnimations = getScaleAnimation(which);
			gotoother2.startAnimation(scaleAnimations[0]);
			gotoother3.startAnimation(scaleAnimations[1]);
			gotoother4.startAnimation(scaleAnimations[2]);
			gotoother5.startAnimation(scaleAnimations[3]);

			gotoother2.setVisibility(8);
			gotoother3.setVisibility(8);
			gotoother4.setVisibility(8);
			gotoother5.setVisibility(8);
			goto_other_visiably = false;
		} else if (which == -1) {
			TranslateAnimation[] translateAnimations = getTranslateAnimation(which);
			gotoother2.startAnimation(translateAnimations[0]);
			gotoother3.startAnimation(translateAnimations[1]);
			gotoother4.startAnimation(translateAnimations[2]);
			gotoother5.startAnimation(translateAnimations[3]);
			gotoother2.setVisibility(0);
			gotoother3.setVisibility(0);
			gotoother4.setVisibility(0);
			gotoother5.setVisibility(0);
			goto_other_visiably = true;
		} else {
			TranslateAnimation[] translateAnimations = getTranslateAnimation(which);
			gotoother2.startAnimation(translateAnimations[0]);
			gotoother3.startAnimation(translateAnimations[1]);
			gotoother4.startAnimation(translateAnimations[2]);
			gotoother5.startAnimation(translateAnimations[3]);
			gotoother2.setVisibility(8);
			gotoother3.setVisibility(8);
			gotoother4.setVisibility(8);
			gotoother5.setVisibility(8);
			goto_other_visiably = false;
		}
	}

	private TranslateAnimation[] getTranslateAnimation(int which) {
		TranslateAnimation[] translateAnimations = new TranslateAnimation[4];
		if (which == -1) {
			translateAnimations[0] = new TranslateAnimation(Animation.ABSOLUTE,
					location[0][0] - location[1][0],
					Animation.RELATIVE_TO_SELF, 0f, Animation.ABSOLUTE,
					location[0][1] - location[1][1],
					Animation.RELATIVE_TO_SELF, 0f);
			translateAnimations[1] = new TranslateAnimation(Animation.ABSOLUTE,
					location[0][0] - location[2][0],
					Animation.RELATIVE_TO_SELF, 0f, Animation.ABSOLUTE,
					location[0][1] - location[2][1],
					Animation.RELATIVE_TO_SELF, 0f);
			translateAnimations[2] = new TranslateAnimation(Animation.ABSOLUTE,
					location[0][0] - location[3][0],
					Animation.RELATIVE_TO_SELF, 0f, Animation.ABSOLUTE,
					location[0][1] - location[3][1],
					Animation.RELATIVE_TO_SELF, 0f);
			translateAnimations[3] = new TranslateAnimation(Animation.ABSOLUTE,
					location[0][0] - location[4][0],
					Animation.RELATIVE_TO_SELF, 0f, Animation.ABSOLUTE,
					location[0][1] - location[4][1],
					Animation.RELATIVE_TO_SELF, 0f);
			translateAnimations[0].setDuration(100);
			translateAnimations[1].setDuration(150);
			translateAnimations[2].setDuration(200);
			translateAnimations[3].setDuration(250);
		} else {

			translateAnimations[0] = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0f, Animation.ABSOLUTE,
					location[0][0] - location[1][0],
					Animation.RELATIVE_TO_SELF, 0f, Animation.ABSOLUTE,
					location[0][1] - location[1][1]);
			translateAnimations[1] = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0f, Animation.ABSOLUTE,
					location[0][0] - location[2][0],
					Animation.RELATIVE_TO_SELF, 0f, Animation.ABSOLUTE,
					location[0][1] - location[2][1]);
			translateAnimations[2] = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0f, Animation.ABSOLUTE,
					location[0][0] - location[3][0],
					Animation.RELATIVE_TO_SELF, 0f, Animation.ABSOLUTE,
					location[0][1] - location[3][1]);
			translateAnimations[3] = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0f, Animation.ABSOLUTE,
					location[0][0] - location[4][0],
					Animation.RELATIVE_TO_SELF, 0f, Animation.ABSOLUTE,
					location[0][1] - location[4][1]);
			translateAnimations[0].setDuration(250);
			translateAnimations[1].setDuration(200);
			translateAnimations[2].setDuration(150);
			translateAnimations[3].setDuration(100);
		}
		return translateAnimations;
	}

	private ScaleAnimation[] getScaleAnimation(int which) {
		ScaleAnimation[] scaleAnimations = new ScaleAnimation[4];
		scaleAnimations[0] = new ScaleAnimation(1, 0.1f, 1, 0.1f);
		scaleAnimations[1] = new ScaleAnimation(1, 0.1f, 1, 0.1f);
		scaleAnimations[2] = new ScaleAnimation(1, 0.1f, 1, 0.1f);
		scaleAnimations[3] = new ScaleAnimation(1, 0.1f, 1, 0.1f);
		scaleAnimations[which] = new ScaleAnimation(1, 1.7f, 1, 1.7f);
		scaleAnimations[0].setDuration(150);
		scaleAnimations[1].setDuration(150);
		scaleAnimations[2].setDuration(150);
		scaleAnimations[3].setDuration(150);
		return scaleAnimations;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Builder builder = new Builder(this);
			builder.setMessage("确定退出吗？");
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							System.exit(0);
						}
					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {

						}
					});
			builder.show();
		}

		return true;
	}
}
