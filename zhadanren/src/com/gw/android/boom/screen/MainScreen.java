package com.gw.android.boom.screen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.gw.android.boom.R;
import com.gw.android.boom.globe.BoomVariable;
import com.gw.android.boom.globe.Utils;
import com.gw.android.boom.pojo.Bomb;
import com.gw.android.boom.pojo.M0;
import com.gw.android.boom.pojo.Mob;
import com.gw.android.boom.pojo.Player;

public class MainScreen {
	private Bitmap sward, rock, block;
	private Controler controler;
	private Player player;
	private List<Bomb> bombs;
	private Context context;
	private List<Mob> mobs;
	public int score = 0;
	/**
	 * 生命条数
	 */
	public int pCount = 3; 
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getpCount() {
		return pCount;
	}

	public void setpCount(int pCount) {
		this.pCount = pCount;
	}

	public int[][] getMap() {
		return map;
	}
  
	public void setMap(int[][] map) {
		this.map = map;
	}
	public Random rnd = new Random(); 
	public int frame;
	public boolean t;
	public int[][] map = { 
			{ 0, 0, 1, 1, 0, 2, 0, 0, 0, 0, 1, 1, 1, 0 },
			{ 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 0 },
			{ 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0 },
			{ 0, 2, 1, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 1 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 1, 2, 0, 2, 0, 2, 0, 1, 2, 0, 0, 2, 2, 1 },
			{ 0, 0, 2, 2, 1, 2, 0, 1, 1, 0, 0, 1, 1, 1 },
			{ 0, 1, 1, 0, 0, 1, 0, 1, 0, 0, 2, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 2, 0, 0, 2, 0, 0, 0, 2, 0 },
			{ 0, 2, 0, 2, 0, 0, 1, 1, 0, 0, 0, 2, 0, 0 } };
	public List<M0> ml = new ArrayList<M0>();

	public MainScreen() {

	}

	public MainScreen(Context ct) {
		this.context = ct;
		this.controler = new Controler(ct);
		this.player = new Player(0, 0, ct);
		this.bombs = new ArrayList<Bomb>();
		this.mobs = new ArrayList<Mob>();
		for (int i = 0; i < map.length; i++) {//初始化地图方格块，保存了x,y坐标      rowMax=9,colMax=13;
			for (int j = 0; j < map[0].length; j++) {
				if (map[i][j] == 0) {
					M0 m = new M0(j * 32, i * 32);
					ml.add(m);
				}
			}
		}
		
		//将敌人布局到地图中
		for (int i = 0; i < 6; i++) {
			int x = rnd.nextInt(ml.size());
			int t = rnd.nextInt(3) + 1;//1、2、3：用来表示敌人的类型
			Mob m = new Mob(ml.get(x).getX(), ml.get(x).getY(), ct, t, player);
			m.setMap(map);
			m.setBombs(bombs);//设置炸弹集合
			mobs.add(m);
		}
		
		Resources res = ct.getResources();
		sward = BitmapFactory.decodeResource(res, R.drawable.sward);
		rock = BitmapFactory.decodeResource(res, R.drawable.rock);
		block = BitmapFactory.decodeResource(res, R.drawable.block);
		res = null;
		this.player.setMap(map);
		this.player.setBombs(bombs);
	}

	public synchronized void paint(Canvas c) {
		Canvas canvas = Utils.getCanvas();
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		canvas.drawRect(new Rect(448, 0, 480, 320), paint);
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				if (map[i][j] == 1) {
					canvas.drawBitmap(rock, j * 32, i * 32, paint);
				} else if (map[i][j] == 2) {
					canvas.drawBitmap(block, j * 32, i * 32, paint);
				} else
					canvas.drawBitmap(sward, j * 32, i * 32, paint);
			}
		}
		controler.paint(canvas);
		if (mobs.size() != 0) {
			for (Mob m : mobs) {
				m.paint(canvas);
			}
		}
		player.paint(canvas);
		if (bombs.size() != 0) {
			for (Bomb b : bombs) {
				b.paint(canvas);
			}
		}
		if (pCount <= 0) {
			paint.setTextSize(32);
			paint.setColor(Color.BLACK);
			String endString = "GAME OVER!";
			canvas.drawText(endString, 0, endString.length(), (448 - endString
					.length() * 16) / 2 - 16,
					(320 - endString.length() * 16) / 2 + 50, paint);
			paint.setTextSize(20);
			String tips = "按中间键退出游戏！";
			canvas.drawText(tips, 0, tips.length(),
					(448 - tips.length() * 20) / 2,
					(320 - tips.length() * 10) / 2 + 70, paint);
		}
		if (score >= 600) {
			paint.setTextSize(32);
			paint.setColor(Color.BLACK);
			String endString = "YOU WIN!";
			canvas.drawText(endString, 0, endString.length(), (448 - endString
					.length() * 16) / 2 - 16,
					(320 - endString.length() * 16) / 2 + 50, paint);
			paint.setTextSize(20);
			String tips = "按中间键退出游戏！";
			canvas.drawText(tips, 0, tips.length(),
					(448 - tips.length() * 20) / 2,
					(320 - tips.length() * 10) / 2 + 70, paint);
		}
		paint.setTextSize(12);
		paint.setColor(Color.WHITE);
		canvas.drawText("X" + pCount, 455, 76, paint);
		canvas.drawText("score", 450, 116, paint);
		canvas.drawText(score + "分", 450, 132, paint);
		c.drawBitmap(Utils.mscBitmap, 0, 0, paint);
	}

	public synchronized void update() {
		if (score < 600) {
			if (pCount > 0) {
				if (t) {
					frame++;
					if (frame > 50) {
						frame = 0;
						player.isWudi = false;
						for (Mob mob : mobs) {
							mob.setPlayer(player);
						}
						t = false;
					}
				}
				if (player.isDead) {
					pCount -= 1;
					if (pCount != 0) {
						t = true;
						player = new Player(0, 0, context);
						this.player.setMap(map);
						this.player.setBombs(bombs);
						player.isWudi = true;
					}
				}
				//使用迭代器，可以迭代List,迭代器迭代器允许调用者利用定义良好的语义在迭代期间从迭代器所指向的 collection 移除元素
				Iterator<Mob> it1 = mobs.iterator();
				while (it1.hasNext()) {
					Mob b = it1.next();
					if (!b.isDead) {
						b.update();
					} else {
						it1.remove();
					}
				}
//				controler.update();
			}
			player.update();
		}

		Iterator<Bomb> it = bombs.iterator();
		while (it.hasNext()) {
			Bomb b = it.next();
			if (!b.isend) {
				b.update();
			} else {
				it.remove();
			}
		}
	}

	@SuppressWarnings("all")
	public synchronized boolean onTouchEvent(MotionEvent event) {
		if (pCount > 0 && score < 600) {
			float _x = event.getX();
			float _y = event.getY();
			if (event.getAction() == event.ACTION_DOWN) {
				if (_x > 40 && _x < 80 && _y > 200 && _y < 240) {
					Controler.preD = Controler.direction = "up";
				}
				if (_x > 0 && _x < 40 && _y > 240 && _y < 280) {
					Controler.preD = Controler.direction = "left";
				}
				if (_x > 40 && _x < 80 && _y > 280 && _y < 320) {
					Controler.preD = Controler.direction = "down";
				}
				if (_x > 80 && _x < 120 && _y > 240 && _y < 280) {
					Controler.preD = Controler.direction = "right";
				}
				if (_x > 400 && _x < 442 && _y > 240 && _y < 282) {
					Controler.direction = "bomb";
					Bomb b = new Bomb(player.getX(), player.getY(), context,this);
					b.setMobs(mobs);
					b.setMap(map);
					b.setPlayer(player);
					bombs.add(b);
				}
			}
			if (event.getAction() == event.ACTION_UP) {
				Controler.direction = "none";
			}
		}
		return true;
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_CENTER:
				BoomVariable.GAME_STATE = BoomVariable.TITLE_SCREEN;
			break;
		}
		return true;
	}
}
