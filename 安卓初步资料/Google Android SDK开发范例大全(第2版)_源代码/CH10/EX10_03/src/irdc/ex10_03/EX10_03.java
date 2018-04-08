package irdc.ex10_03;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EX10_03 extends Activity
{
  /* 放置设定值的文件 */
  public static String fileName = "mc.ini";
  /* 上次MC第一天的日期 */
  public static String mcdate_key = "mcdate";
  private String mcdate_value = "";
  /* MC周期 */
  public static String period_key = "period";
  private String period_value = "28";
  /* 每日提醒时间 */
  public static String remind_key = "remind";
  private String remind_value = "1200";

  private TextView TextView02;
  private TextView TextView04;
  private TextView TextView05;
  private TextView TextView08;
  private Button Button01;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    TextView02 = (TextView) this.findViewById(R.id.TextView02);
    TextView04 = (TextView) this.findViewById(R.id.TextView04);
    TextView05 = (TextView) this.findViewById(R.id.TextView05);
    TextView08 = (TextView) this.findViewById(R.id.TextView08);
    Button01 = (Button) this.findViewById(R.id.Button01);

    /* 开启设定画面 */
    Button01.setOnClickListener(new Button.OnClickListener()
    {

      @Override
      public void onClick(View arg0)
      {
        Intent intent = new Intent();
        intent.setClass(EX10_03.this, EX10_03_1.class);
        Bundle bundle = new Bundle();
        bundle.putString(mcdate_key, mcdate_value);
        bundle.putString(period_key, period_value);
        bundle.putString(remind_key, remind_value);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
      }
    });
    /* 检查文件mc.ini是否存在 */
    checkFile();
    /* 算有关日期 */
    calDate();

  }

  /* 检查文件mc.ini是否存在 */
  private void checkFile()
  {
    boolean isExit = true;

    FileOutputStream fos = null;
    try
    {
      openFileInput(fileName);
    } catch (FileNotFoundException e)
    {
      isExit = false;
    }
    if (!isExit)
    {
      try
      {
        fos = openFileOutput(fileName, MODE_WORLD_WRITEABLE);
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        /* 系统日期为上次MC第一天的日期 */
        mcdate_value = DateUtil.getDateTime("yyyyMMdd", System
            .currentTimeMillis());
        String txt = mcdate_key + "=" + mcdate_value;
        bos.write(txt.getBytes());
        /* 周期为28天 */
        bos.write(new String("\n").getBytes());
        txt = period_key + "=" + period_value;
        bos.write(txt.getBytes());
        /* 提醒时间为中午12点 */
        bos.write(new String("\n").getBytes());
        txt = remind_key + "=" + remind_value;
        bos.write(txt.getBytes());

        bos.close();
        fos.close();

      } catch (FileNotFoundException e)
      {
        e.printStackTrace();
      } catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    /* 将文件mc.ini里的值取出 */
    Properties p = new Properties();
    try
    {
      p.load(openFileInput(fileName));
      mcdate_value = p.getProperty(mcdate_key);
      period_value = p.getProperty(period_key);
      remind_value = p.getProperty(remind_key);
    } catch (FileNotFoundException e)
    {
      e.printStackTrace();
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private void calDate()
  {

    String format = "yyyy.MM.dd";

    /* 上次MC第一天的日期 */
    TextView02.setText(DateUtil
        .getNextDate(mcdate_value, 0, format));

    /* 预估下次MC日期 */
    TextView04.setText(DateUtil.getNextDate(mcdate_value, Integer
        .parseInt(period_value), format));

    /* 距离现在还有N天 */
    String nDate = DateUtil.getNextDate(mcdate_value, Integer
        .parseInt(period_value), "yyyyMMdd");
    int days = DateUtil.computerDiffDate(DateUtil
        .getDateTime(nDate), System.currentTimeMillis());
    String text = "";
    if (days >= 0)
    {
      text += getResources().getString(R.string.strMessage5);
      text += days;
      text += getResources().getString(R.string.strMessage7);
    } else
    {
      text += getResources().getString(R.string.strMessage8);
      text += Math.abs(days);
      text += getResources().getString(R.string.strMessage7);
    }
    TextView05.setText(text);

    /* 排卵日，公式=即从下次月经来潮的第一天起，倒数14天就是排卵期 */
    TextView08.setText(DateUtil.getNextDate(nDate, -14, format));
  }
}

