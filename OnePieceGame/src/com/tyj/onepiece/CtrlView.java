package com.tyj.onepiece;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CtrlView extends GameView {
	public final int GAMETIME = 300;
	public final int UPTIME = 1;
	public int PROCESS_VALUE = 300;
	public static boolean CURRENT_CH = false;
	public int CURRENT_TYPE = 0;
	private Point C_POINT;
	private Point P_POINT;
	LinkedList<Line> li;

	public CtrlView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initType();
		initGrid();
		much = (row - 2) * (col - 2);
	}

	public CtrlView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initType();
		initGrid();
		much = (row - 2) * (col - 2);
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() != MotionEvent.ACTION_DOWN)
			return super.onTouchEvent(event);

		int selX = (int) (event.getX() / width);
		int selY = (int) (event.getY() / height);

		if (grid[selX][selY] == 0)
			return true;
		else {
			if (CURRENT_CH == false) {
				select(selX, selY);
				CURRENT_CH = true;
				P_POINT = new Point(selX, selY);
			} else {
				C_POINT = new Point(selX, selY);
				lineType = 0;
				if (checkLink(P_POINT, C_POINT)) {
					isLine = true;
					much = much - 2;
					if (0 < PROCESS_VALUE
							&& (PROCESS_VALUE + UPTIME) < GAMETIME) {
						PROCESS_VALUE = PROCESS_VALUE + UPTIME;
					}
					invalidate();
					mRedrawHandler.sleep(300);
				}
				CURRENT_CH = false;
			}
		}
		return true;
	}

	public void reset() {
		CURRENT_CH = false;
		CURRENT_TYPE = 0;
		C_POINT = null;
		P_POINT = null;
		lineType = 0;
		isLine = false;
		Point[] p = null;
		initType();
		initGrid();
		much = (row - 2) * (col - 2);
		invalidate();
	}

	public void rearrange() {
		CURRENT_CH = false;
		CURRENT_TYPE = 0;
		C_POINT = null;
		P_POINT = null;
		lineType = 0;
		isLine = false;
		Point[] p = null;
		List<Integer> temp = new ArrayList<Integer>();
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if (grid[i][j] != 0) {
					temp.add(grid[i][j]);
				}
			}
		}
		type.clear();
		Random ad = new Random();
		for (int i = 0; i < temp.size(); i++) {
			type.add(temp.get(i));
		}
		temp.clear();
		temp = null;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if (grid[i][j] != 0) {
					int index = ad.nextInt(type.size());
					grid[i][j] = type.get(index);
					type.remove(index);
				}
			}
		}
		invalidate();
	}

	private RefreshHandler mRedrawHandler = new RefreshHandler();

	class RefreshHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			isLine = false;
			grid[P_POINT.x][P_POINT.y] = 0;
			grid[C_POINT.x][C_POINT.y] = 0;
			CtrlView.this.invalidate();
		}

		public void sleep(long delayMillis) {
			this.removeMessages(0);// 移除信息队列中最顶部的信息（从顶部取出信息）
			sendMessageDelayed(obtainMessage(0), delayMillis);// 获得顶部信息并延时发送
		}
	};

	public class Point {
		public int x;
		public int y;

		public Point(int newx, int newy) {
			this.x = newx;
			this.y = newy;
		}

		public boolean equals(Point p) {
			if (p.x == x && p.y == y)
				return true;
			else
				return false;
		}
	}

	private boolean horizon(Point a, Point b) {
		if (a.x == b.x && a.y == b.y)
			return false;
		int x_start = a.y <= b.y ? a.y : b.y;
		int x_end = a.y <= b.y ? b.y : a.y;
		for (int x = x_start + 1; x < x_end; x++)
			if (grid[a.x][x] != 0) {
				return false;
			}
		p = new Point[] { a, b };
		lineType = H_LINE;
		return true;
	}

	private boolean vertical(Point a, Point b) {
		if (a.x == b.x && a.y == b.y)
			return false;
		int y_start = a.x <= b.x ? a.x : b.x;
		int y_end = a.x <= b.x ? b.x : a.x;
		for (int y = y_start + 1; y < y_end; y++)
			if (grid[y][a.y] != 0)
				return false;
		p = new Point[] { a, b };
		lineType = V_LINE;
		return true;
	}

	private boolean oneCorner(Point a, Point b) {
		Point c = new Point(a.x, b.y);
		Point d = new Point(b.x, a.y);
		if (grid[c.x][c.y] == 0) {
			boolean method1 = horizon(a, c) && vertical(b, c);
			p = new Point[] { a, new Point(c.x, c.y), b };
			lineType = ONE_C_LINE;
			return method1;
		}
		if (grid[d.x][d.y] == 0) {
			boolean method2 = vertical(a, d) && horizon(b, d);
			p = new Point[] { a, new Point(d.x, d.y), b };
			lineType = ONE_C_LINE;
			return method2;
		} else {
			return false;
		}
	}

	class Line {
		public Point a;
		public Point b;
		public int direct;

		public Line() {
		}

		public Line(int direct, Point a, Point b) {
			this.direct = direct;
			this.a = a;
			this.b = b;
		}
	}

	private LinkedList<Line> scan(Point a, Point b) {
		li = new LinkedList<Line>();
		for (int y = a.y; y >= 0; y--)
			if (grid[a.x][y] == 0 && grid[b.x][y] == 0
					&& vertical(new Point(a.x, y), new Point(b.x, y)))
				li.add(new Line(0, new Point(a.x, y), new Point(b.x, y)));

		for (int y = a.y; y < row; y++)
			if (grid[a.x][y] == 0 && grid[b.x][y] == 0
					&& vertical(new Point(a.x, y), new Point(b.x, y)))
				li.add(new Line(0, new Point(a.x, y), new Point(b.x, y)));

		for (int x = a.x; x >= 0; x--)
			if (grid[x][a.y] == 0 && grid[x][b.y] == 0
					&& horizon(new Point(x, a.y), new Point(x, b.y)))
				li.add(new Line(1, new Point(x, a.y), new Point(x, b.y)));

		for (int x = a.x; x < col; x++)
			if (grid[x][a.y] == 0 && grid[x][b.y] == 0
					&& horizon(new Point(x, a.y), new Point(x, b.y)))
				li.add(new Line(1, new Point(x, a.y), new Point(x, b.y)));

		return li;
	}

	private boolean twoCorner(Point a, Point b) {
		li = scan(a, b);
		if (li.isEmpty())
			return false;
		for (int index = 0; index < li.size(); index++) {
			Line line = (Line) li.get(index);
			if (line.direct == 1) {
				if (vertical(a, line.a) && vertical(b, line.b)) {
					p = new Point[] { a, line.a, line.b, b };
					lineType = TWO_C_LINE;
					return true;
				}
			} else if (horizon(a, line.a) && horizon(b, line.b)) {
				p = new Point[] { a, line.a, line.b, b };
				lineType = TWO_C_LINE;
				return true;
			}
		}
		return false;
	}

	public boolean checkLink(Point a, Point b) {
		if (grid[a.x][a.y] != grid[b.x][b.y])// 如果图案不同，直接为false
			return false;
		if (a.x == b.x && horizon(a, b))
			return true;
		if (a.y == b.y && vertical(a, b))
			return true;
		if (oneCorner(a, b))
			return true;
		else
			return twoCorner(a, b);
	}

}
