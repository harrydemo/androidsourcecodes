package five.itcast.cn;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import five.itcast.cn.player.AiFactory;
import five.itcast.cn.player.concrete.HumanPlayer;
import five.itcast.cn.player.interfaces.IPlayer;
//棋盘
public class Chessboard extends View implements IChessboard{

	//游戏状态常量：
    //已准备好，可开局
    private static final int READY = 1;
    //已开局
    private static final int RUNNING = 2;
    //已结束
    private static final int PLAYER_TWO_LOST = 3;
    private static final int PLAYER_ONE_LOST = 4;
    
    //当前状态，默认为可开局状态
    private int currentMode = READY;
    
	//画笔对象
	private final Paint paint = new Paint();
	
	//代表绿色
	private static final int GREEN = 0;
	private static final int NEW_GREEN = 1;
	
	//红色
	private static final int RED = 2;
	//黄色
	private static final int NEW_RED = 3;
	
	//点大小
    private static int pointSize = 20;
	
    //用于提示输赢的文本控件
	private TextView textView = null;
	
	//不同颜色的Bigmap数组
	private Bitmap[] pointArray = new Bitmap[4];
	
	//屏幕右下角的坐标值，即最大坐标值
    private static int maxX;
    private static int maxY;
    
    //第一点偏离左上角从像数，为了棋盘居中
	private static int yOffset;
	private static int xOffset;
	
	//两个玩家
	//第一个玩家默认为人类玩家
	private IPlayer player1 = new HumanPlayer();
	//第二个则根据选择人机战还是对战模式来初始化
	private IPlayer player2;
	//预先初始两个第二玩家
	//电脑玩家
	private static final IPlayer computer = AiFactory.getInstance(2);
	//人类玩家
	private static final IPlayer human = new HumanPlayer();
	
	// 所有未下的空白点
	private final List<Point> allFreePoints = new ArrayList<Point>();
	
    public Chessboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        
        //把三个颜色的点准备好，并放入数组
        Resources r = this.getContext().getResources();
        fillPointArrays(GREEN,r.getDrawable(R.drawable.green_point));
        fillPointArrays(NEW_GREEN,r.getDrawable(R.drawable.new_green_point));
        fillPointArrays(RED,r.getDrawable(R.drawable.red_point));
        fillPointArrays(NEW_RED,r.getDrawable(R.drawable.new_red_point));
        
        //设置画线时用的颜色
        paint.setColor(Color.LTGRAY);
   }
    
    
    //初始横线和竖线的数目
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        maxX = (int) Math.floor(w / pointSize);
        maxY = (int) Math.floor(h / pointSize);

        //设置X、Y座标微调值，目的整个框居中
        xOffset = ((w - (pointSize * maxX)) / 2);
        yOffset = ((h - (pointSize * maxY)) / 2);
        //创建棋盘上的线条
        createLines();
        //初始化棋盘上所有空白点
        createPoints();
    }
    
    //产生棋盘上所有的线
    private void createLines(){
    	for (int i = 0; i <= maxX; i++) {//竖线
    		lines.add(new Line(xOffset+i*pointSize-pointSize/2, yOffset, xOffset+i*pointSize-pointSize/2, yOffset+maxY*pointSize));
		}
    	for (int i = 0; i <= maxY; i++) {//横线
    		lines.add(new Line(xOffset, yOffset+i*pointSize-pointSize/2, xOffset+maxX*pointSize, yOffset+i*pointSize-pointSize/2));
		}
    }
    
    //画棋盘
    private List<Line> lines = new ArrayList<Line>();
    private void drawChssboardLines(Canvas canvas){
    	for (Line line : lines) {
    		canvas.drawLine(line.xStart, line.yStart, line.xStop, line.yStop, paint);
		}
    }
    
    //线类
    class Line{
    	float xStart,yStart,xStop,yStop;
		public Line(float xStart, float yStart, float xStop, float yStop) {
			this.xStart = xStart;
			this.yStart = yStart;
			this.xStop = xStop;
			this.yStop = yStop;
		}
    }
    
    //画点
    private void drawPoint(Canvas canvas,Point p,int color){
    	canvas.drawBitmap(pointArray[color],p.x*pointSize+xOffset,p.y*pointSize+yOffset,paint);
    }
    
    
    

    //设置运行状态
	public void setMode(int newMode) {
		currentMode = newMode;
		if(currentMode==PLAYER_TWO_LOST){
			//提示玩家2输了
			textView.setText(R.string.player_two_lost);
			currentMode = READY;
		}else if(currentMode==RUNNING){
			textView.setText(null);
		}else if(currentMode==READY){
			textView.setText(R.string.mode_ready);
		}else if(currentMode==PLAYER_ONE_LOST){
			//提示玩家1输了
			textView.setText(R.string.player_one_lost);
			currentMode = READY;
		}
	}
	

	//设置提示控件
	public void setTextView(TextView textView) {
		this.textView = textView;
	}

	//监听键盘事件
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent msg) {
        if (currentMode == READY && (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_DPAD_LEFT)) {
        	if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){//向右键，人机对战
        		player2 = computer;
        	}else if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT){//向左键，人--人对战
        		player2 = human;
        	}
        	restart();
        	setMode(RUNNING);
        }else if(currentMode==RUNNING && keyCode == KeyEvent.KEYCODE_DPAD_DOWN){//重新开始
        	restart();
        	setMode(READY);
        }else{
        	return false;
        }
        return true;
	}
	
	//根据触摸点坐标找到对应点
	private Point newPoint(Float x, Float y){
		Point p = new Point(0, 0);
		for (int i = 0; i < maxX; i++) {
			if ((i * pointSize + xOffset) <= x
					&& x < ((i + 1) * pointSize + xOffset)) {
				p.setX(i);
			}
		}
		for (int i = 0; i < maxY; i++) {
			if ((i * pointSize + yOffset) <= y
					&& y < ((i + 1) * pointSize + yOffset)) {
				p.setY(i);
			}
		}
		return p;
	}
	
	//重新开始
	private void restart() {
		createPoints();
		player1.setChessboard(this);
		player2.setChessboard(this);
		setPlayer1Run();
		//刷新一下
		refressCanvas();
	}
	
	//是否已开局
	private boolean hasStart(){
		return currentMode==RUNNING;
	}

	//处理触摸事件
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//还没有开局，或者是按下事件，不处理，只处理开局后的触摸弹起事件
		if(!hasStart() || event.getAction()!=MotionEvent.ACTION_UP){
			return true;
		}
		//是否正在处理一步棋的过程中
		if(onProcessing()){
			return true;
		}
		
		playerRun(event);
		
		return true;
	}
	
	private synchronized void playerRun(MotionEvent event){
		if(isPlayer1Run()){//第一玩家下棋
			player1Run(event);
		}else if(isPlayer2Run()){//第二玩家下棋
			player2Run(event);
		}
	}
	
	
	private void player1Run(MotionEvent event){
		Point point = newPoint(event.getX(), event.getY());
		if(allFreePoints.contains(point)){//此棋是否可下
			setOnProcessing();
			player1.run(player2.getMyPoints(),point);
			//playerOnePoints.add(point);
			//刷新一下棋盘
			refressCanvas();
			//判断第一个玩家是否已经下了
			if(!player1.hasWin()){//我还没有赢
				if(player2==computer){//如果第二玩家是电脑
					//10豪秒后才给玩家2下棋
					refreshHandler.computerRunAfter(10);
				}else{
					setPlayer2Run();
				}
			}else{
				//否则，提示游戏结束
				setMode(PLAYER_TWO_LOST);
			}
		}
	}
	
	private void player2Run(MotionEvent event){
		Point point = newPoint(event.getX(), event.getY());
		if(allFreePoints.contains(point)){//此棋是否可下
			setOnProcessing();
			player2.run(player1.getMyPoints(),point);
//			playerTwoPoints.add(point);
			//刷新一下棋盘
			refressCanvas();
			//判断我是否赢了
			if(!player2.hasWin()){//我还没有赢
				setPlayer1Run();
			}else{
				//否则，提示游戏结束
				setMode(PLAYER_ONE_LOST);
			}
		}
	}
	
	
	private RefreshHandler refreshHandler = new RefreshHandler();
	class RefreshHandler extends Handler {

		//这个方法主要在指定的时刻发一个消息
        public void computerRunAfter(long delayMillis) {
        	this.removeMessages(0);
        	//发消息触发handleMessage函数
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
        
        //收到消息
        @Override
        public void handleMessage(Message msg) {
        	//电脑走一步棋子
    		player2.run(player1.getMyPoints(),null);
    		//刷新一下
    		refressCanvas();
    		if(!player2.hasWin()){
    			//人下
    			setPlayer1Run();
    		}else{//第二个玩家赢了
    			setMode(PLAYER_ONE_LOST);
    		}
        }
    };
	
    //是否正在下某一步棋过程中，主是电脑下棋时需要较长的计算时间，这期间一定不可以再响应触摸事件
	private boolean onProcessing() {
		return whoRun == -1;
	}


	//默认第一个玩家先行
	private int whoRun = 1;
	private void setPlayer1Run(){
		whoRun = 1;
	}
	private void setOnProcessing(){
		whoRun = -1;
	}
	//是否轮到人类玩家下子
	private boolean isPlayer1Run(){
		return whoRun==1;
	}
	
	//是否轮到人类玩家下子
	private boolean isPlayer2Run(){
		return whoRun==2;
	}
	
	private void setPlayer2Run(){
		whoRun = 2;
	}
	
	private void refressCanvas(){
		//触发onDraw函数
        Chessboard.this.invalidate();
	}
	
	private void drawPlayer1Point(Canvas canvas){
		int size = player1.getMyPoints().size()-1;
		if(size<0){
			return ;
		}
		for (int i = 0; i < size; i++) {
			drawPoint(canvas, player1.getMyPoints().get(i), GREEN);
		}
		//最后下的一个点标成黄色
		drawPoint(canvas, player1.getMyPoints().get(size), NEW_GREEN);
	}
	
	private void drawPlayer2Point(Canvas canvas){
		if(player2==null){
			return ;
		}
		int size = player2.getMyPoints().size()-1;
		if(size<0){
			return ;
		}
		for (int i = 0; i < size; i++) {
			drawPoint(canvas, player2.getMyPoints().get(i), RED);
		}
		//最后下的一个点标成黄色
		drawPoint(canvas, player2.getMyPoints().get(size), NEW_RED);
	}
    
	
	//初始化好三种颜色的点
    public void fillPointArrays(int color,Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(pointSize, pointSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, pointSize, pointSize);
        drawable.draw(canvas);
        pointArray[color] = bitmap;
    }
    
    //doRun方法操作的是看不见的内存数据，此方法内容数据以图画的方式表现出来，所以画之前数据一定要先准备好
    @Override
    protected void onDraw(Canvas canvas) {
    	drawChssboardLines(canvas);
    	//画鼠标所在的点
    	drawPlayer1Point(canvas);
    	//画电脑下的棋子
    	drawPlayer2Point(canvas);
    }


	@Override
	public List<Point> getFreePoints() {
		return allFreePoints;
	}
	
	//初始化空白点集合
	private void createPoints(){
		allFreePoints.clear();
		for (int i = 0; i < maxX; i++) {
			for (int j = 0; j < maxY; j++) {
				allFreePoints.add(new Point(i, j));
			}
		}
	}

	@Override
	public int getMaxX() {
		return maxX;
	}

	@Override
	public int getMaxY() {
		return maxY;
	}
    
}
