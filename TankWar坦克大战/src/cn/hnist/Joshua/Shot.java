package cn.hnist.Joshua;

import java.awt.*;

abstract class Shot
{
	int shotSpeed; // 子弹移动速度
	int shotRadius; // 子弹大小(半径)
	int shotPower; // 子弹威力
	int ox, oy, sx, sy; // o表示Tank的中心坐标,s表示炮弹坐标(初始时为炮筒端点)
	int lengthX, lengthY; // 子弹每次移动的步长
	boolean isLive;
	boolean isBomb; // 死时是否产生爆炸
	TankClient tc = null;
	Tank tk = null;
	ShotBomb shotBomb = null;
	public static final Toolkit TLK = Toolkit.getDefaultToolkit();

	public Shot(int ox, int oy, int sx, int sy, Tank tk)
	{
		this.ox = ox;
		this.oy = oy;
		this.sx = sx;
		this.sy = sy;
		this.tc = tk.tc;
		this.tk = tk;
		this.isLive = true;
	}

	public Shot(Tank tk)
	{
		this.ox = tk.x;
		this.oy = tk.y;
		this.sx = tk.turretDirx;
		this.sy = tk.turretDiry;
		this.tc = tk.tc;
		this.tk = tk;
		this.isLive = true;
	}

	abstract void draw(Graphics g);

	abstract void move();

	abstract void dead();

	public boolean isOutOfWindow() // 检查子弹是否跃出边界
	{
		if (sx > TankClient.WIN_WIDTH || sx < 0 || sy > TankClient.WIN_HEIGHT || sy < 0)
		{
			this.isBomb = false;
			return true;
		} else
		{
			return false;
		}
	}

	public Rectangle getRect() // 碰撞检测时用于判断
	{
		return new Rectangle(sx - shotRadius, sy - shotRadius, shotRadius * 2, shotRadius * 2);
	}

}

class HydraShot extends Shot
{
	static final Image HYDRASHOT = TLK.getImage(Shot.class.getClassLoader().getResource("Image/Shot/Hydra.png"));

	public HydraShot(int ox, int oy, int sx, int sy, Tank tk)
	{
		super(ox, oy, sx, sy, tk);
		this.shotPower = 1;
		this.shotRadius = 6;
		this.shotSpeed = 13;
		this.lengthX = shotSpeed * (sx - ox) / (int) Math.hypot(sx - ox, sy - oy);
		this.lengthY = shotSpeed * (sy - oy) / (int) Math.hypot(sx - ox, sy - oy);
	}

	public void draw(Graphics g)
	{
		if (isLive)
		{
			g.drawImage(HYDRASHOT, sx - shotRadius, sy - shotRadius, null);
		} else
		{
			if (isBomb && shotBomb != null)
			{
				shotBomb.draw(g);
			}
		}
	}

	public boolean isHitWall()
	{
		Wall w = null;

		for (int i = 0; i < tc.wallList.size(); i++)
		{
			w = tc.wallList.get(i);
			if (w.isHits(this))
			{
				this.isBomb = true;
				return true; // 散弹不可穿墙
			}
		}
		return false;
	}

	public boolean isHitTank() // 检查炮弹是否击中对方Tank
	{
		Tank tempTk = null;
		for (int i = 0; i < tc.tanksList.size(); i++)
		{
			tempTk = tc.tanksList.get(i);

			if (tempTk.isLive && getRect().intersects(tempTk.getRect()) && tempTk.camp != tk.camp)
			{
				tempTk.tankHitPoint.cutsHitPoint(shotPower, this); // 减少一些血
				this.isBomb = true;
				return true;

			}
		}
		return false; // 未打中任何Tank
	}

	public void dead()
	{
		if (isLive)
		{
			this.isLive = false;
			if (isBomb)
			{
				shotBomb = new ShotBomb(sx - 32, sy - 32, this);
			} // 当撞到墙或Tank时子弹才爆炸
			else
			{
				tc.shotsList.remove(this);
			}
		}
	}

	public void move()
	{
		if (isLive)
		{
			sx += lengthX;
			sy += lengthY;

			if (isOutOfWindow() || isHitWall() || isHitTank())
			{
				this.dead();
			}
		}
	}
}

class NormalShot extends Shot // 普通炮弹
{
	static final Image[] NORSHOT =
	{ TLK.getImage(Shot.class.getClassLoader().getResource("Image/Shot/NOR1.png")), TLK.getImage(Shot.class.getClassLoader().getResource("Image/Shot/NOR2.png")) };

	public NormalShot(Tank tk)
	{
		super(tk);
		this.shotPower = 1;
		this.shotRadius = 6;
		this.shotSpeed = 12;
		this.lengthX = shotSpeed * (sx - ox) / (int) Math.hypot(sx - ox, sy - oy);
		this.lengthY = shotSpeed * (sy - oy) / (int) Math.hypot(sx - ox, sy - oy);
	}

	public void draw(Graphics g)
	{
		if (isLive)
		{
			if (tk.camp)
			{
				g.drawImage(NORSHOT[0], sx - shotRadius, sy - shotRadius, null);
			} else
			{
				g.drawImage(NORSHOT[1], sx - shotRadius, sy - shotRadius, null);
			}
		} else
		{
			if (isBomb && shotBomb != null)
			{
				shotBomb.draw(g);
			}
		}
	}

	public boolean isHitWall()
	{
		Wall w = null;

		for (int i = 0; i < tc.wallList.size(); i++)
		{
			w = tc.wallList.get(i);
			if (w.isHits(this))
			{
				this.isBomb = true;
				return true; // 普通子弹不可穿墙
			}
		}
		return false;
	}

	public boolean isHitTank() // 检查炮弹是否击中对方Tank
	{
		Tank tempTk = null;
		for (int i = 0; i < tc.tanksList.size(); i++)
		{
			tempTk = tc.tanksList.get(i);

			if (tempTk.isLive && getRect().intersects(tempTk.getRect()) && tempTk.camp != tk.camp)
			{
				tempTk.tankHitPoint.cutsHitPoint(shotPower, this); // 减少一些血
				this.isBomb = true;
				return true;

			}
		}
		return false; // 未打中任何Tank
	}

	public void dead()
	{
		if (isLive)
		{
			this.isLive = false;
			tk.shotsCount--; // 将Tank已发射的子弹数减1
			if (isBomb)
			{
				shotBomb = new ShotBomb(sx - 32, sy - 32, this);
			} // 当撞到墙或Tank时子弹才爆炸
			else
			{
				tc.shotsList.remove(this);
			}
		}
	}

	public void move()
	{
		if (isLive)
		{
			sx += lengthX;
			sy += lengthY;

			if (isOutOfWindow() || isHitWall() || isHitTank())
			{
				this.dead();
			}
		}
	}
}

class SpecialShot extends Shot // 增强的炮弹
{
	int hitCount; // 子弹可以攻击的次数
	static final Image SPESHOT = TLK.getImage(Shot.class.getClassLoader().getResource("Image/Shot/SPE.png"));

	public SpecialShot(Tank tk)
	{
		super(tk);
		this.hitCount = 2;
		this.shotPower = 2;
		this.shotRadius = 6;
		this.shotSpeed = 10;
		this.lengthX = shotSpeed * (sx - ox) / (int) Math.hypot(sx - ox, sy - oy);
		this.lengthY = shotSpeed * (sy - oy) / (int) Math.hypot(sx - ox, sy - oy);
	}

	public boolean isHitTank() // 检查炮弹是否击中Tank
	{
		Tank tempTk = null;
		for (int i = 0; i < tc.tanksList.size(); i++)
		{
			tempTk = tc.tanksList.get(i);

			if (getRect().intersects(tempTk.getRect()) & tempTk.camp != tk.camp)// 自己发射的子弹不打自己人
			{
				if (tempTk.isLive)
				{
					tempTk.tankHitPoint.cutsHitPoint(shotPower, this); // 减少一些血
					this.hitCount--;
					if (0 == hitCount)
					{
						this.isBomb = true;
						return true;
					}
				}
			}
		}
		return false; // 未打中任何Tank
	}

	public void dead()
	{
		if (isLive)
		{
			this.isLive = false;

			if (isBomb)
			{
				shotBomb = new ShotBomb(sx - 32, sy - 32, this);
			} // 当撞到墙或Tank时子弹才爆炸
			else
			{
				this.tc.shotsList.remove(this); // 将子弹从List中移除
			}
		}
	}

	void move()
	{
		if (isLive)
		{
			sx += lengthX;
			sy += lengthY;

			if (isOutOfWindow() || isHitTank())
			{
				this.dead();
			}
		}
	}

	void draw(Graphics g)
	{
		if (isLive)
		{
			g.drawImage(SPESHOT, sx - shotRadius, sy - shotRadius, null);
		} else
		{
			if (isBomb && shotBomb != null)
			{
				shotBomb.draw(g);
			}
		}
	}

}

class SuperShot extends Shot // 超级炮弹
{
	int oldx, oldy; // 起始坐标
	static final int MOVERANGE = 150; // 发射后移动此距离后爆炸
	static final Image SUPERSHOT = TLK.getImage(Shot.class.getClassLoader().getResource("Image/Shot/SUPER.png"));

	public SuperShot(Tank tk)
	{
		super(tk);
		this.shotPower = 5;
		this.shotRadius = 4;
		this.shotSpeed = 8;
		this.oldx = sx;
		this.oldy = sy;
		this.lengthX = shotSpeed * (sx - ox) / (int) Math.hypot(sx - ox, sy - oy);
		this.lengthY = shotSpeed * (sy - oy) / (int) Math.hypot(sx - ox, sy - oy);
	}

	public void dead()
	{
		if (isLive)
		{
			this.isLive = false;
			if (isBomb)
			{
				shotBomb = new SuperShotBomb(sx, sy, this);
			} else
			{
				this.tc.shotsList.remove(this);
			}
		}
	}

	void move()
	{
		if (isLive)
		{
			sx += lengthX;
			sy += lengthY;

			if (isOutOfWindow())
			{
				this.dead();
			}

			if ((int) Math.hypot(sx - oldx, sy - oldy) > MOVERANGE) // 移动一定距离后爆炸
			{
				this.isBomb = true;
				this.dead();
			}
		}
	}

	void draw(Graphics g)
	{
		if (isLive)
		{
			g.drawImage(SUPERSHOT, sx - shotRadius, sy - shotRadius, null);
		} else
		{
			if (isBomb && shotBomb != null)
			{
				shotBomb.draw(g);
			}
		}
	}
}
