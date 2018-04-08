package bbth.game.achievements.impls;

import bbth.engine.achievements.AchievementInfo;
import bbth.game.Team;
import bbth.game.achievements.BBTHAchievement;
import bbth.game.achievements.events.UnitDeadEvent;
import bbth.game.units.Unit;

public class IncrementOnUnitKillAchievement extends BBTHAchievement {

	public IncrementOnUnitKillAchievement(AchievementInfo achievementInfo) {
		super(achievementInfo);
	}

	@Override
	public void unitDead(UnitDeadEvent e) {
		Unit killedUnit = e.getKilledUnit();
		Unit killer = killedUnit.getKiller();
		Team localTeam = e.getLocalPlayer().getTeam();
		if (killer != null && killedUnit.getTeam() != localTeam
				&& killer.getTeam() == localTeam) {
			increment();
		}
	}

	@Override
	public boolean usesUnitDead() {
		return true;
	}

}
