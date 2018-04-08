package org.metalslugd.gameobject.player;

import org.redengine.systems.actionsystem.RAction;
import org.redengine.systems.actionsystem.RActionManager;
import org.redengine.systems.actionsystem.RModified;
import org.redengine.systems.graphsystem.sprite.RBaseSprite;

/**
 * 上半身状态 	MetalSlugD
 * @author xujun
 */

public abstract class StateUp extends RAction {
	
	Player player;
	
	public StateUp(Player player,RActionManager m){
		this.setActionManager(m);
		this.player=player;
	}
	
	public abstract void StateInit();
	public abstract void StateRunning();
	public void StateEnd(){
		kill();
	}
	
	public void reLife(){
		if(player.gen_dead){
			player.changeBodyState(player.s_up_idle);
		}
	}
	
	
	/**
	 * 都可以发送到执行器
	 * @return
	 */
	public boolean canSendToManager(){
		return true;
	}
	
	/**
	 * 当不需要上身显示的状态时调用次方法
	 */
	public void crouch(){}
	
	public void stand(){
		//player.changeBodyState(player.s_up_idle);
	}
	
	//public void down(){
		//if(!player.gen_canJump){
			//player.changeBodyState(player.s_up_down);
		//}
	//}
	
	public void onAir(){}
	public void onGround(){}
	public void dead(){
		if(player.gen_dead)return;
		player.changeBodyState(player.s_up_dead);
	}
	public void up(){}
	public void attack(){}
	public void grenade(){}
	public void cancel(){}
	public void horizontal(){}
	public void idle(){}
	
	public void actionInit(){
		StateInit();
		this.addModifieds(new RModified(true){
			public void execute() {
				StateRunning();
			}
			public boolean turnNext() {
				return false;
			}
		});
	}
	
	
	//TODO 空闲状态
	public static class StateIdle extends StateUp{

		public StateIdle(Player player, RActionManager m) {
			super(player, m);
		}

		public void StateInit() {
			final RBaseSprite sprUp=player.getSprite();
			sprUp.setVisible(true);
			sprUp.setSpriteWidthHeight(Player.SPR_BODY_W, Player.SPR_BODY_H);
			sprUp.setAnima(player.anima_body_normal);
			if(player.gen_right){
				sprUp.setOffset(Player.SPR_BODY_OFFSET_X, Player.SPR_BODY_OFFSET_Y);
				sprUp.clearVerticalMirrorFlag();
			}else{
				sprUp.setOffset(-Player.SPR_BODY_OFFSET_X, Player.SPR_BODY_OFFSET_Y);
				sprUp.setVerticalMirrorFlag();
			}
		}

		public void StateRunning() {
			final RBaseSprite sprUp=player.getSprite();
			if(player.gen_right){
				sprUp.setOffset(Player.SPR_BODY_OFFSET_X, Player.SPR_BODY_OFFSET_Y);
				sprUp.clearVerticalMirrorFlag();
			}else{
				sprUp.setOffset(-Player.SPR_BODY_OFFSET_X, Player.SPR_BODY_OFFSET_Y);
				sprUp.setVerticalMirrorFlag();
			}
		}
		
		public void crouch(){
			player.changeBodyState(player.s_up_crouch);
		}
		
		public void up(){
			player.changeBodyState(player.s_up_up);
		}
		
		public void grenade(){
			
		}
		
		public void attack(){
			player.changeBodyState(player.s_up_attack);
		}
	}
	
	//TODO 攻击状态
	public static class StateAttack extends StateUp{

		public StateAttack(Player player, RActionManager m) {
			super(player, m);
		}

		public void up(){
			player.changeBodyState(player.s_up_up);
		}
		
		public void crouch(){
			player.changeBodyState(player.s_up_crouch);
		}
		
		@Override
		public void StateInit() {
			final RBaseSprite spr=player.getSprite();
			player.weapon.animaA.reset();player.weapon.animaKnifeA.reset();
			spr.setSpriteWidthHeight(Player.SPR_BODY_W_2, Player.SPR_BODY_H_2);
			spr.setAnima(player.weapon.getAnimationA());	
			if(player.gen_right){
				spr.clearVerticalMirrorFlag();
				spr.setOffset(Player.SPR_BODY_OFFSET_X_2, Player.SPR_BODY_OFFSET_Y_2);
			}else{
				spr.setVerticalMirrorFlag();
				spr.setOffset(-Player.SPR_BODY_OFFSET_X_2, Player.SPR_BODY_OFFSET_Y_2);
			}
			player.weapon.attackA();
		}

		@Override
		public void StateRunning() {
			final RBaseSprite spr=player.getSprite();
			if(player.gen_right){
				spr.clearVerticalMirrorFlag();
				spr.setOffset(Player.SPR_BODY_OFFSET_X_2, Player.SPR_BODY_OFFSET_Y_2);
			}else{
				spr.setVerticalMirrorFlag();
				spr.setOffset(-Player.SPR_BODY_OFFSET_X_2, Player.SPR_BODY_OFFSET_Y_2);
			}
			if(waitAnimation(player.weapon.animaA)||waitAnimation(player.weapon.animaKnifeA)){
				player.changeBodyState(player.s_up_idle);
			}
		}
		
		public void attack(){
			if(waitAnimation(player.weapon.animaA,3)&&!player.gen_knife){
				player.weapon.attackA();
				player.weapon.animaA.reset();
			}
		}
	}
	
	
	//TODO 上状态
	public static class StatedToUp extends StateUp{

		public StatedToUp(Player player, RActionManager m) {
			super(player, m);
		}
		
		public void crouch(){
			player.changeBodyState(player.s_up_crouch);
		}

		@Override
		public void StateInit() {
			final RBaseSprite spr=player.getSprite();
			spr.setSpriteWidthHeight(Player.SPR_BODY_W_3, Player.SPR_BODY_H_3);
			spr.setAnima(player.anima_up);
			if(player.gen_right){
				spr.clearVerticalMirrorFlag();
				spr.setOffset(Player.SPR_BODY_OFFSET_X_2, Player.SPR_BODY_OFFSET_Y_2);
			}else{
				spr.setVerticalMirrorFlag();
				spr.setOffset(-Player.SPR_BODY_OFFSET_X_2, Player.SPR_BODY_OFFSET_Y_2);
			}
		}

		@Override
		public void StateRunning() {
			final RBaseSprite spr=player.getSprite();
			if(player.gen_right){
				spr.clearVerticalMirrorFlag();
				spr.setOffset(Player.SPR_BODY_OFFSET_X_2, Player.SPR_BODY_OFFSET_Y_3);
			}else{
				spr.setVerticalMirrorFlag();
				spr.setOffset(-Player.SPR_BODY_OFFSET_X_2, Player.SPR_BODY_OFFSET_Y_3);
			}
		}
		
		public void horizontal(){
			player.changeBodyState(player.s_up_idle);
		}
		
		public void attack(){
			player.changeBodyState(player.s_up_up_attack);
		}
	}
	
	//TODO 上攻击状态
	public static class StateUpAttack extends StateUp{

		public StateUpAttack(Player player, RActionManager m) {
			super(player, m);
		}

		public void crouch(){
			player.changeBodyState(player.s_up_crouch);
		}
		
		public void StateInit() {
			final RBaseSprite spr=player.getSprite();
			player.weapon.animaB.reset();player.weapon.animaKnifeA.reset();
			if(player.gen_knife){
				spr.setSpriteWidthHeight(Player.SPR_BODY_W_2, Player.SPR_BODY_H_2);
				spr.setAnima(player.weapon.animaKnifeA);	
				if(player.gen_right){
					spr.clearVerticalMirrorFlag();
					spr.setOffset(Player.SPR_BODY_OFFSET_X_2, Player.SPR_BODY_OFFSET_Y_2);
				}else{
					spr.setVerticalMirrorFlag();
					spr.setOffset(-Player.SPR_BODY_OFFSET_X_2, Player.SPR_BODY_OFFSET_Y_2);
				}
			}else{
				spr.setSpriteWidthHeight(Player.SPR_BODY_W_3, Player.SPR_BODY_H_3);
				spr.setAnima(player.weapon.animaB);
				if(player.gen_right){
					spr.clearVerticalMirrorFlag();
					spr.setOffset(Player.SPR_BODY_OFFSET_X_2, Player.SPR_BODY_OFFSET_Y_3);
				}else{
					spr.setVerticalMirrorFlag();
					spr.setOffset(-Player.SPR_BODY_OFFSET_X_2, Player.SPR_BODY_OFFSET_Y_3);
				}
			}
			player.weapon.attackB();
		}

		public void StateRunning() {
			final RBaseSprite spr=player.getSprite();
			if(player.gen_knife){
				spr.setAnima(player.weapon.animaKnifeA);
				spr.setSpriteWidthHeight(Player.SPR_BODY_W_2, Player.SPR_BODY_H_2);
				if(player.gen_right){
					spr.clearVerticalMirrorFlag();
					spr.setOffset(Player.SPR_BODY_OFFSET_X_2, Player.SPR_BODY_OFFSET_Y_2);
				}else{
					spr.setVerticalMirrorFlag();
					spr.setOffset(-Player.SPR_BODY_OFFSET_X_2, Player.SPR_BODY_OFFSET_Y_2);
				}
			}else{
				spr.setAnima(player.weapon.animaB);
				spr.setSpriteWidthHeight(Player.SPR_BODY_W_3, Player.SPR_BODY_H_3);
				if(player.gen_right){
					spr.clearVerticalMirrorFlag();
					spr.setOffset(Player.SPR_BODY_OFFSET_X_2, Player.SPR_BODY_OFFSET_Y_3);
				}else{
					spr.setVerticalMirrorFlag();
					spr.setOffset(-Player.SPR_BODY_OFFSET_X_2, Player.SPR_BODY_OFFSET_Y_3);
				}
			}
			if(waitAnimation(player.weapon.animaB)||waitAnimation(player.weapon.animaKnifeA)){
				player.changeBodyState(player.s_up_up);
			}
		}
		
		public void attack(){
			if(waitAnimation(player.weapon.animaB,4)&&!player.gen_knife){
				player.weapon.attackB();
				player.weapon.animaB.reset();
			}
		}
		
	}
	
	//TODO 下状态
	public static class StateToDown extends StateUp{

		public StateToDown(Player player, RActionManager m) {
			super(player, m);
		}

		@Override
		public void StateInit() {
			final RBaseSprite spr=player.getSprite();
			spr.setSpriteWidthHeight(Player.SPR_BODY_W_3, Player.SPR_BODY_H_3);
			spr.setAnima(player.anima_down);
			if(player.gen_right){
				spr.clearVerticalMirrorFlag();
				spr.setOffset(0, 60);
			}else{
				spr.setVerticalMirrorFlag();
				spr.setOffset(0, 60);
			}
		}

		@Override
		public void StateRunning() {
			
		}
		
		public void attack(){
			
		}
		
		public void horizontal(){
			
		}
		
	}
	//TODO 下攻击
	public static class StateDownAttack extends StateUp{

		public StateDownAttack(Player player, RActionManager m) {
			super(player, m);
		}

		@Override
		public void StateInit() {
			
		}

		@Override
		public void StateRunning() {
			
		}

		@Override
		public boolean canSendToManager() {
			return false;
		}
		
	}
	//TODO 手雷状态
	public static class StateGrenade extends StateUp{

		public StateGrenade(Player player, RActionManager m) {
			super(player, m);
		}

		@Override
		public void StateInit() {
			
		}

		@Override
		public void StateRunning() {
			
		}

		@Override
		public boolean canSendToManager() {
			return false;
		}
		
	}
	
	//TODO 蹲状态
	public static class StateCrouch extends StateUp{

		public StateCrouch(Player player, RActionManager m) {
			super(player, m);
		}

		@Override
		public void StateInit() {
			player.getSprite().setVisible(false);
		}

		@Override
		public void StateRunning() {
			
		}
		
		public void stand(){
			player.getSprite().setVisible(true);
			player.changeBodyState(player.s_up_idle);
		}
	}
	
	//TODO 死亡状态
	public static class StateDead extends StateUp{

		public StateDead(Player player, RActionManager m) {
			super(player, m);
		}

		@Override
		public void StateInit() {
			player.getSprite().setVisible(false);
		}

		@Override
		public void StateRunning() {
			
		}
	}
}
