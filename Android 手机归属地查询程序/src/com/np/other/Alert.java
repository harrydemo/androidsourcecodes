package com.np.other;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import android.view.View;

import com.wb.np.R;



public class Alert {
	public static short LEFT = 0;
	public static short RIGHT = 1;
	private Builder builder;
	private AlertListener alertListener;
	int blockKeyCode[] = {};
	
	private int iconId;
	private String title;
	private String message;
	private String buttonLeft;
	private String buttonRight;
	private View view;
	private boolean isTypeView = false;
	public Alert(Context context){  
		builder = new AlertDialog.Builder(context);
	}
	public void setBlockKeyCode(int[] blockKeyCode) {
		this.blockKeyCode = blockKeyCode;
	}
	
	private void same(int iconId,String title,String buttonLeft,String buttonRight){
		this.iconId = iconId;
		this.title = title;
		this.buttonLeft = buttonLeft;
		this.buttonRight = buttonRight;
	}
	public void setBuilder(int iconId,String title,String message,String buttonLeft,String buttonRight){
		same(iconId,title,buttonLeft,buttonRight);
		this.message = message;
		isTypeView = false;
		show();
	}
	
	public void setBuilder(int iconId,String title,View view,String buttonLeft,String buttonRight){
		same(iconId,title,buttonLeft,buttonRight);
		this.view = view;
		isTypeView = true;
		show();
	}
	
	public void show(){
		if(iconId == 0)
			builder.setIcon(R.drawable.dialog);
		else
			builder.setIcon(iconId);
		if(title == null)
			builder.setTitle("温馨提示");
		else
			builder.setTitle(title);
		if(isTypeView && view != null){
			builder.setView(view);
		}else{
			builder.setMessage(message);
		}
		if(buttonLeft == null){
			buttonLeft = "确定";
		}
		builder.setPositiveButton(buttonLeft,new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if(alertListener != null)
							alertListener.setOnListenetr(LEFT);
					}
				});
		
		if(buttonRight != null){
			builder.setNegativeButton(buttonRight,new android.content.DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							alertListener.setOnListenetr(RIGHT);
						}
					});
		}
		
		builder.setOnKeyListener(new OnKeyListener(){
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event){
				if(blockKeyCode.length > 0){
					for(int i=0;i < blockKeyCode.length;i++){
						if(keyCode == blockKeyCode[i])
							return true;
					}
				}
				return false;
			}
		});
		builder.create().show();
	}
	
	public void revert(){
		iconId = 0x00;
		view = null;
		title = null;
		message = null;
		buttonLeft = null;
		buttonRight = null;
		isTypeView = false;
	}

	public void setAlertListenet(AlertListener alertListener){  
		this.alertListener = alertListener;
	}
	public interface AlertListener{
		public void setOnListenetr(int button);
	}
}
