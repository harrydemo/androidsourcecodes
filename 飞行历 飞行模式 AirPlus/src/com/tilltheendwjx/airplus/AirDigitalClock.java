/**
 * 
 */
package com.tilltheendwjx.airplus;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import com.tilltheendwjx.airplus.R;
import com.tilltheendwjx.airplus.utils.Log;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * @author wjx
 * 
 */
public class AirDigitalClock extends RelativeLayout {
	private final static String M12 = "h:mm";
	private AirClockTextView mStartTimeDisplay;
	private AirClockTextView mEndTimeDisplay;
	private String mFormat;
	private Calendar mStartCalendar;
	private Calendar mEndCalendar;
	private boolean mLive = true;
	private AmPm mStartAmPm;
	private AmPm mEndAmPm;
	private ContentObserver mFormatChangeObserver;
	private boolean mAttached;

	/* called by system on minute ticks */
	private final Handler mHandler = new Handler();
	private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (mLive
					&& intent.getAction()
							.equals(Intent.ACTION_TIMEZONE_CHANGED)) {
				mStartCalendar = Calendar.getInstance();
				mEndCalendar = Calendar.getInstance();
			}
			// Post a runnable to avoid blocking the broadcast.
			mHandler.post(new Runnable() {
				public void run() {
					updateTime();
				}
			});
		}
	};

	public AirDigitalClock(Context context) {
		this(context, null);
	}

	public AirDigitalClock(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private class FormatChangeObserver extends ContentObserver {
		public FormatChangeObserver() {
			super(new Handler());
		}

		@Override
		public void onChange(boolean selfChange) {
			setDateFormat();
			updateTime();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onFinishInflate()
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mStartTimeDisplay = (AirClockTextView) findViewById(R.id.startTimeDisplay);
		mEndTimeDisplay = (AirClockTextView) findViewById(R.id.endTimeDisplay);

		mStartAmPm = new AmPm(this, R.id.start_am_pm);
		mEndAmPm = new AmPm(this, R.id.end_am_pm);
		mStartCalendar = Calendar.getInstance();
		mEndCalendar = Calendar.getInstance();
		setDateFormat();

	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

		if (Log.LOGV)
			Log.v("onAttachedToWindow " + this);

		if (mAttached)
			return;
		mAttached = true;

		if (mLive) {
			/* monitor time ticks, time changed, timezone */
			IntentFilter filter = new IntentFilter();
			filter.addAction(Intent.ACTION_TIME_TICK);
			filter.addAction(Intent.ACTION_TIME_CHANGED);
			filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
			getContext().registerReceiver(mIntentReceiver, filter);
		}

		/* monitor 12/24-hour display preference */
		mFormatChangeObserver = new FormatChangeObserver();
		getContext().getContentResolver().registerContentObserver(
				Settings.System.CONTENT_URI, true, mFormatChangeObserver);

		updateTime();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();

		if (!mAttached)
			return;
		mAttached = false;

		if (mLive) {
			getContext().unregisterReceiver(mIntentReceiver);
		}
		getContext().getContentResolver().unregisterContentObserver(
				mFormatChangeObserver);
	}

	void updateTime(Calendar startc, Calendar endc) {
		mStartCalendar = startc;
		mEndCalendar = endc;
		updateTime();
	}

	static class AmPm {
		private AirClockTextView mAmPm;
		private String mAmString, mPmString;

		AmPm(View parent, int n) {
			mAmPm = (AirClockTextView) parent.findViewById(n);

			String[] ampm = new DateFormatSymbols().getAmPmStrings();
			mAmString = ampm[0];
			mPmString = ampm[1];
		}

		void setShowAmPm(boolean show) {
			mAmPm.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
		}

		void setIsMorning(boolean isMorning) {
			mAmPm.setText(isMorning ? mAmString : mPmString);
		}
	}

	private void updateTime() {
		if (mLive) {
			mStartCalendar.setTimeInMillis(System.currentTimeMillis());
			mEndCalendar.setTimeInMillis(System.currentTimeMillis());
		}

		CharSequence newStartTime = DateFormat.format(mFormat, mStartCalendar);
		CharSequence newEndTime = DateFormat.format(mFormat, mEndCalendar);
		mStartTimeDisplay.setText(newStartTime);
		mEndTimeDisplay.setText(newEndTime);
		mStartAmPm.setIsMorning(mStartCalendar.get(Calendar.AM_PM) == 0);
		mEndAmPm.setIsMorning(mEndCalendar.get(Calendar.AM_PM) == 0);
	}

	private void setDateFormat() {
		mFormat = Airs.get24HourMode(getContext()) ? Airs.M24 : M12;
		mStartAmPm.setShowAmPm(mFormat == M12);
		mEndAmPm.setShowAmPm(mFormat == M12);
	}

	void setLive(boolean live) {
		mLive = live;
	}

}
