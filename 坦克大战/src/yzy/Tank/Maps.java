package yzy.Tank;

import java.util.ArrayList;

public class Maps {
	public static int[][] pathA = {//坦克行走路径 坐标
		{854, -146},//路径中每个点的X坐标
		{376, 376},//路径中每个点的Y坐标
		{1000,1000 },//路径中两点间的步数
	};
	public static int[][] pathB = { //飞碟路径 坐标 
		{854, 654, 347, 201,-101},//路径中每个点的X坐标
		{67 , 267, 230, 180,200},//路径中每个点的Y坐标
		{300, 300, 246, 302,300},//路径中两点间的步数
	};
	public static int[][] pathC = { //飞碟路径 坐标 2
		{854, 754, 647, 401, 300, 100, -120},//路径中每个点的X坐标
		{300, 200, 400, 280 , 130, 250, 164},//路径中每个点的Y坐标
		{300, 250, 250, 250, 200, 250, 285},//路径中两点间的步数
	};
	public static int[][] pathK = { //boss 路径
		{854, -283},//路径中每个点的X坐标
		{267, 267},//路径中每个点的Y坐标
		{1137},//路径中两点间的步数
	};
	public static int[][] pathJ = { //血块的路径
		{854, -146},//路径中每个点的X坐标
		{376, 376},//路径中每个点的Y坐标
		{1000,1000},//路径中两点间的步数
	};
	public static ArrayList<Life> getFirstLife(){//为第一关添加血块
		ArrayList<Life> lifes = new ArrayList<Life>();
		lifes.add(new Life(0, 1, 0, pathJ, false, 190));
		lifes.add(new Life(0, 1, 0, pathJ, false, 370));
		lifes.add(new Life(0, 1, 0, pathJ, false, 470));
		lifes.add(new Life(0, 1, 0, pathJ, false, 570));
		lifes.add(new Life(0, 1, 0, pathJ, false, 640));
		return lifes;
	}

	
	//出发点、目标点、当前段上的第几步、路径数组、状态、触发时间、类型、生命
	public static ArrayList<EnemyTank> getFirst(){
		ArrayList<EnemyTank> EnemyTanks = new ArrayList<EnemyTank>();
		
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, (int)(Math.random()*1000)+(int)(Math.random()*50), 1, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathB, false, (int)(Math.random()*800)+(int)(Math.random()*50), 4, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, (int)(Math.random()*900)+(int)(Math.random()*50), 2, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, (int)(Math.random()*999)+(int)(Math.random()*50), 3, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathC, false, (int)(Math.random()*899)+(int)(Math.random()*50), 4, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, (int)(Math.random()*799)+(int)(Math.random()*50), 1, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathB, false, (int)(Math.random()*1100)+(int)(Math.random()*50), 4, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, (int)(Math.random()*788)+(int)(Math.random()*50), 2, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, (int)(Math.random()*888)+(int)(Math.random()*50), 3, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathC, false, (int)(Math.random()*999)+(int)(Math.random()*50), 4, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, (int)(Math.random()*1111)+(int)(Math.random()*50), 1, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathB, false, (int)(Math.random()*777)+(int)(Math.random()*50), 4, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, (int)(Math.random()*1000)+(int)(Math.random()*50), 2, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, (int)(Math.random()*1000)+(int)(Math.random()*30), 3, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathC, false, (int)(Math.random()*1000)+(int)(Math.random()*60), 4, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, (int)(Math.random()*600)+(int)(Math.random()*550), 1, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathB, false, (int)(Math.random()*1000)+(int)(Math.random()*50), 4, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, (int)(Math.random()*1000)+(int)(Math.random()*20), 2, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, (int)(Math.random()*1000)+(int)(Math.random()*30), 3, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathC, false, (int)(Math.random()*1000)+(int)(Math.random()*40), 4, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, (int)(Math.random()*1000)+(int)(Math.random()*70), 1, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathB, false, (int)(Math.random()*1000)+(int)(Math.random()*80), 4, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, (int)(Math.random()*1000)+(int)(Math.random()*50), 2, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, (int)(Math.random()*1000)+(int)(Math.random()*50), 3, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathC, false, (int)(Math.random()*1000)+(int)(Math.random()*40), 4, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, (int)(Math.random()*1000)+(int)(Math.random()*33), 1, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, (int)(Math.random()*1000)+(int)(Math.random()*50), 2, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, (int)(Math.random()*1000)+(int)(Math.random()*77), 3, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathB, false, (int)(Math.random()*1000)+(int)(Math.random()*99), 4, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, (int)(Math.random()*1000)+(int)(Math.random()*50), 1, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, (int)(Math.random()*1000)+(int)(Math.random()*50), 2, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathC, false, (int)(Math.random()*1000)+(int)(Math.random()*50), 4, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, (int)(Math.random()*1000)+(int)(Math.random()*22), 3, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathB, false, (int)(Math.random()*1000)+(int)(Math.random()*50), 4, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, (int)(Math.random()*1000)+(int)(Math.random()*11), 1, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathC, false, (int)(Math.random()*1000)+(int)(Math.random()*50), 4, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, (int)(Math.random()*1000)+(int)(Math.random()*88), 2, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, (int)(Math.random()*1000)+(int)(Math.random()*67), 3, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathB, false, (int)(Math.random()*1000)+(int)(Math.random()*50), 4, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, (int)(Math.random()*1000)+(int)(Math.random()*34), 1, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathC, false, (int)(Math.random()*1000)+(int)(Math.random()*50), 4, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, (int)(Math.random()*789)+(int)(Math.random()*21), 2, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, (int)(Math.random()*1000)+(int)(Math.random()*32), 3, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathB, false, (int)(Math.random()*1000)+(int)(Math.random()*50), 4, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, (int)(Math.random()*1000)+(int)(Math.random()*13), 1, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathC, false, (int)(Math.random()*980)+(int)(Math.random()*22), 4, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, (int)(Math.random()*1000)+(int)(Math.random()*44), 2, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathB, false, (int)(Math.random()*1000)+(int)(Math.random()*55), 4, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, (int)(Math.random()*1000)+(int)(Math.random()*88), 3, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathC, false, (int)(Math.random()*990)+(int)(Math.random()*88), 4, 1));
		/*EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, 30, 1, 1));
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathB, false, 80, 4, 1));
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, 80, 3, 1));
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, 120, 2, 1));
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathB, false, 150, 4, 1));
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathC, false, 140, 4, 1));
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, 180, 1, 1));
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, 200, 2, 1));
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathB, false, 250, 4, 1));
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, 260, 3, 1));
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathC, false, 300, 4, 1));
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, 300, 2, 1));
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, 360, 3, 1));
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, 370, 1, 1));
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathB, false, 380, 4, 1));
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, 390, 1, 1));
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathB, false, 400, 4, 1));
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, 410, 3, 1));
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, 420, 2, 1));
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathB, false, 450, 4, 1));
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathC, false, 440, 4, 1));
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, 480, 1, 1));
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, 400, 2, 1));
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathB, false, 450, 4, 1));
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, 460, 3, 1));
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathC, false, 500, 4, 1));
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, 520, 2, 1));
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, 510, 3, 1));
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, 580, 1, 1));
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathB, false, 600, 4, 1));

		EnemyTanks.add(new EnemyTank(0, 1, 0, pathB, false, 610, 4, 1));
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, 620, 1, 1));
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathB, false, 630, 4, 1));
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, 650, 3, 1));
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, 660, 2, 1));
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathB, false, 680, 4, 1));
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathC, false, 690, 4, 1));
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, 700, 1, 1));
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, 720, 2, 1));
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathB, false, 740, 4, 1));*/
		
		
		
		/*for(int i = 0 ; i<100 ;i=i+10){
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, 740+i, 2, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, 840+i+i, 1, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathC, false, 840+i+i+3, 4, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, 940+i+i+5, 3, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathC, false, 1040+i+i+3, 4, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathC, false, 1140+i+i+2, 4, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathA, false, 1240+i+i+2, 2, 1));
			EnemyTanks.add(new EnemyTank(0, 1, 0, pathC, false, 1340+i+i+2, 4, 1));
		}*/
		
		EnemyTanks.add(new EnemyTank(0, 1, 0, pathK, false, 1100, 5, 10));//关口BOSS 
		return EnemyTanks;
	}
}