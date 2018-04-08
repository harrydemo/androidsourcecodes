package wyf.wpf;
import static wyf.wpf.ConstantUtil.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PublishActivity extends Activity{
	String [] items = {			//存放ListView中的选项内容
		"更新心情","发布日志","拍照上传"	
	};
	int [] imgIds = {
		R.drawable.p_status,
		R.drawable.p_diary,
		R.drawable.p_shoot
	};
	String uno = null;		//存放
	MyConnector mc = null;		//MyConnector对象引用
	ProgressDialog pd = null;	//ProgressDialog对象引用
	View dialog_view = null;		//心情
	BaseAdapter ba = new BaseAdapter() {
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout ll = new LinearLayout(PublishActivity.this);
			ll.setOrientation(LinearLayout.HORIZONTAL);
			ll.setGravity(Gravity.CENTER);
			ImageView iv = new ImageView(PublishActivity.this);			//创建ImageView对象
			iv.setAdjustViewBounds(true);
			iv.setImageResource(imgIds[position]);							//设置ImageView的
			ll.addView(iv);													//将ImageView添加到线性布局中
			TextView tv = new TextView(PublishActivity.this);
			tv.setPadding(10, 0, 0, 0);
			tv.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			tv.setTextAppearance(PublishActivity.this, R.style.title);
			tv.setText(items[position]);			//设置TextView显示的内容
			ll.addView(tv);
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
			return items.length;
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();		//获得启动该Activity的Intent
		uno = intent.getStringExtra("uno");
		setContentView(R.layout.publish);			//设置当前屏幕
		ListView lvPublish = (ListView)findViewById(R.id.lvPublish);		//获得ListView对象引用
		lvPublish.setAdapter(ba);
		lvPublish.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				switch(position){			//判断点击的是哪一个选项
				case 0:				//更新心情
					LayoutInflater li = LayoutInflater.from(PublishActivity.this);
					dialog_view = li.inflate(R.layout.publish_status, null);
					new AlertDialog.Builder(PublishActivity.this)
						.setTitle("更新心情")
						.setIcon(R.drawable.p_status)
						.setView(dialog_view)
						.setPositiveButton(
							"发表",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									pd = ProgressDialog.show(PublishActivity.this, "请稍候", "正在更新心情...",true,true);
									updateStatus();
								}
							})
						.setNegativeButton(
							"取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {}
							})
						.show();
					break;
				case 1:				//发表日志
					Intent intent1 = new Intent(PublishActivity.this,PublishDiaryActivity.class);//创建Intent
					intent1.putExtra("uno", uno);		//设置Intent的Extra字段
					startActivity(intent1);
					break;
				case 2:				//拍照上传
					Intent intent2 = new Intent(PublishActivity.this,ShootActivity.class);//创建Intent
					intent2.putExtra("uno", uno);		//设置Intent的Extra字段
					startActivity(intent2);
					break;
				}
			}
		});
	}
	//方法:连接服务器，更新心情
	public void updateStatus(){
		new Thread(){
			public void run(){
				Looper.prepare();
				EditText etStatus = (EditText)dialog_view.findViewById(R.id.etInputStatus);
				String status = etStatus.getEditableText().toString().trim();	//获得心情内容
				if(status.equals("")){		//如果输入的心情为空
					pd.dismiss();
					Toast.makeText(PublishActivity.this, "请输入更新的心情", Toast.LENGTH_LONG).show();//输出提示
					Looper.loop();
					return;
				}
				String message = "<#NEW_STATUS#>"+status+"|"+uno;
				if(mc == null){
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
				}
				try{
					mc.dout.writeUTF(message);				//向服务器发送更新的心情
					pd = ProgressDialog.show(PublishActivity.this, "正在连接网络", "请稍候",true);
					String reply = mc.din.readUTF();		//从服务器返回的回复
					pd.dismiss();
					if(reply.equals("<#STATUS_SUCCESS#>")){	//心情更新成功
						Toast.makeText(PublishActivity.this, "心情更新成功！", Toast.LENGTH_LONG).show();
						Looper.loop();
					}
					else {		//心情更新失败
						Toast.makeText(PublishActivity.this, "心情更新失败！", Toast.LENGTH_LONG).show();
						Looper.loop();
					}
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
		}
		super.onDestroy();
	}
	
}