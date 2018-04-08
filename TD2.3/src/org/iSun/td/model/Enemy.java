package org.iSun.td.model;

import org.iSun.td.GameOverLayer;
import org.iSun.td.MapLayer;
import org.loon.framework.android.game.IAndroid2DHandler;
import org.loon.framework.android.game.action.ActionListener;
import org.loon.framework.android.game.action.FadeTo;
import org.loon.framework.android.game.action.MoveTo;
import org.loon.framework.android.game.action.map.Field2D;
import org.loon.framework.android.game.action.sprite.StatusBar;
import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.device.LGraphics;
import org.loon.framework.android.game.core.graphics.window.actor.Actor;
import org.loon.framework.android.game.core.graphics.window.actor.ActorLayer;
import org.loon.framework.android.game.media.sound.AssetsSound;
import org.loon.framework.android.game.media.sound.AssetsSoundManager;

/**
 * 敌人
 * 
 * @author iSun
 * 
 */
public class Enemy extends Actor {
	private boolean isexploding = false;
	private static LImage[] explodes = new LImage[] {
			new LImage("assets/explode/bom_1.png"),
			new LImage("assets/explode/bom_2.png"),
			new LImage("assets/explode/bom_3.png"),
			new LImage("assets/explode/bom_4.png"),
			new LImage("assets/explode/bom_5.png"),
			new LImage("assets/explode/bom_6.png"),
			new LImage("assets/explode/bom_7.png"),
			new LImage("assets/explode/bom_8.png"),
			new LImage("assets/explode/bom_9.png") };
	public int startX, startY;
	public int endX, endY;
	// 是否中毒
	public boolean bePosion = false;
	public int posionTime = 10;

	public MoveTo move;
	public int speed, hp;
	private int value;

	public boolean removeFlag;

	// 使用精灵StatusBar充当血块
	public StatusBar hpBar;

	private MapLayer layer;
	private int index;

	public Enemy(String fileName, int sx, int sy, int ex, int ey, int speed,
			int hp, MapLayer layer) {
		this.setImage(fileName);
		this.hpBar = new StatusBar(hp, hp, (this.getWidth() - 25) / 2,
				this.getHeight() - 25, 25, 5);
		this.startX = sx;
		this.startY = sy;
		this.endX = ex;
		this.endY = ey;
		this.speed = speed;
		this.hp = hp;
		this.value = this.hp;
		this.layer = layer;
		this.setDelay(50);
	}

	/**
	 * 图形接口，绘制额外的图形到Actor
	 */
	@Override
	public void draw(LGraphics g) {
		// TODO Auto-generated method stub
		// 绘制精灵
		if (isexploding) {
			if (index < explodes.length) {
				LImage img = explodes[index];
				g.drawImage(img, -50, -50);
			} else {
				isexploding = false;
			}
		}
		hpBar.createUI(g);
	}

	/**
	 * 死亡
	 */
	public void dead() {
		AssetsSound as = new AssetsSound("audio/enemy_die.mp3");
		as.setLooping(false);
		as.play();
		// 设定死亡时渐变
		FadeTo fade = fadeIn();
		this.removeFlag = true;
		fade.setSpeed(10);
		fade.setActionListener(new ActionListener() {
			@Override
			public void stop(Actor o) {
				// TODO Auto-generated method stub
				// 删除所有以当前Actor注册的动作事件
				Enemy.this.removeActionEvents();
				Enemy.this.getLayer().removeObject(Enemy.this);
				Enemy.this.layer.enemys.remove(Enemy.this);
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

	@Override
	public void action(long elapsedTime) {
		// TODO Auto-generated method stub
		if (removeFlag) {
			return;
		}
		this.updateExplodeFrame();
		if (this.bePosion && !removeFlag) {
			if (this.posionTime > 0) {
				this.posionTime--;
			} else {
				this.bePosion = false;
				this.move.setSpeed(this.speed);
			}
		}

		hpBar.update(elapsedTime);
		if (hp <= 0 && !removeFlag) {
			this.layer.td.setScore(this.layer.td.getScore() + this.value / 2);
			this.layer.td.setMoney(this.layer.td.getMoney() + this.value / 2);
			this.dead();
		}
	}

	// 首次注入Layer时调用此函数
	@Override
	protected void addLayer(final ActorLayer gameLayer) {
		// TODO Auto-generated method stub

		// 修正敌人的坐标
		final int offsetX = (getLayer().getField2D().getTileWidth() - this
				.getWidth()) / 2;
		final int offsetY = (getLayer().getField2D().getTileWidth() - this
				.getHeight()) / 2;
		// 初始化在Layer中的坐标
		super.setLocation(startX + offsetX, startY + offsetY);

		// 移动到目标地点
		move = moveTo(endX, endY, false);
		// 设定移动的速度
		move.setSpeed(speed);
		move.setActionListener(new ActionListener() {
			// 敌人到达目的地
			@Override
			public void stop(Actor o) {
				// TODO Auto-generated method stub
				// 从Layer中删除这个角色
				removeFlag = true;
				LSystem.getSystemHandler().getAssetsSound()
						.playSound("audio/enemy_score.mp3", false);
				gameLayer.removeObject(o);
				// 从容器中删除
				Enemy.this.layer.enemys.remove(Enemy.this);
				layer.td.lives--;
				if (layer.td.lives <= 0) {
					enemyWin();
				}
			}

			@Override
			public void start(Actor o) {
				// TODO Auto-generated method stub

			}

			// 截获事件进行中的数据
			@Override
			public void process(Actor o) {
				// TODO Auto-generated method stub
				// 矫正坐标，让角色居中
				o.setLocation(o.getX() + offsetX, o.getY() + offsetY);
				// 获取角色的移动方向
				switch (move.getDirection()) {
				case Field2D.TUP:
					// 更具当前的移动方向，改变角色的旋转方向
					o.setRotation(270);
					break;
				case Field2D.TLEFT:
					o.setRotation(180);
					break;
				case Field2D.TRIGHT:
					o.setRotation(0);
					break;
				case Field2D.TDOWN:
					o.setRotation(90);
					break;
				default:
					break;
				}
			}
		});
	}

	/**
	 * 敌人胜利了，显示gameOver画面
	 */
	public void enemyWin() {
		clear();
		GameOverLayer gameOverLayer = new GameOverLayer("assets/game_over.png",
				480, 320);
		gameOverLayer.setLayer(103);
		layer.td.centerOn(gameOverLayer);
		layer.td.add(gameOverLayer);
	}

	/**
	 * 清理精灵，和声音
	 */
	public void clear() {
		IAndroid2DHandler handler = (IAndroid2DHandler) LSystem
				.getSystemHandler();
		handler.getAssetsSound().stopSoundAll();
		layer.td.removeAll();
	}

	/**
	 * 被击中
	 * 
	 * @param damage
	 */
	public void beAttacted(int damage) {
		if (removeFlag) {
			return;
		}
		isexploding = true;
		AssetsSoundManager.getInstance().playSound("audio/minigun_hit.mp3",
				false);

		// 减少敌人的HP
		this.hp -= damage;
		this.hpBar.setUpdate(this.hp);
	}

	public void updateExplodeFrame() {

		if (index < explodes.length) {
			index++;
		} else {
			index = 0;
		}
	}
}
