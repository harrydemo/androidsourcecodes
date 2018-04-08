package wyf.wpf;

import static wyf.wpf.ConstantUtil.SERVER_ADDRESS;
import static wyf.wpf.ConstantUtil.SERVER_PORT;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
/*
 * ��Activity����Ҫ��������ʾ���ѵ�����б�
 * ��MyAlbumActivity��������
 */
public class AlbumListActivity extends Activity{
	MyConnector mc = null;
	ListView lvAlbumList = null;
	ArrayList<String []> albumList;		//��ź��������Ϣ���б�
	String [] albumArray;		//��ź��������Ϣ������
	String uno = null;					//��ű������ߵ�ID
	String visitor = null;				//��ŷ����ߵ�ID
	BaseAdapter ba = new BaseAdapter() {
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv = new TextView(AlbumListActivity.this);
			tv.setTextSize(20.5f);		//���������С
			tv.setGravity(Gravity.CENTER);
			tv.setText(albumList.get(position)[1]);	//����TextView��ʾ������
			tv.setTextAppearance(AlbumListActivity.this, R.style.title);
			return tv;
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
			return albumList.size();
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
		Intent intent = getIntent();
		uno = intent.getStringExtra("uno");
		visitor = intent.getStringExtra("visitor");
		setContentView(R.layout.album_list);		//���õ�ǰ��Ļ
		lvAlbumList = (ListView)findViewById(R.id.lvAlbumList);	//���ListView����
		lvAlbumList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				Intent intent = new Intent(AlbumListActivity.this,AlbumActivity.class);	//����Intent����
				intent.putExtra("uno", uno);
				intent.putExtra("visitor", visitor);
				intent.putExtra("albumlist", albumArray);
				intent.putExtra("xid", albumList.get(position)[0]);
				intent.putExtra("position", position);
				intent.putExtra("from", 1);
				startActivity(intent);
				finish();
			}
		});
		getAlbumListByAccess();
	}
	public void getAlbumListByAccess(){
		new Thread(){
			public void run(){
				Looper.prepare();
				try{
					if(mc == null){
						mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
					}
					mc.dout.writeUTF("<#GET_ALBUM_LIST_BY_ACCESS#>"+uno+"|"+visitor);
					String reply = mc.din.readUTF();
					System.out.println("-------getAlbumByAccess reply is:"+reply);
					if(reply.equals("<#NO_ALBUM#>")){		//���û������
						Toast.makeText(AlbumListActivity.this, "���û���û���ϴ�����Ƭ", Toast.LENGTH_LONG).show();
						Looper.loop();
					}
					albumArray = reply.split("\\$");
					albumList = new ArrayList<String []>();
					for(String s:albumArray){	//����ÿ��������Ϣ
						String [] sa = s.split("\\|");		//�и��ַ���
						albumList.add(sa);
					}
					myHandler.sendEmptyMessage(0);
				}catch(Exception e){
					e.printStackTrace();
				}
				Looper.myLooper().quit();
			}
		}.start();
	}
	@Override
	protected void onDestroy() {
		if(mc != null){
			mc.sayBye();
			mc = null;
		}
		super.onDestroy();
	}
	
}