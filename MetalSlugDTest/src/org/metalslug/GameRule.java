package org.metalslug;

//为了简化就使用了public对象

public class GameRule {
	
	public int score;			//得分
	
	public int comb;            //连击数
	
	public int rate=1;
	
	public int totalBullet;     //子弹射出数
	
	public int hitBullet;		//击中数量
	
	public int deadCount;		//死亡数
	
	public int knifeUseCount;   //出刀数
	
	public long costTime;		//所用时间(ms)
	
	public boolean wiatComb;
	
	public void clearState(){
		score=0;comb=0;
		rate=1;
	}
	
	//在mission模式中子弹击中分数为100，刀子击中分数为500.
	//得到高评价的要求：1、死亡次数为0 
	//2、击中率大于60%	3、当出刀数占总攻击次数的40%以上时加分
	//4、所用时间少得分高		5、连击奖励。
	//当在有效攻击（攻击已死亡的敌人或未击倒的攻击不算）后的5s内再次出现有效攻击
	//则连击数+1连击数奖励：
	//3-10：分数2倍
	//10-15: 分数3倍
	//15-20: 分数4倍
	//20-30: 分数8倍
	//30-50: 分数16倍
	//当死亡或超时，连击归零
	
	//另外打败特别的敌人会有奖励（倍率同样有效）
	//0死亡 +10000
	//高命中 +10000
	//刀客 +20000
	//向着高分努力吧！shaonian！
	
	public void addScore(int score){
		this.score+=score*rate;
	}
	
	public void addComb(){
		this.comb++;
		if(comb>2&&comb<=5) {rate=2;return;}
		if(comb>5&&comb<=10) {rate=4;return;}
		if(comb>10&&comb<=15){rate=8;return;}
		if(comb>15&&comb<=30) {rate=16;return;}
		if(comb>30) rate=32;
	}
}
