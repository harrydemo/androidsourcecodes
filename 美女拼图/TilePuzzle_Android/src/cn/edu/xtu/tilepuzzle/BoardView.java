package cn.edu.xtu.tilepuzzle;


import android.R;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
/**
 * ��Ϸ����
 * */
public class BoardView extends View{
    Canvas canvas;
    ClassBoardModel classBoardModel;
    /**
     * �ڷ���������ַ��ĸ߶�
     * */
    int strHeight=0;
    
    // cell geometry in pixels
    /**
     * ����Ŀ�
     * */
    public int cellWidth;
    /**
     * ����ĸ�
     * */
    public int cellHeight; 
    /**
     * ���з���Ŀ�
     * */
    public int gridWidth;
    /**
     * ���з���ĸ�
     * */
    public int gridHeight;
    Paint paint;
    /**
     * ��ǰ��ͼ����һ����ͼ
     * */
    private View preView;
    
    private GamePlayActivity gamePlayActivity;
    public View getPreView() {
		return preView;
	}

	public void setPreView(View preView) {
		this.preView = preView;
	}

	public BoardView(GamePlayActivity gamePlayActivity,ClassBoardModel classBoardModel) {
		super(gamePlayActivity);		
		this.gamePlayActivity=gamePlayActivity;
        this.classBoardModel=classBoardModel;
        init();        
    }
    
    public void init(){
    	paint=new Paint();
      //  this.setFullScreenMode(true);
        this.gridWidth = getWidth();
        this.gridHeight = getHeight();
        //System.out.println(gridWidth+","+gridHeight);
        Rect rect = new Rect();  
        ClassPaint.paintAddStr.getTextBounds("��Ҫ��", 0, 1, rect);
        strHeight=rect.height();
    }
    public void setBoardModel(ClassBoardModel classBoardModel){
        this.classBoardModel=classBoardModel;
    }
    
    private void concreteUpdateUI(){
    	for (int j = 0; j <classBoardModel.columns ; j++) {
            for (int k = 0; k < classBoardModel.rows; k++) {
                classBoardModel.grid[j][k].paint(canvas,strHeight);
            }
        }
    	
    };
    @Override
    protected void onDraw(Canvas canvas) {
    	this.canvas=canvas;
        
    	//paint.setColor(0xffffff);       
    	
    	//���û�����ɫ    	
    	paint.setColor(Color.argb(180,255, 255, 190));//cn.edu.xtu.tilepuzzle.R.color.background);
    	//����һ���ֻ�ȫ��Ļ�����ɫ�ľ���
    	canvas.drawRect(0,0,getWidth(),getHeight(),paint);
        //  g.translate(gridWidth, gridHeight);
        //canvas.drawRect(0, 0, boardModel.rows * cellWidth, boardModel.columns * cellHeight,paint);
        
        //       grid[2][2].paint(g);
        
        for (int j = 0; j <classBoardModel.columns ; j++) {
            for (int k = 0; k < classBoardModel.rows; k++) {
                classBoardModel.grid[j][k].paint(canvas,strHeight);
            }
        }
        
        if (classBoardModel.gameState == ClassGameDB.WON) {
             //boardModel.all[(boardModel.rows * boardModel.columns) - 1].paint(g);
            //System.out.println("paint ��ʾ��������������");
            // g.translate(-g.getTranslateX(), -g.getTranslateY());
        }
        final Handler handler = new Handler();   
		 new Thread(new Runnable() {     
         @Override    
         public void run() {     
             // delay some minutes you desire.     
             try {   
                 Thread.sleep(300);   
             } catch (InterruptedException e) {   
             }    
             handler.post(new Runnable() {     
                 public void run() {     
                 	concreteUpdateUI();     
                 	invalidate();     
                 }     
             });     
         }     
     }).start(); 
        
    }
    
    /**
     * tempx=xUp-xDown
     * (tempx > 0)// �տ�����
     * (tempx < 0)// �տ�����
     * */
    float tempx=0;
    /**
     * tempy=yUp-yDown
     * (tempy > 0)// �տ�����
     * (tempy < 0)// �տ�����
     * */    
    float tempy=0;
    /**
     * flagX=(xUp-xDown)>0?1:-1;
     *  �տ��� X �᷽���ƶ� (flagX>0 ����, flagX<0 ����)
     *  ��Ӧ���еĸı�
     * */
    int flagX=0;
    /**
     * flagY=(yUp-yDown)>0?1:-1;
     * �տ��� Y �᷽���ƶ� (flagY>0 ����, flagY<0 ����)
     * ��Ӧ���еĸı�
     * */
    int flagY=0;
    /**
     * flagXY=Math.abs(tempx)-Math.abs(tempy);
     * */
    float flagXY=0;
    private void moveBlank(){
    	if (classBoardModel.gameState != ClassGameDB.PLAYING) {
            return;
        }    
    	flagX=0;
    	flagY=0;
    	/*******************************************************************************/
    	/*1�� ��ָ���һ�����flagXY > 0��(xUp - xDown) > 0
    	 * �տ������ƶ���flagX = - 1
    	 * 2�� ��ָ���󻮶���flagXY > 0��(xUp - xDown) < 0
    	 * �տ������ƶ���flagX = 1
    	 * 3�� ��ָ���ϻ�����flagXY < 0��(yUp - yDown) < 0 
    	 * �տ������ƶ���flagY =  1
    	 * 4�� ��ָ���»�����flagXY < 0��(yUp - yDown) > 0 
    	 * �տ������ƶ���flagY =  -1
    	 * */
    	flagXY = Math.abs(xUp - xDown) - Math.abs(yUp - yDown);
		if (flagXY > 0) // �� X �����ƶ� ��Ӧ���еĸı�
			flagX = (xUp - xDown) > 0 ? -1 : 1;
		else if (flagXY < 0) // �� Y �����ƶ�
			flagY = (yUp - yDown) > 0 ? -1 : 1;
		else
			;
		Log.d("XY", "flagXY:"+flagXY+";flagX:"+flagX+";flagY:"+flagY);
		/*******************************************************************************/
        		
        int swapx = classBoardModel.blankp.x; //������
        int swapy = classBoardModel.blankp.y; //������
        
       // int direction = -1;//(boardModel.mainUIController.gameSetUI.reversed ? (-1) : 1);

		swapx += flagY;
		swapy += flagX;
        System.out.println("======="+(classBoardModel.blankp.x+1)+","+(classBoardModel.blankp.y+1)+","+(swapx+1)+","+(swapy+1));
        if ((swapx < 0) || (swapx >= classBoardModel.columns) || (swapy < 0) || (swapy >= classBoardModel.rows)) {
            return;
        }
        classBoardModel.moveBlank(swapx, swapy);
        
        if (classBoardModel.isSolved()) {
        	classBoardModel.sumTime+=System.currentTimeMillis()-classBoardModel.starTime;
        	classBoardModel.starTime=0;
            classBoardModel.setGameState(ClassGameDB.WON);
            gamePlayActivity.showWON(classBoardModel.sumTime);
        }else{//�ػ�����
        	classBoardModel.repaintBlank(this.canvas,swapx,swapy,strHeight);
        }
    }
    private float xDown;
    private float yDown;
    private float xUp;
    private float yUp;
    @Override
    public boolean onTouchEvent(MotionEvent event){
    	int action=event.getAction();
    	switch(action){
    		case MotionEvent.ACTION_DOWN:
    			xDown=event.getX();
    			yDown=event.getY();			
    			Log.d("XY","DOWN(xdown:ydown) = "+xDown+":"+yDown);
    			break;
    		case MotionEvent.ACTION_UP:
    			xUp=event.getX();
    			yUp=event.getY();
    			Log.d("XY","UP(xup:yup) = "+xUp+":"+yUp);
    			moveBlank();
    			break;
    		/*case MotionEvent.ACTION_MOVE:    			
    			break;
    		case MotionEvent.EDGE_BOTTOM:
    			break;*/
    		default:
    			break;
    	}
    	return true;
    }
    /*
    public void keyPressed(int code) {
        //System.out.println("��Ϸ���水����"+code);
        //�ص�ԭ���Ľ���ʱ��Ҫ��ͣ��ʱ
        if(code==-7 || code==7){
        	//��ͣ��ʱ
        	boardModel.sumTime+=System.currentTimeMillis()-boardModel.starTime;
        	boardModel.starTime=0;
        	
            dpy.setCurrent(mainUIController.mainMenuUI);
            mainUIController.mainMenuUI.repaint();
            //System.out.println("�ص�ԭ���Ľ���");
            return;
        }
        
        if (boardModel.gameState != GameDB.PLAYING) {
            return;
        }
        int game = getGameAction(code);
        
        int swapx = boardModel.blankp.x; //������
        int swapy = boardModel.blankp.y; //������
        
        int direction = (boardModel.mainUIController.gameSetUI.reversed ? (-1) : 1);
        
        switch (game) {
            case Canvas.UP:
                swapx += direction;
                
                break;
                
            case Canvas.DOWN:
                swapx -= direction;
                
                break;
                
            case Canvas.LEFT:
                swapy += direction;
                
                break;
                
            case Canvas.RIGHT:
                swapy -= direction;
                
                break;
                
            default:
                return;
        }
        //System.out.println("======="+(boardModel.blankp.x+1)+","+(boardModel.blankp.y+1)+","+(swapx+1)+","+(swapy+1));
        if ((swapx < 0) || (swapx >= boardModel.columns) || (swapy < 0) || (swapy >= boardModel.rows)) {
            return;
        }
        
        boardModel.moveBlank(swapx, swapy);
        repaint();
        
        if (boardModel.isSolved()) {
        	boardModel.sumTime+=System.currentTimeMillis()-boardModel.starTime;
        	boardModel.starTime=0;
            boardModel.setGameState(GameDB.WON);
        }
    }
    */
    
}
