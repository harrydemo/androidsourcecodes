package com.mycommondialog.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CommonDialogActivity extends Activity {
	final int COMMON_DIALOG = 1;

	private Dialog dialog = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_dialog_layout);

		Button button = (Button) findViewById(R.id.button);
		View.OnClickListener listener = new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				showDialog(COMMON_DIALOG);
			}
		};
		button.setOnClickListener(listener);
	}

	/*
	 * 使用onCreateDialog(int)来管理对话框的状态, 那么每次对话框被解除时, 该对话框对象的状态会被Activity保存. 调用
	 * removeDialog(int)将所有该对象的内部引用移除 如本程序那样,如果不加removeDialog，那么显示的是第一次的内容
	 */
	@Override
	protected Dialog onCreateDialog(int id) {

		EditText editText = (EditText) findViewById(R.id.editText);
		switch (id) {
		case COMMON_DIALOG:
			Builder builder = new AlertDialog.Builder(this);
			builder.setIcon(R.drawable.dialog);
			builder.setTitle(R.string.button);
			builder.setMessage(editText.getText());
			DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// 将此处移除后，无论怎么修改EditText内容
					// 每次点击显示普通对话框，总是第一次的内容
					// 如果想更新EditText内容就得加removeDialog
					removeDialog(COMMON_DIALOG);
				}
			};
			builder.setPositiveButton(R.string.ok, listener);
			dialog = builder.create();
			break;
		default:
			break;
		}
		Log.e("onCreateDialog", "onCreateDialog");
		return dialog;
	}
}