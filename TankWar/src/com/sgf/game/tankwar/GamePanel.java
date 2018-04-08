package com.sgf.game.tankwar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

public class GamePanel extends View {
	
	public static final int UNIT=10;
	
	private static final int tankIntalizeWidth=30;
	
	private static final int tankIntalizeHeight=50;
	
	public static final int LEFT=4;
	
	public static final int RIGHT=2;
	
	public static final int UP=1;
	
	public static final int DOWN=3;
	
	private static  int DBY_X=110;
	
	private static  int DBY_Y=340;
	
	private Bitmap dby;
	private Tank myTank=null;
	protected int height;
	protected int width;
	private String status="STOP";
	private int row;
	private int column;
	protected List<Shells> shellsList=new ArrayList<Shells>();
	protected List<Tank> tankList=new ArrayList<Tank>();
	private List<Wall> wallList=new ArrayList<Wall>();
	private Random tankRandom=new Random();
	
	private Handler gameHandler=new Handler();
	
	private Runnable drawThread=new  Runnable(){

		@Override
		public void run() {
			update();
		}
		
	};
	
	public  static int map[][];//0表示地图空闲，1表示地图被坦克占用，2表示地图被墙占用，3表示地图被大本营占用
	
	private AlertDialog dialog;
	
	private TableLayout msgPanel;
	
	
	public GamePanel(Context context) {
		super(context);
	}
	public GamePanel(Context context,AttributeSet attrs){
		 super(context, attrs);
		 dby=((BitmapDrawable)getResources().getDrawable(R.drawable.dby)).getBitmap();
		
	}
	public void setDialog(AlertDialog ad){
		this.dialog=ad;
	}

	public void setMsgPanel(TableLayout msgPanel){
		this.msgPanel=msgPanel;
	}
	public void increasePanelMsg(int keyId){
		TextView tv=(TextView)msgPanel.findViewById(keyId);
		tv.setText(String.valueOf(Integer.valueOf(tv.getText().toString())+1));
	}
	@Override
	protected void onDraw(Canvas canvas) {
		myTank.drawTank(canvas);
		int i=shellsList.size();
		for(int j=0;j<i;j++){
			shellsList.get(j).drawShells(canvas);
		}
		i=tankList.size();
		for(int j=0;j<i;j++){
			tankList.get(j).drawTank(canvas);
		}
		i=wallList.size();
		for(int j=0;j<i;j++){
			wallList.get(j).drawWall(canvas);
		}
		canvas.drawBitmap(dby, DBY_X, DBY_Y, null);
	}
	//初始化墙
	private void initializMap(){
		//初始化大本营
		 for(int i=DBY_Y/UNIT;i<DBY_Y/UNIT+dby.getHeight()/UNIT;i++){
			 for(int j=DBY_X/UNIT;j<DBY_X/UNIT+dby.getWidth()/UNIT;j++){
				 map[i][j]=3;
			 }
		 }
		for(int i=(DBY_Y-2*UNIT)/UNIT;i<(DBY_Y+dby.getHeight())/UNIT;i++){
			for(int j=(DBY_X-2*UNIT)/UNIT;j<(DBY_X+dby.getWidth()+2*UNIT)/UNIT;j++){
				if(map[i][j]==3)
					continue;
				map[i][j]=2;
				wallList.add(new Wall(i,j));
			}
		}
		//以下为初始化地图
		for(int i=10;i<15;i++){
			for(int j=0;j<6;j++){
				if(map[i][j]==2)
					continue;
				map[i][j]=2;
				wallList.add(new Wall(i,j));
			}
			
			for(int j=17;j<column;j++){
				if(map[i][j]==2)
					continue;
				map[i][j]=2;
				wallList.add(new Wall(i,j));
			}
		}
		
		for(int i=20;i<25;i++){
			for(int j=0;j<6;j++){
				if(map[i][j]==2)
					continue;
				map[i][j]=2;
				wallList.add(new Wall(i,j));
			}
			
			for(int j=17;j<column;j++){
				if(map[i][j]==2)
					continue;
				map[i][j]=2;
				wallList.add(new Wall(i,j));
			}
		}
		
		for(int i=16;i<19;i++){
			for(int j=10;j<16;j++){
				if(map[i][j]==2)
					continue;
				map[i][j]=2;
				wallList.add(new Wall(i,j));
			}
		}
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		height=h;
		width=w;
		row=height/GamePanel.UNIT;
		column=width/GamePanel.UNIT;
		myTank=new Tank((BitmapDrawable)getResources().getDrawable(R.drawable.tank),new Point((column/2-8)*UNIT,(row-3)*UNIT),0,UP,h,w);
		DBY_X=((column/2)-2)*UNIT;
		DBY_Y=row*UNIT-dby.getHeight();
		map=new int[row+1][column+1];
		Log.i("System.out", h+":"+w);
		initializMap();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		//Log.i("System", "shells's number:"+shellsList.size());
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			if(!status.equals("STOP")){
				myTank.moveRight();
			}
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			if(!status.equals("STOP")){
				myTank.moveLeft();
			}
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
			if(!status.equals("STOP")){
				myTank.moveUp();	
			}
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			if(!status.equals("STOP")){
				myTank.moveDown();
			}
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:
			if(status.equals("STOP")){
				status="START";
				gameHandler.post(drawThread);
			}else{
				shellsList.add(myTank.fire());
			}
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void update(){
		long begin=System.currentTimeMillis();
		int i=shellsList.size();
		Shells shells;
		for(int j=0;j<i;j++){
			if(status.equals("STOP"))
				break;
			shells=shellsList.get(j);
			if(shells.direction==UP){
				if(shells.getPoint().y<=0){
					if(GameFactory.shellsFactory.size()<GameFactory.SHELLS_FACTORY_SIZE){
						GameFactory.shellsFactory.push(shells);
					}
					shellsList.remove(j--);
					i--;
					continue;
				}
			}else if(shells.direction==DOWN){
				if(shells.getPoint().y>=height){
					if(GameFactory.shellsFactory.size()<GameFactory.SHELLS_FACTORY_SIZE){
						GameFactory.shellsFactory.push(shells);
					}
					shellsList.remove(j--);
					i--;
					continue;
				}
			}else if(shells.direction==LEFT){
				if(shells.getPoint().x<=0){
					if(GameFactory.shellsFactory.size()<GameFactory.SHELLS_FACTORY_SIZE){
						GameFactory.shellsFactory.push(shells);
					}
					shellsList.remove(j--);
					i--;
					continue;
				}
			}else if(shells.direction==RIGHT){
				if(shells.getPoint().x>=width){
					if(GameFactory.shellsFactory.size()<GameFactory.SHELLS_FACTORY_SIZE){
						GameFactory.shellsFactory.push(shells);
					}
					shellsList.remove(j--);
					i--;
					continue;
				}
			}
			if(juageHitWall(shells)){
				if(GameFactory.shellsFactory.size()<GameFactory.SHELLS_FACTORY_SIZE){
					GameFactory.shellsFactory.push(shells);
				}
				shellsList.remove(j--);
				i--;
			}
			shells.move();//子弹继续移动
		}
		if(status.equals("START")){
			randomTank();
			postInvalidate();
			gameHandler.postDelayed(drawThread, 100);
		}
		Log.i("System.out",String.valueOf(System.currentTimeMillis()-begin));
	}
	
	public void randomTank(){
		if(tankRandom.nextInt(100)%10==0){
			if(tankList.size()<3&&map[tankIntalizeHeight/UNIT][tankIntalizeWidth/UNIT]==0){
				tankList.add(new Tank((BitmapDrawable)getResources().getDrawable(R.drawable.tank_d),new Point(tankIntalizeWidth,tankIntalizeHeight), 1,DOWN,height,width));
			}
		}
		Iterator<Tank> it=tankList.iterator();
		while(it.hasNext()){
			Tank t=it.next();
			int r=tankRandom.nextInt(100)%10;
			switch (r) {
			case 0:
				t.moveRight();
				break;
			case 1:
				t.moveLeft();
				break;
			case 2:
				t.moveUp();	
				break;
			case 3:
				t.moveDown();
				break;
			case 4:
				shellsList.add(t.fire());
				break;
			default:
				t.move();
			}
		}
	}
	/**
	 * 判断炮弹是否击中墙：
	 * 根据炮弹的运行方向，动态计算炮弹的地图占位，利用地图占位来判断是否击中墙
	 * @param s
	 * @return
	 */
	public boolean juageHitWall(Shells s){
		int i=0,j=0;
		if(s.direction==UP){
			i=(s.getPoint().getX()-s.radius)/UNIT;
			j=(s.getPoint().getY()-s.radius)/UNIT;
		}else if(s.direction==DOWN){
			i=(s.getPoint().getX()-s.radius)/UNIT;
			j=(s.getPoint().getY()+s.radius)/UNIT;
		}else if(s.direction==RIGHT){
			i=(s.getPoint().getX()+s.radius)/UNIT;
			j=(s.getPoint().getY()-s.radius)/UNIT;
		}else if(s.direction==LEFT){
			i=(s.getPoint().getX()-s.radius)/UNIT;
			j=(s.getPoint().getY()-s.radius)/UNIT;
		}
		if(map[j][i]==3){//击中大本营
			status="STOP";
			dialog.show();
			return false;
		}
		if(map[j][i]==2){
			wallList.remove(new Wall(j,i));
			map[j][i]=0;
			return true;
		}
		if(map[j][i]==1){
			return juageHitTank(s);
		}
		return false;
	}
	/**
	 * 判断子弹是否击中对方坦克
	 * @return
	 */
	public boolean juageHitTank(Shells s){
		Iterator<Tank> tIt;
		if(myTank.hit(s)){//游戏结束
			status="STOP";
			dialog.show();
			return false;
		}
		tIt=tankList.iterator();
		while(tIt.hasNext()){
			Tank t=tIt.next();
			if(t.hit(s)){
				tIt.remove();
				increasePanelMsg(R.id.score);
				return true;
			}
		}
		
		return false;
	}
}
