package cn.edu.xtu.tilepuzzle;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

public class HelpActivity extends Activity{
	
	private String helpContent="";
	private TextView textView;
	//������
	// private GestureDetector gestureDetector; 	
   /* 
    public  GameSetUI gameSetUI;// ������    
    public WaitCanvasUI waitCanvasUI;
    
    public AddUserNameUI addUserNameUI;   
    public ShowPeopleInfoListUI showPeopleInfoListUI;
    public ClassSetPhoto classSetPhoto;
*/    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("MainActivity===>>onCreate");   
		
		//LayoutInflater factory = LayoutInflater.from(HelpActivity.this);
		// �õ��Զ���Ի���
		//view = factory.inflate(R.layout.game_set, null);

		//View view=(View)findViewById(R.layout.game_set);
		
		setContentView(R.layout.help);
		init();
		
    }     
    private void init(){
    	helpContent="����Ϸ��"+"��ʼ�µ���Ϸ��\n"
		+"����ͼƬ��"+"����ƴͼ�����õı���ͼƬ��\n"
		+"��ѳɼ���"+ "��ʾ������ұ�����¼����ѳɼ���\n"
		+"ѡ�"+ "��Ϸ��һЩ���ã������ʾ���б�ǣ�������Ϸ���������ȡ�\n"
		+"������"+ "�鿴�����ĵ���\n"
		+"��Ϸ���ã�"+ "����Ϸ���õ��ս�����Ϸ����ʱ��״̬��\n"
		+"���ף�"+ "���û��������Ϸ���̽�����Ϸʤ��״̬��\n"
		+"����˵����"+ "......";
    	
    	textView=(TextView)HelpActivity.this.findViewById(R.help_id.helpTextView);
    	textView.setText(helpContent);
    	
    }
}
     