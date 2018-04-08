package com.xjf.filedialog;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.xjf.utils.Common;
/**
 * 设置界面的处理
 * */
public class SettingsView {
	public final static String tag = FileManager.tag;
	
	private FileManager fileManager;
	/** 设置主界面layout*/
	RelativeLayout settingsLayout = null;
	/** 备份目录输入框*/
	//private EditText backupEdit = null;
	/** 备份layout*/
	private LinearLayout apkText = null;
	/** 备份目录显示*/
	private TextView backupDir = null;
	
	private CheckBox hideFileCheckBox = null;
	private CheckBox rootCheckBox = null;
	private CheckBox showFileSizeCheckBox = null;
	private CheckBox showFileDateCheckBox = null;
	private boolean preHideFileState = false;
	private TextView helptv = null;

	
	private final static String SP_BUACKUPDIR = "backupdir";
	private final static String SP_HIDEFILE = "hidefile";
	private final static String SP_ROOT = "sproot";
	
	private Animation settingsViewShowAnimation = null;
	private Animation viewHideAnimation = null;
	
	public SettingsView(FileManager fd) {
		init(fd);
		// TODO Auto-generated constructor stub
	}
	
	private void init(FileManager fd){
		fileManager = fd;

		settingsLayout = (RelativeLayout) fileManager.findViewById(R.id.settingslayout);
		settingsLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		initButton();
		initApkBackupView();
		initApkText();
		initCheckBox();
		initHelpView();
	}
	
	public void setBackupDir(String dir) {
		File d = new File(dir);
		if (FileManager.D) Log.d(tag, "dir: " + dir); 
		if (d.exists() || d.mkdir()) {
			if (FileManager.D) Log.d(tag, "dir: " + dir); 
			backupDir.setText(dir);
			fileManager.preBackupDir = dir;
		}
	}
	/**
	 * 显示设置界面时,保存当前设置
	 * */
	public void saveState() {
		preHideFileState = fileManager.isHideFile();
		fileManager.preBackupDir = fileManager.getApkBackupDir();
	}
	
	private boolean isShowFileSize;
	private boolean isShowFileDate;
	/** 取消设置时,还原显示设置界面前的状态*/
	public void restoreState() {
		hideFileCheckBox.setChecked(preHideFileState);
		backupDir.setText(fileManager.preBackupDir);
	}
	
	/** 显示设置界面*/
	public void show(Bundle savedInstanceState) {

		settingsLayout.setVisibility(View.VISIBLE);
		saveState();
		if (savedInstanceState != null)
			restoreInstanceState(savedInstanceState);
		if (settingsViewShowAnimation == null) {
			settingsViewShowAnimation = AnimationUtils
				.loadAnimation(fileManager, R.anim.scalesize);
		}
		settingsLayout.setVisibility(View.VISIBLE);
		settingsLayout.startAnimation(settingsViewShowAnimation);
		
		refreshSDView();
		refreshDataView();
	}
	
	/** 隐藏设置界面*/
	public void hide() {

		if (viewHideAnimation == null)
			viewHideAnimation = AnimationUtils.loadAnimation(
					fileManager, R.anim.settingshide);
		settingsLayout.startAnimation(viewHideAnimation);
		settingsLayout.setVisibility(View.GONE);
	}
	
	/** 转屏或被android回收时*/
	public void saveInstanceState(Bundle outState){
		outState.putBoolean(SP_HIDEFILE, hideFileCheckBox.isChecked());
		outState.putString(SP_BUACKUPDIR, backupDir.getText().toString());
		outState.putBoolean(SP_ROOT, rootCheckBox.isChecked());
	}
	
	public void restoreInstanceState(Bundle state){
		rootCheckBox.setChecked(state.getBoolean(SP_ROOT));
		hideFileCheckBox.setChecked(state.getBoolean(SP_HIDEFILE));
		backupDir.setText(state.getString(SP_BUACKUPDIR));
		//adapter.notifyDataSetChanged();
	}
	
	
	
	public void initButton(){

		Button okButton = (Button) fileManager.findViewById(R.id.settingok);
		Button cancelButton = (Button) fileManager.findViewById(R.id.settingcancel);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				restoreState();
				fileManager.hideSettingsView();
			}
		});
		
		okButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				fileManager.hideSettingsView();
				if (isChagnedHideFileState())
					fileManager.setHideFile(hideFileCheckBox.isChecked());
				String dir = backupDir.getText().toString();
				if (!fileManager.preBackupDir.equals(dir))
					fileManager.setApkBackupDir(dir);
				fileManager.setShowFileSize(showFileSizeCheckBox.isChecked());
				fileManager.setShowFileDate(showFileDateCheckBox.isChecked());
			}
		});
	}

	
	private boolean isChagnedHideFileState() {
		return hideFileCheckBox.isChecked() != preHideFileState;
	}
	private void initApkBackupView() {
		if (backupDir == null) {
			backupDir = (TextView) settingsLayout.findViewById(R.id.apkbackuppath);
			backupDir.setText(fileManager.getApkBackupDir());
		}
	}
	
	private void initApkText(){
		apkText = (LinearLayout) settingsLayout.findViewById(R.id.settingapkbackup);
		apkText.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FileManager.ACTION_GET_DIRECTORY);
				intent.putExtra(FileManager.EXTR_GET_NAME, Common.getPathName2(fileManager.preBackupDir));
				
				fileManager.startActivityForResult(
						intent, 
						FileManager.GET_DIRECTORY);
			}
		});
		
	}
	OnCheckedChangeListener checkBoxChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if (rootCheckBox.equals(buttonView)) {
				rootCheckBox.setChecked(fileManager.changedRoot(isChecked));
			} else if (showFileSizeCheckBox.equals(buttonView)) {
				isShowFileSize = isChecked;
			} else if (showFileDateCheckBox.equals(buttonView)) {
				isShowFileDate = isChecked;
			}
		}
		
	};
	private void initCheckBox() {
		if (hideFileCheckBox == null){
			hideFileCheckBox = (CheckBox) settingsLayout.findViewById(R.id.settinghidefilebox);
			/**
			ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			hideFileCheckBox.setLayoutParams(p);
			/***/
			hideFileCheckBox.setGravity(Gravity.FILL);
			hideFileCheckBox.setFocusable(false);
			hideFileCheckBox.setChecked(fileManager.isHideFile());
		}
		if (rootCheckBox == null) {
			rootCheckBox = (CheckBox) settingsLayout.findViewById(R.id.settingrootbox);
			rootCheckBox.setChecked(fileManager.isRoot());
			rootCheckBox.setOnCheckedChangeListener(checkBoxChangeListener);
		}
		
		if (showFileSizeCheckBox == null) {
			showFileSizeCheckBox = (CheckBox) settingsLayout.findViewById(R.id.settingfilesizebox);
			showFileSizeCheckBox.setChecked(fileManager.showFileSize());
			showFileSizeCheckBox.setOnCheckedChangeListener(checkBoxChangeListener);
		}
		
		if (showFileDateCheckBox == null) {
			showFileDateCheckBox = (CheckBox) settingsLayout.findViewById(R.id.settingfiledatebox);
			showFileDateCheckBox.setChecked(fileManager.showFileDate());
			showFileDateCheckBox.setOnCheckedChangeListener(checkBoxChangeListener);
		}
		
	}
	private TextView sdText = null,
					dataText = null;
	private String sdPath = null,
					dataPath = null;
	private void refreshSDView(){
		if (sdText == null) {
			sdText = (TextView) settingsLayout.findViewById(R.id.sdtext);
			sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		}
		if (sdPath == null) {
			sdText.setText("SD卡不存在");
			return;
		}
		StatFs sf = new StatFs(sdPath);
		long bs = sf.getBlockSize();
		long total = sf.getBlockCount() * bs; 
		long free = sf.getAvailableBlocks() * bs;
		sdText.setText("总容量: " + Common.formatFromSize(total)
				+ "  空闲: " + Common.formatFromSize(free));
	}
	
	private void refreshDataView(){
		if (dataPath == null) {
			dataText = (TextView) settingsLayout.findViewById(R.id.datatext);
			dataPath = Environment.getDataDirectory().getAbsolutePath();
		}
		StatFs sf = new StatFs(dataPath);
		long bs = sf.getBlockSize();
		long total = sf.getBlockCount() * bs; 
		long free = sf.getAvailableBlocks() * bs;
		dataText.setText("总容量: " + Common.formatFromSize(total)
				+ "  空闲: " + Common.formatFromSize(free));
	}
	
	private void initHelpView(){
		if (helptv == null){
			helptv = (TextView) settingsLayout.findViewById(R.id.settingabouthelp);
			helptv.setGravity(Gravity.FILL);
			//helptv.setBackgroundResource(R.drawable.pressed_background);
			helptv.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					AlertDialog.Builder b = new AlertDialog.Builder(fileManager);
					InputStream ips = fileManager.getResources().openRawResource(R.raw.readme);
					//BufferedReader br = new BufferedReader(new InputStreamReader(ips));
					DataInputStream dis = new DataInputStream(ips);
					try {
						byte[] bytes = new byte[dis.available()];
						String str = "";
						while (ips.read(bytes) != -1)
							str = str + new String(bytes, "GBK");
						//StringBuffer str = new StringBuffer();
						//while(br.ready())
						//	str.append(br.readLine());
						b.setTitle("关于/帮助").setMessage(str);
						b.setPositiveButton(fileManager.ok, null);
						b.create().show();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						try {
							dis.close();
							ips.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
		}
	}

}
