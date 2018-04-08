package bbth.engine.ui;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;

public class UITooltip extends UILabel {
	private Paint tooltipPaint;
	private float radius;
	private float borderWidth;
	private int borderColor, backgroundColor;

	public UITooltip(String label, Object tag) {
		super(label, tag);

		tooltipPaint = new Paint(_paint);

		radius = UIDefaultConstants.CORNER_RADIUS;
		borderWidth = UIDefaultConstants.BORDER_WIDTH;
		borderColor = UIDefaultConstants.BACKGROUND_COLOR;
		backgroundColor = UIDefaultConstants.FOREGROUND_COLOR;

		tooltipPaint.setStrokeWidth(borderWidth);
	}

	@Override
	public void onDraw(Canvas canvas) {
		tooltipPaint.setStyle(Style.STROKE);
		tooltipPaint.setColor(borderColor);
		canvas.drawRoundRect(_rect, radius, radius, tooltipPaint);

		tooltipPaint.setStyle(Style.FILL);
		tooltipPaint.setColor(backgroundColor);
		canvas.drawRoundRect(_rect, radius, radius, tooltipPaint);

		super.onDraw(canvas);
	}
}
