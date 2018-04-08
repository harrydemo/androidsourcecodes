package com.crackedcarrot;

public class Tracker {
	private TrackerData[][] data;

	public Tracker() {
		data = new TrackerData[10][13];
		for (int x = 0; x < data.length; x++) {
			for (int y = 0; y < data[0].length; y++) {
				data[x][y] = new TrackerData();
			}
		}
	}

	public TrackerData getTrackerData(int x, int y) {
		return data[x][y];
	}
}