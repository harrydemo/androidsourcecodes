package com.china.square.linksee;

import java.util.ArrayList;

import com.china.square.R;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class LinkSee extends Activity {
	String TAG = "ELOS";
	private final int SQUARELEMENT_NUMBERS = 9;
	private ElosView evElos = null;
	
	int nTimeInterval = 500;
	int nCents = 0;
	int nStage = 1;
	
	private final int ELOS_HEIGHT = 60;
	private final int ELOS_WIDTH = 50;
	
	
	private boolean bNewPlayer = true;
	
	boolean bPlaying = true;
	
	int nElosYOffset = 0;
	int nElosXOffset = 0;
	
	int nPlayerChoice = 0;
	int nDrawableChoice = 0;
	int ELOS_CHOICES = 15;
	int DRAWABLE_CHOICES = SQUARELEMENT_NUMBERS;
	
	private Rect rtElosArea = new Rect();
	
	private ElosLogic[][] elosGuard = null;
	
	private int nTotalLines = 0;
	private int nTotalColumns = 0;
	
	private int nElosHeight = 0;
	private int nElosWidth = 0;
	
	private int nElosAreaWidth = 0;
	private int nElosAreaHeight = 0;
	private boolean bReset = true;	
	
	private Drawable[] drArr = new Drawable[SQUARELEMENT_NUMBERS];	
	private ElosLogic player = null;
	
	private class Pair extends Object{
		private ElosLogic logic1 = null;
		private ElosLogic logic2 = null;
		public Pair(){};
		public Pair(ElosLogic logic1, ElosLogic logic2){this.logic1 = logic1; this.logic2 = logic2; }
		public ElosLogic GetElosLogic1(){ return logic1; }
		public ElosLogic GetElosLogic2(){ return logic2; }
		public void SetLogic1(ElosLogic logic1){ this.logic1 = logic1; }
		public void SetLogic2(ElosLogic logic2){ this.logic1 = logic2; }
		public void SetPair(Pair pair){this.SetLogic1(pair.GetElosLogic1()); this.SetLogic2(pair.GetElosLogic2());}
	}
	
	private boolean bFirstTouch = true;
	ArrayList<ElosLogic>lstTrail = new ArrayList<ElosLogic>();/* 记录路径上的所有的ElosLogic */
	Pair pair = new Pair();
	
	private void InitElosLogicGuard(){
		elosGuard = new ElosLogic[nTotalLines][nTotalColumns];
		int i = 0;
		while (i < nTotalLines){
			for (int j = 0; j <nTotalColumns; j++)
				elosGuard[i][j] = new ElosLogic();
			i++;
		}
	}
	
	private void ClearStageAndCentsAfterFail(){
		nCents = 0;
		nStage = 1;
	}
	
	private void RetriveDrawable(Context c){
		Resources res = c.getResources();
		
		drArr[0] = res.getDrawable(R.drawable.a);
		drArr[1] = res.getDrawable(R.drawable.b);
		drArr[2] = res.getDrawable(R.drawable.c);
		drArr[3] = res.getDrawable(R.drawable.d);
		drArr[4] = res.getDrawable(R.drawable.e);
		drArr[5] = res.getDrawable(R.drawable.f);
		drArr[6] = res.getDrawable(R.drawable.g);
		drArr[7] = res.getDrawable(R.drawable.h);
		drArr[8] = res.getDrawable(R.drawable.i);
	}
	
	private int GetElosHeight(Drawable dr){
		//return dr.getMinimumHeight();
		return ELOS_HEIGHT;
	}
	private int GetElosWidth(Drawable dr){
		//return dr.getMinimumWidth();
		return ELOS_WIDTH;
	}
	
	private void InvalidateStage(){
		nStage = nCents/(10*nTimeInterval) + 1;
		
	}
	int nOldStage = 0;
	private void AnnounceNewStage(){
		if (nOldStage != nStage){
			Toast.makeText(this, "第" + nStage + "关", Toast.LENGTH_SHORT).show();
			nTimeInterval = nTimeInterval - (nStage - 1)*50;
			nOldStage = nStage;
		}
	}
	
	Thread thdTimer =  null;
	boolean bReportResult = false;
	private class  TimerThread extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			
			while (bPlaying){
				try {
					sleep(nTimeInterval);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}		
		}
	}
	final int ELOS_START = Menu.FIRST;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, ELOS_START, 0, "开始");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		if (item.getItemId() == ELOS_START){
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private boolean IsElosGameFail(){
		return false;
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		if (thdTimer != null)if (thdTimer.isAlive())thdTimer.stop();
		super.onStop();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.elos);
	
		LinearLayout ll = (LinearLayout)this.findViewById(R.id.elos);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);
		params.leftMargin = 1;
		params.rightMargin = 1;
		
		ll.addView(evElos = new ElosView(this), params);
		params = new LinearLayout.LayoutParams(params);
		
		RetriveDrawable(this);
		nElosHeight = GetElosHeight(drArr[0]);
		nElosWidth = GetElosWidth(drArr[0]);
		nElosYOffset = nElosHeight;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if (keyCode == 21){//left
			
		}else if (keyCode == 22){//right
	
		}else if (keyCode == 19){//up
			
		}else if (keyCode == 20){//down
		}
		//evElos.invalidate();
		//AnnounceNewStage();
		return super.onKeyDown(keyCode, event);
	}
	
	private ElosLogic GetElosLogicByPoint(Point pt){
		int nCurRow, nCurCol;
		nCurCol = pt.x/nElosWidth;
		nCurRow = pt.y/nElosHeight;
		if (nCurRow >= this.nTotalLines || nCurRow < 0) return null;
		if (nCurCol >= this.nTotalColumns || nCurCol < 0) return null;
		return elosGuard[nCurRow][nCurCol];
	}
	
	Rect rtDraw = new Rect();
	Paint ptFrame = new Paint();
	private void DrawElos(ElosLogic elosLogic, Canvas canvas, int nOffset){
		ptFrame.setColor(Color.BLACK);
		if (!elosLogic.IsDraw()) return;
		Drawable dr = elosLogic.GetDrawable();
		if (dr == null) Log.d(TAG, "drawble is null...");
		rtDraw.set(elosLogic.GetRect());
		rtDraw.right = rtDraw.right - nOffset;
		rtDraw.bottom = rtDraw.bottom - nOffset;
		Rect rt = new Rect();
		rt.set(rtDraw);
		rt.left++;
		rt.right--;
		rt.top++;
		rt.bottom--;
		if (elosLogic.bTouch) dr.setBounds(rt);
		else dr.setBounds(rtDraw);
		dr.draw(canvas);
		
		if (!elosLogic.bTouch){
			//canvas.drawRoundRect(new RectF(rtDraw.left-1, rtDraw.top-1, rtDraw.right+1, rtDraw.bottom+1), 1, 1, ptFrame);
			canvas.drawLine(rtDraw.left-1, rtDraw.top-1, rtDraw.right+1, rtDraw.top-1, ptFrame);
			canvas.drawLine(rtDraw.right+1, rtDraw.bottom+1, rtDraw.right+1, rtDraw.top-1, ptFrame);
			canvas.drawLine(rtDraw.right+1, rtDraw.bottom+1, rtDraw.left-1, rtDraw.bottom+1, ptFrame);
			canvas.drawLine(rtDraw.left-1, rtDraw.top-1, rtDraw.left-1, rtDraw.bottom+1, ptFrame);
		}else {
			//canvas.drawRoundRect(new RectF(rt.left-1, rt.top-1, rt.right+1, rt.bottom+1), 1, 1, ptFrame);
			canvas.drawLine(rt.left-1, rt.top-1, rt.right+1, rt.top-1, ptFrame);
			canvas.drawLine(rt.right+1, rt.bottom+1, rt.right+1, rt.top-1, ptFrame);
			canvas.drawLine(rt.right+1, rt.bottom+1, rt.left-1, rt.bottom+1, ptFrame);
			canvas.drawLine(rt.left-1, rt.top-1, rt.left-1, rt.bottom+1, ptFrame);
		}
	}
	
	private void DrawElosLogicNet(Canvas canvas, ElosLogic[][] elosLogicGuard){
		for (int i = 0; i < elosLogicGuard.length; i++)
			for (int j = 0; j < elosLogicGuard[i].length; j++)
				if (elosLogicGuard[i][j].IsDraw())
				DrawElos(elosLogicGuard[i][j], canvas, 1);
	}
	
	private void MakeElosLogicNet(ElosLogic[][] elosGuard){
		int nLen = elosGuard.length;
		if (nLen != this.nTotalLines){ Log.d(TAG, "guard len is error"); }
		int nColumns = this.nTotalColumns;
		if (nColumns != elosGuard.length){ Log.d(TAG, "guard len is error");}
		int i = 0;
		Rect rt = new Rect();
		while (i < nLen){
			//process line...
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
				
				//---------assign rect for elos logic-----
				
				rt.left = j*this.nElosWidth + nElosXOffset;
				rt.top = i*this.nElosHeight + this.nElosYOffset;
				rt.right = rt.left + this.nElosWidth;
				rt.bottom = rt.top + this.nElosHeight;
				elosGuard[i][j].SetRect(rt);
				
				elosGuard[i][j].SetDrawable(drArr[GetRandomDrawableChoice()]);
				if (i == 0 || i == this.nTotalLines - 1 ||j == this.nTotalColumns - 1 || j == 0)elosGuard[i][j].SetDraw(false);
				else elosGuard[i][j].SetDraw(true);
			}		
			i++;
		}
	}
	
	private boolean IsMatch(ElosLogic elosLogic1, ElosLogic elosLogic2){
		return false;
	}
	
	private int GetRandomPlayerChoice(){		
		return nPlayerChoice = ((int)(Math.random()*10000))%ELOS_CHOICES;
	}
	
	private int GetRandomDrawableChoice(){		
		return nDrawableChoice = ((int)(Math.random()*10000))%DRAWABLE_CHOICES;
	}
	
	private class ElosView extends View{		
		
		public ElosView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}
		int vvvv = 0;
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			super.onDraw(canvas);
			
			Paint pt = new Paint();
			Rect rt = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
			rtElosArea.set(rt);
			pt.setColor(Color.WHITE);
			canvas.drawRect(rt, pt);
			if (bReset){
				//------------------------------------------
				nElosAreaHeight = this.getHeight();
				nElosAreaWidth = this.getWidth();	
				//---------------------------------------
				nTotalLines = nElosAreaHeight/nElosHeight;
				nElosYOffset = nElosAreaHeight%nElosHeight;
				nTotalColumns = nElosAreaWidth/nElosWidth;
				nElosXOffset = nElosAreaWidth%nElosWidth/2;
				InitElosLogicGuard();
				MakeElosLogicNet(elosGuard);
				bReset = false;
				vvvv++;
				
			}
			DrawElosLogicNet(canvas, elosGuard);
			if (bReportResult){
				Toast.makeText(LinkSee.this, "游戏结束,得分：" + nCents, Toast.LENGTH_SHORT).show();vvvv++;
				bReportResult = false;
			}
		}
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			// TODO Auto-generated method stub
			if (event.getAction() == MotionEvent.ACTION_DOWN){
				int ix = (int)event.getX();
				int iy = (int)event.getY();
				Point pt = new Point(ix, iy);
				ElosLogic logic = GetElosLogicByPoint(pt);
				if (logic != null){
					logic.bTouch = true;
					this.invalidate(logic.GetRect());
					if (LinkSee.this.bFirstTouch){
						if (pair.GetElosLogic1() != null || pair.GetElosLogic2() != null){
							Log.d(TAG, "Eloslogic1, 2 in pair are not null");
							return super.onTouchEvent(event); 
						}
						
						pair.SetLogic1(logic);
					}else{
						if (pair.GetElosLogic2() != null){
							Log.d(TAG, "Eloslogic2 in pair are not null");
							return super.onTouchEvent(event); 
						}
						pair.SetLogic2(logic);
					}
				}
			}else if (event.getAction() == MotionEvent.ACTION_UP){
			
			}
			return super.onTouchEvent(event);
		}		
	}
	
	private class CountRoute{
		private Pair pair = new Pair();
		private ArrayList<ElosLogic>lstTrail = new ArrayList<ElosLogic>();
		boolean bLeft ;
		boolean bUp ;
		public CountRoute(){};
		public void SetPair(Pair pair){
			this.pair.SetPair(pair); 
			bLeft = IsLeft(pair.GetElosLogic1(), pair.GetElosLogic2());
			bUp = IsUp(pair.GetElosLogic1(), pair.GetElosLogic2());
		}
		public boolean DetectReachable(){
			ElosLogic logic1 = pair.GetElosLogic1();
			ElosLogic logic2 = pair.GetElosLogic2();
			if (logic1 == null || logic2 == null) return false;
			logic1.bRecursive = true;
			return false;
		}		
		
		//左边的在右边的方块的左边
		private boolean IsLeft(ElosLogic logic1, ElosLogic logic2){
			if (logic1 == null || logic2 == null) return false;
			return logic1.GetRect().left < logic1.GetRect().left;
		}
		
		//左边的在右边的方块的上面
		private boolean IsUp(ElosLogic logic1, ElosLogic logic2){
			if (logic1 == null || logic2 == null) return false;
			return logic1.GetRect().top < logic2.GetRect().top;
		}
	}
	
	//logic2 is not movable
	private boolean IsPairReachable(ElosLogic logic1, ElosLogic logic2){
		if (logic1 == null || logic2 == null){
			Log.d(TAG, "IsPairReachable can't work while logic1,2 are null");
			return false;
		}
		
		
		return false;
	}
	
	
	 class ElosLogic{
		
		boolean bRecursive = false;
		public boolean bPlayer = false;
		public boolean bTouch = false;
		
		//-------------------------------------------
		boolean bDraw = false;
		boolean IsDraw(){ return bDraw; }
		void SetDraw(boolean bDraw){ this.bDraw = bDraw; }
		//-----------------------------------------
		Rect rtElos = new Rect();
		public void SetRect(Rect rt){ rtElos.set(rt); }
		public Rect GetRect(){ return rtElos; }
		//------------------------------------------
		Drawable dr = null;
		public void SetDrawable(Drawable dr){ this.dr = dr; }
		public Drawable GetDrawable(){ return dr; }
		//------------------------
		ElosLogic logicUp = null;
		ElosLogic logicDown = null;
		ElosLogic logicLeft = null;
		ElosLogic logicRight = null;
		//------------------------
		public void SetElosLogicLeft(ElosLogic elosLogic){ logicLeft =  elosLogic;}
		public void SetElosLogicRight(ElosLogic elosLogic){ logicRight =  elosLogic;}
		public void SetElosLogicUp(ElosLogic elosLogic){ logicUp =  elosLogic;}
		public void SetElosLogicDown(ElosLogic elosLogic){ logicDown =  elosLogic;}
		
		public ElosLogic GetElosLogicLeft(){ return  logicLeft; }
		public ElosLogic GetElosLogicRight(){ return  logicRight; }
		public ElosLogic GetElosLogicUp(){ return  logicUp; }
		public ElosLogic GetElosLogicDown(){ return  logicDown; }
		//---------------------------------------------------------------------------
		public ElosLogic(){}
	}
}
