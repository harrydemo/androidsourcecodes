package xujun.keepaccount.calendar;

import xujun.keepaccount.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Align;

public class CalendarHeader
{
	private Context context;
	private Rect headerRect;
	private static final String[] week = new String[]{"日","一","二","三","四","五","六"};
	public Rect getHeaderRect() {
		return headerRect;
	}

	public void setHeaderRect(Rect headerRect) {
		this.headerRect = headerRect;
	}

	public CalendarHeader(Context context)
	{
		this.context = context;
	}
	
	public void drawWeekHeader(Canvas canvas,Rect canlendarRect) 
	{
		int cellWidth = canlendarRect.width()/7;
		Paint paint = new Paint();
		//这里可以实现移动 ，以后需要提取
		int start = canlendarRect.left;
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(15);
		for(int i=0;i<7;i++)
		{
			paint.setColor(Color.WHITE);
			if(week[i].equals("六"))
			{
				paint.setColor(context.getResources().getColor(R.color.canlendar_week_6));
			}
			if(week[i].equals("日"))
			{
				paint.setColor(context.getResources().getColor(R.color.canlendar_week_7));
			}
			canvas.drawText(week[i], start+cellWidth/2, canlendarRect.top+15, paint);
			start+=cellWidth;
		}
	}
	public Rect computeDayCellRect()
	{
		return new Rect(headerRect.left,headerRect.top+16,headerRect.right,headerRect.bottom);
	}
}
