package com.threed.jpct.games.alienrunner;

import android.content.res.Resources;

import com.threed.jpct.Loader;
import com.threed.jpct.Logger;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;

public class ObjectPool {

	private static final int[] WIDTHS = new int[9];
	private static final int[] COUNTS = new int[WIDTHS.length];

	private static final RGBColor[] COLORS = new RGBColor[6];
	private static final int[] POINTS = new int[6];

	private static final int[] TO_BE_CORRECTED = new int[3];

	private static SimpleVector BASE_DIR = new SimpleVector(0, -.85, 0);

	private Object3D[] bumps = new Object3D[10];
	private Object3D[] ramps = new Object3D[10];
	private Object3D[] walls = new Object3D[15];
	private Object3D[] woods = new Object3D[10];
	private Object3D[] water = new Object3D[10];
	private Object3D[] mark = new Object3D[2];
	private Object3D[] decorations = new Object3D[6];
	private Object3D[] speedUps = new Object3D[10];

	private Crystal[] crystals = new Crystal[30];

	private Object3D[][] meta = new Object3D[WIDTHS.length][];

	private SimpleVector tmp = new SimpleVector();
	private SimpleVector tmp2 = new SimpleVector();

	private int lastTileLine = -1;
	private int lineMul = 3;
	private int lastEnd = -1;
	private int collected = 0;

	private int lastDecoLine = 0;

	private static final int WALL = 0;
	private static final int WOOD = 1;
	private static final int RAMP = 2;
	private static final int BUMP = 3;
	private static final int CRYSTAL = 4;
	private static final int WATER = 5;
	private static final int MARK = 6;
	private static final int DECORATION = 7;
	private static final int SPEEDUP = 8;

	static {
		WIDTHS[WALL] = 40;
		WIDTHS[WOOD] = 40;
		WIDTHS[RAMP] = 40;
		WIDTHS[BUMP] = 80;
		WIDTHS[CRYSTAL] = 20;
		WIDTHS[WATER] = 60;
		WIDTHS[MARK] = 120;
		WIDTHS[DECORATION] = 0;
		WIDTHS[SPEEDUP] = 40;

		COUNTS[WALL] = 0;
		COUNTS[WOOD] = 0;
		COUNTS[RAMP] = 0;
		COUNTS[BUMP] = 0;
		COUNTS[CRYSTAL] = 0;
		COUNTS[WATER] = 0;
		COUNTS[MARK] = 0;
		COUNTS[DECORATION] = 0;
		COUNTS[SPEEDUP] = 0;

		COLORS[0] = new RGBColor(255, 255, 255);
		COLORS[1] = new RGBColor(250, 240, 20);
		COLORS[2] = new RGBColor(255, 0, 0);
		COLORS[3] = new RGBColor(0, 255, 0);
		COLORS[4] = new RGBColor(0, 0, 255);
		COLORS[5] = new RGBColor(190, 100, 255);

		POINTS[0] = 500;
		POINTS[1] = 1500;
		POINTS[2] = 250;
		POINTS[3] = 350;
		POINTS[4] = 450;
		POINTS[5] = 1000;

		TO_BE_CORRECTED[0] = WALL;
		TO_BE_CORRECTED[1] = WOOD;
		TO_BE_CORRECTED[2] = RAMP;
	}

	public ObjectPool(World world, Resources res) {
		Object3D bump = null;
		Object3D wood = null;
		Object3D wall = null;
		Object3D ramp = null;
		Object3D pool = null;
		Object3D speedUp = null;
		Object3D mark = null;

		TextureManager texMan = TextureManager.getInstance();

		texMan.addTexture("concrete", new Texture(res.openRawResource(R.raw.concrete)));
		texMan.addTexture("wall", new Texture(res.openRawResource(R.raw.walli)));
		texMan.addTexture("bumps", new Texture(res.openRawResource(R.raw.bumps)));
		texMan.addTexture("wood", new Texture(res.openRawResource(R.raw.wood)));
		texMan.addTexture("diamond", new Texture(res.openRawResource(R.raw.diamond)));
		texMan.addTexture("water", new Texture(res.openRawResource(R.raw.pool2), true));
		texMan.addTexture("mark", new Texture(res.openRawResource(R.raw.mark)));
		texMan.addTexture("glow", new Texture(res.openRawResource(R.raw.glow), true));
		texMan.addTexture("speedup", new Texture(res.openRawResource(R.raw.speedup), true));

		Texture l1 = new Texture(res.openRawResource(R.raw.tree2y));
		Texture l2 = new Texture(res.openRawResource(R.raw.tree3y));
		l1.setMipmap(false);
		l2.setMipmap(false);
		texMan.addTexture("leaves", l1);
		texMan.addTexture("leaves2", l2);
		texMan.addTexture("rock", new Texture(res.openRawResource(R.raw.rocky)));

		bump = Loader.loadSerializedObject(res.openRawResource(R.raw.sphere));
		bump.build();
		bump.translate(0, -7, 0);

		ramp = Loader.loadSerializedObject(res.openRawResource(R.raw.ramp));
		ramp.build();
		ramp.translate(0, -5, 0);

		wood = Loader.loadSerializedObject(res.openRawResource(R.raw.cylinder));
		wood.build();
		wood.translate(0, -8, 0);

		wall = Loader.loadSerializedObject(res.openRawResource(R.raw.wall));
		wall.build();
		wall.translate(0, -15, 0);

		pool = Primitives.getPlane(2, 25);
		pool.getRotationMatrix().set(0, 0, 2f);
		pool.getRotationMatrix().set(1, 1, 0.75f);
		pool.rotateX((float) Math.PI / 2f);
		pool.rotateMesh();
		pool.clearRotation();
		pool.setTexture("water");
		pool.setTransparency(20);
		pool.build();
		pool.translate(0, -0.5f, 0);

		mark = Primitives.getPlane(8, 8.5f);
		mark.getRotationMatrix().set(0, 0, 3.5f);
		mark.rotateX((float) Math.PI / 2f);
		mark.rotateMesh();
		mark.clearRotation();
		mark.setTexture("mark");
		mark.setTransparency(100);
		mark.build();
		mark.translate(0, -0.5f, 0);

		speedUp = Primitives.getPlane(2, 20);
		speedUp.rotateX((float) Math.PI / 2f);
		speedUp.rotateMesh();
		speedUp.clearRotation();
		speedUp.setTexture("speedup");
		speedUp.setTransparency(100);
		speedUp.build();
		speedUp.setAdditionalColor(RGBColor.WHITE);
		speedUp.translate(0, -0.5f, 0);

		create(bumps, bump, world, BUMP);
		create(walls, wall, world, WALL);
		create(ramps, ramp, world, RAMP);
		create(woods, wood, world, WOOD);
		create(water, pool, world, WATER);
		create(this.mark, mark, world, MARK);
		create(speedUps, speedUp, world, SPEEDUP);

		createCrystals(crystals, world);
		createDecorations(decorations, world, res);

		// bumps=null;
	}

	private void createDecorations(Object3D[] decorations, World world, Resources res) {
		Logger.log("Creating decorations!");
		Object3D tree1 = Loader.loadSerializedObject(res.openRawResource(R.raw.sertree1));
		Object3D tree2 = Loader.loadSerializedObject(res.openRawResource(R.raw.sertree2));
		Object3D rock = Loader.loadSerializedObject(res.openRawResource(R.raw.serrock));
		tree1.setTransparency(30);
		tree2.setTransparency(30);

		rock.rotateX(-(float) Math.PI / 2);
		tree1.rotateX(-(float) Math.PI);
		tree2.rotateX(-(float) Math.PI);

		rock.rotateMesh();
		rock.clearRotation();
		tree1.rotateMesh();
		tree1.clearRotation();
		tree2.rotateMesh();
		tree2.clearRotation();

		tree1.translate(0, -80, 0);
		tree2.translate(0, -80, 0);

		tree1.translateMesh();
		tree1.clearTranslation();

		tree2.translateMesh();
		tree2.clearTranslation();

		rock.translate(0, -7, 0);
		rock.translateMesh();
		rock.clearTranslation();

		decorations[0] = tree1;
		decorations[1] = new Object3D(tree1, true);
		decorations[1].shareCompiledData(tree1);
		decorations[1].setVisibility(false);
		world.addObject(tree1);
		world.addObject(decorations[1]);

		decorations[2] = tree2;
		decorations[3] = new Object3D(tree2, true);
		decorations[3].shareCompiledData(tree2);
		decorations[3].setVisibility(false);
		world.addObject(tree2);
		world.addObject(decorations[3]);

		decorations[4] = rock;
		decorations[5] = new Object3D(rock, true);
		decorations[5].shareCompiledData(rock);
		decorations[5].setVisibility(false);
		world.addObject(rock);
		world.addObject(decorations[5]);

		RGBColor addCol = new RGBColor(100, 140, 100);

		for (int i = 0; i < decorations.length; i++) {
			decorations[i].enableLazyTransformations();
			decorations[i].setAdditionalColor(addCol);
			decorations[i].setVisibility(false);
			decorations[i].strip();
			decorations[i].build();
		}

		meta[DECORATION] = decorations;

	}

	private void create(Object3D[] target, Object3D source, World world, int type) {

		Logger.log("Creating objects of type: " + type);
		// minX, maxX, minY, maxY, minZ, maxZ
		float[] bb = source.getMesh().getBoundingBox();
		float width = bb[1] - bb[0];
		if (width < 70) {
			// All obstacles but the bump will be scaled to by 40.5 units width
			width = 40.25f / width;
			source.getRotationMatrix().set(0, 0, width);
			source.rotateMesh();
			source.clearRotation();
			source.build();
		}

		for (int i = 0; i < target.length; i++) {
			Object3D nw = new Object3D(source, true);
			nw.shareCompiledData(source);
			nw.build();
			nw.strip();
			nw.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
			nw.setCollisionOptimization(true);
			nw.setVisibility(false);
			nw.enableLazyTransformations();
			world.addObject(nw);
			target[i] = nw;
			if (type == WALL) {
				nw.setUserObject(new ObjectState());
			}
			if (type == SPEEDUP) {
				nw.setAdditionalColor(RGBColor.WHITE);
			}
		}

		meta[type] = target;
	}

	private void createCrystals(Crystal[] target, World world) {

		Logger.log("Creating crystals!");
		for (int i = 0; i < target.length; i++) {
			Crystal nw = new Crystal();
			nw.setVisibility(false);
			nw.enableLazyTransformations();
			nw.addToWorld(world);
			target[i] = nw;
		}

		meta[CRYSTAL] = target;
	}

	public void process(Player player, ParticleManager partMan, Level level, EndlessTrack track) {

		Crystal.staticProcess();

		if (bumps == null) {
			return;
		}

		tmp = player.getTranslation(tmp);

		int tileSize = level.getTileSize();
		int line = (int) (tmp.z / (tileSize * lineMul));
		int size = track.getSize();

		// Logger.log("Line: "+line);

		if (GameConfig.showDecorations && line - lastDecoLine > GameConfig.decorationDistribution && COUNTS[DECORATION] < 6) {
			lastDecoLine = line;
			boolean left = Randomizer.random() < 0.5f;
			float xPos = Randomizer.random() * 100f;
			if (left) {
				xPos = track.getLeft() - 70 - xPos;
			} else {
				xPos = track.getRight() + 70 + xPos;
			}

			int end = decorations.length;
			int start = line % decorations.length;
			for (int i = 0; i < end; i++) {
				Object3D o = decorations[(start + i) % end];
				if (!o.getVisibility()) {
					o.setVisibility(true);
					o.clearTranslation();
					o.translate(xPos, 0, tmp.z + track.getSize() * 0.9f);
					o.rotateY(line / 24.45f); // Just some random value..
					o.touch();
					COUNTS[DECORATION]++;
					break;
				}
			}

		}

		if (line >= 0 && line > lastTileLine) {
			lastTileLine = line;
			// Make Objects that go out of sight invisible
			int end = meta.length;
			for (int i = 0; i < end; i++) {
				Object3D[] store = meta[i];
				int es = store.length;
				int cnt = 0;
				int maxCnt = COUNTS[i];
				for (int p = 0; p < es; p++) {
					Object3D obj = store[p];
					if (obj.getVisibility()) {
						tmp2 = obj.getTranslation(tmp2);
						if (tmp2.z + 80 < tmp.z) {
							// Out of sight...
							obj.setVisibility(false);
							COUNTS[i]--;
							// Logger.log("Removed: "+i);
						}
						cnt++;
					}
					if (cnt >= maxCnt) {
						// Checked all visible/active instances...done!
						break;
					}
				}
			}

			// Create new Objects that might come into view.
			end = (int) Math.min(line + (((float) size * 0.9f) / (tileSize * lineMul)), level.getLineCount());
			int start = Math.max(line, lastEnd);

			// Logger.log("From "+start+" to "+end);

			for (int i = start; i < end; i++) {
				String data = level.getLine(i);
				if (data != null) {
					int pe = data.length();
					int xPos = track.getLeft();
					int zPos = i * tileSize * lineMul;
					int nextFree = -10000;
					for (int p = 0; p < pe; p++) {
						if (xPos < nextFree) {
							xPos += tileSize;
							continue;
						}
						char c = data.charAt(p);
						if (c != ' ' && c != '=') {
							// Some obstable or similar...
							Object3D obj = null;
							switch (c) {
							case 'w':
								obj = getObject(WALL);
								nextFree = xPos + WIDTHS[WALL];
								break;
							case 'r':
								obj = getObject(RAMP);
								nextFree = xPos + WIDTHS[RAMP];
								break;
							case 's':
								obj = getObject(SPEEDUP);
								nextFree = xPos + WIDTHS[SPEEDUP];
								break;
							case 'b':
								obj = getObject(BUMP);
								nextFree = xPos + WIDTHS[BUMP];
								break;
							case 'c':
								obj = getObject(WOOD);
								nextFree = xPos + WIDTHS[WOOD];
								break;
							case 'p':
								obj = getObject(WATER);
								nextFree = xPos + WIDTHS[WATER];
								break;
							case 'm':
								obj = getObject(MARK);
								// nextFree = xPos + WIDTHS[MARK];
								xPos = 0;
								break;
							case '1':
								obj = getObject(CRYSTAL, 0);
								nextFree = xPos + WIDTHS[CRYSTAL];
								break;
							case '2':
								obj = getObject(CRYSTAL, 1);
								nextFree = xPos + WIDTHS[CRYSTAL];
								break;
							case '3':
								obj = getObject(CRYSTAL, 2);
								nextFree = xPos + WIDTHS[CRYSTAL];
								break;
							case '4':
								obj = getObject(CRYSTAL, 3);
								nextFree = xPos + WIDTHS[CRYSTAL];
								break;
							case '5':
								obj = getObject(CRYSTAL, 4);
								nextFree = xPos + WIDTHS[CRYSTAL];
								break;
							case '6':
								obj = getObject(CRYSTAL, 5);
								nextFree = xPos + WIDTHS[CRYSTAL];
								break;
							}

							if (obj == null) {
								if ((c == 'w' || c == 'r' || c == 'b' || c == 'c' || c == 'p' || c == 's' || c == 'm' || (c > '0' && c <= '9'))) {
									Logger.log("Unable to get object for index '" + c + "'", Logger.WARNING);
								}
							} else {
								tmp2 = obj.getTranslation(tmp2);
								float oy = tmp2.y;
								obj.clearTranslation();
								obj.translate(xPos, oy, zPos);
								obj.touch();
								// Logger.log(c+":"+p+"/"+xPos+"/"+i+"/"+zPos);
							}
						}
						xPos += tileSize;
					}
				}
			}
			lastEnd = end;
		}

		// Check crystals
		int cnt = 0;
		int end = crystals.length;
		int max = COUNTS[CRYSTAL];
		for (int i = 0; i < end; i++) {
			Crystal cr = crystals[i];
			if (cr.getVisibility()) {
				cnt++;
				boolean collected = cr.process(player, partMan);
				if (collected) {
					COUNTS[CRYSTAL]--;
					this.collected++;
				}
				if (cnt >= max) {
					break;
				}
			}
		}
	}

	public boolean isUnselfish() {
		return collected == 0;
	}

	public boolean checkWalls(ParticleManager partMan) {
		// Check walls
		int cnt = 0;
		int endy = walls.length;
		int max = COUNTS[WALL];
		boolean wallHit = false;
		for (int i = 0; i < endy; i++) {
			Object3D w = walls[i];
			if (w.getVisibility()) {
				cnt++;
				if (w.wasTargetOfLastCollision()) {
					wallHit = true;
					ObjectState os = (ObjectState) w.getUserObject();
					if (Ticker.hasPassed(os.lastCollisionTime, 500)) {
						os.lastCollisionTime = Ticker.getTime();
						os.hitCount++;
						if (os.hitCount >= 3) {
							w.setVisibility(false);
							SoundManager.getInstance().play(SoundManager.BOMB);
							partMan.addEmitter(w.getTranslation(tmp), BASE_DIR, 15, "rubble", 1000, 15, 30, 7, false);
							EventRegistrator.getInstance().register(EventRegistrator.CONCUSSION);
						}
					}
				}
				if (cnt >= max) {
					break;
				}
			}
		}
		return wallHit;
	}

	public boolean crossesWater() {
		return crossesXXX(water, WATER);
	}

	public boolean crossesSpeedUp() {
		return crossesXXX(speedUps, SPEEDUP);
	}

	public boolean crossesMark() {
		return crossesXXX(mark, MARK);
	}

	public boolean crossesXXX(Object3D[] objs, int type) {
		int cnt = 0;
		int endy = objs.length;
		int max = COUNTS[type];
		for (int i = 0; i < endy; i++) {
			Object3D w = objs[i];
			if (w.getVisibility()) {
				cnt++;
				if (w.wasTargetOfLastCollision()) {
					return true;
				}
				if (cnt >= max) {
					break;
				}
			}
		}
		return false;
	}

	public void reset() {
		int end = meta.length;
		for (int i = 0; i < end; i++) {
			COUNTS[i] = 0;
			Object3D[] store = meta[i];
			int es = store.length;
			for (int p = 0; p < es; p++) {
				store[p].setVisibility(false);
			}
		}
		lastTileLine = -1;
		lastEnd = -1;
		collected = 0;
		lastDecoLine = 0;
	}

	private Object3D getObject(int type) {
		return getObject(type, -1);
	}

	public Object3D getClosest(SimpleVector pos) {
		float min = 40f;

		Object3D minObj = null;
		int endy = TO_BE_CORRECTED.length;
		for (int p = 0; p < endy; p++) {
			int ci = TO_BE_CORRECTED[p];
			Object3D[] types = meta[ci];
			int e = types.length;
			int cnt = 0;
			for (int i = 0; i < e; i++) {
				Object3D o = types[i];
				if (o.getVisibility()) {
					cnt++;
					tmp = o.getTranslation(tmp);
					float len = pos.distance(tmp);
					if (len < min) {
						minObj = o;
						min = len;
					}
					if (cnt >= ci) {
						break;
					}
				}
			}
		}
		return minObj;
	}

	private Object3D getObject(int type, int colIndex) {
		Object3D[] types = meta[type];
		int e = types.length;
		if (COUNTS[type] >= e) {
			Logger.log("Out of objects for type: " + type + "/" + COUNTS[type]);
			return null;
		}
		for (int i = 0; i < e; i++) {
			Object3D o = types[i];
			if (!o.getVisibility()) {
				o.setVisibility(true);
				// Logger.log("Returned: "+type);
				COUNTS[type]++;
				// Logger.log("Active entities of type "+type+": "+COUNTS[type]);
				if (type == WALL) {
					ObjectState os = (ObjectState) o.getUserObject();
					os.lastCollisionTime = 0;
					os.hitCount = 0;
				} else {
					if (type == CRYSTAL) {
						((Crystal) o).setPoints(POINTS[colIndex]);
					}
				}
				if (colIndex != -1) {
					o.setAdditionalColor(COLORS[colIndex]);
				} else {
					o.clearAdditionalColor();
				}
				return o;
			}
		}
		return null;
	}
}
