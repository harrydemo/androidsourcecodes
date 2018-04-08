package gl.test.view;


import gl.test.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class ToneMenuView {
	
	
	private PopupWindow mPopup;
	private ToneView mToneView;
	private Context mContext;
	private boolean mIsShow;
	
	
	public ToneMenuView(Context context){
		mContext = context;
		
	}
	
	
	public boolean show(){
		if(hide()){
			return false;
		}
		
		
		final Context context = mContext;
		mIsShow = true;
		
		
		mPopup =  new PopupWindow(context);
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		
		mToneView = new ToneView(context);
		View view = mToneView.getParentView();
		view.setBackgroundResource(R.drawable.popup);
		view.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_DOWN){
				hide();}
				return false;
			}
		});
		
		
		float density = metrics.density;
		mPopup.setWidth(metrics.widthPixels);
		mPopup.setHeight(metrics.heightPixels);
		mPopup.setHeight((int) (120*density));
		mPopup.setContentView(view);
		mPopup.setFocusable(true);
		mPopup.setOutsideTouchable(true);
		mPopup.setBackgroundDrawable(null);
		mPopup.showAtLocation(view, Gravity.BOTTOM|Gravity.CENTER, 0, 0);
		return true;
		
	}
	
	public void setSaturationBarListener(OnSeekBarChangeListener l){
		mToneView.setSaturationBarListener(l);
	}


	private boolean hide() {
		// TODO Auto-generated method stub
		
		if(null != mPopup && mPopup.isShowing()){
			mIsShow = false;
			mPopup.dismiss();
			mPopup = null;
			return true;
		}
		return false;
	}
	
	
	public boolean isShow(){
		return mIsShow;
	}
	
	 public ToneView getToneView(){
		return mToneView;
	}

	 


}


