package com.uvchip.mediacenter.filebrowser;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import java.util.HashMap;

import com.uvchip.mediacenter.R;


public class HorizontalLayout extends HorizontalScrollView {
    final boolean DEBUG = false;
    final String TAG = HorizontalLayout.class.getCanonicalName();
    Context mContext;
    LinearLayout layout;
    HashMap<Integer, TextView> tvMap = new HashMap<Integer, TextView>();
    Paint p;
    ImageView iv;
    public HorizontalLayout(Context context) {
        super(context);
        init(context);
    }
    public HorizontalLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public HorizontalLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }
    private void init(Context context) {
        p = new Paint();
        mContext = context;
        layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
        
        iv = new ImageView(context);
        iv.setScaleType(ScaleType.CENTER);
        iv.setImageResource(R.drawable.disk);
        addView(layout);
        this.setHorizontalScrollBarEnabled(false);        
    }
    public void SetFilePath(String path){
        if(DEBUG)Log.i(TAG, "path===============>" + path);
        layout.removeAllViews();
        layout.addView(iv);
        int index = path.lastIndexOf("/");
        if(index > 0){
            String[] paths = path.split("/");
            
            for (int i = 0; i < paths.length; i++) {
                String tmp = paths[i];
                addTextView(i,i+1 == paths.length,tmp + "/");
            }
        }else if(index == 0){
            addTextView(0,false,"/");
        }
    }
    private void addTextView(int position,boolean last,String path){
        final TextView tv = new TextView(mContext);
        tv.setText(path);
        tv.setTextAppearance(mContext, R.style.tvPath);
        tv.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mItemClickListener != null){
                    mItemClickListener.onItemClick(tv);
                }
            }
            
        });
        tv.setFocusable(true);
        tvMap.put(position, tv);
        layout.addView(tv);
        tv.postInvalidate();
    }    
    public String GetPathByTv(View v){
        if(tvMap.containsValue(v)){
            String path = "";
            int size = tvMap.size();
            for(int i = 0; i < size; i++){
                path += tvMap.get(i).getText().toString();
                if(v.equals(tvMap.get(i))){
                    break;
                }
            }
            return path;
        }
        return "";
    }
    public void setOnItemClickListener(OnTVItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    public OnTVItemClickListener getOnItemClickListener() {
        return mItemClickListener;
    }
    private OnTVItemClickListener mItemClickListener;
    public interface OnTVItemClickListener {
        public void onItemClick(TextView v);
    }
}
