package org.metalslugd.gameobject.player;

import org.redengine.game.entity.REntityPool;
import org.redengine.systems.graphsystem.RAnimation;
import org.redengine.systems.graphsystem.sprite.RBaseSprite;
import org.redengine.systems.mediasystem.RMediaSystem;

/**
 * �Ͻ�ͷD MetalSlugD ������
 * @date 2012.10
 * @author xujun 
 * @project MetalSlugD
 */
public abstract class Weapon {
	
	//fields--------
	protected REntityPool pool;       //����ӵ�����
	protected REntityPool attackPool; //���Ӷ���
	protected Player player;			//��Ҷ���
	protected RAnimation animaA,animaB,animaC,animaD;    //ʹ�õĶ���
	protected RAnimation animaKnifeA,animaKnifeB;
	
	/**
	 * װ���������е���Դ
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
	 * ˮƽ���
	 * @return
	 */
	public RAnimation getAnimationA(){
		if(player.gen_knife){
			return animaKnifeA;
		}
		return animaA;
	}
	/**
	 * �������
	 * @return
	 */
	public RAnimation getAnimationB(){
		if(player.gen_knife){
			return animaKnifeA;
		}
		return animaB;
	}
	/**
	 * �������
	 * @return
	 */
	public RAnimation getAnimationC(){
		if(player.gen_knife){
			return animaKnifeA;
		}
		return animaC;
	}
	/**
	 * ����
	 * @return
	 */
	public RAnimation getAnimationD(){
		if(player.gen_knife){
			return animaKnifeB;
		}
		return animaD;
	}
	
	/**
	 * ˮƽ�����ӵ�
	 */
	public abstract void attackA();
	
	/**
	 * ���Ϸ����ӵ�
	 */
	public abstract void attackB();
	
	/**
	 * ���·����ӵ�
	 */
	public abstract void attackC();
	
	/**
	 * �����ӵ�
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
	 * ��ǹ
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
	 * �ػ�ǹ
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
