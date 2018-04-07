package cn.hnist.Joshua;

//download by http://www.codefans.net
import java.awt.*;

abstract class Item
{
	public static Toolkit TLK = Toolkit.getDefaultToolkit();

	int x, y;
	int width, height;
	TankClient tc;

	public Item(int x, int y, TankClient tc)
	{
		this.x = x;
		this.y = y;
		this.tc = tc;
	}

	abstract void draw(Graphics g);

	abstract void eat(Tank tk);

	public Rectangle getRect()
	{
		return new Rectangle(x, y, width, height);
	}
}

class HitPointItem extends Item // 涨血的道具
{
	int raisesNum;
	static final Image hpimg = TLK.getImage(Item.class.getClassLoader().getResource("Image/Item/hp.png"));

	public HitPointItem(int x, int y, int raisesNum, TankClient tc)
	{
		super(x, y, tc);
		this.raisesNum = raisesNum;
		this.width = 34;
		this.height = 30;
	}

	void draw(Graphics g)
	{
		g.drawImage(hpimg, x - width / 2, y - height / 2, null);
	}

	void eat(Tank tk)
	{
		tk.tankHitPoint.raisesHitPoint(raisesNum); // Tank涨血
		tc.itemList.remove(this);
	}
}

class ShotsItem extends Item // 涨炮弹的道具
{
	int type; // 涨的炮弹类型

	public ShotsItem(int x, int y, int type, TankClient tc)
	{
		super(x, y, tc);
		this.type = type;
		this.width = 10;
		this.height = 10;
	}

	void draw(Graphics g)
	{
		Color c = g.getColor();

		g.setColor(Color.orange);
		g.fillOval(x, y, width, height);

		g.setColor(c);
	}

	void eat(Tank tk)
	{
		if (!tk.isNPC) // 玩家才可以吃
		{
			switch (type)
			{
			
			case 0:
			{
				((UserTank) tk).addHydraShots();
			}
				break;
			case 1:
			{
				((UserTank) tk).addSuperShots();
			}
				break;
			default:
				break;
			}
			tc.itemList.remove(this);
		}
	}
}
