package org.loon.act.test;

import org.loon.framework.android.game.core.geom.RectBox;
import org.loon.framework.android.game.core.geom.Vector2f;
import org.loon.framework.android.game.core.graphics.LFont;
import org.loon.framework.android.game.core.graphics.opengl.GLColor;
import org.loon.framework.android.game.core.graphics.opengl.GLEx;
import org.loon.framework.android.game.core.graphics.opengl.LTexture;
import org.loon.framework.android.game.core.timer.LTimer;


public class Enemy {

	private LTimer timer = new LTimer(200);

	public static final int ETYPE = 0;

	public static final int FTYPE = 1;

	private int[] enemyX;

	private int[] enemyY;

	private int[] vx;

	private int[] vy;

	private int[] eType;

	private int[] bound;

	private int[] dir;

	private int[] a_count;

	private int[] stf;

	private int[] score;

	private int[] combo;

	private boolean[] dethf;

	private long[] ew_nowTime;

	private long[] ew_nextTime;

	private boolean[] ew_timef;

	private boolean[] downf;

	private boolean[] reversef;

	private LTexture enemyImg;

	private LTexture enemyImg_F;

	private Map map;

	public Enemy(LTexture img, LTexture img2, Map map) {
		this.map = map;
		this.load();
		this.enemyImg = img;
		this.enemyImg_F = img2;
	}

	public void setStf(int i) {
		this.dethf[i] = true;
		this.stf[i] = 40;
	}

	public void setCombo(int a, int com) {
		this.combo[a] = com;
	}

	public void update(int t) {
		this.vy[t] = (int) ((double) this.vy[t] + 1.0D);
		if (this.enemyX[t] > 0) {
			if (this.dethf[t]) {
				if (this.downf[t]) {
					this.reversef[t] = true;
					this.enemyY[t] += this.vy[t];
					this.enemyX[t] += this.vx[t];
				} else if (this.ew_timef[t]) {
					this.ew_nextTime[t] = System.currentTimeMillis();
					if (this.ew_nextTime[t] - this.ew_nowTime[t] > 100L
							&& this.eType[t] == 0) {
						this.vy[t] = -12;
						this.vx[t] = -this.vx[t];
						this.downf[t] = true;
					} else if (this.ew_nextTime[t] - this.ew_nowTime[t] > 0L
							&& this.eType[t] == 1) {
						this.downf[t] = true;
					}
				} else {
					this.ew_nowTime[t] = System.currentTimeMillis();
					this.ew_timef[t] = true;
				}
			} else {
				if (this.vy[t] > 0) {
					this.stf[t] = 30;
				} else if (this.vy[t] < 0) {
					this.stf[t] = 10;
				}

				int newX = this.enemyX[t] + this.vx[t];
				Vector2f tile = this.map.getTileCollision(this, (double) newX,
						(double) this.enemyY[t], t, this.eType[t]);
				if (tile == null) {
					this.enemyX[t] = newX;
				} else {
					if (this.vx[t] > 0) {
						this.enemyX[t] = map.tilesToWidthPixels(tile.x())
								- map.getTileWidth();
					} else if (this.vx[t] < 0) {
						this.enemyX[t] = map.tilesToWidthPixels(tile.x() + 1);
					}

					this.vx[t] = -this.vx[t];
					if (this.vx[t] > 0) {
						this.dir[t] = 0;
					} else if (this.vx[t] < 0) {
						this.dir[t] = 1;
					}
				}

				int newY = this.enemyY[t] + this.vy[t];
				tile = this.map.getTileCollision(this, (double) this.enemyX[t],
						(double) newY, t, this.eType[t]);
				if (tile == null) {
					this.enemyY[t] = newY;
				} else if (this.vy[t] > 0) {
					this.stf[t] = 20;
					this.enemyY[t] = map.tilesToHeightPixels(tile.y())
							- map.getTileHeight();
					this.vy[t] = this.bound[t];
				} else if (this.vy[t] < 0) {
					this.stf[t] = 30;
					this.enemyY[t] = map.tilesToHeightPixels(tile.y() + 1);
					this.vy[t] = 0;
				}
			}
		}

	}

	private void load() {
		int index = 0;
		this.enemyX = new int[this.map.getECount()];
		this.enemyY = new int[this.map.getECount()];
		this.vx = new int[this.map.getECount()];
		this.vy = new int[this.map.getECount()];
		this.eType = new int[this.map.getECount()];
		this.bound = new int[this.map.getECount()];
		this.dir = new int[this.map.getECount()];
		this.a_count = new int[this.map.getECount()];
		this.stf = new int[this.map.getECount()];
		this.score = new int[this.map.getECount()];
		this.combo = new int[this.map.getECount()];
		this.dethf = new boolean[this.map.getECount()];
		this.ew_nowTime = new long[this.map.getECount()];
		this.ew_nextTime = new long[this.map.getECount()];
		this.ew_timef = new boolean[this.map.getECount()];
		this.downf = new boolean[this.map.getECount()];
		this.reversef = new boolean[this.map.getECount()];

		for (int i = 0; i < this.map.getCol(); ++i) {
			for (int j = 0; j < this.map.getRow(); ++j) {
				switch (this.map.getMap(j, i)) {
				case 69:
					this.enemyX[index] = map.tilesToWidthPixels(i);
					this.enemyY[index] = map.tilesToHeightPixels(j);
					this.vx[index] = -3;
					this.vy[index] = 0;
					this.eType[index] = 0;
					this.bound[index] = 0;
					this.dir[index] = 1;
					this.a_count[index] = 0;
					this.stf[index] = 20;
					this.score[index] = 80;
					this.combo[index] = 1;
					this.dethf[index] = false;
					this.ew_nowTime[index] = 0L;
					this.ew_nextTime[index] = 0L;
					this.ew_timef[index] = false;
					this.downf[index] = false;
					this.reversef[index] = false;
					++index;
					break;
				case 70:
					this.enemyX[index] = map.tilesToWidthPixels(i);
					this.enemyY[index] = map.tilesToHeightPixels(j);
					this.vx[index] = 3;
					this.vy[index] = 0;
					this.eType[index] = 1;
					switch ((int) (Math.random() * 2.0D)) {
					case 0:
						this.bound[index] = -14;
						break;
					case 1:
						this.bound[index] = -12;
					}

					this.dir[index] = 0;
					this.a_count[index] = 0;
					this.stf[index] = 20;
					this.score[index] = 110;
					this.combo[index] = 1;
					this.dethf[index] = false;
					this.ew_nowTime[index] = 0L;
					this.ew_nextTime[index] = 0L;
					this.ew_timef[index] = false;
					this.downf[index] = false;
					this.reversef[index] = false;
					++index;
				}
			}
		}

	}

	public void draw(long elapsedTime, GLEx g, int offsetX, int offsetY) {
		if (timer.action(elapsedTime)) {
			for (int e = 0; e < Enemy.this.map.getECount(); ++e) {
				if (Enemy.this.stf[e] == 10) {
					Enemy.this.a_count[e] = 0;
				} else if (Enemy.this.stf[e] == 40) {
					Enemy.this.a_count[e] = 0;
				} else if (Enemy.this.a_count[e] == 0
						&& Enemy.this.stf[e] == 20) {
					Enemy.this.a_count[e] = 1;
				} else if (Enemy.this.stf[e] == 20) {
					Enemy.this.a_count[e] = 0;
				} else if (Enemy.this.stf[e] == 30) {
					Enemy.this.a_count[e] = 0;
				}
			}
		}
		for (int i = 0; i < this.map.getECount(); ++i) {
			if (this.enemyX[i] > 0) {
				if (this.reversef[i]) {
					switch (this.eType[i]) {
					case 0:
						g.drawTexture(this.enemyImg, this.enemyX[i] + offsetX,
								this.enemyY[i] + offsetY, 32, 32,
								this.a_count[i] * 20 + 20,
								this.dir[i] * 20 + 20, this.a_count[i] * 20,
								this.dir[i] * 20);
						break;
					case 1:
						g.drawTexture(this.enemyImg_F,
								this.enemyX[i] + offsetX, this.enemyY[i]
										+ offsetY, 32, 32,
								this.a_count[i] * 20 + 20,
								this.dir[i] * 20 + 20, this.a_count[i] * 20,
								this.dir[i] * 20);
					}
				} else {
					switch (this.eType[i]) {
					case 0:
						g.drawTexture(this.enemyImg, this.enemyX[i] + offsetX,
								this.enemyY[i] + offsetY, 32, 32,
								this.a_count[i] * 20, this.dir[i] * 20,
								this.a_count[i] * 20 + 20,
								this.dir[i] * 20 + 20);
						break;
					case 1:
						g.drawTexture(this.enemyImg_F,
								this.enemyX[i] + offsetX, this.enemyY[i]
										+ offsetY, 32, 32,
								this.a_count[i] * 20, this.dir[i] * 20,
								this.a_count[i] * 20 + 20,
								this.dir[i] * 20 + 20);
					}
				}
			}
		}

	}

	public void drawScore(GLEx g, int offsetX, int offsetY) {
		for (int i = 0; i < this.map.getECount(); ++i) {
			if (this.dethf[i]) {
				g.setColor(GLColor.white);
				g.setFont(LFont.getFont("Dialog", 0, 10));
				if (this.score[i] * this.combo[i] > 1000) {
					g.drawString("1000", this.enemyX[i] + offsetX + 10,
							this.enemyY[i] + offsetY);
				} else {
					g.drawString("" + this.score[i] * this.combo[i],
							this.enemyX[i] + offsetX + 10, this.enemyY[i]
									+ offsetY);
				}
			}
		}

	}

	public boolean collideWith(Player player, int a) {
		RectBox rectEnemy = new RectBox(this.enemyX[a], this.enemyY[a], 22,
				32);
		RectBox rectPlayer = new RectBox(player.getX(), player.getY(), 20,
				32);
		return rectEnemy.intersects(rectPlayer);
	}

	public int getX(int i) {
		return this.enemyX[i];
	}

	public int getY(int i) {
		return this.enemyY[i];
	}

	public int getType(int i) {
		return this.eType[i];
	}

	public boolean getDethf(int i) {
		return this.dethf[i];
	}

	public int getScore(int i) {
		return this.score[i];
	}

	public void del(int i) {
		this.downf[i] = false;
		this.reversef[i] = false;
		this.enemyX[i] = -50;
		this.enemyY[i] = -50;
	}

}
