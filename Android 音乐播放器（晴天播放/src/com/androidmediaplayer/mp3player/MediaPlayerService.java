package com.androidmediaplayer.mp3player;

import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.RemoteViews;

import com.androidmediaplayer.R;
import com.androidmediaplayer.constant.AppConstant;
import com.androidmediaplayer.constant.ConstantExtendsApplication;
import com.androidmediaplayer.model.Mp3Info;
import com.androidmediaplayer.utils.LinkedListPlayList;

public class MediaPlayerService extends Service {

	private MediaPlayer mediaPlayer = null;
	private boolean isPause = false;
	private boolean isStop = true;
	private Mp3Info mp3Info = null;
	private Intent intent = null;
	private NotificationManager notificationManager = null;
	private ConstantExtendsApplication cea = null;
	private SharedPreferences sp = null;
	private boolean canSendBrocast = true;

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	@Override
	public void onCreate() {
		System.out.println("服务启动");
		super.onCreate();
		sp = getSharedPreferences("playMode", MODE_PRIVATE);
		cea = (ConstantExtendsApplication)getApplicationContext();
		intent = new Intent();
		intent.setAction(AppConstant.Action.MEDIAPLAYERSERVICE_MESSAGE_ACTION);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("服务停止");
		if (null != mediaPlayer) {
			notificationManager.cancel(R.drawable.appwidget_played);
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	private void play() {
		// 第一次
		if (null == mediaPlayer) {
			mediaPlayer = new MediaPlayer();
			setMediaPlayerListener();
			setTelphoneListener();
		}
		mediaPlayer.reset();
		try {
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			if (mp3Info.getPath().contains("http")) {
				mediaPlayer.setDataSource(MediaPlayerService.this,
						Uri.parse(mp3Info.getPath()));
			} else {
				mediaPlayer.setDataSource(MediaPlayerService.this,
						Uri.parse("file://" + mp3Info.getPath()));
			}
			mediaPlayer.prepare();
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		mediaPlayer.start();
	}

	private Binder binder = new IMediaPlayerService.Stub() {

		@Override
		public void start() throws RemoteException {
			mediaPlayer.start();
			sendNotification();
			isPause = false;
			isStop = false;
		}

		@Override
		public void stop() throws RemoteException {
			mediaPlayer.stop();
			isPause = false;
			isStop = true;
		}

		@Override
		public void pause() throws RemoteException {
			mediaPlayer.pause();
			isPause = true;
			isStop = false;
		}

		@Override
		public void release() throws RemoteException {
			mediaPlayer.release();
			mediaPlayer = null;
			isPause = false;
			isStop = true;
		}

		@Override
		public void next() throws RemoteException {

		}

		@Override
		public void previous() throws RemoteException {

		}

		@Override
		public int getCurrentPosition() throws RemoteException {
			return mediaPlayer.getCurrentPosition();
		}

		@Override
		public int getDuration() throws RemoteException {
			return mediaPlayer.getDuration();
		}

		@Override
		public void seekTo(int progress) throws RemoteException {
			mediaPlayer.seekTo(progress);
		}

		@Override
		public boolean isPlaying() throws RemoteException {
			return mediaPlayer.isPlaying();
		}

		@Override
		public boolean isPause() throws RemoteException {
			return isPause;
		}

		@Override
		public boolean isStop() throws RemoteException {
			return isStop;
		}
		
		@Override
		public void setPlayMode(int playMode) throws RemoteException {
			
		}

		@SuppressWarnings("rawtypes")
		@Override
		public void setFileInfo(List fileInfo) throws RemoteException {
			canSendBrocast = true;
			// 第一次进入播放
			if (null == mp3Info) {
				mp3Info = (Mp3Info) fileInfo.get(0);
				play();
				sendNotification();
				cea.setHadInitPlayer(true);
				cea.setCurrentPlayingMp3Info(mp3Info);
			} else {
				String audio_id = ((Mp3Info) fileInfo.get(0)).getAudio_id();
				// 选择原来的歌
				if (mp3Info.getAudio_id().equals(audio_id)) {
					// 暂停或者停止则start
					if (!mediaPlayer.isPlaying()) {
						mediaPlayer.start();
						sendNotification();
					} else{ //原来的歌正在播放
						
					}
				} else { // 选择了其他的歌
					mp3Info = (Mp3Info) fileInfo.get(0);
					mediaPlayer.stop();
					play();
					sendNotification();
					cea.setCurrentPlayingMp3Info(mp3Info);
				}
			}
		}
	};
	
	private void sendNotification(){
		Notification notification = null;
		RemoteViews remoteView = null;
		Intent intent = null;
		PendingIntent pendingIntent = null;
		notification = new Notification();
		notificationManager = (NotificationManager)(this.getSystemService(NOTIFICATION_SERVICE));
		remoteView = new RemoteViews(getPackageName(), R.layout.mediaplayer_playing_notification_320_480);
		intent = new Intent(this,MyPlayerActivity.class); 
		intent.putExtra("mp3Info", mp3Info);
		pendingIntent = PendingIntent.getActivity(this, 0 ,intent , PendingIntent.FLAG_UPDATE_CURRENT);
		notification.icon = R.drawable.appwidget_played;
		remoteView.setImageViewResource(R.id.image, R.drawable.appwidget_played);
		remoteView.setTextViewText(R.id.playingNotName, mp3Info.getMp3Name());
		remoteView.setTextViewText(R.id.playingNotArtist, "- " +  mp3Info.getArtist());
		notification.contentView = remoteView;
		notification.contentIntent = pendingIntent;
		notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;
		notificationManager.notify(R.drawable.appwidget_played, notification);
	}
	
	private void sendBroadcast(int MSG){
		intent = new Intent();
		intent.setAction(AppConstant.Action.MEDIAPLAYERSERVICE_MESSAGE_ACTION);
		intent.putExtra("MEDIAPLAYERMSG", MSG);
		sendBroadcast(intent);
		intent.removeExtra("MEDIAPLAYERMSG");
	}
	
	private int shuffle(int x, int y){
		return (int)(x + Math.random()*(y - x));
	}

	private void setMediaPlayerListener() {
		mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {

			}
		});
		mediaPlayer
				.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
					@Override
					public void onBufferingUpdate(MediaPlayer mp, int percent) {
						// 提示缓冲
					}
				});
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				if(cea.isPlayerActivityExist()){
					if(canSendBrocast){
						sendBroadcast(AppConstant.PlayerMsg.COMPLETE);
					}
				}else{
				    System.out.println(LinkedListPlayList.size());
					if(LinkedListPlayList.size()>0){
						int mode = sp.getInt("playMode", -1);
						System.out.println("mode:"+mode);
						switch (mode) {
						case AppConstant.PlayMode.MODE_LIST_REPEAT:
							try {
								LinkedListPlayList.listPointer++;
								MediaPlayerService.this.mp3Info = LinkedListPlayList.get(LinkedListPlayList.listPointer);
							} catch (IndexOutOfBoundsException e) {
								e.printStackTrace();
								System.out.println("列表播放完毕");
								LinkedListPlayList.listPointer = 0;
								MediaPlayerService.this.mp3Info = LinkedListPlayList.getFirst();
							}
							cea.setCurrentPlayingMp3Info(mp3Info);
							play();
							break;
						case AppConstant.PlayMode.MODE_NOAMAL:
							try {
								LinkedListPlayList.listPointer++;
								MediaPlayerService.this.mp3Info = LinkedListPlayList.get(LinkedListPlayList.listPointer);
								play();
								cea.setCurrentPlayingMp3Info(mp3Info);
							} catch (IndexOutOfBoundsException e) {
								e.printStackTrace();
								System.out.println("列表播放完毕");
								LinkedListPlayList.listPointer = 0;
							}
							break;
						case AppConstant.PlayMode.MODE_SHUFFLE:
							if(LinkedListPlayList.size()>1){
								int _index3 = LinkedListPlayList.listPointer;
								int index3 = shuffle(0,  LinkedListPlayList.size());
								while(index3 == _index3){
									index3 = shuffle(0,  LinkedListPlayList.size());
								}
								MediaPlayerService.this.mp3Info = LinkedListPlayList.get(index3);
								cea.setCurrentPlayingMp3Info(mp3Info);
								LinkedListPlayList.listPointer = index3;
								play();
							}else{ //随机模式 ,播放列表只有一首歌
								LinkedListPlayList.listPointer = 0;
								mediaPlayer.start();
							}
							break;
						case AppConstant.PlayMode.MODE_SINGLE_REPEAT:
						    System.out.println("单曲循环结束");
							mediaPlayer.start();
							break;
						}
						sendNotification();
					}
				}
			}
		});
		mediaPlayer.setOnErrorListener(new OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// 下一首
				System.out.println("mediaplayer 出错了!!!");
				int playMode = sp.getInt("playMode", -1);
				if(playMode == AppConstant.PlayMode.MODE_SHUFFLE){
					if(LinkedListPlayList.size() > 0){
						LinkedListPlayList.remove(LinkedListPlayList.listPointer);
					}
					if(LinkedListPlayList.size() < 1){
						//播放列表没有歌曲时，不发送广播
						canSendBrocast = false;
					}
				} else if(playMode == AppConstant.PlayMode.MODE_SINGLE_REPEAT){
					//单曲循环，如果是有问题的歌曲则不播放广播
					canSendBrocast = false;
				} else{
					if(LinkedListPlayList.size() > 0){
						LinkedListPlayList.remove(LinkedListPlayList.listPointer);
					}else{
						//播放列表没有歌曲时
						canSendBrocast = false;
					}
				}
				notificationManager.cancel(R.drawable.appwidget_played);
				return false;
			}
		});
	}
	
	// 设置电话监听
	private void setTelphoneListener() {
		TelephonyManager manager = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		PhoneStateListener listener = new PhoneStateListener() {
			@Override
			public void onCallStateChanged(int state, String incomingNumber) {
				switch (state) {
				case TelephonyManager.CALL_STATE_RINGING:// 电话进来时
					if (mediaPlayer.isPlaying()) {
						mediaPlayer.pause();
					}
					break;
				case TelephonyManager.CALL_STATE_OFFHOOK:// 拨打电话时
					if (mediaPlayer.isPlaying()) {
						mediaPlayer.pause();
					}
					break;
				case TelephonyManager.CALL_STATE_IDLE: // 没有电话时
					if (null != mediaPlayer && !mediaPlayer.isPlaying()) {
						mediaPlayer.start();
					}
					break;
				}
			}
		};
		manager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	}

}
