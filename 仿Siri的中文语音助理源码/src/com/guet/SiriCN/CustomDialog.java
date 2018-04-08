/*
 *  http://www.guet.edu.cn/
 *  by hmg25 20111212
 *  Just For Learning
 */
package com.guet.SiriCN;

import com.hmg.SiriCN.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class CustomDialog extends Dialog implements OnClickListener {
	public static final int DIALOG_DETAILS = 0;
	public static final int DIALOG_CHECK = 1;
	public static final int DIALOG_DOWNLOAD = 2;

	private Context mContext;
	private Button mBtn_done;
	private Button mBtn_cancel;
	private TextView mTextView;
	private int mType;

	public CustomDialog(Context context, int type,String title, String info) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		mType = type;
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		switch (type) {
		case DIALOG_DETAILS:
			showDetailsDialog(info,title);
			break;
		case DIALOG_DOWNLOAD:
			new AlertDialog.Builder(mContext)
			.setTitle("注意").setMessage(info)
			.setIcon(android.R.drawable.ic_dialog_info)
			.setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			});
		
		default:
			break;
		}
	}

	 private void showDetailsDialog(String info,String title) {
	     this.setContentView(R.layout.details_dialog); 
	     TextView titleView=(TextView)this.findViewById(R.id.details_title_bar);
	     titleView.setText(title);
	     mTextView = (TextView) this.findViewById(R.id.details_text);
	     mTextView.setText(info);
	       
	     mBtn_done = (Button) this.findViewById(R.id.btn_ok);
	     mBtn_done.setEnabled(true);
	     mBtn_done.setText("返回");
	     mBtn_done.setOnClickListener(this);
	     mBtn_cancel= (Button) this.findViewById(R.id.btn_cancel);
	     mBtn_cancel.setVisibility(View.GONE);
	    }
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_cancel:
		case R.id.btn_ok:
			dismiss();
			break;

		default:
			break;
		}
	}
}
