package org.loon.act.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.geom.RectBox;
import org.loon.framework.android.game.core.geom.Vector2f;
import org.loon.framework.android.game.core.graphics.opengl.GL;
import org.loon.framework.android.game.core.graphics.opengl.GLEx;
import org.loon.framework.android.game.core.graphics.opengl.LTexture;
import org.loon.framework.android.game.core.resource.Resources;
import org.loon.framework.android.game.core.timer.LTimer;

public class Map {

	final static class Coin {

		private int firstCoinX;

		private int lastCoinX;

		private int firstCoinY;

		private int lastCoinY;

		private int count;

		protected int width;

		protected int height;

		private LTexture coinImg;

		private Map map;

		private LTimer timer = new LTimer(200);

		public Coin(LTexture img, Map map) {
			this.map = map;
			this.coinImg = img;
			this.count = 0;
		}

		public void draw(long elapsedTime, GLEx g, int offsetX, int offsetY) {
			if (timer.action(elapsedTime)) {
				if (Coin.this.count == 0) {
					Coin.this.count = 1;
				} else if (Coin.this.count == 1) {
					Coin.this.count = 2;
				} else if (Coin.this.count == 2) {
					Coin.this.count = 0;
				}
			}
			this.firstCoinX = map.pixelsToTilesWidth((double) (-offsetX));
			this.lastCoinX = this.firstCoinX
					+ map.pixelsToTilesWidth(LSystem.screenRect.width) + 1;
			this.lastCoinX = Math.min(this.lastCoinX, this.map.getCol());
			this.firstCoinY = map.pixelsToTilesHeight((double) (-offsetY));
			this.lastCoinY = this.firstCoinY
					+ map.pixelsToTilesHeight(LSystem.screenRect.height) + 1;
			this.lastCoinY = Math.min(this.lastCoinY, this.map.getRow());

			for (int i = this.firstCoinY; i < this.lastCoinY; ++i) {
				int j = this.firstCoinX;

				while (j < this.lastCoinX) {
					switch (this.map.getMap(i, j)) {
					case 111:
						g.drawJavaTexture(this.coinImg, map
								.tilesToWidthPixels(j)
								+ offsetX,
								map.tilesToHeightPixels(i) + offsetY, map
										.tilesToWidthPixels(j)
										+ offsetX + 32, map
										.tilesToHeightPixels(i)
										+ offsetY + 32, this.count * 32, 0,
								this.count * 32 + 32, 32);
					default:
						++j;
					}
				}
			}

		}

		public boolean collideWith(Player player, int i, int j) {
			switch (this.map.getMap(i, j)) {
			case 111:
				RectBox rectCoin = new RectBox(map.tilesToHeightPixels(j), map
						.tilesToWidthPixels(i), 25, 32);
				RectBox rectPlayer = new RectBox(player.getX(), player.getY(),
						20, 32);
				if (rectCoin.intersects(rectPlayer)) {
					return true;
				}
			default:
				return false;
			}
		}

		public int getFirstX() {
			return this.firstCoinX;
		}

		public int getLastX() {
			return this.lastCoinX;
		}

		public int getFirstY() {
			return this.firstCoinY;
		}

		public int getLastY() {
			return this.lastCoinY;
		}

		public void del(int i, int j) {
			this.map.setMap(i, j);
		}

	}

	final static class Item {

		private int[] itemX;

		private int[] itemY;

		private int[] vy;

		private boolean[] getf;

		private LTexture itemImg;

		private RectBox rectItem, rectPlayer;

		private Map map;

		public Item(LTexture img, Map map) {
			this.map = map;
			this.load();
			this.itemImg = img;
		}

		public void update(int t) {
			if (this.map.getCount(t) == 1) {
				this.vy[t] = (int) ((double) this.vy[t] + 1.0D);
				this.itemY[t] += this.vy[t];
				int newY = this.itemY[t] + this.vy[t];
				Vector2f tile = this.map.getTileCollision(this,
						(double) this.itemX[t], (double) newY, t);
				if (tile == null) {
					this.itemY[t] = newY;
				} else if (this.vy[t] > 0) {
					this.itemY[t] = map.tilesToHeightPixels(tile.y())
							- map.getTileHeight();
					this.vy[t] = 0;
				} else if (this.vy[t] < 0) {
					this.itemY[t] = map.tilesToHeightPixels(tile.y() + 1);
					this.vy[t] = 0;
				}
			}

		}

		public void draw(GLEx g, int offsetX, int offsetY) {
			itemImg.glBegin();
			for (int i = 0; i < this.map.getICount(); ++i) {
				if (this.itemX[i] > 0 && this.map.getCount(i) > 0) {
					itemImg.draw(this.itemX[i] + offsetX, this.itemY[i]
							+ offsetY);
				}
			}
			itemImg.glEnd();
		}

		public boolean collideWith(Player player, int i) {
			if (rectItem == null) {
				rectItem = new RectBox(this.itemX[i], this.itemY[i], 22, 32);
			} else {
				rectItem.setBounds(this.itemX[i], this.itemY[i], 22, 32);
			}
			if (rectPlayer == null) {
				rectPlayer = new RectBox(player.getX(), player.getY(), 20, 3);
			} else {
				rectPlayer.setBounds(player.getX(), player.getY(), 20, 3);
			}
			return rectItem.intersects(rectPlayer);
		}

		private void load() {
			int a = 0;
			this.itemX = new int[this.map.getICount()];
			this.itemY = new int[this.map.getICount()];
			this.vy = new int[this.map.getICount()];
			this.getf = new boolean[this.map.getICount()];

			for (int i = 0; i < this.map.getRow(); ++i) {
				int j = 0;

				while (j < this.map.getCol()) {
					switch (this.map.getMap(i, j)) {
					case 56:
						this.itemX[a] = map.tilesToWidthPixels(j);
						this.itemY[a] = map.tilesToHeightPixels(i);
						this.getf[a] = false;
						this.vy[a] = -7;
						++a;
					default:
						++j;
					}
				}
			}

		}

		public int getX(int a) {
			return this.itemX[a];
		}

		public int getY(int a) {
			return this.itemY[a];
		}

		public void del(int a) {
			this.getf[a] = true;
			this.itemX[a] = -50;
			this.itemY[a] = -50;
		}
	}

	public static final double GRAVITY = 1.0D;

	private LTexture blockImg;

	private LTexture togeImg;

	private LTexture iboxImg;

	private LTexture ashibaImg;

	private LTexture goalImg;

	private LTexture halfImg;

	private LTexture kusaImg;

	private LTexture tuchiImg;

	private char[][] map;

	private int[] iboxX;

	private int[] iboxY;

	private int[] count;

	private int iCount;

	private int iboxP;

	private int eCount;

	private int row;

	private int col;

	private int width;

	private int height;

	private boolean stickf;

	private boolean clearf;

	private boolean jump1f;

	private boolean jump2f;

	private boolean blockf;

	private boolean outf;

	private int tileWidth, tileHeight;

	public Map(LTexture block, LTexture toge, LTexture ibox, LTexture ashiba,
			LTexture goal, LTexture half, LTexture kusa, LTexture tuchi,
			String filename) {

		this.load(filename);
		this.blockImg = block;
		this.togeImg = toge;
		this.iboxImg = ibox;
		this.ashibaImg = ashiba;
		this.goalImg = goal;
		this.halfImg = half;
		this.kusaImg = kusa;
		this.tuchiImg = tuchi;
		this.iboxP = 0;
		this.stickf = false;
		this.clearf = false;
		this.jump1f = false;
		this.jump2f = true;
		this.blockf = false;
		this.outf = false;
	}

	public void setCount(int a) {
		this.count[a] = 1;
	}

	public void setMap(int i, int j) {
		this.map[i][j] = 32;
	}

	public void setClearf() {
		this.clearf = false;
	}

	public void setClick(boolean hit) {
		this.jump2f = hit;
	}

	public void countReset() {
		for (int a = 0; a < this.iCount; ++a) {
			this.count[a] = 0;
		}

	}

	public void draw(GLEx g, int offsetX, int offsetY) {
		int firstTileX = pixelsToTilesWidth((double) (-offsetX));

		int lastTileX = firstTileX
				+ pixelsToTilesWidth(LSystem.screenRect.width) + 1;

		lastTileX = Math.min(lastTileX, this.col);

		int firstTileY = pixelsToTilesHeight((double) (-offsetY));

		int lastTileY = firstTileY
				+ pixelsToTilesHeight(LSystem.screenRect.height) + 1;

		lastTileY = Math.min(lastTileY, this.row);

		int old = g.getBlendMode();
		g.setBlendMode(GL.MODE_SPEED);
		int i;
		for (i = firstTileY; i < lastTileY; ++i) {
			for (int j = firstTileX; j < lastTileX; ++j) {
				switch (this.map[i][j]) {
				case 45:
					g.drawTexture(this.blockImg, tilesToWidthPixels(j)
							+ offsetX, tilesToHeightPixels(i) + offsetY);
					break;
				case 48:
					g.drawTexture(this.tuchiImg, tilesToWidthPixels(j)
							+ offsetX, tilesToHeightPixels(i) + offsetY);
					break;
				case 65:
					g.drawTexture(this.togeImg,
							tilesToWidthPixels(j) + offsetX,
							tilesToHeightPixels(i) + offsetY);
					break;
				case 66:
					g.drawTexture(this.blockImg, tilesToWidthPixels(j)
							+ offsetX, tilesToHeightPixels(i) + offsetY);
					break;
				case 67:
					g.drawTexture(this.blockImg, tilesToWidthPixels(j)
							+ offsetX, tilesToHeightPixels(i) + offsetY);
					break;
				case 68:
					g.drawTexture(this.tuchiImg, tilesToWidthPixels(j)
							+ offsetX, tilesToHeightPixels(i) + offsetY);
					break;
				case 71:
					g.drawTexture(this.goalImg,
							tilesToWidthPixels(j) + offsetX,
							tilesToHeightPixels(i) + offsetY);
					break;
				case 72:
					g.drawTexture(this.halfImg,
							tilesToWidthPixels(j) + offsetX,
							tilesToHeightPixels(i) + offsetY);
					break;
				case 84:
					g.drawTexture(this.kusaImg,
							tilesToWidthPixels(j) + offsetX,
							tilesToHeightPixels(i) + offsetY);
					break;
				case 88:
					g.drawTexture(this.ashibaImg, tilesToWidthPixels(j)
							+ offsetX, tilesToHeightPixels(i) + offsetY);
					break;
				case 89:
					g.drawTexture(this.kusaImg,
							tilesToWidthPixels(j) + offsetX,
							tilesToHeightPixels(i) + offsetY);
				}
			}
		}
		g.setBlendMode(old);
		iboxImg.glBegin();
		for (i = 0; i < this.iCount; ++i) {
			iboxImg.draw(this.iboxX[i] + offsetX, this.iboxY[i] + offsetY,
					tileWidth, tileHeight, this.count[i] * tileWidth, 0,
					this.count[i] * tileWidth + tileWidth, tileHeight);
		}
		iboxImg.glEnd();
	}

	public Vector2f getTileCollision(Player player, double newX, double newY) {
		this.iboxP = -1;
		this.stickf = false;
		this.jump1f = false;
		this.blockf = false;
		this.outf = false;
		newX = Math.ceil(newX);
		newY = Math.ceil(newY);
		double fromX = Math.min((double) player.getX(), newX);
		double fromY = Math.min((double) player.getY(), newY);
		double toX = Math.max((double) player.getX(), newX);
		double toY = Math.max((double) player.getY(), newY);
		int fromTileX = pixelsToTilesWidth(fromX);

		int fromTileY = pixelsToTilesHeight(fromY);

		int toTileX = pixelsToTilesWidth(toX + tileWidth - 1.0D);

		int toTileY = pixelsToTilesHeight(toY + tileHeight - 1.0D);

		for (int x = fromTileX; x <= toTileX; ++x) {
			int y = fromTileY;

			while (y <= toTileY) {
				if (x >= 0 && x < this.col) {
					if (y < 0) {
						return new Vector2f(x, y);
					}

					if (y >= this.row) {
						this.outf = true;
						return new Vector2f(x, y);
					}

					if (this.map[y][x] != 66 && this.map[y][x] != 84
							&& this.map[y][x] != 48) {
						if (this.jump2f
								&& (this.map[y][x] == 88 || this.map[y][x] == 89)) {
							this.jump1f = true;
							return new Vector2f(x, y);
						}

						for (int i = 0; i < this.iCount; ++i) {
							if (y == pixelsToTilesHeight((double) this.iboxY[i])
									&& x == pixelsToTilesWidth((double) this.iboxX[i])) {
								this.blockf = true;
								this.iboxP = i;
								return new Vector2f(x, y);
							}
						}

						if (this.map[y][x] == 65) {
							this.blockf = true;
							this.stickf = true;
							return new Vector2f(x, y);
						}

						if (this.map[y][x] == 72) {
							ACTWavSound.getInstance().center();
							player.setHalf(x, y);
							this.map[y][x] = 32;
						}

						if (this.map[y][x] == 71) {
							this.clearf = true;
							this.map[y][x] = 32;
						}

						++y;
						continue;
					}

					this.blockf = true;
					return new Vector2f(x, y);
				}

				return new Vector2f(x, y);
			}
		}

		return null;
	}

	public Vector2f getTileCollision(Enemy enemy, double newX, double newY,
			int a, int eType) {
		newX = Math.ceil(newX);
		newY = Math.ceil(newY);
		double fromX = Math.min((double) enemy.getX(a), newX);
		double fromY = Math.min((double) enemy.getY(a), newY);
		double toX = Math.max((double) enemy.getX(a), newX);
		double toY = Math.max((double) enemy.getY(a), newY);
		int fromTileX = pixelsToTilesWidth(fromX);

		int fromTileY = pixelsToTilesHeight(fromY);

		int toTileX = pixelsToTilesWidth(toX + tileWidth - 1.0D);

		int toTileY = pixelsToTilesHeight(toY + tileHeight - 1.0D);

		for (int x = fromTileX; x <= toTileX; ++x) {
			int y = fromTileY;

			while (y <= toTileY) {
				if (x >= 0 && x < this.col) {
					if (y < 0) {
						return new Vector2f(x, y);
					}

					if (y >= this.row) {
						enemy.del(a);
						return new Vector2f(x, y);
					}

					if (this.map[y][x] != 66 && this.map[y][x] != 88
							&& this.map[y][x] != 73 && this.map[y][x] != 84
							&& this.map[y][x] != 48 && this.map[y][x] != 89) {
						if (this.map[y][x] == 124) {
							switch (eType) {
							case 0:
								return new Vector2f(x, y);
							}
						}

						if (this.map[y][x] == 108) {
							switch (eType) {
							case 1:
								return new Vector2f(x, y);
							}
						}

						if (this.map[y][x] == 45) {
							return new Vector2f(x, y);
						}

						if (this.map[y][x] == 65) {
							return new Vector2f(x, y);
						}

						++y;
						continue;
					}

					return new Vector2f(x, y);
				}

				return new Vector2f(x, y);
			}
		}

		return null;
	}

	public Vector2f getTileCollision(Item item, double newX, double newY, int a) {
		newX = Math.ceil(newX);
		newY = Math.ceil(newY);
		double fromX = Math.min((double) item.getX(a), newX);
		double fromY = Math.min((double) item.getY(a), newY);
		double toX = Math.max((double) item.getX(a), newX);
		double toY = Math.max((double) item.getY(a), newY);
		int fromTileX = pixelsToTilesWidth(fromX);

		int fromTileY = pixelsToTilesHeight(fromY);

		int toTileX = pixelsToTilesWidth(toX + tileWidth - 1.0D);

		int toTileY = pixelsToTilesHeight(toY + tileHeight - 1.0D);

		for (int x = fromTileX; x <= toTileX; ++x) {
			int y = fromTileY;

			while (y <= toTileY) {
				if (x >= 0 && x < this.col) {
					if (y < 0) {
						return new Vector2f(x, y);
					}

					if (y >= this.row) {
						this.stickf = true;
						return new Vector2f(x, y);
					}

					if (this.map[y][x] != 66 && this.map[y][x] != 88
							&& this.map[y][x] != 73) {
						if (this.map[y][x] == 65) {
							return new Vector2f(x, y);
						}

						++y;
						continue;
					}

					return new Vector2f(x, y);
				}

				return new Vector2f(x, y);
			}
		}

		return null;
	}

	public int tilesToWidthPixels(int tiles) {
		return tiles * tileWidth;
	}

	public int tilesToHeightPixels(int tiles) {
		return tiles * tileHeight;
	}

	public int pixelsToTilesWidth(double x) {
		return (int) Math.floor(x / tileWidth);
	}

	public int pixelsToTilesHeight(double y) {
		return (int) Math.floor(y / tileHeight);
	}

	private void load(String filename) {
		try {
			tileWidth = 32;
			tileHeight = 32;
			InputStreamReader e = new InputStreamReader(Resources
					.getResourceAsStream("assets/maps/" + filename));
			BufferedReader br = new BufferedReader(e);
			this.row = Integer.parseInt(br.readLine());
			this.col = Integer.parseInt(br.readLine());
			this.eCount = Integer.parseInt(br.readLine());
			this.iCount = Integer.parseInt(br.readLine());
			int a = 0;
			this.map = new char[this.row][this.col];
			this.iboxX = new int[this.iCount];
			this.iboxY = new int[this.iCount];
			this.count = new int[this.iCount];

			for (int i = 0; i < this.row; ++i) {
				String str = br.readLine();
				int j = 0;

				while (j < this.col) {
					this.map[i][j] = str.charAt(j);
					switch (this.map[i][j]) {
					case 73:
						this.iboxX[a] = tilesToWidthPixels(j);
						this.iboxY[a] = tilesToHeightPixels(i);
						this.count[a] = 0;
						++a;
					default:
						++j;
					}
				}
			}
		} catch (Exception var8) {
			var8.printStackTrace();
		}

		this.width = tileWidth * this.col;
		this.height = tileHeight * this.row;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public int getIbox() {
		return this.iboxP;
	}

	public boolean getStickf() {
		return this.stickf;
	}

	public boolean getJumpf() {
		return this.jump1f;
	}

	public boolean getBlockf() {
		return this.blockf;
	}

	public int getCount(int a) {
		return this.count[a];
	}

	public char getMap(int i, int j) {
		return this.map[i][j];
	}

	public int getECount() {
		return this.eCount;
	}

	public int getRow() {
		return this.row;
	}

	public int getCol() {
		return this.col;
	}

	public boolean getClear() {
		return this.clearf;
	}

	public int getICount() {
		return this.iCount;
	}

	public int getIboxY(int a) {
		return this.iboxY[a];
	}

	public int getIboxX(int a) {
		return this.iboxX[a];
	}

	public boolean getFoutf() {
		return this.outf;
	}

	public int getTileHeight() {
		return tileHeight;
	}

	public int getTileWidth() {
		return tileWidth;
	}

}
