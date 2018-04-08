package com.crackedcarrot.fileloader;

import com.crackedcarrot.Scaler;
import com.crackedcarrot.Sprite;
import com.crackedcarrot.Tower;
import com.crackedcarrot.Waypoints;

/**
 * A class for an Map.
 */
public class Map {
	private Waypoints points;
	private Sprite[] bkg;
	private Tower[][] twg;
	private Scaler mapScaler;
	private int textureFile;

	// public int gridSizeX;
	// public int gridSizeY;

	/**
	 * Constructor
	 * 
	 * @param Waypoints
	 *            waypoints that will be used by the Map.
	 * @param Sprite
	 *            [] the background sprite that the Map uses.
	 */
	public Map(Waypoints p, Sprite[] bkg, Tower[][] twg, Scaler mapScaler,
			int textureFile) {
		points = p;
		this.bkg = bkg;
		this.twg = twg;
		this.mapScaler = mapScaler;
		this.textureFile = textureFile;
	}

	/**
	 * return all waypoints of this map
	 * 
	 * @return Waypoints
	 */
	public Waypoints getWaypoints() {
		return points;
	}

	/**
	 * return the background of this map
	 * 
	 * @return Sprite[]
	 */
	public Sprite[] getBackground() {
		return bkg;
	}

	/**
	 * return towergrid for this map
	 * 
	 * @return Sprite[][]
	 */
	public Tower[][] get2DGrid() {
		return twg;
	}

	/**
	 * return towergrid for this map as linear Array.
	 * 
	 * @return Sprite[]
	 */
	public Tower[] getLinearGrid() {
		int size = 0;
		for (int i = 0; i < twg.length; i++) {
			for (int j = 0; j < twg[i].length; j++) {
				if (twg[i][j] != null)
					size++;
			}
		}

		Tower[] result = new Tower[size];

		int index = 0;
		for (int i = 0; i < twg.length; i++) {
			for (int j = 0; j < twg[i].length; j++) {
				if (twg[i][j] != null) {
					result[index] = twg[i][j];
					index++;
				}
			}
		}

		return result;
	}

	/**
	 * return scaler.
	 * 
	 * @return Scaler
	 */
	public Scaler getScaler() {
		return mapScaler;
	}

	/**
	 * return texturefile resource value.
	 * 
	 * @return int
	 */
	public int getTextureFile() {
		return textureFile;
	}
}