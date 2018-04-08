package bbth.game.achievements.events;

import bbth.game.BBTHSimulation;
import bbth.game.BeatTrack;
import bbth.game.Player;

public class GameEndedEvent extends BBTHAchievementEvent {

	BeatTrack beatTrack;

	public GameEndedEvent(BBTHSimulation simulation, boolean singleplayer,
			float aiDifficulty) {
		super(simulation, singleplayer, aiDifficulty);
	}

	boolean tie;
	Player winningPlayer;

	public Player getWinningPlayer() {
		return winningPlayer;
	}

	public boolean isTie() {
		return tie;
	}

	public void set(Player winningPlayer, boolean tie, BeatTrack beatTrack) {
		this.winningPlayer = winningPlayer;
		this.tie = tie;
		this.beatTrack = beatTrack;
	}

	public BeatTrack getBeatTrack() {
		return beatTrack;
	}

}
