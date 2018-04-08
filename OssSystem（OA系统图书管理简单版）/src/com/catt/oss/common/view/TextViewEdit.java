package com.catt.oss.common.view;

import java.util.zip.Inflater;

import com.catt.oss.R;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class TextViewEdit extends LinearLayout {
	private EditText et_content;
	private TextView tv_content;
	private LayoutInflater  inflater;

	public TextViewEdit(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public TextViewEdit(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public String getText() {
		if (et_content==null)
		    return null;
		else
			return et_content.getText().toString();
	}

	public void setText(String text) {
		if (et_content!=null)
			et_content.setText(text);
	}
	public TextViewEdit(Context context,String content,DisplayMetrics dm) {
		super(context);
		this.setOrientation(HORIZONTAL);
		inflater = LayoutInflater.from(context);
		/*inflater = (LayoutInflater) context
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);*/
		View view=inflater.inflate(R.layout.directory_common, null);
		tv_content=(TextView) view.findViewById(R.id.createtextview);
		String result=tv_content.getText().toString();
		tv_content.setText(content);
		tv_content.setGravity(Gravity.LEFT);
		et_content = (EditText) view.findViewById(R.id.createedittext);
	/*	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
            params.height = dm.heightPixels / 12;
            
            params.width = dm.widthPixels/4;
       LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
           		 
           		 LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            
            params2.height = dm.heightPixels / 12;
            
            params2.width = dm.widthPixels/2;
            tv_content.setLayoutParams(params);
            et_content.setLayoutParams(params2);*/
            addView(tv_content,new LinearLayout.LayoutParams(dm.widthPixels/3, dm.heightPixels/12));
            addView(et_content,new LinearLayout.LayoutParams(dm.widthPixels/4, dm.heightPixels/12));
	}
	public TextViewEdit(Context context,String content,String res,DisplayMetrics dm) {
		super(context);
		this.setOrientation(HORIZONTAL);
		tv_content=new TextView(context);
		tv_content.setText(content);
		tv_content.setTextColor(Color.BLACK);
		tv_content.setGravity(Gravity.CENTER);
		addView(tv_content,new LinearLayout.LayoutParams(dm.widthPixels/3, dm.heightPixels/12));
		et_content = new EditText(context);
		et_content.setText(res);
		et_content.setTextColor(Color.BLACK);
		addView(et_content,new LinearLayout.LayoutParams(dm.widthPixels/2, dm.heightPixels/12)); 
	}
	public TextViewEdit(Context context,String content,String res,int textwidth,int textheight,int editwidth,int editheight) {
		super(context);
		this.setOrientation(HORIZONTAL);
		tv_content=new TextView(context);
		tv_content.setText(content);
		tv_content.setTextColor(Color.BLACK);
		tv_content.setGravity(Gravity.CENTER);
		addView(tv_content,new LinearLayout.LayoutParams(textwidth, textheight));
		et_content = new EditText(context);
		et_content.setText(res);
		et_content.setTextColor(Color.BLACK);
		addView(et_content,new LinearLayout.LayoutParams(editwidth,  editheight)); 
	}

}
