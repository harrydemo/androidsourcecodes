package com.threed.jpct.games.alienrunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import android.content.Context;

import com.threed.jpct.Logger;
import com.threed.jpct.SimpleVector;

public class GhostPlayer {

	private float[] positions = null;
	private int index = 0;
	private int time = 0;
	private int score = 0;

	private byte[] buffer = new byte[256];
	private int bufferIndex = 0;
	private int length = 0;
	private boolean loaded = false;
	private String scoreText = null;

	public GhostPlayer() {
	}

	public void load(Context context, Level level) {
		FileInputStream os = null;
		GZIPInputStream zs = null;
		try {
			loaded = false;
			index = 0;
			time = 0;
			score = 0;
			String name = "recording_" + GameConfig.versionIndex + "_" + level.getNumber() + ".pos";
			Logger.log("Reading recorded data from file: " + name);
			os = context.openFileInput(name);
			zs = new GZIPInputStream(os);

			int len = read(zs);
			time = read(zs);
			score = read(zs);

			Logger.log("Time/Score: " + time + "/" + score);

			setScoreText("High score: " + score);

			positions = new float[len];

			for (int i = 0; i < len; i++) {
				positions[i] = Float.intBitsToFloat(read(zs));
			}
			loaded = true;
		} catch (FileNotFoundException fnf) {
			Logger.log("No recording for this level found!");
			positions = new float[0];
		} catch (Throwable e) {
			Logger.log("No recording for this level loaded: " + e.getMessage());
			positions = new float[0];
		} finally {
			try {
				if (zs != null) {
					zs.close();
				}
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				Logger.log(e);
			}
		}
	}

	public void insertTape(float[] data) {
		positions = data;
		rewind();
	}

	public void rewind() {
		index = 0;
	}

	public SimpleVector play(SimpleVector pos) {
		if (index < positions.length) {
			pos.x = positions[index];
			pos.y = positions[index + 1];
			pos.z = positions[index + 2];
			index += 3;
		}
		return pos;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getTime() {
		return time;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getScore() {
		return score;
	}

	private void fillBuffer(InputStream is, int start) throws Exception {
		bufferIndex = 0;
		if (is.available() == 0) {
			throw new Exception("Premature end of file!");
		}
		length += is.read(buffer, start, buffer.length - start);
	}

	private int read(InputStream is) throws Exception {
		if (bufferIndex >= length || length == 0) {
			int start = 0;
			length = 0;
			while (length % 4 != 0 || start == 0) {
				fillBuffer(is, start);
				start = length;
			}
		}
		int ret = (int) (((buffer[bufferIndex] & 0xFF) << 24) + ((buffer[bufferIndex + 1] & 0xFF) << 16) + ((buffer[bufferIndex + 2] & 0xFF) << 8) + (buffer[bufferIndex + 3] & 0xFF));
		bufferIndex += 4;
		return ret;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void setScoreText(String scoreText) {
		this.scoreText = scoreText;
	}

	public String getScoreText() {
		return scoreText;
	}
}
