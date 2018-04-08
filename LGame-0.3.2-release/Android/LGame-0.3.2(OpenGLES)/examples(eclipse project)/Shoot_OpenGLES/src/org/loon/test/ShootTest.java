package org.loon.test;

import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.graphics.Screen;
import org.loon.framework.android.game.core.graphics.component.Actor;
import org.loon.framework.android.game.core.graphics.component.ActorSpeed;
import org.loon.framework.android.game.core.graphics.component.LPaper;
import org.loon.framework.android.game.core.graphics.component.LLayer;
import org.loon.framework.android.game.core.graphics.component.Speed;
import org.loon.framework.android.game.core.graphics.opengl.GLEx;
import org.loon.framework.android.game.core.graphics.opengl.LTexture;
import org.loon.framework.android.game.core.input.LInput;
import org.loon.framework.android.game.core.input.LTouch;
import org.loon.framework.android.game.core.input.LInputFactory.Key;
import org.loon.framework.android.game.core.timer.LTimerContext;


public class ShootTest extends Screen {

	/**
	 * 杂兵用类
	 */
	class Enemy extends ActorSpeed {

		private boolean exploded;

		private int health;

		public Enemy(LTexture image) {
			super(new Speed(LSystem.random.nextInt(360), 2.0f));
			setEnemy(image, image.getWidth());
			// Action动作延迟50毫秒执行
			setDelay(50);
		}

		public void action(long t) {
			if (this.exploded) {
				return;
			}
			move();
		}

		public void setEnemy(LTexture image, int size) {
			this.health = size;
			this.setImage(image);
		}

		private void explode() {
			if (this.exploded) {
				return;
			}
			this.exploded = true;
			getLLayer().removeObject(this);
		}

		public void hit(int damage) {
			if (this.exploded) {
				return;
			}
			this.health -= damage;
			if (this.health <= 0) {
				explode();
			}
		}
	}

	/**
	 * 子弹用类
	 */
	class Fire extends ActorSpeed {

		private int damage = 100;

		private int life = 35;

		public Fire(Speed speed, LTexture image, int rotation) {
			super(speed);
			setImage(image);
			setRotation(rotation);
			increaseSpeed(new Speed(rotation, 7.0f));
			// Action动作延迟50毫秒执行
			setDelay(50);
		}

		public void action(long elapsedTime) {
			// 查询子弹生命（持久力）是否耗尽
			if (this.life <= 0) {
				getLLayer().removeObject(this);
			} else {
				// 移动子弹
				move();
				// 查询子弹是否超过边界
				if (!getLLayer().getCollisionBox().contains(getRectBox())) {
					getLLayer().removeObject(this);
					return;
				}
				// 获得杂兵是否与子弹碰撞，返回唯一值（第一个查询到的）
				// PS:此仅为矩形碰撞，更精确可用SpriteImage取Polygon比对
				Enemy e = (Enemy) getOnlyCollisionObject(Enemy.class);
				if (e != null) {
					getLLayer().removeObject(this);
					e.hit(this.damage);
				}
				this.life -= 1;
			}
		}
	}

	/**
	 * 主角用类
	 */
	class Hero extends ActorSpeed {

		private Speed acceleration = new Speed(0, 0);

		private LTexture fireImage;

		public Hero(LTexture image) {
			this.setImage(image);
		}

		public void setAcceleration(int direction, float length) {
			acceleration.set(direction, length);
			setDelay(100);
		}

		public void action(long elapsedTime) {
			LInput input = getLLayer().screenInput();
			// 根据左右摇摆来旋转角色
			if (input.isKeyPressed(Key.DPAD_LEFT)) {
				setRotation(getRotation() - 3);
			}
			if (input.isKeyPressed(Key.DPAD_RIGHT)) {
				setRotation(getRotation() + 3);
			}
			if (input.isKeyPressed(Key.ENTER)) {
				fire();
			}
			move();
		}

		private void fire() {
			// 当子弹不存在于屏幕时
			if (getCollisionObjects(Fire.class).isEmpty()) {
				Fire f = new Fire(getSpeed().copy(), fireImage, (int)getRotation());
				getLLayer().addObject(f, x(), y());
				f.move();
			}
		}

		public LTexture getFireImage() {
			return fireImage;
		}

		public void setFireImage(LTexture fireImage) {
			this.fireImage = fireImage;
		}
	}

	public void onLoad() {

		// 将重力方向转为键盘方向
		setGravityToKey(true);
		// 构建一个等于Screen大小的图层，不锁定角色移动范围(为true时将刚性制约角色坐标为Layer内,
		// 无论拖拽还是set坐标都无法超出)
		final LLayer layer = new LLayer(getWidth(), getHeight(), false);

		layer.setBackground("assets/back.png");
		// 杂兵出现的边界位置
		final int enemyWidth = getWidth() - 70;
		final int enemyHeight = getHeight() - 70;

		// 杂兵1使用的图形大小
		final int e1size = 68;
		// 主角机图片
		final LTexture a1 = new LTexture("assets/a1.png");
		// 敌人图片
		final LTexture e1 = new LTexture("assets/e1.png").scale(e1size,
				e1size);

		// 子弹图片
		final LTexture f1 = new LTexture("assets/f1.png");

		// 此项参数决定了Layer拖拽是否受到限制；当Layer小于Screen大小时，Layer拖拽无法
		// 超过Screen的显示范围；当Layer大于Screen大小时，Screen仅允许拖拽Layer能够被
		// 显示出来的部分(比如设定一个800x480的Layer在480x320的Screen中，那么无论如何
		// 拖拽也仅可见800x480范围内的Layer画面，而不会出现黑边),默认此项开启。
		// PS:与标准LGame组件一样，Layer能被拖拽的大前提是setLocked项必须设为false
		// layer.setLimitMove(false);
		// layer.setLocked(false);

		// 此项参数决定了Layer中Actor角色是否能被直接拖拽，默认为true，即可以直接拖拽
		// layer.setActorDrag(false);

		// 此项参数决定了Layer中Actor角色是否响应触屏点击，默认为true，即可以直接获得点击事件
		// layer.setTouchClick(false);

		// 为Layer加载背景
		// layer.setBackground("assets/back.png");

		// 构建LPaper作为启动按钮，点击时触发【狼与车夫】机体的战斗事件
		LPaper button1 = new LPaper("assets/b1.png") {
			public void downClick() {
				// 获得Button所在Layer
				LLayer superLayer = getTopLayer();

				// 清空Layer中所有LPaper组件
				superLayer.remove(LPaper.class);

				// 构建【狼与车夫】机体
				Hero hero = new Hero(a1);
				hero.setFireImage(f1);
				hero.setAcceleration(0, 0.05f);

				// 添加主角机并居中
				superLayer.addObject(hero);
				superLayer.centerOn(hero);

				// 在Layer上设置杂兵
				superLayer.addObject(new Enemy(e1), enemyWidth, enemyHeight);
				superLayer.addObject(new Enemy(e1), enemyWidth, 70);
				superLayer.addObject(new Enemy(e1), 70, enemyHeight);
				superLayer.addObject(new Enemy(e1), 70, 70);
				superLayer.addObject(new Enemy(e1), 70, enemyHeight);
				superLayer.addObject(new Enemy(e1), 300, 70);
				superLayer.addObject(new Enemy(e1), 300, enemyHeight);
				superLayer.addObject(new Enemy(e1), enemyWidth, 300);

				// 构建一个固定位置的Actor，充当射击按钮(用LPaper、LButton亦可，此处纯演示用)
				Actor click = new Actor("assets/b2.png") {
					public void downClick(int x, int y) {
						// 当点击此角色时，伪造enter事件给Layer（子弹发射）
						setKeyDown(Key.ENTER);
					}

					public void upClick(int x, int y) {
			
						// 释放enter事件
						setKeyUp(Key.ENTER);
					}
				};
				// 设定此Actor不能被拖拽
				click.setDrag(false);

				superLayer.addObject(click, 10, this.getHeight() - 70);
			}
		};

		// 居中显示按钮1
		layer.centerOn(button1);

		// 在Layer中添加按钮1
		layer.add(button1);

		// 将Layer添加到Screen当中
		add(layer);

	}


	public void alter(LTimerContext arg0) {
		
	}

	public void draw(GLEx arg0) {

	}

	public void touchDown(LTouch arg0) {
	
	}

	public void touchMove(LTouch arg0) {

	}

	public void touchUp(LTouch arg0) {

	}

}
