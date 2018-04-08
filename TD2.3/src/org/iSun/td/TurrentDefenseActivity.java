package org.iSun.td;

import org.loon.framework.android.game.LGameAndroid2DActivity;

/**
 * ������Ϸ������Activity
 * @author iSun
 *
 */
public class TurrentDefenseActivity extends LGameAndroid2DActivity {
	TurretDefense td;
	String mapName;
	Point start;
	Point end;
	int mapId;
	String[] maps = new String[] { "assets/map/map_00_snake.txt",
			"assets/map/map_01_m.txt", "assets/map/map_02_circle.txt",
			"assets/map/map_03_ling.txt", "assets/map/map_04_ru.txt" };
	Point[] starts = new Point[] { new Point(64, 416), new Point(64, 224),
			new Point(32, 160), new Point(32, 32), new Point(32, 352) };
	Point[] ends = new Point[] { new Point(480, 416), new Point(416, 224),
			new Point(256, 256), new Point(320, 384), new Point(416, 352) };

	@Override
	public void onMain() {
		// TODO Auto-generated method stub
		// ����
		init();
		this.initialization(true);
		// ����ʾlogo
		this.setShowLogo(false);
		// ��ʾfps
		// this.setShowFPS(true);
		// ��ʼ����ʹ��TD
		td = new TurretDefense(mapName, start, end);
		this.setScreen(td);
		// ��ʾ����
		this.showScreen();

	}

	public void init() {
		mapId = this.getIntent().getIntExtra("mapId", 0);
		mapName = maps[mapId];
		start = starts[mapId];
		end = ends[mapId];

	}
	/** Called when the activity is first created. */

}