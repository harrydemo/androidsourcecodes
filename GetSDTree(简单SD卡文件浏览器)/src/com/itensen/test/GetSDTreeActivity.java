package com.itensen.test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class GetSDTreeActivity extends Activity {
	//根目录
	File sdRoot;
	//存放当前路径下的所有文件及文件夹
	File[] datas;
	//当前路径
	File nowFile;
	//当前长按的文件/文件夹
	File longFile;
	ListView listView;
	int lastPoint=0;
	boolean isBack=false;
	MyAdapater adapater;
	//匹配图标时用到的文件类型
	String[] fileTypes=new String[]{"apk","avi","bat","bin","bmp","chm","css","dat","dll","doc","docx","dos","dvd","gif","html","ifo","inf","iso"
	,"java","jpeg","jpg","log","m4a","mid","mov","movie","mp2","mp2v","mp3","mp4","mpe","mpeg","mpg","pdf","php","png","ppt","pptx","psd","rar","tif","ttf"
	,"txt","wav","wma","wmv","xls","xlsx","xml","xsl","zip"
	};
	//长按弹出菜单的内容
	String[] menus=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        listView =(ListView)findViewById(R.id.list);
        
        listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				File clickFile=datas[arg2];
				Log.i("test", clickFile.getName());
				if(clickFile.isDirectory()){
					lastPoint=arg2;
					loadFiles(clickFile);
				}else{
					openFile(clickFile);
				}
			}
        });
        listView.setOnItemLongClickListener(new OnItemLongClickListener(){
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				File clickFile=datas[arg2];
				Log.i("test", clickFile.getName());
				longFile=clickFile;
				if(clickFile.isDirectory()){
					menus=new String[]{"删除","重命名"};
					Intent openMenu=new Intent(GetSDTreeActivity.this,MenuActivity.class);
					openMenu.putExtra("menus", menus);
					startActivityForResult(openMenu, 1);
				}else{
					menus=new String[]{"删除","重命名"};
					Intent openMenu=new Intent(GetSDTreeActivity.this,MenuActivity.class);
					openMenu.putExtra("menus", menus);
					startActivityForResult(openMenu, 2);
				}
				return false;
			}
        	
        }); 
        
        sdRoot=new File("/sdcard");
        if(sdRoot.exists()){
			loadFiles(sdRoot);
        }
        
    }
    void loadFiles(File directory){
    	nowFile=directory;
    	setTitle(nowFile.getPath());
    	//分类并排序
    	File[] temp=directory.listFiles();
    	ArrayList<File> tempFolder=new ArrayList<File>();
    	ArrayList<File> tempFile=new ArrayList<File>();
    	for(int i=0;i<temp.length;i++){
    		File file=temp[i];
    		if(file.isDirectory()){
    			tempFolder.add(file);
    		}else if(file.isFile()){
    			tempFile.add(file);
    		}
    	}
    	//对List进行排序
    	Comparator<File> comparator=new MyComparator(); 
    	Collections.sort(tempFolder, comparator);
    	Collections.sort(tempFile, comparator);
    	
    	datas=new File[tempFolder.size()+tempFile.size()];
    	System.arraycopy(tempFolder.toArray(), 0, datas, 0, tempFolder.size());
    	System.arraycopy(tempFile.toArray(), 0, datas,tempFolder.size(), tempFile.size());
    	//数据处理结束 =========================================
    	adapater=new MyAdapater(GetSDTreeActivity.this);
    	listView.setAdapter(adapater);
    	if (isBack) {
    		//listView.setSelection(lastPoint);
    		listView.smoothScrollToPosition(lastPoint);
    		lastPoint=0;
    		isBack=false;
		}
    	
    	
    	//adapater.notifyDataSetChanged();
    } 
    //自定义比较器
    class MyComparator implements Comparator<File>{
		@Override
		public int compare(File lhs, File rhs) {
			return lhs.getName().compareTo(rhs.getName());
		}
    	
    }
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add("新建");
    	menu.add("退出");
		return super.onCreateOptionsMenu(menu);
	}
    
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if(item.getTitle().toString().equals("退出")){
			GetSDTreeActivity.this.finish();
			System.exit(1);
		}else if(item.getTitle().toString().equals("新建")){
			String name=System.currentTimeMillis()+"";
			File file=new File(nowFile.getPath()+"/"+name.substring(name.length()-5));
			try {
				boolean finish=file.createNewFile();
				loadFiles(nowFile);
				if(finish){
					Toast.makeText(this, "随机文件已生成！", Toast.LENGTH_SHORT).show();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return super.onMenuItemSelected(featureId, item);
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			String parent=nowFile.getParent();
			if(parent.equals("/")){
				return true;
			}
			isBack=true;
			loadFiles(new File(parent));
			Log.i("test",parent);
			return true;
		default:
			break;
		}
		return super.onKeyUp(keyCode, event);
	}
	private void openFile(File f) 
    {
      Intent intent = new Intent();
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      intent.setAction(android.content.Intent.ACTION_VIEW);
      /* 调用getMIMEType()来取得MimeType */
      String type = getMIMEType(f);
      /* 设置intent的file与MimeType */
      intent.setDataAndType(Uri.fromFile(f),type);
      startActivity(intent); 
    }
    /* 判断文件MimeType的method */
    private String getMIMEType(File f) 
    { 
      String type="";
      String fName=f.getName();
      /* 取得扩展名 */
      String end=fName.substring(fName.lastIndexOf(".")
      +1,fName.length()).toLowerCase(); 
      
      /* 依扩展名的类型决定MimeType */
      if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")||
      end.equals("xmf")||end.equals("ogg")||end.equals("wav"))
      {
        type = "audio"; 
      }
      else if(end.equals("3gp")||end.equals("mp4"))
      {
        type = "video";
      }
      else if(end.equals("jpg")||end.equals("gif")||end.equals("png")||
      end.equals("jpeg")||end.equals("bmp"))
      {
        type = "image";
      }
      else if(end.equals("apk")) 
      { 
        /* android.permission.INSTALL_PACKAGES */ 
        type = "application/vnd.android.package-archive"; 
      } 
      else if(end.equals("txt")||end.equals("java")) 
      { 
        /* android.permission.INSTALL_PACKAGES */ 
        type = "text"; 
      } 
      else
      {
        type="*";
      }
      /*如果无法直接打开，就跳出软件列表给用户选择 */
      if(end.equals("apk")) 
      { 
      } 
      else 
      { 
        type += "/*";  
      } 
      return type;  
    } 
    
    
    // TODO onActivityResult
 @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==RESULT_OK){
			String exec=data.getStringExtra("exec");
			switch (requestCode) {
			//文件夹
			case 1:
				if(exec.equals("删除")){
					if(longFile!=null){
						if(longFile.exists()){
							try {
								File file=new File(longFile.getPath());
								deleteFolder(file);
								if(allFiles.size()==0){
									boolean deleted= file.delete();
									if(deleted)
										Log.i("test",longFile.getName()+"删除成功");
									
								}else{
									Log.i("test", "还有"+allFiles.size()+"个文件没删除");
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							
						}
					}
				}else if(exec.equals("重命名")){
					String path=longFile.getPath();
					String name=data.getStringExtra("name");
					//String fileType=path.substring(path.lastIndexOf("."));
					File newFile=new File(longFile.getParent()+"/"+name);
					longFile.renameTo(newFile);
				}
				break;
			//文件	
			case 2:
				if(exec.equals("删除")){
					if(longFile!=null){
						if(longFile.exists()){
							try {
								File file=new File(longFile.getPath());
								boolean deleted= file.delete();
								if(deleted)
									Log.i("test",longFile.getName()+"删除成功");
								
							} catch (Exception e) {
								e.printStackTrace();
							}
							
						}
					}
				}else if(exec.equals("重命名")){
					String path=longFile.getPath();
					String name=data.getStringExtra("name");
					int index=path.lastIndexOf(".");
					String fileType="";
					if(index!=-1){
						fileType=path.substring(index);
					}
					File newFile=new File(longFile.getParent()+"/"+name+fileType);
					longFile.renameTo(newFile);
				}
				break;
			default:
				break;
			}
			loadFiles(nowFile);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
 ArrayList<File> allFiles=new ArrayList<File>();
    void getAllFiles(File path){
    	//递归取得目录下的所有文件及文件夹
    	File[] files=path.listFiles();
    	for(int i=0;i<files.length;i++){
    		File file=files[i];
    		allFiles.add(file);
    		if(file.isDirectory()){
    			getAllFiles(file);
    		}
    	}
    	Log.i("test", allFiles.size()+"");
    }
    //TODO deleteFolder
    void deleteFolder(File path){
    	getAllFiles(path);
    	while (allFiles.size()!=0) {
    		for(int i=0;i<allFiles.size();i++){
        		File file=allFiles.get(i);
        		if(file.isFile()){
        			boolean deleted= file.delete();
        			if(deleted)
        				allFiles.remove(i);
        		}else if(file.isDirectory()){
        			try {
        				boolean deleted= file.delete();
            			if(deleted)
            				allFiles.remove(i);
    				} catch (Exception e) {
    					e.printStackTrace();
    				}
        		}
        		
        	}
			
		}
    	
    }
 

	// 自定义的BaseAdapter实现
 	class ViewHolder {
 		ImageView typeView;
 		TextView nameView;
 	}

 // TODO --Class-- MyAdapater 自定义适配器
 	private class MyAdapater extends BaseAdapter {
 		LayoutInflater mInflater;
 		public MyAdapater(Context context) {
 			mInflater = LayoutInflater.from(context);
 		}

 		// TODO getView
 		@Override
 		public View getView(int position, View convertView, ViewGroup parent) {
 			ViewHolder holder;
 			if (convertView == null) {
 				convertView = mInflater.inflate(R.layout.item,
 						null);
 				holder = new ViewHolder();
 				holder.typeView = (ImageView) convertView
 						.findViewById(R.id.image_type);
 				holder.nameView = (TextView) convertView
 						.findViewById(R.id.text_name);
 				convertView.setTag(holder);
 			} else {
 				holder = (ViewHolder) convertView.getTag();
 			}
 			// 从arraylist集合里取出当前行数据；
 			File file=datas[position];

 			// 为页面控件设置数据
 			if(file.isDirectory()){
 				holder.typeView.setImageResource(R.drawable.folder);
 			}else{
 				holder.typeView.setImageResource(R.drawable.file);
 				String name=file.getName();
 				int pointIndex=name.lastIndexOf(".");
 				if(pointIndex!=-1){
 				String type=name.substring(pointIndex+1).toLowerCase();
 					for (int i = 0; i < fileTypes.length; i++) {
						if(type.equals(fileTypes[i])){
							try {
								int resId = getResources().getIdentifier(type, "drawable" , getPackageName());
								holder.typeView.setImageResource(resId);
							} catch (Exception e) {
								e.printStackTrace();
							}
							
						}
					}
 				}
 				
 			}
 			holder.nameView.setText(file.getName());
 			// 标示当前行是否被选中（通过显隐selectView实现）
 			return convertView;
 		}

 		@Override
 		public int getCount() {
 			return datas.length;
 		}

 		@Override
 		public Object getItem(int position) {
 			return datas[position];
 		}

 		@Override
 		public long getItemId(int position) {
 			return position;
 		}

 	}
}