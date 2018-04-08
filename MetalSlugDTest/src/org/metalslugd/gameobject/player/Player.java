package org.metalslugd.gameobject.player;

import org.metalslug.C;
import org.metalslugd.gameobject.enemy.Enemy;
import org.redengine.game.entity.REntity;
import org.redengine.game.entity.RMultiSpriteEntity;
import org.redengine.systems.actionsystem.RActionManager;
import org.redengine.systems.graphsystem.RAnimaFrame;
import org.redengine.systems.graphsystem.RAnimation;
import org.redengine.systems.graphsystem.RImage;
import org.redengine.systems.graphsystem.opengl.RTexture;
import org.redengine.systems.graphsystem.opengl.RTextureManager;
import org.redengine.systems.graphsystem.sprite.RAnimaSprite;
import org.redengine.systems.physicssystem.RPhysicsObject;
import org.redengine.systems.physicssystem.physicshandler.BContactFilter;

/**
 * 合金弹头D MetalSlugD 玩家类
 * @date 2012.10
 * @author xujun 
 * @project MetalSlugD
 */

public class Player extends RMultiSpriteEntity {
	
	//TODO Contants==================
	public static final float RATE=1.2f;
	public static final int PHY_W=(int) (30*RATE);
	public static final int PHY_H=(int) (60*RATE);
	public static final int PHY_OFFSET_X=(int) (25*RATE);
	public static final int PHY_OFFSET_Y=(int) (50*RATE);
	//上身一般状态
	public static final int SPR_BODY_W=(int) (80*RATE);
	public static final int SPR_BODY_H=(int) (80*RATE);
	public static final int SPR_BODY_OFFSET_X=(int) (-8*RATE);
	public static final int SPR_BODY_OFFSET_Y=(int) (-8*RATE);
	//上身攻击状态
	public static final int SPR_BODY_W_2=(int) (120*RATE);
	public static final int SPR_BODY_H_2=(int) (100*RATE);
	public static final int SPR_BODY_OFFSET_X_2=(int) (-26*RATE);
	public static final int SPR_BODY_OFFSET_Y_2=(int) (-18*RATE);
	//上身向上攻击状态
	public static final int SPR_BODY_W_3=(int) (120*RATE);
	public static final int SPR_BODY_H_3=(int) (140*RATE);
	public static final int SPR_BODY_OFFSET_X_3=(int) (-26*RATE);
	public static final int SPR_BODY_OFFSET_Y_3=(int) (-38*RATE);
	//下身1站立状态
	public static final int SPR_LEG_W=(int) (80*RATE);
	public static final int SPR_LEG_H=(int) (50*RATE);
	public static final int SPR_LEG_OFFSET_X=0;
	public static final int SPR_LEG_OFFSET_Y=(int) (25*RATE);
	//下身2跑状态
	public static final int SPR_LEG_OFFSET_X_2=0;
	public static final int SPR_LEG_OFFSET_Y_2=(int) (32*RATE);
	//下身跳状态
	public static final int SPR_LEG_OFFSET_Y_3=(int) (35*RATE);
	
	public static final int SPR_CROUCH_W=(int) (80*RATE);
	public static final int SPR_CROUCH_H=(int) (80*RATE);
	public static final int SPR_CROUCH_OFFSET_Y=(int) (10*RATE);
	
	public static final int SPR_CROUCH_FIRE_W=(int) (120*RATE);
	public static final int SPR_CROUCH_FIRE_H=(int) (100*RATE);
	public static final int SPR_CROUCH_FIRE_OFFSET_X=(int) (-30*RATE);
	public static final int SPR_CROUCH_FIRE_OFFSET_Y=0;
	
	//全身图片状态
	public static final int SPR_ALL_W=120;
	public static final int SPR_ALL_H=120;
	public static final int SPR_ALL_OFFSET_X=0;
	public static final int SPR_ALL_OFFSET_Y=0;
	
	public static final float SPR_BODY_Z=0.2f;
	public static final float SPR_LEG_Z=0.1f;
	public static final float SPR_ALL_Z=0.1f;
	
	
	/**
	 * 为true时无敌
	 */
	public boolean god;
	
	
	//TODO 动画资源anima
	RAnimation anima_leg_stand=new RAnimation();      //腿部站立动画
	RAnimation anima_leg_run=new RAnimation();        //腿部移动动画
	RAnimation anima_leg_jump=new RAnimation();       //腿部跳跃动画
	
	public RAnimation anima_body_hand_fire=new RAnimation();     
	public RAnimation anima_body_hand_fire_up=new RAnimation();
	public RAnimation anima_body_hand_fire_down=new RAnimation();
	public RAnimation anima_crouch_hand_fire=new RAnimation();    
	
	public RAnimation anima_body_knife=new RAnimation();     //身体刀子动画
	//public RAnimation anima_body_knife2=new RAnimation();
	public RAnimation anima_crouch_knife=new RAnimation();
	//public RAnimation anima_crouch_knife2=new RAnimation();
	
	RAnimation anima_body_normal=new RAnimation();    //身体静止动画
	RAnimation anima_up=new RAnimation();
	RAnimation anima_down=new RAnimation();
	RAnimation anima_crouch=new RAnimation();         //下蹲静止动画
	RAnimation anima_crouch_move=new RAnimation();    //下蹲移动动画
	RAnimation anima_dead=new RAnimation();           //中弹动作
	RAnimation anima_up_fire=new RAnimation();
	

	//属性genotype
	public boolean gen_knife;  //刀子攻击属性
	public boolean gen_dead;    //存活属性
		
	//TODO 装载动画
	private void initAnima(){
		RImage[] imgs=null;
		RTextureManager tm=RTextureManager.getTextureManager();
		imgs=tm.getTexture("normal").clipTexture(8, 1);
		anima_body_normal.addAnimaFrames(200, imgs,0,3);
		imgs=tm.getTexture("leg").clipTexture(7, 5);
			
		anima_leg_stand.addAnimeFrames(new RAnimaFrame(imgs[0],99));
		anima_leg_run.addAnimaFrames(30, imgs[23],imgs[24],imgs[25],imgs[26],
				imgs[27],imgs[28],imgs[29],imgs[30],imgs[31],imgs[32],imgs[33],imgs[34]);
		anima_leg_jump.addAnimaFrames(300,imgs[11],imgs[12],imgs[13],imgs[14],
				imgs[15],imgs[16],imgs[15],imgs[14],imgs[13],imgs[12]);
			
		//装载手枪的开枪动画
		final RTexture t=tm.getTexture("fire");
		imgs=t.clipTexture(0, 0, 1020, 50, 17, 1);
		anima_body_hand_fire.addAnimaFrames(40,imgs[0],imgs[1],imgs[2],imgs[3],imgs[4],imgs[5]);
		anima_body_knife.addAnimaFrames(40, imgs,10,16);
		
		imgs=t.clipTexture(0,100,1020,170,17,1);
		anima_body_hand_fire_up.addAnimaFrames(40,imgs[0],imgs[1],imgs[2],imgs[3],imgs[4],imgs[5]);
		anima_up.addAnimaFrames(10000, imgs[6]);
		imgs=t.clipTexture(0,270,1020,340,17,1);
		anima_body_hand_fire_down.addAnimaFrames(40,imgs[0],imgs[1],imgs[2],imgs[3],imgs[4],imgs[5]);
		anima_down.addAnimaFrames(10000, imgs[6]);
		final int n=0;
		imgs=t.clipTexture(0, 50, 1020, 100, 17, 1);
		anima_crouch_hand_fire.addAnimaFrames(40,imgs[0+n]
				,imgs[1+n],imgs[2+n],imgs[3+n],imgs[4+n],imgs[5+n]);
		anima_crouch_knife.addAnimaFrames(40, imgs,10,16);
		//手枪开枪动画完毕
		
		//anima_up.addAnimaFrames(100, imgs[6]);
		
		imgs=tm.getTexture("crouch").clipTexture(9, 2);
			//anima_turn_crouch.addAnimaFrames(40, imgs[0],imgs[1],imgs[2]);
			//anima_turn_stand.addAnimaFrames(40, imgs[2],imgs[1],imgs[0]);
		anima_crouch.addAnimaFrames(200, imgs[3],imgs[4],imgs[5],imgs[6]);
		anima_crouch_move.addAnimaFrames(50, imgs, 7, 17);
			
			imgs=t.clipTexture(0, 100, 1020, 170, 17, 1);
			anima_up.addAnimaFrames(0, imgs[9]);
			anima_up_fire.addAnimaFrames(30, imgs[8],imgs[7],imgs[6],imgs[0],
					imgs[1],imgs[2],imgs[3],imgs[4],imgs[5]);
			
			
			imgs=tm.getTexture("dead").clipTexture(10, 1);
			anima_dead.setModeOnce();
			anima_dead.addAnimaFrames(5,imgs[0],imgs[1],imgs[2],imgs[3],imgs[4],
					imgs[5],imgs[6],imgs[7],imgs[8],imgs[9]);
		}
		
	public void initStates(RActionManager m){
		s_down_idle=new StateDown.StateIdle(this,m);
		s_down_run=new StateDown.StateRun(this,m);
		s_down_jump=new StateDown.StateJump(this,m);
		s_crouch_idle=new StateDown.StateCrouchIdle(this,m);
		s_crouch_move=new StateDown.StateCrouchMove(this,m);
		s_crouch_attack=new StateDown.StateCrouchAttack(this,m);
		s_crouch_grenade=new StateDown.StateCrouchGrenade(this,m);
		s_down_dead=new StateDown.StateDead(this, m);
		s_up_idle=new StateUp.StateIdle(this, m);
		s_up_attack=new StateUp.StateAttack(this, m);
		s_up_crouch=new StateUp.StateCrouch(this, m);
		s_up_up=new StateUp.StatedToUp(this, m);
		s_up_up_attack=new StateUp.StateUpAttack(this, m);
		s_up_down=new StateUp.StateToDown(this, m);
		s_up_down_attack=new StateUp.StateDownAttack(this, m);
		s_up_dead=new StateUp.StateDead(this, m);
	}
	//TODO State==================
	StateUp bodyState;
	StateDown legState;
	//各个状态
	StateDown.StateIdle s_down_idle;
	StateDown.StateRun s_down_run;
	StateDown.StateJump s_down_jump;
	StateDown.StateCrouchIdle s_crouch_idle;
	StateDown.StateCrouchMove s_crouch_move;
	StateDown.StateCrouchAttack s_crouch_attack;
	StateDown.StateCrouchGrenade s_crouch_grenade;
	StateDown.StateDead s_down_dead;
	StateUp.StateIdle s_up_idle;
	StateUp.StateAttack s_up_attack;
	StateUp.StateCrouch s_up_crouch;
	StateUp.StatedToUp s_up_up;
	StateUp.StateUpAttack s_up_up_attack;
	StateUp.StateToDown s_up_down;
	StateUp.StateDownAttack s_up_down_attack;
	StateUp.StateDead s_up_dead;
	
	//属性相关
	/**
	 * 为true时朝向为右
	 */
	public boolean gen_right;
	
	/**
	 * 为true时允许跳跃的发生
	 */
	public boolean gen_canJump;
	
	/**
	 * 为true时为下蹲状态
	 */
	public boolean gen_crouch;
	
	/**
	 * 所使用的武器
	 */
	public Weapon weapon;
	
	/**
	 * 更换武器
	 * @param weapon
	 */
	public void changeWeapon(Weapon weapon){
		this.weapon=weapon;
	}
	
	public void init(RActionManager m,int x,int y){
		this.setEntityID(C.MARCO);
		initAnima();
		initStates(m);
		
		//视图
		RAnimaSprite sprUp=new RAnimaSprite();  //上半身
		RAnimaSprite sprDown=new RAnimaSprite();//下半身
		sprUp.setSpriteWidthHeight(Player.SPR_BODY_W, Player.SPR_BODY_H);
		sprUp.setOffset(Player.SPR_BODY_OFFSET_X, Player.SPR_BODY_OFFSET_Y);
		sprUp.setAnima(anima_body_normal);
		sprUp.setZ(Player.SPR_BODY_Z);

		sprDown.setSpriteWidthHeight(Player.SPR_LEG_W, Player.SPR_LEG_H);
		sprDown.setOffset(Player.SPR_LEG_OFFSET_X, Player.SPR_LEG_OFFSET_Y);
		sprDown.setAnima(anima_leg_stand);
		sprDown.setZ(Player.SPR_LEG_Z);
		
		//模型
		RPhysicsObject phy=new RPhysicsObject();
		phy.setAsAABB(x, y, Player.PHY_W, Player.PHY_H);
		phy.setPositonWithOutUpdate(x+Player.PHY_OFFSET_X, y+Player.PHY_OFFSET_Y);
		phy.registerMove(0);
		phy.getMoveHandler().setAccelerateY(C.GROUND_ACCELERATE);
		phy.registerCollide(0, 0, true, new BContactFilter(C.MARCO_BIT,C.MARCO_FILTER,0x0001,0x0001));
		//实体
		this.addPhysicsObject(phy);
		this.addSprites(sprUp,sprDown);
		
		this.changeBodyState(s_up_idle);
		this.changeLegState(s_down_idle);
	}
	

	
	//TODO 改变状态的方法
	public void changeBodyState(StateUp state){
		if(bodyState!=null)bodyState.StateEnd();
		bodyState=state;
		state.sendToTempStack();
	}
	
	public void changeLegState(StateDown state){
		if(legState!=null)legState.OverState();//结束进程
		legState=state;//更新状态
		state.sendToTempStack();//装载状态
	}
	
	public boolean cycleCondition() {return false;}

	public void onGetFromPool(Object obj) {}
	
	//TODO 状态调用函数==============================
	public void onGround(){
		legState.onGround();
	}
	
	public void onAir(){
		legState.onAir();
	}

	public void doDead(){
		legState.dead();
		bodyState.dead();
	}
	
	public void doMove(){
		legState.move();
	}
	
	public void doStop(){
		//bodyState.horizontal();
		legState.stop();
	}
	
	public void doJump(){
		legState.jump();
	}
	
	public void doCrouch(){
		if(gen_canJump)
			bodyState.crouch();
		legState.crouch();
	}
	
	public void doStand(){
		bodyState.stand();
		legState.stand();
	}
	
	/**
	 * 检测是否在空中
	 */
	public void checkAirOrGround(){
		if(getPhysicsObject().getMoveHandler().getVelo().y<=-0.1f)
			onAir();
		else if(getPhysicsObject().getMoveHandler().getVelo().y==0)
			onGround();
	}
	
	public void doAttack(){
		//检查6个对象如果有敌人则换刀子
		final RPhysicsObject[] temps=getPhysicsObject().getNearPhysicsObject(50,50,8);
		gen_knife=false;
		for(int i=temps.length-1;i>=0;i--){
			REntity entity=temps[i].getEntity();
			if(!entity.getPhysicsObject().sleep&&entity.getEntityID()==C.ENEMYA&&!((Enemy)entity).gen_dead){
				gen_knife=true;
			}
		}
		bodyState.attack();
		legState.attack();
	}
	
	public void doUp(){
		bodyState.up();
	}
	
	public void doHorizontal(){
		bodyState.horizontal();
	}
	
	public void doReLife(){
		if(gen_dead){
			changeBodyState(s_up_idle);
			changeLegState(s_down_idle);
			getPhysicsObject().setPosition(getPhysicsObject().getPosition().x, 200);
			gen_dead=false;
		}
	}
	
	
	//为复活设置的变量与方法
	//public 

}
