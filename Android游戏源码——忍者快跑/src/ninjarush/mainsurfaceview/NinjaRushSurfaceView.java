package ninjarush.mainsurfaceview;

import java.text.AttributedCharacterIterator.Attribute;
import java.util.Vector;

import ninarush.enemy.Enemy;
import ninjarush.bulletclass.Bullet;
import ninjarush.dead.Dead;
import ninjarush.mainactivity.R;
import ninjarush.mainactivity.UserAchieve;
import ninjarush.music.GameMusic;
import ninjarush.relatedclass.Boss;
import ninjarush.relatedclass.GameMap;
import ninjarush.relatedclass.Game_Menu;
import ninjarush.relatedclass.Game_Over;
import ninjarush.relatedclass.Gameing_Bg;
import ninjarush.relatedclass.Loading;
import ninjarush.relatedclass.MyButton;
import ninjarush.relatedclass.Pause;
import ninjarush.relatedclass.Player;
import ninjarush.relatedclass.Tools;
import ninjarushh.food.Food;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class NinjaRushSurfaceView extends SurfaceView implements Runnable,
		SurfaceHolder.Callback {
	private Context context;
	private SurfaceHolder holder;
	private Canvas canvas;
	private Paint paint;
	private BitmapFactory.Options ops;
	// �Ƿ�Ϊ��һ�μ���
	private boolean isFirstLoadLoading;
	private boolean isFirstLoadImage;
	public static boolean flag;// �̱߳��
	public static int status;// ��Ϸ�����״̬
	public static int screenW, screenH;// ��Ļ�Ŀ�͸�
	public static boolean isreset;// �Ƿ�����
	// ���屳������
	private Gameing_Bg gameing_bg;
	private Game_Over gameover;
	// ����BossͼƬ
	public static Vector<Bitmap> vcboss;
	private Bitmap bossBitmap;
	// ����Boss
	private ninjarush.relatedclass.Boss boss;
	//��¼boss����������
	private int bossDeadCount;
	// ���尴ť����
	private MyButton mybutton;
	// ��Ϸ�б�������ͼ���Լ���ͣ���ͷ�쮰�ť
	private Bitmap bmpbg1, bmpbg2, bmpbg3, bmpbulletbnt, bmppause;
	// ��ʼ����Ϸ������ʧ�ܣ�����ͼƬ
	private Bitmap bmpbg;
	// Loading����
	private Bitmap bmploading_bg;
	private Loading loading;
	private Bitmap bmpover_quit_up, bmpover_quit_down, bmpover_try_up,
			bmpover_try_down, bmpover_submit_up, bmpover_submit_down,
			bmpover_achi_up, bmpover_achi_down;// ͼƬ
	// ����
	private Bitmap bmpcloud_0, bmpcloud_1, bmpcloud_2, bmpcloud_3;

	// ---------------------- GameMenu

	private Bitmap bmpinitbg;// GameMenu ����ͼƬ
	private Bitmap bmpmore1;// GameMenu �����ࡱδ����ͼƬ
	private Bitmap bmpmore2;// GameMenu �����ࡱ���±���ͼƬ
	private Bitmap bmpplay_up;// GameMenu ��ʼδ����ͼƬ
	private Bitmap bmpplay_down;// GameMenu ��ʼ����ͼƬ
	private Bitmap bmpachi_up;// GameMenu �ɾ�δ����ͼƬ
	private Bitmap bmpachi_down;// GameMenu �ɾͰ���ͼƬ
	private Bitmap bmpopen_up;// GameMenu GameBoxδ����ͼƬ
	private Bitmap bmpopen_down;// GameMenu GameBox���±���ͼƬ

	private Game_Menu gameMenu;// GameMenu ����

	// ---------------------------Pause
	private Bitmap blackground;// Pause ����ͼƬ
	private Bitmap voice; // Pause ����ͼƬ
	private Bitmap resume;// Pause ����ͼƬ
	private Bitmap quit;// Pause �˳�ͼƬ

	private Pause pause;// Pause ����
	// ///////////////////////
	// ������
	private int count;
	private Vector<GameMap> vcMap;
	private GameMap gameMap;
	// ��
	private Bitmap bam_left, bam_mid, bam_right, bam_under;
	private Bitmap fly_bam_right, fly_bam_mid, fly_bam_left, dre4;
	// ��� ��
	private Bitmap dao, light;
	// ������
	private int style;
	// ���ߵ���ز���----------------------------------------------------------------------------------------------------
	private Bitmap blood, changeToDart, dartMan, hurtBlood, changeff, dartShow,
			bulletMan, rope, ropef, protectDart, leaf, ropeMan;
	private Player player;
	// ���ߵ���ز���-----------------------------------------------------------------------------------------------------
	// 333333333-----------------------------------------
	// �ӵ�
	private Bullet bullet;
	private Vector<Bullet> vetorBullet;
	private Vector<Bullet> vetorBulletBoss;
	private Bitmap bmpBulletPlayer, bmpBulletBoss;
	private int playerBulletCount, BossBulletCount;
	private boolean bulletFire;
	private Rect rect;
	private int BulletKillCount = 0;
	// ��ѻ
	private Bitmap bmpCrow;
	private Bitmap bmpCrowFeather;
	private Bitmap bmpHurtBlood;
	private Vector<Enemy> veEnemy;
	private Vector<Dead> veDead;
	private Bitmap bmpAntDead;
	private Bitmap bmpAnt;
	// ʳ��
	private Bitmap bmpFoodHeart, bmpFoodBullet;
	private Vector<Food> vcFood;

	private int countMusic = 0;// ���ּ�����

	// /////////////////////////
	public NinjaRushSurfaceView(Context context, AttributeSet attribute) {
		super(context, attribute);
		this.context = context;
		holder = getHolder();
		holder.addCallback(this);
		paint = new Paint();
		paint.setColor(Color.WHITE);
		isFirstLoadLoading = true;
		isFirstLoadImage = true;
		// ������ɶ�ͼƬ�ռ�Ŀ���
		ops = new BitmapFactory.Options();
		status = Tools.GAME_MENU;// ��ʼ����Ϸ�����״̬
		flag = true;// ��ʼ���̱߳��
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
	}

	public void myDraw() {
		try {
			canvas = holder.lockCanvas();
			if (canvas != null) {
				switch (status) {
				// ����loading����
				case Tools.GAME_LOADING:
					loading.draw(canvas, paint);
					break;
				// ���� GAME_MENU ����
				case Tools.GAME_MENU:
					gameMenu.draw(canvas, paint);
					break;
				// ����GAME_PLAYING ����
				case Tools.GAME_PLAYING:
					gameing_bg.draw(canvas, paint);
					for (int i = 0; i < veEnemy.size(); i++) {
						veEnemy.elementAt(i).onDraw(canvas, paint);

					}
					mapDraw();
					mybutton.draw(canvas, paint);
					player.draw(canvas, paint);

					// �ӵ�
					for (int i = 0; i < vetorBullet.size(); i++) {
						vetorBullet.elementAt(i).onDraw(canvas, paint);
					}
					// ����Ч��
					for (int i = 0; i < veDead.size(); i++) {
						veDead.elementAt(i).onDraw(canvas, paint);

					}
					// ʳ��
					for (int i = 0; i < vcFood.size(); i++) {
						vcFood.elementAt(i).onDraw(canvas, paint);
					}
					// ����Boss
					if (boss != null) {
						if (boss.isDead() == false) {
							boss.draw(canvas, paint);
						}
					}
					// boss�ӵ�
					if (boss != null) {
						for (int i = 0; i < vetorBulletBoss.size(); i++) {
							vetorBulletBoss.elementAt(i).onDraw(canvas, paint);

						}
					}
					break;
				// ����GAME_OVER ����
				case Tools.GAME_OVER:
					// ������������gameover����
					Game_Over.latestmeter = gameing_bg.getMeter();
					gameover.draw(canvas, paint);
					break;
				// ���� GAME_PAUSE ����
				case Tools.GAME_PAUSE:
					gameing_bg.draw(canvas, paint);
					mapDraw();
					pause.draw(canvas, paint);
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (canvas != null) {
				holder.unlockCanvasAndPost(canvas);
			}
		}

	}

	public void logic() {
		switch (status) {
		// loading�����߼�
		case Tools.GAME_LOADING:
			if (isFirstLoadLoading) {
				initImage();
				isFirstLoadLoading = false;
			}
			init();
			status = Tools.GAME_PLAYING;

			if (countMusic++ == 0) {
				GameMusic.startMusic();// ���� GAME_PLAYING ���� �������֣�
				GameMusic.playSound(R.raw.land, 0);// ����һ����� ��Ч
			} else {
				GameMusic.nextMusic(R.raw.bg);
				GameMusic.playSound(R.raw.land, 0);// ����һ����� ��Ч
			}

			break;
		// GAME_MENU ���� �߼�
		case Tools.GAME_MENU:
			
			gameMenu.logic();
			
			break;
		// GAME_PLAYING �����߼�
		case Tools.GAME_PLAYING:

			gameing_bg.logic();

			// ///////////
			for (int i = 0; i < vcMap.size(); i++) {
				gameMap = vcMap.elementAt(i);
				if (gameMap.isDead()) {
					vcMap.remove(i);
				} else {
					gameMap.logic();
					vcMap.elementAt(i).setYy(gameing_bg.getYy());
				}
			}
			if (vcMap.size() >= 1) {
				if (vcMap.elementAt(vcMap.size() - 1).mapEndX <= screenW
						+ dre4.getWidth()) {
					int num = (int) (Math.random() * 10);
					if (num <= 6) {
						style = Tools.STYLE_LAND;
						gameMap = new GameMap(bam_left, bam_mid, bam_right,
								bam_under, light, screenW, screenH, style,
								false);
						vcMap.add(gameMap);
						// ���ϻ���
						if (vcMap.elementAt(vcMap.size() - 1).getStyle() == Tools.STYLE_LAND) {
							int i = (int) (Math.random() * 5);

							int[] position = { gameMap.getleftX(),
									gameMap.getLeftY(), gameMap.getEndX(),
									gameMap.getLeftY() };
							if (i == 0) {
								veEnemy.add(new Enemy(Tools.ENEMY_DAO, dao,
										position));
							}
							if (i == 1 || i == 4) {
								veEnemy.add(new Enemy(Tools.ENEMY_ANT, bmpAnt,
										position));
							}
							if (i == 3) {
								veEnemy.add(new Enemy(Tools.ENEMY_DAO, dao,
										position));
								veEnemy.add(new Enemy(Tools.ENEMY_ANT, bmpAnt,
										position));
							}
						}
					}

					else {
						style = Tools.STYLE_SKY;
						if (vcMap.elementAt(vcMap.size() - 1).getStyle() == Tools.STYLE_LAND) {
							gameMap = new GameMap(fly_bam_left, fly_bam_mid,
									fly_bam_right, dre4, light, screenW, vcMap
											.elementAt(vcMap.size() - 1)
											.getLeftY(), style, true);
							vcMap.add(gameMap);
						} else {
							gameMap = new GameMap(fly_bam_left, fly_bam_mid,
									fly_bam_right, dre4, light, screenW, vcMap
											.elementAt(vcMap.size() - 1)
											.getLeftY(), style, false);
							vcMap.add(gameMap);
						}
					}
				}
			}
			// ���õ�ͼ�ٶ�
			for (int i = 0; i < vcMap.size(); i++) {
				vcMap.elementAt(i).setSpeed(gameing_bg.getBgspeed() * 5);
			}
			// ���õ����ٶ�
			for (int i = 0; i < veEnemy.size(); i++) {
				if (veEnemy.elementAt(i).getEnemyType() == Tools.ENEMY_DAO) {
					veEnemy.elementAt(i).setSpeed(gameing_bg.getBgspeed() * 5);
				}
				// ����ʳ���ٶ�
				for (int j = 0; j < vcFood.size(); j++) {
					vcFood.elementAt(j).setSpeed(gameing_bg.getBgspeed() * 5);
					vcFood.elementAt(j).setYy(gameing_bg.getYy());
				}
			}
			//�����ӵ�ƫ����
			for (int i = 0; i < vetorBullet.size(); i++) {
				vetorBullet.elementAt(i).setYy(gameing_bg.getYy());
				
			}
			for (int i = 0; i < vetorBulletBoss.size(); i++) {
				vetorBulletBoss.elementAt(i).setYy(gameing_bg.getYy());
				
			}
			// // ////////
			// ���ߵ��߼�����-----------------------------------------------------------------------------------------------------
			player.logic();
			// �ж�������ľ׮����ײ
			for (GameMap e : vcMap) {

				// �ж������Ƿ��ڵ��ϵ���֮��
				if ((player.getPosition()[0] + player.getPosition()[2] >= e
						.getleftX() && player.getPosition()[0] <= e.getEndX())
						&& (player.getPosition()[1] + player.getPosition()[3] >= e
								.getLeftY() && player.getPosition()[1]
								+ player.getPosition()[3] <= e.getLeftY()
								+ Tools.NINJA_SPEED)
						&& (e.getStyle() == Tools.STYLE_LAND || e.getStyle() == Tools.STYLE_START)) {
					if (gameing_bg.getBgspeed() != 0)
						player.setIsUpOfPale(true);
					break;
				} else {
					player.setIsUpOfPale(false);
				}
				// �ж������Ƿ������ϵ���֮��
				if ((player.getPosition()[0] >= e.getleftX() && player
						.getPosition()[0] + player.getPosition()[2] / 2 <= e
							.getEndX()) && e.getStyle() == Tools.STYLE_SKY) {
					player.setIsPaleOnHead(true, gameMap.getSky_y()
							+ fly_bam_left.getHeight() / 2,
							gameing_bg.getBgspeed()*5);
					break;
				} else
					player.setIsPaleOnHead(false, 0, 0);
				// �ж���������ͻ�����ײ
				if ((player.getPosition()[1] + player.getPosition()[3] >= e
						.getLeftY() + Tools.NINJA_SPEED)
						&& (player.getPosition()[1] <= e.getLeftY()
								+ bam_left.getHeight())
						&& (player.getPosition()[0] + player.getPosition()[2] >= e
								.getleftX() && player.getPosition()[0] < e
								.getleftX())
						&& e.getStyle() == Tools.STYLE_LAND) {
					if (gameing_bg.getBgspeed() != 0) {
						player.setIsUpOfPale(false);
						player.setIsPaleOnHead(false, 0, 0);
						player.setStatus(Tools.PLAYERWALK);
						gameing_bg.setBgspeed(0);
						gameing_bg.islogic = false;
					}
				}
				
				// �ж���������׮�ӵ���ײ
				if ((player.getPosition()[1] >= e.getLeftY()
						+ bam_left.getHeight())
						&& (player.getPosition()[0] + player.getPosition()[2] >= e
								.getleftX() + bam_left.getWidth() && player
								.getPosition()[0] < e.getleftX())
						&& e.getStyle() == Tools.STYLE_LAND) {
					player.setIsUpOfPale(false);
					player.setIsPaleOnHead(false, 0, 0);
					if (player.getPlayerStatus() != Tools.PLAYERDART)
						player.setStatus(Tools.PLAYERWALK);
					gameing_bg.islogic = false;
					gameing_bg.setBgspeed(0);
//					break;
				}
				// �����Ļֹͣ�ˣ���������������ľ׮֮�ϣ���Ҫ����Ļ�����ƶ�
				if (gameing_bg.getBgspeed() == 0
						&& (player.getPosition()[1] + player.getPosition()[3] <= e
								.getLeftY())
						&& (player.getPosition()[0] + player.getPosition()[2] <= e
								.getleftX() + Tools.LATERBGSPEED * 3)) {
					gameing_bg.setBgspeed(Tools.PREBGSPEED*3);
					gameing_bg.islogic = true;
				}
			}
			

			if (player.getIsPlayerDead()) {
				boss=null;
				GameMusic.playSound(R.raw.dead, 0);// ����������Ч
				GameMusic.pauseRun();
				GameMusic.pauseSound(Tools.sound_wind);
				GameMusic.pauseMusic();// ��ͣ��Ϸ�б�������
				GameMusic.pauseWind();//��ͣ����
				status = Tools.GAME_OVER;
			}

			// ���ߵ��߼�����-----------------------------------------------------------------------------------------------------
			// 33333333333333333-------------------------------------------------------------
			// �����ӵ�
			if (player.getIsShootMore()) {
				vetorBullet.add(new Bullet(Tools.BULLETPLAYERWUDIAFTER, player
						.getPosition()[0], player.getPosition()[1],
						bmpBulletPlayer));
				player.setIsShootMore(false);
			}
			if (bulletFire && player.getPlayerStatus() != Tools.PLAYERDART) {
				GameMusic.playSound(R.raw.shoot, 0);
				vetorBullet.add(new Bullet(Tools.BULLETPLAYER, player
						.getPosition()[0] + player.getPosition()[0] / 2, player
						.getPosition()[1] + ropeMan.getHeight() / 2,
						bmpBulletPlayer));
				bulletFire = false;
			}
			// �����ӵ��Ƴ��������߼�����boss
			for (int i = 0; i < vetorBullet.size(); i++) {
				Bullet b = vetorBullet.elementAt(i);
				if (boss != null) {
					int[] position1 = { b.getBulletX(), b.getBulletY(),
							b.getBmpW(), b.getBmpH() };
					if (Tools.isCollision(position1, boss.getPosition())) {
						if (b.isFirstCollision()==false) {
							GameMusic.playSound(R.raw.boss_hurt, 0);// boss �յ�����
																	// ��Ч
							Dead d = new Dead(Tools.DEAD_BOSS, b.getBulletX(),
									boss.getPosition()[1]
											+ boss.getPosition()[3] / 2,
									bmpHurtBlood);
							veDead.add(d);
							b.isBulletIsDead();
							boss.setHp(boss.getHp() - 1);
							b.setFirstCollision(true);
						}
						if (boss.getHp() <= -1) {
							bossDeadCount++;
							if(bossDeadCount==1)
								UserAchieve.userAchieve[7]=1;
							if(bossDeadCount==3)
								UserAchieve.userAchieve[8]=1;
							GameMusic.playSound(R.raw.boss_die, 0);
							boss.setDead(true);
							boss = null;
							player.setIsProtectDart();
						}
					}
				}
				if (b.isBulletIsDead()) {
					if (b.getCount() > BulletKillCount) {
						BulletKillCount = b.getCount();
						if (BulletKillCount == 5)
							UserAchieve.userAchieve[5] = 1;
						if (BulletKillCount == 15)
							UserAchieve.userAchieve[6] = 1;
					}
					vetorBullet.removeElementAt(i);
				} else {
					b.logic();
				}
			}
			// ��ѻ

			if (gameing_bg.getMeter() % 100 == 0) {
				Enemy enemy = new Enemy(Tools.ENEMY_CROW, bmpCrow);
				veEnemy.add(enemy);
				enemy = null;
			}
			for (int i = 0; i < veEnemy.size(); i++) {
				Enemy c = veEnemy.elementAt(i);
				if (c.isEnemyIsDead()) {
					veEnemy.remove(i);
				} else {

					if (c.getEnemyType() != Tools.ENEMY_CROW) {
						c.setYy(gameing_bg.getYy());
					}
					if (c.getEnemyType() == Tools.ENEMY_ANT) {
						c.setSpeed(gameing_bg.getBgspeed() * 5);
					}
					c.logic();
				}
			}
			// ��ײ

			for (int j = 0; j < veEnemy.size(); j++) {
				Enemy c = veEnemy.elementAt(j);
				// �ӵ�
				for (int i = 0; i < vetorBullet.size(); i++) {
					Bullet b = vetorBullet.elementAt(i);
					int[] position1 = { c.getEnemyX(), c.getEnemyY(),
							c.getEnemyW(), c.getEnemyH() };
					int[] position2 = { b.getBulletX(), b.getBulletY(),
							b.getBmpW(), b.getBmpH() };
					if (Tools.isCollision(position1, position2)) {
						b.setCount();
						if (c.getEnemyType() == Tools.ENEMY_CROW) {
							GameMusic.playSound(R.raw.crow_hurt, 0);// ��ѻ��������Ч
							Dead e = new Dead(Tools.DEAD_CROW, c.getEnemyX(),
									c.getEnemyY(), bmpCrow, bmpHurtBlood,
									bmpCrowFeather);
							c.setEnemyIsDead(true);
							veDead.add(e);
							continue;
						}
						if (c.getEnemyType() == Tools.ENEMY_ANT) {
							GameMusic.playSound(R.raw.enemy_die, 0);
							Dead e = new Dead(Tools.DEAD_ANT, c.getEnemyX(),
									c.getEnemyY(), bmpHurtBlood, bmpAntDead);
							veDead.add(e);
							c.setEnemyIsDead(true);
							continue;
						}
					}
				}
				// �����޵������
				if (player.getIsUndead()) {
					int[] position1 = { c.getEnemyX(), c.getEnemyY(),
							c.getEnemyW(), c.getEnemyH() };
					if (Tools.isCollision(position1, player.getPosition())) {
						if (c.getEnemyType() == Tools.ENEMY_CROW) {
							Dead e = new Dead(Tools.DEAD_CROW, c.getEnemyX(),
									c.getEnemyY(), bmpCrow, bmpHurtBlood,
									bmpCrowFeather);
							c.setEnemyIsDead(true);
							veDead.add(e);
						}
						if (c.getEnemyType() == Tools.ENEMY_ANT) {
							Dead e = new Dead(Tools.DEAD_ANT, c.getEnemyX(),
									c.getEnemyY(), bmpHurtBlood, bmpAntDead);
							veDead.add(e);
							c.setEnemyIsDead(true);
						}
					}
				}
			}
			// ����Ч��
			for (int i = 0; i < veDead.size(); i++) {
				Dead d = veDead.elementAt(i);
				if (d.isDeadIsOver()) {
					veDead.remove(i);
				} else {
					d.logic();
				}

			}

			// ʳ��350,250
			if (gameing_bg.getMeter() %250 == 0) {
				Food f = new Food(bmpFoodHeart, Tools.FOODHEART,
						gameMap.getLeftY());
				vcFood.add(f);

			}
			if (gameing_bg.getMeter() % 150 == 0) {
				Food f1 = new Food(bmpFoodBullet, Tools.FOODBULLET,
						gameMap.getLeftY());
				vcFood.add(f1);

			}
			for (int i = 0; i < vcFood.size(); i++) {
				Food f = vcFood.elementAt(i);
				if (f.isFoodIsDead()) {
					vcFood.remove(i);
				} else {
					f.logic();
				}
			}
			// ������������ײ
			for (Enemy e : veEnemy) {
				int position[] = { e.getEnemyX(), e.getEnemyY(), e.getEnemyW(),
						e.getEnemyH() };
				if (Tools.isCollision(position, player.getPosition())) {
					if (!e.getIsCollision()) {
						if(player.getIsUndead()){
						if (e.getEnemyType() == Tools.ENEMY_CROW) {
							GameMusic.playSound(R.raw.crow_hurt, 0);// ���� �޵�״̬  ��ѻ �յ����� ��Ч
						} else if (e.getEnemyType() == Tools.ENEMY_ANT) {
							GameMusic.playSound(R.raw.enemy_die, 0);// ���� �޵�״̬	 ���� �ܵ����� ��Ч
						}
						}
						player.setIsHurt();
						e.setIsCollision(true);
					}
					if (player.getIsUndead())

						e.setEnemyIsDead(true);
				}
			}
			// ʳ����������ײ
			for (Food f : vcFood) {
				int position[] = { f.getFoodX(), f.getFoodY(), f.getFoodW(),
						f.getFoodH() };
				if (Tools.isCollision(position, player.getPosition())) {
					switch (f.getFoodType()) {
					case Tools.FOODHEART:
						player.eatFood(Tools.FOODHEART);
						f.setFoodIsDead(true);
						break;

					case Tools.FOODBULLET:
						player.eatFood(Tools.FOODBULLET);
						f.setFoodIsDead(true);
						break;
					}
				}
			}
			// Boss�߼�
			if (gameing_bg.getMeter() % 1000 == 0) {
				boss = new Boss(vcboss);
			}
			if (boss != null) {
				boss.logic();
				BossBulletCount++;
				if (BossBulletCount % 20 == 0) {
					vetorBulletBoss.add(new Bullet(Tools.BULLETBOSS, screenW
							- boss.getPosition()[2], boss.getPosition()[1]
							+ boss.getPosition()[3] * 2 / 3, bmpBulletBoss));
				}

				for (int i = 0; i < vetorBulletBoss.size(); i++) {
					Bullet b = vetorBulletBoss.elementAt(i);
					if (b.isBulletIsDead()) {
						vetorBulletBoss.remove(i);
					} else {
						b.logic();
					}
				}

			}
			// boss�ӵ���������ײ
			for (int i = 0; i < vetorBulletBoss.size(); i++) {
				Bullet b = vetorBulletBoss.elementAt(i);
				int[] position1 = { b.getBulletX(), b.getBulletY(),
						b.getBmpW(), b.getBmpH() };
				if (Tools.isCollision(position1, player.getPosition())) {
					if (!b.isIsCollision()) {
						player.setIsHurt();
						b.setIsCollision(true);
					}
				}
			}
			break;
		// GAME_OVER �����߼�
		case Tools.GAME_OVER:
			gameover.logic();
			break;
		// GAME_PAUSE �����߼�
		case Tools.GAME_PAUSE:
			break;
		}
	}

	public void surfaceCreated(SurfaceHolder holder) {
		screenW = this.getWidth();// ��ȡ��Ļ�Ŀ�
		screenH = this.getHeight();// ��ȡ��Ļ�ĸ�
		bossDeadCount=0;
		
		if (ops == null) {
			ops = new BitmapFactory.Options();
		}
		if (isFirstLoadImage) {
			initLoading();
			// GameMusic.startMusic();// ��������
			isFirstLoadImage = false;
			// ////////////
			vcMap = new Vector<GameMap>();
			gameMap = new GameMap(bam_left, bam_mid, bam_right, bam_under, 0,
					screenH - bam_under.getHeight() / 2, Tools.STYLE_START);
			vcMap.add(gameMap);
			// //////////////
			new Thread(this).start();// ������Ϸ�߳�
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	public void surfaceDestroyed(SurfaceHolder holder) {

	}

	public void run() {
		while (flag) {
			long start = System.currentTimeMillis();
			myDraw();
			logic();
			long end = System.currentTimeMillis();
			try {
				if (end - start <= 100)
					Thread.sleep(100 - end + start);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	// ���γ�ʼ�����Ĳ���
	public void initLoading() {
		// ��ʼ����Ϸ�ĳɾ�����
		UserAchieve.userAchieve = UserAchieve.getAchive(context);
		// /// map ��ͼ
		bam_left = BitmapFactory.decodeResource(getResources(),
				R.drawable.bam_left, ops);
		bam_mid = BitmapFactory.decodeResource(getResources(),
				R.drawable.bam_mid, ops);
		bam_right = BitmapFactory.decodeResource(getResources(),
				R.drawable.bam_right, ops);
		bam_under = BitmapFactory.decodeResource(getResources(),
				R.drawable.bam_under, ops);
		fly_bam_right = BitmapFactory.decodeResource(getResources(),
				R.drawable.fly_bam_right, ops);
		fly_bam_mid = BitmapFactory.decodeResource(getResources(),
				R.drawable.fly_bam_mid, ops);
		fly_bam_left = BitmapFactory.decodeResource(getResources(),
				R.drawable.fly_bam_left, ops);
		dre4 = BitmapFactory.decodeResource(getResources(), R.drawable.dre4,
				ops);
		light = BitmapFactory.decodeResource(getResources(), R.drawable.light,
				ops);
		dao = BitmapFactory.decodeResource(getResources(), R.drawable.dao, ops);
		// ���� GameMenu �����е�ͼƬ
		bmpinitbg = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.initbg, ops);
		bmpplay_up = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.play_up, ops);
		bmpplay_down = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.play_down, ops);
		bmpachi_up = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.achi_up, ops);
		bmpachi_down = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.achi_down, ops);
		bmpopen_up = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.over_quit_up, ops);
		bmpopen_down = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.over_quit_down, ops);
		bmpmore1 = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.more1, ops);
		bmpmore2 = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.more2, ops);
		// �ڼ������˲����Ժ�ʵ����GameMenu ����
		gameMenu = new Game_Menu(bmpinitbg, bmpmore1, bmpmore2, bmpplay_up,
				bmpplay_down, bmpachi_up, bmpachi_down, bmpopen_up,
				bmpopen_down, context);

		// ��ʼ��loading����
		bmploading_bg = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.loading, ops);
		loading = new Loading(bmploading_bg);

	}

	// ��ʼ��ͼƬ����
	public void initImage() {
		// ��ʼ����Ϸ�б���ͼƬ
		bmpbg1 = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.bg1, ops);
		bmpbg2 = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.bg2, ops);
		bmpbg3 = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.bg3, ops);
		// ��ʼ����Ϸ����ʱ�ı���
		bmpbg = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.b_page, ops);
		bmpover_achi_down = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.over_achi_down, ops);
		bmpover_achi_up = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.over_achi_up, ops);
		bmpover_quit_down = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.over_quit_down, ops);
		bmpover_quit_up = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.over_quit_up, ops);
		bmpover_submit_down = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.over_submit_down, ops);
		bmpover_submit_up = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.over_submit_up, ops);
		bmpover_try_down = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.over_try_down, ops);
		bmpover_try_up = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.over_try_up, ops);
		// ��Ϸ�е���ͣ���ͷ��ڵİ�ť
		bmpbulletbnt = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.dartbtn, ops);
		bmppause = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.pausebt, ops);
		// ��Ϸ�еİ���
		bmpcloud_0 = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.cloud_0, ops);
		bmpcloud_1 = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.cloud_1, ops);
		bmpcloud_2 = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.cloud_2, ops);
		bmpcloud_3 = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.cloud_3, ops);

		// ���� Pause �����е�ͼƬ
		blackground = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.pausepanel, ops);
		voice = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.voice, ops);
		resume = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.resume, ops);
		quit = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.quit, ops);
		// ���ߵ���ز���----------------------------------------------------------------------------------------------------
		blood = BitmapFactory.decodeResource(getResources(), R.drawable.blood,
				ops);
		changeToDart = BitmapFactory.decodeResource(getResources(),
				R.drawable.changetodart, ops);
		dartMan = BitmapFactory.decodeResource(getResources(),
				R.drawable.dartman, ops);
		hurtBlood = BitmapFactory.decodeResource(getResources(),
				R.drawable.hurtblood, ops);
		changeff = BitmapFactory.decodeResource(getResources(),
				R.drawable.changeff, ops);
		dartShow = BitmapFactory.decodeResource(getResources(),
				R.drawable.dartshow, ops);
		rope = BitmapFactory.decodeResource(getResources(), R.drawable.rope,
				ops);
		ropef = BitmapFactory.decodeResource(getResources(), R.drawable.ropef,
				ops);
		protectDart = BitmapFactory.decodeResource(getResources(),
				R.drawable.protectdart, ops);
		leaf = BitmapFactory.decodeResource(getResources(), R.drawable.leaf,
				ops);
		ropeMan = BitmapFactory.decodeResource(getResources(),
				R.drawable.ropeman, ops);
		bulletMan = BitmapFactory.decodeResource(getResources(),
				R.drawable.bulletman, ops);
	}

	// ��ʼ��ʵ������
	public void init() {
		// �Ƿ�λ����
		Pause.isJingYin = false;
		// ʵ����������
		gameing_bg = new Gameing_Bg(bmpbg1, bmpbg2, bmpbg3, bmpcloud_0,
				bmpcloud_1, bmpcloud_2, bmpcloud_3);
		// ʵ�����Ŷ���
		vcMap = new Vector<GameMap>();
		gameMap = new GameMap(bam_left, bam_mid, bam_right, bam_under, 0,
				screenH - bam_under.getHeight() / 2, Tools.STYLE_START);
		vcMap.add(gameMap);

		// ʵ������ť �������ͣ
		mybutton = new MyButton(bmpbulletbnt, bmppause);
		pause = new Pause(blackground, voice, resume, quit);
		// ʵ������Ϸ����Over����
		gameover = new Game_Over(bmpbg, bmpover_quit_up, bmpover_quit_down,
				bmpover_try_up, bmpover_try_down, bmpover_submit_up,
				bmpover_submit_down, bmpover_achi_up, bmpover_achi_down,
				context);
		// ���ߵ���ز���-----------------------------------------------------------------------------------------------------
		player = new Player(blood, changeToDart, dartMan, hurtBlood, changeff,
				dartShow, rope, ropef, protectDart, leaf, ropeMan, bulletMan);
		// 333333333-----------------------------------
		// �ӵ���س�ʼ��
		vetorBullet = new Vector<Bullet>();
		vetorBulletBoss = new Vector<Bullet>();
		bmpBulletPlayer = BitmapFactory.decodeResource(getResources(),
				R.drawable.weapon, ops);
		bmpBulletBoss = BitmapFactory.decodeResource(getResources(),
				R.drawable.attackfire, ops);
		bulletFire = false;
		rect = new Rect(mybutton.getBulletX(), mybutton.getBulletY(),
				mybutton.getBulletX() + mybutton.bulletW, mybutton.getBulletY()
						+ mybutton.getBulletH());
		BossBulletCount = 1;
		// ��ѻ
		bmpCrow = BitmapFactory.decodeResource(getResources(), R.drawable.crow,
				ops);
		bmpCrowFeather = BitmapFactory.decodeResource(getResources(),
				R.drawable.crowfeather, ops);
		bmpHurtBlood = BitmapFactory.decodeResource(getResources(),
				R.drawable.hurtblood, ops);
		veEnemy = new Vector<Enemy>();
		veDead = new Vector<Dead>();
		bmpAntDead = BitmapFactory.decodeResource(getResources(),
				R.drawable.antdead, ops);
		bmpAnt = BitmapFactory.decodeResource(getResources(), R.drawable.enemy,
				ops);
		// ʳ��
		bmpFoodHeart = BitmapFactory.decodeResource(getResources(),
				R.drawable.foodblood, ops);
		bmpFoodBullet = BitmapFactory.decodeResource(getResources(),
				R.drawable.dartfood, ops);
		vcFood = new Vector<Food>();
		// ��ʼ��bossͼƬ
		vcboss = new Vector<Bitmap>();
		for (int i = 0; i < 8; i++) {
			bossBitmap = BitmapFactory.decodeResource(this.getResources(),
					R.drawable.boss_0 + i);
			vcboss.add(bossBitmap);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (status) {
		case Tools.GAME_MENU:
			gameMenu.onTouchEvent(event);// GameMenu ���� �����¼�
			break;
		case Tools.GAME_PLAYING:
			if (!mybutton.onTouchEvent(event) && !player.getIsJumpTwice()
					&& !player.getIsScreenDown()) {
				// Log.e("",
				// !mybutton.onTouchEvent(event)+","+!player.getIsJumpTwice()+","+!player.getIsDrawRope()+"");
				gameing_bg.onTouchEvent(event, player.getPosition()[1]);
			}
			// ����ǰ������������ͣ����������ʾ
			Pause.currentmeter = gameing_bg.getMeter();
			mybutton.onTouchEvent(event);// ��ͣ����������ť�����¼�
			player.ontouch(event, mybutton.getPosition(),
					mybutton.getPositionLeftRound());
			// //////33333����
			if (inRect((int) event.getX(), (int) event.getY(), rect)) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					bulletFire = true;
				} else {
					bulletFire = false;
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					bulletFire = false;
				}
			}
			break;
		case Tools.GAME_OVER:
			gameover.onTouchEvent(event);
			break;
		case Tools.GAME_PAUSE:

			pause.onTouchEvent(event);// GamePause ���� �����¼�
			break;
		}
		return true;
		// /////////////////////////////////////////////////////////////////////

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP)
			player.eatFood(Tools.FOODHEART);
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
			player.setIsProtectDart();
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
			player.eatFood(Tools.FOODBULLET);

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			switch (status) {
			// GAME_MENU ���� �߼�
			case Tools.GAME_MENU:
				NinjaRushSurfaceView.flag = false;
				GameMusic.releaseMusic();// �ͷű���������Դ
				GameMusic.releaseRun();//�ͷ� run���� 
				GameMusic.releaseWind();//�ͷ� ������Դ
				
				return super.onKeyDown(keyCode, event);
				// GAME_PLAYING �����߼�
			case Tools.GAME_PLAYING:
				GameMusic.pauseMusic();//��ͣ ��������
				GameMusic.pauseWind();
				GameMusic.pauseRun();
				NinjaRushSurfaceView.status = Tools.GAME_MENU;
				init();
				// System.out.println(Tools.GAME_PLAYING+"  Tools.GAME_PLAYING");
				break;
			// GAME_OVER �����߼�
			case Tools.GAME_OVER:
				NinjaRushSurfaceView.status = Tools.GAME_MENU;
				break;
			// GAME_PAUSE �����߼�
			case Tools.GAME_PAUSE:
				NinjaRushSurfaceView.status = Tools.GAME_MENU;
				init();
				break;
			}
		}

		return true;
	}

	private void mapDraw() {
		for (int i = 0; i < vcMap.size(); i++) {
			GameMap map = vcMap.elementAt(i);
			map.draw(canvas, paint);
		}
	}

	private boolean inRect(int touchX, int touchY, Rect rect) {
		return rect.contains(touchX, touchY);
	}
}
