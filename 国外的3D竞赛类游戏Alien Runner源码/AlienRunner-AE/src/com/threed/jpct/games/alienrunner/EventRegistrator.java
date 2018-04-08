package com.threed.jpct.games.alienrunner;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.RGBColor;
import com.threed.jpct.Texture;
import com.threed.jpct.games.alienrunner.glfont.GLFont;

public class EventRegistrator {

	public final static int CONCUSSION = 0; // 3 Walls crashed in 20 sec.
	public final static int AQUARIUS = 1; // 5 water pools crossed in 10 sec
	public final static int UNSELFISH = 2; // collected not a single diamond in
	// a level
	public final static int RUBBER_BALL = 3; // 10 jumps in 10 sec
	public final static int VORACITY = 4; // collected 10 diamonds in 7 sec
	public final static int RELAXED = 5; // Haven't touched the controls for 1
	public final static int HEAD_IN_THE_SKY = 6; // Two high jumps in 7 secs.

	private final static int COUNT = 0;
	private final static int FIRST_TIME = 1;
	private final static int LAST_TIME = 2;
	private final static int DONE = 3;

	private int showing = -1;
	private long startShowing = 0;

	private final static String[] NAMES = new String[] { "Concussion!", "Aquarius!", "Unselfish!", "Jumping ball!", "Greed!", "Relaxed!", "Head in the clouds!" };

	private final static int[] SCORES = new int[] { 7500, 3000, 20000, 4000, 3500, 2500, 1000 };

	private long[][] events = new long[NAMES.length][4];

	private static EventRegistrator instance = null;
	private GLFont font = null;

	private EventRegistrator() {
		clear();
	}

	public static synchronized EventRegistrator getInstance() {
		if (instance == null) {
			instance = new EventRegistrator();
		}
		return instance;
	}

	public void reset() {
		clear();
	}

	public void clear() {
		for (int i = 0; i < events.length; i++) {
			events[i][0] = 0;
			events[i][1] = 0;
			events[i][2] = 0;
			events[i][3] = 0;
		}
		showing = -1;
		startShowing = 0;
	}

	public void register(int event) {
		long[] events = this.events[event];
		if (events[DONE] == 0) {

			long now = Ticker.getTime();

			switch (event) {
			case CONCUSSION:
				if (now - events[FIRST_TIME] <= 20000) {
					events[COUNT]++;
					if (events[COUNT] >= 3) {
						events[DONE] = 1;
						ScoreManager.getInstance().add(SCORES[CONCUSSION]);
					} else {
						events[LAST_TIME] = now;
					}
				} else {
					events[COUNT] = 1;
					events[LAST_TIME] = now;
					events[FIRST_TIME] = now;
				}
				break;
			case AQUARIUS:
				if (now - events[FIRST_TIME] <= 10000) {
					events[COUNT]++;
					if (events[COUNT] >= 5) {
						events[DONE] = 1;
						ScoreManager.getInstance().add(SCORES[AQUARIUS]);
					} else {
						events[LAST_TIME] = now;
					}
				} else {
					events[COUNT] = 1;
					events[LAST_TIME] = now;
					events[FIRST_TIME] = now;
				}
				break;
			case RUBBER_BALL:
				if (now - events[FIRST_TIME] <= 9000) {
					events[COUNT]++;
					if (events[COUNT] >= 9) {
						events[DONE] = 1;
						ScoreManager.getInstance().add(SCORES[RUBBER_BALL]);
					} else {
						events[LAST_TIME] = now;
					}
				} else {
					events[COUNT] = 1;
					events[LAST_TIME] = now;
					events[FIRST_TIME] = now;
				}
				break;
			case VORACITY:
				if (now - events[FIRST_TIME] <= 7000) {
					events[COUNT]++;
					if (events[COUNT] >= 14) {
						events[DONE] = 1;
						ScoreManager.getInstance().add(SCORES[VORACITY]);
					} else {
						events[LAST_TIME] = now;
					}
				} else {
					events[COUNT] = 1;
					events[LAST_TIME] = now;
					events[FIRST_TIME] = now;
				}
				break;
			case RELAXED:
				events[DONE] = 1;
				ScoreManager.getInstance().add(SCORES[RELAXED]);
				break;
			case UNSELFISH:
				events[DONE] = 1;
				ScoreManager.getInstance().add(SCORES[UNSELFISH]);
				break;
			case HEAD_IN_THE_SKY:
				events[DONE] = 1;
				ScoreManager.getInstance().add(SCORES[HEAD_IN_THE_SKY]);
				break;
			}

			events[2] = now;
		}
	}

	public String getName(int event) {
		return NAMES[event];
	}

	public boolean isShowing() {
		return showing != -1;
	}

	public void process(FrameBuffer buffer, Texture medalTexture) {
		if (font == null) {
			font = FontProvider.getFont(16, buffer, true);
		}

		if (showing != -1) {
			if (Ticker.getTime() - startShowing <= 3000) {

				float mul = buffer.getHeight() / GameConfig.defaultHeight;

				buffer.blit(medalTexture, 0, 0, (int) (10 * mul), (int) (20 * mul), 32, 64, true);
				font.blitString(buffer, getName(showing), (int) (50 * mul), (int) (40 * mul), 50, RGBColor.WHITE);
			} else {
				events[showing][DONE] = 2;
				showing = -1;
			}
		}

		if (showing == -1) {
			int end = events.length;
			for (int i = 0; i < end; i++) {
				if (events[i][DONE] == 1) {
					showing = i;
					startShowing = Ticker.getTime();
					SoundManager.getInstance().play(SoundManager.PERK);
					break;
				}
			}
		}
	}

}
