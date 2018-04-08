package com.china.square.eloscomplex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.china.square.R;
import com.china.square.elos.Elos;
import com.china.square.elos.SqlDBHelper;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
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

public class ElosComplex extends Activity {
	String TAG = "ELOS";
	private final int SQUARELEMENT_NUMBERS = 9;
	private ElosView evElos = null;
	private ElosStateView esvElos = null;
	
	int nTimeInterval = 500;
	int nCents = 0;
	int nStage = 1;
	
	private boolean bNewPlayer = true;
	
	boolean bPlaying = true;
	
	int nElosYOffset = 0;
	int nElosXOffset = 0;
	
	int nPlayerChoice = 0;
	int nDrawableChoice = 0;
	int ELOS_CHOICES = 35;
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
	
	private Drawable[] drArrCartoonBG = null;
	private Drawable[] drArrBeautyBG = null;
	private Drawable[] drArrPPBG = null;
	private final int CARTOON_COUNT = 7;
	private final int BEAUTY_COUNT = 8;
	private final int PPBG_COUNT = 9;
	private boolean bDrawBG = true;
	private int nCartoonChoice = 0;
	private int nBeautyChoice = 0;
	private int nPPChoice = 0;
	private int nBGCategory = 0;
	private final int CATEGORY_COUNT = 3;
	
	ArrayList<Map> listRecord = new ArrayList<Map>();
	SqlDBHelper dbHelper = null;
	
	private void InitBackGroundDrawable(Context c){
		Resources res = c.getResources();
		drArrCartoonBG = new Drawable[CARTOON_COUNT];
		drArrCartoonBG[0] = res.getDrawable(R.drawable.p1);
		drArrCartoonBG[1] = res.getDrawable(R.drawable.p2);
		drArrCartoonBG[3] = res.getDrawable(R.drawable.p3);
		
		drArrCartoonBG[4] = res.getDrawable(R.drawable.p4);
		drArrCartoonBG[5] = res.getDrawable(R.drawable.p1);
		drArrCartoonBG[6] = res.getDrawable(R.drawable.p2);
		drArrCartoonBG[2] = res.getDrawable(R.drawable.p3);
		
		drArrBeautyBG = new Drawable[BEAUTY_COUNT];
		drArrBeautyBG[0] = res.getDrawable(R.drawable.p4);
		drArrBeautyBG[1] = res.getDrawable(R.drawable.p3);
		drArrBeautyBG[2] = res.getDrawable(R.drawable.p1);
		drArrBeautyBG[3] = res.getDrawable(R.drawable.p3);
		drArrBeautyBG[4] = res.getDrawable(R.drawable.p1);
		drArrBeautyBG[5] = res.getDrawable(R.drawable.p4);
		drArrBeautyBG[6] = res.getDrawable(R.drawable.p3);
		drArrBeautyBG[7] = res.getDrawable(R.drawable.p2);
		
		drArrPPBG = new Drawable[PPBG_COUNT];
		drArrPPBG[0] = res.getDrawable(R.drawable.p4);
		drArrPPBG[1] = res.getDrawable(R.drawable.p1);
		drArrPPBG[8] = res.getDrawable(R.drawable.p3);
		drArrPPBG[2] = res.getDrawable(R.drawable.p2);
		drArrPPBG[3] = res.getDrawable(R.drawable.p1);
		drArrPPBG[4] = res.getDrawable(R.drawable.p2);
		drArrPPBG[5] = res.getDrawable(R.drawable.p4);
		drArrPPBG[6] = res.getDrawable(R.drawable.p1);
		drArrPPBG[7] = res.getDrawable(R.drawable.p3);	
		
	}
	
	private void GetRecordFromDatabase(ArrayList<Map> al, SqlDBHelper dbHelper){
		Log.d(TAG, "get record from dababase");
		if (dbHelper == null) return;
		
		Cursor cursor = dbHelper.query();
		if (cursor == null) return;
		
		int nCount = cursor.getCount();
		if (nCount <= 0) {Log.d(TAG, "nCount <= 0"); return;}
		int i = 0;
		cursor.moveToFirst();
		if(al.size()>0) al.clear();
		do{
			Log.d(TAG, "get from dababase...");
			String[] result = cursor.getColumnNames();
			
			Map map = new HashMap();
			map.put("name", result[1]);
			map.put("cent", result[2]);			
			al.add(map);
			Log.d(TAG, "Get Record...");
		}while (cursor.moveToNext());
		Log.d(TAG, "finish dababase visit");
	}
	private void SaveRecordToDatabase(ArrayList<Map> al, SqlDBHelper dbHelper){
		Log.d(TAG, "SaveRecordToDatabase");
		if (dbHelper == null) return;
		
		Cursor cursor = dbHelper.query();
		if (cursor == null){Log.d(TAG, "cursor is null"); return;}
		int nLen = al.size();
		if (nLen <= 0) { Log.d(TAG, "recordList is null"); return;}
		if (cursor.getCount() == 0){Log.d(TAG, "cursor is zero");
			for (int i = 0; i < nLen; i++){
				Log.d(TAG, "insert...");
				dbHelper.insert(i, (String)al.get(i).get("name"), (String)al.get(i).get("cent"));
			}
		}else if (cursor.getCount() > 0){Log.d(TAG, "cursor count " + cursor.getCount());
			for (int i = cursor.getCount(); i > 0; i--){
				dbHelper.delete("" + i);
			}
			for (int i = 0; i < nLen; i++){
				dbHelper.insert(i, (String)al.get(i).get("name"), (String)al.get(i).get("cent"));
			}			
		}else{
			
		}
		Log.d(TAG, "finish save work");
	}
	
	void DeleteResult(){
		if (dbHelper == null) return;
		
		Cursor cursor = dbHelper.query();
		int i = cursor.getCount();
		while (i >= 0){
			dbHelper.delete("" + i);
			i--;
		}
	}
	private void AddRecord(ArrayList<Map> al, Map record){
		int nLen = al.size();
		int nCurCent = Integer.parseInt((String)record.get("cent"));
		Log.d(TAG, "" + nCurCent);
		if (nCurCent <= 0) return;
		if (nLen == 0)al.add(record);
		else{
			int nCent = 0;
			String name = "";
			boolean bIsInserted = false;
			
			 for (int i = 0; i < nLen; i++){
				 nCent = Integer.parseInt((String)al.get(i).get("cent"));
				 name = (String)al.get(i).get("name");
				 if (nCent < nCurCent){
					 al.add(i, record);
					 bIsInserted = true;
					 break;
				 }
			 }
			 if (!bIsInserted)al.add(record);
			 if (al.size() > 10){
				 al.remove(10);
			 }
		}
	}
	//keyArr == {"name", "cent"}
	private void SaveRecord(ArrayList<Map> al){
		SharedPreferences sp = this.getPreferences(MODE_WORLD_WRITEABLE);
		Editor et = sp.edit();
		for (int i = 0; i < al.size(); i++){
			et.putString("ELOS" + i, (String)al.get(i).get("name") + "," + (String)al.get(i).get("cent"));
				
		}
		
		et.commit();
		Log.d(TAG, "Record saved");
	}
	
	//keyArr == {"name", "cent"}
	private void GetRecordFromPref(ArrayList<Map> al){
		SharedPreferences sp = this.getPreferences(MODE_WORLD_WRITEABLE);
		//Editor et = sp.edit()\
		int i = 0;
		while(true){
			String strRecord = sp.getString("ELOS" + i, null);
			if (strRecord == null) return;
			String[] strResult;
			strResult = strRecord.split(",");
			if (strResult.length <= 2)return;
			Map map = new HashMap();
			map.put("name", strResult[0]);
			map.put("cent", strResult[1]);
			AddRecord(al, map);
			i++;
			Log.d(TAG, "Get Record...");
		}
		
	}
	
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
		return dr.getMinimumHeight();
	}
	private int GetElosWidth(Drawable dr){
		return dr.getMinimumWidth();
	}
	
	private void InvalidateStage(){
		nStage = nCents/10000 + 1;
		
	}
	int nOldStage = 0;
	private void AnnounceNewStage(){
		if (nOldStage != nStage){
			Toast.makeText(this, "第" + nStage + "关", Toast.LENGTH_SHORT).show();
			nTimeInterval = nTimeInterval - (nStage - 1)*50;
			nOldStage = nStage;
		}
	}
	private boolean bPause = false;
	Thread thdTimer =  null;
	boolean bReportResult = false;
	private class  TimerThread extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			
			while (bPlaying){
				while (bPause){
					
				}
				esvElos.postInvalidate();
				try {
					sleep(nTimeInterval);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (IsElosGameFail()){	
					bPlaying = false;
					bReportResult = true;
				
					Map map = new HashMap();
					map.put("name", "noname");
					map.put("cent", "" + ElosComplex.this.nCents);
					AddRecord(ElosComplex.this.listRecord, map);
					evElos.postInvalidate(); 
					esvElos.postInvalidate();
					while (!bPlaying){
						try {
							sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} 
					
				}
				if (!IsPlayerMoveableDown(player)){
					ClearElosLogicUnitUnitTag(player);
					int nErasableLine = CheckGuard(player);
					if (nErasableLine > 0)nCents += 100*nErasableLine + (50*nStage)*(nErasableLine - 1);
					InvalidateStage();					
					player = CreatePlayer(drArr[GetRandomDrawableChoice()], GetRandomPlayerChoice());					
				}else{
					player = PlayerMoveDown(player, nPlayerChoice);
					
				}
				evElos.postInvalidate();
			}
		}		
	}
	
	
	int nTest = 0;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub		
		if (event.getAction() == MotionEvent.ACTION_DOWN)ElosTransform(player);
		return super.onTouchEvent(event);
	}
	final int ELOS_START = Menu.FIRST;
	final int ELOS_CHANGEBG = ELOS_START + 1;
	final int ELOS_CHANGECAT = ELOS_CHANGEBG + 1;
	final int ELOS_BGCHOICE = ELOS_CHANGECAT + 1;
	final int ELOS_EXIT = ELOS_BGCHOICE + 1;
	final int ELOS_PAUSE = ELOS_EXIT + 1;
	private Menu menuMain = null;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(1, ELOS_START, 0, "开始");
		menu.add(0, ELOS_CHANGEBG, 1, "更换背景");
		menu.add(0, ELOS_CHANGECAT, 2, "更换背景主题");
		menu.add(1, ELOS_BGCHOICE, 3, this.bDrawBG?"禁用背景":"启用背景");
		menu.add(1, ELOS_EXIT, 4, "退出");
		menu.add(1, ELOS_PAUSE, 5, "暂停");
		menuMain = menu;
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		if (item.getItemId() == ELOS_START){
			bPlaying = true;
			MakeElosLogicNet();
			ClearStageAndCentsAfterFail();
			player = CreatePlayer(drArr[GetRandomDrawableChoice()], GetRandomPlayerChoice());
			if (thdTimer == null){
				this.thdTimer = new TimerThread();
				thdTimer.start();
			}else{
			}
			
			AnnounceNewStage();
		}else if (item.getItemId() == ELOS_CHANGEBG){
			if (this.nBGCategory == 0){//cartoon
				this.nCartoonChoice++;
				if (nCartoonChoice >= this.CARTOON_COUNT)nCartoonChoice = 0;
			}else if (this.nBGCategory == 1){
				this.nBeautyChoice++;
				if (nBeautyChoice >= this.BEAUTY_COUNT)nBeautyChoice = 0;
			}else if (this.nBGCategory == 2){
				this.nPPChoice++;
				if (nPPChoice >= this.PPBG_COUNT)nPPChoice = 0;
			}
			evElos.invalidate();
		}else if (item.getItemId() == ELOS_CHANGECAT){
			this.nBGCategory++;
			if (nBGCategory >= CATEGORY_COUNT)nBGCategory = 0;
			evElos.invalidate();
		}else if (item.getItemId() == ELOS_BGCHOICE){
			bDrawBG = !bDrawBG;
			if (bDrawBG){
				menuMain.setGroupEnabled(0, true);
			}else menuMain.setGroupEnabled(0, false);
			
			item.setTitle(bDrawBG?"禁用背景":"启用背景");
			evElos.invalidate();
		}else if (item.getItemId() == ELOS_EXIT){
			this.finish();
		}else if (item.getItemId() == ELOS_PAUSE){
			if (item.getTitle().equals("暂停")){
				bPause = true;
				item.setTitle("继续");
				evElos.setEnabled(true);
			}else {
				bPause = false;
				item.setTitle("暂停");
				evElos.setEnabled(false);
			}
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	
	
	private boolean IsElosGameFail(){
		ElosLogic visit = elosGuard[2][0];
		while (visit != null){
			if (visit.IsDraw() && !visit.bUnit) return true;
			visit = visit.GetElosLogicRight();
		}
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
		params.weight = 1;
		ll.addView(evElos = new ElosView(this), params);
		params = new LinearLayout.LayoutParams(params);
		params.weight = 4;
		ll.addView(esvElos = new ElosStateView(this), params);
		
		RetriveDrawable(this);
		nElosHeight = GetElosHeight(drArr[0]);
		nElosWidth = GetElosWidth(drArr[0]);
		nElosYOffset = nElosHeight;		
		InitBackGroundDrawable(this);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == 21){//left
			if (!bPause) 
			player = PlayerMoveLeft(player, nPlayerChoice);
		}else if (keyCode == 22){//right
			if (!bPause) 
			player = PlayerMoveRight(player, nPlayerChoice);
		}else if (keyCode == 19){//up
			if (!bPause) 
			ElosTransform(player);
		}else if (keyCode == 20){//down
			if (!bPause) 
				if (this.IsPlayerMoveableDown(player))
					player = PlayerMoveDown(player, nPlayerChoice);
		}else if (4 == keyCode){
			if (!bPause) 
			if (player != null)
			ElosTransform(player);
			
		}
		if (keyCode == 4 || keyCode == 21 || keyCode == 22 || keyCode == 19 || keyCode == 20){
			evElos.invalidate();
			AnnounceNewStage();
		}
		if (keyCode == 4) return true;
		return super.onKeyDown(keyCode, event);
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
		if (elosLogic.bUnit) dr.setBounds(rt);
		else dr.setBounds(rtDraw);
		dr.draw(canvas);
		
		if (!elosLogic.bUnit){
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
	
	private void MakeElosLogicNet(){
		int nLen = elosGuard.length;
		if (nLen != this.nTotalLines){ Log.d(TAG, "guard len is error"); }
		int nColumns = this.nTotalColumns;
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
				
				elosGuard[i][j].SetDrawable(drArr[0]);
				
				elosGuard[i][j].SetDraw(false);
				elosGuard[i][j].bUnit = false;
			}		
			i++;
		}
	}
	
	private int GetRandomPlayerChoice(){		
		return nPlayerChoice = ((int)(Math.random()*10000))%ELOS_CHOICES;
	}
	
	private int GetRandomDrawableChoice(){		
		return nDrawableChoice = ((int)(Math.random()*10000))%DRAWABLE_CHOICES;
	}
	
	private ElosLogic CreatePlayer(Drawable dr, int nChoice){
		player = elosGuard[2][this.nTotalColumns/2];
		AssembleElosUnitType(player, dr, nChoice);
		return player;
	}
	
	private ElosLogic CreatePlayer(Drawable dr, int nChoice, ElosLogic elosLogic){
		//player = elosLogic;
		AssembleElosUnitType(elosLogic, dr, nChoice);
		return elosLogic;
	}
	
	private void ClearPlayer(ElosLogic elosPlayer){
	//	if (!elosPlayer.bUnit) return;
		elosPlayer.bUnit = false;
		elosPlayer.SetDraw(false);
		elosPlayer.nNumber = -1;
		elosPlayer.dr = elosPlayer.keep;
		if (elosPlayer.GetElosLogicLeft() != null && elosPlayer.GetElosLogicLeft().bUnit)ClearPlayer(elosPlayer.GetElosLogicLeft());
		if (elosPlayer.GetElosLogicRight() != null && elosPlayer.GetElosLogicRight().bUnit)ClearPlayer(elosPlayer.GetElosLogicRight());
		if (elosPlayer.GetElosLogicUp() != null && elosPlayer.GetElosLogicUp().bUnit)ClearPlayer(elosPlayer.GetElosLogicUp());
		if (elosPlayer.GetElosLogicDown() != null && elosPlayer.GetElosLogicDown().bUnit)ClearPlayer(elosPlayer.GetElosLogicDown());
	}
	
	private boolean IsPlayerMoveableLeft(ElosLogic elosPlayer){
		if (elosPlayer == null) return false;
		
		return (IsRecursiveElosLogicLeftable(elosPlayer));
	}
	
	private boolean IsRecursiveElosLogicLeftable(ElosLogic elosLogic){
		if (!IsElosLogicLeftable(elosLogic)) return false;
		elosLogic.bUnit = false;
		if (elosLogic.GetElosLogicLeft() != null && elosLogic.GetElosLogicLeft().bUnit)if(!IsRecursiveElosLogicLeftable(elosLogic.GetElosLogicLeft())) {elosLogic.bUnit = true;return false;}
		if (elosLogic.GetElosLogicUp() != null && elosLogic.GetElosLogicUp().bUnit)if(!IsRecursiveElosLogicLeftable(elosLogic.GetElosLogicUp())) {elosLogic.bUnit = true;return false;}
		if (elosLogic.GetElosLogicDown() != null && elosLogic.GetElosLogicDown().bUnit)if(!IsRecursiveElosLogicLeftable(elosLogic.GetElosLogicDown())) {elosLogic.bUnit = true;return false;}
		elosLogic.bUnit = true;
		return true;
	}
	
	private boolean IsElosLogicLeftable(ElosLogic logic){
		if (logic == null) return false;
		if (logic.GetElosLogicLeft() == null) return false;
		if (logic.GetElosLogicLeft().bUnit) return true;
		if (logic.GetElosLogicLeft().bDraw) return false;
		return true;
	}
	
	private boolean IsPlayerMoveableRight(ElosLogic elosPlayer){
		if (elosPlayer == null) return false;
		
		return (IsRecursiveElosLogicRightable(elosPlayer));
	}
	
	private boolean IsRecursiveElosLogicRightable(ElosLogic elosLogic){
		if (!IsElosLogicRightable(elosLogic)) return false;
		elosLogic.bUnit = false;
		if (elosLogic.GetElosLogicRight() != null && elosLogic.GetElosLogicRight().bUnit)if(!IsRecursiveElosLogicRightable(elosLogic.GetElosLogicRight())) {elosLogic.bUnit = true;return false;}
		if (elosLogic.GetElosLogicUp() != null && elosLogic.GetElosLogicUp().bUnit)if(!IsRecursiveElosLogicRightable(elosLogic.GetElosLogicUp())) {elosLogic.bUnit = true;return false;}
		if (elosLogic.GetElosLogicDown() != null && elosLogic.GetElosLogicDown().bUnit)if(!IsRecursiveElosLogicRightable(elosLogic.GetElosLogicDown())) {elosLogic.bUnit = true;return false;}
		elosLogic.bUnit = true;
		return true;
	}
	
	private boolean IsElosLogicRightable(ElosLogic logic){
		if (logic == null) return false;
		if (logic.GetElosLogicRight() == null) return false;
		if (logic.GetElosLogicRight().bUnit) return true;
		if (logic.GetElosLogicRight().bDraw) return false;
		return true;
	}
	
	private boolean IsPlayerMoveableDown(ElosLogic elosPlayer){
		if (elosPlayer == null) return false;
		return (IsRecursiveElosLogicDownable(elosPlayer));
	}
	
	private ElosLogic PlayerMoveDown(ElosLogic elosPlayer, int nPlayerChoice){		
		if (elosPlayer == null) return elosPlayer;
		if (!IsPlayerMoveableDown(elosPlayer)) {return elosPlayer; }
		Drawable dr  = elosPlayer.GetDrawable();
		ClearPlayer(elosPlayer);
		elosPlayer = elosPlayer.GetElosLogicDown();
		elosPlayer = CreatePlayer(dr, nPlayerChoice, elosPlayer);
		//setTitle("move");
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
	
	boolean IsRecursiveElosLogicDownable(ElosLogic elosLogic){
		if (!IsElosLogicDownable(elosLogic)) return false;
		elosLogic.bUnit = false;
		if (elosLogic.GetElosLogicLeft() != null && elosLogic.GetElosLogicLeft().bUnit)if(!IsRecursiveElosLogicDownable(elosLogic.GetElosLogicLeft())) {elosLogic.bUnit = true;return false;}
		if (elosLogic.GetElosLogicRight() != null && elosLogic.GetElosLogicRight().bUnit)if(!IsRecursiveElosLogicDownable(elosLogic.GetElosLogicRight())) {elosLogic.bUnit = true;return false;}
		if (elosLogic.GetElosLogicDown() != null && elosLogic.GetElosLogicDown().bUnit)if(!IsRecursiveElosLogicDownable(elosLogic.GetElosLogicDown())) {elosLogic.bUnit = true;return false;}
		elosLogic.bUnit = true;
		return true;
	}
	
	int CheckGuard(ElosLogic elosPlayer){
		int nErasableLines = 0;
		Log.d(TAG, "Checkguard");
		if (elosPlayer == null) return nErasableLines;
		if (IsWholeLineDrawed(elosPlayer)){ClearWholeLine(elosPlayer); ElosLogicLineCopyAfterLineFull(elosPlayer);nErasableLines++;}
		if (elosPlayer.GetElosLogicDown() != null){
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
		if (elosPlayer.GetElosLogicUp() != null){
			if (IsWholeLineDrawed(elosPlayer.GetElosLogicUp())){
				ClearWholeLine(elosPlayer.GetElosLogicUp());ElosLogicLineCopyAfterLineFull(elosPlayer.GetElosLogicUp());
				nErasableLines++;
			}
		}
		
		Log.d(TAG, "Checkguard finished");
		return nErasableLines;
	}
	
	private void ElosLogicLineCopyAfterLineFull(ElosLogic logic){
		Rect rtElos = logic.GetRect();
		Rect rtGuardZero = elosGuard[0][0].GetRect();
		int nLineNumber = (rtElos.top - rtGuardZero.top)/rtElos.height();
		while (nLineNumber >= 1){
			Log.d(TAG, "Loop in Copy");
				Log.d(TAG, "Line:" + nLineNumber + 1 + "empty");
				for (int i = 0; i < this.nTotalColumns; i++){
					ElosLogicCopyAfterLineFull(elosGuard[nLineNumber - 1][i], elosGuard[nLineNumber][i]);
				}
			nLineNumber--;
		}
	}
	
	private boolean IsElosLineEmpty(ElosLogic logic){
		ElosLogic keep1, keep2;
		keep1 = keep2 = logic;
		if (logic == null) return false;
		
		while (keep1 != null){
			if (keep1.bDraw) return false;
			keep1 = keep1.GetElosLogicLeft();
		}
		keep2 = keep2.GetElosLogicRight();
		while (keep2 != null){
			if (keep2.bDraw) return false;
			keep2 = keep2.GetElosLogicRight();
		}
		
		return true;
	}
	
	private void ElosLogicCopyAfterLineFull(ElosLogic src, ElosLogic des){
		des.bDraw = src.bDraw;
		src.bDraw = false;
		des.SetDrawable(src.GetDrawable());
	}
	
	private void ClearWholeLine(ElosLogic logic){
		Log.d(TAG, "ClearWholeLine");
		if (logic == null) return;
		ElosLogic keep1, keep2;
		keep1 = keep2 = logic;
		while (keep1 != null){
			keep1.SetDraw(false);
			keep1 = keep1.GetElosLogicLeft();
		}
		
		while((keep2 = keep2.GetElosLogicRight()) != null){
			keep2.SetDraw(false);
		}
		Log.d(TAG, "ClearWholeLine ended");
	}
	private boolean IsWholeLineDrawed(ElosLogic logic){
		Log.d(TAG, "IsWholeLineDrawed");
		if (logic == null) return false;
		Log.d(TAG, "ElosLogic is ok");
		ElosLogic keep1, keep2;
		keep1 = keep2 = logic;
		int nCount = 0;
		
		while (keep1 != null){
			Log.d(TAG, "in loop1");
			if (keep1.IsDraw())nCount++;
			keep1 = keep1.GetElosLogicLeft();
		}
		keep2 = keep2.GetElosLogicRight();
		while(keep2 != null){
			Log.d(TAG, "in loop2");
			if (keep2.IsDraw())nCount++;
			keep2 = keep2.GetElosLogicRight();
		}
		Log.d(TAG, "out of loop");
		if (nCount == this.nTotalColumns) return true;
		return false;
	}
	
	private void ClearElosLogicUnitUnitTag(ElosLogic elosPlayer){
		if (elosPlayer == null) return;
		Log.d(TAG, "clear unit tag");
		elosPlayer.bUnit = false;
		if (elosPlayer.GetElosLogicLeft() != null && elosPlayer.GetElosLogicLeft().bUnit)ClearElosLogicUnitUnitTag(elosPlayer.GetElosLogicLeft());
		if (elosPlayer.GetElosLogicRight() != null && elosPlayer.GetElosLogicRight().bUnit)ClearElosLogicUnitUnitTag(elosPlayer.GetElosLogicRight());
		if (elosPlayer.GetElosLogicUp() != null && elosPlayer.GetElosLogicUp().bUnit)ClearElosLogicUnitUnitTag(elosPlayer.GetElosLogicUp());
		if (elosPlayer.GetElosLogicDown() != null && elosPlayer.GetElosLogicDown().bUnit)ClearElosLogicUnitUnitTag(elosPlayer.GetElosLogicDown());
	}
	
	private void ClearElosLogicUnitTag(ElosLogic logic){
		if (logic == null) return;
		logic.bUnit = false;
	}
	
	private boolean IsElosLogicDownable(ElosLogic logic){
		if (logic == null){ return false;}
		if (logic.GetElosLogicDown() == null) {  return false;}
		if (logic.GetElosLogicDown().bUnit) return true;
		if (logic.GetElosLogicDown().IsDraw()) { return false;}
		return true;
	}
	
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
		else if (nChoice == 15){AssembleElosUnitType16(elosLogic, dr);}
		else if (nChoice == 16){AssembleElosUnitType17(elosLogic, dr);}
		else if (nChoice == 17){AssembleElosUnitType18(elosLogic, dr);}
		else if (nChoice == 18){AssembleElosUnitType19(elosLogic, dr);}
		else if (nChoice == 19){AssembleElosUnitType20(elosLogic, dr);}
		else if (nChoice == 20){AssembleElosUnitType21(elosLogic, dr);}
		else if (nChoice == 21){AssembleElosUnitType22(elosLogic, dr);}
		else if (nChoice == 22){AssembleElosUnitType23(elosLogic, dr);}
		else if (nChoice == 23){AssembleElosUnitType24(elosLogic, dr);}
		else if (nChoice == 24){AssembleElosUnitType25(elosLogic, dr);}
		else if (nChoice == 25){AssembleElosUnitType26(elosLogic, dr);}
		else if (nChoice == 26){AssembleElosUnitType27(elosLogic, dr);}
		else if (nChoice == 27){AssembleElosUnitType28(elosLogic, dr);}
		else if (nChoice == 28){AssembleElosUnitType29(elosLogic, dr);}
		else if (nChoice == 29){AssembleElosUnitType30(elosLogic, dr);}
		else if (nChoice == 30){AssembleElosUnitType31(elosLogic, dr);}
		else if (nChoice == 31){AssembleElosUnitType32(elosLogic, dr);}
		else if (nChoice == 32){AssembleElosUnitType33(elosLogic, dr);}
		else if (nChoice == 33){AssembleElosUnitType34(elosLogic, dr);}
		else if (nChoice == 34){AssembleElosUnitType35(elosLogic, dr);}
		
		
	}
	
	private void AssignElosLogic(ElosLogic elosLogic, Drawable dr){
		if (elosLogic == null) { Log.d(TAG, "logic is null"); return;}
		elosLogic.keep = elosLogic.dr;
		elosLogic.dr = dr;
		elosLogic.bUnit = true;
		elosLogic.SetDraw(true);
	}
	
	//NNNNNNNN
	private void AssembleElosUnitType1(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 1;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft().GetElosLogicLeft(), dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft(), dr);
		AssignElosLogic(elosLogic.GetElosLogicRight(), dr);
	}
	
	//NNNNNN
	//     N
	private void AssembleElosUnitType2(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 2;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft().GetElosLogicLeft(), dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft(), dr);
		AssignElosLogic(elosLogic.GetElosLogicDown(), dr);
	}
	
	//NNNNNNNN
	//   N
	private void AssembleElosUnitType3(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 3;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft(), dr);
		AssignElosLogic(elosLogic.GetElosLogicDown(), dr);
		AssignElosLogic(elosLogic.GetElosLogicRight(), dr);
	}
	
	//NNNNNNNN
	//N
	private void AssembleElosUnitType4(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 4;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicRight(), dr);
		AssignElosLogic(elosLogic.GetElosLogicRight().GetElosLogicRight(), dr);
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
	
	//  N
	//  N
	//NNN
	private void AssembleElosUnitType7(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 7;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicUp(), dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft(), dr);
		AssignElosLogic(elosLogic.GetElosLogicUp().GetElosLogicUp(), dr);
	}
	
	//N
	//NNNNN
	private void AssembleElosUnitType8(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 8;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicRight(), dr);
		AssignElosLogic(elosLogic.GetElosLogicRight().GetElosLogicRight(), dr);
		AssignElosLogic(elosLogic.GetElosLogicUp(), dr);
	}
	
	//NNN
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
	
	//  N
	//NNN
	//  N
	private void AssembleElosUnitType10(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 10;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicUp(), dr);
		AssignElosLogic(elosLogic.GetElosLogicDown(), dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft(), dr);
	}
	
	//  N
	//NNNNN
	private void AssembleElosUnitType11(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 11;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicUp(), dr);
		AssignElosLogic(elosLogic.GetElosLogicRight(), dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft(), dr);
	}
	
	//N
	//NNN
	//N
	private void AssembleElosUnitType12(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 12;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicUp(), dr);
		AssignElosLogic(elosLogic.GetElosLogicRight(), dr);
		AssignElosLogic(elosLogic.GetElosLogicDown(), dr);
	}
	
	//NNN
	//  N
	//  N
	private void AssembleElosUnitType13(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 13;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft(), dr);
		AssignElosLogic(elosLogic.GetElosLogicDown(), dr);
		AssignElosLogic(elosLogic.GetElosLogicDown().GetElosLogicDown(), dr);
	}
	
	//   N
	//NNNN
	private void AssembleElosUnitType14(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 14;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicUp(), dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft().GetElosLogicLeft(), dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft(), dr);
	}
	
	//  N
	//  N
	//  NNN
	private void AssembleElosUnitType15(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 15;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicUp(), dr);
		AssignElosLogic(elosLogic.GetElosLogicUp().GetElosLogicUp(), dr);
		AssignElosLogic(elosLogic.GetElosLogicRight(), dr);
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
	//NN
	// N
	private void AssembleElosUnitType16(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 16;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft(), dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft().GetElosLogicUp(), dr);
		AssignElosLogic(elosLogic.GetElosLogicDown(), dr);
	}
	
	//   NNN
	// NNN
	// 
	private void AssembleElosUnitType17(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 17;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicUp(), dr);
		AssignElosLogic(elosLogic.GetElosLogicUp().GetElosLogicRight(), dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft(), dr);
	}
	
	//   N
	//  NN
	//  N
	private void AssembleElosUnitType18(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 18;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicUp(), dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft(), dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft().GetElosLogicDown(), dr);
	}
	
	//  NNNN
	//     NNNN
	//  
	private void AssembleElosUnitType19(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 19;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicUp(), dr);
		AssignElosLogic(elosLogic.GetElosLogicUp().GetElosLogicLeft(), dr);
		AssignElosLogic(elosLogic.GetElosLogicRight(), dr);
	}
	
	//   N
	// NNNNN
	//   N
	private void AssembleElosUnitType20(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 20;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicUp(), dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft(), dr);
		AssignElosLogic(elosLogic.GetElosLogicRight(), dr);
		AssignElosLogic(elosLogic.GetElosLogicDown(), dr);
	}
	
	//N   N
	//NNNNN
	private void AssembleElosUnitType21(ElosLogic elosLogic, Drawable dr){
		Log.d(TAG, "AssembleElosUnitType21");
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 21;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft(), dr);
		AssignElosLogic(elosLogic.GetElosLogicRight(), dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft().GetElosLogicUp(), dr);
		AssignElosLogic(elosLogic.GetElosLogicRight().GetElosLogicUp(), dr);
	}
	
	//NNNN
	//N
	//NNNN
	private void AssembleElosUnitType22(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 22;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicUp(), dr);
		AssignElosLogic(elosLogic.GetElosLogicDown(), dr);
		AssignElosLogic(elosLogic.GetElosLogicUp().GetElosLogicRight(), dr);
		AssignElosLogic(elosLogic.GetElosLogicDown().GetElosLogicRight(), dr);
	}
	//NNNNNN
	//N    N
	private void AssembleElosUnitType23(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 23;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft(), dr);
		AssignElosLogic(elosLogic.GetElosLogicRight(), dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft().GetElosLogicDown(), dr);
		AssignElosLogic(elosLogic.GetElosLogicRight().GetElosLogicDown(), dr);
	}
	
	//NNNN
	//   N
	//   N
	//   N
	//NNNN
	private void AssembleElosUnitType24(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 24;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicUp(), dr);
		AssignElosLogic(elosLogic.GetElosLogicDown(), dr);
		AssignElosLogic(elosLogic.GetElosLogicUp().GetElosLogicLeft(), dr);
		AssignElosLogic(elosLogic.GetElosLogicDown().GetElosLogicLeft(), dr);
	}
	//---------------------------------------------------------
	
	//N
	// N
	private void AssembleElosUnitType25(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 25;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicUp(), dr);
		AssignElosLogic(elosLogic.GetElosLogicUp().GetElosLogicLeft(), dr);
		elosLogic.GetElosLogicUp().SetDraw(false);
	}
	
	//  N
	// N
	private void AssembleElosUnitType26(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 26;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicUp(), dr);
		AssignElosLogic(elosLogic.GetElosLogicUp().GetElosLogicRight(), dr);
		elosLogic.GetElosLogicUp().SetDraw(false);
	}
	
	//  N
	// 
	private void AssembleElosUnitType27(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 27;
		AssignElosLogic(elosLogic, dr);
	}
	
	//	N
	//NNNNN
	//  N
	//  N	
	private void AssembleElosUnitType28(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 28;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft(), dr);
		AssignElosLogic(elosLogic.GetElosLogicRight(), dr);
		AssignElosLogic(elosLogic.GetElosLogicUp(), dr);
		AssignElosLogic(elosLogic.GetElosLogicDown(), dr);
		AssignElosLogic(elosLogic.GetElosLogicDown().GetElosLogicDown(), dr);
	}
	
	//   N
	//   N
	// NNNNN
	//   N
	private void AssembleElosUnitType29(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 29;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft(), dr);
		AssignElosLogic(elosLogic.GetElosLogicRight(), dr);
		AssignElosLogic(elosLogic.GetElosLogicUp(), dr);
		AssignElosLogic(elosLogic.GetElosLogicDown(), dr);
		AssignElosLogic(elosLogic.GetElosLogicUp().GetElosLogicUp(), dr);
	}
	
	//  N
	//NNNNNNN
	//  N
	private void AssembleElosUnitType30(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 30;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft(), dr);
		AssignElosLogic(elosLogic.GetElosLogicRight(), dr);
		AssignElosLogic(elosLogic.GetElosLogicUp(), dr);
		AssignElosLogic(elosLogic.GetElosLogicDown(), dr);
		AssignElosLogic(elosLogic.GetElosLogicRight().GetElosLogicRight(), dr);
	}
	
	//    N
	//NNNNNNN
	//    N
	private void AssembleElosUnitType31(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 31;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft(), dr);
		AssignElosLogic(elosLogic.GetElosLogicRight(), dr);
		AssignElosLogic(elosLogic.GetElosLogicUp(), dr);
		AssignElosLogic(elosLogic.GetElosLogicDown(), dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft().GetElosLogicLeft(), dr);
	}
	
	//N
	//NNN
	private void AssembleElosUnitType32(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 32;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicUp(), dr);
		AssignElosLogic(elosLogic.GetElosLogicRight(), dr);
	}
	
	//  N
	//NNN
	private void AssembleElosUnitType33(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 33;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicUp(), dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft(), dr);
	}
	
	//NNN
	//  N
	private void AssembleElosUnitType34(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 34;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicDown(), dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft(), dr);
	}
	
	//NNN
	//N
	private void AssembleElosUnitType35(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 35;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicDown(), dr);
		AssignElosLogic(elosLogic.GetElosLogicRight(), dr);
	}
	//NNNNNNNNNN
	
	//N
	//N
	//N
	//N
	private void ElosTransform1(ElosLogic elosPlayer){
		if (elosPlayer.nNumber == 1){
			if (elosPlayer.GetElosLogicUp() == null) return;
			if (elosPlayer.GetElosLogicUp().IsDraw()) return;
			if (elosPlayer.GetElosLogicDown() == null) return;
			if (elosPlayer.GetElosLogicDown().IsDraw()) return;
			if (elosPlayer.GetElosLogicDown().GetElosLogicDown() == null) return;
			if (elosPlayer.GetElosLogicDown().GetElosLogicDown().IsDraw()) return;
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
	
	//NNNNN
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
	
	//NNNNNNN
	//   N
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
	
	//NNNNNNN
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
	
	//  N
	//  NN
	//   N
	private void ElosTransform5(ElosLogic elosPlayer){
		if (elosPlayer.nNumber == 16){
			if (elosPlayer.GetElosLogicUp() == null) return;
			if (elosPlayer.GetElosLogicUp().IsDraw()) return;
			if (elosPlayer.GetElosLogicUp().GetElosLogicRight() == null) return;
			if (elosPlayer.GetElosLogicUp().GetElosLogicRight().IsDraw()) return;
			Drawable dr = elosPlayer.GetDrawable();
			this.ClearPlayer(elosPlayer);
			AssembleElosUnitType(elosPlayer, dr, 16);
		}else if (elosPlayer.nNumber == 17){
			if (elosPlayer.GetElosLogicLeft().GetElosLogicUp() == null) return;
			if (elosPlayer.GetElosLogicLeft().GetElosLogicUp().IsDraw()) return;
			if (elosPlayer.GetElosLogicDown() == null) return;
			if (elosPlayer.GetElosLogicDown().IsDraw()) return;
			Drawable dr = elosPlayer.GetDrawable();
			this.ClearPlayer(elosPlayer);
			AssembleElosUnitType(elosPlayer, dr, 15);
		}
	}
	
	// 	 N
	//  NN
	//  N
	private void ElosTransform6(ElosLogic elosPlayer){
		if (elosPlayer.nNumber == 18){
			if (elosPlayer.GetElosLogicUp().GetElosLogicLeft() == null) return;
			if (elosPlayer.GetElosLogicUp().GetElosLogicLeft().IsDraw()) return;
			if (elosPlayer.GetElosLogicRight()== null) return;
			if (elosPlayer.GetElosLogicRight().IsDraw()) return;
			Drawable dr = elosPlayer.GetDrawable();
			this.ClearPlayer(elosPlayer);
			AssembleElosUnitType(elosPlayer, dr, 18);
		}else if (elosPlayer.nNumber == 19){
			if (elosPlayer.GetElosLogicLeft() == null) return;
			if (elosPlayer.GetElosLogicLeft().IsDraw()) return;
			if (elosPlayer.GetElosLogicLeft().GetElosLogicDown() == null) return;
			if (elosPlayer.GetElosLogicLeft().GetElosLogicDown().IsDraw()) return;
			Drawable dr = elosPlayer.GetDrawable();
			this.ClearPlayer(elosPlayer);
			AssembleElosUnitType(elosPlayer, dr, 17);
		}
	}
	
	//	 N   N
	//   NNNNN
	//  
	private void ElosTransform7(ElosLogic elosPlayer){
		if (elosPlayer.nNumber == 21){
			if (elosPlayer.GetElosLogicUp() == null) return;
			if (elosPlayer.GetElosLogicUp().IsDraw()) return;
			if (elosPlayer.GetElosLogicDown()== null) return;
			if (elosPlayer.GetElosLogicDown().IsDraw()) return;
			if (elosPlayer.GetElosLogicDown().GetElosLogicRight() == null) return;
			if (elosPlayer.GetElosLogicDown().GetElosLogicRight().IsDraw()) return;
			Drawable dr = elosPlayer.GetDrawable();
			this.ClearPlayer(elosPlayer);
			AssembleElosUnitType(elosPlayer, dr, 21);
		}else if (elosPlayer.nNumber == 22){
			if (elosPlayer.GetElosLogicLeft() == null) return;
			if (elosPlayer.GetElosLogicLeft().IsDraw()) return;
			if (elosPlayer.GetElosLogicLeft().GetElosLogicDown() == null) return;
			if (elosPlayer.GetElosLogicLeft().GetElosLogicDown().IsDraw()) return;
			if (elosPlayer.GetElosLogicRight() == null) return;
			if (elosPlayer.GetElosLogicRight().IsDraw()) return;
			Drawable dr = elosPlayer.GetDrawable();
			this.ClearPlayer(elosPlayer);
			AssembleElosUnitType(elosPlayer, dr, 22);
		}else if (elosPlayer.nNumber == 23){
			if (elosPlayer.GetElosLogicDown() == null) return;
			if (elosPlayer.GetElosLogicDown().IsDraw()) return;
			if (elosPlayer.GetElosLogicUp() == null) return;
			if (elosPlayer.GetElosLogicUp().IsDraw()) return;
			if (elosPlayer.GetElosLogicUp().GetElosLogicLeft() == null) return;
			if (elosPlayer.GetElosLogicUp().GetElosLogicLeft().IsDraw()) return;
			Drawable dr = elosPlayer.GetDrawable();
			this.ClearPlayer(elosPlayer);
			AssembleElosUnitType(elosPlayer, dr, 23);
		}else if (elosPlayer.nNumber == 24){
			if (elosPlayer.GetElosLogicLeft() == null) return;
			if (elosPlayer.GetElosLogicLeft().IsDraw()) return;
			if (elosPlayer.GetElosLogicRight() == null) return;
			if (elosPlayer.GetElosLogicRight().IsDraw()) return;
			if (elosPlayer.GetElosLogicRight().GetElosLogicUp() == null) return;
			if (elosPlayer.GetElosLogicRight().GetElosLogicUp().IsDraw()) return;
			Drawable dr = elosPlayer.GetDrawable();
			this.ClearPlayer(elosPlayer);
			AssembleElosUnitType(elosPlayer, dr, 20);
			Log.d(TAG, "make transform...");
		}
	}
	
	//N
	// N
	private void ElosTransform8(ElosLogic elosPlayer){
		if (elosPlayer.nNumber == 25){
			if (elosPlayer.GetElosLogicUp() == null) return;
			if (elosPlayer.GetElosLogicUp().IsDraw()) return;
			Drawable dr = elosPlayer.GetDrawable();
			this.ClearPlayer(elosPlayer);
			AssembleElosUnitType(elosPlayer, dr, 25);
		}else if (elosPlayer.nNumber == 26){
			if (elosPlayer.GetElosLogicUp() == null) return;
			if (elosPlayer.GetElosLogicUp().IsDraw()) return;
			Drawable dr = elosPlayer.GetDrawable();
			this.ClearPlayer(elosPlayer);
			AssembleElosUnitType(elosPlayer, dr, 24);
		}
	}
	
	//  N
	//NNNNN
	//  N
	//  N
	private void ElosTransform9(ElosLogic elosPlayer){
		if (elosPlayer.nNumber == 28){
			if (elosPlayer.GetElosLogicRight().GetElosLogicRight() == null) return;
			if (elosPlayer.GetElosLogicRight().GetElosLogicRight().IsDraw()) return;
			Drawable dr = elosPlayer.GetDrawable();
			this.ClearPlayer(elosPlayer);
			AssembleElosUnitType(elosPlayer, dr, 29);
		}else if (elosPlayer.nNumber == 29){
			if (elosPlayer.GetElosLogicLeft().GetElosLogicLeft() == null) return;
			if (elosPlayer.GetElosLogicLeft().GetElosLogicLeft().IsDraw()) return;
			Drawable dr = elosPlayer.GetDrawable();
			this.ClearPlayer(elosPlayer);
			AssembleElosUnitType(elosPlayer, dr, 30);
		}else if (elosPlayer.nNumber == 30){
			if (elosPlayer.GetElosLogicUp().GetElosLogicUp() == null) return;
			if (elosPlayer.GetElosLogicUp().GetElosLogicUp().IsDraw()) return;
			Drawable dr = elosPlayer.GetDrawable();
			this.ClearPlayer(elosPlayer);
			AssembleElosUnitType(elosPlayer, dr, 28);
		}else if (elosPlayer.nNumber == 31){
			if (elosPlayer.GetElosLogicDown().GetElosLogicDown() == null) return;
			if (elosPlayer.GetElosLogicDown().GetElosLogicDown().IsDraw()) return;
			Drawable dr = elosPlayer.GetDrawable();
			this.ClearPlayer(elosPlayer);
			AssembleElosUnitType(elosPlayer, dr, 27);
		}
	}
	
	//N
	//NNN
	private void ElosTransform10(ElosLogic elosPlayer){
		if (elosPlayer.nNumber == 32){
			if (elosPlayer.GetElosLogicLeft()== null) return;
			if (elosPlayer.GetElosLogicLeft().IsDraw()) return;
			Drawable dr = elosPlayer.GetDrawable();
			this.ClearPlayer(elosPlayer);
			AssembleElosUnitType(elosPlayer, dr, 32);
		}else if (elosPlayer.nNumber == 33){
			if (elosPlayer.GetElosLogicDown()== null) return;
			if (elosPlayer.GetElosLogicDown().IsDraw()) return;
			Drawable dr = elosPlayer.GetDrawable();
			this.ClearPlayer(elosPlayer);
			AssembleElosUnitType(elosPlayer, dr, 33);
		}else if (elosPlayer.nNumber == 34){
			if (elosPlayer.GetElosLogicRight()== null) return;
			if (elosPlayer.GetElosLogicRight().IsDraw()) return;
			Drawable dr = elosPlayer.GetDrawable();
			this.ClearPlayer(elosPlayer);
			AssembleElosUnitType(elosPlayer, dr, 34);
		}else if (elosPlayer.nNumber == 35){
			if (elosPlayer.GetElosLogicUp()== null) return;
			if (elosPlayer.GetElosLogicUp().IsDraw()) return;
			Drawable dr = elosPlayer.GetDrawable();
			this.ClearPlayer(elosPlayer);
			AssembleElosUnitType(elosPlayer, dr, 31);
		}
	}
	//----------------------------------------------------
	private void ElosTransform(ElosLogic elosPlayer){
		ElosTransform1(elosPlayer);
		ElosTransform2(elosPlayer);
		ElosTransform3(elosPlayer);
		ElosTransform4(elosPlayer);	
		ElosTransform5(elosPlayer);	
		ElosTransform6(elosPlayer);	
		ElosTransform7(elosPlayer);	
		ElosTransform8(elosPlayer);
		ElosTransform9(elosPlayer);
		ElosTransform10(elosPlayer);
		this.nPlayerChoice = elosPlayer.nNumber - 1;
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
			if (bReset){
				//------------------------------------------
				nElosAreaHeight = this.getHeight();
				nElosAreaWidth = this.getWidth();	
				//---------------------------------------
				nTotalLines = nElosAreaHeight/nElosHeight + 3;
				nElosYOffset = nElosAreaHeight%nElosHeight - 3*nElosHeight;
				nTotalColumns = nElosAreaWidth/nElosWidth;
				nElosXOffset = nElosAreaWidth%nElosWidth/2;
				InitElosLogicGuard();
				MakeElosLogicNet();
				bReset = false;
			}
			
			Paint pt = new Paint();
			Rect rt = new Rect(0, 0, nElosAreaWidth, nElosAreaHeight);
			rtElosArea.set(rt);
			
			pt.setColor(Color.BLACK);
			canvas.drawRect(rt, pt);
			int nX = rt.centerX();
			int nY = rt.centerY() - 100;
			pt.setTextSize(20);
			pt.setColor(Color.WHITE);
			canvas.drawText("Richie Peng", nX - 60, nY, pt);
			canvas.drawText(" 史家之绝唱，无韵之离骚", nX - 120, nY + 40, pt);
			canvas.drawText("2009 - 01", nX - 25, nY + 80, pt);
			
			if (bDrawBG){				
				DrawBackGrundByLogic(canvas, nBGCategory);
			}			
			DrawElosLogicNet(canvas, elosGuard);
			if (bReportResult){
				Toast.makeText(ElosComplex.this, "游戏结束,得分：" + nCents, Toast.LENGTH_SHORT).show();vvvv++;
				bReportResult = false;
			}
		}
		
		private void DrawBackGrundByLogic(Canvas canvas, int nBackCategory){
			if (nBackCategory == 0){//cartoon
				DrawBackGround(canvas, drArrCartoonBG[ElosComplex.this.nCartoonChoice]);
			}else if (nBackCategory == 1){//beauty
				DrawBackGround(canvas, drArrBeautyBG[nBeautyChoice]);
			}else if (nBackCategory == 2)DrawBackGround(canvas, drArrPPBG[nPPChoice]);
		}
		
		private void DrawBackGround(Canvas canvas, Drawable dr){
			
			
			int nPicHeight = dr.getMinimumHeight();
			int nPicWidth = dr.getMinimumWidth();
			double a = nElosAreaHeight;
			double b = nElosAreaWidth;
			double c = nPicHeight;
			double d = nPicWidth;
			double rat1 = a/b;
			double rat2 = c/d;
			
			if (rat1 >= rat2){			
				nPicHeight = nElosAreaHeight;
				nPicWidth = (int)(nPicHeight/rat2);
			}else{
				nPicWidth = nElosAreaWidth;
				nPicHeight = (int)(nPicWidth*rat2);
			}
			Rect rt = new Rect();
			rt.left = rt.top = 0;
			rt.bottom = rt.top + nPicHeight;
			rt.right = rt.left + nPicWidth;
			dr.setBounds(rt);
			dr.draw(canvas);
		}		
	}
	
	private class ElosStateView extends View{
		private int nAreaHeight = 0;
		private int nAreaWidth = 0;
		private int nElosPrevHeight = 20;
		private int nElosPrevWidth = 20;
		private int nElosYOffset = 20;
		private int nElosXOffset = 0;
		private ElosLogic[][] logicPrevArr = new ElosLogic[5][5];
		private boolean bInitState = true;
		private void InitLogicPrevArr(){
			nElosPrevWidth = nAreaWidth/5;
			nElosXOffset = nAreaWidth%5/2;
			nElosPrevHeight = nElosPrevWidth;
			
			for (int i = 0; i < 5; i++){
				for (int j = 0; j < 5; j++)
					logicPrevArr[i][j] = new ElosLogic();
			}
			Rect rt = new Rect();
			for (int i = 0; i < 5; i++){
				for (int j = 0; j < 5; j++){
					if (i == 0){
						logicPrevArr[i][j].logicUp = null;
					}else{
						logicPrevArr[i][j].logicUp = logicPrevArr[i - 1][j];
					}
					
					if (i == 4){
						logicPrevArr[i][j].logicDown = null;
					}else {
						logicPrevArr[i][j].logicDown = logicPrevArr[i + 1][j];
					}
					
					if (j == 0){
						logicPrevArr[i][j].logicLeft = null;
					}else {
						logicPrevArr[i][j].logicLeft = logicPrevArr[i][j - 1];
					}
					
					if (j == 4){
						logicPrevArr[i][j].logicRight = null;
					}else {
						logicPrevArr[i][j].logicRight = logicPrevArr[i][j + 1];
					}
					
					rt.left = j*this.nElosPrevWidth + nElosXOffset;
					rt.top = i*this.nElosPrevHeight + this.nElosYOffset;
					rt.right = rt.left + this.nElosPrevWidth;
					rt.bottom = rt.top + this.nElosPrevHeight;
					logicPrevArr[i][j].SetRect(rt);
					
					logicPrevArr[i][j].SetDrawable(drArr[ElosComplex.this.nDrawableChoice]);
					
					logicPrevArr[i][j].SetDraw(false);
					logicPrevArr[i][j].bUnit = false;
				}
			}
		}
		
		
		public ElosStateView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			
			
		}
		@Override
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			super.onDraw(canvas);
			nAreaWidth = this.getWidth();
			nAreaHeight = this.getHeight();
			if (bInitState){
				InitLogicPrevArr();
				
				bInitState = false;
			}
			
			Paint pt = new Paint();
			Rect rt = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
			pt.setColor(Color.GREEN);
			canvas.drawRect(rt, pt);
			ElosComplex.this.ClearPlayer(logicPrevArr[2][2]);
			ElosComplex.this.CreatePlayer(drArr[ElosComplex.this.nDrawableChoice], ElosComplex.this.nPlayerChoice, logicPrevArr[2][2]);
			ElosComplex.this.DrawElosLogicNet(canvas, logicPrevArr);
			DrawElosPrevViewFrame(canvas, logicPrevArr);
			
			DrawStageAndCents(canvas, ElosComplex.this.nStage, ElosComplex.this.nCents, logicPrevArr[4][0].GetRect().left, logicPrevArr[4][0].GetRect().bottom + 20);
			DrawRecordList(listRecord, logicPrevArr[4][0].GetRect().left, logicPrevArr[4][0].GetRect().bottom + 20 + 50, canvas);
		}
		
		private void DrawElosPrevViewFrame(Canvas canvas, ElosLogic[][] logicPreview){
			Rect rt = new Rect();
			rt.set(logicPreview[0][0].GetRect());
			rt.right = logicPreview[4][4].GetRect().right;
			rt.bottom = logicPreview[4][4].GetRect().bottom;
			Paint pt = new Paint();
			pt.setColor(Color.BLACK);
			canvas.drawLine(rt.left, rt.top, rt.left, rt.bottom, pt);
			canvas.drawLine(rt.left, rt.bottom, rt.right, rt.bottom, pt);
			canvas.drawLine(rt.right, rt.bottom, rt.right, rt.top, pt);
			canvas.drawLine(rt.right, rt.top, rt.left, rt.top, pt);
		}
		
		private void DrawStageAndCents(Canvas canvas, int nStage, int nCents, int x, int y){
			Paint pt = new Paint();
			pt.setColor(Color.BLACK);
			canvas.drawText("第" + nStage + "关", x, y, pt);
			canvas.drawText("分数:" + nCents, x, y + 20, pt);
		}
		
		private void DrawRecordList(ArrayList<Map> listRecord, int x, int y, Canvas canvas){
			if (listRecord == null || listRecord.size() <= 0) return;
			Paint pt = new Paint();
			pt.setColor(Color.BLACK);
			
			for (int i = 0; i < listRecord.size(); i++){
				Map map = listRecord.get(i);
				String name = (String)map.get("name");
				String cent = (String)map.get("cent");
				canvas.drawText("选手:", x, y + i*80, pt);
				canvas.drawText(name, x, y + i*80 + 20, pt);
				canvas.drawText("分数:" + cent, x, y + i*80 + 35, pt);
				canvas.drawText(cent, x, y + i*80 + 50, pt);
				canvas.drawLine(x, y + i*80 + 50 + 2, x + 20, y + i*80 + 50 + 2, pt);
			}
		}
	}
	
	 class ElosLogic{
		int nNumber = -1;			
		
		boolean bRecursive = false;
		public boolean bPlayer = false;
		public void SetNumber(int nNum){ this.nNumber = nNum; }
		public int GetNumber(){ return nNumber; }
		public boolean bUnit = false;
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
		Drawable keep = null;
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
