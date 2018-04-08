package org.iSun.td;

import java.util.ArrayList;
import java.util.HashMap;

import org.iSun.td.constant.SystemConstant;
import org.iSun.td.constant.TurrentType;
import org.iSun.td.model.BombTurret;
import org.iSun.td.model.BulletTurret;
import org.iSun.td.model.Enemy;
import org.iSun.td.model.PosionTurret;
import org.iSun.td.model.SoundWaveTurret;
import org.iSun.td.model.Turret;
import org.loon.framework.android.game.action.map.Field2D;
import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.device.LGraphics;
import org.loon.framework.android.game.core.graphics.window.actor.Actor;
import org.loon.framework.android.game.core.graphics.window.actor.Layer;

import android.graphics.Color;

/**
 * 游戏的地图
 * 
 * @author iSun
 * 
 */
public class MapLayer extends Layer {
	private boolean start;
	private int startX, startY, endX, endY;
	private int enemyIndex;
	public TurretDefense td;
	private Menu menu;
	public ArrayList<Enemy> enemys = new ArrayList<Enemy>();
	private int[] baseValue = new int[] { 15, 30, 25, 50 };

	public MapLayer(TurretDefense td, Menu menu) {

		super(576, 480, true);
		this.menu = menu;
		this.td = td;
		// 不锁定MapLayer的拖拽
		setLocked(false);
		// 锁定MapLayer中角色的拖拽
		setActorDrag(false);
		// 设置MapLayer背景元素（键值需要与map.txt文件中标识相互对应）
		HashMap<Integer, LImage> pathMap = new HashMap<Integer, LImage>(10);
		pathMap.put(new Integer(0), new LImage("assets/sand.png"));
		pathMap.put(new Integer(1), new LImage("assets/sandTurn1.png"));
		pathMap.put(new Integer(2), new LImage("assets/sandTurn2.png"));
		pathMap.put(new Integer(3), new LImage("assets/sandTurn3.png"));
		pathMap.put(new Integer(4), new LImage("assets/sandTurn4.png"));
		pathMap.put(new Integer(5), new LImage("assets/base.png"));
		pathMap.put(new Integer(6), new LImage("assets/castle.png"));

		// 为Layer加入简单的2D地图北京，瓦片大小32X32,以rock图片为铺垫
		setField2DBackground(new Field2D(td.mapName, 32, 32), pathMap,
				"assets/rock.png");
		this.td.field = getField2D();
		// 敌人出现的坐标
		this.startX = td.start.x;
		this.startY = td.start.y;

		// 敌人消失的坐标
		this.endX = td.end.x;
		this.endY = td.end.y;

		// 设定MapLayer每隔2秒执行一次内部Action
		setDelay(LSystem.SECOND);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action(long elapsedTime) {
		// TODO Auto-generated method stub
		// 启动标识为true时执行以下操作
		if (start) {
			if (enemyIndex < td.waveArray[td.wave % td.waveArray.length].length) {
				addEnemy(td.waveArray[td.wave % td.waveArray.length][enemyIndex]);
				enemyIndex++;
			} else if (enemyIndex >= td.waveArray[td.wave % td.waveArray.length].length
					&& enemys.size() <= 0) {
				td.wave++;
				enemyIndex = 0;
			}
		}
	}

	private void addEnemy(int type) {
		Enemy enemy = null;
		switch (type) {
		case SystemConstant.EnemyType.BASE_ENEMY:
			enemy = new Enemy("assets/enemy.png", startX, startY, endX, endY,
					3, 40 + (td.wave - 1) * 3, this);

			break;
		case SystemConstant.EnemyType.FASE_ENEMY:
			enemy = new Enemy("assets/fastEnemy.png", startX, startY, endX,
					endY, 5, 30 + (td.wave - 1) * 3, this);
			break;
		case SystemConstant.EnemyType.SMALL_ENEMY:
			enemy = new Enemy("assets/smallEnemy.png", startX, startY, endX,
					endY, 4, 25 + (td.wave - 1) * 3, this);
			break;
		case SystemConstant.EnemyType.BIG_ENEMY:
			enemy = new Enemy("assets/bigEnemy.png", startX, startY, endX,
					endY, 2, 50 + (td.wave - 1) * 2, this);
			break;
		}
		this.enemys.add(enemy);
		addObject(enemy);
	}

	// 指向被点击的Actor
	private Actor o = null;

	@Override
	public void downClick(int x, int y) {
		// TODO Auto-generated method stub
		// 将上一次的点击标准位去掉
		if (td.beSelectedTurret != null && td.beSelectedTurret.selected) {
			td.beSelectedTurret.selected = false;
			menu.sell.setVisible(false);
			menu.update.setVisible(false);
		}
		// 转换点击区域为数组地图坐标
		int newX = x / td.field.getTileWidth();
		int newY = y / td.field.getTileHeight();
		// 当选中塔炮（参数不为-1)且数组地图参数为-1（不可通过）并且无其他角色在此时
		if ((o = getClickActor()) == null && td.selectTurret != -1
				&& td.field.getType(newY, newX) == -1) {
			// 钱够了就添加炮台
			if (td.getMoney() >= baseValue[td.selectTurret]) {
				addTurret(td.selectTurret, x, y);
			}
			// 防止连续添加
			td.selectTurret = -1;
		}
		// 更新UI的显示
		updateUI(o);
		super.downClick(newX, newY);
	}

	public void updateUI(Object turret) {
		if (o != null && o instanceof Turret) {
			Turret temp = (Turret) o;
			temp.selected = true;
			td.beSelectedTurret = temp;
			menu.sell.setVisible(true);
			if (td.beSelectedTurret.updateAble
					&& td.getMoney() >= td.beSelectedTurret.values[td.beSelectedTurret.currentLevel] / 2) {
				menu.update.setVisible(true);
			}
		}
	}

	public void addTurret(int type, int x, int y) {
		int newX = x / td.field.getTileWidth();
		int newY = y / td.field.getTileHeight();
		Turret turret = null;
		switch (type) {
		case TurrentType.BULLET_TURRET:
			turret = new BulletTurret(td.turrets[type], td);
			break;
		case TurrentType.BOMB_TURRET:
			turret = new BombTurret(td.turrets[type], td);
			break;
		case TurrentType.POSION_TURRET:
			turret = new PosionTurret(td.turrets[type], td);
			break;
		case TurrentType.SOUNDWAVE_TURRET:
			turret = new SoundWaveTurret(td.turrets[type], td);
			break;
		}
		td.setMoney(td.getMoney() - turret.values[0]);
		addObject(turret, newX * td.field.getTileWidth(),
				newY * td.field.getTileHeight());
	}

	public void doStart() {
		this.start = true;
	}

	@Override
	public void paint(LGraphics g) {
		// TODO Auto-generated method stub
		LColor color = g.getColor();
		g.setColor(Color.WHITE);
		g.drawString("" + td.lives, endX + 10, endY + 20);
		g.setColor(color);
	}

}
