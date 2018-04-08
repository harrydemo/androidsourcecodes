package wyf.wpf;
import static wyf.wpf.ConstantUtil.HEAD_HEIGHT;
import static wyf.wpf.ConstantUtil.HEAD_WIDTH;
import static wyf.wpf.ConstantUtil.SERVER_ADDRESS;
import static wyf.wpf.ConstantUtil.SERVER_PORT;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SearchActivity extends Activity{
	MyConnector mc = null;						//负责通信的MyConnector对象引用
	Bitmap [] headArray = null;					//头像图片数组
	List<String []> searchResult = null;		//搜索结果列表，字符串数组中的内容依次为用户ID,昵称，邮箱，心情，头像ID
	ListView lvResult = null;					//显示结果列表的ListView对象
	String visitor = null;						//声明表示访问者，即使用搜索功能的用户ID
	Button btnGo = null;			//声明Button对象
	String keyword = null;
	BaseAdapter ba = new BaseAdapter() {
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout ll = new LinearLayout(SearchActivity.this);
			ll.setOrientation(LinearLayout.HORIZONTAL);			//设置布局方式
			ImageView ivHead = new ImageView(SearchActivity.this);
			ivHead.setLayoutParams(new LinearLayout.LayoutParams(HEAD_WIDTH, HEAD_HEIGHT));
			ivHead.setImageBitmap(headArray[position]);		//设置头像
			ll.addView(ivHead);
			LinearLayout ll2 = new LinearLayout(SearchActivity.this);
			ll2.setOrientation(LinearLayout.VERTICAL);
			TextView tvName = new TextView(SearchActivity.this);
			tvName.setTextAppearance(SearchActivity.this, R.style.title);
			tvName.setText(searchResult.get(position)[1]);
			ll2.addView(tvName);
			TextView tvStatus = new TextView(SearchActivity.this);
			tvStatus.setTextAppearance(SearchActivity.this, R.style.content);
			tvStatus.setText(searchResult.get(position)[3]);
			ll2.addView(tvStatus);
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
			return searchResult.size();
		}
	};
	Handler myHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:		
				lvResult.setAdapter(ba);
				break;
			}
			super.handleMessage(msg);
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		Intent i = getIntent();
		visitor = i.getStringExtra("visitor");
		lvResult = (ListView)findViewById(R.id.lvSearchResult);		//获得ListView对象
		lvResult.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				Intent intent = new Intent(SearchActivity.this,HomePageActivity.class);	//创建Intent
				intent.putExtra("uno", searchResult.get(position)[0]);			//将指定的用户ID添加到Extra字段中
				intent.putExtra("visitor", visitor);
				startActivity(intent);
			}
		});
		btnGo = (Button)findViewById(R.id.btnSearch);
		btnGo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText etKeyword = (EditText)findViewById(R.id.etKeyword);		//获得EditText控件
				keyword = etKeyword.getEditableText().toString().trim();		//获得查询关键字
				if(keyword.equals("")){		//如果关键字为空
					Toast.makeText(SearchActivity.this, "请先输入要查询的关键字", Toast.LENGTH_LONG).show();
					return;
				}
				search();
			}
		});
		
	}
	//方法：查询
	public void search(){
		new Thread(){
			public void run(){
				if(mc == null){
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
				}
				try{
					mc.dout.writeUTF("<#SEARCH_CONTACT#>"+keyword);			//发出查询请求
					int result = mc.din.readInt();		//读取服务器的回复
					System.out.println("#### the number of result:"+result);
					if(result == 0){
						Toast.makeText(SearchActivity.this, "对不起，没有找到相关的微博用户！", Toast.LENGTH_LONG).show();
						return;
					}
					else{				//搜索结果不为空
//						mc.dout.writeUTF("<#READY_TO_READ_SEARCH#>");				//向服务器发出准备好接受数据的消息
						searchResult = new ArrayList<String []>(result);		//创建存放查询结果数据的List
						headArray = new Bitmap[result];							//创建头像数组
						for(int i=0;i<result;i++){
							String msg = mc.din.readUTF();			//读取服务器发来的消息
							
							System.out.println("@@@@ one search result is"+msg);
							String [] sa = msg.split("\\|");		//切割字符串
							searchResult.add(sa);					//将字符串数组添加到查询结果列表中
							int size = mc.din.readInt();			//读取头像的长度
							System.out.println("@@@@ the head size is :"+size);
							byte [] buf = new byte[size];			//声明存放数据的字节数组
							mc.din.read(buf);						//读取字节数组
							headArray[i] = BitmapFactory.decodeByteArray(buf, 0, buf.length);	
						}
						myHandler.sendEmptyMessage(0);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
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