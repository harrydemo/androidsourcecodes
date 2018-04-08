package com.china.square.elos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.china.square.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Elos extends Activity {
	String TAG = "ELOS";
	private final int SQUARELEMENT_NUMBERS = 7;
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
	
	ArrayList<Map> listRecord = new ArrayList<Map>();
	SqlDBHelper dbHelper = null;
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
		
		drArr[0] = res.getDrawable(R.drawable.blue);
		drArr[1] = res.getDrawable(R.drawable.green);
		drArr[2] = res.getDrawable(R.drawable.orange);
		drArr[3] = res.getDrawable(R.drawable.purple);
		drArr[4] = res.getDrawable(R.drawable.qing);
		drArr[5] = res.getDrawable(R.drawable.red);
		drArr[6] = res.getDrawable(R.drawable.yellow);
	}
	
	private int GetElosHeight(Drawable dr){
		return dr.getMinimumHeight();
	}
	private int GetElosWidth(Drawable dr){
		return dr.getMinimumWidth();
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
				esvElos.postInvalidate();
				try {
					sleep(nTimeInterval);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (IsElosGameFail()){
					/* Map map = new HashMap();
					map.put("name", "noname");
					map.put("cent", "" + nCents);
					AddRecord(listRecord, map); */
					
					bPlaying = false;
					bReportResult = true;
					evElos.postInvalidate(); 
				//	SaveRecordToDatabase(listRecord, dbHelper);
				//	GetRecordFromDatabase(listRecord, dbHelper);
					while (!bPlaying){
						try {
							sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} 
					//continue;
				}
				if (!Elos.this.IsPlayerMoveableDown(player)){
					ClearElosLogicUnitUnitTag(player);
					int nErasableLine = CheckGuard(player);
					nCents += 100*nErasableLine + (int)((nErasableLine == 0? 1: nErasableLine) - 1)*50;	
					
					InvalidateStage();
				//	AnnounceNewStage();
					
					player = CreatePlayer(drArr[GetRandomDrawableChoice()], GetRandomPlayerChoice());					
				}else{
					player = Elos.this.PlayerMoveDown(player, nPlayerChoice);
					
				}
				evElos.postInvalidate();
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
			bPlaying = true;
			MakeElosLogicNet();
			ClearStageAndCentsAfterFail();
			player = CreatePlayer(drArr[GetRandomDrawableChoice()], GetRandomPlayerChoice());
			if (thdTimer == null){
				this.thdTimer = new TimerThread();
				thdTimer.start();
			}else{
				//thdTimer.resume();
				//thdTimer.start();
				//thdTimer.notify();
			}
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	
	
	private boolean IsElosGameFail(){
		ElosLogic visit = elosGuard[0][0];
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
		//SaveRecordToDatabase(listRecord, dbHelper);
	//	dbHelper.close();
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
		//setTitle("" + nElosWidth + "," + nElosHeight);
		//dbHelper = new SqlDBHelper(this);
		//DeleteResult();
	//	GetRecordFromDatabase(listRecord, dbHelper);
		
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
	Rect rtDraw = new Rect();
	private void DrawElos(ElosLogic elosLogic, Canvas canvas, int nOffset){
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
		player = elosGuard[1][this.nTotalColumns/2];
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
		//setTitle("move");
		return elosPlayer;
		
	}
	
	private ElosLogic PlayerMoveRight(ElosLogic elosPlayer, int nPlayerChoice){
		if (elosPlayer == null) return elosPlayer;
		if (!IsPlayerMoveableRight(elosPlayer)) {return elosPlayer; }
		Drawable dr  = elosPlayer.GetDrawable();
		ClearPlayer(elosPlayer);
		elosPlayer = elosPlayer.GetElosLogicRight();
		elosPlayer = CreatePlayer(dr, nPlayerChoice, elosPlayer);
		//setTitle("move");
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
		/*if (elosGuard == null) return;
		for (int i = 0; i < this.nTotalLines; i++){
			int nCount = 0;
			for (int j = 0; j < this.nTotalColumns; j++){
				if (elosGuard[i][j])
			}
		}*/
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
		//else if (nChoice == 15){AssembleElosUnitType16(elosLogic, dr);}
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
	//NNN
	private void AssembleElosUnitType13(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 13;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft(), dr);
		AssignElosLogic(elosLogic.GetElosLogicDown(), dr);
		AssignElosLogic(elosLogic.GetElosLogicDown().GetElosLogicDown(), dr);
	}
	
	//NNN
	//NNN
	private void AssembleElosUnitType14(ElosLogic elosLogic, Drawable dr){
		elosLogic.bPlayer = true;
		elosLogic.nNumber = 14;
		AssignElosLogic(elosLogic, dr);
		AssignElosLogic(elosLogic.GetElosLogicUp(), dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft().GetElosLogicLeft(), dr);
		AssignElosLogic(elosLogic.GetElosLogicLeft(), dr);
	}
	
	//NNN
	//NNN
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
	
	private void ElosTransform(ElosLogic elosPlayer){
	//	Drawable dr  = elosPlayer.GetDrawable();
		ElosTransform1(elosPlayer);
		ElosTransform2(elosPlayer);
		ElosTransform3(elosPlayer);
		ElosTransform4(elosPlayer);	
	//	elosPlayer.SetDrawable(dr);
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
				nTotalLines = nElosAreaHeight/nElosHeight + 3;
				nElosYOffset = nElosAreaHeight%nElosHeight - 3*nElosHeight;
				nTotalColumns = nElosAreaWidth/nElosWidth;
				nElosXOffset = nElosAreaWidth%nElosWidth/2;
				InitElosLogicGuard();
				MakeElosLogicNet();
				bReset = false;
				vvvv++;
				//setTitle("" + nTotalLines  + "," + nTotalColumns + "," + vvvv);
			}
			DrawElosLogicNet(canvas, elosGuard);
			if (bReportResult){
				Toast.makeText(Elos.this, "游戏结束,得分：" + nCents, Toast.LENGTH_SHORT).show();vvvv++;
				//setTitle("" + vvvv);
				bReportResult = false;
			}
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
					
					logicPrevArr[i][j].SetDrawable(drArr[Elos.this.nDrawableChoice]);
					
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
			Elos.this.ClearPlayer(logicPrevArr[2][2]);
			Elos.this.CreatePlayer(drArr[Elos.this.nDrawableChoice], Elos.this.nPlayerChoice, logicPrevArr[2][2]);
			Elos.this.DrawElosLogicNet(canvas, logicPrevArr);
			DrawElosPrevViewFrame(canvas, logicPrevArr);
			
			DrawStageAndCents(canvas, Elos.this.nStage, Elos.this.nCents, logicPrevArr[4][0].GetRect().left, logicPrevArr[4][0].GetRect().bottom + 20);
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
	
	
	
	/*private final String TAG = "ELOS";
	private final int SQUARELEMENT_NUMBERS = 7;
	private int nElosWidth = 0;
	private int nElosHeight = 0;
	
	private ElosLogic elosExistLink = null;
	private ElosLogic player = null;
	private Rect rtPlayer = null;
	private int nPlayerStep = 10;	
	
	private boolean bGuardRejoin = false;
	
	private int nModOffset = 0;
	
	private boolean bStartElos = false;
	private int nTimeInterval = 400;
	
	private ElosView evElos = null;
	private ElosStateView esvElos = null;
	private final int ELOS_MARGIN = 1;
	private int nElosLines = 0;
	
	private Canvas elosCanvas = null;
	
	private int nElosAreaWidth = 0;
	private int nElosAreaHeight = 0;	
	
	private ElosLogic[] ElosLogicGuard = null;
	private int nElosLineOffset= 1;
	
	int nPlayerChoice = 0;
	final int  nPlayerChoices = 5;
	
	boolean bActionDown = false;
	
	
	//记录方块的种类
	private Drawable[] drArr = new Drawable[SQUARELEMENT_NUMBERS];
	
	//记录一行方块的个数
	private int nElosColumns = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.elos);
		RetriveDrawable(this);
		
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
		
	}
	
	private void RetriveDrawable(Context c){
		Resources res = c.getResources();
		
		drArr[0] = res.getDrawable(R.drawable.blue);
		drArr[1] = res.getDrawable(R.drawable.green);
		drArr[2] = res.getDrawable(R.drawable.orange);
		drArr[3] = res.getDrawable(R.drawable.purple);
		drArr[4] = res.getDrawable(R.drawable.qing);
		drArr[5] = res.getDrawable(R.drawable.red);
		drArr[6] = res.getDrawable(R.drawable.yellow);
	}
	
	private int GetELosCount(int a_ElosViewLength, int a_nElosWidth){
		return this.nElosColumns = a_ElosViewLength/a_nElosWidth;
	}
	
	private int GetElosWidthFromDrawable(Drawable dr){
		return nElosWidth = dr.getMinimumHeight();
	}
	
	private int GetElosHeightFromDrawable(Drawable dr){
		return nElosHeight = dr.getMinimumWidth();
	}
	
	int GetRandomPlayerChoice(){
		int nChoice = 0;
		double db = Math.random();
		nChoice = (int)(db*10000);
		return (nPlayerChoice = nChoice%nPlayerChoices);		
	}
	
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	
		// TODO Auto-generated method stub
		if (bActionDown){ return super.onKeyDown(keyCode, event); }
		
		if (keyCode == 21){//left
			InitElosUnitRecursiveable(player);	
			if (IsRecursiveElosLogicUnitMoveableLeft(player)) rtPlayer.offset(-nElosWidth, 0);
		}else if (keyCode == 22){//right
			InitElosUnitRecursiveable(player);	
			if (IsRecursiveElosLogicUnitMoveableRight(player)) rtPlayer.offset(nElosWidth, 0);
		}else if (keyCode == 19){//up
			
		}else if (keyCode == 20){//down
			InitElosUnitRecursiveable(player);	
			if (IsRecursiveElosLogicUnitMoveableDown(player)) {
				rtPlayer.offset(0, nPlayerStep);
			//	evElos.invalidate();
			}
			
		}
		
		bActionDown = true;
	//	if (keyCode == 20) bActionDown = false;
		return super.onKeyDown(keyCode, event);
	}



	private final int MENU_START = Menu.FIRST;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, MENU_START, 0, "开始");
		return super.onCreateOptionsMenu(menu);
	}
	
	
	private void CreatePlayer(){
		Log.d(TAG, "CreatePlayer");
		int nChoice = GetRandomPlayerChoice();
		Log.d(TAG, "GetRandomPlayerChoice");
		
		rtPlayer = new Rect();
		rtPlayer.left =  nElosWidth*nElosColumns/2;
		rtPlayer.right = rtPlayer.left + nElosWidth;
		rtPlayer.top = evElos.getHeight() - nElosHeight*nElosLines - nElosHeight;
		rtPlayer.bottom = rtPlayer.top + nElosHeight;			
		player = AssembleElosType(nChoice, null, rtPlayer);
	}
	
	ElosLogic AssembleElosType(int nChoice,ElosLogic logic, Rect rt){
		ElosLogic elosLogic = null;
		if (nChoice == 0){
			elosLogic = AssembleElosType1(logic, rt);
		}if (nChoice == 1){
			elosLogic = AssembleElosType2(logic, rt);
		}else if (nChoice == 2){
			elosLogic = AssembleElosType3(logic, rt);
		}else if (nChoice == 3){
			elosLogic = AssembleElosType4(logic, rt);
		}else if (nChoice == 4){
			elosLogic = AssembleElosType5(logic, rt);
		}
		
		return elosLogic;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == MENU_START){
			nElosLines = evElos.getHeight()/nElosHeight;	
		
			ElosLogicGuard = new ElosLogic[nElosLines];
			for (int i = 0; i < nElosLines; i++){ ElosLogicGuard[i] = null; }
			
			CreatePlayer();
			
			bStartElos = true;
			if (thdTimer == null){
				thdTimer = new TimerThread();
				thdTimer.start();
			}
			
			
			Toast.makeText(this, "start game:" + nElosLines + ",", Toast.LENGTH_SHORT).show();
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void ElosLogicHitTest(ElosLogic elosLogic){
		if (elosLogic == null){ Log.d(TAG, "elos logic is null..."); return; }
		
		int nLine = this.GetLineNumberFromPosition(elosLogic.GetRect());	
				
	}
	
	private boolean IsElosLogicMoveableLeft(ElosLogic elosLogic){
		String TAG = "PENG";
		if (elosLogic == null) return false;
		if (elosLogic.GetRect().left <= 0) return false;
		if (!IsElosLogicMoveableDown(elosLogic))return false;
		int nLineNumber = elosLogic.GetLineNumber();
		if (ElosLogicGuard[nLineNumber] == null) return true;
		
		Log.d(TAG, "" + elosLogic.GetRect().left + "," + elosLogic.GetRect().right);
		
		Rect rt = new Rect();
		rt.set(elosLogic.GetRect());
		rt.offset(-nElosWidth, 0);
		
		ElosLogic visitor = ElosLogicGuard[nLineNumber];
		while (visitor != null){
			if (visitor.GetRect().right == rt.left)return false;
			if (visitor.GetRect().contains(rt)) return false;
			visitor = visitor.GetRightElos();
		}		
		return true;
	}
	
	private boolean IsRecursiveElosLogicUnitMoveableLeft(ElosLogic elosLogic){
		if (elosLogic == null){ Log.d(TAG, "null"); return false; }
		elosLogic.SetRecursiveable(false);
		Log.d(TAG, "IsRecursiveElosLogicUnitMoveableLeft");
		if (!IsElosLogicMoveableLeft(elosLogic)) {
			
			return false;
		}
		
		if (elosLogic.GetLeftElos() != null && elosLogic.GetLeftElos().IsRecursiveable()) if (!IsRecursiveElosLogicUnitMoveableLeft(elosLogic.GetLeftElos())) return false;
		if (elosLogic.GetRightElos() != null && elosLogic.GetRightElos().IsRecursiveable()) if (!IsRecursiveElosLogicUnitMoveableLeft(elosLogic.GetRightElos())) return false;
		if (elosLogic.GetUpElos() != null && elosLogic.GetUpElos().IsRecursiveable()) if (!IsRecursiveElosLogicUnitMoveableLeft(elosLogic.GetUpElos())) return false;
		if (elosLogic.GetDownElos() != null && elosLogic.GetDownElos().IsRecursiveable()) if (!IsRecursiveElosLogicUnitMoveableLeft(elosLogic.GetDownElos())) return false;
		
		return true;
	}
	
	private boolean IsElosLogicMoveableRight(ElosLogic elosLogic){
		String TAG = "PENG";
		Log.d(TAG, "" + elosLogic.GetRect().right + "," + nElosAreaWidth);
		if (elosLogic == null) return false;
		if (!IsElosLogicMoveableDown(elosLogic))return false;
		int nColNum = GetColumnNumberFromPosition(elosLogic.GetRect());
		if (nColNum >= nElosColumns - 1) return false;
		int nLineNumber = elosLogic.GetLineNumber();
		if (ElosLogicGuard[nLineNumber] == null) return true;
		
		Rect rt = new Rect();
		rt.set(elosLogic.GetRect());
		rt.offset(nElosWidth, 0);
		
		ElosLogic visitor = ElosLogicGuard[nLineNumber];
		while (visitor != null){
			if (visitor.GetRect().left == rt.right)return false;
			if (visitor.GetRect().contains(rt)) return false;
			visitor = visitor.GetRightElos();
		}		
		return true;
	}
	
	private boolean IsRecursiveElosLogicUnitMoveableRight(ElosLogic elosLogic){
		String TAG = "PENG";
		if (elosLogic == null){ Log.d(TAG, "null"); return false; }
		elosLogic.SetRecursiveable(false);
		Log.d(TAG, "IsRecursiveElosLogicUnitMoveableRight");
		if (!IsElosLogicMoveableRight(elosLogic)) {
			
			return false;
		}
		
		if (elosLogic.GetLeftElos() != null && elosLogic.GetLeftElos().IsRecursiveable()) if (!IsRecursiveElosLogicUnitMoveableRight(elosLogic.GetLeftElos())) return false;
		if (elosLogic.GetRightElos() != null && elosLogic.GetRightElos().IsRecursiveable()) if (!IsRecursiveElosLogicUnitMoveableRight(elosLogic.GetRightElos())) return false;
		if (elosLogic.GetUpElos() != null && elosLogic.GetUpElos().IsRecursiveable()) if (!IsRecursiveElosLogicUnitMoveableRight(elosLogic.GetUpElos())) return false;
		if (elosLogic.GetDownElos() != null && elosLogic.GetDownElos().IsRecursiveable()) if (!IsRecursiveElosLogicUnitMoveableRight(elosLogic.GetDownElos())) return false;
		
		return true;
	}
	
	private boolean IsElosLogicMoveableDown(ElosLogic elosLogic){
		Log.d(TAG, "IsElosLogicMoveableDown");
		if (elosLogic == null){Log.d(TAG, "elosLogic is null in moveable..."); return false;}
		int nLineNumber = GetLineNumberFromPosition(elosLogic.GetRect());
		
		if (nLineNumber == elosLogic.GetLineNumber()) return true;		
		elosLogic.SetLineNumber(nLineNumber);
		Log.d(TAG, "Linenumber:" + nLineNumber);
		
		if (ElosLogicGuard == null ){ Log.d(TAG, "sever error, elos guard is null"); return false; }
		if (nLineNumber >= nElosLines) return false;
		
		if (ElosLogicGuard[nLineNumber] == null) return true;
		ElosLogic visitor = ElosLogicGuard[nLineNumber];
		Rect rt = new Rect();
		rt.set(elosLogic.GetRect());
		rt.offset(0, nElosHeight);
		
		while(visitor != null){
			if (visitor.GetRect().contains(rt)) return false;
			visitor = visitor.GetRightElos();
		}		
	
		return true;
	}
	
	private boolean IsRecursiveElosLogicUnitMoveableDown(ElosLogic elosLogic){
		if (elosLogic == null){ Log.d(TAG, "null"); return false; }
		elosLogic.SetRecursiveable(false);
		Log.d(TAG, "IsRecursiveElosLogicUnitMoveableDown");
		if (!IsElosLogicMoveableDown(elosLogic)) {
			
			return false;
		}
		
		if (elosLogic.GetLeftElos() != null && elosLogic.GetLeftElos().IsRecursiveable()) if (!IsRecursiveElosLogicUnitMoveableDown(elosLogic.GetLeftElos())) return false;
		if (elosLogic.GetRightElos() != null && elosLogic.GetRightElos().IsRecursiveable()) if (!IsRecursiveElosLogicUnitMoveableDown(elosLogic.GetRightElos())) return false;
		if (elosLogic.GetUpElos() != null && elosLogic.GetUpElos().IsRecursiveable()) if (!IsRecursiveElosLogicUnitMoveableDown(elosLogic.GetUpElos())) return false;
		if (elosLogic.GetDownElos() != null && elosLogic.GetDownElos().IsRecursiveable()) if (!IsRecursiveElosLogicUnitMoveableDown(elosLogic.GetDownElos())) return false;
		
		return true;
	}
	
	private void RemoveELosGuardWholeLines(){
		String TAG = "REMOVE";
		Log.d(TAG, "RemoveELosGuardWholeLines");
		if (ElosLogicGuard == null) return;
		int i = 0;
		
		while (i < ElosLogicGuard.length){
			if (ElosLogicGuard[i] == null){i++;continue;}
			else{
				RemoveElosGuardLine(i);
				i++;
			}
		}
	}
	
	private void RemoveElosGuardLine(int nLineNumber){
		String TAG = "REMOVE";
		Log.d(TAG, "RemoveElosGuardLine");
		if (nLineNumber < 0 || nLineNumber >= ElosLogicGuard.length) return;		
		if (GetElosLineCount(nLineNumber) < nElosColumns) return;	
		Log.d(TAG, "Remove:" + nLineNumber);
		MovedownElosGuardLine(nLineNumber);
	}
	
	private void MovedownElosGuardLine(int nLineNumber){
		if (nLineNumber == 0 ){
			ElosLogicGuard[0] = null;
			return;
		}
		if (nLineNumber < 1 || nLineNumber>= ElosLogicGuard.length) return;
		ElosLogic tmp = ElosLogicGuard[nLineNumber - 1];
		if (tmp == null) return;
		while (tmp != null){
			tmp.GetRect().offset(0, nElosHeight);
			tmp = tmp.GetRightElos();
		}
		ElosLogicGuard[nLineNumber] = ElosLogicGuard[nLineNumber - 1];
		ElosLogicGuard[nLineNumber - 1] = null;		
	}
	
	private int GetElosLineCount(int nLineNumber){
		if (nLineNumber < 0 || nLineNumber >= ElosLogicGuard.length) return -1 ;
		ElosLogic tmp = ElosLogicGuard[nLineNumber];
		int nCount = 0;
		while (tmp != null){
			nCount++;
			tmp = tmp.GetRightElos();
		}
		
		return nCount;		
	}
	
	static int nStatCount = 0;
	private void RecursiveRecalcHome(ElosLogic elosLogic){		
		String TAG = "CALC";
		nStatCount++;
		Log.d(TAG, "recalc:" + nStatCount);
		
		if (elosLogic == null){ Log.d(TAG, "null"); return; }
		elosLogic.SetRecursiveable(false);
		RecalcHomeForElosLogic(elosLogic);
		
		if (elosLogic.GetLeftElos() != null && elosLogic.GetLeftElos().IsRecursiveable()) RecursiveRecalcHome(elosLogic.GetLeftElos());
		if (elosLogic.GetRightElos() != null && elosLogic.GetRightElos().IsRecursiveable()) RecursiveRecalcHome(elosLogic.GetRightElos());
		if (elosLogic.GetUpElos() != null && elosLogic.GetUpElos().IsRecursiveable())RecursiveRecalcHome(elosLogic.GetUpElos());
		if (elosLogic.GetDownElos() != null && elosLogic.GetDownElos().IsRecursiveable()) RecursiveRecalcHome(elosLogic.GetDownElos());
	}
	
	private void ReDrawElosGuard(Canvas canvas){
		String TAG = "GUARD";
		if (bGuardRejoin) Log.d(TAG, "ReDrawElosGuard");
		if (ElosLogicGuard == null) { Log.d(TAG, " guard is null, redraw fail"); return; }
		int nLen = ElosLogicGuard.length;
		if (nLen <= 0) {if (bGuardRejoin) Log.d(TAG, "len  is less than 1"); return;}
		for (int i = 0; i < nLen; i++){
			if (bGuardRejoin) Log.d(TAG, "i:" + i);
			ReDrawElosGuardLine(ElosLogicGuard[i], canvas);
		}
		bGuardRejoin = false;
	}
	
	private void ReDrawElosGuardLine(ElosLogic elosLogic, Canvas canvas){
		String TAG = "GUARD";
		if (bGuardRejoin) Log.d(TAG, "ReDrawElosGuardLine");
		if (elosLogic == null) {if (bGuardRejoin)Log.d(TAG, "elos logic is null");return;}
		ElosLogic tmp = elosLogic;
		int nKeep = 0;
		while (tmp != null){
			nKeep++;
			Rect rt = tmp.GetRect();
			//DrawElosWithDrawable(nPosX + nCount*nElosWidth, nPosY - nLineNum*nElosHeight , nModOffset, drArr[nCount%7], canvas);
			DrawElosWithDrawable(rt.left, rt.bottom, nModOffset, drArr[0], canvas);
			if (bGuardRejoin){
				Log.d(TAG, "" + tmp.GetRect().left + "," + tmp.GetRect().top);
			}
			
			tmp = tmp.GetRightElos();
		}
		
		if (bGuardRejoin)Log.d(TAG, "total:" + nKeep);
	}
	
	
	
	private void RecalcHomeForElosLogic(ElosLogic elosLogic){
		String TAG = "CALC";
		ElosLogic elosCopy = elosLogic.CopyElosLogic();
		int nLineNumber = this.GetLineNumberFromPosition(elosCopy.GetRect());
		Log.d(TAG, "Line number:" + nLineNumber);
		nLineNumber--;
		if (ElosLogicGuard == null){Log.d(TAG, "null guard，check...");}
		/*if (ElosLogicGuard[nLineNumber] == null){
			Log.d(TAG, "null(1) ...");
			
			ElosLogicGuard[nLineNumber] = elosCopy;
		}else{
		/*	Log.d(TAG, "Add(2) ...");
			ElosLogic keeper1 = ElosLogicGuard[nLineNumber];
			ElosLogic keeper2 = keeper1;//= ElosLogicGuard[nLineNumber].GetRightElos();
			//ElosLogic LogicGuard = ElosLogicGuard[nLineNumber];
			do {
				Log.d(TAG, "Loop(3) ...");
				if (keeper1.GetRect().left >= elosCopy.GetRect().right){
				//	ElosLogic keeper3 = keeper1.GetLeftElos();
					if (keeper1.GetLeftElos() != null){
						keeper1.GetLeftElos().SetRightElos(elosCopy);
						
						elosCopy.SetLeftElos(keeper1.GetLeftElos());
						keeper1.SetLeftElos(elosCopy);
						elosCopy.SetRightElos(keeper1);
					}else{
						keeper1.SetLeftElos(elosCopy);
						elosCopy.SetRightElos(keeper1);
					}
					
					break;
				}
				keeper2 = keeper1;
			}while((keeper1 = keeper1.GetRightElos()) != null);
			if (keeper1 == null){
				Log.d(TAG, "re Ring (4) ...");
				keeper2.SetRightElos(elosCopy);
				elosCopy.SetLeftElos(keeper2);
			}
			
		}*//*
		if (ElosLogicGuard[nLineNumber] == null){
			ElosLogicGuard[nLineNumber] = elosCopy;
		}else{
			ElosLogicGuard[nLineNumber].SetLeftElos(elosCopy);
			elosCopy.SetRightElos(ElosLogicGuard[nLineNumber]);
			ElosLogicGuard[nLineNumber] = elosCopy;
		}
	}
	
	private int GetLineNumberFromPosition(Rect rt){
		if (rt == null) return -1;
		int nPos = rt.bottom;
		int nLineNumber = nPos/nElosHeight;
		nLineNumber = nLineNumber - nElosLineOffset;
		if (nLineNumber < 0) nLineNumber = 0;
		//setTitle("在第" + nLineNumber + "行");		
		return nLineNumber;
	}
	
	private int GetColumnNumberFromPosition(Rect rt){
		if (rt == null) return -1;
		int nPos = rt.right;
		int nColumnNumber = nPos/nElosWidth;
				
		return nColumnNumber;
	}
	
	private void DrawPlayer(Canvas canvas){
		if (rtPlayer == null) return;
		InitElosUnitRecursiveable(player);
		
		player = AssembleElosType(nPlayerChoice, player, rtPlayer);
		InitElosUnitRecursiveable(player);
		RecursiveSetElosUnitDrawable(player, drArr[4]);		
		InitElosUnitRecursiveable(player);
		RecursiveDrawElosUnit(player, elosCanvas == null?canvas:elosCanvas);
		GetLineNumberFromPosition(player.GetRect());
	}

	private class ElosView extends View{
		
		
		public ElosView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			
		}
		
		private int GetModAfterColumnCount(int a_ElosViewLength, int a_nElosWidth){
			return nModOffset = a_ElosViewLength%a_nElosWidth;
		}

		@Override
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			super.onDraw(canvas);
			Log.d(TAG, "onDraw...");
			if (elosCanvas == null) elosCanvas = canvas;
			
			nElosAreaWidth = canvas.getWidth();
			nElosAreaHeight = canvas.getHeight();
			
			//初始化一个俄罗斯方块的宽高和栏目数的代码
			GetELosCount(canvas.getWidth(), GetElosWidthFromDrawable(drArr[0]));			
			GetElosHeightFromDrawable(drArr[0]);
			nModOffset = GetModAfterColumnCount(this.getWidth(), GetElosWidthFromDrawable(drArr[0]))/2;
			
		/*	
			Paint pt = new Paint();
			Rect rt = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
			pt.setColor(Color.GREEN);
			canvas.drawRect(rt, pt);
			Log.d(TAG, "onDraw...");
			//DrawElosWithDrawable()
			
			int nPosX = 0;
			int nPosY = canvas.getHeight();
			int nLineNum = 0;
			while (nLineNum < nElosLines){
				nLineNum++;				
				int nCount = 0;
				while (nCount < nElosColumns){
					DrawElosWithDrawable(nPosX + nCount*nElosWidth, nPosY - nLineNum*nElosHeight , nModOffset, drArr[nCount%7], canvas);
					nCount++;
				}
			}*//*
			
			ReDrawElosGuard(canvas);
			DrawPlayer(canvas);		
		}
	}
	
	private class ElosStateView extends View{
		public ElosStateView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			super.onDraw(canvas);
			
			Paint pt = new Paint();
			Rect rt = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
			pt.setColor(Color.GREEN);
			canvas.drawRect(rt, pt);
		}
		
	}
	
	//-------------------------------------------------------------------
	//画一个俄罗斯块
	void DrawElosWithDrawable(int x, int y, int modOffSet, Drawable dr, Canvas canvas){
		int xOffset = modOffSet;
		
		//+1 作为空隙边框
		Rect rt = new Rect(x + xOffset + 1, y - nElosHeight + 1, x + xOffset + nElosWidth, y);
		
		dr.setBounds(rt);
		dr.draw(canvas);
	}
	
	//NNNNNNN
	//返回中心俄罗斯方块
	private ElosLogic AssembleElosType1(ElosLogic centerLogic, Rect rt){
		if (centerLogic == null){
			Log.d(TAG, "create Center logic");
			centerLogic = new ElosLogic(true);
			
			ElosLogic item1Logic = new ElosLogic(false);
			ElosLogic item2Logic = new ElosLogic(false);
			ElosLogic item3Logic = new ElosLogic(false);
			
			item1Logic.SetRightElos(item2Logic);
			item1Logic.SetLeftElos(null);
			item1Logic.SetUpElos(null);
			item1Logic.SetDownElos(null);
			
			item2Logic.SetRightElos(centerLogic);
			item2Logic.SetLeftElos(item1Logic);
			item2Logic.SetUpElos(null);
			item2Logic.SetDownElos(null);
			
			centerLogic.SetRightElos(item3Logic);
			centerLogic.SetLeftElos(item2Logic);
			centerLogic.SetUpElos(null);
			centerLogic.SetDownElos(null);
			
			item3Logic.SetRightElos(null);
			item3Logic.SetLeftElos(centerLogic);
			item3Logic.SetUpElos(null);
			item3Logic.SetDownElos(null);
			
		}

		Rect rt2 = new Rect();
		Rect rt1 = new Rect();
		Rect rt3 = new Rect();
		centerLogic.SetRect(rt);
		
		int nHeight = centerLogic.GetRect().height();
		int nWidth = centerLogic.GetRect().width();
		rt2.set(centerLogic.GetRect());
		rt2.offset(-nWidth, 0);		
		rt1.set(rt2);
		rt1.offset(-nWidth, 0);		
		rt3.set(centerLogic.GetRect());
		rt3.offset(nWidth, 0);
		
		centerLogic.GetLeftElos().SetRect(rt2);
		centerLogic.GetLeftElos().GetLeftElos().SetRect(rt1);
		centerLogic.GetRightElos().SetRect(rt3);
		
		return centerLogic;
	}
	
	//NNNNN
	//    N
	private ElosLogic AssembleElosType2(ElosLogic centerLogic, Rect rt){
		if (centerLogic == null){
			Log.d(TAG, "create Center logic");
			centerLogic = new ElosLogic(true);
			
			ElosLogic item1Logic = new ElosLogic(false);
			ElosLogic item2Logic = new ElosLogic(false);
			ElosLogic item3Logic = new ElosLogic(false);
			
			item1Logic.SetRightElos(item2Logic);
			item1Logic.SetLeftElos(null);
			item1Logic.SetUpElos(null);
			item1Logic.SetDownElos(null);
			
			item2Logic.SetRightElos(centerLogic);
			item2Logic.SetLeftElos(item1Logic);
			item2Logic.SetUpElos(null);
			item2Logic.SetDownElos(null);
			
			centerLogic.SetRightElos(null);
			centerLogic.SetLeftElos(item2Logic);
			centerLogic.SetUpElos(null);
			centerLogic.SetDownElos(item3Logic);
			
			item3Logic.SetRightElos(null);
			item3Logic.SetLeftElos(null);
			item3Logic.SetUpElos(centerLogic);
			item3Logic.SetDownElos(null);
		}

		Rect rt2 = new Rect();
		Rect rt1 = new Rect();
		Rect rt3 = new Rect();
		centerLogic.SetRect(rt);
		
		int nHeight = centerLogic.GetRect().height();
		int nWidth = centerLogic.GetRect().width();		
		rt2.set(centerLogic.GetRect());
		rt2.offset(-nWidth, 0);		
		rt1.set(rt2);
		rt1.offset(-nWidth, 0);		
		rt3.set(centerLogic.GetRect());
		rt3.offset(0, nHeight);
		
		centerLogic.GetLeftElos().SetRect(rt2);
		centerLogic.GetLeftElos().GetLeftElos().SetRect(rt1);
		centerLogic.GetDownElos().SetRect(rt3);
		
		return centerLogic;
	}
	
	//NNNNN
	//  N
	private ElosLogic AssembleElosType3(ElosLogic centerLogic, Rect rt){
		if (centerLogic == null){
			Log.d(TAG, "create Center logic");
			centerLogic = new ElosLogic(true);
			
			ElosLogic item1Logic = new ElosLogic(false);
			ElosLogic item2Logic = new ElosLogic(false);
			ElosLogic item3Logic = new ElosLogic(false);
			
			item1Logic.SetRightElos(centerLogic);
			item1Logic.SetLeftElos(null);
			item1Logic.SetUpElos(null);
			item1Logic.SetDownElos(null);
			
			item2Logic.SetRightElos(null);
			item2Logic.SetLeftElos(centerLogic);
			item2Logic.SetUpElos(null);
			item2Logic.SetDownElos(null);
			
			centerLogic.SetRightElos(item2Logic);
			centerLogic.SetLeftElos(item1Logic);
			centerLogic.SetUpElos(null);
			centerLogic.SetDownElos(item3Logic);
			
			item3Logic.SetRightElos(null);
			item3Logic.SetLeftElos(null);
			item3Logic.SetUpElos(centerLogic);
			item3Logic.SetDownElos(null);
		}

		Rect rt2 = new Rect();
		Rect rt1 = new Rect();
		Rect rt3 = new Rect();
		centerLogic.SetRect(rt);
		
		int nHeight = centerLogic.GetRect().height();
		int nWidth = centerLogic.GetRect().width();		
		rt2.set(centerLogic.GetRect());
		rt2.offset(nWidth, 0);		
		rt1.set(centerLogic.GetRect());
		rt1.offset(-nWidth, 0);		
		rt3.set(centerLogic.GetRect());
		rt3.offset(0, nHeight);
		
		centerLogic.GetLeftElos().SetRect(rt1);
		centerLogic.GetRightElos().SetRect(rt2);
		centerLogic.GetDownElos().SetRect(rt3);
		
		return centerLogic;
	}
	
	//NNNNN
	//N
	private ElosLogic AssembleElosType4(ElosLogic centerLogic, Rect rt){
		if (centerLogic == null){
			Log.d(TAG, "create Center logic");
			centerLogic = new ElosLogic(true);
			
			ElosLogic item1Logic = new ElosLogic(false);
			ElosLogic item2Logic = new ElosLogic(false);
			ElosLogic item3Logic = new ElosLogic(false);
			
			item1Logic.SetRightElos(null);
			item1Logic.SetLeftElos(null);
			item1Logic.SetUpElos(centerLogic);
			item1Logic.SetDownElos(null);
			
			item2Logic.SetRightElos(item3Logic);
			item2Logic.SetLeftElos(centerLogic);
			item2Logic.SetUpElos(null);
			item2Logic.SetDownElos(null);
			
			centerLogic.SetRightElos(item2Logic);
			centerLogic.SetLeftElos(null);
			centerLogic.SetUpElos(null);
			centerLogic.SetDownElos(item1Logic);
			
			item3Logic.SetRightElos(null);
			item3Logic.SetLeftElos(item2Logic);
			item3Logic.SetUpElos(null);
			item3Logic.SetDownElos(null);
		}

		Rect rt2 = new Rect();
		Rect rt1 = new Rect();
		Rect rt3 = new Rect();
		centerLogic.SetRect(rt);
		
		int nHeight = centerLogic.GetRect().height();
		int nWidth = centerLogic.GetRect().width();
		rt2.set(centerLogic.GetRect());
		rt2.offset(nWidth, 0);		
		rt1.set(centerLogic.GetRect());
		rt1.offset(0, nHeight);		
		rt3.set(rt2);
		rt3.offset(nWidth, 0);
		
		centerLogic.GetRightElos().SetRect(rt2);
		centerLogic.GetRightElos().GetRightElos().SetRect(rt3);
		centerLogic.GetDownElos().SetRect(rt1);
		
		return centerLogic;
	}
	
	//NNN
	//NNN
	private ElosLogic AssembleElosType5(ElosLogic centerLogic, Rect rt){
		if (centerLogic == null){
			Log.d(TAG, "create Center logic");
			centerLogic = new ElosLogic(true);
			
			ElosLogic item1Logic = new ElosLogic(false);
			ElosLogic item2Logic = new ElosLogic(false);
			ElosLogic item3Logic = new ElosLogic(false);
			
			item1Logic.SetRightElos(item2Logic);
			item1Logic.SetLeftElos(null);
			item1Logic.SetUpElos(centerLogic);
			item1Logic.SetDownElos(null);
			
			item2Logic.SetRightElos(null);
			item2Logic.SetLeftElos(item1Logic);
			item2Logic.SetUpElos(item3Logic);
			item2Logic.SetDownElos(null);
			
			centerLogic.SetRightElos(item3Logic);
			centerLogic.SetLeftElos(null);
			centerLogic.SetUpElos(null);
			centerLogic.SetDownElos(item1Logic);
			
			item3Logic.SetRightElos(null);
			item3Logic.SetLeftElos(centerLogic);
			item3Logic.SetUpElos(null);
			item3Logic.SetDownElos(item2Logic);
		}

		Rect rt2 = new Rect();
		Rect rt1 = new Rect();
		Rect rt3 = new Rect();
		centerLogic.SetRect(rt);
		
		int nHeight = centerLogic.GetRect().height();
		int nWidth = centerLogic.GetRect().width();
		rt3.set(centerLogic.GetRect());
		rt3.offset(nWidth, 0);		
		rt2.set(rt3);
		rt2.offset(0, nHeight);		
		rt1.set(rt2);
		rt1.offset(-nWidth, 0);
		
		centerLogic.GetRightElos().SetRect(rt3);
		centerLogic.GetRightElos().GetDownElos().SetRect(rt2);
		centerLogic.GetDownElos().SetRect(rt1);
		
		return centerLogic;
	}
	
	
	private class ElosLogic{
		private Canvas canvas = null;
		private Drawable dr = null;
		private int nModOffset = 0;
		private boolean bRecursiveable = true;		
		
		private int nLineNumber = 0;
		
		private Rect rtElos;
		private ElosLogic leftElos = null;
		private ElosLogic rightElos = null;
		private ElosLogic upElos = null;
		private ElosLogic downElos = null;
		
		private boolean bCenterLogic = false;
		
		public int GetLineNumber(){ return nLineNumber; }
		public void SetLineNumber(int nLineNumber){ this.nLineNumber = nLineNumber; }
		
		public boolean IsRecursiveable() { return bRecursiveable; }
		public void SetRecursiveable(boolean bRecursiveable){ this.bRecursiveable = bRecursiveable; }
		
		public ElosLogic(Rect rt, boolean bCenterLogic){ rtElos = rt; this.bCenterLogic = bCenterLogic;}
		public ElosLogic(boolean bCenterLogic){this.bCenterLogic = bCenterLogic;}
		
		public ElosLogic CopyElosLogic(){
			ElosLogic elosLogic = new ElosLogic(bRecursiveable);
			elosLogic.canvas = canvas;
			elosLogic.dr = dr;
			elosLogic.nModOffset = nModOffset;
			elosLogic.bRecursiveable = bRecursiveable;
			elosLogic.nLineNumber = nLineNumber;
			elosLogic.rtElos = rtElos;
			elosLogic.leftElos = leftElos;
			elosLogic.rightElos = rightElos;
			elosLogic.upElos = upElos;
			elosLogic.downElos = downElos;
			
			return elosLogic;			
		}
		
		public void SetDrawable(Drawable dr){ this.dr = dr; }
		public void SetCanvas(Canvas canvas){ this.canvas = canvas; }
		
		public void SetModOffset(int nModOffset){ this.nModOffset = nModOffset; }
		
		public void SetRect(Rect rt){rtElos = rt; }
		public Rect GetRect(){ return rtElos; }
		
		public boolean IsCenterLogic(){ return bCenterLogic; }
		
		public ElosLogic GetLeftElos(){ return leftElos; }
		public ElosLogic GetRightElos(){ return rightElos; }
		public ElosLogic GetUpElos(){ return upElos; }
		public ElosLogic GetDownElos(){ return downElos; }
		
		public void SetLeftElos(ElosLogic leftElos){ this.leftElos = leftElos; }
		public void SetRightElos(ElosLogic rightElos){ this.rightElos = rightElos; }
		public void SetUpElos(ElosLogic upElos){ this.upElos = upElos; }
		public void SetDownElos(ElosLogic downElos){ this.downElos = downElos; }
		
		public void DrawElosLogic(Canvas canvas){
			Log.d(TAG, "Draw Elos with Logic" + rtElos.left + "," + rtElos.top + "," + rtElos.width() + "," + rtElos.height());
			if (dr == null || canvas == null) { Log.d(TAG, "drawable or canvas is null"); return;}
			DrawElosWithDrawable(rtElos.left, rtElos.bottom, nModOffset, dr, this.canvas != null?this.canvas:canvas);
		}
	}
	
	private void RecursiveDrawElosUnit(ElosLogic centerLogic, Canvas canvas){
		if (centerLogic == null){
			Log.d(TAG, "center logic is null");
			return;
		}
		Log.d(TAG, "RecursiveDrawElosUnit");
		centerLogic.SetRecursiveable(false);
		centerLogic.DrawElosLogic(canvas);
		
		if (centerLogic.GetLeftElos() != null && centerLogic.GetLeftElos().IsRecursiveable()) RecursiveDrawElosUnit(centerLogic.GetLeftElos(), canvas);
		if (centerLogic.GetRightElos() != null && centerLogic.GetRightElos().IsRecursiveable()) RecursiveDrawElosUnit(centerLogic.GetRightElos(), canvas);
		if (centerLogic.GetUpElos() != null && centerLogic.GetUpElos().IsRecursiveable()) RecursiveDrawElosUnit(centerLogic.GetUpElos(), canvas);
		if (centerLogic.GetDownElos() != null && centerLogic.GetDownElos().IsRecursiveable()) RecursiveDrawElosUnit(centerLogic.GetDownElos(), canvas);
		//centerLogic.SetRecursive(true);
	}
	
	private void RecursiveSetElosUnitDrawable(ElosLogic elosLogic, Drawable dr){
		if (elosLogic == null){
			Log.d(TAG, "center logic is null");
			return;
		}
		Log.d(TAG, "RecursiveSetElosUnitDrawable");
		elosLogic.SetRecursiveable(false);
		elosLogic.SetDrawable(dr);
		
		if (elosLogic.GetLeftElos() != null && elosLogic.GetLeftElos().IsRecursiveable()) RecursiveSetElosUnitDrawable(elosLogic.GetLeftElos(), dr);
		if (elosLogic.GetRightElos() != null && elosLogic.GetRightElos().IsRecursiveable()) RecursiveSetElosUnitDrawable(elosLogic.GetRightElos(), dr);
		if (elosLogic.GetUpElos() != null && elosLogic.GetUpElos().IsRecursiveable()) RecursiveSetElosUnitDrawable(elosLogic.GetUpElos(), dr);
		if (elosLogic.GetDownElos() != null && elosLogic.GetDownElos().IsRecursiveable()) RecursiveSetElosUnitDrawable(elosLogic.GetDownElos(), dr);
		//elosLogic.SetRecursive(true);
	}
	
	private void RecursiveSetElosUnitOffset(ElosLogic elosLogic, int nOffsetX, int nOffsetY){
		if (elosLogic == null){
			Log.d(TAG, "center logic is null");
			return;
		}
		Log.d(TAG, "RecursiveSetElosUnitDrawable");
		elosLogic.SetRecursiveable(false);
		elosLogic.GetRect().offset(nOffsetX, nOffsetY);
		
		if (elosLogic.GetLeftElos() != null && elosLogic.GetLeftElos().IsRecursiveable()) RecursiveSetElosUnitOffset(elosLogic.GetLeftElos(), nOffsetX, nOffsetY);
		if (elosLogic.GetRightElos() != null && elosLogic.GetRightElos().IsRecursiveable()) RecursiveSetElosUnitOffset(elosLogic.GetRightElos(), nOffsetX, nOffsetY);
		if (elosLogic.GetUpElos() != null && elosLogic.GetUpElos().IsRecursiveable()) RecursiveSetElosUnitOffset(elosLogic.GetUpElos(), nOffsetX, nOffsetY);
		if (elosLogic.GetDownElos() != null && elosLogic.GetDownElos().IsRecursiveable()) RecursiveSetElosUnitOffset(elosLogic.GetDownElos(), nOffsetX, nOffsetY);
		
	}
	
	private void RecursiveEraseElosUnit(ElosLogic elosLogic){
		if (elosLogic == null){
			Log.d(TAG, "center logic is null");
			return;
		}
		Log.d(TAG, "RecursiveSetElosUnitDrawable");
		elosLogic.SetRecursiveable(false);
		Rect rt = elosLogic.GetRect();
		evElos.postInvalidate(rt.left, rt.top, rt.right, rt.bottom);		
		
		if (elosLogic.GetLeftElos() != null && elosLogic.GetLeftElos().IsRecursiveable()) RecursiveEraseElosUnit(elosLogic.GetLeftElos());
		if (elosLogic.GetRightElos() != null && elosLogic.GetRightElos().IsRecursiveable()) RecursiveEraseElosUnit(elosLogic.GetRightElos());
		if (elosLogic.GetUpElos() != null && elosLogic.GetUpElos().IsRecursiveable()) RecursiveEraseElosUnit(elosLogic.GetUpElos());
		if (elosLogic.GetDownElos() != null && elosLogic.GetDownElos().IsRecursiveable()) RecursiveEraseElosUnit(elosLogic.GetDownElos());
		
	}
	
	private void InitElosUnitRecursiveable(ElosLogic elosLogic){
		if (elosLogic == null){
			Log.d(TAG, "center logic is null");
			return;
		}
		Log.d(TAG, "InitElosUnitRecursiveable");
		elosLogic.SetRecursiveable(true);
		
		if (elosLogic.GetLeftElos() != null && !elosLogic.GetLeftElos().IsRecursiveable()) InitElosUnitRecursiveable(elosLogic.GetLeftElos());
		if (elosLogic.GetRightElos() != null && !elosLogic.GetRightElos().IsRecursiveable()) InitElosUnitRecursiveable(elosLogic.GetRightElos());
		if (elosLogic.GetUpElos() != null && !elosLogic.GetUpElos().IsRecursiveable()) InitElosUnitRecursiveable(elosLogic.GetUpElos());
		if (elosLogic.GetDownElos() != null && !elosLogic.GetDownElos().IsRecursiveable()) InitElosUnitRecursiveable(elosLogic.GetDownElos());
	}
	
	private TimerThread thdTimer = null;
	private class TimerThread extends Thread{
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			
			while (bStartElos && nTimeInterval > 0){
				if (player == null){
					//Toast.makeText(Elos.this, "方块还没有出现？？？", Toast.LENGTH_SHORT).show();
					Log.d(TAG, "player is null");
					break;
				}
				
				
				
				try {
					sleep(nTimeInterval);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
								
			//	RecursiveEraseElosUnit(player);
				
				InitElosUnitRecursiveable(player);				
				boolean	bMoveable = IsRecursiveElosLogicUnitMoveableDown(player);
				
				if (!bMoveable){
					bGuardRejoin = true;					
					InitElosUnitRecursiveable(player);
					RecursiveRecalcHome(player);
					RemoveELosGuardWholeLines();
					CreatePlayer();
				}
				
				InitElosUnitRecursiveable(player);
				RecursiveSetElosUnitOffset(player, 0, nPlayerStep);
				InitElosUnitRecursiveable(player);
				RecursiveDrawElosUnit(player, elosCanvas);				
				evElos.postInvalidate();	
				
				bActionDown = false;
			}
		}
		
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (thdTimer != null) thdTimer.stop();
	};
	
	*/
}
