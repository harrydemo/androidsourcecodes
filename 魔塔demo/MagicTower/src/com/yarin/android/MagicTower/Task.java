package com.yarin.android.MagicTower;

public class Task
{
	public static final String[][] recieveDialog = {
		//FIND_CROSS
		{"......",
		"�����ˣ�",
		"......\n����˭���������",
		"������������ӣ��ղ��㱻С�ִ����ˡ�",
		"���������ҵĽ��أ�",
		"��Ľ������������ˣ���ֻ���ü����������",
		"��...�����أ��������ȹ����ġ�",
		"�������������أ���������ȥ�Ǵ򲻹������С�ֵġ�",
		"������ô�죿�Ҵ�Ӧ�˹���һ��Ҫ�ѹ����ȳ����ģ���������Ӧ����ô���أ�",
		"���İɣ��Ұ��ҵ���������㣬��Ϳ��Դ�Ӯ��ЩС�֡������������ȥ������һ���������ҵ��������������ҡ�",
		"�Ҷ�������ʲô������",
		"��һ��ʮ�ּܣ��м���һ�ź�ɫ�ı�ʯ��",
		"�Ǹ�������ʲô����",
		"�ұ��������������ػ��ߣ��ɲ���ǰ���ӱ�������һ����ħ������ռ�����������������ҵ�ħ�����������ʮ�ּ����棬������ܽ����������������ҵ�ħ����������Ļָ�������ʱ���ұ���԰����������ȥ�ȹ����ˡ�\nҪ��ס��ֻ�����ҵ�ħ�����ܴ򿪶�ʮһ����š�",
		"......\n�ðɣ������Կ���",
		"�ղ���ȥ�����ˣ���Ľ���������¥����Ķ�����¥�ϣ����Ǹ�ʮ�ּܱ����Ƿ�����¥�������ȡ����ý��Ͷܡ�\n���⣬�����������¥���ϣ�����һЩ����˺ü�����ı����ͱ�������õ����ǣ�������Ը�����Ĺ��ｫ�кܴ�İ�����",
		"���ǣ�����ô��ȥ�أ�",
		"������������Կ�ף�������ȥ���������滹�кܶ�������Կ�ף���һ��Ҫ��ϧʹ�á�\n�¸ҵ�ȥ�ɣ���ʿ��"},
		//FIND_AX
		{"��þ�����",
		"����������̫���ˣ����ֿ�Ҫ��������Ѱ���ˣ�\nŶ���һ�û�����ҽ��ܣ��ҽнܿˣ����⸽��������С͵��ʲô�����Ʊ���������͵����������������ɲ���̫�ã��ս����ͱ�ץ�ˡ���������Ҵ����ţ��ҾͰ�����һ���°ɡ�",
		"���߰ɣ����滹�кܶ����ҿ��ܹ˲����㣡",
		"���������������µġ���˵�ɣ�������ʲô��",
		"......\n��Ὺ����",
		"�ǵ�Ȼ��",
		"�Ǿ�������Ҵ򿪵ڶ�����Űɣ�",
		"�Ǹ��򵥣�������������ܰ����ҵ�һ��Ƕ�˺챦ʯ�����ͷ�Ļ����һ������ͨ��ʮ�˲��·��",
		"Ƕ�˺챦ʯ�����ͷ���ðɣ��Ұ������ң�",
		"�ǳ��ĸ�л��һ����ұ�Ὣ�ڶ�����Ŵ򿪡�������ҵ��Ǹ����ͷ�Ļ����������������ң�"},
		//GET_QINGFEND_JIAN
		{"���Ѿ��þ��ˣ�",
		 "Ŷ�������ʵ̫��л���ˣ����Ǹ����ˣ���֪��Ϊʲô��ץ���������ˡ�",
		 "���߰ɣ��������Ѿ������ˣ�",
		 "Ŷ���ԶԶԣ����Ѿ������ˡ�����������͸���ɣ��ұ�����׼����Ǯ�ġ�����������һ�����а�����"},
		//GET_HUANGJIN_DUN
		{"���Ѿ��þ��ˣ�",
		 "Ŷ���ҵĺ��ӣ���ʵ̫��л���ˣ�����ط������л���������ǿ������ȥ�ˣ�",
		 "���߰ɣ��һ��þ��߱���������Ĺ�����",
		 "Ŷ���������ȹ����ģ�Ϊ�˱�ʾ����ø�л������������͸���ɣ��⻹���������ʱ���ù����أ�������ȥ��ȹ����ɣ�"},
		//GET_SHENGGUANG_JIAN
		{"��ã����˼ң�",
		"��ã��������500�㾭�飬�ҿ��������ǿ��",
		"����������û�а���",
		"���ź����������㹻�ľ���ʱ�����ɡ�"},
		//GET_XINGGUANG_DUN
		{"��ã����˼ң�",
		"��ã��������800����ң��ҿ��������ǿ��",
		"����������û�а���",
		"���ź����������㹻�ľ���ʱ�����ɡ�"},
		//RESCUE_PRINCESS
		{"��������þ��ˣ�",
		"�������������ҵ���",
		"�ǵģ��ҷ�����������������!��������ҳ�ȥ�ɣ�",
		"�����һ������ߣ�",
		"Ϊʲô�������浽�����Ƕ�ħ��",
		"������Ϊ���ﵽ�����Ƕ�ħ�����ԲŲ����Ծ�������ȥ����Ҫ�����Ǹ���ħ��ɱ����Ӣ�۵���ʿ��������ǽ��Ǹ����ħɱ�����Ҿͺ���һ���ȥ��",
		"���ħ�����Ѿ�ɱ����һ��ħ����",
		"���ħ�����������㣬��ɱ���Ŀ�����һ��С�ӳ�֮��Ķ�ħ��",
		"�ã�����ţ�����ɱ���Ǹ���ħ�����������㣡",
		"���ħ����ղ�ɱ�����������ˣ��������������������ħ�������Ĺ������ͷ�����������������һ�����ϣ����С�ģ���һ��ɱ����ħ����"},
		//FIGHT_BOSS
		{"��ħͷ��������ڵ��ˣ�",
		"������......\n��Ҳ��������˼������Ϊ�����Ǽһ������������Ϳ��Դ���ң��������㻹�����أ�",
		"�ϻ���˵��ȥ���ɣ�",
		"���������㻹�������ӣ��б���Ļ���21�㣡�������Ϳ��Լ�����������ʵ���ˣ�"}
	};
	public static final String[][] finishedDialog = {
		//FIND_CROSS
		{"���ӣ����Ѿ����Ǹ�ʮ�ּ��ҵ��ˣ�",
		"�����ĺܺã���ô�����ҾͿ�ʼ�������ǿ��������\n�������仩......\n�������Ѿ��������ڵ����������ˣ���ס�������û���㹻��ʵ���Ļ�����Ҫȥ��ʮһ�㣡����һ��������б���ķ�������ʧȥ���ã�"},
		//FIND_AX
		{"�����쿴�����ҵ�ʲô�ˣ�",
		"̫���ˣ����������Ȼ��������ðɣ������ȥ�����޺�ʮ�˲��·�档"},
		//GET_QINGFEND_JIAN
		{"��þ��ˣ�",
		"Ŷ��лл�������ˣ�Ϊ��л��������һ���������������Ĺ�������",
		"лл�������˼�",
		"��������"},
		//GET_HUANGJIN_DUN
		{"��þ��ˣ�",
		"Ŷ��лл�������ˣ�Ϊ��л��������һ���������������ķ�������",
		"лл�������˼�",
		"��������"},
		//GET_SHENGGUANG_JIAN
		{"��ã����˼ң�",
		"��ã��������500�㾭�飬�ҿ��������ǿ��",
		"�Ǻã������㹻�ľ��顣",
		"�һ�����Ĺ���������110�㡣"},
		//GET_XINGGUANG_DUN
		{"��ã����˼ң�",
		"��ã��������800����ң��ҿ��������ǿ��",
		"�Ǻã������㹻�Ľ�ҡ�",
		"�һ�����ķ���������120�㡣"},
		//RESCUE_PRINCESS
		{"���������Ѿ��������Ǹ�ħ����",
		"�õġ������ǻ�ȥ�ɣ���ȥ����ѧϰAndroid�����أ�"},
		//FIGHT_BOSS
		{"......���������ɣ�",
		"��̫�����ˣ���ƾ���ʵ�������һ����أ�",
		"��ѧϰ�˺ܳ�ʱ���Android�������������ˣ�",
		"�ã����ɣ�"}
	};
	//hold the task state
	private static final int UNRECIEVE = 0,  //don't recieve task
							 RECIEVED = 1,   //have recieved task,can't finish
							 CANFINISH = 2,  //can finish task,but no finish
							 FINISHED = 3;   //finished
	//hold the task type
	public static final int  FIND_CROSS = 0,
							 FIND_AX = 1,
							 GET_QINGFEND_JIAN = 2,
							 GET_HUANGJIN_DUN = 3,
							 GET_SHENGGUANG_JIAN = 4,
							 GET_XINGGUANG_DUN = 5,
							 RESCUE_PRINCESS = 6,
							 FIGHT_BOSS = 7;
							 
	private byte[] taskState = 
		{
		0, //0:find cross                      (7),
		0, //1:find ax                         (12),
		2, //2:get qingfeng jian--MAP_SWORD3   (2),
		2, //3:get huangjin dun--MAP_SHIELD3   (2),
		1, //4:get shengguang jian--MAP_SWORD4 (15),
		1, //5:get xingguang dun MAP_SHIELD4   (15),
		0, //6:rescue princess                 (18),
		0  //7:fight boss                      (19),
		};
	private GameScreen gameScreen;
	private HeroSprite hero;
	private GameMap gameMap;
	public int curTask;
	public int curTask2;
	public boolean mbtask = false;
	
	public Task(GameScreen gameScreen,HeroSprite hero,GameMap gameMap)
	{
		this.gameScreen = gameScreen;
		this.hero = hero;
		this.gameMap = gameMap;
	}
	
	public void execTask(int curTask)
	{
		this.curTask = curTask;
		this.curTask2 = 0;
		switch (taskState[curTask])
		{
			case UNRECIEVE:
				mbtask = false;	
				gameScreen.mshowDialog = true;
				gameScreen.tu.InitText(recieveDialog[curTask][curTask2], 0, (yarin.SCREENH-yarin.MessageBoxH)/2, yarin.SCREENW, yarin.MessageBoxH, 0x0, 0xffff00, 255, yarin.TextSize);
				//gameScreen.dialog(recieveDialog[curTask]);
				recieveTask();
				break;
			case RECIEVED:
				if (curTask == GET_SHENGGUANG_JIAN)
				{
					if (hero.getExperience() >= 500)
					{
						mbtask = true;	
						gameScreen.mshowDialog = true;
						gameScreen.tu.InitText(finishedDialog[curTask][curTask2], 0, (yarin.SCREENH-yarin.MessageBoxH)/2, yarin.SCREENW, yarin.MessageBoxH, 0x0, 0xffff00, 255, yarin.TextSize);
						
						//gameScreen.dialog(finishedDialog[curTask]);
						finishTask();
						hero.cutExperience(500);
					}
					else
					{
						mbtask = false;	
						gameScreen.mshowDialog = true;
						gameScreen.tu.InitText(recieveDialog[curTask][curTask2], 0, (yarin.SCREENH-yarin.MessageBoxH)/2, yarin.SCREENW, yarin.MessageBoxH, 0x0, 0xffff00, 255, yarin.TextSize);
						//gameScreen.dialog(recieveDialog[curTask]);
						recieveTask();
					}
				}
				else if (curTask == GET_XINGGUANG_DUN)
				{
					if (hero.getMoney() >= 800)
					{
						mbtask = true;	
						gameScreen.mshowDialog = true;
						gameScreen.tu.InitText(finishedDialog[curTask][curTask2], 0, (yarin.SCREENH-yarin.MessageBoxH)/2, yarin.SCREENW, yarin.MessageBoxH, 0x0, 0xffff00, 255, yarin.TextSize);
						//gameScreen.dialog(finishedDialog[curTask]);
						finishTask();
						hero.cutMoney(800);
					}
					else
					{
						mbtask = false;	
						gameScreen.mshowDialog = true;
						gameScreen.tu.InitText(recieveDialog[curTask][curTask2], 0, (yarin.SCREENH-yarin.MessageBoxH)/2, yarin.SCREENW, yarin.MessageBoxH, 0x0, 0xffff00, 255, yarin.TextSize);
						//gameScreen.dialog(recieveDialog[curTask]);
						recieveTask();
					}
				}
				break;
			case CANFINISH:
				mbtask = true;	
				gameScreen.mshowDialog = true;
				gameScreen.tu.InitText(finishedDialog[curTask][curTask2], 0, (yarin.SCREENH-yarin.MessageBoxH)/2, yarin.SCREENW, yarin.MessageBoxH, 0x0, 0xffff00, 255, yarin.TextSize);
				//gameScreen.dialog(finishedDialog[curTask]);
				finishTask();
				break;
			case FINISHED:
				break;
		}
	}
	
	private void recieveTask()
	{
		taskState[curTask]++;
		switch (curTask)
		{
			case FIND_CROSS:
				gameMap.remove();
				gameMap.changeCell(92, GameMap.MAP_ANGLE);
				hero.addYellowKey();
				hero.addBlueKey();
				hero.addRedKey();
				break;
			case FIND_AX:
				gameMap.remove(2, 67);
				break;
			case GET_SHENGGUANG_JIAN:
				taskState[curTask]--;
				break;
			case GET_XINGGUANG_DUN:
				taskState[curTask]--;
				break;
			case RESCUE_PRINCESS:
				gameMap.changeCell(120, GameMap.MAP_UPSTAIR);
				break;
			case FIGHT_BOSS:
				gameMap.remove();
				taskState[curTask]++;
				break;
		}
	}
	private void finishTask()
	{
		taskState[curTask]++;
		switch (curTask)
		{
			case FIND_CROSS:
				hero.addHp(hero.getHp() / 3);
				hero.addAttack(hero.getAttack() / 3);
				hero.addDefend(hero.getDefend() / 3);
				break;
			case FIND_AX:
				gameMap.remove();
				gameMap.remove(18, 49, GameMap.MAP_PRINCESS);
				gameMap.remove(18, 60, GameMap.MAP_BARRIER);
				gameMap.remove(18, 71, GameMap.MAP_RED_DOOR);
				gameMap.remove(18, 82, GameMap.MAP_RED_DOOR);
				break;
			case GET_QINGFEND_JIAN:
				gameScreen.showMessage(hero.takeGem(GameMap.MAP_SWORD3));
				break;
			case GET_HUANGJIN_DUN:
				gameScreen.showMessage(hero.takeGem(GameMap.MAP_SHIELD3));
				break;
			case GET_SHENGGUANG_JIAN:
				gameScreen.showMessage(hero.takeGem(GameMap.MAP_SWORD4));
				break;
			case GET_XINGGUANG_DUN:
				gameScreen.showMessage(hero.takeGem(GameMap.MAP_SHIELD4));
				break;
			case RESCUE_PRINCESS:
				gameScreen.end();
				break;
			case FIGHT_BOSS:
				if (gameScreen.fight(GameMap.MAP_ORGE31) == true)
				{
					gameMap.changeCell(81, GameMap.MAP_ROAD);
					gameMap.changeCell(83, GameMap.MAP_ROAD);
					taskState[RESCUE_PRINCESS]++;
				}
				break;
		}
	}
	
	public void updateTaskState(int type)
	{
		taskState[type] ++;
	}
	
	public byte[] getTask()
	{
		return taskState;
	}
	
	public void setTask(byte[] data)
	{
		taskState = data;
	}
}

