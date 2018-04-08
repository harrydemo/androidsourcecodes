package wyf.wpf;					//���������
import static wyf.wpf.ConstantUtil.*;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Looper;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class GameView extends SurfaceView implements SurfaceHolder.Callback{
	RunActivity father;		//Activity��������
	DrawThread dt;			//DrawThread��������
	KeyThread kt;			//KeyThread��������
	Hero hero;				//Ӣ�۶�������
	int gameStatus = -1;		//��Ϸ״̬��0���أ�1ͨȫ�أ�2ʧ�ܣ�3��Ϸ���ڽ���
	int currStage = 0;		//��ǰ�ؿ�����0����,��0�����1��
	LayerList layerList;		//��ŵ�ǰ�ؿ��ĵ�ͼ����
	int [][] currNotIn;		//��ŵ�ǰ�ؿ��Ĳ���ͨ������
	int [] heroLocation;	//��ų�ʼ��Ӣ�۵�λ�ã�����ǰ���ں�
	int [] homeLocation;		//��ŵ�ǰ�ؿ��ҵ�λ��
	int startRow = 0;			//��Ļ���Ͻ��ڴ��ͼ�е�����
	int startCol = 0;			//��Ļ���Ͻ��ڴ��ͼ�е�����
	int offsetX = 0;			//��Ļ���Ͻ������startCol��ƫ����
	int offsetY = 0;			//��Ļ���Ͻ������startRow��ƫ����
	Rect rectAlert = new Rect(0,160,320,310);		//��Ϸ��ʾ�ľ��ο�
	
	public GameView(RunActivity context) {
		super(context);
		this.father = context;
		initStageData();					//��ʼ���ؿ�����
		getHolder().addCallback(this);				//���Callback�ӿ�
		dt = new DrawThread(this, getHolder());		//����DrawThreaed����
		kt = new KeyThread(this);					//����KeyThread����
	
		
		setGameStatus(STATUS_PLAYING);
	}
	//��������ʼ���ؿ���Ϣ
	public void initStageData(){
		BitmapManager.loadCurrentStage(getResources(), currStage);
		layerList = LayerList.getLayerListByStage(currStage);
		currNotIn = layerList.getTotalNotInMatrix();
		heroLocation = GameData.getHeroLocationByStage(currStage);
		homeLocation = GameData.getHomeLocationByStage(currStage);
		hero = new Hero(heroLocation[0], heroLocation[1]);	//����Ӣ�۶���
		hero.makeAnimation(BitmapManager.getHeroFrmList());	//ΪӢ�۴���������
		father.keyState = 0;			//��ռ���״̬
		startRow = 0;					//��startRow����
		startCol = 0;					//��startCol����
		offsetX = 0;					//��offsetX����
		offsetY = 0;					//��offsetY����
	}	
	//��Ļ���Ʒ���
	public void doDraw(Canvas canvas){
		int heroX = hero.x;				//��ȡӢ��X����
		int heroY = hero.y;				//��ȡӢ��Y����
		int hRow = (heroY+SPRITE_HEIGHT-1) / TILE_SIZE ;//���Ӣ�����½��ڴ��ͼ�ϵ��к���
		int hCol = (heroX+SPRITE_WIDTH-1) / TILE_SIZE;//���Ӣ�����½��ڴ��ͼ�ϵ�����
		int tempStartRow = this.startRow;	//��ȡ������ʼ��
		int tempStartCol = this.startCol;	//��ȡ������ʼ��
		int tempOffsetX = this.offsetX;		//��ȡ�����tempStartRow��ƫ����
		int tempOffsetY = this.offsetY;		//��ȡ�����tempStartCol��ƫ����
		canvas.drawColor(Color.BLACK);		//����Ļ
		for(int i=-1; i<=SCREEN_ROWS; i++){     
			if(tempStartRow+i < 0 || tempStartRow+i>=MAP_ROWS){//����໭����һ�в����ڣ��ͼ���
				continue;		//������һ��ѭ��
			}
			for(int j=-1; j<=SCREEN_COLS; j++){
				if(tempStartCol+j <0 || tempStartCol+j>=MAP_COLS){//����໭����һ�в����ڣ��ͼ���
					continue;		//������һ��ѭ��
				}
				for(Layer l:layerList.layerList){
					if(l.mapMatrix[tempStartRow+i][tempStartCol+j] != null){
						l.mapMatrix[tempStartRow+i][tempStartCol+j].drawSelf(canvas, i, j, tempOffsetX, tempOffsetY);
					}
				}
				//����Ƿ���Ҫ���Ƽ�
				if(homeLocation[0]-tempStartCol==j && homeLocation[1]-tempStartRow==i){	//���������
					int homeX = j*TILE_SIZE - tempOffsetX;
					int homeY = i*TILE_SIZE - tempOffsetY;
					BitmapManager.drawGamePublic(36, canvas, homeX, homeY);
				}
				//����Ƿ���Ҫ����Ӣ��
				if(hRow - tempStartRow == i && hCol-tempStartCol == j){		//Ӣ�۵����½ǵ�λ�ڴ˵�ͼ��
					int screenX = heroX - tempStartCol*TILE_SIZE - tempOffsetX;
					int screenY = heroY - tempStartRow*TILE_SIZE - tempOffsetY;
					hero.drawSelf(canvas, screenX, screenY);
				}
				}
		}
		drawGameStatus(canvas);		//����Ƿ���Ҫ������Ϸ��ʾ
		
	}
	
		
	//�����������û������Ļ�¼�
	public boolean myTouchEvent(int x,int y){
		if(rectAlert.contains(x, y)){		//���������Ϸ��ʾ
			switch(gameStatus){
			case STATUS_WIN:		//ͨȫ��
			case STATUS_LOSE:		//��Ϸʧ��
			case STATUS_PASS:		//ͨ��һ��	
				stopGame();
		        father.pv = new ProgressView(father, 3);					//����һ��ProgressView����Ŀ��Ϊ3����������ȥWelcomeView
		        father.setContentView(father.pv);
		        father.currentView = father.pv;
		    	new Thread(){
		    		public void run(){
		    			Looper.prepare();
		    			BitmapManager.loadWelcomePublic(getResources());	//���ػ�ӭ�����ͼƬ��Դ
		    		
		    			father.wv = new WelcomeView(father);//��ʼ��WelcomeView
		    			
		    			
		    			father.pv.progress=100;
		    		}
		    	}.start();
				father.gv = null;
				break;
			
			}
		}
		return true;
	}
	//��������ʼ��Ϸ
	public void startGame(){
		kt.isGameOn = true;	//�ָ���Ϸ����
		if(!kt.isAlive()){
			kt.start();		//���������߳�
		}
		hero.startAnimation();	//����Ӣ�ۻ�֡�߳�
		
		}
	//��������ͣ��Ϸ
	public void pauseGame(){
		kt.isGameOn = false;		//��ͣ�����߳�
		hero.pauseAnimation();		//��ͣӢ�۶���
	
	}
	//������������Ϸ
	public void stopGame(){
		kt.flag = false;
		kt.isGameOn = false;
		
		hero.stopAnimation();
	}
	//������������Ϸ״̬���Ʋ�ͬ����ʾ
	public void drawGameStatus(Canvas canvas){
		switch(gameStatus){
		//case STATUS_LOSE:		//��Ϸʧ��
			//BitmapManager.drawGamePublic(2, canvas, 0, 160);
		//break;
		case STATUS_PASS:		//ͨ��һ��
			BitmapManager.drawGamePublic(0, canvas, 0, 160);
			break;
		//case STATUS_WIN:		//ͨȫ��
			//BitmapManager.drawGamePublic(1, canvas, gameAlertX, gameAlertY);
		}
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {//��д�ӿڷ���
		
	}

	public void surfaceCreated(SurfaceHolder holder) {//��дsurfaceCreated����
		dt.isViewOn = true;//�ָ���Ϸ����
		if(! dt.isAlive()){//���û����������
			dt.start();		//����ˢ���߳�
		}
		startGame();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {//��дsurfaceDestroyed����
		dt.flag = false;
		dt.isViewOn = false;
	}
	public int getGameStatus() {
		return gameStatus;
	}
	public void setGameStatus(int gameStatus) {
		this.gameStatus = gameStatus;
	}
}