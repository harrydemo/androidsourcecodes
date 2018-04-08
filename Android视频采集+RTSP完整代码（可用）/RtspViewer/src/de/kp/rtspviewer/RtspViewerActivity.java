package de.kp.rtspviewer;

/**
 * This is the most minimal viewer for RtspCamera app
 * 
 * @author Peter Arwanitis (arwanitis@dr-kruscheundpartner.de)
 *
 */
import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import com.orangelabs.rcs.platform.AndroidFactory;
import com.orangelabs.rcs.provider.settings.RcsSettings;
import com.orangelabs.rcs.service.api.client.media.video.VideoSurfaceView;

import de.kp.net.rtp.viewer.RtpVideoRenderer;

public class RtspViewerActivity extends Activity {

	/**
	 * Video renderer
	 */
	private RtpVideoRenderer incomingRenderer = null;

	/**
	 * Video preview
	 */
	private VideoSurfaceView incomingVideoView = null;

	/**
	 * hardcoded rtsp server path
	 */
	private String rtspConnect = "rtsp://192.168.1.45:8080/video";
	// private String rtsp =
	// "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_175k.mov";

	private int videoHeight;

	private int videoWidth;

	private String TAG = "RtspViewer";

	@Override
	public void onCreate(Bundle icicle) {

        // ���StrictMode�ĵ�
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
		
		
		Log.i(TAG, "onCreate");

		super.onCreate(icicle);

		// Set application context ... skipping FileFactory
		AndroidFactory.setApplicationContext(getApplicationContext());

		// Instantiate the settings manager
		RcsSettings.createInstance(getApplicationContext());

		setContentView(R.layout.videoview);

		// <string
		// name="rcs_settings_label_default_video_format">h263-2000</string>
		// <string-array name="rcs_settings_list_video_format_value">
		// <item>h263-2000</item>
		// <item>h264</item>
		// </string-array>
		// <string-array name="rcs_settings_list_video_format_label">
		// <item>Low (H.263)</item>
		// <item>High (H.264)</item>
		// </string-array>
		//
		// <string name="rcs_settings_label_default_video_size">QCIF</string>
		// <string-array name="rcs_settings_list_video_size_value">
		// <item>QCIF</item>
		// <!-- <item>QVGA</item> -->
		// </string-array>
		// <string-array name="rcs_settings_list_video_size_label">
		// <item>Low (176x144)</item>
		// <!-- <item>High (320x240)</item> -->
		// </string-array>

		// Set incoming video preview
		if (incomingVideoView == null) {
			incomingVideoView = (VideoSurfaceView) findViewById(R.id.incoming_video_view);
			incomingVideoView.setAspectRatio(videoWidth, videoHeight);

			try {
				incomingRenderer = new RtpVideoRenderer(rtspConnect);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			incomingRenderer.setVideoSurface(incomingVideoView);
		}

	}

	@Override
	protected void onPause() {
		Log.i(TAG, "onPause");
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		Log.i(TAG, "onResume");
		super.onResume();

		incomingRenderer.open();
		incomingRenderer.start();

		Log.i(TAG, "onResume renderer started");

	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy");

		super.onDestroy();

		incomingRenderer.stop();
		incomingRenderer.close();

	}

}
