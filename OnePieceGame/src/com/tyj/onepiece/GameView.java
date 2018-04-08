package com.tyj.onepiece;

//画出网格，并对应的画上分布好的图像
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.tyj.onepiece.CtrlView.Point;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class GameView extends View {

	public final int row = 10;
	public final int col = 10;
	public float width;
	public float height;
	private int selY;
	private int selX;
	public boolean isLine = false;
	public int grid[][] = new int[row][col];
	private Rect selRect = new Rect();
	public int lineType = 0;
	public final int V_LINE = 1;
	public final int H_LINE = 1;
	public final int ONE_C_LINE = 2;
	public final int TWO_C_LINE = 3;
	public int much = 0;
	Point[] p;
	public int[] imageType = new int[] { R.drawable.aa, R.drawable.bb,
			R.drawable.cc, R.drawable.dd, R.drawable.ee, R.drawable.ff,
			R.drawable.gg, R.drawable.hh, R.drawable.ii, R.drawable.jj,
			R.drawable.kk, R.drawable.ll, R.drawable.mm, R.drawable.nn,
			R.drawable.oo, R.drawable.pp };
	public Bitmap[] image;
	public List<Integer> type = new ArrayList<Integer>();

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
	}

	public GameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
	}

	public void reset() {

	}

	public void fillImage(Context context) {
		int lth = imageType.length;
		image = new Bitmap[lth];
		for (int i = 0; i < lth; i++) {
			Bitmap bitmap = Bitmap.createBitmap((int) width, (int) height,
					Bitmap.Config.ARGB_8888);
			Drawable drw;
			Canvas canvas = new Canvas(bitmap);
			drw = context.getResources().getDrawable(imageType[i]);
			drw.setBounds(1, 1, 30, 30);
			drw.draw(canvas);
			image[i] = bitmap;
		}
	}

	public void initType() {
		int size = (row - 2) * (col - 2);
		int count = size / imageType.length;
		for (int j = 0; j < imageType.length; j++) {
			for (int i = 0; i < count; i++) {
				type.add(imageType[j]);
			}
		}
	}

	public void select(int x, int y) {
		invalidate(selRect);
		selX = Math.min(Math.max(x, 0), 9);
		selY = Math.min(Math.max(y, 0), 9);
		getRect(selX, selY, selRect);
		invalidate(selRect);
	}

	private void getRect(int x, int y, Rect rect) {
		rect.set((int) (x * width), (int) (y * height),
				(int) (x * width + width), (int) (y * height + height));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint background = new Paint();
		background.setColor(Color.WHITE);
		canvas.drawRect(0, 0, getWidth(), getHeight(), background);
		Paint hilite = new Paint();
		hilite.setColor(getResources().getColor(R.color.hilite));
		Paint light = new Paint();
		light.setColor(getResources().getColor(R.color.light));
		//画出网格
		for (int i = 0; i <= 9; i++) {
			canvas.drawLine(0, i * height, getWidth(), i * height, light);
			canvas.drawLine(0, i * height + 1, getWidth(), i * height + 1,
					hilite);
			canvas.drawLine(i * width, 0, i * width, getHeight(), light);
			canvas.drawLine(i * width + 1, 0, i * width + 1, getHeight(),
					hilite);
		}
		
		if (CtrlView.CURRENT_CH) {
			Paint selected = new Paint();
			selected.setColor(getResources().getColor(R.color.puzzle_selected));
			canvas.drawRect(selRect, selected);
		}

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (grid[i][j] != 0) {
					canvas.drawBitmap(image[Arrays.binarySearch(imageType,
							grid[i][j])], i * width, j * height, null);
				}
			}
		}

		if (isLine) {
			Paint lineColor = new Paint();
			lineColor.setColor(Color.RED);

			switch (lineType) {
			case V_LINE:
				canvas.drawLine(p[0].x * width + width / 2, p[0].y * height
						+ height / 2, p[1].x * width + width / 2, p[1].y
						* height + height / 2, lineColor);
				break;
			case ONE_C_LINE:
				canvas.drawLine(p[0].x * width + width / 2, p[0].y * height
						+ height / 2, p[1].x * width + width / 2, p[1].y
						* height + height / 2, lineColor);
				canvas.drawLine(p[1].x * width + width / 2, p[1].y * height
						+ height / 2, p[2].x * width + width / 2, p[2].y
						* height + height / 2, lineColor);
				break;
			case TWO_C_LINE:
				canvas.drawLine(p[0].x * width + width / 2, p[0].y * height
						+ height / 2, p[1].x * width + width / 2, p[1].y
						* height + height / 2, lineColor);
				canvas.drawLine(p[1].x * width + width / 2, p[1].y * height
						+ height / 2, p[2].x * width + width / 2, p[2].y
						* height + height / 2, lineColor);
				canvas.drawLine(p[3].x * width + width / 2, p[3].y * height
						+ height / 2, p[2].x * width + width / 2, p[2].y
						* height + height / 2, lineColor);
				break;
			default:
				break;
			}
		}
		super.onDraw(canvas);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		width = w / row;
		height = h / col;
		// getRect(1,1,selRect);
		fillImage(this.getContext());
		super.onSizeChanged(w, h, oldw, oldh);
	}

	public void initGrid() {
		Random ad = new Random();
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if (i == 0 || i == row - 1 || j == 0 || j == col - 1) {
					grid[i][j] = 0;
				} else {
					if (type != null && type.size() > 0) {
						int index = ad.nextInt(type.size());
						grid[i][j] = type.get(index);
						type.remove(index);
					}
				}
			}
		}
	}

}
