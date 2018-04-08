package wyf.wpf;
import static wyf.wpf.ConstantUtil.SERVER_ADDRESS;
import static wyf.wpf.ConstantUtil.SERVER_PORT;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
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

public class MyDiaryActivity extends Activity{
	MyConnector mc = null;//
	ArrayList<String []> diaryList = new ArrayList<String []>();
	ListView lvDiary = null;			//声明ListView对象
	int positionToDelete = -1;
	String uno = null;		//记录用户ID
	Handler myHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:		
				lvDiary.setAdapter(ba);
				break;
			}
			super.handleMessage(msg);
		}
	};
	BaseAdapter ba = new BaseAdapter() {
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout ll = new LinearLayout(MyDiaryActivity.this);
			TextView tvTitle = new TextView(MyDiaryActivity.this);
			ll.setOrientation(LinearLayout.HORIZONTAL);
			ll.setGravity(Gravity.CENTER_VERTICAL);		//设置子控件的对齐方式
			LinearLayout llDiary = new LinearLayout(MyDiaryActivity.this);
			llDiary.setOrientation(LinearLayout.VERTICAL);
			tvTitle.setTextAppearance(MyDiaryActivity.this, R.style.title);
			tvTitle.setGravity(Gravity.LEFT);			//设置TextView的对齐方式
			tvTitle.setText(diaryList.get(position)[1]);
			TextView tvContent = new TextView(MyDiaryActivity.this);
			tvContent.setTextAppearance(MyDiaryActivity.this, R.style.content);
			tvContent.setGravity(Gravity.LEFT);		//设置TextView的对齐方式
			String content = diaryList.get(position)[2];
			int i = (content.length()>8?8:content.length());
			tvContent.setText(content.substring(0,i)+"...");		//设置显示的内容
			llDiary.addView(tvTitle);
			llDiary.addView(tvContent);
			ll.addView(llDiary);			//添加到总线性布局中
			LinearLayout llButton = new LinearLayout(MyDiaryActivity.this);
			llButton.setOrientation(LinearLayout.HORIZONTAL);		//设置布局方式
			llButton.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			llButton.setGravity(Gravity.RIGHT);
			Button btnEditDiary = new Button(MyDiaryActivity.this);			//创建编辑按钮
			btnEditDiary.setTextAppearance(MyDiaryActivity.this, R.style.button);
			btnEditDiary.setLayoutParams(new LinearLayout.LayoutParams(60, LayoutParams.WRAP_CONTENT));
			btnEditDiary.setText(R.string.btnEdit);
			btnEditDiary.setId(position);	//设置Button的ID
			btnEditDiary.setOnClickListener(listenerToEdit);		//设置按钮的监听器
			Button btnDeleteDiary = new Button(MyDiaryActivity.this);			//创建删除按钮
			btnDeleteDiary.setTextAppearance(MyDiaryActivity.this, R.style.button);
			btnDeleteDiary.setLayoutParams(new LinearLayout.LayoutParams(60, LayoutParams.WRAP_CONTENT));
			btnDeleteDiary.setText(R.string.btnDelete);
			btnDeleteDiary.setId(position);	//设置Button的ID
			btnDeleteDiary.setOnClickListener(listenerToDelete);		//设置按钮的监听器
			llButton.addView(btnEditDiary);
			llButton.addView(btnDeleteDiary);
			ll.addView(llButton);
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
			return diaryList.size();
		}
	};
	View.OnClickListener listenerToEdit = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MyDiaryActivity.this,ModifyDiaryActivity.class);
			int pos = v.getId();
			String [] data = diaryList.get(pos);
			intent.putExtra("diary_info", data);
			intent.putExtra("uno", uno);
			startActivity(intent);
		}
	};
	View.OnClickListener listenerToDelete = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			positionToDelete = v.getId();		//获得ID
			new AlertDialog.Builder(MyDiaryActivity.this)
				.setTitle("提示")
				.setIcon(R.drawable.alert_icon)
				.setMessage("确认删除该篇日志？")
				.setPositiveButton(
						"确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								deleteDiary();
							}
						})
				.setNegativeButton(
						"取消", 
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {}
						}
						).show();
				
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		uno = intent.getStringExtra("uno");
		setContentView(R.layout.diary);		//设置当前屏幕
		lvDiary = (ListView)findViewById(R.id.lvDiary);
		getDiaryList();
	}
	public void deleteDiary(){
		new Thread(){
			public void run(){
				Looper.prepare();
				try{
					if(mc == null){
						mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
					}
					String rid = diaryList.get(positionToDelete)[0];
					String msg = "<#DELETE_DIARY#>"+rid;
					mc.dout.writeUTF(msg);
					String reply = mc.din.readUTF();		//读取返回信息
					if(reply.equals("<#DELETE_DIARY_SUCCESS#>")){			//删除成功
						Toast.makeText(MyDiaryActivity.this, "删除日志成功！", Toast.LENGTH_LONG).show();
						getDiaryList();
						Looper.loop();
					}
					else{			//删除失败
						Toast.makeText(MyDiaryActivity.this, "删除失败，请重试！", Toast.LENGTH_LONG).show();
						Looper.loop();
					}
					
				}catch(Exception e){
					e.printStackTrace();
				}
				Looper.myLooper().quit();
			}
		}.start();
	}
	//方法：获取日志列表
	public void getDiaryList(){
		new Thread(){
			public void run(){
				try{
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
					mc.dout.writeUTF("<#GET_DIARY#>"+uno+"|"+"1");
					int size = mc.din.readInt();		//读取日志的长度
					diaryList = null;
					diaryList = new ArrayList<String []>();		//初始化diaryLsit
					for(int i=0;i<size;i++){					//循环接受日志信息
						String diaryInfo = mc.din.readUTF();		//读取日志信息
						String [] sa = diaryInfo.split("\\|");
						diaryList.add(sa);				//将日志信息添加到列表中
					}
					myHandler.sendEmptyMessage(0);
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