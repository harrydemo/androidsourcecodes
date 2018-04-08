package cn.hnist.Joshua;

//download by http://www.codefans.net
import java.awt.*;

abstract class Bomb
{
	int x, y;
	int step = 0;
	static final Toolkit TLK = Toolkit.getDefaultToolkit();

	public Bomb(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	abstract void draw(Graphics g);
}

class TankBomb extends Bomb
{
	Tank tk;
	public static final Image[] TBIMGS =
	{ TLK.getImage(Bomb.class.getClassLoader().getResource("Image/TankBomb/1.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/TankBomb/2.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/TankBomb/3.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/TankBomb/4.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/TankBomb/5.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/TankBomb/6.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/TankBomb/7.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/TankBomb/8.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/TankBomb/9.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/TankBomb/10.png")),
			TLK.getImage(Bomb.class.getClassLoader().getResource("Image/TankBomb/11.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/TankBomb/12.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/TankBomb/13.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/TankBomb/14.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/TankBomb/15.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/TankBomb/16.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/TankBomb/17.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/TankBomb/18.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/TankBomb/19.png")),
			TLK.getImage(Bomb.class.getClassLoader().getResource("Image/TankBomb/20.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/TankBomb/21.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/TankBomb/22.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/TankBomb/23.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/TankBomb/24.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/TankBomb/25.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/TankBomb/26.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/TankBomb/27.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/TankBomb/28.png")),
			TLK.getImage(Bomb.class.getClassLoader().getResource("Image/TankBomb/29.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/TankBomb/30.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/TankBomb/31.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/TankBomb/32.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/TankBomb/33.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/TankBomb/34.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/TankBomb/35.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/TankBomb/36.png")) };
	static boolean init = false; // 第一次画时将图片载入内存

	public TankBomb(int x, int y, Tank tk)
	{
		super(x, y);
		this.tk = tk;
	}

	void draw(Graphics g)
	{
		if (!init)
		{
			for (int i = 0; i < TBIMGS.length; i++)
			{
				g.drawImage(TBIMGS[i], x, y, null);
				init = true;
			}
		}

		if (step == TBIMGS.length) // 爆炸完成
		{
			tk.tankBomb = null;

			if (tk.isNPC)
			{
				tk.tc.tanksList.remove(tk); // 将机器人Tank从tankList中去除
				tk = null;
			}
			return;
		}
		g.drawImage(TBIMGS[step], x, y, null);
		step++;
	}
}

class ShotBomb extends Bomb
{
	Shot s;
	private static final Image[] SBIMGS =
	{ TLK.getImage(Bomb.class.getClassLoader().getResource("Image/ShotBomb/1.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/ShotBomb/2.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/ShotBomb/3.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/ShotBomb/4.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/ShotBomb/5.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/ShotBomb/6.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/ShotBomb/7.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/ShotBomb/8.png")) };
	static boolean init = false; // 第一次画是将图片载入内存

	public ShotBomb(int x, int y, Shot s)
	{
		super(x, y);
		this.s = s;
	}

	void draw(Graphics g)
	{
		if (!init)
		{
			for (int i = 0; i < SBIMGS.length; i++)
			{
				g.drawImage(SBIMGS[i], x, y, null);
				init = true;
			}
		}

		if (step == SBIMGS.length) // 爆炸完成
		{
			s.tc.shotsList.remove(s);
			s.shotBomb = null; // 清除垃圾
			s = null;
			return;
		}

		g.drawImage(SBIMGS[step], x, y, null);
		step++;

	}
}

class SuperShotBomb extends ShotBomb
{
	public static final Image[] SUPERBOMBIMGS =
	{ TLK.getImage(Bomb.class.getClassLoader().getResource("Image/ShotBomb/SUPER/1.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/ShotBomb/SUPER/2.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/ShotBomb/SUPER/3.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/ShotBomb/SUPER/4.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/ShotBomb/SUPER/5.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/ShotBomb/SUPER/6.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/ShotBomb/SUPER/7.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/ShotBomb/SUPER/8.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/ShotBomb/SUPER/9.png")),
			TLK.getImage(Bomb.class.getClassLoader().getResource("Image/ShotBomb/SUPER/10.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/ShotBomb/SUPER/11.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/ShotBomb/SUPER/12.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/ShotBomb/SUPER/13.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/ShotBomb/SUPER/14.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/ShotBomb/SUPER/15.png")), TLK.getImage(Bomb.class.getClassLoader().getResource("Image/ShotBomb/SUPER/16.png")) };
	static boolean init = false; // 第一次画是将图片载入内存

	public SuperShotBomb(int x, int y, Shot s)
	{
		super(x, y, s);
	}

	public Rectangle getRect() // 碰撞检测时用于判断
	{
		return new Rectangle(x - 80, y - 80, 160, 160);
	}

	void draw(Graphics g)
	{
		if (!init)
		{
			for (int i = 0; i < SUPERBOMBIMGS.length; i++)
			{
				g.drawImage(SUPERBOMBIMGS[i], x, y, null);
				init = true;
			}
		}

		if (step == SUPERBOMBIMGS.length) // 爆炸完成
		{
			s.tc.shotsList.remove(s);
			s.shotBomb = null; // 清除垃圾
			s = null;
			return;
		}

		g.drawImage(SUPERBOMBIMGS[step], x - 128, y - 128, null);

		if (step == SUPERBOMBIMGS.length / 2 + 1) // 爆炸到最大时碰撞检测
		{
			Tank tempTk = null;
			for (int i = 0; i < s.tc.tanksList.size(); i++)
			{
				tempTk = s.tc.tanksList.get(i);

				if (getRect().intersects(tempTk.getRect()) && tempTk.isLive && tempTk.camp != s.tk.camp)// 自己发射的子弹不打自己人
				{
					tempTk.tankHitPoint.cutsHitPoint(s.shotPower, this.s); // 减少一些血
				}
			}

			Shot tcShots = null;
			for (int i = 0; i < s.tc.shotsList.size(); i++)
			{
				tcShots = s.tc.shotsList.get(i);

				if (getRect().contains(tcShots.getRect()) && tcShots.tk.camp != s.tk.camp)
				{
					tcShots.isBomb = false; // 不产生爆炸
					tcShots.dead(); // 碰到超级炮弹的敌对阵营的子弹死亡
				}
			}
		}

		step++;

	}

}
