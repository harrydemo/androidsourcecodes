package com.yarin.android.MagicTower;

public class FightCalc
{
	public static final int MIN_ORGE_INDEX = 50;
	public static final int MAX_ORGE_INDEX = 80;

	private static final int OFFSET = 50;
	private static final int[][] orgeAttr= {
	//HP,attack,defend,money,experience	
		{50, 20, 1, 1, 1},
		{70, 15, 2, 2, 1},
		{100, 20, 5, 3, 2},
		{110, 25, 5, 5, 3},
		{200, 35, 10, 5, 3},
		{150, 40, 20, 8, 5},
		{125, 50, 25, 10, 7},
		{150, 65, 30, 10, 8},
		{300, 75, 45, 13, 10},
		{400, 90, 50, 15, 12},
		{500, 115, 65, 15, 15},
		{250, 120, 70, 20, 15},
		{450, 150, 90, 22, 19},
		{550, 170, 100, 25, 20},
		{100, 200, 110, 30, 25},
		{700, 250, 125, 32, 25},
		{1300, 300, 150, 40, 30},
		{850, 350, 200, 45, 35},
		{500, 400, 260, 47, 35},
		{900, 450, 330, 50, 40},
		{1250, 500, 400, 55, 45},
		{1500, 560, 460, 60, 50},
		{1200, 620, 520, 65, 50},
		{2000, 680, 590, 70, 55},
		{900, 750, 650, 77, 60},
		{1500, 830, 730, 80, 65},
		{2500, 900, 850, 84, 70},
		{1200, 980, 900, 88, 75},
		{3100, 1050, 950, 92, 80},
		{15000, 1000, 1000, 100, 100},
		{25000, 1500, 1200, 150, 120}
	};
	public static final int HP = 0,
							 ATTACK = 1,
							 DEFEND = 2,
							 MONEY = 3,
							 EXPERIENCE = 4;
	public static final int BOUT_NUM = 0,
							HERO_DAMAGE_PER_BOUT = 1,
							ORGE_DAMAGE_PER_BOUT = 2;
	private static final String[] orgeName = 
	{
		"绿头怪 ", "红头怪 ","小蝙蝠 ", "骷髅人 ","青头怪","骷髅士兵",
		"初级法师", "大蝙蝠 ", "兽面人 ", "骷髅队长", "石头怪人", "麻衣法师",
		"初级卫兵", "红蝙蝠 ", "高级法师", "怪王", "白衣武士", "金甲卫士", 
		"红衣法师", "兽面武士", "冥卫兵 ", "高级卫兵", "双手剑士", "冥战士 ", 
		"金甲队长", "灵法师 ", "冥队长 ", "灵武士 ", "影子战士", "红衣魔王", "冥灵魔王"
	};
	
	private HeroSprite					hero;
	private int						curOrge;
	private int						heroDamagePerBout, orgeDamagePerBout;
	private int						heroBout;
	private int						orgeBout;


	public FightCalc(HeroSprite hero)
	{
		this.hero = hero;
	}


	private int calcType(int type)
	{
		return type - OFFSET;
	}


	private void calcBout()
	{
		heroDamagePerBout = orgeAttr[curOrge][ATTACK] - hero.getDefend();
		orgeDamagePerBout = hero.getAttack() - orgeAttr[curOrge][DEFEND];
		heroBout = orgeAttr[curOrge][HP] / orgeDamagePerBout;
		if ((orgeAttr[curOrge][HP] - orgeDamagePerBout * heroBout) > 0)
			heroBout++;
		if (heroDamagePerBout <= 0)
		{
			heroDamagePerBout = 0;
			orgeBout = Integer.MAX_VALUE;
		}
		else
		{
			orgeBout = hero.getHp() / heroDamagePerBout;
			if ((hero.getHp() - heroDamagePerBout * orgeBout) > 0)
				orgeBout++;
		}
	}


	public boolean canAttack(int type)
	{
		boolean result = false;
		curOrge = calcType(type);
		if (orgeAttr[curOrge][DEFEND] >= hero.getAttack())
		{
			result = false;
		}
		else
		{
			calcBout();
			if (heroBout < orgeBout)
				result = true;
		}
		return result;
	}


	public int[] getFightParam()
	{
		int[] result = new int[3];
		result[BOUT_NUM] = heroBout;
		result[HERO_DAMAGE_PER_BOUT] = heroDamagePerBout;
		result[ORGE_DAMAGE_PER_BOUT] = orgeDamagePerBout;
		return result;
	}


	public String getOrgeName(int type)
	{
		return orgeName[calcType(type)];
	}


	public int[] getOrgeAttr(int type)
	{
		int[] result = new int[5];
		for (int i = 0; i < 5; i++)
		{
			result[i] = orgeAttr[calcType(type)][i];

		}
		return result;
	}


	public int getHeroDamage(int type)
	{
		int result = 0;
		curOrge = calcType(type);
		if (orgeAttr[curOrge][DEFEND] >= hero.getAttack())
		{
			result = -1;
		}
		else
		{
			calcBout();
			result = heroBout * heroDamagePerBout;
		}
		return result;
	}
}

