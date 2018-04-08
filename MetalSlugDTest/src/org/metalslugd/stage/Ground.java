package org.metalslugd.stage;

import org.metalslug.C;
import org.redengine.game.entity.RPhysicsEntity;
import org.redengine.systems.physicssystem.RPhysicsObject;

public class Ground extends RPhysicsEntity{
	
	public Ground(){
		this.setEntityID(C.GROUD);
	}
	
	public void init(int x,int y,int width,int height){
		RPhysicsObject phy=new RPhysicsObject();
		phy.registerCollide(0, 0, true);
		phy.setAsAABB(x, y, width, height);
		this.addPhysicsObject(phy);
	}
	
	@Override
	public boolean cycleCondition() {
		return false;
	}

	@Override
	public void onGetFromPool() {
		
	}

	@Override
	public void onCycle() {
		
	}

	@Override
	public void onGetFromPool(Object obj) {
		// TODO Auto-generated method stub
		
	}
	
}
