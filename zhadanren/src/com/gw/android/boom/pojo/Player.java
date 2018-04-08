package com.gw.android.boom.pojo;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.gw.android.boom.R;
import com.gw.android.boom.screen.Controler;

public class Player {
	private Bitmap play_d, play_l, play_u, play_r, play_o;
	private int x = 0;
	private int y = 0;
	private int[][] map;
	private List<Bomb> bombs;
	public boolean isDead;
	private int frame = 0;
	/**
	 * Œﬁµ– Ù–‘
	 */
	public boolean isWudi;
	private int a = 80;

	public List<Bomb> getBombs() {
		return bombs;
	}

	public void setBombs(List<Bomb> bombs) {
		this.bombs = bombs;
	}

	public int[][] getMap() {
		return map;
	}

	public void setMap(int[][] map) {
		this.map = map;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Player() {
		super();
	}

	public Player(int x, int y, Context ct) {
		this(ct);
		this.x = x;
		this.y = y;
	}

	public Player(Context ct) {
		Resources res = ct.getResources();
		play_d = BitmapFactory.decodeResource(res, R.drawable.play_d);
		play_l = BitmapFactory.decodeResource(res, R.drawable.play_l);
		play_u = BitmapFactory.decodeResource(res, R.drawable.play_u);
		play_r = BitmapFactory.decodeResource(res, R.drawable.play_r);
		play_o = BitmapFactory.decodeResource(res, R.drawable.play_o);
		res = null;
	}

	public boolean canMove(String direction) {
		if (bombs.size() != 0) {
			for (Bomb bomb : bombs) {
				if (direction.equals("left")) {
					if (bomb.getPx() == x - 32 && bomb.getPy() == y) {
						return false;
					}
				} else if (direction.equals("right")) {
					if (bomb.getPx() == x + 32 && bomb.getPy() == y) {
						return false;
					}
				} else if (direction.equals("up")) {
					if (bomb.getPy() == y - 32 && bomb.getPx() == x) {
						return false;
					}
				} else if (direction.equals("down")) {
					if (bomb.getPy() == y + 32 && bomb.getPx() == x) {
						return false;
					}
				}
			}
		}

		if (direction.equals("left")
				&& (x - 32 < 0 || map[y / 32][(x - 32) / 32] == 1 || map[y / 32][(x - 32) / 32] == 2)) {
			return false;
		} else if (direction.equals("right")
				&& (x + 32 >= 448 || map[y / 32][(x + 32) / 32] == 1 || map[y / 32][(x + 32) / 32] == 2)) {
			return false;
		} else if (direction.equals("up")
				&& (y - 32 < 0 || map[(y - 32) / 32][x / 32] == 1 || map[(y - 32) / 32][x / 32] == 2)) {
			return false;
		} else if (direction.equals("down")
				&& (y + 32 >= 320 || map[(y + 32) / 32][x / 32] == 1 || map[(y + 32) / 32][x / 32] == 2)) {
			return false;
		}
		return true;
	}

	public void paint(Canvas c) {
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		c.drawBitmap(play_d, 448, 32, paint);

		if (isDead && frame < 30) {
			c.drawBitmap(play_o, x, y, paint);
			return;
		}
		if (!isDead) {
			if (Controler.direction.equals("none")) {
				if (Controler.preD.equals("left")) {
					c.drawBitmap(play_l, x, y, paint);
				}
				if (Controler.preD.equals("right")) {
					c.drawBitmap(play_r, x, y, paint);
				}
				if (Controler.preD.equals("up")) {
					c.drawBitmap(play_u, x, y, paint);
				}
				if (Controler.preD.equals("down")) {
					c.drawBitmap(play_d, x, y, paint);
				}
			}
			if (Controler.direction.equals("left")) {
				c.drawBitmap(play_l, x, y, paint);
			}
			if (Controler.direction.equals("right")) {
				c.drawBitmap(play_r, x, y, paint);
			}
			if (Controler.direction.equals("up")) {
				c.drawBitmap(play_u, x, y, paint);
			}
			if (Controler.direction.equals("down")) {
				c.drawBitmap(play_d, x, y, paint);
			}
			if (isWudi) {
				a += 20;
				if (a >= 230) {
					a = 0;
				}
				paint.setARGB(a, 255, 30, 30);
				c.drawRoundRect(new RectF(x + 6, y + 2, x + 24, y + 30), 5, 5,
						paint);
				// c.drawRect(new RectF(x + 8, y + 2, x + 22, y + 30), paint);
			}
		}
	}

	public void update() {
		if (isDead) {
			frame++;
		}
		if (canMove(Controler.direction)) {
			if (Controler.direction.equals("left")) {
				x -= 32;
			}
			if (Controler.direction.equals("right")) {
				x += 32;
			}
			if (Controler.direction.equals("up")) {
				y -= 32;
			}
			if (Controler.direction.equals("down")) {
				y += 32;
			}
		}
	}
}
