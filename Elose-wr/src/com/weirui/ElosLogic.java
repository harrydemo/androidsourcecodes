package com.weirui;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import com.weirui.ElosLogic;

public class ElosLogic {		
	int nNumber = -1;	
	boolean bRecursive = false;
	public boolean bPlayer = false;
	public boolean bUnit = false;//false表示左边界面的数组的量没存储
	public void SetNumber(int nNum){ this.nNumber = nNum; }
	public int GetNumber(){ return nNumber; }
	//-------------------------------------------
	boolean bDraw = false;//表示是否可以被绘制界面
	boolean IsDraw(){ return bDraw; }
	void SetDraw(boolean bDraw){ this.bDraw = bDraw; }
	//-----------------------------------------
	Rect rtElos = new Rect();
	public void SetRect(Rect rt){ rtElos.set(rt); }
	public Rect GetRect(){ return rtElos; }
	//------------------------------------------
	Drawable dr = null;//dr用来被赋予各种图片
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
