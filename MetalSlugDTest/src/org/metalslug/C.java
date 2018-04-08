package org.metalslug;

public class C {
	//TODO 碰撞掩码
	public static final int MARCO_BIT=0x0001;
	public static final int ENEMY_BIT=0x0002;
	public static final int BULLET_BIT=0x0004;
	public static final int GROUND_BIT=0x0008;
	public static final int PLAYER_GROUND_BIT=0x0010;
	public static final int ENEMY_BULLET_BIT=0x0020;
	public static final int ENEMY_BIGPLANE_BIT=0x0040;
	
	public static final int ENEMY_BULLET_FILTER=0xffDD;
	public static final int MARCO_FILTER=0xffff;
	public static final int ENEMY_FILTER=0xffED;
	public static final int BULLET_FILTER=0xfffB;
	public static final int GROUND_FILTER=0xffff;
	public static final int PLAYER_GROUND_FILTER=0xffDD;
	public static final int ENEMY_BIGPLANE_FILTER=0xffBf;
	
	
	//种类代码
	public static final int MARCO=7;
	public static final int ENEMY_BULLET=11;
	public static final int GROUD=13;
	public static final int BULLET=3;
	public static final int ENEMYA=17;
	public static final int ENEMYMACHINE=19;
	
	//池对象ID
	public static final int TOTAL_POOL=6;
	
	public static final int BULLET_POOL_ID=0;
	public static final int KNIFE_POOL_ID=1;
	public static final int HIT_EFFECT_POOL_ID=2;
	public static final int ENEMYA_POOL_ID=3;
	public static final int ENEMY_BULLET_A_POOL_ID=4;
	public static final int PLAYER_HIT_EFFECT_POOL_ID=5;
	
	
	//固定池对象数量
	public static final int BULLET_CAPACITY=8;
	public static final int HIT_EFFECT_CAPACITY = 5;
	public static final int ENEMY_CAPACITY=8;
	public static final int ENEMY_BULLET_A_CAPACITY=6;
	public static final int ENEMY_KNIFE_CAPACITY=5;
	public static final int PLAYER_HIT_EFFECT_CAPACITY = 3;
	
	//常量
	public static final float JUMP_VELO_Y=6.7f;
	public static final float P_STAND_VELO = 2.3f;
	public static final float P_CROUCH_VELO = 0.8f;
	public static final float P_JUMP_VELO = 2.5f;
	public static final float GROUND_ACCELERATE=-0.19f;
	public static final float GROUND_ACCELERATE2=-0.1f;
	public static final int ATTACK_DELAY = 180;
	public static final int KNIFE_DELAY=40;//(ms)
	public static final float ENEMYA_VELO=2.0f;
	
	
}
