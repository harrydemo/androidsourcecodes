package org.metalslugd.gameobject.player;

import org.redengine.game.entity.REntityPool;
import org.redengine.systems.graphsystem.RAnimation;
import org.redengine.systems.graphsystem.sprite.RBaseSprite;
import org.redengine.systems.mediasystem.RMediaSystem;

/**
 * 合金弹头D MetalSlugD 武器类
 * @date 2012.10
 * @author xujun 
 * @project MetalSlugD
 */
public abstract class Weapon {
	
	//fields--------
	protected REntityPool pool;       //存放子弹对象
	protected REntityPool attackPool; //刀子对象
	protected Player player;			//玩家对象
	protected RAnimation animaA,animaB,animaC,animaD;    //使用的动画
	protected RAnimation animaKnifeA,animaKnifeB;
	
	/**
	 * 装载武器所有的资源
	 * @param player
	 * @param pool
	 * @param anima
	 */
	public void initWeapon(Player player,REntityPool pool,REntityPool attackPool,RAnimation animaA,
			RAnimation animaB,RAnimation animaC,RAnimation animaD,
			RAnimation animaKnifeA,RAnimation animaKnifeB){
		this.player=player;
		this.attackPool=attackPool;
		this.pool=pool;
		this.animaA=animaA;
		this.animaB=animaB;
		this.animaC=animaC;
		this.animaD=animaD;
		this.animaKnifeA=animaKnifeA;
		this.animaKnifeB=animaKnifeB;
	}
	/**
	 * 水平射击
	 * @return
	 */
	public RAnimation getAnimationA(){
		if(player.gen_knife){
			return animaKnifeA;
		}
		return animaA;
	}
	/**
	 * 向上射击
	 * @return
	 */
	public RAnimation getAnimationB(){
		if(player.gen_knife){
			return animaKnifeA;
		}
		return animaB;
	}
	/**
	 * 向下射击
	 * @return
	 */
	public RAnimation getAnimationC(){
		if(player.gen_knife){
			return animaKnifeA;
		}
		return animaC;
	}
	/**
	 * 蹲射
	 * @return
	 */
	public RAnimation getAnimationD(){
		if(player.gen_knife){
			return animaKnifeB;
		}
		return animaD;
	}
	
	/**
	 * 水平发射子弹
	 */
	public abstract void attackA();
	
	/**
	 * 向上发射子弹
	 */
	public abstract void attackB();
	
	/**
	 * 向下发射子弹
	 */
	public abstract void attackC();
	
	/**
	 * 蹲射子弹
	 */
	public abstract void attackD();
	
	/*public static class Knife extends Weapon{

		@Override
		public void attackA() {
			
		}

		@Override
		public void attackB() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void attackC() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void attackD() {
			// TODO Auto-generated method stub
			
		}
		
	}*/
	/**
	 * 手枪
	 */
	public static class Handgun extends Weapon{
		//methods--------
		@Override
		public void attackA() {
			if(!player.gen_knife){
				//RMediaSystem.getMediaSystem().play("gun");
				pool.getEntity(player, 1);
			}else{
				attackPool.getEntity(player);
			}
		}

		@Override
		public void attackB() {
			if(!player.gen_knife){
				//RMediaSystem.getMediaSystem().play("gun");
				pool.getEntity(player, 2);
			}else{
				attackPool.getEntity(player);
			}
		}

		@Override
		public void attackC() {
			if(!player.gen_knife){
				//RMediaSystem.getMediaSystem().play("gun");
				pool.getEntity(player, 3);
			}else{
				attackPool.getEntity(player);
			}
		}

		public void attackD() {
			if(!player.gen_knife){
				//RMediaSystem.getMediaSystem().play("gun");
				pool.getEntity(player, 4);
			}else{
				attackPool.getEntity(player);
			}
		}
	}
	
	/**
	 * 重机枪
	 */
	/*public static class HeavyMachineGun extends Weapon{

		@Override
		public void attack() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void initWeapon(Player player, REntityPool pool, RAnimation anima) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public RAnimation getAnimationA() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public RAnimation getAnimationB() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}*/
}
