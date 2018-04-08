package org.iSun.td.model;

import org.iSun.td.constant.SystemConstant;
import org.loon.framework.android.game.action.map.Field2D;
import org.loon.framework.android.game.core.graphics.window.actor.Actor;
import org.loon.framework.android.game.core.graphics.window.actor.ActorLayer;

/**
 * 原始的炮弹
 * 
 * @author iSun
 * 
 */
public class BaseMissle extends Actor {
	protected int dir;
	protected int damage;
	protected double x, y;
	protected boolean removeFlag;
	protected Field2D field;
	protected int speed = SystemConstant.BulletSpeed.BASE_BULLET_SPEED;

	public BaseMissle(String fileName, int dir, int damage, Field2D field) {
		this.dir = dir;
		this.damage = damage;
		this.setImage(fileName);
		this.field = field;
		// 设置动作出发延时时间
		this.setDelay(50);
	}

	public BaseMissle(String fileName, int dir, Field2D field) {
		this.dir = dir;
		this.setImage(fileName);
		this.field = field;
	}

	/**
	 * 首次添加到图层是调用
	 */
	@Override
	protected void addLayer(ActorLayer gameLayer) {
		// TODO Auto-generated method stub
		this.x = this.getX();
		this.y = this.getY();
	}

	/**
	 * 子弹沿着炮筒的方向运动
	 */
	public void move() {
		// 多次改变子弹的坐标
		for (int i = 0; i < speed; i++) {
			// 纠正弹道的位置
			double angle = Math.toRadians((double) this.dir);
			this.x += Math.cos(angle);
			this.y += Math.sin(angle);
		}

		this.setLocation((int) this.x
				+ (field.getTileWidth() - this.getWidth()) / 2, (int) this.y
				+ (field.getTileHeight() - this.getHeight()) / 2);

	}

	/**
	 * 动作处理
	 */
	@Override
	public void action(long elapsedTime) {
		// TODO Auto-generated method stub
		if (removeFlag) {
			return;
		}
		move();
		Enemy enemy = this.getCollisionEnemy();
		// 当与敌人碰撞时
		if (enemy != null) {
			this.removeFromLayer();
			enemy.beAttacted(this.damage);
			return;
		} else if (isOutOfScreen()) {
			this.removeFromLayer();
		}
	}

	/**
	 * 子弹是否飞出屏幕
	 * 
	 * @return
	 */
	public boolean isOutOfScreen() {
		return this.getX() <= 0 || this.getX() >= this.getLayer().getWidth()
				|| this.getY() <= 0
				|| this.getY() >= this.getLayer().getHeight();
	}

	/**
	 * 获取被击中的敌人
	 * 
	 * @return
	 */
	public Enemy getCollisionEnemy() {
		Enemy enemy = null;
		enemy = (Enemy) this.getOnlyCollisionObject(Enemy.class);
		return enemy;
	}

	public void removeFromLayer() {
		this.removeFlag = true;
		getLayer().removeObject(this);
	}

}
