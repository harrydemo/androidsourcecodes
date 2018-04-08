package org.iSun.td.model;

import java.util.List;

import org.iSun.td.TurretDefense;
import org.loon.framework.android.game.action.ActionListener;
import org.loon.framework.android.game.action.FadeTo;
import org.loon.framework.android.game.action.map.Field2D;
import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.device.LGraphics;
import org.loon.framework.android.game.core.graphics.window.actor.Actor;
import org.loon.framework.android.game.core.graphics.window.actor.ActorLayer;

import android.graphics.Color;

/**
 * 最原始的炮台，所有炮台的父类
 * 
 * @author iSun
 * 
 */
public class Turret extends Actor {
	// 炮击间隔
	public int delay = 5;
	// 设置炮台的收索敌人的间隔时间
	public int interval;
	public int currentLevel;
	public static int maxLevel = 4;
	public int values[];
	public int ranges[] = new int[] { 60, 70, 80, 100 };
	public int typeID;

	public boolean updateAble = true;
	public boolean selected;

	public Field2D field;
	public TurretDefense td;
	public String[] turrets;

	public Turret(String fileName, TurretDefense td) {
		setImage(fileName);
		setAlpha(0);
		this.td = td;
		this.field = this.td.field;
		this.turrets = this.td.turrets;
	}

	public void setInterval(int interval) {
		this.interval = interval;
		super.setDelay(this.interval);
	}

	@Override
	protected void addLayer(ActorLayer gameLayer) {
		// TODO Auto-generated method stub
		FadeTo fade = fadeOut();
		fade.setSpeed(10);
		fade.setActionListener(new ActionListener() {
			// 当渐进完毕
			@Override
			public void stop(Actor o) {
				// TODO Auto-generated method stub
				// 旋转90度
				rotateTo(90);
			}

			@Override
			public void start(Actor o) {
				// TODO Auto-generated method stub

			}

			@Override
			public void process(Actor o) {
				// TODO Auto-generated method stub

			}
		});

	}

	/**
	 * 画出攻击范围
	 * 
	 * @param g
	 */
	public void drawAttackRange(LGraphics g) {
		LColor color = g.getColor();
		float alpha = g.getAlpha();
		g.setAntiAlias(true);
		g.setColor(Color.WHITE);

		int radios = ranges[currentLevel] * 2 - 1;
		g.drawOval(-(ranges[currentLevel] * 2 - field.getTileWidth()) / 2,
				-(ranges[currentLevel] * 2 - field.getTileHeight()) / 2,
				radios, radios);

		g.setAlpha(0.15f);
		g.fillOval(-(ranges[currentLevel] * 2 - field.getTileWidth()) / 2,
				-(ranges[currentLevel] * 2 - field.getTileHeight()) / 2,
				radios, radios);
		g.setColor(color);
		g.setAlpha(alpha);
	}

	/**
	 * 返回攻击范围内的敌人
	 * 
	 * @return
	 */
	public List<?> getAttactAbleEnemys() {
		List<?> es = this.getCollisionObjects(this.ranges[currentLevel],
				Enemy.class);
		return es;
	}

	public void fire() {

	}

	public int update() {
		int cost = 0;
		if (updateAble) {
			System.out.println(currentLevel);
			cost = this.values[currentLevel] / 2;
			this.currentLevel++;
		}
		if (this.currentLevel == 4) {
			updateAble = false;
			this.currentLevel--;
		}
		return cost;
	}

	public int sell() {
		int earned;
		earned = this.values[currentLevel] / 2;
		this.destroy();
		return earned;
	}

	private void destroy() {
		this.removeActionEvents();
		this.getLayer().removeObject(this);
	}

	/**
	 * 旋转炮台对准目标
	 * 
	 * @param target
	 */
	public void rotateToTarget(Enemy target) {
		setRotation((int) Math.toDegrees(Math.atan2(
				(target.getY() - this.getY()), (target.getX()) - this.getX())));
	}

	@Override
	public void draw(LGraphics g) {
		// TODO Auto-generated method stub
		if (selected) {
			drawAttackRange(g);
		}
	}
}
