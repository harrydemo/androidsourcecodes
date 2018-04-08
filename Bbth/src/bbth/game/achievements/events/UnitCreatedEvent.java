package bbth.game.achievements.events;

import bbth.game.BBTHSimulation;
import bbth.game.units.Unit;
import bbth.game.units.UnitType;

public class UnitCreatedEvent extends BBTHAchievementEvent {

	private Unit m_unit;

	public UnitCreatedEvent(BBTHSimulation simulation, boolean singleplayer,
			float aiDifficulty) {
		super(simulation, singleplayer, aiDifficulty);
	}

	public void set(Unit u) {
		m_unit = u;
	}

	public UnitType getUnitType() {
		return m_unit.getType();
	}

	public Unit getUnit() {
		return m_unit;
	}

}
