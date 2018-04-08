package org.app.music;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.TreeMap;

import org.music.tools.LRCbean;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * ���Ž��棬�Ҿ���֡���ֲ���ġ������Ҿ�����Ȼ�������ܲ��ţ����ǻ��Ǿ��ò��á���߶�һЩ��Դ�Լ�״̬�Ĳ���������OnStart�½��С���ʾ�������֣�����loadclip
 * ��ʵ���readlrc��·�������ֻ�SD����ͬ����ͬ.�����ԣ�������ʾ���йصĶ������ǵ���OnStart����
 */
public class LocalPlayMusicActivity extends Activity {
	private int[] _ids;// ��ʱ����id
	private String[] _artists;// ������
	private String[] _titles;// ����
	private TextView musicnames;// ������
	private TextView artisting;// ������
	private ImageButton play_btn;// ���Ű�ť
	private ImageButton last_btn;// ��һ��
	private ImageButton next_btn;// ��һ��
	private TextView playtimes = null;// �Ѳ���ʱ��
	private TextView durationTime = null;// ����ʱ��
	private SeekBar seekbar;// ������
	private int position;// ����һ��λ�ã����ڶ�λ�û�����б�����
	private int currentPosition;// ��ǰ����λ��
	private int duration;// ��ʱ��
	private TextView lrc;// ���
	private ImageView album;// ר��
	private static final String MUSIC_CURRENT = "com.music.currentTime";// Action��״̬
	private static final String MUSIC_DURATION = "com.music.duration";
	private static final String MUSIC_NEXT = "com.music.next";
	private static final String MUSIC_UPDATE = "com.music.update";
	private static final int PLAY = 1;// ���岥��״̬
	private static final int PAUSE = 2;// ��ͣ״̬
	private static final int STOP = 3;// ֹͣ
	private static final int PROGRESS_CHANGE = 4;// �������ı�
	private static final int STATE_PLAY = 1;// ����״̬��Ϊ1
	private static final int STATE_PAUSE = 2;// ����״̬��Ϊ2
	private int flag;// ���
	private Cursor cursor;// �α�
	private TreeMap<Integer, LRCbean> lrc_map = new TreeMap<Integer, LRCbean>();// Treemap����
	private AudioManager am;
	private int maxVolume;// �������
	private int currentVolume;// ��ǰ����

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playmusic);
		Intent intent = getIntent();// ��ȡ�����б�����ݡ�
		Bundle bundle = intent.getExtras();// ���б�����ȡ����
		_ids = bundle.getIntArray("_ids");// bundleȡ��int���͵�_ids
		position = bundle.getInt("position");// �����������һ��Ҫ��ȷ������ʲô���ͣ����շ�����Ҫʲô����
		_titles = bundle.getStringArray("_titles");
		_artists = bundle.getStringArray("_artists");
		musicnames = (TextView) findViewById(R.id.musicname);// ��������ID�������Ϥ�ģ��ʲ�����
		artisting = (TextView) findViewById(R.id.artists);
		play_btn = (ImageButton) findViewById(R.id.playBtn);
		last_btn = (ImageButton) findViewById(R.id.lastBtn);
		next_btn = (ImageButton) findViewById(R.id.nextBtn);
		playtimes = (TextView) findViewById(R.id.playtime);
		durationTime = (TextView) findViewById(R.id.duration);
		seekbar = (SeekBar) findViewById(R.id.seekbar);
		lrc = (TextView) findViewById(R.id.lrc);
		album = (ImageView) findViewById(R.id.albums);
		am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
		/**
		 * ����д������ť������������ID֮��Ӽ������ڼ���д����
		 */
		ShowPlayBtn();
		ShowLastBtn();
		ShowNextBtn();
		ShowSeekBar();
	}

	private void ShowPlayBtn() {
		play_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (flag) {
				case STATE_PLAY:
					pause();
					break;

				case STATE_PAUSE:
					play();
					break;
				}

			}
		});

	}

	private void ShowSeekBar() {
		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				play();

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				pause();

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (fromUser) {
					seekbar_change(progress);
				}

			}
		});

	}

	private void ShowNextBtn() {
		next_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				nextOne();

			}
		});

	}

	private void ShowLastBtn() {
		last_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				lastOne();
			}
		});

	}

	/**
	 * ��������
	 */
	private void play() {
		flag = PLAY;
		play_btn.setImageResource(R.drawable.pause_button_default);
		Intent intent = new Intent();
		intent.setAction("com.app.media.MUSIC_SERVICE");
		intent.putExtra("op", PLAY);
		startService(intent);

	}

	/**
	 * ��ͣ��������
	 */
	private void pause() {
		flag = PAUSE;
		play_btn.setImageResource(R.drawable.play_button_default);
		Intent intent = new Intent();
		intent.setAction("com.app.media.MUSIC_SERVICE");
		intent.putExtra("op", PAUSE);
		startService(intent);

	}

	/**
	 * �������ı�
	 * 
	 * @param progress
	 */
	private void seekbar_change(int progress) {
		Intent intent = new Intent();
		intent.setAction("com.app.media.MUSIC_SERVICE");
		intent.putExtra("op", PROGRESS_CHANGE);
		intent.putExtra("progress", progress);
		startService(intent);

	}

	/**
	 * ��һ��
	 */
	private void nextOne() {
		if (position == _ids.length - 1) {
			position = 0;
		} else if (position < _ids.length - 1) {
			position++;
		}
		stop();
		setup();
		play();

	}

	/**
	 * ��һ��
	 */
	private void lastOne() {
		if (position == 0) {
			position = _ids.length - 1;
		} else if (position > 0) {
			position--;
		}
		stop();
		setup();
		play();
	}

	/**
	 * ֹͣ��������
	 */
	private void stop() {
		unregisterReceiver(musicreciver);
		Intent intent = new Intent();
		intent.setAction("com.app.media.MUSIC_SERVICE");
		intent.putExtra("op", STOP);
		startService(intent);

	}

	@Override
	protected void onStart() {
		super.onStart();
		setup();
		play();
	}

	/**
	 * �����治�ɼ�ʱ�򣬷�ע��Ĺ㲥
	 */
	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(musicreciver);
	}

	/**
	 * ���·��ؼ��¼�
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(LocalPlayMusicActivity.this,
					LocalTabMusicListActivity.class);
			startActivity(intent);

		}
		return true;
	}

	/**
	 * ��ʼ��
	 */
	private void setup() {
		loadclip();
		init();
		ReadSDLrc();

	}

	/**
	 * ����ʷ���
	 */
	private void ReadSDLrc() {
		cursor = getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Audio.Media.TITLE,
						MediaStore.Audio.Media.DURATION,
						MediaStore.Audio.Media.ARTIST,
						MediaStore.Audio.Media.ALBUM,
						MediaStore.Audio.Media.DISPLAY_NAME,
						MediaStore.Audio.Media.ALBUM_ID }, "_id=?",// �������ڵĸ�ʾ���ҪString����ĵ�4����������ʾ�ļ�����
				new String[] { _ids[position] + "" }, null);
		cursor.moveToFirst();// ���α�������һλ
		Bitmap bm = getArtwork(this, _ids[position], cursor.getInt(5), true);
		album.setImageBitmap(bm);
		String name = cursor.getString(4);// �α궨λ��DISPLAY_NAME
		read("/sdcard/music/" + name.substring(0, name.indexOf(".")) + ".lrc");// sd�����������ֽ�ȡ�ַ��ܲ��ҵ�����λ��,�ⲽ��Ҫ��û��дһֱ��ʾ����ļ��޷���ʾ,˳��˵����ͬ�ֻ��ͺ�SD���в�ͬ��·����
		System.out.println(cursor.getString(4));// �ڵ���ʱ���Ȱ���������д�����ڿ���̨��ӡ������ʾ���������֣���ô�ɴ��жϸ���û����.ֻ��û�л�ȡλ��

	}

	/**
	 * ��ʼ������
	 */
	private void init() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(MUSIC_CURRENT);
		filter.addAction(MUSIC_DURATION);
		filter.addAction(MUSIC_NEXT);
		filter.addAction(MUSIC_UPDATE);
		registerReceiver(musicreciver, filter);

	}

	/**
	 * ��ȡ���������ֵ��ַ���
	 */
	private void loadclip() {
		seekbar.setProgress(0);
		int pos = _ids[position];
		musicnames.setText(_titles[position]);
		artisting.setText(_artists[position]);
		Intent intent = new Intent();
		intent.putExtra("_id", pos);
		intent.putExtra("_titles", _titles);
		intent.putExtra("position", position);
		intent.setAction("com.app.media.MUSIC_SERVICE");// �������action���ͷ���
		startService(intent);

	}

	/**
	 * �ں�̨MusicService��ʹ��handler��Ϣ���ƣ���ͣ����ǰ̨���͹㲥���㲥����������ǵ�ǰmp���ŵ�ʱ��㣬
	 * ǰ̨���յ��㲥���ò���ʱ��������½�����,����������������һЩ��˵��Ȼ������ʵ�֡����ǻ��Ǿ��ÿ������̲߳���
	 */
	private BroadcastReceiver musicreciver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(MUSIC_CURRENT)) {
				currentPosition = intent.getExtras().getInt("currentTime");
				playtimes.setText(toTime(currentPosition));
				seekbar.setProgress(currentPosition);
				Iterator<Integer> iterator = lrc_map.keySet().iterator();
				while (iterator.hasNext()) {
					Object o = iterator.next();
					LRCbean val = lrc_map.get(o);
					if (val != null) {
						if (currentPosition > val.getBeginTime()
								&& currentPosition < val.getBeginTime()
										+ val.getLineTime()) {
							lrc.setText(val.getLrcBody());
							break;
						}
					}
				}
			} else if (action.equals(MUSIC_DURATION)) {
				duration = intent.getExtras().getInt("duration");// ��ȡ��ʱ��
				seekbar.setMax(duration);// �������������ֵ������ʱ�䣩
				durationTime.setText(toTime(duration));// ��ʱ������ת���ĺ���
			} else if (action.equals(MUSIC_NEXT)) {
				System.out.println("���ּ���������һ��");
				nextOne();
			} else if (action.equals(MUSIC_UPDATE)) {
				position = intent.getExtras().getInt("position");
				setup();
			}
		}
	};

	/**
	 * ����ʱ��ת��
	 */
	public String toTime(int time) {

		time /= 1000;
		int minute = time / 60;
		int second = time % 60;
		minute %= 60;
		return String.format("%02d:%02d", minute, second);
	}

	/**
	 * ��ȡ��ʵķ���������IO����һ��һ�е���ʾ
	 */
	private void read(String path) {
		lrc_map.clear();
		TreeMap<Integer, LRCbean> lrc_read = new TreeMap<Integer, LRCbean>();
		String data = "";
		BufferedReader br = null;
		File file = new File(path);
		System.out.println(path);
		if (!file.exists()) {
			lrc.setText("��ʲ�����");
			return;
		}
		FileInputStream stream = null;
		try {
			stream = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			while ((data = br.readLine()) != null) {
				if (data.length() > 6) {
					if (data.charAt(3) == ':' && data.charAt(6) == '.') {// �Ӹ�����Ŀ�ʼ
						data = data.replace("[", "");
						data = data.replace("]", "@");
						data = data.replace(".", ":");
						String lrc[] = data.split("@");
						String lrcContent = null;
						if (lrc.length == 2) {
							lrcContent = lrc[lrc.length - 1];// ���
						} else {
							lrcContent = "";
						}

						for (int i = 0; i < lrc.length - 1; i++) {
							String lrcTime[] = lrc[0].split(":");

							int m = Integer.parseInt(lrcTime[0]);// ��
							int s = Integer.parseInt(lrcTime[1]);// ��
							int ms = Integer.parseInt(lrcTime[2]);// ����

							int begintime = (m * 60 + s) * 1000 + ms;// ת���ɺ���
							LRCbean lrcbean = new LRCbean();
							lrcbean.setBeginTime(begintime);// ���ø�ʿ�ʼʱ��
							lrcbean.setLrcBody(lrcContent);// ���ø�ʵ�����
							lrc_read.put(begintime, lrcbean);

						}

					}
				}
			}
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// ����ÿ������Ҫ��ʱ��
		lrc_map.clear();
		data = "";
		Iterator<Integer> iterator = lrc_read.keySet().iterator();
		LRCbean oldval = null;
		int i = 0;

		while (iterator.hasNext()) {
			Object ob = iterator.next();
			LRCbean val = lrc_read.get(ob);
			if (oldval == null) {
				oldval = val;
			} else {
				LRCbean item1 = new LRCbean();
				item1 = oldval;
				item1.setLineTime(val.getBeginTime() - oldval.getBeginTime());
				lrc_map.put(new Integer(i), item1);
				i++;
				oldval = val;
			}
		}
	}

	/**
	 * �ص�������С����
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int action = event.getAction();
		int keyCode = event.getKeyCode();
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_UP:
			if (action == KeyEvent.ACTION_UP) {
				if (currentVolume < maxVolume) {
					currentVolume = currentVolume + 1;
					am.setStreamVolume(AudioManager.STREAM_MUSIC,
							currentVolume, 0);
				} else {
					am.setStreamVolume(AudioManager.STREAM_MUSIC,
							currentVolume, 0);
				}
			}
			return false;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			if (action == KeyEvent.ACTION_UP) {
				if (currentVolume > 0) {
					currentVolume = currentVolume - 1;
					am.setStreamVolume(AudioManager.STREAM_MUSIC,
							currentVolume, 0);
				} else {
					am.setStreamVolume(AudioManager.STREAM_MUSIC,
							currentVolume, 0);
				}
			}
			return false;
		default:
			return super.dispatchKeyEvent(event);
		}
	}
/**
 * �����Ǹ����ŵ�ʱ����ʾר��ͼƬ�����б�ͬ,����ʱͼƬҪ������cam�Ǹ�����д���ʵ�ͼƬ��
 * @param context
 * @param song_id
 * @param album_id
 * @param allowdefault
 * @return
 */
	public static Bitmap getArtwork(Context context, long song_id,
			long album_id, boolean allowdefault) {
		if (album_id < 0) {

			if (song_id >= 0) {
				Bitmap bm = getArtworkFromFile(context, song_id, -1);
				if (bm != null) {
					return bm;
				}
			}
			if (allowdefault) {
				return getDefaultArtwork(context);
			}
			return null;
		}
		ContentResolver res = context.getContentResolver();
		Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);
		if (uri != null) {
			InputStream in = null;
			try {
				in = res.openInputStream(uri);
				BitmapFactory.Options options = new BitmapFactory.Options();
				// ��ָ��ԭʼ��С
				options.inSampleSize = 1;
				// ֻ���д�С�ж�
				options.inJustDecodeBounds = true;
				// ���ô˷����õ�options�õ�ͼƬ�Ĵ�С
				BitmapFactory.decodeStream(in, null, options);
				// ���ǵ�Ŀ��������N pixel�Ļ�������ʾ��
				// ������Ҫ����computeSampleSize�õ�ͼƬ���ŵı���
				options.inSampleSize = computeSampleSize(options, 100);
				// ���ǵõ������ŵı��������ڿ�ʼ��ʽ����BitMap����
				options.inJustDecodeBounds = false;
				options.inDither = false;
				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				in = res.openInputStream(uri);
				return BitmapFactory.decodeStream(in, null, options);
			} catch (FileNotFoundException ex) {
				// The album art thumbnail does not actually exist. Maybe the
				// user deleted it, or
				// maybe it never existed to begin with.
				Bitmap bm = getArtworkFromFile(context, song_id, album_id);
				if (bm != null) {
					if (bm.getConfig() == null) {
						bm = bm.copy(Bitmap.Config.RGB_565, false);
						if (bm == null && allowdefault) {
							return getDefaultArtwork(context);
						}
					}
				} else if (allowdefault) {
					bm = getDefaultArtwork(context);
				}
				return bm;
			} finally {
				try {
					if (in != null) {
						in.close();
					}
				} catch (IOException ex) {
				}
			}
		}

		return null;
	}

	private static Bitmap getArtworkFromFile(Context context, long songid,
			long albumid) {
		Bitmap bm = null;
		if (albumid < 0 && songid < 0) {
			throw new IllegalArgumentException(
					"Must specify an album or a song id");
		}
		try {

			BitmapFactory.Options options = new BitmapFactory.Options();

			FileDescriptor fd = null;
			if (albumid < 0) {
				Uri uri = Uri.parse("content://media/external/audio/media/"
						+ songid + "/albumart");
				ParcelFileDescriptor pfd = context.getContentResolver()
						.openFileDescriptor(uri, "r");
				if (pfd != null) {
					fd = pfd.getFileDescriptor();
					// bm = BitmapFactory.decodeFileDescriptor(fd,null,options);
				}
			} else {
				Uri uri = ContentUris.withAppendedId(sArtworkUri, albumid);
				ParcelFileDescriptor pfd = context.getContentResolver()
						.openFileDescriptor(uri, "r");
				if (pfd != null) {
					fd = pfd.getFileDescriptor();
					// bm = BitmapFactory.decodeFileDescriptor(fd,null,options);
				}
			}
			options.inSampleSize = 1;
			// ֻ���д�С�ж�
			options.inJustDecodeBounds = true;
			// ���ô˷����õ�options�õ�ͼƬ�Ĵ�С
			BitmapFactory.decodeFileDescriptor(fd, null, options);
			// ���ǵ�Ŀ������800pixel�Ļ�������ʾ��
			// ������Ҫ����computeSampleSize�õ�ͼƬ���ŵı���
			options.inSampleSize = 200;
			// OK,���ǵõ������ŵı��������ڿ�ʼ��ʽ����BitMap����
			options.inJustDecodeBounds = false;
			options.inDither = false;
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;

			// ����options��������������Ҫ���ڴ�
			bm = BitmapFactory.decodeFileDescriptor(fd, null, options);
		} catch (FileNotFoundException ex) {

		}

		return bm;
	}

	// ����������ͼƬ�Ĵ�С�����жϣ����õ����ʵ����ű���������2��1/2,3��1/3
	static int computeSampleSize(BitmapFactory.Options options, int target) {
		int w = options.outWidth;
		int h = options.outHeight;
		int candidateW = w / target;
		int candidateH = h / target;
		int candidate = Math.max(candidateW, candidateH);
		if (candidate == 0)
			return 1;
		if (candidate > 1) {
			if ((w > target) && (w / candidate) < target)
				candidate -= 1;
		}
		if (candidate > 1) {
			if ((h > target) && (h / candidate) < target)
				candidate -= 1;
		}
		return candidate;
	}

	private static Bitmap getDefaultArtwork(Context context) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
		return BitmapFactory.decodeStream(context.getResources()
				.openRawResource(R.drawable.default_album), null, opts);
	}

	private static final Uri sArtworkUri = Uri
			.parse("content://media/external/audio/albumart");
	// private static final BitmapFactory.Options sBitmapOptions = new
	// BitmapFactory.Options();
	// private static Bitmap mCachedBit = null;
}
