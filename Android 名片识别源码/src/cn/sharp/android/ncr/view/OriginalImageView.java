package cn.sharp.android.ncr.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class OriginalImageView extends ImageView {
	private Matrix matrix;
	private final static String TAG = "OriginalImageView";
	private int viewHeight;
	private int viewWidth;
	private OnZoomOutToMinListener onZoomOutToMinListener;
	private OnZoomInToMaxListener onZoomInToMaxListener;

	public OriginalImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		matrix = getImageMatrix();
		Log.e(TAG, "height:" + viewHeight + ",width:" + viewWidth);
		if (this.getDrawable() == null)
			return;
		if (firstDraw) {
			viewHeight = this.getHeight();
			viewWidth = this.getWidth();
			Rect rect = getDrawable().getBounds();
			int imageHeight = rect.bottom - rect.top;
			int imageWidth = rect.right - rect.left;
			taller = imageHeight * 1.0 / imageWidth > viewHeight * 1.0
					/ viewWidth;
			firstDraw = false;
			Log.e(TAG, "taller?" + taller);

			if (taller) {
				Log.d(TAG, "image height:" + imageHeight);
				if (imageHeight > viewHeight) {
					matrix.postTranslate(-viewWidth / 2, -viewHeight / 2);
					float ratio = viewHeight * 1.0f / imageHeight;
					matrix.postScale(ratio, ratio);
					matrix.postTranslate(viewWidth / 2, viewHeight / 2);
				} else {
					if (onZoomInToMaxListener != null) {
						onZoomInToMaxListener.OnZoomInToMax();
					}
				}
			} else {
				if (imageWidth > viewWidth) {
					Log.v(TAG, "image has been zoomed out to min");
					matrix.postTranslate(-viewWidth / 2, -viewHeight / 2);
					float ratio = viewWidth * 1.0f / imageWidth;
					matrix.postScale(ratio, ratio);
					matrix.postTranslate(viewWidth / 2, viewHeight / 2);
				} else {
					if (onZoomInToMaxListener != null) {
						onZoomInToMaxListener.OnZoomInToMax();
					}
				}
			}
			if (onZoomOutToMinListener != null) {
				onZoomOutToMinListener.OnZoomOutToMin();
			}
		}
		super.onDraw(canvas);
	}

	private boolean firstDraw = true;
	private boolean taller = false;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * if ratio>1 the image will be zoomed in, else if ratio<1 the image will be
	 * zoomed out
	 * 
	 * @param ratio
	 */
	public void zoom(float ratio) {
		if (getDrawable() == null)
			return;
		if (ratio > 0) {
			if (src == null) {
				Rect rect = getDrawable().getBounds();
				src = new float[] { rect.left, rect.top, rect.right,
						rect.bottom };
			}
			matrix.mapPoints(dst, src);
			if (ratio < 1) {
				if (taller) {
					float imageHeight = dst[3] - dst[1];
					Log.d(TAG, "image height:" + imageHeight);
					if (imageHeight * ratio <= viewHeight) {
						ratio = (viewHeight / imageHeight);
						if (onZoomOutToMinListener != null) {
							onZoomOutToMinListener.OnZoomOutToMin();
						}
					}
				} else {
					float imageWidth = dst[2] - dst[0];
					Log.d(TAG, "image width:" + imageWidth);
					if (imageWidth * ratio <= viewWidth) {
						Log.v(TAG, "image has been zoomed out to min");
						ratio = (viewWidth / imageWidth);
						if (onZoomOutToMinListener != null) {
							onZoomOutToMinListener.OnZoomOutToMin();
						}
					}
				}
			} else if (ratio > 1) {
				Rect rect = getDrawable().getBounds();
				float imageHeight = dst[3] - dst[1];
				int originalImageHeight = rect.bottom - rect.top;
				if (imageHeight * ratio >= originalImageHeight) {
					ratio = originalImageHeight * 1.0f / imageHeight;
					if (onZoomInToMaxListener != null) {
						onZoomInToMaxListener.OnZoomInToMax();
					}
				}
			}
			if (ratio == 1) {
				return;
			}
			matrix.postTranslate(-viewWidth / 2, -viewHeight / 2);
			matrix.postScale(ratio, ratio);
			matrix.postTranslate(viewWidth / 2, viewHeight / 2);
			if (src == null) {
				Rect rect = getDrawable().getBounds();
				src = new float[] { rect.left, rect.top, rect.right,
						rect.bottom };
			}
			matrix.mapPoints(dst, src);
			float dx = viewWidth - dst[2];
			float dy = viewHeight - dst[3];
			if (dx > 0) {
				float adx = dst[0] + dx;
				if (adx > 0) {
					adx = (dx - dst[0]) / 2;
					matrix.postTranslate(adx, 0);
				} else {
					matrix.postTranslate(dx, 0);
				}
			} else if (dst[0] > 0) {
				float adx = (dst[0] + dx);
				if (adx > 0) {
					adx = (dst[0] - dx) / 2;
					matrix.postTranslate(-adx, 0);
				} else {
					matrix.postTranslate(-dst[0], 0);
				}
			}
			if (dy > 0) {
				float ady = dst[1] + dy;
				if (ady > 0) {
					ady = (dy - dst[1]) / 2;
					matrix.postTranslate(0, ady);
				} else {
					matrix.postTranslate(0, dy);
				}
			} else if (dst[1] > 0) {
				float ady = dst[1] + dy;
				if (ady > 0) {
					ady = (dst[1] - dy) / 2;
					matrix.postTranslate(0, -ady);
				} else {
					matrix.postTranslate(0, -dst[1]);
				}
			}

		} else {
			Log.e(TAG, "ratio should be positive:" + ratio);
		}
	}

	float[] src;
	float[] dst = new float[4];

	/**
	 * 
	 * @param dx
	 * @param dy
	 */
	public void move(float dx, float dy) {
		if (getDrawable() == null)
			return;
		matrix.postTranslate(dx, dy);
		if (src == null) {
			Rect rect = getDrawable().getBounds();
			/** left-top corner and right-bottom corner */
			src = new float[] { rect.left, rect.top, rect.right, rect.bottom };
		}
		Log.d(TAG, "view width:" + viewWidth + ",view heigth:" + viewHeight);
		matrix.mapPoints(dst, src);
		if (dst[0] <= 0 && dst[2] >= viewWidth) {
			dx = 0;
		}
		if (dst[1] <= 0 && dst[3] >= viewHeight) {
			dy = 0;
		}
		Log.d(TAG, "dx:" + dx + ",dy:" + dy);
		matrix.postTranslate(-dx, -dy);
	}

	public void resetImage() {
		firstDraw = true;
	}

	public OnZoomOutToMinListener getOnZoomOutToMinListener() {
		return onZoomOutToMinListener;
	}

	public void setOnZoomOutToMinListener(
			OnZoomOutToMinListener onZoomOutToMinListener) {
		this.onZoomOutToMinListener = onZoomOutToMinListener;
	}

	public OnZoomInToMaxListener getOnZoomInToMaxListener() {
		return onZoomInToMaxListener;
	}

	public void setOnZoomInToMaxListener(
			OnZoomInToMaxListener onZoomInToMaxListener) {
		this.onZoomInToMaxListener = onZoomInToMaxListener;
	}

	/**
	 * Àı–°
	 * @author linjunqi
	 * 
	 */
	public interface OnZoomOutToMinListener {
		void OnZoomOutToMin();
	}

	/**
	 * ∑≈¥Û
	 * @author linjunqi
	 * 
	 */
	public interface OnZoomInToMaxListener {
		void OnZoomInToMax();
	}
}
