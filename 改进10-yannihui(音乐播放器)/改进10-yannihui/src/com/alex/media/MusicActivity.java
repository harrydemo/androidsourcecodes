package com.alex.media;

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
import java.util.Random;
import java.util.TreeMap;

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
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MusicActivity extends Activity {

	private int[] _ids;
	private int position;
	private String _titles[] = null;
	private String _artists[] = null;
	private Uri uri;
	private ImageView AlbumPic;
	private ImageButton LoopBtn = null;//循环
	private ImageButton RandomBtm = null;//随机
	private ImageButton playBtn = null;//播放、暂停
	//private Button stopBtn = null;//停止
	private ImageButton latestBtn = null;//上一首
	private ImageButton nextBtn = null;//下一首
	private ImageButton forwardBtn = null;//快进
	private ImageButton rewindBtn = null;//快退
	private TextView lrcText = null;//歌词文本
	private TextView playtime = null;//已播放时间
	private TextView durationTime = null;//歌曲时间
	private SeekBar seekbar = null;//歌曲进度
	private SeekBar soundBar = null;//音量调节
	private Handler handler = null;//用于进度条
	private Handler fHandler = null;//用于快进
	private int currentPosition;//当前播放位置
	private int duration;
	private DBHelper dbHelper = null;
	private TextView name = null;
	private TextView artist = null;
	private GestureDetector gestureDetector;
	
	private TreeMap<Integer, LRCbean> lrc_map = new TreeMap<Integer, LRCbean>();
	private Cursor myCur;
	private static final String MUSIC_CURRENT = "com.alex.currentTime";
	private static final String MUSIC_DURATION = "com.alex.duration";
	private static final String MUSIC_NEXT = "com.alex.next";
	private static final String MUSIC_UPDATE = "com.alex.update";
	private static final int MUSIC_PLAY = 1;
	private static final int MUSIC_PAUSE = 2;
	private static final int MUSIC_STOP = 3;
	private static final int PROGRESS_CHANGE = 4;
	private static final int MUSIC_REWIND = 5;
	private static final int MUSIC_FORWARD = 6;
	
	private static final int STATE_PLAY = 1;
	private static final int STATE_PAUSE = 2;
	
	public static final int LOOP_NONE = 0;
	public static final int LOOP_ONE = 1;
	public static final int LOOP_ALL = 2;
	
	private int flag;
	public static int loop_flag = LOOP_NONE;
	public static boolean random_flag = false;
	public static int[] randomIDs = null;
	public static int randomNum = 0;
	//关于音量的变量
	private AudioManager mAudioManager = null;
	private int maxVolume;//最大音量
	private int currentVolume;//当前音量
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main1);
		AlbumPic = (ImageView)findViewById(R.id.albumPic);
		//AlbumPic.setImageResource(R.drawable.listbg1);
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		_ids = bundle.getIntArray("_ids");	
		randomIDs = new int[_ids.length];
		
		position = bundle.getInt("position");
		_titles = bundle.getStringArray("_titles");
		_artists = bundle.getStringArray("_artists");
		lrcText = (TextView)findViewById(R.id.lrc);
		name = (TextView)findViewById(R.id.musicname);
		artist = (TextView)findViewById(R.id.artist);
		playtime = (TextView)findViewById(R.id.playtime);//已经播放的时间
		durationTime = (TextView)findViewById(R.id.duration);
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE); 
		maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//获得最大音量  
		
		currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);//获得当前音量 
		
		gestureDetector=new GestureDetector(new ChangeGestureDetector(this));	//手势识别
		
		soundBar = (SeekBar)findViewById(R.id.volum);
		soundBar.setMax(maxVolume);
		soundBar.setProgress(currentVolume);
		soundBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, AudioManager.FLAG_ALLOW_RINGER_MODES);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		LoopBtn = (ImageButton)findViewById(R.id.loop);
		LoopBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (loop_flag) {
				case LOOP_NONE:
					loop_flag = LOOP_ONE;
					LoopBtn.setBackgroundResource(R.drawable.loop_one);
					break;

				case LOOP_ONE:
					loop_flag = LOOP_ALL;
					LoopBtn.setBackgroundResource(R.drawable.loop_all);
					break;
				case LOOP_ALL:
					loop_flag = LOOP_NONE;
					LoopBtn.setBackgroundResource(R.drawable.loop_none);
					break;
				}
			}
		});
		
		RandomBtm = (ImageButton) findViewById(R.id.random);
		RandomBtm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(random_flag == false)
				{
					for(int i = 0; i < _ids.length; i++)
					{
						randomIDs[i] = -1;
					}
					random_flag = true;
					RandomBtm.setBackgroundResource(R.drawable.random_select);
				}
				else
				{
					random_flag = false;
					RandomBtm.setBackgroundResource(R.drawable.random);
				}
					
			}
			
		});
		
		playBtn = (ImageButton)findViewById(R.id.playBtn);
		playBtn.setOnClickListener(new View.OnClickListener() {
			
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
		
		seekbar = (SeekBar)findViewById(R.id.seekbar);
		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				//play();
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				//pause();
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (fromUser){
					seekbar_change(progress);
				}
			}
		});
		
		rewindBtn = (ImageButton)findViewById(R.id.rewindBtn);
		rewindBtn.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					pause();
					rewind();
					break;
				case MotionEvent.ACTION_UP:
					play();
					break;
				}
				return true;
			}
		});
		
		forwardBtn = (ImageButton)findViewById(R.id.forwardBtn);
		forwardBtn.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()){
				case MotionEvent.ACTION_DOWN:
					pause();
					forward();
					break;
				case MotionEvent.ACTION_UP:
					play();
					break;
				}
				return true;
			}
		});
		
		latestBtn = (ImageButton)findViewById(R.id.lastOne);
		latestBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				latestOne();
			}
		});
		
		nextBtn = (ImageButton)findViewById(R.id.nextOne);
		nextBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				nextOne();
			}
		});
		
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		setup();
		play();
		
	}
	
	private void loadClip(){
		seekbar.setProgress(0);
		//int pos = _ids[position];
		//设置歌曲名
		if (_titles[position].length()>15)
			name.setText(_titles[position].substring(0,12)+"...");//设置歌曲名
		else
			name.setText(_titles[position]);
		
		//设置艺术家名
		if (_artists[position].equals("<unknown>"))
			artist.setText("未知艺术家");
		else	
			artist.setText(_artists[position]);
		Intent intent = new Intent();
		intent.putExtra("_ids", _ids);
		intent.putExtra("_titles", _titles);
		intent.putExtra("position", position);
		intent.setAction("com.alex.media.MUSIC_SERVICE");
		startService(intent);
	}
	
	private void init(){
		IntentFilter filter = new IntentFilter();
		filter.addAction(MUSIC_CURRENT);
		filter.addAction(MUSIC_DURATION);
		filter.addAction(MUSIC_NEXT);
		filter.addAction(MUSIC_UPDATE);
		registerReceiver(musicReceiver, filter);
	}
	
	private void setup(){
		refreshView();
		loadClip();
		init();
	}
	
	/**
	 * 音乐播放
	 */
	private void play(){
		flag = STATE_PLAY;
		playBtn.setBackgroundResource(R.drawable.pause_selecor);
		Intent intent = new Intent();
		intent.setAction("com.alex.media.MUSIC_SERVICE");
		intent.putExtra("op",MUSIC_PLAY);
		startService(intent);
	}
	
	/**
	 * 音乐暂停
	 */
	private void pause(){
		flag = STATE_PAUSE;
		playBtn.setBackgroundResource(R.drawable.play_selecor);
		Intent intent = new Intent();
		intent.setAction("com.alex.media.MUSIC_SERVICE");
		intent.putExtra("op",MUSIC_PAUSE);
		startService(intent);
	}
	
	/**
	 * 音乐停止
	 */
	private void stop(){
		unregisterReceiver(musicReceiver);
		Intent intent = new Intent();
		intent.setAction("com.alex.media.MUSIC_SERVICE");
		intent.putExtra("op", MUSIC_STOP);
		startService(intent);
	}
	
	/**
	 * 用户拖动进度条
	 */
	private void seekbar_change(int progress){
		Intent intent = new Intent();
		intent.setAction("com.alex.media.MUSIC_SERVICE");
		intent.putExtra("op", PROGRESS_CHANGE);
		intent.putExtra("progress", progress);
		startService(intent);
	}
	
	/**
	 * 快退
	 */
	private void rewind(){
		Intent intent = new Intent();
		intent.setAction("com.alex.media.MUSIC_SERVICE");
		intent.putExtra("op", MUSIC_REWIND);
		startService(intent);
	}
	
	/**
	 * 快进
	 */
	private void forward(){
		Intent intent = new Intent();
		intent.setAction("com.alex.media.MUSIC_SERVICE");
		intent.putExtra("op", MUSIC_FORWARD);
		startService(intent);
	}
	
	/**
	 * 上一首
	 */
	
	public void latestOne(){
		if (_ids.length==1 || loop_flag == LOOP_ONE){
			position = position;
			Intent intent = new Intent();
			intent.setAction("com.alex.media.MUSIC_SERVICE");
			intent.putExtra("length", 1);
			startService(intent);
			play();
			return;
		}
		if(random_flag == true)
		{
			if(randomNum < _ids.length-1)
			{
				randomIDs[randomNum] = position;
				position = findRandomSound(_ids.length);
				randomNum++;
				
			}
			else 
			{
				randomNum = 0;
				for(int i = 0; i < _ids.length; i++)
				{
					randomIDs[i] = -1;
				}
				randomIDs[randomNum] = position;
				position = findRandomSound(_ids.length);
				randomNum++;
			}
		}
		else
		{
			if (position==0){
				position = _ids.length-1;
			} else if (position>0){
				position--;
			}
		}
		
		
		stop();
		setup();
		play();
	}
	
	public static int  findRandomSound( int end)
	{
		int ret = -1;
		ret = (int)(Math.random()*end);
		while(havePlayed(ret,end))
		{
			ret = (int)(Math.random()*end);
		}
		return ret;
	}
	public static boolean havePlayed(int position,int num)
	{
		boolean ret = false;
		
		for(int i = 0; i < num; i++)
		{
			if(position == randomIDs[i])
			{
				ret =  true;
				break;
			}
		}
		
		return ret;
	}
	/**
	 * 下一首
	 */
	public void nextOne(){
		if (_ids.length==1 || loop_flag == LOOP_ONE){
			position = position;
			Intent intent = new Intent();
			intent.setAction("com.alex.media.MUSIC_SERVICE");
			intent.putExtra("length", 1);
			startService(intent);
			play();
			return;
			
		} 
		if(random_flag == true)
		{
			if(randomNum < _ids.length-1)
			{
				randomIDs[randomNum] = position;
				position = findRandomSound(_ids.length);
				randomNum++;
				
			}
			else 
			{
				randomNum = 0;
				for(int i = 0; i < _ids.length; i++)
				{
					randomIDs[i] = -1;
				}
				randomIDs[randomNum] = position;
				position = findRandomSound(_ids.length);
				randomNum++;
			}
		}
		else
		{
			if (position == _ids.length-1){
				position = 0;
			} else if (position < _ids.length-1){
				position++;
			}
		}
		
		
		stop();
		setup();
		play();
	}
	
	/**
	 * 定义musicReceiver,接收MusicService发送的广播
	 */
	protected BroadcastReceiver musicReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(MUSIC_CURRENT)){
				currentPosition =  intent.getExtras().getInt("currentTime");//获得当前播放位置
				playtime.setText(toTime(currentPosition));
				seekbar.setProgress(currentPosition);//设置进度条
				Iterator<Integer> iterator=lrc_map.keySet().iterator();
				while(iterator.hasNext()){
					Object o = iterator.next();
		        	LRCbean val = lrc_map.get(o);
		        	if (val!=null){
		        		
			        	if (currentPosition>val.getBeginTime()
			        			&&currentPosition<val.getBeginTime()+val.getLineTime()){
			        		lrcText.setText(val.getLrcBody());
			        		break;
			        	}
		        	}
				}
			} else if (action.equals(MUSIC_DURATION)){
				duration = intent.getExtras().getInt("duration");
				seekbar.setMax(duration);
				durationTime.setText(toTime(duration));
				
			} else if (action.equals(MUSIC_NEXT)){
				nextOne();
			} else if (action.equals(MUSIC_UPDATE)){
				position = intent.getExtras().getInt("position");
				//refreshView();
				//name.setText(_titles[position]);
				setup();
			}
		}
	};
	
	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(musicReceiver);
		
		
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == event.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(this, TestMain.class);
			startActivity(intent);
			finish();
		}
		return true;
    }
	/**
	 * 音量控制
	 */
	public boolean dispatchKeyEvent(KeyEvent event) { 
		int action = event.getAction(); 
		int keyCode = event.getKeyCode(); 
		switch (keyCode) { 
			case KeyEvent.KEYCODE_VOLUME_UP: 
			if (action == KeyEvent.ACTION_UP) {
				if (currentVolume<maxVolume){
					currentVolume = currentVolume + 1;
					mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
				} else {
					mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
				}
			} 
			return false; 
			case KeyEvent.KEYCODE_VOLUME_DOWN: 
			if (action == KeyEvent.ACTION_UP) { 
				if (currentVolume>0){
					currentVolume = currentVolume - 1;
					mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume , 0);
				} else {
					mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
				}
			} 
			return false; 
			default: 
			return super.dispatchKeyEvent(event); 
		} 
	}
	
	/**
	 * 解析歌词
	 * @param path
	 */
	private void read(String path){
		lrc_map.clear();
    	TreeMap<Integer, LRCbean> lrc_read = new TreeMap<Integer, LRCbean>();
    	String data = "";
    	BufferedReader br = null;
    	File file = new File(path);
    	if (!file.exists()){
    		lrcText.setText("歌词文件不存在...");
    		return;
    	}
    	FileInputStream stream = null;
		try {
			stream = new FileInputStream(file);
            br = new BufferedReader(new InputStreamReader(
					stream, "GBK"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		try {
			while((data=br.readLine())!=null){
				if (data.length()>6){
					if (data.charAt(3)==':'&&data.charAt(6)=='.'){//从歌词正文开始
						data = data.replace("[", "");
						data = data.replace("]", "@");
						data = data.replace(".", ":");
						String lrc[] = data.split("@");
						String lrcContent= null;
						if (lrc.length==2){
							lrcContent = lrc[lrc.length-1];//歌词
						}else{
							lrcContent = "";
						}
						for (int i = 0; i<lrc.length-1;i++){
							String lrcTime[] = lrc[0].split(":");
							
							int m = Integer.parseInt(lrcTime[0]);//分
							int s = Integer.parseInt(lrcTime[1]);//秒
							int ms = Integer.parseInt(lrcTime[2]);//毫秒
							
							int begintime = (m*60 + s) * 1000 + ms;//转换成毫秒
							LRCbean lrcbean = new LRCbean();
							lrcbean.setBeginTime(begintime);//设置歌词开始时间
							lrcbean.setLrcBody(lrcContent);//设置歌词的主体
							lrc_read.put(begintime,lrcbean);
						}
					}
				}
			}
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//计算每句歌词需要的时间
		lrc_map.clear();
		data = "";
		Iterator<Integer> iterator = lrc_read.keySet().iterator();
		LRCbean oldval = null;
		int i = 0;
		while (iterator.hasNext()){
			Object ob = iterator.next();
			LRCbean val = lrc_read.get(ob);
			if (oldval==null){
				oldval = val;
			} else{
				LRCbean item1 = new LRCbean();
				item1 = oldval;
				item1.setLineTime(val.getBeginTime()-oldval.getBeginTime());
				lrc_map.put(new Integer(i), item1);
				i++;
				oldval = val;
			}
		}
		
    }
	
	/**
	 * 读取sd卡歌词
	 */
	public void refreshView() {
		myCur = getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				new String[] { 
						MediaStore.Audio.Media.TITLE,
						MediaStore.Audio.Media.DURATION,
						MediaStore.Audio.Media.ARTIST,
						MediaStore.Audio.Media.ALBUM,
						MediaStore.Audio.Media.DISPLAY_NAME,
						MediaStore.Audio.Media.ALBUM_ID}, "_id=?",
				new String[] { _ids[position] + "" }, null);
		myCur.moveToFirst();
		
		AlbumPic.setImageBitmap(getArtwork(this, _ids[position], myCur.getInt(5), true));
		String name = myCur.getString(0);
		read("/mnt/sdcard/" + name + ".lrc");
	}
	
	/**
	 * 时间格式转换函数
	 * @param time
	 * @return
	 */
	public String toTime(int time) {

		time /= 1000;
		int minute = time / 60;
		int hour = minute / 60;
		int second = time % 60;
		minute %= 60;
		return String.format("%02d:%02d", minute, second);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		return gestureDetector.onTouchEvent(event);
	}
	
	
	public static Bitmap getArtwork(Context context, long song_id, long album_id,  
            boolean allowdefault) {  
        if (album_id < 0) {  
            // This is something that is not in the database, so get the album art directly  
            // from the file.  
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
                //先指定原始大小  
                options.inSampleSize = 1;  
                //只进行大小判断  
                options.inJustDecodeBounds = true;  
                //调用此方法得到options得到图片的大小  
                BitmapFactory.decodeStream(in, null, options);  
                //我们的目标是在你N pixel的画面上显示。  
                //所以需要调用computeSampleSize得到图片缩放的比例  
                options.inSampleSize = computeSampleSize(options, 200);  
                //OK,我们得到了缩放的比例，现在开始正式读入BitMap数据  
                options.inJustDecodeBounds = false;  
                options.inDither = false;  
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;  
                in = res.openInputStream(uri); 
                return BitmapFactory.decodeStream(in, null, options);  
            } catch (FileNotFoundException ex) {  
                // The album art thumbnail does not actually exist. Maybe the user deleted it, or  
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
      
    private static Bitmap getArtworkFromFile(Context context, long songid, long albumid) {  
        Bitmap bm = null;  
        byte [] art = null;  
        String path = null;  
        if (albumid < 0 && songid < 0) {  
            throw new IllegalArgumentException("Must specify an album or a song id");  
        }  
        try {  
        	
    		BitmapFactory.Options options = new BitmapFactory.Options(); 

    	    FileDescriptor fd = null;
            if (albumid < 0) {  
                Uri uri = Uri.parse("content://media/external/audio/media/" + songid + "/albumart");  
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");  
                if (pfd != null) {  
                    fd = pfd.getFileDescriptor();  
                   // bm = BitmapFactory.decodeFileDescriptor(fd,null,options);  
                }  
            } else {  
                Uri uri = ContentUris.withAppendedId(sArtworkUri, albumid);  
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");  
                if (pfd != null) {  
                    fd = pfd.getFileDescriptor();  
                    //bm = BitmapFactory.decodeFileDescriptor(fd,null,options);  
                }  
            }  
            options.inSampleSize = 1;  
    	    //只进行大小判断  
    	    options.inJustDecodeBounds = true;  
            //调用此方法得到options得到图片的大小  
            BitmapFactory.decodeFileDescriptor(fd, null, options);  
    	    //我们的目标是在800pixel的画面上显示。  
    	    //所以需要调用computeSampleSize得到图片缩放的比例  
    	    options.inSampleSize = 500;//computeSampleSize(options, 800);  
    	    //OK,我们得到了缩放的比例，现在开始正式读入BitMap数据  
    	    options.inJustDecodeBounds = false;  
    	    options.inDither = false;  
    	    options.inPreferredConfig = Bitmap.Config.ARGB_8888; 
    	    
    	  //根据options参数，减少所需要的内存  
    	    bm = BitmapFactory.decodeFileDescriptor(fd, null, options);  
        } catch (FileNotFoundException ex) {  
   
        }  
        if (bm != null) {  
            mCachedBit = bm;  
        }  
        return bm;  
    }  
    
  //这个函数会对图片的大小进行判断，并得到合适的缩放比例，比如2即1/2,3即1/3  
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
        return BitmapFactory.decodeStream(  
                context.getResources().openRawResource(R.drawable.media_music), null, opts);                 
    }  
    private static final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");  
    private static final BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();  
    private static Bitmap mCachedBit = null;  

	
	
}
