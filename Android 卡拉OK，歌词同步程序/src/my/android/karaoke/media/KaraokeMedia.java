package my.android.karaoke.media;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import my.android.karaoke.bean.LyricSentence;
import my.android.karaoke.bean.LyricText;
import my.android.karaoke.bean.LyricWord;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class KaraokeMedia {

private static KaraokeMedia karaokeMedia=new KaraokeMedia();
	
	//文章每个段落的起始和结束时间段
	private static List<Integer> sentencesTimes=null;
	private static MediaPlayer mp3Player = new MediaPlayer();
	
	private static int currentSentence_index ; //当前段落索引
	private static int currentWord_index;//当前段落单个字的字数索引
	private static int showWord_index;//当前段落单个字的显示字数索引
	
	private static List<LyricSentence> sentences;
	
	private KaraokeMedia(){}
	
	public static KaraokeMedia getInstance() {
		return karaokeMedia;
	}
	
	//开始播放文章
	public static void playArticleMedia(LyricText lyricText,Context context) {
		
		karaokeMedia.prepareSentencesTimes(lyricText);
		
		currentSentence_index=0;
		currentWord_index=0;
		
		mMode = RUNNING;
		karaokeMedia.update();
		
		AssetManager asset = context.getAssets();
		FileDescriptor fd = null;
		try {
			fd = asset.openFd(lyricText.getSound_path()).getFileDescriptor();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		  
		if(fd == null){
			return;
		}
		
		try {
			//mp3Player.setDataSource(article.getSound_path());
			mp3Player.setDataSource(fd);
			mp3Player.prepare();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

		mp3Player.start();
		
		//Log.v("KaraokeMedia playArticleMedia() Duration:", ""+mp3Player.getDuration());
	}
	
	//开始播放某个文章段落
	public static void seekToArticleMedia(int seekTotSentence_index) {
		
		currentSentence_index=seekTotSentence_index;
		mMode = PAUSE;
		
		int seekToTime = sentencesTimes.get(currentSentence_index);
		
		if(mp3Player.isPlaying()){
			
			mp3Player.seekTo(seekToTime);
			
		} else {		

			mp3Player.start();
			mp3Player.seekTo(seekToTime);
		}
		
		mMode = RUNNING;
		karaokeMedia.update();
	}
	
	//暂停播放文章,如果暂停成功则返回true
	public static boolean pauseArticleMedia() {
		
		if(mp3Player.isPlaying()){
			mp3Player.pause();
			mMode = PAUSE;
			
			return true;
		} else {
			return false;
		}
	}
	
	//恢复播放文章,如果恢复成功则返回true
	public static boolean reStartoArticleMedia() {
		
		if(!mp3Player.isPlaying()){		
			mp3Player.start();
			mMode = RUNNING;
			karaokeMedia.update();
			
			return true;
		} else {
			return false;
		}
	}
	
	//初始化文章音频LRC时间段
	private void prepareSentencesTimes(LyricText lyricText){
		
		sentences = lyricText.getSentences();
		int Sentences_size = sentences.size();
		
		if(sentencesTimes!=null){
			sentencesTimes.clear();
		}
		
		sentencesTimes = new ArrayList<Integer>(Sentences_size);
		for(LyricSentence sentence : sentences){
			
			Integer time = sentence.getSentence_offset();
			sentencesTimes.add(time);
		}
	}
	
    private static int mMediaDelay = 10;
    
    private static int mMode;
    public static final int PAUSE = 0;
    public static final int READY = 1;
    public static final int RUNNING = 2;
    public static final int STOP = 3;
    
    //定时器，每mMediaDelay时间循环一次，判断当前音频是否更换段落
    //update在RUNNING状态的更新间隔为此方法执行时间+mMediaDelay，因为经过测试，该方式执行时间大概为100MS，所以间隔时间为100MS左右+mMediaDelay
    //有一定误差
    private void update() {
        if (mMode == RUNNING) {
        	
            int now = mp3Player.getCurrentPosition();

            //Log.v("ArticleMedia update() now:", ""+now);
            
            if(currentSentence_index < (sentencesTimes.size()))	{
            	
            	//判断歌词段落的移动
	            if (now >= sentencesTimes.get(currentSentence_index)) {
	            	
	            //	Log.v("ArticleMedia update():", ""+currentSentence_index);	            	
	            	mOnSentenceChangeListener.OnSentenceChange(currentSentence_index);
	            	currentSentence_index++;	
	            	currentWord_index=0;
	            	showWord_index=0;
	            }	            
            }
            
            if(currentSentence_index <= (sentencesTimes.size()))	{
	            //判断歌词单个字的移动
	            int sentenceIndex4word = currentSentence_index-1;
	        	LyricSentence currentSentence = sentences.get(sentenceIndex4word);
	        	List<LyricWord> words = currentSentence.getWords();
	        	
	        	if(currentWord_index < words.size()) {
	        		
	        		LyricWord word = words.get(currentWord_index);
	            	if (now >= word.getWord_offset()) {
	            		
	            //		Log.v("KaraokeMedia update():", ""+currentWord_index+"/"+words.size());
	            		
	            		showWord_index = showWord_index + word.getWord().length();
	            		
	            		mOnSentenceChangeListener.OnWordChange(showWord_index);	            		
	            		currentWord_index++;
	            	}
	        	}
            }
            
            mRedrawHandler.sleep(mMediaDelay);
        }

    }
	
    private RefreshHandler mRedrawHandler = new RefreshHandler();

    class RefreshHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
        	karaokeMedia.update();
        }

        public void sleep(long delayMillis) {
        	this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
    };
    
	public void SetOnSentenceChangeListener(
			OnSentenceChangeListener l) {
		mOnSentenceChangeListener = l;
	}
	
	OnSentenceChangeListener mOnSentenceChangeListener = null;
	
	public interface OnSentenceChangeListener {
		void OnSentenceChange(int currentSentence_index);
		void OnWordChange(int showWord_index);
	}
	
	//停止播放文章
	public static void stopArticleMedia() {
		
		mMode=STOP;
		
		if(mp3Player!=null) {
			mp3Player.stop();
			mp3Player.release();
			mp3Player=null;
		}		
	}
}
