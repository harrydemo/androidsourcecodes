package Ye.Mp3TagTestActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SearchFileActivity extends ListActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_file);
		
		pathList = new ArrayList<String>();
		list = new ArrayList<HashMap<String,String>>();
		
		searchFile(SDCardRoot);
	}
	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) 
	{
		super.onListItemClick(l, v, position, id);
		String path = pathList.get(position);
		File f = new File(path);
		if(f.isFile())
		{
			if(f.getName().endsWith(".mp3"))
			{
				Intent intent = new Intent();
				intent.setAction(AppConstant.LOAD_MP3_PATH_ACTION);
				intent.putExtra("path", f.getPath());
				sendBroadcast(intent);
				SearchFileActivity.this.finish();
			}
		}
		else
			searchFile(path);
	}

	private List<String> pathList;
	private List<HashMap<String, String>> list;
	
	private static final String SDCardRoot = Environment.getExternalStorageDirectory() + "";
	
	private void searchFile(String pathName)
	{
		File f = new File(pathName);
		if(f.isDirectory())
		{
			pathList.clear();
			list.clear();
			
			if(!f.getPath().equals(SDCardRoot))
			{
				pathList.add(SDCardRoot);
				pathList.add(f.getParent());
				
				HashMap<String, String> map1 = new HashMap<String, String>();
				map1.put("fileName", "back to/");
				map1.put("fileAttr", "文件夹");
				list.add(map1);
				
				HashMap<String, String> map2 = new HashMap<String, String>();
				map2.put("fileName", "back to.");
				map2.put("fileAttr", "文件夹");
				list.add(map2);
			}
			
			File[] fileList = f.listFiles();
			for (File file : fileList) 
			{
				pathList.add(file.getPath());
				
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("fileName", file.getName());
				if(file.isDirectory())
					map.put("fileAttr", "文件夹");
				else
				{
					map.put("fileAttr", "文件");
				}
				list.add(map);
			}
			
			SimpleAdapter simpleAdapter = new SimpleAdapter(SearchFileActivity.this, list, R.layout.list_item, new String[]{"fileName", "fileAttr"}, new int[]{R.id.fileName, R.id.fileAttr});
			SearchFileActivity.this.setListAdapter(simpleAdapter);
		}
	}
}
