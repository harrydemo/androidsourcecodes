package org.metalslugd.mission.stages;

import javax.microedition.khronos.opengles.GL10;

import org.redengine.engine.frame.Core;
import org.redengine.engine.manager.RSceneManager;
import org.redengine.game.scene.RBaseScene;
import org.redengine.systems.graphsystem.RCamera;
import org.redengine.systems.graphsystem.sprite.RStaticSprite;
import org.redengine.systems.inputsystem.RInputSystem;

import android.view.MotionEvent;

public class Stage_Logo extends RBaseScene {

	public Stage_Logo(RCamera camera) {
		super(camera);
	}

	@Override
	public void onSceneInit() {
		RStaticSprite spr=new RStaticSprite();
		spr.setSpriteVertexMD(0, 0, Core.getCore().getScreenWidth(), Core.getCore().getScreenHeight());
		spr.setImage(getTextureManager().getImage("logo"));
		getSpriteScene().addSprite(spr);
		
		RInputSystem.getSystem().hidden();
	}

	@Override
	public void onSceneGLInit() {
		getTextureManager().createTexture("logo", "logo.png");
	}

	@Override
	public void onSceneStart() {
	
	}

	long nextTime;
	@Override
	public void onSceneCycle() {
		if(nextTime==0){
			nextTime=System.currentTimeMillis()+2000;
		}
		
		if(System.currentTimeMillis()>nextTime){
			RInputSystem.getSystem().show();
			RSceneManager.getSceneManager().changeScene("menu");
			nextTime=0;
		}
	}

	@Override
	public void onSceneDraw(GL10 gl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTouch(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScenePause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSceneActive() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSceneEnd() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSceneGLEnd() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void instance() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void release() {
		// TODO Auto-generated method stub
		
	}

}
