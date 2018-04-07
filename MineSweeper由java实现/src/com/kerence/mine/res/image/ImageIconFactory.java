package com.kerence.mine.res.image;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * 图像资源的工厂类
 * 
 * @author Kerence
 * 
 */
public class ImageIconFactory
{
	private static ImageIcon minePressed = new ImageIcon("./image/0.gif");
	private static ImageIcon number1 = new ImageIcon("./image/1.gif");
	private static ImageIcon number2 = new ImageIcon("./image/2.gif");
	private static ImageIcon number3 = new ImageIcon("./image/3.gif");
	private static ImageIcon number4 = new ImageIcon("./image/4.gif");
	private static ImageIcon number5 = new ImageIcon("./image/5.gif");
	private static ImageIcon number6 = new ImageIcon("./image/6.gif");
	private static ImageIcon number7 = new ImageIcon("./image/7.gif");
	private static ImageIcon number8 = new ImageIcon("./image/8.gif");
	private static ImageIcon number0 = minePressed;
	private static ImageIcon[] number =
	{ number0, number1, number2, number3, number4, number5, number6, number7, number8 };

	private static ImageIcon led0 = new ImageIcon("./image/d0.gif");
	private static ImageIcon led1 = new ImageIcon("./image/d1.gif");
	private static ImageIcon led2 = new ImageIcon("./image/d2.gif");
	private static ImageIcon led3 = new ImageIcon("./image/d3.gif");
	private static ImageIcon led4 = new ImageIcon("./image/d4.gif");
	private static ImageIcon led5 = new ImageIcon("./image/d5.gif");
	private static ImageIcon led6 = new ImageIcon("./image/d6.gif");
	private static ImageIcon led7 = new ImageIcon("./image/d7.gif");
	private static ImageIcon led8 = new ImageIcon("./image/d8.gif");
	private static ImageIcon led9 = new ImageIcon("./image/d9.gif");
	private static ImageIcon blank = new ImageIcon("image/blank.gif");
	private static ImageIcon[] led =
	{ led0, led1, led2, led3, led4, led5, led6, led7, led8, led9 };
	private static ImageIcon led10 = new ImageIcon("./image/d10.gif");
	private static ImageIcon faceSmile = new ImageIcon("./image/face0.gif");
	private static ImageIcon faceSmilePressed = new ImageIcon("./image/face1.gif");
	private static ImageIcon faceSurprised = new ImageIcon("./image/face2.gif");
	private static ImageIcon faceCry = new ImageIcon("./image/face3.gif");
	private static ImageIcon faceHappy = new ImageIcon("./image/face4.gif");
	private static ImageIcon flag = new ImageIcon("./image/flag.gif");
	private static ImageIcon mine = new ImageIcon("./image/mine.gif");
	private static ImageIcon wrongMine = new ImageIcon("./image/mine1.gif");
	private static ImageIcon redMine = new ImageIcon("./image/mine2.gif");
	private static ImageIcon ask = new ImageIcon("./image/ask0.gif");
	private static ImageIcon askPressed = new ImageIcon("./image/ask1.gif");
	private static ImageIcon hole = new ImageIcon("./image/hole.gif");

	/**
	 * 得到空白图片
	 * 
	 * @return 相应的ImageIcon对象
	 */
	public static ImageIcon getBlank()
	{
		return blank;
	}

	/**
	 * 得到问号图片
	 * 
	 * @return 相应的ImageIcon对象
	 */
	public static ImageIcon getAsk()
	{
		return ask;
	}

	/**
	 * 得到问号按下的图片
	 * 
	 * @return 相应的ImageIcon对象
	 */
	public static ImageIcon getAskPressed()
	{
		return askPressed;
	}

	/**
	 * 得到红旗图片
	 * 
	 * @return 相应的ImageIcon对象
	 */
	public static ImageIcon getFlag()
	{
		return flag;
	}

	/**
	 * 得到数字图片
	 * 
	 * @return 相应的ImageIcon对象
	 */
	public static ImageIcon getNumber(int i)
	{
		return number[i];
	}

	/**
	 * 得到负号图片
	 * 
	 * @return 相应的ImageIcon对象
	 */
	public static ImageIcon getLedMinus()
	{
		return led10;
	}

	/**
	 * 得到雷图片
	 * 
	 * @return 相应的ImageIcon对象
	 */
	public static ImageIcon getMine()
	{
		return mine;
	}

	/**
	 * 得到错雷图片
	 * 
	 * @return 相应的ImageIcon对象
	 */
	public static ImageIcon getWrongMine()
	{
		return wrongMine;
	}

	/**
	 * 得到红雷图片
	 * 
	 * @return 相应的ImageIcon对象
	 */
	public static ImageIcon getRedMine()
	{
		return redMine;
	}

	/**
	 * 得到LED数字图片
	 * 
	 * @return 相应的ImageIcon对象
	 */
	public static ImageIcon getLed(int i)
	{
		return led[i];
	}

	/**
	 * 得到按下的空白图片
	 * 
	 * @return 相应的ImageIcon对象
	 */
	public static ImageIcon getBlankPressed()
	{
		return minePressed;
	}

	/**
	 * 得到表情微笑图片
	 * 
	 * @return 相应的ImageIcon对象
	 */
	public static ImageIcon getFaceSmile()
	{
		return faceSmile;
	}

	/**
	 * 得到表情按下的微笑图片
	 * 
	 * @return 相应的ImageIcon对象
	 */
	public static ImageIcon getFaceSmilePressed()
	{
		return faceSmilePressed;
	}

	/**
	 * 得到表情惊讶图片
	 * 
	 * @return 相应的ImageIcon对象
	 */
	public static ImageIcon getFaceSurprised()
	{
		return faceSurprised;
	}

	/**
	 * 得到表情哭泣图片
	 * 
	 * @return 相应的ImageIcon对象
	 */
	public static ImageIcon getFaceCry()
	{
		return faceCry;
	}

	/**
	 * 得到表情快乐图片
	 * 
	 * @return 相应的ImageIcon对象
	 */
	public static ImageIcon getFaceHappy()
	{
		return faceHappy;
	}

	/**
	 * 得到黑点图片
	 * 
	 * @return 相应的ImageIcon对象
	 */
	public static Icon getDot()
	{
		return hole;
	}

}
