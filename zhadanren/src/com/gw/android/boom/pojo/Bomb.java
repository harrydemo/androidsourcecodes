package com.gw.android.boom.pojo;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.gw.android.boom.R;
import com.gw.android.boom.screen.MainScreen;
/**
 * ’®µ•¿‡
 */
public class Bomb {
	private int px, py;
	private Bitmap bombB, boomB, fireB;
	private int frame = 0;
	public boolean isend;
	public Player player;
	public List<Mob> mobs;
    public MainScreen ms;
	public List<Mob> getMobs() {
		return mobs;
	}

	public void setMobs(List<Mob> mobs) {
		this.mobs = mobs;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	private int[][] map;

	public int[][] getMap() {
		return map;
	}

	public void setMap(int[][] map) {
		this.map = map;
	}

	public int getPx() {
		return px;
	}

	public void setPx(int px) {
		this.px = px;
	}

	public int getPy() {
		return py;
	}

	public void setPy(int py) {
		this.py = py;
	}

	public Bomb() {
		super();
	}

	public Bomb(int px, int py, Context ct,MainScreen ms) {
		super();
		this.px = px;
		this.py = py;
        this.ms=ms;
		Resources res = ct.getResources();
		bombB = BitmapFactory.decodeResource(res, R.drawable.bomb);
		boomB = BitmapFactory.decodeResource(res, R.drawable.boom);
		fireB = BitmapFactory.decodeResource(res, R.drawable.fire);
		res = null;
	}

	public void paint(Canvas c) {
		if (frame < 30) {
			c.drawBitmap(bombB, px, py, null);
		}
		if (frame >= 30) {
			c.drawBitmap(boomB, px, py, null);
		}
		if (frame >= 35) {
			c.drawBitmap(fireB, px, py, null);
			if (mobs != null && mobs.size() != 0) {
				for (Mob mob : mobs) {
					if ((py == mob.getY() && px == mob.getX())
							|| (px + 32 == mob.getX() && py == mob.getY())
							|| (px - 32 == mob.getX() && py == mob.getY())
							|| (py - 32 == mob.getY() && px == mob.getX())
							|| (py + 32 == mob.getY() && px == mob.getX())) {
						mob.isDead = true;
						ms.setScore(ms.getScore()+100);
					}
				}
			}
			if (!player.isWudi) {
				if ((py == player.getY() && px == player.getX())
						|| (px + 32 == player.getX() && py == player.getY())
						|| (px - 32 == player.getX() && py == player.getY())
						|| (py - 32 == player.getY() && px == player.getX())
						|| (py + 32 == player.getY() && px == player.getX())) {
					player.isDead = true;
				}
			}
			if (px + 32 < 448 && map[py / 32][(px + 32) / 32] != 2) {
				c.drawBitmap(fireB, px + 32, py, null);
				if (map[py / 32][(px + 32) / 32] == 1) {
					map[py / 32][(px + 32) / 32] = 0;
				}
			}
			if (px - 32 >= 0 && map[py / 32][(px - 32) / 32] != 2) {
				c.drawBitmap(fireB, px - 32, py, null);
				if (map[py / 32][(px - 32) / 32] == 1) {
					map[py / 32][(px - 32) / 32] = 0;
				}
			}
			if (py - 32 >= 0 && map[(py - 32) / 32][px / 32] != 2) {
				c.drawBitmap(fireB, px, py - 32, null);
				if (map[(py - 32) / 32][px / 32] == 1) {
					map[(py - 32) / 32][px / 32] = 0;
				}
			}
			if (py + 32 < 320 && map[(py + 32) / 32][px / 32] != 2) {
				c.drawBitmap(fireB, px, py + 32, null);
				if (map[(py + 32) / 32][px / 32] == 1) {
					map[(py + 32) / 32][px / 32] = 0;
				}
			}
			ms.setMap(map);
		}
		if (frame > 40) {
			isend = true;
		}
	}

	public void update() {
		frame++;
	}
}
