package org.iSun.td.model;

import java.util.List;

import org.iSun.td.TurretDefense;
import org.iSun.td.constant.SystemConstant;
import org.iSun.td.constant.TurrentType;
import org.loon.framework.android.game.action.map.Field2D;

/**
 * ��ҩ��̨�����ٹ���
 * 
 * @author iSun
 * 
 */
public class PosionTurret extends Turret {
	// �ڻ����
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
		// ����ָ���뾶�����е�Enemy
		List<?> es = this.getAttactAbleEnemys();

		// �����ڵ���ʱ
		if (!es.isEmpty()) {
			Enemy target = (Enemy) es.get(0);
			this.rotateToTarget(target);
		}

		// �����ڻ���ʱ����
		if (this.delay > 0) {
			--this.delay;
		} else if (!es.isEmpty()) {
			fire();
		}
	}

	@Override
	public void fire() {
		// �����ڵ�
		PosionBullet bullet = new PosionBullet("assets/poison.png",
				this.getRotation(), field);
		// �����ڻ���
		int x = (int) Math.round(Math.cos(Math.toRadians(this.getRotation()))
				* (double) bullet.getWidth() * 2)
				+ this.getX();

		int y = (int) Math.round(Math.sin(Math.toRadians(this.getRotation()))
				* (double) bullet.getHeight() * 2)
				+ this.getY();
		// ���ڵ���ӵ�Layer
		this.getLayer().addObject(bullet, x, y);
		// td.playtAssetsMusic("audio/minigun_shot.mp3", false);
		// ��λ��ʱ��������
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
			// ���ö���������ʱʱ��
			this.setDelay(50);
		}

		/**
		 * ��������
		 */
		@Override
		public void action(long elapsedTime) {
			// TODO Auto-generated method stub
			if (removeFlag) {
				return;
			}

			move();
			Enemy enemy = this.getCollisionEnemy();
			// ���������ײʱ
			if (enemy != null) {
				this.removeFromLayer();
				enemy.beAttacted(this.damages[PosionTurret.this.currentLevel]);
				// ����
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
