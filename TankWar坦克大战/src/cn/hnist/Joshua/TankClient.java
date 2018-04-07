package cn.hnist.Joshua;

//download by http://www.codefans.net
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class TankClient extends Frame
{
	public static final int WIN_WIDTH = 800;
	public static final int WIN_HEIGHT = 600;
	boolean repaintFlag = true; // 控制重画线程的结束
	Image bkImage = null; // 用于双缓冲的缓存图片
	Point mousePoint = new Point(600, 500);

	UserTank ut = new UserTank(600, 520, Tank.TANK_DIR.L, true, this); // 玩家控制的Tank

	List<Shot> shotsList = Collections.synchronizedList(new ArrayList<Shot>());// 存储界面上的子弹
	List<Tank> tanksList = Collections.synchronizedList(new ArrayList<Tank>());// 存储界面上的Tank
	List<Wall> wallList = Collections.synchronizedList(new ArrayList<Wall>());// 界面上的障碍物
	List<Item> itemList = Collections.synchronizedList(new ArrayList<Item>());// 界面上的道具

	public static void main(String[] args)
	{
		TankClient tc = new TankClient();
		tc.lunchFrame();
	}

	void lunchFrame() // 界面初始化
	{
		tanksList.add(ut);

		this.wallList.add(new Wall(200, 160, Wall.WALLTYPE.SEXSYMBOL, this));
		// this.wallList.add(new Wall(200,200,2,25,this)); //添加障碍物
		// this.wallList.add(new Wall(20,400,2,20,this)); //添加障碍物
		// this.wallList.add(new Wall(450,400,2,20,this)); //添加障碍物
		RobotTank.add(4, false, RobotTank.RTANKTYPE.BOSS, this); // 添加机器人Tank
		RobotTank.add(3, true, RobotTank.RTANKTYPE.BOSS, this); // 添加机器人Tank

		this.itemList.add(new HitPointItem(700, 500, 3, this));// 添加一个涨血的道具
		this.itemList.add(new ShotsItem(700, 520, 1, this)); // 超级炮弹的道具
		this.itemList.add(new ShotsItem(700, 550, 0, this)); // 特殊炮弹的道具

		this.setLocation(200, 100);
		this.setSize(WIN_WIDTH, WIN_HEIGHT);
		this.setTitle("TankWar");
		this.setResizable(false); // 不能改变窗口大小
		this.setBackground(Color.black);

		this.addWindowListener(new FrameClose());
		this.addKeyListener(new TankMoveLis());
		this.addMouseMotionListener(new MouseMoveLis());
		this.addMouseListener(new MouseCleckLis()); // Tank开火监听者
		new Thread(new RepaintThread()).start(); // 定时重画的线程
		new Thread(new RobotTanksThread(this)).start();

		this.setVisible(true);
	}

	class FrameClose extends WindowAdapter // 窗口关闭事件监听者
	{
		public void windowClosing(WindowEvent e)
		{
			Frame f = (Frame) e.getWindow();
			repaintFlag = false;
			f.dispose();
		}
	}

	public void paint(Graphics g) // synchronized
	{
		// if(!ut.isLive) { ut.isLive=true; ut.tankHitPoint.number=5; }
		// else
		{
			Wall tcWall = null;
			for (int i = 0; i < wallList.size(); i++)
			{
				tcWall = wallList.get(i);
				tcWall.draw(g);
			}
			Tank tcTanks = null;
			for (int i = tanksList.size() - 1; i >= 0; i--)
			{
				tcTanks = tanksList.get(i);
				tcTanks.draw(g);
			}
			Item tcItems = null;
			for (int i = 0; i < itemList.size(); i++)
			{
				tcItems = itemList.get(i);
				tcItems.draw(g);
			}
			Shot tcShots = null;
			for (int i = 0; i < shotsList.size(); i++)
			{
				tcShots = shotsList.get(i);
				tcShots.draw(g);
			}
		}
	}

	public void update(Graphics g)
	{
		if (null == bkImage)
		{
			bkImage = this.createImage(WIN_WIDTH, WIN_HEIGHT);
		}

		g.drawImage(bkImage, 0, 0, null); // 把虚拟图片的内容画到当前窗口

		Graphics gBkImg = bkImage.getGraphics();
		gBkImg.clearRect(0, 0, WIN_WIDTH, WIN_HEIGHT); // 先清空图片
		this.paint(gBkImg); // 将图像先画到虚拟图片上
	}

	class RepaintThread implements Runnable // 画面重画线程
	{
		public void run()
		{
			while (repaintFlag)
			{
				try
				{
					repaint();
					ut.move(); // Tank定时移动

					Shot tcShots = null;
					for (int i = 0; i < shotsList.size(); i++) // 界面上的子弹定时移动
					{
						tcShots = shotsList.get(i);
						tcShots.move();
					}

					Thread.sleep(33);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
					System.exit(-1);
				}
			}
		}
	}

	class RobotTanksThread implements Runnable // 机器人Tank的行动线程
	{
		TankClient tc;

		public RobotTanksThread(TankClient tc)
		{
			this.tc = tc;
		}

		public void run()
		{
			Tank tcTanks = null;
			int count = -1;

			while (repaintFlag)
			{
				if (0 == count) // 电脑Tank全灭后重新加几辆
				{
					// RobotTank.add(1,false,RobotTank.RTANKTYPE.BOSS,tc);
					RobotTank.add(2, false, RobotTank.RTANKTYPE.SPE, tc);
				}

				try
				{
					count = 0;

					for (int i = 0; i < tanksList.size(); i++)
					{
						tcTanks = tanksList.get(i);
						if (!tcTanks.camp)
						{
							count++;
						}

						if (!tcTanks.isNPC || !tcTanks.isLive)
						{
							continue;
						} // 不控制玩家的Tank

						((RobotTank) tcTanks).autoAction(); // 自主行动
					}

					Thread.sleep(38);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
					System.exit(-1);
				}
			}
		}
	}

	class TankMoveLis extends KeyAdapter // 玩家Tank按键监听者
	{
		public void keyPressed(KeyEvent e)
		{
			if (ut.isLive)
			{
				ut.keyDispose(e.getKeyCode(), true);
			}
		}

		public void keyReleased(KeyEvent e)
		{
			if (ut.isLive)
			{
				ut.keyDispose(e.getKeyCode(), false);
			} else
			{
				if (e.getKeyCode() == KeyEvent.VK_F2)
				{
					ut.rebirth(); // 复活
				}
			}
		}
	}

	class MouseMoveLis extends MouseMotionAdapter // 鼠标移动监听者
	{
		public void mouseMoved(MouseEvent e)
		{
			if (ut.isLive)
			{
				mousePoint = e.getPoint();
				ut.setTurretDir(mousePoint); // 根据鼠标位置更新炮筒方向
			}
		}
	}

	class MouseCleckLis extends MouseAdapter // 点击鼠标,玩家Tank发射炮弹
	{
		public void mouseClicked(MouseEvent e)
		{
			if (ut.isLive)
			{
				ut.fire(MouseEvent.BUTTON3 == e.getButton()); // 按鼠标右键会发射特殊子弹
			}
		}
	}
}
