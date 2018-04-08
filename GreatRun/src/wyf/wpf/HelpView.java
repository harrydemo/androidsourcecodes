package wyf.wpf;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class HelpView extends SurfaceView implements SurfaceHolder.Callback{
	RunActivity father;
	Bitmap bmpBack;
	//Path path;
	
	public HelpView(RunActivity father){
		super(father);
		this.father = father;
		getHolder().addCallback(this);
		bmpBack = BitmapFactory.decodeResource(father.getResources(), R.drawable.help_view);
	}
	
	public void doDraw(Canvas canvas){
		canvas.drawBitmap(bmpBack, 0, 0, null);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}


	public void surfaceCreated(SurfaceHolder holder) {
		Canvas canvas=null;
		try {
			canvas = holder.lockCanvas(null);
			doDraw(canvas);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			if(canvas != null){
				holder.unlockCanvasAndPost(canvas);
			}
		}		
	}


	public void surfaceDestroyed(SurfaceHolder holder) {
	}
}