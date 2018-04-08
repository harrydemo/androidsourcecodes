package com.pixtas.yogapowervinyasa.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.pixtas.framework.YogaAppContext;
import com.pixtas.helpers.DialogHelper;
import com.pixtas.helpers.FileDownHelper;
import com.pixtas.helpers.HardwareHelper;
import com.pixtas.models.DatabaseAdapter;
import com.pixtas.models.option.DataBaseOption;
import com.pixtas.models.rest.RestAPI;
import com.pixtas.models.struts.FlashUrl;
import com.pixtas.yogapowervinyasa.Config;
import com.pixtas.yogapowervinyasa.R;
import com.pixtas.yogapowervinyasa.YogaApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class Home extends Activity implements OnClickListener,OnItemSelectedListener,OnTouchListener{
	private static final String TAG = "Home";

	private ProgressDialog  pd = null;
	
	private TextView playTitle;
	private ProgressBar playPB;
	private ImageView pre,pause,next,yogaImg;
	private MediaPlayer mp;
	private Spinner chapters;
	
	private String saveDir;
	private String saveAudioTo;
	private String[] saveImgTos = null;
	private int chapter = 1;
	private boolean isPlaying = false;
	
	/*-------------弹出窗口控件-------------*/
	//下载窗口
	private Dialog dlg;
	private TextView cancel;
	private ProgressBar installPB;
	
	//设置窗口
	private RadioButton englishRadio,sanskritRadio,spanishRadio;
	private Button sureBtn,cancelBtn;
	private RadioGroup languageRadioGroup;
	private int arg = 0;
	/*------------------------------------------*/
	
	/*访问远程音频文件的大小*/
	private int audioSize = 0;//flash音频大小
	private int allImageSize = 0;//flash所有照片大小
	private int flashSize = 0;//flash大小（audioSize + allImamgeSize）
	private int audioTime = 0;//audio时间
	
	private boolean switchChapter = true;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        YogaAppContext.initApp((YogaApp)getApplication());
        DataBaseOption.databaseAdapter = new DatabaseAdapter(YogaAppContext.getApp());
        if(HardwareHelper.connectedNetwork(YogaAppContext.getApp())){
        	if(!RestAPI.checkApkUpgrade()){
        		Builder builder = DialogHelper.showAlertBuilder(Home.this, R.string.main_need_update, R.string.warning);
        		builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String url = "market://details?id=com.pixtas.yogatestaudio";
            			Intent intent = new Intent(Intent.ACTION_VIEW);
            			intent.setData(Uri.parse(url));
            			startActivity(intent);
                		finish();
					}
				});
    			builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						initCreate();
					}
				});
    			builder.show();
        	}else{
        		this.initCreate();
        	}
        }else{
        	this.initCreate();
        }
        
    }
    /*创建视图之前的准备工作*/
    private void initCreate(){
    	if(!DataBaseOption.isInstall()){
        	if(this.checkNet()){
        		this.progressDlg(1);//没下载完
        	}
        }else{
        	if(HardwareHelper.connectedNetwork(YogaAppContext.getApp())){
        		if(RestAPI.checkNewVersion()){
        			Builder builder = DialogHelper.showAlertBuilder(Home.this,R.string.update_msg,R.string.warning);
        			builder.setCancelable(true);
        			builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							progressDlg(2);//检测到有更改再下载
						}
					});
        			builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							initFlashMsg();
						}
					});
        			builder.show();
        		}else{
        			this.initFlashMsg();
        		}
        	}else{
	        	this.initFlashMsg();
        	}
        }
        this.initComponents();
    }
    /*加载老版本的东西*/
    private void initFlashMsg(){
    	this.progressDlg(0);
    }
    /*加载flash信息*/
    private void loadFlashMsg(){
    	RestAPI.initFlashUrl();
    	audioSize = Config.audioSize;
    	allImageSize = Config.allImageSize;
		flashSize = audioSize + allImageSize;
    }
    /*删除照片*/
    private void delPhotos(){
    	for(int i = 1;i <= FlashUrl.chaptersNum;i ++){
			String path = FileDownHelper.getSaveImgName(YogaAppContext.getApp(),Integer.toString(i));
			File f = new File(path);
			if(f.exists()){
				f.delete();
			}
		}
    }
    /*创建加载窗口*/
	private void progressDlg(final int type){
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setMessage("Loading...");
		pd.setCancelable(false);
		pd.show();
		new Thread(){
			public void run(){
				if(type == 0){
					RestAPI.initFlash();
				}else if(type == 2){//检查更新
					DataBaseOption.updateInstallVersion(1, Config.newVersion);
					DataBaseOption.updateInstallData(1, 0);
					delPhotos();
					loadFlashMsg();
				}else if(type == 3){//重新下载
					DataBaseOption.updateInstallData(1, 0);
					File audioFile = new File(saveAudioTo);
					if(audioFile.exists()){
						audioFile.delete();
					}
					delPhotos();
					loadFlashMsg();
				}else{
					loadFlashMsg();
				}
				pd.dismiss();
			}
		}.start();
		pd.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				if(type == 3){
					playPB.setProgress(0);
					pause.setImageResource(R.drawable.play);
				}
				initTitleSpinner();
				initInstallAudioDlg(type);
			}
		});
	}
	private void initTitleSpinner(){
		List<String> data = new ArrayList<String>();
		switch (arg) {
		case 0:
			for(int i = 0 ;i < FlashUrl.photoEnglishTitles.length;i ++){
				data.add(Integer.toString(i + 1) + ". " + FlashUrl.photoEnglishTitles[i]);
			}
			break;
		case 1:
			for(int i = 0 ;i < FlashUrl.photoSanskritTitles.length;i ++){
				data.add(Integer.toString(i + 1) + ". " + FlashUrl.photoSanskritTitles[i]);
			}
			break;
		case 2:
			for(int i = 0 ;i < FlashUrl.photoSpanishTitles.length;i ++){
				data.add(Integer.toString(i + 1) + ". " + FlashUrl.photoSpanishTitles[i]);
			}	
			break;
		default:
			break;
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(Home.this,android.R.layout.simple_spinner_item,data);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		chapters.setAdapter(adapter);
	}
    private boolean checkNet(){
    	if(!HardwareHelper.connectedNetwork(YogaAppContext.getApp())){
    		Builder builder = DialogHelper.showAlertBuilder(Home.this,R.string.no_wifi,R.string.warning);
    		builder.setNeutralButton(R.string.close, new DialogInterface.OnClickListener(){
    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    				clearStatus();
    			}
    		}).show();
    		return false;
    	}else{
    		return true;
    	}
    }
    @Override
	protected void onStart() {
		super.onStart();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK)
			this.clearPlayer();
    	return super.onKeyDown(keyCode, event);
	}
    /*清除player*/
    private void clearPlayer(){
    	if(mp != null){
    		if(mp.isPlaying()){
    			mp.stop();
    			mp.release();
    		}
			mp = null;
		}
    	playHandler.removeCallbacks(playTask);
		
    }
    private Handler playHandler = new Handler();
	private Runnable playTask = new Runnable() {
		@Override
		public void run() {
			int pro = mp.getCurrentPosition();
			for(int i = FlashUrl.chapterTimes.length - 1;i > 0;i --){
				if(pro >= FlashUrl.chapterTimes[i]){
					chapter = i + 1;
					status();
					break;
				}
			}
			if(pro < audioTime){
				playPB.setProgress(pro);
			}else{
				playPB.setProgress(audioTime);
				return;
			}
			playHandler.postDelayed(playTask, 1 * 1000);
		}
	};
   
    /* 初始化player*/
    private void startPlayer(String path){
    	mp = new MediaPlayer(); 
    	try {
			mp.setDataSource(path);
			mp.prepare();
			audioTime = mp.getDuration();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			Builder builder = DialogHelper.showAlertBuilder(Home.this,R.string.no_wifi,R.string.warning);
			builder.setNeutralButton(R.string.close, new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					clearStatus();
				}
			}).show();
			return;
		}
		mp.start();
		mp.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer arg) {
				mp.release();
				playHandler.removeCallbacks(playTask);
				playPB.setProgress(0);
				isPlaying = false;
				mp = null;
				chapter = 1;
				pause.setImageResource(R.drawable.play);
				status();
			}
		});
		this.play();
		this.seekTo();
    }
    private void play(){
    	isPlaying = true;
		playPB.setMax(audioTime);
		playPB.setProgress(0);
		pause.setImageResource(R.drawable.pause);
		this.status();
		new Thread(
				new Runnable() {
					@Override
					public void run() {
						playHandler.post(playTask);
					}
				}).start();
		
    }
    /*初始化组件*/
	private void initComponents(){
		playTitle = (TextView) findViewById(R.id.playTitle_text);
		playPB = (ProgressBar) findViewById(R.id.ProgressBar_playing);
    	pre = (ImageView) findViewById(R.id.pre_img);
    	pause = (ImageView) findViewById(R.id.pause_img);
    	next = (ImageView) findViewById(R.id.next_img);
    	yogaImg = (ImageView) findViewById(R.id.yoga_img);
    	chapters = (Spinner) findViewById(R.id.chapter_spinner);
    	
		chapters.setOnItemSelectedListener(this);
		
    	pre.setOnTouchListener(this);
    	pause.setOnTouchListener(this);
    	next.setOnTouchListener(this);

    }
	private void initSavePath(){
		for(int i = 1;i <= FlashUrl.chaptersNum;i ++){
			String path = FileDownHelper.getSaveImgName(YogaAppContext.getApp(),Integer.toString(i));
			this.saveImgTos[i - 1] = path;
		}
	}
	private void initInstallAudioDlg(int type){
		saveDir = FileDownHelper.getLocalDirectory(YogaAppContext.getApp());
		saveAudioTo = FileDownHelper.getSaveAudioName(YogaAppContext.getApp());
		saveImgTos = new String[FlashUrl.chaptersNum];
		this.initSavePath();
		if(type != 0){
			this.tapInstallDlg();
		}
		
		
	}
	/*初始化弹出下载窗口*/
	private void tapInstallDlg(){
		
		View DialogView = DialogHelper.showOptionDialog(Home.this, R.layout.download);
        dlg = new AlertDialog.Builder(this)
    								.setView(DialogView)
    								.setCancelable(false)
    								.create();
    	dlg.show();
    								
    	this.cancel = (TextView) DialogView.findViewById(R.id.cancel_text);
    	this.cancel.setOnClickListener(this);
    	this.installPB = (ProgressBar) DialogView.findViewById(R.id.ProgressBar_download);
    	long leftSize = 0;
    	long leftMediaSize = 0;
    	if(FileDownHelper.getExternalStoragePath() != null){
    		leftSize = FileDownHelper.getAvailableStore(FileDownHelper.getExternalStoragePath());
    		leftMediaSize = flashSize - FileDownHelper.getDirLength(saveDir);
    	}else{
    		leftSize = FileDownHelper.getAvailableStore(FileDownHelper.getInternalDirectory(getApplication()));
    		leftMediaSize = flashSize - FileDownHelper.getDirLength(saveDir);
    	}
    	if(Config.debug){
    		Log.i(TAG,"leftSize-->" + Long.toString(leftSize) + "flashSize-->" + Long.toString(flashSize) + "leftMediaSize-->" + Long.toString(leftMediaSize));
    	}
    	if(leftSize < leftMediaSize){
    		Builder builder = DialogHelper.showAlertBuilder(Home.this,R.string.notEnoughSize,R.string.warning);
    		builder.setCancelable(true);
    		builder.setNeutralButton(R.string.close, new DialogInterface.OnClickListener(){
    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    				
    			}
    		}).show();
		}else{
			this.installStart();
		}
	}
    /*开始下载动作*/
    private void installStart(){
		this.installPB.setVisibility(View.VISIBLE);
		this.installPB.setMax(flashSize);
		this.installPB.setProgress(0);
		new Thread(
				new Runnable() {
					@Override
					public void run() {
						doWork(); 
					}
				}).start();
		new Thread(
				new Runnable() {
					@Override
					public void run() {
						mHandler.post(mTask); 
					}
				}).start();
    }
	/*显示下载进度条状态*/
	private void doWork(){
		if(FileDownHelper.getFileLength(saveAudioTo) < audioSize){
			FileDownHelper.getFile(FlashUrl.audioUrl, saveAudioTo);
		}
		for(int i = 1;i <= FlashUrl.chaptersNum; i++){
			File f = new File(saveImgTos[i - 1]);
			if(!f.exists()){
				FileDownHelper.getFile(FlashUrl.photoUrls[i - 1], saveImgTos[i - 1]);
			}
		}
	}
	private int mProgressStatus = 0;
	private Handler mHandler = new Handler();
	private Runnable mTask = new Runnable() {
        public void run() {
        	int len = FileDownHelper.getDirLength(saveDir);
            mProgressStatus = len;
            if(mProgressStatus < flashSize){
            	installPB.setProgress(mProgressStatus);
            }else{
            	installPB.setProgress(flashSize);
            }
            if(mProgressStatus >= flashSize){
            	installPB.setVisibility(View.GONE);
    			if(dlg != null){
    				dlg.dismiss();
    				dlg = null;
    			}
    			if(DataBaseOption.installHasData()){
    				DataBaseOption.updateInstallData(1, 1);
    			}
    			status();
    			return;
            }
            mHandler.postDelayed(mTask, 5 * 1000);
        }
    };
    /*关闭当前窗口所做的清理工作*/
    private void clearStatus(){
    	if(dlg != null){
			dlg.dismiss();
			dlg = null;
		}
		if(mHandler != null){
			mHandler.removeCallbacks(mTask);
		}
		finish();
    }
    public boolean onTouch(View v, MotionEvent event) {
    	
    	if(v == pre){
    		if(event.getAction() == MotionEvent.ACTION_DOWN){
    			pre.setImageResource(R.drawable.back_down);
    		}else if(event.getAction() == MotionEvent.ACTION_UP){
    			pre.setImageResource(R.drawable.back);
    			this.playPre();
    		}

    	}
    	if(v == next){
    		if(event.getAction() == MotionEvent.ACTION_DOWN){
    			next.setImageResource(R.drawable.next_down);
    		}else if(event.getAction() == MotionEvent.ACTION_UP){
    			next.setImageResource(R.drawable.next);
    			this.playNext();
    		}
   		
    	}
    	if(v == pause){
    		if(event.getAction() == MotionEvent.ACTION_DOWN){
    			pause.setImageResource(R.drawable.play_down);
    		}else if(event.getAction() == MotionEvent.ACTION_UP){
    			pause.setImageResource(R.drawable.pause);
    			this.pausePlayer();
    		}
   		
    	}
		return true;
    }
   
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.cancel_text:
				this.clearStatus();
				break;
			case R.id.sure_button:
				if(englishRadio.isChecked()){
	    			arg = 0;
	    		}else if(sanskritRadio.isChecked()){
	    			arg = 1;
	    		}else if(spanishRadio.isChecked()){
	    			arg = 2;
	    		}
				this.initTitleSpinner();
				if(dlg != null){
					dlg.dismiss();
					dlg = null;
				}
				break;
			case R.id.cancel_button:
				if(dlg != null){
    				dlg.dismiss();
    				dlg = null;
    			}
				break;
			default:
				break;
		}
		
		
		
	}
	/*开始播放*/
	private void initPlayer(String url){
		this.clearPlayer();
		this.startPlayer(url);
	}
	/*播放上一节*/
	private void playPre(){
		if(chapter == 1){
			DialogHelper.showToastAlert(YogaAppContext.getApp(), R.string.first_chapter).show();
			return;
		}
		chapter -= 1;
		if(mp != null && isPlaying){
			this.seekTo();
		}
		this.status();
	}
	/*暂停播放*/
	private void pausePlayer(){
		if(mp != null){
			if(mp.isPlaying()){
				mp.pause();
				pause.setImageResource(R.drawable.play);
			}else{
				mp.start();
				pause.setImageResource(R.drawable.pause);
			}
		}else{
			this.initPlayer(saveAudioTo);
		}
	}
	/*播放下一节*/
	private void playNext(){
		if(chapter == FlashUrl.chaptersNum){
			DialogHelper.showToastAlert(YogaAppContext.getApp(), R.string.last_chapter).show();
			return;
		}
		chapter += 1;
		if(mp != null && isPlaying){
			this.seekTo();
		}
		this.status();
	}
	private void seekTo(){
		mp.seekTo(FlashUrl.chapterTimes[chapter - 1]);
	}

	private void _status(){
		playTitle.setText(Integer.toString(chapter) + " " + getString(R.string.play_title).toString() + " " + Integer.toString(FlashUrl.chaptersNum));
		Uri imgUri = Uri.parse(saveImgTos[chapter - 1]);
		yogaImg.setImageURI(imgUri);
	}
	private void status(){
		this._status();
		if (chapters.getSelectedItemPosition() != chapter - 1) {
			switchChapter = false;
			chapters.setSelection(chapter - 1);
		}
		
	} 
	/*选章节*/
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		if (!switchChapter) {
			switchChapter = true;
			return;
		}
		chapter = arg2 + 1;	
		this._status();
		if(mp != null && isPlaying){ 
			seekTo();
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	/*menu设置语言界面*/
	private void showLangView(){
		View DialogView = DialogHelper.showOptionDialog(Home.this, R.layout.language_settings);
        dlg = new AlertDialog.Builder(this)
    								.setView(DialogView)
    								.setCancelable(true)
    								.create();
    	dlg.show();
    	languageRadioGroup = (RadioGroup) DialogView.findViewById(R.id.languageRadioGroup);
    	languageRadioGroup.setOnCheckedChangeListener(mChangeRadio);
    	englishRadio = (RadioButton) DialogView.findViewById(R.id.english_radio);
    	sanskritRadio = (RadioButton) DialogView.findViewById(R.id.sanskrit_radio);
    	spanishRadio = (RadioButton) DialogView.findViewById(R.id.spanish_radio);
    	switch(arg){
    	case 0:englishRadio.setChecked(true);break;
    	case 1:sanskritRadio.setChecked(true);break;
    	case 2:spanishRadio.setChecked(true);break;
    	default:englishRadio.setChecked(true);break;
    	}
    	sureBtn = (Button) DialogView.findViewById(R.id.sure_button);
    	cancelBtn = (Button) DialogView.findViewById(R.id.cancel_button);
    	
    	sureBtn.setOnClickListener(this);
    	cancelBtn.setOnClickListener(this);
    	
	}
	/*menu About*/
	private void showAbout(){
		Builder builder = DialogHelper.showAlertBuilder(Home.this,R.string.version,R.string.about_title);
		builder.setCancelable(true);
		builder.setNeutralButton(R.string.close, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		}).show();
	}
	 @Override
	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);

        menu.add(0, Menu.FIRST + 1, 1, R.string.menu_settings).setIcon(R.drawable.ic_menu_settings);
        menu.add(0, Menu.FIRST + 2, 2, R.string.re_download).setIcon(R.drawable.ic_menu_refresh);
        menu.add(0, Menu.FIRST + 3, 3, R.string.menu_about).setIcon(R.drawable.ic_menu_about);
        
        return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){

            case Menu.FIRST + 1:
            	this.showLangView();
                break;
            case Menu.FIRST + 2:
            	this.showReloadDlg();
                break;
            case Menu.FIRST + 3:
            	this.showAbout();
                break;
            default:
                break;
        }
        
        return true;
	}
	
	private RadioGroup.OnCheckedChangeListener mChangeRadio = new
    RadioGroup.OnCheckedChangeListener()
    {
    	@Override
    	public void onCheckedChanged(RadioGroup group, int checkedId){}
    }; 
    
    /*重新下载提示框*/
    private void showReloadDlg(){
    	Builder builder = DialogHelper.showAlertBuilder(Home.this,R.string.reload,R.string.warning);
		builder.setCancelable(true);
		builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(checkNet()){
					clearPlayer();
					isPlaying = false;
					chapter = 1;
					audioSize = 0;
					allImageSize = 0;
					flashSize = 0;
					progressDlg(3);//手动重新下载
				}
			}
		});
		builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {}
		});
		builder.show();
    }
}