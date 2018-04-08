package wyf.wpf;
import static wyf.wpf.ConstantUtil.SERVER_ADDRESS;
import static wyf.wpf.ConstantUtil.SERVER_PORT;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class DiaryActivity extends Activity{
	ArrayList<String []> diaryList = new ArrayList<String []>();
	MyConnector mc = null;
	String uno = null;
	String visitor = null;
	BaseAdapter ba = new BaseAdapter() {
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout ll = new LinearLayout(DiaryActivity.this);
			TextView tvTitle = new TextView(DiaryActivity.this);
			ll.setOrientation(LinearLayout.VERTICAL);
			tvTitle.setTextAppearance(DiaryActivity.this, R.style.title);
			tvTitle.setGravity(Gravity.LEFT);			//����TextView�Ķ��뷽ʽ
			tvTitle.setText(diaryList.get(position)[1]);
			TextView tvContent = new TextView(DiaryActivity.this);
			tvContent.setTextAppearance(DiaryActivity.this, R.style.content);	//���������С
			tvContent.setGravity(Gravity.LEFT);		//����TextView�Ķ��뷽ʽ
			tvContent.setText(diaryList.get(position)[2]);		//������ʾ������
			ll.addView(tvTitle);
			ll.addView(tvContent);
			return ll;
		}
		@Override
		public long getItemId(int position) {
			return 0;
		}
		@Override
		public Object getItem(int arg0) {
			return null;
		}
		@Override
		public int getCount() {
			return diaryList.size();
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		uno = intent.getStringExtra("uno");					//�������ID
		visitor = intent.getStringExtra("visitor");		//��÷ÿ�ID
		getDiaryList();
		setContentView(R.layout.diary);
		ListView lvDiary = (ListView)findViewById(R.id.lvDiary);		//���ListView����
		lvDiary.setAdapter(ba);
		lvDiary.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				Intent intent = new Intent(DiaryActivity.this,CommentActivity.class);
				intent.putExtra("rid", diaryList.get(position)[0]);
				intent.putExtra("uno", uno);
				intent.putExtra("visitor", visitor);
				startActivity(intent);
			}
		});
	}
	//��������ȡ��־�б�
	public void getDiaryList(){
		new Thread(){
			public void run(){
				try{
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
					mc.dout.writeUTF("<#GET_DIARY#>"+uno+"|"+"1");
					int size = mc.din.readInt();		//��ȡ��־�ĳ���
					for(int i=0;i<size;i++){	//ѭ��������־��Ϣ
						String diaryInfo = mc.din.readUTF();		//��ȡ��־��Ϣ
						String [] sa = diaryInfo.split("\\|");
						diaryList.add(sa);				//����־��Ϣ��ӵ��б���
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
			mc = null;
		}
		super.onDestroy();
	}
	
}