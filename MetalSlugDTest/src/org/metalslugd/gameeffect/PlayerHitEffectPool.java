package org.metalslugd.gameeffect;

import org.redengine.game.entity.REntity;
import org.redengine.game.entity.REntityPool;
import org.redengine.game.entity.RImageEntity;
import org.redengine.systems.common.V2;
import org.redengine.systems.graphsystem.RAnimation;
import org.redengine.systems.graphsystem.RImage;
import org.redengine.systems.graphsystem.opengl.RTextureManager;
import org.redengine.systems.graphsystem.sprite.RAnimaSprite;
import org.redengine.utils.RTimer;

public class PlayerHitEffectPool extends REntityPool {

	public PlayerHitEffectPool() {
		super(2);
	}

	@Override
	public REntity createPoolObject() {
		PlayerHitEffect temp=new PlayerHitEffect();
		temp.init();
		return temp;
	}
	
	public static class PlayerHitEffect extends RImageEntity {

		private RTimer t;
		private RAnimation anima=new RAnimation();
		
		public PlayerHitEffect(){
			t=new RTimer(200); //定时200ms回收
		}
		
		public void init(){
			//RAnimation anima=new RAnimation();
			RImage[] imgs=RTextureManager.getTextureManager().getTexture("enemybullet").clipTexture(4, 7);
			anima.addAnimaFrames(20, imgs,16,27);
			RAnimaSprite spr=new RAnimaSprite();
			spr.setSpriteWidthHeight(60, 60);
			spr.setAnima(anima);
			spr.setVisible(false);
			addSprite(spr);
		}
		
		
		@Override
		public boolean cycleCondition() {
			/*if(t.timeOfArrival()){
				//anima.reset(0);
				return true;
			}*/
			return t.timeOfArrival();  //到时回收
		} 

		@Override
		public void onGetFromPool(Object obj) {
			super.onGetFromPool();
			anima.reset();
			t.reset();                //复位计时器
			t.run();                  //开始计时
			V2 p=((V2)obj);
			this.getSprite().translateTo(p.x, p.y);
		}
	}
	
}
