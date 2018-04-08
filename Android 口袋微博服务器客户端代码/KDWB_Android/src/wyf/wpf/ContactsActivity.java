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
	String uno = null;			//记录当前用户的id
	int type = -1;				//为0表示显示好友列表，为1表示显示访客列表
	Bitmap [] headList = null;	//存放头像的数组
	ArrayList<String []> infoList = null;	//存放联系人信息的列表如果是好友则为id、姓名、email、状态、头像。若为访客则为id、姓名、日期、头像
	MyConnector mc = null;		//网络连接器对象
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
		Intent intent = getIntent();			//获得启动该Activity的Intent对象
		uno = intent.getStringExtra("uno");		//获得当前用户的id
		type = intent.getIntExtra("type", -1);
		if(type == 0){			//好友列表
			baContacts = new BaseAdapter() {
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					LinearLayout ll = new LinearLayout(ContactsActivity.this);		//创建线性布局
					ll.setOrientation(LinearLayout.HORIZONTAL);
					ImageView iv = new ImageView(ContactsActivity.this);			//创建ImageView对象
					iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
					iv.setImageBitmap(headList[position]);		//设置头像
					iv.setLayoutParams(new LinearLayout.LayoutParams(HEAD_WIDTH, HEAD_HEIGHT));
					LinearLayout ll2 = new LinearLayout(ContactsActivity.this);		//创建子线性布局
					ll2.setOrientation(LinearLayout.VERTICAL);
					TextView tvName = new TextView(ContactsActivity.this);			//创建用于显示姓名的TextView
					tvName.setText(infoList.get(position)[1]);						//设置TextView的内容
					tvName.setTextSize(20.5f);										//设置字体大小
					tvName.setTextColor(Color.BLUE);								//设置字体颜色
					tvName.setPadding(5, 0, 0, 0);									//设置边界空白
					TextView tvStatus = new TextView(ContactsActivity.this);		//创建用于显示心情的TextView
					tvStatus.setTextSize(18.0f);										//设置这字体大小
					tvStatus.setTextAppearance(ContactsActivity.this, R.style.content);
					tvStatus.setPadding(5, 0, 0, 0);							
					tvStatus.setText("心情:"+infoList.get(position)[3]);				//设置TextView内容
					ll2.addView(tvName);											//将显示姓名的TextView添加到线性布局
					ll2.addView(tvStatus);											//将显示心情的TextView添加到线性布局
					ll.addView(iv);													//将显示头像的ImageView添加到现象布局
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
		else if(type == 1){			//访客列表
			baContacts = new BaseAdapter() {
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					LinearLayout ll = new LinearLayout(ContactsActivity.this);		//创建线性布局
					ll.setOrientation(LinearLayout.HORIZONTAL);
					ImageView iv = new ImageView(ContactsActivity.this);			//创建ImageView对象
					iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
					iv.setImageBitmap(headList[position]);		//设置头像
					iv.setLayoutParams(new LinearLayout.LayoutParams(HEAD_WIDTH, HEAD_HEIGHT));
					LinearLayout ll2 = new LinearLayout(ContactsActivity.this);		//创建子线性布局
					ll2.setOrientation(LinearLayout.VERTICAL);
					TextView tvName = new TextView(ContactsActivity.this);			//创建用于显示姓名的TextView
					tvName.setText(infoList.get(position)[1]);						//设置TextView的内容
					tvName.setTextSize(20.5f);										//设置字体大小
					tvName.setTextColor(Color.BLUE);								//设置字体颜色
					tvName.setPadding(5, 0, 0, 0);									//设置边界空白
					TextView tvDate = new TextView(ContactsActivity.this);		//创建用于显示心情的TextView
					tvDate.setTextSize(18.0f);										//设置这字体大小
					tvDate.setTextAppearance(ContactsActivity.this, R.style.content);
					tvDate.setPadding(5, 0, 0, 0);							
					tvDate.setText(infoList.get(position)[2]);				//设置TextView内容
					ll2.addView(tvName);											//将显示姓名的TextView添加到线性布局
					ll2.addView(tvDate);											//将显示心情的TextView添加到线性布局
					ll.addView(iv);													//将显示头像的ImageView添加到现象布局
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
		lv = (ListView)findViewById(R.id.listFriend);		//获得ListView对象的引用
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
	//方法：获取联系人列表
	public void getContact(){
		new Thread(){
			public void run(){
				try{
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);	//创建MyConnector对象
					mc.dout.writeUTF(messageHead[type]+uno);		//向服务器发出请求	
					int size = mc.din.readInt();					//读取列表的长度
					headList = null;
					infoList = null;
					headList = new Bitmap[size];					//初始化好友头像列表
					infoList = new ArrayList<String []>(size);		//初始化好友信息列表
					for(int i=0;i<size;i++){			//循环，获取每个好友的信息和头像
						String fInfo = mc.din.readUTF();		//读取好友信息
						String [] sa = fInfo.split("\\|");		//分割字符串
						infoList.add(sa);						//将好友信息添加到相应的列表中
						int headSize = mc.din.readInt();		//读取头像大小
						byte[] buf = new byte[headSize];			//创建缓冲区
						mc.din.read(buf);						//读取头像信息
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