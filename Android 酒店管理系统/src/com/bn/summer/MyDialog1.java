package com.bn.summer;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class MyDialog1 extends Dialog{

	public MyDialog1(Context context) {
		super(context,R.style.FullHeightDialog);
	}
	public void onCreate(Bundle savedInstanceState)
	{
		this.setContentView(R.layout.dialog_date_input_1);
	}
	public String toString()
	{
		return "MyDialog";
	}

}
