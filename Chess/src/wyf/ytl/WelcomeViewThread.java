package wyf.ytl;


public class WelcomeViewThread extends Thread{
	private boolean flag = true;//ѭ����־λ
	WelcomeView welcomeView;//WelcomeView������
	public WelcomeViewThread(WelcomeView welcomeView){//������
		this.welcomeView = welcomeView;//�õ�WelcomeView������
	}
	public void setFlag(boolean flag){//����ѭ����־λ
		this.flag = flag;
	}
	public void run(){//��д��run����
    	try{
    		Thread.sleep(300);//˯�����ٺ��룬��֤�����Ѿ���ʾ
    	}
    	catch(Exception e){//�����쳣
    		e.printStackTrace();//��ӡ�쳣��Ϣ
    	}
		while(flag){
			welcomeView.logoX += 10;//�ƶ���ӭ�����logo
			if(welcomeView.logoX>0){//��λ��ֹͣ�ƶ�
				welcomeView.logoX = 0;
			}
			welcomeView.boyX += 20;//�ƶ�С�к�ͼƬ
			if(welcomeView.boyX>70){//��λ�ú�ֹͣ�ƶ�
				welcomeView.boyX = 70;
			}
			welcomeView.oldboyX += 15;//�ƶ�С��ͷ
			if(welcomeView.oldboyX>0){//��λ��ֹͣ�ƶ�
				welcomeView.oldboyX = 0;
			}
			welcomeView.bordbackgroundY += 50;//�ƶ����ֱ���
			if(welcomeView.bordbackgroundY>240){
				welcomeView.bordbackgroundY = 240;
			}
			welcomeView.logo2X -= 30;//����ͼƬ������
			if(welcomeView.logo2X<150){
				welcomeView.logo2X = 150; //ֹͣ�ƶ�
			}
			if(welcomeView.logo2X == 150){//��logo2��λ��ť���ƶ�����
				welcomeView.menuY -= 30;
				if(welcomeView.menuY<355){
					welcomeView.menuY = 355;
				}
			} 
        	try{ 
        		Thread.sleep(100);//˯��ָ�������� 
        	}catch(Exception e){//�����쳣
        		e.printStackTrace();//��ӡ�쳣��Ϣ
        	}
		}
	}
}