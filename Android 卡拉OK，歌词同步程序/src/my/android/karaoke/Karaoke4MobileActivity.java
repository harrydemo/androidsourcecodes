package my.android.karaoke;

import java.util.ArrayList;
import java.util.List;

import my.android.karaoke.bean.LyricSentence;
import my.android.karaoke.bean.LyricText;
import my.android.karaoke.config.Config;
import my.android.karaoke.media.KaraokeMedia;
import my.android.karaoke.media.KaraokeMedia.OnSentenceChangeListener;
import my.android.karaoke.parse.LyricParse;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class Karaoke4MobileActivity extends Activity {
    /** Called when the activity is first created. */
	private final static String lyricpath="kgezhiwang.xml";
	private List<LyricSentence> sentences;
	private List<TextView> txtViewSentence = null; //歌词原文
	
	private static int sentences_size;
	private static int currentSentence_index ; //当前段落索引
	private static int lastSentence_index;  //上个段落索引
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.lrc_show);
        
        initLrcText();
    }
    
    //根据歌词LRC生成对应的SmartText对象
    private void initLrcText(){
    	
    	LyricText lyricText = LyricParse.getLyricTextNew(lyricpath, this);

    	sentences = new ArrayList<LyricSentence>();
    	
    	initialLrc(lyricText);
    	
    	initButton(lyricText);
    }
    
	//初始化歌词显示
	public boolean initialLrc(LyricText lyricText){
		try{
		  
		  if (lyricText == null || lyricText.getSentences().size() == 0){ //如果段落数为0也不可以继续
		    return false;
		  }
		  else{
			sentences = lyricText.getSentences();
			sentences_size = sentences.size();		
			currentSentence_index = -1;
			lastSentence_index	= -1;	
			txtViewSentence = new ArrayList<TextView>();
			LinearLayout layout2 = (LinearLayout) findViewById(R.id.article_main);
	  	    LayoutParams params = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 
	  	    		                               android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	        int mCount = sentences.size();
			for (int i = 0; i < mCount; i ++){
				TextView tempText1 = new TextView(this);
				tempText1.setTextSize(Config.FONTSIZE);
				tempText1.setTextSize(Config.FONTCOLOR);			
				tempText1.setText(sentences.get(i).getSentence());
				txtViewSentence.add(tempText1);
				tempText1.setTextColor(Config.FONTCOLOR);
				tempText1.setId(i);
				tempText1.setOnClickListener( new View.OnClickListener(){
					@Override
					public void onClick(View v) {
						if (currentSentence_index == -1)
							return;
						//lastSentence_index = currentSentence_index; //记住上次的索引id
						currentSentence_index = v.getId(); //点击时，切换至点中的段
						//setSentenceForUI(); /**在UI上显示当前段落*/
						//playMP3();
					}
				});
				layout2.addView(tempText1, i,params);    //i指这个控件在linearLayout的排序号
			}		    
		    return true;
		  }
		}
		catch(Exception ex){
			Toast.makeText(this, "显示URL文件出错", Toast.LENGTH_SHORT).show();
			return false;			
		}
	}
	
	/**在UI上显示当前段落*/
	public void setSentenceForUI(int currentIndex) {

		//正在播放的段落显示为红色
		 if (currentSentence_index != -1){
		     txtViewSentence.get(currentIndex).setTextColor(Config.SELECTED_FONTCOLOR); //更改当前段字体颜色
		     txtViewSentence.get(currentIndex).setBackgroundColor(Color.WHITE);
		 }		
		 
		 //恢复上段播放段落的颜色
		 if(lastSentence_index!=-1) {
			 //txtViewSentence.get(lastSentence_index).setTextColor(Config.FONTCOLOR);
			 txtViewSentence.get(lastSentence_index).setTextColor(Color.CYAN);
			 txtViewSentence.get(lastSentence_index).setBackgroundColor(Color.TRANSPARENT);
		 }
		 lastSentence_index = currentSentence_index;
	}
	
	public void setWordForUI(int showWord_index) {		
		
		//Log.v("Karaoke4MobileActivity setWordForUI():", ""+showWord_index);
		
		if(currentSentence_index < 0 || currentSentence_index >= sentences.size())
			return;
		
		String sentence = sentences.get(currentSentence_index).getSentence();
		SpannableString spannableString = new SpannableString(sentence);		   
		
        spannableString.setSpan( 
            new ForegroundColorSpan(Color.CYAN), 0, 
            showWord_index, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        
        ((TextView)txtViewSentence.get(currentSentence_index)).setText(spannableString);
	}
	
	private void initButton(final LyricText lyricText){
		
		KaraokeMedia.getInstance().SetOnSentenceChangeListener(new OnSentenceChangeListener(){

			@Override
			public void OnSentenceChange(int currentIndex) {
				// TODO Auto-generated method stub
				
				currentSentence_index = currentIndex;
				setSentenceForUI(currentIndex);
			}

			@Override
			public void OnWordChange(int showWord_index) {
				// TODO Auto-generated method stub
				setWordForUI(showWord_index);
			}
			
		});	
		
		KaraokeMedia.playArticleMedia(lyricText,getBaseContext());
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			KaraokeMedia.stopArticleMedia();
			
			finish();
		}
		return false;
	}
}