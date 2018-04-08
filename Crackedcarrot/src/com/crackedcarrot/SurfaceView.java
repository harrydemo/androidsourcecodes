package com.crackedcarrot;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.crackedcarrot.UI.UIHandler;

public class SurfaceView extends GLSurfaceView {

	public GameLoop gameLoop = null;

	// Towertype to build, set by the GUI.
	public int towerType = 0;

	// Not very magic, read the comment below for explanation.
	private int magicValue;
	private boolean buildTower = false;
	private UIHandler ui;

	@Override
	public boolean onTouchEvent(MotionEvent me) {
		int action = me.getAction();
		int x = (int) me.getX();
		int y = magicValue - (int) me.getY();
		boolean test = false;
		// We need to do this because Java and our grid counts backwards.
		// 480 - clickedYValue = the correct Y-value, for example, on a
		// screen with a 480 Y-resolution.
		// we build where the user last touched the screen.

		if (action == MotionEvent.ACTION_UP) {

			if (buildTower && gameLoop.gridOcupied(x, y))
				gameLoop.showTowerUpgradeUI(x, y);

			if (buildTower) {
				test = gameLoop.createTower(new Coords(x, y), towerType);
			}

			if (ui != null && gameLoop.gridOcupied(x, y)) {

				int[] data = gameLoop.getTowerCoordsAndRange(x, y);
				if (data != null) {
					ui.showRangeIndicator(data[0], data[1], data[2], data[3],
							data[4]);
					if (!buildTower)
						gameLoop.showTowerUpgradeUI(x, y);

					test = true;
				} else {
					// Log.d("SURFACEVIEW","Guru Meditation: Cant get towerdata");
				}
			}
			if (buildTower && !test && ui != null) {

				// You are not allowed to place tower here
				ui.blinkRedGrid();
			}

			return false;
		}

		return true;
	}

	public SurfaceView(Context context) {
		super(context);
	}

	public SurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setScreenHeight(int i) {
		this.magicValue = i;
	}

	public void setSimulationRuntime(GameLoop simulationRuntime) {
		this.gameLoop = simulationRuntime;
	}

	public void setTowerType(int i) {
		if (i == -1) {
			this.buildTower = false;
		} else
			this.buildTower = true;
		this.towerType = i;
	}

	public void setHUDHandler(UIHandler hudHandler) {
		ui = hudHandler;
	}
}
