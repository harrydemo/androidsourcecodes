package com.threed.jpct.games.alienrunner;

public class Randomizer {

	private static int index = 0;

	private static float[] rnds = new float[500];

	static {
		for (int i = 0; i < rnds.length; i++) {
			rnds[i] = (float) Math.random();
		}
		index = (int) (Math.random() * rnds.length);
	}

	public static float random() {
		try {
			float res = rnds[index];
			index++;
			if (index >= rnds.length) {
				index = 0;
			}
			return res;

		} catch (Exception e) {
			return 0.5f;
		}
	}

}
