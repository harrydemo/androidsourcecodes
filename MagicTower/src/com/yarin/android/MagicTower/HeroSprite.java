package com.yarin.android.MagicTower;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.microedition.lcdui.game.Sprite;

import android.graphics.Bitmap;

public class HeroSprite extends Sprite
{
	private int		hp			= 1000;
	private int		attack		= 100;
	private int		defend		= 100;
	private int		experience	= 0;
	private int		money		= 0;

	private int		level		= 1;
	private int		yellowKey	= 100;
	private int		blueKey		= 100;
	private int		redKey		= 100;

	public boolean	canLookup	= false;
	public boolean	canJump		= false;

	private Task	task;


	public HeroSprite(Bitmap image, int frameWidth, int frameHeight)
	{
		super(image, frameWidth, frameHeight);

	}


	public void setTask(Task task)
	{
		this.task = task;
	}


	public boolean useKey(int key)
	{
		boolean result = false;
		switch (key)
		{
			case GameMap.MAP_YELLOW_DOOR:
				if (yellowKey > 0)
				{
					yellowKey--;
					result = true;
				}
				break;
			case GameMap.MAP_BLUE_DOOR:
				if (blueKey > 0)
				{
					blueKey--;
					result = true;
				}
				break;
			case GameMap.MAP_RED_DOOR:
				if (redKey > 0)
				{
					redKey--;
					result = true;
				}
				break;
		}
		return result;
	}


	// take gem
	public String takeGem(int type)
	{
		String result = "";
		switch (type)
		{
			case GameMap.MAP_YELLOW_KEY:
				yellowKey++;
				result = "获得一把黄钥匙！";
				break;
			case GameMap.MAP_BLUE_KEY:
				blueKey++;
				result = "获得一把蓝钥匙！";
				break;
			case GameMap.MAP_RED_KEY:
				redKey++;
				result = "获得一把红钥匙！";
				break;
			case GameMap.MAP_KEY_RING:
				yellowKey++;
				blueKey++;
				redKey++;
				result = "得到钥匙盒，各种钥匙数加1！";
				break;
			case GameMap.MAP_SMALL_BLOOD:
				hp += 200;
				result = "得到一个小血瓶，生命值加200！";
				break;
			case GameMap.MAP_BIG_BLOOD:
				hp += 500;
				result = "得到一个大血瓶，生命值加500！";
				break;
			case GameMap.MAP_RED_GEM:
				attack += 3;
				result = "得到一个红宝石，攻击力加3！";
				break;
			case GameMap.MAP_BLUE_GEM:
				defend += 3;
				result = "得到一个蓝宝石，防御力加3！";
				break;
			case GameMap.MAP_SWORD1:
				attack += 10;
				result = "得到铁剑，攻击加10！";
				break;
			case GameMap.MAP_SWORD2:
				attack += 40;
				result = "得到钢剑，攻击加40！";
				break;
			case GameMap.MAP_SWORD3:
				attack += 70;
				result = "得到青锋剑，攻击加70 ！";
				break;
			case GameMap.MAP_SWORD4:
				attack += 110;
				result = "得到圣光剑，攻击加110 ！";
				break;
			case GameMap.MAP_SWORD5:
				attack += 150;
				result = "得到星光神剑，攻击加150 ！";
				break;
			case GameMap.MAP_SHIELD1:
				defend += 10;
				result = "得到铁盾，，防御加10 ！";
				break;
			case GameMap.MAP_SHIELD2:
				defend += 30;
				result = "得到钢盾，防御加30 ！";
				break;
			case GameMap.MAP_SHIELD3:
				defend += 85;
				result = "得到黄金盾，防御加85 ！";
				break;
			case GameMap.MAP_SHIELD4:
				defend += 120;
				result = "得到星光盾，防御加 120 ！";
				break;
			case GameMap.MAP_SHIELD5:
				defend += 190;
				result = "得到光芒神盾，防御加190 ！";
				break;
			case GameMap.MAP_LOOKUP:
				canLookup = true;
				result = "得到圣光徽，按数字3键，可查看所有怪物的属性值！";
				break;
			case GameMap.MAP_JUMP:
				canJump = true;
				result = "得到风之罗盘，按数字1键，可在去过的楼层间自由上下！";
				break;
			case GameMap.MAP_LEVEL_UP1:
				levelUp(1);
				result = "得到小飞羽，等级提升一级 ！";
				break;
			case GameMap.MAP_LEVEL_UP3:
				levelUp(3);
				result = "得到大飞羽，等级提升三级 ！";
				break;
			case GameMap.MAP_COIN:
				money += 300;
				result = "得到金块，金币数加300 ！";
				break;
			case GameMap.MAP_DOUBLE_BLOOD:
				hp = hp * 2;
				result = "得到圣水瓶，将生命值加倍！";
				break;
			case GameMap.MAP_CROSS:
				task.updateTaskState(Task.FIND_CROSS);
				result = "得到 幸运十字架 把它交给仙子 可以提升所有属性值";
				break;
			case GameMap.MAP_AX:
				task.updateTaskState(Task.FIND_AX);
				result = "得到 星光神锒 把它交给小偷 可以打通十八层的隐藏地面";
				break;
		}
		return result;
	}


	public void levelUp(int value)
	{
		level += value;
		hp += 1000 * value;
		attack += 7 * value;
		defend += 7 * value;
	}


	public int getAttack()
	{
		return attack;
	}


	public int getHp()
	{
		return hp;
	}


	public int getDefend()
	{
		return defend;
	}


	public int getMoney()
	{
		return money;
	}


	public int getExperience()
	{
		return experience;
	}


	public void cutHp(int value)
	{
		hp -= value;
	}


	public void addHp(int value)
	{
		hp += value;
	}


	public void setHp(int value)
	{
		hp = value;
	}


	public void addMoney(int value)
	{
		money += value;
	}


	public void cutMoney(int value)
	{
		money -= value;
	}


	public void addExperience(int value)
	{
		experience += value;
	}


	public void cutExperience(int value)
	{
		experience -= value;
	}


	public void addAttack(int value)
	{
		attack += value;
	}


	public void addDefend(int value)
	{
		defend += value;
	}


	public void addYellowKey()
	{
		yellowKey++;
	}


	public void cutYellowKey()
	{
		yellowKey--;
	}


	public void addBlueKey()
	{
		blueKey++;
	}


	public void cutBlueKey()
	{
		blueKey--;
	}


	public void addRedKey()
	{
		redKey++;
	}


	public void cutRedKey()
	{
		redKey--;
	}


	public int getYellowKey()
	{
		return yellowKey;
	}


	public int getBlueKey()
	{
		return blueKey;
	}


	public int getRedKey()
	{
		return redKey;
	}


	public int getLevel()
	{
		return level;
	}


	public byte[] encode()
	{
		byte[] result = null;
		try
		{

			ByteArrayOutputStream bstream = new ByteArrayOutputStream();
			DataOutputStream ostream = new DataOutputStream(bstream);
			ostream.writeInt(level);
			ostream.writeInt(hp);
			ostream.writeInt(attack);
			ostream.writeInt(defend);
			ostream.writeInt(money);
			ostream.writeInt(experience);
			ostream.writeInt(yellowKey);
			ostream.writeInt(blueKey);
			ostream.writeInt(redKey);
			// canlookup
			if (canLookup == true)
			{
				ostream.writeInt(1);
			}
			else
			{
				ostream.writeInt(0);
			}
			// canjump
			if (canJump == true)
			{
				ostream.writeInt(1);
			}
			else
			{
				ostream.writeInt(0);
			}
			result = bstream.toByteArray();
		}
		catch (Exception e)
		{
			System.out.println("encode error::" + e);
		}
		return result;

	}


	// decode the byte[] from RMS
	public void decode(byte[] data)
	{
		try
		{

			ByteArrayInputStream bstream = new ByteArrayInputStream(data);
			DataInputStream istream = new DataInputStream(bstream);
			level = istream.readInt();
			hp = istream.readInt();
			attack = istream.readInt();
			defend = istream.readInt();
			money = istream.readInt();
			experience = istream.readInt();
			yellowKey = istream.readInt();
			blueKey = istream.readInt();
			redKey = istream.readInt();
			int can = istream.readInt();
			// canlookup
			if (can == 0)
			{
				canLookup = false;
			}
			else
			{
				canLookup = true;
			}
			can = istream.readInt();
			// canjump
			if (can == 0)
			{
				canJump = false;
			}
			else
			{
				canJump = true;
			}
		}
		catch (Exception e)
		{
			System.out.println("decode error::" + e);
		}
	}
}
