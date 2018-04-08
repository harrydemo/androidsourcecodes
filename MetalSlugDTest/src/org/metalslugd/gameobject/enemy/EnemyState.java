package org.metalslugd.gameobject.enemy;

import org.metalslug.C;
import org.redengine.systems.actionsystem.RActionManager;
import org.redengine.systems.actionsystem.actionex.RState;
import org.redengine.systems.common.V2;

/**
 * ���˵�״̬��
 * @author xujun
 */

public abstract class EnemyState extends RState {

	protected EnemyA enemy;
	
	public EnemyState(EnemyA enemy,RActionManager m){
		this.enemy=enemy;
		this.setActionManager(m);
	}
	
	public void StateEnd() {
		this.setFlag(false);//���ռ��
	}
	
	public void dead(){
		enemy.changeState(enemy.stateDead);
	}
	
	public void onAir(){
		enemy.gen_canJump=false;
		//enemy.changeState(enemy.stateJump);
	}
	
	public void onGround(){
		enemy.gen_canJump=true;
		//enemy.changeState(enemy.stateMove);
	}
	
	
	//TODO  ֹͣ״̬
	public static class StateIdle extends EnemyState{

		public StateIdle(EnemyA enemy, RActionManager m) {
			super(enemy, m);
		}

		@Override
		public void StateInit() {
			enemy.getSprite().setSpriteWidthHeight(EnemyA.SPR_W_1, EnemyA.SPR_H_1);
			enemy.getPhysicsObject().getMoveHandler().clearVeloX();
			enemy.getSprite().setAnima(enemy.anima_stop);
			if(enemy.gen_right){
				enemy.getSprite().clearVerticalMirrorFlag();
			}else{
				enemy.getSprite().setVerticalMirrorFlag();
			}
		}

		@Override
		public void StateRunning() {
			if(enemy.gen_right){
				enemy.getSprite().clearVerticalMirrorFlag();
			}else{
				enemy.getSprite().setVerticalMirrorFlag();
			}
		}
	}
	
	//TODO �ƶ�״̬
	public static class StateMove extends EnemyState{
		public StateMove(EnemyA enemy, RActionManager m) {
			super(enemy, m);
		}

		@Override
		public void StateInit() {
			enemy.getSprite().setSpriteWidthHeight(EnemyA.SPR_W_1, EnemyA.SPR_H_1);
			final int rate=enemy.gen_right?1:-1;
			enemy.getPhysicsObject().getMoveHandler().setVeloX(C.ENEMYA_VELO*rate);
			enemy.getSprite().setAnima(enemy.anima_stand_move);
			if(enemy.gen_right){
				enemy.getSprite().clearVerticalMirrorFlag();
			}else{
				enemy.getSprite().setVerticalMirrorFlag();
			}
		}

		@Override
		public void StateRunning() {
			final int rate=enemy.gen_right?1:-1;
			enemy.getPhysicsObject().getMoveHandler().setVeloX(C.ENEMYA_VELO*rate);
			enemy.getSprite().setAnima(enemy.anima_stand_move);
			if(enemy.gen_right){
				enemy.getSprite().clearVerticalMirrorFlag();
			}else{
				enemy.getSprite().setVerticalMirrorFlag();
			}
		}
	}
	
	//TODO������
	public static class StateKnife extends EnemyState{

		public StateKnife(EnemyA enemy, RActionManager m) {
			super(enemy, m);
		}

		public void StateInit() {
			enemy.getPhysicsObject().getMoveHandler().clearVeloX();
			enemy.getSprite().setSpriteWidthHeight(EnemyA.SPR_W_2, EnemyA.SPR_H_2);
			enemy.getSprite().setAnima(enemy.anima_attack_stand_knife);
			if(enemy.gen_right){
				enemy.getSprite(0).clearVerticalMirrorFlag();
			}else{
				enemy.getSprite(0).setVerticalMirrorFlag();
			}
		}

		@Override
		public void StateRunning() {
			if(waitAnimation(enemy.anima_attack_stand_knife,5)){
				enemy.knife.getEntity(enemy);
			}
			if(waitAnimation(enemy.anima_attack_stand_knife)){
				enemy.changeState(enemy.stateIdle);
			}
		}
		
	}
	
	//TODO ������
	public static class StateGrenade extends EnemyState{

		public StateGrenade(EnemyA enemy, RActionManager m) {
			super(enemy, m);
		}

		public void StateInit() {
			enemy.getPhysicsObject().getMoveHandler().clearVeloX();
			enemy.getSprite().setAnima(enemy.anima_attack_stand_grenade);
			if(enemy.gen_right){
				enemy.getSprite(0).clearVerticalMirrorFlag();
			}else{
				enemy.getSprite(0).setVerticalMirrorFlag();
			}
		}

		@Override
		public void StateRunning() {
			this.setFlag(true);//����ռ��
			if(waitAnimation(enemy.anima_attack_stand_grenade,11)){
				V2 v=enemy.getPhysicsObject().getPosition();
				enemy.weapon.attack(enemy);
				//enemy.getEnityManager().getPool(C.ENEMY_BULLET_A_POOL_ID).getEntity(v);
			}
			if(waitAnimation(enemy.anima_attack_stand_grenade)){
				this.setFlag(false);//���ռ��
				enemy.changeState(enemy.stateIdle);
			}
		}
		
	}
	
	//TODO ��Ծ
	public static class StateJump extends EnemyState{

		public StateJump(EnemyA enemy, RActionManager m) {
			super(enemy, m);
		}

		@Override
		public void StateInit() {
			enemy.anima_jump.reset();
			enemy.anima_jump.setStateRun();
			enemy.getSprite().setSpriteWidthHeight(EnemyA.SPR_W_1, EnemyA.SPR_H_1);
			enemy.getSprite().setAnima(enemy.anima_jump);
			if(enemy.gen_right){
				enemy.getSprite().clearVerticalMirrorFlag();
				if(enemy.gen_canJump){
					enemy.getPhysicsObject().getMoveHandler().setVelo(2,C.JUMP_VELO_Y);
					enemy.gen_canJump=false;
				}
			}
			else {
				enemy.getSprite().setVerticalMirrorFlag();
				if(enemy.gen_canJump){
					enemy.getPhysicsObject().getMoveHandler().setVelo(-2,C.JUMP_VELO_Y);
					enemy.gen_canJump=false;
				}
			}
		}

		@Override
		public void StateRunning() {
			if(waitAnimation(enemy.anima_jump)){
				enemy.anima_jump.setStatePause();
			}
			if(enemy.gen_right)enemy.getSprite().clearVerticalMirrorFlag();
			else enemy.getSprite().setVerticalMirrorFlag();
		}
	}
	
	//TODO ����
	public static class StateDead extends EnemyState{

		public StateDead(EnemyA enemy, RActionManager m) {
			super(enemy, m);
		}

		@Override
		public void StateInit() {
			enemy.gen_dead=true;
			enemy.anima_dead.reset();
			enemy.getSprite().setSpriteWidthHeight(EnemyA.SPR_W_1, EnemyA.SPR_H_1);
			enemy.getPhysicsObject().getMoveHandler().clearVeloX();
			enemy.getSprite().setAnima(enemy.anima_dead);
			if(enemy.gen_right){
				enemy.getSprite().clearVerticalMirrorFlag();
			}else{
				enemy.getSprite().setVerticalMirrorFlag();
			}
		}

		@Override
		public void StateRunning() {
			if(waitAnimation(enemy.anima_dead)){
				enemy.cycle();
			}
		}
		
		/**
		 * ����һ���շ�����ֹdead��dead��״̬ת��
		 */
		public void dead(){
			
		}
		
	}
}
