package wordroid.business;

import java.util.Locale;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

public class TTS {
	private TextToSpeech tts;
	public void speak(Context context,String toSay){
		tts = new TextToSpeech(context, ttsInitListener);
		tts.speak(toSay, TextToSpeech.QUEUE_FLUSH,
	              null);
		Log.i("speak", toSay);
	}
	
	private TextToSpeech.OnInitListener ttsInitListener = new TextToSpeech.OnInitListener()
	  {

	    @Override
	    public void onInit(int status)
	    {
	      // TODO Auto-generated method stub
	      /* 使用美国时区目前不支持中文 */
	      Locale loc = new Locale("us", "", "");
	      /* 检查是否支持输入的时区 */
	      if (tts.isLanguageAvailable(loc) == TextToSpeech.LANG_AVAILABLE)
	      {
	        /* 设定语言 */
	        tts.setLanguage(loc);
	      }
	      tts.setOnUtteranceCompletedListener(ttsUtteranceCompletedListener);
	    }

	  };
	  private TextToSpeech.OnUtteranceCompletedListener ttsUtteranceCompletedListener = new TextToSpeech.OnUtteranceCompletedListener()
	  {
	    @Override
	    public void onUtteranceCompleted(String utteranceId)
	    {
	    }
	  };

}
