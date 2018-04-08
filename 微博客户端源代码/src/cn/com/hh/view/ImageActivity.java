package cn.com.hh.view;

import android.app.Activity;
import android.os.Bundle;

public class ImageActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image);
		
		
//		Intent i=this.getIntent();
//        if(i!=null){
//            Bundle b=i.getExtras();
//            if(b!=null){
//                if(b.containsKey("url")){
//                    String url = b.getString("url");
//                    mZoomView=(ImageZoomView)findViewById(R.id.pic);
//                    Drawable img= AsyncImageLoader.loadImageFromUrl(url);
//                    
//                
//                    image=drawableToBitmap(img);
//                    mZoomView.setImage(image);
//                    
//                    mZoomState = new ZoomState();
//                    mZoomView.setZoomState(mZoomState);
//                    mZoomListener = new SimpleZoomListener();
//                    mZoomListener.setZoomState(mZoomState);
//                    
//                    mZoomView.setOnTouchListener(mZoomListener);
//                    resetZoomState();
//                }
//            }
//        }
	}

}
