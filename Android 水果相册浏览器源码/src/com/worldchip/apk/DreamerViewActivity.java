package com.worldchip.apk;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.dream.hlper.DBAdapter;
import com.dream.hlper.ImageCommon;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class DreamerViewActivity extends ListActivity
{
	  protected static final String TAG = "DreamerViewActivity";
	  private static final String PATH="/flash/.thumbnails/";
	  private List<String> items=null;
	  private List<String> paths=null;
	  private String rootPath="/";
	  private DBAdapter dbAdapter = null;
      private final String INTERNAL="/flash/";
      private final String USBHOST="/usbhost/";
	  private LinkedList<String> extens=null;
	  protected final static int MENU_EDIT = Menu.FIRST ;
	  protected final static int MENU_ABOUT = Menu.FIRST + 1;
	  protected final static int MENU_EXIT = Menu.FIRST+2;
	  public boolean Damn=true;
	  public  static int skin=0;
	  private static final String MEMORIES_SKIN="MEMORIES_SKIN";
	  private LinearLayout Thelaout;
	  private LayoutInflater mInflater;
      int delay = 40; // 毫秒延迟的更新循环
      int maxBarValue=10000;
      int typeBar=0;  // 确定型的进度：0 =微调，1 =水平

      ProgressThread progThread;
      ProgressDialog progDialog;
      
	  @Override
	  protected void onCreate(Bundle icicle)
	  {
	    super.onCreate(icicle);
	    //初始化显示布局，初始布局为Layout里面的main
	    if(Damn){
	    	setTitle(R.string.welcome);
	    	 setContentView(R.layout.main);
	 	    //mInflater = LayoutInflater.from(this);
	 	    getRootView(rootPath);
	    }
	   
	    SharedPreferences sharedpreferences=getSharedPreferences(MEMORIES_SKIN,0);	
		skin=sharedpreferences.getInt("skin_value",0);
		try{
			//皮肤记录  
			 if(skin==0){
			    	System.gc();
				    setContentView(R.layout.back_view0);
				    getRootView(rootPath);
				    mInflater = LayoutInflater.from(this);
					
			    }
			 else if(skin==1){
			    	System.gc();	
				    setContentView(R.layout.back_view1);
				    getRootView(rootPath);
				    mInflater = LayoutInflater.from(this);
			
			    }
			    else if(skin==2){
			    	System.gc();
				    setContentView(R.layout.back_view2);
				    getRootView(rootPath);
				    mInflater = LayoutInflater.from(this);
				
			    }
			    else if(skin==3){
			    	System.gc();		  
				    setContentView(R.layout.back_view3);
				    getRootView(rootPath);
				    mInflater = LayoutInflater.from(this);
				
			    }
			    else if(skin==4){
			    	System.gc();
				    setContentView(R.layout.back_view4);
				    getRootView(rootPath);
				    mInflater = LayoutInflater.from(this);
					
			    }
			    else if(skin==5){
			    	System.gc();
				    setContentView(R.layout.back_view5);
				    getRootView(rootPath);
				    mInflater = LayoutInflater.from(this);
					
			    }
			    else if(skin==6){
			    	System.gc();
				    setContentView(R.layout.back_view6);
				    getRootView(rootPath);
				    mInflater = LayoutInflater.from(this);
					
			       }
		        }catch(Exception e){System.out.println(e);}
		
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

	  //扫描各个媒介的路径
	  private void getRootView(String filePath)
	  {
		  items=new ArrayList<String>();
		  paths=new ArrayList<String>();
		  items.add("internal");
		  paths.add("/flash/");
		   
		  String  SDpath = Environment.getExternalStorageDirectory() + "/";
		  items.add("sdcard");
		  paths.add(SDpath);
		  
		  
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
		    intent.setClass(DreamerViewActivity.this, ImageListView.class);  
		    intent.putExtra("path",path);  
            DreamerViewActivity.this.startActivity(intent);  
		 }
	  }
    //图像更新内容
	private void updateImages() {
		typeBar = 0;
		dbAdapter = new DBAdapter(this);
		new AlertDialog.Builder(DreamerViewActivity.this)
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

                        /* 点击按钮所做出的响应 */
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
	//保存图像文件
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
					
					//图片的缩略
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
	//打开关于窗口方法
		private void openaboutDialog(){
			new AlertDialog.Builder(this).setTitle(R.string.about_title).setMessage(R.string.about_msg)
			.setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener(){ 
				public void onClick( DialogInterface diagloginterface , int i){
				}	
			}).show();
		} 
	  public boolean onOptionsItemSelected(MenuItem item) {
			super.onOptionsItemSelected(item);
			switch (item.getItemId()) {
			
			case MENU_EDIT:
	        //肤色主题的切换			
				System.gc();
				skin++;
				if(skin>=7){skin=0;}
			    SharedPreferences sharedpreferences=getSharedPreferences(MEMORIES_SKIN,0);	
				sharedpreferences.edit().putInt("skin_value",skin).commit();	
				skin=sharedpreferences.getInt("skin_value",0);
				 if(skin==0){
				    	System.gc();
					   
					    setContentView(R.layout.back_view0);
					    mInflater = LayoutInflater.from(this);
					    getRootView(rootPath);
				    }
				 else if(skin==1){
				    	System.gc();
					    setContentView(R.layout.back_view1);
					    mInflater = LayoutInflater.from(this);
					    getRootView(rootPath);
				    }
				    else if(skin==2){
				    	System.gc();
					 
					    setContentView(R.layout.back_view2);
					    getRootView(rootPath);
					    mInflater = LayoutInflater.from(this);
						
				    }
				    else if(skin==3){
				    	System.gc();
					   
					    setContentView(R.layout.back_view3);
					    mInflater = LayoutInflater.from(this);
					    getRootView(rootPath);
				    }
				    else if(skin==4){
				    	System.gc();
					   
					    setContentView(R.layout.back_view4);
					    mInflater = LayoutInflater.from(this);
					    getRootView(rootPath);
				    }
				    else if(skin==5){
				    	System.gc();
					    setContentView(R.layout.back_view5);
					    mInflater = LayoutInflater.from(this);
					    getRootView(rootPath);
				    }
				    else if(skin==6){
				    	System.gc();
					 
					    setContentView(R.layout.back_view6);
					    mInflater = LayoutInflater.from(this);
					    getRootView(rootPath);
				       }
			    Toast.makeText(DreamerViewActivity.this,"已成功更换主题！",Toast.LENGTH_SHORT).show();
				break;
				
			case MENU_ABOUT://关于
				openaboutDialog();
				break;
			case MENU_EXIT://退出
			     finish();

				break;
			}
			return true;
		}
	  
		public boolean onCreateOptionsMenu(Menu menu) {
	        
			super.onCreateOptionsMenu(menu);
			
			SubMenu sub=menu.addSubMenu("想做什么？");
			sub.setIcon(android.R.drawable.ic_menu_slideshow);
			  
			sub.add(0,MENU_EDIT,0,"主题设置");
			sub.add(0,MENU_ABOUT,0,"关  于");
			sub.add(0,MENU_EXIT,0,"退  出");
			return true;
					
		}
	// 处理的主要（用户界面）线程接收消息的线程的进度和更新。
      
      final Handler handler = new Handler() {
          public void handleMessage(Message msg) {
              // 获得该变量的当前值总的消息数据并更新进度栏。
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

	// 内部类的执行进度计算的一个线程。实现线程的子线程和重写其run()方法。
    //还提供了一个设置状态的方法来阻止冗余线程的运行。
      
      private class ProgressThread extends Thread {	
          
          // 类常量定义的线程的状态
          final static int DONE = 0;
          final static int RUNNING = 1;
          
          Handler mHandler;
          int mState;
          int total;
      
          // 构造函数处理在该线程中的指定参数，并将消息发送到该线程
          
          ProgressThread(Handler h) {
              mHandler = h;
          }
          
          // 覆盖run()方法，开启主线程。
          //这需要更新进度条本线发送一个消息处理程序的主界面线程，即实际上改变视觉表现的进步。
          //在这个例子中我们计算指数总降为零，所以水平进度栏将一一被初始化
          
          @Override
          public void run() {
        	  Log.i(TAG, "coming run again!");
              //mState = RUNNING;   
              if(mState == RUNNING) {
              //线程异常捕捉，如Thread.interrupt() 进入休眠状态时，须捕获异常
                  try {
                      // 控制更新速度（但延时精度不能保证）
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
                 
                  // 发送消息来处理用户界面线程，以便可以更新进度栏。                  
                  Message msg = mHandler.obtainMessage();
                  Bundle b = new Bundle();
                  b.putInt("total", total);
                  msg.setData(b);
                  mHandler.sendMessage(msg);
              
              }
              else
              {
            	  //结束当前进程
            	  //dbAdapter.close();
            	  Log.i(TAG, "Thread interrupt!");
            	  Thread.currentThread().interrupt();  
              }
          }
          
          // 设置当前线程的状态
          public void setState(int state) {
              mState = state;
          }
      }


}
