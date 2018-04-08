package wyf.wpf;

import static wyf.wpf.ConstantUtil.*;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class MyAlbumListActivity extends Activity{
	MyConnector mc = null;		//����MyConnector����
	ListView lvAlbumList = null;	//ListView���������
	List<String []> albumInfoList = null;	//��������Ϣ��List
	String albumInfoArray [] = null;		//��������Ϣ��Array
	String uno = null;				//����û���ID
	int newAccess = -1;		//��¼�����õ�Ȩ��
	String [] accessOptions={
		"����","���ѿɼ�","�����˿ɼ�"	
	};
	int albumIndexToChange = -1;	//��¼Ҫ����Ȩ�޵��������Ϣ�б��е�����
	String albumToChange = null;	//��¼Ҫ����Ȩ�޵����ID
	String accessToChange = null;	//��¼Ҫ���ĵ�Ȩ��
	BaseAdapter ba = new BaseAdapter() {
		@Override
		public View getView(int position, View arg1, ViewGroup arg2) {
			LinearLayout ll = new LinearLayout(MyAlbumListActivity.this);
			ll.setOrientation(LinearLayout.HORIZONTAL);
			ll.setGravity(Gravity.CENTER_VERTICAL);			//�������Բ��ֵķֲ���ʽ
			TextView tvName = new TextView(MyAlbumListActivity.this);
			tvName.setTextAppearance(MyAlbumListActivity.this, R.style.title);
			tvName.setPadding(5, 0, 0, 0);
			tvName.setText(albumInfoList.get(position)[1]);		//����TextView��ʾ������
			tvName.setLayoutParams(new LinearLayout.LayoutParams(170, LayoutParams.WRAP_CONTENT));
			ll.addView(tvName);
			LinearLayout ll2 = new LinearLayout(MyAlbumListActivity.this);
			ll2.setOrientation(LinearLayout.HORIZONTAL);		//�������Բ��ֵķֲ���ʽ
			ll2.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			ll2.setGravity(Gravity.RIGHT);
			Button btnSee = new Button(MyAlbumListActivity.this);	//�����鿴�����Ƭ��Button����
			btnSee.setId(position);									//����Button��ID
			btnSee.setText("�鿴");
			btnSee.setTextAppearance(MyAlbumListActivity.this, R.style.button);
			btnSee.setOnClickListener(listenerToDetail);			//����Button�ļ�����
			btnSee.setLayoutParams(new LinearLayout.LayoutParams(60, LayoutParams.WRAP_CONTENT));
			ll2.addView(btnSee);
			Button btnChangeAccess = new Button(MyAlbumListActivity.this);	//�����޸����Ȩ�޵�Button����
			btnChangeAccess.setId(position);						//����Button��ID
			btnChangeAccess.setText("�޸�Ȩ��");
			btnChangeAccess.setTextAppearance(MyAlbumListActivity.this, R.style.button);	//���ð�ť��ʽ
			btnChangeAccess.setOnClickListener(listenerToAcess);		//��Ӽ�����
			btnChangeAccess.setLayoutParams(new LinearLayout.LayoutParams(80, LayoutParams.WRAP_CONTENT));//���ò��ֲ���
			ll2.addView(btnChangeAccess);
			ll.addView(ll2);
			return ll;
		}
		@Override
		public long getItemId(int arg0) {
			return 0;
		}
		@Override
		public Object getItem(int arg0) {
			return null;
		}
		@Override
		public int getCount() {
			return albumInfoList.size();
		}
	};
	View.OnClickListener listenerToDetail = new View.OnClickListener() {	//���²鿴��ť�󴥷��ļ�����
		@Override
		public void onClick(View v) {					//��дonClick����
			Intent intent = new Intent(MyAlbumListActivity.this,AlbumActivity.class);	//����Intent����
			intent.putExtra("uno", uno);
			intent.putExtra("albumlist", albumInfoArray);
			intent.putExtra("xid", albumInfoList.get(v.getId())[0]);
			intent.putExtra("position", v.getId());
			intent.putExtra("from", 0);
			startActivity(intent);
			finish();
		}
	};
	View.OnClickListener listenerToAcess = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			albumIndexToChange = v.getId();
			albumToChange = albumInfoList.get(v.getId())[0];
			newAccess = Integer.valueOf(albumInfoList.get(v.getId())[2]);
			showMyDialog();
		}
	};
	Handler myHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:		
				lvAlbumList.setAdapter(ba);
				break;
			}
			super.handleMessage(msg);
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.album_list);					//���õ�ǰ��Ļ
		Intent intent = getIntent();			//���������Activity��Intent
		uno = intent.getStringExtra("uno");		//���Intent�е�uno��ֵ
		lvAlbumList = (ListView)findViewById(R.id.lvAlbumList);	//���ListView����
		getAlbumList();							//���ָ���û�������б�
	}
	public void getAlbumList(){
		new Thread(){
			public void run(){
				Looper.prepare();
				try{
					if(mc == null){		//���MyConnector�����Ƿ�Ϊ��
						mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);			//����MyConnector����
					}
					mc.dout.writeUTF("<#GET_ALBUM_LIST#>"+uno);					//������ȡ����б�����
					String reply = mc.din.readUTF();					//��ȡ����б�
					if(reply.equals("<#NO_ALBUM#>")){							//�ж�����б��Ƿ�Ϊ��
						Toast.makeText(MyAlbumListActivity.this, "����û���ϴ�����Ƭ", Toast.LENGTH_LONG).show();
						Looper.loop();
						return;
					}
					albumInfoArray = reply.split("\\$");				//�и��ַ���
					albumInfoList = new ArrayList<String []>();
					for(String s:albumInfoArray){
						String [] sa = s.split("\\|");					//�и��ַ���
						albumInfoList.add(sa);
					}
					myHandler.sendEmptyMessage(0);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}.start();
	}
	//��������ʾ�޸�Ȩ�޶Ի���
	public void showMyDialog(){
		new AlertDialog.Builder(this)
		.setSingleChoiceItems(accessOptions, newAccess, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				newAccess = which;
			}
		})
		.setPositiveButton("ȷ��", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				accessToChange=newAccess+"";
				changeAlbumAccess();
			}
		}).show();
	}
	//�������޸����Ȩ��
	public int changeAlbumAccess(){
		int result = 0;
		new Thread(){
			public void run(){
				Looper.prepare();
				try{
					if(mc == null){
						mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);		//����MyConnector
					}
					String msg = "<#CHANGE_ALBUM_ACCESS#>"+albumToChange+"|"+accessToChange;
					mc.dout.writeUTF(msg);
					String reply = mc.din.readUTF();		//���շ���������
					if(reply.equals("<#ALBUM_ACCESS_SUCCESS#>")){				//����Ȩ�޳ɹ�
						Toast.makeText(MyAlbumListActivity.this, "���Ȩ�޸��³ɹ���", Toast.LENGTH_LONG).show();
						Looper.loop();			//ִ����Ϣ�����е���Ϣ
					}
					else{								//����Ȩ��ʧ��
						Toast.makeText(MyAlbumListActivity.this, "���Ȩ�޸���ʧ�ܣ�", Toast.LENGTH_LONG).show();
						Looper.loop();			//ִ����Ϣ�����е���Ϣ
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}.start();
		return result;
	}
	protected void onDestroy() {
		if(mc != null){
			mc.sayBye();
			mc = null;
		}
		super.onDestroy();
	}
	
}