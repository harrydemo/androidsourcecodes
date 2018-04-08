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
	ListView lvDiary = null;			//����ListView����
	int positionToDelete = -1;
	String uno = null;		//��¼�û�ID
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
			ll.setGravity(Gravity.CENTER_VERTICAL);		//�����ӿؼ��Ķ��뷽ʽ
			LinearLayout llDiary = new LinearLayout(MyDiaryActivity.this);
			llDiary.setOrientation(LinearLayout.VERTICAL);
			tvTitle.setTextAppearance(MyDiaryActivity.this, R.style.title);
			tvTitle.setGravity(Gravity.LEFT);			//����TextView�Ķ��뷽ʽ
			tvTitle.setText(diaryList.get(position)[1]);
			TextView tvContent = new TextView(MyDiaryActivity.this);
			tvContent.setTextAppearance(MyDiaryActivity.this, R.style.content);
			tvContent.setGravity(Gravity.LEFT);		//����TextView�Ķ��뷽ʽ
			String content = diaryList.get(position)[2];
			int i = (content.length()>8?8:content.length());
			tvContent.setText(content.substring(0,i)+"...");		//������ʾ������
			llDiary.addView(tvTitle);
			llDiary.addView(tvContent);
			ll.addView(llDiary);			//��ӵ������Բ�����
			LinearLayout llButton = new LinearLayout(MyDiaryActivity.this);
			llButton.setOrientation(LinearLayout.HORIZONTAL);		//���ò��ַ�ʽ
			llButton.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			llButton.setGravity(Gravity.RIGHT);
			Button btnEditDiary = new Button(MyDiaryActivity.this);			//�����༭��ť
			btnEditDiary.setTextAppearance(MyDiaryActivity.this, R.style.button);
			btnEditDiary.setLayoutParams(new LinearLayout.LayoutParams(60, LayoutParams.WRAP_CONTENT));
			btnEditDiary.setText(R.string.btnEdit);
			btnEditDiary.setId(position);	//����Button��ID
			btnEditDiary.setOnClickListener(listenerToEdit);		//���ð�ť�ļ�����
			Button btnDeleteDiary = new Button(MyDiaryActivity.this);			//����ɾ����ť
			btnDeleteDiary.setTextAppearance(MyDiaryActivity.this, R.style.button);
			btnDeleteDiary.setLayoutParams(new LinearLayout.LayoutParams(60, LayoutParams.WRAP_CONTENT));
			btnDeleteDiary.setText(R.string.btnDelete);
			btnDeleteDiary.setId(position);	//����Button��ID
			btnDeleteDiary.setOnClickListener(listenerToDelete);		//���ð�ť�ļ�����
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
			positionToDelete = v.getId();		//���ID
			new AlertDialog.Builder(MyDiaryActivity.this)
				.setTitle("��ʾ")
				.setIcon(R.drawable.alert_icon)
				.setMessage("ȷ��ɾ����ƪ��־��")
				.setPositiveButton(
						"ȷ��",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								deleteDiary();
							}
						})
				.setNegativeButton(
						"ȡ��", 
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
		setContentView(R.layout.diary);		//���õ�ǰ��Ļ
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
					String reply = mc.din.readUTF();		//��ȡ������Ϣ
					if(reply.equals("<#DELETE_DIARY_SUCCESS#>")){			//ɾ���ɹ�
						Toast.makeText(MyDiaryActivity.this, "ɾ����־�ɹ���", Toast.LENGTH_LONG).show();
						getDiaryList();
						Looper.loop();
					}
					else{			//ɾ��ʧ��
						Toast.makeText(MyDiaryActivity.this, "ɾ��ʧ�ܣ������ԣ�", Toast.LENGTH_LONG).show();
						Looper.loop();
					}
					
				}catch(Exception e){
					e.printStackTrace();
				}
				Looper.myLooper().quit();
			}
		}.start();
	}
	//��������ȡ��־�б�
	public void getDiaryList(){
		new Thread(){
			public void run(){
				try{
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
					mc.dout.writeUTF("<#GET_DIARY#>"+uno+"|"+"1");
					int size = mc.din.readInt();		//��ȡ��־�ĳ���
					diaryList = null;
					diaryList = new ArrayList<String []>();		//��ʼ��diaryLsit
					for(int i=0;i<size;i++){					//ѭ��������־��Ϣ
						String diaryInfo = mc.din.readUTF();		//��ȡ��־��Ϣ
						String [] sa = diaryInfo.split("\\|");
						diaryList.add(sa);				//����־��Ϣ��ӵ��б���
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