package cn.itcast.mp3;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
    private EditText filenameText;
    private String filename;
    private MediaPlayer mediaPlayer;
    private int position;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mediaPlayer = new MediaPlayer();
        ButtonClickListener listener = new ButtonClickListener();
        Button playButton = (Button)this.findViewById(R.id.play);
        Button pauseButton = (Button)this.findViewById(R.id.pause);
        Button resetButton = (Button)this.findViewById(R.id.reset);
        Button stopButton = (Button)this.findViewById(R.id.stop);
        filenameText = (EditText)this.findViewById(R.id.filename);
        playButton.setOnClickListener(listener);
        pauseButton.setOnClickListener(listener);
        resetButton.setOnClickListener(listener);
        stopButton.setOnClickListener(listener);        
    }
    
    @Override
	protected void onResume() {
		if(position>0 && filename!=null){
			File file = new File(Environment.getExternalStorageDirectory(), filename);
			try {
				play(file);
				mediaPlayer.seekTo(position);
				position = 0;
			} catch (IOException e) {
				Log.e(TAG, e.toString());
			}
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		if(mediaPlayer.isPlaying()){
			position = mediaPlayer.getCurrentPosition();
			mediaPlayer.stop();
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if(mediaPlayer!=null) mediaPlayer.release();
		super.onDestroy();
	}

	private final class ButtonClickListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			filename = filenameText.getText().toString();			
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
				File file = new File(Environment.getExternalStorageDirectory(), filename);
				if(file.exists()){
					try {
						switch (v.getId()) {
						case R.id.play:
							play(file);
							break;
							
						case R.id.pause:
							if(mediaPlayer.isPlaying()){
								mediaPlayer.pause();
								((Button)v).setText(R.string.continues);
							}else{
								mediaPlayer.start();//¼ÌÐø²¥·Å
								((Button)v).setText(R.string.pause);
							}							
							break;
						case R.id.reset:	
							if(mediaPlayer.isPlaying()){
								mediaPlayer.seekTo(0);
							}else{
								play(file);
							}							
							break;
						case R.id.stop:
							if(mediaPlayer.isPlaying()) mediaPlayer.stop();															
							break;
						}
					} catch (Exception e) {
						Log.e(TAG, e.toString());
					}
				}else{
					Toast.makeText(MainActivity.this, R.string.noexist, 1).show();
				}				
			}else{
				Toast.makeText(MainActivity.this, R.string.SDCarderror, 1).show();
			}	
		}
    }
	
	private void play(File file) throws IOException {
		mediaPlayer.reset();
		mediaPlayer.setDataSource(file.getAbsolutePath());
		mediaPlayer.prepare();
		mediaPlayer.start();
	}
}