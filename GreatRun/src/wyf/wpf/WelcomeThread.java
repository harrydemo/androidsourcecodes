package wyf.wpf;				//���������
public class WelcomeThread extends Thread{
	WelcomeView father;
	boolean flag;
	int sleepSpan = 100;//����ʱ��
	
	public WelcomeThread(WelcomeView father){
		this.father = father;
		flag = true;
	}
	//�̵߳�ִ�з���
	public void run(){
		while(flag){
			switch(father.status){
			case 0://���ڽ��Ա���ͼƬ
				father.alpha += 5;
				if(father.alpha == 255){
					father.status = 4;
				}
				break;
			case 5://�а�ť����
				int index = father.selectedIndex;
				father.father.myHandler.sendEmptyMessage(index);
				father.status = 4;//�ص�����״̬
				break;
			default:
				break;
			}
			try{
				Thread.sleep(sleepSpan);
			}										//�߳�����
			catch(Exception e){
				e.printStackTrace();
			}										//���񲢴�ӡ�쳣
		}
	}
}