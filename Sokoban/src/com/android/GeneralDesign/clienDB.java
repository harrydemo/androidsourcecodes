package com.android.GeneralDesign;

public final class clienDB
{
	public static final String LOG_TAG = "Sokoban";
	public static final int	GAME_LOOP = 50;
	public static final int DEFAULT_COL_NUM = 15;
	public static final int DEFAULT_ROW_NUM = 15;
	public static final int MAX_COL_NUM = 15;
	public static final int MAX_ROW_NUM = 15;
	public static final int MP_ANIMAITON_FRAME_PERIOD = (int)(0.5 * 1000/GAME_LOOP);
	public static final class GameObjectID
	{
		public static final int NULL = 0;
		public static final int WALL = 1;
		public static final int PATH = 2;
		public static final int DEST = 3;
		public static final int BOX = 4;
		public static final int MP = 5;
		public static final int CURSOR = 6;
	}
	
	/*
	private static Paint paint = new Paint();
	public static void drawGrid(Canvas canvas)	
	{
		int i;
		int tiledWidth = 50;
		int tiledHeight = 50;
		int colNum = 480 / tiledWidth;
		int rowNum = 480 / tiledHeight;
		int gridRangeWidth = tiledWidth * colNum;
		int gridRangeHeight = tiledHeight * rowNum;
		paint.setColor(Color.rgb(50, 50, 50));
		paint.setStrokeWidth(1);
		int x1, y1;
		int x2, y2;
		
		for(i = 0; i <= colNum; i++)
		{
			x1 = tiledWidth * i;
			y1 = 0;
			x2 = x1;
			y2 = gridRangeHeight;
			canvas.drawLine(x1, y1, x2, y2, paint);
			canvas.drawText("" + x1, x1 + 2, y1 + 10, paint);
		}
		for(i = 0; i <= rowNum; i++)
		{
			x1 = 0;
			y1 = tiledHeight * i;
			x2 = gridRangeWidth;
			y2 = tiledHeight * i;
			canvas.drawLine(x1, y1, x2, y2, paint);
			canvas.drawText("" + y1, x1 + 2, y1 + 10, paint);
		}
	}
	*/
}