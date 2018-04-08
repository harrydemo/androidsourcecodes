package org.iSun.td.model;

import java.util.List;

import org.iSun.td.TurretDefense;
import org.iSun.td.constant.SystemConstant;
import org.iSun.td.constant.TurrentType;
import org.loon.framework.android.game.action.map.Field2D;

/**
 * 毒药炮台，减速功能
 * 
 * @author iSun
 * 
 */
public class PosionTurret extends Turret {
	// 炮击间隔
	public int delay = 5;

	public PosionTurret(String fileName, TurretDefense td) {
		super(fileName, td);
		this.typeID = TurrentType.SOUNDWAVE_TURRET;
		this.setInterval(70);
		this.ranges = new int[] { 60, 80, 120, 150 };
		this.values = new int[] { 25, 50, 100, 150 };
	}

	@Override
	public void action(long elapsedTime) {
		// TODO Auto-generated method stub
		// 遍历指定半径类所有的Enemy
		List<?> es = this.getAttactAbleEnemys();

		// 当存在敌人时
		if (!es.isEmpty()) {
			Enemy target = (Enemy) es.get(0);
			this.rotateToTarget(target);
		}

		// 设置炮击的时间间隔
		if (this.delay > 0) {
			--this.delay;
		} else if (!es.isEmpty()) {
			fire();
		}
	}

	@Override
	public void fire() {
		// 构造炮弹
		PosionBullet bullet = new PosionBullet("assets/poison.png",
				this.getRotation(), field);
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

	class PosionBullet extends BaseMissle {
		private int[] damages;
		private float[] dragEffects;

		public PosionBullet(String fileName, int dir, Field2D field) {
			super(fileName, dir, field);
			this.damages = new int[] { 2, 4, 6, 8 };
			this.dragEffects = new float[] { 0.5f, 0.5f, 0.5f, 0.5f };
			speed = SystemConstant.BulletSpeed.POSION_BULLET_SPEED;
			// 设置动作出发延时时间
			this.setDelay(50);
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
				enemy.beAttacted(this.damages[PosionTurret.this.currentLevel]);
				// 减速
				enemy.bePosion = true;
				enemy.posionTime = 10;
				enemy.move
						.setSpeed((int) (enemy.speed * this.dragEffects[PosionTurret.this.currentLevel]));
				return;
			} else if (isOutOfScreen()) {
				this.removeFromLayer();
			}
		}

	}
}
