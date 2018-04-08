package org.iSun.td.model;

import java.util.List;

import org.iSun.td.TurretDefense;
import org.iSun.td.constant.TurrentType;
import org.loon.framework.android.game.action.map.Field2D;

/**
 * 机枪炮台
 * 
 * @author iSun
 * 
 */
public class BulletTurret extends Turret {
	// 炮击间隔
	public int delay = 5;

	public BulletTurret(String fileName, TurretDefense td) {
		super(fileName, td);
		this.typeID = TurrentType.BULLET_TURRET;
		this.setInterval(50);
		this.ranges = new int[] { 60, 90, 100, 160 };
		this.values = new int[] { 15, 30, 75, 100 };
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

		// 炮击延时
		if (this.delay > 0) {
			--this.delay;
		} else if (!es.isEmpty()) {
			fire();
		}
	}

	@Override
	public void fire() {
		// 构造炮弹
		JetBullet bullet = new JetBullet(turrets[4], this.getRotation(), 4,
				field);
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
	 * 机枪子弹
	 * 
	 * @author iSun
	 * 
	 */
	class JetBullet extends BaseMissle {
		public JetBullet(String fileName, int dir, int damage, Field2D field) {
			super(fileName, dir, damage, field);
		}
	}
}
