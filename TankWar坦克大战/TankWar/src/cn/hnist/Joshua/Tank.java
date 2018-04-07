package cn.hnist.Joshua;

//download by http://www.codefans.net
import java.awt.*;
import java.util.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

abstract class Tank
{
	boolean init = false; // 第一次画时将图片载入内存
	static final Toolkit TLK = Toolkit.getDefaultToolkit(); // 用来将图片读入Image数组
	public static Image[] turretImg =
	{ TLK.getImage(Tank.class.getClassLoader().getResource("Image/Tank/turret1.png")), TLK.getImage(Tank.class.getClassLoader().getResource("Image/Tank/turret2.png")) };

	BufferedImage bTurretImg = null;
	Graphics2D g2 = null;
	AffineTransform trans = new AffineTransform();

	public static final int SHOTSCOUNT = 5; // 同时可发射的子弹数
	int shotsCount = 0; // 已经发射的子弹数
	public static final int TANKSPEED = 5; // Tank移动速度
	public static final int TANKWIDTH = 36; // Tank的尺寸
	public static final int TANKHEIGHT = 38;

	public static enum TANK_DIR
	{
		U, D, L, R, STOP
	};

	TANK_DIR tankDir; // Tank主体的方向
	TANK_DIR moveDir; // Tank的运动方向
	boolean camp; // Tank的阵营
	boolean isNPC; // 是否是有电脑控制的机器人Tank
	boolean isLive = true; // Tank是否存活
	int x, y; // Tank主体的中心点坐标
	int oldx, oldy; // 存储Tank前一步的坐标
	int turretDirx, turretDiry; // 大炮端点的坐标
	public static final int TURRETLENGTH = 30; // 炮筒的长度

	double oldAngel = 0;
	double nowAngel;
	double turnAngel;

	TankClient tc = null; // 主界面类的引用
	Bomb tankBomb = null;
	HitPoint tankHitPoint; // Tank的生命值
	static Random rn = new Random(); // 随机数发生器
	public static final Image[] TANKBODY1 =
	{ TLK.getImage(Tank.class.getClassLoader().getResource("Image/Tank/bodyu1.PNG")), TLK.getImage(Tank.class.getClassLoader().getResource("Image/Tank/bodyd1.PNG")), TLK.getImage(Tank.class.getClassLoader().getResource("Image/Tank/bodyl1.PNG")), TLK.getImage(Tank.class.getClassLoader().getResource("Image/Tank/bodyr1.PNG")) };
	public static final Image[] TANKBODY2 =
	{ TLK.getImage(Tank.class.getClassLoader().getResource("Image/Tank/bodyu2.PNG")), TLK.getImage(Tank.class.getClassLoader().getResource("Image/Tank/bodyd2.PNG")), TLK.getImage(Tank.class.getClassLoader().getResource("Image/Tank/bodyl2.PNG")), TLK.getImage(Tank.class.getClassLoader().getResource("Image/Tank/bodyr2.PNG")) };
	static Map<TANK_DIR, Image> imgMap1 = new HashMap<TANK_DIR, Image>();
	static Map<TANK_DIR, Image> imgMap2 = new HashMap<TANK_DIR, Image>();
	static
	{
		imgMap1.put(TANK_DIR.U, TANKBODY1[0]);
		imgMap1.put(TANK_DIR.D, TANKBODY1[1]);
		imgMap1.put(TANK_DIR.L, TANKBODY1[2]);
		imgMap1.put(TANK_DIR.R, TANKBODY1[3]);
		imgMap2.put(TANK_DIR.U, TANKBODY2[0]);
		imgMap2.put(TANK_DIR.D, TANKBODY2[1]);
		imgMap2.put(TANK_DIR.L, TANKBODY2[2]);
		imgMap2.put(TANK_DIR.R, TANKBODY2[3]);
	}

	public Tank(int x, int y, boolean camp, TankClient tc) // Tank初始方向随机
	{
		this.x = x;
		this.y = y;
		this.oldx = x;
		this.oldy = y;
		this.tc = tc;
		this.camp = camp;

		int i = rn.nextInt(4);
		if (0 == i)
		{
			this.tankDir = TANK_DIR.U;
		} else if (1 == i)
		{
			this.tankDir = TANK_DIR.D;
		} else if (2 == i)
		{
			this.tankDir = TANK_DIR.L;
		} else if (3 == i)
		{
			this.tankDir = TANK_DIR.R;
		}
		this.moveDir = TANK_DIR.STOP;
		this.setTurretDir(tankDir);
	}

	/**
	 * 构造函数
	 * 
	 * @param x
	 *            Tank坐标
	 * @param y
	 * @param dir
	 *            Tank方向
	 * @param camp
	 *            Tank阵营
	 * @param tc
	 *            TankClient的引用
	 */
	public Tank(int x, int y, TANK_DIR dir, boolean camp, TankClient tc) // 指明Tank初始方向
	{
		this.x = x;
		this.y = y;
		this.oldx = x;
		this.oldy = y;
		this.tc = tc;
		this.camp = camp;
		this.tankDir = dir;
		this.moveDir = TANK_DIR.STOP;
		this.setTurretDir(dir);
	}

	/**
	 * 设定Tank炮筒的方向
	 * 
	 * @param dir
	 */
	public void setTurretDir(TANK_DIR dir)
	{
		switch (dir)
		{
		case U:
		{
			turretDirx = x;
			turretDiry = y - TURRETLENGTH;
			setTurretDir(new Point(x, y - 5));
		}
			break;
		case D:
		{
			turretDirx = x;
			turretDiry = y + TURRETLENGTH;
			setTurretDir(new Point(x, y + 5));
		}
			break;
		case L:
		{
			turretDirx = x - TURRETLENGTH;
			turretDiry = y;
			setTurretDir(new Point(x - 5, y));
		}
			break;
		case R:
		{
			turretDirx = x + TURRETLENGTH;
			turretDiry = y;
			setTurretDir(new Point(x + 5, y));
		}
			break;
		default:
			break;
		}
	}

	public void setTurretDir(Point p) // 设置大炮的方向
	{
		int length = (int) Math.hypot(p.x - x, p.y - y);
		if (0 == length)
		{
			return;
		} // 若鼠标和Tank中心重合则不改变
		turretDirx = x + TURRETLENGTH * (p.x - x) / length;
		turretDiry = y + TURRETLENGTH * (p.y - y) / length;

		nowAngel = Math.atan2(x - p.x, y - p.y);

		if (oldAngel != nowAngel)
		{
			turnAngel = oldAngel - nowAngel;
			oldAngel = nowAngel;

			trans.rotate(turnAngel, 32, 32);
			bTurretImg = new BufferedImage(64, 64, BufferedImage.TYPE_4BYTE_ABGR);
			g2 = bTurretImg.createGraphics();
			g2.setTransform(trans);
			if (this.camp)
			{
				g2.drawImage(turretImg[0], null, null);
			} else
			{
				g2.drawImage(turretImg[1], null, null);
			}
		}

	}

	public void draw(Graphics g)
	{
		if (!init) // 读入炮筒图片
		{
			init = true;
			this.setTurretDir(new Point(turretDirx + 10, turretDiry + 10));
		}

		if (isLive)
		{
			if (this.camp)
			{
				switch (tankDir)
				{
				case U:
				case D:
				{
					g.drawImage(imgMap1.get(tankDir), x - 18, y - 19, null);
				}
					break;
				case L:
				case R:
				{
					g.drawImage(imgMap1.get(tankDir), x - 19, y - 18, null);
				}
					break;
				default:
				{
				}
					break;
				}
			} else
			{
				switch (tankDir)
				{
				case U:
				case D:
				{
					g.drawImage(imgMap2.get(tankDir), x - 18, y - 19, null);
				}
					break;
				case L:
				case R:
				{
					g.drawImage(imgMap2.get(tankDir), x - 19, y - 18, null);
				}
					break;
				default:
				{
				}
					break;
				}
			}

			g.drawImage(bTurretImg, x - 32, y - 32, null); // 绘制大炮

			this.tankHitPoint.draw(g); // 绘制出Tank的血量
		} else
		{
			if (tankBomb != null)
				tankBomb.draw(g);
		}

	}

	public void move() // Tank主体移动
	{
		if (this.isLive && moveDir != TANK_DIR.STOP)
		{
			tankDir = moveDir;
			switch (moveDir)
			{
			case U:
			{
				if (y > 50)
				{
					oldy = y;
					y -= TANKSPEED;
					if (isHitWall())
					{
						y = oldy;
					}
				}
			}
				break;
			case D:
			{
				if (y < TankClient.WIN_HEIGHT - 25)
				{
					oldy = y;
					y += TANKSPEED;
					if (isHitWall())
					{
						y = oldy;
					}
				}
			}
				break;
			case L:
			{
				if (x > 25)
				{
					oldx = x;
					x -= TANKSPEED;
					if (isHitWall())
					{
						x = oldx;
					}
				}
			}
				break;
			case R:
			{
				if (x < TankClient.WIN_WIDTH - 25)
				{
					oldx = x;
					x += TANKSPEED;
					if (isHitWall())
					{
						x = oldx;
					}
				}
			}
				break;
			default:
				break;
			}

			if (this instanceof UserTank)
			{
				setTurretDir(tc.mousePoint);
			} // 玩家Tank炮筒随鼠标转动
			else
			{
				setTurretDir(tankDir);
			}

			Item itemTemp = null;
			for (int i = 0; i < tc.itemList.size(); i++) // 碰到道具的处理
			{
				itemTemp = tc.itemList.get(i);
				if (this.getRect().intersects(itemTemp.getRect()))
				{
					itemTemp.eat(this);
				}
			}
		}
		this.hitDispose(); // 处理和其它Tank的碰撞
	}

	abstract void hitDispose(); // 处理和其它Tank的碰撞

	public void setMoveDir(TANK_DIR dir) // 设定Tank的运动方向
	{
		this.moveDir = dir;
	}

	public void fire()
	{
		if (shotsCount < SHOTSCOUNT)
		{
			shotsCount++;
			tc.shotsList.add(new NormalShot(this));
		}
	}

	public void dead()
	{
		isLive = false;
		tankBomb = new TankBomb(x - 64, y - 64, this);
	}

	/**
	 * 判断Tank是否撞墙
	 * 
	 * @return 撞墙返回true, 没撞返回false
	 */
	public boolean isHitWall() // Tank是否撞墙了
	{
		Wall w = null;

		for (int i = 0; i < tc.wallList.size(); i++)
		{
			w = tc.wallList.get(i);

			if (w.isHits(this.getRect()))
			{
				return true;
			}
		}
		return false;
	}

	public Rectangle getRect()
	{
		return new Rectangle(x - 19, y - 19, 38, 38);
	}

}

class RobotTank extends Tank // 电脑控制的Tank
{
	public static enum RTANKTYPE
	{
		NOR, SPE, BOSS
	}; // 机器人Tank的类型

	public static ArrayList<TANK_DIR> HITDIR = new ArrayList<TANK_DIR>(); // 处理碰撞时用到
	static
	{
		HITDIR.add(TANK_DIR.U);
		HITDIR.add(TANK_DIR.D);
		HITDIR.add(TANK_DIR.L);
		HITDIR.add(TANK_DIR.R);
		HITDIR.add(TANK_DIR.STOP);
	}
	ArrayList<TANK_DIR> hitDir = new ArrayList<TANK_DIR>(); // 存储发生碰撞的方向
	double shotdir = -1.0; // 用于中弹时记录所中炮弹的方向

	public RobotTank(int x, int y, boolean camp, TankClient tc)
	{
		super(x, y, camp, tc);
		this.isNPC = true;
		this.tankHitPoint = new HitPoint(6, this);
	}

	public static void add(int count, boolean camp, RTANKTYPE type, TankClient tc) // 添加机器人Tank
	{
		if (count > 20)
		{
			count = 20;
		} // 一次添加的数目不能多于20

		RobotTank tempTank = null;
		boolean addFlag = true;
		int randx;
		int randy;

		for (int i = 0; i < count;)
		{
			addFlag = true; // 重要,每次都要置为true,不然会陷入死循环
			randx = (Tank.rn.nextInt(15) + 3) * 40;
			randy = (Tank.rn.nextInt(15) + 3) * 30;
			if (randy < 50 || randy > TankClient.WIN_HEIGHT - 25 || randx < 25 || randx > TankClient.WIN_WIDTH - 25)
			{
				continue;
			}

			switch (type)
			{
			case NOR:
			{
				tempTank = new RobotTank(randx, randy, camp, tc);
			}
				break;
			case SPE:
			{
				tempTank = new BrainRobotTank(randx, randy, camp, tc);
			}
				break;
			default:
			{
				tempTank = new BossRobotTank(randx, randy, camp, 30, tc);
			}
				break;
			}

			for (int j = 0; j < tc.tanksList.size(); j++)
			{
				if (tempTank.getRect().intersects(tc.tanksList.get(j).getRect()))
				{
					addFlag = false;
					break;
				}
			}
			if (addFlag)
			{
				Wall w = null;

				for (int j = 0; j < tc.wallList.size(); j++) // Tank是否和墙有碰撞
				{
					w = tc.wallList.get(j);
					if (w.isHits(tempTank.getRect()))
					{
						addFlag = false;
						break;
					}
				}
			}
			if (addFlag)
			{
				tc.tanksList.add(tempTank);
				i++;
			}
		}
	}

	public void hitDispose() // 处理碰撞
	{
		boolean hitFlag = false;
		Tank tempTank;
		hitDir.addAll(0, HITDIR); // 碰撞后接下来可移动的方向
		for (int j = 0; j < tc.tanksList.size(); j++) // 碰撞检测
		{
			tempTank = tc.tanksList.get(j);
			if (tempTank == this || !tempTank.isLive)
			{
				continue;
			} // Tank是自己或对方是死Tank则不操作

			if (this.getRect().intersects(tempTank.getRect())) // 和别的Tank碰撞
			{
				switch (moveDir)
				{
				case U:
				case D:
				{
					y = oldy;
				}
					break;
				case L:
				case R:
				{
					x = oldx;
				}
					break;
				default:
					break;
				}
				this.setMoveDir(TANK_DIR.STOP);
				tempTank.setMoveDir(TANK_DIR.STOP); // 将对方Tank停下来

				if (hitDir.contains(this.moveDir)) // 不再向发生碰撞的方向走
				{
					hitFlag = true;
					hitDir.remove(this.moveDir);
				}
				if (camp != tempTank.camp) // 碰到不同阵营的Tank,都减血
				{
					tempTank.tankHitPoint.cutsHitPoint(1);
					this.tankHitPoint.cutsHitPoint(1);
				}
			}
		}
		if (hitFlag) // 若发生碰撞,则从新选择一个新方向
		{
			hitDir.add(TANK_DIR.STOP);
			if (hitDir.size() > 0)
			{
				setMoveDir(hitDir.get(Tank.rn.nextInt(hitDir.size())));
			}
		}
	}

	public void autoAction()
	{
		int notMoveDir1 = -1, notMoveDir2 = -1;
		if (this.moveDir != TANK_DIR.STOP) // 若Tank在边界上并且是向着边界运动,就不让它继续撞了
		{
			if (x + 26 > TankClient.WIN_WIDTH)
			{
				notMoveDir1 = 0;
			} else if (x - 30 < 0)
			{
				notMoveDir1 = 1;
			}
			if (y + 25 > TankClient.WIN_HEIGHT)
			{
				notMoveDir2 = 2;
			} else if (y - 50 < 0)
			{
				notMoveDir2 = 3;
			}
		}

		if (notMoveDir1 != -1 || notMoveDir2 != -1)
		{
			int change = 0;
			while (change == notMoveDir1 || change == notMoveDir2)
			{
				change = Tank.rn.nextInt(4);
			}

			switch (change)
			{
			case 3:
			{
				setMoveDir(TANK_DIR.U);
			}
				break;
			case 2:
			{
				setMoveDir(TANK_DIR.D);
			}
				break;
			case 1:
			{
				setMoveDir(TANK_DIR.L);
			}
				break;
			case 0:
			{
				setMoveDir(TANK_DIR.R);
			}
				break;
			default:
				break;
			}
		} else
		{
			int change = Tank.rn.nextInt(30); // 随机改变方向和开火
			switch (change)
			{
			case 7:
			case 6:
			{
				fire();
			}
				break;
			case 5:
			{
				setMoveDir(Tank.TANK_DIR.U);
			}
				break;
			case 4:
			{
				setMoveDir(Tank.TANK_DIR.D);
			}
				break;
			case 3:
			{
				setMoveDir(Tank.TANK_DIR.L);
			}
				break;
			case 2:
			{
				setMoveDir(Tank.TANK_DIR.R);
			}
				break;
			case 1:
			{
				setMoveDir(Tank.TANK_DIR.STOP);
			}
				break;
			default:
				break;
			}
		}

		super.move();
	}

	public boolean isHitWall(Rectangle r) // 是否碰墙,躲避炮弹时用于辅助判断往哪个方向躲合适
	{
		Wall w = null;
		for (int i = 0; i < tc.wallList.size(); i++)
		{
			w = tc.wallList.get(i);
			if (w.isHits(r))
			{
				return true;
			}
		}
		return false;
	}

	public void avoidThrust(Shot s) // 中弹时躲避
	{
		if (shotdir != Math.abs(s.lengthY) / (Math.abs(s.lengthX) + 0.001))// 子弹角度改变时改变躲避的方向
		{
			if (Math.abs((double) s.lengthY) / (Math.abs(s.lengthX) + 0.001) > 3)
			{
				if (x + 30 > TankClient.WIN_WIDTH || isHitWall(new Rectangle(x + 20, y - 20, 10, 40)))
				{
					setMoveDir(Tank.TANK_DIR.L);
				} else if (x - 30 < 0 || isHitWall(new Rectangle(x - 30, y - 20, 10, 40)))
				{
					setMoveDir(Tank.TANK_DIR.R);
				} else
				{
					int change = Tank.rn.nextInt(2);
					switch (change)
					{
					case 1:
					{
						setMoveDir(Tank.TANK_DIR.L);
					}
						break;
					case 0:
					{
						setMoveDir(Tank.TANK_DIR.R);
					}
						break;
					default:
						break;
					}
				}
			} else if (Math.abs((double) s.lengthY) / (Math.abs(s.lengthX) + 0.001) < 0.3)
			{
				if (y + 30 > TankClient.WIN_HEIGHT || isHitWall(new Rectangle(x - 20, y + 20, 40, 10)))
				{
					setMoveDir(Tank.TANK_DIR.U);
				} else if (y - 30 < 0 || isHitWall(new Rectangle(x - 20, y - 30, 40, 10)))
				{
					setMoveDir(Tank.TANK_DIR.D);
				} else
				{
					int change = Tank.rn.nextInt(2);
					switch (change)
					{
					case 1:
					{
						setMoveDir(Tank.TANK_DIR.U);
					}
						break;
					case 0:
					{
						setMoveDir(Tank.TANK_DIR.D);
					}
						break;
					default:
						break;
					}
				}
			} else
			{
				int change = Tank.rn.nextInt(4);

				switch (change)
				{
				case 3:
				{
					setMoveDir(Tank.TANK_DIR.U);
				}
					break;
				case 2:
				{
					setMoveDir(Tank.TANK_DIR.D);
				}
					break;
				case 1:
				{
					setMoveDir(Tank.TANK_DIR.L);
				}
					break;
				case 0:
				{
					setMoveDir(Tank.TANK_DIR.R);
				}
					break;
				default:
					break;
				}
			}
			this.move();

			shotdir = Math.abs(s.lengthY) / (Math.abs(s.lengthX) + 0.001);
		}
	}

}

class BrainRobotTank extends RobotTank // 智能较高的机器人Tank
{
	int specialShotsNum = 3; // 特殊子弹的数目

	public BrainRobotTank(int x, int y, boolean camp, TankClient tc)
	{
		super(x, y, camp, tc);
		this.tankHitPoint = new HitPoint(10, this);
	}

	public void fire(boolean b) // 特殊子弹
	{
		if (b && specialShotsNum > 0)
		{
			specialShotsNum--;
			{
				tc.shotsList.add(new SpecialShot(this));
			}
		} else
		{
			fire();
		}
	}

	public void autoAction()
	{
		if (0 == Tank.rn.nextInt(50))
		{
			attackOtherCamp();
		} else
		{
			super.autoAction();
		}
	}

	public void avoidThrust(Shot s) // 躲避攻击(中弹时调用)
	{
		System.out.println("中弹,赶快躲避");
		super.avoidThrust(s);
		if (0 == Tank.rn.nextInt(10))
		{
			setTurretDir(new Point(s.tk.x, s.tk.y)); // 将炮筒对着攻击自己的Tank
			fire();
		}

	}

	public void attackOtherCamp() // 攻击敌对阵营Tank
	{
		Tank otk;
		int attackCount = 0;
		for (int i = 0; i < tc.tanksList.size(); i++)
		{
			otk = tc.tanksList.get(i);
			if (otk.isLive && otk.camp != this.camp)
			{
				attackCount++;
				if (2 == attackCount)
				{
					break;
				} // 一轮攻击共发射3枚炮弹,避免发射太多

				if (otk.moveDir != Tank.TANK_DIR.STOP) // Tank在运动,根据运动方向打提前量
				{
					int shift = (int) (Math.hypot(turretDirx - otk.x, turretDiry - otk.y) / 2.5);
					switch (otk.moveDir)
					{
					case U:
					{
						setTurretDir(new Point(otk.x, otk.y - shift));
					}
						break;
					case D:
					{
						setTurretDir(new Point(otk.x, otk.y + shift));
					}
						break;
					case L:
					{
						setTurretDir(new Point(otk.x - shift, otk.y));
					}
						break;
					case R:
					{
						setTurretDir(new Point(otk.x + shift, otk.y));
					}
						break;
					default:
						break;
					}
					fire(); // 根据玩家Tank运动方向打提前量
				} else
				{
					setTurretDir(new Point(otk.x, otk.y)); // 将炮筒对着敌对Tank
					if (Math.hypot(x - otk.x, y - otk.y) < 260)
					{
						fire(true);
					} // 距离近就发射大威力炮弹
					else
					{
						fire();
					}
				}
			}
		}
		this.move(); // 发完炮弹后移动一下
	}

}

class BossRobotTank extends BrainRobotTank // Boos机器人Tank
{
	public static int SCANRADIUS = 150; // 扫描范围
	boolean avoidAttack = false; // 躲避攻击是否开启
	int avoidStep; // 躲避攻击时的移动步数(一次移动2格)
	public static TANK_DIR[] dirArray =
	{ TANK_DIR.U, TANK_DIR.D, TANK_DIR.L, TANK_DIR.R };
	TANK_DIR[] toMoveWay =
	{ null, null, null, null };// 躲避时的移动路径
	public static int WAYSCOUNT = 10; // 存储路径的容器中最多存储的路径数
	ArrayList<TANK_DIR[]> toMoveWays = new ArrayList<TANK_DIR[]>(); // 存储多个可行的路径,供选择
	ArrayList<InScopeShot> scopeShots = new ArrayList<InScopeShot>();// 处于扫描范围内的敌方炮弹

	public BossRobotTank(int x, int y, boolean camp, int hitPointNum, TankClient tc)
	{
		super(x, y, camp, tc);
		this.tankHitPoint = new HitPoint(hitPointNum, this);
	}

	public void autoAction()
	{
		if (!avoidAttack)
		{
			if (Tank.rn.nextInt(16) == 0)
			{
				scanThrust();
			} // 定时扫描
			else
			{
				super.autoAction();
			}
		} else
		{
			if (avoidStep >= 0)
			{
				this.setMoveDir(toMoveWay[avoidStep]);
				this.move();
				this.move();
				avoidStep--;
				if (avoidStep == -1)
				{
					avoidAttack = false;
				}
			}
		}
	}

	public void avoidThrust(Shot s) // 躲避攻击(中弹时调用)
	{
		System.out.println("中弹,赶快躲避");
		super.avoidThrust(s);
		setTurretDir(new Point(s.tk.x, s.tk.y)); // 将炮筒对着攻击自己的Tank
		fire();
	}

	private void scanThrust() // 扫描一定范围内是否有炮弹攻击向自己
	{
		Shot tempShot = null;
		scopeShots.clear(); // 先清空,不然会添加到先前加入的子弹的后面
		for (int i = 0; i < tc.shotsList.size(); i++)
		{
			tempShot = tc.shotsList.get(i);
			if (tempShot.tk.camp != this.camp)
			{
				if (Math.hypot(tempShot.sx - x, tempShot.sy - y) < SCANRADIUS) // 处于扫描范围之内
				{
					scopeShots.add(new InScopeShot(tempShot.sx, tempShot.sy, tempShot.lengthX, tempShot.lengthY));
				}
			}
		}// 将扫描范围内的所有敌方炮弹存储下来

		if (scopeShots.size() == 0 || !ableHitInScope(new Rectangle(x - 20, y - 20, 40, 40)))
		{
			return;
		}// 范围内无敌方炮弹或炮弹都不能击中自己

		System.out.println("敌弹来袭!!!\n" + "子弹数:" + scopeShots.size());

		int notMoveDir1, notMoveDir2; // 根据射来的子弹方向设定第一步不能移动的方向
		InScopeShot issDir = scopeShots.get(0);
		if (Math.abs((double) issDir.dirY) / (Math.abs(issDir.dirX) + 0.001) > 3) // 竖直方向打来
		{
			notMoveDir1 = 0;
			notMoveDir2 = 1;
		} else if (Math.abs((double) issDir.dirY) / (Math.abs(issDir.dirX) + 0.001) < 0.3) // 水平方向
		{
			notMoveDir1 = 2;
			notMoveDir2 = 3;
		} else
		{
			notMoveDir1 = -1;
			notMoveDir2 = -1;
		}

		int tempX; // 存储Tank当前坐标
		int tempY;
		toMoveWays.clear(); // 先将List清空

		for (int s1 = 0; s1 < 4; s1++)
		{
			tempX = this.x;
			tempY = this.y;

			if (s1 == notMoveDir1 || s1 == notMoveDir2)
			{
				continue;
			} // 不向平行于子弹的方向走

			if (s1 == 0)
			{
				tempY -= 10;
			} else if (s1 == 1)
			{
				tempY += 10;
			} else if (s1 == 2)
			{
				tempX -= 10;
			} else if (s1 == 3)
			{
				tempX += 10;
			}
			if (!ableHitInScope(new Rectangle(tempX - 20, tempY - 20, 40, 40)) && tempY > 50 && tempY < TankClient.WIN_HEIGHT - 25 && tempX > 25 && x < TankClient.WIN_WIDTH - 25)
			{
				TANK_DIR[] tempDir =
				{ dirArray[s1] };
				toMoveWays.add(tempDir);
				if (toMoveWays.size() > WAYSCOUNT) // 只存储WAYSCOUNT路径
				{
					choiceMoveDir();
					return;
				}
			}
			for (int s2 = 0; s2 < 4; s2++)
			{
				if (s2 == notMoveDir1 || s2 == notMoveDir2)
				{
					continue;
				} // 不向平行于子弹的方向走

				if (s2 == 0)
				{
					tempY -= 10;
				} else if (s2 == 1)
				{
					tempY += 10;
				} else if (s2 == 2)
				{
					tempX -= 10;
				} else if (s2 == 3)
				{
					tempX += 10;
				}
				if (!ableHitInScope(new Rectangle(tempX - 20, tempY - 20, 40, 40)) && tempY > 50 && tempY < TankClient.WIN_HEIGHT - 25 && tempX > 25 && x < TankClient.WIN_WIDTH - 25)
				{
					TANK_DIR[] tempDir =
					{ dirArray[s2], dirArray[s1] };
					toMoveWays.add(tempDir);
					if (toMoveWays.size() > WAYSCOUNT)
					{
						choiceMoveDir();
						return;
					}
				}
				for (int s3 = 0; s3 < 4; s3++)
				{
					if (s3 == 0)
					{
						tempY -= 10;
					} else if (s3 == 1)
					{
						tempY += 10;
					} else if (s3 == 2)
					{
						tempX -= 10;
					} else if (s3 == 3)
					{
						tempX += 10;
					}
					if (!ableHitInScope(new Rectangle(tempX - 20, tempY - 20, 40, 40)) && tempY > 50 && tempY < TankClient.WIN_HEIGHT - 25 && tempX > 25 && x < TankClient.WIN_WIDTH - 25)
					{
						TANK_DIR[] tempDir =
						{ dirArray[s3], dirArray[s2], dirArray[s1] };
						toMoveWays.add(tempDir);
						if (toMoveWays.size() > WAYSCOUNT)
						{
							choiceMoveDir();
							return;
						}
					}
					for (int s4 = 0; s4 < 4; s4++)
					{
						if (s4 == 0)
						{
							tempY -= 10;
						} else if (s4 == 1)
						{
							tempY += 10;
						} else if (s4 == 2)
						{
							tempX -= 10;
						} else if (s4 == 3)
						{
							tempX += 10;
						}
						if (!ableHitInScope(new Rectangle(tempX - 20, tempY - 20, 40, 40)) && tempY > 50 && tempY < TankClient.WIN_HEIGHT - 25 && tempX > 25 && x < TankClient.WIN_WIDTH - 25)
						{
							TANK_DIR[] tempDir =
							{ dirArray[s4], dirArray[s3], dirArray[s2], dirArray[s1] };
							toMoveWays.add(tempDir);
							if (toMoveWays.size() > WAYSCOUNT)
							{
								choiceMoveDir();
								return;
							}
						}
					}
				}
			}
		}// 添加移动方向完毕

		if (toMoveWays.size() > 0)
		{
			choiceMoveDir();
		} // 有可以躲避的移动方向
		this.move();
	}

	private void choiceMoveDir() // 从多个躲避方向中选出较好的
	{
		if (toMoveWays.size() == 1) // 只有一个躲避方向
		{
			avoidAttack = true;
			toMoveWay = toMoveWays.get(0);
			avoidStep = toMoveWay.length - 1;
			return;
		}

		// 有多个躲避方向
		int endx, endy;
		TANK_DIR[] tempWay;
		for (int i = 0; i < toMoveWays.size(); i++)
		{
			endx = this.x;
			endy = this.y;
			tempWay = toMoveWays.get(i);
			for (int j = tempWay.length - 1; j >= 0; j--) // 计算按此tempDir移动到达的最远点
			{
				switch (tempWay[j])
				{
				case U:
				{
					if (Math.abs(endy - 10 - y) > Math.abs(endy - y)) // 远离Tank原点才移动
					{
						endy -= 10;
					}
				}
					break;
				case D:
				{
					if (Math.abs(endy + 10 - y) > Math.abs(endy - y))
					{
						endy += 10;
					}
				}
					break;
				case L:
				{
					if (Math.abs(endx - 10 - x) > Math.abs(endx - x))
					{
						endx -= 10;
					}
				}
					break;
				case R:
				{
					if (Math.abs(endx + 10 - x) > Math.abs(endx - x))
					{
						endx += 10;
					}
				}
					break;
				default:
					break;
				}
			}

			if (!isHitWall(new Rectangle(endx - 20, endy - 20, 40, 40))) // 最远点是否撞墙
			{
				toMoveWay = tempWay;
				avoidAttack = true; // 躲避攻击开启
				avoidStep = tempWay.length - 1; // 移动步数
				return;
			}
		}

		avoidAttack = false; // 遍历了List,没有好的方向,不躲了
	}

	private boolean ableHitInScope(Rectangle tkRect)// 在范围内的子弹是否会击中Tank
	{
		double length;
		double step;
		int mx, my;
		InScopeShot iss;
		for (int i = 0; i < scopeShots.size(); i++)
		{
			iss = scopeShots.get(i);

			length = Math.hypot(iss.x - this.x, iss.y - this.y); // 子弹距离Tank的距离
			step = length / Math.hypot(iss.dirX, iss.dirY); // 移动length距离要移动的步数
			mx = iss.x + (int) (iss.dirX * step); // 将子弹移动length距离
			my = iss.y + (int) (iss.dirY * step);

			if (tkRect.intersects(new Rectangle(mx - 5, my - 5, 10, 10)))// 相交表示会击中
			{
				return true;
			}
		}

		return false;
	}

	private class InScopeShot // 在范围内的敌方子弹类
	{
		int x, y; // 坐标
		int dirX, dirY; // 每次移动的步长

		public InScopeShot(int x, int y, int dirX, int dirY)
		{
			this.x = x;
			this.y = y;
			this.dirX = dirX;
			this.dirY = dirY;
		}
	}

}

class UserTank extends Tank // 由玩家自主控制的Tank
{
	public static int HITPOINT = 30; // 玩家Tank的血量
	int hydraShotsNum = 60; // 特殊子弹的数目
	int superShotsNum = 5; // 超级炮弹的数目
	LinkedList<TANK_DIR> keyList = new LinkedList<TANK_DIR>(); // 存储用户所按的方向键
	static final Image SHOTS[] =
	{ TLK.getImage(Tank.class.getClassLoader().getResource("Image/Tank/ammo1.png")), TLK.getImage(Tank.class.getClassLoader().getResource("Image/Tank/ammo2.png")) };

	public UserTank(int x, int y, TANK_DIR dir, boolean camp, TankClient tc)
	{
		super(x, y, dir, camp, tc);
		this.isNPC = false;
		this.tankHitPoint = new HitPoint(HITPOINT, this);
		keyList.add(TANK_DIR.STOP);
	}

	public void draw(Graphics g)
	{
		super.draw(g);
		showAmmo(g);
	}

	public void fire(boolean b) // 散弹
	{
		if (b && hydraShotsNum > 0)
		{
			hydraShotsNum--;
			{
				HydraShot();
			}
		} else
		{
			fire();
		}
	}

	private void HydraShot()
	{
		double range = Math.atan2(turretDirx - x, turretDiry - y);
		int dx1 = (int) (x + 20 * Math.sin(range + 0.15));
		int dy1 = (int) (y + 20 * Math.cos(range + 0.15));
		int dx2 = (int) (x + 20 * Math.sin(range - 0.15));
		int dy2 = (int) (y + 20 * Math.cos(range - 0.15));
		tc.shotsList.add(new HydraShot(x, y, dx1, dy1, this));
		tc.shotsList.add(new HydraShot(x, y, dx2, dy2, this));
		tc.shotsList.add(new HydraShot(x, y, turretDirx, turretDiry, this));
	}

	public void superFire()
	{
		if (superShotsNum > 0)
		{
			superShotsNum--;
			tc.shotsList.add(new SuperShot(this));
		}
	}

	public void addHydraShots()
	{
		this.hydraShotsNum++;
	}

	public void addSuperShots()
	{
		this.superShotsNum++;
	}

	public void showAmmo(Graphics g)
	{
		for (int i = 0; i < superShotsNum; i++)
		{
			g.drawImage(SHOTS[0], 15 + 10 * i, 556, null);
		}
		Color c = g.getColor();
		g.setColor(Color.white);
		g.drawImage(SHOTS[1], 15, 572, null);
		g.drawString("" + hydraShotsNum, 33, 583);
		g.setColor(c);
	}

	public void keyDispose(int Key, boolean b) // 对用户按键的处理
	{
		if (b) // 有键按下
		{
			switch (Key)
			{
			case KeyEvent.VK_UP:
			case KeyEvent.VK_W:
			{
				keyList.remove(TANK_DIR.U);
				keyList.add(TANK_DIR.U);
			}
				break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_S:
			{
				keyList.remove(TANK_DIR.D);
				keyList.add(TANK_DIR.D);
			}
				break;
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_A:
			{
				keyList.remove(TANK_DIR.L);
				keyList.add(TANK_DIR.L);
			}
				break;
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D:
			{
				keyList.remove(TANK_DIR.R);
				keyList.add(TANK_DIR.R);
			}
				break;
			case KeyEvent.VK_SPACE:
			{
				this.superFire();
			}
				break;
			default:
				break;
			}
		} else
		// 有键抬起
		{
			switch (Key)
			{
			case KeyEvent.VK_UP:
			case KeyEvent.VK_W:
			{
				keyList.remove(TANK_DIR.U);
			}
				break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_S:
			{
				keyList.remove(TANK_DIR.D);
			}
				break;
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_A:
			{
				keyList.remove(TANK_DIR.L);
			}
				break;
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D:
			{
				keyList.remove(TANK_DIR.R);
			}
				break;
			default:
				break;
			}
		}
		if (keyList.size() == 0)
		{
			keyList.add(TANK_DIR.STOP);
		}
		super.setMoveDir(keyList.get(keyList.size() - 1));
	}

	public void hitDispose()
	{
		Tank tempTank;
		for (int i = 0; i < tc.tanksList.size(); i++) // 碰撞检测
		{
			tempTank = tc.tanksList.get(i);
			if (tempTank == this || !tempTank.isLive)
			{
				continue;
			} // Tank是自己或对方是死Tank则不操作

			if (this.getRect().intersects(tempTank.getRect())) // 和别的Tank碰撞
			{
				switch (moveDir)
				{
				case U:
				case D:
				{
					y = oldy;
				}
					break;
				case L:
				case R:
				{
					x = oldx;
				}
					break;
				default:
					break;
				}
				this.setMoveDir(TANK_DIR.STOP);
				if (camp != tempTank.camp) // 碰到不同阵营的Tank,都减血
				{
					tempTank.tankHitPoint.cutsHitPoint(1);
					this.tankHitPoint.cutsHitPoint(1);
				}
			}
		}
	}

	public void rebirth() // 复活
	{
		this.isLive = true;
		this.keyList.clear();
		this.setMoveDir(TANK_DIR.STOP);
		this.tankHitPoint.raisesHitPoint(UserTank.HITPOINT); // 回满血
	}
}
