package org.metalslugd.gameobject.enemymachine;

import org.metalslug.C;
import org.metalslugd.gameobject.player.Player;
import org.redengine.game.entity.REntity;
import org.redengine.game.entity.RMultiSpriteEntity;
import org.redengine.game.entity.RMultiStandardEntity;
import org.redengine.systems.actionsystem.RActionManager;
import org.redengine.systems.actionsystem.RModified;
import org.redengine.systems.actionsystem.RSingleEntityAction;
import org.redengine.systems.common.V2;
import org.redengine.systems.graphsystem.RAnimation;
import org.redengine.systems.graphsystem.RImage;
import org.redengine.systems.graphsystem.opengl.RTexture;
import org.redengine.systems.graphsystem.opengl.RTextureManager;
import org.redengine.systems.graphsystem.sprite.RAnimaSprite;
import org.redengine.systems.graphsystem.sprite.RSprite;
import org.redengine.systems.physicssystem.RPhysicsObject;
import org.redengine.systems.physicssystem.physicshandler.BContactFilter;
import org.redengine.utils.ImageUtils;

/**
 * 敌人大(不是“打”)飞机
 * @author xujun
 * @project MetalSlugD
 */
public class EnemyBigPlane extends RMultiSpriteEntity implements EnemyMachine {
	
	//fields--------
	RAnimation anima_leftFire=new RAnimation();
	RAnimation anima_rightFire=new RAnimation();
	RImage[] img_body;
	AAISystemAction ai;
	
	//TODO 属性
	public boolean gen_dead;
	public int life=120;

	
	//constructor--------
	public EnemyBigPlane() {
		super(3);
		this.setEntityID(C.ENEMYMACHINE);
	}
	
	
	//methods--------
	public boolean hit(){
		if(gen_dead) return false;
		life--;
		this.getSprite().setColor(1, 0, 0, 1);
		if(life<50){
			getSprite().setImage(img_body[1]);
		}
		if(life<0){
			getSprite().setImage(img_body[2]);
			boom();
			gen_dead=true;
			return true;
		}
		return false;
	}
	
	public void resume(){
		this.getSprite().clearColor();
	}
	
	private void initAnima(){
		final RTexture t=RTextureManager.getTextureManager().getTexture("plane");
		RImage[] imgs=t.clipTexture(0, 420, 280, 480, 4, 1);
		anima_rightFire.addAnimaFrames(40, imgs);
		anima_leftFire.addAnimaFrames(40, ImageUtils.getVerticalMirrorImages(imgs));
		img_body=t.clipTexture(0, 0, 280, 420, 1, 3);
	}
	
	//private void init
	
	public void init(){
		initAnima();
		
		RPhysicsObject phy1=new RPhysicsObject(); 
		phy1.setAsAABB(400, 300, 180, 120);
		//phy1.registerMove();
		phy1.registerCollide(0,0,true,new BContactFilter(C.ENEMY_BIGPLANE_BIT,C.ENEMY_BIGPLANE_FILTER));
		this.addPhysicsObject(0,phy1);
		
		RSprite spr=new RSprite();
		spr.setSpriteVertexMD(200,240,600, 250);
		spr.setImage(img_body[0]);
		spr.setOffset(0, -60);
		this.addSprite(0, spr);
		RAnimaSprite aspr1=new RAnimaSprite();
		aspr1.setAnima(anima_rightFire);
		aspr1.setSpriteVertexMD(230,200,150,180);
		//aspr1.setOffset(200, 80);
		aspr1.setZ(0.1f);
		this.addSprite(1, aspr1);
		RAnimaSprite aspr2=new RAnimaSprite();
		aspr2.setSpriteVertexMD(630,200,150,180);
		aspr2.setAnima(anima_leftFire);
		//aspr2.setOffset(-200, 80);
		aspr2.setZ(0.1f);
		this.addSprite(2, aspr2);
	}

	public boolean cycleCondition() {
		return false;
	}


	public void onGetFromPool(Object obj) {
		
	}
	
	
	//TODO AISystem AI系统
	class AAISystemAction extends RSingleEntityAction {
		Player player;
		//了解玩家的位置
		public AAISystemAction(final REntity entity,final RActionManager m){
			super(entity,m);
			this.setActionDelay(400);
			setModifiedList(new RModified(true){
			public void execute() {
				if(player==null){
					player=(Player) entity.getEnityManager().getEntity("player");
				}
					
				final V2 p=entity.getPhysicsObject().getPosition();
				final V2 pp=player.getPhysicsObject().getPosition();
				
				
			}
			public boolean turnNext() {
				return false;
			}});
		}
		public boolean canSendToManager() {
			return false;
		}
		public void actionInit() {
			
		}
	}
	
	public void boom(){
		for(int i=0;i<5;i++)
			getEnityManager().getPool(8).getEntity(this);
	}
	
	public void reset(){
		life=120;
		gen_dead=false;
		getSprite().setImage(img_body[0]);
	}
}
