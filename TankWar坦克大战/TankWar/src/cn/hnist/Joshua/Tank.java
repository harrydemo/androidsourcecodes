package cn.hnist.Joshua;

//download by http://www.codefans.net
import java.awt.*;
import java.util.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

abstract class Tank
{
	boolean init = false; // ��һ�λ�ʱ��ͼƬ�����ڴ�
	static final Toolkit TLK = Toolkit.getDefaultToolkit(); // ������ͼƬ����Image����
	public static Image[] turretImg =
	{ TLK.getImage(Tank.class.getClassLoader().getResource("Image/Tank/turret1.png")), TLK.getImage(Tank.class.getClassLoader().getResource("Image/Tank/turret2.png")) };

	BufferedImage bTurretImg = null;
	Graphics2D g2 = null;
	AffineTransform trans = new AffineTransform();

	public static final int SHOTSCOUNT = 5; // ͬʱ�ɷ�����ӵ���
	int shotsCount = 0; // �Ѿ�������ӵ���
	public static final int TANKSPEED = 5; // Tank�ƶ��ٶ�
	public static final int TANKWIDTH = 36; // Tank�ĳߴ�
	public static final int TANKHEIGHT = 38;

	public static enum TANK_DIR
	{
		U, D, L, R, STOP
	};

	TANK_DIR tankDir; // Tank����ķ���
	TANK_DIR moveDir; // Tank���˶�����
	boolean camp; // Tank����Ӫ
	boolean isNPC; // �Ƿ����е��Կ��ƵĻ�����Tank
	boolean isLive = true; // Tank�Ƿ���
	int x, y; // Tank��������ĵ�����
	int oldx, oldy; // �洢Tankǰһ��������
	int turretDirx, turretDiry; // ���ڶ˵������
	public static final int TURRETLENGTH = 30; // ��Ͳ�ĳ���

	double oldAngel = 0;
	double nowAngel;
	double turnAngel;

	TankClient tc = null; // �������������
	Bomb tankBomb = null;
	HitPoint tankHitPoint; // Tank������ֵ
	static Random rn = new Random(); // �����������
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

	public Tank(int x, int y, boolean camp, TankClient tc) // Tank��ʼ�������
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
	 * ���캯��
	 * 
	 * @param x
	 *            Tank����
	 * @param y
	 * @param dir
	 *            Tank����
	 * @param camp
	 *            Tank��Ӫ
	 * @param tc
	 *            TankClient������
	 */
	public Tank(int x, int y, TANK_DIR dir, boolean camp, TankClient tc) // ָ��Tank��ʼ����
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
	 * �趨Tank��Ͳ�ķ���
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

	public void setTurretDir(Point p) // ���ô��ڵķ���
	{
		int length = (int) Math.hypot(p.x - x, p.y - y);
		if (0 == length)
		{
			return;
		} // ������Tank�����غ��򲻸ı�
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
		if (!init) // ������ͲͼƬ
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

			g.drawImage(bTurretImg, x - 32, y - 32, null); // ���ƴ���

			this.tankHitPoint.draw(g); // ���Ƴ�Tank��Ѫ��
		} else
		{
			if (tankBomb != null)
				tankBomb.draw(g);
		}

	}

	public void move() // Tank�����ƶ�
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
			} // ���Tank��Ͳ�����ת��
			else
			{
				setTurretDir(tankDir);
			}

			Item itemTemp = null;
			for (int i = 0; i < tc.itemList.size(); i++) // �������ߵĴ���
			{
				itemTemp = tc.itemList.get(i);
				if (this.getRect().intersects(itemTemp.getRect()))
				{
					itemTemp.eat(this);
				}
			}
		}
		this.hitDispose(); // ���������Tank����ײ
	}

	abstract void hitDispose(); // ���������Tank����ײ

	public void setMoveDir(TANK_DIR dir) // �趨Tank���˶�����
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
	 * �ж�Tank�Ƿ�ײǽ
	 * 
	 * @return ײǽ����true, ûײ����false
	 */
	public boolean isHitWall() // Tank�Ƿ�ײǽ��
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

class RobotTank extends Tank // ���Կ��Ƶ�Tank
{
	public static enum RTANKTYPE
	{
		NOR, SPE, BOSS
	}; // ������Tank������

	public static ArrayList<TANK_DIR> HITDIR = new ArrayList<TANK_DIR>(); // ������ײʱ�õ�
	static
	{
		HITDIR.add(TANK_DIR.U);
		HITDIR.add(TANK_DIR.D);
		HITDIR.add(TANK_DIR.L);
		HITDIR.add(TANK_DIR.R);
		HITDIR.add(TANK_DIR.STOP);
	}
	ArrayList<TANK_DIR> hitDir = new ArrayList<TANK_DIR>(); // �洢������ײ�ķ���
	double shotdir = -1.0; // �����е�ʱ��¼�����ڵ��ķ���

	public RobotTank(int x, int y, boolean camp, TankClient tc)
	{
		super(x, y, camp, tc);
		this.isNPC = true;
		this.tankHitPoint = new HitPoint(6, this);
	}

	public static void add(int count, boolean camp, RTANKTYPE type, TankClient tc) // ��ӻ�����Tank
	{
		if (count > 20)
		{
			count = 20;
		} // һ����ӵ���Ŀ���ܶ���20

		RobotTank tempTank = null;
		boolean addFlag = true;
		int randx;
		int randy;

		for (int i = 0; i < count;)
		{
			addFlag = true; // ��Ҫ,ÿ�ζ�Ҫ��Ϊtrue,��Ȼ��������ѭ��
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

				for (int j = 0; j < tc.wallList.size(); j++) // Tank�Ƿ��ǽ����ײ
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

	public void hitDispose() // ������ײ
	{
		boolean hitFlag = false;
		Tank tempTank;
		hitDir.addAll(0, HITDIR); // ��ײ����������ƶ��ķ���
		for (int j = 0; j < tc.tanksList.size(); j++) // ��ײ���
		{
			tempTank = tc.tanksList.get(j);
			if (tempTank == this || !tempTank.isLive)
			{
				continue;
			} // Tank���Լ���Է�����Tank�򲻲���

			if (this.getRect().intersects(tempTank.getRect())) // �ͱ��Tank��ײ
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
				tempTank.setMoveDir(TANK_DIR.STOP); // ���Է�Tankͣ����

				if (hitDir.contains(this.moveDir)) // ����������ײ�ķ�����
				{
					hitFlag = true;
					hitDir.remove(this.moveDir);
				}
				if (camp != tempTank.camp) // ������ͬ��Ӫ��Tank,����Ѫ
				{
					tempTank.tankHitPoint.cutsHitPoint(1);
					this.tankHitPoint.cutsHitPoint(1);
				}
			}
		}
		if (hitFlag) // ��������ײ,�����ѡ��һ���·���
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
		if (this.moveDir != TANK_DIR.STOP) // ��Tank�ڱ߽��ϲ��������ű߽��˶�,�Ͳ���������ײ��
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
			int change = Tank.rn.nextInt(30); // ����ı䷽��Ϳ���
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

	public boolean isHitWall(Rectangle r) // �Ƿ���ǽ,����ڵ�ʱ���ڸ����ж����ĸ���������
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

	public void avoidThrust(Shot s) // �е�ʱ���
	{
		if (shotdir != Math.abs(s.lengthY) / (Math.abs(s.lengthX) + 0.001))// �ӵ��Ƕȸı�ʱ�ı��ܵķ���
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

class BrainRobotTank extends RobotTank // ���ܽϸߵĻ�����Tank
{
	int specialShotsNum = 3; // �����ӵ�����Ŀ

	public BrainRobotTank(int x, int y, boolean camp, TankClient tc)
	{
		super(x, y, camp, tc);
		this.tankHitPoint = new HitPoint(10, this);
	}

	public void fire(boolean b) // �����ӵ�
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

	public void avoidThrust(Shot s) // ��ܹ���(�е�ʱ����)
	{
		System.out.println("�е�,�Ͽ���");
		super.avoidThrust(s);
		if (0 == Tank.rn.nextInt(10))
		{
			setTurretDir(new Point(s.tk.x, s.tk.y)); // ����Ͳ���Ź����Լ���Tank
			fire();
		}

	}

	public void attackOtherCamp() // �����ж���ӪTank
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
				} // һ�ֹ���������3ö�ڵ�,���ⷢ��̫��

				if (otk.moveDir != Tank.TANK_DIR.STOP) // Tank���˶�,�����˶��������ǰ��
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
					fire(); // �������Tank�˶��������ǰ��
				} else
				{
					setTurretDir(new Point(otk.x, otk.y)); // ����Ͳ���ŵж�Tank
					if (Math.hypot(x - otk.x, y - otk.y) < 260)
					{
						fire(true);
					} // ������ͷ���������ڵ�
					else
					{
						fire();
					}
				}
			}
		}
		this.move(); // �����ڵ����ƶ�һ��
	}

}

class BossRobotTank extends BrainRobotTank // Boos������Tank
{
	public static int SCANRADIUS = 150; // ɨ�跶Χ
	boolean avoidAttack = false; // ��ܹ����Ƿ���
	int avoidStep; // ��ܹ���ʱ���ƶ�����(һ���ƶ�2��)
	public static TANK_DIR[] dirArray =
	{ TANK_DIR.U, TANK_DIR.D, TANK_DIR.L, TANK_DIR.R };
	TANK_DIR[] toMoveWay =
	{ null, null, null, null };// ���ʱ���ƶ�·��
	public static int WAYSCOUNT = 10; // �洢·�������������洢��·����
	ArrayList<TANK_DIR[]> toMoveWays = new ArrayList<TANK_DIR[]>(); // �洢������е�·��,��ѡ��
	ArrayList<InScopeShot> scopeShots = new ArrayList<InScopeShot>();// ����ɨ�跶Χ�ڵĵз��ڵ�

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
			} // ��ʱɨ��
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

	public void avoidThrust(Shot s) // ��ܹ���(�е�ʱ����)
	{
		System.out.println("�е�,�Ͽ���");
		super.avoidThrust(s);
		setTurretDir(new Point(s.tk.x, s.tk.y)); // ����Ͳ���Ź����Լ���Tank
		fire();
	}

	private void scanThrust() // ɨ��һ����Χ���Ƿ����ڵ��������Լ�
	{
		Shot tempShot = null;
		scopeShots.clear(); // �����,��Ȼ����ӵ���ǰ������ӵ��ĺ���
		for (int i = 0; i < tc.shotsList.size(); i++)
		{
			tempShot = tc.shotsList.get(i);
			if (tempShot.tk.camp != this.camp)
			{
				if (Math.hypot(tempShot.sx - x, tempShot.sy - y) < SCANRADIUS) // ����ɨ�跶Χ֮��
				{
					scopeShots.add(new InScopeShot(tempShot.sx, tempShot.sy, tempShot.lengthX, tempShot.lengthY));
				}
			}
		}// ��ɨ�跶Χ�ڵ����ез��ڵ��洢����

		if (scopeShots.size() == 0 || !ableHitInScope(new Rectangle(x - 20, y - 20, 40, 40)))
		{
			return;
		}// ��Χ���޵з��ڵ����ڵ������ܻ����Լ�

		System.out.println("�е���Ϯ!!!\n" + "�ӵ���:" + scopeShots.size());

		int notMoveDir1, notMoveDir2; // �����������ӵ������趨��һ�������ƶ��ķ���
		InScopeShot issDir = scopeShots.get(0);
		if (Math.abs((double) issDir.dirY) / (Math.abs(issDir.dirX) + 0.001) > 3) // ��ֱ�������
		{
			notMoveDir1 = 0;
			notMoveDir2 = 1;
		} else if (Math.abs((double) issDir.dirY) / (Math.abs(issDir.dirX) + 0.001) < 0.3) // ˮƽ����
		{
			notMoveDir1 = 2;
			notMoveDir2 = 3;
		} else
		{
			notMoveDir1 = -1;
			notMoveDir2 = -1;
		}

		int tempX; // �洢Tank��ǰ����
		int tempY;
		toMoveWays.clear(); // �Ƚ�List���

		for (int s1 = 0; s1 < 4; s1++)
		{
			tempX = this.x;
			tempY = this.y;

			if (s1 == notMoveDir1 || s1 == notMoveDir2)
			{
				continue;
			} // ����ƽ�����ӵ��ķ�����

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
				if (toMoveWays.size() > WAYSCOUNT) // ֻ�洢WAYSCOUNT·��
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
				} // ����ƽ�����ӵ��ķ�����

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
		}// ����ƶ��������

		if (toMoveWays.size() > 0)
		{
			choiceMoveDir();
		} // �п��Զ�ܵ��ƶ�����
		this.move();
	}

	private void choiceMoveDir() // �Ӷ����ܷ�����ѡ���Ϻõ�
	{
		if (toMoveWays.size() == 1) // ֻ��һ����ܷ���
		{
			avoidAttack = true;
			toMoveWay = toMoveWays.get(0);
			avoidStep = toMoveWay.length - 1;
			return;
		}

		// �ж����ܷ���
		int endx, endy;
		TANK_DIR[] tempWay;
		for (int i = 0; i < toMoveWays.size(); i++)
		{
			endx = this.x;
			endy = this.y;
			tempWay = toMoveWays.get(i);
			for (int j = tempWay.length - 1; j >= 0; j--) // ���㰴��tempDir�ƶ��������Զ��
			{
				switch (tempWay[j])
				{
				case U:
				{
					if (Math.abs(endy - 10 - y) > Math.abs(endy - y)) // Զ��Tankԭ����ƶ�
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

			if (!isHitWall(new Rectangle(endx - 20, endy - 20, 40, 40))) // ��Զ���Ƿ�ײǽ
			{
				toMoveWay = tempWay;
				avoidAttack = true; // ��ܹ�������
				avoidStep = tempWay.length - 1; // �ƶ�����
				return;
			}
		}

		avoidAttack = false; // ������List,û�кõķ���,������
	}

	private boolean ableHitInScope(Rectangle tkRect)// �ڷ�Χ�ڵ��ӵ��Ƿ�����Tank
	{
		double length;
		double step;
		int mx, my;
		InScopeShot iss;
		for (int i = 0; i < scopeShots.size(); i++)
		{
			iss = scopeShots.get(i);

			length = Math.hypot(iss.x - this.x, iss.y - this.y); // �ӵ�����Tank�ľ���
			step = length / Math.hypot(iss.dirX, iss.dirY); // �ƶ�length����Ҫ�ƶ��Ĳ���
			mx = iss.x + (int) (iss.dirX * step); // ���ӵ��ƶ�length����
			my = iss.y + (int) (iss.dirY * step);

			if (tkRect.intersects(new Rectangle(mx - 5, my - 5, 10, 10)))// �ཻ��ʾ�����
			{
				return true;
			}
		}

		return false;
	}

	private class InScopeShot // �ڷ�Χ�ڵĵз��ӵ���
	{
		int x, y; // ����
		int dirX, dirY; // ÿ���ƶ��Ĳ���

		public InScopeShot(int x, int y, int dirX, int dirY)
		{
			this.x = x;
			this.y = y;
			this.dirX = dirX;
			this.dirY = dirY;
		}
	}

}

class UserTank extends Tank // ������������Ƶ�Tank
{
	public static int HITPOINT = 30; // ���Tank��Ѫ��
	int hydraShotsNum = 60; // �����ӵ�����Ŀ
	int superShotsNum = 5; // �����ڵ�����Ŀ
	LinkedList<TANK_DIR> keyList = new LinkedList<TANK_DIR>(); // �洢�û������ķ����
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

	public void fire(boolean b) // ɢ��
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

	public void keyDispose(int Key, boolean b) // ���û������Ĵ���
	{
		if (b) // �м�����
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
		// �м�̧��
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
		for (int i = 0; i < tc.tanksList.size(); i++) // ��ײ���
		{
			tempTank = tc.tanksList.get(i);
			if (tempTank == this || !tempTank.isLive)
			{
				continue;
			} // Tank���Լ���Է�����Tank�򲻲���

			if (this.getRect().intersects(tempTank.getRect())) // �ͱ��Tank��ײ
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
				if (camp != tempTank.camp) // ������ͬ��Ӫ��Tank,����Ѫ
				{
					tempTank.tankHitPoint.cutsHitPoint(1);
					this.tankHitPoint.cutsHitPoint(1);
				}
			}
		}
	}

	public void rebirth() // ����
	{
		this.isLive = true;
		this.keyList.clear();
		this.setMoveDir(TANK_DIR.STOP);
		this.tankHitPoint.raisesHitPoint(UserTank.HITPOINT); // ����Ѫ
	}
}
