package com.threed.jpct.games.alienrunner;

import android.content.res.Resources;

import com.threed.jpct.Light;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;

public class EndlessTrack {
	private Object3D parts[] = new Object3D[2];
	private int size = 600;
	private int joint = size;
	private int back = parts.length - 1;
	private Light light = null;
	private boolean first = true;
	private int startLeft = -60;

	// private SimpleVector tmp = new SimpleVector();
	// private SimpleVector tmp2 = new SimpleVector();
	private SimpleVector tmp3 = new SimpleVector();

	public EndlessTrack(Resources res) {
		Object3D trackPart = Primitives.getPlane(4, 150);
		trackPart.rotateX((float) Math.PI / 2f);
		trackPart.rotateMesh();
		trackPart.clearRotation();
		trackPart.translate(0, 0, size / 2);
		trackPart.translateMesh();
		trackPart.clearTranslation();
		TextureManager.getInstance().addTexture("ground1", new Texture(res.openRawResource(R.raw.ground)));
		TextureManager.getInstance().addTexture("ground2", new Texture(res.openRawResource(R.raw.ground_2)));
		TextureManager.getInstance().addTexture("ground3", new Texture(res.openRawResource(R.raw.ground_3)));
		trackPart.setTexture("ground1");
		// TextureManager.getInstance().getTexture("ground").setMipmap(true);
		for (int i = 0; i < parts.length; i++) {
			Object3D part = new Object3D(trackPart, true);
			parts[i] = part;
			part.build();
			part.translate(0, 0, size * i);
			part.enableLazyTransformations();
		}
	}

	public void setTexture(int type) {
		String name = "ground" + type;

		for (Object3D obj : parts) {
			obj.setTexture(name);
		}
	}

	public void reset() {
		for (int i = 0; i < parts.length; i++) {
			parts[i].clearTranslation();
			parts[i].translate(0, 0, size * i);
			parts[i].touch();
		}
		back = parts.length - 1;
		joint = size;
		first = true;
	}

	public void add(World world) {
		for (int i = 0; i < parts.length; i++) {
			world.addObject(parts[i]);
		}
		light = new Light(world);
		light.setPosition(new SimpleVector(0, -50, 0));
	}

	public int getSize() {
		return size;
	}

	public int getLeft() {
		return startLeft;
	}

	public int getRight() {
		return -startLeft;
	}

	public void process(Player runner) {
		SimpleVector pos = runner.getTranslation(tmp3);
		if (pos.z >= joint + 50) {
			joint += size;
			back++;
			back %= parts.length;
			parts[back].translate(0, 0, 2 * size);
			parts[back].touch();
			first = false;
		}

		if (!first && pos.z < joint - size + 50) {
			joint -= size;
			parts[back].translate(0, 0, -2 * size);
			parts[back].touch();
			back--;
			if (back < 0) {
				back = parts.length - 1;
			}
			back %= parts.length;
		}

		// Translate light source
		pos.x = 20;
		pos.y = -70;
		pos.z -= 40;
		light.setPosition(pos);
	}
}