package bbth.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.FloatMath;
import bbth.engine.ai.Pathfinder;
import bbth.engine.fastgraph.FastGraphGenerator;
import bbth.engine.fastgraph.Wall;
import bbth.engine.net.simulation.Hash;
import bbth.engine.net.simulation.LockStepProtocol;
import bbth.engine.net.simulation.Simulation;
import bbth.engine.particles.Particle;
import bbth.engine.ui.UIScrollView;
import bbth.engine.util.Bag;
import bbth.engine.util.MathUtils;
import bbth.engine.util.Timer;
import bbth.game.achievements.BBTHAchievementManager;
import bbth.game.achievements.events.BaseDestroyedEvent;
import bbth.game.achievements.events.BeatHitEvent;
import bbth.game.achievements.events.GameEndedEvent;
import bbth.game.achievements.events.UnitDeadEvent;
import bbth.game.achievements.events.UpdateEvent;
import bbth.game.ai.AIController;
import bbth.game.units.Unit;
import bbth.game.units.UnitManager;
import bbth.game.units.UnitType;

public class BBTHSimulation extends Simulation implements UnitManager {
	public static enum GameState {
		WAITING_TO_START, IN_PROGRESS, SERVER_WON, CLIENT_WON, TIE,
	}

	private GameState gameState = GameState.WAITING_TO_START;

	private Team team;
	public Player localPlayer, remotePlayer;
	private HashMap<Boolean, Player> playerMap;
	public Player serverPlayer, clientPlayer;
	private AIController aiController;
	private Pathfinder pathFinder;
	private FastGraphGenerator graphGen;
	private FastLineOfSightTester tester;
	private GridAcceleration accel;
	private Paint paint = new Paint();
	private Bag<Unit> cachedUnitBag = new Bag<Unit>();
	private HashSet<Unit> cachedUnitSet = new HashSet<Unit>();
	public Timer accelTickTimer = new Timer();
	public Timer aiTickTimer = new Timer();
	public Timer entireTickTimer = new Timer();
	public Timer aiControllerTimer = new Timer();
	public Timer serverPlayerTimer = new Timer();
	public Timer clientPlayerTimer = new Timer();
	private static final Random random = new Random();
	private boolean serverReady;
	private boolean clientReady;
	private float startingCountdown = 3;

	// This is the virtual size of the game
	public static final float GAME_X = BeatTrack.BEAT_TRACK_WIDTH;
	public static final float GAME_Y = 0;
	public static final float GAME_WIDTH = BBTHGame.WIDTH - BeatTrack.BEAT_TRACK_WIDTH;
	public static final float GAME_HEIGHT = BBTHGame.HEIGHT;

	// Minimal length of a wall
	public static final float MIN_WALL_LENGTH = 5.f;

	// Combo constants
	public static final float UBER_UNIT_THRESHOLD = 5;
	public static final int TUTORIAL_DONE_EVENT = 13;
	public static final int MUSIC_STOPPED_EVENT = 69;

	long placement_tip_start_time;

	// The song id
	public Song song;

	private InGameScreen inGameScreen;
	public BBTHSimulation(Team localTeam, LockStepProtocol protocol, boolean isServer, InGameScreen inGameScreen) {
		// 3 fine timesteps per coarse timestep
		// coarse timestep takes 0.1 seconds
		// user inputs lag 2 coarse timesteps behind
		super(3, 0.1f, 2, protocol, isServer);

		this.inGameScreen = inGameScreen;
		
		aiController = new AIController();
		accel = new GridAcceleration(GAME_WIDTH, GAME_HEIGHT, GAME_WIDTH / 10);

		team = localTeam;
		serverPlayer = new Player(Team.SERVER, aiController, this, team == Team.SERVER);
		clientPlayer = new Player(Team.CLIENT, aiController, this, team == Team.CLIENT);
		localPlayer = (team == Team.SERVER) ? serverPlayer : clientPlayer;
		remotePlayer = (team == Team.SERVER) ? clientPlayer : serverPlayer;

		playerMap = new HashMap<Boolean, Player>();
		playerMap.put(true, serverPlayer);
		playerMap.put(false, clientPlayer);

		graphGen = new FastGraphGenerator(15.0f, GAME_WIDTH, GAME_HEIGHT);
		graphGen.compute();
		accel.insertWalls(graphGen.walls);

		pathFinder = new Pathfinder(graphGen.graph);
		tester = new FastLineOfSightTester(15.f, accel);

		aiController.setPathfinder(pathFinder, graphGen.graph, tester, accel);
		aiController.setUpdateFraction(.10f);
	}

	public GameState getGameState() {
		return gameState;
	}

	public void setupSubviews(UIScrollView view) {
		localPlayer.setupSubviews(view, true);
		remotePlayer.setupSubviews(view, false);
	}

	public Unit getOpponentsMostAdvancedUnit() {
		return remotePlayer.getMostAdvancedUnit();
	}

	public UnitSelector getMyUnitSelector() {
		return localPlayer.getUnitSelector();
	}

	// Only use BBTHSimulation.randInRange() for things that are supposed to
	// be synced (not particles!)
	public static float randInRange(float min, float max) {
		return (max - min) * random.nextFloat() + min;
	}

	@Override
	protected void simulateTapDown(float x, float y, boolean isServer,
			boolean isHold, boolean isOnBeat) {
		// Don't interact at all if the game isn't running
		if (gameState != GameState.IN_PROGRESS)
			return;

		Player player = playerMap.get(isServer);

		if (x < 0 || y < 0)
			return;

		if (placement_tip_start_time == 0
				&& player.getMostAdvancedUnit() != null) {
			if (((isServer && y > player.getMostAdvancedUnit().getY()) || (!isServer && y < player.getMostAdvancedUnit().getY()))) {
				placement_tip_start_time = System.currentTimeMillis();
			}
		}

		if (isOnBeat) {
			if (isHold) {
				player.startWall(x, y);
			} else {
				float newcombo = player.getCombo() + 1;
				player.setCombo(newcombo);

				player.spawnUnit(x, y);
			}
			
			if (beatHitEvent != null) {
				beatHitEvent.set(player);
				BBTHAchievementManager.INSTANCE.notifyBeatHit(beatHitEvent);
			}
		} else {
			player.setCombo(0);
		}
	}

	@Override
	protected void simulateTapMove(float x, float y, boolean isServer) {
		// Don't interact at all if the game isn't running
		if (gameState != GameState.IN_PROGRESS)
			return;

		Player player = playerMap.get(isServer);

		if (!player.settingWall())
			return;

		if (x < 0 || y < 0) {
			generateWall(player);
		} else {
			player.updateWall(x, y);
		}
	}

	@Override
	protected void simulateTapUp(float x, float y, boolean isServer) {
		// Don't interact at all if the game isn't running
		if (gameState != GameState.IN_PROGRESS)
			return;

		Player player = playerMap.get(isServer);
		generateWall(player);
	}

	@Override
	protected void simulateCustomEvent(float x, float y, int code, boolean isServer) {
		if (code < 0) {
			this.song = Song.fromInt(code);
			setupEvents();
		} else {
			Player player = playerMap.get(isServer);

			UnitType type = UnitType.fromInt(code);
			if (type != null) {
				player.setUnitType(type);
			} else if (code == TUTORIAL_DONE_EVENT) {
				if (isServer) {
					serverReady = true;
				} else {
					clientReady = true;
				}
				if (serverReady && clientReady) {
					Unit.resetNextHashCodeID();
					Wall.resetNextHashCodeID();
				}
			} else if ((gameState == GameState.IN_PROGRESS || gameState == GameState.WAITING_TO_START) && code == MUSIC_STOPPED_EVENT) {
				endTheGame();
			}
		}
	}

	/**
	 * Creates a wall out of the given player, and lets the AI know about it.
	 */
	public void generateWall(Player player) {
		if (!player.settingWall())
			return;

		Wall w = player.endWall();
		if (w == null)
			return;

		if (player != localPlayer) {
			BBTHSimulation.generateParticlesForWall(w, player.getTeam());
		}

		addWall(w);
	}

	public static void generateParticlesForWall(Wall wall, Team team) {
		int numParticles = 40;

		for (int i = 0; i < numParticles; i++) {
			float posX = wall.a.x * i / numParticles + wall.b.x
					* (numParticles - i) / numParticles;
			float posY = wall.a.y * i / numParticles + wall.b.y
					* (numParticles - i) / numParticles;
			float angle = MathUtils.randInRange(0, 2 * MathUtils.PI);
			float xVel = MathUtils.randInRange(25.f, 50.f)
					* FloatMath.cos(angle);
			float yVel = MathUtils.randInRange(25.f, 50.f)
					* FloatMath.sin(angle);

			BBTHGame.PARTICLES.createParticle().circle().velocity(xVel, yVel)
					.shrink(0.1f, 0.15f).radius(3.0f).position(posX, posY)
					.color(team.getRandomShade());
		}
	}

	private void addWall(Wall wall) {
		graphGen.walls.add(wall);
		graphGen.compute();
		accel.clearWalls();
		accel.insertWalls(graphGen.walls);
	}

	@Override
	public void removeWall(Wall wall) {
		graphGen.walls.remove(wall);
		graphGen.compute();
		accel.clearWalls();
		accel.insertWalls(graphGen.walls);
	}

	@Override
	protected void update(float seconds) {
		if (!isReady()) {
			return;
		}

		if (gameState == GameState.WAITING_TO_START) {
			startingCountdown -= seconds;
			if (startingCountdown < 0) {
				// THIS IS IMPORTANT
				random.setSeed(0);

				gameState = GameState.IN_PROGRESS;
			}
		}

		// DON'T ADVANCE THE SIMULATION WHEN WE AREN'T PLAYING
		if (gameState != GameState.IN_PROGRESS) {
			return;
		}

		entireTickTimer.start();

		// update acceleration data structure
		accelTickTimer.start();
		accel.clearUnits();
		accel.insertUnits(serverPlayer.units);
		accel.insertUnits(clientPlayer.units);
		accelTickTimer.stop();

		aiTickTimer.start();

		aiControllerTimer.start();
		aiController.update();
		aiControllerTimer.stop();

		serverPlayerTimer.start();
		serverPlayer.update(seconds);
		serverPlayerTimer.stop();

		clientPlayerTimer.start();
		clientPlayer.update(seconds);
		clientPlayerTimer.stop();
		
		// Prevent units from leaving the play area
		int size = serverPlayer.units.size();
		for (int i = 0; i < size; i++)
		{
			serverPlayer.units.get(i).setX(MathUtils.clamp(0, GAME_WIDTH, serverPlayer.units.get(i).getX()));
		}
		
		size = clientPlayer.units.size();
		for (int i = 0; i < size; i++)
		{
			clientPlayer.units.get(i).setX(MathUtils.clamp(0, GAME_WIDTH, clientPlayer.units.get(i).getX()));
		}

		aiTickTimer.stop();

		serverPlayer.base.damageUnits(accel);
		clientPlayer.base.damageUnits(accel);

		if (gameState == GameState.IN_PROGRESS && localPlayer.getHealth() <= 0 || remotePlayer.getHealth() <= 0) {
			endTheGame();
		}
		
		updateEvent.set(seconds);
		BBTHAchievementManager.INSTANCE.notifyUpdate(updateEvent);

		entireTickTimer.stop();
	}

	private void drawGrid(Canvas canvas) {
		paint.setARGB(60, 255, 255, 255);
		paint.setStrokeWidth(1);

		for (float x = 0; x < GAME_WIDTH; x += GAME_WIDTH/6.0f) {
//			float offset = 10 * FloatMath.cos(((x/GAME_WIDTH) * 4 * MathUtils.PI + (System.currentTimeMillis()%10000/10000.0f) * 2 * MathUtils.PI));
//			if (x + offset < 0) {
//				offset = -x;
//			}
//			canvas.drawLine(x + offset, 0, x + offset, GAME_HEIGHT, paint);
			canvas.drawLine(x, 0, x, GAME_HEIGHT, paint);
		}
		for (float y = 0; y < GAME_HEIGHT; y += GAME_HEIGHT/12.0f) {
//			float offset = 10 * FloatMath.sin(((y/GAME_HEIGHT) * 8 * MathUtils.PI + (System.currentTimeMillis()%10000/10000.0f) * 2 * MathUtils.PI));
//			canvas.drawLine(0, y + offset, GAME_WIDTH, y + offset, paint);
			canvas.drawLine(0, y, GAME_WIDTH, y, paint);
		}
	}

	public void draw(Canvas canvas) {
		boolean serverDraw = team == Team.SERVER;
		
		if (gameState == GameState.IN_PROGRESS) {
			drawWavefronts(canvas);
		}
		
		drawGrid(canvas);

		localPlayer.draw(canvas, serverDraw);
		remotePlayer.draw(canvas, serverDraw);
		
		BBTHGame.PARTICLES.draw(canvas, BBTHGame.PARTICLE_PAINT);

		if (BBTHGame.DEBUG) {
			graphGen.draw(canvas);
		}

		localPlayer.postDraw(canvas, serverDraw);
		remotePlayer.postDraw(canvas, serverDraw);
	}

	private void drawWavefronts(Canvas canvas) {
		Unit serverAdvUnit = serverPlayer.getMostAdvancedUnit();
		Unit clientAdvUnit = clientPlayer.getMostAdvancedUnit();
		float serverWavefrontY = serverAdvUnit != null ? serverAdvUnit.getY() + 10 : 0;
		float clientWavefrontY = clientAdvUnit != null ? clientAdvUnit.getY() - 10 : BBTHSimulation.GAME_HEIGHT;
		paint.setStyle(Style.FILL);

		// server wavefront
		paint.setColor(Team.SERVER.getWavefrontColor());
		canvas.drawRect(0, 0, BBTHSimulation.GAME_WIDTH,
				Math.min(clientWavefrontY, serverWavefrontY), paint);

		// client wavefront
		paint.setColor(Team.CLIENT.getWavefrontColor());
		canvas.drawRect(0, Math.max(clientWavefrontY, serverWavefrontY),
				BBTHSimulation.GAME_WIDTH, BBTHSimulation.GAME_HEIGHT, paint);

		// overlapped wavefronts
		if (serverWavefrontY > clientWavefrontY) {
			paint.setColor(Color.rgb(63, 0, 63));
			canvas.drawRect(0, clientWavefrontY, BBTHSimulation.GAME_WIDTH, serverWavefrontY, paint);
		}
		
		// server wavefront line
		paint.setColor(Team.SERVER.getUnitColor());
		canvas.drawLine(0, serverWavefrontY, BBTHSimulation.GAME_WIDTH, serverWavefrontY, paint);
		
		// client wavefront line
		paint.setColor(Team.CLIENT.getUnitColor());
		canvas.drawLine(0, clientWavefrontY, BBTHSimulation.GAME_WIDTH, clientWavefrontY, paint);
	}

	@Override
	public void notifyUnitDead(Unit unit) {
		// for (int i = 0; i < 10; i++) {
		// float angle = MathUtils.randInRange(0, 2 * MathUtils.PI);
		// float xVel = MathUtils.randInRange(25.f, 50.f)
		// * FloatMath.cos(angle);
		// float yVel = MathUtils.randInRange(25.f, 50.f)
		// * FloatMath.sin(angle);
		//
		// BBTHSimulation.PARTICLES.createParticle().circle()
		// .velocity(xVel, yVel).shrink(0.1f, 0.15f).radius(3.0f)
		// .position(unit.getX(), unit.getY())
		// .color(unit.getTeam().getRandomShade());
		// }

		serverPlayer.units.remove(unit);
		clientPlayer.units.remove(unit);
		aiController.removeEntity(unit);
		unitDeadEvent.set(unit);
		BBTHAchievementManager.INSTANCE.notifyUnitDead(unitDeadEvent);
	}

	/**
	 * WILL RETURN THE SAME BAG OVER AND OVER
	 */
	@Override
	public Bag<Unit> getUnitsInCircle(float x, float y, float r) {
		float r2 = r * r;
		cachedUnitBag.clear();
		accel.getUnitsInAABB(x - r, y - r, x + r, y + r, cachedUnitSet);
		for (Unit unit : cachedUnitSet) {
			float dx = x - unit.getX();
			float dy = y - unit.getY();
			if (dx * dx + dy * dy < r2) {
				cachedUnitBag.add(unit);
			}
		}
		return cachedUnitBag;
	}

	/**
	 * WILL RETURN THE SAME BAG OVER AND OVER
	 */
	@Override
	public Bag<Unit> getUnitsIntersectingLine(float x, float y, float x2,
			float y2) {
		cachedUnitBag.clear();

		// calculate axis vector
		float axisX = -(y2 - y);
		float axisY = x2 - x;

		// normalize axis vector
		float axisLen = FloatMath.sqrt(axisX * axisX + axisY * axisY);
		axisX /= axisLen;
		axisY /= axisLen;

		float lMin = axisX * x + axisY * y;
		float lMax = axisX * x2 + axisY * y2;
		if (lMax < lMin) {
			float temp = lMin;
			lMin = lMax;
			lMax = temp;
		}

		accel.getUnitsInAABB(Math.min(x, y), Math.min(y, y2), Math.max(x2, x2),
				Math.max(y, y2), cachedUnitSet);

		for (Unit unit : cachedUnitSet) {
			// calculate projections
			float projectedCenter = axisX * unit.getX() + axisY * unit.getY();
			float radius = unit.getRadius();
			if (!intervalsDontOverlap(projectedCenter - radius, projectedCenter + radius, lMin, lMax)) {
				cachedUnitBag.add(unit);
			}
		}

		return cachedUnitBag;
	}

	private static final boolean intervalsDontOverlap(float min1, float max1,
			float min2, float max2) {
		return (min1 < min2 ? min2 - max1 : min1 - max2) > 0;
	}

	public boolean isReady() {
		return (clientReady && serverReady);
	}

	@Override
	protected int getSimulationSyncHash() {
		int hash = 0;
		hash = Hash.mix(hash, serverPlayer.getSimulationSyncHash());
		hash = Hash.mix(hash, clientPlayer.getSimulationSyncHash());
		for (int i = 0, n = graphGen.walls.size(); i < n; i++) {
			hash = Hash.mix(hash, graphGen.walls.get(i).getSimulationSyncHash());
		}
		return hash;
	}

	public void setBothPlayersReady() {
		clientReady = serverReady = true;
		Unit.resetNextHashCodeID();
		Wall.resetNextHashCodeID();
	}

	private void endTheGame() {
		float serverHealth = Math.max(0, serverPlayer.getHealth());
		float clientHealth = Math.max(0, clientPlayer.getHealth());
		gameState = (serverHealth > clientHealth) ? GameState.SERVER_WON : (serverHealth < clientHealth) ? GameState.CLIENT_WON : GameState.TIE;
			
		// PARTICLES!
		if (gameState != GameState.SERVER_WON) {
			explodeBase(Team.SERVER);
		}
		if (gameState != GameState.CLIENT_WON) {
			explodeBase(Team.CLIENT);
		}
		
		// achievement notifications
		if (serverHealth == 0) {
			baseDestroyedEvent.set(serverPlayer, inGameScreen.getBeatTrack());
			BBTHAchievementManager.INSTANCE.notifyBaseDestroyed(baseDestroyedEvent);
		}
		if (clientHealth == 0) {
			baseDestroyedEvent.set(clientPlayer, inGameScreen.getBeatTrack());
			BBTHAchievementManager.INSTANCE.notifyBaseDestroyed(baseDestroyedEvent);
		}
		
		gameEndedEvent.set(gameState == GameState.SERVER_WON ? serverPlayer : clientPlayer, gameState == GameState.TIE, inGameScreen.getBeatTrack());
		BBTHAchievementManager.INSTANCE.notifyGameEnded(gameEndedEvent);
		
	}

	private void explodeBase(Team team) {
		float speed = 100;
		float baseY = (team == Team.SERVER) ? 0 : GAME_HEIGHT - Base.BASE_HEIGHT;
		for (int i = 0; i < 50; i++) {
			float x = MathUtils.randInRange(0, GAME_WIDTH);
			float y = baseY + MathUtils.randInRange(0, Base.BASE_HEIGHT);
			float angle = MathUtils.randInRange(0, MathUtils.PI);
			if (team == Team.CLIENT) angle += MathUtils.PI;
			float radius = MathUtils.randInRange(0, speed);
			float vx = FloatMath.cos(angle) * radius;
			float vy = FloatMath.sin(angle) * radius;
			Particle particle = BBTHGame.PARTICLES.createParticle().position(x, y).velocity(vx, vy).shrink(0.1f, 0.15f).radius(10).color(team.getRandomShade()).angle(angle);
			if ((i & 1) != 0) {
				particle.line();
			} else {
				particle.circle();
			}
		}
		if (team == Team.CLIENT) {
			clientPlayer.base.drawFill = false;
		} else {
			serverPlayer.base.drawFill = false;
		}
	}

	public float getStartingCountdown() {
		return startingCountdown;
	}
	
	private UnitDeadEvent unitDeadEvent;
	private BaseDestroyedEvent baseDestroyedEvent;
	private GameEndedEvent gameEndedEvent;
	private BeatHitEvent beatHitEvent;
	private UpdateEvent updateEvent;
	private void setupEvents() {
		boolean singlePlayer = inGameScreen.singlePlayer;
		float aiDifficulty = inGameScreen.aiDifficulty;
		
		gameEndedEvent = new GameEndedEvent(this, singlePlayer, aiDifficulty);
		baseDestroyedEvent = new BaseDestroyedEvent(this, singlePlayer, aiDifficulty);
		beatHitEvent = new BeatHitEvent(this, singlePlayer, aiDifficulty);
		unitDeadEvent = new UnitDeadEvent(this, inGameScreen.singlePlayer, inGameScreen.aiDifficulty);
		updateEvent = new UpdateEvent(this, singlePlayer, aiDifficulty);
		
		serverPlayer.setupEvents(inGameScreen, this);
		clientPlayer.setupEvents(inGameScreen, this);
	}
}
