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
	boolean repaintFlag = true; // �����ػ��̵߳Ľ���
	Image bkImage = null; // ����˫����Ļ���ͼƬ
	Point mousePoint = new Point(600, 500);

	UserTank ut = new UserTank(600, 520, Tank.TANK_DIR.L, true, this); // ��ҿ��Ƶ�Tank

	List<Shot> shotsList = Collections.synchronizedList(new ArrayList<Shot>());// �洢�����ϵ��ӵ�
	List<Tank> tanksList = Collections.synchronizedList(new ArrayList<Tank>());// �洢�����ϵ�Tank
	List<Wall> wallList = Collections.synchronizedList(new ArrayList<Wall>());// �����ϵ��ϰ���
	List<Item> itemList = Collections.synchronizedList(new ArrayList<Item>());// �����ϵĵ���

	public static void main(String[] args)
	{
		TankClient tc = new TankClient();
		tc.lunchFrame();
	}

	void lunchFrame() // �����ʼ��
	{
		tanksList.add(ut);

		this.wallList.add(new Wall(200, 160, Wall.WALLTYPE.SEXSYMBOL, this));
		// this.wallList.add(new Wall(200,200,2,25,this)); //����ϰ���
		// this.wallList.add(new Wall(20,400,2,20,this)); //����ϰ���
		// this.wallList.add(new Wall(450,400,2,20,this)); //����ϰ���
		RobotTank.add(4, false, RobotTank.RTANKTYPE.BOSS, this); // ��ӻ�����Tank
		RobotTank.add(3, true, RobotTank.RTANKTYPE.BOSS, this); // ��ӻ�����Tank

		this.itemList.add(new HitPointItem(700, 500, 3, this));// ���һ����Ѫ�ĵ���
		this.itemList.add(new ShotsItem(700, 520, 1, this)); // �����ڵ��ĵ���
		this.itemList.add(new ShotsItem(700, 550, 0, this)); // �����ڵ��ĵ���

		this.setLocation(200, 100);
		this.setSize(WIN_WIDTH, WIN_HEIGHT);
		this.setTitle("TankWar");
		this.setResizable(false); // ���ܸı䴰�ڴ�С
		this.setBackground(Color.black);

		this.addWindowListener(new FrameClose());
		this.addKeyListener(new TankMoveLis());
		this.addMouseMotionListener(new MouseMoveLis());
		this.addMouseListener(new MouseCleckLis()); // Tank���������
		new Thread(new RepaintThread()).start(); // ��ʱ�ػ����߳�
		new Thread(new RobotTanksThread(this)).start();

		this.setVisible(true);
	}

	class FrameClose extends WindowAdapter // ���ڹر��¼�������
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

		g.drawImage(bkImage, 0, 0, null); // ������ͼƬ�����ݻ�����ǰ����

		Graphics gBkImg = bkImage.getGraphics();
		gBkImg.clearRect(0, 0, WIN_WIDTH, WIN_HEIGHT); // �����ͼƬ
		this.paint(gBkImg); // ��ͼ���Ȼ�������ͼƬ��
	}

	class RepaintThread implements Runnable // �����ػ��߳�
	{
		public void run()
		{
			while (repaintFlag)
			{
				try
				{
					repaint();
					ut.move(); // Tank��ʱ�ƶ�

					Shot tcShots = null;
					for (int i = 0; i < shotsList.size(); i++) // �����ϵ��ӵ���ʱ�ƶ�
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

	class RobotTanksThread implements Runnable // ������Tank���ж��߳�
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
				if (0 == count) // ����Tankȫ������¼Ӽ���
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
						} // ��������ҵ�Tank

						((RobotTank) tcTanks).autoAction(); // �����ж�
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

	class TankMoveLis extends KeyAdapter // ���Tank����������
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
					ut.rebirth(); // ����
				}
			}
		}
	}

	class MouseMoveLis extends MouseMotionAdapter // ����ƶ�������
	{
		public void mouseMoved(MouseEvent e)
		{
			if (ut.isLive)
			{
				mousePoint = e.getPoint();
				ut.setTurretDir(mousePoint); // �������λ�ø�����Ͳ����
			}
		}
	}

	class MouseCleckLis extends MouseAdapter // ������,���Tank�����ڵ�
	{
		public void mouseClicked(MouseEvent e)
		{
			if (ut.isLive)
			{
				ut.fire(MouseEvent.BUTTON3 == e.getButton()); // ������Ҽ��ᷢ�������ӵ�
			}
		}
	}
}
