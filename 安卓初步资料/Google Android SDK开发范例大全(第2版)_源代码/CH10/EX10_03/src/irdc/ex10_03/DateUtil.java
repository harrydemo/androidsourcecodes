package irdc.ex10_03;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class DateUtil
{
  /* ����������20090101�����ڡ���ʽ�ַ�����yyyyMMdd�ش��´����� */
  public static String getNextDate(String date, int period,
      String format)
  {
    int mYear = Integer.parseInt(date.substring(0, 4));
    int mMonth = Integer.parseInt(date.substring(4, 6));
    int mDay = Integer.parseInt(date.substring(6, 8));
    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
        format);
    String nextDate = "";
    Calendar calendar = Calendar.getInstance();
    calendar.set(mYear, mMonth - 1, mDay);
    calendar.add(Calendar.DAY_OF_YEAR, period);
    nextDate = sdf.format(calendar.getTime());
    return nextDate;
  }

  /* ��������������������� */
  public static int computerDiffDate(long date1, long date2)
  {
    Calendar calendar1 = Calendar.getInstance();
    calendar1.setTimeInMillis(date1);

    Calendar calendar2 = Calendar.getInstance();
    calendar2.setTimeInMillis(date2);
    /* ���ж��Ƿ�ͬ�� */
    int y1 = calendar1.get(Calendar.YEAR);
    int y2 = calendar2.get(Calendar.YEAR);

    int d1 = calendar1.get(Calendar.DAY_OF_YEAR);
    int d2 = calendar2.get(Calendar.DAY_OF_YEAR);
    int maxDays = 0;
    int day = 0;
    if (y1 - y2 > 0)
    {
      maxDays = calendar2.getActualMaximum(Calendar.DAY_OF_YEAR);
      day = d1 - d2 + maxDays;
    } else if (y1 - y2 < 0)
    {
      maxDays = calendar1.getActualMaximum(Calendar.DAY_OF_YEAR);
      day = d1 - d2 - maxDays;
    } else
    {
      day = d1 - d2;
    }
    return day;
  }

  /* ����������20090101�ش����ڵ�longֵ */
  public static long getDateTime(String strDate)
  {
    return getDateByFormat(strDate, "yyyyMMdd").getTime();
  }

  /* �������ڵ�longֵ����ʽ�ַ�����yyyyMMdd */
  public static String getDateTime(String format, long millis)
  {
    java.util.Calendar calendar = java.util.Calendar.getInstance();
    calendar.setTimeInMillis(millis);
    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
        format);
    return (sdf.format(calendar.getTime()));
  }

  public static Date getDateByFormat(String strDate, String format)
  {
    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
        format);
    try
    {
      return (sdf.parse(strDate));
    } catch (ParseException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return null;
    }
  }
}

