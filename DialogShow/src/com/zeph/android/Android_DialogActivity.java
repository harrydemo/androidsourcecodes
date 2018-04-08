package com.zeph.android;

import com.example.dialogshow.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class Android_DialogActivity extends Activity
{
	/** Called when the activity is first created. */
	ProgressDialog p_dialog;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		AlertDialog dialog = new AlertDialog.Builder(
				Android_DialogActivity.this)
				.setTitle("��¼��ʾ")
				.setMessage("�Ƿ��¼")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// TODO Auto-generated method stub
						LayoutInflater factory = LayoutInflater
								.from(Android_DialogActivity.this);
						final View DialogView = factory.inflate(
								R.layout.dialog, null);
						AlertDialog dlg = new AlertDialog.Builder(
								Android_DialogActivity.this)
								.setTitle("��½��")
								.setView(DialogView)
								.setPositiveButton("ȷ��",
										new DialogInterface.OnClickListener()
										{

											@Override
											public void onClick(
													DialogInterface dialog,
													int which)
											{
												// TODO Auto-generated method
												// stub
												p_dialog = ProgressDialog
														.show(Android_DialogActivity.this,
																"��ȴ�",
																"����Ϊ����¼...",
																true);
												new Thread()
												{
													public void run()
													{
														try
														{
															sleep(3000);
														} catch (Exception e)
														{
															e.printStackTrace();
														} finally
														{
															p_dialog.dismiss();
														}
													}
												}.start();
											}
										})
								.setNegativeButton("ȡ��",
										new DialogInterface.OnClickListener()
										{

											@Override
											public void onClick(
													DialogInterface dialog,
													int which)
											{
												// TODO Auto-generated method
												// stub
												Android_DialogActivity.this
														.finish();
											}
										}).create();
						dlg.show();

					}
				})
				.setNegativeButton("�˳�", new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// TODO Auto-generated method stub
						Android_DialogActivity.this.finish();
					}
				}).create();
		dialog.show();
	}
}