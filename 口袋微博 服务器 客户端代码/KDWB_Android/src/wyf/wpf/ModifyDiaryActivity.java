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
	String [] diaryInfo = null;	//��¼��־��Ϣ
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
		etModifyTitle = (EditText)findViewById(R.id.etModifyTitle);		//��ñ���EditText
		etModifyTitle.setText(diaryInfo[1]);
		etModifyContent = (EditText)findViewById(R.id.etModifyDiary);		//�������EditText
		etModifyContent.setText(diaryInfo[2]);
		Button btnModifyDiary = (Button)findViewById(R.id.btnModifyDiary);	//����޸İ�ť
		btnModifyDiary.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String modifyTitle = etModifyTitle.getEditableText().toString().trim();
				String modifyContent = etModifyContent.getEditableText().toString().trim();
				if(modifyContent.equals("") || modifyTitle.equals("")){	//������ݻ����Ϊ��
					Toast.makeText(ModifyDiaryActivity.this, "�뽫�����������д������", Toast.LENGTH_LONG).show();
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
						Toast.makeText(ModifyDiaryActivity.this, "��־�޸ĳɹ���", Toast.LENGTH_LONG).show();
						Looper.loop();
					}
					else{
						Toast.makeText(ModifyDiaryActivity.this, "�޸�ʧ�ܣ������ԣ�", Toast.LENGTH_LONG).show();
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