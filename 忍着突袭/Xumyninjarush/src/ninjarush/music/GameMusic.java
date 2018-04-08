package ninjarush.music;

import java.util.HashMap;
import java.util.Map;

import ninjarush.mainactivity.R;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

public class GameMusic {
	public static MediaPlayer mediaPlayer;//���� �������ֲ�����
	public static SoundPool soundPool;//���� ��Ч������
	public static MediaPlayer mp;//���ŷ���������
	public static MediaPlayer mprun;//������
	
	private static boolean musicSwitch = true;//���ֿ���
	private static boolean soundSwitch = true;//��Ч����
	
	private static Map<Integer,Integer> soundMap; //��Ч��Դid����ع������Դid��ӳ���ϵ��

	
	private static Context context;
	
	//��ʼ�� ����������
	public static void inIt(Context c){
		context = c;
	}
	//��ʼ������������
	public static void windMediaplayer(){
		mp = MediaPlayer.create(context, R.raw.wind);
		mp.setLooping(true);
	}
	//��ʼ�� run������
	public static void runMediaplayer(){
		mprun = MediaPlayer.create(context, R.raw.run);
		mprun.setLooping(true);
	}
	//��ʼ���������� ������
	public static void inItMusic(int resid){
		mediaPlayer = MediaPlayer.create(context, resid);
		mediaPlayer.setLooping(true);
	}
	
	//��ʼ����Ч������   
	public static void inItSound(){
		//��һ������ 10 ��ʾ ����ܹ����ŵ���Ч���� 
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		
		soundMap = new HashMap<Integer, Integer>();
		//����Ч��Դ���� soundPool��������soundMap ӳ��
		soundMap.put(R.raw.boss_die,soundPool.load(context, R.raw.boss_die, 1));
		soundMap.put(R.raw.boss_hurt,soundPool.load(context, R.raw.boss_hurt, 1));
		soundMap.put(R.raw.changed,soundPool.load(context, R.raw.changed, 1));
		soundMap.put(R.raw.changing,soundPool.load(context, R.raw.changing, 1));
		soundMap.put(R.raw.crow_hurt,soundPool.load(context, R.raw.crow_hurt, 1));
		soundMap.put(R.raw.dead,soundPool.load(context, R.raw.dead, 1));
		soundMap.put(R.raw.eat,soundPool.load(context, R.raw.eat, 1));
		soundMap.put(R.raw.enemy_die,soundPool.load(context, R.raw.enemy_die, 1));
		soundMap.put(R.raw.hurt,soundPool.load(context, R.raw.hurt, 1));
		soundMap.put(R.raw.jump,soundPool.load(context, R.raw.jump, 1));
		soundMap.put(R.raw.land,soundPool.load(context, R.raw.land, 1));
		soundMap.put(R.raw.run,soundPool.load(context, R.raw.run, 1));
		soundMap.put(R.raw.shoot,soundPool.load(context, R.raw.shoot, 1));
		soundMap.put(R.raw.sword,soundPool.load(context, R.raw.sword, 1));
		soundMap.put(R.raw.wind,soundPool.load(context, R.raw.wind, 1));
		soundMap.put(R.raw.woop,soundPool.load(context, R.raw.woop, 1));	
		
	}
	
	//��ͣ����
	public static void pauseMusic(){
		if(mediaPlayer.isPlaying()){
			mediaPlayer.pause();
		}
	}
	
	//��ͣ����
	
	public static void pauseWind(){
		if(mp.isPlaying()){
			mp.pause();
		}
	}
	//��ͣ run
	public static void pauseRun(){
		if(mprun.isPlaying()){
			mprun.pause();
		}
	}
	//��������
	public static void startMusic(){
		if(musicSwitch){
			mediaPlayer.start();
		}
	}
	
	//���ŷ���
	public static void startWind(){
		if(musicSwitch){
			mp.start();
		}
	}
	
	//����run
	public static void startRun(){
		if(musicSwitch){
			mprun.start();
		}
	}
	
	//�л����� ������
	public static void nextMusic(int resid){
		releaseMusic();
		inItMusic(resid);
		startMusic();
	}
	
	
	//�л�����  ������
	public static void nextWind(int resid){
		releaseWind();
		windMediaplayer();
		startWind();
	}
	
	//�л�run  ������
	public static void nextrun(int resid){
		releaseRun();
		runMediaplayer();
		startRun();
	}
	
	//�ͷ�������Դ
	public static void releaseMusic(){
		if(mediaPlayer != null){
			mediaPlayer.release();
		}
	}
	
	//�ͷŷ�����Դ
	public static void releaseWind(){
		if(mp != null){
			mp.release();
		}
	}
	
	//�ͷ�run����Դ
	public static void releaseRun(){
		if(mprun != null){
			pauseRun();
			mprun.release();
		}
	}
	
	//�������ֿ��� 
	public static void setMusicSwitch(boolean musicSwitch){
		GameMusic.musicSwitch = musicSwitch;
		if(GameMusic.musicSwitch){
			mediaPlayer.start();
		}
		else{
			mediaPlayer.stop();
		}
	}
	
	//������Ч
	// resId raw�ļ����е� ��Ч��Դ��
	/*loop ���� ѭ�����ŵĴ�����
     0Ϊֵ����һ�Σ�-1Ϊ����ѭ��������ֵΪ����loop+1��(���磬3Ϊһ������4��).*/
	public static int playSound(int resId,int loop){
		
		if(!soundSwitch){
			return 0;
		}
		
		Integer soundId = soundMap.get(resId);
        if(soundId != null){
        	if(soundId==soundMap.get(R.raw.run))
        		 return soundPool.play(soundId, 1, 1, 1, loop, 0.7f);
        	 return soundPool.play(soundId, 1, 1, 1, loop, 1);
        }
        else{
        	return 0;
        }
          

	}
	
	//��ͣ��Ч
	// streamID ��ʾ soundPool ��Ч���� ��id
	public static void pauseSound(int streamID){
		
		if(streamID !=0 ){
			soundPool.pause(streamID);
		}
	
	}
	
	//������Ч
	// streamID ��ʾ soundPool ��Ч���� ��id
	public static void resumeSound(int streamID){
		
		if(streamID !=0 ){
			soundPool.resume(streamID);
		}
		
	}
	
	//��ֹ��Ч 
	// streamID ��ʾ soundPool ��Ч���� ��id
		public static void stopSound(int streamID){
			
			if(streamID !=0 ){
				soundPool.stop(streamID);
			}
			
		}
		
		public static void releaseSound(){
			if(soundPool != null){
				soundPool.release();
			}
		}

}



























