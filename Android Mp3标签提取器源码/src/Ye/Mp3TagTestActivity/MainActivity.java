package Ye.Mp3TagTestActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.charset.CharsetEncoder;

import org.w3c.dom.Text;

import Ye.Utils.ParseMp3Tag;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        pathView = (TextView)findViewById(R.id.pathView);
        
        loadMp3Button = (Button)findViewById(R.id.loadMp3Button);
        loadMp3Button.setOnClickListener(new LoadMp3ButtonListener());
        
        charsetButton01 = (RadioButton)findViewById(R.id.charsetButton01);
        charsetButton02 = (RadioButton)findViewById(R.id.charsetButton02);
        
        tagType = (TextView)findViewById(R.id.tagType);
        
        trackName = (TextView)findViewById(R.id.trackName);
        
        artistName = (TextView)findViewById(R.id.artistName);
        
        albumName = (TextView)findViewById(R.id.albumName);
        
        year = (TextView)findViewById(R.id.year);
        
        albumImage = (ImageView)findViewById(R.id.albumImage);
        
        parseButton = (Button)findViewById(R.id.parseButton);
        parseButton.setOnClickListener(new ParseButtonListener());
        
        BroadcastReceiver loadMp3PathReceiver = new LoadMp3PathRecevier();
        IntentFilter loadMp3PathIntentFilter = new IntentFilter(AppConstant.LOAD_MP3_PATH_ACTION);
        registerReceiver(loadMp3PathReceiver, loadMp3PathIntentFilter);
    }
    
    private TextView pathView;
    private String path;
    private Button loadMp3Button;
    private Button parseButton;
    private TextView tagType;
    private TextView trackName;
    private TextView artistName;
    private TextView albumName;
    private TextView year;
    private ImageView albumImage;
    private RadioButton charsetButton01;
    private RadioButton charsetButton02;
    
    class LoadMp3ButtonListener implements OnClickListener
    {
		@Override
		public void onClick(View v) 
		{
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, SearchFileActivity.class);
			startActivity(intent);
		}
    }
    
    class LoadMp3PathRecevier extends BroadcastReceiver
    {
		@Override
		public void onReceive(Context context, Intent intent) 
		{
			path = intent.getStringExtra("path");
			//System.out.println(path);
			String textValue = "歌曲路径:\n" + path;
			if(path != null)
			{
				pathView.setText(textValue);
				parseButton.setEnabled(true);
			}
		}
    }
    
    class ParseButtonListener implements OnClickListener
    {
		@Override
		public void onClick(View v) 
		{
			if(charsetButton01.isChecked())
				parseTag(path, "utf-16");
			else
				parseTag(path, "gbk");
		}
    }
    
    private void parseTag(String path, String charsetName)
    {
    	ParseMp3Tag mp3Tag = new ParseMp3Tag(path);
    	mp3Tag.parse(charsetName);
    	tagType.setText("标签类型:" + mp3Tag.getTagType());
    	trackName.setText("歌曲名:" + mp3Tag.getTrackName());
    	artistName.setText("歌手名:" + mp3Tag.getArtistName());
    	albumName.setText("专辑名:" + mp3Tag.getAlbumName());
    	year.setText("发行年代:" + mp3Tag.getYear());
    	Bitmap tempPhoto = mp3Tag.getAlbumImage();
    	if(tempPhoto != null)
    	{
    		albumImage.setImageBitmap(tempPhoto);
    		System.out.println("tempPhoto is not null....");
    	}
    	else
    		albumImage.setImageResource(R.drawable.icon);
    }
    
}