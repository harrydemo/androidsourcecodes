package bbth.game.achievements.events;

import bbth.engine.util.Bag;
import bbth.game.BBTHSimulation;
import bbth.game.Player;
import bbth.game.Song;
import bbth.game.units.Unit;

public class BBTHAchievementEvent {
	private BBTHSimulation simulation;
	private boolean singleplayer;
	private float aiDifficulty;

	public BBTHAchievementEvent(BBTHSimulation simulation,
			boolean singleplayer, float aiDifficulty) {
		this.simulation = simulation;
		this.singleplayer = singleplayer;
		this.aiDifficulty = aiDifficulty;
	}

	public Song getSong() {
		return simulation.song;
	}

	public Bag<Unit> getUnitsInCircle(float x, float y, float r) {
		return simulation.getUnitsInCircle(x, y, r);
	}

	public Player getLocalPlayer() {
		return simulation.localPlayer;
	}

	public Player getRemotePlayer() {
		return simulation.remotePlayer;
	}

	public boolean isSingleplayer() {
		return singleplayer;
	}

	public float getAiDifficulty() {
		return aiDifficulty;
	}

}
