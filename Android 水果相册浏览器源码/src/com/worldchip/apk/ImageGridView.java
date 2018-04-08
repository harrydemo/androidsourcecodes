package com.worldchip.apk;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;


import com.dream.hlper.BaseActivity;
import com.dream.hlper.ImageInfo;
import com.dream.hlper.InfoHelper;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ImageGridView extends BaseActivity{

	 private static final String TAG = "ImageGridView";
	 LinkedList<ImageInfo> bitImages=null;
	 String[] albums=null;
	 int flag=2;
	 public GridView gView ;
	 public static  int REQUEST_CODE_GETIMAGE_BYSDCARD = 0;
	 public static  int REQUEST_CODE_GETIMAGE_BYCAMERA = 1;
	 public static  String thisLarge = null, theSmall = null;
	 protected final static int MENU_PHOTO = Menu.FIRST;
	 protected final static int MENU_CHANGE = Menu.FIRST + 1;

		
	 @Override  
	 public void onCreate(Bundle savedInstanceState) {  
	    super.onCreate(savedInstanceState);  
	    setContentView(R.layout.grid_view);  
	    gView = (GridView) this.findViewById(R.id.gridview);
	    try{
			//皮肤记录  
			 if(DreamerViewActivity.skin==0){
			    	System.gc();
			    	 setContentView(R.layout.grid_view1);  
			    	 gView = (GridView) this.findViewById(R.id.gridview1);
					
			    }
			 else if(DreamerViewActivity.skin==1){
			    	System.gc();	
			    	 setContentView(R.layout.grid_view2);  
			    	 gView = (GridView) this.findViewById(R.id.gridview2);
			
			    }
			    else if(DreamerViewActivity.skin==2){
			    	System.gc();
			    	 setContentView(R.layout.grid_view3);  
			    	 gView = (GridView) this.findViewById(R.id.gridview3);
				
			    }
			    else if(DreamerViewActivity.skin==3){
			    	System.gc();		  
			    	 setContentView(R.layout.grid_view4);  
			    	 gView = (GridView) this.findViewById(R.id.gridview4);
				
			    }
			    else if(DreamerViewActivity.skin==4){
			    	System.gc();
			    	 setContentView(R.layout.grid_view5);  
			    	 gView = (GridView) this.findViewById(R.id.gridview5);
					
			    }
			    else if(DreamerViewActivity.skin==5){
			    	System.gc();
			    	 setContentView(R.layout.grid_view6);  
			    	 gView = (GridView) this.findViewById(R.id.gridview6);		  
					
			    }
			    else if(DreamerViewActivity.skin==6){
			    	System.gc();
			    	 setContentView(R.layout.grid_view7);  
			    	 gView = (GridView) this.findViewById(R.id.gridview7);		   
					
			       }
		        }catch(Exception e){System.out.println(e);}
	    bitImages=new  LinkedList<ImageInfo>();   
	    Intent intent = getIntent();  
        final String path = intent.getStringExtra("path"); 
        String name = intent.getStringExtra("name");  
        flag=intent.getIntExtra("flag",1);
        albums=intent.getStringArrayExtra("data");
        Log.i("ImageGridView_onCreate", "flag="+flag+";path="+path+"; name="+name);
        
        setTitle((String) getText(R.string.album)+":"+name+"     "
        		+(String) getText(+R.string.path)+":"+path);

        gView.setAdapter(new GridViewAdapter(this,flag, name, getNames(flag, albums)));
        
	    gView.setOnItemClickListener(new OnItemClickListener(){  
	           public void onItemClick(AdapterView<?> arg0, View arg1, int position,long id) 
	           {  
	                 //Log.i("GridView.setOnItemClickListener", "position="+position);
	                 //String path=images.get(position).path;
	        		 //Log.i("ImageListView_onListItemClick", "the path="+path);

	        		 ArrayList<String> pathArray=new ArrayList<String>();
	        		 for(int i=0; i<albums.length; i++)
	        		 {
	        			 if(flag==2)
	        			 {
	        				 pathArray.add(albums[i]);
	        			 }
	        			 else
	        			 {
	        			     String absolutePath=albums[i].split("&")[1];
	        			     Log.i(TAG, "absolutePath="+absolutePath);
	        			     pathArray.add(absolutePath);
	        			 }
	        		 }
	        		 
	        		 Intent intent = new Intent();  
	        		 intent.setClass(ImageGridView.this, ImageGalleryView.class);  
	        		 intent.putExtra("path", path);
	        		 intent.putExtra("id", position);
	        		 intent.putExtra("data", (String[])pathArray.toArray(new String[pathArray.size()]));
	        		 Log.i("ImageGridView_setOnItemClickListener", "position="+position+"; path="+path);
	        		 ImageGridView.this.startActivity(intent); 
	           }    
	     });  
	   
		     
	 }

	//Menu菜单的功能实现
		public boolean onOptionsItemSelected(MenuItem item) {
			super.onOptionsItemSelected(item);
			switch (item.getItemId()) {
			case MENU_PHOTO://拍照
				Intent takephoto = new Intent("android.media.action.IMAGE_CAPTURE");	
				
				String camerName = InfoHelper.getFileName();
				String fileName = "Share" + camerName + ".tmp";	
				
				File camerFile = new File( InfoHelper.getCamerPath(), fileName );
						
				theSmall = InfoHelper.getCamerPath() + fileName;
				thisLarge = getLatestImage();
				
				Uri originalUri = Uri.fromFile( camerFile );
				takephoto.putExtra(MediaStore.EXTRA_OUTPUT, originalUri); 	
				startActivityForResult(takephoto, REQUEST_CODE_GETIMAGE_BYCAMERA);
			

				break;
		
				
	        case MENU_CHANGE://返回上一步
				   
	        	System.gc();
	        	System.exit(0);
				break;
			
	    
			}
			return true;
		}

		public boolean onCreateOptionsMenu(Menu menu) {
	            
			super.onCreateOptionsMenu(menu);
			
			SubMenu sub=menu.addSubMenu("菜单条目");
			sub.setIcon(android.R.drawable.ic_menu_gallery);
			sub.add(0,MENU_PHOTO,0,"拍照");
			sub.add(0,MENU_CHANGE,0,"返回上一步");
			
			return true;
			
			
		}

/*	private int[] getRowIds(String[] albums)
	{
		int[] ids=new int[albums.length];
		for(int i=0; i<albums.length; i++)
		{
			
			String id=albums[i].split("&")[0];
			ids[i]=Integer.valueOf(id);
		}
		return ids;
	}*/
	
	private String[] getNames(int flag, String[] albums)
	{
		if(flag==0)
		{
			Log.i(TAG, "----code comes to here----");
		   String[] paths=new String[albums.length];
		   String path=null;
		   String name=null;
		   for(int i=0; i<albums.length; i++)
		   {
			 path=albums[i].split("&")[1];
			 name=path.substring(path.lastIndexOf("/")+1);
			 Log.i(TAG, "path="+path+"; name="+name);
			 paths[i]=name;
		   }
		   return paths;
		}
		else if(flag==1)
		{
			String[] ids=new String[albums.length];
			for(int i=0; i<albums.length; i++)
			{
				String id=albums[i].split("&")[0];
				ids[i]=id;
			}
			return ids;
		}
		else 
			return albums;
	}
}
