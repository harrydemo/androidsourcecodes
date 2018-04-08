package com.cellcom;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//�Ի������
public class AlertDialogActivity extends Activity {

	private Button button1;
	private Button button2;
	private Button button3;
	private Button button4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_dialog);
		setTitle("4�ֶԻ���");
		
		button1=(Button)findViewById(R.id.button1);
		button2=(Button)findViewById(R.id.button2);
		button3=(Button)findViewById(R.id.button3);
		button4=(Button)findViewById(R.id.button4);
		
		//��һ�ֶԻ���
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Builder builder=new AlertDialog.Builder(AlertDialogActivity.this);
				builder.setIcon(R.drawable.alert_dialog_icon);
				builder.setTitle("�۹�����");
				builder.setMessage("ȥ��ȥ��");
				builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(AlertDialogActivity.this, "��ѡ����ȷ����ť��", Toast.LENGTH_SHORT).show();
					}
				});
				builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(AlertDialogActivity.this, "��ѡ����ȡ����ť��", Toast.LENGTH_SHORT).show();
					}
				});
				builder.show();
			}
		});
		
		//�ڶ��ֶԻ���
		button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(AlertDialogActivity.this)
				.setIcon(R.drawable.alert_dialog_icon)
				.setTitle("��ܰ��ʾ")
				.setMessage("��ʾ���ݣ�������ť")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(AlertDialogActivity.this, "��ѡ����ȷ����ť��", Toast.LENGTH_SHORT).show();
					}
				})
				.setNeutralButton("����", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(AlertDialogActivity.this, "��ѡ�������鰴ť��", Toast.LENGTH_SHORT).show();
					}
				})
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(AlertDialogActivity.this, "��ѡ����ȡ����ť��", Toast.LENGTH_SHORT).show();
					}
				})
				.show();
			}
		});
		
		//�����ְ�ť
		button3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LayoutInflater inflater=LayoutInflater.from(AlertDialogActivity.this);
				final View textEntryView=inflater.inflate(R.layout.alert_dialog_text_entry, null);
				
				final EditText usernameET=(EditText)textEntryView.findViewById(R.id.username_value);
				final EditText passwordET=(EditText)textEntryView.findViewById(R.id.password_value);
				//final String username=usernameET.getText().toString();
				
				new AlertDialog.Builder(AlertDialogActivity.this)
				.setIcon(R.drawable.alert_dialog_icon)
				.setTitle("��ܰ����")
				.setView(textEntryView)
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(AlertDialogActivity.this, "�û���="+usernameET.getText().toString()+"\n����="+passwordET.getText().toString(), Toast.LENGTH_LONG).show();
					}
				})
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(AlertDialogActivity.this, "��ѡ����ȷ��ȡ����", Toast.LENGTH_SHORT).show();
					}
				})
				.show();
			}
		});
		
		//�����ֶԻ���
		button4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ProgressDialog dialog=new ProgressDialog(AlertDialogActivity.this);
				dialog.setTitle("�����С�����");
				dialog.setMessage("���Ժ󡣡���");
				dialog.show();
			}
		});
	}
}
