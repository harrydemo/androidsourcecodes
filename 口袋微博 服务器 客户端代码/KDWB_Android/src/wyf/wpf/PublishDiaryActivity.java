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
		Button btnDiary = (Button)findViewById(R.id.btnDiary);			//��÷�����־��ť
		btnDiary.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pd = ProgressDialog.show(PublishDiaryActivity.this, "���Ժ�", "���ڷ�����־...",true,true);
				publishDiary();							//������־
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
	//���������ӷ�������������־
	public void publishDiary(){
		new Thread(){
			public void run(){
				Looper.prepare();
				EditText etTitle = (EditText)findViewById(R.id.etTitle);		//����ռǱ���EditText����
				EditText etDiary = (EditText)findViewById(R.id.etDiary);				//����ռ�����EditText����
				String title = etTitle.getEditableText().toString().trim();		//����ռǱ���
				String diary = etDiary.getEditableText().toString().trim();		//����ռ�����
				if(title.equals("") || diary.equals("")){			//������������Ϊ��
					Toast.makeText(PublishDiaryActivity.this, "�뽫�ռǵı����������д����", Toast.LENGTH_LONG).show();
					return;
				}
				try{
					if(mc == null){
						mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);		//����һ��Socket����
					}
					String message = "<#NEW_DIARY#>" + title+"|"+diary+"|"+uno;
					mc.dout.writeUTF(message);				//������Ϣ
					String reply = mc.din.readUTF();		//������Ϣ
					pd.dismiss();
					if(reply.equals("<#DIARY_SUCCESS#>")){			//�����־�����ɹ�
						pd.dismiss();						//�رս��ȶԻ���
						Toast.makeText(PublishDiaryActivity.this, "��־�����ɹ������ڸ�����־�в鿴", Toast.LENGTH_LONG).show();
						Looper.loop();
					}
					else if(reply.equals("<#DIARY_FAIL#>")){		//�����־����ʧ��
						pd.dismiss();					//�رս��ȶԻ���
						Toast.makeText(PublishDiaryActivity.this, "��־����ʧ�ܣ����Ժ����ԣ�", Toast.LENGTH_LONG).show();
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
			mc.sayBye();		//����MyConnector��sayBye����
		}
		super.onDestroy();
	}
	
	
}