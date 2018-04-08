package org.iSun.td;

import org.iSun.td.model.Turret;
import org.loon.framework.android.game.action.map.Field2D;

/**
 * ÓÎÏ·µÄÖ÷ÆÁ
 * 
 * @author Administrator
 * 
 */
public class TurretDefense extends BaseAdapter {
	private int score;
	private int money = 1500;
	public int wave;
	public int lives = 150;
	public String mapName;
	public Point start;
	public Point end;

	public Turret beSelectedTurret;
	public Field2D field;
	public String[] turrets = new String[] { "assets/bulletTurret.png",
			"assets/bombTurret.png", "assets/poisonTurret.png",
			"assets/soundTurret.png", "assets/bullet.png" };
	public int selectTurret = -1;
	public int[][] waveArray = new int[][] { { 1, 1, 0, 2, 3 },
			{ 1, 1, 2, 3, 3, 1, }, { 1, 0, 3, 2, 2, 0, 2, 3 },
			{ 1, 2, 2, 2, 1, 3, 3, 2, 3, 2, 2, 3 },
			{ 1, 3, 2, 2, 1, 1, 1, 2, 3, 2, 2, 3 },
			{ 1, 2, 3, 0, 2, 1, 1, 1, 2, 3, 2, 2 },
			{ 1, 3, 2, 2, 1, 1, 1, 2, 3, 2, 2, 3 },
			{ 1, 3, 2, 2, 1, 1, 1, 2, 3, 2, 2, 3 },
			{ 1, 3, 2, 2, 1, 1, 1, 2, 3, 2, 2, 3 },
			{ 1, 3, 2, 2, 1, 1, 1, 2, 3, 2, 2, 3 } };

	public TurretDefense(String mapName, Point start, Point end) {
		this.mapName = mapName;
		this.start = start;
		this.end = end;
	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		Menu menu = new Menu(this);
		rightOn(menu);
		menu.setY(0);
		add(menu);

		MapLayer layer = new MapLayer(this, menu);
		centerOn(layer);
		add(layer);
		layer.doStart();

		TopTitle topTitle = new TopTitle(TurretDefense.this);
		topOn(topTitle);
		add(topTitle);
		topTitle.setLocation(0, 0);
		this.playtAssetsMusic("audio/bg_music.mp3", true);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		this.stopAssetsMusic();
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getWave() {
		return wave;
	}

	public void setWave(int wave) {
		this.wave = wave;
	}

}
