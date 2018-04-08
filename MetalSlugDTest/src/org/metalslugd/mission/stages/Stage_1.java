package org.metalslugd.mission.stages;

import javax.microedition.khronos.opengles.GL10;

import org.metalslug.C;
import org.metalslug.R;
import org.metalslugd.gameeffect.HitEffectPool;
import org.metalslugd.gameeffect.MetalHitEffectPool;
import org.metalslugd.gameeffect.PlayerHitEffectPool;
import org.metalslugd.gameobject.BulletPool;
import org.metalslugd.gameobject.EnemyBulletAPool;
import org.metalslugd.gameobject.EnemyKnifeAttack;
import org.metalslugd.gameobject.KnifeAttack;
import org.metalslugd.gameobject.enemy.EnemyA;
import org.metalslugd.gameobject.enemy.EnemyAPool;
import org.metalslugd.gameobject.enemy.EnemyGrenade;
import org.metalslugd.gameobject.enemymachine.EnemyBigPlane;
import org.metalslugd.gameobject.player.Player;
import org.metalslugd.gameobject.player.Weapon;
import org.metalslugd.stage.Ground;
import org.metalslugd.stage.PlayerGround;
import org.metalslugd.stage.TestBK;
import org.redengine.game.entity.REntity;
import org.redengine.game.scene.RScene;
import org.redengine.systems.common.V2;
import org.redengine.systems.graphsystem.RCamera;
import org.redengine.systems.graphsystem.opengl.RTextureManager;
import org.redengine.systems.graphsystem.sprite.RSprite;
import org.redengine.systems.graphsystem.sprite.spriteex.RStringPanel;
import org.redengine.systems.inputsystem.RInputListener;
import org.redengine.systems.physicssystem.RPhysicsObject;
import org.redengine.systems.physicssystem.physicshandler.BContactListener;
import android.view.MotionEvent;

/**
 * 本场景是任务模式的场景
 * 该场景的任务目标是在限定时间内存活并尽可能的取得高分
 * @author xujun
 * @project RedEngine
 */
public class Stage_1 extends RScene {

	public Stage_1(RCamera camera) {
		super(camera);
	}
	
	public static int score;
	
	//==================TODO 变量申明===================
	Player player;
	EnemyBigPlane plane;
	TestBK testBK;
	HitEffectPool effectPool;
	BulletPool bulletPool;
	EnemyBulletAPool eBulletPool;
	PlayerHitEffectPool peffectPool;
	EnemyAPool enemyPool;
	KnifeAttack knifePool;
	EnemyKnifeAttack enemyKnifePool;
	MetalHitEffectPool metalHitEffectPool;
	Weapon.Handgun handgun=new Weapon.Handgun();
	EnemyGrenade.GeneralGrenade genralGrenade=new EnemyGrenade.GeneralGrenade();
	//===============================================
	
	
	public void onSceneGLInit() {
	//====================纹理装载====================
		RTextureManager tm=getTextureManager();
		tm.createTexture("bk", R.drawable.train_bk_1);
		tm.createTexture("sboom", R.drawable.smallboom);
		tm.createTexture("fire", R.drawable.fire_body);
		tm.createTexture("bullet", R.drawable.smallbullet);
		tm.createTexture("crouch", R.drawable.crouch);
		tm.createTexture("dead", R.drawable.dead);
		tm.createTexture("leg", R.drawable.marco_leg);
		tm.createTexture("yeah", R.drawable.yeah);
		tm.createTexture("normal", R.drawable.normal_body);
		tm.createTexture("effect", R.drawable.effect);
		tm.createTexture("enemy1", R.drawable.enemy1);
		tm.createTexture("enemy1ex", R.drawable.enemy1_ex);
		tm.createTexture("enemybullet", R.drawable.enemy_ex2);
		tm.createTexture("boxtank", R.drawable.box_tank);
		tm.createTexture("hbullet", R.drawable.heavybullet);
		tm.createTexture("font", R.drawable.font);
		tm.createTexture("bk0", R.drawable.train_bk);
		tm.createTexture("enemycar", R.drawable.enemycar);
		tm.createTexture("plane",R.drawable.bigplane);
		//RFontReaderer.getFontReaderer().initFont(this,"font", 5, 8);
	}
	
	public void onSceneInit() {
	//====================变量装载====================
		getCommand().setDrawFilter(true);
		getCommand().addInputListener(new RInputListener(){
			public void onUp() {
				player.doStand();
				player.doStop();
				player.doUp();
			}
			public void onLeft() {
				player.gen_right=false;
				player.doStand();
				player.doMove();
				player.doHorizontal();
			}
			public void onDown() {
				player.doCrouch();
				player.doStop();
			}
			
			public void onRight() {
				player.gen_right=true;
				player.doStand();
				player.doMove();
				player.doHorizontal();
			}
			public void onUp_Left() {
				player.gen_right=false;
				player.doStand();
				player.doMove();
			}
			
			public void onDown_Left() {
				player.gen_right=false;
				player.doMove();
			}
			public void onDown_Right() {
				player.gen_right=true;
				player.doMove();
			}
			public void onUp_Right() {
				player.gen_right=true;
				player.doStand();
				player.doMove();
			}
			public void onButtonA() {
				player.doAttack();
			}
			public void onButtonB() {
				player.doStand();
				player.doJump();
			}
			public void onButtonC() {
				player.doReLife();
				//RSceneManager.getSceneManager().changeScene("menu");
			}
			public void onNullDirect() {
				player.doHorizontal();
				player.doStand();
				player.doStop();
			}
			public void onNullButton() {
			
			}
			public void onButtonADown() {
				
			}
			public void onButtonBDown() {
				
			}
			public void onButtonCDown() {
				
			}
		});
		
		
	//RMediaSystem.getMediaSystem().load("gun", R.raw.handgun);
	//RMediaSystem.getMediaSystem().load("dead", R.raw.mslugdead);
		
	/**创建物理场景**/
	getCommand().createPhysicsScene(-600, -600, 12000, 2000, 2);
	getCommand().addCollisionListener(new BContactListener(){
		public void contactCreated(final RPhysicsObject o1,final RPhysicsObject o2,final V2 MTD){
			//TODO 碰撞处理=================================
			final REntity e1=o1.getEntity();
			final REntity e2=o2.getEntity();
			int contactID=e1.getEntityID()*e2.getEntityID();
			final V2 v1=o1.getPosition();final V2 v2=o2.getPosition();
			switch(contactID){
			case 91://落到地上
				if(e1.getEntityID()==C.MARCO){
					if(v1.y<v2.y||Math.abs(MTD.x)>=Math.abs(MTD.y)){return;}
					o1.getMoveHandler().clearAccelerate();
					o1.getMoveHandler().clearVeloY();
				}else{
					if(v2.y<v1.y||Math.abs(MTD.x)>=Math.abs(MTD.y)){return;}
					o2.getMoveHandler().clearAccelerate();
					o2.getMoveHandler().clearVeloY();
					}
				player.onGround();
				break;
			case 51: //玩家子弹或刀子与敌人碰撞
				score+=100;
				if(e1.getEntityID()==C.ENEMYA){
					((EnemyA)e1).dead();
					effectPool.getEntity(e1);
					if(e2.getClass()==org.metalslugd.gameobject.BulletPool.Bullet.class)e2.cycle();
				}else{
					((EnemyA)e2).dead();
					effectPool.getEntity(e2);
					if(e1.getClass()==org.metalslugd.gameobject.BulletPool.Bullet.class)e1.cycle();
				}
				break;
			case 57: //玩家子弹与机械碰撞
				if(e1.getEntityID()==C.ENEMYMACHINE){
					((EnemyBigPlane)e1).hit();
					float x=e2.getPhysicsObject().getPosition().x;
					float y=e2.getPhysicsObject().getPosition().y;
					System.out.println("getPosotion: "+x+" "+y);
					//V2 v=new V2(e2.getPhysicsObject().getPosition().x,e2.getPhysicsObject().getPosition().y);
					metalHitEffectPool.getEntity(new V2(x,y));
					if(e2.getClass()==org.metalslugd.gameobject.BulletPool.Bullet.class)
						e2.cycle();
				}else{
					((EnemyBigPlane)e2).hit();
					//metalHitEffectPool.getEntity(e1.getPhysicsObject().getPosition());
					if(e1.getClass()==org.metalslugd.gameobject.BulletPool.Bullet.class)
						e1.cycle();
				}
				break;
			case 77:  //玩家与敌人子弹碰撞
				player.doDead();
				if(o1.getEntity().getEntityID()==C.ENEMY_BULLET)
					o1.getEntity().cycle();
				else o2.getEntity().cycle();
				break;
			case 143: //手雷与地碰撞
				if(o1.getEntity().getEntityID()==C.GROUD){
					o2.getEntity().cycle();
				}else {
					o1.getEntity().cycle();
				}
			}
			
		}

		public void contactPersisted(RPhysicsObject o1,RPhysicsObject o2,V2 MTD){
			
		}

		public void contactDestroyed(final RPhysicsObject o1,final RPhysicsObject o2){
			final REntity e1=o1.getEntity();
			final REntity e2=o2.getEntity();
			final int contactID=e1.getEntityID()*e2.getEntityID();
			switch(contactID){
			case 91:
				player.getPhysicsObject().getMoveHandler().setAccelerateY(C.GROUND_ACCELERATE);
				break;
			}
		}
	});
	//this.sprManager.addSprite(ImageUtils.getTestAnimaSprite("enemycar",600, 50, 200, 100, 100, 3, 3));
	
	/*spr=new RSprite();
	spr.setImage(this.getTextureManager().getImage("plane", 
			0,140f/480f,
			1,140f/480f,
			1,0,
			0,0));
	spr.setSpriteVertexMD(100, 300, 600, 250);
	sprManager.addSprite(spr);*/
	
	player=new Player();
	player.init(actionManager,300,300);
	getEntityManager().addEntity("player",player);
	Stage_1bk bk=new Stage_1bk();
	bk.init(0, 0);
	Ground g=new Ground();g.init(0,0,10000,50);
	final int gCount=2;
	PlayerGround[] gs=new PlayerGround[gCount];
	for(int i=0;i<gCount;i++) gs[i]=new PlayerGround();
	gs[0].init(-100, -10, 100, 500);
	gs[1].init(900, -10, 100, 500);
	
	plane=new EnemyBigPlane();
	plane.init();
	getEntityManager().addEntity(plane);
	//TODO 对象池的装载==========================
	getEntityManager().initPools(C.TOTAL_POOL);
	bulletPool=new BulletPool();
	getEntityManager().addPool(bulletPool);
	//getEntityManager().addPool(new KnifeAttack());
	effectPool=new HitEffectPool();
	getEntityManager().addPool(effectPool);
	eBulletPool=new EnemyBulletAPool();
	getEntityManager().addPool(eBulletPool);genralGrenade.initGrenade(eBulletPool);
	enemyKnifePool=new EnemyKnifeAttack();
	getEntityManager().addPool(enemyKnifePool);
	enemyPool=new EnemyAPool(getActionManager(),genralGrenade,enemyKnifePool);
	getEntityManager().addPool(enemyPool);
	knifePool=new KnifeAttack();
	getEntityManager().addPool(knifePool);
	peffectPool=new PlayerHitEffectPool();
	getEntityManager().addPool(peffectPool);
	metalHitEffectPool=new MetalHitEffectPool();
	getEntityManager().addPool(metalHitEffectPool);
	//===========================================
	getEntityManager().addEntity(g);
	getEntityManager().addEntity(gs[0]);getEntityManager().addEntity(gs[1]);
	getEntityManager().addEntity(bk);
	
	handgun.initWeapon(player, bulletPool, knifePool,player.anima_body_hand_fire,player.anima_body_hand_fire_up,
			player.anima_body_hand_fire_down,player.anima_crouch_hand_fire,
			player.anima_body_knife,player.anima_crouch_knife);
	player.changeWeapon(handgun);
	
	//RFontReaderer.createFontReaderer(2);
	//RFontReaderer.getFontReaderer().setScene(this);
	//RFontReaderer.getFontReaderer().getFreeStringPanel();
	//RFontReaderer.getFontReaderer().drawLetters("only for test 1",50, 460);
	
	panel=new RStringPanel(this,30,30);
	panel.initSystemFont();
	//===============================================		
	}
	
	
	RStringPanel panel;
	public void onSceneStart() {
	//====================场景运行====================
	//===============================================	
	}
	
	//TODO temp class
	long next=0;
	RSprite spr;
	public void onSceneCycle() {
	//====================场景循环====================
		//用来判断玩家跳跃状态的代码
		if(player==null)return;

		panel.drawLetters("scroe: "+score, 30, 300);

		//if(score>=1000)score=0;
		//RFontReaderer.getFontReaderer().getFreeStringPanel().drawLetters("fuck", 0, 0);
		player.checkAirOrGround();
		RCamera camera=RCamera.getCamera();
		final float x=player.getPhysicsObject().getPosition().x;
		if(x>400&&x<500)
		camera.moveTo(x-400,0);
		long curr=System.currentTimeMillis();
		if(curr>next){
			next=curr+400;
			enemyPool.getEntity(null);
			plane.resume();
		}
		//RFontReaderer.getFontReaderer().drawLetters(score+" ",50,460);

		//System.out.println("current Score "+score);
		//System.out.println("current time: "+System.currentTimeMillis());
		//System.out.println(marco.getPhysicsObject().getMoveHandler().getVelo());
	//===============================================	
	}
	public void onTouch(MotionEvent e) {
	//====================操作事件====================
		//System.out.println("touch");
	//===============================================	
	}

	@Override
	public void onScenePause() {
		
	}

	@Override
	public void onSceneActive() {
		
	}

	@Override
	public void onSceneEnd() {
		
	}

	@Override
	public void onSceneGLEnd() {
		
	}

	@Override
	public void instance() {
		
	}

	@Override
	public void release() {
		
	}

	@Override
	public void onSceneDraw(GL10 gl) {
	}

}
