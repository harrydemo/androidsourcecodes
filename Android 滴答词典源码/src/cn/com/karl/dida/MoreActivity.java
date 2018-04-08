package cn.com.karl.dida;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MoreActivity extends Activity {

	private LinearLayout layoutSet;
	private LinearLayout layoutNumber;
	private LinearLayout layoutIdea;
	private LinearLayout version;
	private LinearLayout about;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.more);
		
		layoutSet=(LinearLayout) this.findViewById(R.id.layoutSet);
		layoutNumber=(LinearLayout) this.findViewById(R.id.layoutNumber);
		layoutIdea=(LinearLayout) this.findViewById(R.id.layoutIdea);
		version=(LinearLayout) this.findViewById(R.id.version);
		about=(LinearLayout) this.findViewById(R.id.about);
		layoutSet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));  
			}
		});
		layoutNumber.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 startActivity(new Intent(MoreActivity.this,TestsActivity.class));
			}
		});
		//�������
		layoutIdea.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			startActivity(new Intent(MoreActivity.this,SendMailActivity.class));
			}
		});
		version.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "��ǰ�޸��°汾", 1).show();
			
			}
		});
		about.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(MoreActivity.this);
				builder.setTitle("����");
				builder.setMessage("�δ��ֻ��ʵ�\n\n���ߣ�Karl\n��ϵ��ʽ��995316358\n");
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
		
		
		
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) {
	
			AlertDialog.Builder builder = new AlertDialog.Builder(
					MoreActivity.this);
			builder.setIcon(R.drawable.bee);
			builder.setTitle("��ȷ���˳���");
			builder.setPositiveButton("ȷ��",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							MoreActivity.this.finish();
							android.os.Process.killProcess(android.os.Process
									.myPid());
							android.os.Process.killProcess(android.os.Process
									.myTid());
							android.os.Process.killProcess(android.os.Process
									.myUid());
						}
					});
			builder.setNegativeButton("����",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							dialog.cancel();
						}
					});
			builder.show();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
}

