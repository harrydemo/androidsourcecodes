package bbth.engine.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import bbth.game.BBTHActivity;

public class UIImageView extends UIView {

	private Bitmap _image;
	private Paint _paint;
	private float _old_width, _old_height;

	public UIImageView(int id) {

		_paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

		Options options = new Options();
		options.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;
		_image = BitmapFactory.decodeResource(
				BBTHActivity.instance.getResources(), id);
	}

	public UIImageView(Bitmap bitmap) {
		_paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
		_image = bitmap;
	}

	@Override
	public void setBounds(float left, float top, float right, float bottom) {
		super.setBounds(left, top, right, bottom);
		if (_width > 0 && _height > 0
				&& (_width != _old_width || _height != _old_height)) {
			if (_image.getHeight() != (int) _height
					|| _image.getWidth() != (int) _width)
				_image = Bitmap.createScaledBitmap(_image, (int) _width,
						(int) _height, true);
			_old_width = _width;
			_old_height = _height;
		}

	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawBitmap(_image, _rect.left, _rect.top, _paint);
	}

}
