package irdc.ex10_03;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

public class EX10_03_1 extends Activity
{
  /* 上次MC第一天的日期 */
  private String mcdate_value = "";
  /* MC周期 */
  private String period_value = "";
  /* 每日提醒时间 */
  private String remind_value = "";
  /* 宣告日期变量 */
  private int mYear;
  private int mMonth;
  private int mDay;
  private DatePicker DatePicker01;
  private EditText EditText01;
  private Button Button02;
  private TimePicker TimePicker01;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_1);

    DatePicker01 = (DatePicker) findViewById(R.id.DatePicker01);
    EditText01 = (EditText) this.findViewById(R.id.EditText01);
    Button02 = (Button) this.findViewById(R.id.Button02);
    TimePicker01 = (TimePicker) findViewById(R.id.TimePicker01);

    Bundle bunde = this.getIntent().getExtras();
    mcdate_value = bunde.getString(EX10_03.mcdate_key);
    period_value = bunde.getString(EX10_03.period_key);
    remind_value = bunde.getString(EX10_03.remind_key);

    /* MC周期 */
    EditText01.setText(period_value);

    /* 上次MC第一天的日期设定于DatePicker */
    Calendar calendar = Calendar.getInstance();
    if (mcdate_value != null)
    {
      mYear = Integer.parseInt(mcdate_value.substring(0, 4));
      mMonth = Integer.parseInt(mcdate_value.substring(4, 6)) - 1;
      mDay = Integer.parseInt(mcdate_value.substring(6, 8));
    } else
    {
      mYear = calendar.get(Calendar.YEAR);
      mMonth = calendar.get(Calendar.MONTH);
      mDay = calendar.get(Calendar.DAY_OF_MONTH);
    }
    DatePicker01.init(mYear, mMonth, mDay, null);

    /* 每日提醒时间设定于TimePicker */
    if (remind_value != null && remind_value.length() == 4)
    {
      TimePicker01.setCurrentHour(Integer.parseInt(remind_value
          .substring(0, 2)));
      TimePicker01.setCurrentMinute(Integer.parseInt(remind_value
          .substring(2, 4)));
    }
    /* 将新的设定值写回档案 */
    Button02.setOnClickListener(new Button.OnClickListener()
    {

      @Override
      public void onClick(View arg0)
      {

        FileOutputStream fos;
        try
        {
          /* 取得DatePicker的日期 */
          int m = DatePicker01.getMonth() + 1;
          String strM = m >= 10 ? "" + m : "0" + m;
          int d = DatePicker01.getDayOfMonth();
          String strD = d >= 10 ? "" + d : "0" + d;
          mcdate_value = "" + DatePicker01.getYear() + "" + strM
              + "" + strD;

          /* 取得EditText的值 */
          period_value = EditText01.getText().toString();

          /* 取得TimePicker的时间 */
          int h = TimePicker01.getCurrentHour();
          String strH = h >= 10 ? "" + h : "0" + h;
          int mu = TimePicker01.getCurrentMinute();
          String strMu = mu >= 10 ? "" + mu : "0" + mu;
          remind_value = strH + strMu;

          fos = openFileOutput(EX10_03.fileName,
              MODE_WORLD_WRITEABLE);
          BufferedOutputStream bos = new BufferedOutputStream(fos);

          String txt = EX10_03.mcdate_key + "=" + mcdate_value;
          bos.write(txt.getBytes());

          bos.write(new String("\n").getBytes());
          txt = EX10_03.period_key + "=" + period_value;
          bos.write(txt.getBytes());

          bos.write(new String("\n").getBytes());
          txt = EX10_03.remind_key + "=" + remind_value;
          bos.write(txt.getBytes());

          bos.close();
          fos.close();
        } catch (FileNotFoundException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (IOException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }

        Intent receiverIntent = new Intent(EX10_03_1.this,
            AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(
            EX10_03_1.this, 1, receiverIntent, 0);
        /* 取得AlarmManager服务 */
        AlarmManager am;
        am = (AlarmManager) getSystemService(ALARM_SERVICE);
        /* 先将AlarmManager停止 */
        am.cancel(sender);

        /* 1天的毫秒 */
        int times = 24 * 60 * 60 * 1000;

        /* 取得TimePicker的值当作驱动的时间 */
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, TimePicker01
            .getCurrentHour());
        calendar.set(Calendar.MINUTE, TimePicker01
            .getCurrentMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long triggerTime = calendar.getTimeInMillis();

        /* 每日同一时间执行一次 */
        am.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime,
            times, sender);

        /* 回到主画面 */
        Intent intent = new Intent();
        intent.setClass(EX10_03_1.this, EX10_03.class);
        startActivity(intent);
        finish();
      }
    });
  }

}

