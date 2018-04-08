package com.gw.android.boom.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.gw.android.boom.R;
/**
 * ������
 */
public class Mob {
	private int x, y;
	private int[][] map;
	private int type;
	private Bitmap foemans1, foemans2, foemans3;
	private String direction = "up";
	private Player player;
	public boolean isDead;
	public int frame;
	private List<Bomb> bombs;
	Random rnd = new Random();
	public static String UP="up";
	public static String DOWN="down";
	public static String LEFT="left";
	public static String RIGHT="right";
	public List<Bomb> getBombs() {
		return bombs;
	}

	public void setBombs(List<Bomb> bombs) {
		this.bombs = bombs;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int[][] getMap() {
		return map;
	}

	public void setMap(int[][] map) {
		this.map = map;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Mob() {

	}

	public Mob(int x, int y, Context ct, int type, Player p) {
		this.player = p;
		int d = rnd.nextInt(8);
		
		//�˶θ�����û����
		/*if (d < 2) {	
			direction = "up";
		}
		if (d < 4) {
			direction = "left";
		}
		if (d < 6) {
			direction = "down";
		}
		if (d < 8) {
			direction = "right";
		}*/
		
		
		this.x = x;
		this.y = y;
		this.type = type;
		Resources res = ct.getResources();
		foemans1 = BitmapFactory.decodeResource(res, R.drawable.foemans1);
		foemans2 = BitmapFactory.decodeResource(res, R.drawable.foemans2);
		foemans3 = BitmapFactory.decodeResource(res, R.drawable.foemans3);
		res = null;
	}

	public void paint(Canvas c) {
		if (!isDead) {
			if (type == 1) {
				c.drawBitmap(foemans1, x, y, null);
			}
			if (type == 2) {
				c.drawBitmap(foemans2, x, y, null);
			}
			if (type == 3) {
				c.drawBitmap(foemans3, x, y, null);
			}
		}
	}
	/**
	 * �жϹ����������û��ը�� �������ܷ��ƶ����ж�
	 */
	public boolean canMove(String d) {
		for (Bomb bomb : bombs) {
			if (d.equals("up") && (y - 32 == bomb.getPy() && x == bomb.getPx())) {
				return false;
			}
			if (d.equals("down")
					&& (y + 32 == bomb.getPy() && x == bomb.getPx())) {
				return false;
			}
			if (d.equals("left")
					&& (x - 32 == bomb.getPx() && y == bomb.getPy())) {
				return false;
			}
			if (d.equals("right")
					&& (x + 32 == bomb.getPx() && y == bomb.getPy())) {
				return false;
			}
		}
		return true;
	}
	/**
	 * �������
	 */
	public void update() {
		/*
		 * ը�����ڷ��޵�״̬�µĵ���ײ���
		 */
		if (!player.isWudi) {
			if (x == player.getX() && y == player.getY()) {
				player.isDead = true;
			}
		}
		/*
		 *�����ƶ�
		 */
		if (!isDead) {
			frame++;
			if (frame % 10 == 0) {
				if (frame > 500)
				{
					frame = 0;
				}
				/*
				 * ��: �õ���λ��
				 * �ڣ�����λ����Χ�����ߵĿ�λ
				 * �ۣ�����Ĵӿ�λ�г�ȡһ��λ�ý����ƶ�
				 */
				int row=y/32;
				int col=x/32;
				if(row==8){
					System.out.println("row="+row);
				}
				if(col==13){
					System.out.println("col"+col);
				}
				/*
				 * ֱ���ų�һЩ�����ߵĿ����ԣ��ĸ��ǵ����
				 * row==0����������
				 * col==0����������
				 * row==map.length:��������
				 * col==map[0].length:��������
				 */
				//�������ߵ�������ý���ƶ����⣺���ڲ����ߵ����̫�࣬���ƶ������ֻ��һ�֣����ǵ�ͼ���Ϊ��0ʱ�Ϳ����ƶ�\
				
				List<String> list=new ArrayList<String>();
				try {
					if((row!=0)&&(map[row-1][col]==0)){//����
						direction="up";
						list.add(UP);
						System.out.println("up");
					}
					if((row!=map.length)&&(map[row+1][col]==0)){//����
						direction="down";
						list.add(DOWN);
						System.out.println("down");
					}
					if((col!=0)&&(map[row][col-1]==0)){//����
						direction="left";
						list.add(LEFT);
						System.out.println("left");
					}
					if((col!=map[0].length)&&(map[row][col+1]==0)){//����
						direction="right";
						list.add(RIGHT);
						System.out.println("right");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				int randDir=rnd.nextInt(list.size());
				if(list.get(randDir).equals(UP)){
					y-=32;
				}else if(list.get(randDir).equals(DOWN)){
					y+=32;
				}else if(list.get(randDir).equals(LEFT)){
					x-=32;
				}else if(list.get(randDir).equals(RIGHT)){
					x+=32;
				}
			}
		}
	}
}
