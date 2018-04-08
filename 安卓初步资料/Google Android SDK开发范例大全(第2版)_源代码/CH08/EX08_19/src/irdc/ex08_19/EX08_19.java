package irdc.ex08_19;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class EX08_19 extends Activity
{
  private TextView TextView01;
  private Button Button01;
  public static final int VOICE_RECOGNITION_REQUEST_CODE = 0x1008;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    TextView01 = (TextView) this.findViewById(R.id.TextView01);
    Button01 = (Button) this.findViewById(R.id.Button01);

    Button01.setOnClickListener(new Button.OnClickListener()
    {

      @Override
      public void onClick(View v)
      {
        // TODO Auto-generated method stub
        PackageManager pm = getPackageManager();
        /* 查询有无安装Google Voice Search Engine */
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
            RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        /* 若有安装Google Voice Search Engine */
        if (activities.size() != 0)
        {
          try
          {
            /* 语音识别Intent */
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            /* 识别画面出现的说明 */
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "语音识别");
            /* 启动语音识别Intent */
            startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);

          } catch (Exception e)
          {
            TextView01.setText("" + e.getMessage());
            Toast.makeText(EX08_19.this, e.getMessage(), Toast.LENGTH_LONG)
                .show();
          }

        } else
        {
          TextView01.setText("RecognizerIntent NOT Found!");
          Toast.makeText(EX08_19.this, "RecognizerIntent NOT Found!",
              Toast.LENGTH_LONG).show();
        }
      }

    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    // TODO Auto-generated method stub
    switch (requestCode)
    {
      case VOICE_RECOGNITION_REQUEST_CODE:
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE
            && resultCode == RESULT_OK)
        {
          String strRet = "";

          /* 取得识别结果 */
          ArrayList<String> results = data
              .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
          for (int i = 0; i < results.size(); i++)
          {
            strRet += results.get(i);
          }

          if (strRet.length() > 0)
          {
            TextView01.setText(strRet);
            Toast.makeText(EX08_19.this, strRet, Toast.LENGTH_LONG).show();

            /* 启动google网页搜索 */
            Intent search = new Intent(Intent.ACTION_WEB_SEARCH);
            search.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            /* 放大搜索字符串 */
            search.putExtra(SearchManager.QUERY, strRet);
            final Bundle appData = getIntent().getBundleExtra(
                SearchManager.APP_DATA);
            if (appData != null)
            {
              search.putExtra(SearchManager.APP_DATA, appData);
            }
            startActivity(search);

          } else
          {
            TextView01.setText("Can not recognize...");
            Toast.makeText(EX08_19.this, "Can not recognize...",
                Toast.LENGTH_LONG).show();
          }
        }
        break;
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

}

