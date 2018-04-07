package cn.com.ldci.plants;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class ApahaThread extends Thread {

	boolean flag = true ;
	SeedBank sb ;
	ApahaThread(SeedBank sb){
		this.sb = sb ;
	}
	@Override
	public void run() {
		while(flag){
			for(int i = 0 ; i < sb.rv.size(); i++){//for(Rect rt:sb.rv){
				Rect rt = sb.rv.get(i);
				if(rt.bottom<=rt.top){
					sb.rv.remove(rt);
					int k = (int) ((rt.left-sb.startx)/(sb.W+4));
					sb.plantflag[k] = true ;
				}else{
					rt.bottom = rt.bottom - 1;
				}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				Log.e("app", e.toString());
			}
		}
	}

}
