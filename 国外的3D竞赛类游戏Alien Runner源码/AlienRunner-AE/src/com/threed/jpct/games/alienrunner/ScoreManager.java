package com.threed.jpct.games.alienrunner;

public class ScoreManager {
	private static ScoreManager instance = null;

	private int score = 0;

	public static synchronized ScoreManager getInstance() {
		if (instance == null) {
			instance = new ScoreManager();
		}
		return instance;
	}

	public void reset() {
		score = 0;
	}

	public void add(int add) {
		if (add < 0) {
			return;
		}
		score += add;
	}

	public void sub(int sub) {
		if (sub < 0) {
			return;
		}
		score -= sub;
		if (score < 0) {
			score = 0;
		}
	}

	public int getScore() {
		return score;
	}

}
