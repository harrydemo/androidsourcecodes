package org.music.tools;

import org.app.music.R;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
/**
 * 苹果的对话框
 *
 */
public class IphoneDialog extends AlertDialog {
	private IphoneDialogView view;
	private LayoutInflater mInflater;
	private Context context;
	
	protected IphoneDialog(Context context) {
		super(context);
		this.context=context;
		mInflater=LayoutInflater.from(this.context);
		view=(IphoneDialogView)mInflater.inflate(R.layout.dialog_iphone, null);
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(view);
	}

	@Override
	public void setMessage(CharSequence message) {
		view.setMessage(message);
	}

	@Override
	public void setTitle(CharSequence title) {
		view.setTitle(title);
	}

	@Override
	public void setButton(CharSequence text, final OnClickListener listener) {
		final Button button=(Button)view.findViewById(R.id.dialog_yes);
		button.setText(text);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				listener.onClick(IphoneDialog.this, 0);
				dismiss();
				
			}
		});
		super.setButton(text, listener);
	}

	@Override
	public void setButton2(CharSequence text, final OnClickListener listener) {
		final Button button=(Button)view.findViewById(R.id.dialog_no);
		button.setText(text);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				listener.onClick(IphoneDialog.this, 0);
				dismiss();
				
			}
		});
		super.setButton2(text, listener);
	}

}
