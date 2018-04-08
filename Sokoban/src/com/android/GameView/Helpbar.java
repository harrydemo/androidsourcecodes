package com.android.GameView;

import com.android.GeneralDesign.LayoutDesign;
import com.android.Sokoban.MainGame;

public class Helpbar extends TextItem
{
	public Helpbar(MainGame mainGame)
	{
		super(mainGame, LayoutDesign.DisplayItemID.HELP_BAR, 10);
	}
	@Override
	protected void finalize() throws Throwable
	{
		super.finalize();
	}
}
