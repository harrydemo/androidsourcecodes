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
	MyConnector mc = null;						//����ͨ�ŵ�MyConnector��������
	Bitmap [] headArray = null;					//ͷ��ͼƬ����
	List<String []> searchResult = null;		//��������б��ַ��������е���������Ϊ�û�ID,�ǳƣ����䣬���飬ͷ��ID
	ListView lvResult = null;					//��ʾ����б��ListView����
	String visitor = null;						//������ʾ�����ߣ���ʹ���������ܵ��û�ID
	Button btnGo = null;			//����Button����
	String keyword = null;
	BaseAdapter ba = new BaseAdapter() {
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout ll = new LinearLayout(SearchActivity.this);
			ll.setOrientation(LinearLayout.HORIZONTAL);			//���ò��ַ�ʽ
			ImageView ivHead = new ImageView(SearchActivity.this);
			ivHead.setLayoutParams(new LinearLayout.LayoutParams(HEAD_WIDTH, HEAD_HEIGHT));
			ivHead.setImageBitmap(headArray[position]);		//����ͷ��
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
		lvResult = (ListView)findViewById(R.id.lvSearchResult);		//���ListView����
		lvResult.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				Intent intent = new Intent(SearchActivity.this,HomePageActivity.class);	//����Intent
				intent.putExtra("uno", searchResult.get(position)[0]);			//��ָ�����û�ID��ӵ�Extra�ֶ���
				intent.putExtra("visitor", visitor);
				startActivity(intent);
			}
		});
		btnGo = (Button)findViewById(R.id.btnSearch);
		btnGo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText etKeyword = (EditText)findViewById(R.id.etKeyword);		//���EditText�ؼ�
				keyword = etKeyword.getEditableText().toString().trim();		//��ò�ѯ�ؼ���
				if(keyword.equals("")){		//����ؼ���Ϊ��
					Toast.makeText(SearchActivity.this, "��������Ҫ��ѯ�Ĺؼ���", Toast.LENGTH_LONG).show();
					return;
				}
				search();
			}
		});
		
	}
	//��������ѯ
	public void search(){
		new Thread(){
			public void run(){
				if(mc == null){
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
				}
				try{
					mc.dout.writeUTF("<#SEARCH_CONTACT#>"+keyword);			//������ѯ����
					int result = mc.din.readInt();		//��ȡ�������Ļظ�
					System.out.println("#### the number of result:"+result);
					if(result == 0){
						Toast.makeText(SearchActivity.this, "�Բ���û���ҵ���ص�΢���û���", Toast.LENGTH_LONG).show();
						return;
					}
					else{				//���������Ϊ��
//						mc.dout.writeUTF("<#READY_TO_READ_SEARCH#>");				//�����������׼���ý������ݵ���Ϣ
						searchResult = new ArrayList<String []>(result);		//������Ų�ѯ������ݵ�List
						headArray = new Bitmap[result];							//����ͷ������
						for(int i=0;i<result;i++){
							String msg = mc.din.readUTF();			//��ȡ��������������Ϣ
							
							System.out.println("@@@@ one search result is"+msg);
							String [] sa = msg.split("\\|");		//�и��ַ���
							searchResult.add(sa);					//���ַ���������ӵ���ѯ����б���
							int size = mc.din.readInt();			//��ȡͷ��ĳ���
							System.out.println("@@@@ the head size is :"+size);
							byte [] buf = new byte[size];			//����������ݵ��ֽ�����
							mc.din.read(buf);						//��ȡ�ֽ�����
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