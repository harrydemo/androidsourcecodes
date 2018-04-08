package com.threed.jpct.games.alienrunner;

import android.content.res.Resources;

import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;

public class Ghost extends Object3D {

	private static final long serialVersionUID = 1L;

	private SimpleVector tmp=new SimpleVector();
	
	public Ghost(Resources res) {
		super(Primitives.getPlane(1, 15));
		TextureManager texMax = TextureManager.getInstance();
		texMax.addTexture("ghost", new Texture(res.openRawResource(R.raw.ghost)));
		setTexture("ghost");
		setTransparency(4);
		translate(0,-15,0);
		translateMesh();
		clearTranslation();
		setTransparencyMode(Object3D.TRANSPARENCY_MODE_ADD);
		setVisibility(false);
		build();
	}

	public void update(GhostPlayer ghost, long ticks) {
		if (ghost.isLoaded()) {
			for (int i=0; i<ticks; i++) {
				tmp=ghost.play(tmp);
			}
			this.clearTranslation();
			tmp.y-=10;
			this.translate(tmp);
		}
	}
	
	public void init(GhostPlayer ghost) {
		if (ghost.isLoaded()) {
			ghost.rewind();
			tmp=ghost.play(tmp);
			this.clearTranslation();
			tmp.y-=10;
			this.translate(tmp);
			ghost.rewind();
		}
	}

}
