package bbth.game;

import static bbth.game.BBTHSimulation.GAME_HEIGHT;
import static bbth.game.BBTHSimulation.GAME_WIDTH;
import static bbth.game.BBTHSimulation.GAME_X;
import static bbth.game.BBTHSimulation.GAME_Y;
import static bbth.game.BeatTrack.BEAT_CIRCLE_RADIUS;
import static bbth.game.BeatTrack.BEAT_LINE_X;
import static bbth.game.BeatTrack.BEAT_LINE_Y;
import static bbth.game.BeatTrack.BEAT_TRACK_WIDTH;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.FloatMath;
import bbth.engine.ai.Pathfinder;
import bbth.engine.core.GameActivity;
import bbth.engine.fastgraph.FastGraphGenerator;
import bbth.engine.fastgraph.Wall;
import bbth.engine.sound.Beat;
import bbth.engine.ui.Anchor;
import bbth.engine.ui.UIButton;
import bbth.engine.ui.UIButtonDelegate;
import bbth.engine.ui.UIView;
import bbth.engine.util.Bag;
import bbth.engine.util.MathUtils;
import bbth.game.ai.AIController;
import bbth.game.units.Unit;
import bbth.game.units.UnitManager;

/**
 * This reimplements a large part of BBTHSimulation because our code is all
 * mixed up instead of being factored into MVC.
 */
public class InteractiveTutorial extends Tutorial implements UIButtonDelegate, UnitManager {

	private static final Bag<Unit> emptyUnitBag = new Bag<Unit>();
	private static final Path path = new Path();
	private static final Paint paint = new Paint();
	public static final float MIN_SONG_TIME = -6;
	static {
		paint.setAntiAlias(true);
	}

	/**
	 * If this is true, there will be an OK button after most text prompts to
	 * give people enough time to digest the message.
	 */
	public static final boolean USE_OK_BUTTONS = true;

	private abstract class Step extends UIView implements UIButtonDelegate {
		@Override
		public boolean containsPoint(float x, float y) {
			return true; // >_>
		}

		public boolean isPaused() {
			return false;
		}

		protected final void addOKButton(float x, float y) {
			if (USE_OK_BUTTONS) {
				UIButton button = new UIButton(R.string.ok);
				button.setAnchor(Anchor.TOP_CENTER);
				button.setSize(50, 30);
				button.setPosition(x, y + 28);
				button.setButtonDelegate(this);
				button.expandForHitTesting(20, 20);
				addSubview(button);
			} else {
				onClick(null);
			}
		}

		@Override
		public void onClick(UIButton button) {
		}
	}

	private class PlaceUnitStep extends Step {
		private boolean dontTapOnBeatTrack;

		public PlaceUnitStep() {
			beats.clear();
			for (int i = 0; i < 7; i++) {
				beats.add(Beat.tap(0, 1000 * i));
			}
		}

		String tapfurtherrighttocreateaunit_1 = GameActivity.instance.getString(R.string.tapfurtherrighttocreateaunit_1);
		String tapfurtherrighttocreateaunit_2 = GameActivity.instance.getString(R.string.tapfurtherrighttocreateaunit_2);
		String whenthebeatisbetweentwolines_1 = GameActivity.instance.getString(R.string.whenthebeatisbetweentwolines_1);
		String whenthebeatisbetweentwolines_2 = GameActivity.instance.getString(R.string.whenthebeatisbetweentwolines_2);
		String whenthebeatisbetweentwolines_3 = GameActivity.instance.getString(R.string.whenthebeatisbetweentwolines_3);
		
		@Override
		public void onDraw(Canvas canvas) {
			float x = GAME_X + GAME_WIDTH / 2;
			float y = GAME_Y + GAME_HEIGHT * 0.8f;
			paint.setColor(Color.WHITE);
			paint.setTextSize(15);
			paint.setTextAlign(Align.CENTER);
			if (dontTapOnBeatTrack) {
				canvas.drawText(tapfurtherrighttocreateaunit_1, x, y - 8, paint);
				canvas.drawText(tapfurtherrighttocreateaunit_2, x, y + 8, paint);
				drawArrow(canvas, x - 25, y + 25, x + 25, y + 25, 5);
			} else {
				canvas.drawText(whenthebeatisbetweentwolines_1, x, y - 17, paint);
				canvas.drawText(whenthebeatisbetweentwolines_2, x, y, paint);
				canvas.drawText(whenthebeatisbetweentwolines_3, x, y + 17, paint);
			}
		}

		@Override
		public void onUpdate(float seconds) {
			if (songTime > 1) {
				songTime = 0;
			}
		}

		@Override
		public void onTouchDown(float x, float y) {
			x -= GAME_X;
			y -= GAME_Y;
			if (x < 0) {
				// Player shouldn't be tapping on the beat track
				dontTapOnBeatTrack = true;
			} else {
				dontTapOnBeatTrack = false;
				for (int i = 0; i < beats.size(); i++) {
					if (beats.get(i).onTouchDown((int) (songTime * 1000))) {
						localPlayer.spawnUnit(x, y);
						transition(new UnitsUpAndDownStep());
						break;
					}
				}
			}
		}
	}

	private class UnitsUpAndDownStep extends Step {
		private static final float x = GAME_X + GAME_WIDTH / 2;
		private static final float y = GAME_Y + GAME_HEIGHT * 0.33f;
		private boolean wasPaused;
		private float time;

		String yourunitstravelup_1 = GameActivity.instance.getString(R.string.yourunitstravelup_1);
		String yourunitstravelup_2 = GameActivity.instance.getString(R.string.yourunitstravelup_2);
		
		@Override
		public void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			paint.setColor(Color.WHITE);
			paint.setTextSize(15);
			paint.setTextAlign(Align.CENTER);
			canvas.drawText(yourunitstravelup_1, x, y - 8, paint);
			canvas.drawText(yourunitstravelup_2, x, y + 8, paint);
		}

		@Override
		public void onUpdate(float seconds) {
			time += seconds;
			if (!wasPaused && isPaused()) {
				addOKButton(x, y);
				wasPaused = true;
			}
		}

		@Override
		public boolean isPaused() {
			return time > 6;
		}

		@Override
		public void onClick(UIButton button) {
			transition(new WavefrontStep());
		}
	}

	private class WavefrontStep extends Step {
		private static final float x = GAME_X + GAME_WIDTH / 2;
		private static final float y = GAME_Y + GAME_HEIGHT * 0.75f;
		private boolean wasPaused;
		private float time;

		String youcanonlyplaceunits_1 = GameActivity.instance.getString(R.string.youcanonlyplaceunits_1);
		String youcanonlyplaceunits_2 = GameActivity.instance.getString(team.getYouCanOnlyPlaceUnitsResourceID());
		
		@Override
		public void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			paint.setColor(Color.WHITE);
			paint.setTextSize(15);
			paint.setTextAlign(Align.CENTER);
			canvas.drawText(youcanonlyplaceunits_1, x, y - 8, paint);
			canvas.drawText(youcanonlyplaceunits_2, x, y + 8, paint);
		}

		@Override
		public void onUpdate(float seconds) {
			time += seconds;
			if (!wasPaused && isPaused()) {
				addOKButton(x, y);
				wasPaused = true;
			}
		}

		@Override
		public boolean isPaused() {
			return time > 6;
		}

		@Override
		public void onClick(UIButton button) {
			transition(new WinConditionStep());
		}
	}

	private class WinConditionStep extends Step {
		private static final float x = GAME_X + GAME_WIDTH / 2;
		private static final float y = GAME_Y + GAME_HEIGHT / 2;
		private boolean wasPaused;
		private float time;

		String youwinwhenyouropponentshealth_1 = GameActivity.instance.getString(R.string.youwinwhenyouropponentshealth_1);
		String youwinwhenyouropponentshealth_2 = GameActivity.instance.getString(R.string.youwinwhenyouropponentshealth_2);
		String youwinwhenyouropponentshealth_3 = GameActivity.instance.getString(R.string.youwinwhenyouropponentshealth_3);
		
		@Override
		public void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			paint.setColor(Color.WHITE);
			paint.setTextSize(15);
			paint.setTextAlign(Align.CENTER);
			canvas.drawText(youwinwhenyouropponentshealth_1, x, y - 17, paint);
			canvas.drawText(youwinwhenyouropponentshealth_2, x, y, paint);
			canvas.drawText(youwinwhenyouropponentshealth_3, x, y + 17, paint);
		}

		@Override
		public void onUpdate(float seconds) {
			time += seconds;
			if (!wasPaused && isPaused()) {
				addOKButton(x, y + 17);
				wasPaused = true;
			}
		}

		@Override
		public boolean isPaused() {
			return time > 6;
		}

		@Override
		public void onClick(UIButton button) {
			transition(new DrawWallStep());
		}
	}

	private class DrawWallStep extends Step {
		private boolean dontTapOnBeatTrack;
		private boolean isTooShort;
		private boolean isDragging;

		public DrawWallStep() {
			beats.clear();
			for (int i = 0; i < 4; i++) {
				beats.add(Beat.hold(1000, i * 2000));
			}
			songTime = MIN_SONG_TIME;
		}
		
		String youneedtodragyourfingeraway_1 = GameActivity.instance.getString(R.string.youneedtodragyourfingeraway_1);
		String youneedtodragyourfingeraway_2 = GameActivity.instance.getString(R.string.youneedtodragyourfingeraway_2);
		String youneedtodragyourfingeraway_3 = GameActivity.instance.getString(R.string.youneedtodragyourfingeraway_3);
		String tapfurtherrighttocreateawall_1 = GameActivity.instance.getString(R.string.tapfurtherrighttocreateawall_1);
		String tapfurtherrighttocreateawall_2 = GameActivity.instance.getString(R.string.tapfurtherrighttocreateawall_2);
		String whenabeathasatailyoucandrag_1 = GameActivity.instance.getString(R.string.whenabeathasatailyoucandrag_1);
		String whenabeathasatailyoucandrag_2 = GameActivity.instance.getString(R.string.whenabeathasatailyoucandrag_2);
		String whenabeathasatailyoucandrag_3 = GameActivity.instance.getString(R.string.whenabeathasatailyoucandrag_3);

		@Override
		public void onDraw(Canvas canvas) {
			float x = GAME_X + GAME_WIDTH / 2;
			float y = GAME_Y + GAME_HEIGHT * 0.8f;
			paint.setColor(Color.WHITE);
			paint.setTextSize(15);
			paint.setTextAlign(Align.CENTER);
			if (isTooShort) {
				canvas.drawText(youneedtodragyourfingeraway_1, x, y - 17, paint);
				canvas.drawText(youneedtodragyourfingeraway_2, x, y, paint);
				canvas.drawText(youneedtodragyourfingeraway_3, x, y + 17, paint);
			} else if (dontTapOnBeatTrack) {
				canvas.drawText(tapfurtherrighttocreateawall_1, x, y - 8, paint);
				canvas.drawText(tapfurtherrighttocreateawall_2, x, y + 8, paint);
				drawArrow(canvas, x - 25, y + 25, x + 25, y + 25, 5);
			} else {
				canvas.drawText(whenabeathasatailyoucandrag_1, x, y - 17, paint);
				canvas.drawText(whenabeathasatailyoucandrag_2, x, y, paint);
				canvas.drawText(whenabeathasatailyoucandrag_3, x, y + 17, paint);
			}
		}

		@Override
		public void onUpdate(float seconds) {
			// Continually reset the song if the player hasn't tapped any
			// beat yet so it looks like an infinite stream of beats
			for (int i = 0; i < beats.size(); i++) {
				if (beats.get(i).isTapped()) {
					return;
				}
			}
			if (songTime > 2) {
				songTime = 0;
			}
		}

		@Override
		public void onTouchDown(float x, float y) {
			if (x < GAME_X) {
				// Player shouldn't be tapping on the beat track
				dontTapOnBeatTrack = true;
			} else {
				dontTapOnBeatTrack = false;
				for (int i = 0; i < beats.size(); i++) {
					if (beats.get(i).onTouchDown((int) (songTime * 1000))) {
						isDragging = true;
						wallStartX = wallEndX = transformToGameSpaceX(x);
						wallStartY = wallEndY = transformToGameSpaceY(y);
					}
				}
			}
		}

		@Override
		public void onTouchMove(float x, float y) {
			if (isDragging) {
				wallEndX = transformToGameSpaceX(x);
				wallEndY = transformToGameSpaceY(y);
			}
		}

		@Override
		public void onTouchUp(float x, float y) {
			if (isDragging) {
				wallEndX = transformToGameSpaceX(x);
				wallEndY = transformToGameSpaceY(y);
				BBTHSimulation.generateParticlesForWall(new Wall(wallStartX, wallStartY, wallEndX, wallEndY), team);
				isTooShort = MathUtils.getDist(wallStartX, wallStartY, wallEndX, wallEndY) < 30;
				isDragging = false;
				if (isTooShort) {
					for (int i = 0; i < beats.size(); i++) {
						beats.set(i, Beat.hold(1000, i * 2000));
					}
				} else {
					transition(new WallBlockStep());
				}
			}
		}
	}

	private class WallBlockStep extends Step {
		private static final float x = GAME_X + GAME_WIDTH / 2;
		private static final float y = GAME_Y + GAME_HEIGHT / 2;
		private boolean wasPaused;
		private float time;

		String unitstakelongertogoaroundwalls_1 = GameActivity.instance.getString(R.string.unitstakelongertogoaroundwalls_1);
		String unitstakelongertogoaroundwalls_2 = GameActivity.instance.getString(R.string.unitstakelongertogoaroundwalls_2);
		
		@Override
		public void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			paint.setColor(Color.WHITE);
			paint.setTextSize(15);
			paint.setTextAlign(Align.CENTER);
			canvas.drawText(unitstakelongertogoaroundwalls_1, x, y - 8, paint);
			canvas.drawText(unitstakelongertogoaroundwalls_2, x, y + 8, paint);
		}

		@Override
		public void onUpdate(float seconds) {
			time += seconds;
			if (!wasPaused && isPaused()) {
				addOKButton(x, y);
				wasPaused = true;
			}
		}

		@Override
		public boolean isPaused() {
			return time > 6;
		}

		@Override
		public void onClick(UIButton button) {
			transition(new GoodLuckStep());
		}
	}

	private class GoodLuckStep extends Step {
		private static final float x = GAME_X + GAME_WIDTH / 2;
		private static final float y = GAME_Y + GAME_HEIGHT / 2;
		private float time;

		String havefunandgoodluck = GameActivity.instance.getString(R.string.havefunandgoodluck);
		@Override
		public void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			paint.setColor(Color.WHITE);
			paint.setTextSize(15);
			paint.setTextAlign(Align.CENTER);
			canvas.drawText(havefunandgoodluck, x, y, paint);
		}

		@Override
		public void onUpdate(float seconds) {
			time += seconds;
			if (time > 4) {
				transition(new FinishedStep());
			}
		}
	}

	private class FinishedStep extends Step {
	}

	private Step step;
	private UIButton skipButton;
	private Team team;
	private Player serverPlayer;
	private Player clientPlayer;
	private Player localPlayer;
	private Player remotePlayer;
	private AIController aiController;
	private List<Beat> beats = new ArrayList<Beat>();
	private float songTime = MIN_SONG_TIME;
	private GridAcceleration accel;
	private FastGraphGenerator gen;
	private Pathfinder pathfinder;
	private FastLineOfSightTester tester;
	private float wallStartX;
	private float wallStartY;
	private float wallEndX;
	private float wallEndY;

	public InteractiveTutorial(Team localTeam) {
		skipButton = new UIButton(R.string.skiptutorial);
		skipButton.setAnchor(Anchor.TOP_RIGHT);
		skipButton.setSize(100, 30);
		skipButton.setPosition(BBTHGame.WIDTH - 20, Base.BASE_HEIGHT + 20);
		skipButton.setButtonDelegate(this);
		skipButton.expandForHitTesting(20, 20);
		addSubview(skipButton);

		team = localTeam;
		aiController = new AIController();
		serverPlayer = new Player(Team.SERVER, aiController, this, team == Team.SERVER);
		clientPlayer = new Player(Team.CLIENT, aiController, this, team == Team.CLIENT);
		localPlayer = (team == Team.SERVER) ? serverPlayer : clientPlayer;
		remotePlayer = (team == Team.SERVER) ? clientPlayer : serverPlayer;

		gen = new FastGraphGenerator(15.0f, GAME_WIDTH, GAME_HEIGHT);
		pathfinder = new Pathfinder(gen.graph);
		accel = new GridAcceleration(GAME_WIDTH, GAME_HEIGHT, GAME_WIDTH / 10);
		tester = new FastLineOfSightTester(15, accel);
		aiController.setPathfinder(pathfinder, gen.graph, tester, accel);

		transition(new PlaceUnitStep());
	}

	@Override
	public boolean isFinished() {
		return step instanceof FinishedStep;
	}

	@Override
	public void onClick(UIButton button) {
		transition(new FinishedStep());
	}

	protected void transition(Step newStep) {
		if (step != null) {
			removeSubview(step);
		}
		step = newStep;
		if (step != null) {
			addSubview(step);
		}
	}

	protected static void drawArrow(Canvas canvas, float ax, float ay, float bx, float by, float r) {
		final float s = 2.5f;
		float dx = bx - ax;
		float dy = by - ay;
		float d = r / FloatMath.sqrt(dx * dx + dy * dy);
		dx *= d;
		dy *= d;
		path.reset();
		path.moveTo(ax - dy, ay + dx);
		path.lineTo(bx - dy - s * dx, by + dx - s * dy);
		path.lineTo(bx - s * dy - s * dx, by + s * dx - s * dy);
		path.lineTo(bx - dy - dx, by + dx - dy);
		path.lineTo(bx, by);
		path.lineTo(bx + dy - dx, by - dx - dy);
		path.lineTo(bx + s * dy - s * dx, by - s * dx - s * dy);
		path.lineTo(bx + dy - s * dx, by - dx - s * dy);
		path.lineTo(ax + dy, ay - dx);
		canvas.drawPath(path, paint);
	}

	protected static void drawDashedLine(Canvas canvas, float ax, float ay, float bx, float by, float r, float percent) {
		float dx = bx - ax;
		float dy = by - ay;
		float d = FloatMath.sqrt(dx * dx + dy * dy);
		dx /= d;
		dy /= d;
		for (float t = percent * r * 2 - r; t < d; t += 2 * r) {
			float t1 = Math.max(0, t);
			float t2 = Math.min(d, t + r);
			canvas.drawLine(ax + dx * t1, ay + dy * t1, ax + dx * t2, ay + dy * t2, paint);
		}
	}

	protected void transformToGameSpace(Canvas canvas, Team team) {
		canvas.translate(GAME_X, GAME_Y);
		if (team == Team.SERVER) {
			canvas.translate(0, GAME_HEIGHT / 2);
			canvas.scale(1.f, -1.f);
			canvas.translate(0, -GAME_HEIGHT / 2);
		}
	}

	private void drawWavefronts(Canvas canvas) {
		Unit serverAdvUnit = serverPlayer.getMostAdvancedUnit();
		Unit clientAdvUnit = clientPlayer.getMostAdvancedUnit();
		float serverWavefrontY = serverAdvUnit != null ? serverAdvUnit.getY() + 10 : 0;
		float clientWavefrontY = clientAdvUnit != null ? clientAdvUnit.getY() - 10 : GAME_HEIGHT;
		paint.setStyle(Style.FILL);

		// server wavefront
		paint.setColor(Team.SERVER.getWavefrontColor());
		canvas.drawRect(0, 0, GAME_WIDTH, Math.min(clientWavefrontY, serverWavefrontY), paint);

		// client wavefront
		paint.setColor(Team.CLIENT.getWavefrontColor());
		canvas.drawRect(0, Math.max(clientWavefrontY, serverWavefrontY), GAME_WIDTH, GAME_HEIGHT, paint);

		// overlapped wavefronts
		if (serverWavefrontY > clientWavefrontY) {
			paint.setColor(Color.rgb(63, 0, 63));
			canvas.drawRect(0, clientWavefrontY, GAME_WIDTH, serverWavefrontY, paint);
		}

		// server wavefront line
		paint.setColor(Team.SERVER.getUnitColor());
		canvas.drawLine(0, serverWavefrontY, GAME_WIDTH, serverWavefrontY, paint);

		// client wavefront line
		paint.setColor(Team.CLIENT.getUnitColor());
		canvas.drawLine(0, clientWavefrontY, GAME_WIDTH, clientWavefrontY, paint);
	}

	private void drawGrid(Canvas canvas) {
		paint.setARGB(63, 255, 255, 255);
		for (float x = 0; x < GAME_WIDTH; x += GAME_WIDTH / 6) {
			canvas.drawLine(x, 0, x, GAME_HEIGHT, paint);
		}
		for (float y = 0; y < GAME_HEIGHT; y += GAME_HEIGHT / 12) {
			canvas.drawLine(0, y, GAME_WIDTH, y, paint);
		}
	}

	private void drawBeatTrack(Canvas canvas) {
		paint.setStrokeWidth(2);
		paint.setARGB(127, 255, 255, 255);
		canvas.drawLine(BEAT_LINE_X, 0, BEAT_LINE_X, BEAT_LINE_Y - BEAT_CIRCLE_RADIUS, paint);
		canvas.drawLine(BEAT_LINE_X, BEAT_LINE_Y + BEAT_CIRCLE_RADIUS, BEAT_LINE_X, BBTHGame.HEIGHT, paint);

		for (int i = 0; i < beats.size(); i++) {
			beats.get(i).draw((int) (songTime * 1000), BEAT_LINE_X, BEAT_LINE_Y, canvas, paint);
		}

		paint.setColor(Color.WHITE);
		canvas.drawLine(BEAT_LINE_X - BEAT_TRACK_WIDTH / 2, BEAT_LINE_Y - BEAT_CIRCLE_RADIUS, BEAT_LINE_X + BEAT_TRACK_WIDTH / 2, BEAT_LINE_Y - BEAT_CIRCLE_RADIUS, paint);
		canvas.drawLine(BEAT_LINE_X - BEAT_TRACK_WIDTH / 2, BEAT_LINE_Y + BEAT_CIRCLE_RADIUS, BEAT_LINE_X + BEAT_TRACK_WIDTH / 2, BEAT_LINE_Y + BEAT_CIRCLE_RADIUS, paint);
		paint.setStrokeWidth(1);
	}

	@Override
	public void onUpdate(float seconds) {
		super.onUpdate(seconds);

		if (!step.isPaused()) {
			accel.clearUnits();
			accel.insertUnits(serverPlayer.units);
			accel.insertUnits(clientPlayer.units);
			aiController.update();
			serverPlayer.update(seconds);
			clientPlayer.update(seconds);
			serverPlayer.base.damageUnits(accel);
			clientPlayer.base.damageUnits(accel);
			songTime += seconds;
		}
	}

	@Override
	public void onDraw(Canvas canvas) {
		canvas.save();
		transformToGameSpace(canvas, team);
		drawWavefronts(canvas);
		drawGrid(canvas);
		localPlayer.draw(canvas, team == Team.SERVER);
		remotePlayer.draw(canvas, team == Team.SERVER);
		BBTHGame.PARTICLES.draw(canvas, BBTHGame.PARTICLE_PAINT);
		paint.setColor(team.getWallColor());
		canvas.drawLine(wallStartX, wallStartY, wallEndX, wallEndY, paint);
		canvas.restore();
		drawBeatTrack(canvas);
		super.onDraw(canvas);
	}

	@Override
	public boolean supressDrawing() {
		return true;
	}

	@Override
	public void notifyUnitDead(Unit unit) {
		serverPlayer.units.remove(unit);
		clientPlayer.units.remove(unit);
		aiController.removeEntity(unit);
	}

	@Override
	public Bag<Unit> getUnitsInCircle(float x, float y, float r) {
		return emptyUnitBag;
	}

	@Override
	public Bag<Unit> getUnitsIntersectingLine(float x, float y, float x2, float y2) {
		return emptyUnitBag;
	}

	@Override
	public void removeWall(Wall wall) {
	}

	private float transformToGameSpaceX(float x) {
		return x - GAME_X;
	}

	private float transformToGameSpaceY(float y) {
		y -= GAME_Y;
		if (team == Team.SERVER) {
			y = GAME_HEIGHT - y;
		}
		return y;
	}
}
