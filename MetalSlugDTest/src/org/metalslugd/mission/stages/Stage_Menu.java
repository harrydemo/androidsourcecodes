package org.metalslugd.mission.stages;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import javax.microedition.khronos.opengles.GL10;

import org.metalslug.GameFont;
import org.metalslug.R;
import org.redengine.engine.frame.Core;
import org.redengine.engine.manager.RSceneManager;
import org.redengine.game.scene.RBaseScene;
import org.redengine.systems.graphsystem.RCamera;
import org.redengine.systems.graphsystem.sprite.RStaticSprite;
import org.redengine.systems.graphsystem.sprite.spriteex.RStringPanel;
import org.redengine.systems.inputsystem.RInputSystem;
import org.redengine.systems.mediasystem.RMediaSystem;
import org.redengine.utils.ioutils.RFileIOUtils;

import android.view.MotionEvent;

public class Stage_Menu extends RBaseScene {

	public Stage_Menu(RCamera camera) {
		super(camera);
	}
	
	final int W=Core.getCore().getScreenWidth();
	final int H=Core.getCore().getScreenHeight();

	int highScore;
	
	@Override
	public void onSceneInit() {
		RCamera.getCamera().moveTo(0, 0);
		InputStream is=RFileIOUtils.readFile("gameinfo.dat");
		try {
			ObjectInputStream ois=new ObjectInputStream(is);
			highScore=ois.readInt();
			ois.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		RStaticSprite spr=new RStaticSprite();
		spr.setSpriteVertexMD(0, 0, Core.getCore().getScreenWidth(), Core.getCore().getScreenHeight());
		spr.setImage(getTextureManager().getImage("menubk"));
		
		getSpriteScene().addSprite(spr);
		
		p1.drawLetters("high score "+highScore, 10, 0.8f*H);	
		p4.drawLetters("game start", 0.3f*W, 0.1f*H);
	}

	@Override
	public void onSceneGLInit() {
		getTextureManager().createTexture("gamefont", R.drawable.font);
		getTextureManager().createTexture("menubk", R.drawable.gamemenu);
		p1=new RStringPanel(this,30,30);
		p4=new RStringPanel(this,30,30);
		gf=new GameFont();gf.init();
		p1.setFont(gf);p4.setFont(gf);
	}
	
	GameFont gf;
	RStringPanel p1;
	RStringPanel p4;

	@Override
	public void onSceneStart() {
	
	}

	long nextTime;
	@Override
	public void onSceneCycle() {
		
	}

	@Override
	public void onSceneDraw(GL10 gl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTouch(MotionEvent e) {
		final float x=e.getX();
		final float y=e.getY();
		if(Math.abs(x-W*0.5f)<40&&Math.abs(y-H*0.85f)<20)
			RSceneManager.getSceneManager().changeScene("stage1");
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

