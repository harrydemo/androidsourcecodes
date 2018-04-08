package com.threed.jpct.games.alienrunner;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.threed.jpct.Loader;
import com.threed.jpct.Logger;
import com.threed.jpct.RGBColor;

public class Level {

	private String[] levelData = null;
	private int tileSize = 10;
	private String parText = null;
	private long par = 0;
	private int number = 0;
	private int sky = 0;
	private RGBColor fog = null;
	private RGBColor ambient = null;
	private int texture=1;

	public Level(InputStream level) {
		String levelTxt = Loader.loadTextFile(level);
		String[] lines = levelTxt.split("\n");
		List<String> lst = new ArrayList<String>(Arrays.asList(lines));

		// read number
		String ln = lst.get(0);
		lst.remove(0);
		number = Integer.valueOf(ln.trim());

		// read backdrop
		ln = lst.get(0);
		lst.remove(0);
		sky = Integer.valueOf(ln.trim());

		// read fog color
		ln = lst.get(0);
		lst.remove(0);
		String[] parts = ln.trim().split(",");
		setFog(new RGBColor(Integer.valueOf(parts[0]), Integer.valueOf(parts[1]), Integer.valueOf(parts[2])));

		// read ambient color
		ln = lst.get(0);
		lst.remove(0);
		parts = ln.trim().split(",");
		setAmbient(new RGBColor(Integer.valueOf(parts[0]), Integer.valueOf(parts[1]), Integer.valueOf(parts[2])));

		ln = lst.get(0);
		lst.remove(0);
		setTexture(Integer.valueOf(ln.trim()));
		
		Collections.reverse(lst);
		levelData = new String[lst.size()];
		levelData = lst.toArray(levelData);

		for (int i = 0; i < levelData.length; i++) {
			if (levelData[i].trim().length() == 0) {
				// Only spaces?? Space that memory and processing power.
				levelData[i] = null;
			}
		}

		par = (long) ((((float) (levelData.length - 2)) * 12.38f) / 100 + 0.5f);
		parText = "Par time: " + getFormattedPar() + "s.";

		Logger.log("Level " + number + " loaded with " + levelData.length + " tile lines!");
	}

	public int getTileSize() {
		return tileSize;
	}

	public int getLineCount() {
		return levelData.length;
	}

	public String getLine(int pos) {
		return levelData[pos];
	}

	public void reset() {

	}

	public long getPar() {
		return par;
	}

	public long getLongPar() {
		return par * 1000;
	}

	public String getFormattedPar() {
		return String.valueOf(getPar());
	}

	public String getParText() {
		return parText;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getNumber() {
		return number;
	}

	public void setSky(int sky) {
		this.sky = sky;
	}

	public int getSky() {
		return sky;
	}

	public void setFog(RGBColor fog) {
		this.fog = fog;
	}

	public RGBColor getFog() {
		return fog;
	}

	public void setAmbient(RGBColor ambient) {
		this.ambient = ambient;
	}

	public RGBColor getAmbient() {
		return ambient;
	}

	public void setTexture(int texture) {
		this.texture = texture;
	}

	public int getTexture() {
		return texture;
	}

}
