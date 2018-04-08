package org.metalslugd.gameobject;

import org.metalslug.C;
import org.metalslugd.gameobject.player.Player;
import org.redengine.game.entity.REntity;
import org.redengine.game.entity.REntityPool;
import org.redengine.game.entity.RStandardEntity;
import org.redengine.game.scene.RBaseScene;
import org.redengine.systems.common.V2;
import org.redengine.systems.graphsystem.RImage;
import org.redengine.systems.graphsystem.opengl.RTextureManager;
import org.redengine.systems.graphsystem.sprite.RSprite;
import org.redengine.systems.mediasystem.RMediaSystem;
import org.redengine.systems.physicssystem.RPhysicsObject;
import org.redengine.systems.physicssystem.physicshandler.BContactFilter;
import org.redengine.utils.RTimer;

public class BulletPool extends REntityPool {
	
	public BulletPool() {
		super(C.BULLET_CAPACITY);
	}

	
	//override methods--------
	public REntity createPoolObject() {
		Bullet temp=new Bullet();
		temp.init();
		return temp;
	}
	
	
	//inner classes--------
	public static class Bullet extends RStandardEntity {

		//fields--------
		RImage i1;
		RImage i2;
		//Player player;
		
		
		//methods--------
		/**
		 * ÷ÿ–¥∑Ω∑®
		 */
		public void addToScene(RBaseScene scene){
			scene.getPhysicsScene().addPhysicsObjectBullet(getPhysicsObject());
			scene.getSpriteScene().addSprite(getSprite());
		}
		
		public void init(){
			this.setEntityID(C.BULLET);
			RImage[] imgs;
			imgs=RTextureManager.getTextureManager().getTexture("bullet").clipTexture(2, 1);
			RPhysicsObject phy=new RPhysicsObject();
			phy.setAsAABB(10, -20, 30, 10);
			phy.registerMove(0);
			phy.registerCollide(0, 0, false,new BContactFilter(C.BULLET_BIT,C.BULLET_FILTER));
			RSprite spr=new RSprite();
			spr.setSpriteWidthHeight(30, 30);
			spr.setImage(imgs[0]);
			i1=imgs[0];i2=imgs[1];
			this.addPhysicsObject(phy);
			this.addSprite(spr);
			phy.sleep=true;
		}

		@Override
		public boolean cycleCondition() {
			return this.getSprite().isFilter();
		}

		@Override
		public void onGetFromPool(Object obj,int n) {
			super.onGetFromPool();
			RMediaSystem.getMediaSystem().play("gun");
			getSprite().setFilter(false);
			final Player player=(Player)obj;
			final V2 v=player.getPhysicsObject().getPosition();
			final int rate=player.gen_right?1:-1;
			switch(n){
			case 1:
				this.getSprite().setImage(i1);
				this.getPhysicsObject().setPosition(v.x+rate*60, v.y);
				this.getPhysicsObject().getMoveHandler().setVelo(18*rate,0);
				break;
			case 2:
				this.getSprite().setImage(i2);
				this.getPhysicsObject().setPosition(v.x, v.y+30);
				this.getPhysicsObject().getMoveHandler().setVelo(0,18);
				break;
			case 3:
				this.getSprite().setImage(i2);
				this.getPhysicsObject().setPosition(v.x, v.y-60);
				this.getPhysicsObject().getMoveHandler().setVelo(0,-18);
				break;
			case 4:
				this.getSprite().setImage(i1);
				this.getPhysicsObject().setPosition(v.x+rate*60, v.y-20);
				this.getPhysicsObject().getMoveHandler().setVelo(18*rate,0);
				break;
			}
		}

		@Override
		public void onGetFromPool(Object obj) {
			// TODO Auto-generated method stub
			
		}
	}
}
