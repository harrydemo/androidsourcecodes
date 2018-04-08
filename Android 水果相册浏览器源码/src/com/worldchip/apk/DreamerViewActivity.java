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
      int delay = 40; // �����ӳٵĸ���ѭ��
      int maxBarValue=10000;
      int typeBar=0;  // ȷ���͵Ľ��ȣ�0 =΢����1 =ˮƽ

      ProgressThread progThread;
      ProgressDialog progDialog;
      
	  @Override
	  protected void onCreate(Bundle icicle)
	  {
	    super.onCreate(icicle);
	    //��ʼ����ʾ���֣���ʼ����ΪLayout�����main
	    if(Damn){
	    	setTitle(R.string.welcome);
	    	 setContentView(R.layout.main);
	 	    //mInflater = LayoutInflater.from(this);
	 	    getRootView(rootPath);
	    }
	   
	    SharedPreferences sharedpreferences=getSharedPreferences(MEMORIES_SKIN,0);	
		skin=sharedpreferences.getInt("skin_value",0);
		try{
			//Ƥ����¼  
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
            //  progDialog.setCancelable(false);  //��ֹ�ͻ��������ء���
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

	  //ɨ�����ý���·��
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
		 
		 if(path.equals("update"))  //����ɨ��
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
    //ͼ���������
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

                        /* �����ť����������Ӧ */
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
	//����ͼ���ļ�
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
					
					//ͼƬ������
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
	//�򿪹��ڴ��ڷ���
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
	        //��ɫ������л�			
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
			    Toast.makeText(DreamerViewActivity.this,"�ѳɹ��������⣡",Toast.LENGTH_SHORT).show();
				break;
				
			case MENU_ABOUT://����
				openaboutDialog();
				break;
			case MENU_EXIT://�˳�
			     finish();

				break;
			}
			return true;
		}
	  
		public boolean onCreateOptionsMenu(Menu menu) {
	        
			super.onCreateOptionsMenu(menu);
			
			SubMenu sub=menu.addSubMenu("����ʲô��");
			sub.setIcon(android.R.drawable.ic_menu_slideshow);
			  
			sub.add(0,MENU_EDIT,0,"��������");
			sub.add(0,MENU_ABOUT,0,"��  ��");
			sub.add(0,MENU_EXIT,0,"��  ��");
			return true;
					
		}
	// �������Ҫ���û����棩�߳̽�����Ϣ���̵߳Ľ��Ⱥ͸��¡�
      
      final Handler handler = new Handler() {
          public void handleMessage(Message msg) {
              // ��øñ����ĵ�ǰֵ�ܵ���Ϣ���ݲ����½�������
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

	// �ڲ����ִ�н��ȼ����һ���̡߳�ʵ���̵߳����̺߳���д��run()������
    //���ṩ��һ������״̬�ķ�������ֹ�����̵߳����С�
      
      private class ProgressThread extends Thread {	
          
          // �ೣ��������̵߳�״̬
          final static int DONE = 0;
          final static int RUNNING = 1;
          
          Handler mHandler;
          int mState;
          int total;
      
          // ���캯�������ڸ��߳��е�ָ��������������Ϣ���͵����߳�
          
          ProgressThread(Handler h) {
              mHandler = h;
          }
          
          // ����run()�������������̡߳�
          //����Ҫ���½��������߷���һ����Ϣ���������������̣߳���ʵ���ϸı��Ӿ����ֵĽ�����
          //��������������Ǽ���ָ���ܽ�Ϊ�㣬����ˮƽ��������һһ����ʼ��
          
          @Override
          public void run() {
        	  Log.i(TAG, "coming run again!");
              //mState = RUNNING;   
              if(mState == RUNNING) {
              //�߳��쳣��׽����Thread.interrupt() ��������״̬ʱ���벶���쳣
                  try {
                      // ���Ƹ����ٶȣ�����ʱ���Ȳ��ܱ�֤��
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
                 
                  // ������Ϣ�������û������̣߳��Ա���Ը��½�������                  
                  Message msg = mHandler.obtainMessage();
                  Bundle b = new Bundle();
                  b.putInt("total", total);
                  msg.setData(b);
                  mHandler.sendMessage(msg);
              
              }
              else
              {
            	  //������ǰ����
            	  //dbAdapter.close();
            	  Log.i(TAG, "Thread interrupt!");
            	  Thread.currentThread().interrupt();  
              }
          }
          
          // ���õ�ǰ�̵߳�״̬
          public void setState(int state) {
              mState = state;
          }
      }


}
