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
 * �����б��Ȳ���ÿ�ν���ɨ��һ��SD������ǿ���MediaStore����ý����Ϣһһ��ʾ���б���showMp3List����
 * �˵�����UC���ƵĲ˵����������˵���ʱ��
 */
public class LocalMusicListActivity extends Activity {
	private ListView listview;// �б�
	private int[] _ids;// ��ʱ����id
	private String[] _artists;// ������
	private String[] _titles;// ����
	private String[] _path;// ·��
	private final int create_list = 0;// ����UC�ı�ʶ
	private final int scan = 1;
	private final int settings = 2;
	private final int sleep = 3;
	private final int exit = 4;
	private static final int Ringtone = 0;
	private static final int Alarm = 1;
	private static final int Notification = 2;
	private int num;
	LocalMusicListAdapter adapter;// ������
	// ģ�²˵�ͼƬ����
	int[] images = { R.drawable.menu_create_list, R.drawable.menu_scan,
			R.drawable.menu_settings, R.drawable.local_singer,
			R.drawable.menu_quit };
	// ģ�²˵���������
	String[] texts = { "�½��б�", "ɨ��SD��", "ϵͳ����", "����", "�˳�" };// �˵�������
	GridView menuGrid;// ��GridView
	PopupWindow pop;// ��������
	AlertDialog menuDialog;// �Ի���
	View menuView;// ��ͼ
	TextView clocks;// ����ʱ
	int c;
	int index;
	Timer times;
	// �����Ĳ˵���
	private static final int PLAY_ITEM = Menu.FIRST;
	private static final int DELETE_ITEM = Menu.FIRST + 1;
	private static final int RING = Menu.FIRST + 2;
	private static final int MUSIC_INFO = Menu.FIRST + 3;
	/**
	 * ��ѯý����Ϣ����,��MediaStore��������Ϊ���⣬��ʱ��,�����ң�ID����ʾ���֣����ݣ�ר��ID��
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
		listview.setOnItemClickListener(new MusicListOnClickListener());// �б��ѡ��ļ�����
		listview.setOnCreateContextMenuListener(new MusicContextMenuListner());
	}

	/**
	 * ��ʾMP3�б�ķ���
	 */
	private void showMp3List() {
		// ͨ���α��getContentResolver��ѯý����Ϣ
		Cursor cursor = getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, media_info, null,
				null, null);
		// ����ý����Ϣ���α꣬����Ҫ�ж�һ�£����򼴱���SD����û������Ҳ�ᱨ�쳣��
		if (null != cursor && cursor.getCount() > 0) {
			cursor.moveToFirst();// ���α��Ƶ���һ��
			_ids = new int[cursor.getCount()];// ʵ������ʱ����
			_titles = new String[cursor.getCount()];
			_artists = new String[cursor.getCount()];
			_path = new String[cursor.getCount()];
			// ��ѭ��������ý����Ϣ��������
			for (int i = 0; i < cursor.getCount(); i++) {
				_ids[i] = cursor.getInt(3);// ������Ͳ�Ҫ�����
				_titles[i] = cursor.getString(0);
				_artists[i] = cursor.getString(2);
				_path[i] = cursor.getString(5).substring(4);
				cursor.moveToNext();// ���α�������һ�С�
				System.out.println(_ids[i]);
				System.out.println(_titles[i]);
				System.out.println(_artists[i]);
				System.out.println(_path[i]);

			}
			listview.setAdapter(new LocalMusicListAdapter(this, cursor));// ���Զ����������װ����
		}
	}

	/**
	 * ��ʾUC���ƴ���
	 */
	private void showMenu() {
		menuView = View.inflate(this, R.layout.popupwindow, null);// װ�ز���
		menuDialog = new AlertDialog.Builder(this).create();// �öԻ�����ʽ��ʾ
		menuDialog.setView(menuView);// ������ͼ
		menuGrid = (GridView) menuView.findViewById(R.id.gridview);
		menuGrid.setAdapter(getMenuAdapter(texts, images));
		menuGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				// �жϲ˵����λ��
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
					sleeps();// ���߷���
					break;
				case exit:
					finish();
					break;

				}

			}
		});

	}

	// �ⲿ��Ӽ�����
	public class MusicListOnClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long id) {
			playMusic(position);

		}

	}

	/**
	 * �����˵�
	 */
	public class MusicContextMenuListner implements OnCreateContextMenuListener {

		@Override
		public void onCreateContextMenu(ContextMenu menu, View view,
				ContextMenuInfo info) {
			menu.setHeaderTitle("����");
			menu.add(0, PLAY_ITEM, 0, "����");
			menu.add(1, DELETE_ITEM, 1, "ɾ��");
			menu.add(2, RING, 2, "��������");
			menu.add(3, MUSIC_INFO, 3, "�鿴�ø�����Ϣ");
			final AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) info;
			num = menuInfo.position;

		}

	}

	/**
	 * �˵������¼�
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case PLAY_ITEM:
			playMusic(num);// ���ݰ������ֲ��Ÿ���
			break;
		case DELETE_ITEM: // ɾ��һ�׸���
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("���Ҫɾ�����׸�����")
					.setPositiveButton("��",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									deleteMusic(num); // ���б���ɾ������
									deleteMusicFile(num); // ��SD����ɾ������
									showMp3List(); // ���»���б���ҩ��ʾ������
									adapter.notifyDataSetChanged(); // �����б�UI
								}
							}).setNegativeButton("��", null).show();
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
		String[] soundSet = new String[] { "��Ϊ��������", "��Ϊ֪ͨ����", "��Ϊȫ������" };
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(_titles[num]);
		builder.setSingleChoiceItems(soundSet, -1,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						index = which;
					}
				});
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				if (index == 0)// ��Ϊ��������
				{
					phoneSound();
					Toast.makeText(LocalMusicListActivity.this, "��Ϊ���������ɹ�!",
							Toast.LENGTH_SHORT).show();

				}
				if (index == 1)// ��Ϊ֪ͨ����
				{
					notificationSound();
					Toast.makeText(LocalMusicListActivity.this, "��Ϊ֪ͨ�����ɹ�!",
							Toast.LENGTH_SHORT).show();
				}
				if (index == 2)// ��Ϊȫ������
				{
					allSound();
					Toast.makeText(LocalMusicListActivity.this, "��Ϊȫ�������ɹ�!",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

			}
		});
		builder.show();

	}

	// ���б���ɾ��ѡ�е�����
	private void deleteMusic(int position) {
		this.getContentResolver().delete(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				MediaStore.Audio.Media._ID + "=" + _ids[position], null);
	}

	// ��sdcard��ɾ��ѡ�е�����
	private void deleteMusicFile(int position) {
		File file = new File(_path[position]);
		System.out.println(_path[position]);
		file.delete();
	}

	/**
	 * ��������
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
	 * ���һ��menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("menu");
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * ���ز˵�.�����������Logcat�ᱨ�쳣���������û�г���ǿ�ƹرվ����˰ɡ�
	 */
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		// ����Ի��ǿյģ���ô�ʹ���һ��������ֱ����ʾ
		if (menuDialog == null) {
			menuDialog = new AlertDialog.Builder(this).setView(menuView).show();
		} else {
			menuDialog.show();
		}
		return false;// Ҫ����false��trueϵͳ����
	}

	/**
	 * ���·��ؼ��������¼�
	 * 
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			IphoneDialogBuilder ib = new IphoneDialogBuilder(this);// �Զ���ƻ�����
			ib.setTitle("ѯ��");
			ib.setMessage("ȷʵҪ�˳���?");
			ib.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();

				}
			}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			}).show();
		}
		return false;
	}

	/**
	 * ��HashMapװ������,����˵�
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
	 * ���߷���
	 */
	private void sleeps() {
		String[] sleep = { "��ʱ������" };
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("��ѡ������ģʽ");
		builder.setSingleChoiceItems(sleep, -1,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						index = which;

					}
				});
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (index == 0) {
					TimeSleep();
				}

			}
		}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		}).show();

	}

	/**
	 * ��ʱ������,����ʱ���ԶԻ������ʽ���֡�
	 */
	private void TimeSleep() {
		final EditText editText = new EditText(this);// �༭������
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("���������ߵȴ�ʱ��(����)");
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setView(editText);
		editText.setText("15");
		editText.setHint("��������λ�����ڵ�ֵ");
		editText.setKeyListener(new DigitsKeyListener(false, true));
		editText.setGravity(Gravity.CENTER_HORIZONTAL);
		editText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		editText.setTextColor(Color.RED);
		editText.setSelection(editText.length());
		editText.selectAll();
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// �����˼������༭�����ַ�С��2����û������ᱨ����Ƿ���
				if (editText.length() <= 2 && editText.length() != 0) {
					if (".".equals(editText.getText().toString())) {
						Toast.makeText(LocalMusicListActivity.this,
								"���������ǷǷ��ַ�", Toast.LENGTH_SHORT).show();
					} else {
						// �����û������ʱ�����ִ��
						final String time = editText.getText().toString();
						long money = Integer.parseInt(time);
						long cx = money * 60000;
						times = new Timer(cx, 1000);
						times.start();
						Toast.makeText(
								LocalMusicListActivity.this,
								"����ģʽ����!\n" + String.valueOf(time)
										+ "\t���Ӻ�رճ���!", Toast.LENGTH_LONG)
								.show();
						clocks.setText(String.valueOf(time));
					}
				}

			}
		}).setNegativeButton("ȡ��", null).show();
	}

	/**
	 * дһ������ʱ���̳�CountDownTimer
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
				// ��ȫ�˳�
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
			// �������������9 ˵������2λ����
			// ����ֱ�����롣����С�ڵ���9 �Ǿ���1λ��������ǰ���һ��0
			String abc = (millisUntilFinished / 1000 / 60) > 9 ? (millisUntilFinished / 1000 / 60)
					+ ""
					: "0" + (millisUntilFinished / 1000 / 60);
			String b = (millisUntilFinished / 1000 % 60) > 9 ? (millisUntilFinished / 1000 % 60)
					+ ""
					: "0" + (millisUntilFinished / 1000 % 60);
			clocks.setText(abc + ":" + b);
		}

	}

	// ������������
	public void phoneSound() {
		Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
				RingtoneManager.TYPE_RINGTONE);
		startActivityForResult(intent, Ringtone);

	}

	// ����֪ͨ����
	public void notificationSound() {

		Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
				RingtoneManager.TYPE_NOTIFICATION);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "����֪ͨ�����ɹ�");
		startActivityForResult(intent, Notification);
	}

	// ����ȫ������
	public void allSound() {

		Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
				RingtoneManager.TYPE_NOTIFICATION);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
				RingtoneManager.TYPE_RINGTONE);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "����ȫ�������ɹ�");
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
					Toast.makeText(this, "���������ɹ���", Toast.LENGTH_SHORT).show();
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

	// �鿴������Ϣ
	private void ReadMusicInfo() {
		String[] musicinfo = { "����:" + _titles[num], "����:" + _artists[num],
				"·��:" + _path[num] };
		AlertDialog.Builder builder = new AlertDialog.Builder(
				LocalMusicListActivity.this);
		builder.setTitle(_titles[num]).setItems(musicinfo,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		builder.setPositiveButton("�༭", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface arg0, int arg1) {

			}
		});
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub

			}
		});
		builder.create().show();
	}
}
