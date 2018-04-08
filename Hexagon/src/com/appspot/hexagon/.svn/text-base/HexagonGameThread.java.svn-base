package com.appspot.hexagon;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.appspot.hexagon.constants.MapConstants;
import com.appspot.hexagon.dbo.ImageCrop;
import com.appspot.hexagon.dbo.LocationAnchor;
import com.appspot.hexagon.dbo.MapCell;
import com.appspot.hexagon.dbo.PlayerObject;
import com.appspot.hexagon.dbo.WorldMap;
import com.appspot.hexagon.util.CSVUtil;
import com.appspot.hexagon.util.FrameRateCounter;
import com.appspot.hexagon.util.Graphic2DUtil;
import com.appspot.hexagon.util.HexagonDBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class HexagonGameThread extends Thread {
	public static final int STATE_PAUSE = 2;
	public static final int STATE_READY = 3;
	public static final int STATE_RUNNING = 4;

	private PlayerObject avatar = null;
	private PlayerObject avatarHexagon = null;
	private LocationAnchor nextAnchor = null;
	private Resources res = null;
	private static int background_color = Color.BLACK;
	private FrameRateCounter fpsCounter = null;
	private Handler handler = null;
	private SurfaceHolder holder = null;
	private Context context = null;
	private int gameMode;
	private boolean isRunning = false;
	private RectF gameBackground = null;
	private Paint cPaint = null;
	private DecimalFormat df = null;
	private boolean drawBackground;
	private boolean drawAnchor;
	private Canvas canvas = null;
	private Bitmap rawTileMapImg = null;
	private Bitmap[][] tileImg = null;
	// private HexagonDBHelper dbHelper = null;
	// private SQLiteDatabase db = null;
	public static String TAG = HexagonGameThread.class.getName();
	public List<String[]> lsMapData = null;
	public static final List<ImageCrop> imageMapss = MapConstants.getImageGalleries();

	public HexagonGameThread(SurfaceHolder holder, Context context,
			Handler handler) {
		this.holder = holder;
		this.handler = handler;
		this.context = context;
		// this.

		df = new DecimalFormat("#########0.00");
		res = context.getResources();

		drawBackground = true;
		drawAnchor = true;

		init();
	}

	public void init() {
		// dbHelper = new HexagonDBHelper(context);
		lsMapData = populateWorld(R.raw.map_vector);

		cPaint = new Paint();
		// canvas = holder.lockCanvas(null);
		// drawBackground(canvas);

		Drawable plant_circle = res.getDrawable(R.drawable.plant_circle);
		Drawable imgHexagon = res.getDrawable(R.drawable.hexagon);
		Drawable ylw_pushpin = res.getDrawable(R.drawable.ylw_pushpin);
		// TODO: replace with SQL query to get background image gallery
		int current_background_img = R.drawable.map_chip;
		rawTileMapImg = BitmapFactory.decodeStream(res
				.openRawResource(current_background_img));

		int screen_row = context.getWallpaperDesiredMinimumHeight() / 32;
		int screen_row_remainder = context.getWallpaperDesiredMinimumHeight() % 32;
		if (screen_row_remainder > 0) {
			++screen_row;
		}
		int screen_col = context.getWallpaperDesiredMinimumWidth() / 32;
		int screen_col_remainder = context.getWallpaperDesiredMinimumWidth() % 32;
		if (screen_col_remainder > 0) {
			++screen_col;
		}

		tileImg = new Bitmap[screen_row][screen_col];
		/***
		 * for(int row=0;row<screen_row;row++){ for(int
		 * col=0;col<screen_col;col++){ int left,top,right,bottom,height,width;
		 * // TODO: query from mapcell left = top = right = bottom = 0; height =
		 * width = 32; right = left + width; bottom = top + height;
		 * 
		 * //tileImg[row][col] = Bitmap.createBitmap(rawTileMapImg, left, top,
		 * right, bottom); } }
		 ****/
		Log.i(TAG, "screen(row=" + screen_row + ",col=" + screen_col + ")");
		tileImg = findMapTilesByCurrentLocation(screen_row, screen_col);

		nextAnchor = new LocationAnchor(0, 0, ylw_pushpin);
		nextAnchor.setStayAliveTime(1000);
		avatarHexagon = new PlayerObject(100, 100, imgHexagon);

		gameBackground = new RectF(0, 0, context
				.getWallpaperDesiredMinimumWidth(), context
				.getWallpaperDesiredMinimumHeight());
		fpsCounter = new FrameRateCounter();
	}

	public void doStart() {
		synchronized (holder) {
			setState(STATE_RUNNING);
		}
	}

	public void pause() {
		synchronized (holder) {
			if (gameMode == STATE_RUNNING)
				setState(STATE_PAUSE);
		}
	}

	@Override
	public void run() {
		while (isRunning) {
			try {
				canvas = holder.lockCanvas(null);
				synchronized (holder) {
					// --- update game state ---
					if (gameMode == STATE_RUNNING) {
						updatePhysics();
					}
					// -- render game --
					doDraw(canvas);
				}
			} finally {
				// do this in a finally so that if an exception is thrown
				// during the above, we don't leave the Surface in an
				// inconsistent state
				if (canvas != null) {
					holder.unlockCanvasAndPost(canvas);
				}
			}
			fpsCounter.update();
		}
	}

	private void updatePhysics() {
		// TODO Auto-generated method stub

	}

	public synchronized void restoreState(Bundle savedState) {
		synchronized (holder) {
			setState(STATE_PAUSE);
		}
	}

	private void setState(int gameMode) {
		this.gameMode = gameMode;
	}

	private void doDraw(Canvas canvas) {
		// draw background
		// TODO: do a query on map tile, draw background using map tiles
		if (drawBackground) {
			drawBackground(canvas);
		}

		// do a query on world objects, draw each of them on screen
		// TODO: draw Player(s) and other character(s)
		avatarHexagon.drawOnScreen(canvas);

		if (drawAnchor) {
			if (nextAnchor.isAlive()) {
				nextAnchor.drawOnScreen(canvas);
			}
		}

		String msg = "FPS = " + df.format(fpsCounter.getFps());
		cPaint.setColor(Color.WHITE);
		canvas.drawRoundRect(new RectF(0, 10, gameBackground.width() / 2, 30),
				2.0f, 2.0f, cPaint);
		cPaint.setColor(Color.BLUE);
		canvas.drawRoundRect(new RectF(2, 12, gameBackground.width() / 2 - 2,
				28), 2.0f, 2.0f, cPaint);
		cPaint.setColor(Color.WHITE);
		canvas.drawText(msg, 22F, 22F, cPaint);
	}

	private void drawBackground(Canvas canvas) {
		// TODO Auto-generated method stub
		cPaint.setColor(Color.BLACK);
		canvas.drawRect(gameBackground, cPaint);
		for (int row = 0; row < tileImg.length; row++) {
			for (int col = 0; col < tileImg[row].length; col++) {
				canvas.drawBitmap(tileImg[row][col], row
						* tileImg[row][col].getHeight(), col
						* tileImg[row][col].getWidth(), null);
			}
		}
	}

	// remove following 2 methods or move them to Graphic2DUtil
	public static Bitmap loadBitmapFromDrawable(Drawable sprite) {
		Bitmap.Config DEFAULT_BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
		int width = sprite.getIntrinsicWidth();
		int height = sprite.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				DEFAULT_BITMAP_CONFIG);
		Canvas canvas = new Canvas(bitmap);
		sprite.setBounds(0, 0, width, height);
		sprite.draw(canvas);
		return bitmap;
	}

	public static Drawable cropBitmap(Bitmap sprite, int left, int top,
			int right, int bottom) {
		Bitmap croppedSprite = Bitmap.createBitmap(sprite, left, top, right,
				bottom);
		return null;
	}

	private ArrayList<String[]> populateWorld(int fileIndex) {
		// ContentValues worldMapValues = null;
		// ContentValues mapCellValues = null;

		// initialization
		/*
		 * if (db == null) db = dbHelper.getWritableDatabase();
		 */
		InputStream is = res.openRawResource(fileIndex);

		// create world
		/*
		 * worldMapValues = new ContentValues();
		 * worldMapValues.put(WorldMap.Constants.DESC, "Level 1 Map");
		 * worldMapValues.put(WorldMap.Constants.NAME, "");
		 * worldMapValues.put(WorldMap.Constants.WIDTH, 512);
		 * worldMapValues.put(WorldMap.Constants.HEIGHT, 512);
		 */
		// insert new row
		/*
		 * db.insertOrThrow(WorldMap.Constants.TABLE, null, worldMapValues);
		 */

		// int parentMapId = 1;
		ArrayList<String[]> cellRawData = CSVUtil.readFromFile(is);
		return cellRawData;
		/*
		 * Iterator<String[]> cellDataIter = cellRawData.iterator();
		 * 
		 * // mapping parameter(s) with value(s) for (int row = 0;
		 * cellDataIter.hasNext(); row++) { String[] cellColumns =
		 * cellDataIter.next(); for (int col = 0; col < cellColumns.length;
		 * col++) { StringBuffer name = new StringBuffer();
		 * name.append(row).append('|').append(col); MapCell cellData = new
		 * MapCell(name.toString(), null, Integer .parseInt(cellColumns[col]),
		 * parentMapId, row, col); mapCellValues = new ContentValues();
		 * mapCellValues.put(MapCell.Constants.DESC, cellData.getDesc());
		 * mapCellValues.put(MapCell.Constants.NAME, cellData.getName());
		 * mapCellValues.put(MapCell.Constants.PARENT_MAP, new Integer(
		 * cellData.getParentMapId()));
		 * mapCellValues.put(MapCell.Constants.MAP_ROW, cellData .getMapRow());
		 * mapCellValues.put(MapCell.Constants.MAP_COLUMN, cellData
		 * .getMapColumn()); mapCellValues.put(MapCell.Constants.IMAGE, new
		 * Integer(cellData .getImage())); // insert new row Log.v(TAG,
		 * "inserting values[" + mapCellValues + "]");
		 * db.insertOrThrow(MapCell.Constants.TABLE, null, mapCellValues); } }
		 */
	}

	private Bitmap[][] findMapTilesByCurrentLocation(int canvasRow,
			int canvasColumn) {
		Bitmap[][] tileImgs = new Bitmap[canvasRow][canvasColumn];
		/*
		int playerRowOnMap = 0;
		int playerColOnMap = 0;
		*/

		for (int row = 0; row < canvasRow; row++) {
			// for (int row = 0; row < lsMapData.size(); row++) {
			String[] rowData = lsMapData.get(row);
			
			Log.i(TAG, "lsMapData.get(" + row + "," + rowData.length
					+ ") - " + rowData);
			
			//if (rowData != null) {
				for (int col = 0; col < canvasColumn; col++) {
					String colData = "";
					Log.i(TAG, "colData = "+colData+"|"+colData.getClass().getCanonicalName());
					// for (int col = 0; col < rowData.length; col++) {
					int left, top, right, bottom, height, width;
					// TODO: query from mapcell
					/*
					 * int imgIndex = findMapCellFromMapByRowColumn(1, row +
					 * playerRowOnMap, col + playerColOnMap);
					 */

					int imgIndex = -1;

					if (col < rowData.length) {
						colData = rowData[col];
						imgIndex = Integer.parseInt(colData);
					}else{
						imgIndex = 1; // default image
					}
					// int imgIndex = 1;
					Log.i(TAG,"image id = "+imgIndex);
					ImageCrop img = null;
					try{
						img = findImageCropFromGalleryById(imgIndex);
					}catch(Exception ex){
						img = new ImageCrop();
						img.setRow(0); 
						img.setColumn(0); 
						img.setWidth(32);
						img.setHeight(32);
					}
					Log.i(TAG,"img = "+img);
					/**
					 * img.setRow(0); img.setColumn(0); img.setWidth(32);
					 * img.setHeight(32);
					 */
					
					height = width = 32;
					left = img.getColumn() * img.getWidth();
					top = img.getRow() * img.getHeight();
					right = bottom = 0;
					right = left + width;
					bottom = top + height;
					// tileImgs[row][col] = Bitmap.createBitmap(rawTileMapImg,
					// left,top, right, bottom);
					tileImgs[row][col] = Bitmap.createBitmap(rawTileMapImg,
							left, top, width, height);
				}
			//}
		}
		return tileImgs;
	}

	@SuppressWarnings("finally")
	public ImageCrop findImageCropFromGalleryById(int image_id) {
		//Cursor cursor = null;
		//StringBuilder sb = null;
		//int[] xy = new int[2];
		ImageCrop img = null;
		// ImageCrop img = new ImageCrop();

		try {
			img = imageMapss.get(image_id);
			// initialization
			/*
			 * if (db == null) db = dbHelper.getReadableDatabase(); sb = new
			 * StringBuilder();
			 * sb.append(ImageCrop.Constants._ID).append(" = ").
			 * append(image_id);
			 * 
			 * Log.i(TAG, sb.toString()); cursor =
			 * db.query(ImageCrop.Constants.TABLE, // table new String[] {
			 * ImageCrop.Constants.ROW, ImageCrop.Constants.COLUMN,
			 * ImageCrop.Constants.WIDTH, ImageCrop.Constants.HEIGHT }, //
			 * selection sb.toString(), // where null, // arguments null, //
			 * group by null, // having null); // order by if (cursor != null &&
			 * cursor.moveToFirst()) { int rowIndex =
			 * cursor.getColumnIndex(ImageCrop.Constants.ROW); int columnIndex =
			 * cursor .getColumnIndex(ImageCrop.Constants.COLUMN); int
			 * widthIndex = cursor.getColumnIndex(ImageCrop.Constants.WIDTH);
			 * int heightIndex =
			 * cursor.getColumnIndex(ImageCrop.Constants.HEIGHT);
			 * 
			 * img.setRow(cursor.getInt(rowIndex));
			 * img.setColumn(cursor.getInt(columnIndex));
			 * img.setWidth(cursor.getInt(widthIndex));
			 * img.setHeight(cursor.getInt(heightIndex)); }
			 */
			// startManagingCursor(cursor);
		} catch (Exception ex) {
			Log.e(TAG,"can't get image",ex);
		} finally {
			/*
			 * if (cursor != null) cursor.close();
			 */
			return img;
		}
	}

	@SuppressWarnings("finally")
	public int findMapCellFromMapByRowColumn(int map_id, int row, int column) {
		Cursor cursor = null;
		StringBuilder sb = null;
		int retImg = -1;

		try {
			// initialization
			/*
			 * if (db == null) db = dbHelper.getReadableDatabase(); sb = new
			 * StringBuilder();
			 * sb.append(MapCell.Constants.MAP_ROW).append(" = ").append(row)
			 * .append(" AND ").append(MapCell.Constants.MAP_COLUMN)
			 * .append(" = ").append(column).append(" AND ").append(
			 * MapCell.Constants.PARENT_MAP).append(" = ").append( map_id);
			 * 
			 * Log.i(TAG, sb.toString()); cursor =
			 * db.query(MapCell.Constants.TABLE, // table new String[] {
			 * MapCell.Constants.IMAGE }, // selection sb.toString(), // where
			 * null, // arguments null, // group by null, // having null); //
			 * order by if (cursor != null && cursor.moveToFirst()) { int
			 * imageIdIndex = cursor .getColumnIndex(MapCell.Constants.IMAGE);
			 * retImg = cursor.getInt(imageIdIndex); } //
			 * startManagingCursor(cursor); if (retImg < 0) retImg = 1;
			 */
		} catch (Exception ex) {
			retImg = 1;
		} finally {
			/*
			 * if (cursor != null) cursor.close();
			 */
			return retImg;
		}
	}

	public boolean doKeyDown(int keyCode, KeyEvent event) {
		boolean handled = false;
		synchronized (holder) {
			if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
				Point currentLocation = avatarHexagon.getCoordinate();
				Point newLocation = new Point(currentLocation.x,
						currentLocation.y - 4);

				avatarHexagon
						.setPath(Graphic2DUtil.getPathOf2Points(
								currentLocation, newLocation, avatarHexagon
										.getSpeed()));
				handled = true;
			} else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
				Point currentLocation = avatarHexagon.getCoordinate();
				Point newLocation = new Point(currentLocation.x,
						currentLocation.y + 4);

				avatarHexagon
						.setPath(Graphic2DUtil.getPathOf2Points(
								currentLocation, newLocation, avatarHexagon
										.getSpeed()));
				handled = true;
			} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
				Point currentLocation = avatarHexagon.getCoordinate();
				Point newLocation = new Point(currentLocation.x - 4,
						currentLocation.y);

				avatarHexagon
						.setPath(Graphic2DUtil.getPathOf2Points(
								currentLocation, newLocation, avatarHexagon
										.getSpeed()));
				handled = true;
			} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
				Point currentLocation = avatarHexagon.getCoordinate();
				Point newLocation = new Point(currentLocation.x + 4,
						currentLocation.y);

				avatarHexagon
						.setPath(Graphic2DUtil.getPathOf2Points(
								currentLocation, newLocation, avatarHexagon
										.getSpeed()));
				handled = true;
			}
		}
		return handled;
	}

	public boolean doKeyUp(int keyCode, KeyEvent event) {
		boolean handled = false;
		synchronized (holder) {
			if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
				// do something
				handled = true;
			}
		}
		return handled;
	}

	// capture user touch gesture input
	public boolean onDown(MotionEvent e) {
		synchronized (holder) {

		}
		return false;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		synchronized (holder) {

		}
		return false;
	}

	public void onLongPress(MotionEvent e) {
		synchronized (holder) {

		}
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		synchronized (holder) {

		}
		return false;
	}

	public void onShowPress(MotionEvent e) {
		synchronized (holder) {

		}
	}

	public boolean onSingleTapUp(MotionEvent e) {
		synchronized (holder) {
			int x, y = 0;
			x = (int) e.getX() - (nextAnchor.getWidth() / 2);
			y = (int) e.getY() - nextAnchor.getHeight();
			// x = (int)e.getX();
			// y = (int)e.getY();

			nextAnchor.invoke(x, y);
			avatarHexagon.setPath(Graphic2DUtil.getPathOf2Points(avatarHexagon
					.getCoordinate(), nextAnchor.getCoordinate(), avatarHexagon
					.getSpeed()));
			// Log.v(HexagonGameView.class.getName(),
			// "touched on (x="+x+", y="+y+")");
		}
		return false;
	}

	public Bundle saveState(Bundle state) {
		synchronized (holder) {

		}
		return state;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public void setSurfaceSize(int width, int height) {
		synchronized (holder) {
			// mCanvasWidth = width;
			// mCanvasHeight = height;

			// don't forget to resize the background image
			// mBackgroundImage =
			// mBackgroundImage.createScaledBitmap(mBackgroundImage, width,
			// height, true);
		}
	}
}
