package com.kerence.mine.record;

import java.io.Serializable;

/**
 * 排名类 用于记录排名
 * 
 * @download:http://www.codefans.net
 * @author Kerence
 * 
 */
public class Rank implements Serializable, Comparable<Rank>
{
	private String level;
	private int time;
	private String name;

	/**
	 * 构造方法
	 * 
	 * @param level
	 *            游戏的模式
	 * @param time
	 *            完成游戏的时间
	 * @param name
	 *            玩家姓名
	 */
	public Rank(String level, int time, String name)
	{
		super();
		this.level = level;
		this.time = time;
		this.name = name;
	}

	/**
	 * 得到游戏模式
	 * 
	 * @return 游戏模式的字符串描述
	 */
	public String getLevel()
	{
		return level;
	}

	/**
	 * 设置游戏模式
	 * 
	 * @param level
	 *            游戏模式文本
	 */
	public void setLevel(String level)
	{
		this.level = level;
	}

	/**
	 * 得到完成游戏的时间
	 * 
	 * @return 完成游戏的时间
	 */
	public int getTime()
	{
		return time;
	}

	/**
	 * 设置完成游戏的时间
	 * 
	 * @param time
	 *            完成游戏的时间
	 */
	public void setTime(int time)
	{
		this.time = time;
	}

	/**
	 * 得到玩家的名字
	 * 
	 * @return 玩家的名字
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * 设置玩家名字
	 * 
	 * @param name
	 *            玩家名字
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * 将Rank对象转换成字符串
	 */
	@Override
	public String toString()
	{
		return time + "秒\t" + name;
	}

	/**
	 * 用于不同的rank之间的比较 比较的标准是完成游戏的时间
	 */
	@Override
	public int compareTo(Rank o)
	{
		if (this.time > o.time)
		{
			return 1;
		} 
		else if (this.time < o.time)
		{
			return -1;
		} 
		else
		{
			return 0;
		}
	}

}
