package com.threed.jpct.games.alienrunner;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import android.app.Activity;
import android.content.Context;

import com.threed.jpct.Logger;
import com.threed.jpct.SimpleVector;

public class Recorder {

	private float[] positions = new float[30000];
	private int index = 0;
	private SimpleVector tmp = new SimpleVector();
	private boolean stopped = true;

	public Recorder() {
	}

	public void start() {
		stopped = false;
	}

	public void record(Player player) {
		if (stopped) {
			return;
		}
		if (positions.length == index) {
			Logger.log("Enlarging buffer to " + (positions.length + 30000));
			float[] nt = new float[positions.length + 30000];
			System.arraycopy(positions, 0, nt, 0, positions.length);
			positions = nt;
		}

		tmp = player.getTranslation(tmp);

		positions[index++] = tmp.x;
		positions[index++] = tmp.y;
		positions[index++] = tmp.z;
	}

	public void stop() {
		stopped = true;
	}

	public void rewind() {
		index = 0;
		stopped = true;
	}

	public float[] ejectTape() {
		float[] ret = new float[index];
		System.arraycopy(positions, 0, ret, 0, index);
		return ret;
	}

	public int getIndex() {
		return index;
	}

	public void writeData(final Context context, Level level, Player player) {
		final float[] res = ejectTape();
		final int length = res.length;
		final int time = player.getTime();
		final int score = ScoreManager.getInstance().getScore();
		final int number = level.getNumber();

		new Thread() {
			public void run() {

				FileOutputStream os = null;
				GZIPOutputStream zs = null;
				byte[] buffer = new byte[4];
				try {
					String name = "recording_" + GameConfig.versionIndex + "_" + number + ".pos";
					Logger.log("Writing recorded data to file: " + name);
					os = context.openFileOutput(name, Activity.MODE_PRIVATE);
					zs = new GZIPOutputStream(os);

					// Write length...
					int ii = length;
					buffer[0] = (byte) (ii >> 24);
					buffer[1] = (byte) ((ii >> 16) & 0xFF);
					buffer[2] = (byte) ((ii >> 8) & 0xFF);
					buffer[3] = (byte) (ii & 0xFF);
					zs.write(buffer);

					// Write time...
					ii = time;
					buffer[0] = (byte) (ii >> 24);
					buffer[1] = (byte) ((ii >> 16) & 0xFF);
					buffer[2] = (byte) ((ii >> 8) & 0xFF);
					buffer[3] = (byte) (ii & 0xFF);
					zs.write(buffer);

					// Write score...
					ii = score;
					buffer[0] = (byte) (ii >> 24);
					buffer[1] = (byte) ((ii >> 16) & 0xFF);
					buffer[2] = (byte) ((ii >> 8) & 0xFF);
					buffer[3] = (byte) (ii & 0xFF);
					zs.write(buffer);

					// Write data
					for (int i = 0; i < res.length; i++) {
						ii = Float.floatToIntBits(res[i]);
						buffer[0] = (byte) (ii >> 24);
						buffer[1] = (byte) ((ii >> 16) & 0xFF);
						buffer[2] = (byte) ((ii >> 8) & 0xFF);
						buffer[3] = (byte) (ii & 0xFF);
						zs.write(buffer);
					}
				} catch (Exception e) {
					Logger.log("Unable to save recording: " + e.getMessage());
				} finally {
					try {
						zs.close();
						os.close();
					} catch (IOException e) {
						Logger.log(e);
					}
				}
			}
		}.start();
	}
}
