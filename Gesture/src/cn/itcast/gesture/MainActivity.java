package cn.itcast.gesture;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity {
    private GestureLibrary library;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        library = GestureLibraries.fromRawResource(this, R.raw.gestures);
        library.load();//�������ƿ�
        GestureOverlayView gestureView = (GestureOverlayView)this.findViewById(R.id.gestures);
        gestureView.addOnGesturePerformedListener(new GestureListener());
    }
    
    private final class GestureListener implements OnGesturePerformedListener{
		@Override
		public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
			ArrayList<Prediction> predictions = library.recognize(gesture);
			if(!predictions.isEmpty()){
				Prediction prediction = predictions.get(0);//��ȡ��ƥ��ļ�¼
				if(prediction.score>1){
					if("close".equals(prediction.name)){
						finish();
					}else if("zhang".equals(prediction.name)){
						Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:13783293933"));
						startActivity(intent);
					}
				}else{
					Toast.makeText(MainActivity.this, "�������Ʋ��̫��", 1).show();
				}
			}
		}
    }

	@Override
	protected void onDestroy() {
		android.os.Process.killProcess(android.os.Process.myPid());
		super.onDestroy();
	}
    
}