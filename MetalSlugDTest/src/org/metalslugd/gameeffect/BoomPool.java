package org.metalslugd.gameeffect;

import java.util.Random;

import org.metalslug.C;
import org.redengine.game.entity.REntity;
import org.redengine.game.entity.REntityPool;
import org.redengine.game.entity.RImageEntity;
import org.redengine.systems.common.V2;
import org.redengine.systems.graphsystem.RAnimation;
import org.redengine.systems.graphsystem.RImage;
import org.redengine.systems.graphsystem.opengl.RTextureManager;
import org.redengine.systems.graphsystem.sprite.RAnimaSprite;

public class BoomPool extends REntityPool {
	public BoomPool() {
		super(C.HIT_EFFECT_CAPACITY);
	}

	@Override
	public REntity createPoolObject() {
		Boom temp=new Boom();
		temp.init();
		return temp;
	}
	
	public static class Boom extends RImageEntity {
		
		RAnimation a;
		
		public void init(){
			RImage[] imgs=null;
			imgs=RTextureManager.getTextureManager().getTexture("sboom").clipTexture(7, 4);
			RAnimaSprite spr=new RAnimaSprite();
			a=new RAnimation();
			a.addAnimaFrames(40, imgs,0,15);
			a.setModeOnce();a.setStatePause();
			spr.setSpriteWidthHeight(80, 80);
			spr.setZ(0.05f);
			spr.setVisible(false);
			spr.setAnima(a);
			this.addSprite(spr);
		}

		@Override
		public boolean cycleCondition() {
			return a.getCurrentFrame()>=15;
		}

		public void onGetFromPool() {}

		@Override
		public void onCycle() {
			this.getSprite().setVisible(false);
			a.reset();
		}

		Random rand=new Random();
		@Override
		public void onGetFromPool(Object obj) {
			this.getSprite().setVisible(true);
			final V2 v=((REntity)obj).getPhysicsObject().getPosition();
			a.setStateRun();
			this.getSprite().translateTo(v.x+(rand.nextFloat()-0.5f)*400, v.y+(rand.nextFloat()-0.5f)*100);
		}
	}
}
