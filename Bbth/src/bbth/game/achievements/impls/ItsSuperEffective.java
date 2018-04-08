package bbth.game.achievements.impls;

import java.util.ArrayList;

import bbth.engine.achievements.AchievementInfo;
import bbth.game.Team;
import bbth.game.achievements.BBTHAchievement;
import bbth.game.achievements.events.UnitDeadEvent;
import bbth.game.achievements.events.UpdateEvent;
import bbth.game.units.Unit;

public class ItsSuperEffective extends BBTHAchievement {
	ArrayList<Integer> hashCodes = new ArrayList<Integer>();
	ArrayList<Integer> kills = new ArrayList<Integer>();

	public ItsSuperEffective(AchievementInfo achievementInfo) {
		super(achievementInfo);
	}

	@Override
	public void unitDead(UnitDeadEvent e) {
		Unit killedUnit = e.getKilledUnit();
		Team localTeam = e.getLocalPlayer().getTeam();
		if (localTeam.isEnemy(killedUnit.getTeam())) {
			Unit killer = killedUnit.getKiller();
			if (killer != null && killer.getTeam() == localTeam) {
				Integer hashCode = killer.hashCode();
				int index = hashCodes.indexOf(hashCode);
				if (index < 0) {
					index = hashCodes.size();
					hashCodes.add(hashCode);
					kills.add(0);
				}
				int numKills = kills.get(index) + 1;
				if (numKills >= 4) {
					increment();
				} else
					kills.set(index, numKills);
			}
		}
	}

	@Override
	public void update(UpdateEvent e) {
		hashCodes.clear();
		kills.clear();
	}

	@Override
	public boolean usesUnitDead() {
		return true;
	}

	@Override
	public boolean usesUpdate() {
		return true;
	}

}
