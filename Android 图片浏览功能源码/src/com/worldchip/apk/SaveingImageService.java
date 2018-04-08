package com.worldchip.apk;

import java.io.File;
import java.io.FileFilter;
import java.util.LinkedList;

import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;

public class SaveingImageService extends Service{

	
	private static final String TAG = "SaveingImageService";
	private DBAdapter dbAdapter = null;
	private final String INTERNAL="/flash/";
	private LinkedList<String> extens=null;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreated started!");
        
        dbAdapter = new DBAdapter(this);
		dbAdapter.open();
		dbAdapter.deleteAllImage();
		
        extens=new LinkedList<String>();
        getExtens();
        
        File file=new File(INTERNAL);
        saveImageFile(file);
			
        dbAdapter.close();
        
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy started!");
    }
    

	public void getExtens()
	{
		extens.add(".JPEG");
		extens.add(".JPG");
		extens.add(".PNG");
		extens.add(".GIF");
		extens.add(".BMP");
	}
	
	  public void saveImageFile(File file){
		Log.i(TAG+"_before accept", "code comes to saveImageFile, the name="+file.getName()+"the path="+file.getAbsolutePath());
	  	file.listFiles(new FileFilter(){
				public boolean accept(File file) {
					String name = file.getName();
					int i = name.lastIndexOf('.');
					if(i != -1){
						name = name.substring(i).toUpperCase();
						if(extens.contains(name)){
							Log.i(TAG+"_accept-file", "for ready to savePicture! name="+file.getName()
									 +"; the path="+file.getAbsolutePath());
							
							savePicture(file);
							return true;
						}
					}else if(file.isDirectory()){
						saveImageFile(file);
					}
					return false;
				}

				private void savePicture(File file) {
					// TODO Auto-generated method stub
					String name=file.getName();
					String album=file.getParent();
					String path=file.getAbsolutePath();
					Log.i(TAG+"_savePicture", "the name="+name+"; the parent="+album+"; the path="+path);

                    album=album.substring(album.lastIndexOf("/")+1);
					Log.i(TAG, "after sub, the album="+album);
					
					//Thumbnail Picture
					Bitmap bitmap=ImageCommon.getFitSizePicture(file);
					if(bitmap==null)
					{
					    Resources res=getResources();
					    bitmap=BitmapFactory.decodeResource(res, R.drawable.icon);
					}
					long rowId=dbAdapter.insertImage(name, album, path, bitmap);
					
				    Log.i(TAG, "after inserted the rowid="+rowId);
				    if(rowId==-1)
				    {
				    	Log.i(TAG, "insert new image has err!");
				    	dbAdapter.close();
				    }
				}
	  	});
	  }

}
