package org.metalslugd.stage;

import org.redengine.game.entity.RImageEntity;
import org.redengine.systems.graphsystem.RImage;
import org.redengine.systems.graphsystem.opengl.RTextureManager;
import org.redengine.systems.graphsystem.sprite.RStaticSprite;

public class TestBK2 extends RImageEntity {
	public void init(int x,int y){
		RImage bk=RTextureManager.getTextureManager().getTexture("bk0").createImageI(
				720,262,
				1023,262,
				1023,0,
				720,0);
		RStaticSprite spr=new RStaticSprite();
		spr.setSpriteVertexMD(x, y, 600, 600);
		spr.setImage(bk);
		spr.setZ(-0.5f);
		addSprite(spr);
	}

	@Override
	public void onGetFromPool() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCycle() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean cycleCondition() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onGetFromPool(Object obj) {
		// TODO Auto-generated method stub
		
	}
}
