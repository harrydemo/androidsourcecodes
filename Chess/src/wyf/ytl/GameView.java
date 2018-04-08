package wyf.ytl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
/**
 * 该类是整个程序最主要的类，是主游戏的界面
 * 该界面继承自SurfaceView并实现了SurfaceHolder.Callback接口
 * 其中包含了一个刷帧的线程类
 * 
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback{
	private TutorialThread thread;//刷帧的线程
	TimeThread timeThread ;
	ChessActivity activity;//声明Activity的引用
	Bitmap qiPan;//棋盘
	Bitmap qizibackground;//棋子的背景图片
	Bitmap win;//胜利的图片
	Bitmap lost;//失败的图片
	Bitmap ok;//确定按钮
	Bitmap vs;//黑方红方VS的图片
	Bitmap right;//向右的指针
	Bitmap left;//向左的指针 
	Bitmap current;//“当前”文字
	Bitmap exit2;//退出按钮图片
	Bitmap sound2;//声音按钮图片
	Bitmap sound3;//当前是否播放了声音
	Bitmap time;//冒号
	Bitmap redtime;//红色冒号
	Bitmap background;//背景图片
	MediaPlayer go;//下棋声音	
	Paint paint;//画笔
	boolean caiPan = true;//是否为玩家走棋
	boolean focus = false;//当前是否有选中的棋子
	int selectqizi = 0; //当然选中的棋子

	int startI, startJ;//记录当前棋子的开始位置
	int endI, endJ;//记录当前棋子的目标位置
	Bitmap[] heiZi = new Bitmap[7];//黑子的图片数组
	Bitmap[] hongZi = new Bitmap[7];//红子的图片数组
	Bitmap[] number = new Bitmap[10];//数字的图片数组，用于显示时间 
	Bitmap[] redNumber = new Bitmap[10];//红色数字的图片，用于显示时间 
	
	GuiZe guiZe;//规则类

	int status = 0;//游戏状态。0游戏中，1胜利, 2失败
	int heiTime = 0;//黑方总共思考时间
	int hongTime = 0;//红方总共思考时间 

	int[][] qizi = new int[][]{//棋盘
		{2,3,6,5,1,5,6,3,2},
		{0,0,0,0,0,0,0,0,0},
		{0,4,0,0,0,0,0,4,0},
		{7,0,7,0,7,0,7,0,7},
		{0,0,0,0,0,0,0,0,0},

		{0,0,0,0,0,0,0,0,0},
		{14,0,14,0,14,0,14,0,14},
		{0,11,0,0,0,0,0,11,0},
		{0,0,0,0,0,0,0,0,0},
		{9,10,13,12,8,12,13,10,9},
	};
	
	public GameView(Context context,ChessActivity activity) {//构造器
		super(context);
		this.activity = activity;//得到Activity的引用
		getHolder().addCallback(this);
		go  = MediaPlayer.create(this.getContext(), R.raw.go);//加载下棋的声音
		this.thread = new TutorialThread(getHolder(), this);//初始化刷帧线程
		this.timeThread = new TimeThread(this);//初始化思考时间的线程
		init();//初始化所需资源
		guiZe = new GuiZe();//初始化规则类
	}
	
	public void init(){//初始化方法 
		paint = new Paint();//初始化画笔
		qiPan = BitmapFactory.decodeResource(getResources(), R.drawable.qipan);//棋盘图片
		qizibackground = BitmapFactory.decodeResource(getResources(), R.drawable.qizi);//棋子的背景
		win = BitmapFactory.decodeResource(getResources(), R.drawable.win);//胜利的图片
		lost = BitmapFactory.decodeResource(getResources(), R.drawable.lost);//失败的图片
		ok = BitmapFactory.decodeResource(getResources(), R.drawable.ok);//确定按钮图片
		vs = BitmapFactory.decodeResource(getResources(), R.drawable.vs);//vs字样的图片
		right = BitmapFactory.decodeResource(getResources(), R.drawable.right);//向右的指针
		left = BitmapFactory.decodeResource(getResources(), R.drawable.left);//向左的指针
		current = BitmapFactory.decodeResource(getResources(), R.drawable.current);//文字“当前”
		exit2 = BitmapFactory.decodeResource(getResources(), R.drawable.exit2);//退出按钮图片
		sound2 = BitmapFactory.decodeResource(getResources(), R.drawable.sound2);//声音按钮图片
		time = BitmapFactory.decodeResource(getResources(), R.drawable.time);//黑色冒号
		redtime = BitmapFactory.decodeResource(getResources(), R.drawable.redtime);//红色冒号
		sound3 = BitmapFactory.decodeResource(getResources(), R.drawable.sound3);
		
		heiZi[0] = BitmapFactory.decodeResource(getResources(), R.drawable.heishuai);//黑帅
		heiZi[1] = BitmapFactory.decodeResource(getResources(), R.drawable.heiju);//黑车
		heiZi[2] = BitmapFactory.decodeResource(getResources(), R.drawable.heima);//黑马
		heiZi[3] = BitmapFactory.decodeResource(getResources(), R.drawable.heipao);//黑炮
		heiZi[4] = BitmapFactory.decodeResource(getResources(), R.drawable.heishi);//黑士
		heiZi[5] = BitmapFactory.decodeResource(getResources(), R.drawable.heixiang);//黑象
		heiZi[6] = BitmapFactory.decodeResource(getResources(), R.drawable.heibing);//黑兵
		
		hongZi[0] = BitmapFactory.decodeResource(getResources(), R.drawable.hongjiang);//红将
		hongZi[1] = BitmapFactory.decodeResource(getResources(), R.drawable.hongju);//红车
		hongZi[2] = BitmapFactory.decodeResource(getResources(), R.drawable.hongma);//红马
		hongZi[3] = BitmapFactory.decodeResource(getResources(), R.drawable.hongpao);//红砲
		hongZi[4] = BitmapFactory.decodeResource(getResources(), R.drawable.hongshi);//红仕
		hongZi[5] = BitmapFactory.decodeResource(getResources(), R.drawable.hongxiang);//红相
		hongZi[6] = BitmapFactory.decodeResource(getResources(), R.drawable.hongzu);//红卒
		
		number[0] = BitmapFactory.decodeResource(getResources(), R.drawable.number0);//黑色数字0
		number[1] = BitmapFactory.decodeResource(getResources(), R.drawable.number1);//黑色数字1
		number[2] = BitmapFactory.decodeResource(getResources(), R.drawable.number2);//黑色数字2
		number[3] = BitmapFactory.decodeResource(getResources(), R.drawable.number3);//黑色数字3
		number[4] = BitmapFactory.decodeResource(getResources(), R.drawable.number4);//黑色数字4
		number[5] = BitmapFactory.decodeResource(getResources(), R.drawable.number5);//黑色数字5
		number[6] = BitmapFactory.decodeResource(getResources(), R.drawable.number6);//黑色数字6
		number[7] = BitmapFactory.decodeResource(getResources(), R.drawable.number7);//黑色数字7
		number[8] = BitmapFactory.decodeResource(getResources(), R.drawable.number8);//黑色数字8
		number[9] = BitmapFactory.decodeResource(getResources(), R.drawable.number9);//黑色数字9
		
		redNumber[0] = BitmapFactory.decodeResource(getResources(), R.drawable.rednumber0);//红色数字0
		redNumber[1] = BitmapFactory.decodeResource(getResources(), R.drawable.rednumber1);//红色数字1
		redNumber[2] = BitmapFactory.decodeResource(getResources(), R.drawable.rednumber2);//红色数字2
		redNumber[3] = BitmapFactory.decodeResource(getResources(), R.drawable.rednumber3);//红色数字3
		redNumber[4] = BitmapFactory.decodeResource(getResources(), R.drawable.rednumber4);//红色数字4
		redNumber[5] = BitmapFactory.decodeResource(getResources(), R.drawable.rednumber5);//红色数字5
		redNumber[6] = BitmapFactory.decodeResource(getResources(), R.drawable.rednumber6);//红色数字6
		redNumber[7] = BitmapFactory.decodeResource(getResources(), R.drawable.rednumber7);//红色数字7
		redNumber[8] = BitmapFactory.decodeResource(getResources(), R.drawable.rednumber8);//红色数字8
		redNumber[9] = BitmapFactory.decodeResource(getResources(), R.drawable.rednumber9);//红色数字9
		
		background = BitmapFactory.decodeResource(getResources(), R.drawable.bacnground);
		
		
	}
	/**
	 * 该方法是自己定义的并非重写的
	 * 该方法是死的，只根据数据绘制屏幕
	 */
	public void onDraw(Canvas canvas){//自己写的绘制方法
		canvas.drawColor(Color.WHITE);
		canvas.drawBitmap(background, 0,0, null);//清背景
		canvas.drawBitmap(qiPan, 10, 10, null);//绘制棋盘	
		for(int i=0; i<qizi.length; i++){
			for(int j=0; j<qizi[i].length; j++){//绘制棋子
				if(qizi[i][j] != 0){
					canvas.drawBitmap(qizibackground, 9+j*34, 10+i*35, null);//绘制棋子的背景					
					if(qizi[i][j] == 1){//为黑帅时
						canvas.drawBitmap(heiZi[0], 12+j*34, 13+i*35, paint);
					}
					else if(qizi[i][j] == 2){//为黑车时
						canvas.drawBitmap(heiZi[1], 12+j*34, 13+i*35, paint);
					}
					else if(qizi[i][j] == 3){//为黑马时
						canvas.drawBitmap(heiZi[2], 12+j*34, 13+i*35, paint);
					}
					else if(qizi[i][j] == 4){//为黑炮时
						canvas.drawBitmap(heiZi[3], 12+j*34, 13+i*35, paint);
					}
					else if(qizi[i][j] == 5){//为黑士时
						canvas.drawBitmap(heiZi[4], 12+j*34, 13+i*35, paint);
					}
					else if(qizi[i][j] == 6){//为黑象时
						canvas.drawBitmap(heiZi[5], 12+j*34, 13+i*35, paint);
					}
					else if(qizi[i][j] == 7){//为黑兵时
						canvas.drawBitmap(heiZi[6], 12+j*34, 13+i*35, paint);
					}
					else if(qizi[i][j] == 8){//为红将时
						canvas.drawBitmap(hongZi[0], 12+j*34, 13+i*35, paint);
					}
					else if(qizi[i][j] == 9){//为红车时
						canvas.drawBitmap(hongZi[1], 12+j*34, 13+i*35, paint);
					}
					else if(qizi[i][j] == 10){//为红马时
						canvas.drawBitmap(hongZi[2], 12+j*34, 13+i*35, paint);
					}
					else if(qizi[i][j] == 11){//为红砲时
						canvas.drawBitmap(hongZi[3], 12+j*34, 13+i*35, paint);
					}
					else if(qizi[i][j] == 12){//为红仕时
						canvas.drawBitmap(hongZi[4], 12+j*34, 13+i*35, paint);
					}
					else if(qizi[i][j] == 13){//为红相时
						canvas.drawBitmap(hongZi[5], 12+j*34, 13+i*35, paint);
					}
					else if(qizi[i][j] == 14){//为红卒时
						canvas.drawBitmap(hongZi[6], 12+j*34, 13+i*35, paint);
					}
				}
			}
		}
		canvas.drawBitmap(vs, 10, 360, paint);//绘制VS背景图
		//绘制黑方的时间
		canvas.drawBitmap(time, 81, 411, paint);//绘制冒号
		int temp = this.heiTime/60;//换算时间
		String timeStr = temp+"";//转换成字符串
		if(timeStr.length()<2){//当不足两位时前面填0
			timeStr = "0" + timeStr;
		}
    	for(int i=0;i<2;i++){//循环绘制时间
    		int tempScore=timeStr.charAt(i)-'0';
    		canvas.drawBitmap(number[tempScore], 65+i*7, 412, paint);
    	}
    	//画分钟
    	temp = this.heiTime%60;
		timeStr = temp+"";//转换成字符串
		if(timeStr.length()<2){   
			timeStr = "0" + timeStr;//当长度小于2时在前面添加一个0
		}
    	for(int i=0;i<2;i++){//循环
    		int tempScore=timeStr.charAt(i)-'0';
    		canvas.drawBitmap(number[tempScore], 85+i*7, 412, paint);//绘制
    	}
    	//开始绘制红方时间
		canvas.drawBitmap(this.redtime, 262, 410, paint);//红方的冒号
		int temp2 = this.hongTime/60;//换算时间
		String timeStr2 = temp2+"";//转换成字符串
		if(timeStr2.length()<2){//当不足两位时前面填0
			timeStr2 = "0" + timeStr2;
		}
    	for(int i=0;i<2;i++){//循环绘制时间
    		int tempScore=timeStr2.charAt(i)-'0';
    		canvas.drawBitmap(redNumber[tempScore], 247+i*7, 411, paint);//绘制
    	}
    	//画分钟
    	temp2 = this.hongTime%60;//求出当前的秒数
		timeStr2 = temp2+"";//转换成字符串
		if(timeStr2.length()<2){//不足两位时前面用0补
			timeStr2 = "0" + timeStr2;
		}
    	for(int i=0;i<2;i++){//循环绘制
    		int tempScore=timeStr2.charAt(i)-'0';
    		canvas.drawBitmap(redNumber[tempScore], 267+i*7, 411, paint);//绘制时间数字
    	}
		if(caiPan == true){//当该玩家走棋时,即红方走棋
			canvas.drawBitmap(right, 155, 420, paint);//绘制向右的指针
		}
		else{//黑方走棋，即电脑走棋时
			canvas.drawBitmap(left, 120, 420, paint);//绘制向左的指针
		}
		
		canvas.drawBitmap(current, 138, 445, paint);//绘制当前文字
		canvas.drawBitmap(sound2, 10, 440, paint);//绘制声音
		if(activity.isSound){//如果正在播放声音
			canvas.drawBitmap(sound3, 80, 452, paint);//绘制
		}
		
		canvas.drawBitmap(exit2, 250, 440, paint);//绘制退出按钮
		if(status == 1){//当胜利时
			canvas.drawBitmap(win, 85, 150, paint);//绘制胜利图片
			canvas.drawBitmap(ok, 113, 240, paint);
		}
		if(status == 2){//失败后
			canvas.drawBitmap(lost, 85, 150, paint);//绘制失败界面
			canvas.drawBitmap(ok, 113, 236, paint);	
		}
	}
	/**
	 * 该方法是游戏主要逻辑接口
	 * 接受玩家输入
	 * 根据点击的位置和当前的游戏状态做出相应的处理
	 * 而当需要切换View时，通过给Activity发送Handler消息来处理
	 * 注意的是只取屏幕被按下的事件
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {//重写的屏幕监听
		if(event.getAction() == MotionEvent.ACTION_DOWN){//只取鼠标按下的事件
			if(event.getX()>10&&event.getX()<10+sound2.getWidth()
					&& event.getY()>440 && event.getY()<440+sound2.getHeight()){//按下了声音按钮
				activity.isSound = !activity.isSound;//声音取反
				if(activity.isSound){//当需要放声音时
					if(activity.gamesound != null){//gamesound不为空时
						if(!activity.gamesound.isPlaying()){//当前没有音乐时
			    			activity.gamesound.start();//播放音乐
			    		}
					}
				}
				else{
					if(activity.gamesound != null){//gamesound不为空时
						if(activity.gamesound.isPlaying()){//当前有音乐时
							activity.gamesound.pause();//停止音乐
						}
					} 
				}
			}//end 按下了声音按钮 
			if(event.getX()>250&&event.getX()<250+exit2.getWidth()
					&& event.getY()>440 && event.getY()<440+exit2.getHeight()){//按下了退出按钮
				activity.myHandler.sendEmptyMessage(1);//发送消息，切换到MenuView
			}
			if(status == 1){//胜利后 
				if(event.getX()>135&&event.getX()<190
						&& event.getY()>249 && event.getY()<269){//点击了确定按钮
					activity.myHandler.sendEmptyMessage(1);//发送消息，切换到MenuView
				}
			}
			else if(status == 2){//失败后
				if(event.getX()>135&&event.getX()<190
						&& event.getY()>245 && event.getY()<265){//点击了确定按钮
					activity.myHandler.sendEmptyMessage(1);//发送消息，切换到MenuView
				}
			}
			/**
			 * 游戏过程中的逻辑处理
			 * 当点击棋盘时，先判断当前是否为玩家走棋，
			 * 然后再判断当然玩家是否已经有选中的棋子,如果没有则选中
			 * 如果之前有选中的棋子，再判断点击的位置是空地、对方棋子还是自己的棋子
			 * 是空地判断是否可走
			 * 是对方棋子同样判断是否可以走，能走自然吃子
			 * 是自己的棋子则选中该棋子
			 */
			else if(status == 0){//游戏中时 
				if(event.getX()>10&&event.getX()<310
						&& event.getY()>10 && event.getY()<360){//点击的位置在棋盘内时
						if(caiPan == true){//如果是该玩家走棋
							int i = -1, j = -1;
							int[] pos = getPos(event);//根据坐标换算成所在的行和列
							i = pos[0];
							j = pos[1];
							if(focus == false){//之前没有选中的棋子
								if(qizi[i][j] != 0){//点击的位置有棋子
									if(qizi[i][j] > 7){//点击的是自己的棋子。即下面的黑色棋子
										selectqizi = qizi[i][j];//将该棋子设为选中的棋子
										focus = true;//标记当前有选中的棋子
										startI = i;
										startJ = j;
									}
								}
							}
							else{//之前选中过棋子
								if(qizi[i][j] != 0){//点击的位置有棋子
									if(qizi[i][j] > 7){//如果是自己的棋子.
										selectqizi = qizi[i][j];//将该棋子设为选中的棋子
										startI = i;
										startJ = j;
									}
									else{//如果是对方的棋子
										endI = i;
										endJ = j;//保存该点
										boolean canMove = guiZe.canMove(qizi, startI, startJ, endI, endJ);
										if(canMove){//如果可以移动过去
											caiPan = false;//不让玩家走了
											if(qizi[endI][endJ] == 1 || qizi[endI][endJ] == 8){//如果是“帅”或“将”
												this.success();//胜利了 
											}
											else{
												if(activity.isSound){
													go.start();//播放下棋声音
												}
												qizi[endI][endJ] = qizi[startI][startJ];//移动棋子
												qizi[startI][startJ] = 0;//将原来处设空
												startI = -1;
												startJ = -1;
												endI = -1;
												endJ = -1;//还原保存点
												focus = false;//标记当前没有选中棋子
												
												ChessMove cm = guiZe.searchAGoodMove(qizi);//根据当前局势查询一个最好的走法
												if(activity.isSound){
													go.start();//播放下棋声音
												}
												qizi[cm.toX][cm.toY] = qizi[cm.fromX][cm.fromY];//移动棋子
												qizi[cm.fromX][cm.fromY] = 0;
												caiPan = true;//恢复玩家响应
											}
										}
									}
								}//end点击的位置有棋子
								else{//如果点击的位置没有棋子
									endI = i;
									endJ = j;							
									boolean canMove = guiZe.canMove(qizi, startI, startJ, endI, endJ);//查看是否可走
									if(canMove){//如果可以移动
										caiPan = false;//不让玩家走了
										if(activity.isSound){
											go.start();//播放下棋声音
										}
										qizi[endI][endJ] = qizi[startI][startJ];//移动棋子
										qizi[startI][startJ] = 0;//将原来处置空
										startI = -1;
										startJ = -1;
										endI = -1;
										endJ = -1;//还原保存点
										focus = false;//标志位设false

										ChessMove cm = guiZe.searchAGoodMove(qizi);//得到一步走法 
										if(qizi[cm.toX][cm.toY] == 8){//电脑吃了您的将
											status = 2;//切换游戏状态为失败
										}
										if(activity.isSound){//需要播放声音时
											go.start();//播放下棋声音
										}
										qizi[cm.toX][cm.toY] = qizi[cm.fromX][cm.fromY];//移动棋子
										qizi[cm.fromX][cm.fromY] = 0;
										caiPan = true;//恢复玩家响应
									}
								}
							}//end 之前选中过棋子
						}
					}//end点击的位置在棋盘内时
			}//end游戏中时
		}
		return super.onTouchEvent(event);
	}
	
	public int[] getPos(MotionEvent e){//将坐标换算成数组的维数
		int[] pos = new int[2];
		double x = e.getX();//得到点击位置的x坐标
		double y = e.getY();//得到点击位置的y坐标
		if(x>10 && y>10 && x<10+qiPan.getWidth() && y<10+qiPan.getHeight()){//点击的是棋盘时
			pos[0] = Math.round((float)((y-21)/36));//取得所在的行
			pos[1] = Math.round((float)((x-21)/35));//取得所在的列
		}
		else{//点击的位置不是棋盘时
			pos[0] = -1;//将位置设为不可用
			pos[1] = -1;
		}
		return pos;//将坐标数组返回
	}
	
	public void success(){//胜利了
		status = 1;//切换到胜利状态
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	public void surfaceCreated(SurfaceHolder holder) {//重写的
        this.thread.setFlag(true);
        this.thread.start();//启动刷帧线程
        timeThread.setFlag(true);
        timeThread.start();//启动思考时间的线程
	}

	public void surfaceDestroyed(SurfaceHolder holder) {//view被释放时调用的
        boolean retry = true;
        thread.setFlag(false);//停止刷帧线程
        timeThread.setFlag(false);//停止思考时间线程
        while (retry) {
            try {
                thread.join();
                timeThread.join();//等待线程结束
                retry = false;//设置循环标志位为false
            } 
            catch (InterruptedException e) {//不断地循环，直到等待的线程结束
            }
        }
	}
	class TutorialThread extends Thread{//刷帧线程
		private int span = 300;//睡眠的毫秒数 
		private SurfaceHolder surfaceHolder;//SurfaceHolder的引用
		private GameView gameView;//gameView的引用
		private boolean flag = false;//循环标志位
        public TutorialThread(SurfaceHolder surfaceHolder, GameView gameView) {//构造器
            this.surfaceHolder = surfaceHolder;//得到SurfaceHolder引用
            this.gameView = gameView;//得到GameView的引用
        }
        public void setFlag(boolean flag) {//设置循环标记
        	this.flag = flag;
        }
		public void run() {//重写的方法
			Canvas c;//画布
            while (this.flag) {//循环绘制
                c = null;
                try {
                    c = this.surfaceHolder.lockCanvas(null);
                    synchronized (this.surfaceHolder) {
                    	gameView.onDraw(c);//调用绘制方法
                    }
                } finally {//用finally保证下面代码一定被执行
                    if (c != null) {
                    	//更新屏幕显示内容
                        this.surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
                try{
                	Thread.sleep(span);//睡眠span毫秒
                }catch(Exception e){//不会异常信息
                	e.printStackTrace();//打印异常堆栈信息
                }
            }
		}
	}
}