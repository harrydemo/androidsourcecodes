package cn.itcast.accelerometer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class BallView extends ImageView {

	public BallView(Context context) {
		super(context);
	}

    public BallView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    public BallView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
 
    public void moveTo(int x, int y) {
        super.setFrame(x, y, x + getWidth(), y + getHeight());//绘制视图，由左上角与右下角确定视图矩形位置
    }
}
