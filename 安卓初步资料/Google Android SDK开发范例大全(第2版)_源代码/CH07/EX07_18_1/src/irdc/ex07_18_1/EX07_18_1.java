package irdc.ex07_18_1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class EX07_18_1 extends Activity implements OnInitListener
{
  int MY_DATA_CHECK_CODE = 99;
  private Button Button01;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    Button01 = (Button) findViewById(R.id.Button01);
    Button01.setOnClickListener(new OnClickListener()
    {

      @Override
      public void onClick(View v)
      {
        // TODO Auto-generated method stub
        Intent check = new Intent();
        /* 检查是否安装TTS */
        check.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(check, MY_DATA_CHECK_CODE);
      }
    });

  }

  protected void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    if (requestCode == MY_DATA_CHECK_CODE)
    {
      if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS)
      {
        /* 已安装 */
        Toast.makeText(this, "已安装", Toast.LENGTH_SHORT).show();
      } else
      {
        /* 无安装时启动安装画面 */
        Intent install = new Intent();
        install.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
        startActivity(install);
      }
    }
  }

  @Override
  public void onInit(int status)
  {
    // TODO Auto-generated method stub

  }
}

