package org.metalslugd.stage;

import org.metalslug.C;
import org.redengine.game.entity.RMultiPhysicsEntity;
import org.redengine.systems.graphsystem.RImage;
import org.redengine.systems.graphsystem.opengl.RTextureManager;
import org.redengine.systems.graphsystem.sprite.RStaticSprite;
import org.redengine.systems.physicssystem.RPhysicsObject;
import org.redengine.systems.physicssystem.physicshandler.BContactFilter;

public class TrainGround2 extends RMultiPhysicsEntity {
	
	public TrainGround2() {
		super(2);
		this.setEntityID(C.GROUD);
	}

	public void init(int x,int y){
		this.setEntityID(C.GROUD);
		//视图
		RStaticSprite spr=new RStaticSprite();
		RImage img=RTextureManager.getTextureManager().getTexture("bk").createImageI(
				250,350,
				445,350,
				445,260,
				250,260);
		spr.setImage(img);
		spr.setSpriteVertexMD(x, y, 270, 125);
		spr.setZ(-0.1f);
		
		//模型
		RPhysicsObject phy1=new RPhysicsObject();
		phy1.setAsBox(x+20, y, 230, 53);
		phy1.registerCollide(0, 0, true, new BContactFilter());
		RPhysicsObject phy2=new RPhysicsObject();
		phy2.setAsBox(x+40, y+53, 180, 53);
		phy2.registerCollide(0, 0, true, new BContactFilter());
		//实体
		this.addPhysicsObject(phy1);this.addPhysicsObject(1,phy2);
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
