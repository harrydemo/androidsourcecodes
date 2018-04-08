package com.worldchip.apk;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class JoyImageViewActivity extends ListActivity
{
	  protected static final String TAG = "JoyImageViewActivity";
	  private static final String PATH="/flash/.thumbnails/";
	  private List<String> items=null;
	  private List<String> paths=null;
	  private String rootPath="/";
	  private DBAdapter dbAdapter = null;
      private final String INTERNAL="/flash/";
      private final String USBHOST="/usbhost/";
	  private LinkedList<String> extens=null;
      int delay = 40;                  // Milliseconds of delay in the update loop
      int maxBarValue=10000;
      int typeBar=0;                     // Determines type progress bar: 0 = spinner, 1 = horizontal

      ProgressThread progThread;
      ProgressDialog progDialog;
      
	  @Override
	  protected void onCreate(Bundle icicle)
	  {
	    super.onCreate(icicle);
	    setTitle(R.string.welcome);
	    setContentView(R.layout.main);
	    
	    getRootView(rootPath);
	  }
	  
	  @Override
	  protected void onDestroy() {
	     super.onDestroy();
	     
	     if(progThread!=null)
	         progThread.setState(ProgressThread.DONE);
	     if(dbAdapter!=null)
	         dbAdapter.close();
	  }
	  
	  @Override
      protected Dialog onCreateDialog(int id) {
          switch(id) {
          case 0:                      // Spinner
              progDialog = new ProgressDialog(this);
            //  progDialog.setCancelable(false);  //防止客户按“返回”键
              progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
              progDialog.setMessage((String) getText(R.string.scan));
              progThread = new ProgressThread(handler);
              progThread.setState(ProgressThread.RUNNING);
              progThread.start();
              return progDialog;
          case 1:                      // Horizontal
              progDialog = new ProgressDialog(this);
            //  progDialog.setCancelable(false);
              progDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
              progDialog.setMax(maxBarValue);
              progDialog.setMessage("Dollars in checking account:");
              progThread = new ProgressThread(handler);
              progThread.start();
              return progDialog;
          default:
              return null;
          }
      }

	  
	  private void getRootView(String filePath)
	  {
		  items=new ArrayList<String>();
		  paths=new ArrayList<String>();
		  items.add("internal");
		  paths.add("/flash/");
		    
		  items.add("sdcard");
		  paths.add("/sdcard/");
		  
		  
		  items.add("usbhost");
		  paths.add("/usbhost/");
		  
		  items.add("update");
		  paths.add("update");
		    
		  setListAdapter(new ListAdapter(this,items,paths));
	  }
	  
	  @Override
	  protected void onListItemClick(ListView l,View v,
	                                 int position,long id)
	  {
		 String path=paths.get(position).toString();
		 Log.i("onListItemClick", "path="+path);
		 
		 if(path.equals("update"))  //重新扫描
		 {
			 updateImages();
		 }
		 else
		 {
			Intent intent = new Intent();  
		    intent.setClass(JoyImageViewActivity.this, ImageListView.class);  
		    intent.putExtra("path",path);  
            JoyImageViewActivity.this.startActivity(intent);  
		 }
	  }

	private void updateImages() {
		typeBar = 0;
		dbAdapter = new DBAdapter(this);
		new AlertDialog.Builder(JoyImageViewActivity.this)
        .setIcon(R.drawable.icon)
        .setTitle(R.string.redue)
        .setMessage(R.string.scan_redue)
        .setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                            int whichButton) {
                        showDialog(typeBar);
                    }
                })
        .setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                            int whichButton) {

                        /* User clicked OK so do some stuff */
                    }
                })
        .show();
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
	  
	// Handler on the main (UI) thread that will receive messages from the 
      // second thread and update the progress.
      
      final Handler handler = new Handler() {
          public void handleMessage(Message msg) {
              // Get the current value of the variable total from the message data
              // and update the progress bar.
              int total = msg.getData().getInt("total");
              Log.i(TAG, "Handler total="+total);
            //  progDialog.setProgress(total);
              if (total ==1){
            	  Log.i(TAG, "dismissDialog, the dialog will shutdown! the tatal="+total);
                  dismissDialog(typeBar);
                  progThread.setState(ProgressThread.DONE);
              }
          }
      };

	// Inner class that performs progress calculations on a second thread.  Implement
      // the thread by subclassing Thread and overriding its run() method.  Also provide
      // a setState(state) method to stop the thread gracefully.
      
      private class ProgressThread extends Thread {	
          
          // Class constants defining state of the thread
          final static int DONE = 0;
          final static int RUNNING = 1;
          
          Handler mHandler;
          int mState;
          int total;
      
          // Constructor with an argument that specifies Handler on main thread
          // to which messages will be sent by this thread.
          
          ProgressThread(Handler h) {
              mHandler = h;
          }
          
          // Override the run() method that will be invoked automatically when 
          // the Thread starts.  Do the work required to update the progress bar on this
          // thread but send a message to the Handler on the main UI thread to actually
          // change the visual representation of the progress. In this example we count
          // the index total down to zero, so the horizontal progress bar will start full and
          // count down.
          
          @Override
          public void run() {
        	  Log.i(TAG, "coming run again!");
              //mState = RUNNING;   
              if(mState == RUNNING) {
                  // The method Thread.sleep throws an InterruptedException if Thread.interrupt() 
                  // were to be issued while thread is sleeping; the exception must be caught.
                  try {
                      // Control speed of update (but precision of delay not guaranteed)
                	  total=0;
                	  if(dbAdapter==null)
                	  {
                		  total=1;
                		  Thread.sleep(delay);
                	  }
                	  else
                	  {
                	     dbAdapter.open();
              		     dbAdapter.deleteAllImage();
              		
                         extens=new LinkedList<String>();
                         getExtens();
                      
                         Log.i(TAG, "create new dir");
                         File dir = new File(PATH);
                         if(dir.exists())
                         {
                    	    dir.delete();
                         }
                         dir.mkdir();
                      
                         File file=new File(INTERNAL);
                         saveImageFile(file);
                         dbAdapter.close();
                      
                         total=1;
                         Thread.sleep(delay);
                	  }
                  } catch (InterruptedException e) {
                      Log.e("ERROR", "Thread was Interrupted");
                  }
                  
                  // Send message (with current value of  total as data) to Handler on UI thread
                  // so that it can update the progress bar.
                  
                  Message msg = mHandler.obtainMessage();
                  Bundle b = new Bundle();
                  b.putInt("total", total);
                  msg.setData(b);
                  mHandler.sendMessage(msg);
                  
                 //total--;    // Count down
              }
              else
              {
            	  //结束当前进程
            	  //dbAdapter.close();
            	  Log.i(TAG, "Thread interrupt!");
            	  Thread.currentThread().interrupt();  
              }
          }
          
          // Set current state of thread (use state=ProgressThread.DONE to stop thread)
          public void setState(int state) {
              mState = state;
          }
      }


}
