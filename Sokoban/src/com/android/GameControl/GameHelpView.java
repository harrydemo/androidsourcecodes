package com.android.GameControl;

import android.content.Context;

import com.android.Sokoban.MainGame;
import com.android.Sokoban.Sokoban;

public class GameHelpView extends GameTextView
{
	public GameHelpView(
			Context context,
			Sokoban sokoban,
			MainGame mainGame)
	{
		super(
				context,
				sokoban,
				mainGame,
				GameTextView.GameTextViewType.HELP_VIEW);
	}
}

