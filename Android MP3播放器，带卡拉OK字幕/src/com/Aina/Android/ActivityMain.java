package com.Aina.Android;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityMain extends Activity{
	/** Called when the activity is first created. */
	private ViewlrcKLOK viewlrcKLOK;
	private MediaPlayer mMediaPlayer = null;
	private SeekBar mSeekBar = null;
	private ListView mListView = null;
	private ImageButton mLastbtn = null;
	private ImageButton mStartbtn = null;
	private ImageButton mPausebtn = null;
	private ImageButton mStopbtn = null;
	private ImageButton mNextbtn = null;
	private TextView mTextView = null;
	private ScrollView scrollview =null;
	private ImageView  header =null;
	private TextView lrc = null;
	private List<String> mMusiclist = null;
	private int mCurrent = 0;
	private int count = 0;
	private boolean isrun = false;
	private boolean isauto = false;
	private static final String PATH = "/sdcard/mp3/";
	private static TreeMap<Integer, lrcObject> lrc_map;
	private static int lrc_timeend =0;
	private long   KeyDownOldTime =0;
	private int    KeyDownCount   =0;
	
	private String  songName ="";
	private String  songAuthor ="";
	private NotificationManager mNotificationManager;
	private static int NOTIFICATIONS_ID = R.layout.main;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        
		setContentView(R.layout.main);
		mListView = (ListView) this.findViewById(R.id.lv_music);
		mSeekBar = (SeekBar) this.findViewById(R.id.SeekBar01);
		mTextView = (TextView) this.findViewById(R.id.mTextView);
		viewlrcKLOK = (ViewlrcKLOK) this.findViewById(R.id.lrc_view);
		lrc         = (TextView) this.findViewById(R.id.lrc);
		scrollview  = (ScrollView) this.findViewById(R.id.scrollview);
		mLastbtn = (ImageButton) this.findViewById(R.id.imgbtn_last);
		mStartbtn = (ImageButton) this.findViewById(R.id.imgbtn_start);
		mPausebtn = (ImageButton) this.findViewById(R.id.imgbtn_pause);
		mStopbtn = (ImageButton) this.findViewById(R.id.imgbtn_stop);
		mNextbtn = (ImageButton) this.findViewById(R.id.imgbtn_next);
		header   = (ImageView) this.findViewById(R.id.header);
		
		mMusiclist = new ArrayList<String>();
		mMediaPlayer = new MediaPlayer();

		lrc_map = new TreeMap<Integer, lrcObject>();
		viewlrcKLOK.SetlrcContent("���",0,0,"......",0);
		lrc_timeend = 0;
		
		lrc.setVisibility(View.GONE);
		
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		mMediaPlayer.setOnPreparedListener(prepareListener);
		mMediaPlayer.setOnCompletionListener(CompletionListener);
		mMediaPlayer.setOnErrorListener(ErrorListener);

		
		// ��ʼ��ť
		mStartbtn.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				//lrc.setVisibility(View.GONE); //�ؼ�����
				PlayMusic(PATH + mMusiclist.get(mCurrent));
			}

		});
		// ��һ��
		mNextbtn.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				NextMusic();
			}

		});
		// ��һ��
		mLastbtn.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LastMusic();
			}

		});
		// ��ͣ
		mPausebtn.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isrun = true;
				isauto = false;
				mPausebtn.setVisibility(View.GONE);// ������ͣ��ť
				mStartbtn.setVisibility(View.VISIBLE);// ��ʾ������ť
				// �Ƿ����ڲ���
				if (mMediaPlayer.isPlaying()) {
					viewlrcKLOK.Stop();
					mMediaPlayer.pause();
				}
			}

		});
		// ֹͣ
		mStopbtn.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				StopMusic();
			}

		});
		mListView.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				mCurrent = arg2;
				StopMusic();
				PlayMusic(PATH + mMusiclist.get(mCurrent));
			}

		});
		mSeekBar
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						if (!isauto) {
							mMediaPlayer.seekTo(progress);
						}

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						isauto = false;
					}

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						isauto = true;
					}

				});
		this.MusicList();
	}

	private void setPlayint(String tickerText, String title, String content,
			int drawable) {

		Notification notification = new Notification(drawable, tickerText,
				System.currentTimeMillis());

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, ActivityMain.class), 0);

		notification.setLatestEventInfo(this, title, content, contentIntent);

		mNotificationManager.notify(NOTIFICATIONS_ID, notification);
	}
	
	private MediaPlayer.OnPreparedListener prepareListener = new MediaPlayer.OnPreparedListener(){   
	    public void onPrepared(MediaPlayer mp){   
			isrun = true;
			setPlayint(songAuthor,songName, songAuthor, R.drawable.icon);

			lrc_timeend = 0;
			viewlrcKLOK.SetlrcContent("׼����",0,0,"go,go,go..",0);
			viewlrcKLOK.Star();
			
			count = mMediaPlayer.getDuration();
			Log.i("TAG-count", count + "");
			mSeekBar.setMax(count);// �������ֵ.
			mTextView.setText("��ǰ���Ÿ���:" + mMusiclist.get(mCurrent));
	    	
			mp.start(); //��ʼ���� 
			//Toast.makeText(ActivityMain.this, "��ʼ����",Toast.LENGTH_SHORT).show();
			myHandler.sendEmptyMessage(PROGRESS_CHANGED);
	    }   
	};
	
	//��Ƶ�������
	private MediaPlayer.OnCompletionListener CompletionListener=new MediaPlayer.OnCompletionListener(){
		@Override
		public void onCompletion(MediaPlayer arg0) {
			mMediaPlayer.stop();
			isauto = true;
			mSeekBar.setProgress(0);
			
			viewlrcKLOK.SetlrcContent("׼����",0,0,"... ...",0);
			viewlrcKLOK.Stop();
			//Toast.makeText(ActivityMain.this, "�������:",Toast.LENGTH_SHORT).show();
			NextMusic();
		}
	};

	//����ʱ���ִ���
	private MediaPlayer.OnErrorListener ErrorListener=new MediaPlayer.OnErrorListener(){
		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			// TODO Auto-generated method stub
			mMediaPlayer.stop();
			Toast.makeText(ActivityMain.this, "���ִ���:-������:"+what,Toast.LENGTH_SHORT).show();
			return true;
		}
	};

	public void PlayMusic(String path) {
		try {

			String lrcpath = path;
			//װ�ظ��
			lrcpath = lrcpath.substring(0,lrcpath.length()-4)+".lrc";
			this.lrc.setText(read(lrcpath));
			//Toast.makeText(this,"��",Toast.LENGTH_SHORT).show();
			//װ��ͷ��
			
			String headerFile = path.substring(PATH.length());
			int pos1 = headerFile.indexOf("-");
			int pos2 = headerFile.indexOf(".");
			if (pos1>=0)
			{
				songName   = headerFile.substring(pos1+1,pos2);
				headerFile = headerFile.substring(0,pos1);
			    songAuthor = headerFile.trim();
			    
			}
		
			headerFile = PATH+"pic/"+headerFile.trim()+".jpg";
			
			//Toast.makeText(this, headerFile, Toast.LENGTH_SHORT).show();
			
			File myFile = new File(headerFile);
			if (myFile.exists())
			{
			  Bitmap bm = BitmapFactory.decodeFile(headerFile);
			  header.setImageBitmap(bm);
			}
			else
			{
				  Bitmap bm = BitmapFactory.decodeResource(this.getResources(),R.drawable.back4);
				  header.setImageBitmap(bm);
			}
			
			
			mStartbtn.setVisibility(View.GONE);// ����������ť
			mPausebtn.setVisibility(View.VISIBLE);// ��ʾ��ͣ��ť
			if (!isrun) {
				mMediaPlayer.reset();// ����
				mMediaPlayer.setDataSource(path);// ��������Դ
				mMediaPlayer.prepare();// ׼��
			} else {
				mMediaPlayer.start();// ��֮ͣ����Ų���
				viewlrcKLOK.Star();
			}
			isauto = true;
		} catch (Exception ex) {
			this.ShowDialog("���������쳣:" + ex.getMessage());
		}
	}

	
    //����һ����Ϣ����
	private final static int PROGRESS_CHANGED = 0;
    private final static int HIDE_CONTROLER = 1;
    private final static int VIDEO_READY =3;
    Handler myHandler = new Handler(){
 		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			    case VIDEO_READY:
			    	break;
			    case PROGRESS_CHANGED:
			       {
						int nowTime = mMediaPlayer.getCurrentPosition();
						if (nowTime<count)
						{
							mSeekBar.setProgress(nowTime);
							mSeekBar.invalidate();						
							
							if (nowTime>lrc_timeend && isrun)
							{
								//���Ҹ��
								Iterator<Integer> iterator = lrc_map.keySet().iterator();
								while(iterator.hasNext()) {
									Object ob =iterator.next();
									lrcObject val = (lrcObject)lrc_map.get(ob);
									if (nowTime>val.begintime)
									{
										lrcObject val_1 = new lrcObject();
										val_1.begintime = 0;
										val_1.lrc ="...";
										val_1.timeline = 0;
										
										if (iterator.hasNext())
										{
										  Object ob_1 =iterator.next();
										  val_1 = (lrcObject)lrc_map.get(ob_1);
										}
	
										lrc_timeend = val_1.begintime+val_1.timeline;
										viewlrcKLOK.SetlrcContent(val.lrc,val.timeline,val_1.begintime-val.begintime-val.timeline,val_1.lrc,val_1.timeline);
										viewlrcKLOK.invalidate();
									}
								}
							}
							
							nowTime/=1000;
							int minute = nowTime/60;
							int hour = minute/60;
							int second = nowTime%60;
							minute %= 60;
							  //mTextView.setText(String.format("%02d",nowTime));
	
							mTextView.setText(String.format("%02d:%02d:%02d", hour,minute,second));
				       }
			    	  sendEmptyMessage(PROGRESS_CHANGED);
			       }
					break;
				case HIDE_CONTROLER:
					break;
			}
			super.handleMessage(msg);
		}	
    };   
	
	public void NextMusic() {
		int num = mMusiclist.size();
		if (++mCurrent >= num) {
			mCurrent = 0;
		}
		StopMusic();
		PlayMusic(PATH + mMusiclist.get(mCurrent));
	}

	public void LastMusic() {
		int num = mMusiclist.size();
		if (--mCurrent < 0) {
			mCurrent = num - 1;
		}
		StopMusic();
		PlayMusic(PATH + mMusiclist.get(mCurrent));

	}


	@Override
	protected void onPause() {
		super.onPause();
		/*
		Dialog dialog = new AlertDialog.Builder(this).setTitle("�Ի������")
		.setIcon(R.drawable.icon).setMessage("��½�Ի���")
		// ���öԻ�����ʾһ����ѡ��list
		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				finish();	
			}
		})
		.setNeutralButton("�˳�",  new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		})
		.create();
		dialog.show();
		*/
		setPlayint(songAuthor,songName, songAuthor, R.drawable.icon);
	}

	
	/**  
	* ����  
	*/  

	@Override  
	protected void onDestroy() {   

		isauto = false;
		if (mMediaPlayer.isPlaying()) {
			mMediaPlayer.stop();// ֹͣ
		}
		mMediaPlayer.reset();
		mMediaPlayer.release();
   
	   super.onDestroy();
	}

	
	protected void dialog() {
		AlertDialog.Builder builder = new Builder(ActivityMain.this);
		builder.setMessage("ȷ��Ҫ�˳���?");
		builder.setTitle("��ʾ");
		builder.setPositiveButton("ȷ��",
		new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				mNotificationManager.cancel(NOTIFICATIONS_ID);
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		});
		builder.setNegativeButton("ȡ��",
		new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				onPause();
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	
	@Override
	public boolean onKeyDown(int keyCode,KeyEvent event)
	{
        if (keyCode==79)//������
        {
        	long time = System.currentTimeMillis();
        	if ((time-KeyDownOldTime)/1000>5)
        	    KeyDownCount = 0;
         	KeyDownCount++;
    		Toast.makeText(this, "��������:"+event.getRepeatCount()+"����ʱ��:"+(time-KeyDownOldTime)/1000, Toast.LENGTH_SHORT).show();
        	KeyDownOldTime = time;
        }else if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        			dialog();
        			return true;
       	}
        
        return super.onKeyDown(keyCode, event);        
		/*
        if (keyCode == KeyEvent.KEYCODE_BACK) {
			PackageManager pm = getPackageManager();
	        ResolveInfo homeInfo = pm.resolveActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), 0);
		
				ActivityInfo ai = homeInfo.activityInfo;
				Intent startIntent = new Intent(Intent.ACTION_MAIN);
				startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
				startIntent.setComponent(new ComponentName(ai.packageName,
						ai.name));
				startActivitySafely(startIntent);
				return true;
			} else
				return super.onKeyDown(keyCode, event);
		*/
        
	}		

   void startActivitySafely(Intent intent) {
	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	try {
		startActivity(intent);
	} catch (ActivityNotFoundException e) {
	} catch (SecurityException e) {
	}
}

	public void StopMusic() {
		isrun = false;
		
		viewlrcKLOK.Stop();
		lrc_timeend = 99999;
		mPausebtn.setVisibility(View.GONE);// ������ͣ��ť
		mStartbtn.setVisibility(View.VISIBLE);// ��ʾ������ť

		if (mMediaPlayer.isPlaying()) {
			mMediaPlayer.stop();// ֹͣ
		}
	}

	/**
	 * �ļ�������
	 * 
	 * @author Aina
	 * 
	 */
	class MusicFilter implements FilenameFilter {

		@Override
		public boolean accept(File dir, String filename) {

			return (filename.endsWith(".mp3"));
		}

	}

	/**
	 * ��ȡ�ļ�
	 */
	public static String read(String file) {
		TreeMap<Integer, lrcObject> lrc_read =new TreeMap<Integer, lrcObject>();
		String data = "";
		try {
		  File saveFile=new File(file);
		  FileInputStream stream = new FileInputStream(saveFile);//  context.openFileInput(file);
		  
		  BufferedReader br = new BufferedReader(new InputStreamReader(stream,"GB2312"));   
		  int i = 0;
		  while ((data = br.readLine()) != null) {   
			    data = data.replace("[","");
			    data = data.replace("]","@");
			    
			    String splitdata[] =data.split("@");
			    String lrcContenet = splitdata[splitdata.length-1]; 
			    for (int j=0;j<splitdata.length-1;j++)
			    {
				    String tmpstr = splitdata[j];
				    
				    tmpstr = tmpstr.replace(":",".");
				    tmpstr = tmpstr.replace(".","@");
				    String timedata[] =tmpstr.split("@");

				    int m = Integer.parseInt(timedata[0]);  //��
				    int s = Integer.parseInt(timedata[1]);  //��
				    int ms = Integer.parseInt(timedata[2]); //����
			    	int currTime = (m*60+s)*1000+ms*10;
				    lrcObject item1= new lrcObject();

					item1.begintime = currTime;
					item1.lrc       = lrcContenet;
					lrc_read.put(currTime,item1);// ͼ����Դ��ID
					i++;
			    }
			    
		  } 
		 stream.close();
		}
		catch (FileNotFoundException e) {
		}
		catch (IOException e) {
		}
		
		/*
		 * ����hashmap ����ÿ��������Ҫ��ʱ��
		*/
		lrc_map.clear();
		data ="";
		Iterator<Integer> iterator = lrc_read.keySet().iterator();
		lrcObject oldval  = null;
		int i =0;
	    StringBuffer sb = new StringBuffer();
		while(iterator.hasNext()) {
			Object ob =iterator.next();
			
			lrcObject val = (lrcObject)lrc_read.get(ob);
			if (oldval==null)
				oldval = val;
			else
			{
				lrcObject item1= new lrcObject();
				item1  = oldval;
				item1.timeline = val.begintime-oldval.begintime;
				lrc_map.put(new Integer(i), item1);
				sb.append(String.format("[%04d]-[%04d]-%s\n",item1.begintime,item1.timeline,item1.lrc));
				i++;
				oldval = val;
			}
		}
		data = sb.toString();
		return data;
	}	
	
	/**
	 * �����б�
	 */
	public void MusicList() {
		try {
			File home = new File(PATH);
			File[] f = home.listFiles(new MusicFilter());
			if (f.length > 0) {
				for (int i = 0; i < f.length; i++) {
					File file = f[i];
					mMusiclist.add(file.getName().toString());
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1, mMusiclist);
				mListView.setAdapter(adapter);
			}
		} catch (Exception ex) {
			this.ShowDialog("��ʾ�����б��쳣:" + ex.getMessage());
		}

	}

	public void ShowDialog(String str) {
		new AlertDialog.Builder(this).setTitle("��ʾ").setMessage(str)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}

				}).show();
	}

	/*
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
				if (isauto) {
					int n = mMediaPlayer.getCurrentPosition();
					Message msg = new Message();
					msg.what = n;
					handler.sendMessage(msg);
				}
				Thread.sleep(100);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
	}
	*/

}