package wyf.wpf;

import static wyf.wpf.ConstantUtil.*;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ModifyDiaryActivity extends Activity{
	MyConnector mc = null;
	String [] diaryInfo = null;	//记录日志信息
	EditText etModifyTitle = null;
	EditText etModifyContent = null;
	String uno = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		diaryInfo = intent.getStringArrayExtra("diary_info");
		uno = intent.getStringExtra("uno");
		setContentView(R.layout.modify_diary);
		etModifyTitle = (EditText)findViewById(R.id.etModifyTitle);		//获得标题EditText
		etModifyTitle.setText(diaryInfo[1]);
		etModifyContent = (EditText)findViewById(R.id.etModifyDiary);		//获得内容EditText
		etModifyContent.setText(diaryInfo[2]);
		Button btnModifyDiary = (Button)findViewById(R.id.btnModifyDiary);	//获得修改按钮
		btnModifyDiary.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String modifyTitle = etModifyTitle.getEditableText().toString().trim();
				String modifyContent = etModifyContent.getEditableText().toString().trim();
				if(modifyContent.equals("") || modifyTitle.equals("")){	//如果内容或标题为空
					Toast.makeText(ModifyDiaryActivity.this, "请将标题或内容填写完整！", Toast.LENGTH_LONG).show();
					return;
				}
				modifyDiary();
			}
		});
		Button btnModifyBack = (Button)findViewById(R.id.btnModifyDiaryBack);
		btnModifyBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ModifyDiaryActivity.this,FunctionTabActivity.class);
				intent.putExtra("uno", uno);
				intent.putExtra("tab", "tab4");
				startActivity(intent);
			}
		});
	}
	public void modifyDiary(){
		new Thread(){
			public void run(){
				Looper.prepare();
				try{
					if(mc == null){
						mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
					}
					String modifyTitle = etModifyTitle.getEditableText().toString().trim();
					String modifyContent = etModifyContent.getEditableText().toString().trim();
					String msg = "<#MODIFY_DIARY#>"+diaryInfo[0]+"|"+modifyTitle+"|"+modifyContent;
					mc.dout.writeUTF(msg);
					String reply = mc.din.readUTF();
					if(reply.equals("<#MODIFY_DIARY_SUCCESS#>")){	
						Toast.makeText(ModifyDiaryActivity.this, "日志修改成功！", Toast.LENGTH_LONG).show();
						Looper.loop();
					}
					else{
						Toast.makeText(ModifyDiaryActivity.this, "修改失败，请重试！", Toast.LENGTH_LONG).show();
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
		if(mc == null){
			mc.sayBye();
		}
		super.onDestroy();
	}
	
}