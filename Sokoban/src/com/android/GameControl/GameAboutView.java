package com.android.GameControl;

import android.content.Context;

import com.android.Sokoban.MainGame;
import com.android.Sokoban.Sokoban;

public class GameAboutView extends GameTextView
{
	public GameAboutView(
			Context context,
			Sokoban sokoban,
			MainGame mainGame)
	{
		super(
				context,
				sokoban,
				mainGame,
				GameTextView.GameTextViewType.ABOUT_VIEW);
	}
}