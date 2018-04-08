package org.iSun.td.model;

import java.util.List;

import org.iSun.td.TurretDefense;
import org.iSun.td.constant.TurrentType;
import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.device.LGraphics;

import android.graphics.Color;

/**
 * 声波炮台
 * 
 * @author Administrator
 * 
 */
public class SoundWaveTurret extends Turret {
	// 炮击间隔
	public int delay = 5;
	boolean isScale = false;
	private int radios;
	private float alpha = 0f;
	private int[] damages = new int[] { 1, 1, 1, 1 };

	public SoundWaveTurret(String fileName, TurretDefense td) {
		super(fileName, td);
		this.typeID = TurrentType.SOUNDWAVE_TURRET;
		this.radios = this.ranges[this.currentLevel] / 2;
		this.ranges = new int[] { 60, 70, 80, 100 };
		this.values = new int[] { 50, 100, 180, 250 };
		this.setInterval(50);
	}

	@Override
	public void draw(LGraphics g) {
		// TODO Auto-generated method stub
		if (selected) {
			drawAttackRange(g);
		}
		if (isScale) {
			scaleAttack(g);
		}
	}

	/**
	 * 发出动动波
	 * 
	 * @param g
	 */
	public void scaleAttack(LGraphics g) {
		LColor color = g.getColor();
		float alphaOld = g.getAlpha();
		g.setAntiAlias(true);
		g.setColor(Color.GRAY);
		if (alpha > 0.5f) {
			alpha = 0;
		}
		g.setAlpha(alpha);
		g.fillOval(-(radios * 2 - field.getTileWidth()) / 2,
				-(radios * 2 - field.getTileHeight()) / 2, radios * 2,
				radios * 2);
		g.setAlpha(alphaOld);
		g.setColor(color);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void action(long elapsedTime) {
		// TODO Auto-generated method stub
		List<?> es = this.getAttactAbleEnemys();
		// 当存在敌人时
		if (!es.isEmpty()) {
			isScale = true;
			for (Enemy e : (List<Enemy>) es) {
				if (!e.removeFlag) {
					e.beAttacted(this.damages[this.currentLevel]);
				}
			}
		}

		if (isScale && radios < this.ranges[this.currentLevel]) {
			radios = radios + 2;
			alpha = (float) radios / 200f;
			System.out.println();
			isScale = true;
		}
		if (radios >= this.ranges[this.currentLevel]) {
			this.radios = this.ranges[this.currentLevel] / 2;
			isScale = false;
		}

	}
}
