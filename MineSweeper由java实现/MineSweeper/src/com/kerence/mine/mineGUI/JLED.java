package com.kerence.mine.mineGUI;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.BevelBorder;

import com.kerence.mine.exceptions.LEDException;
import com.kerence.mine.res.image.ImageIconFactory;

/**
 * LED数字类 由三个数字图标组成
 * 
 * @author Administrator
 * 
 */
public class JLED extends Box
{
	private JLabel integerPlace[] = new JLabel[3];
	private int second;

	/**
	 * 构造函数 进行初始化操作
	 */
	public JLED()
	{

		super(BoxLayout.X_AXIS);
		for (int i = 0; i < integerPlace.length; i++)
		{
			integerPlace[i] = new JLabel(new ImageIcon("./image/d0.gif"));
			this.add(integerPlace[i]);
		}
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
	}

	/**
	 * 设置显示的数字
	 * 
	 * @param second
	 *            数字的值 只能在-999和999之间
	 */
	public void setNumber(int second)
	{
		if (second > 999 || second < -999)
		{
			throw new LEDException("数字超出范围");
		}
		this.second = second;
		showNumber(second);
	}

	/**
	 * 显示数字
	 * 
	 * @param number
	 */
	private void showNumber(int number)
	{
		if (number > 999 || number < -999)
		{
			throw new LEDException("数字超出范围");
		}

		int digit = 2;
		resetDisplay();// 全部置0 将非0的LED设置为相应的数字
		StringBuffer str = new StringBuffer(String.valueOf(Math.abs(number)));
		if (number < 0)
		{
			integerPlace[0].setIcon(ImageIconFactory.getLedMinus());
			if (number < -99)
				str.deleteCharAt(0);
		}

		for (int i = str.length(); i > 0; i--)
		{
			// this.integerPlace[digit].setIcon(new ImageIcon("./image/d"
			// + Integer.parseInt(str.charAt(i - 1) + "") + ".gif"));
			this.integerPlace[digit].setIcon(ImageIconFactory.getLed(Integer.parseInt(str.charAt(i - 1) + "")));
			digit--;
		}
	}

	/**
	 * 把三幅图片都置为数字0
	 */
	private void resetDisplay()
	{
		for (int i = 0; i < 3; i++)
			this.integerPlace[i].setIcon(new ImageIcon("./image/d0.gif"));
	}

	/**
	 * 得到数字图片显示的数字
	 * 
	 * @return 数字图片显示的数字
	 */
	public int getNumber()
	{
		return this.second;
	}
}
