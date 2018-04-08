package bbth.game.achievements;

import bbth.engine.achievements.AchievementInfo;
import bbth.engine.achievements.AchievementManager;
import bbth.engine.achievements.Achievements;
import bbth.engine.util.Bag;
import bbth.game.R;
import bbth.game.Song;
import bbth.game.achievements.events.BaseDestroyedEvent;
import bbth.game.achievements.events.BeatHitEvent;
import bbth.game.achievements.events.BeatMissedEvent;
import bbth.game.achievements.events.GameEndedEvent;
import bbth.game.achievements.events.UnitCreatedEvent;
import bbth.game.achievements.events.UnitDeadEvent;
import bbth.game.achievements.events.UpdateEvent;
import bbth.game.achievements.events.WallCreatedEvent;
import bbth.game.achievements.impls.AnythingYouCanDo;
import bbth.game.achievements.impls.ComboCounterAchievement;
import bbth.game.achievements.impls.DesperateMeasures;
import bbth.game.achievements.impls.DesperateTimes;
import bbth.game.achievements.impls.FlawlessVictory;
import bbth.game.achievements.impls.FullComboAchievement;
import bbth.game.achievements.impls.Humiliation;
import bbth.game.achievements.impls.IncrementOnUberCreateAchievement;
import bbth.game.achievements.impls.IncrementOnUnitCreateAchievement;
import bbth.game.achievements.impls.IncrementOnUnitKillAchievement;
import bbth.game.achievements.impls.ItsSuperEffective;
import bbth.game.achievements.impls.QuantityOverQuality;
import bbth.game.achievements.impls.Showdown;
import bbth.game.achievements.impls.SongAchievement;
import bbth.game.achievements.impls.StrategistAchievement;
import bbth.game.achievements.impls.UltimateStalemate;
import bbth.game.achievements.impls.UnitCountAchievement;
import bbth.game.achievements.impls.YoDawgIHeardYouLikeUnits;

public final class BBTHAchievementManager extends
		AchievementManager<BBTHAchievement> {
	public static final BBTHAchievementManager INSTANCE = new BBTHAchievementManager(
			R.xml.achievements);

	private BBTHAchievementManager(int achievementsResourceID) {
		super(achievementsResourceID);
	}

	public void unregisterAchievementFromEvents(BBTHAchievement achievement) {
		if (achievement.usesBaseDestroyed())
			baseDestroyedAchievements.remove(achievement);
		if (achievement.usesGameEnded())
			gameEndedAchievements.remove(achievement);
		if (achievement.usesUnitDead())
			unitDeadAchievements.remove(achievement);
		if (achievement.usesUnitCreated())
			unitCreatedAchievements.remove(achievement);
		if (achievement.usesBeatHit())
			beatHitAchievements.remove(achievement);
		if (achievement.usesBeatMissed())
			beatMissedAchievements.remove(achievement);
		if (achievement.usesWallCreated())
			wallCreatedAchievements.remove(achievement);
		if (achievement.usesUpdate())
			updateAchievements.remove(achievement);
	}

	void postRegisterAchievements() {
		for (BBTHAchievement achievement : achievements) {
			if (Achievements.INSTANCE.isUnlocked(achievement.achievementInfo))
				continue;

			if (achievement.usesBaseDestroyed())
				baseDestroyedAchievements.add(achievement);
			if (achievement.usesGameEnded())
				gameEndedAchievements.add(achievement);
			if (achievement.usesUnitDead())
				unitDeadAchievements.add(achievement);
			if (achievement.usesUnitCreated())
				unitCreatedAchievements.add(achievement);
			if (achievement.usesBeatHit())
				beatHitAchievements.add(achievement);
			if (achievement.usesBeatMissed())
				beatMissedAchievements.add(achievement);
			if (achievement.usesWallCreated())
				wallCreatedAchievements.add(achievement);
			if (achievement.usesUpdate())
				updateAchievements.add(achievement);
		}
	}

	private Bag<BBTHAchievement> baseDestroyedAchievements = new Bag<BBTHAchievement>();

	public void notifyBaseDestroyed(BaseDestroyedEvent e) {
		for (BBTHAchievement achievement : achievements)
			achievement.baseDestroyed(e);
	}

	private Bag<BBTHAchievement> gameEndedAchievements = new Bag<BBTHAchievement>();

	public void notifyGameEnded(GameEndedEvent e) {
		for (BBTHAchievement achievement : achievements)
			achievement.gameEnded(e);
	}

	private Bag<BBTHAchievement> unitDeadAchievements = new Bag<BBTHAchievement>();

	public void notifyUnitDead(UnitDeadEvent e) {
		for (BBTHAchievement achievement : achievements)
			achievement.unitDead(e);
	}

	private Bag<BBTHAchievement> unitCreatedAchievements = new Bag<BBTHAchievement>();

	public void notifyUnitCreated(UnitCreatedEvent e) {
		for (BBTHAchievement achievement : achievements)
			achievement.unitCreated(e);
	}

	private Bag<BBTHAchievement> beatHitAchievements = new Bag<BBTHAchievement>();

	public void notifyBeatHit(BeatHitEvent e) {
		for (BBTHAchievement achievement : achievements)
			achievement.beatHit(e);
	}

	private Bag<BBTHAchievement> beatMissedAchievements = new Bag<BBTHAchievement>();

	public void notifyBeatMissed(BeatMissedEvent e) {
		for (BBTHAchievement achievement : achievements)
			achievement.beatMissed(e);
	}

	private Bag<BBTHAchievement> wallCreatedAchievements = new Bag<BBTHAchievement>();

	public void notifyWallCreated(WallCreatedEvent e) {
		for (BBTHAchievement achievement : achievements)
			achievement.wallCreated(e);
	}

	private Bag<BBTHAchievement> updateAchievements = new Bag<BBTHAchievement>();

	public void notifyUpdate(UpdateEvent e) {
		for (BBTHAchievement achievement : achievements)
			achievement.update(e);
	}

	public void initialize() {
		registerAchievements();
	}

	@Override
	public void registerAchievements() {
		AchievementInfo info;

		if (!achievements.isEmpty()) {
			return;
		}

		info = infoMap.get(0);
		if (info != null)
			achievements
					.add(new SongAchievement(info, Song.MISTAKE_THE_GETAWAY));

		info = infoMap.get(1);
		if (info != null)
			achievements.add(new SongAchievement(info, Song.MIGHT_AND_MAGIC));

		info = infoMap.get(2);
		if (info != null)
			achievements.add(new SongAchievement(info, Song.RETRO));

		info = infoMap.get(3);
		if (info != null)
			achievements.add(new SongAchievement(info, Song.JAVLA_SLADDAR));

		info = infoMap.get(4);
		if (info != null)
			achievements.add(new SongAchievement(info, Song.ODINS_KRAFT));

		info = infoMap.get(100);
		if (info != null)
			achievements.add(new DesperateTimes(info));

		info = infoMap.get(101);
		if (info != null)
			achievements.add(new DesperateMeasures(info));

		info = infoMap.get(102);
		if (info != null)
			achievements.add(new UltimateStalemate(info));

		info = infoMap.get(103);
		if (info != null)
			achievements.add(new FlawlessVictory(info));

		info = infoMap.get(104);
		if (info != null)
			achievements.add(new StrategistAchievement(info));

		info = infoMap.get(200);
		if (info != null)
			achievements.add(new IncrementOnUberCreateAchievement(info));

		info = infoMap.get(201);
		if (info != null)
			achievements.add(new ComboCounterAchievement(info, 25));

		info = infoMap.get(202);
		if (info != null)
			achievements.add(new ComboCounterAchievement(info, 100));

		info = infoMap.get(203);
		if (info != null)
			achievements.add(new ComboCounterAchievement(info, 300));

		info = infoMap.get(204);
		if (info != null)
			achievements.add(new FullComboAchievement(info));

		info = infoMap.get(300);
		// Prereqs:

		info = infoMap.get(400);
		if (info != null)
			achievements.add(new UnitCountAchievement(info, 25));

		info = infoMap.get(401);
		if (info != null)
			achievements.add(new Humiliation(info));

		info = infoMap.get(402);
		if (info != null)
			achievements.add(new ItsSuperEffective(info));

		info = infoMap.get(403);
		if (info != null)
			achievements.add(new YoDawgIHeardYouLikeUnits(info));

		info = infoMap.get(404);
		if (info != null)
			achievements.add(new Showdown(info));

		info = infoMap.get(405);
		if (info != null)
			achievements.add(new QuantityOverQuality(info));

		info = infoMap.get(406);
		// Prereqs:

		info = infoMap.get(500);
		if (info != null)
			achievements.add(new AnythingYouCanDo(info));

		info = infoMap.get(600);
		if (info != null)
			achievements.add(new IncrementOnUnitKillAchievement(info));

		info = infoMap.get(601);
		if (info != null)
			achievements.add(new IncrementOnUnitKillAchievement(info));

		info = infoMap.get(602);
		if (info != null)
			achievements.add(new IncrementOnUnitKillAchievement(info));

		info = infoMap.get(603);
		if (info != null)
			achievements.add(new IncrementOnUnitCreateAchievement(info));

		info = infoMap.get(604);
		if (info != null)
			achievements.add(new IncrementOnUnitCreateAchievement(info));

		info = infoMap.get(605);
		if (info != null)
			achievements.add(new IncrementOnUnitCreateAchievement(info));

		info = infoMap.get(606);
		if (info != null)
			achievements.add(new IncrementOnUberCreateAchievement(info));

		info = infoMap.get(607);
		if (info != null)
			achievements.add(new IncrementOnUberCreateAchievement(info));

		info = infoMap.get(608);
		if (info != null)
			achievements.add(new IncrementOnUberCreateAchievement(info));

		postRegisterAchievements();
	}

}
