package whu.iss.sric.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import whu.iss.sric.android.R;
import whu.iss.sric.android.SoundPlay;
import android.content.Context;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class GameView extends BoardView {

	private static final int REFRESH_VIEW = 1;

	public static final int WIN = 1;
	public static final int LOSE = 2;
	public static final int PAUSE = 3;
	public static final int PLAY = 4;
	public static final int QUIT = 5;

	private int Help = 3;
	private int Refresh = 3;
	/**
	 * 第一关为100秒钟的时间
	 */
	private int totalTime = 100;
	private int leftTime;
	
	public static SoundPlay soundPlay;
	public MediaPlayer player;
	
	private RefreshTime refreshTime;
	private RefreshHandler refreshHandler = new RefreshHandler();
	/**
	 * 用来停止计时器的线程
	 */
	private boolean isStop;
	
	private OnTimerListener timerListener = null;
	private OnStateListener stateListener = null;
	private OnToolsChangeListener toolsChangedListener = null;

	private List<Point> path = new ArrayList<Point>();
	
	public GameView(Context context, AttributeSet atts) {
		super(context, atts);
		player = MediaPlayer.create(context, R.raw.back2new); 
		player.setLooping(true);//设置循环播放
	}
	
	public static final int ID_SOUND_CHOOSE = 0;
	public static final int ID_SOUND_DISAPEAR = 1;
	public static final int ID_SOUND_WIN = 4;
	public static final int ID_SOUND_LOSE = 5;
	public static final int ID_SOUND_REFRESH = 6;
	public static final int ID_SOUND_TIP = 7;
	public static final int ID_SOUND_ERROR = 8;
	
	public void startPlay(){
		Help = 3;
		Refresh = 3;
		isStop = false;
		toolsChangedListener.onRefreshChanged(Refresh);
		toolsChangedListener.onTipChanged(Help);
		
		leftTime = totalTime;
		initMap();
		player.start();
		refreshTime = new RefreshTime();
		refreshTime.start();
		GameView.this.invalidate();
	}
	
	public void startNextPlay(){
		//下一关为上一关减去10秒的时间
		totalTime-=10;
		startPlay();
	}
	
	public static void initSound(Context context){
		 soundPlay = new SoundPlay();
	        soundPlay.initSounds(context);
	        soundPlay.loadSfx(context, R.raw.choose, ID_SOUND_CHOOSE);
	        soundPlay.loadSfx(context, R.raw.disappear1, ID_SOUND_DISAPEAR);
	        soundPlay.loadSfx(context, R.raw.win, ID_SOUND_WIN);
	        soundPlay.loadSfx(context, R.raw.lose, ID_SOUND_LOSE);
	        soundPlay.loadSfx(context, R.raw.item1, ID_SOUND_REFRESH);
	        soundPlay.loadSfx(context, R.raw.item2, ID_SOUND_TIP);
	        soundPlay.loadSfx(context, R.raw.alarm, ID_SOUND_ERROR);
	}

	public void setOnTimerListener(OnTimerListener timerListener){
		this.timerListener = timerListener;
	}
	
	public void setOnStateListener(OnStateListener stateListener){
		this.stateListener = stateListener;
	}
	
	public void setOnToolsChangedListener(OnToolsChangeListener toolsChangedListener){
		this.toolsChangedListener = toolsChangedListener;
	}
	
	public void stopTimer(){
		isStop = true;
	}
	
	class RefreshHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == REFRESH_VIEW) {
				GameView.this.invalidate();
				if (win()) {
					setMode(WIN);
					soundPlay.play(ID_SOUND_WIN, 0);
					isStop = true;
				} else if (die()) {
					change();
				}
			}
		}

		public void sleep(int delayTime) {
			this.removeMessages(0);
			Message message = new Message();
			message.what = REFRESH_VIEW;
			sendMessageDelayed(message, delayTime);
		}
	}

	class RefreshTime extends Thread {

		public void run() {
			while (leftTime >= 0 && !isStop) {
				timerListener.onTimer(leftTime);
				leftTime--;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(!isStop){
				setMode(LOSE);
				soundPlay.play(ID_SOUND_LOSE, 0);
			}
			
		}
	}
	
	public int getTotalTime(){
		return totalTime;
	}
	
	public int getTipNum(){
		return Help;
	}
	
	public int getRefreshNum(){
		return Refresh;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		Point p = screenToindex(x, y);
		if (map[p.x][p.y] > 0) {
			if (selected.size() == 1) {
				if (link(selected.get(0), p)) {
					selected.add(p);
					drawLine(path.toArray(new Point[] {}));
					soundPlay.play(ID_SOUND_DISAPEAR, 0);
					refreshHandler.sleep(500);
				} else {
					selected.clear();
					selected.add(p);
					soundPlay.play(ID_SOUND_CHOOSE, 0);
					GameView.this.invalidate();
				}
			} else {
				selected.add(p);
				soundPlay.play(ID_SOUND_CHOOSE, 0);
				GameView.this.invalidate();
			}
		}
		return super.onTouchEvent(event);
	}

	public void initMap() {
		int x = 1;
		int y = 0;
		for (int i = 1; i < xCount - 1; i++) {
			for (int j = 1; j < yCount - 1; j++) {
				map[i][j] = x;
				if (y == 1) {
					x++;
					y = 0;
					if (x == iconCounts) {
						x = 1;
					}
				} else {
					y = 1;
				}
			}
		}
		change();
	}

	private void change() {
		Random random = new Random();
		int tmpV, tmpX, tmpY;
		for (int x = 1; x < xCount - 1; x++) {
			for (int y = 1; y < yCount - 1; y++) {
				tmpX = 1 + random.nextInt(xCount - 2);
				tmpY = 1 + random.nextInt(yCount - 2);
				tmpV = map[x][y];
				map[x][y] = map[tmpX][tmpY];
				map[tmpX][tmpY] = tmpV;
			}
		}
		if (die()) {
			change();
		}
		GameView.this.invalidate();
	}

	public void setMode(int stateMode) {
		this.stateListener.OnStateChanged(stateMode);
	}

	private boolean die() {
		for (int y = 1; y < yCount - 1; y++) {
			for (int x = 1; x < xCount - 1; x++) {
				if (map[x][y] != 0) {
					for (int j = y; j < yCount - 1; j++) {
						if (j == y) {
							for (int i = x + 1; i < xCount - 1; i++) {
								if (map[i][j] == map[x][y]
										&& link(new Point(x, y),
												new Point(i, j))) {
									return false;
								}
							}
						} else {
							for (int i = 1; i < xCount - 1; i++) {
								if (map[i][j] == map[x][y]
										&& link(new Point(x, y),
												new Point(i, j))) {
									return false;
								}
							}
						}
					}
				}
			}
		}
		return true;
	}

	List<Point> p1E = new ArrayList<Point>();
	List<Point> p2E = new ArrayList<Point>();

	private boolean link(Point p1, Point p2) {
		if (p1.equals(p2)) {
			return false;
		}
		path.clear();
		if (map[p1.x][p1.y] == map[p2.x][p2.y]) {
			if (linkD(p1, p2)) {
				path.add(p1);
				path.add(p2);
				return true;
			}

			Point p = new Point(p1.x, p2.y);
			if (map[p.x][p.y] == 0) {
				if (linkD(p1, p) && linkD(p, p2)) {
					path.add(p1);
					path.add(p);
					path.add(p2);
					return true;
				}
			}
			p = new Point(p2.x, p1.y);
			if (map[p.x][p.y] == 0) {
				if (linkD(p1, p) && linkD(p, p2)) {
					path.add(p1);
					path.add(p);
					path.add(p2);
					return true;
				}
			}
			expandX(p1, p1E);
			expandX(p2, p2E);

			for (Point pt1 : p1E) {
				for (Point pt2 : p2E) {
					if (pt1.x == pt2.x) {
						if (linkD(pt1, pt2)) {
							path.add(p1);
							path.add(pt1);
							path.add(pt2);
							path.add(p2);
							return true;
						}
					}
				}
			}

			expandY(p1, p1E);
			expandY(p2, p2E);
			for (Point pt1 : p1E) {
				for (Point pt2 : p2E) {
					if (pt1.y == pt2.y) {
						if (linkD(pt1, pt2)) {
							path.add(p1);
							path.add(pt1);
							path.add(pt2);
							path.add(p2);
							return true;
						}
					}
				}
			}
			return false;
		}
		return false;
	}

	private boolean linkD(Point p1, Point p2) {
		if (p1.x == p2.x) {
			int y1 = Math.min(p1.y, p2.y);
			int y2 = Math.max(p1.y, p2.y);
			boolean flag = true;
			for (int y = y1 + 1; y < y2; y++) {
				if (map[p1.x][y] != 0) {
					flag = false;
					break;
				}
			}
			if (flag) {
				return true;
			}
		}
		if (p1.y == p2.y) {
			int x1 = Math.min(p1.x, p2.x);
			int x2 = Math.max(p1.x, p2.x);
			boolean flag = true;
			for (int x = x1 + 1; x < x2; x++) {
				if (map[x][p1.y] != 0) {
					flag = false;
					break;
				}
			}
			if (flag) {
				return true;
			}
		}
		return false;
	}

	private void expandX(Point p, List<Point> l) {
		l.clear();
		for (int x = p.x + 1; x < xCount; x++) {
			if (map[x][p.y] != 0) {
				break;
			}
			l.add(new Point(x, p.y));
		}
		for (int x = p.x - 1; x >= 0; x--) {
			if (map[x][p.y] != 0) {
				break;
			}
			l.add(new Point(x, p.y));
		}
	}

	private void expandY(Point p, List<Point> l) {
		l.clear();
		for (int y = p.y + 1; y < yCount; y++) {
			if (map[p.x][y] != 0) {
				break;
			}
			l.add(new Point(p.x, y));
		}
		for (int y = p.y - 1; y >= 0; y--) {
			if (map[p.x][y] != 0) {
				break;
			}
			l.add(new Point(p.x, y));
		}
	}

	private boolean win() {
		for (int x = 0; x < xCount; x++) {
			for (int y = 0; y < yCount; y++) {
				if (map[x][y] != 0) {
					return false;
				}
			}
		}
		return true;
	}

	public void autoClear() {
		if (Help == 0) {
			soundPlay.play(ID_SOUND_ERROR, 0);
		}else{
			soundPlay.play(ID_SOUND_TIP, 0);
			Help--;
			toolsChangedListener.onTipChanged(Help);
			drawLine(path.toArray(new Point[] {}));
			refreshHandler.sleep(500);
		}
	}
	
	public void refreshChange(){
		if(Refresh == 0){
			soundPlay.play(ID_SOUND_ERROR, 0);
			return;
		}else{
			soundPlay.play(ID_SOUND_REFRESH, 0);
			Refresh--;
			toolsChangedListener.onRefreshChanged(Refresh);
			change();
		}
	}
}
