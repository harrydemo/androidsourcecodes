package org.loon.test;

import org.loon.framework.android.game.core.geom.RectBox;
import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.Screen;
import org.loon.framework.android.game.core.graphics.component.Actor;
import org.loon.framework.android.game.core.graphics.component.ActorLayer;
import org.loon.framework.android.game.core.graphics.component.LLayer;
import org.loon.framework.android.game.core.graphics.device.LGraphics;
import org.loon.framework.android.game.core.graphics.opengl.GLEx;
import org.loon.framework.android.game.core.graphics.opengl.LTexture;
import org.loon.framework.android.game.core.input.LInput;
import org.loon.framework.android.game.core.input.LTouch;
import org.loon.framework.android.game.core.input.LInputFactory.Key;
import org.loon.framework.android.game.core.timer.LTimerContext;

public class Test1 extends Screen {

	LTexture right;

	LTexture left;

	LTexture shooting;

	LTexture title;

	LTexture arrow;

	LTexture groundImage;

	public Test1() {

	}

	class TestLayer extends LLayer {

		private boolean started;

		private boolean once;

		private int scrollSpeed;

		private boolean scroll;

		private boolean fall;

		private boolean ended;

		private int height;

		public TestLayer() {
			// Layer大小为300x400，不限制角色活动区域(为true时角色无法掉出屏幕)
			super(300, 400, false);
			LTexture.AUTO_LINEAR();
			// 禁止画面中的角色被拖拽
			setActorDrag(true);
			setLocked(true);
			setLimitMove(true);
			// 设定Layer中Action触发间隔为500毫秒
			setDelay(100);
			// 初始化Layer画面
			init();
		}

		/**
		 * 初始化游戏状态
		 * 
		 */
		public void init() {
			cleanup();
			this.started = false;
			this.addObject(new Ground(), 31, 312);
			this.addObject(new Doodler(false), 31, 195);
			this.setBackground("assets/title.png");
			this.once = true;
			this.started = false;
			this.height = 0;
			this.fall = false;
			this.ended = false;
		}

		public void downClick(int x, int y) {
			// 检查是否点击了图片上的游戏开始按钮（也可以直接add一个按钮）
			if (!this.started) {
				if ((x >= 48 && x < 140) && (y >= 104 && y <= 130)) {
					this.started = true;
				}
			}
		}

		public void action(long elapsedTime) {
			if (this.started && this.once) {
				// 以瓦片方式填充背景（即拼装等于Layer大小的背景）
				setTileBackground("assets/paper.png");
				cleanup();
				setLevel(1);
			}
			if (this.fall) {
				init();
			}
		}

		public void cleanup() {
			this.once = false;
			// 删除所有Doodler和Ground对象
			this.removeObject(Doodler.class);
			this.removeObject(Ground.class);
		}

		public void setLevel(int level) {
			switch (level) {
			case 1:
				gamePlay();
			}
		}

		/**
		 * 在Layer上绘制得分
		 */
		public void paint(LGraphics g) {
			g.setColor(LColor.black);
			g.drawString(height + " doodles.", 200, 390);
		}

		/**
		 * 初始化游戏
		 * 
		 */
		public void gamePlay() {
			int move = 0;
			// 添加Doodler角色到画面中心
			addObject(new Doodler(), (getWidth() - groundImage.getWidth()) / 2,
					100);

			// 添加最底层Ground
			addObject(new Ground(false), move, 390);
			addObject(new Ground(false), move += groundImage.getWidth(), 390);
			addObject(new Ground(false), move += groundImage.getWidth(), 390);
			addObject(new Ground(false), move += groundImage.getWidth(), 390);
			addObject(new Ground(false), move += groundImage.getWidth(), 390);
			addObject(new Ground(false), move += groundImage.getWidth(), 390);

			// 随机生成三个跳跃用位置，并以此添加Ground
			RectBox[] boxs = getRandomLayerLocation(groundImage.getWidth(),
					groundImage.getHeight(), 3);

			addObject(new Ground(), boxs[0].x(), 250);
			addObject(new Ground(), boxs[1].x(), 150);
			addObject(new Ground(), boxs[2].x(), 50);

		}

	}

	// 向上箭头
	class Arrow extends Actor {

		private boolean flag;

		public Arrow() {
			this.setImage("assets/arrow.png");
			this.flag = false;
		}

		public void action(long elapsedTime) {
			if (!flag) {
				this.setX((getLLayer().getWidth() - getWidth()) / 2);
				flag = true;
			}
		}

	}

	// 跳跃用瓦片
	class Ground extends Actor {

		private boolean hasBeenBounced;

		private int x_pos;

		private int y_pos;

		public Ground() {
			this.hasBeenBounced = false;
			this.setImage(groundImage);
			// 设定Action触发间隔为50毫秒
			this.setDelay(50);
		}

		public Ground(boolean bouncable) {
			this.hasBeenBounced = true;
			this.setImage(groundImage);
			// 设定Action触发间隔为50毫秒
			this.setDelay(50);
		}

		public void action(long elapsedTime) {

			TestLayer layer = (TestLayer) getLLayer();
			if (layer.scroll == true) {
				scroll(layer.scrollSpeed);
			}
			if (layer.fall == true) {
				fall(layer.scrollSpeed);
			}
			if (this.y_pos < 0) {
				layer.removeObject(this);
				return;
			}

		}

		protected void addLayer(ActorLayer world) {
			this.x_pos = x();
			this.y_pos = y();
		}

		public void scroll(int speed) {
			if (speed > 0) {
				this.y_pos += speed;
				setLocation(this.x_pos, this.y_pos);
			}
		}

		public void fall(int speed) {
			if (!((TestLayer) getLLayer()).ended) {
				this.y_pos += speed;
				setLocation(this.x_pos, this.y_pos);
			}
		}

	}

	// 跳跃用游戏角色
	class Doodler extends Actor {

		private float ys;

		private float xs;

		private boolean canMove;

		private int hits;

		public Doodler() {
			setImage(right);
			// 设定Action触发间隔为50毫秒
			this.setDelay(50);
			this.canMove = true;
		}

		public Doodler(boolean moveable) {
			setImage(right);
			// 设定Action触发间隔为50毫秒
			this.setDelay(50);
			this.canMove = moveable;

		}

		public void action(long elapsedTime) {

			TestLayer layer = (TestLayer) getLLayer();

			if (!layer.fall) {
				move();
			}

			if (this.ys > 12.0F) {
				this.ys = 10.0F;
			}
			if (this.canMove) {
				keys();
			}
			this.ys += 0.3F;
			setLocation(this.getX() + (int) this.xs, this.getY()
					+ (int) this.ys);
			limitJump();

			if ((this.canMove & this.getY() < layer.getHeight())) {
				scrollUp();
			}
			if (this.getY() > layer.getHeight()) {
				fall();
			}
			layer.height = this.hits;

			arrow();
		}

		public void move() {
			// 判定角色是否和任意Ground对象碰撞
			Ground g = (Ground) getOnlyCollisionObject(Ground.class);
			if ((g != null) && (this.ys > 0.0F)
					&& getX() + groundImage.getWidth() >= g.getX()
					&& getY() + groundImage.getHeight() <= g.getY()) {
				this.ys = -10.0F;
				// 生成跳跃用瓦片
				if ((this.canMove & !g.hasBeenBounced)) {
					ActorLayer world = getLLayer();
					g.hasBeenBounced = true;

					RectBox[] boxs = world.getRandomLayerLocation(groundImage
							.getWidth(), groundImage.getHeight(), 2);
					world.addObject(new Ground(), boxs[0].x(), 0);
					world.addObject(new Ground(), boxs[1].x(), 70);

				}
			}
		}

		/**
		 * 判定键盘事件（已设定此部分同重力感应联动,重力事件也会转到此）
		 * 
		 */
		public void keys() {
			LInput input = getLLayer().screenInput();
			if (input.isKeyPressed(Key.DPAD_RIGHT)) {
				this.xs += 0.25F;
				setImage(right);
			}
			if (input.isKeyPressed(Key.DPAD_LEFT)) {
				this.xs -= 0.25F;
				setImage(left);
			}
			if ((!input.isKeyPressed(Key.DPAD_LEFT)
					&& (!input.isKeyPressed(Key.DPAD_RIGHT)) && this.xs != 0.0F)) {
				this.xs = 0.0F;
			}
		}

		/**
		 * 限制角色移动区域（上、左、右）
		 * 
		 */
		public void limitJump() {
			RectBox rect = getRectBox();
			int limitWidth = (int) (getLLayer().getWidth() - rect.getWidth());
			int limitHeight = (int) rect.getHeight();
			if (this.getX() > limitWidth) {
				setLocation(limitWidth, getY());
			}
			if (this.getX() < 0) {
				setLocation(0, getY());
			}
			if (this.getY() < limitHeight) {
				setLocation(getX(), limitHeight);
			}
		}

		/**
		 * 调整滚动区域
		 * 
		 */
		public void scrollUp() {
			TestLayer layer = (TestLayer) getLLayer();
			if (this.getY() < 200 && this.getY() > 100) {
				layer.scrollSpeed = (int) (-this.ys);
				layer.scroll = true;
				this.hits += 1;
			} else if (this.getY() < 100) {
				layer.scrollSpeed = ((int) (-this.ys) * 2);
				layer.scroll = true;
				this.hits += 1;
			} else {
				layer.scroll = false;
			}
		}

		public void fall() {
			TestLayer layer = (TestLayer) getLLayer();
			layer.fall = true;
			layer.scrollSpeed = (int) (-this.ys);
		}

		public void arrow() {
			// 当游戏角色触顶时绘制向上箭头
			if ((this.getY() <= groundImage.getWidth() & getLLayer()
					.getCollisionObjects(Arrow.class).isEmpty())) {
				getLLayer().addObject(new Arrow());
			}
			// 当游戏角色非触顶时删除向上箭头
			if (((this.getY() > groundImage.getWidth()) && (!getLLayer()
					.getCollisionObjects(Arrow.class).isEmpty()))) {
				getLLayer().removeObject(Arrow.class);
			}
		}

	}

	public void onLoaded() {
		right = new LTexture("assets/doodler.png");
		left = right.flip(true, false);
		shooting = new LTexture("assets/shooting.png").scale(25, 40);
		title = new LTexture("assets/title.png");
		arrow = new LTexture("assets/arrow.png");
		groundImage = new LTexture("assets/ground.png");
		// 将重力方向转为键盘方向
		setGravityToKey(true);
		// 构建Layer
		TestLayer test = new TestLayer();
		// 居中显示
		centerOn(test);
		// 添加到Screen
		add(test);
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
