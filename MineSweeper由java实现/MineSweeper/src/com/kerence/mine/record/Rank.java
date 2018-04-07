package com.kerence.mine.record;

import java.io.Serializable;

/**
 * ������ ���ڼ�¼����
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
	 * ���췽��
	 * 
	 * @param level
	 *            ��Ϸ��ģʽ
	 * @param time
	 *            �����Ϸ��ʱ��
	 * @param name
	 *            �������
	 */
	public Rank(String level, int time, String name)
	{
		super();
		this.level = level;
		this.time = time;
		this.name = name;
	}

	/**
	 * �õ���Ϸģʽ
	 * 
	 * @return ��Ϸģʽ���ַ�������
	 */
	public String getLevel()
	{
		return level;
	}

	/**
	 * ������Ϸģʽ
	 * 
	 * @param level
	 *            ��Ϸģʽ�ı�
	 */
	public void setLevel(String level)
	{
		this.level = level;
	}

	/**
	 * �õ������Ϸ��ʱ��
	 * 
	 * @return �����Ϸ��ʱ��
	 */
	public int getTime()
	{
		return time;
	}

	/**
	 * ���������Ϸ��ʱ��
	 * 
	 * @param time
	 *            �����Ϸ��ʱ��
	 */
	public void setTime(int time)
	{
		this.time = time;
	}

	/**
	 * �õ���ҵ�����
	 * 
	 * @return ��ҵ�����
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * �����������
	 * 
	 * @param name
	 *            �������
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * ��Rank����ת�����ַ���
	 */
	@Override
	public String toString()
	{
		return time + "��\t" + name;
	}

	/**
	 * ���ڲ�ͬ��rank֮��ıȽ� �Ƚϵı�׼�������Ϸ��ʱ��
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
