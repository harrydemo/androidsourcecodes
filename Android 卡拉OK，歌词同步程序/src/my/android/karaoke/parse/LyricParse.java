package my.android.karaoke.parse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import my.android.karaoke.bean.LyricSentence;
import my.android.karaoke.bean.LyricText;
import my.android.karaoke.bean.LyricWord;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class LyricParse {
	
	/**���صĸ������**/
	public static LyricText getLyricTextNew(String lyricpath,Context context) {		
		LyricText lyricText = null; 	
		try {
				String lyricContent = getLyricContent(lyricpath,context); //��ȡ���lyric�����ַ���
			    Log.v("lrcText",lyricContent );
				if ( lyricContent != null && lyricContent != "")
				{ 
					lyricText = new LyricText();
					lyricText = getLyricTextFromString(context,lyricContent);
			        String soundPath = lyricpath.replace(".xml", ".mp3"); //lrc����ҪСд
			        lyricText.setSound_path(soundPath);
				}
				//smartText_map.put(textpath, smartText);
			    
			} catch (Exception e) {
			   e.printStackTrace();
			   //Toast.makeText(context, "�޷������ļ�:" + textpath, Toast.LENGTH_LONG).show();
			   return null;
		   }
	    //}
		return lyricText;
	}
	
	public static String getLyricContent(String lyricpath,Context context) throws XmlPullParserException, IOException{
		
		String lyricContent="";
		
        XmlPullParser xpp = XmlPullFactory.CreateXppFromAssets(lyricpath,context);
        //XmlPullParser xpp = XmlPullFactory.CreateXppFromXml(lyricpath);
        int eventType = xpp.getEventType();
        
        while(eventType != XmlPullParser.END_DOCUMENT) {
        	if(eventType == XmlPullParser.START_TAG) {
        		String tagName = xpp.getName();
        		if("Lyric_1".equals(tagName)) {
        			lyricContent = XmlPullFactory.getAttrValueByName("LyricContent",xpp);
        		}
        	}
        	eventType = xpp.next();
        }
		return lyricContent;
	}
	
	/**�ӽ��ܺ�õ���string����lrc(ֱ�Ӵ��ڴ��н���,������д��ʱ�ļ�)*/	
	public static LyricText getLyricTextFromString(Context context,String lrcText) {
		
		LyricText lyricText = new LyricText();
		
   		 try {
   			 
		    String[] lrcList = lrcText.split("\\["); //���ַ����� [ ���Ϊ����
		    List<LyricSentence> sentences = new ArrayList<LyricSentence>(); //��ʶ���ʱ����ǩ
	    	int count = lrcList.length;
	    	int sentence_id=0;
	    	
	    	for (int i = 0 ; i < count; i ++) {
	    		
	    		if (lrcList[i].trim() != ""){
	    			
			    	String[] lrc_str = lrcList[i].split("\\]");
			    	
			    	//�ж��Ƿ�Ϊ��ʲ��ֻ�����Ϣ����
			    	if(lrc_str.length<2){
			    		
			    		String[] lrc_info = lrc_str[0].split(":");
			    		
			    		if("ti".equals(lrc_info[0])) {
			    			lyricText.setLyric_ti(lrc_info[1]);
			    			continue;
			    		}
			    		
			    		if("ar".equals(lrc_info[0])) {
			    			lyricText.setLyric_ar(lrc_info[1]);
			    			continue;
			    		}
			    		
			    		if("al".equals(lrc_info[0])) {
			    			lyricText.setLyric_al(lrc_info[1]);
			    			continue;
			    		}
			    		
			    		if("by".equals(lrc_info[0])) {
			    			lyricText.setLyric_by(lrc_info[1]);
			    			continue;
			    		}
			    		
			    		if("offset".equals(lrc_info[0])) {
			    			lyricText.setLyric_offset(lrc_info[1]);
			    			continue;
			    		}
			    		
			    	} else {			    		
			    		
			    		LyricSentence sentence = new LyricSentence();
			    		sentence.setSentence_id(sentence_id);			    		
			    		sentence_id++;
			    		
			    		String[] lrc_time = lrc_str[0].split(",");
			    		int sentence_offset = Integer.parseInt(lrc_time[0]);
			    		sentence.setSentence_offset(sentence_offset);
			    		
			    		int sentence_duration = Integer.parseInt(lrc_time[1]);
			    		sentence.setSentence_duration(sentence_duration);

			    		parseLyricWord(sentence,lrc_str[1]);
			    		
			    		sentences.add(sentence);
			    	}
	    		}
	    	}
	    	lyricText.setSentences(sentences);
	    	Log.v("sentences.size()", ""+sentences.size());
		}
	    catch (Exception e) {
		    Toast.makeText(context, "���������ʲ��ֳ���", Toast.LENGTH_LONG).show();
		    return null;
	   }
		return lyricText;
	}
	
	public static void parseLyricWord(LyricSentence sentence,String original_sentence){
		
		List<LyricWord> words = new ArrayList<LyricWord>();
		String sentence_str="";
		
		String[] words_str = original_sentence.split("\\)");
		int count = words_str.length;
		
		for (int i = 0 ; i < count; i ++) {
			
			if(words_str[i].trim() != ""){
				String[] word_str = words_str[i].split("\\(");				
				LyricWord word = new LyricWord();
				
				word.setWord(word_str[0]);
				sentence_str = sentence_str+word_str[0];
				
				String[] word_time = word_str[1].split(",");
	    		int word_offset = Integer.parseInt(word_time[0]);
	    		word.setWord_offset(word_offset);
	    		
	    		int word_duration = Integer.parseInt(word_time[1]);
	    		word.setWord_duration(word_duration);
	    		
	    		words.add(word);
			}			
		}
		
		sentence.setSentence(sentence_str);
		sentence.setWords(words);
	}
}
