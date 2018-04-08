package com.yarin.android.MagicTower;
import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
public abstract class GameView extends View
{
	public GameView(Context context)
	{
		super(context);
	}
	/**
	 * ��ͼ
	 *
	 * @param		N/A		
	 *
	 * @return		null
	 */
	protected abstract void onDraw(Canvas canvas);
	/**
	 * ��������
	 *
	 * @param		N/A		
	 *
	 * @return		null
	 */
	public abstract boolean onKeyDown(int keyCode);
	/**
	 * ��������
	 *
	 * @param		N/A		
	 *
	 * @return		null
	 */
	public abstract boolean onKeyUp(int keyCode);
	/**
	 * ������Դ
	 *
	 */
	protected abstract void reCycle();	
	
	/**
	 * ˢ��
	 *
	 */
	protected abstract void refurbish();
}

