package org.loon.test.barrage;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.loon.framework.android.game.action.map.AStarFinder;
import org.loon.framework.android.game.action.map.Config;
import org.loon.framework.android.game.action.map.Field2D;
import org.loon.framework.android.game.action.sprite.Sprite;
import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.geom.Ellipse;
import org.loon.framework.android.game.core.geom.RectBox;
import org.loon.framework.android.game.core.geom.Vector2f;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.Screen;
import org.loon.framework.android.game.core.graphics.component.LButton;
import org.loon.framework.android.game.core.graphics.opengl.GLColor;
import org.loon.framework.android.game.core.graphics.opengl.GLEx;
import org.loon.framework.android.game.core.graphics.opengl.LTexture;
import org.loon.framework.android.game.core.input.LTouch;
import org.loon.framework.android.game.core.timer.LTimer;
import org.loon.framework.android.game.core.timer.LTimerContext;
import org.loon.framework.android.game.utils.GraphicsUtils;
import org.loon.framework.android.game.utils.MathUtils;

abstract class Barrage {

	protected float x, y;

	protected int power, life;

	protected boolean remove;

	public abstract boolean isKilled();

	public abstract boolean checkCollision(float tx, float ty, int w, int h);

	abstract public void draw(GLEx g);

	abstract public void update(List<Barrage> attacks, int px, int py);
}

class Ball extends Barrage {

	private final static GLColor color1 = new GLColor(255, 153, 0),
			color2 = new GLColor(255, 209, 139);

	public Ball(int sx, int sy) {
		x = sx;
		y = sy;
		power = 1;
	}

	public boolean isKilled() {
		return remove || (y < -20);
	}

	public void draw(GLEx g) {
		g.setColor(color1);
		g.fillRect((int) (x - 1), (int) (y + 5), 3, 15);
		g.setColor(color2);
		g.drawLine((int) x, (int) y, (int) x, (int) (y + 20));
		g.resetColor();
	}

	public void update(List<Barrage> attacks, int px, int py) {
		y -= 10;
	}

	public boolean checkCollision(float tx, float ty, int w, int h) {
		return false;
	}

}

class EnemyBall extends Barrage {

	private float vx, vy;

	private int width, height;

	private LTexture image;

	private RectBox rect;

	public EnemyBall(float sx, float sy, float ivx, float ivy, int type) {
		x = sx;
		y = sy;
		vx = ivx;
		vy = ivy;
		switch (type) {
		case 0:
			image = new LTexture("assets/b.png");
			break;
		case 1:
			image = new LTexture("assets/c.png");
			break;
		}
		width = image.getWidth();
		height = image.getHeight();

	}

	public boolean isKilled() {
		if (x < 0 || x > LSystem.screenRect.getWidth())
			return true;
		if (y < 0 || y > LSystem.screenRect.getHeight())
			return true;
		return remove;
	}

	public boolean checkCollision(float tx, float ty, int w, int h) {
		if (rect == null) {
			rect = new RectBox((int) x, (int) y, image.getWidth(), image
					.getHeight());
		} else {
			rect.setBounds((int) x, (int) y, image.getWidth(), image
					.getHeight());
		}
		return rect.intersects((int) tx, (int) ty, w, h)
				|| rect.contains((int) tx, (int) ty);
	}

	public void draw(GLEx g) {
		g.drawTexture(image, (int) x - width / 2, (int) y - height / 2);
	}

	public void update(List<Barrage> attacks, int px, int py) {
		x += vx;
		y += vy;
	}
}

class EnemyA extends Barrage {

	private float vx, vy;

	private int c, ca;

	private static LImage[] bees;

	private LTimer timer;

	private int size;

	private int index;

	private RectBox rect;

	public EnemyA(float sx, float sy, float ivx, float ivy) {
		timer = new LTimer(100);
		size = 48;
		x = sx;
		y = sy;
		vx = ivx;
		vy = ivy;
		if (bees == null) {
			bees = GraphicsUtils.getSplitImages("assets/d.png", size, size,
					false);
		}
		life = 5;
	}

	public boolean isKilled() {
		if (x < 0 || x > LSystem.screenRect.getWidth()) {
			return true;
		}
		if (y < 0 || y > LSystem.screenRect.getHeight()) {
			return true;
		}
		return remove;
	}

	public boolean checkCollision(float tx, float ty, int w, int h) {
		if (rect == null) {
			rect = new RectBox((int) x, (int) y, size, size);
		} else {
			rect.setBounds((int) x, (int) y, size, size);
		}
		return rect.intersects((int) tx, (int) ty, w, h);
	}

	public void draw(GLEx g) {
		LImage img = bees[index];
		if (img != null) {
			g.drawImage(img, (int) x - size / 2, (int) y - size / 2);
		}
	}

	public float distance(float x1, float y1, float x2, float y2) {
		x1 -= x2;
		y1 -= y2;
		return MathUtils.sqrt(x1 * x1 + y1 * y1);
	}

	public void update(List<Barrage> attacks, int px, int py) {
		x += vx;
		y += vy;
		if (timer.action(30)) {
			if (index < 4) {
				index++;
			} else {
				index = 0;
			}
		}
		if (c % 15 == 0 && ca < 5) {
			// 做一组以斜角射出的子弹
			float d = distance(x, y, px, py);
			attacks.add(new EnemyBall(x, y, (px - x) / d * 3, (py - y) / d * 3,
					0));
			++ca;
		}

		if (c == 0) {
			// 做一组360度围绕的子弹
			for (int i = 0; i < 360; i += 30) {
				float rad = 2 * MathUtils.PI * ((float) i / 360);
				attacks.add(new EnemyBall(x, y, MathUtils.cos(rad) + vx,
						MathUtils.sin(rad) + vy, 1));
			}
		}

		++c;
		c %= 150;
	}

}

public class MyBarrage extends Screen {

	private boolean fire, bomb;

	private int counterEnemy;

	private int counterShot, counterBomb;

	private int hit, hitCount;

	// 我方子弹集合
	private LinkedList<Barrage> balls = new LinkedList<Barrage>();

	// 敌方角色集合
	private LinkedList<Barrage> enemys = new LinkedList<Barrage>();

	// 敌方子弹集合
	private LinkedList<Barrage> enemyBalls = new LinkedList<Barrage>();

	// 弹幕对象移动用集合
	ListIterator<Barrage> move;

	// 敌人状态处理用集合
	ListIterator<Barrage> enemyList;

	@SuppressWarnings("unchecked")
	private List[] barrages = { balls, enemys, enemyBalls };

	private int size;

	private int speed;

	private int sleep;

	private LinkedList<Vector2f> movePath;

	private Sprite spr;

	private int[][] map;

	private LButton btn1, btn2;

	private Ellipse bombArea;

	public MyBarrage() {
		LTexture.AUTO_NEAREST();
	}

	public void onLoad() {
		// 截按钮小图所使用的宽与高
		int w = 42, h = 42;
		// 创建按钮1，并关联子弹发射
		btn1 = new LButton("assets/b1.png", w, h) {
			public void downClick() {
				fire = true;
			}

			public void upClick() {
				fire = false;
			}
		};
		btn1.setLocation(getWidth() - w - 25, getHeight() - h - 25);
		add(btn1);

		// 创建按钮2，并关联小全屏攻击
		btn2 = new LButton("assets/b2.png", w, h) {
			public void downClick() {
				bomb = true;
			}

			public void upClick() {
				bomb = false;
			}
		};
		btn2.setLocation(25, getHeight() - h - 25);
		add(btn2);
		// 设定地图块大小
		size = 32;
		// 设定主角机移动速度
		speed = 9;
		// 创建一个2维地图数组，用以进行触摸屏寻径，每块地图区域大小为size大小
		map = new int[getHeight() / size][getWidth() / size];
		// 创建精灵
		spr = new Sprite("assets/a.png", 47, 47);
		// 不自动播放精灵
		spr.setRunning(false);
		// 画面显示为第二帧
		spr.setCurrentFrameIndex(2);
		// 设定精灵初始位置
		spr.setLocation(128, 128);
		// 插入精灵
		add(spr);
	}

	public void alter(LTimerContext c) {

		// 角色进行射击
		if (fire) {
			if (counterShot == 0) {
				// 装入子弹
				balls
						.add(new Ball((spr.x() + spr.getWidth() / 2) - 5, spr
								.y()));
				balls
						.add(new Ball((spr.x() + spr.getWidth() / 2) + 5, spr
								.y()));

			}
			counterShot += 1;
			counterShot %= 5;
		} else {
			counterShot = 0;
		}

		// 当地人数量为0时，添加新的敌人
		if (counterEnemy == 0) {
			enemys.add(new EnemyA(MathUtils.random() * getWidth(), 0, 0, 2));
		}
		counterEnemy += 1;
		counterEnemy %= 100;

		// 遍历弹幕对象，并删除已死亡对象
		for (List<Barrage> x : barrages) {
			move = x.listIterator();
			for (; move.hasNext();) {
				Barrage f = move.next();
				f.update(enemyBalls, spr.x(), spr.y());
				if (f.isKilled()) {
					move.remove();
				}
			}
		}

		// 设定屏幕炸弹大小
		if (counterBomb > 0) {
			counterBomb -= 1;
		} else if (bomb) {
			counterBomb = 30;
		}

		int bombHalf = (30 - counterBomb) * 4, bombSize = 2 * bombHalf;
		if (bombArea == null) {
			bombArea = new Ellipse((spr.x() + spr.getWidth() / 2) - bombHalf,
					(spr.y() + spr.getHeight() / 2) - bombHalf, bombSize,
					bombSize);
		} else {
			bombArea.set((spr.x() + spr.getWidth() / 2) - bombHalf,
					(spr.y() + spr.getHeight() / 2) - bombHalf, bombSize,
					bombSize);
		}

		// 重新获得敌方移动行为
		move = enemys.listIterator();
		for (; move.hasNext();) {
			Barrage enemy = move.next();
			if (enemy.isKilled()) {
				continue;
			}
			if (counterBomb > 0) {
				if (bombArea.contains(enemy.x, enemy.y)) {
					enemy.remove = true;
					continue;
				}
			}

			// 处理与我方子弹对应的敌方状态
			enemyList = balls.listIterator();
			for (; enemyList.hasNext();) {
				Barrage bullet = enemyList.next();
				if (bullet.isKilled()) {
					continue;
				}
				if (enemy.checkCollision(bullet.x, bullet.y, spr.getWidth(),
						spr.getHeight())) {
					enemy.life -= bullet.power;

					bullet.remove = true;

					if (enemy.life < 0) {
						enemy.remove = true;
					}
				}
			}
		}

		enemyList = enemyBalls.listIterator();
		for (; enemyList.hasNext();) {
			Barrage bullet = enemyList.next();
			if (bullet.isKilled()) {
				continue;
			}
			// 为指定区域清场
			if (counterBomb > 0) {
				if (bombArea.contains(bullet.x, bullet.y)) {
					bullet.remove = true;
					continue;
				}
			}

			// 判定是否击中敌人
			if (bullet.checkCollision(spr.x(), spr.y(), 0, 0)) {
				++hitCount;
				hit = 20;
				bullet.remove = true;
				continue;
			}
		}

		// 如果移动路径不存在，则跳出此函数
		if (movePath == null) {
			return;
		}

		/**
		 * PS:此种写法并非必需，如果仅仅想触摸屏移动角色，只要验证触摸点坐标相对于屏幕上下左右四点位置并
		 * 移动角色即可(getTouchDirection())。此种写法的实际作用在于，获得角色的移动方向，并根据不同移动 方向变更角色用图。
		 */

		// 如果存在移动行为
		if (movePath.size() > 1) {
			Vector2f cell1 = (Vector2f) movePath.get(0);
			Vector2f cell2 = (Vector2f) movePath.get(1);
			int nx = cell2.x() - cell1.x();
			int ny = cell2.y() - cell1.y();

			// 获得角色移动方向，也可以据此改变角色动作，八方位移动足够任何2D游戏使用
			int d = Field2D.getDirection(nx, ny);

			// 如果步长超过了size(意味着此格已走完)，删除原前进路径，进行下一路径
			if (sleep > size) {
				movePath.remove(0);
				sleep = 0;
			}

			// 根据前进方向改变角色移动行为，如果准备了不同方向的角色图也可在此处变更。
			// 顺带一提，LGame中无论是精灵或组件都支持八方向移动
			switch (d) {
			case Config.DOWN:
				spr.move_45D_down(speed);
				spr.setCurrentFrameIndex(1);
				break;
			case Config.UP:
				spr.move_45D_up(speed);
				spr.setCurrentFrameIndex(4);
				break;
			case Config.LEFT:
				spr.move_45D_left(speed);
				spr.setCurrentFrameIndex(0);
				break;
			case Config.RIGHT:
				spr.move_45D_right(speed);
				spr.setCurrentFrameIndex(5);
				break;
			case Config.TUP:
				spr.move_up(speed);
				spr.setCurrentFrameIndex(2);
				break;
			case Config.TLEFT:
				spr.move_left(speed);
				spr.setCurrentFrameIndex(0);
				break;
			case Config.TDOWN:
				spr.move_down(speed);
				spr.setCurrentFrameIndex(6);
				break;
			case Config.TRIGHT:
				spr.move_right(speed);
				spr.setCurrentFrameIndex(4);

				break;
			}
			sleep += speed;

			// 矫正一下角色位置，以防“逃出”屏幕
			if (spr.x() > getWidth() - spr.getWidth()) {
				spr.setX(getWidth() - spr.getWidth());
			}
			if (spr.y() > getHeight() - spr.getHeight()) {
				spr.setY(getHeight() - spr.getHeight());
			}
		} else {
			spr.setCurrentFrameIndex(2);
		}

	}

	public void draw(GLEx g) {

		if (counterBomb > 0) {
			int bombHalf = (30 - counterBomb) * 4, bombSize = 2 * bombHalf;
			g.setColor(GLColor.orange);
			g.fillOval((spr.x() + spr.getWidth() / 2) - bombHalf,
					(spr.y() + spr.getHeight() / 2) - bombHalf, bombSize,
					bombSize);
		}
		if (hit > 0) {
			spr.setFilterColor(GLColor.red);
			--hit;
		} else {
			spr.setFilterColor(null);
		}
		// 遍历所有弹幕对象，并一一绘制
		for (List<Barrage> o : barrages) {
			ListIterator<Barrage> barrage = o.listIterator();
			for (; barrage.hasNext();) {
				barrage.next().draw(g);
			}
		}
		g.resetColor();

	}

	@Override
	public void touchDown(LTouch arg0) {
		int posX = getTouchX();
		int posY = getTouchY();
		// 不处理绘制的按钮1与2
		if (btn1 != null && btn1.intersects(posX, posY)
				|| btn1.contains(posX, posY)) {
			return;
		}
		if (btn2 != null && btn2.intersects(posX, posY)
				|| btn1.contains(posX, posY)) {
			return;
		}
		// 清空原有的寻径结果
		if (movePath != null) {
			movePath.clear();
		}
		// A*寻径，八方向全开(为false时四方向寻径)
		movePath = AStarFinder.find(map, (spr.x() / size), (spr.y() / size),
				posX / size, posY / size, true);

	}

	@Override
	public void touchMove(LTouch arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void touchUp(LTouch arg0) {
		// TODO Auto-generated method stub

	}

}
