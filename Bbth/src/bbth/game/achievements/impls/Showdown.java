package bbth.game.achievements.impls;

import bbth.engine.achievements.AchievementInfo;
import bbth.game.Team;
import bbth.game.achievements.BBTHAchievement;
import bbth.game.achievements.events.UnitDeadEvent;
import bbth.game.units.Unit;
import bbth.game.units.UnitType;

public class Showdown extends BBTHAchievement {

	public Showdown(AchievementInfo achievementInfo) {
		super(achievementInfo);
	}

	@Override
	public void unitDead(UnitDeadEvent e) {
		Unit killedUnit = e.getKilledUnit();
		Unit killer = killedUnit.getKiller();
		Team localTeam = e.getLocalPlayer().getTeam();
		if (killer != null && killedUnit.getTeam() != localTeam
				&& killer.getTeam() == localTeam
				&& killedUnit.getType() == UnitType.UBER
				&& killer.getType() == UnitType.UBER) {
			increment();
		}
	}

	@Override
	public boolean usesUnitDead() {
		return true;
	}

}
