package irdc.ex10_10_1;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Bundle;
import android.widget.TextView;

public class EX10_10_1 extends Activity
{
  TextView TextView01;
  private ActivityManager mActivityManager;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    TextView01 = (TextView) findViewById(R.id.TextView01);

    String msg = "";

    /* 取得ActivityManager */
    mActivityManager = (ActivityManager) EX10_10_1.this
        .getSystemService(ACTIVITY_SERVICE);
    /* 取得内存信息 */
    ActivityManager.MemoryInfo menInfo = new ActivityManager.MemoryInfo();
    mActivityManager.getMemoryInfo(menInfo);

    msg += "可用的内存：" + (menInfo.availMem >> 10) + "K";

    TextView01.setText(msg);

  }
}

