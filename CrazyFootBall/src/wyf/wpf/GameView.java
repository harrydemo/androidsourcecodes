package wyf.wpf;			//���������
import java.util.ArrayList;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
/*
 * ��������Ϸ������ͼ�࣬������Ϸ����Ļ��ƣ��˵��Ļ��ƻ��Ƶȵ�
 * ��Ա������Ϊ���Ʒ�����ͬʱҲ���޸���Ϸ���ݵķ�������ؿ����ݵȡ�Ϊʵ��
 * SurfaceHolder.Callback�ӿ���д��һЩ����
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback{
	Ball ball;			//С�����
	int fieldLeft=27;		//�򳡵���߽�����Ļ�е�λ��	
	int fieldRight=293;		//�򳡵��ұ߽�����Ļ�е�λ��
	int fieldUp = 68;		//�򳡵��ϱ߽�����Ļ�е�λ��
	int fieldDown = 470;	//�򳡵��±߽�����Ļ�е�λ��
	int barDistance = 50;		//����ҡ�˵ļ��
	int playerSize = 18;		//�˶�ԱͼƬ�Ĵ�С
	int maxLeftPosition = 115;		//����Ա�������ߵ��ĵط�=
	int maxRightPosition = 205;		//����Ա�������ߵ��ĵط�=
	int aiDirection = -1;			//��¼AI���˶�����4Ϊ��12Ϊ��=
	int level=1;				//��Ϸ���Ѷ�
	int MaxLevel = 3;		//��ߵȼ�
	int [] scores = {0,0};	//�ȷ֣�ǰ�Һ��
	int scoreLeft = 80;		//������߱ȷ�ʱ�Ĳο�X����
	int scoreRight = 240;	//�����ұ߱ȷ�ʱ�Ĳο�X����
	int timeCounter=0;		//��ʱ����������ʾ
	int winPoint = 8;			//��ʤ�㣬˭�Ľ������ȴﵽ��ֵ��ʤ
	int AIGoalLeft;				//AI�������=
	int AIGoalRight;			//AI�����ұ�=
	int myGoalLeft;				//��ҵ��������=
	int myGoalRight;			//��ҵ������ұ�=
	
	boolean isScoredAGoal;	//������������ʱ����Ϊtrue
	boolean isGameOver;		//һ�ֱ�������
	boolean isGameWin;		//�Ƿ�Ӯ��
	boolean isShowDialog;	//�Ƿ���ʾ�Ի���
	boolean isGamePassAll;	//�Ƿ��Ӯ�����еȼ��ı���

	ArrayList<Bonus> balLive = new ArrayList<Bonus>();//���������ײ��⣬����ȫ�ǻ̬��Bonus=
	ArrayList<Bonus> balForDraw = new ArrayList<Bonus>();//������ڻ��ƣ�������л̬��Ҳ����Ч̬��Bonus=
	ArrayList<Bonus> balAdd = new ArrayList<Bonus>();			//��Ŵ����Bonus�������ʱ����=
	ArrayList<Bonus> balDelete = new ArrayList<Bonus>();		//��Ŵ�ɾ��Bonus�������ʱ����=
	BonusManager bm;											//BonusManager��������=
	
	FootballActivity father;		//FootballActivity��������
	DrawThread dt;				//��̨ˢ���߳�
	AIThread ait;				//����AI�ƶ����߳�	
	ArrayList<Player> alMyPlayer = new ArrayList<Player>(11);	//��ҵ��˶�Ա����
	ArrayList<Player> alAIPlayer = new ArrayList<Player>(11);	//AI ���˶�Ա����

	Bitmap bmpMyPlayer;		//��ҵ���ԱͼƬ	
	Bitmap bmpAIPlayer;		//AI����ԱͼƬ
	Bitmap bmpGoalBanner;	//�������ʾ��ͼƬ
	Bitmap [] bmpScores;	//��ʾ�ȷֵ�����ͼƬ
	Bitmap bmpWinBanner;	//��Ϸʤ����ͼƬ
	Bitmap bmpWinText;		//��Ϸʤ��������
	Bitmap bmpLoseBanner;	//��Ϸʧ�ܵ�ͼƬ
	Bitmap bmpLoseText;		//��Ϸʧ�ܵ�����
	Bitmap bmpPassAll;		//��Ӯ���б�����ʾ��ͼƬ
	Bitmap bmpMyClub;		//��¼�����ѡ�ľ��ֲ�
	Bitmap bmpAIClub;		//��¼AI�ľ��ֲ�
	Bitmap bmpMenu;			//�˵�ͼƬ
	Bitmap bmpDialog;		//�Ի���ͼƬ
	Bitmap bmpBackField;	//�򳡱���ͼƬ
	//Rect�������������¼�������λ�ý���ƥ��
	Rect rectMenu = new Rect(134,0,186,60);		//����˵��ľ��ο�
	Rect rectYesToDialog = new Rect(75,271,100,292);	//���������˵��Ի����е��ǰ�ť
	Rect rectNoToDialog = new Rect(223,271,248,292);	//���������˵��Ի����еķ�ť
	int clubID;//��ž��ֲ�logo��ͼƬID
		
	//����������ʼ����Ա����
	public GameView(FootballActivity father,int clubID) {
		super(father);		//���ø��๹����
		getHolder().addCallback(this);//���Callback�ӿ�
		this.father = father;
		this.clubID = clubID;//��þ��ֲ�logo		
		ball = new Ball(this);		//���������߳�	
		initPlayerInstance();//��ʼ��˫����Ա		
		initGame();//��ʼ����Ϸ		
		initBitmap(father);	//��ʼ��ͼƬ��Դ			
		ait = new AIThread(this);//����AI�����߳�		
		bm = new BonusManager(this);//��ʼ��BonusManager		
		dt = new DrawThread(this,getHolder());//������̨ˢ���߳�
	}
	//�����߳�
	public void startGame(){
		ait.start();			//����AI�����߳�
		bm.start();				//����BonusManager�߳�
		ball.start();			//���������߳�
	}
	//��ʼ��λͼ
	public void initBitmap(Context context){
		Resources r = context.getResources();		//���Resources����
		bmpMyPlayer = BitmapFactory.decodeResource(r,R.drawable.player18);		//�����ԱͼƬ��ʼ��
		bmpAIPlayer = BitmapFactory.decodeResource(r, R.drawable.ai_player);	//AI��ԱͼƬ��ʼ��
		bmpScores = new Bitmap[10];										 		//����ͼƬ��ʼ��
		bmpScores[0] = BitmapFactory.decodeResource(r, R.drawable.digit_0);		//����ͼƬ0
		bmpScores[1] = BitmapFactory.decodeResource(r, R.drawable.digit_1);		//����ͼƬ1
		bmpScores[2] = BitmapFactory.decodeResource(r, R.drawable.digit_2);		//����ͼƬ2
		bmpScores[3] = BitmapFactory.decodeResource(r, R.drawable.digit_3);		//����ͼƬ3
		bmpScores[4] = BitmapFactory.decodeResource(r, R.drawable.digit_4);		//����ͼƬ4
		bmpScores[5] = BitmapFactory.decodeResource(r, R.drawable.digit_5);		//����ͼƬ5
		bmpScores[6] = BitmapFactory.decodeResource(r, R.drawable.digit_6);		//����ͼƬ6
		bmpScores[7] = BitmapFactory.decodeResource(r, R.drawable.digit_7);		//����ͼƬ7 
		bmpScores[8] = BitmapFactory.decodeResource(r, R.drawable.digit_8);		//����ͼƬ8  
		bmpScores[9] = BitmapFactory.decodeResource(r, R.drawable.digit_9);		//����ͼƬ9
		bmpGoalBanner = BitmapFactory.decodeResource(r, R.drawable.game_goal);	//�����ı���		
		bmpWinText = BitmapFactory.decodeResource(r, R.drawable.game_win);	//����ʤ��ʱ����ʾ����
		bmpLoseBanner = BitmapFactory.decodeResource(r, R.drawable.game_over);	//����ʧ��ʱ����ʾͼƬ
		bmpLoseText = BitmapFactory.decodeResource(r, R.drawable.game_lose);	//����ʧ��ʱ����ʾ����
		Matrix m = new Matrix();			//����Matrix�������ڲü�ͼƬ���ʺ���Ϸ�����о��ֲ�ͼƬ�Ĵ�С
		Bitmap bmpTemp = BitmapFactory.decodeResource(r,(int) clubID);	//�õ�ָ���ľ��ֲ�ͼƬ
		int width = bmpTemp.getWidth();				//��þ��ֲ�ͼƬ���
		int height = bmpTemp.getHeight();			//��ȡ���ֲ�ͼƬ�߶�
		m.postScale(60.0f/width,60.0f/height);		//����Matrix
		bmpMyClub = Bitmap.createBitmap(bmpTemp, 0, 0, width,height , m, true);		//��Ҿ��ֲ�logo
		bmpAIClub = BitmapFactory.decodeResource(r, R.drawable.ai_club);		//AI�ľ��ֲ�logo		
		bmpMenu = BitmapFactory.decodeResource(r, R.drawable.menu);//�˵���ťͼƬ
		bmpDialog = BitmapFactory.decodeResource(r, R.drawable.dialog);	//�˵��Ի���	 
		bmpBackField = BitmapFactory.decodeResource(r, R.drawable.field);//���򳡱���
		bmpPassAll = BitmapFactory.decodeResource(r, R.drawable.game_pass);	//��Ӯ���б�������ʾͼƬ
	}	
	//��ʼ����Ϸ����,ͨ�غ������������,����initRound����
	public void initGame(){
		scores[0]=0;		//��ұȷֹ���
		scores[1]=0;		//AI�ȷֹ���
		AIGoalLeft = maxLeftPosition;		//��ʼ��AI�������������
		AIGoalRight = maxRightPosition;		//��ʼ��AI�������ұ�����
		myGoalLeft = maxLeftPosition;		//��ʼ����ҵ������������
		myGoalRight = maxRightPosition;		//��ʼ����ҵ������������
		initRound();
	}
	//��Ļ��Ⱦ����
	protected void doDraw(Canvas canvas) {
		canvas.drawBitmap(bmpBackField, 0, 0, null);		//���򳡱���
		//����С��
		ball.drawSelf(canvas);
		//����˫�����
		drawBothPlayers(canvas);
		//�����ȷ�
		drawScores(canvas);
		//��鲢���ƻ��Bonus
		checkAndDrawBonus(canvas);
		//����˫����logo
		drawLogo(canvas);
		//�����˵�
		canvas.drawBitmap(bmpMenu, 134, 0, null);
		if(isScoredAGoal){		//�������һ�򣬻��ƽ���ͼƬ
			canvas.drawBitmap(bmpGoalBanner, (fieldLeft+fieldRight)/2-bmpGoalBanner.getWidth()/2, (fieldUp+fieldDown)/2-bmpGoalBanner.getHeight()/2, null);
			timeCounter++;		//ÿ��һ�Σ���ʱ������
			if(timeCounter > 50){	//�����ʱ�����ӵ�50
				isScoredAGoal = false;		//���ý����־λΪfalse
				initRound();				//��ʼ����Ϸ�غ�
				timeCounter = 0;			//��ռ�ʱ��
				ball.isPlaying = true;		//С���˶���־λ��Ϊtrue
			}
		}
		if(isGameOver){//һ����������������ʤ����������ӦͼƬ	
			if(isGamePassAll){	//�Ƿ��Ӯ����ǿ��AI
				canvas.drawBitmap(bmpPassAll,(fieldLeft+fieldRight)/2-bmpPassAll.getWidth()/2, (fieldUp+fieldDown)/2-bmpPassAll.getHeight()/2, null);
				timeCounter++;//ÿ��һ�Σ���ʱ������
				if(timeCounter >50){//�����ʱ�����ӵ�50
					isGameOver = false;					//������Ϸ������־λΪfalse
					isShowDialog = true;				//������ʾ�Ի���Ϊtrue				
				}
			}
			else if(isGameWin){			//���ʤ��				
				canvas.drawBitmap(bmpLoseBanner, (fieldLeft+fieldRight)/2-bmpWinBanner.getWidth()/2, (fieldUp+fieldDown)/2-bmpWinBanner.getHeight()/2-20, null);
				canvas.drawBitmap(bmpWinText, (fieldLeft+fieldRight)/2-bmpWinText.getWidth()/2, (fieldUp+fieldDown)/2-bmpWinText.getHeight()/2, null);
				timeCounter++;//ÿ��һ�Σ���ʱ������
				if(timeCounter >50){//�����ʱ�����ӵ�50
					isGameOver = false;//������Ϸ������־λΪfalse
					initGame();			//��ʼ����Ϸ
					levelUp();			//����
					timeCounter = 0;	//��ʱ������
					ball.isPlaying = true;	//�����˶���־λΪtrue
				}
			}
			else{
				canvas.drawBitmap(bmpLoseBanner, (fieldLeft+fieldRight)/2-bmpWinBanner.getWidth()/2, (fieldUp+fieldDown)/2-bmpWinBanner.getHeight()/2-20, null);
				canvas.drawBitmap(bmpLoseText, (fieldLeft+fieldRight)/2-bmpWinText.getWidth()/2, (fieldUp+fieldDown)/2-bmpWinText.getHeight()/2, null);
				timeCounter++;//ÿ��һ�Σ���ʱ������
				if(timeCounter >50){//�����ʱ�����ӵ�50
					isGameOver = false;					//������Ϸ������־λΪfalse
					isShowDialog = true;				//������ʾ�Ի���Ϊtrue				
				}
			}
		}
		if(isShowDialog){
			canvas.drawBitmap(bmpDialog, 60, 200, null);	//�����������˵��Ի���
		}
	}	
	//�Ѻ�˻�����
	public void drawBars(Canvas canvas){
		Paint paint = new Paint();
		paint.setStrokeWidth(2.5f);
		for(int i=0;i<8;i++){
			canvas.drawLine(fieldLeft, fieldUp+barDistance/2+i*barDistance, fieldRight, fieldUp+barDistance/2+i*barDistance, paint);
		}
	}
	//��������ʼ����Ա����
	public void initPlayerInstance(){
		for(int i=0;i<11;i++){
			Player p1 = new Player();	//����һ��Player����
			p1.attackDirection = 0;		//������Ա�Ľ�������
			alMyPlayer.add(p1);			//��Player������ӵ���ҵ���Ա������
			Player p2 = new Player();	//����һ��Player����
			p2.attackDirection = 8;		//������Ա�Ľ�������
			alAIPlayer.add(p2);			//��Player������ӵ�AI����Ա������
		}
	}
	//�����������û���ѡ���ű�����,directionΪ��������0Ϊ���ϣ�1Ϊ����
	public void initPlayerPositions(ArrayList<Player> al,int [] pos,int direction){
		int index = 0;
		for(int i=0;i<3;i++){			//һ����3��������
			int playerNumber = pos[i];	//ȡ��ÿ�����������ж��ٶ�Ա
			int segmentNumber = playerNumber + 1;		//���ݽ������ϵ���ż�Էֶ�
			int segmentSpan = (fieldRight - fieldLeft)/segmentNumber;		//���ÿ�ζγ�
			for(int j=1;j<=playerNumber;j++){
				Player p = al.get(index++);
				p.x = fieldLeft + j*segmentSpan;
				if(direction == 0){		//������������
					p.y = fieldUp + barDistance/2 + barDistance*2*(i+1);
				}
				else{					//������������
					p.y = fieldDown - barDistance/2 - barDistance*2*(i+1);
				}
			}
		}
		//ȷ������Ա��λ��
		Player p = al.get(index);		//����ԱΪ��Ա�б��е����һ��
		p.x = (fieldLeft + fieldRight)/2;		//����Ա��X����Ϊ�򳡵�
		if(direction == 0){			//���������������
			p.y =  fieldUp + barDistance/2 + 7*barDistance;
		}
		else{		//���������������
			p.y = fieldUp + barDistance/2;
		}
	}
	//��������ʼ����Ϸ�غϣ�ÿ�ν������ø÷���һ��
	public void initRound(){
		//��λС��λ�ã������������
		ball.x = (fieldLeft + fieldRight)/2;//ָ��С��X����
		ball.y = (fieldUp + fieldDown)/2;//ָ��С��Y����
		ball.direction = (int)(Math.random()*100)%16;	//���ָ��С��ķ���
		//��λ˫���˶�Ա		
		int [] layoutArray = father.layoutArray;		//�����Ա��ռλ����
		initPlayerPositions(alMyPlayer,layoutArray,0);		//Ϊ������Ավλ
		int [] tempArray2 = {3,3,4};					//AI��Ա��ռλ����		
		initPlayerPositions(alAIPlayer,tempArray2,1);		//ΪAI����Ավλ
	}
	//�������������˶�Ա
	public void drawBothPlayers(Canvas canvas){
		for(Player p:alMyPlayer){	//���������Ա�б�
			canvas.drawBitmap(bmpMyPlayer, p.x-playerSize/2, p.y-playerSize/2, null);//������Ա
		}
		for(Player p:alAIPlayer){		//����AI��Ա�б�
			canvas.drawBitmap(bmpAIPlayer, p.x-playerSize/2, p.y-playerSize/2, null);//������Ա
		}
	}
	//��������˫���ȷ�
	public void drawScores(Canvas canvas){
		//����߱ȷִ������һ�
		String score = scores[0]+"";	
		int l = score.length();	//��ñȷ��ַ�������
		for(int j=0;j<l;j++){	//�����ַ������ݻ�������ͼƬ
			canvas.drawBitmap(bmpScores[score.charAt(j)-'0'], scoreLeft+j*32, 2, null);
		}
		//���ұ߱ȷ֣���������
		String scoreR = scores[1]+"";
		int l2 = scoreR.length();//��ñȷ��ַ�������
		for(int i=l2-1;i>=0;i--){//�����ַ������ݻ�������ͼƬ
			canvas.drawBitmap(bmpScores[scoreR.charAt(i)-'0'], scoreRight-(l2-i)*32, 2, null);
		}
	}
	//��������鲢����Bonus
	public void checkAndDrawBonus(Canvas canvas){
		if(balForDraw.size() != 0){		//����Ƿ��пɻ���Bonus
				for(Bonus b:balForDraw){
					if(b.status == Bonus.LIVE){		//�ж�Bonus��״̬�Ƿ�ΪLIVE
						b.drawSelf(canvas);	//��Bonus
					}
					else if(b.status == Bonus.EFFECTIVE){//�ж�Bonus��״̬�Ƿ�ΪEFFECTIVE
						b.drawEffect(canvas);		//��Bonus������Ч��
					}					
				}				
		}
		//���balAdd��û����Ҫ��ӵ�Bonus
		if(balAdd.size() != 0){	//
			balForDraw.addAll(balAdd);		//��balAdd�е�Bonus��ӵ�balForDraw��
			balAdd.clear();					//���balAdd
		}
		//���balDelete��û����Ҫɾ����Bonus
		if(balDelete.size() != 0){
			balForDraw.removeAll(balDelete);	//��balForDrawɾ��balDelete�е�Bonus
			balDelete.clear();					//���balDelete
		}
	}
	//��������˫�����ֲ�logo
	public void drawLogo(Canvas canvas){
		canvas.drawBitmap(bmpMyClub, 0, 0, null);		//����Ҿ��ֲ���ͼƬ
		canvas.drawBitmap(bmpAIClub, 240, 0, null);		//��AI���ֲ���ͼƬ
	}
	//�������ƶ��˶�Ա������Ĳ�������Ϊ12(��),4(��)�����������������Ա������Ϊ-1
	public void movePlayers(ArrayList<Player> al,int direction){
		switch(direction){					//�Դ���ķ�����������ж�
		case 12:			//12Ϊ��
			Player pl1 = al.get(al.size()-1);
			if(pl1.x - pl1.movingSpan  >= maxLeftPosition){	//�ж��Ƿ���������
				for(Player p: al){
					p.x -= p.movingSpan;		//�����ƶ�ָ������
					p.movingDirection = 12;		//��¼�ƶ�����
				}				
			}			
			break;
		case 4:			//4Ϊ��
			Player pl2 = al.get(al.size()-1);
			if(pl2.x + pl2.movingSpan  <= maxRightPosition){//�ж��Ƿ���������
				for(Player p: al){
					p.x += p.movingSpan;		//�����ƶ�ָ���ľ���
					p.movingDirection = 4;		//��¼�ƶ�����
				}				
			}	
			break;
		default:		//�������Ĳ����Ȳ���4Ҳ����12
			for(Player p: al){
				p.movingDirection = -1;			//����Ա���ƶ�������Ϊ-1
			}
			break;
		}
	}
	//����������Ƿ��������,ԭ����˭�ȵõ�ָ����˭��Ӯ
	public void checkIfLevelUp(){
		if(Math.max(scores[0], scores[1]) == winPoint){	//�����������һ�ӵıȷִﵽ��ʤ����
			isGameOver = true;							//����Ϸ������־λ��Ϊtrue
			if(scores[0] == winPoint){					//�ж��Ƿ������Ӯ�ñ���
				if(this.level >= MaxLevel){		//�ﵽ��ߵȼ�
					isGamePassAll = true;			//������Ϸ��ͨ���еȼ���־λ
				}
				else{
					isGameWin = true;				//���ñ���ʤ����־λ
				}				
				if(father.wantSound && father.mpCheerForWin!= null){					//����Ҫ��������Ӧ����
					father.mpCheerForWin.start();
				}				
			}
			else{										//��AIӮ�ñ���
				isGameWin = false;
				if(father.wantSound && father.mpCheerForLose!=null){					//����Ҫ��������Ӧ����
					father.mpCheerForLose.start();
				}				
			}
		}
		else{											//���ֻ�ǵ����Ľ���һ����
			isScoredAGoal = true;						//�������־λ��Ϊtrue
			if(father.wantSound && father.mpCheerForGoal!=null){						//����Ҫ��������Ӧ����
				father.mpCheerForGoal.start();
			}			
		}
	}
	//��������Ϸ�������ı���������Ѷ�
	public void levelUp(){
		ball.levelUp();	//����С����������
		for(Player p:alMyPlayer){		//���������Ա�б�
			p.levelUp();				//������ҵ���������
		}
		this.level++;
	}
	@Override
	protected void finalize() throws Throwable {
		System.out.println("############ FieldView  is dead##########");
		super.finalize();
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {		//��дsurfaceChanged����
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {//��дsurfaceCreated����
		if(!dt.isAlive()){
			dt.start();
		}			 
        father.pmt = new PlayerMoveThread(father);//��ʼ����������Ա���ƶ������߳�
        father.pmt.start();
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {//��дsurfaceDestroyed����
		dt.isGameOn = false;	//ֹͣˢ���̵߳�ִ��
		father.pmt.flag = false;
	}
}