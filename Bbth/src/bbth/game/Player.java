package bbth.game;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.util.FloatMath;
import bbth.engine.fastgraph.Wall;
import bbth.engine.net.simulation.Hash;
import bbth.engine.ui.Anchor;
import bbth.engine.ui.UIScrollView;
import bbth.engine.util.MathUtils;
import bbth.engine.util.Vibrate;
import bbth.game.achievements.BBTHAchievementManager;
import bbth.game.achievements.events.UnitCreatedEvent;
import bbth.game.ai.AIController;
import bbth.game.units.Unit;
import bbth.game.units.UnitManager;
import bbth.game.units.UnitType;
import bbth.game.units.WallUnit;

/**
 * A player is someone who is interacting with the game.
 */
public class Player {
	private Team team;
	public List<Unit> units;
	public Base base;
	private AIController aiController;
	private Paint paint;
	private UnitSelector selector;
	private int _health;
	private float _combo;
	private boolean _isLocal;
	
	private int totalUnitsCreated;

	public ArrayList<WallUnit> walls;
	private Wall currentWall;
	private UnitManager unitManager;

	public Player(Team team, AIController controller, UnitManager unitManager, boolean isLocal) {
		_isLocal = isLocal;
		this.team = team;
		this.unitManager = unitManager;
		units = new ArrayList<Unit>();

		base = new Base(this);
		resetHealth();
		setCombo(0);

		paint = new Paint();
		paint.setStrokeWidth(2.0f);
		paint.setStrokeJoin(Join.ROUND);
		paint.setTextSize(20);
		paint.setAntiAlias(true);
		paint.setColor(team.getUnitColor());
		paint.setStrokeCap(Cap.ROUND);

		switch (team) {
		case CLIENT:
			base.setAnchor(Anchor.BOTTOM_LEFT);
			base.setPosition(0, BBTHSimulation.GAME_HEIGHT);
			break;

		case SERVER:
			base.setAnchor(Anchor.TOP_LEFT);
			base.setPosition(0, 0);
			break;
		}

		this.aiController = controller;
		selector = new UnitSelector(team, unitManager, BBTHGame.PARTICLES);

		walls = new ArrayList<WallUnit>();
	}

	public Team getTeam() {
		return this.team;
	}

	public boolean settingWall() {
		return currentWall != null;
	}

	public void startWall(float x, float y) {
		currentWall = new Wall(x, y, x, y);
	}

	public void updateWall(float x, float y) {
		currentWall.b.set(x, y);
	}

	public Wall endWall() {
		currentWall.updateLength();
		if (currentWall.length < BBTHSimulation.MIN_WALL_LENGTH) {
			return null;
		}

		walls.add(new WallUnit(currentWall, unitManager, team, paint, BBTHGame.PARTICLES));

		Wall toReturn = currentWall;
		currentWall = null;
		return toReturn;
	}

	public void setUnitType(UnitType type) {
		selector.setUnitType(type);
	}

	public void setupSubviews(UIScrollView view, boolean isLocal) {
		if (isLocal) {
			view.scrollTo(base.getPosition().x, base.getPosition().y);
		}
	}

	public void spawnUnit(float x, float y) {
		// Can't spawn in front of most advanced unit.
		Unit advUnit = getMostAdvancedUnit();
		if (advUnit != null) {
			if (team == Team.CLIENT) {
				y = Math.max(y, advUnit.getY());
			} else {
				y = Math.min(y, advUnit.getY());
			}
		} else {
			if (team == Team.CLIENT) {
				y = BBTHSimulation.GAME_HEIGHT - Base.BASE_HEIGHT * 2.0f;
			} else {
				y = Base.BASE_HEIGHT * 2.0f;
			}
		}
		
		for (int i = 0; i < 10; ++i) {
			float angle = MathUtils.randInRange(0, 2 * MathUtils.PI);
			float xVel = MathUtils.randInRange(25.f, 50.f) * FloatMath.cos(angle);
			float yVel = MathUtils.randInRange(25.f, 50.f) * FloatMath.sin(angle);
			BBTHGame.PARTICLES.createParticle().circle().velocity(xVel, yVel).shrink(0.1f, 0.15f).radius(3.0f).position(x, y).color(team.getRandomShade());
		}

		Unit newUnit = null;
		if (_combo != 0 && _combo % BBTHSimulation.UBER_UNIT_THRESHOLD == 0) {
			newUnit = UnitType.UBER.createUnit(unitManager, team, paint, BBTHGame.PARTICLES);
		} else {
			newUnit = selector.getUnitType().createUnit(unitManager, team, paint, BBTHGame.PARTICLES);
		}

		newUnit.setPosition(x, y);
		if (team == Team.SERVER) {
			newUnit.setVelocity(BBTHSimulation.randInRange(30, 70), MathUtils.PI / 2.f);
		} else {
			newUnit.setVelocity(BBTHSimulation.randInRange(30, 70), -MathUtils.PI / 2.f);
		}
		
		aiController.addEntity(newUnit);
		units.add(newUnit);
		totalUnitsCreated++;
		if (unitCreatedEvent != null) {
			unitCreatedEvent.set(newUnit);
			BBTHAchievementManager.INSTANCE.notifyUnitCreated(unitCreatedEvent);
		}
	}

	public Unit getMostAdvancedUnit() {
		Unit toReturn = null;

		for (int i = 0; i < units.size(); i++) {
			Unit currUnit = units.get(i);
			if (toReturn == null
					|| (team == Team.SERVER && currUnit.getY() > toReturn
							.getY())
					|| (team == Team.CLIENT && currUnit.getY() < toReturn
							.getY())) {
				toReturn = currUnit;
			}
		}

		return toReturn;
	}

	public void update(float seconds) {
		for (int i = 0; i < units.size(); i++) {
			Unit unit = units.get(i);

			unit.update(seconds);
			if (unit.getY() < 0 || unit.getY() > BBTHSimulation.GAME_HEIGHT) {
				units.remove(i);
				i--;
				aiController.removeEntity(unit);
			}
		}

		for (int i = walls.size() - 1; i >= 0; --i) {
			walls.get(i).update(seconds);
			if (walls.get(i).isDead()) {
				removeWall(i);
			}
		}

	}

	private void removeWall(int i) {
		unitManager.removeWall(walls.get(i).getWall());
		walls.remove(i);
	}

	public UnitSelector getUnitSelector() {
		return this.selector;
	}

	public void draw(Canvas canvas, boolean serverDraw) {
		// Draw bases
		base.draw(canvas, serverDraw);

		// draw walls
		paint.setColor(team.getWallColor());
		for (int i = 0; i < walls.size(); i++) {
			walls.get(i).drawChassis(canvas);
		}

		// draw units
		paint.setStyle(Style.STROKE);
		paint.setColor(team.getUnitColor());
		for (int i = 0; i < units.size(); i++) {
			units.get(i).drawChassis(canvas);
		}
		for (int i = 0; i < units.size(); i++) {
			units.get(i).drawEffects(canvas);
		}
	}
	
	public void postDraw(Canvas canvas, boolean serverDraw) {
		for (int i = 0; i < units.size(); i++) {
			units.get(i).drawHealthBar(canvas, serverDraw);
		}
	}

	public float getHealth() {
		return _health;
	}

	public void resetHealth() {
		_health = 100;
		base.setHealth(100);
	}

	public void adjustHealth(int delta) {
		_health = (int)MathUtils.clamp(0, 100, _health + delta);
		base.setHealth(_health);
		if (_isLocal) {
			Vibrate.vibrate(0.1f);
		}
	}

	public void setCombo(float _combo) {
		if (_combo < 0) {
			_combo = 0;
		}
		this._combo = _combo;
	}

	public float getCombo() {
		return _combo;
	}

	public boolean isLocal() {
		return _isLocal;
	}
	
	public int getSimulationSyncHash() {
		int hash = 0;
		hash = Hash.mix(hash, _health);
		for (int i = 0; i < units.size(); i++) {
			hash = Hash.mix(hash, units.get(i).getSimulationSyncHash());
		}
		return hash;
	}
	
	private UnitCreatedEvent unitCreatedEvent;
	public void setupEvents(InGameScreen inGameScreen, BBTHSimulation sim) {
		unitCreatedEvent = new UnitCreatedEvent(sim, inGameScreen.singlePlayer, inGameScreen.aiDifficulty);
	}

	public int getTotalUnitsCreated() {
		return totalUnitsCreated;
	}
	
	public List<Unit> getUnits() {
		return units;
	}
}
