package org.metalslug;

import org.metalslugd.gameeffect.BoomPool;
import org.metalslugd.gameeffect.HitEffectPool;
import org.metalslugd.gameeffect.MetalHitEffectPool;
import org.metalslugd.gameeffect.PlayerHitEffectPool;
import org.metalslugd.gameobject.BulletPool;
import org.metalslugd.gameobject.EnemyBulletAPool;
import org.metalslugd.gameobject.EnemyKnifeAttack;
import org.metalslugd.gameobject.KnifeAttack;
import org.metalslugd.gameobject.enemy.EnemyAPool;
import org.metalslugd.gameobject.enemy.EnemyGrenade;
import org.metalslugd.gameobject.player.Player;
import org.metalslugd.gameobject.player.Weapon;
import org.metalslugd.mission.stages.Stage_1bk;
import org.metalslugd.stage.Ground;
import org.metalslugd.stage.PlayerGround;
import org.redengine.game.entity.REntityManager;
import org.redengine.systems.actionsystem.RActionManager;

/**
 * 游戏对象数据
 * <br>
 * Copyright (c) 2012 xujun. All rights reserved.
 */
public class GameObjectData {
	
	/**玩家对象*/
	public Player player;
	
	/**玩家刀判定池*/
	public KnifeAttack knifePool;
	
	/**玩家手枪子弹池*/
	public BulletPool bulletPool;
	
	/**手枪*/
	public Weapon.Handgun handgun;
	
	/**敌人大型机械*/
	//public EnemyMachine plane;
	
	/**敌人池*/
	public EnemyAPool enemyPool;
	
	/**敌人刀判定池*/
	public EnemyKnifeAttack enemyKnifePool;
	
	/**敌人手雷*/
	public EnemyGrenade.GeneralGrenade genralGrenad;
	
	/**敌人子弹池（手雷）*/
	public EnemyBulletAPool eBulletPool;
	
	/**敌人手雷击中效果池*/
	public PlayerHitEffectPool peffectPool;
	
	/**击中效果（机械）*/
	public MetalHitEffectPool metalHitEffectPool;
	
	/**击中效果池(敌人)*/
	public HitEffectPool effectPool;
	
	public BoomPool boomPool;
	
	
	
	public void initGameObjects(final REntityManager em,final RActionManager am){
		player=new Player();
		handgun=new Weapon.Handgun();
		genralGrenad=new EnemyGrenade.GeneralGrenade();
		player.init(am,300,300);
		em.addEntity("player",player);
		//TODO 对象池的装载==========================
		em.initPools(C.TOTAL_POOL);
		bulletPool=new BulletPool();em.addPool(bulletPool);
		effectPool=new HitEffectPool();em.addPool(effectPool);
		eBulletPool=new EnemyBulletAPool();em.addPool(eBulletPool);genralGrenad.initGrenade(eBulletPool);
		enemyKnifePool=new EnemyKnifeAttack();em.addPool(enemyKnifePool);
		enemyPool=new EnemyAPool(am,genralGrenad,enemyKnifePool);em.addPool(enemyPool);
		knifePool=new KnifeAttack();em.addPool(knifePool);
		peffectPool=new PlayerHitEffectPool();em.addPool(peffectPool);
		metalHitEffectPool=new MetalHitEffectPool();em.addPool(metalHitEffectPool);
		boomPool=new BoomPool();em.addPool(boomPool);
		//===========================================
		handgun.initWeapon(player, bulletPool, knifePool,player.anima_body_hand_fire,player.anima_body_hand_fire_up,
				player.anima_body_hand_fire_down,player.anima_crouch_hand_fire,
				player.anima_body_knife,player.anima_crouch_knife);
		player.changeWeapon(handgun);
	}
	
	
	
	
	private static GameObjectData god;
	
	private GameObjectData(){
		
	}
	
	public static final GameObjectData getGameObjectData(){
		if(god==null){
			god=new GameObjectData();
		}
		return god;
	}
}
