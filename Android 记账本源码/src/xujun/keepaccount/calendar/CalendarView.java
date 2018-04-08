package xujun.keepaccount.calendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class CalendarView extends View 
{
	private Context context;
	private Paint paint;
	private int borderWidth = 3;
	private String dateFormatString;
	private Calendar calendar;
	
	public CalendarView(Context context) {
		super(context,null);
	}
	public CalendarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		calendar = Calendar.getInstance();
		dateFormatString = String.format("%d年 %d月 %d日", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DATE));	
	}
	public void setCalendar(int year,int month,int day)
	{
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DATE, day);
		dateFormatString = String.format("%d年 %d月 %d日", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DATE));	
		invalidate();
	}
	@Override
	protected void onDraw(Canvas canvas) {
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(borderWidth);
		paint.setColor(Color.parseColor("#d2c2a8"));
		canvas.drawRoundRect(new RectF(1,1,getWidth()-1,getHeight()-1),6,6, paint);
		
		paint.setStyle(Paint.Style.FILL);
		paint.setShader(new LinearGradient(0, 0, getWidth(), getHeight(), Color.parseColor("#43682f"), Color.parseColor("#5cc35c"),  Shader.TileMode.MIRROR));
		canvas.drawRoundRect(new RectF(borderWidth-1,borderWidth-1,getWidth()-borderWidth+1,getHeight()-borderWidth+1),4,4, paint);
		
		paint.setShader(null);
		paint.setColor(Color.WHITE);
		paint.setTextAlign(Align.CENTER);
		paint.setFakeBoldText(true);
		paint.setTextSize(15);
		FontMetrics fm = paint.getFontMetrics();
		int fontHeight = (int) Math.ceil(fm.descent - fm.top)-8;
		canvas.drawText(dateFormatString, getWidth()/2, fontHeight+(getHeight()-fontHeight)/2, paint);
	}
	
	public String getDate()
	{
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(calendar.getTime());
	}
	public String getDateFormatString() {
		return dateFormatString;
	}
}
