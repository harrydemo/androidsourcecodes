package wyf.wpf;
import static wyf.wpf.ConstantUtil.*;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ContactsActivity extends Activity{
	String uno = null;			//��¼��ǰ�û���id
	int type = -1;				//Ϊ0��ʾ��ʾ�����б�Ϊ1��ʾ��ʾ�ÿ��б�
	Bitmap [] headList = null;	//���ͷ�������
	ArrayList<String []> infoList = null;	//�����ϵ����Ϣ���б�����Ǻ�����Ϊid��������email��״̬��ͷ����Ϊ�ÿ���Ϊid�����������ڡ�ͷ��
	MyConnector mc = null;		//��������������
	ListView lv = null;
	String [] messageHead = {"<#FRIEND_LIST#>","<#VISITOR_LIST#>"};
	
	BaseAdapter baContacts=null; 
	Handler myHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:		
				lv.setAdapter(baContacts);
				break;
			}
			super.handleMessage(msg);
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();			//���������Activity��Intent����
		uno = intent.getStringExtra("uno");		//��õ�ǰ�û���id
		type = intent.getIntExtra("type", -1);
		if(type == 0){			//�����б�
			baContacts = new BaseAdapter() {
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					LinearLayout ll = new LinearLayout(ContactsActivity.this);		//�������Բ���
					ll.setOrientation(LinearLayout.HORIZONTAL);
					ImageView iv = new ImageView(ContactsActivity.this);			//����ImageView����
					iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
					iv.setImageBitmap(headList[position]);		//����ͷ��
					iv.setLayoutParams(new LinearLayout.LayoutParams(HEAD_WIDTH, HEAD_HEIGHT));
					LinearLayout ll2 = new LinearLayout(ContactsActivity.this);		//���������Բ���
					ll2.setOrientation(LinearLayout.VERTICAL);
					TextView tvName = new TextView(ContactsActivity.this);			//����������ʾ������TextView
					tvName.setText(infoList.get(position)[1]);						//����TextView������
					tvName.setTextSize(20.5f);										//���������С
					tvName.setTextColor(Color.BLUE);								//����������ɫ
					tvName.setPadding(5, 0, 0, 0);									//���ñ߽�հ�
					TextView tvStatus = new TextView(ContactsActivity.this);		//����������ʾ�����TextView
					tvStatus.setTextSize(18.0f);										//�����������С
					tvStatus.setTextAppearance(ContactsActivity.this, R.style.content);
					tvStatus.setPadding(5, 0, 0, 0);							
					tvStatus.setText("����:"+infoList.get(position)[3]);				//����TextView����
					ll2.addView(tvName);											//����ʾ������TextView��ӵ����Բ���
					ll2.addView(tvStatus);											//����ʾ�����TextView��ӵ����Բ���
					ll.addView(iv);													//����ʾͷ���ImageView��ӵ����󲼾�
					ll.addView(ll2);
					return ll;
				}
				@Override
				public long getItemId(int position) {
					return 0;
				}
				@Override
				public Object getItem(int position) {
					return null;
				}
				@Override
				public int getCount() {
					return headList.length;
				}
			};
		}
		else if(type == 1){			//�ÿ��б�
			baContacts = new BaseAdapter() {
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					LinearLayout ll = new LinearLayout(ContactsActivity.this);		//�������Բ���
					ll.setOrientation(LinearLayout.HORIZONTAL);
					ImageView iv = new ImageView(ContactsActivity.this);			//����ImageView����
					iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
					iv.setImageBitmap(headList[position]);		//����ͷ��
					iv.setLayoutParams(new LinearLayout.LayoutParams(HEAD_WIDTH, HEAD_HEIGHT));
					LinearLayout ll2 = new LinearLayout(ContactsActivity.this);		//���������Բ���
					ll2.setOrientation(LinearLayout.VERTICAL);
					TextView tvName = new TextView(ContactsActivity.this);			//����������ʾ������TextView
					tvName.setText(infoList.get(position)[1]);						//����TextView������
					tvName.setTextSize(20.5f);										//���������С
					tvName.setTextColor(Color.BLUE);								//����������ɫ
					tvName.setPadding(5, 0, 0, 0);									//���ñ߽�հ�
					TextView tvDate = new TextView(ContactsActivity.this);		//����������ʾ�����TextView
					tvDate.setTextSize(18.0f);										//�����������С
					tvDate.setTextAppearance(ContactsActivity.this, R.style.content);
					tvDate.setPadding(5, 0, 0, 0);							
					tvDate.setText(infoList.get(position)[2]);				//����TextView����
					ll2.addView(tvName);											//����ʾ������TextView��ӵ����Բ���
					ll2.addView(tvDate);											//����ʾ�����TextView��ӵ����Բ���
					ll.addView(iv);													//����ʾͷ���ImageView��ӵ����󲼾�
					ll.addView(ll2);
					return ll;
				}
				@Override
				public long getItemId(int position) {
					return 0;
				}
				@Override
				public Object getItem(int position) {
					return null;
				}
				@Override
				public int getCount() {
					return headList.length;
				}
			};
		}
		setContentView(R.layout.contacts);
		lv = (ListView)findViewById(R.id.listFriend);		//���ListView���������
		getContact();
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				Intent intent = new Intent(ContactsActivity.this,HomePageActivity.class);
				intent.putExtra("uno", infoList.get(position)[0]);
				intent.putExtra("visitor", uno);
				startActivity(intent);
			}
		});
	}
	//��������ȡ��ϵ���б�
	public void getContact(){
		new Thread(){
			public void run(){
				try{
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);	//����MyConnector����
					mc.dout.writeUTF(messageHead[type]+uno);		//���������������	
					int size = mc.din.readInt();					//��ȡ�б�ĳ���
					headList = null;
					infoList = null;
					headList = new Bitmap[size];					//��ʼ������ͷ���б�
					infoList = new ArrayList<String []>(size);		//��ʼ��������Ϣ�б�
					for(int i=0;i<size;i++){			//ѭ������ȡÿ�����ѵ���Ϣ��ͷ��
						String fInfo = mc.din.readUTF();		//��ȡ������Ϣ
						String [] sa = fInfo.split("\\|");		//�ָ��ַ���
						infoList.add(sa);						//��������Ϣ��ӵ���Ӧ���б���
						int headSize = mc.din.readInt();		//��ȡͷ���С
						byte[] buf = new byte[headSize];			//����������
						mc.din.read(buf);						//��ȡͷ����Ϣ
						headList[i] = BitmapFactory.decodeByteArray(buf, 0, headSize);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				myHandler.sendEmptyMessage(0);
			}
		}.start();
	}
	@Override
	protected void onDestroy() {
		if(mc != null){
			mc.sayBye();
		}
		super.onDestroy();
	}
}