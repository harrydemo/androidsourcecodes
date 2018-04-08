package com.lequ.Base;


import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MyToast extends Dialog
{
	private static final int DISMISS = 1;
	private static final int DURATE = 5;
	private Handler mHandler;
	private OnClickListener mListener;

	public MyToast(Context paramContext, int paramInt, OnClickListener paramOnClickListener)
	{
		super(paramContext, paramInt);
		MyToastMessageHandler local1 = new MyToastMessageHandler();
		this.mHandler = local1;
		this.mListener = paramOnClickListener;
		setContentView(2130903042);
		ImageView localImageView = (ImageView)findViewById(2131165205);
		LocalImageListener local2 = new LocalImageListener();
		localImageView.setOnTouchListener(local2);
		TextView localTextView = (TextView)findViewById(2131165206);
		Spanned localSpanned = Html.fromHtml(paramContext.getString(2130968630));
		localTextView.setText(localSpanned);
		LocalTextViewListener local3 = new LocalTextViewListener();
		localTextView.setOnTouchListener(local3);
		setCancelable(true);
	}

	public MyToast(Context paramContext, OnClickListener paramOnClickListener)
	{
		this(paramContext, 2131034112, paramOnClickListener);
	}

	public void dismiss()
	{
		super.dismiss();
		this.mHandler.removeMessages(1);
	}

	public void show()
	{
		super.show();
		this.mHandler.sendEmptyMessageDelayed(1, 5000L);
	}

	public abstract interface OnClickListener
	{
		public abstract void onClick(MotionEvent paramMotionEvent);
	}


	class MyToastMessageHandler extends Handler
	{
		public void handleMessage(Message paramMessage)
		{
			switch (paramMessage.what){
			   default:
			   case 1:
				   dismiss();
				   break;
			}
		}
	}

	class LocalImageListener implements View.OnTouchListener  {
		public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
		{
//			if (MyToast.access$0(this.this$0) != null)
//				MyToast.access$0(this.this$0).onClick(paramMotionEvent);
			mHandler.sendEmptyMessageDelayed(1, 1000L);
			return true;
		}
	}

	class LocalTextViewListener implements View.OnTouchListener  {
		public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
		{
//			if (MyToast.access$0(this.this$0) != null)
//				MyToast.access$0(this.this$0).onClick(paramMotionEvent);
			mHandler.sendEmptyMessageDelayed(1, 1000L);
			return true;
		}
	}
}
