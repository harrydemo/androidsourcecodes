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
 * ��Ϸ��������
 * <br>
 * Copyright (c) 2012 xujun. All rights reserved.
 */
public class GameObjectData {
	
	/**��Ҷ���*/
	public Player player;
	
	/**��ҵ��ж���*/
	public KnifeAttack knifePool;
	
	/**�����ǹ�ӵ���*/
	public BulletPool bulletPool;
	
	/**��ǹ*/
	public Weapon.Handgun handgun;
	
	/**���˴��ͻ�е*/
	//public EnemyMachine plane;
	
	/**���˳�*/
	public EnemyAPool enemyPool;
	
	/**���˵��ж���*/
	public EnemyKnifeAttack enemyKnifePool;
	
	/**��������*/
	public EnemyGrenade.GeneralGrenade genralGrenad;
	
	/**�����ӵ��أ����ף�*/
	public EnemyBulletAPool eBulletPool;
	
	/**�������׻���Ч����*/
	public PlayerHitEffectPool peffectPool;
	
	/**����Ч������е��*/
	public MetalHitEffectPool metalHitEffectPool;
	
	/**����Ч����(����)*/
	public HitEffectPool effectPool;
	
	public BoomPool boomPool;
	
	
	
	public void initGameObjects(final REntityManager em,final RActionManager am){
		player=new Player();
		handgun=new Weapon.Handgun();
		genralGrenad=new EnemyGrenade.GeneralGrenade();
		player.init(am,300,300);
		em.addEntity("player",player);
		//TODO ����ص�װ��==========================
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
