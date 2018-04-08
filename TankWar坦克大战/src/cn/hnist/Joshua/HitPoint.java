package cn.hnist.Joshua;

//download by http://www.codefans.net
import java.awt.*;

public class HitPoint
{
	public static final int HITPOINTSIZE = 50; // Ѫ���ĳ���
	int topNumber; // ��Ѫ��
	int number; // ��ǰѪ��
	Tank tk;

	public HitPoint(int topNum, Tank tk)
	{
		if (topNum > 50 || topNum <= 0)
		{
			this.topNumber = 50;
		}
		topNumber = topNum;
		number = topNum;
		this.tk = tk;
	}

	public void draw(Graphics g)
	{
		Color c = g.getColor();

		g.setColor(Color.white);
		g.drawRect(tk.x - 25, tk.y - 35, HITPOINTSIZE, 5);
		g.setColor(Color.red);
		g.fillRect(tk.x - 24, tk.y - 34, number * HITPOINTSIZE / topNumber - 1, 4);

		g.setColor(c);
	}

	public void cutsHitPoint(int cuts) // ������ײʱ�ļ�Ѫ
	{
		number -= cuts;
		if (isEmpty())
		{
			tk.dead();
		} // ��Ѫ����,Tank����
	}

	public void cutsHitPoint(int cuts, Shot s) // ���ӵ�����ʱ�ļ�Ѫ
	{
		number -= cuts;
		if (tk.isLive && tk instanceof RobotTank)
		{
			((RobotTank) tk).avoidThrust(s);
		}
		if (isEmpty())
		{
			tk.dead();
		} // ��Ѫ����,Tank����
	}

	public void raisesHitPoint(int raises) // Ѫ������
	{
		number += raises;
		if (number > topNumber)
		{
			number = topNumber;
		}
	}

	public boolean isEmpty()
	{
		if (number <= 0)
		{
			return true;
		} else
		{
			return false;
		}
	}
}
