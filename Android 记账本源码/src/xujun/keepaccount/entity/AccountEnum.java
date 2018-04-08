/*
 * 系统名称：
 * 模块名称：
 * 描述：
 * 作者：徐骏
 * version 1.0
 * time  2010-11-12 下午03:11:36
 * copyright Anymusic Ltd.
 */
package xujun.keepaccount.entity;

import xujun.keepaccount.R;

/**
 * @author 徐骏
 * @data   2010-11-12
 */
public enum AccountEnum
{
	/**
	 * 日常开支
	 */
	Daily(0),
	/**
	 * 外出就餐
	 */
	Eatery(1),
	/**
	 * 服装鞋帽
	 */
	Shirt(2),
	/**
	 * 交通通讯
	 */
	Traffic(3),
	/**
	 * 水电煤气
	 */
	Electricity(4),
	/**
	 * 娱乐
	 */
	Amusement(5),
	/**
	 * 体育
	 */
	Sport(6),
	/**
	 * 交际
	 */
	Company(7),
	/**
	 * 其他
	 */
	Other(8);
	
	private int typeId;
	private AccountEnum(int typeId)
	{
		this.typeId = typeId;
	}
	public int getValue()
	{
		return this.typeId;
	}
	public static AccountEnum getAccountEnum(int typeId)
	{
		switch (typeId) {
		case 0:
			return AccountEnum.Daily;
		case 1:
			return AccountEnum.Eatery;
		case 2:
			return AccountEnum.Shirt;
		case 3:
			return AccountEnum.Traffic;
		case 4:
			return AccountEnum.Electricity;
		case 5:
			return AccountEnum.Amusement;
		case 6:
			return AccountEnum.Sport;
		case 7:
			return AccountEnum.Company;
		case 8:
			return AccountEnum.Other;
		default:
			return null;
		}
	}
	public static int getAccountEnumImage(AccountEnum accountEnum)
	{
		switch (accountEnum) {
		case Daily:
			return R.drawable.tabbar_food;
		case Eatery:
			return R.drawable.tabbar_eatery;
		case Shirt:
			return R.drawable.tabbar_shirt;
		case Traffic:
			return R.drawable.tabbar_wireless;
		case Electricity:
			return R.drawable.tabbar_lamp;
		case Amusement:
			return R.drawable.tabbar_music;
		case Sport:
			return R.drawable.tabbar_sport;
		case Company:
			return R.drawable.tabbar_friend;
		case Other:
			return R.drawable.tabbar_bell;
		default:
			return R.drawable.tabbar_bell;
		}
	}
	@Override
	public String toString()
	{
		switch (typeId)
		{
			case 0:
				return "日常开支";
			case 1:
				return "外出就餐";
			case 2:
				return "服装鞋帽";
			case 3:
				return "交通通讯";
			case 4:
				return "水电煤气";
			case 5:
				return "娱乐";
			case 6:
				return "体育";
			case 7:
				return "交际";
			case 8:
				return "其他";
			default:
				return "";
		}
	}
}
