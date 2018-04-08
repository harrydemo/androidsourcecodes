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

	// private final String duration = "1"; //ͨ��ʱ��һСʱ
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
		calendar.setFirstDayOfWeek(GregorianCalendar.MONDAY);// ����һ�����ڵĵ�һ��Ϊ����1��Ĭ����������
		calendar.set(GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.MONDAY);
		SimpleDateFormat dateutil = new SimpleDateFormat("yyyy-MM-dd");
		String dayOfWeek = dateutil.format(calendar.getTime()) + " 00:00:00";// ��ȡ����һ��ʱ��
		int actualMaxOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);// ��ȡ�����ж�����
		int currentDay = calendar.get(calendar.DAY_OF_MONTH);// ȡ��ǰ����;
		// ��ȡ�����õ����ײͰ���ͨ��ʱ��
		sp = new SharedPreferencesHelper(this, "callTimeRecorder");
		timesPerMonth = sp.getValue("month_total");
		// ��ȡ�����Ѳ�ͨ��ʱ��
		String where = "type=2 and datetime(substr(date,1,10),'unixepoch','localtime')>datetime('now','start of month')";
		outOfmonth = this.getCallTimeByWhere(where);
		textView = (TextView) this.findViewById(R.id.outofmonth);
		textView.setText(String.valueOf(outOfmonth));
		// ��ȡ�����Ѳ�ͨ��ʱ��
		where = "type=2 and datetime(substr(date,1,10),'unixepoch','localtime')>datetime('"
				+ dayOfWeek + "')";
		// Log.v("SQL", where);
		outOfWeek = this.getCallTimeByWhere(where);
		textView = (TextView) this.findViewById(R.id.outofweek);
		textView.setText(String.valueOf(outOfWeek));
		// ��ȡ�����Ѳ�ͨ��ʱ��
		where = "type=2 and datetime(substr(date,1,10),'unixepoch','localtime')>date('now','localtime')||' 00:00:00'";
		outOfToday = this.getCallTimeByWhere(where);
		textView = (TextView) this.findViewById(R.id.outoftoday);
		textView.setText(String.valueOf(outOfToday));
		// ��ȡ�����ѽ�ͨ��ʱ��
		where = "type=1 and datetime(substr(date,1,10),'unixepoch','localtime')>date('now','localtime')||' 00:00:00'";
		inOfToday = this.getCallTimeByWhere(where);
		textView = (TextView) this.findViewById(R.id.inoftoday);
		textView.setText(String.valueOf(inOfToday));
		// ��ȡ�����ѽ�ͨ��ʱ��
		where = "type=1 and datetime(substr(date,1,10),'unixepoch','localtime')>datetime('"
				+ dayOfWeek + "')";
		inOfWeek = this.getCallTimeByWhere(where);
		textView = (TextView) this.findViewById(R.id.inofweek);
		textView.setText(String.valueOf(inOfWeek));
		// ��ȡ�����ѽ�ͨ��ʱ��
		where = "type=1 and datetime(substr(date,1,10),'unixepoch','localtime')>datetime('now','start of month')";
		inOfMonth = this.getCallTimeByWhere(where);
		textView = (TextView) this.findViewById(R.id.inofmonth);
		textView.setText(String.valueOf(inOfMonth));
		// ��ȡ�Ѳ���ռ���ײ͵İٷֱ�
		double persent = (double) outOfmonth * 100 / (double) timesPerMonth;
		java.text.DecimalFormat df = new java.text.DecimalFormat("########.000");
		persent = (new Double(df.format(persent))).doubleValue();
		// �������ײͰ�������ͨ��ʱ��
		textView = (TextView) this.findViewById(R.id.txt_total);
		textView.setText(String.valueOf(timesPerMonth));
		// ���ñ����Ѳ���ͨ��ʱ��
		textView = (TextView) this.findViewById(R.id.txt_reality);
		textView.setText(String.valueOf(outOfmonth));
		// ���ò���ռ���ײ�ͨ��ʱ���İٷֱ�
		textView = (TextView) this.findViewById(R.id.txt_proportions);
		textView.setText(String.valueOf(persent) + "%");
		// ���ð�Ŀǰ�����Ԥ���������绰ʱ��
		int estimated = outOfmonth * actualMaxOfMonth / currentDay;
		textView = (TextView) this.findViewById(R.id.txt_estimated);
		textView.setText(String.valueOf(estimated));
		//����Ԥ��ʱ��textView����ɫ
//		if(estimated>timesPerMonth)
//			textView.setBackgroundColor(R.color.orange);
		// ���ò����ͽ����ܼ�ʱ��
		textView = (TextView) this.findViewById(R.id.totaloftoday);
		textView.setText(String.valueOf(outOfToday + inOfToday));
		textView = (TextView) this.findViewById(R.id.totalofweek);
		textView.setText(String.valueOf(outOfWeek + inOfWeek));
		textView = (TextView) this.findViewById(R.id.totalofmonth);
		textView.setText(String.valueOf(outOfmonth + inOfMonth));
		// ���ý�����
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
