package org.loon.act.test;

import org.loon.framework.android.game.core.geom.Vector2f;
import org.loon.framework.android.game.core.graphics.opengl.GLColor;
import org.loon.framework.android.game.core.graphics.opengl.GLEx;
import org.loon.framework.android.game.core.graphics.opengl.LTexture;
import org.loon.framework.android.game.core.timer.LTimer;


public class Player {

	private LTimer timer = new LTimer(200);

	private int x;

	private int y;

	private int vx;

	private int vy;

	private int halfX;

	private int halfY;

	private long nowTime;

	private long nextTime;

	private boolean timef;

	private boolean on_se;

	private boolean dashf;

	private boolean downJumpf;

	private boolean gotof;

	private boolean jumpDownf;

	private boolean jumpTwof;

	private boolean onGround;

	private boolean dethf;

	private boolean itemGetf;

	private int dir;

	private int stf;

	private int count;

	private LTexture playerImg;

	private Map map;

	public Player(int x, int y, LTexture img, Map map) {
		this.playerImg = img;

		this.x = x;
		this.y = y;
		this.map = map;
		this.vx = 0;
		this.vy = 0;
		this.halfX = x;
		this.halfY = y;
		this.nowTime = 0L;
		this.nextTime = 0L;
		this.timef = false;
		this.on_se = false;
		this.dashf = false;
		this.onGround = false;
		this.downJumpf = false;
		this.jumpTwof = false;
		this.dethf = false;
		this.gotof = false;
		this.jumpDownf = false;
		this.itemGetf = false;
		this.stf = 20;
		this.dir = 0;
		this.count = 0;
	}

	public void setItemGetf(boolean f) {
		this.itemGetf = f;
	}

	public void setSpeed() {
		this.vy = 0;
	}

	public void stop() {
		this.vx = 0;
		if (this.onGround) {
			this.stf = 20;
		}

	}

	public void leftmove() {
		if (this.dashf) {
			this.vx = -15;
		} else {
			this.vx = -8;
		}

		this.dir = 1;
		if (this.onGround) {
			this.stf = 30;
		}

	}

	public void rightmove() {
		if (this.dashf) {
			this.vx = 15;
		} else {
			this.vx = 8;
		}

		this.dir = 0;
		if (this.onGround) {
			this.stf = 30;
		}

	}

	public void jump(int typ) {
		if (typ == 0 && this.onGround) {
			ACTWavSound.getInstance().jump();
			this.stf = 10;
			if (this.dashf) {
				this.vy = -14;
			} else {
				this.vy = -13;
			}

			this.jumpTwof = true;
			this.onGround = false;
		} else if (typ == 0 && this.jumpTwof) {
			ACTWavSound.getInstance().jump();
			this.stf = 10;
			if (this.dashf) {
				this.vy = -14;
			} else {
				this.vy = -13;
			}

			this.jumpTwof = false;
		} else if (typ == 1) {
			this.stf = 10;
			if (this.dashf) {
				this.vy = -3;
			} else if (this.jumpTwof) {
				this.vy = -13;
				this.jumpTwof = false;
			} else {
				this.vy = -6;
			}
		}

	}

	public void onDown() {
		if (this.onGround) {
			if (this.map.getMap(map.pixelsToTilesHeight((double) this.y) + 1,
					map.pixelsToTilesWidth((double) this.x) + 1) != 66) {
				this.stf = 40;
				this.jumpDownf = true;
			} else {
				this.map.setClick(false);
			}
		}

	}

	public void dethJump() {
		this.vy = -20;
		this.stf = 50;
		this.dir = 2;
	}

	public void dash(boolean dash) {
		this.dashf = dash;
	}

	public void deth() {
		this.x = this.halfX;
		this.y = this.halfY;
		this.stf = 0;
		this.dir = 0;
	}

	public void setDethf(boolean f) {
		this.dethf = f;
	}

	public void setTimef(boolean f) {
		this.timef = f;
	}

	public void setNowTime(long time) {
		this.nowTime = time;
	}

	public void setNextTime(long time) {
		this.nextTime = time;
	}

	public void setHalf(int x, int y) {
		this.halfX = map.tilesToWidthPixels(x);
		this.halfY = map.tilesToHeightPixels(y);
	}

	public void update() {
		this.vy = (int) ((double) this.vy + 1.0D);
		if (this.timef) {
			this.nextTime = System.currentTimeMillis();
			if (this.nextTime - this.nowTime > 3000L) {
				this.timef = false;
			}
		}

		if (this.dethf) {
			this.y += this.vy;
		} else {
			int newX = this.x + this.vx;
			Vector2f tile = this.map.getTileCollision(this, (double) newX,
					(double) this.y);
			if (tile == null) {
				this.x = newX;
			} else if (this.map.getJumpf()) {
				this.map.setClick(false);
				this.x = newX;
			} else {
				if (this.vx > 0) {
					this.x = map.tilesToWidthPixels(tile.x())
							- map.getTileWidth();
				} else if (this.vx < 0) {
					this.x = map.tilesToWidthPixels(tile.x() + 1);
				}

				this.vx = 0;
			}

			this.dethf = false;
			int newY = this.y + this.vy;
			tile = this.map.getTileCollision(this, (double) this.x,
					(double) newY);
			if (!this.map.getFoutf()) {
				if (this.map.getMap(map.pixelsToTilesHeight((double) this.y),
						map.pixelsToTilesWidth((double) this.x)) == map
						.getTileWidth()
						&& this.map.getMap(map
								.pixelsToTilesHeight((double) this.y) + 1, map
								.pixelsToTilesWidth((double) this.x)) != 88) {
					this.map.setClick(true);
				}
			} else {
				this.dethf = true;
			}

			if (tile == null) {
				this.y = newY;
				this.onGround = false;
				this.gotof = false;
				this.on_se = false;
				if (this.stf != 10 && this.stf != 40) {
					this.stf = 20;
					if (this.downJumpf) {
						this.jumpTwof = true;
						this.downJumpf = false;
					}
				}
			} else if (this.vy > 0) {
				if (this.gotof && !this.map.getBlockf()) {
					this.y = newY;
					this.onGround = false;
				} else if (this.jumpDownf && !this.map.getBlockf()) {
					this.y = newY;
					this.onGround = false;
					this.jumpDownf = false;
				} else {
					this.map.setClick(true);
					this.y = map.tilesToHeightPixels(tile.y())
							- map.getTileHeight();
					this.vy = 0;
					this.onGround = true;
					this.downJumpf = true;
					if (!this.on_se && !this.map.getStickf()) {
						ACTWavSound.getInstance().bomb();
						this.on_se = true;
					}
				}

				if (this.map.getStickf() && !this.timef) {
					if (!this.itemGetf) {
						this.dethJump();
						this.dethf = true;
					} else {
						this.itemGetf = false;
						this.nowTime = System.currentTimeMillis();
						this.timef = true;
					}
				}
			} else if (this.vy < 0) {
				if (this.map.getBlockf()) {
					this.y = map.tilesToHeightPixels(tile.y() + 1);
					this.vy = 0;
					if (this.map.getIbox() >= 0) {
						if (this.map.getCount(this.map.getIbox()) != 1) {
							ACTWavSound.getInstance().ibox();
							this.map.setCount(this.map.getIbox());
						} else {
							ACTWavSound.getInstance().ten();
						}
					}

					this.downJumpf = false;
					this.jumpTwof = false;
				} else {
					this.map.setClick(false);
					this.y = newY;
					this.gotof = true;
					this.downJumpf = false;
				}
			}

			if (!this.onGround && this.vy > 0) {
				this.stf = 40;
			}
		}

	}

	public void draw(long elapsedTime, GLEx g, int offsetX, int offsetY) {
		if (timer.action(elapsedTime)) {
			if (Player.this.stf == 10) {
				Player.this.count = 2;
			} else if (Player.this.stf == 50) {
				Player.this.count = 0;
			} else if (Player.this.count == 1 && Player.this.stf == 20) {
				Player.this.count = 3;
			} else if (Player.this.stf == 20) {
				Player.this.count = 1;
			} else if (Player.this.count == 0 && Player.this.stf == 30) {
				Player.this.count = 1;
			} else if (Player.this.stf == 30) {
				Player.this.count = 0;
			} else if (Player.this.stf == 40) {
				Player.this.count = 4;
			}

		}

		if (this.dashf) {
			g.drawTexture(this.playerImg, this.x + offsetX, this.y + offsetY,
					32, 32, this.count * 20, this.dir * 20,
					this.count * 20 + 20, this.dir * 20 + 20, GLColor.red);
		} else {
			g.drawTexture(this.playerImg, this.x + offsetX, this.y + offsetY,
					32, 32, this.count * 20, this.dir * 20,
					this.count * 20 + 20, this.dir * 20 + 20);
		}

	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public boolean getDeth() {
		return this.dethf;
	}

	public boolean getItemGetf() {
		return this.itemGetf;
	}

	public boolean getTimef() {
		return this.timef;
	}

	public long getNowTime() {
		return this.nowTime;
	}

	public long getNextTimef() {
		return this.nextTime;
	}

	public int getVy() {
		return this.vy;
	}

}
