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
	String [] items = {			//���ListView�е�ѡ������
		"��������","������־","�����ϴ�"	
	};
	int [] imgIds = {
		R.drawable.p_status,
		R.drawable.p_diary,
		R.drawable.p_shoot
	};
	String uno = null;		//���
	MyConnector mc = null;		//MyConnector��������
	ProgressDialog pd = null;	//ProgressDialog��������
	View dialog_view = null;		//����
	BaseAdapter ba = new BaseAdapter() {
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout ll = new LinearLayout(PublishActivity.this);
			ll.setOrientation(LinearLayout.HORIZONTAL);
			ll.setGravity(Gravity.CENTER);
			ImageView iv = new ImageView(PublishActivity.this);			//����ImageView����
			iv.setAdjustViewBounds(true);
			iv.setImageResource(imgIds[position]);							//����ImageView��
			ll.addView(iv);													//��ImageView��ӵ����Բ�����
			TextView tv = new TextView(PublishActivity.this);
			tv.setPadding(10, 0, 0, 0);
			tv.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			tv.setTextAppearance(PublishActivity.this, R.style.title);
			tv.setText(items[position]);			//����TextView��ʾ������
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
		Intent intent = getIntent();		//���������Activity��Intent
		uno = intent.getStringExtra("uno");
		setContentView(R.layout.publish);			//���õ�ǰ��Ļ
		ListView lvPublish = (ListView)findViewById(R.id.lvPublish);		//���ListView��������
		lvPublish.setAdapter(ba);
		lvPublish.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				switch(position){			//�жϵ��������һ��ѡ��
				case 0:				//��������
					LayoutInflater li = LayoutInflater.from(PublishActivity.this);
					dialog_view = li.inflate(R.layout.publish_status, null);
					new AlertDialog.Builder(PublishActivity.this)
						.setTitle("��������")
						.setIcon(R.drawable.p_status)
						.setView(dialog_view)
						.setPositiveButton(
							"����",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									pd = ProgressDialog.show(PublishActivity.this, "���Ժ�", "���ڸ�������...",true,true);
									updateStatus();
								}
							})
						.setNegativeButton(
							"ȡ��",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {}
							})
						.show();
					break;
				case 1:				//������־
					Intent intent1 = new Intent(PublishActivity.this,PublishDiaryActivity.class);//����Intent
					intent1.putExtra("uno", uno);		//����Intent��Extra�ֶ�
					startActivity(intent1);
					break;
				case 2:				//�����ϴ�
					Intent intent2 = new Intent(PublishActivity.this,ShootActivity.class);//����Intent
					intent2.putExtra("uno", uno);		//����Intent��Extra�ֶ�
					startActivity(intent2);
					break;
				}
			}
		});
	}
	//����:���ӷ���������������
	public void updateStatus(){
		new Thread(){
			public void run(){
				Looper.prepare();
				EditText etStatus = (EditText)dialog_view.findViewById(R.id.etInputStatus);
				String status = etStatus.getEditableText().toString().trim();	//�����������
				if(status.equals("")){		//������������Ϊ��
					pd.dismiss();
					Toast.makeText(PublishActivity.this, "��������µ�����", Toast.LENGTH_LONG).show();//�����ʾ
					Looper.loop();
					return;
				}
				String message = "<#NEW_STATUS#>"+status+"|"+uno;
				if(mc == null){
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
				}
				try{
					mc.dout.writeUTF(message);				//����������͸��µ�����
					pd = ProgressDialog.show(PublishActivity.this, "������������", "���Ժ�",true);
					String reply = mc.din.readUTF();		//�ӷ��������صĻظ�
					pd.dismiss();
					if(reply.equals("<#STATUS_SUCCESS#>")){	//������³ɹ�
						Toast.makeText(PublishActivity.this, "������³ɹ���", Toast.LENGTH_LONG).show();
						Looper.loop();
					}
					else {		//�������ʧ��
						Toast.makeText(PublishActivity.this, "�������ʧ�ܣ�", Toast.LENGTH_LONG).show();
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