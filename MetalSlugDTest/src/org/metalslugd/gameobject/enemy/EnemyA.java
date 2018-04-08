package org.metalslugd.gameobject.enemy;

import org.metalslug.C;
import org.metalslugd.gameobject.EnemyKnifeAttack;
import org.metalslugd.gameobject.player.Player;
import org.redengine.game.entity.REntity;
import org.redengine.systems.actionsystem.RActionManager;
import org.redengine.systems.actionsystem.RModified;
import org.redengine.systems.actionsystem.RSingleEntityAction;
import org.redengine.systems.common.V2;
import org.redengine.systems.graphsystem.RAnimation;
import org.redengine.systems.graphsystem.RImage;
import org.redengine.systems.graphsystem.opengl.RTextureManager;
import org.redengine.systems.graphsystem.sprite.RAnimaSprite;
import org.redengine.systems.mediasystem.RMediaSystem;
import org.redengine.systems.physicssystem.RPhysicsObject;
import org.redengine.systems.physicssystem.physicshandler.BContactFilter;
import org.redengine.utils.AIUtils;
import org.redengine.utils.RTimer;

/**
 * ��������A
 * @date 2012.10
 * @author xujun
 */
public class EnemyA extends Enemy {
	
	public static final int SPR_W_1=120;
	public static final int SPR_H_1=120;
	public static final int SPR_W_2=160;
	public static final int SPR_H_2=120;
	
	//����
	EnemyGrenade weapon;
	EnemyKnifeAttack knife;
	
	//TODO ������Դaction
	AAISystemAction ai;
		
	//TODO ������Դanima
	RAnimation anima_stop=new RAnimation();
	RAnimation anima_fool=new RAnimation();
	RAnimation anima_haha=new RAnimation();
	RAnimation anima_ohoh=new RAnimation();
	RAnimation anima_stand_move=new RAnimation();
	RAnimation anima_attack_stand_grenade=new RAnimation();
	RAnimation anima_attack_stand_knife=new RAnimation();
	RAnimation anima_attack_jump_knife=new RAnimation();
	RAnimation anima_dead=new RAnimation();
	RAnimation anima_jump=new RAnimation();

	public boolean gen_canJump;//������Ծ����
	//public boolean gen_dead;   //�������
	
	//TODO ״̬
	EnemyState state;    //�������ڵ�״̬
	EnemyState.StateIdle stateIdle;
	EnemyState.StateDead stateDead;
	EnemyState.StateMove stateMove;
	EnemyState.StateKnife stateKnife;
	EnemyState.StateGrenade stateGrenade;
	EnemyState.StateJump stateJump;
		
	//TODO װ�ض���
	private void initAnima(){
		RImage[] images=null;
		RTextureManager tm=RTextureManager.getTextureManager();
		images=tm.getTexture("enemy1").clipTexture(10, 15);
		anima_stop.addAnimaFrames(60, images,0,3);
		anima_fool.addAnimaFrames(50, images[4],images[5],images[6],images[7],
				images[8],images[9],images[10],images[11],images[12],images[13],
				images[12],images[11],images[10],images[9],images[8],images[7],
				images[6],images[5]);
		anima_haha.addAnimaFrames(50, images,14,17);
		anima_ohoh.addAnimaFrames(40, images,18,20);
		anima_stand_move.addAnimaFrames(40, images,21,28);
		anima_jump.addAnimaFrames(100, images[30],images[31],images[32]);
		anima_attack_stand_grenade.addAnimaFrames(30, images, 87, 102);
		anima_attack_jump_knife.addAnimaFrames(50, images,64,80);
		anima_dead.setModeOnce();
		anima_dead.addAnimaFrames(30, images,103,120);
		images=tm.getTexture("enemy1ex").clipTexture(7,4);
		anima_attack_stand_knife.addAnimaFrames(60, images,0,13);
	}
		
	//TODO װ��״̬
	private void initStates(RActionManager m){
		stateJump=new EnemyState.StateJump(this, m);
		stateIdle=new EnemyState.StateIdle(this, m);
		stateMove=new EnemyState.StateMove(this, m);
		stateDead=new EnemyState.StateDead(this, m);
		stateGrenade=new EnemyState.StateGrenade(this, m);
		stateKnife=new EnemyState.StateKnife(this, m);
		ai=new AAISystemAction(this,m);
	}
		

	//getter setter--------
	public void setAI(RSingleEntityAction ai){
		this.ai=(AAISystemAction) ai;
	}
	
	//constructor--------
	public EnemyA(){
		this.setEntityID(C.ENEMYA);
	}
		
	
	//methods--------
	public void init(RActionManager m,EnemyGrenade eg,EnemyKnifeAttack knife,int x,int y){
		initAnima();
		initStates(m);
		this.weapon=eg;
		this.knife=knife;
		//��ͼ
		RAnimaSprite spr=new RAnimaSprite(); 
		spr.setSpriteWidthHeight(SPR_W_1, SPR_H_1);
		spr.setVisible(false);
		//ģ��
		RPhysicsObject phy=new RPhysicsObject();
		phy.setAsAABB(x, y, 50*1.2f, 60*1.2f);
		phy.setPositonWithOutUpdate(x+25*1.2f, y+47*1.2f);
		//phy.sleep=true;
		phy.registerMove(0);
		phy.registerCollide(0, 0, true, new BContactFilter(C.ENEMY_BIT,C.ENEMY_FILTER,0x0001,0x0001));
		//ʵ��
		this.addPhysicsObject(phy);
		this.addSprite(spr);
		changeState(stateMove);
	}
	
	/**
	 * ת��enemy��״̬
	 * @param state
	 */
	public void changeState(EnemyState state){
		if(!gen_dead&&state.getFlag())return;
		if(this.state!=null)this.state.OverState();
		this.state=state;
		state.sendToTempStack();
	}
	
	//������ִ�нӿ�
	public boolean dead(){
		if(!gen_dead){
			state.dead();
			RMediaSystem.getMediaSystem().play("dead");
			return true;
		}
		return false;
	}
	
	public void attack(){
	
	}
	
	public void knife(){
		
	}
	
	public void jump(){
		
	}
	
	public void onAir(){
		
	}
	
	public void onGround(){
		
	}
	
	/**
	 * �����л��ǵ���״̬
	 */
	public void checkAirOrGround(){
		if(getPhysicsObject().getMoveHandler().getVelo().y<=-0.1f)
			onAir();
		else if(getPhysicsObject().getMoveHandler().getVelo().y==0)
			onGround();
	}
	
	
	public boolean cycleCondition() {
		return false;
	}
	
	public void onGetFromPool(Object obj) {
		super.onGetFromPool();
		this.gen_dead=false;
		changeState(stateMove);
		if(Math.random()>0.5)
			if(Math.random()>0.5)
				getPhysicsObject().setPosition(1000, 200);
			else
				getPhysicsObject().setPosition(400, 500);
		else
			if(Math.random()>0.5)
				getPhysicsObject().setPosition(0, 200);
			else
				getPhysicsObject().setPosition(600, 500);
		getPhysicsObject().getMoveHandler().setAccelerateY(C.GROUND_ACCELERATE);
		//if(!ai.isAttached())
		ai.sendToTempStack();  //����AIϵͳ
	}
		
	//TODO AISystem AIϵͳ
	class AAISystemAction extends RSingleEntityAction {
		Player player;
		//�˽���ҵ�λ��
		public AAISystemAction(final REntity entity,final RActionManager m){
			super(entity,m);
			this.setActionDelay(300);
			setModifiedList(new RModified(true){
			public void execute() {
				//if(gen_dead)return;
				if(player==null){
					player=(Player) entity.getEnityManager().getEntity("player");
				}
				if(state.getClass()!=EnemyState.StateMove.class&&
						state.getClass()!=EnemyState.StateIdle.class)
					return;
				final V2 p=entity.getPhysicsObject().getPosition();
				final V2 pp=player.getPhysicsObject().getPosition();
				
				if(AIUtils.testDistanceY(p, pp, 150)){
					changeState(stateMove);
					return;
				}
				if(AIUtils.testDistanceX(p, pp, 250)){  //����Ƚ�Զʱ�ƶ�
					gen_right=p.x<pp.x;
					changeState(stateMove);
				}else if(AIUtils.testDistanceX(p, pp, 80)){  //�������������
					if(AIUtils.testRandom(0.5f))
						changeState(stateGrenade);
					else{
						if(AIUtils.testRandom(0.3f)){
							gen_right=!gen_right;
							changeState(stateMove);
						}
						else changeState(stateIdle);
					}
				}else{									//��������򹥻�
					if(AIUtils.testRandom(0.6f)){
						entity.getPhysicsObject().getMoveHandler().clearVeloX();
						changeState(stateKnife);
					}else{
						gen_right=!gen_right;
						changeState(stateMove);
					}
				}
				
			}
			public boolean turnNext() {
				if(gen_dead||entity.isFree()){
					entity.getPhysicsObject().getMoveHandler().clearAll();
					return true;
				}
				return false;//����ʱAI�˳�
			}});
		}
		public boolean canSendToManager() {
			return !gen_dead;
		}
		@Override
		public void actionInit() {
			
		}
	}
}