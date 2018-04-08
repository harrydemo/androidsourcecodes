package com.pro;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProgressDialog1 extends AlertDialog {
    
    public static final int STYLE_SPINNER = 0;
    
    /** Creates a ProgressDialog1 with a horizontal progress bar.
     */
    public static final int STYLE_HORIZONTAL = 1;
    
    public static final int STYLE_DISMISS    = -1;
    
    private ProgressBar mProgress_h;
    
    private int mProgressStyle = STYLE_SPINNER;
    private TextView mProgressNumber_s,message_tv,message_ed_tv,message_center_tv,message_all_tv;
    
    private int mMax;
    private int mProgressVal;
    private int mSecondaryProgressVal;
    private int mIncrementBy;
    private int mIncrementSecondaryBy;
    private Drawable mProgressDrawable;
    private Drawable mIndeterminateDrawable;
    private CharSequence mMessage = "";
    private boolean mIndeterminate;
    
    private boolean mHasStarted;
    private Handler mViewUpdateHandler;
    
    private Context mContext;
    private LinearLayout layout,layout_h,layout_s,layout_message_h;
    private LinearLayout layout_progress_h,layout_progress_s;
    private TextView message_h,number_ed,number_cen,number_all,progress_number2;
    private ProgressBar progress_h,progress;
    
    private SpannableString tmp;
    
    
    public SpannableString getTmp() {
		return tmp;
	}

	public void setTmp(SpannableString tmp) {
		this.tmp = tmp;
	}

	public ProgressDialog1(Context context) {
    	this(context, android.R.style.Theme_Panel);
    	mContext = context;
    }

    public ProgressDialog1(Context context, int theme) {
        super(context, theme);
    }

    public static ProgressDialog1 show(Context context, CharSequence title,
            CharSequence message) {
        return show(context, title, message, false);
    }

    public static ProgressDialog1 show(Context context, CharSequence title,
            CharSequence message, boolean indeterminate) {
        return show(context, title, message, indeterminate, false, null);
    }

    public static ProgressDialog1 show(Context context, CharSequence title,
            CharSequence message, boolean indeterminate, boolean cancelable) {
        return show(context, title, message, indeterminate, cancelable, null);
    }

    public static ProgressDialog1 show(Context context, CharSequence title,
            CharSequence message, boolean indeterminate,
            boolean cancelable, OnCancelListener cancelListener) {
        ProgressDialog1 dialog = new ProgressDialog1(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setIndeterminate(indeterminate);
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);
        dialog.show();
        return dialog;
    }

    
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (mProgressStyle == STYLE_HORIZONTAL) 
        {
            /* Use a separate handler to update the text views as they
             * must be updated on the same thread that created them.
             */
        	mViewUpdateHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    
                    /* Update the number and percent */
                    int progress = mProgress_h.getProgress();
                    int max = mProgress_h.getMax();
                    message_tv.setText(getMessage());
                    message_ed_tv.setText(""+progress);
                    message_all_tv.setText(""+max);
                }
            };
            View view = initLayout();
            layout_progress_h = layout_h;
            layout_progress_s = layout_s;
            layout_progress_s.setVisibility(View.GONE);
            
            mProgress_h = progress_h;
            mProgressNumber_s = progress_number2;
            message_tv = message_h;
            message_ed_tv = number_ed;
            message_center_tv = number_cen;
            message_all_tv = number_all;
            message_center_tv.setText("/");
            setView(view);
        } 
        if (mMax > 0) {
            setMax(mMax);
        }
        if (mProgressVal > 0) {
            setProgress(mProgressVal);
        }
        if (mSecondaryProgressVal > 0) {
            setSecondaryProgress(mSecondaryProgressVal);
        }
        if (mIncrementBy > 0) {
            incrementProgressBy(mIncrementBy);
        }
        if (mIncrementSecondaryBy > 0) {
            incrementSecondaryProgressBy(mIncrementSecondaryBy);
        }
        if (mProgressDrawable != null) {
            setProgressDrawable(mProgressDrawable);
        }
        if (mIndeterminateDrawable != null) {
            setIndeterminateDrawable(mIndeterminateDrawable);
        }
        if (mMessage != null) {
            setMessage(mMessage);
        }
        setIndeterminate(mIndeterminate);
        onProgressChanged();
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        mHasStarted = true;
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        mHasStarted = false;
    }

    public void setProgress(int value) {
        if (mHasStarted) {
            mProgress_h.setProgress(value);
            onProgressChanged();
        } else {
            mProgressVal = value;
        }
    }

    public void setSecondaryProgress(int secondaryProgress) {
        if (mProgress_h != null) {
        	mProgress_h.setSecondaryProgress(secondaryProgress);
            onProgressChanged();
        } else {
            mSecondaryProgressVal = secondaryProgress;
        }
    }

    public int getProgress() {
        if (mProgress_h != null) {
            return mProgress_h.getProgress();
        }
        return mProgressVal;
    }

    public int getSecondaryProgress() {
        if (mProgress_h != null) {
            return mProgress_h.getSecondaryProgress();
        }
        return mSecondaryProgressVal;
    }

    public int getMax() {
        if (mProgress_h != null) {
            return mProgress_h.getMax();
        }
        return mMax;
    }

    public void setMax(int max) {
        if (mProgress_h != null) {
        	mProgress_h.setMax(max);
            onProgressChanged();
        } else {
            mMax = max;
        }
    }

    public void incrementProgressBy(int diff) {
        if (mProgress_h != null) {
        	mProgress_h.incrementProgressBy(diff);
            onProgressChanged();
        } else {
            mIncrementBy += diff;
        }
    }

    public void incrementSecondaryProgressBy(int diff) {
        if (mProgress_h != null) {
        	mProgress_h.incrementSecondaryProgressBy(diff);
            onProgressChanged();
        } else {
            mIncrementSecondaryBy += diff;
        }
    }

    public void setProgressDrawable(Drawable d) {
        if (mProgress_h != null) {
        	mProgress_h.setProgressDrawable(d);
        } else {
            mProgressDrawable = d;
        }
    }

    public void setIndeterminateDrawable(Drawable d) {
        if (mProgress_h != null) {
        	mProgress_h.setIndeterminateDrawable(d);
        } else {
            mIndeterminateDrawable = d;
        }
    }

    public void setIndeterminate(boolean indeterminate) {
        if (mProgress_h != null) {
        	mProgress_h.setIndeterminate(indeterminate);
        } else {
            mIndeterminate = indeterminate;
        }
    }

    public boolean isIndeterminate() {
        if (mProgress_h != null) {
            return mProgress_h.isIndeterminate();
        }
        return mIndeterminate;
    }
    
    @Override
    public void setMessage(CharSequence message) {
        if (mProgress_h != null) {
            if (mProgressStyle == STYLE_HORIZONTAL) {
                mMessage = message;
            } 
        } else {
            mMessage = message;
        }
    }
    
    public void setProgressStyle(int style) {
        mProgressStyle = style;
    }
    
    private void onProgressChanged() {
        if (mProgressStyle == STYLE_HORIZONTAL) {
        	mViewUpdateHandler.sendEmptyMessage(0);
        }
    }
    
    public void setDynamicStyle(int style,CharSequence message){
    	mProgressNumber_s.setText(message);
    	if (style == STYLE_HORIZONTAL) {
			layout_progress_h.setVisibility(View.VISIBLE);
			layout_progress_s.setVisibility(View.GONE);
		}else if(style == STYLE_SPINNER){
			layout_progress_s.setVisibility(View.VISIBLE);
			layout_progress_h.setVisibility(View.GONE);
		}else {
		}
    }
    
    private CharSequence getMessage()
    {
    	return mMessage;
    }

    
    private LinearLayout initLayout()
    {
    	layout = new LinearLayout(mContext);
    	layout.setOrientation(LinearLayout.VERTICAL);
    	layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
    	
    	layout_h = new LinearLayout(mContext);
    	layout_h.setOrientation(LinearLayout.VERTICAL);
    	layout_h.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));

    	layout_message_h = new LinearLayout(mContext);
    	layout_message_h.setOrientation(LinearLayout.HORIZONTAL);
    	layout_message_h.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
    	layout_message_h.setGravity(Gravity.CENTER);
    	
    	message_h = new TextView(mContext);
    	message_h.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
    	message_h.setMinHeight(30);
    	message_h.setWidth(60);
    	
    	number_ed = new TextView(mContext);
    	number_ed.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
    	number_ed.setGravity(Gravity.RIGHT);
    	number_ed.setMinHeight(30);
    	number_ed.setWidth(40);
    	
    	number_cen = new TextView(mContext);
    	number_cen.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
    	number_cen.setMinHeight(30);
    	
    	number_all = new TextView(mContext);
    	number_all.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
    	number_all.setMinHeight(30);
    	
    	layout_message_h.addView(message_h);
    	layout_message_h.addView(number_ed);
    	layout_message_h.addView(number_cen);
    	layout_message_h.addView(number_all);
    	
    	
    	progress_h = new ProgressBar(mContext);
        BeanUtils.setFieldValue(progress_h, "mOnlyIndeterminate", new Boolean(false));
        BeanUtils.setFieldValue(progress_h, "mMinHeight", new Integer(15));
        progress_h.setIndeterminate(false);
        progress_h.setProgressDrawable(mContext.getResources().getDrawable(android.R.drawable.progress_horizontal));
        progress_h.setIndeterminateDrawable(mContext.getResources().getDrawable(android.R.drawable.progress_indeterminate_horizontal));
        progress_h.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
           

    	layout_h.addView(layout_message_h);
    	layout_h.addView(progress_h);
    	
    	layout_s = new LinearLayout(mContext);
    	layout_s.setOrientation(LinearLayout.HORIZONTAL);
    	layout_s.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));

    	progress = new ProgressBar(mContext);
        progress.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
        BeanUtils.setFieldValue(progress, "mMinHeight", new Integer(45));
        progress.setMax(10000);
        
        progress_number2 = new TextView(mContext);
        progress_number2.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
        progress_number2.setPadding(20, 10, 0, 0);
    	
        layout_s.addView(progress);
        layout_s.addView(progress_number2);
        
        layout.addView(layout_h);
        layout.addView(layout_s);
        
    	return layout;
    }
}
