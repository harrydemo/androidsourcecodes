package bbth.game.achievements;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import bbth.engine.achievements.AchievementInfo;
import bbth.engine.ui.Anchor;
import bbth.engine.ui.UIImageView;
import bbth.engine.ui.UILabel;
import bbth.engine.ui.UILabel.VAlign;
import bbth.engine.ui.UIProgressBar;
import bbth.engine.ui.UIView;

/**
 * A view for a single achievement
 * 
 * @author jardini
 * 
 */
public class AchievementView extends UIView {

	public static final float NAME_SIZE = 19.f;
	public static final float DESCRIPTION_SIZE = 13.f;
	private static final float PADDING = 5;
	private static final float TOP_PADDING = 6;

	private UILabel _nameLabel;
	private UILabel _descriptionLabel;
	private UIImageView _image;
	private float _unlockProgress;
	private UIProgressBar _progressBar;
	private UILabel _progressLabel;
	private Paint _paint;

	public AchievementView(AchievementInfo info, int activations, Bitmap image) {
		_nameLabel = new UILabel(info.name, null);
		_nameLabel.setTextSize(NAME_SIZE);
		_nameLabel.setTextAlign(Align.LEFT);
		_nameLabel.sizeToFit();
		addSubview(_nameLabel);

		_descriptionLabel = new UILabel(info.description, null);
		_descriptionLabel.setTextSize(DESCRIPTION_SIZE);
		_descriptionLabel.setTextAlign(Align.LEFT);
		_descriptionLabel.setLineHeight(DESCRIPTION_SIZE * 1.4f);
		_descriptionLabel.setWrapText(true);
		_descriptionLabel.setVerticalAlign(VAlign.MIDDLE);
		addSubview(_descriptionLabel);

		_image = new UIImageView(image);
		_image.setSize(32, 32);
		_image.setAnchor(Anchor.CENTER_LEFT);
		addSubview(_image);

		_unlockProgress = activations / (float) (info.maxActivations);
		if (info.maxActivations > 1) {
			_progressBar = new UIProgressBar();
			_progressBar.setProgress(_unlockProgress);
			_progressBar.setBackgroundColor(Color.rgb(100, 100, 100));
			// _progressBar.setForegroundColor(Color.rgb(255, 255, 0));
			addSubview(_progressBar);
			_progressLabel = new UILabel(String.valueOf(activations)
					+ "/" + info.maxActivations); //$NON-NLS-1$
			_progressLabel.setTextSize(DESCRIPTION_SIZE);
			_progressLabel.sizeToFit();
			addSubview(_progressLabel);
		}

		_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		_paint.setColor(Color.WHITE);
		_paint.setStyle(Style.STROKE);
	}

	@Override
	public void setBounds(float left, float top, float right, float bottom) {
		super.setBounds(left, top, right, bottom);

		_image.setPosition(left, center.y);
		float imageRight = _image.getRect().right;
		float textWidth = _width - _image.getWidth() - PADDING;
		float textLeft = left + imageRight + PADDING;
		_nameLabel.setPosition(textLeft, top + TOP_PADDING);
		_nameLabel.setSize(textWidth, NAME_SIZE);
		_descriptionLabel.setPosition(textLeft, top + NAME_SIZE + TOP_PADDING
				+ 5);
		_descriptionLabel.setSize(textWidth, DESCRIPTION_SIZE);

		if (_progressBar != null) {
			_progressBar.setPosition(textLeft, top + NAME_SIZE + TOP_PADDING
					+ DESCRIPTION_SIZE + 12);
			_progressBar.setSize(175, 11);
			_progressLabel.setPosition(textLeft + 185, top + NAME_SIZE
					+ TOP_PADDING + DESCRIPTION_SIZE + 12);
		}
	}

	@Override
	public boolean isDraggable() {
		return true;
	}

	@Override
	public void onDraw(Canvas canvas) {
		if (_unlockProgress == 1) {
			_paint.setColor(Color.rgb(20, 20, 25));
			_paint.setStyle(Style.FILL);
			canvas.drawRect(_rect, _paint);
			_paint.setStyle(Style.STROKE);
		}
		super.onDraw(canvas);

		// draw border lines
		_paint.setColor(Color.WHITE);
		canvas.drawLine(_rect.left, _rect.top, _rect.right, _rect.top, _paint);
		canvas.drawLine(_rect.left, _rect.bottom, _rect.right, _rect.bottom,
				_paint);
	}
}
