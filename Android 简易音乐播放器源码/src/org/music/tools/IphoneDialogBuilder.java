package org.music.tools;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;

public class IphoneDialogBuilder extends AlertDialog.Builder {
     private IphoneDialog md;
     private Context context;
     
	public IphoneDialogBuilder(Context context) {
		super(context);
		md=new IphoneDialog(context);
		this.context=context;
		
	}
	public IphoneDialogBuilder setMessage(int messageId) {
		md.setMessage(context.getResources().getString(messageId));
		return this;
	}

	public IphoneDialogBuilder setMessage(CharSequence message) {
		md.setMessage(message);
		return this;
	}

	public IphoneDialogBuilder setTitle(int titleId) {
		md.setTitle(context.getResources().getString(titleId));
		return this;
	}

	public IphoneDialogBuilder setTitle(CharSequence title) {
		md.setTitle(title);
		return this;
	}

	// 认同按钮
	public IphoneDialogBuilder setPositiveButton(int textId,
			OnClickListener listener) {
		md.setButton(context.getResources().getString(textId), listener);
		return this;
	}

	// 认同按钮
	public IphoneDialogBuilder setPositiveButton(CharSequence text,
			OnClickListener listener) {
		md.setButton(text, listener);
		return this;
	}

	// 中立按钮
	public IphoneDialogBuilder setNeutralButton(int textId,
			OnClickListener listener) {
		md.setButton2(context.getResources().getString(textId), listener);
		return this;
	}

	// 中立按钮
	public IphoneDialogBuilder setNeutralButton(CharSequence text,
			OnClickListener listener) {
		md.setButton2(text, listener);
		return this;
	}

	// 否定按钮
	public IphoneDialogBuilder setNegativeButton(int textId,
			OnClickListener listener) {
		md.setButton3(context.getResources().getString(textId), listener);
		return this;
	}

	// 否定按钮
	public IphoneDialogBuilder setNegativeButton(CharSequence text,
			OnClickListener listener) {
		md.setButton3(text, listener);
		return this;
	}
	@Override
	public IphoneDialog create() {
		return md;
	}

}
