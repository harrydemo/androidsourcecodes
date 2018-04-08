package com.threed.jpct.games.alienrunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

import android.content.res.Resources;

import com.threed.jpct.Config;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Interact2D;
import com.threed.jpct.Loader;
import com.threed.jpct.Logger;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;

public class Player extends Object3D {
	private static final long serialVersionUID = 1L;

	private static SimpleVector DOWN = new SimpleVector(0, 1, 0);
	private static SimpleVector BASE_DIR = new SimpleVector(0, -.75, 0);
	private static SimpleVector BASE_DUST_DIR = new SimpleVector(0, -.6, 0);

	private float speed = 8;
	private float maxSpeed = 11;

	private SimpleVector dir = new SimpleVector();

	private SimpleVector tmp = new SimpleVector();

	private SimpleVector work = new SimpleVector();
	private SimpleVector work2 = new SimpleVector();
	private SimpleVector work3 = new SimpleVector();
	private SimpleVector work4 = new SimpleVector();
	private SimpleVector work5 = new SimpleVector();
	private SimpleVector work6 = new SimpleVector();

	private static float size = 15 * 0.85f;

	private static int plane = 0;

	private static float gravityDiv = 22f;

	private static float maxJump = -0.55f;

	private float mul = speed / 3f;

	private float defMul = speed / 3f;

	private int sequence = 1;
	private int lastSequence = 1;

	private float index = 0;

	private long deathTime = 0;

	private boolean rotated = false;

	private boolean fallen = false;

	private int callCnt = 0;

	private Object3D shadowPlane = null;
	private boolean added = false;

	private boolean lastLeft = false;
	private boolean lastRight = false;

	private boolean inWater = false;
	private boolean highSpeed = false;

	private boolean onTrack = false;

	private boolean finished = false;
	private long startTime = 0;
	private long totalTime = 0;

	private boolean stopped = true;

	private float[] animSpeeds = { 0, 0.007f, 0.05f, 0.04f, 0.04f, 0.02f, 0.04f, 0.04f, 0.04f, 0.04f, 0.04f, 0.04f, 0.04f, 0.04f, 0.04f, 0.04f, 0.04f, 0.06f, 0.04f };

	private static InputStream loadZip(Resources res) {
		ZipInputStream zis = new ZipInputStream(res.openRawResource(R.raw.alien));
		try {
			zis.getNextEntry();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return zis;
	}

	public Player(Resources res) {
		super(Loader.loadSerializedObject(loadZip(res)));
		shadowPlane = Primitives.getPlane(1, 30);
		TextureManager texMax = TextureManager.getInstance();
		texMax.addTexture("alien", new Texture(res.openRawResource(R.raw.alienskin)));
		texMax.addTexture("dust", new Texture(res.openRawResource(R.raw.dustcloud)));
		Texture sh = new Texture(res.openRawResource(R.raw.blobb), true);
		texMax.addTexture("shadow", sh);
		// setAdditionalColor(new RGBColor(100, 100, 100));
		setTexture("alien");
		setScale(0.85f);
		rotateY((float) -Math.PI / 2f);
		rotateMesh();
		clearRotation();
		strip();
		build();
		shadowPlane.rotateX((float) Math.PI / 2f);
		shadowPlane.rotateMesh();
		shadowPlane.clearRotation();
		shadowPlane.setTexture("shadow");
		shadowPlane.setTransparency(10);
		shadowPlane.build();
		translate(0, plane - size, 50);
		dir = new SimpleVector(0, 0, 0);
		Config.glTransparencyOffset = 0;
		Config.glTransparencyMul = 0.05f;
	}

	public boolean isOnTrack() {
		return onTrack;
	}

	public void setOnTrack(boolean onTrack) {
		this.onTrack = onTrack;
	}

	public void reset() {
		speed = 8;
		maxSpeed = 11;
		dir.set(SimpleVector.ORIGIN);
		mul = speed / 3f;
		defMul = speed / 3f;
		sequence = 1;
		lastSequence = 1;
		index = 0;
		deathTime = 0;
		rotated = false;
		fallen = false;
		callCnt = 0;
		lastLeft = false;
		lastRight = false;
		inWater = false;
		onTrack = false;
		finished = false;
		stopped = true;
		startTime = 0;
		totalTime = 0;

		clearTranslation();
		clearRotation();
		translate(0, plane - size, 50);
	}

	public void startRace() {
		dir.set(0, 0, 1);
		dir.scalarMul(speed);
	}

	public boolean hasFinished() {
		return this.finished;
	}

	public void setFinished(boolean fi) {
		this.finished = fi;
	}

	public void setStopped(boolean stopped) {
		this.stopped = stopped;
		if (!stopped) {
			startRace();
		}
	}

	public boolean isStopped() {
		return stopped;
	}

	public int getTime() {
		if (startTime == 0) {
			return 0;
		}
		if (totalTime == 0) {
			return (int) (Ticker.getTime() - startTime) / 10;
		}
		return (int) (totalTime / 10);
	}

	private void startRace(Recorder recorder) {
		onTrack = true;
		startTime = Ticker.getTime();
		recorder.start();
		SoundManager.getInstance().play(SoundManager.START);
	}

	private void completeRace(Level level, ObjectPool pool, Recorder recorder) {
		finished = true;
		stopped = true;
		recorder.stop();
		totalTime = Ticker.getTime() - startTime;
		long scoreAdd = (long) ((level.getLongPar() - totalTime) * 2.5f);
		if (scoreAdd < 0) {
			ScoreManager.getInstance().sub((int) -scoreAdd / 6);
		} else {
			ScoreManager.getInstance().add((int) scoreAdd);
		}
		if (pool.isUnselfish()) {
			EventRegistrator.getInstance().register(EventRegistrator.UNSELFISH);
		}
		SoundManager.getInstance().play(SoundManager.DONE);
		Logger.log("Race completed in " + totalTime + "ms with " + ScoreManager.getInstance().getScore() + " points, " + (recorder.getIndex() / 3) + " steps recorded!");
	}

	public boolean move(long ticks, FrameBuffer buffer, World world, ParticleManager partMan, ObjectPool pool, Recorder recorder, Level level, boolean[] movement, float x,
			boolean ok) {

		boolean left = movement[0];
		boolean right = movement[1];

		if (!added) {
			world.addObject(shadowPlane);
			added = true;
		}

		float dl = 1;

		for (int i = 0; i < ticks; i++) {

			callCnt++;

			SimpleVector lp = getTranslation(work3);

			if (lp.y <= -52.5f) {
				EventRegistrator.getInstance().register(EventRegistrator.HEAD_IN_THE_SKY);
			}

			shadowPlane.clearTranslation();
			shadowPlane.translate(lp);

			work5.set(lp);

			if (!fallen && !stopped) {
				translate(dir);
			}

			float height = 20;

			SimpleVector pos = getTranslation(work2);

			if (pos.y > (plane - size)) {
				// Bounce...
				dir.y *= -mul;
				mul *= 0.5f;

				pos.y = (plane - size);
				clearTranslation();
				translate(pos);
			}

			pos.y -= height;

			if (GameConfig.correctCollisions) {
				Object3D closest = pool.getClosest(pos);
				if (closest == null) {
					work6.set(pos);
				} else {
					work6 = closest.getTranslation();
					float dif = work6.x - pos.x;
					if (dif > 15 || dif < -15) {
						if (dif < 0) {
							work6.set(pos.x - 5, pos.y, pos.z);
						} else {
							work6.set(pos.x + 5, pos.y, pos.z);
						}
					} else {
						work6.set(pos);
					}
				}
			} else {
				work6.set(pos);
			}

			float dist = world.calcMinDistance(work6, DOWN, 50);

			boolean drops = false;
			float damping = 1f;

			if (pool.crossesMark()) {
				if (!onTrack && startTime == 0) {
					startRace(recorder);
				} else {
					if (onTrack && !finished && Ticker.getTime() - startTime > 3500) {
						completeRace(level, pool, recorder);
					}
				}
				dist = Object3D.COLLISION_NONE;
			}

			
			if (lp.y <= 0.5f && lp.y>-15 && pool.crossesSpeedUp()) {
				
				if (!highSpeed) {
					SoundManager.getInstance().play(SoundManager.HISS, 500);
				}
				dir=dir.normalize(dir);
				dir.scalarMul(maxSpeed+5);
				highSpeed = true;
				dist = Object3D.COLLISION_NONE;
			} else {
				highSpeed=false;
			}
			
			
			if (lp.y <= 0.5f && lp.y>-15 && pool.crossesWater()) {
				// Water causes a slow down, no jump...
				damping = 0.85f;
				if (!inWater) {
					EventRegistrator.getInstance().register(EventRegistrator.AQUARIUS);
					SoundManager.getInstance().play(SoundManager.WATER, 500);
				}
				drops = true;
				inWater = true;
				dist = Object3D.COLLISION_NONE;
			} else {
				inWater = false;
			}

			if (dist == Object3D.COLLISION_NONE && pos.y + height < (plane - size)) {
				dist = plane - pos.y;
			}

			if (dist != Object3D.COLLISION_NONE) {
				float dd = dist - size;
				if (pos.y + dd > plane - size) {
					dd = (plane - size) - pos.y;
				}
				work.set(0, dd, 0);

			} else {
				work.set(0, height, 0);
			}

			pos.add(work);

			lp.scalarMul(-1);
			lp.add(pos);
			lp = lp.normalize(work4);

			boolean hasToClear = false;

			if (!stopped) {
				if (lp.y < -0.05) {
					// up
					if (lp.y > -0.9) {
						if (lp.y < maxJump) {
							// Limit height!
							lp.y = maxJump;
						}
						float len = dir.length();
						if (len > maxSpeed) {
							len = maxSpeed;
						}
						dir.set(lp);
						dir.scalarMul(len);
						clearTranslation();
						translate(pos);
						mul = defMul;
						sequence = 2;
					} else {
						boolean wasWall = pool.checkWalls(partMan);
						if (wasWall) {
							// Too steep
							dir.scalarMul(-2);
							dir.y = 0;
							translate(dir);
							dir.set(0, 0, 0);

							if (sequence != 17) {
								partMan.addEmitter(work5, BASE_DIR, 12, "particle");
								SoundManager.getInstance().play(SoundManager.BUMP, 500);
							}

							sequence = 17;
							deathTime = Ticker.getTime();
							index = 0;
							mul = 0;
						} else {
							// Collision flaw...don't stop, just jump...the code
							// is the same as above.
							if (lp.y < maxJump) {
								// Limit height!
								lp.y = maxJump;
							}
							float len = dir.length();
							if (len > maxSpeed) {
								len = maxSpeed;
							}
							dir.set(lp);
							dir.scalarMul(len);
							clearTranslation();
							translate(pos);
							mul = defMul;
							sequence = 2;
						}
					}
				} else {
					if (lp.y > 0.05 && deathTime == 0 && !fallen) {
						// down
						dir.y += speed / (gravityDiv / (defMul / mul));
						ScoreManager.getInstance().add((int) (lp.y * 10));

						if (sequence == 2 && mul == defMul) {
							EventRegistrator.getInstance().register(EventRegistrator.RUBBER_BALL);
							SoundManager.getInstance().play(SoundManager.SPRING, 300);
						}

						sequence = 5;
						hasToClear = true;
						if (mul != defMul) {
							sequence = 2;
							// Increase speed faster if we are going to
							// slow...to keep the "flow"
							if (dir.z < speed / 1.5f) {
								dir.z *= 1.2f;
								// Logger.log("dir.z: "+dir.z);
							}
						}

					} else {
						dir.y = 0;
						sequence = 2; // 2
					}
				}
			}

			dl = dir.length();

			if (dl < 0.05) {
				sequence = 1;
			}

			if (deathTime != 0 && Ticker.getTime() - deathTime < 250) {
				sequence = 17;
				dir.set(0, 0, -10);

				pos = getTranslation(work2);
				if (pos.y != plane - size) {
					translate(0, (plane - size) - pos.y, 0);
				}

			} else {
				if (deathTime != 0) {
					dir.set(0, 0, speed / 2);
					fallen = true;
					sequence = 17;
					left = false;
					right = false;
					index = 0.8f;
					animate(index, sequence);
					translate(0, -5, 0);
				}
				deathTime = 0;
			}

			if (sequence == 17 && index > 0.8) {
				index = 0.8f;
			}

			tmp = getTranslation(tmp);
			SimpleVector td = Interact2D.project3D2D(world.getCamera(), buffer, tmp, tmp);
			float xLast=x;
			if (td!=null) {
				xLast=td.x;
			}
			
			if (sequence == 2 && !fallen && !stopped) {
				float xTrans = speed / 2.66f;

				if (left) {
					if (pos.x > -60) {
						translate(-xTrans, 0, 0);

						tmp = getTranslation(tmp);
						td = Interact2D.project3D2D(world.getCamera(), buffer, tmp, tmp);

						if (td.x < x) {
							float ov=Math.abs(td.x-xLast);
							float d=x-td.x;
							ov=d/ov;
							float backSpeed=xTrans*ov;
							translate(backSpeed, 0,0);
							
							left = false;
							right = false;
						} else {
							rotateZ(0.01f);
							rotated = true;
						}
					} else {
						hasToClear = true;
					}
				}
				if (right) {
					if (pos.x < 60) {
						translate(xTrans, 0, 0);
						
						tmp = getTranslation(tmp);
						td = Interact2D.project3D2D(world.getCamera(), buffer, tmp, tmp);

						if (td.x > x) {
							float ov=Math.abs(td.x-xLast);
							float d=td.x-x;
							ov=d/ov;
							float backSpeed=xTrans*ov;
							translate(-backSpeed, 0,0);
							
							left = false;
							right = false;
						} else {
							rotateZ(-0.01f);
							rotated = true;
						}
					} else {
						hasToClear = true;
					}
				}

				if (left != lastLeft || right != lastRight) {
					hasToClear = true;
				}
				lastLeft = left;
				lastRight = right;

				if (dl > 5 || drops) {
					work5.y = -0.1f;
					work5.z -= 8;

					if ((callCnt & 1) == 0) {
						work5.x -= 5;
					} else {
						work5.x += 5;
					}

					if (!drops) {
						partMan.addEmitter(work5, BASE_DUST_DIR, 1, "dust", 90, 12, 10, 5, true);
					} else {
						partMan.addEmitter(work5, BASE_DUST_DIR, 1, "drop", 90, 8, 15, 5, true);
					}
				}
			}

			if (fallen && (left || right || ok)) {
				fallen = false;
				translate(0, 5, 0);
			}

			if (((!right && !left) || hasToClear) && rotated) {
				rotated = false;
				clearRotation();
			}

			// Correct the translation if wrong...
			pos = getTranslation(work2);
			if (pos.y > plane - size) {
				translate(0, (plane - size) - pos.y, 0);
				dir.y = 0;
			}

			recorder.record(this);

			if (!fallen && !stopped) {
				if (dl != 0 && dl < speed / 1.015f && !(lp.y > 0.05f)) {
					dir.scalarMul(1.015f);
				}

				if (dir.length() > speed * 1.015f) {
					dir.scalarMul(1f / 1.015f);
				}

				if (drops && dl > speed * 0.6f) {
					dir.scalarMul(damping);
				}
			}
		}

		if (stopped) {
			if (rotated) {
				rotated = false;
				clearRotation();
			}

			dir.scalarMul(1f / 1.05f);
			tmp = getTranslation(tmp);

			if (dir.length() > 1f) {
				sequence = 2;
				tmp.y = -12.75f;
				tmp.add(dir);
			} else {
				sequence = 1;
				tmp.y = -17;
			}
			clearTranslation();
			translate(tmp);
		}

		if (!fallen) {

			if (lastSequence != sequence) {
				index = 0;
				lastSequence = sequence;
			}

			// Do animation here...it doesn't have to happen for each tick!
			animate(index, sequence);
			float animSpeed = animSpeeds[sequence];
			if (sequence == 2 || sequence == 5) {
				// Run or jump? Adjust animation speed according to running
				// speed
				animSpeed *= (dl / speed);
			}
			index += ticks * animSpeed;
			if (index > 1) {
				index = 0;
			}
		}

		// Update Shadowblob
		SimpleVector lp = getTranslation(work3);
		shadowPlane.clearTranslation();
		float yd = lp.y;
		yd = 10 + (yd / 7);
		if (yd < 1) {
			yd = 1;
		}
		shadowPlane.setTransparency((int) yd);
		lp.y = -0.5f;
		lp.z -= 5;
		shadowPlane.translate(lp);

		movement[0] = left;
		movement[1] = right;

		return fallen;
	}
}