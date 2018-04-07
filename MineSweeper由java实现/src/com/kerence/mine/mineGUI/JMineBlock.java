package com.kerence.mine.mineGUI;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.kerence.mine.res.image.ImageIconFactory;

/**
 * 它继承至JLabel代表每个雷块的显示控件
 * 
 * @author Kerence
 * 
 */
public class JMineBlock extends JLabel
{
	private int row;
	private int column;

	/**
	 * 构造方法
	 */
	public JMineBlock()
	{
		super();
	}

	/**
	 * 带有icon参数的构造方法，将图标设置为icon
	 * 
	 * @param icon
	 *            图标
	 */
	public JMineBlock(ImageIcon icon)
	{
		this.setIcon(icon);
	}

	/**
	 * 设置这个雷块的行号
	 * 
	 * @param row
	 *            行号
	 */
	public void setRow(int row)
	{
		this.row = row;
	}

	/**
	 * 设置这个雷块的图标为雷
	 */
	public void setMine()
	{
		this.setIcon(ImageIconFactory.getMine());
	}

	/**
	 * 设置这个雷块的图标为红雷
	 */
	public void setRedMine()
	{
		this.setIcon(ImageIconFactory.getRedMine());
	}

	/**
	 * 设置这个雷块的图标为问号
	 */
	public void setAsk()
	{
		this.setIcon(ImageIconFactory.getAsk());
	}

	/**
	 * 设置这个雷块的图标为按下的问号
	 */
	public void setAskPressed()
	{
		this.setIcon(ImageIconFactory.getAskPressed());
	}

	/**
	 * 设置这个雷块的图标为带红叉的雷
	 */
	public void setWrongMine()
	{
		this.setIcon(ImageIconFactory.getWrongMine());
	}

	/**
	 * 设置这个雷块的图标为旗
	 */

	public void setFlag()
	{
		this.setIcon(ImageIconFactory.getFlag());
	}

	/**
	 * 设置这个雷块的图标为按下的标记
	 */
	public void setMarkPressed()
	{
		this.setIcon(ImageIconFactory.getAskPressed());
	}

	/**
	 * 设置这个雷块的列号
	 * 
	 * @param column
	 *            列号
	 */
	public void setColumn(int column)
	{
		this.column = column;
	}

	/**
	 * 得到这个雷块的行号
	 * 
	 * @return 行号
	 */
	public int getRow()
	{
		return this.row;
	}

	/**
	 * 得到这个雷块的列号
	 * 
	 * @return 列号
	 */
	public int getColumn()
	{
		return this.column;
	}

	/**
	 * 得到当前显示的图标
	 * 
	 * @return 当前显示的图标
	 */
	public Icon getIcon()
	{
		return super.getIcon();
	}

	/**
	 * 得到这个雷块为的图标为空
	 * 
	 */
	public void setBlank()
	{
		this.setIcon(ImageIconFactory.getBlank());
	}

	/**
	 * 得到这个雷块为的图标为按下的空白
	 * 
	 */
	public void setBlankPressed()
	{
		this.setIcon(ImageIconFactory.getBlankPressed());
	}

	/**
	 * 得到这个雷块为的图标为指定的数字
	 * 
	 * @param num
	 *            指定的数字
	 */
	public void setNumber(int num)
	{
		this.setIcon(ImageIconFactory.getNumber(num));
	}

	/**
	 * 设置该雷块的图标为点
	 */
	public void setDot()
	{
		this.setIcon(ImageIconFactory.getDot());
	}

}
