package cn.com.mythos.android;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.mythos.android.Contents.Contents;
import cn.com.mythos.android.Contents.Utils;
import cn.com.mythos.touhoucartoonreader.R;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class SDcardActivity extends BaseActivity{
	private Button goBack;
	private ListView filesList;
	private Button sdcard;
	private Button fileParent;
	private String path = Utils.getSDCardPath();
	private String fileParentPath;
	private Bundle bundle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_sdcard_directory_list);
		goBack = (Button) findViewById(R.id.goBack);
		goBack.setOnClickListener(goBackButton);
		sdcard = (Button) findViewById(R.id.sdcard);
		sdcard.setOnClickListener(sdcardButton);
		filesList = (ListView) findViewById(R.id.filesList);
		fileParent = (Button) findViewById(R.id.fileParent);
		fileParent.setOnClickListener(parentDirectorButton);
		
		refreshListItems(path);
	}
	
	//goBackButton按键的监听,返回图片浏览窗口
	private Button.OnClickListener goBackButton = new Button.OnClickListener() {
		
		public void onClick(View v) {
			String picPath = getPicPath();
			Intent intent = new Intent(SDcardActivity.this, MainActivity.class);
			if(picPath != null) {
				bundle = new Bundle();
				bundle.putString("picPath", picPath);
				intent.putExtras(bundle);
			}
			startActivity(intent);
			finish();
		}
	};
	
	//返回图片路径，该路径是访问当前窗口之前MainActivity正在浏览的图片
	private String getPicPath() {
		bundle = this.getIntent().getExtras();
		if(bundle != null && bundle.size() > 0) {
			return bundle.getString("picPath");
		}
		return null;
	}
	
	//打开SDCard目录
	private View.OnClickListener sdcardButton = new View.OnClickListener() {
		
		public void onClick(View v) {
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				File sdCardDir = Environment.getExternalStorageDirectory();
				String sdCardPath = sdCardDir.getAbsolutePath();
				File file =new File(sdCardPath);
				fileParentPath=file.getParent();
				goToChild(sdCardPath);
			}else{
				showMsg(R.string.sdcard_no_sdcard);
			}
			
		}
	};
	
	//刷新并显示SDCard窗口
	private void refreshListItems(String path) {
		List<Map<String, Object>> list = buildListForSimpleAdapter(path);
		SimpleAdapter spAdapter = new SimpleAdapter(this, list, R.layout.layout_sdcard_directory, new String[] {"img", "name", "path"}, new int[] {R.id.img, R.id.name, R.id.path});
		filesList.setAdapter(spAdapter);
		filesList.setOnItemClickListener(openChildFolder);
	}
	
	//显示文件目录
	private List<Map<String, Object>> buildListForSimpleAdapter(String path) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		File[] files = Utils.getPicOrder(path);
		File parent = new File(path);
		if(parent.getParent() != null && ! "".equals(parent.getParent())) {
			fileParentPath = path;
			Map<String, Object> root = new HashMap<String, Object>();
			root.put("name", getString(R.string.sdcard_goback_fatherDir).toString());
			root.put("img", "");
			root.put("path", parent.getParent());
			list.add(root);
		}
		if(files != null && files.length > 0) {
			for(File file : files) {
				if("/".equals(file.getParent())) {
					fileParent.setText(R.string.sdcard_is_root_dir);
					fileParent.setEnabled(false);
				}else {
					fileParent.setText(R.string.sdcard_parentDirector);
					fileParent.setEnabled(true);
				}
				Map<String, Object> folder = new HashMap<String, Object>();
				if(file.isDirectory()) {
					folder.put("img", R.raw.folder);
					folder.put("name", file.getName());
					folder.put("path", file.getPath());
					list.add(folder);
				}else if(Utils.getFileExt(file.getPath())) {
					folder.put("img", R.raw.pic);
					folder.put("name", file.getName());
					folder.put("path", file.getPath());
					list.add(folder);
				}
			}
		}
		return list;
	}
	
	//获取到图片列表
	public void getPicPath(String picPath) {
		if(picPath != null && !"".equals(picPath)) {
			String fileParentPath = new File(picPath).getParent();
			File[] files = new File(fileParentPath).listFiles();
			if(files != null && files.length > 0) {
				savePicPath(fileParentPath);
			}
			Intent intent = new Intent(SDcardActivity.this, MainActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("picPath", picPath);
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
		}
	}
	
	//保存浏览图片的文件夹路径，将其记录在SDCard中的/cartoonReader/temp/recentHistory.txt文件中
	public void savePicPath(String picsPath) {
		Utils.saveFile(Contents.FILENAME, picsPath, false);
	}
	
	//跳转到子目录
	public void goToChild(String path) {
		try{
		refreshListItems(path);
		}catch (Exception e) {
			showMsg(R.string.sdcard_no_insert);
		}
	}
	
	//跳转到上级目录
	public void goToParent(String path) {
		if(path != null && !"/".equals(path)) {
			File files = new File(path);
			if(files.isDirectory()) {
				refreshListItems(files.getParent());
			}
			
		}
	}
	
	//信息提示
	public void showMsg(int id) {
		Toast.makeText(SDcardActivity.this, id, Toast.LENGTH_SHORT).show();
	}
	
	//openChildFolder的按键监听，用来获取文件目录
	public ListView.OnItemClickListener openChildFolder = new ListView.OnItemClickListener() {

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Map map = (Map) parent.getItemAtPosition(position);
			String path = (String) map.get("path");
			File file = new File(path);
			if(Utils.getFileExt(path)) {
				getPicPath(file.getPath());
			}else if (file.isDirectory()) {
				goToChild(path);
			}else {
				goToParent(path);
			}
		}
	};
	
	//parentDirectorButton的按键监听，用来返回到上一级目录
	public View.OnClickListener parentDirectorButton = new View.OnClickListener() {
		
		public void onClick(View v) {
			if(fileParentPath != null && !"/".equals(fileParentPath)) {
				goToParent(fileParentPath);
			}else {
				showMsg(R.string.sdcard_root_dir);
			}
		}
	};
}
