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
	MyConnector mc = null;		//声明MyConnector对象
	ListView lvAlbumList = null;	//ListView对象的引用
	List<String []> albumInfoList = null;	//存放相册信息的List
	String albumInfoArray [] = null;		//存放相册信息的Array
	String uno = null;				//存放用户的ID
	int newAccess = -1;		//记录新设置的权限
	String [] accessOptions={
		"公开","好友可见","仅个人可见"	
	};
	int albumIndexToChange = -1;	//记录要更改权限的相册在信息列表中的索引
	String albumToChange = null;	//记录要更改权限的相册ID
	String accessToChange = null;	//记录要更改的权限
	BaseAdapter ba = new BaseAdapter() {
		@Override
		public View getView(int position, View arg1, ViewGroup arg2) {
			LinearLayout ll = new LinearLayout(MyAlbumListActivity.this);
			ll.setOrientation(LinearLayout.HORIZONTAL);
			ll.setGravity(Gravity.CENTER_VERTICAL);			//设置线性布局的分布方式
			TextView tvName = new TextView(MyAlbumListActivity.this);
			tvName.setTextAppearance(MyAlbumListActivity.this, R.style.title);
			tvName.setPadding(5, 0, 0, 0);
			tvName.setText(albumInfoList.get(position)[1]);		//设置TextView显示的内容
			tvName.setLayoutParams(new LinearLayout.LayoutParams(170, LayoutParams.WRAP_CONTENT));
			ll.addView(tvName);
			LinearLayout ll2 = new LinearLayout(MyAlbumListActivity.this);
			ll2.setOrientation(LinearLayout.HORIZONTAL);		//设置线性布局的分布方式
			ll2.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			ll2.setGravity(Gravity.RIGHT);
			Button btnSee = new Button(MyAlbumListActivity.this);	//创建查看相册照片的Button对象
			btnSee.setId(position);									//设置Button的ID
			btnSee.setText("查看");
			btnSee.setTextAppearance(MyAlbumListActivity.this, R.style.button);
			btnSee.setOnClickListener(listenerToDetail);			//设置Button的监听器
			btnSee.setLayoutParams(new LinearLayout.LayoutParams(60, LayoutParams.WRAP_CONTENT));
			ll2.addView(btnSee);
			Button btnChangeAccess = new Button(MyAlbumListActivity.this);	//创建修改相册权限的Button对象
			btnChangeAccess.setId(position);						//设置Button的ID
			btnChangeAccess.setText("修改权限");
			btnChangeAccess.setTextAppearance(MyAlbumListActivity.this, R.style.button);	//设置按钮样式
			btnChangeAccess.setOnClickListener(listenerToAcess);		//添加监听器
			btnChangeAccess.setLayoutParams(new LinearLayout.LayoutParams(80, LayoutParams.WRAP_CONTENT));//设置布局参数
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
	View.OnClickListener listenerToDetail = new View.OnClickListener() {	//点下查看按钮后触发的监听器
		@Override
		public void onClick(View v) {					//重写onClick方法
			Intent intent = new Intent(MyAlbumListActivity.this,AlbumActivity.class);	//创建Intent对象
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
		setContentView(R.layout.album_list);					//设置当前屏幕
		Intent intent = getIntent();			//获得启动该Activity的Intent
		uno = intent.getStringExtra("uno");		//获得Intent中的uno的值
		lvAlbumList = (ListView)findViewById(R.id.lvAlbumList);	//获得ListView对象
		getAlbumList();							//获得指定用户的相册列表
	}
	public void getAlbumList(){
		new Thread(){
			public void run(){
				Looper.prepare();
				try{
					if(mc == null){		//检查MyConnector对象是否为空
						mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);			//创建MyConnector对象
					}
					mc.dout.writeUTF("<#GET_ALBUM_LIST#>"+uno);					//发出获取相册列表请求
					String reply = mc.din.readUTF();					//读取相册列表
					if(reply.equals("<#NO_ALBUM#>")){							//判断相册列表是否为空
						Toast.makeText(MyAlbumListActivity.this, "您还没有上传过照片", Toast.LENGTH_LONG).show();
						Looper.loop();
						return;
					}
					albumInfoArray = reply.split("\\$");				//切割字符串
					albumInfoList = new ArrayList<String []>();
					for(String s:albumInfoArray){
						String [] sa = s.split("\\|");					//切割字符串
						albumInfoList.add(sa);
					}
					myHandler.sendEmptyMessage(0);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}.start();
	}
	//方法：显示修改权限对话框
	public void showMyDialog(){
		new AlertDialog.Builder(this)
		.setSingleChoiceItems(accessOptions, newAccess, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				newAccess = which;
			}
		})
		.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				accessToChange=newAccess+"";
				changeAlbumAccess();
			}
		}).show();
	}
	//方法：修改相册权限
	public int changeAlbumAccess(){
		int result = 0;
		new Thread(){
			public void run(){
				Looper.prepare();
				try{
					if(mc == null){
						mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);		//创建MyConnector
					}
					String msg = "<#CHANGE_ALBUM_ACCESS#>"+albumToChange+"|"+accessToChange;
					mc.dout.writeUTF(msg);
					String reply = mc.din.readUTF();		//接收服务器反馈
					if(reply.equals("<#ALBUM_ACCESS_SUCCESS#>")){				//更新权限成功
						Toast.makeText(MyAlbumListActivity.this, "相册权限更新成功！", Toast.LENGTH_LONG).show();
						Looper.loop();			//执行消息队列中的消息
					}
					else{								//更新权限失败
						Toast.makeText(MyAlbumListActivity.this, "相册权限更新失败！", Toast.LENGTH_LONG).show();
						Looper.loop();			//执行消息队列中的消息
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