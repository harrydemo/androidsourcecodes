package wyf.wpf;
import static wyf.wpf.ConstantUtil.*;


public class KeyThread extends Thread{
	GameView gv;		//GameView����
	int sleepSpan = KEY_THREAD_SLEEP_SPAN;	//����ʱ��
	boolean flag;		//�߳��Ƿ�ִ�б�־λ
	boolean isGameOn;	//��Ϸ�Ƿ���б�־λ
	
	public KeyThread(GameView gv){
		this.gv = gv;
		flag = true;
		isGameOn = true;
	}
	//�������߳�ִ�з���
	public void run(){
		while(flag){
			while(isGameOn){				
				int key = gv.father.keyState;		//��ȡ����״̬
				int tempSegment = gv.hero.currentSegment;	//��ȡӢ�۵Ķ�������
				if((key & 1) == 1){		//����
					gv.hero.y -= HERO_MOVING_SPAN;	//�ƶ�Ӣ��λ��
					if(checkCollision()){		//���������ײ
						gv.hero.x = TILE_SIZE*gv.hero.col;
						gv.hero.y = TILE_SIZE*gv.hero.row+TILE_SIZE-SPRITE_HEIGHT;
					}
					else{						//���û�з�����ײ
					
						checkIfHome();				//����Ƿ�������
						gv.hero.row = (gv.hero.y+SPRITE_HEIGHT-SPRITE_WIDTH/2)/TILE_SIZE;//��������
					}
					if(tempSegment != 7){	//����Ƿ���Ҫ�������ö�����
						gv.hero.setAnimationSegment(7);
					}
				}
				else if((key & 2) == 2){	//����
					gv.hero.y += HERO_MOVING_SPAN;	//�ƶ�Ӣ��λ��
					if(checkCollision()){		//���������ײ
						gv.hero.x = TILE_SIZE*gv.hero.col;
						gv.hero.y = TILE_SIZE*gv.hero.row+TILE_SIZE-SPRITE_HEIGHT;
					}
					else{						//���û�з�����ײ
					//	checkIfRollScreen(2);		//����Ƿ���Ҫ����,1,2,4,8������������
						checkIfHome();				//����Ƿ�������
						gv.hero.row = (gv.hero.y+SPRITE_HEIGHT-SPRITE_WIDTH/2)/TILE_SIZE;//��������
					}
					if(tempSegment != 4){	//����Ƿ���Ҫ�������ö�����
						gv.hero.setAnimationSegment(4);
					}
				}
				else if((key & 4) == 4){	//����
					gv.hero.x -= HERO_MOVING_SPAN;	//�ƶ�Ӣ��λ��
					if(checkCollision()){		//���������ײ
						gv.hero.x = TILE_SIZE*gv.hero.col;
						gv.hero.y = TILE_SIZE*gv.hero.row+TILE_SIZE-SPRITE_HEIGHT;
					}
					else{						//���û������ײ
					
						checkIfHome();				//����Ƿ�������
						gv.hero.col = (gv.hero.x+SPRITE_WIDTH/2)/TILE_SIZE;	//��������
					}
					if(tempSegment != 5){		//����Ƿ���Ҫ�������ö�����
						gv.hero.setAnimationSegment(5);
					}
				}
				else if((key & 8) == 8){	//����
					gv.hero.x += HERO_MOVING_SPAN;		//�ƶ�Ӣ��λ��
					if(checkCollision()){			//���������ײ
						gv.hero.x = TILE_SIZE*gv.hero.col;
						gv.hero.y = TILE_SIZE*gv.hero.row+TILE_SIZE-SPRITE_HEIGHT;
					}
					else{							//���û������ײ
					checkIfHome();				//����Ƿ�������
						gv.hero.col = (gv.hero.x+SPRITE_WIDTH/2)/TILE_SIZE;	//��������
					}
					if(tempSegment != 6){	//����Ƿ���Ҫ�������ö�����
						gv.hero.setAnimationSegment(6);
					}
				}
				else if((key & 0xff) == 0){				//����״̬
					gv.hero.setAnimationSegment(tempSegment%4);	//������������Ϊ��ֹ����
				}
				try{
					Thread.sleep(sleepSpan);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			try{
				Thread.sleep(1500);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	//������������ײ��⣬��������ײ�򷵻�true����û�з�����ײ�򷵻�false
	public boolean checkCollision(){
		int [][] notIn = gv.currNotIn;		//��ȡ��ͼ�Ĳ���ͨ������
		int hx = gv.hero.x;					//��ȡӢ�۵�x����	
		int hy = gv.hero.y;					//��ȡӢ�۵�y����
		int row = 0;	//(hy+SPRITE_HEIGHT-TILE_SIZE/2)/TILE_SIZE;	//���ĵ������
		int col = 0;	//(hx+TILE_SIZE/2)/TILE_SIZE;					//���ĵ������
		//������Ͻǣ���������µĽ��ǻ���SpriteͼƬ�²�31��31���ǿ�
		row = (hy+SPRITE_HEIGHT-TILE_SIZE+1)/TILE_SIZE;	//�������Ͻ���������
		col = hx/TILE_SIZE;								//�������Ͻ���������
		if(row>=0&&row<MAP_ROWS&&col>=0&&col<MAP_COLS&&notIn[row][col]==1){
			return true;
		}
		//������½�
		row = (hy+SPRITE_HEIGHT-1)/TILE_SIZE;		//�������½���������
		col = hx/TILE_SIZE;						//�������½���������
		if(row>=0&&row<MAP_ROWS&&col>=0&&col<MAP_COLS&&notIn[row][col]==1){
			return true;
		}		
		//������Ͻ�
		row = (hy+SPRITE_HEIGHT-TILE_SIZE+1)/TILE_SIZE;	//�������Ͻ���������
		col = (hx+SPRITE_WIDTH-1)/TILE_SIZE;				//�������Ͻ���������
		if(row>=0&&row<MAP_ROWS&&col>=0&&col<MAP_COLS&&notIn[row][col]==1){
			return true;
		}
		//������½�
		row = (hy+SPRITE_HEIGHT-1)/TILE_SIZE;		//�������½���������
		col = (hx+SPRITE_WIDTH-1)/TILE_SIZE;				//�������½���������
		if(row>=0&&row<MAP_ROWS&&col>=0&&col<MAP_COLS&&notIn[row][col]==1){
			return true;
		}
		return false;							//��û�з�����ײ�򷵻�false
	}
	
	//����������Ƿ�������
	public void checkIfHome(){
		if(gv.hero.foundHome(gv.homeLocation[0], gv.homeLocation[1])){	//�ж��Ƿ񵽼�
			gv.pauseGame();			
			if(gv.currStage<MAX_STAGE){	//���������һ��
				gv.currStage++;			//���ӹؿ����
				gv.setGameStatus(STATUS_PASS);
				//gv.startMyAnimation();
			}
			else if(gv.currStage == MAX_STAGE){	//�������һ��
				gv.setGameStatus(STATUS_WIN);		//����״̬Ϊͨȫ��
				//gv.startMyAnimation();
			}
		}
	}
}