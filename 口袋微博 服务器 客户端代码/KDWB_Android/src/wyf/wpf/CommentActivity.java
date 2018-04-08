package wyf.wpf;
import static wyf.wpf.ConstantUtil.SERVER_ADDRESS;
import static wyf.wpf.ConstantUtil.SERVER_PORT;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CommentActivity extends Activity{
	MyConnector mc = null;
	String visitor = null;		//������ID
	String uno = null;			//������ID
	String rid = null;			//�ռǵ�ID
	List<String []> cmtList;	//������۵��б�
	BaseAdapter baListView = new BaseAdapter(){
		@Override
		public int getCount() {
			return cmtList.size();
		}
		@Override
		public Object getItem(int position) {
			return null;
		}
		@Override
		public long getItemId(int position) {
			return 0;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout ll = new LinearLayout(CommentActivity.this);
			ll.setOrientation(LinearLayout.VERTICAL);
			TextView tvName = new TextView(CommentActivity.this);
			tvName.setGravity(Gravity.LEFT);
			tvName.setTextAppearance(CommentActivity.this, R.style.title);
			String [] cmt = cmtList.get(position);		//���ָ����������Ϣ
			tvName.setText(cmt[1]+"-��"+cmt[3]+"˵��");
			TextView tvContent = new TextView(CommentActivity.this);
			tvContent.setTextAppearance(CommentActivity.this, R.style.content);
			tvContent.setText(cmt[2]);			//������������
			ll.addView(tvName);
			ll.addView(tvContent);
			return ll;
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		visitor = intent.getStringExtra("visitor");
		uno = intent.getStringExtra("uno");
		rid = intent.getStringExtra("rid");
		initComments();
		setContentView(R.layout.comment);
		ListView lvComment = (ListView)findViewById(R.id.lvComent);
		lvComment.setAdapter(baListView);
		Button btnComment = (Button)findViewById(R.id.btnComment);
		btnComment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText etComment = (EditText)findViewById(R.id.etComentContent);
				String content = etComment.getEditableText().toString().trim();	//���Ʒ������
				if(content.equals("")){		//δ������������
					Toast.makeText(CommentActivity.this, "��������������", Toast.LENGTH_LONG).show();
					return;
				}
				if(visitor == null){
					makeNewComment(content,rid,uno);
				}
				else if(visitor != null){
					makeNewComment(content,rid,visitor);
				}
				initComments();
				baListView.notifyDataSetChanged();
			}
		});
		Button btnCommentBack = (Button)findViewById(R.id.btnCommentBack);		//��÷��ذ�ť����
		btnCommentBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	//��������ȡ�����б�
	public void initComments(){
		cmtList = new ArrayList<String []>();
		if(mc == null){
			mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
		}
		try{
			mc.dout.writeUTF("<#GET_COMMENT#>"+rid);		//������ȡ��������
			int size = mc.din.readInt();			//��ȡ���۵ĸ���	
			System.out.println("the number of comment is :"+size);
			mc.dout.writeUTF("<#READY_TO_READ_COMMENT#>");
			for(int i=0;i<size;i++){
				String msg = mc.din.readUTF();		//��ȡÿ��������Ϣ
				String [] sa = msg.split("\\|");	//�и��ַ���
				cmtList.add(sa);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	//����������������
	public void makeNewComment(String content,String rid,String uno){
		try{
			if(mc == null){
				mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
			}
			mc.dout.writeUTF("<#NEW_COMMENT#>"+content+"|"+rid+"|"+uno);
			String reply = mc.din.readUTF();		//��ȡ�ظ�
			if(reply.equals("<#NEW_COMMENT_SUCESS#>")){				//�����ظ�
				Toast.makeText(CommentActivity.this, "������ӳɹ���", Toast.LENGTH_LONG).show();
			}
			else{
				Toast.makeText(CommentActivity.this, "�������ʧ�ܣ�", Toast.LENGTH_LONG).show();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
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