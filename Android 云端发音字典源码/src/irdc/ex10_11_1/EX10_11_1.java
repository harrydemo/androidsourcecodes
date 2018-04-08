package irdc.ex10_11_1;

import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EX10_11_1 extends Activity
{

  private TextView TextView01;
  private Button Button01, Button02;
  private EditText EditText01;
  private WebView WebView01;
  private Handler mHandler01 = new Handler();
  private TextToSpeech tts;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    TextView01 = (TextView) this.findViewById(R.id.TextView01);
    Button01 = (Button) this.findViewById(R.id.Button01);
    Button02 = (Button) this.findViewById(R.id.myButton2);
    /* �򽫷�����ť����ʾ */
    Button02.setVisibility(View.INVISIBLE);
    EditText01 = (EditText) this.findViewById(R.id.EditText01);
    EditText01.setText("����");
    WebView01 = (WebView) this.findViewById(R.id.myWebView1);

    /* ȡ��WebSettings */
    WebSettings webSettings = WebView01.getSettings();
    /* �趨?ִ��JavaScript */
    webSettings.setJavaScriptEnabled(true);
    /* �趨��html���õĶ������� */
    WebView01.addJavascriptInterface(new runJavaScript(), "irdc");
    /* ��assets/google_translate.html���� */
    WebView01.loadUrl("file:///android_asset/google_translate.html");
    /* ����context��OnInitListener */
    tts = new TextToSpeech(this, ttsInitListener);

    Button01.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        // TODO Auto-generated method stub
        TextView01.setText("");
        Button02.setVisibility(View.INVISIBLE);
        if (EditText01.getText().toString().length() > 0)
        {
          /* ����google_translate.html���javascript */
          WebView01.loadUrl("javascript:google_translate('"
              + EditText01.getText().toString() + "')");
        }
      }
    });

    Button02.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        // TODO Auto-generated method stub
        if (TextView01.getText().toString().length() > 0)
        {
          String txt = TextView01.getText().toString();
          HashMap<String, String> myHash = new HashMap();
          String fileName = "/sdcard/" + txt + ".wav";
          /* ���輸��ID??onUtteranceCompletedȡ�� */
          myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,
              "UTTERANCE_ID_01");
          /* �����ļ� */
          tts.synthesizeToFile(txt, myHash, fileName);
          tts.addSpeech(txt, fileName);
          /* ����Ҫ˵���ַ��� */
          tts.speak(TextView01.getText().toString(), TextToSpeech.QUEUE_FLUSH,
              myHash);
        } else
        {
          tts.speak("Nothing to say", TextToSpeech.QUEUE_FLUSH, null);
        }
      }
    });

  }

  final class runJavaScript
  {
    public void runOnAndroidJavaScript(final String strRet)
    {
      mHandler01.post(new Runnable()
      {
        public void run()
        {
          if (!strRet.equals(""))
          {
            TextView01.setText(strRet);
            /* ��ʾ������ť */
            Button02.setVisibility(View.VISIBLE);
          } else
          {
            TextView01.setText("�Ҳ������ذ�Ӣ�İ�ť");
          }
        }
      });
    }
  }

  private TextToSpeech.OnInitListener ttsInitListener = new TextToSpeech.OnInitListener()
  {
    @Override
    public void onInit(int status)
    {
      // TODO Auto-generated method stub
      /* ʹ������ʱ��Ŀǰ��֧������ */
      Locale loc = new Locale("us", "", "");
      /* �����?֧Ԯ���ɵ�ʱ�� */
      if (tts.isLanguageAvailable(loc) == TextToSpeech.LANG_AVAILABLE)
      {
        /* �趨���� */
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
      // TODO Auto-generated method stub
    }
  };

  @Override
  protected void onDestroy()
  {
    // TODO Auto-generated method stub
    /* �ͷ�TextToSpeech����Դ */
    tts.shutdown();
    super.onDestroy();
  }
}

