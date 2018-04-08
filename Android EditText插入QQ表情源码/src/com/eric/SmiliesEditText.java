package com.eric;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.widget.EditText;

public class SmiliesEditText extends EditText {

	public SmiliesEditText(Context context) {
		super(context);
	}
	public SmiliesEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}
	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
	public void insertIcon(int id){
		SpannableString ss = new SpannableString(getText().toString()+"[smile]");  
        Drawable d = getResources().getDrawable(id);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());  
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);  
        ss.setSpan(span, getText().length(),getText().length()+"[smile]".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);  
        setText(ss);
	}
}
