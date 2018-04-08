package gl.test.view;


import gl.test.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.PopupWindow;

public class MenuView {
	
	
	public PopupWindow mPop;
	private Context mContext;
	private boolean isShow;
	private MenuView menuView;
	public Bitmap mBitmap;
	
	public View  contentView;

	public MenuView(Context context,Bitmap biitmap){
		mBitmap = biitmap;
		mContext = context;
	}
	
	
	public boolean show(){
		if(hide()){
			return false;
		}
		
		final Context context = mContext;
		
		mPop = new PopupWindow(context);
		DisplayMetrics disPlayMetrics = context.getResources().getDisplayMetrics();
		
		 contentView = LayoutInflater.from(context.getApplicationContext()).
		inflate(R.layout.popup, null);
		contentView.setBackgroundResource(R.drawable.popup);
		contentView.setOnTouchListener(new OnTouchListener(
				) {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					hide();
				}
				return false;
			}
		});
		
		float density =disPlayMetrics.density;
		mPop.setWidth(disPlayMetrics.widthPixels);
		mPop.setHeight((int) (100*density));
		mPop.setContentView(contentView);
		mPop.setFocusable(true);
		mPop.setOutsideTouchable(true);
		mPop.setTouchable(true);
		mPop.setBackgroundDrawable(null);
		mPop.showAtLocation(contentView, Gravity.BOTTOM|Gravity.CENTER, 0, 0);
		
	
		
		
		return true;
	}


	private boolean hide() {
		// TODO Auto-generated method stub
		if(null != mPop && mPop.isShowing() ){
			isShow = false;
			mPop.dismiss();
			mPop = null;
			return true;
		}
		
		return false;
	}
	
	
	public Bitmap  ButtonClick(Bitmap bitmap ){
		
		final Button liftButton = (Button) contentView.findViewById(R.id.left);
		liftButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		final Button rightButton = (Button) contentView.findViewById(R.id.right);
		rightButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		return bitmap;
		
	}

}
