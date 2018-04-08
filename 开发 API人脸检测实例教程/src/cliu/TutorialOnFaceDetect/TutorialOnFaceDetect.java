package cliu.TutorialOnFaceDetect;

/*
 * TutorialOnFaceDetect
 * 
 * [AUTHOR]: Chunyen Liu
 * [SDK   ]: Android SDK 2.1 and up
 * [NOTE  ]: developer.com tutorial, "Face Detection with Android APIs"
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.LinearLayout.LayoutParams;

public class TutorialOnFaceDetect extends Activity {
	private MyImageView mIV;
	private Bitmap mFaceBitmap;
	private int mFaceWidth = 200;
	private int mFaceHeight = 200;   
	private static final int MAX_FACES = 10;
	private static String TAG = "TutorialOnFaceDetect";
	private static boolean DEBUG = false;
	
	protected static final int GUIUPDATE_SETFACE = 999;
	protected Handler mHandler = new Handler(){         
		// @Override         
		public void handleMessage(Message msg) {         
			mIV.invalidate();
			super.handleMessage(msg);
		}
	}; 
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIV = new MyImageView(this);
		setContentView(mIV, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));       

		// load the photo
		Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.face3); 
		mFaceBitmap = b.copy(Bitmap.Config.RGB_565, true); 
		b.recycle();
		
		mFaceWidth = mFaceBitmap.getWidth();
		mFaceHeight = mFaceBitmap.getHeight();  
		mIV.setImageBitmap(mFaceBitmap); 
		mIV.invalidate();
		
		// perform face detection in setFace() in a background thread
		doLengthyCalc();
    }

    public void setFace() {
    	FaceDetector fd;
    	FaceDetector.Face [] faces = new FaceDetector.Face[MAX_FACES];
    	PointF eyescenter = new PointF();
    	float eyesdist = 0.0f;
    	int [] fpx = null;
    	int [] fpy = null;
    	int count = 0;
    	
    	try {
    		fd = new FaceDetector(mFaceWidth, mFaceHeight, MAX_FACES);        
    		count = fd.findFaces(mFaceBitmap, faces);
    	} catch (Exception e) {
    		Log.e(TAG, "setFace(): " + e.toString());
    		return;
    	}

    	// check if we detect any faces
    	if (count > 0) {
    		fpx = new int[count * 2];
    		fpy = new int[count * 2];

    		for (int i = 0; i < count; i++) { 
    			try {                 
    				faces[i].getMidPoint(eyescenter);                  
    				eyesdist = faces[i].eyesDistance();                  

    				// set up left eye location
    				fpx[2 * i] = (int)(eyescenter.x - eyesdist / 2);
    				fpy[2 * i] = (int)eyescenter.y;
    				
    				// set up right eye location
    				fpx[2 * i + 1] = (int)(eyescenter.x + eyesdist / 2);
    				fpy[2 * i + 1] = (int)eyescenter.y;

    				if (DEBUG)
    					Log.e(TAG, "setFace(): face " + i + ": confidence = " + faces[i].confidence() 
    							+ ", eyes distance = " + faces[i].eyesDistance()                             
    							+ ", pose = ("+ faces[i].pose(FaceDetector.Face.EULER_X) + ","                            
    							+ faces[i].pose(FaceDetector.Face.EULER_Y) + ","                            
    							+ faces[i].pose(FaceDetector.Face.EULER_Z) + ")"                            
    							+ ", eyes midpoint = (" + eyescenter.x + "," + eyescenter.y +")"); 
    			} catch (Exception e) { 
    				Log.e(TAG, "setFace(): face " + i + ": " + e.toString());
    			}            
    		}      
    	}

    	mIV.setDisplayPoints(fpx, fpy, count * 2, 1);
    } 

    private void doLengthyCalc() {
    	Thread t = new Thread() {
    		Message m = new Message();

    		public void run() {
    			try {
    				setFace();
    				m.what = TutorialOnFaceDetect.GUIUPDATE_SETFACE;      		    
    				TutorialOnFaceDetect.this.mHandler.sendMessage(m); 
    			} catch (Exception e) { 
    				Log.e(TAG, "doLengthyCalc(): " + e.toString());
    			}
    		}
    	};      

    	t.start();        
    }  
    
}