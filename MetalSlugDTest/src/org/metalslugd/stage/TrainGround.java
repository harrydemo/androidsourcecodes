package org.metalslugd.stage;

import org.metalslug.C;
import org.redengine.game.entity.RStandardEntity;
import org.redengine.systems.graphsystem.RImage;
import org.redengine.systems.graphsystem.opengl.RTextureManager;
import org.redengine.systems.graphsystem.sprite.RAnimaSprite;
import org.redengine.systems.graphsystem.sprite.RStaticSprite;
import org.redengine.systems.physicssystem.RPhysicsObject;
import org.redengine.systems.physicssystem.physicshandler.BContactFilter;

public class TrainGround extends RStandardEntity {

	public void init(int x,int y){
		this.setEntityID(C.GROUD);
		//视图
		RStaticSprite spr=new RStaticSprite();
		RImage img=RTextureManager.getTextureManager().getTexture("bk").createImageI(
				0,350,
				185,350,
				185,306,
				0,306);
		spr.setImage(img);
		spr.setSpriteVertexMD(x, y, 400, 100);
		spr.setZ(-0.1f);
		
		//模型
		RPhysicsObject phy=new RPhysicsObject();
		phy.setAsAABB(x+20*1.2f, y, 230*1.2f, 53*1.2f);
		phy.registerCollide(0, 0, true, new BContactFilter());
		//实体
		this.addPhysicsObject(phy);
		this.addSprite(spr);
	}
	
	@Override
	public boolean cycleCondition() {
		// TODO Auto-generated method stub
		return false;
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
	public void onGetFromPool(Object obj) {
		// TODO Auto-generated method stub
		
	}
	
}
