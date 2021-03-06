package org.metalslugd.gameeffect;

import org.metalslug.C;
import org.redengine.game.entity.REntity;
import org.redengine.game.entity.REntityPool;
import org.redengine.game.entity.RImageEntity;
import org.redengine.systems.common.V2;
import org.redengine.systems.graphsystem.RAnimation;
import org.redengine.systems.graphsystem.RImage;
import org.redengine.systems.graphsystem.opengl.RTextureManager;
import org.redengine.systems.graphsystem.sprite.RAnimaSprite;

@SuppressWarnings("rawtypes")
public class MetalHitEffectPool extends REntityPool {

	public MetalHitEffectPool() {
		super(C.HIT_EFFECT_CAPACITY);
	}

	@Override
	public REntity createPoolObject() {
		HitEffect temp=new HitEffect();
		temp.init();
		return temp;
	}
	
	public static class HitEffect extends RImageEntity {
		
		RAnimation a;
		
		public void init(){
			RImage[] imgs=null;
			imgs=RTextureManager.getTextureManager().getTexture("effect").clipTexture(8, 4);
			RAnimaSprite spr=new RAnimaSprite();
			a=new RAnimation();
			a.addAnimaFrames(40, imgs,16,31);
			//a.setModeOnce();
			a.setStatePause();
			//spr.setSpriteWidthHeight(60, 60);
			spr.setSpriteVertexMD(0, 0, 60, 60);
			spr.setZ(5f);
			//spr.setVisible(false);
			spr.setAnima(a);
			this.addSprite(spr);
		}

		@Override
		public boolean cycleCondition() {
			return a.getCurrentFrame()>=14;
		}
		
		@Override
		public void onCycle() {
			this.getSprite().setVisible(false);
			a.reset();
		}

		public void onGetFromPool(Object obj) {
			this.getSprite().setVisible(true);
			final V2 v=(V2)obj;
			a.setStateRun();
			this.getSprite().translateTo(v.x, v.y);
		}
	}

}

