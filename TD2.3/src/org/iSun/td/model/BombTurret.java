package org.iSun.td.model;

import java.util.List;

import org.iSun.td.TurretDefense;
import org.iSun.td.constant.SystemConstant;
import org.iSun.td.constant.TurrentType;
import org.loon.framework.android.game.action.map.Field2D;

public class BombTurret extends Turret {

	public BombTurret(String fileName, TurretDefense td) {
		super(fileName, td);
		this.typeID = TurrentType.BOMB_TURRET;
		this.setInterval(150);
		this.ranges = new int[] { 60, 90, 120, 180 };
		this.values = new int[] { 30, 60, 100, 180 };
	}

	@Override
	public void action(long elapsedTime) {
		// TODO Auto-generated method stub
		// 遍历指定半径类所有的Enemy
		List<?> es = this.getAttactAbleEnemys();
		Enemy target = null;
		// 当存在敌人时
		if (!es.isEmpty()) {
			target = (Enemy) es.get(0);
			this.rotateToTarget(target);
		}

		// 设置炮击的时间间隔
		if (this.delay > 0) {
			--this.delay;
		} else if (target != null) {
			fireTo(target);
		}
	}

	/**
	 * 向目标开火
	 * 
	 * @param target
	 */
	public void fireTo(Enemy target) {
		// 构造炮弹
		Bomb bullet = new Bomb("assets/rocket.png", this.getRotation(), 4,
				field);
		bullet.target = target;
		// 计算炮击点
		int x = (int) Math.round(Math.cos(Math.toRadians(this.getRotation()))
				* (double) bullet.getWidth() * 2)
				+ this.getX();

		int y = (int) Math.round(Math.sin(Math.toRadians(this.getRotation()))
				* (double) bullet.getHeight() * 2)
				+ this.getY();
		// 将炮弹添加到Layer
		this.getLayer().addObject(bullet, x, y);
		// td.playtAssetsMusic("audio/minigun_shot.mp3", false);
		// 复位延时计数变量
		this.delay = 5;

	}

	/**
	 * 导弹
	 * 
	 * @author iSun
	 * 
	 */
	class Bomb extends BaseMissle {
		// 被锁定的目标
		private Enemy target;

		public Bomb(String fileName, int dir, int damage, Field2D field) {
			super(fileName, dir, damage, field);
			this.speed = SystemConstant.BulletSpeed.BOMB_BULLET_SPEED;
			this.setRotation(dir);
		}

		/**
		 * 导弹运动运动
		 */
		public void move() {
			// 多次改变子弹的坐标
			for (int i = 0; i < speed; i++) {
				// 纠正弹道的位置
				double angle = Math.toRadians((double) this.dir);
				this.x += 1.5 * Math.cos(angle);
				this.y += Math.sin(angle);
			}
			if (!this.target.removeFlag) {
				traceTarget();
			}

			this.setLocation(
					(int) this.x + (field.getTileWidth() - this.getWidth()) / 2,
					(int) this.y + (field.getTileHeight() - this.getHeight())
							/ 2);

		}

		/**
		 * 追踪目标
		 */
		public void traceTarget() {
			this.dir = (int) Math.toDegrees(Math.atan2(
					(target.getY() - this.getY()),
					(target.getX()) - this.getX()));
			if (!target.removeFlag) {
				setRotation(this.dir);
			}
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
			if (enemy != null && enemy.equals(target)) {
				this.removeFromLayer();
				enemy.beAttacted(this.damage);
				return;
			} else if (isOutOfScreen()) {
				this.removeFromLayer();
			}
		}
	}
}
