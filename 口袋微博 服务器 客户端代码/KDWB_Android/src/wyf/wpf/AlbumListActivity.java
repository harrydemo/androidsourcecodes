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
 * 该Activity的主要功能是显示好友的相册列表
 * 和MyAlbumActivity有所区别。
 */
public class AlbumListActivity extends Activity{
	MyConnector mc = null;
	ListView lvAlbumList = null;
	ArrayList<String []> albumList;		//存放好友相册信息的列表
	String [] albumArray;		//存放好友相册信息的数组
	String uno = null;					//存放被访问者的ID
	String visitor = null;				//存放访问者的ID
	BaseAdapter ba = new BaseAdapter() {
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv = new TextView(AlbumListActivity.this);
			tv.setTextSize(20.5f);		//设置字体大小
			tv.setGravity(Gravity.CENTER);
			tv.setText(albumList.get(position)[1]);	//设置TextView显示的内容
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
		setContentView(R.layout.album_list);		//设置当前屏幕
		lvAlbumList = (ListView)findViewById(R.id.lvAlbumList);	//获得ListView对象
		lvAlbumList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				Intent intent = new Intent(AlbumListActivity.this,AlbumActivity.class);	//创建Intent对象
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
					if(reply.equals("<#NO_ALBUM#>")){		//该用户无相册
						Toast.makeText(AlbumListActivity.this, "该用户还没有上传过照片", Toast.LENGTH_LONG).show();
						Looper.loop();
					}
					albumArray = reply.split("\\$");
					albumList = new ArrayList<String []>();
					for(String s:albumArray){	//解析每个相册的信息
						String [] sa = s.split("\\|");		//切割字符串
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