package bbth.game.achievements.events;

import bbth.game.BBTHSimulation;
import bbth.game.units.Unit;

public class UnitDeadEvent extends BBTHAchievementEvent {

	public UnitDeadEvent(BBTHSimulation simulation, boolean singleplayer,
			float aiDifficulty) {
		super(simulation, singleplayer, aiDifficulty);
	}

	Unit killedUnit;

	public void set(Unit killedUnit) {
		this.killedUnit = killedUnit;
	}

	public Unit getKilledUnit() {
		return killedUnit;
	}

}
