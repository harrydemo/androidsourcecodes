package com.bn.summer;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class EXDialog extends Dialog{

	public EXDialog(Context context) {
		super(context,R.style.FullHeightDialog);
		// TODO Auto-generated constructor stub
	}
	public void onCreate(Bundle savedInstanceState)
	{
		this.setContentView(R.layout.dialog_ex);
	}
	public String toString()
	{
		return "MyDialog";
	}
}
