package cn.hnist.Joshua;

//download by http://www.codefans.net
import java.awt.*;
import java.util.ArrayList;

public class Wall
{
	static final int WIDTH = 16;
	static final int HEIGHT = 16;

	public static enum WALLTYPE
	{
		HEART, SEXSYMBOL
	};

	private static int[][] HEARTMAP =
	{
	{ 6 * WIDTH, 0 },
	{ 7 * WIDTH, 0 },
	{ 8 * WIDTH, 0 },
	{ 18 * WIDTH, 0 },
	{ 19 * WIDTH, 0 },
	{ 20 * WIDTH, 0 },
	{ 5 * WIDTH, HEIGHT },
	{ 6 * WIDTH, HEIGHT },
	{ 7 * WIDTH, HEIGHT },
	{ 8 * WIDTH, HEIGHT },
	{ 9 * WIDTH, HEIGHT },
	{ 17 * WIDTH, HEIGHT },
	{ 18 * WIDTH, HEIGHT },
	{ 19 * WIDTH, HEIGHT },
	{ 20 * WIDTH, HEIGHT },
	{ 21 * WIDTH, HEIGHT },
	{ 4 * WIDTH, 2 * HEIGHT },
	{ 5 * WIDTH, 2 * HEIGHT },
	{ 9 * WIDTH, 2 * HEIGHT },
	{ 10 * WIDTH, 2 * HEIGHT },
	{ 11 * WIDTH, 2 * HEIGHT },
	{ 12 * WIDTH, 2 * HEIGHT },
	{ 14 * WIDTH, 2 * HEIGHT },
	{ 15 * WIDTH, 2 * HEIGHT },
	{ 16 * WIDTH, 2 * HEIGHT },
	{ 17 * WIDTH, 2 * HEIGHT },
	{ 21 * WIDTH, 2 * HEIGHT },
	{ 22 * WIDTH, 2 * HEIGHT },

	{ 3 * WIDTH, 3 * HEIGHT },
	{ 4 * WIDTH, 3 * HEIGHT },
	{ 10 * WIDTH, 3 * HEIGHT },
	{ 11 * WIDTH, 3 * HEIGHT },
	{ 12 * WIDTH, 3 * HEIGHT },
	{ 13 * WIDTH, 3 * HEIGHT },
	{ 14 * WIDTH, 3 * HEIGHT },
	{ 15 * WIDTH, 3 * HEIGHT },
	{ 16 * WIDTH, 3 * HEIGHT },
	{ 22 * WIDTH, 3 * HEIGHT },
	{ 23 * WIDTH, 3 * HEIGHT },

	{ 2 * WIDTH, 4 * HEIGHT },
	{ 3 * WIDTH, 4 * HEIGHT },
	{ 13 * WIDTH, 4 * HEIGHT },
	{ 23 * WIDTH, 4 * HEIGHT },
	{ 24 * WIDTH, 4 * HEIGHT },

	{ 1 * WIDTH, 5 * HEIGHT },
	{ 2 * WIDTH, 5 * HEIGHT },
	{ 24 * WIDTH, 5 * HEIGHT },
	{ 25 * WIDTH, 5 * HEIGHT },

	{ 0 * WIDTH, 6 * HEIGHT },
	{ 1 * WIDTH, 6 * HEIGHT },
	{ 25 * WIDTH, 6 * HEIGHT },
	{ 26 * WIDTH, 6 * HEIGHT },
	{ 0 * WIDTH, 7 * HEIGHT },
	{ 1 * WIDTH, 7 * HEIGHT },
	{ 25 * WIDTH, 7 * HEIGHT },
	{ 26 * WIDTH, 7 * HEIGHT },
	{ 0 * WIDTH, 8 * HEIGHT },
	{ 1 * WIDTH, 8 * HEIGHT },
	{ 25 * WIDTH, 8 * HEIGHT },
	{ 26 * WIDTH, 8 * HEIGHT },
	{ 0 * WIDTH, 9 * HEIGHT },
	{ 1 * WIDTH, 9 * HEIGHT },
	{ 25 * WIDTH, 9 * HEIGHT },
	{ 26 * WIDTH, 9 * HEIGHT },

	{ 1 * WIDTH, 10 * HEIGHT },
	{ 2 * WIDTH, 10 * HEIGHT },
	{ 24 * WIDTH, 10 * HEIGHT },
	{ 25 * WIDTH, 10 * HEIGHT },
	{ 1 * WIDTH, 11 * HEIGHT },
	{ 2 * WIDTH, 11 * HEIGHT },
	{ 24 * WIDTH, 11 * HEIGHT },
	{ 25 * WIDTH, 11 * HEIGHT },
	{ 1 * WIDTH, 12 * HEIGHT },
	{ 2 * WIDTH, 12 * HEIGHT },
	{ 24 * WIDTH, 12 * HEIGHT },
	{ 25 * WIDTH, 12 * HEIGHT },

	{ 2 * WIDTH, 13 * HEIGHT },
	{ 3 * WIDTH, 13 * HEIGHT },
	{ 4 * WIDTH, 13 * HEIGHT },
	{ 22 * WIDTH, 13 * HEIGHT },
	{ 23 * WIDTH, 13 * HEIGHT },
	{ 24 * WIDTH, 13 * HEIGHT },

	{ 5 * WIDTH, 14 * HEIGHT },
	{ 3 * WIDTH, 14 * HEIGHT },
	{ 4 * WIDTH, 14 * HEIGHT },
	{ 21 * WIDTH, 14 * HEIGHT },
	{ 22 * WIDTH, 14 * HEIGHT },
	{ 23 * WIDTH, 14 * HEIGHT },

	{ 4 * WIDTH, 15 * HEIGHT },
	{ 5 * WIDTH, 15 * HEIGHT },
	{ 6 * WIDTH, 15 * HEIGHT },
	{ 20 * WIDTH, 15 * HEIGHT },
	{ 21 * WIDTH, 15 * HEIGHT },
	{ 22 * WIDTH, 15 * HEIGHT },

	{ 5 * WIDTH, 16 * HEIGHT },
	{ 6 * WIDTH, 16 * HEIGHT },
	{ 7 * WIDTH, 16 * HEIGHT },
	{ 19 * WIDTH, 16 * HEIGHT },
	{ 20 * WIDTH, 16 * HEIGHT },
	{ 21 * WIDTH, 16 * HEIGHT },

	{ 6 * WIDTH, 17 * HEIGHT },
	{ 7 * WIDTH, 17 * HEIGHT },
	{ 8 * WIDTH, 17 * HEIGHT },
	{ 18 * WIDTH, 17 * HEIGHT },
	{ 19 * WIDTH, 17 * HEIGHT },
	{ 20 * WIDTH, 17 * HEIGHT },

	{ 7 * WIDTH, 18 * HEIGHT },
	{ 8 * WIDTH, 18 * HEIGHT },
	{ 9 * WIDTH, 18 * HEIGHT },
	{ 10 * WIDTH, 18 * HEIGHT },
	{ 11 * WIDTH, 18 * HEIGHT },
	{ 12 * WIDTH, 18 * HEIGHT },
	{ 13 * WIDTH, 18 * HEIGHT },
	{ 14 * WIDTH, 18 * HEIGHT },
	{ 15 * WIDTH, 18 * HEIGHT },
	{ 16 * WIDTH, 18 * HEIGHT },
	{ 17 * WIDTH, 18 * HEIGHT },
	{ 18 * WIDTH, 18 * HEIGHT },
	{ 19 * WIDTH, 18 * HEIGHT },

	{ 8 * WIDTH, 19 * HEIGHT },
	{ 9 * WIDTH, 19 * HEIGHT },
	{ 10 * WIDTH, 19 * HEIGHT },
	{ 11 * WIDTH, 19 * HEIGHT },
	{ 12 * WIDTH, 19 * HEIGHT },
	{ 13 * WIDTH, 19 * HEIGHT },
	{ 14 * WIDTH, 19 * HEIGHT },
	{ 15 * WIDTH, 19 * HEIGHT },
	{ 16 * WIDTH, 19 * HEIGHT },
	{ 17 * WIDTH, 19 * HEIGHT },
	{ 18 * WIDTH, 19 * HEIGHT },

	{ 10 * WIDTH, 20 * HEIGHT },
	{ 11 * WIDTH, 20 * HEIGHT },
	{ 12 * WIDTH, 20 * HEIGHT },
	{ 13 * WIDTH, 20 * HEIGHT },
	{ 14 * WIDTH, 20 * HEIGHT },
	{ 15 * WIDTH, 20 * HEIGHT },
	{ 16 * WIDTH, 20 * HEIGHT },

	{ 11 * WIDTH, 21 * HEIGHT },
	{ 12 * WIDTH, 21 * HEIGHT },
	{ 13 * WIDTH, 21 * HEIGHT },
	{ 14 * WIDTH, 21 * HEIGHT },
	{ 15 * WIDTH, 21 * HEIGHT },

	{ 13 * WIDTH, 22 * HEIGHT } };

	private static int[][] SEXSYMBOLMAP =
	{
	{ 6 * WIDTH, 0 },
	{ 20 * WIDTH, 0 },
	{ 21 * WIDTH, 0 },
	{ 22 * WIDTH, 0 },

	{ 5 * WIDTH, HEIGHT },
	{ 6 * WIDTH, HEIGHT },
	{ 7 * WIDTH, HEIGHT },
	{ 19 * WIDTH, HEIGHT },
	{ 20 * WIDTH, HEIGHT },
	{ 22 * WIDTH, HEIGHT },
	{ 23 * WIDTH, HEIGHT },

	{ 4 * WIDTH, 2 * HEIGHT },
	{ 5 * WIDTH, 2 * HEIGHT },
	{ 6 * WIDTH, 2 * HEIGHT },
	{ 7 * WIDTH, 2 * HEIGHT },
	{ 8 * WIDTH, 2 * HEIGHT },
	{ 18 * WIDTH, 2 * HEIGHT },
	{ 19 * WIDTH, 2 * HEIGHT },
	{ 23 * WIDTH, 2 * HEIGHT },
	{ 24 * WIDTH, 2 * HEIGHT },

	{ 3 * WIDTH, 3 * HEIGHT },
	{ 4 * WIDTH, 3 * HEIGHT },
	{ 6 * WIDTH, 3 * HEIGHT },
	{ 8 * WIDTH, 3 * HEIGHT },
	{ 9 * WIDTH, 3 * HEIGHT },
	{ 17 * WIDTH, 3 * HEIGHT },
	{ 18 * WIDTH, 3 * HEIGHT },
	{ 24 * WIDTH, 3 * HEIGHT },
	{ 25 * WIDTH, 3 * HEIGHT },

	{ 2 * WIDTH, 4 * HEIGHT },
	{ 3 * WIDTH, 4 * HEIGHT },
	{ 6 * WIDTH, 4 * HEIGHT },
	{ 19 * WIDTH, 4 * HEIGHT },
	{ 10 * WIDTH, 4 * HEIGHT },
	{ 16 * WIDTH, 4 * HEIGHT },
	{ 17 * WIDTH, 4 * HEIGHT },
	{ 25 * WIDTH, 4 * HEIGHT },
	{ 26 * WIDTH, 4 * HEIGHT },

	{ 6 * WIDTH, 5 * HEIGHT },
	{ 15 * WIDTH, 5 * HEIGHT },
	{ 16 * WIDTH, 5 * HEIGHT },
	{ 26 * WIDTH, 5 * HEIGHT },
	{ 27 * WIDTH, 5 * HEIGHT },
	{ 6 * WIDTH, 6 * HEIGHT },
	{ 15 * WIDTH, 6 * HEIGHT },
	{ 16 * WIDTH, 6 * HEIGHT },
	{ 26 * WIDTH, 6 * HEIGHT },
	{ 27 * WIDTH, 6 * HEIGHT },
	{ 5 * WIDTH, 7 * HEIGHT },
	{ 6 * WIDTH, 7 * HEIGHT },
	{ 7 * WIDTH, 7 * HEIGHT },
	{ 16 * WIDTH, 7 * HEIGHT },
	{ 17 * WIDTH, 7 * HEIGHT },
	{ 25 * WIDTH, 7 * HEIGHT },
	{ 26 * WIDTH, 7 * HEIGHT },

	{ 4 * WIDTH, 8 * HEIGHT },
	{ 5 * WIDTH, 8 * HEIGHT },
	{ 7 * WIDTH, 8 * HEIGHT },
	{ 8 * WIDTH, 8 * HEIGHT },
	{ 17 * WIDTH, 8 * HEIGHT },
	{ 18 * WIDTH, 8 * HEIGHT },
	{ 24 * WIDTH, 8 * HEIGHT },
	{ 25 * WIDTH, 8 * HEIGHT },

	{ 3 * WIDTH, 9 * HEIGHT },
	{ 4 * WIDTH, 9 * HEIGHT },
	{ 8 * WIDTH, 9 * HEIGHT },
	{ 9 * WIDTH, 9 * HEIGHT },
	{ 18 * WIDTH, 9 * HEIGHT },
	{ 19 * WIDTH, 9 * HEIGHT },
	{ 24 * WIDTH, 9 * HEIGHT },
	{ 23 * WIDTH, 9 * HEIGHT },

	{ 2 * WIDTH, 10 * HEIGHT },
	{ 3 * WIDTH, 10 * HEIGHT },
	{ 9 * WIDTH, 10 * HEIGHT },
	{ 10 * WIDTH, 10 * HEIGHT },
	{ 19 * WIDTH, 10 * HEIGHT },
	{ 20 * WIDTH, 10 * HEIGHT },
	{ 22 * WIDTH, 10 * HEIGHT },
	{ 23 * WIDTH, 10 * HEIGHT },

	{ 1 * WIDTH, 11 * HEIGHT },
	{ 2 * WIDTH, 11 * HEIGHT },
	{ 10 * WIDTH, 11 * HEIGHT },
	{ 11 * WIDTH, 11 * HEIGHT },
	{ 20 * WIDTH, 11 * HEIGHT },
	{ 21 * WIDTH, 11 * HEIGHT },
	{ 22 * WIDTH, 11 * HEIGHT },

	{ 0, 12 * HEIGHT },
	{ 1 * WIDTH, 12 * HEIGHT },
	{ 11 * WIDTH, 12 * HEIGHT },
	{ 12 * WIDTH, 12 * HEIGHT },
	{ 21 * WIDTH, 12 * HEIGHT },

	{ 0, 13 * HEIGHT },
	{ 1 * WIDTH, 13 * HEIGHT },
	{ 11 * WIDTH, 13 * HEIGHT },
	{ 12 * WIDTH, 13 * HEIGHT },
	{ 21 * WIDTH, 13 * HEIGHT },

	{ 1 * WIDTH, 14 * HEIGHT },
	{ 2 * WIDTH, 14 * HEIGHT },
	{ 10 * WIDTH, 14 * HEIGHT },
	{ 11 * WIDTH, 14 * HEIGHT },
	{ 17 * WIDTH, 14 * HEIGHT },
	{ 18 * WIDTH, 14 * HEIGHT },
	{ 19 * WIDTH, 14 * HEIGHT },
	{ 20 * WIDTH, 14 * HEIGHT },
	{ 21 * WIDTH, 14 * HEIGHT },
	{ 22 * WIDTH, 14 * HEIGHT },
	{ 23 * WIDTH, 14 * HEIGHT },
	{ 24 * WIDTH, 14 * HEIGHT },
	{ 25 * WIDTH, 14 * HEIGHT },

	{ 2 * WIDTH, 15 * HEIGHT },
	{ 3 * WIDTH, 15 * HEIGHT },
	{ 9 * WIDTH, 15 * HEIGHT },
	{ 10 * WIDTH, 15 * HEIGHT },
	{ 21 * WIDTH, 15 * HEIGHT },

	{ 3 * WIDTH, 16 * HEIGHT },
	{ 4 * WIDTH, 16 * HEIGHT },
	{ 8 * WIDTH, 16 * HEIGHT },
	{ 9 * WIDTH, 16 * HEIGHT },
	{ 21 * WIDTH, 16 * HEIGHT },

	{ 4 * WIDTH, 17 * HEIGHT },
	{ 5 * WIDTH, 17 * HEIGHT },
	{ 7 * WIDTH, 17 * HEIGHT },
	{ 8 * WIDTH, 17 * HEIGHT },
	{ 21 * WIDTH, 17 * HEIGHT },

	{ 5 * WIDTH, 18 * HEIGHT },
	{ 6 * WIDTH, 18 * HEIGHT },
	{ 7 * WIDTH, 18 * HEIGHT },
	{ 21 * WIDTH, 18 * HEIGHT } };

	int x, y;
	int line, row;
	boolean isLive;
	ArrayList<WallNode> wallNodesList = new ArrayList<WallNode>();
	TankClient tc;

	public Wall(int x, int y, int line, int row, TankClient tc)
	{
		this.x = x;
		this.y = y;
		this.line = line;
		this.row = row;
		this.isLive = true;
		this.tc = tc;

		this.creat();
	}

	public Wall(int x, int y, WALLTYPE type, TankClient tc)
	{
		this.x = x;
		this.y = y;
		this.isLive = true;
		this.tc = tc;
		if (type == WALLTYPE.HEART)
		{
			this.line = 23;
			this.row = 27;
			for (int i = 0; i < HEARTMAP.length; i++)
			{
				wallNodesList.add(new WallNode(HEARTMAP[i][0] + x, HEARTMAP[i][1] + y));
			}
		} else
		{
			this.line = 19;
			this.row = 28;
			for (int i = 0; i < SEXSYMBOLMAP.length; i++)
			{
				wallNodesList.add(new WallNode(SEXSYMBOLMAP[i][0] + x, SEXSYMBOLMAP[i][1] + y));
			}
		}

	}

	private void creat()
	{
		for (int i = 0; i < line; i++)
		{
			for (int j = 0; j < row; j++)
			{
				this.wallNodesList.add(new WallNode(x + j * WallNode.WIDTH, y + i * WallNode.HEIGHT));
			}
		}
	}

	public boolean isHits(Shot s) // 普通子弹可以破坏墙
	{
		WallNode wn = null;
		for (int i = 0; i < wallNodesList.size(); i++)
		{
			wn = wallNodesList.get(i);

			if (s.getRect().intersects(wn.getRect()))
			{
				wn.isLive = false;
				wallNodesList.remove(i);
				if (0 == wallNodesList.size())
				{
					this.isLive = false;
					this.tc.wallList.remove(this);
					System.out.println(tc.wallList.size());
				} // 墙全部被破坏了
				return true;
			}
		}

		return false;
	}

	public boolean isHits(Rectangle rect) // 碰撞检测
	{
		WallNode wn = null;
		for (int i = 0; i < wallNodesList.size(); i++)
		{
			wn = wallNodesList.get(i);

			if (rect.intersects(wn.getRect()))
			{
				return true;
			}
		}

		return false;
	}

	public void draw(Graphics g)
	{
		for (int i = 0; i < wallNodesList.size(); i++)
		{
			wallNodesList.get(i).draw(g);
		}
	}

}

class WallNode
{
	static final Toolkit TLK = Toolkit.getDefaultToolkit(); // 用来将图片读入
	static final Image WallImage = TLK.getImage(WallNode.class.getClassLoader().getResource("Image/Wall/Wall.png"));

	static final int WIDTH = 16;
	static final int HEIGHT = 16;
	int x, y;
	boolean isLive;

	public WallNode(int x, int y)
	{
		this.x = x;
		this.y = y;
		this.isLive = true;
	}

	public void draw(Graphics g)
	{
		if (isLive)
		{
			g.drawImage(WallImage, x, y, null);
		}
	}

	public Rectangle getRect()
	{
		return new Rectangle(this.x, this.y, WIDTH, HEIGHT);
	}

}
