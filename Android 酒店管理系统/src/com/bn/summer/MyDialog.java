package com.bn.summer;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class MyDialog extends Dialog {

	public MyDialog(Context context) {
		super(context,R.style.FullHeightDialog);
		// TODO Auto-generated constructor stub
	}
	
	public void onCreate(Bundle savedInstanceState)
	{
		this.setContentView(R.layout.dialog_date_input);
	}
	public String toString()
	{
		return "MyDialog";
	}

}
