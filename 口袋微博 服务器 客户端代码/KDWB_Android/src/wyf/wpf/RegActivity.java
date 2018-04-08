package wyf.wpf;

import static wyf.wpf.ConstantUtil.SERVER_ADDRESS;
import static wyf.wpf.ConstantUtil.SERVER_PORT;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class RegActivity extends Activity{
	MyConnector mc = null;			//����MyConnector����
	String uno = "";				//��¼�û���ID
	ProgressDialog pd= null;		//�������ȶԻ���
	Handler myHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:		
				View linearLayout = (LinearLayout)findViewById(R.id.regResult);	//������Բ���
				linearLayout.setVisibility(View.VISIBLE);		//���ÿɼ���
				EditText etUno = (EditText)findViewById(R.id.etUno);
				etUno.setText(uno);		
				break;
			}
			super.handleMessage(msg);
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reg);							//���õ�ǰ��Ļ
		Button btnReg = (Button)findViewById(R.id.btnReg);		//���ע��Button��ť����
		btnReg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pd = ProgressDialog.show(RegActivity.this, "���Ժ�...", "�������ӷ�����...", false);
				register();
			}
		});
		Button btnBack = (Button)findViewById(R.id.btnBack);				//��÷��ذ�ť����
		btnBack.setOnClickListener(new View.OnClickListener() {				//Ϊ���ذ�ť��Ӽ�����
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RegActivity.this,LoginActivity.class);	//����Intent����
				startActivity(intent);										//����Activity
				finish();
			}
		});
		Button btnEnter = (Button)findViewById(R.id.btnEnter);				//��ý��빦����尴ť����
		btnEnter.setOnClickListener(new View.OnClickListener() {			//Ϊ���빦�����İ�ť��Ӽ�����
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RegActivity.this,FunctionTabActivity.class);	//����Intent
				intent.putExtra("uno", uno);		//����Extra�ֶ�
				startActivity(intent);				//����FunctionTab
				finish();							//�رո�Activity
			}
		});
	}
	//���������ӷ�����������ע��
	public void register(){
		new Thread(){
			public void run(){
				Looper.prepare();
				//����û���������ݲ�������֤
				EditText etName = (EditText)findViewById(R.id.etName);			//����ǳ�EditText����
				EditText etPwd1 = (EditText)findViewById(R.id.etPwd1);			//�������EditText����
				EditText etPwd2 = (EditText)findViewById(R.id.etPwd2);			//���ȷ������EditText����
				EditText etEmail = (EditText)findViewById(R.id.etEmail);		//�������EditText����
				EditText etStatus = (EditText)findViewById(R.id.etStatus);		//�������EditText����
				String name = etName.getEditableText().toString().trim();		//����ǳ�
				String pwd1 = etPwd1.getEditableText().toString().trim();		//�������
				String pwd2 = etPwd2.getEditableText().toString().trim();		//���ȷ������
				String email = etEmail.getEditableText().toString().trim();		//�������
				String status = etStatus.getEditableText().toString().trim();	//���״̬
				if(name.equals("") || pwd1.equals("") || pwd2.equals("") || email.equals("") || status.equals("")){
					Toast.makeText(RegActivity.this, "�뽫ע����Ϣ��д����", Toast.LENGTH_LONG).show();
					return;
				}
				if(!pwd1.equals(pwd2)){				//�ж���������������Ƿ�һ��
					Toast.makeText(RegActivity.this, "������������벻һ�£�", Toast.LENGTH_LONG).show();
					return;
				}
				try{
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
					String regInfo = "<#REGISTER#>"+name+"|"+pwd1+"|"+email+"|"+status;
					mc.dout.writeUTF(regInfo);
					String result = mc.din.readUTF();
					pd.dismiss();
					if(result.startsWith("<#REG_SUCCESS#>")){		//������ϢΪע��ɹ�
						result= result.substring(15);		//ȥ����Ϣͷ
						uno = result;				//��¼�û���ID
						myHandler.sendEmptyMessage(0);				//����Handler��Ϣ
						Toast.makeText(RegActivity.this, "ע��ɹ���", Toast.LENGTH_LONG).show();
					}
					else{		//ע��ʧ��
						Toast.makeText(RegActivity.this, "ע��ʧ�ܣ������ԣ�", Toast.LENGTH_LONG).show();
					}
				}
				catch(Exception e){
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