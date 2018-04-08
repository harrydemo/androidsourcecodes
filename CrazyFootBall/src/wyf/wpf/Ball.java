package wyf.wpf;			//���������
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
/*
 * Ball����һ���̳���Thread����߳��ࡣ����Ҫ�Ĺ��������Ƿ�װ�����йص���Ϣ��������㡢�����ƶ��ٶȵȡ�
 * ��Σ�����Ҳ�����ƶ������λ�ã�������ײ��⣬��Щ������Ҫ��ͨ��run������ʵ�ֵģ�run��������Ҫ����������
 * move��checkCollision��ǰ�߸����������ķ���16��֮һ�������ƶ������λ�á��������ڽ�����ײ��⣬�鿴
 * �Ƿ���������AI����ҵ��˶�Ա���Ƿ������߽磬�Ƿ�����һЩBonus�����С��ȵȡ����໹��һ��drawSelf������
 * ��������ϷView��myDraw�����е����Ի����Լ���
 */
public class Ball extends Thread{
	int x;								//�������ĵ�x����
	int y;								//�������ĵ�y����
	int direction=-1;					//������˶����򣬴�0��15˳ʱ���������Ͽ�ʼ��16������д���ʱ�򻭸�ͼ����ȥ
	int velocity=20;					//������˶�����
	int maxVelocity = 20;				//����˶�����
	int minVelocity = 5;				//��С�˶�����
	int ballSize = 10;					//�����С
	Matrix matrix;						//Matrix��������ʵ������ͼƬ�ķ�תЧ��
	Bitmap bmpBall;						//�����ͼƬ
	GameView father;					//FieldView��������
	float acceleration=-0.10f;			//����������ײ��ʱ�ٶȻ���˥��
	boolean isStarted;					//�����Ƿ�ʼ
	boolean isPlaying;					//�����Ƿ����ڽ���
	float sin675=0.92f;					//�ض��Ƕ�����ֵ�����ڼ����ƶ������ظ���
	float sin225=0.38f;					//�ض��Ƕ�����ֵ�����ڼ����ƶ������ظ���
	float sin45=0.7f;					//�ض��Ƕ�����ֵ�����ڼ����ƶ������ظ���
	int sleepSpan = 50;					//����ʱ��
	float changeOdd = 0.6f;				//����ļ���
	int lastKicker;						//�������һ����˭�ߵģ�0�����Լ���8����AI
	
	public Ball(GameView father){
		super.setName("##-Ball");			//�����߳����֣����ڵ�����
		this.father = father;
		
		Resources r = father.getContext().getResources();	//��ȡResources����
		bmpBall = BitmapFactory.decodeResource(r, R.drawable.ball);//����ͼƬ
		matrix = new Matrix();		
		isStarted = true;		//����ѭ������
		isPlaying = true;		//
	}
	//�̵߳����񷽷�
	public void run(){
		while(isStarted){
			while(isPlaying){
				//�ƶ�����
				move();
				//��ײ���
				checkCollision();
				//����һ��
				try{
					Thread.sleep(sleepSpan);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			try{
				Thread.sleep(500);
			}
			catch(Exception e){		e.printStackTrace();
			}
		}
	}
	//��������ͼƬ
	public void drawSelf(Canvas canvas){
		Bitmap bmp=null;
		if(isPlaying){
			matrix.postRotate(5);		//������ͼƬ��תһ���Ƕȣ������µ�Bitmap����
			 bmp = Bitmap.createBitmap(bmpBall, 0, 0, ballSize, ballSize, matrix, true);
		}		
		else{
			bmp = bmpBall;				//���û������Ϸ�У�������ת�任
		}
		canvas.drawBitmap(bmp, x-ballSize/2, y-ballSize/2, null);	
	}
	//�ƶ�����
	public void move(){
		switch(direction){
		case 0:			//��������
			y -= velocity;									//�ƶ�
			decreamentVelocity();								//˥���ٶ�
			break;
		case 1:			//��ƫ��22.5��
			x += (int)(velocity*sin225);						//�ƶ�
			y -= (int)(velocity*sin675);							
			decreamentVelocity();								//˥���ٶ�
			break;
		case 2:			//��ƫ��45��
			x += (int)(velocity*sin45);							//�ƶ�	
			y -= (int)(velocity*sin45);						
			decreamentVelocity();								//˥���ٶ�
			break;
		case 3:			//��ƫ��67.5��
			x += (int)(velocity*sin225);							//�ƶ�	
			y -= (int)(velocity*sin675);						
			decreamentVelocity();								//˥���ٶ�
			break;
		case 4:			//��������
			x += velocity;									//�ƶ�				
			decreamentVelocity();								//˥���ٶ�
			break;
		case 5:			//��ƫ��22.5��
			x += (int)(velocity*sin675);							//�ƶ�	
			y += (int)(velocity*sin225);						
			decreamentVelocity();								//˥���ٶ�
			break;
		case 6:			//��ƫ��45��
			x += (int)(velocity*sin45);						//�ƶ�	
			y += (int)(velocity*sin45);						
			decreamentVelocity();								//˥���ٶ�
			break;
		case 7:			//��ƫ��67.5��
			x += (int)(velocity*sin225);						//�ƶ�	
			y += (int)(velocity*sin675);						
			decreamentVelocity();								//˥���ٶ�
			break;
		case 8:			//��������
			y += velocity;									//�ƶ�										
			decreamentVelocity();								//˥���ٶ�
			break;
		case 9:			//��ƫ��22.5��
			x -= (int)(velocity*sin225);						//�ƶ�	
			y += (int)(velocity*sin675);						
			decreamentVelocity();								//˥���ٶ�
			break;
		case 10:		//��ƫ��45��
			x -= (int)(velocity*sin45);						//�ƶ�	
			y += (int)(velocity*sin45);						
			decreamentVelocity();								//˥���ٶ�
			break;
		case 11:		//��ƫ��67.5��
			x -= (int)(velocity*sin675);						//�ƶ�	
			y += (int)(velocity*sin225);						
			decreamentVelocity();								//˥���ٶ�
			break;
		case 12:		//��������
			x -= velocity;									//�ƶ�					
			decreamentVelocity();								//˥���ٶ�
			break;
		case 13:		//��ƫ��22.5��
			x -= (int)(velocity*sin675);						//�ƶ�	
			y -= (int)(velocity*sin225);						
			decreamentVelocity();								//˥���ٶ�
			break;
		case 14:		//��ƫ��45��
			x -= (int)(velocity*sin45);						//�ƶ�	
			y -= (int)(velocity*sin45);						
			decreamentVelocity();								//˥���ٶ�
			break;
		case 15:		//��ƫ��67.5��
			x -= (int)(velocity*sin225);						//�ƶ�	
			y -= (int)(velocity*sin675);						
			decreamentVelocity();								//˥���ٶ�
			break;
		default:
			break;
		}
	}
	
	public void decreamentVelocity(){
		velocity = (int)(velocity*(1+acceleration));		//˥���ٶ�
		if(velocity<minVelocity){		//˥�����������С�ٶ���ֹͣ˥��
			velocity = minVelocity;
		}
	}	
	//�ܵ���ײ��ⷽ�������а�������������ֱ�Ϊ������һ������
	public void checkCollision(){
		checkForBorders();		//����Ƿ���߽�
		checkForAIPlayers();	//����Ƿ�����AI
		checkForUserPlayers();	//����Ƿ��������
		checkIfScoreAGoal();	//����Ƿ������
		checkForBonus();		//����Ƿ�����Bonus
	}
	/*
	 * �˷�����������Ƿ������˱߽磬������������������е�ĳһ�߽�
	 * ������Ϊ��һ����������ȷ��·�ߣ����Ʒ��䶨�ɣ��ı䷽��
	 * һ����������������Է���1�����ϱ߽磬����ĳ���ϴ�ĸ���
	 * ��С��ķ����Ϊ7����������Խ�С�ĸ��ʽ������Ϊ5��6��7����
	 * �����е�ĳһ����
	 */
	public void checkForBorders(){
		int d = direction;
		//�����ǲ��ǳ��߽���
		if(x <= father.fieldLeft){
			//ײ����߽�
			if(d>8 && d<16 && d!=12){	//���������ײ����߽�
				if(Math.random() < changeOdd){	//һ�����ʸ�������ȷ����·�߱���
					direction = 16 - direction;
				}
				else{							//һ�������������
					direction = (direction>12?1:5) + (int)(Math.random()*100)%3;
				}
			}	
			else if(d == 12){			//�������ײ����߽�
				if(Math.random() < 0.4){		//ע���������ҪС����Ϊ��ײ��ȥϣ���������ĸ��ʴ�һЩ	
					direction = 4;
				}
				else{
					direction = (Math.random() > 0.5?3:5);
				}
			}
		}
		else if(x > father.fieldRight){
			//ײ���ұ߽�
			if(d >0 && d<8 && d!=4){
				if(Math.random() < changeOdd){		//����������·�߱���
					direction = 16-direction;
				}
				else{								//һ�������������
					direction = (direction>4?9:13) + (int)(Math.random()*100)%3;
				}
			}
			else if(d == 4){			//�������ײ���ұ߽�
				if(Math.random() < 0.4){
					direction = 12;
				}
				else{
					direction = (Math.random()>0.5?11:13);
				}
			}
		}
		d = direction;
		//�ж��Ƿ�ײ���ϱ߽�		
		if(y < father.fieldUp){
			//������ײ
			if(d>0 && d<4 || d>12&&d<16){
				if(Math.random() < changeOdd){			//һ����������ȷ����·�߱���
					direction = (d>12?24:8) - d;
				}
				else{									//һ�������������
					direction = (d>12?9:5) + (int)(Math.random()*100)%3;
				}
			}
			else if(d == 0){			//��ײ���ϱ߽�
				if(Math.random() < 0.4){				//һ����������ȷ����·�߷���
					direction = 8;
				}
				else{
					direction = (Math.random() < 0.5?7:9);		//һ�������������
				}
			}
		}
		//�ж��Ƿ�ײ���±߽�
		else if(y > father.fieldDown){
			//������ײ
			if(d >4 && d<12 && d!=8){
				if(Math.random() < changeOdd){	//����������·�߱���
					direction = (d>8?24:8) - d;
				}
				else{							//�������
					direction = (d>8?13:1) +(int)(Math.random()*100)%3;
				}
			}
			else if(d == 8){		//��ײ���±߽�
				if(Math.random() < 0.4){		//��������
					direction = 0;
				}
				else{							//�������
					direction = (Math.random()>0.5?1:15);
				}
			}
		}
	}
	/*
	 * �˷�������Ƿ�����AI�˶�Ա����������������handleCollision����������ײ��
	 * ͬʱ���������������������ʺ�����lastKicker
	 */
	public void checkForAIPlayers(){
		int r = (this.ballSize + father.playerSize)/2;
		for(Player p:father.alAIPlayer){
			if((p.x - this.x)*(p.x - this.x) + (p.y - this.y)*(p.y - this.y) <= r*r){		//������ײ
				handleCollision(this,p);			//������ײ
				if(father.father.wantSound && father.father.mpKick!=null){		//��������
					try {							//��try/catch����װ
						father.father.mpKick.start();
					} catch (Exception e) {}
				}				
				velocity = p.power;
				lastKicker = 8;				//��¼���һ����˭�ߵ�
			}
		}		
	}
	/*
	 * �˷�������Ƿ���������� �������˶�Ա��
	 */
	public void checkForUserPlayers(){
		int r = (this.ballSize + father.playerSize)/2;
		for(Player p:father.alMyPlayer){
			if((p.x - this.x)*(p.x - this.x) + (p.y - this.y)*(p.y - this.y) <= r*r){		//������ײ
				handleCollision(this,p);			//������ײ
				if(father.father.wantSound && father.father.mpKick!=null){		//��������
					try {
						father.father.mpKick.start();
					} catch (Exception e) {}	
				}				
				velocity = p.power;			//�� �������ٶ�
				lastKicker = 0;				//��¼���һ��˭�ߵ�
			}
		}
	}
	/*
	 * �˷�������������˶�Ա֮�����ײ,AI����ҵĴ���ʽ��һ���ģ�
	 * ���Ȳ鿴Player�����movingDirection�����ۺ�Player�����
	 * attackDirection��ȷ������Χ������ֱ������ϵ�е�4�����ޣ�
	 * Ȼ���ڷ���Χ���������һ�������������ķ����й��������棬����
	 * �Ƚ���ʵ����Ҫע��������������˶�Ա��ײʱ�˶�Ա��ֹ������
	 * ��ô��ѡ�ķ������1��15�����������ϣ���7��9���������г��£�
	 */
	public void handleCollision(Ball ball,Player p){
		switch(p.movingDirection){
		case 12:				//�ƶ���������
			if(p.attackDirection == 0){		//������������
				ball.direction = 13 + (int)(Math.random()*100)%3;		//ȡ13��14��15��һ��
			}
			else{							//������������
				ball.direction = 9 + (int)(Math.random()*100)%3;		//ȡ9��10��11��һ��
			}
			break;
		case 4:					//�ƶ���������
			if(p.attackDirection == 0){		//������������
				ball.direction = 1 + (int)(Math.random()*100)%3;		//ȡ1��2��3��һ��
			}
			else{							//������������
				ball.direction = 5 + (int)(Math.random()*100)%3;		//ȡ5,6,7��һ��
			}
			break;
		default:				//û���ƶ�
			if(p.attackDirection == 0){		//������������
				ball.direction = 15 + (int)(Math.random()*100)%3;		//ȡ1��2��3��һ��
				if(ball.direction > 15){
					ball.direction = ball.direction % 16;
				}
			}
			else{							//������������
				ball.direction = 7 + (int)(Math.random()*100)%3;		//ȡ7��8��9��һ��
			}
			break;			
		}
	}
	/*
	 * �˷������ڼ���Ƿ�������ǣ�����Ӧ��ӵ÷ּ�1��Ȼ���ж���Ϸ�Ƿ��������Ϸ������˭�Ƚ���8��˭��Ӯ��
	 */
	public void checkIfScoreAGoal(){
		if(this.y <= father.fieldUp && this.x > father.AIGoalLeft && this.x < father.AIGoalRight){
			//�Ϸ����Ž���,�����
			isPlaying = false;
			father.scores[0]++;
			father.checkIfLevelUp();
		}
		else if(this.y >= father.fieldDown && this.x > father.myGoalLeft && this.x < father.myGoalRight){
			//AI����
			isPlaying = false;
			father.scores[1]++;
			father.checkIfLevelUp();
		}
	}
	//��������
	public void levelUp(){
		this.minVelocity +=3;
		if(minVelocity > 20){
			minVelocity = 20;
		}
	}
	/*
	 * �÷����ж��Ƿ�������Bonus���������������Ӧ��Bonus���в���
	 * �ı���״̬�������䷽���޸���Ϸ�����ȵȣ�����������
	 */
	public void checkForBonus(){
		if(father.balLive.size() != 0){
			for(Bonus b:father.balLive){
				if((b.x - x)*(b.x - x) + (b.y-y)*(b.y-y) <= (b.bonusSize/2+ballSize/2)*(b.bonusSize/2+ballSize/2)
						&& b.status == Bonus.LIVE){
					b.status = Bonus.EFFECTIVE;
					father.balLive.remove(b);
					b.setTarget(this.lastKicker);
					b.doJob();
					b.setTimeout(Bonus.EFFECT_SPAN);	
					if(father.father.wantSound){
						if(b instanceof IceBonus){		//�Ǳ���С��
							try {
								father.father.mpIce.start();
							} catch (Exception e) {}
						}							
						else if( b instanceof LargerGoalBonus){		//���������ŵ�
							try {
								father.father.mpLargerGoal.start();
							} catch (Exception e) {}
						}
					}
					break;
				}					
			}
		}
	}
	
	
}