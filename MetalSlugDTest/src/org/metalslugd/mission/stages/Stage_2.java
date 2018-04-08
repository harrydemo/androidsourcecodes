package org.metalslugd.mission.stages;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import javax.microedition.khronos.opengles.GL10;

import org.metalslug.C;
import org.metalslug.GameFont;
import org.metalslug.GameObjectData;
import org.metalslug.GameRule;
import org.metalslug.R;
import org.metalslugd.gameobject.enemy.EnemyA;
import org.metalslugd.gameobject.enemymachine.EnemyBigPlane;
import org.metalslugd.stage.Ground;
import org.metalslugd.stage.PlayerGround;
import org.metalslugd.stage.TestBK;
import org.redengine.engine.frame.Core;
import org.redengine.engine.manager.RSceneManager;
import org.redengine.game.entity.REntity;
import org.redengine.game.scene.RScene;
import org.redengine.systems.common.V2;
import org.redengine.systems.graphsystem.RCamera;
import org.redengine.systems.graphsystem.opengl.RTextureManager;
import org.redengine.systems.graphsystem.sprite.RSprite;
import org.redengine.systems.graphsystem.sprite.spriteex.RStringPanel;
import org.redengine.systems.inputsystem.RInputListener;
import org.redengine.systems.mediasystem.RMediaSystem;
import org.redengine.systems.physicssystem.RPhysicsObject;
import org.redengine.systems.physicssystem.physicshandler.BContactListener;
import org.redengine.utils.RTimer;
import org.redengine.utils.ioutils.RFileIOUtils;

import android.view.MotionEvent;

/**
 * 本场景是任务模式的场景
 * 该场景的任务目标是在限定时间内存活并尽可能的取得高分
 * @author xujun
 * @project RedEngine
 */
public class Stage_2 extends RScene {

	public Stage_2(RCamera camera) {
		super(camera);
	}

	final int W=Core.getCore().getScreenWidth();
	final int H=Core.getCore().getScreenHeight();
	
	int highScore;
	
	//==================TODO 变量申明===================
	EnemyBigPlane plane;
	GameFont gf;
	TestBK testBK;
	GameObjectData god;
	GameRule gameRule;
	
	int time;
	boolean firstInit=true;
	//===============================================
	
	
	public void onSceneGLInit() {
		if(!firstInit)return;
	//====================纹理装载====================
		gameRule=new GameRule();
		time=60;
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
		tm.createTexture("gamefont", R.drawable.font);
		panel=new RStringPanel(this,30,30);
		gf=new GameFont();gf.init();
		panel.setFont(gf);
		panel2=new RStringPanel(this,30,30);
		panel2.setFont(gf);
		panel3=new RStringPanel(this,40,50);
		panel3.setFont(gf);
		panel4=new RStringPanel(this,30,30);
		panel4.setFont(gf);
	}
	
	public void onSceneInit() {
	//====================变量装载====================
		InputStream is=RFileIOUtils.readFile("gameinfo.dat");
		try {
			ObjectInputStream ois=new ObjectInputStream(is);
			highScore=ois.readInt();
			ois.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if(!firstInit)return;

		RMediaSystem.getMediaSystem().load("boom", R.raw.boom);
		RMediaSystem.getMediaSystem().load("gun", R.raw.handgun);
		RMediaSystem.getMediaSystem().load("dead", R.raw.mslugdead);
		
		
		
		
		
		getCommand().setDrawFilter(true);
		getCommand().addInputListener(new RInputListener(){
			public void onUp() {
				god.player.doStand();
				god.player.doStop();
				god.player.doUp();
			}
			public void onLeft() {
				god.player.gen_right=false;
				god.player.doStand();
				god.player.doMove();
				god.player.doHorizontal();
			}
			public void onDown() {
				god.player.doCrouch();
				god.player.doStop();
			}
			
			public void onRight() {
				god.player.gen_right=true;
				god.player.doStand();
				god.player.doMove();
				god.player.doHorizontal();
			}
			public void onUp_Left() {
				god.player.gen_right=false;
				god.player.doStand();
				god.player.doMove();
			}
			
			public void onDown_Left() {
				god.player.gen_right=false;
				god.player.doMove();
			}
			public void onDown_Right() {
				god.player.gen_right=true;
				god.player.doMove();
			}
			public void onUp_Right() {
				god.player.gen_right=true;
				god.player.doStand();
				god.player.doMove();
			}
			public void onButtonA() {
				god.player.doAttack();
			}
			public void onButtonB() {
				god.player.doStand();
				god.player.doJump();
			}
			public void onButtonC() {
				//god.player.doReLife();
				//god.player.god=true;
				//RSceneManager.getSceneManager().changeScene("menu");
			}
			public void onNullDirect() {
				god.player.doHorizontal();
				god.player.doStand();
				god.player.doStop();
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
				god.player.onGround();
				break;
			case 51: //玩家子弹或刀子与敌人碰撞
				if(e1.getEntityID()==C.ENEMYA){
					if(((EnemyA)e1).dead()){
						gameRule.addComb();
						gameRule.wiatComb=true;
						gameRule.addScore(100);
					}
					god.effectPool.getEntity(e1);
					if(e2.getClass()==org.metalslugd.gameobject.BulletPool.Bullet.class)e2.cycle();
					else gameRule.addScore(400);
				}else{
					if(((EnemyA)e2).dead()){
						gameRule.addComb();
						gameRule.wiatComb=true;
						gameRule.addScore(100);
					}
					god.effectPool.getEntity(e2);
					if(e1.getClass()==org.metalslugd.gameobject.BulletPool.Bullet.class)e1.cycle();
					else gameRule.addScore(400);
				}
				break;
			case 57: //玩家子弹与机械碰撞
				if(e1.getEntityID()==C.ENEMYMACHINE){
					if(((EnemyBigPlane)e1).hit()){
						gameRule.addScore(8000);
					}
					god.metalHitEffectPool.getEntity(e2.getPhysicsObject().getPosition());
					if(e2.getClass()==org.metalslugd.gameobject.BulletPool.Bullet.class)
						e2.cycle();
				}else{
					if(((EnemyBigPlane)e2).hit()){
						gameRule.addScore(8000);
					}
					god.metalHitEffectPool.getEntity(e1.getPhysicsObject().getPosition());
					if(e1.getClass()==org.metalslugd.gameobject.BulletPool.Bullet.class)
						e1.cycle();
				}
				break;
			case 77:  //玩家与敌人子弹碰撞
				if(god.player.god)return;
				
				t3.reset();t3.run();
				god.player.doDead();
				gameRule.comb=0;
				gameRule.rate=1;
				//t2.reset();t2.run();
				
				if(o1.getEntity().getEntityID()==C.ENEMY_BULLET)
					o1.getEntity().cycle();
				else o2.getEntity().cycle();
				break;
			case 143: //手雷与地碰撞
				RMediaSystem.getMediaSystem().play("boom");
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
				god.player.getPhysicsObject().getMoveHandler().setAccelerateY(C.GROUND_ACCELERATE);
				break;
			}
		}
	});
	god=GameObjectData.getGameObjectData();
	god.initGameObjects(getEntityManager(), actionManager);
	
	plane=new EnemyBigPlane();
	plane.init();
	getEntityManager().addEntity(plane);
	Stage_1bk bk=new Stage_1bk();
	bk.init(0, 0);
	Ground g=new Ground();g.init(0,0,10000,50);
	final int gCount=2;
	PlayerGround[] gs=new PlayerGround[gCount];
	for(int i=0;i<gCount;i++) gs[i]=new PlayerGround();
	gs[0].init(-100, -10, 100, 500);
	gs[1].init(900, -10, 100, 500);
	getEntityManager().addEntity(g);
	getEntityManager().addEntity(gs[0]);getEntityManager().addEntity(gs[1]);
	getEntityManager().addEntity(bk);
	//firstInit=false;
	//===============================================		
	}
	
	
	RStringPanel panel;
	RStringPanel panel2;
	RStringPanel panel3;
	RStringPanel panel4;
	
	
	public void onSceneStart() {
		firstInit=false;
		god.player.getPhysicsObject().getMoveHandler().clearVeloX();
		god.player.getPhysicsObject().setPosition(300, 300);
		god.player.doStop();
		plane.reset();
	}
	
	//TODO temp class
	long next=0;
	RSprite spr;
	RTimer t=new RTimer(1000);
	RTimer t2=new RTimer(3000,true,true);
	RTimer t3=new RTimer(1000,true,true);
	public void onSceneCycle() {
	//====================场景循环====================
		//用来判断玩家跳跃状态的代码
		if(god.player==null)return;

		panel.drawLetters("scroe "+gameRule.score, 30, 400);
		panel4.drawLetters("highscore "+highScore, 30, 360);
		
		if(gameRule.comb>3)
			panel2.drawLetters("comb "+gameRule.comb, 600, 400);
		else panel2.clearString();

		if(t.timeOfArrival()){
			time--;
		}
		if(t2.timeOfArrival()){
			god.player.god=false;
		}
		if(t3.timeOfArrival()){
			god.player.doReLife();
			god.player.god=true;
			t2.reset();t2.run();
		}
		
		god.player.checkAirOrGround();
		RCamera camera=RCamera.getCamera();
		final float x=god.player.getPhysicsObject().getPosition().x;
		if(x>W/2&&x<W/2+100)
		camera.moveTo(x-W/2,0);
		
		long curr=System.currentTimeMillis();	
		if(curr>next){
			next=curr+400;
			god.enemyPool.getEntity(null);
			plane.resume();
		}
		if(time>=0)
			panel3.drawLetters(""+time, 400, 450);
		else {
			if(gameRule.score>highScore){
				OutputStream os=RFileIOUtils.writeFile("gameinfo.dat");
				try {
					ObjectOutputStream oos=new ObjectOutputStream(os);
					oos.writeInt(gameRule.score);
					oos.close();
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			RSceneManager.getSceneManager().changeScene("menu");
		}
		
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
		//回收所有的对象
		//this.clearScene();
		//getPhysicsScene().
		time=60;
		//getActionManager().clearAction();
		god.enemyKnifePool.cycleAll();
		god.eBulletPool.cycleAll();
		god.enemyPool.cycleAll();
		god.boomPool.cycleAll();
		god.bulletPool.cycleAll();
		god.effectPool.cycleAll();
		god.knifePool.cycleAll();
		gameRule.clearState();
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

