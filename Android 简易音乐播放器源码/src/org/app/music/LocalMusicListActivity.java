package org.app.music;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.music.tools.IphoneDialogBuilder;
import org.music.tools.LocalMusicListAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.text.method.DigitsKeyListener;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 音乐列表，先采用每次进入扫描一遍SD卡。用强大的MediaStore读出媒体信息一一显示在列表，见showMp3List方法
 * 菜单采用UC类似的菜单，还增加了倒计时。
 */
public class LocalMusicListActivity extends Activity {
	private ListView listview;// 列表
	private int[] _ids;// 临时音乐id
	private String[] _artists;// 艺术家
	private String[] _titles;// 标题
	private String[] _path;// 路径
	private final int create_list = 0;// 类似UC的标识
	private final int scan = 1;
	private final int settings = 2;
	private final int sleep = 3;
	private final int exit = 4;
	private static final int Ringtone = 0;
	private static final int Alarm = 1;
	private static final int Notification = 2;
	private int num;
	LocalMusicListAdapter adapter;// 适配器
	// 模仿菜单图片数组
	int[] images = { R.drawable.menu_create_list, R.drawable.menu_scan,
			R.drawable.menu_settings, R.drawable.local_singer,
			R.drawable.menu_quit };
	// 模仿菜单文字数组
	String[] texts = { "新建列表", "扫描SD卡", "系统设置", "休眠", "退出" };// 菜单的文字
	GridView menuGrid;// 用GridView
	PopupWindow pop;// 弹出窗口
	AlertDialog menuDialog;// 对话框
	View menuView;// 视图
	TextView clocks;// 倒计时
	int c;
	int index;
	Timer times;
	// 上下文菜单项
	private static final int PLAY_ITEM = Menu.FIRST;
	private static final int DELETE_ITEM = Menu.FIRST + 1;
	private static final int RING = Menu.FIRST + 2;
	private static final int MUSIC_INFO = Menu.FIRST + 3;
	/**
	 * 查询媒体信息数组,用MediaStore读出依次为标题，总时间,艺术家，ID，显示名字，数据，专辑ID。
	 */
	String[] media_info = new String[] { MediaStore.Audio.Media.TITLE,
			MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ARTIST,
			MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME,
			MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.localmusic);
		listview = (ListView) findViewById(R.id.music_list);
		clocks = (TextView) findViewById(R.id.timer_clock);
		showMp3List();
		showMenu();
		listview.setOnItemClickListener(new MusicListOnClickListener());// 列表的选项的监听器
		listview.setOnCreateContextMenuListener(new MusicContextMenuListner());
	}

	/**
	 * 显示MP3列表的方法
	 */
	private void showMp3List() {
		// 通过游标的getContentResolver查询媒体信息
		Cursor cursor = getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, media_info, null,
				null, null);
		// 这里媒体信息用游标，不过要判断一下，否则即便在SD卡下没有音乐也会报异常。
		if (null != cursor && cursor.getCount() > 0) {
			cursor.moveToFirst();// 先游标移到第一列
			_ids = new int[cursor.getCount()];// 实例化临时变量
			_titles = new String[cursor.getCount()];
			_artists = new String[cursor.getCount()];
			_path = new String[cursor.getCount()];
			// 用循环把所有媒体信息便利出来
			for (int i = 0; i < cursor.getCount(); i++) {
				_ids[i] = cursor.getInt(3);// 获得类型不要搞错了
				_titles[i] = cursor.getString(0);
				_artists[i] = cursor.getString(2);
				_path[i] = cursor.getString(5).substring(4);
				cursor.moveToNext();// 将游标移至下一行。
				System.out.println(_ids[i]);
				System.out.println(_titles[i]);
				System.out.println(_artists[i]);
				System.out.println(_path[i]);

			}
			listview.setAdapter(new LocalMusicListAdapter(this, cursor));// 用自定义的适配器装数据
		}
	}

	/**
	 * 显示UC类似窗口
	 */
	private void showMenu() {
		menuView = View.inflate(this, R.layout.popupwindow, null);// 装载布局
		menuDialog = new AlertDialog.Builder(this).create();// 用对话框形式显示
		menuDialog.setView(menuView);// 设置视图
		menuGrid = (GridView) menuView.findViewById(R.id.gridview);
		menuGrid.setAdapter(getMenuAdapter(texts, images));
		menuGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				// 判断菜单点击位置
				switch (position) {
				case create_list:

					break;

				case scan:
					break;

				case settings:
					Intent intent = new Intent(LocalMusicListActivity.this,
							LocalSettingsActivity.class);
					startActivity(intent);
					finish();
					break;
				case sleep:
					sleeps();// 休眠方法
					break;
				case exit:
					finish();
					break;

				}

			}
		});

	}

	// 外部添加监听器
	public class MusicListOnClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long id) {
			playMusic(position);

		}

	}

	/**
	 * 长按菜单
	 */
	public class MusicContextMenuListner implements OnCreateContextMenuListener {

		@Override
		public void onCreateContextMenu(ContextMenu menu, View view,
				ContextMenuInfo info) {
			menu.setHeaderTitle("操作");
			menu.add(0, PLAY_ITEM, 0, "播放");
			menu.add(1, DELETE_ITEM, 1, "删除");
			menu.add(2, RING, 2, "设置铃声");
			menu.add(3, MUSIC_INFO, 3, "查看该歌曲信息");
			final AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) info;
			num = menuInfo.position;

		}

	}

	/**
	 * 菜单长按事件
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case PLAY_ITEM:
			playMusic(num);// 根据按的数字播放歌曲
			break;
		case DELETE_ITEM: // 删除一首歌曲
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("真的要删除这首歌曲吗")
					.setPositiveButton("是",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									deleteMusic(num); // 从列表中删除音乐
									deleteMusicFile(num); // 从SD卡中删除音乐
									showMp3List(); // 从新获得列表中药显示的数据
									adapter.notifyDataSetChanged(); // 更新列表UI
								}
							}).setNegativeButton("否", null).show();
		case RING:
			SetRing();
			break;
		case MUSIC_INFO:
			ReadMusicInfo();
			break;
		}
		return true;
	}

	private void SetRing() {
		String[] soundSet = new String[] { "设为来电铃声", "设为通知铃声", "设为全部铃声" };
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(_titles[num]);
		builder.setSingleChoiceItems(soundSet, -1,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						index = which;
					}
				});
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				if (index == 0)// 设为来电铃声
				{
					phoneSound();
					Toast.makeText(LocalMusicListActivity.this, "设为来电铃声成功!",
							Toast.LENGTH_SHORT).show();

				}
				if (index == 1)// 设为通知铃声
				{
					notificationSound();
					Toast.makeText(LocalMusicListActivity.this, "设为通知铃声成功!",
							Toast.LENGTH_SHORT).show();
				}
				if (index == 2)// 设为全部铃声
				{
					allSound();
					Toast.makeText(LocalMusicListActivity.this, "设为全部铃声成功!",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

			}
		});
		builder.show();

	}

	// 从列表中删除选中的音乐
	private void deleteMusic(int position) {
		this.getContentResolver().delete(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				MediaStore.Audio.Media._ID + "=" + _ids[position], null);
	}

	// 从sdcard中删除选中的音乐
	private void deleteMusicFile(int position) {
		File file = new File(_path[position]);
		System.out.println(_path[position]);
		file.delete();
	}

	/**
	 * 播放音乐
	 */
	private void playMusic(int position) {
		Intent intent = new Intent(LocalMusicListActivity.this,
				LocalPlayMusicActivity.class);
		intent.putExtra("_ids", _ids);
		intent.putExtra("_titles", _titles);
		intent.putExtra("_artists", _artists);
		intent.putExtra("position", position);
		startActivity(intent);
		finish();

	}

	/**
	 * 添加一个menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("menu");
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * 拦截菜单.但是真机测试Logcat会报异常，但是真机没有出现强制关闭就算了吧。
	 */
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		// 如果对话是空的，那么就创建一个，否则直接显示
		if (menuDialog == null) {
			menuDialog = new AlertDialog.Builder(this).setView(menuView).show();
		} else {
			menuDialog.show();
		}
		return false;// 要返回false，true系统调用
	}

	/**
	 * 按下返回键发生的事件
	 * 
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			IphoneDialogBuilder ib = new IphoneDialogBuilder(this);// 自定义苹果风格
			ib.setTitle("询问");
			ib.setMessage("确实要退出吗?");
			ib.setPositiveButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();

				}
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			}).show();
		}
		return false;
	}

	/**
	 * 用HashMap装载数据,构造菜单
	 */
	private SimpleAdapter getMenuAdapter(String[] menuNameArray,
			int[] imageResourceArray) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < texts.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", images[i]);
			map.put("itemText", texts[i]);
			data.add(map);
		}
		SimpleAdapter simperAdapter = new SimpleAdapter(this, data,
				R.layout.popupwindow_item, new String[] { "itemImage",
						"itemText" }, new int[] { R.id.item_image,
						R.id.item_text });
		return simperAdapter;
	}

	/**
	 * 休眠方法
	 */
	private void sleeps() {
		String[] sleep = { "按时间休眠" };
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("请选择休眠模式");
		builder.setSingleChoiceItems(sleep, -1,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						index = which;

					}
				});
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (index == 0) {
					TimeSleep();
				}

			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		}).show();

	}

	/**
	 * 按时间休眠,输入时间以对话框的形式出现。
	 */
	private void TimeSleep() {
		final EditText editText = new EditText(this);// 编辑框定死了
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("请输入休眠等待时间(分钟)");
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setView(editText);
		editText.setText("15");
		editText.setHint("请输入两位数以内的值");
		editText.setKeyListener(new DigitsKeyListener(false, true));
		editText.setGravity(Gravity.CENTER_HORIZONTAL);
		editText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		editText.setTextColor(Color.RED);
		editText.setSelection(editText.length());
		editText.selectAll();
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 这边意思是如果编辑框里字符小于2或者没有输入会报输入非法。
				if (editText.length() <= 2 && editText.length() != 0) {
					if (".".equals(editText.getText().toString())) {
						Toast.makeText(LocalMusicListActivity.this,
								"输入内容是非法字符", Toast.LENGTH_SHORT).show();
					} else {
						// 否则按用户输入的时间继续执行
						final String time = editText.getText().toString();
						long money = Integer.parseInt(time);
						long cx = money * 60000;
						times = new Timer(cx, 1000);
						times.start();
						Toast.makeText(
								LocalMusicListActivity.this,
								"休眠模式启动!\n" + String.valueOf(time)
										+ "\t分钟后关闭程序!", Toast.LENGTH_LONG)
								.show();
						clocks.setText(String.valueOf(time));
					}
				}

			}
		}).setNegativeButton("取消", null).show();
	}

	/**
	 * 写一个倒计时，继承CountDownTimer
	 */
	private class Timer extends CountDownTimer {

		public Timer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);

		}

		@Override
		public void onFinish() {
			if (c == 0) {
				finish();
				onDestroy();
				// 完全退出
				System.exit(0);
				android.os.Process.killProcess(android.os.Process.myPid());
			} else {
				finish();
				onDestroy();
				android.os.Process.killProcess(android.os.Process.myPid());

			}

		}

		@Override
		public void onTick(long millisUntilFinished) {
			clocks.setText("" + millisUntilFinished / 1000 / 60 + ":"
					+ millisUntilFinished / 1000 % 60);
			// 假如这个数大于9 说明就是2位数了
			// 可以直接输入。假如小于等于9 那就是1位数。所以前面加一个0
			String abc = (millisUntilFinished / 1000 / 60) > 9 ? (millisUntilFinished / 1000 / 60)
					+ ""
					: "0" + (millisUntilFinished / 1000 / 60);
			String b = (millisUntilFinished / 1000 % 60) > 9 ? (millisUntilFinished / 1000 % 60)
					+ ""
					: "0" + (millisUntilFinished / 1000 % 60);
			clocks.setText(abc + ":" + b);
		}

	}

	// 设置来电铃声
	public void phoneSound() {
		Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
				RingtoneManager.TYPE_RINGTONE);
		startActivityForResult(intent, Ringtone);

	}

	// 设置通知铃声
	public void notificationSound() {

		Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
				RingtoneManager.TYPE_NOTIFICATION);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "设置通知铃声成功");
		startActivityForResult(intent, Notification);
	}

	// 设置全部铃声
	public void allSound() {

		Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
				RingtoneManager.TYPE_NOTIFICATION);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
				RingtoneManager.TYPE_RINGTONE);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "设置全部铃声成功");
		startActivityForResult(intent, Alarm);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		} else {
			Uri uri = data
					.getParcelableExtra(android.media.RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
			if (uri != null) {
				switch (resultCode) {
				case Ringtone:
					android.media.RingtoneManager.setActualDefaultRingtoneUri(
							this, android.media.RingtoneManager.TYPE_RINGTONE,
							uri);

					break;
				case Alarm:
					android.media.RingtoneManager
							.setActualDefaultRingtoneUri(this,
									android.media.RingtoneManager.TYPE_ALARM,
									uri);
					Toast.makeText(this, "设置铃声成功！", Toast.LENGTH_SHORT).show();
					break;
				case Notification:
					android.media.RingtoneManager.setActualDefaultRingtoneUri(
							this,
							android.media.RingtoneManager.TYPE_NOTIFICATION,
							uri);

				default:
					break;
				}
			}
		}
	}

	// 查看音乐信息
	private void ReadMusicInfo() {
		String[] musicinfo = { "歌名:" + _titles[num], "歌手:" + _artists[num],
				"路径:" + _path[num] };
		AlertDialog.Builder builder = new AlertDialog.Builder(
				LocalMusicListActivity.this);
		builder.setTitle(_titles[num]).setItems(musicinfo,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		builder.setPositiveButton("编辑", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface arg0, int arg1) {

			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub

			}
		});
		builder.create().show();
	}
}
