package wyf.wpf;
import static wyf.wpf.ConstantUtil.SERVER_ADDRESS;
import static wyf.wpf.ConstantUtil.SERVER_PORT;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PublishDiaryActivity extends Activity{
	MyConnector mc = null;
	ProgressDialog pd = null;
	String uno = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		uno = intent.getStringExtra("uno");
		setContentView(R.layout.publish_diary);
		Button btnDiary = (Button)findViewById(R.id.btnDiary);			//获得发布日志按钮
		btnDiary.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pd = ProgressDialog.show(PublishDiaryActivity.this, "请稍候", "正在发布日志...",true,true);
				publishDiary();							//发表日志
			}
		});
		Button btnDiaryBack = (Button)findViewById(R.id.btnDiaryBack);
		btnDiaryBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	//方法：连接服务器，发表日志
	public void publishDiary(){
		new Thread(){
			public void run(){
				Looper.prepare();
				EditText etTitle = (EditText)findViewById(R.id.etTitle);		//获得日记标题EditText对象
				EditText etDiary = (EditText)findViewById(R.id.etDiary);				//获得日记内容EditText对象
				String title = etTitle.getEditableText().toString().trim();		//获得日记标题
				String diary = etDiary.getEditableText().toString().trim();		//获得日记内容
				if(title.equals("") || diary.equals("")){			//如果标题或内容为空
					Toast.makeText(PublishDiaryActivity.this, "请将日记的标题或内容填写完整", Toast.LENGTH_LONG).show();
					return;
				}
				try{
					if(mc == null){
						mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);		//创建一个Socket连接
					}
					String message = "<#NEW_DIARY#>" + title+"|"+diary+"|"+uno;
					mc.dout.writeUTF(message);				//发出消息
					String reply = mc.din.readUTF();		//接收消息
					pd.dismiss();
					if(reply.equals("<#DIARY_SUCCESS#>")){			//如果日志发布成功
						pd.dismiss();						//关闭进度对话框
						Toast.makeText(PublishDiaryActivity.this, "日志发布成功，请在个人日志中查看", Toast.LENGTH_LONG).show();
						Looper.loop();
					}
					else if(reply.equals("<#DIARY_FAIL#>")){		//如果日志发布失败
						pd.dismiss();					//关闭进度对话框
						Toast.makeText(PublishDiaryActivity.this, "日志发布失败，请稍候重试！", Toast.LENGTH_LONG).show();
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
			mc.sayBye();		//调用MyConnector的sayBye方法
		}
		super.onDestroy();
	}
	
	
}