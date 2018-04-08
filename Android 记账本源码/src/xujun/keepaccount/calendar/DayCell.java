package xujun.keepaccount.calendar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;

class DayCell
{
	private Rect rect;
	private String text;
	private boolean selected;
	private int fontColor = Color.WHITE;
	private int year;
	private int month;
	private int day;
	private boolean today;
	
	public boolean isClicked(int mouseX,int mouseY)
	{
		if(rect.contains(mouseX, mouseY))
		{
			return true;
		}
		return false;
	}
	public void drawDay(Canvas canvas)
	{
		Paint paint = new Paint();
		//选中状态，增加背景色
		if(selected && !today)
		{
			Shader bgShader = new LinearGradient(rect.left, rect.top, rect.right, rect.bottom, Color.parseColor("#43682f"), Color.parseColor("#5cc35c"), Shader.TileMode.MIRROR);
			paint.setShader(bgShader);
			canvas.drawRoundRect(new RectF(rect), 3, 3, paint);
		}
		//是今天，增加背景标识
		if(today)
		{
			Shader bgShader = new LinearGradient(rect.left, rect.top, rect.right, rect.bottom, Color.parseColor("#ff8200"), Color.parseColor("#ffcea8"), Shader.TileMode.MIRROR);
			paint.setShader(bgShader);
			canvas.drawRoundRect(new RectF(rect), 3, 3, paint);
		}
		paint.setShader(null);
		paint.setColor(fontColor);
		paint.setTextAlign(Align.CENTER);
		//rect的开始＋宽度/2，这样得到rect的X中点坐标
		//计算字体高度，计算出居中的定位
		FontMetrics fm = paint.getFontMetrics();
		int fontHeight = (int) Math.ceil(fm.descent - fm.top)-6;
		if(text != null)
		{
			canvas.drawText(text, rect.left+rect.width()/2, rect.bottom-(rect.height()-fontHeight)/2, paint);
		}
	}
	public void clearState()
	{
		setText(null);
		setSelected(false);
		setDay(0);
		setMonth(0);
		setYear(0);
		setToday(false);
	}
	public Rect getRect() {
		return rect;
	}
	public void setRect(Rect rect) {
		this.rect = rect;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getFontColor() {
		return fontColor;
	}
	public void setFontColor(int fontColor) {
		this.fontColor = fontColor;
	}
	public boolean isToday()
	{
		return today;
	}
	public void setToday(boolean today)
	{
		this.today = today;
	}
	
}
