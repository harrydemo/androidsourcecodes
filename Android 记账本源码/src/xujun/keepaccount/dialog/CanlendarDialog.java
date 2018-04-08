/*
 * 系统名称：
 * 模块名称：
 * 描述：
 * 作者：徐骏
 * version 1.0
 * time  2010-11-5 下午01:20:23
 * copyright Anymusic Ltd.
 */
package xujun.keepaccount.dialog;

import xujun.keepaccount.R;
import xujun.keepaccount.calendar.CalendarWidget;
import xujun.keepaccount.calendar.OnCalendarSelectedListenter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

/**
 * @author 徐骏
 * @data   2010-11-5
 */
public class CanlendarDialog extends Activity implements OnCalendarSelectedListenter
{
	private CalendarWidget calendarWidget;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		setContentView(R.layout.calendar_dialog);
		getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.calendar);
		
		calendarWidget = (CalendarWidget)findViewById(R.id.calenaract_calendar_widget);
		calendarWidget.setCalendarSelectedListenter(this);
	}

	@Override
	public void onSelected(int year, int month, int day)
	{
		Intent intent = new Intent();
		intent.putExtra("year", year);
		intent.putExtra("month", month);
		intent.putExtra("day", day);
		setResult(RESULT_OK,intent);
		finish();
	}
}
