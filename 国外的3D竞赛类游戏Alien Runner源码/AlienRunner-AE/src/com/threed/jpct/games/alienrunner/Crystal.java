package com.threed.jpct.games.alienrunner;

import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;

public class Crystal extends Object3D {

	private static final long serialVersionUID = 1L;
	private static Object3D dummy = Primitives.getDoubleCone(4, 8);
	private static Object3D dummyPlane = Primitives.getPlane(1, 24);

	private final static SimpleVector BASE_DIR = new SimpleVector(0f, -0.8f, 0.2f);

	private static Matrix rotMat = new Matrix();

	private static SimpleVector tmp1 = new SimpleVector();
	private static SimpleVector tmp2 = new SimpleVector();

	private Object3D glowPlane = null;
	private int points = 0;

	static {
		dummy.calcTextureWrapSpherical();
	}

	public Crystal() {
		super(dummy, true);

		setTexture("diamond");
		shareCompiledData(dummy);
		build();
		strip();
		super.translate(0, -10, 0);
		setRotationMatrix(rotMat);
		setAdditionalColor(RGBColor.WHITE);
		setLighting(Object3D.LIGHTING_NO_LIGHTS);

		glowPlane = new Object3D(dummyPlane, true);
		glowPlane.setTexture("glow");
		glowPlane.setAdditionalColor(RGBColor.WHITE);
		glowPlane.setLighting(Object3D.LIGHTING_NO_LIGHTS);
		glowPlane.setTransparency(7);
		glowPlane.shareCompiledData(dummyPlane);
		glowPlane.build();
		glowPlane.strip();
	}

	public void clearTranslation() {
		super.clearTranslation();
		glowPlane.clearTranslation();
	}

	public void translate(SimpleVector v) {
		super.translate(v);
		glowPlane.translate(v);
	}

	public void translate(float x, float y, float z) {
		super.translate(x, y, z);
		glowPlane.translate(x, y, z);
	}

	public void touch() {
		super.touch();
		glowPlane.touch();
		glowPlane.setAdditionalColor(this.getAdditionalColor());
	}

	public void enableLazyTransformations() {
		super.enableLazyTransformations();
		glowPlane.enableLazyTransformations();
	}

	public void addToWorld(World world) {
		world.addObject(this);
		world.addObject(glowPlane);
	}

	public void setVisibility(boolean vis) {
		super.setVisibility(vis);
		if (!GameConfig.glow) {
			vis = false;
		}
		glowPlane.setVisibility(vis);

	}

	public static void staticProcess() {
		rotMat.rotateY(0.3f);
	}

	public boolean process(Player player, ParticleManager partMan) {
		SimpleVector pt = player.getTranslation(tmp1);
		SimpleVector tt = getTranslation(tmp2);

		float dist = pt.distance(tt);
		if (dist < 20) {
			SoundManager.getInstance().play(SoundManager.COLLECTED);
			setVisibility(false);
			partMan.addEmitter(tt, BASE_DIR, 4, "collected");
			EventRegistrator.getInstance().register(EventRegistrator.VORACITY);
			ScoreManager.getInstance().add(points);
			return true;
		}
		return false;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getPoints() {
		return points;
	}

}
