package com.milifan.contact;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.milifan.R;
import com.milifan.contact.base.BaseContactList;
import com.milifan.util.Common;
import com.milifan.util.SharedPreferencesHelper;

public class TotalDisplay extends Activity {
	boolean isrun = false;
	SharedPreferencesHelper sp;

	// private final String duration = "1"; //通话时长一小时
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// String sql =
		// "_id in(select total._id from (select _id,sum(duration) as duration from calls where date>="+beforeTime+" and date<="+newTime+" group by number) as total where total.duration>="+duration+")";
		super.onCreate(savedInstanceState);
		setContentView(R.layout.total_display);

		// setProperties();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setProperties();
	}

	private void setProperties() {
		int outOfmonth, timesPerMonth, outOfToday, outOfWeek, inOfToday, inOfWeek, inOfMonth, missOfToday, missOfWeek, missOfMonth;
		TextView textView;

		GregorianCalendar calendar = new GregorianCalendar();
		Date now = new Date();
		calendar.setTime(now);
		calendar.setFirstDayOfWeek(GregorianCalendar.MONDAY);// 设置一个星期的第一天为星期1，默认是星期日
		calendar.set(GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.MONDAY);
		SimpleDateFormat dateutil = new SimpleDateFormat("yyyy-MM-dd");
		String dayOfWeek = dateutil.format(calendar.getTime()) + " 00:00:00";// 获取本周一得时间
		int actualMaxOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);// 获取当月有多少天
		int currentDay = calendar.get(calendar.DAY_OF_MONTH);// 取当前日期;
		// 获取所设置的月套餐包含通话时长
		sp = new SharedPreferencesHelper(this, "callTimeRecorder");
		timesPerMonth = sp.getValue("month_total");
		// 获取本月已拨通话时长
		String where = "type=2 and datetime(substr(date,1,10),'unixepoch','localtime')>datetime('now','start of month')";
		outOfmonth = this.getCallTimeByWhere(where);
		textView = (TextView) this.findViewById(R.id.outofmonth);
		textView.setText(String.valueOf(outOfmonth));
		// 获取本周已拨通话时长
		where = "type=2 and datetime(substr(date,1,10),'unixepoch','localtime')>datetime('"
				+ dayOfWeek + "')";
		// Log.v("SQL", where);
		outOfWeek = this.getCallTimeByWhere(where);
		textView = (TextView) this.findViewById(R.id.outofweek);
		textView.setText(String.valueOf(outOfWeek));
		// 获取当日已拨通话时长
		where = "type=2 and datetime(substr(date,1,10),'unixepoch','localtime')>date('now','localtime')||' 00:00:00'";
		outOfToday = this.getCallTimeByWhere(where);
		textView = (TextView) this.findViewById(R.id.outoftoday);
		textView.setText(String.valueOf(outOfToday));
		// 获取当日已接通话时长
		where = "type=1 and datetime(substr(date,1,10),'unixepoch','localtime')>date('now','localtime')||' 00:00:00'";
		inOfToday = this.getCallTimeByWhere(where);
		textView = (TextView) this.findViewById(R.id.inoftoday);
		textView.setText(String.valueOf(inOfToday));
		// 获取本周已接通话时长
		where = "type=1 and datetime(substr(date,1,10),'unixepoch','localtime')>datetime('"
				+ dayOfWeek + "')";
		inOfWeek = this.getCallTimeByWhere(where);
		textView = (TextView) this.findViewById(R.id.inofweek);
		textView.setText(String.valueOf(inOfWeek));
		// 获取本月已接通话时长
		where = "type=1 and datetime(substr(date,1,10),'unixepoch','localtime')>datetime('now','start of month')";
		inOfMonth = this.getCallTimeByWhere(where);
		textView = (TextView) this.findViewById(R.id.inofmonth);
		textView.setText(String.valueOf(inOfMonth));
		// 获取已拨所占月套餐的百分比
		double persent = (double) outOfmonth * 100 / (double) timesPerMonth;
		java.text.DecimalFormat df = new java.text.DecimalFormat("########.000");
		persent = (new Double(df.format(persent))).doubleValue();
		// 设置月套餐包含拨出通话时长
		textView = (TextView) this.findViewById(R.id.txt_total);
		textView.setText(String.valueOf(timesPerMonth));
		// 设置本月已拨出通话时长
		textView = (TextView) this.findViewById(R.id.txt_reality);
		textView.setText(String.valueOf(outOfmonth));
		// 设置拨出占月套餐通话时长的百分比
		textView = (TextView) this.findViewById(R.id.txt_proportions);
		textView.setText(String.valueOf(persent) + "%");
		// 设置按目前情况下预计所拨出电话时长
		int estimated = outOfmonth * actualMaxOfMonth / currentDay;
		textView = (TextView) this.findViewById(R.id.txt_estimated);
		textView.setText(String.valueOf(estimated));
		//设置预计时长textView的颜色
//		if(estimated>timesPerMonth)
//			textView.setBackgroundColor(R.color.orange);
		// 设置拨出和接入总计时长
		textView = (TextView) this.findViewById(R.id.totaloftoday);
		textView.setText(String.valueOf(outOfToday + inOfToday));
		textView = (TextView) this.findViewById(R.id.totalofweek);
		textView.setText(String.valueOf(outOfWeek + inOfWeek));
		textView = (TextView) this.findViewById(R.id.totalofmonth);
		textView.setText(String.valueOf(outOfmonth + inOfMonth));
		// 设置进度条
		ProgressBar progressBar = (ProgressBar) this
				.findViewById(R.id.pbar_process);

		progressBar.setMax(timesPerMonth);
		progressBar.setProgress(outOfmonth);
		//progressBar.setTag(persent);

	}

	private int getCallTimeByWhere(String where) {
		int total = 0;
		final Cursor cursor = getContentResolver().query(
				CallLog.Calls.CONTENT_URI,
				new String[] { CallLog.Calls.NUMBER, CallLog.Calls.DURATION },
				where, null, CallLog.Calls.DEFAULT_SORT_ORDER);
		// startManagingCursor(cursor)

		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			int duration = 0;
			try {
				duration = Integer.parseInt(cursor.getString(cursor
						.getColumnIndex(CallLog.Calls.DURATION)));
				if (duration % 60 > 0)
					duration = duration / 60 + 1;
				else
					duration = duration / 60;
			} catch (Exception e) {
				duration = 1;
			}
			total += duration;
		}
		return total;
	}
}
