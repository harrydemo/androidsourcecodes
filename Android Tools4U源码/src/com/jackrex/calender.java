package com.jackrex;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.LinearLayout.LayoutParams;



public class calender extends Activity implements OnTouchListener {

	//�ж�������
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;

	//����
	private Animation slideLeftIn;
	private Animation slideLeftOut;
	private Animation slideRightIn;
	private Animation slideRightOut;
	private ViewFlipper viewFlipper;
	GestureDetector mGesture = null;

	
	public boolean onTouch(View v, MotionEvent event) {
		return mGesture.onTouchEvent(event);
	}

	AnimationListener animationListener=new AnimationListener() {
	
		public void onAnimationStart(Animation animation) {
		}
		
		
		public void onAnimationRepeat(Animation animation) {
		}
		
		
		public void onAnimationEnd(Animation animation) {
			//��������ɺ����
			CreateGirdView();
		}
	};

	class GestureListener extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
			try {
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
					return false;
				// right to left swipe
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE	&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					viewFlipper.setInAnimation(slideLeftIn);
					viewFlipper.setOutAnimation(slideLeftOut);
					viewFlipper.showNext();
					setNextViewItem();
					//CreateGirdView();
					return true;

				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					viewFlipper.setInAnimation(slideRightIn);
					viewFlipper.setOutAnimation(slideRightOut);
					viewFlipper.showPrevious();
					setPrevViewItem();
					//CreateGirdView();
					return true;

				}
			} catch (Exception e) {
				// nothing
			}
			return false;
		}
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// ListView lv = getListView();
			//�õ���ǰѡ�е��ǵڼ�����Ԫ��
			int pos = gView2.pointToPosition((int) e.getX(), (int) e.getY());
			LinearLayout txtDay = (LinearLayout) gView2.findViewById(pos + 5000);
			if (txtDay != null) {
				if (txtDay.getTag() != null) {
					Date date = (Date) txtDay.getTag();
					calSelected.setTime(date);

					gAdapter.setSelectedDate(calSelected);
					gAdapter.notifyDataSetChanged();

					gAdapter1.setSelectedDate(calSelected);
					gAdapter1.notifyDataSetChanged();

					gAdapter3.setSelectedDate(calSelected);
					gAdapter3.notifyDataSetChanged();
				}
			}

			Log.i("TEST", "onSingleTapUp -  pos=" + pos);

			return false;
		}
	}

	// / }}}

	// ��������
	private Context mContext = calender.this;
	private GridView title_gView;
	private GridView gView1;// ��һ����
	private GridView gView2;// ��ǰ��
	private GridView gView3;// ��һ����
	// private GridView gView1;
	boolean bIsSelection = false;// �Ƿ���ѡ���¼�����
	private Calendar calStartDate = Calendar.getInstance();// ��ǰ��ʾ������
	private Calendar calSelected = Calendar.getInstance(); // ѡ�������
	private Calendar calToday = Calendar.getInstance(); // ����
	private CalendarGridViewAdapter gAdapter;
	private CalendarGridViewAdapter gAdapter1;
	private CalendarGridViewAdapter gAdapter3;
	// ������ť
	private Button btnToday = null;
	private RelativeLayout mainLayout;

	//
	private int iMonthViewCurrentMonth = 0; // ��ǰ��ͼ��
	private int iMonthViewCurrentYear = 0; // ��ǰ��ͼ��
	private int iFirstDayOfWeek = Calendar.MONDAY;

	private static final int mainLayoutID = 88; // ����������ID
	private static final int titleLayoutID = 77; // title����ID
	private static final int caltitleLayoutID = 66; // title����ID
	private static final int calLayoutID = 55; // ��������ID
	/** �ײ��˵����� **/
	String[] menu_toolbar_name_array;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(generateContentView());
		UpdateStartDateForMonth();
		
		
		slideLeftIn = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
		slideLeftOut = AnimationUtils.loadAnimation(this, R.anim.slide_left_out);
		slideRightIn = AnimationUtils.loadAnimation(this, R.anim.slide_right_in);
		slideRightOut = AnimationUtils.loadAnimation(this,R.anim.slide_right_out);
		
		slideLeftIn.setAnimationListener(animationListener);
		slideLeftOut.setAnimationListener(animationListener);
		slideRightIn.setAnimationListener(animationListener);
		slideRightOut.setAnimationListener(animationListener);
		
		mGesture = new GestureDetector(this, new GestureListener());
	}

	AlertDialog.OnKeyListener onKeyListener = new AlertDialog.OnKeyListener() {


		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				calender.this.finish();
			}
			return false;

		}

	};

	
	// ����������ͼ
	private View generateContentView() {
		// ����һ����ֱ�����Բ��֣��������ݣ�
		viewFlipper = new ViewFlipper(this);
		viewFlipper.setId(calLayoutID);
		
		mainLayout = new RelativeLayout(this); // ����һ����ֱ�����Բ��֣��������ݣ�
		RelativeLayout.LayoutParams params_main = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);
		mainLayout.setLayoutParams(params_main);
		mainLayout.setId(mainLayoutID);
		mainLayout.setGravity(Gravity.CENTER_HORIZONTAL);

		LinearLayout layTopControls = createLayout(LinearLayout.HORIZONTAL); // ���ɶ�����ť����

		generateTopButtons(layTopControls); // ���ɶ�����ť ����һ�£���һ�£���ǰ�£�
		RelativeLayout.LayoutParams params_title = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params_title.topMargin = 5;
		// params_title.addRule(RelativeLayout.ALIGN_PARENT_TOP, 20);
		layTopControls.setId(titleLayoutID);
		mainLayout.addView(layTopControls, params_title);

		calStartDate = getCalendarStartDate();

		setTitleGirdView();
		RelativeLayout.LayoutParams params_cal_title = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params_cal_title.addRule(RelativeLayout.BELOW, titleLayoutID);
		// params_cal_title.topMargin = 5;
		mainLayout.addView(title_gView, params_cal_title);

		CreateGirdView();

		RelativeLayout.LayoutParams params_cal = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params_cal.addRule(RelativeLayout.BELOW, caltitleLayoutID);

		mainLayout.addView(viewFlipper, params_cal);
		
		LinearLayout br = new LinearLayout(this);
		RelativeLayout.LayoutParams params_br = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT, 1);
		params_br.addRule(RelativeLayout.BELOW, calLayoutID);
		br.setBackgroundColor(getResources().getColor(R.color.calendar_background));
		mainLayout.addView(br, params_br);

		return mainLayout;

	}

	// ����һ�����Բ���
	// ����������
	private LinearLayout createLayout(int iOrientation) {
		LinearLayout lay = new LinearLayout(this);
		LayoutParams params = new LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,// *fill_parent���������ؼ��Ŀհ�
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.topMargin = 10;
		// ���ò��ֲ���
		lay.setLayoutParams(params);// *wrap_content����ʾ��С�պ��㹻��ʾ��ǰ�ؼ��������
		lay.setOrientation(iOrientation);// ���÷���
		lay.setGravity(Gravity.LEFT);
		return lay;
	}

	// ���ɶ�����ť
	// ����������
	private void generateTopButtons(LinearLayout layTopControls) {
		// ����һ����ǰ�°�ť���м�İ�ť��
		btnToday = new Button(this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.leftMargin = 20;
		btnToday.setLayoutParams(lp);
		btnToday.setTextSize(25);
		btnToday.setBackgroundResource(Color.TRANSPARENT);//
		// btn_cal.setBackgroundResource(R.drawable.editbox_background_normal);//
		// ���õ�ǰ�°�ť�ı�����ɫΪ��ťĬ����ɫ

		// ��ǰ�µĵ���¼��ļ���
		btnToday.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				setToDayViewItem();
			}
		});

		layTopControls.setGravity(Gravity.CENTER_HORIZONTAL);
		layTopControls.addView(btnToday);

	}

	private void setTitleGirdView() {

		title_gView = setGirdView();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		// params.topMargin = 5;
		title_gView.setLayoutParams(params);
		title_gView.setVerticalSpacing(0);// ��ֱ���
		title_gView.setHorizontalSpacing(0);// ˮƽ���
		TitleGridAdapter titleAdapter = new TitleGridAdapter(this);
		title_gView.setAdapter(titleAdapter);// ���ò˵�Adapter
		title_gView.setId(caltitleLayoutID);
	}

	private void CreateGirdView() {

		Calendar tempSelected1 = Calendar.getInstance(); // ��ʱ
		Calendar tempSelected2 = Calendar.getInstance(); // ��ʱ
		Calendar tempSelected3 = Calendar.getInstance(); // ��ʱ
		tempSelected1.setTime(calStartDate.getTime());
		tempSelected2.setTime(calStartDate.getTime());
		tempSelected3.setTime(calStartDate.getTime());

		gView1 = new CalendarGridView(mContext);
		tempSelected1.add(Calendar.MONTH, -1);
		gAdapter1 = new CalendarGridViewAdapter(this, tempSelected1);
		gView1.setAdapter(gAdapter1);// ���ò˵�Adapter
		gView1.setId(calLayoutID);

		gView2 = new CalendarGridView(mContext);
		gAdapter = new CalendarGridViewAdapter(this, tempSelected2);
		gView2.setAdapter(gAdapter);// ���ò˵�Adapter
		gView2.setId(calLayoutID);

		gView3 = new CalendarGridView(mContext);
		tempSelected3.add(Calendar.MONTH, 1);
		gAdapter3 = new CalendarGridViewAdapter(this, tempSelected3);
		gView3.setAdapter(gAdapter3);// ���ò˵�Adapter
		gView3.setId(calLayoutID);

		gView2.setOnTouchListener(this);
		gView1.setOnTouchListener(this);
		gView3.setOnTouchListener(this);

		if (viewFlipper.getChildCount() != 0) {
			viewFlipper.removeAllViews();
		}

		viewFlipper.addView(gView2);
		viewFlipper.addView(gView3);
		viewFlipper.addView(gView1);

		String s = calStartDate.get(Calendar.YEAR)
				+ "-"
				+ NumberHelper.LeftPad_Tow_Zero(calStartDate
						.get(Calendar.MONTH) + 1);

		btnToday.setText(s);
	}

	private GridView setGirdView() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		GridView gridView = new GridView(this);
		gridView.setLayoutParams(params);
		gridView.setNumColumns(7);// ����ÿ������
		gridView.setGravity(Gravity.CENTER_VERTICAL);// λ�þ���
		gridView.setVerticalSpacing(1);// ��ֱ���
		gridView.setHorizontalSpacing(1);// ˮƽ���
		gridView.setBackgroundColor(getResources().getColor(
				R.color.calendar_background));

		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int i = display.getWidth() / 7;
		int j = display.getWidth() - (i * 7);
		int x = j / 2;
		gridView.setPadding(x, 0, 0, 0);// ����

		return gridView;
	}

	// ��һ����
	private void setPrevViewItem() {
		iMonthViewCurrentMonth--;// ��ǰѡ����--
		// �����ǰ��Ϊ�����Ļ���ʾ��һ��
		if (iMonthViewCurrentMonth == -1) {
			iMonthViewCurrentMonth = 11;
			iMonthViewCurrentYear--;
		}
		calStartDate.set(Calendar.DAY_OF_MONTH, 1); // ������Ϊ����1��
		calStartDate.set(Calendar.MONTH, iMonthViewCurrentMonth); // ������
		calStartDate.set(Calendar.YEAR, iMonthViewCurrentYear); // ������

	}

	// ����
	private void setToDayViewItem() {

		calSelected.setTimeInMillis(calToday.getTimeInMillis());
		calSelected.setFirstDayOfWeek(iFirstDayOfWeek);
		calStartDate.setTimeInMillis(calToday.getTimeInMillis());
		calStartDate.setFirstDayOfWeek(iFirstDayOfWeek);

	}

	// ��һ����
	private void setNextViewItem() {
		iMonthViewCurrentMonth++;
		if (iMonthViewCurrentMonth == 12) {
			iMonthViewCurrentMonth = 0;
			iMonthViewCurrentYear++;
		}
		calStartDate.set(Calendar.DAY_OF_MONTH, 1);
		calStartDate.set(Calendar.MONTH, iMonthViewCurrentMonth);
		calStartDate.set(Calendar.YEAR, iMonthViewCurrentYear);

	}

	// ���ݸı�����ڸ�������
	// ��������ؼ���
	private void UpdateStartDateForMonth() {
		calStartDate.set(Calendar.DATE, 1); // ���óɵ��µ�һ��
		iMonthViewCurrentMonth = calStartDate.get(Calendar.MONTH);// �õ���ǰ������ʾ����
		iMonthViewCurrentYear = calStartDate.get(Calendar.YEAR);// �õ���ǰ������ʾ����

		String s = calStartDate.get(Calendar.YEAR)
				+ "-"
				+ NumberHelper.LeftPad_Tow_Zero(calStartDate
						.get(Calendar.MONTH) + 1);
		btnToday.setText(s);

		// ����һ��2 ��������1 ���ʣ������
		int iDay = 0;
		int iFirstDayOfWeek = Calendar.MONDAY;
		int iStartDay = iFirstDayOfWeek;
		if (iStartDay == Calendar.MONDAY) {
			iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
			if (iDay < 0)
				iDay = 6;
		}
		if (iStartDay == Calendar.SUNDAY) {
			iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
			if (iDay < 0)
				iDay = 6;
		}
		calStartDate.add(Calendar.DAY_OF_WEEK, -iDay);

	}

	private Calendar getCalendarStartDate() {
		calToday.setTimeInMillis(System.currentTimeMillis());
		calToday.setFirstDayOfWeek(iFirstDayOfWeek);

		if (calSelected.getTimeInMillis() == 0) {
			calStartDate.setTimeInMillis(System.currentTimeMillis());
			calStartDate.setFirstDayOfWeek(iFirstDayOfWeek);
		} else {
			calStartDate.setTimeInMillis(calSelected.getTimeInMillis());
			calStartDate.setFirstDayOfWeek(iFirstDayOfWeek);
		}

		return calStartDate;
	}

	public class TitleGridAdapter extends BaseAdapter {

		int[] titles = new int[] { R.string.Sun, R.string.Mon, R.string.Tue,
				R.string.Wed, R.string.Thu, R.string.Fri, R.string.Sat };

		private Activity activity;

		// construct
		public TitleGridAdapter(Activity a) {
			activity = a;
		}

	
		public int getCount() {
			return titles.length;
		}

		
		public Object getItem(int position) {
			return titles[position];
		}

		
		public long getItemId(int position) {
			return position;
		}


		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout iv = new LinearLayout(activity);
			TextView txtDay = new TextView(activity);
			txtDay.setFocusable(false);
			txtDay.setBackgroundColor(Color.TRANSPARENT);
			iv.setOrientation(1);

			txtDay.setGravity(Gravity.CENTER);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);

			int i = (Integer) getItem(position);

			txtDay.setTextColor(Color.WHITE);
			Resources res = getResources();

			if (i == R.string.Sat) {
				// ����
				txtDay.setBackgroundColor(res.getColor(R.color.title_text_6));
			} else if (i == R.string.Sun) {
				// ����
				txtDay.setBackgroundColor(res.getColor(R.color.title_text_7));
			} else {

			}

			txtDay.setText((Integer) getItem(position));

			iv.addView(txtDay, lp);

			return iv;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
			overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
		
		}
		return false;
	}
}
