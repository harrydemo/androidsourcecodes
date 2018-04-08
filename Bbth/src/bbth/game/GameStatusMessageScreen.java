package bbth.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.FloatMath;
import bbth.engine.core.GameActivity;
import bbth.engine.net.bluetooth.Bluetooth;
import bbth.engine.net.simulation.LockStepProtocol;
import bbth.engine.particles.ParticleSystem;
import bbth.engine.ui.Anchor;
import bbth.engine.ui.UIButton;
import bbth.engine.ui.UIButtonDelegate;
import bbth.engine.ui.UILabel;
import bbth.engine.ui.UINavigationController;
import bbth.engine.ui.UIView;
import bbth.engine.util.ColorUtils;
import bbth.engine.util.MathUtils;

public class GameStatusMessageScreen extends UIView implements UIButtonDelegate {
	public static class DisconnectScreen extends GameStatusMessageScreen {
		public DisconnectScreen(UINavigationController controller, boolean isSinglePlayer) {
			super(GameActivity.instance.getString(R.string.youhavebeendisconnected), controller, isSinglePlayer);
		}
	}

	public static class WinScreen extends GameStatusMessageScreen {
		private static final int NUM_PARTICLES = 1000;
		private static final float PARTICLE_THRESHOLD = 0.5f;
		private static final ParticleSystem PARTICLES = new ParticleSystem(NUM_PARTICLES, PARTICLE_THRESHOLD);
		private static final Paint PARTICLE_PAINT = new Paint();
		static {
			PARTICLE_PAINT.setStrokeWidth(2.f);
			PARTICLE_PAINT.setAntiAlias(true);
		}
		private float secondsUntilNext;
		
		public WinScreen(UINavigationController controller, boolean isSinglePlayer) {
			super(GameActivity.instance.getString(R.string.congratulationsyouwin), controller, isSinglePlayer);
			PARTICLES.reset();
		}

		@Override
		public void onDraw(Canvas canvas) {
			PARTICLES.draw(canvas, PARTICLE_PAINT);
			super.onDraw(canvas);
		}

		float[] tempHsv = new float[3];
		@Override
		public void onUpdate(float seconds) {
			PARTICLES.tick(seconds);
			PARTICLES.gravity(150 * seconds);
			PARTICLES.updateAngles();

			secondsUntilNext -= seconds;
			while (secondsUntilNext < 0) {
				secondsUntilNext += 0.5f;
				float x = MathUtils.randInRange(0, BBTHGame.WIDTH);
				float y = MathUtils.randInRange(0, BBTHGame.HEIGHT);
				int color = ColorUtils.randomHSV(0f, 360f, .8f, 1f, .5f, 1f);
				Color.colorToHSV(color, tempHsv);
				float multiplier = MathUtils.randInRange(0, 1) == 0 ? -1f : 1f;
				int color2 = ColorUtils.randomHSV(multiplier*(tempHsv[0]+15) % 360f, multiplier*(tempHsv[0]+30) % 360f, .8f, 1f, .5f, 1f);
				for (int i = 0; i < 100; i++) {
					float angle = MathUtils.randInRange(0, MathUtils.TWO_PI);
					float radius = MathUtils.randInRange(0, 150);
					float vx = FloatMath.cos(angle) * radius;
					float vy = FloatMath.sin(angle) * radius;
					PARTICLES.createParticle().position(x, y).velocity(vx, vy).angle(angle).shrink(0.1f, 0.2f).line().radius(10).color(i % 2 == 0 ? color : color2);
				}
			}
		}
	}

	public static class LoseScreen extends GameStatusMessageScreen {
		private static final int NUM_PARTICLES = 1000;
		private static final float PARTICLE_THRESHOLD = 0.5f;
		private static final ParticleSystem PARTICLES = new ParticleSystem(NUM_PARTICLES, PARTICLE_THRESHOLD);
		private static final Paint PARTICLE_PAINT = new Paint();
		static {
			PARTICLE_PAINT.setStrokeWidth(2.f);
			PARTICLE_PAINT.setAntiAlias(true);
		}
		private float secondsUntilNext;
		
		public LoseScreen(UINavigationController controller, boolean isSinglePlayer) {
			super(GameActivity.instance.getString(R.string.toobadyoulose), controller, isSinglePlayer);
			PARTICLES.reset();
		}
		
		@Override
		public void onDraw(Canvas canvas) {
			PARTICLES.draw(canvas, PARTICLE_PAINT);
			super.onDraw(canvas);
		}
		
		@Override
		public void onUpdate(float seconds) {
			PARTICLES.tick(seconds);
			PARTICLES.updateAngles();
			PARTICLES.gravity(120 * seconds);

			secondsUntilNext -= seconds;
			while (secondsUntilNext < 0) {
				secondsUntilNext += 0.05f;
				for (int i = 0; i < 2; ++i) {
					float x = MathUtils.randInRange(0, BBTHGame.WIDTH);
					float y = MathUtils.randInRange(-BBTHGame.HEIGHT / 4, BBTHGame.HEIGHT / 4);
					float radius = MathUtils.randInRange(1.5f, 2.5f);
					int color = MathUtils.randInRange(0.f, 1.f) < 0.7f ? Color.BLUE : Color.CYAN;
					for (int j = 0; j < 1; ++j) {
						float vy = MathUtils.randInRange(100, 200);
						PARTICLES.createParticle().position(x, y).velocity(0, vy).shrink(0.5f, 0.65f).line().radius(radius).color(color);
					}
				}
			}
		}
	}

	public static class TieScreen extends GameStatusMessageScreen {
		public TieScreen(UINavigationController controller, boolean isSinglePlayer) {
			super(GameActivity.instance.getString(R.string.itsatie), controller, isSinglePlayer);
		}
	}

	private UILabel message;
	private UIButton playAgain, mainMenu;
	UINavigationController controller;
	boolean isSinglePlayer;

	public GameStatusMessageScreen(String text, UINavigationController controller, boolean isSinglePlayer) {
		this.controller = controller;
		this.isSinglePlayer = isSinglePlayer;
		
		message = new UILabel(text, tag);
		message.setAnchor(Anchor.TOP_CENTER);
		message.setTextSize(20.f);
		message.setPosition(BBTHGame.WIDTH / 2.f, BBTHGame.TITLE_TOP);
		message.setTextAlign(Align.CENTER);
		this.addSubview(message);

		playAgain = new UIButton(R.string.playagain, tag);
		playAgain.setSize(BBTHGame.WIDTH * 0.75f, 45);
		playAgain.setAnchor(Anchor.CENTER_CENTER);
		playAgain.setPosition(BBTHGame.WIDTH / 2.f, BBTHGame.HEIGHT / 2 - 65);
		playAgain.setButtonDelegate(this);
		this.addSubview(playAgain);

		mainMenu = new UIButton(R.string.mainmenu, tag);
		mainMenu.setSize(BBTHGame.WIDTH * 0.75f, 45);
		mainMenu.setAnchor(Anchor.CENTER_CENTER);
		mainMenu.setPosition(BBTHGame.WIDTH / 2.f, BBTHGame.HEIGHT / 2);
		mainMenu.setButtonDelegate(this);
		this.addSubview(mainMenu);

		this.setSize(BBTHGame.WIDTH, BBTHGame.HEIGHT);
	}

	@Override
	public void onClick(UIButton button) {
		if (button == playAgain) {
			if (isSinglePlayer) {
				LockStepProtocol protocol = new LockStepProtocol();
				controller.pushUnder(new SongSelectionScreen(controller, Team.SERVER, new Bluetooth(GameActivity.instance, protocol), protocol, true));
			} else {
				controller.pushUnder(new GameSetupScreen(controller));
			}
			controller.pop(BBTHGame.FROM_LEFT_TRANSITION);
		} else if (button == mainMenu) {
			while (controller.pop(BBTHGame.FROM_LEFT_TRANSITION)) {
				// Oh Hai
			}
		}
	}

	public void setText(String text) {
		message.setText(text);
	}
}
