package org.metalslugd.gameobject.player;

import org.metalslug.C;
import org.redengine.systems.actionsystem.RAction;
import org.redengine.systems.actionsystem.RActionManager;
import org.redengine.systems.actionsystem.RModified;
import org.redengine.systems.actionsystem.actionex.RState;
import org.redengine.systems.graphsystem.sprite.RBaseSprite;

/**
 * ÏÂ°ëÉí×´Ì¬  MetalSlagD
 * @author xujun
 */

public abstract class StateDown extends RState {
	
	Player player;
	
	public StateDown(Player p,RActionManager m){
		this.player=p;
		this.setActionManager(m);
	}
	
	public void StateEnd(){}
	
	/**
	 * ÒÆ¶¯
	 */
	public void move(){}
	
	/**
	 * Í£Ö¹
	 */
	public void stop(){}
	
	/**
	 * ÌøÔ¾
	 */
	public void jump() {}
	
	/**
	 * ¹¥»÷
	 */
	public void attack() {}
	
	/**
	 * ÊÖÀ×
	 */
	public void grenade() {}
	
	/**
	 * ¶×
	 */
	public void crouch() {}
	
	/**
	 * Õ¾
	 */
	public void stand() {}

	/**
	 * ¸´»î
	 */
	public void revive() {}
	
	
	//ÏµÍ³µ÷ÓÃµÄ×ª»»Ö¸Áî
	/**
	 * È¡Ïû×Ô¶¯ÔËÐÐµÄ×´Ì¬
	 */
	public void cancel(){}
	
	/**
	 * ËÀÍö
	 */
	public final void dead(){
		if(player.gen_dead)return;
		player.changeLegState(player.s_down_dead);
	}
	
	/**
	 * ÂäµØ
	 */
	public void onGround(){}
	
	/**
	 * ¿ÕÖÐ
	 */
	public void onAir(){
		if(player.gen_dead)return;
		player.gen_canJump=false;
		player.changeLegState(player.s_down_jump);
	}

	public void airOrGround(){
		if(player.gen_dead)return;
		if(player.getPhysicsObject().getMoveHandler().getVelo().y<=-0.1f)
			player.onAir();
		else if(player.getPhysicsObject().getMoveHandler().getVelo().y==0)
			player.onGround();
	}
	
	public void reLife(){
		player.changeLegState(player.s_down_jump);
	}
	
	
	
	
	//TODO ¿ÕÏÐ×´Ì¬
	public static class StateIdle extends StateDown{
		public StateIdle(Player p,RActionManager m) {
			super(p,m);
		}

		public void move(){
			player.changeLegState(player.s_down_run);
		}
		
		public void jump(){
			player.changeLegState(player.s_down_jump);
		}
		
		public void crouch(){
			player.changeLegState(player.s_crouch_idle);
		}

		public void StateInit() {
			player.getPhysicsObject().getMoveHandler().clearVeloX();
			final RBaseSprite sprDown=player.getSprite(1);
			sprDown.setSpriteWidthHeight(Player.SPR_LEG_W, Player.SPR_LEG_H);
			sprDown.setOffset(Player.SPR_LEG_OFFSET_X, Player.SPR_LEG_OFFSET_Y);
			sprDown.setAnima(player.anima_leg_stand);
			if(player.gen_right)
				player.getSprite(1).clearVerticalMirrorFlag();
			else player.getSprite(1).setVerticalMirrorFlag();
		}

		public void StateRunning() {
			if(player.gen_right)
				player.getSprite(1).clearVerticalMirrorFlag();
			else player.getSprite(1).setVerticalMirrorFlag();
		}

	}
	
	//TODO ÌøÔ¾×´Ì¬
	public static class StateJump extends StateDown{
		public StateJump(Player p,RActionManager m) {
			super(p,m);
		}
		
		public void move(){
			final float v=player.gen_right?C.P_JUMP_VELO:-C.P_JUMP_VELO;
			player.getPhysicsObject().getMoveHandler().setVeloX(v);
			if(player.gen_right){
				player.getSprite(1).clearVerticalMirrorFlag();
			}else{
				player.getSprite(1).setVerticalMirrorFlag();
			}
		}

		public void onGround(){
			player.gen_canJump=true;
			player.changeLegState(player.s_down_idle);
		}

		@Override
		public void StateInit() {
			final RBaseSprite spr=player.getSprite(1);
			if(player.gen_canJump){
				player.getPhysicsObject().getMoveHandler().setVeloY(C.JUMP_VELO_Y);
				player.gen_canJump=false;
			}
				spr.setSpriteWidthHeight(Player.SPR_LEG_W, Player.SPR_LEG_H);
				spr.setOffset(0, Player.SPR_LEG_OFFSET_Y_3);
				spr.setAnima(player.anima_leg_jump);
			
			if(player.gen_right)spr.clearVerticalMirrorFlag();
			else spr.setVerticalMirrorFlag();
		}

		@Override
		public void StateRunning() {
			final RBaseSprite spr=player.getSprite(1);
			if(player.gen_right)spr.clearVerticalMirrorFlag();
			else spr.setVerticalMirrorFlag();
		}
	}
	
	//TODO ÅÜ²½×´Ì¬
	public static class StateRun extends StateDown{
		public StateRun(Player p,RActionManager m) {
			super(p,m);
		}

		public void crouch(){
			player.changeLegState(player.s_crouch_idle);
		}
		
		public void jump(){
			//player.gen_bigJump=true;
			player.changeLegState(player.s_down_jump);
		}
		
		public void stop(){
			player.changeLegState(player.s_down_idle);
		}

		public void StateInit() {
			RBaseSprite spr=player.getSprite(1);
			spr.setSpriteWidthHeight(Player.SPR_LEG_W, Player.SPR_LEG_H);
			spr.setOffset(0, Player.SPR_LEG_OFFSET_Y_2);
			spr.setAnima(player.anima_leg_run);
			if(player.gen_right){
				spr.clearVerticalMirrorFlag();
			}else{
				spr.setVerticalMirrorFlag();
			}
		}

		public void StateRunning() {
			final float v=player.gen_right?C.P_STAND_VELO:-C.P_STAND_VELO;
			player.getPhysicsObject().getMoveHandler().setVeloX(v);
			if(player.gen_right){
				player.getSprite(1).clearVerticalMirrorFlag();
			}else{
				player.getSprite(1).setVerticalMirrorFlag();
			}
		}
	}
	
	//TODO ¶×¡¢¿ÕÏÐ×´Ì¬
	public static class StateCrouchIdle extends StateDown{
		public StateCrouchIdle(Player p,RActionManager m) {
			super(p,m);
		}

		public void move(){
			player.changeLegState(player.s_crouch_move);
		}
		
		public void jump(){
			
		}
		
		public void stand(){
			player.changeLegState(player.s_down_idle);
		}
		
		public void attack(){
			player.changeLegState(player.s_crouch_attack);
		}
		
		public void grenade(){
			
		}

		@Override
		public void StateInit() {
			player.getPhysicsObject().getMoveHandler().clearVeloX();
			player.getPhysicsObject().changeAsAABBwithoutUpdatePosition(Player.PHY_OFFSET_X, Player.PHY_OFFSET_Y,
					Player.PHY_W, Player.PHY_H);
			player.getSprite(1).setOffset(0, Player.SPR_CROUCH_OFFSET_Y);
			player.getSprite(1).setSpriteWidthHeight(Player.SPR_CROUCH_W, Player.SPR_CROUCH_H);
			player.getSprite(1).setAnima(player.anima_crouch);
			if(player.gen_right)player.getSprite(1).clearVerticalMirrorFlag();
			else player.getSprite(1).setVerticalMirrorFlag();
		}

		@Override
		public void StateRunning() {
			if(player.gen_right)player.getSprite(1).clearVerticalMirrorFlag();
			else player.getSprite(1).setVerticalMirrorFlag();
		}
	}
	
	//TODO ¶×ÒÆ¶¯×´Ì¬
	public static class StateCrouchMove extends StateDown{
		public StateCrouchMove(Player p,RActionManager m) {
			super(p,m);
		}

		public void attack(){
			player.changeLegState(player.s_crouch_attack);
		}
		
		public void jump(){
			player.changeLegState(player.s_down_jump);
		}
		
		public void stand(){
			player.changeLegState(player.s_down_idle);
		}
		
		public void stop(){
			player.changeLegState(player.s_crouch_idle);
		}

		@Override
		public void StateInit() {
			player.getSprite(1).setOffset(0, Player.SPR_CROUCH_OFFSET_Y);
			player.getSprite(1).setSpriteWidthHeight(Player.SPR_CROUCH_W, Player.SPR_CROUCH_H);
			player.getSprite(1).setAnima(player.anima_crouch_move);
			if(player.gen_right)player.getSprite(1).clearVerticalMirrorFlag();
			else player.getSprite(1).setVerticalMirrorFlag();
		}

		@Override
		public void StateRunning() {
			if(player.gen_right){
				player.getSprite(1).clearVerticalMirrorFlag();
				player.getPhysicsObject().getMoveHandler().setVeloX(C.P_CROUCH_VELO);
			}
			else {
				player.getSprite(1).setVerticalMirrorFlag();
				player.getPhysicsObject().getMoveHandler().setVeloX(-C.P_CROUCH_VELO);
			}
		}
	}
	
	//TODO ¶×¡¢¹¥»÷×´Ì¬
	public static class StateCrouchAttack extends StateDown{
		public StateCrouchAttack(Player p,RActionManager m) {
			super(p,m);
		}

		public void attack(){
			if(player.weapon.animaD.getCurrentFrame()>=3&&!player.gen_knife){
				player.weapon.attackD();
				player.weapon.animaD.reset();
			}
		}

		public void stand(){
			player.changeLegState(player.s_down_idle);
		}
		
		@Override
		public void StateInit() {
			player.getPhysicsObject().getMoveHandler().clearVeloX();
			player.weapon.animaD.reset();
			player.weapon.animaKnifeB.reset();
			if(!player.gen_knife)
				player.getSprite(1).setAnima(player.weapon.animaD);
			else
				player.getSprite(1).setAnima(player.weapon.animaKnifeB);
			final RBaseSprite spr=player.getSprite(1);
			spr.setSpriteWidthHeight(Player.SPR_CROUCH_FIRE_W, Player.SPR_CROUCH_FIRE_H);
			if(player.gen_right){
				spr.clearVerticalMirrorFlag();
				spr.setOffset(Player.SPR_CROUCH_FIRE_OFFSET_X, 2);
			}
			else {
				spr.setVerticalMirrorFlag();
				spr.setOffset(-Player.SPR_CROUCH_FIRE_OFFSET_X, 2);
			}
			player.weapon.attackD();
		}

		@Override
		public void StateRunning() {
			final RBaseSprite spr=player.getSprite(1);
			if(player.gen_right){
				spr.clearVerticalMirrorFlag();
				spr.setOffset(Player.SPR_CROUCH_FIRE_OFFSET_X, 2);
			}
			else {
				spr.setVerticalMirrorFlag();
				spr.setOffset(-Player.SPR_CROUCH_FIRE_OFFSET_X, 2);
			}
			if(waitAnimation(player.weapon.animaD)||waitAnimation(player.weapon.animaKnifeB)){
				player.changeLegState(player.s_crouch_idle);
			}
		}
	}
	
	//TODO ¶×¡¢ÊÖÀ××´Ì¬
	public static class StateCrouchGrenade extends StateDown{
		public StateCrouchGrenade(Player p,RActionManager m) {
			super(p,m);
		}

		public void grenade(){
			
		}
		
		public void cancel(){
			
		}

		public void StateInit() {
			
		}

		public void StateRunning() {
			
		}
	}
	
	public static class StateDead extends StateDown{

		public StateDead(Player p, RActionManager m) {
			super(p, m);
		}

		@Override
		public void StateInit() {
			player.gen_dead=true;
			player.getPhysicsObject().getMoveHandler().clearVeloX();
			player.getSprite(1).setSpriteWidthHeight(Player.SPR_ALL_W, Player.SPR_ALL_H);
			player.getSprite(1).setAnima(player.anima_dead);
			player.getSprite(1).setOffset(Player.SPR_ALL_OFFSET_X, Player.SPR_ALL_OFFSET_Y);
		}

		@Override
		public void StateRunning() {
			//if()
		}
		public void onGround(){}
		public void onAir(){}
	}
}
