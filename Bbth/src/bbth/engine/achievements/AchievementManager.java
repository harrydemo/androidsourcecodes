package bbth.engine.achievements;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import bbth.engine.util.Bag;

public abstract class AchievementManager<T extends Achievement> {
	protected Map<Integer, AchievementInfo> infoMap = new HashMap<Integer, AchievementInfo>();
	protected Bag<T> achievements;

	public AchievementManager(int achievementsResourceID) {
		AchievementInfo[] achievementInfos = AchievementInfoParser
				.parseAchievementInfos(achievementsResourceID);
		for (AchievementInfo achievementInfo : achievementInfos) {
			infoMap.put(Integer.valueOf(achievementInfo.id), achievementInfo);
		}
		achievements = new Bag<T>();
	}

	protected abstract void registerAchievements();

	public Collection<T> getAchievements() {
		return Collections.unmodifiableCollection(achievements);
	}

}
