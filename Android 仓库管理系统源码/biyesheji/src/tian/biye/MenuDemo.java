package tian.biye;



import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
/**
 * ���˵�ҳ��
 * @author ��־Զ
 *
 */

public class MenuDemo extends TabActivity {
	/**
	 * ����û���
	 */
	private Button tianjia1;
	/**
	 * �޸��û���
	 */
	private Button xiugai1;
	/**
	 * ɾ���û���
	 */
	private Button shanchu1;
	/**
	 * ��ѯ�û���
	 */
	private Button chaxun1;
	/**
	 * �����Ʒ��
	 */
	private Button tianjia2;
	/**
	 * �޸���Ʒ��
	 */
	private Button xiugai2;
	/**
	 * ɾ����Ʒ��
	 */
	private Button shanchu2;
	/**
	 * ��ѯ��Ʒ��
	 */
	private Button chaxun2;
	/**
	 * ��ӹ�Ӧ�̼�
	 */
	private Button tianjia3;
	/**
	 * �޸Ĺ�Ӧ�̼�
	 */
	private Button xiugai3;
	/**
	 * ɾ����Ӧ�̼�
	 */
	private Button shanchu3;
	/**
	 * ��ѯ��Ӧ�̼�
	 */
	private Button chaxun3;
	/**
	 * �������
	 */
	private Button tianjia4;
	/**
	 * �޸�����
	 */
	private Button xiugai4;
	/**
	 * ɾ������
	 */
	private Button shanchu4;
	/**
	 * ��ѯ����
	 */
	private Button chaxun4;
	/**
	 * ��ӳ����
	 */
	private Button tianjia5;
	/**
	 * �޸ĳ����
	 */
	private Button xiugai5;
	/**
	 * ɾ�������
	 */
	private Button shanchu5;
	/**
	 * ��ѯ�����
	 */
	private Button chaxun5;
	/**
	 * �û������
	 */
	private Button yonghu;
	/**
	 * �޸������
	 */
	private Button mima;
	String names=null;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    TabHost tab=getTabHost();
	    tab.setPadding(0, -20, 0, 0);
	    
	    tab.setDrawingCacheBackgroundColor(Color.BLUE);
	    LayoutInflater inf=getLayoutInflater();
	    inf.inflate(R.layout.menudemo, tab.getTabContentView());
	  
	    Bundle name=getIntent().getExtras();
	    names=name.getString("username");
	    System.out.println(names);
	    /**
	     * ���а�ť��ʼ��
	     */
	    tianjia1=(Button) findViewById(R.id.tianjia1);
	    xiugai1=(Button) findViewById(R.id.xiugai1);
	    shanchu1=(Button) findViewById(R.id.shanchu1);
	    chaxun1=(Button) findViewById(R.id.chaxun1);
	    tianjia2=(Button) findViewById(R.id.tianjia2);
	    xiugai2=(Button) findViewById(R.id.xiugai2);
	    shanchu2=(Button) findViewById(R.id.shanchu2);
	    chaxun2=(Button) findViewById(R.id.chaxun2);
	    tianjia3=(Button) findViewById(R.id.tianjia3);
	    xiugai3=(Button) findViewById(R.id.xiugai3);
	    shanchu3=(Button) findViewById(R.id.shanchu3);
	    chaxun3=(Button) findViewById(R.id.chaxun3);
	    tianjia4=(Button) findViewById(R.id.tianjia4);
	    xiugai4=(Button) findViewById(R.id.xiugai4);
	    shanchu4=(Button) findViewById(R.id.shanchu4);
	    chaxun4=(Button) findViewById(R.id.chaxun4);
	    tianjia5=(Button) findViewById(R.id.tianjia5);
	    xiugai5=(Button) findViewById(R.id.xiugai5);
	    shanchu5=(Button) findViewById(R.id.shanchu5);
	    chaxun5=(Button) findViewById(R.id.chaxun5);
	    yonghu=(Button) findViewById(R.id.yonghu);
	    mima=(Button) findViewById(R.id.mima);
	    if(!names.equals("ceshi")){
	    	System.out.println("111111111111111111");
	    yonghu.setVisibility(View.INVISIBLE);}
	    /**
	     * tabhost.tabspec����
	     */
	    final TabHost.TabSpec tabs1 = tab.newTabSpec("������Ϣ");
	    tabs1.setContent(R.id.li1);
		tabs1.setIndicator("������Ϣ", null);
		final TabHost.TabSpec tabs2 = tab.newTabSpec("������");
		   tabs2.setContent(R.id.li2);
		tabs2.setIndicator("������", null);
		 final TabHost.TabSpec tabs3 = tab.newTabSpec("��Ϣ��ѯ");
		   tabs3.setContent(R.id.li3);
		tabs3.setIndicator("��Ϣ��ѯ", null);
		 final TabHost.TabSpec tabs4 = tab.newTabSpec("����");
		    tabs4.setContent(R.id.li4);
			tabs4.setIndicator("����", null);
			 final TabHost.TabSpec tabs5 = tab.newTabSpec("�û�����");
			   
	    tabs5.setContent(R.id.li5);
		tabs5.setIndicator("�û�����", null);
		tab.addTab(tabs1);
		tab.addTab(tabs2);
		tab.addTab(tabs3);
		tab.addTab(tabs5);
		tab.addTab(tabs4);
		
	}
	/**
	 * ��Ʒ��Ϣ��ť����
	 * @param v
	 */
	public void onshangpin(View v){
		tianjia1.setVisibility(View.VISIBLE);
		xiugai1.setVisibility(View.VISIBLE);
		shanchu1.setVisibility(View.VISIBLE);
		chaxun1.setVisibility(View.VISIBLE);
		tianjia2.setVisibility(View.INVISIBLE);
		xiugai2.setVisibility(View.INVISIBLE);
		shanchu2.setVisibility(View.INVISIBLE);
		chaxun2.setVisibility(View.INVISIBLE);
		tianjia3.setVisibility(View.INVISIBLE);
		xiugai3.setVisibility(View.INVISIBLE);
		shanchu3.setVisibility(View.INVISIBLE);
		chaxun3.setVisibility(View.INVISIBLE);
	}
	
	/**
	 * ��Ӧ����Ϣ��ť����
	 * @param v
	 */
	public void ongongyingshang(View v){
		tianjia3.setVisibility(View.VISIBLE);
		xiugai3.setVisibility(View.VISIBLE);
		shanchu3.setVisibility(View.VISIBLE);
		chaxun3.setVisibility(View.VISIBLE);
		tianjia2.setVisibility(View.INVISIBLE);
		xiugai2.setVisibility(View.INVISIBLE);
		shanchu2.setVisibility(View.INVISIBLE);
		chaxun2.setVisibility(View.INVISIBLE);
		tianjia1.setVisibility(View.INVISIBLE);
		xiugai1.setVisibility(View.INVISIBLE);
		shanchu1.setVisibility(View.INVISIBLE);
		chaxun1.setVisibility(View.INVISIBLE);
	}

	/**
	 * �ͻ���Ϣ��ť����
	 * @param v
	 */
	public void onkehu(View v){
		tianjia2.setVisibility(View.VISIBLE);
		xiugai2.setVisibility(View.VISIBLE);
		shanchu2.setVisibility(View.VISIBLE);
		chaxun2.setVisibility(View.VISIBLE);
		tianjia1.setVisibility(View.INVISIBLE);
		xiugai1.setVisibility(View.INVISIBLE);
		shanchu1.setVisibility(View.INVISIBLE);
		chaxun1.setVisibility(View.INVISIBLE);
		tianjia3.setVisibility(View.INVISIBLE);
		xiugai3.setVisibility(View.INVISIBLE);
		shanchu3.setVisibility(View.INVISIBLE);
		chaxun3.setVisibility(View.INVISIBLE);
	}

	/**
	 * ��Ʒ�����Ϣ��ť����
	 * @param v
	 */
	public void onruku(View v){
		tianjia4.setVisibility(View.VISIBLE);
		xiugai4.setVisibility(View.VISIBLE);
		shanchu4.setVisibility(View.VISIBLE);
		chaxun4.setVisibility(View.VISIBLE);
		tianjia5.setVisibility(View.INVISIBLE);
		xiugai5.setVisibility(View.INVISIBLE);
		shanchu5.setVisibility(View.INVISIBLE);
		chaxun5.setVisibility(View.INVISIBLE);
		
	}

	/**
	 * ��Ʒ������Ϣ��ť����
	 * @param v
	 */
	public void onchuku(View v){
		tianjia5.setVisibility(View.VISIBLE);
		xiugai5.setVisibility(View.VISIBLE);
		shanchu5.setVisibility(View.VISIBLE);
		chaxun5.setVisibility(View.VISIBLE);
		tianjia4.setVisibility(View.INVISIBLE);
		xiugai4.setVisibility(View.INVISIBLE);
		shanchu4.setVisibility(View.INVISIBLE);
		chaxun4.setVisibility(View.INVISIBLE);
		
	}
	/**
	 * ��ӹ�Ӧ�̰�ť����
	 * @param v
	 */
	public void tianjiag(View v){
		Intent intent=new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("username",names);
		intent.putExtras(bundle);
		intent.setClass(getApplicationContext(), Tianjiag.class);
		startActivity(intent);
	
	}
	/**
	 * ��ѯ��Ӧ�̰�ť����
	 * @param v
	 */
	public void chaxung(View v){
		Intent intent=new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("username",names);
		intent.putExtras(bundle);
		intent.setClass(getApplicationContext(), Chaxung.class);
		startActivity(intent);
	
	}
	/**
	 * ��������Ϣ��ť����
	 * @param v
	 */
	public void tianjiar(View v){
		
		Intent intent=new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("username",names);
		intent.putExtras(bundle);
		intent.setClass(getApplicationContext(), Tianjiar.class);
		startActivity(intent);
		
	}
	/**
	 * ��ѯ�����Ϣ��ť����
	 * @param v
	 */
	public void chaxunruku(View v){
		
		
		Intent intent=new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("username",names);
		intent.putExtras(bundle);
		intent.setClass(getApplicationContext(), Chaxunr.class);
		startActivity(intent);
		
	}
	/**
	 * �����Ʒ��Ϣ��ť����
	 * @param v
	 */
	public void ontianjias(View v){
		Intent intent=new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("username",names);
		intent.putExtras(bundle);
		intent.setClass(getApplicationContext(), Tianjias.class);
		startActivity(intent);
		
	}
	/**
	 * ��ӿͻ���Ϣ��ť����
	 * @param v
	 */
	public void tianjiak(View v){
		Intent intent=new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("username",names);
		intent.putExtras(bundle);
		intent.setClass(getApplicationContext(), Tianjiak.class);
		startActivity(intent);
	
	}
	/**
	 * ��ѯ�ͻ���Ϣ��ť����
	 * @param v
	 */
	public void chaxunk(View v){
		Intent intent=new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("username",names);
		intent.putExtras(bundle);
		intent.setClass(getApplicationContext(), Chaxunk.class);
		startActivity(intent);
	
	}
	/**
	 * ��ӳ��ⰴť����
	 * @param v
	 */
	public void tianjiac(View v){
		Intent intent=new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("username",names);
		intent.putExtras(bundle);
		intent.setClass(getApplicationContext(), Tianjiac.class);
		startActivity(intent);
	}
	/**
	 * ��ѯ���ⰴť����
	 * @param v
	 */
	public void chaxunc(View v){
		Intent intent=new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("username",names);
		intent.putExtras(bundle);
		intent.setClass(getApplicationContext(), Chaxunc.class);
		startActivity(intent);
	}
	/**
	 * �޸����밴ť����
	 * @param v
	 */
	
	public void mima(View v){
		Intent intent=new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("username",names);
		intent.putExtras(bundle);
		intent.setClass(getApplicationContext(),Xiugai.class);
		startActivity(intent);
	}
	/**
	 * �û�����ť����
	 * @param v
	 */
	public void yonghu(View v){
		
		Intent intent=new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("username",names);
		intent.putExtras(bundle);
		intent.setClass(getApplicationContext(),Yonghu.class);
		startActivity(intent);
	}
	/**
	 * ɾ����Ʒ��ť����
	 * @param v
	 */
	public void shanchus(View v){
		Intent intent=new Intent();
		intent.setClass(MenuDemo.this, Shanchus.class);
		startActivity(intent);
	}
	/**
	 * ɾ���ͻ���ť���� 
	 * @param v
	 */
	 
	 
	public void shanchuk(View v){
		Intent intent=new Intent();
		intent.setClass(MenuDemo.this, Shanchuk.class);
		startActivity(intent);
	}
	/**
	 * ��ѯ��Ʒ��ť����
	 * @param v
	 */
	public void chaxuns(View v){
		Intent intent=new Intent();
		intent.setClass(MenuDemo.this, Chaxuns.class);
		startActivity(intent);
	}
	/**
	 * ɾ����Ӧ�̰�ť����
	 * @param v
	 */
	public void shanchug(View v){
		Intent intent=new Intent();
		intent.setClass(MenuDemo.this, Shanchug.class);
		startActivity(intent);
	}
	/**
	 * �޸���Ʒ��ť����
	 * @param v
	 */
	public void xiugais(View v){
		Intent intent=new Intent();
		intent.setClass(MenuDemo.this, Xiugais.class);
		startActivity(intent);
	}
	/**
	 * �޸Ŀͻ���ť����
	 * @param v
	 */
	public void xiugaik(View v){
		Intent intent=new Intent();
		intent.setClass(MenuDemo.this, Xiugaik.class);
		startActivity(intent);
	}
	/**
	 * �޸Ĺ�Ӧ�̰�ť����
	 * @param v
	 */
	public void xiugaig(View v){
		Intent intent=new Intent();
		intent.setClass(MenuDemo.this, Xiugaig.class);
		startActivity(intent);
	}
	/**
	 * ɾ�����ⰴť����
	 * @param v
	 */
	public void shanchuc(View v){
		Intent intent=new Intent();
		intent.setClass(MenuDemo.this, Shanchuc.class);
		startActivity(intent);
	}
	/**
	 * ɾ����ⰴť����
	 * @param v
	 */
	public void shanchur(View v){
		Intent intent=new Intent();
		intent.setClass(MenuDemo.this, Shanchur.class);
		startActivity(intent);
	}
	/**
	 * �޸���ⰴť����
	 * @param v
	 */
	public void xiugair(View v){
		Intent intent=new Intent();
		intent.setClass(MenuDemo.this, Xiugair.class);
		startActivity(intent);
	}
	/**
	 * �޸ĳ��ⰴť����
	 * @param v
	 */
	public void xiugaic(View v){
		Intent intent=new Intent();
		intent.setClass(MenuDemo.this, Xiugaic.class);
		startActivity(intent);
	}
	/**
	 * �����Ϣ��ѯ��ť����
	 * @param v
	 */
	public void kucun(View v){
		Intent intent=new Intent();
		intent.setClass(MenuDemo.this, Kucun.class);
		startActivity(intent);
	}
	
	
	
	

}
