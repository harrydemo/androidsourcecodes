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
	private ElosLogic player = null;//����һ���Զ���������
	private ElosView evElos = null;
	private ElosStateView esvElos = null;
	private ElosLogic[][] elosGuard = null;//�����������
	private Drawable[] drArr = new Drawable[SQUARELEMENT_NUMBERS];//���淽���ͼƬ
	private Drawable drArrCartoonBG = null;//���汳��ͼƬ
	private int nElosHeight = 0;//����һ��������ɷ���ͼƬ�ĸߵ���
	private int nElosWidth = 0;//����һ��������ɷ���ͼƬ�Ŀ����
	private boolean bReset = true;//Ϊtrueʱ��ʾ��ʼ
	private boolean bPause =true;//false��ʾ������ͣʹ�õ�
	int DRAWABLE_CHOICES = SQUARELEMENT_NUMBERS;//����һ��һ���������������ȡͼƬ�����ͼ��
	int ELOS_CHOICES = 15;
	int time=0;//ʱ��
	int ntime=0;//�ۼ�ʱ��
	int nTimeInterval = 500;//��ʼֵΪ500��ʾ��һ�ص��ܷ�Ϊ500*10
	int nCents = 0;//��Ϸ�÷�
	int nStage = 1;//��Ϸ�ؿ�
	int nOldStage = 0;//������һ���ؿ���
	private int nTotalLines = 0;
	private int nTotalColumns = 0;//��������߽��������������
	int nElosYOffset = 0;
	int nElosXOffset = 0;//��������Ļ����ʼ��
	private int nElosAreaWidth = 0;
	private int nElosAreaHeight = 0;//�������������Ŀ���ߵ���
	Thread thdTimer =  null;//����һ���߳�
	boolean bReportResult = false;//ֻ�е���Ϸʧ�ܺ�ֵ��Ϊtrue��������Ļ����ʾ���
	boolean bPlaying = true;//������ΪĬ�ϵ�true��ʾ�߳��￪ʼ����
	boolean start=false;
	int nPlayerChoice = 0;//����������ȡ����ʾҪ��ʾ�Ǹ�����ͼ��ʱ�����������
	int nDrawableChoice = 0;//����������ȡ����ʾѡ������ͼƬ��Ϊ���췽��ͼ�θ�ֵ���������
	
	//��ȡ��ԴͼƬ
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
	
	//-----����û���ⶨÿ��СͼƬ�Ĵ�С�����Ե��ú�������趨�Ŀ�͸�--------
	private int GetElosHeight(Drawable dr){
		return dr.getMinimumHeight();
	}
	private int GetElosWidth(Drawable dr){
		return dr.getMinimumWidth();
	}
	//-------------------------------------------------------------------
		
	private void AnnounceNewStage(){//��ʾ�ؿ�����ÿ�ع����½��ٶȼӿ�
		if (nOldStage != nStage){
			Toast.makeText(this, "��" + nStage + "��", Toast.LENGTH_SHORT).show();
			nTimeInterval = nTimeInterval - (nStage - 1)*50;//ÿ�غ�˯��ʱ�����
			nOldStage = nStage;
		}
	}
	
	private void ClearStageAndCentsAfterFail(){//��ʼ��ʱ��Ҫ��չؿ��ͷ���
		nCents = 0;
		nStage = 1;
		time=0;
		ntime=0;
	}
	
	//---------------------------------��Ϸʧ��-------------------------------------------
	private boolean IsElosGameFail(){
		thdTimer=null;
		ElosLogic visit = elosGuard[0][0];
		while (visit != null){
			if (visit.IsDraw() && !visit.bUnit) return true;
			visit = visit.GetElosLogicRight();//һֱ����߷�������ĵ�һ��Ѱ�Ҵ�������ͼƬ�����������򷵻�true��ʾʧ��
		}
		return false;
	}
	
	//------------------------------�ж�Ҫ��ȥ������----------------------------------
	private boolean IsWholeLineDrawed(ElosLogic logic){
		if (logic == null) return false;
		ElosLogic keep1, keep2;//��������ElosLogic�ı���
		keep1 = keep2 = logic;
		int nCount = 0;
		//ͨ�������ֱ�����ѭ�������鿴��һ���Ƿ���Ҫ����
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
		if (nCount == this.nTotalColumns) return true;//�����������ͬ������Ҫ��������true
		return false;
	}
	
	private void ClearWholeLine(ElosLogic logic){//���Ҫ����������һ��ȫ�趨Ϊ����ʾ
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
		src.bDraw = false;//����һ�����óɲ�����
		des.SetDrawable(src.GetDrawable());//����һ�б�������һ�е�ͼƬ
	}
	
	private void ElosLogicLineCopyAfterLineFull(ElosLogic logic){//û���������������Զ��½�
		Rect rtElos = logic.GetRect();
		Rect rtGuardZero = elosGuard[0][0].GetRect();
		int nLineNumber = (rtElos.top - rtGuardZero.top)/rtElos.height();
		while (nLineNumber >= 1){
				for (int i = 0; i < this.nTotalColumns; i++){
					//���ζ���һ�е�ÿ�ж�������һ�е�ͼƬ
					ElosLogicCopyAfterLineFull(elosGuard[nLineNumber - 1][i], elosGuard[nLineNumber][i]);
				}
			nLineNumber--;
		}
	}
	
	int CheckGuard(ElosLogic elosPlayer){//ÿ�β����½���ʱ��Ҫ�˶�һ����в���
		int nErasableLines = 0;
		if (elosPlayer == null) return nErasableLines;
		if (IsWholeLineDrawed(elosPlayer)){
			ClearWholeLine(elosPlayer); 
			ElosLogicLineCopyAfterLineFull(elosPlayer);
			nErasableLines++;
			}
		if (elosPlayer.GetElosLogicDown() != null){//���ϲ�����
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
		if (elosPlayer.GetElosLogicUp() != null){//���ϲ�һ��
			if (IsWholeLineDrawed(elosPlayer.GetElosLogicUp())){
				ClearWholeLine(elosPlayer.GetElosLogicUp());
				ElosLogicLineCopyAfterLineFull(elosPlayer.GetElosLogicUp());
				nErasableLines++;
			}
		}
		return nErasableLines;//������Ҫ����������
	}
	

	//-------�������������Ƿֱ���Ӧ���������Һ��µ�ʱ����Ϸ�����е��ƶ�-------------
	private ElosLogic PlayerMoveDown(ElosLogic elosPlayer, int nPlayerChoice){//������¿����ƶ��ľ͵ý��в���
		if (elosPlayer == null) return elosPlayer;
		if (!IsPlayerMoveableDown(elosPlayer)) {return elosPlayer; }
		Drawable dr  = elosPlayer.GetDrawable();
		ClearPlayer(elosPlayer);
		elosPlayer = elosPlayer.GetElosLogicDown();//��������Ѿ�������һ�е���
		elosPlayer = CreatePlayer(dr, nPlayerChoice, elosPlayer);//��������һ��������ǰһ���ķ���
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
	
	//--------------------------��ʱ�����һ��ͼ��--------------------------------
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
	
	//--------------------�ж������ҵ�ֵ���ص�����IsRecursiveElosLogic��---------------------------------
	private boolean IsElosLogicDownable(ElosLogic logic){//�ж������������
		if (logic == null){ return false;}
		if (logic.GetElosLogicDown() == null) { return false;}//��ʾͼ���Ѿ������˷���false
		if (logic.GetElosLogicDown().bUnit) return true;//��ʾ�����Ѿ���ͼ���˷���true
		if (logic.GetElosLogicDown().IsDraw()) { return false;}//�����û�л���ͼƬ�򷵻�false
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
	
	//------------------------------------ͨ������������������ֵ�ж��������ܷ��ƶ�-----------------------
	boolean IsRecursiveElosLogicDownable(ElosLogic elosLogic){//�������溯������ֵ�жϷ����Ƿ�����ƶ�
		if (!IsElosLogicDownable(elosLogic)) return false;
			elosLogic.bUnit = false;//ÿ�ζԷ����е�һ���������жϲ����Ƿ�����½����Ƚ�����Ϊfalse��ʾû��
		if (elosLogic.GetElosLogicDown() != null && elosLogic.GetElosLogicDown().bUnit)
			if(!IsRecursiveElosLogicDownable(elosLogic.GetElosLogicDown())) {//ֻҪ��һ�������ܶ���ʱ��͵ý�bUnit��Ϊtrue��ʾ����
				return false;}
		elosLogic.bUnit = true;//֪��ͨ���������Ҽ�����ȷ�����ƶ��ڽ����趨Ϊtrue
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
	
	//----------------------------����ܷ��ƶ��Ľ���󷵻�true��false��ֵ���߳���---------------------------
	private boolean IsPlayerMoveableDown(ElosLogic elosPlayer){
		if (elosPlayer == null) return false;//���õ���ߵ������е�����Ϊnull
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
					//�����ۼ�ʱ���
					ntime=ntime+nTimeInterval;
					if(ntime%1000==0)
						time++;
				
					if (IsElosGameFail()){
						bPlaying = false;
						bReportResult = true;
						evElos.postInvalidate(); 
					}
					
					//������player������ط���ָ�������꣨1��nTotalColumns/2��
					if (!Elos.this.IsPlayerMoveableDown(player)){
						ClearElosLogicUnitUnitTag(player);
						int nErasableLine = CheckGuard(player);//���Ҫ����������
						nCents += 100*nErasableLine + (int)((nErasableLine == 0? 1: nErasableLine) - 1)*50;//�������������������϶��������ȥ�Ĳ�����һ�ٳ�50	
						nStage = nCents/(10*nTimeInterval) + 1;//����ÿ���ؿ��ļ���
						player = CreatePlayer(drArr[GetRandomDrawableChoice()], GetRandomPlayerChoice());//û�˶���һ�����䶼Ҫ������һ�ε�����					
					}else{
						player = Elos.this.PlayerMoveDown(player, nPlayerChoice);	
					}
					evElos.postInvalidate();//ִ��������Ķ�Ҫ���»���һ�½���
				}
			}
		}		
	}
	
	//--------------�������������������ò˵���----------------------
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 0, 0, "��ʼ");
		menu.add(0, 1, 1, "�˳�");
		menu.add(0, 2, 2, "��ͣ");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == 0){
			bPlaying = true;
			bPause = true;
			start=true;
			MakeElosLogicNet();//����߽����ÿ���������±���
			evElos.postInvalidate();
			ClearStageAndCentsAfterFail();//�����Ϸ�ķ����͹ؿ�
			
			//-------------------����ĺ�����ͨ����������������ͼƬ��ʹ��ͼƬ��ɵ�ͼ��-------------
			player = CreatePlayer(drArr[GetRandomDrawableChoice()], GetRandomPlayerChoice());
			//��������һ������ͼ�κ��Ҫ����һ���߳�
			if (thdTimer == null){
				thdTimer = new TimerThread();
				thdTimer.start();
			}
		}
		if (item.getItemId()==1){	
			this.finish();
		}
		if (item.getItemId()==2){
			if (item.getTitle().equals("��ͣ")){
				bPause = false;
				item.setTitle("����");
				evElos.setEnabled(false);
			}else {
				bPause = true;
				item.setTitle("��ͣ");
				evElos.setEnabled(true);
			}
		}
		return super.onOptionsItemSelected(item);
	}
	//---------------------------------------------------------
	
	//-------------------------��ڵ�������---------------------------------
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
		
		//����ʱ����ʾ��Ĳ���
		params.weight = 1;
		elos.addView(evElos = new ElosView(this), params);
		params = new LinearLayout.LayoutParams(params);
		params.weight = 5;
		elos.addView(esvElos = new ElosStateView(this), params);
		
		RetriveDrawable(this);//��ͼƬ������ȡ
		nElosHeight = GetElosHeight(drArr[0]);
		nElosWidth = GetElosWidth(drArr[0]);
	}
	
	//-----------------------------�����¼��������ͼ��̣�--------------------------
	@Override
	public boolean onTouchEvent(MotionEvent event){	 
	    //�����Ļ�Ŀ�͸�
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
	

	//��������Ϸ���ı���
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
	
	//----------------������ֳɵ�����ÿ����newһ���Զ������ElosLogic����-----------------
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
	
	//---------------������������ÿ�������ܻ�ʹ�õ����ݱ��浽�Զ�������---------------
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
				
				//----------------�����������ÿ�����Ŀ��------------------
				rt.left = j*this.nElosWidth + nElosXOffset;
				rt.top = i*this.nElosHeight + this.nElosYOffset;
				rt.right = rt.left + this.nElosWidth;
				rt.bottom = rt.top + this.nElosHeight;
				elosGuard[i][j].SetRect(rt);//�������е�ÿ�����ı߿򱣴浽�Զ�������
				
				elosGuard[i][j].SetDrawable(drArr[0]);
				
				elosGuard[i][j].SetDraw(false);
				elosGuard[i][j].bUnit = false;
			}		
		}
	}
	//--------------------------------------------------------------------------------
	
	//----------------------------����������Ҫ���������ƽ����-------------------------
	Rect rtDraw = new Rect();	//��ͼ
	private void DrawElosLogicNet(Canvas canvas, ElosLogic[][] elosLogicGuard,Rect rtDraw){
		for (int i = 0; i < elosLogicGuard.length; i++)
			for (int j = 0; j < elosLogicGuard[i].length; j++)
				if (elosLogicGuard[i][j].IsDraw())
				DrawElos(elosLogicGuard[i][j], canvas);//�������ÿ�������л���
	}
	
	private void DrawElos(ElosLogic elosLogic, Canvas canvas){
		if (!elosLogic.IsDraw()) return;
		Drawable dr = elosLogic.GetDrawable();
		if (dr == null) return;	
		//-----------������������е�ÿ�����ı߿��ͼƬ-----------------
		rtDraw.set(elosLogic.GetRect());
		rtDraw.right = rtDraw.right - 1/2;
		rtDraw.bottom = rtDraw.bottom - 1/2;
		dr.setBounds(rtDraw);
		dr.draw(canvas);//��ͼƬ������Ӧ�ľ��ο����
		//------------------------------------------------------------
	}
	//-----------------------------------------------------------------------------
	
	//----------------------------������߽������----------------------------------
	private class ElosView extends View{

		public ElosView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			super.onDraw(canvas);
			
			Paint pt = new Paint();//��û���
			Rect rtDraw=new Rect();
			Rect rt = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());//��ñ߿�ܵľ���
			pt.setColor(Color.RED);
			drArrCartoonBG.setBounds(rt);
			drArrCartoonBG.draw(canvas);
			
			if (bReset){
				//-------------�����Ļ�Ŀ�͸�------------------
				nElosAreaHeight = this.getHeight();
				nElosAreaWidth = this.getWidth();	
				//-------------�õ����Էֳɵ����������������Ե�ľ���-----------------
				nTotalLines = nElosAreaHeight/nElosHeight + 3;
				nElosYOffset = nElosAreaHeight%nElosHeight - 3*nElosHeight;
				nTotalColumns = nElosAreaWidth/nElosWidth;
				nElosXOffset = nElosAreaWidth%nElosWidth/2;
				rtDraw =new Rect(nElosXOffset,nElosYOffset,nTotalColumns*nElosWidth,nTotalLines*nElosHeight);
				
				InitElosLogicGuard();//�Էֳɵ�����ÿ����newһ������
				MakeElosLogicNet();//����ÿ��������Ϣ���Զ������
				bReset = false;
			}
			DrawElosLogicNet(canvas, elosGuard,rtDraw);//���ƽ���
			
			if (bReportResult){
				Toast.makeText(Elos.this, "��Ϸ����,�÷֣�" + nCents, Toast.LENGTH_SHORT).show();
				bReportResult = false;
			}
		}		
	}
	//------------------------------------------------------------------------------
	
	//��������ʹ������������
	private int GetRandomPlayerChoice(){		
		return nPlayerChoice = ((int)(Math.random()*10000))%ELOS_CHOICES;
	}
	
	private int GetRandomDrawableChoice(){		
		return nDrawableChoice = ((int)(Math.random()*10000))%DRAWABLE_CHOICES;
	}
	
	//������15�����������ͼ������ʾ����
	private void AssignElosLogic(ElosLogic elosLogic, Drawable dr){
		if (elosLogic == null) return;//���鲻�����򷵻�
		elosLogic.dr = dr;//�õ�ͼƬ
		elosLogic.bUnit = true;//
		elosLogic.SetDraw(true);//��SetDraw������true���ʾ���Ի���
	}
	
	//--------------����15��������ͨ�������ȡ��ͼƬ�����������������״---------------------
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
	
	//����߽���Ķ�������һ��Ҫ���µķ���ͼ��
	private ElosLogic CreatePlayer(Drawable dr, int nChoice){
		player = elosGuard[1][this.nTotalColumns/2];
		AssembleElosUnitType(player, dr, nChoice);
		return player;
	}
	
	//����߽���ĸ����������е�������һ��Ҫ���µķ���ͼ��
	private ElosLogic CreatePlayer(Drawable dr, int nChoice, ElosLogic elosLogic){
		AssembleElosUnitType(elosLogic,dr, nChoice);
		return elosLogic;
	}
	
	private void ClearPlayer(ElosLogic elosPlayer){//ÿ������ʾ��һ��ͼ�κ�Ҫ����������ɾ�
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
	//---------------------------------�����ұ߽������---------------------------------
	private class ElosStateView extends View{
		private int nAreaWidth = 0;
		private int nElosPrevHeight = 0;
		private int nElosPrevWidth = 0;
		private int nElosYOffset = 20;
		private int nElosXOffset = 0;
		private ElosLogic[][] logicPrevArr = new ElosLogic[6][6];
		private boolean bInitState = true;
		
		
		public ElosStateView(Context context) {//���캯��
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
					
					//-------------��ȡ�ұ߽�����ʾ��һ��ͼ�εĿ��-------------------
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
			//--------��ȡ�ұ߽���Ŀ����---------
			nAreaWidth = this.getWidth();
			this.getHeight();
			
			if (bInitState){//��ʼֵΪtrue��ʾҪ���ƣ�������ߵĽ����ڽ���ͼ���˶�Ӱ���ұ߽���
				InitLogicPrevArr();//���ú���
				bInitState = false;
			}
			
			Paint pt = new Paint();
			Rect rt = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
			pt.setColor(Color.GRAY);
			canvas.drawRect(rt, pt);//���ұ߽�����Ƴɻ�ɫ
			
			Rect rtDraw = new Rect();
			rtDraw.set(logicPrevArr[0][0].GetRect());
			rtDraw.right = logicPrevArr[5][5].GetRect().right;
			rtDraw.bottom = logicPrevArr[5][5].GetRect().bottom;
			
			Elos.this.ClearPlayer(logicPrevArr[2][2]);//ÿ�δ���һ��ͼ��ǰ��Ҫ���һ������
			Elos.this.CreatePlayer(drArr[Elos.this.nDrawableChoice], Elos.this.nPlayerChoice, logicPrevArr[2][2]);
			Elos.this.DrawElosLogicNet(canvas, logicPrevArr,rtDraw);//���ú���߽���ͬ���Ļ��ƺ������Ƴ�ͼ��
			//���ұߵĽ�������ʾһЩ�ؿ��ͷ���
			DrawStageAndCents(canvas, Elos.this.nStage, Elos.this.nCents, logicPrevArr[5][0].GetRect().left, logicPrevArr[5][0].GetRect().bottom + 100);
		}
		
		//���ұߵļ�����ʾ���ؿ��ͷ���
		private void DrawStageAndCents(Canvas canvas, int nStage, int nCents, int x, int y){
			Paint pt = new Paint();
			pt.setColor(Color.BLACK);
			pt.setTextSize(15);
			canvas.drawText("��" + nStage + "��", x, y, pt);
			canvas.drawText("����:", x, y + 40, pt);
			canvas.drawText(Integer.toString(nCents)+"��", x + 5, y + 55, pt);	
			canvas.drawText("ʱ��:", x, y+95, pt);
			canvas.drawText(Integer.toString(time),x+10,y+110,pt);
		}	
	}
}
