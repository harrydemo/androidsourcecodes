/*
 * 系统名称：
 * 模块名称：
 * 描述：
 * 作者：徐骏
 * version 1.0
 * time  2010-11-5 下午01:07:14
 * copyright Anymusic Ltd.
 */
package xujun.keepaccount.calendar;

import java.util.Calendar;

import xujun.keepaccount.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.Toast;

/**
 * @author 徐骏
 * @data   2010-11-5
 */
public class CalendarWidget extends View implements OnTouchListener
{
	private Context context;
	private Paint paint = new Paint();
	private RectF borderRect;
	private Rect canlendarRect;
	private Rect dayCellContextRect;
	private CalendarHeader header;
	private DayCell[] dayCells = new DayCell[42];
	private Calendar calendar = Calendar.getInstance();
	private Calendar todayCalendar = Calendar.getInstance();
	
	private DayCell curDayCell;
	private int moveX =0;//X方向上在onScroll中的移动像素
	private GestureDetector detector;//手势运动委托器

	private CalendarAction gestureAction ;
	
	private OnCalendarSelectedListenter calendarSelectedListenter;
	
	public CalendarWidget(Context context)
	{
		super(context,null);
	}
	public CalendarWidget(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}
	private void init(Context context)
	{
		this.context = context;
		gestureAction = CalendarAction.CALENDAR_DEFAULT;
		
		setOnTouchListener(this);
		detector = new GestureDetector(context,new MyGestureListener());
		setLongClickable(true);//这样才能启动Gesture [dʒestʃə]
		
		header = new CalendarHeader(this.context);
		for(int i=0;i<42;i++)
		{
			dayCells[i] = new DayCell();	
		}
		setCalendar();
	}
	private void setCalendar()
	{
		//先清除状态
		for(int i=0;i<42;i++)
		{
			dayCells[i].clearState();
		}
		//根据日期设置DayCell
		//设置日历是本月1号
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
		
		//获得本月有多少天
		int maxday = calendar.getActualMaximum(Calendar.DATE);
		//第一天是星期几
		int dayofweek = calendar.get(Calendar.DAY_OF_WEEK)-1;
		
		for(int i=dayofweek;i<maxday+dayofweek;i++)
		{
			int day = calendar.get(Calendar.DATE);
			dayCells[i].setText(""+day);
			//今天 （年月日三者相等 小时毫秒这些不比较）
			if(todayCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
				&&todayCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
				&&todayCalendar.get(Calendar.DATE) == calendar.get(Calendar.DATE))
			{
				dayCells[i].setToday(true);
			}
			//周六
			if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
			{
				dayCells[i].setFontColor(context.getResources().getColor(R.color.canlendar_week_6));
			}
			//周日
			if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
			{
				dayCells[i].setFontColor(context.getResources().getColor(R.color.canlendar_week_7));
			}
			//设置值
			dayCells[i].setYear(calendar.get(Calendar.YEAR));
			dayCells[i].setMonth(calendar.get(Calendar.MONTH));
			dayCells[i].setDay(calendar.get(Calendar.DATE));
			//+1天，注意运行到最后，就是第二个月的第一天了。
			calendar.add(Calendar.DATE, 1);
		}
		//最后要重新设置为本月的第一天，这样为了滑动作准备
		calendar.add(Calendar.MONTH, -1);
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent e)
	{
		detector.onTouchEvent(e);
		if(e.getAction() == MotionEvent.ACTION_UP)
		{
			if(gestureAction == CalendarAction.CALENDAR_RIGHT)
			{
				moveX = 0;
				calendar.add(Calendar.MONTH, -1);
				setCalendar();
				anycInvalidate();
			}
			if(gestureAction == CalendarAction.CALENDAR_LEFT)
			{
				moveX = 0;
				calendar.add(Calendar.MONTH, 1);
				setCalendar();
				anycInvalidate();
			}
			if(gestureAction == CalendarAction.CALENDAR_NONE)
			{
				moveX = 0;
				setCalendar();
				anycInvalidate();
			}
			//转发事件
			if(gestureAction == CalendarAction.CALENDAR_TOUCH)
			{
				if(calendarSelectedListenter != null)
				{
					calendarSelectedListenter.onSelected(curDayCell.getYear(), curDayCell.getMonth(), curDayCell.getDay());
				}
			}
			gestureAction = CalendarAction.CALENDAR_DEFAULT;// 清除状态
		}
		
		return false;
	}
	private void anycInvalidate()
	{
		new Thread(new Runnable(){

			@Override
			public void run()
			{
				postInvalidate();
			}
			
		}).start();
	}
	@Override
	public void onDraw(Canvas canvas)
	{
		compute();
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
	//	drawBorder(canvas);
		drawTitle(canvas);
		header.drawWeekHeader(canvas,canlendarRect);
		drawDayCells(canvas);
	}
	private void drawDayCells(Canvas canvas)
	{
		for (DayCell cell:dayCells)
		{
			cell.drawDay(canvas);
		}
	}
	private void drawBorder(Canvas canvas)
	{
		paint.setColor(context.getResources().getColor(R.color.canlendar_border));
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(3);
		canvas.drawRoundRect(borderRect, 10, 10, paint);
	}
	
	private void compute()
	{
		//Stroke宽度为3，留白1，这样就空4出来
		borderRect = new RectF(4,4,getWidth()-4,getHeight()-4);	
		canlendarRect = new Rect(moveX+14,24,getWidth()+moveX-14,getHeight()-14);
		header.setHeaderRect(canlendarRect);
		dayCellContextRect = header.computeDayCellRect();
		//计算每个Cell的高和宽
		int cellWidth = dayCellContextRect.width() / 7;
		int cellHeight = dayCellContextRect.height()/6;//最多6行 
		int startX = dayCellContextRect.left;
		int startY = dayCellContextRect.top;
		int colIndex = 1;
		for(int i=0;i<42;i++)
		{
			Rect rect = new Rect(startX,startY,startX+cellWidth,startY+cellHeight);
			//7个一行
			if(colIndex % 7 != 0)
			{
				startX = startX+cellWidth;
			}
			//换行，Y下移一个cell的高度，X回到起点
			else
			{
				startX = dayCellContextRect.left;
				startY = startY+cellHeight;
			}
			colIndex++;
			DayCell dayCell = dayCells[i];
			dayCell.setRect(rect);
		}
	}
	private void drawTitle(Canvas canvas)
	{
		paint.setStyle(Paint.Style.FILL);
		paint.setTextSize(15);
		paint.setFakeBoldText(true);//伪粗体，中文使用
		paint.setTextAlign(Align.CENTER);
		paint.setColor(context.getResources().getColor(R.color.canlendar_header));
		int month = calendar.get(Calendar.MONTH)+1;
		canvas.drawText(calendar.get(Calendar.YEAR)+"年"+month+"月", canlendarRect.left+canlendarRect.width() / 2, canlendarRect.top-4 , paint);
	}
	
	class MyGestureListener extends SimpleOnGestureListener
	{
		//用户按下触摸屏，尚未松开或拖动，由一个MotionEvent.ACTION_DOWN触发.
		//注意和onDown的区别，强调的是没有松开或者拖动的状态
		@Override
		public void onShowPress(MotionEvent arg0)
		{
			super.onShowPress(arg0);
		}
		//用户按下触摸屏，由一个MotionEvent.ACTION_DOWN触发
		@Override
		public boolean onDown(MotionEvent e) 
		{
			//只增加背景，不触发处理
			for(final DayCell cell : dayCells)
			{
				if(cell.isClicked((int)e.getX(), (int)e.getY()) && cell.getText() != null)
				{		
					if(curDayCell != null)
					{
						
						curDayCell.setSelected(false);
						//更新局部，提高性能
						CalendarWidget.this.invalidate(curDayCell.getRect().left, curDayCell.getRect().top,
								curDayCell.getRect().right, curDayCell.getRect().bottom);
					}
					cell.setSelected(true);
					CalendarWidget.this.invalidate(cell.getRect().left, cell.getRect().top,
							cell.getRect().right, cell.getRect().bottom);
					curDayCell = cell;
					gestureAction = CalendarAction.CALENDAR_TOUCH;
				}
			}
			return false;
		}
		//用户(用户按下触摸屏后)松开，由MotionEvent.ACTION_UP触发
		//如果在onDown和onFling之中2选1，那么使用这个事件是可以区分的
		@Override
		public boolean onSingleTapUp(MotionEvent arg0)
		{
			return false;
		}
		//长按 多个ACTION_DOWN触发
		@Override
		public void onLongPress(MotionEvent arg0)
		{
			super.onLongPress(arg0);
		}
		//用户按下触摸屏，快速移动后松开，由一个MotionEvent.ACTION_DOWN,多个ACTION_MOVE,一个ACTION_UP触发.
		//velocityX和velocityY分别表示在X和Y方向上移动的速度
		//e1 第一个Down，e2最后一个move
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY)
		{
			//Toast.makeText(context, "飞行了", Toast.LENGTH_SHORT).show();
			//向左,速度每秒>200个像素
			if( (e1.getX()-e2.getX() > getWidth()/2) && (Math.abs(velocityX)) > 200)
			{
				gestureAction = CalendarAction.CALENDAR_LEFT;
			}
			//向右,速度每秒>200个像素
			else if( (e2.getX()-e1.getX() > getWidth()/2) && (Math.abs(velocityX)) > 200)
			{
				gestureAction = CalendarAction.CALENDAR_RIGHT;
			}
			
			return false;
		}
		//按下屏幕，并拖动，一个DOWM,多个MOVE触发
		//e1 第一个Down，e2当前移动的point
		//distance表示Scroll过程中的移动的像素。distanceX >0 表示向左，distanceY >0 表示向上，两个都大就是左上
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,float distanceY)
		{
			moveX -=distanceX;
			new Thread(new Runnable(){

				@Override
				public void run()
				{
					CalendarWidget.this.postInvalidate();
				}
				
			}).start();
			//向右
			if(e2.getX() - e1.getX()>getWidth()/2)
			{
				gestureAction = CalendarAction.CALENDAR_RIGHT;
			}
			//向左
			else if(e1.getX() - e2.getX()>getWidth()/2)
			{
				gestureAction = CalendarAction.CALENDAR_LEFT;
			}
			else 
			{
				gestureAction = CalendarAction.CALENDAR_NONE;
			}
			return false;
		}
	}
	public void setCalendarSelectedListenter(
			OnCalendarSelectedListenter calendarSelectedListenter)
	{
		this.calendarSelectedListenter = calendarSelectedListenter;
	}
}
enum CalendarAction
{
	CALENDAR_DEFAULT,
	CALENDAR_NONE,
	CALENDAR_TOUCH,
	CALENDAR_LEFT,
	CALENDAR_RIGHT,
}