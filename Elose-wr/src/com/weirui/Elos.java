package com.weirui;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Elos extends Activity {
	private final int SQUARELEMENT_NUMBERS = 7;
	private ElosLogic player = null;//定义一个自定义的类的量
	private ElosView evElos = null;
	private ElosStateView esvElos = null;
	private ElosLogic[][] elosGuard = null;//定义界面数组
	private Drawable[] drArr = new Drawable[SQUARELEMENT_NUMBERS];//保存方块的图片
	private Drawable drArrCartoonBG = null;//保存背景图片
	private int nElosHeight = 0;//定义一个保存组成方块图片的高的量
	private int nElosWidth = 0;//定义一个保存组成方块图片的宽的量
	private boolean bReset = true;//为true时表示开始
	private boolean bPause =true;//false表示用来暂停使用的
	int DRAWABLE_CHOICES = SQUARELEMENT_NUMBERS;//定义一个一个变量用来随机抽取图片来组成图形
	int ELOS_CHOICES = 15;
	int time=0;//时间
	int ntime=0;//累计时间
	int nTimeInterval = 500;//初始值为500表示第一关的总分为500*10
	int nCents = 0;//游戏得分
	int nStage = 1;//游戏关卡
	int nOldStage = 0;//定义上一个关卡数
	private int nTotalLines = 0;
	private int nTotalColumns = 0;//定义了左边界面的数组行与列
	int nElosYOffset = 0;
	int nElosXOffset = 0;//定义了屏幕的起始点
	private int nElosAreaWidth = 0;
	private int nElosAreaHeight = 0;//定义了左面界面的宽与高的量
	Thread thdTimer =  null;//定义一个线程
	boolean bReportResult = false;//只有当游戏失败后值变为true，会在屏幕上显示结果
	boolean bPlaying = true;//当变量为默认的true表示线程里开始工作
	boolean start=false;
	int nPlayerChoice = 0;//当获得随机抽取数表示要显示那个方块图形时赋给这个变量
	int nDrawableChoice = 0;//当获得随机抽取数表示选用那张图片做为构造方块图形赋值给这个变量
	
	//获取资源图片
	private void RetriveDrawable(Context c){
		Resources res = c.getResources();
		drArr[0] = res.getDrawable(R.drawable.a);
		drArr[1] = res.getDrawable(R.drawable.b);
		drArr[2] = res.getDrawable(R.drawable.c);
		drArr[3] = res.getDrawable(R.drawable.d);
		drArr[4] = res.getDrawable(R.drawable.e);
		drArr[5] = res.getDrawable(R.drawable.f);
		drArr[6] = res.getDrawable(R.drawable.g);
		drArrCartoonBG = res.getDrawable(R.drawable.p4);
	}
	
	//-----由于没有拟定每个小图片的大小，所以调用函数获得设定的宽和高--------
	private int GetElosHeight(Drawable dr){
		return dr.getMinimumHeight();
	}
	private int GetElosWidth(Drawable dr){
		return dr.getMinimumWidth();
	}
	//-------------------------------------------------------------------
		
	private void AnnounceNewStage(){//显示关卡数并每关过后将下降速度加快
		if (nOldStage != nStage){
			Toast.makeText(this, "第" + nStage + "关", Toast.LENGTH_SHORT).show();
			nTimeInterval = nTimeInterval - (nStage - 1)*50;//每关后睡眠时间减少
			nOldStage = nStage;
		}
	}
	
	private void ClearStageAndCentsAfterFail(){//开始的时候要清空关卡和分数
		nCents = 0;
		nStage = 1;
		time=0;
		ntime=0;
	}
	
	//---------------------------------游戏失败-------------------------------------------
	private boolean IsElosGameFail(){
		thdTimer=null;
		ElosLogic visit = elosGuard[0][0];
		while (visit != null){
			if (visit.IsDraw() && !visit.bUnit) return true;
			visit = visit.GetElosLogicRight();//一直在左边方框数组的第一行寻找存在有了图片并被画出的则返回true表示失败
		}
		return false;
	}
	
	//------------------------------判断要消去的行数----------------------------------
	private boolean IsWholeLineDrawed(ElosLogic logic){
		if (logic == null) return false;
		ElosLogic keep1, keep2;//定义两个ElosLogic的变量
		keep1 = keep2 = logic;
		int nCount = 0;
		//通过两个分别左右循环的来查看这一行是否需要消除
		while (keep1 != null){
			if (keep1.IsDraw())
				nCount++;
			keep1 = keep1.GetElosLogicLeft();
		}
		keep2 = keep2.GetElosLogicRight();
		while(keep2 != null){
			if (keep2.IsDraw())
				nCount++;
			keep2 = keep2.GetElosLogicRight();
		}
		if (nCount == this.nTotalColumns) return true;//如果与列数相同，则需要消除返回true
		return false;
	}
	
	private void ClearWholeLine(ElosLogic logic){//如果要消除，则将这一行全设定为不显示
		if (logic == null) return;
		ElosLogic keep1, keep2;
		keep1 = keep2 = logic;
		while (keep1 != null){
			keep1.SetDraw(false);
			keep1 = keep1.GetElosLogicLeft();
		}
		keep2 = keep2.GetElosLogicRight();
		while(keep2 != null){
			keep2.SetDraw(false);
			keep2 = keep2.GetElosLogicRight();
		}
	}
	
	private void ElosLogicCopyAfterLineFull(ElosLogic src, ElosLogic des){
		des.bDraw = src.bDraw;
		src.bDraw = false;//把上一行设置成不绘制
		des.SetDrawable(src.GetDrawable());//将下一行保存了上一行的图片
	}
	
	private void ElosLogicLineCopyAfterLineFull(ElosLogic logic){//没消除掉行数都得自动下降
		Rect rtElos = logic.GetRect();
		Rect rtGuardZero = elosGuard[0][0].GetRect();
		int nLineNumber = (rtElos.top - rtGuardZero.top)/rtElos.height();
		while (nLineNumber >= 1){
				for (int i = 0; i < this.nTotalColumns; i++){
					//依次对下一行的每列都换成上一行的图片
					ElosLogicCopyAfterLineFull(elosGuard[nLineNumber - 1][i], elosGuard[nLineNumber][i]);
				}
			nLineNumber--;
		}
	}
	
	int CheckGuard(ElosLogic elosPlayer){//每次不再下降的时候都要核对一遍进行操作
		int nErasableLines = 0;
		if (elosPlayer == null) return nErasableLines;
		if (IsWholeLineDrawed(elosPlayer)){
			ClearWholeLine(elosPlayer); 
			ElosLogicLineCopyAfterLineFull(elosPlayer);
			nErasableLines++;
			}
		if (elosPlayer.GetElosLogicDown() != null){//向上查两行
			if (IsWholeLineDrawed(elosPlayer.GetElosLogicDown())){
				ClearWholeLine(elosPlayer.GetElosLogicDown());
				ElosLogicLineCopyAfterLineFull(elosPlayer.GetElosLogicDown());
				nErasableLines++;
				}
			if (elosPlayer.GetElosLogicDown().GetElosLogicDown() != null){
				if (IsWholeLineDrawed(elosPlayer.GetElosLogicDown().GetElosLogicDown())){
					ClearWholeLine(elosPlayer.GetElosLogicDown().GetElosLogicDown());
					ElosLogicLineCopyAfterLineFull(elosPlayer.GetElosLogicDown().GetElosLogicDown());
					nErasableLines++;
					}
			}
		}
		if (elosPlayer.GetElosLogicUp() != null){//向上查一行
			if (IsWholeLineDrawed(elosPlayer.GetElosLogicUp())){
				ClearWholeLine(elosPlayer.GetElosLogicUp());
				ElosLogicLineCopyAfterLineFull(elosPlayer.GetElosLogicUp());
				nErasableLines++;
			}
		}
		return nErasableLines;//返回所要消除的行数
	}
	

	//-------以下三个函数是分别相应当按下左，右和下的时候组合方块进行的移动-------------
	private ElosLogic PlayerMoveDown(ElosLogic elosPlayer, int nPlayerChoice){//如果向下可以移动的就得进行操作
		if (elosPlayer == null) return elosPlayer;
		if (!IsPlayerMoveableDown(elosPlayer)) {return elosPlayer; }
		Drawable dr  = elosPlayer.GetDrawable();
		ClearPlayer(elosPlayer);
		elosPlayer = elosPlayer.GetElosLogicDown();//这个数组已经换成下一行的了
		elosPlayer = CreatePlayer(dr, nPlayerChoice, elosPlayer);//重新生成一个跟下落前一样的方块
		return elosPlayer;
		
	}
	
	private ElosLogic PlayerMoveLeft(ElosLogic elosPlayer, int nPlayerChoice){
		if (elosPlayer == null) return elosPlayer;
		if (!IsPlayerMoveableLeft(elosPlayer)) {return elosPlayer; }
		Drawable dr  = elosPlayer.GetDrawable();
		ClearPlayer(elosPlayer);
		elosPlayer = elosPlayer.GetElosLogicLeft();
		elosPlayer = CreatePlayer(dr, nPlayerChoice, elosPlayer);
		return elosPlayer;
		
	}
	
	private ElosLogic PlayerMoveRight(ElosLogic elosPlayer, int nPlayerChoice){
		if (elosPlayer == null) return elosPlayer;
		if (!IsPlayerMoveableRight(elosPlayer)) {return elosPlayer; }
		Drawable dr  = elosPlayer.GetDrawable();
		ClearPlayer(elosPlayer);
		elosPlayer = elosPlayer.GetElosLogicRight();
		elosPlayer = CreatePlayer(dr, nPlayerChoice, elosPlayer);
		return elosPlayer;
		
	}
	//---------------------------------------------------------------------------
	
	//--------------------------及时清空上一个图形--------------------------------
	private void ClearElosLogicUnitUnitTag(ElosLogic elosPlayer){
		if (elosPlayer == null) return;
			elosPlayer.bUnit = false;
		if (elosPlayer.GetElosLogicLeft() != null && elosPlayer.GetElosLogicLeft().bUnit)
			ClearElosLogicUnitUnitTag(elosPlayer.GetElosLogicLeft());
		if (elosPlayer.GetElosLogicRight() != null && elosPlayer.GetElosLogicRight().bUnit)
			ClearElosLogicUnitUnitTag(elosPlayer.GetElosLogicRight());
		if (elosPlayer.GetElosLogicUp() != null && elosPlayer.GetElosLogicUp().bUnit)
			ClearElosLogicUnitUnitTag(elosPlayer.GetElosLogicUp());
		if (elosPlayer.GetElosLogicDown() != null && elosPlayer.GetElosLogicDown().bUnit)
			ClearElosLogicUnitUnitTag(elosPlayer.GetElosLogicDown());
	}
	
	//--------------------判断下左右的值返回到函数IsRecursiveElosLogic中---------------------------------
	private boolean IsElosLogicDownable(ElosLogic logic){//判断它的下面情况
		if (logic == null){ return false;}
		if (logic.GetElosLogicDown() == null) { return false;}//表示图形已经到底了返回false
		if (logic.GetElosLogicDown().bUnit) return true;//表示下面已经有图形了返回true
		if (logic.GetElosLogicDown().IsDraw()) { return false;}//下面的没有绘制图片则返回false
		return true;
	}
	
	private boolean IsElosLogicLeftable(ElosLogic logic){
		if (logic == null) return false;
		if (logic.GetElosLogicLeft() == null) return false;
		if (logic.GetElosLogicLeft().bUnit) return true;
		if (logic.GetElosLogicLeft().bDraw) return false;
		return true;
	}
	
	private boolean IsElosLogicRightable(ElosLogic logic){
		if (logic == null) return false;
		if (logic.GetElosLogicRight() == null) return false;
		if (logic.GetElosLogicRight().bUnit) return true;
		if (logic.GetElosLogicRight().bDraw) return false;
		return true;
	}
	//---------------------------------------------------------------------------------
	
	//------------------------------------通过上面三个函数返回值判断下左右能否移动-----------------------
	boolean IsRecursiveElosLogicDownable(ElosLogic elosLogic){//利用上面函数返还值判断方块是否可以移动
		if (!IsElosLogicDownable(elosLogic)) return false;
			elosLogic.bUnit = false;//每次对方块中的一个数组量判断不出是否可以下降是先将量赋为false表示没有
		if (elosLogic.GetElosLogicDown() != null && elosLogic.GetElosLogicDown().bUnit)
			if(!IsRecursiveElosLogicDownable(elosLogic.GetElosLogicDown())) {//只要在一个方向不能动的时候就得将bUnit变为true表示有量
				return false;}
		elosLogic.bUnit = true;//知道通过上下左右检查出的确不能移动在将他设定为true
		return true;
	}
	
	private boolean IsRecursiveElosLogicLeftable(ElosLogic elosLogic){
		if (!IsElosLogicLeftable(elosLogic)) return false;
		elosLogic.bUnit = false;
		if (elosLogic.GetElosLogicLeft() != null && elosLogic.GetElosLogicLeft().bUnit)
			if(!IsRecursiveElosLogicLeftable(elosLogic.GetElosLogicLeft()))
				{elosLogic.bUnit = true;return false;}
		elosLogic.bUnit = true;
		return true;
	}
	
	private boolean IsRecursiveElosLogicRightable(ElosLogic elosLogic){
		if (!IsElosLogicRightable(elosLogic)) return false;
		elosLogic.bUnit = false;
		if (elosLogic.GetElosLogicRight() != null && elosLogic.GetElosLogicRight().bUnit)
			if(!IsRecursiveElosLogicRightable(elosLogic.GetElosLogicRight())) 
				{elosLogic.bUnit = true;return false;}
		elosLogic.bUnit = true;
		return true;
	}
	//---------------------------------------------------------------------------------------------
	
	//----------------------------获得能否移动的结果后返回true和false的值给线程里---------------------------
	private boolean IsPlayerMoveableDown(ElosLogic elosPlayer){
		if (elosPlayer == null) return false;//调用的左边的数组中的量不为null
		return (IsRecursiveElosLogicDownable(elosPlayer));
	}
	
	private boolean IsPlayerMoveableLeft(ElosLogic elosPlayer){
		if (elosPlayer == null) return false;
		return (IsRecursiveElosLogicLeftable(elosPlayer));
	}
	
	private boolean IsPlayerMoveableRight(ElosLogic elosPlayer){
		if (elosPlayer == null) return false;	
		return (IsRecursiveElosLogicRightable(elosPlayer));
	}
	//-------------------------------------------------------------------------------------
	//-----------------------------------------------------------------------------------

	private class  TimerThread extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while (bPlaying){
				while(bPause){
					esvElos.postInvalidate();
					try {
						sleep(nTimeInterval);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//用来累计时间的
					ntime=ntime+nTimeInterval;
					if(ntime%1000==0)
						time++;
				
					if (IsElosGameFail()){
						bPlaying = false;
						bReportResult = true;
						evElos.postInvalidate(); 
					}
					
					//创建的player在这个地方是指数组坐标（1，nTotalColumns/2）
					if (!Elos.this.IsPlayerMoveableDown(player)){
						ClearElosLogicUnitUnitTag(player);
						int nErasableLine = CheckGuard(player);//获得要消除的行数
						nCents += 100*nErasableLine + (int)((nErasableLine == 0? 1: nErasableLine) - 1)*50;//若连续消除了两成以上额外加上消去的层数减一再乘50	
						nStage = nCents/(10*nTimeInterval) + 1;//对于每个关卡的计算
						player = CreatePlayer(drArr[GetRandomDrawableChoice()], GetRandomPlayerChoice());//没核对完一次下落都要重新下一次的下落					
					}else{
						player = Elos.this.PlayerMoveDown(player, nPlayerChoice);	
					}
					evElos.postInvalidate();//执行完上面的都要重新绘制一下界面
				}
			}
		}		
	}
	
	//--------------这两个函数是用来设置菜单的----------------------
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 0, 0, "开始");
		menu.add(0, 1, 1, "退出");
		menu.add(0, 2, 2, "暂停");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == 0){
			bPlaying = true;
			bPause = true;
			start=true;
			MakeElosLogicNet();//对左边界面的每个数组重新保存
			evElos.postInvalidate();
			ClearStageAndCentsAfterFail();//清空游戏的分数和关卡
			
			//-------------------下面的函数是通过两个随机函数获得图片和使用图片组成的图形-------------
			player = CreatePlayer(drArr[GetRandomDrawableChoice()], GetRandomPlayerChoice());
			//当创建了一个方块图形后就要进入一个线程
			if (thdTimer == null){
				thdTimer = new TimerThread();
				thdTimer.start();
			}
		}
		if (item.getItemId()==1){	
			this.finish();
		}
		if (item.getItemId()==2){
			if (item.getTitle().equals("暂停")){
				bPause = false;
				item.setTitle("继续");
				evElos.setEnabled(false);
			}else {
				bPause = true;
				item.setTitle("暂停");
				evElos.setEnabled(true);
			}
		}
		return super.onOptionsItemSelected(item);
	}
	//---------------------------------------------------------
	
	//-------------------------入口的主函数---------------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.elos);
		
		LinearLayout elos = (LinearLayout)this.findViewById(R.id.elos);
		LinearLayout.LayoutParams params = 
			new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);
		params.leftMargin = 1;
		params.rightMargin = 1;
		
		//布局时对显示框的布局
		params.weight = 1;
		elos.addView(evElos = new ElosView(this), params);
		params = new LinearLayout.LayoutParams(params);
		params.weight = 5;
		elos.addView(esvElos = new ElosStateView(this), params);
		
		RetriveDrawable(this);//对图片进行提取
		nElosHeight = GetElosHeight(drArr[0]);
		nElosWidth = GetElosWidth(drArr[0]);
	}
	
	//-----------------------------触发事件（触屏和键盘）--------------------------
	@Override
	public boolean onTouchEvent(MotionEvent event){	 
	    //获得屏幕的宽和高
	    WindowManager windowManager = getWindowManager();
	    Display display = windowManager.getDefaultDisplay();
	    float screenWidth = display.getWidth()*4/5;
	    float screenHeight = display.getHeight();

	      	
	    float x = event.getX();
	   	float y = event.getY();
	      
	    if(event.getAction()==MotionEvent.ACTION_DOWN&&start==true){
	      if (y>screenHeight*2/3&&x<screenWidth/3){//left
	      		player = PlayerMoveLeft(player, nPlayerChoice);
	      }else if (y>screenHeight*2/3&&x>screenWidth*2/3&&x<screenWidth){//right
	      		player = PlayerMoveRight(player, nPlayerChoice);
	      }else if (y<screenHeight*2/3&&x<screenWidth){//up
	      		ElosTransform(player);
	      }else if (x>screenWidth/3&&x<screenWidth*2/3&&y>screenHeight*2/3){//down
	      if (this.IsPlayerMoveableDown(player))
	      		player = PlayerMoveDown(player, nPlayerChoice);	
	      }
	  	}
	      	
		evElos.invalidate();
		AnnounceNewStage();
		return super.onTouchEvent(event);
	} 
	    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if (keyCode == 21){//left
			player = PlayerMoveLeft(player, nPlayerChoice);
		}else if (keyCode == 22){//right
			player = PlayerMoveRight(player, nPlayerChoice);
		}else if (keyCode == 19){//up
			ElosTransform(player);
		}else if (keyCode == 20){//down
				if (this.IsPlayerMoveableDown(player))
					player = PlayerMoveDown(player, nPlayerChoice);
		}
		evElos.invalidate();
		AnnounceNewStage();
		return super.onKeyDown(keyCode, event);
	}
	//--------------------------------------------------------------------------
	

	//下面是组合方块的变形
	//NNNN
	
	//N
	//N
	//N
	//N
	private void ElosTransform1(ElosLogic elosPlayer){
		if (elosPlayer.nNumber == 1){
			Drawable dr = elosPlayer.GetDrawable();
			this.ClearPlayer(elosPlayer);
			AssembleElosUnitType(elosPlayer, dr, 5);
		}else if (elosPlayer.nNumber == 6){
			if (elosPlayer.GetElosLogicLeft() == null) return;
			if (elosPlayer.GetElosLogicLeft().IsDraw()) return;
			if (elosPlayer.GetElosLogicRight() == null) return;
			if (elosPlayer.GetElosLogicRight().IsDraw()) return;
			if (elosPlayer.GetElosLogicLeft().GetElosLogicLeft() == null) return;
			if (elosPlayer.GetElosLogicLeft().GetElosLogicLeft().IsDraw()) return;
			Drawable dr = elosPlayer.GetDrawable();
			this.ClearPlayer(elosPlayer);
			AssembleElosUnitType(elosPlayer, dr, 0);
		}
	}
	
	//	NNN
	//    N
	
	//    N
	//    N
	//	NNN	
	private void ElosTransform2(ElosLogic elosPlayer){
		if (elosPlayer.nNumber == 2){
			if (elosPlayer.GetElosLogicUp() == null) return;
			if (elosPlayer.GetElosLogicUp().IsDraw()) return;
			if (elosPlayer.GetElosLogicUp().GetElosLogicUp() == null) return;
			if (elosPlayer.GetElosLogicUp().GetElosLogicUp().IsDraw()) return;
			Drawable dr = elosPlayer.GetDrawable();
			this.ClearPlayer(elosPlayer);
			AssembleElosUnitType(elosPlayer, dr, 6);
		}else if (elosPlayer.nNumber == 7){
			if (elosPlayer.GetElosLogicRight() == null) return;
			if (elosPlayer.GetElosLogicRight().IsDraw()) return;
			if (elosPlayer.GetElosLogicRight().GetElosLogicRight() == null) return;
			if (elosPlayer.GetElosLogicRight().GetElosLogicRight().IsDraw()) return;
			Drawable dr = elosPlayer.GetDrawable();
			this.ClearPlayer(elosPlayer);
			AssembleElosUnitType(elosPlayer, dr, 7);
		}else if(elosPlayer.nNumber == 8){
			if (elosPlayer.GetElosLogicDown() == null) return;
			if (elosPlayer.GetElosLogicDown().IsDraw()) return;
			if (elosPlayer.GetElosLogicDown().GetElosLogicDown() == null) return;
			if (elosPlayer.GetElosLogicDown().GetElosLogicDown().IsDraw()) return;
			Drawable dr = elosPlayer.GetDrawable();
			this.ClearPlayer(elosPlayer);
			AssembleElosUnitType(elosPlayer, dr, 8);
		}else if (elosPlayer.nNumber == 9){
			if (elosPlayer.GetElosLogicLeft() == null) return;
			if (elosPlayer.GetElosLogicLeft().IsDraw()) return;
			if (elosPlayer.GetElosLogicLeft().GetElosLogicLeft() == null) return;
			if (elosPlayer.GetElosLogicLeft().GetElosLogicLeft().IsDraw()) return;
			Drawable dr = elosPlayer.GetDrawable();
			this.ClearPlayer(elosPlayer);
			AssembleElosUnitType(elosPlayer, dr, 1);
		}
	}
	
	//NNN
	// N
	private void ElosTransform3(ElosLogic elosPlayer){
		if (elosPlayer.nNumber == 3){
			if (elosPlayer.GetElosLogicUp() == null) return;
			if (elosPlayer.GetElosLogicUp().IsDraw()) return;
			Drawable dr = elosPlayer.GetDrawable();
			this.ClearPlayer(elosPlayer);
			AssembleElosUnitType(elosPlayer, dr, 9);
		}else if (elosPlayer.nNumber == 10){
			if (elosPlayer.GetElosLogicRight() == null) return;
			if (elosPlayer.GetElosLogicRight().IsDraw()) return;
			Drawable dr = elosPlayer.GetDrawable();
			this.ClearPlayer(elosPlayer);
			AssembleElosUnitType(elosPlayer, dr, 10);
		}else if (elosPlayer.nNumber == 11){
			if (elosPlayer.GetElosLogicDown() == null) return;
			if (elosPlayer.GetElosLogicDown().IsDraw()) return;
			Drawable dr = elosPlayer.GetDrawable();
			this.ClearPlayer(elosPlayer);
			AssembleElosUnitType(elosPlayer, dr, 11);
		}else if (elosPlayer.nNumber == 12){
			if (elosPlayer.GetElosLogicLeft() == null) return;
			if (elosPlayer.GetElosLogicLeft().IsDraw()) return;
			Drawable dr = elosPlayer.GetDrawable();
			this.ClearPlayer(elosPlayer);
			AssembleElosUnitType(elosPlayer, dr, 2);
		}
	}
	
	//NNN
	//N
	private void ElosTransform4(ElosLogic elosPlayer){
		if (elosPlayer.nNumber == 4){
			if (elosPlayer.GetElosLogicLeft() == null) return;
			if (elosPlayer.GetElosLogicLeft().IsDraw()) return;
			if (elosPlayer.GetElosLogicDown().GetElosLogicDown() == null) return;
			if (elosPlayer.GetElosLogicDown().GetElosLogicDown().IsDraw()) return;
			Drawable dr = elosPlayer.GetDrawable();
			this.ClearPlayer(elosPlayer);
			AssembleElosUnitType(elosPlayer, dr, 12);
		}else if (elosPlayer.nNumber == 13){
			if (elosPlayer.GetElosLogicUp() == null) return;
			if (elosPlayer.GetElosLogicUp().IsDraw()) return;
			if (elosPlayer.GetElosLogicLeft().GetElosLogicLeft() == null) return;
			if (elosPlayer.GetElosLogicLeft().GetElosLogicLeft().IsDraw()) return;
			Drawable dr = elosPlayer.GetDrawable();
			this.ClearPlayer(elosPlayer);
			AssembleElosUnitType(elosPlayer, dr, 13);
		}else if(elosPlayer.nNumber == 14){
			if (elosPlayer.GetElosLogicRight() == null) return;
			if (elosPlayer.GetElosLogicRight().IsDraw()) return;
			if (elosPlayer.GetElosLogicUp().GetElosLogicUp() == null) return;
			if (elosPlayer.GetElosLogicUp().GetElosLogicUp().IsDraw()) return;
			Drawable dr = elosPlayer.GetDrawable();
			this.ClearPlayer(elosPlayer);
			AssembleElosUnitType(elosPlayer, dr, 14);
		}else if (elosPlayer.nNumber == 15){
			if (elosPlayer.GetElosLogicDown() == null) return;
			if (elosPlayer.GetElosLogicDown().IsDraw()) return;
			if (elosPlayer.GetElosLogicRight().GetElosLogicRight() == null) return;
			if (elosPlayer.GetElosLogicRight().GetElosLogicRight().IsDraw()) return;
			Drawable dr = elosPlayer.GetDrawable();
			this.ClearPlayer(elosPlayer);
			AssembleElosUnitType(elosPlayer, dr, 3);
		}
	}
	
	private void ElosTransform(ElosLogic elosPlayer){
	//	Drawable dr  = elosPlayer.GetDrawable();
		ElosTransform1(elosPlayer);
		ElosTransform2(elosPlayer);
		ElosTransform3(elosPlayer);
		ElosTransform4(elosPlayer);	
	//	elosPlayer.SetDrawable(dr);
		this.nPlayerChoice = elosPlayer.nNumber - 1;
	}
	
	//----------------将界面分成的数组每个量new一个自定义的类ElosLogic对象-----------------
	private void InitElosLogicGuard(){
		elosGuard = new ElosLogic[nTotalLines][nTotalColumns];
		int i = 0;
		while (i < nTotalLines){
			for (int j = 0; j <nTotalColumns; j++)
				elosGuard[i][j] = new ElosLogic();
			i++;
		}
	}
	//-----------------------------------------------------------------------------------
	
	//---------------将我们数组中每个量可能会使用的数据保存到自定义类中---------------
	private void MakeElosLogicNet(){
		Rect rt = new Rect();
		for (int i=0;i <this.nTotalLines;i++ ){
			int j = 0;
			for (; j < this.nTotalColumns; j++){
				//------------left------------------
				if (j == 0){
					elosGuard[i][j].SetElosLogicLeft(null);
				}else{
					elosGuard[i][j].SetElosLogicLeft(elosGuard[i][j - 1]);
				}
				//------------right--------------------
				if (j == this.nTotalColumns - 1){
					elosGuard[i][j].SetElosLogicRight(null);
				}else{
					elosGuard[i][j].SetElosLogicRight(elosGuard[i][j + 1]);
				}
				//-----------up--------------------------------
				if (i == 0){
					elosGuard[i][j].SetElosLogicUp(null);
				}else{
					elosGuard[i][j].SetElosLogicUp(elosGuard[i - 1][j]);
				}
				//------------down-------------------------
				if (i == this.nTotalLines - 1){
					elosGuard[i][j].SetElosLogicDown(null);
				}else{
					elosGuard[i][j].SetElosLogicDown(elosGuard[i + 1][j]);
				}
				
				//----------------保存好数组中每个量的框架------------------
				rt.left = j*this.nElosWidth + nElosXOffset;
				rt.top = i*this.nElosHeight + this.nElosYOffset;
				rt.right = rt.left + this.nElosWidth;
				rt.bottom = rt.top + this.nElosHeight;
				elosGuard[i][j].SetRect(rt);//将数组中的每个量的边框保存到自定义类中
				
				elosGuard[i][j].SetDrawable(drArr[0]);
				
				elosGuard[i][j].SetDraw(false);
				elosGuard[i][j].bUnit = false;
			}		
		}
	}
	//--------------------------------------------------------------------------------
	
	//----------------------------这两个类主要是用来绘制界面的-------------------------
	Rect rtDraw = new Rect();	//画图
	private void DrawElosLogicNet(Canvas canvas, ElosLogic[][] elosLogicGuard,Rect rtDraw){
		for (int i = 0; i < elosLogicGuard.length; i++)
			for (int j = 0; j < elosLogicGuard[i].length; j++)
				if (elosLogicGuard[i][j].IsDraw())
				DrawElos(elosLogicGuard[i][j], canvas);//对数组的每个量进行绘制
	}
	
	private void DrawElos(ElosLogic elosLogic, Canvas canvas){
		if (!elosLogic.IsDraw()) return;
		Drawable dr = elosLogic.GetDrawable();
		if (dr == null) return;	
		//-----------绘制左边数组中的每个量的边框和图片-----------------
		rtDraw.set(elosLogic.GetRect());
		rtDraw.right = rtDraw.right - 1/2;
		rtDraw.bottom = rtDraw.bottom - 1/2;
		dr.setBounds(rtDraw);
		dr.draw(canvas);//将图片画在相应的矩形框架里
		//------------------------------------------------------------
	}
	//-----------------------------------------------------------------------------
	
	//----------------------------绘制左边界面的类----------------------------------
	private class ElosView extends View{

		public ElosView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			super.onDraw(canvas);
			
			Paint pt = new Paint();//获得画笔
			Rect rtDraw=new Rect();
			Rect rt = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());//获得边框架的矩阵
			pt.setColor(Color.RED);
			drArrCartoonBG.setBounds(rt);
			drArrCartoonBG.draw(canvas);
			
			if (bReset){
				//-------------获得屏幕的宽和高------------------
				nElosAreaHeight = this.getHeight();
				nElosAreaWidth = this.getWidth();	
				//-------------得到可以分成的数组行与列且与边缘的距离-----------------
				nTotalLines = nElosAreaHeight/nElosHeight + 3;
				nElosYOffset = nElosAreaHeight%nElosHeight - 3*nElosHeight;
				nTotalColumns = nElosAreaWidth/nElosWidth;
				nElosXOffset = nElosAreaWidth%nElosWidth/2;
				rtDraw =new Rect(nElosXOffset,nElosYOffset,nTotalColumns*nElosWidth,nTotalLines*nElosHeight);
				
				InitElosLogicGuard();//对分成的数组每个量new一个对象
				MakeElosLogicNet();//保存每个量的信息进自定义的类
				bReset = false;
			}
			DrawElosLogicNet(canvas, elosGuard,rtDraw);//绘制界面
			
			if (bReportResult){
				Toast.makeText(Elos.this, "游戏结束,得分：" + nCents, Toast.LENGTH_SHORT).show();
				bReportResult = false;
			}
		}		
	}
	//------------------------------------------------------------------------------
	
	//两个都是使用随机获得整型
	private int GetRandomPlayerChoice(){		
		return nPlayerChoice = ((int)(Math.random()*10000))%ELOS_CHOICES;
	}
	
	private int GetRandomDrawableChoice(){		
		return nDrawableChoice = ((int)(Math.random()*10000))%DRAWABLE_CHOICES;
	}
	
	//将下面15个函数构造的图形来显示出来
	private void AssignElosLogic(ElosLogic elosLogic, Drawable dr){
		if (elosLogic == null) return;//数组不存在则返回
		elosLogic.dr = dr;//得到图片
		elosLogic.bUnit = true;//
		elosLogic.SetDraw(true);//当SetDraw保存了true则表示可以绘制
	}
	
	//--------------以上15个函数是通过随机抽取的图片来构造出方块的组合形状---------------------
	//NNNN
	private void AssembleElosUnitType1(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 1;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft().GetElosLogicLeft(), dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft(), dr);
		AssignElosLogic(elosLogic.GetElosLogicRight(), dr);
	}
	
	//NNN
	//  N
	private void AssembleElosUnitType2(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 2;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft().GetElosLogicLeft(), dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft(), dr);
		AssignElosLogic(elosLogic.GetElosLogicDown(), dr);
	}
	
	//NNN
	// N
	private void AssembleElosUnitType3(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 3;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft(), dr);
		AssignElosLogic(elosLogic.GetElosLogicDown(), dr);
		AssignElosLogic(elosLogic.GetElosLogicRight(), dr);
	}
	
	//NNN
	//N
	private void AssembleElosUnitType4(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 4;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicRight(), dr);
		AssignElosLogic(elosLogic.GetElosLogicRight().GetElosLogicRight(), dr);
		AssignElosLogic(elosLogic.GetElosLogicDown(), dr);
	}
	
	//NNN
	//NNN
	private void AssembleElosUnitType5(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 5;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicRight(), dr);
		AssignElosLogic(elosLogic.GetElosLogicRight().GetElosLogicDown(), dr);
		AssignElosLogic(elosLogic.GetElosLogicDown(), dr);
	}
	
	//N
	//N
	//N
	//N
	private void AssembleElosUnitType6(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 6;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicUp(), dr);
		AssignElosLogic(elosLogic.GetElosLogicDown(), dr);
		AssignElosLogic(elosLogic.GetElosLogicDown().GetElosLogicDown(), dr);
	}
	
	// N
	// N
	//NN
	private void AssembleElosUnitType7(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 7;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicUp(), dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft(), dr);
		AssignElosLogic(elosLogic.GetElosLogicUp().GetElosLogicUp(), dr);
	}
	
	//N
	//NNN
	private void AssembleElosUnitType8(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 8;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicRight(), dr);
		AssignElosLogic(elosLogic.GetElosLogicRight().GetElosLogicRight(), dr);
		AssignElosLogic(elosLogic.GetElosLogicUp(), dr);
	}
	
	//NN
	//N
	//N
	private void AssembleElosUnitType9(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 9;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicRight(), dr);
		AssignElosLogic(elosLogic.GetElosLogicDown(), dr);
		AssignElosLogic(elosLogic.GetElosLogicDown().GetElosLogicDown(), dr);
	}
	
	// N
	//NN
	// N
	private void AssembleElosUnitType10(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 10;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicUp(), dr);
		AssignElosLogic(elosLogic.GetElosLogicDown(), dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft(), dr);
	}
	
	// N
	//NNN
	private void AssembleElosUnitType11(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 11;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicUp(), dr);
		AssignElosLogic(elosLogic.GetElosLogicRight(), dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft(), dr);
	}
	
	//N
	//NN
	//N
	private void AssembleElosUnitType12(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 12;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicUp(), dr);
		AssignElosLogic(elosLogic.GetElosLogicRight(), dr);
		AssignElosLogic(elosLogic.GetElosLogicDown(), dr);
	}
	
	//NN
	// N
	// N
	private void AssembleElosUnitType13(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 13;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft(), dr);
		AssignElosLogic(elosLogic.GetElosLogicDown(), dr);
		AssignElosLogic(elosLogic.GetElosLogicDown().GetElosLogicDown(), dr);
	}
	
	//  N
	//NNN
	private void AssembleElosUnitType14(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 14;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicUp(), dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft().GetElosLogicLeft(), dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft(), dr);
	}
	
	//N
	//N
	//NN
	private void AssembleElosUnitType15(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 15;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicUp(), dr);
		AssignElosLogic(elosLogic.GetElosLogicUp().GetElosLogicUp(), dr);
		AssignElosLogic(elosLogic.GetElosLogicRight(), dr);
	}
	//--------------------------------------------------------------------------------
	
	private void AssembleElosUnitType(ElosLogic elosLogic, Drawable dr, int nChoice){
		if (nChoice == 0){AssembleElosUnitType1(elosLogic, dr);}
		else if (nChoice == 1){AssembleElosUnitType2(elosLogic, dr);}
		else if (nChoice == 2){AssembleElosUnitType3(elosLogic, dr);}
		else if (nChoice == 3){AssembleElosUnitType4(elosLogic, dr);}
		else if (nChoice == 4){AssembleElosUnitType5(elosLogic, dr);}
		else if (nChoice == 5){AssembleElosUnitType6(elosLogic, dr);}
		else if (nChoice == 6){AssembleElosUnitType7(elosLogic, dr);}
		else if (nChoice == 7){AssembleElosUnitType8(elosLogic, dr);}
		else if (nChoice == 8){AssembleElosUnitType9(elosLogic, dr);}
		else if (nChoice == 9){AssembleElosUnitType10(elosLogic, dr);}
		else if (nChoice == 10){AssembleElosUnitType11(elosLogic, dr);}
		else if (nChoice == 11){AssembleElosUnitType12(elosLogic, dr);}
		else if (nChoice == 12){AssembleElosUnitType13(elosLogic, dr);}
		else if (nChoice == 13){AssembleElosUnitType14(elosLogic, dr);}
		else if (nChoice == 14){AssembleElosUnitType15(elosLogic, dr);}
	}
	
	//在左边界面的顶端生成一个要落下的方块图形
	private ElosLogic CreatePlayer(Drawable dr, int nChoice){
		player = elosGuard[1][this.nTotalColumns/2];
		AssembleElosUnitType(player, dr, nChoice);
		return player;
	}
	
	//在左边界面的给定的数组中的量生成一个要落下的方块图形
	private ElosLogic CreatePlayer(Drawable dr, int nChoice, ElosLogic elosLogic){
		AssembleElosUnitType(elosLogic,dr, nChoice);
		return elosLogic;
	}
	
	private void ClearPlayer(ElosLogic elosPlayer){//每次在显示完一个图形后都要将数组清除干净
		elosPlayer.bUnit = false;
		elosPlayer.SetDraw(false);
		elosPlayer.nNumber = -1;
		elosPlayer.dr = null;
		if (elosPlayer.GetElosLogicLeft() != null && elosPlayer.GetElosLogicLeft().bUnit)
			ClearPlayer(elosPlayer.GetElosLogicLeft());
		if (elosPlayer.GetElosLogicRight() != null && elosPlayer.GetElosLogicRight().bUnit)
			ClearPlayer(elosPlayer.GetElosLogicRight());
		if (elosPlayer.GetElosLogicUp() != null && elosPlayer.GetElosLogicUp().bUnit)
			ClearPlayer(elosPlayer.GetElosLogicUp());
		if (elosPlayer.GetElosLogicDown() != null && elosPlayer.GetElosLogicDown().bUnit)
			ClearPlayer(elosPlayer.GetElosLogicDown());
	}
	//---------------------------------绘制右边界面的类---------------------------------
	private class ElosStateView extends View{
		private int nAreaWidth = 0;
		private int nElosPrevHeight = 0;
		private int nElosPrevWidth = 0;
		private int nElosYOffset = 20;
		private int nElosXOffset = 0;
		private ElosLogic[][] logicPrevArr = new ElosLogic[6][6];
		private boolean bInitState = true;
		
		
		public ElosStateView(Context context) {//构造函数
			super(context);
			// TODO Auto-generated constructor stub
		}
		
			
		private void InitLogicPrevArr(){
			nElosPrevWidth = nAreaWidth/6;
			nElosXOffset = nAreaWidth%6/2;
			nElosPrevHeight = nElosPrevWidth;
			nElosYOffset = nElosXOffset+nElosYOffset;
				
			for (int i = 0; i < 6; i++){
				for (int j = 0; j < 6; j++)
					logicPrevArr[i][j] = new ElosLogic();
			}
			Rect rt = new Rect();
			for (int i = 0; i < 6; i++){
				for (int j = 0; j < 6; j++){
					if (i == 0){
						logicPrevArr[i][j].logicUp = null;
					}else{
						logicPrevArr[i][j].logicUp = logicPrevArr[i - 1][j];
					}
					if (i == 5){
						logicPrevArr[i][j].logicDown = null;
					}else {
						logicPrevArr[i][j].logicDown = logicPrevArr[i + 1][j];
					}
					if (j == 0){
						logicPrevArr[i][j].logicLeft = null;
					}else {
						logicPrevArr[i][j].logicLeft = logicPrevArr[i][j - 1];
					}	
					if (j == 5){
						logicPrevArr[i][j].logicRight = null;
					}else {
						logicPrevArr[i][j].logicRight = logicPrevArr[i][j + 1];
					}
					
					//-------------获取右边界面显示下一个图形的框架-------------------
					rt.left = j*this.nElosPrevWidth + nElosXOffset;
					rt.top = i*this.nElosPrevHeight + nElosYOffset;
					rt.right = rt.left + this.nElosPrevWidth;
					rt.bottom = rt.top + this.nElosPrevHeight;
					logicPrevArr[i][j].SetRect(rt);
					//--------------------------------------------------------------
					
					logicPrevArr[i][j].SetDrawable(drArr[Elos.this.nDrawableChoice]);
					logicPrevArr[i][j].SetDraw(false);
					logicPrevArr[i][j].bUnit = false;
				}
			}
		}
		
		@Override
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			super.onDraw(canvas);
			//--------获取右边界面的宽与高---------
			nAreaWidth = this.getWidth();
			this.getHeight();
			
			if (bInitState){//初始值为true表示要绘制，避免左边的界面在进行图形运动影响右边界面
				InitLogicPrevArr();//调用函数
				bInitState = false;
			}
			
			Paint pt = new Paint();
			Rect rt = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
			pt.setColor(Color.GRAY);
			canvas.drawRect(rt, pt);//将右边界面绘制成灰色
			
			Rect rtDraw = new Rect();
			rtDraw.set(logicPrevArr[0][0].GetRect());
			rtDraw.right = logicPrevArr[5][5].GetRect().right;
			rtDraw.bottom = logicPrevArr[5][5].GetRect().bottom;
			
			Elos.this.ClearPlayer(logicPrevArr[2][2]);//每次创建一个图形前都要清空一下数组
			Elos.this.CreatePlayer(drArr[Elos.this.nDrawableChoice], Elos.this.nPlayerChoice, logicPrevArr[2][2]);
			Elos.this.DrawElosLogicNet(canvas, logicPrevArr,rtDraw);//利用和左边界面同样的绘制函数绘制出图形
			//在右边的界面上显示一些关卡和分数
			DrawStageAndCents(canvas, Elos.this.nStage, Elos.this.nCents, logicPrevArr[5][0].GetRect().left, logicPrevArr[5][0].GetRect().bottom + 100);
		}
		
		//在右边的见面显示出关卡和分数
		private void DrawStageAndCents(Canvas canvas, int nStage, int nCents, int x, int y){
			Paint pt = new Paint();
			pt.setColor(Color.BLACK);
			pt.setTextSize(15);
			canvas.drawText("第" + nStage + "关", x, y, pt);
			canvas.drawText("分数:", x, y + 40, pt);
			canvas.drawText(Integer.toString(nCents)+"分", x + 5, y + 55, pt);	
			canvas.drawText("时间:", x, y+95, pt);
			canvas.drawText(Integer.toString(time),x+10,y+110,pt);
		}	
	}
}
