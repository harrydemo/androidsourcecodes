package com.china.square.elossap;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.china.square.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class ElosSap extends Activity {
	String TAG = "ELOS";
	private final int SQUARELEMENT_NUMBERS = 9;
	private ElosView evElos = null;
	
	int nTimeInterval = 500;
	int nCents = 0;
	int nStage = 1;
	
	int nSapBGChoice = 1;
	
	private final int ELOS_HEIGHT = 60;
	private final int ELOS_WIDTH = 50;	
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

	
		

	//ArrayList<ElosLogic>lstTrail = new ArrayList<ElosLogic>();/* 记录路径上的所有的ElosLogic */
	
	private void InitElosLogicGuard(){
		elosGuard = new ElosLogic[nTotalLines][nTotalColumns];
		int i = 0;
		while (i < nTotalLines){
			for (int j = 0; j <nTotalColumns; j++)
				elosGuard[i][j] = new ElosLogic();
			i++;
		}
	}

	Drawable[] drBGArr = new Drawable[4];
	private Drawable drSap = null;
	private void RetriveDrawable(Context c){
		Resources res = c.getResources();
		
		drArr[0] = res.getDrawable(R.drawable.blue);
		drArr[1] = res.getDrawable(R.drawable.green);
		drArr[2] = res.getDrawable(R.drawable.orange);
		drArr[3] = res.getDrawable(R.drawable.purple);
		drArr[4] = res.getDrawable(R.drawable.qing);
		drArr[5] = res.getDrawable(R.drawable.red);
		drArr[6] = res.getDrawable(R.drawable.yellow);
		drArr[7] = res.getDrawable(R.drawable.blue);
		drArr[8] = res.getDrawable(R.drawable.green);
		drBGArr[0] = res.getDrawable(R.drawable.p2);
		drBGArr[1] = res.getDrawable(R.drawable.p1);
		drBGArr[2] = res.getDrawable(R.drawable.p3);
		drBGArr[3] = res.getDrawable(R.drawable.p4);
		drSap = res.getDrawable(R.drawable.sap);
	}
	
	private int GetElosHeight(Drawable dr){
		//return dr.getMinimumHeight();
		return ELOS_HEIGHT;
	}
	private int GetElosWidth(Drawable dr){
		//return dr.getMinimumWidth();
		return ELOS_WIDTH;
	}
	
	private boolean IsSuccessded(){
		Log.d(TAG, "IsSuccessded?");
		int nCount = 0;
		for (int i = 0; i < elosGuard.length; i++)
			for (int j = 0; j < elosGuard[i].length; j++)
				if (!elosGuard[i][j].bTouch)nCount++;
		if (nCount == this.nTotalSaps) return true;
		return false;
	}
	
	Thread thdTimer =  null;
	boolean bReportResult = false;
	final int ELOS_START = Menu.FIRST;
	final int ELOS_SETTING = ELOS_START + 1;
	final int ELOS_ABOUT = ELOS_SETTING + 1;
	final int ELOS_ZEROSHOW = ELOS_ABOUT + 1;
	final int ELOS_BG = ELOS_ZEROSHOW + 1;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, ELOS_START, 0, "开始");
		menu.add(0, ELOS_SETTING, 1, "设置");
		menu.add(0, ELOS_ABOUT, 2, "关于");
		menu.add(0, ELOS_ZEROSHOW, 3, "0显示模式");
		menu.add(0, ELOS_BG, 4, "切换背景");
		return super.onCreateOptionsMenu(menu);
	}

	private String[] strSapCount = new String[20];
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		if (item.getItemId() == ELOS_START){
			MakeElosLogicNet(elosGuard);
			InitGame();
			evElos.invalidate();
		}else if (item.getItemId() == ELOS_SETTING){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setSingleChoiceItems(strSapCount, this.nTotalSaps - 1, listener);
			builder.setTitle("请选择地雷个数");
			builder.setIcon(drSap);
			builder.setNeutralButton("确定", null);
			builder.show();
		}else if (item.getItemId() == ELOS_ABOUT){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("扫雷1.0\nCopyRight (C) ALL RIGHTS PRESERVED\n2009.01\n作者：Ｐ.Ｐ.流");
			builder.setIcon(drSap);
			builder.setTitle("扫雷");
			builder.setPositiveButton("确定", null);
			builder.show();
		}else if (item.getItemId() == ELOS_ZEROSHOW){
			this.bZeroShowTag = !this.bZeroShowTag;
			evElos.invalidate();
		}else if (item.getItemId() == ELOS_BG){
			this.nSapBGChoice = (nSapBGChoice + 1)%5;
			evElos.invalidate();
		}
		
		return super.onOptionsItemSelected(item);
	}
	private DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener(){

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			lstSap.clear();
			nTotalSaps = Integer.parseInt(strSapCount[which]);
			MakeElosLogicNet(elosGuard);
			InitGame();
			
			evElos.invalidate();
		}
		
	};
	
	
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
		for (int i = 0; i < strSapCount.length; i++)strSapCount[i] = "" + (i + 1);
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
	
	private boolean bZeroShowTag = true;
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
		if (elosLogic.nSaps != 0 || !bZeroShowTag || elosLogic.bSap) rt.set(rtDraw);
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
		}else if (elosLogic.nSaps != 0){
			//canvas.drawRoundRect(new RectF(rt.left-1, rt.top-1, rt.right+1, rt.bottom+1), 1, 1, ptFrame);
			canvas.drawLine(rt.left-1, rt.top-1, rt.right+1, rt.top-1, ptFrame);
			canvas.drawLine(rt.right+1, rt.bottom+1, rt.right+1, rt.top-1, ptFrame);
			canvas.drawLine(rt.right+1, rt.bottom+1, rt.left-1, rt.bottom+1, ptFrame);
			canvas.drawLine(rt.left-1, rt.top-1, rt.left-1, rt.bottom+1, ptFrame);
		}
		
		if (elosLogic.bTouch){
			if (!elosLogic.bSap && elosLogic.nSaps != 0){
				int nNum = elosLogic.nSaps;
				ptFrame.setTextSize(20);
				canvas.drawText("" + nNum, rt.centerX() - 5, rt.centerY() - 5, ptFrame);
			}
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
		if (nColumns != elosGuard[0].length){ Log.d(TAG, "guard len is error");}
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
				
				elosGuard[i][j].SetDrawable(drArr[6]);
				//if (i == 0 || i == this.nTotalLines - 1 ||j == this.nTotalColumns - 1 || j == 0)elosGuard[i][j].SetDraw(false);
				elosGuard[i][j].SetDraw(true);
				
				elosGuard[i][j].bSap = false;
				elosGuard[i][j].nSaps = 0;
//				elosGuard[i][j].bIsPressed = false;
				elosGuard[i][j].bTouch = false;
			}		
			i++;
		}
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
			
			
			Rect rt = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
			rtElosArea.set(rt);
			
			if (nSapBGChoice != 4){
				drBGArr[nSapBGChoice].setBounds(rt);
				drBGArr[nSapBGChoice].draw(canvas);
			}else {
				Paint pt = new Paint();
				pt.setColor(Color.WHITE);
				canvas.drawRect(rt, pt);
			}
			
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
				InitGame();
				//evElos.invalidate();
				bReset = false;
				vvvv++;
				
			}
			DrawElosLogicNet(canvas, elosGuard);
			if (bReportResult){
				Toast.makeText(ElosSap.this, "游戏结束,得分：" + nCents, Toast.LENGTH_SHORT).show();vvvv++;
				bReportResult = false;
			}
		}
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			// TODO Auto-generated method stub
			if (event.getAction() == MotionEvent.ACTION_DOWN){
				Log.d(TAG, "onTouchEvent");
				int ix = (int)event.getX();
				int iy = (int)event.getY();
				Point pt = new Point(ix, iy);
				ElosLogic logic = GetElosLogicByPoint(pt);
				if (logic != null && !logic.bTouch){
					logic.bTouch = true;
					if (!logic.bSap)
						logic.SetDrawable(drArr[logic.nSaps]);
					else {
						//game failed
						ReportFail();
						ClearSaps();
					}
					ProcessSapAfterTouch(logic);
					RestoreElosLogicRecursive(logic);
					this.invalidate(logic.GetRect());
					if (IsSuccessded()){
						Log.d(TAG, "Succeeded true");
						ShowSaps();
						ClearSaps();
						ReportWinner();
					}else{
						Log.d(TAG, " Succeeded false");
					}
				}
			}else if (event.getAction() == MotionEvent.ACTION_UP){
			
			}
			Log.d(TAG, "onTouchEvent exited");
			return super.onTouchEvent(event);
		}		
	}
	
	private void ReportWinner(){
		Toast.makeText(this, "你胜利了", Toast.LENGTH_SHORT).show();
	}
	
	private void IncreaseCentAroundSap(ElosLogic sap){
		if (sap == null) return;
		if (sap.GetElosLogicLeft() != null)sap.GetElosLogicLeft().nSaps++;
		if (sap.GetElosLogicRight() != null)sap.GetElosLogicRight().nSaps++;
		if (sap.GetElosLogicUp() != null)sap.GetElosLogicUp().nSaps++;
		if (sap.GetElosLogicDown() != null)sap.GetElosLogicDown().nSaps++;
		
		
		if (sap.GetElosLogicLeft() != null && sap.GetElosLogicLeft().GetElosLogicUp() != null)
			sap.GetElosLogicLeft().GetElosLogicUp().nSaps++;
		if (sap.GetElosLogicLeft() != null && sap.GetElosLogicLeft().GetElosLogicDown() != null)
			sap.GetElosLogicLeft().GetElosLogicDown().nSaps++;
		
		if (sap.GetElosLogicRight() != null && sap.GetElosLogicRight().GetElosLogicUp() != null)
			sap.GetElosLogicRight().GetElosLogicUp().nSaps++;
		if (sap.GetElosLogicRight() != null && sap.GetElosLogicRight().GetElosLogicDown() != null)
			sap.GetElosLogicRight().GetElosLogicDown().nSaps++;
	}
	
	private int nTotalSaps = 10;
	
	private void InitSapElosLogic(ElosLogic logic){
		if (logic == null) return;
		IncreaseCentAroundSap(logic);
	}
	
	//在MakeElosLogicNet之后使用
	private void InitGame(){
		CreateSaps(nTotalSaps);
	}
	
	private ArrayList<Map<String, Integer>> lstSap = new ArrayList<Map<String, Integer>>();
	
	private void CreateSaps(int nTotalSaps){
		Log.d(TAG, "CreateSaps...");
		if (elosGuard == null) return;
		int nLines = elosGuard.length;
		int nCols = elosGuard[0].length;
		int[] result = CreateSapTag(nTotalSaps, nLines*nCols);
		int nLine = 0;
		int nCol = 0;
		
		for (int i = 0; i < result.length; i++){
			nLine = result[i]/nCols;
			nCol = result[i]%nCols;
			elosGuard[nLine][nCol].bSap = true;
			InitSapElosLogic(elosGuard[nLine][nCol]);//初始化周边
			HashMap<String, Integer> map = new  HashMap<String, Integer>();
			map.put("line", (Integer)nLine);
			map.put("col", (Integer)nCol);
			lstSap.add(map);
			Log.d(TAG, "add" + i);
		}
		Log.d(TAG, "CreateSaps exited");
	}
	
	private int[] CreateSapTag(int nTotalSaps, int nRange){
		if (nTotalSaps <= 0 || nRange <= 0) return null;
		int[] result = new int[nTotalSaps];
		int nSap = 0;
		for (int i = 0; i < nTotalSaps; i++){
			nSap = GrantSapTag(nRange);
			while (IsReused(result, i, nSap))nSap = GrantSapTag(nRange);
			result[i] = nSap;
		}
		
		return result;
	}
	
	private boolean IsReused(int[] result, int nIndex, int nSap){
		if (result == null || nIndex <= 0) return false;
		int nLen = result.length;
		if (nIndex >= nLen) return false;
		int i = 0;
		while (i < nIndex){
			if (result[i] == nSap) return true;
			i++;
		}
		return false;
	}
	private int GrantSapTag(int nRange){
		if (nRange <= 0) return 0;
		return (int)(Math.random()*10000)%nRange;
	}
	
	private void ShowSaps(){
		int i = lstSap.size();
		int nLine, nCol;
		Map<String, Integer> map;
		//setTitle("" + i);
		while (i > 0){
			map = lstSap.get(i - 1);
			nLine = (Integer)map.get("line");
			nCol = (Integer)map.get("col");
			elosGuard[nLine][nCol].bTouch = true;
			elosGuard[nLine][nCol].SetDrawable(drSap);
			evElos.invalidate(elosGuard[nLine][nCol].GetRect());
			i--;
		}
	}
	private void ClearSaps(){
		lstSap.clear();
	}
	
	private void ProcessSapAfterTouch(ElosLogic logic){
		Log.d(TAG, "ProcessSapAfterTouch");
		if (logic == null) return;
		if (logic.bSap){
			// game failure
		}else {
			if (logic.nSaps == 0 && !logic.bRecursive){
				logic.bRecursive = true;
				if (!logic.bTouch){
					logic.bTouch = true;
					logic.SetDrawable(drArr[logic.nSaps]);
				}
				if (logic.GetElosLogicLeft() != null && !logic.GetElosLogicLeft().bTouch){
					logic.GetElosLogicLeft().bTouch = true;
					logic.GetElosLogicLeft().SetDrawable(drArr[logic.GetElosLogicLeft().nSaps]);
					evElos.invalidate(logic.GetElosLogicLeft().GetRect());
					ProcessSapAfterTouch(logic.GetElosLogicLeft());
				}
				if (logic.GetElosLogicRight() != null && !logic.GetElosLogicRight().bTouch){
					logic.GetElosLogicRight().bTouch = true;
					logic.GetElosLogicRight().SetDrawable(drArr[logic.GetElosLogicRight().nSaps]);
					evElos.invalidate(logic.GetElosLogicRight().GetRect());
					ProcessSapAfterTouch(logic.GetElosLogicRight());
				}
				if (logic.GetElosLogicUp() != null && !logic.GetElosLogicUp().bTouch){
					logic.GetElosLogicUp().bTouch = true;
					logic.GetElosLogicUp().SetDrawable(drArr[logic.GetElosLogicUp().nSaps]);
					evElos.invalidate(logic.GetElosLogicUp().GetRect());
					ProcessSapAfterTouch(logic.GetElosLogicUp());
				}
				if (logic.GetElosLogicDown() != null && !logic.GetElosLogicDown().bTouch){
					logic.GetElosLogicDown().bTouch = true;
					logic.GetElosLogicDown().SetDrawable(drArr[logic.GetElosLogicDown().nSaps]);
					evElos.invalidate(logic.GetElosLogicDown().GetRect());
					ProcessSapAfterTouch(logic.GetElosLogicDown());
				}
				
				if (logic.GetElosLogicLeft() != null && logic.GetElosLogicLeft().GetElosLogicUp() != null
						&& !logic.GetElosLogicLeft().GetElosLogicUp().bTouch){
					logic.GetElosLogicLeft().GetElosLogicUp().bTouch = true;
					logic.GetElosLogicLeft().GetElosLogicUp().SetDrawable(drArr[logic.GetElosLogicLeft().GetElosLogicUp().nSaps]);
					evElos.invalidate(logic.GetElosLogicLeft().GetElosLogicUp().GetRect());
					ProcessSapAfterTouch(logic.GetElosLogicLeft().GetElosLogicUp());
				}
				if (logic.GetElosLogicRight() != null && logic.GetElosLogicRight().GetElosLogicUp() != null
						&& !logic.GetElosLogicRight().GetElosLogicUp().bTouch){
					logic.GetElosLogicRight().GetElosLogicUp().bTouch = true;
					logic.GetElosLogicRight().GetElosLogicUp().SetDrawable(drArr[logic.GetElosLogicRight().GetElosLogicUp().nSaps]);
					evElos.invalidate(logic.GetElosLogicRight().GetElosLogicUp().GetRect());
					ProcessSapAfterTouch(logic.GetElosLogicRight().GetElosLogicUp());
				}
				
				if (logic.GetElosLogicLeft() != null && logic.GetElosLogicLeft().GetElosLogicDown() != null
						&& !logic.GetElosLogicLeft().GetElosLogicDown().bTouch){
					logic.GetElosLogicLeft().GetElosLogicDown().bTouch = true;
					logic.GetElosLogicLeft().GetElosLogicDown().SetDrawable(drArr[logic.GetElosLogicLeft().GetElosLogicDown().nSaps]);
					evElos.invalidate(logic.GetElosLogicLeft().GetElosLogicDown().GetRect());
					ProcessSapAfterTouch(logic.GetElosLogicLeft().GetElosLogicDown());
				}
				if (logic.GetElosLogicRight() != null && logic.GetElosLogicRight().GetElosLogicDown() != null
						&& !logic.GetElosLogicRight().GetElosLogicDown().bTouch){
					logic.GetElosLogicRight().GetElosLogicDown().bTouch = true;
					logic.GetElosLogicRight().GetElosLogicDown().SetDrawable(drArr[logic.GetElosLogicRight().GetElosLogicDown().nSaps]);
					evElos.invalidate(logic.GetElosLogicRight().GetElosLogicDown().GetRect());
					ProcessSapAfterTouch(logic.GetElosLogicRight().GetElosLogicDown());
				}
			}
		}
		Log.d(TAG, "ProcessSapAfterTouch exited");
	}
	
	private void RestoreElosLogicRecursive(ElosLogic logic){
		if (logic == null) return;
		if (logic.bRecursive){
			logic.bRecursive = false;
			RestoreElosLogicRecursive(logic.GetElosLogicLeft());
			RestoreElosLogicRecursive(logic.GetElosLogicRight());
			RestoreElosLogicRecursive(logic.GetElosLogicUp());
			RestoreElosLogicRecursive(logic.GetElosLogicDown());
		}
		
	}
	
	private void ReportFail(){
		Toast.makeText(ElosSap.this, "you fail the game", Toast.LENGTH_SHORT).show();
		ShowElosSaps();
	}
	
	private void ShowElosSaps(){
		for (int i = 0; i < elosGuard.length; i++)
			for (int j = 0; j <elosGuard[i].length; j++){
				elosGuard[i][j].bTouch = true;
				if (!elosGuard[i][j].bSap)elosGuard[i][j].SetDrawable(drArr[elosGuard[i][j].nSaps]);
				else elosGuard[i][j].SetDrawable(drSap);
			}
		evElos.invalidate();
	}
	
	 class ElosLogic{
		
		boolean bRecursive = false;
		public boolean bPlayer = false;
		public boolean bTouch = false;
		int nSaps = 0;
		boolean bIsPressed = false;
		boolean bSap = false;
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