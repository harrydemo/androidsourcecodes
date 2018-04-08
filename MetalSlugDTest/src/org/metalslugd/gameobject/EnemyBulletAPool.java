package org.metalslugd.gameobject;

import org.metalslug.C;
import org.metalslugd.gameobject.enemy.EnemyA;
import org.redengine.game.entity.REntity;
import org.redengine.game.entity.REntityPool;
import org.redengine.game.entity.RStandardEntity;
import org.redengine.systems.common.V2;
import org.redengine.systems.graphsystem.RAnimation;
import org.redengine.systems.graphsystem.RImage;
import org.redengine.systems.graphsystem.opengl.RTextureManager;
import org.redengine.systems.graphsystem.sprite.RAnimaSprite;
import org.redengine.systems.physicssystem.RPhysicsObject;
import org.redengine.systems.physicssystem.physicshandler.BContactFilter;

public class EnemyBulletAPool extends REntityPool {

	public EnemyBulletAPool() {
		super(C.ENEMY_BULLET_A_CAPACITY);
	}

	@Override
	public REntity createPoolObject() {
		EnemyBulletA temp=new EnemyBulletA();
		temp.init();
		return temp;
	}

	
	public static class EnemyBulletA extends RStandardEntity {

		public void init(){
			this.setEntityID(C.ENEMY_BULLET);
			RPhysicsObject phy=new RPhysicsObject();
			phy.sleep=true;
			phy.setAsAABB(300, 1000, 20, 20);
			phy.registerCollide(0, 0, false, new BContactFilter());
			phy.registerMove();
			
			RAnimation anima=new RAnimation();
			RImage[] imgs=RTextureManager.getTextureManager().getTexture("enemybullet").clipTexture(4, 7);
			anima.addAnimaFrames(50,imgs,0,7);
			RAnimaSprite spr=new RAnimaSprite();
			spr.setSpriteWidthHeight(50, 50);
			spr.setAnima(anima);
			this.addPhysicsObject(phy);
			this.addSprite(spr);
			getPhysicsObject().sleep=true;
		}
		
		
		@Override
		public boolean cycleCondition() {
			return getSprite().isFilter();
		}

		@Override
		public void onGetFromPool(Object obj) {
			super.onGetFromPool();
			getSprite().setFilter(false);
			final EnemyA e=(EnemyA)obj;
			final V2 p=e.getPhysicsObject().getPosition();
			final int rate=e.gen_right?1:-1;
			getPhysicsObject().setPosition(p.x, p.y+30);
			getPhysicsObject().getMoveHandler().setVelo(2*rate,5);
			getPhysicsObject().getMoveHandler().setAccelerateY(C.GROUND_ACCELERATE2);
		}

		//@Override
		public void onCycle() {
			super.onCycle();
			getEnityManager().getPool(C.PLAYER_HIT_EFFECT_POOL_ID+1).getEntity(getPhysicsObject().getPosition());
		}
		
	}
}
