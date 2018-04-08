package irdc.ex07_18;

import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.EditText;

public class EX07_18 extends Activity
{
  public static String TAG = "EX07_18_DEBUG";
  private TextToSpeech tts;
  private EditText EditText01;
  private ImageButton ImageButton01;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    /* 传入context及OnInitListener */
    tts = new TextToSpeech(this, ttsInitListener);

    EditText01 = (EditText) this.findViewById(R.id.EditText01);
    ImageButton01 = (ImageButton) this.findViewById(R.id.ImageButton01);

    ImageButton01.setOnClickListener(new ImageButton.OnClickListener()
    {

      @Override
      public void onClick(View v)
      {
        // TODO Auto-generated method stub
        if (EditText01.getText().length() > 0)
        {
          /* 传入要说的字符串 */
          tts.speak(EditText01.getText().toString(), TextToSpeech.QUEUE_FLUSH,
              null);
        } else
        {
          /* 无输入字符串时 */
          tts.speak("Nothing to say", TextToSpeech.QUEUE_FLUSH, null);
        }

      }

    });
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
      Log.i(TAG, "TextToSpeech.OnInitListener");
    }

  };
  private TextToSpeech.OnUtteranceCompletedListener ttsUtteranceCompletedListener = new TextToSpeech.OnUtteranceCompletedListener()
  {
    @Override
    public void onUtteranceCompleted(String utteranceId)
    {
      // TODO Auto-generated method stub
      Log.i(TAG, "TextToSpeech.OnUtteranceCompletedListener");
    }
  };

  @Override
  protected void onDestroy()
  {
    // TODO Auto-generated method stub
    /* 释放TextToSpeech的资源 */
    tts.shutdown();
    Log.i(TAG, "tts.shutdown");
    super.onDestroy();
  }

}

