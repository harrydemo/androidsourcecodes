package com.weibo.android.logic;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.weibo.android.IWeiboActivity;
import com.weibo.android.Paging;
import com.weibo.net.Weibo;
import com.weibo.net.WeiboException;

public class MainService extends Service implements Runnable,IService{
  
	private static ArrayList<IWeiboActivity> allActivity=new ArrayList<IWeiboActivity>();
	private static ArrayList<Task> allTasks=new ArrayList<Task>();
	public static boolean isRun=false;
	
	public static void newTask(Task t){
		allTasks.add(t);
	}
	public static void addActivity(IWeiboActivity iw){
		allActivity.add(iw);
	}
	public static void removeActivity(IWeiboActivity iw){
		allActivity.remove(iw);
	}
	public static IWeiboActivity getActivityByName(String name){
		for(IWeiboActivity iw:allActivity){
			if(iw.getClass().getName().indexOf(name)>=0){
				return iw;
			}
		}
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	
		isRun=true;
		new Thread(this).start();
		Log.e("=============================", "MainService    onCreate()");
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		isRun=false;
		super.onDestroy();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(isRun){
			try{
				if(allTasks.size()>0)
				{
			     //执行任务
	            doTask(allTasks.get(0));
				}else{
					try{
					Thread.sleep(2000);}catch(Exception e){}
					//Log.e("=============================", "MainService   doing        ");
				}
			}catch(Exception e){
				if(allTasks.size()>0)
					allTasks.remove(allTasks.get(0));
				Log.d("error","------------------"+e);
			}
		}
	}
   private Handler handler=new Handler(){

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);
		
		switch (msg.what) {
		case Task.HOMEREFRESH:
			MainService.getActivityByName("HomeActivity").refresh(msg.obj);
			break;
		case Task.LOGIN:
			
			break;
		case Task.MYCONTACT://个人资料
			MainService.getActivityByName("MyContactActivity")
			.refresh(msg.obj);
		    break;
		case Task.MSGACTIVITY:
			/*MainService.getActivityByName("MsgActivity")
			.refresh(msg.obj);*/
			break;
		case Task.NEWWEIBO:
			MainService.getActivityByName("NewWeiboActivity").refresh(msg.obj);
			break;
		case Task.VIEWWEIBO:
			MainService.getActivityByName("ViewWeiBoActivity").refresh(msg.obj);
			break;
		default:
			break;
		}
	}
	   
   };
	private void doTask(Task task) {
		// TODO Auto-generated method stub
		Message msg=handler.obtainMessage();
		msg.what=task.getTaskId();
		
		switch (task.getTaskId()) {
		 case Task.HOMEREFRESH:
			try {
				String result = Weibo.getInstance().getPublicTimeline(task.getCtx(),new Paging(1,10));
				Log.v("result",result);
				msg.obj=result;
			} catch (WeiboException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case Task.LOGIN://登录
			break;
		case Task.MYCONTACT://个人资料
			try {
				String mycontactJson = Weibo.getInstance().showUser(task.getCtx(),Weibo.getInstance().getUid());
				msg.obj=mycontactJson;
				Log.v("return user detail", mycontactJson);
			} catch (WeiboException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    break;
		case Task.MSGACTIVITY://信息
			/* preferences = getSharedPreferences("ss",
					Context.MODE_PRIVATE);
			 token = preferences.getString("token", "");
			 tokenSecret=preferences.getString("tokenSecret", "");
			  weibo=OAuthConstant.getInstance().getWeibo();
			  weibo.setToken(token,tokenSecret);
			  try {
				User user=weibo.showUser(preferences.getString("userId", ""));
				msg.obj=user;
			} catch (WeiboException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			break;
		case Task.NEWWEIBO:
				  try {
					  String content=task.getTaskParams().get("msg").toString();
					Weibo.getInstance().updateStatus(task.getCtx(),content);
					msg.obj="发表微博成功";
				} catch (WeiboException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			break;
		case Task.VIEWWEIBO:
			  try {
				String weiboid=task.getTaskParams().get("weiboid").toString();
				String returnStatus  = Weibo.getInstance().showStatus(task.getCtx(),weiboid);
				msg.obj=returnStatus;
			} catch (WeiboException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		break;
		default:
			break;
		}
		allTasks.remove(task);
		//通知主线程更新UI
		handler.sendMessage(msg);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void destory() {
		// TODO Auto-generated method stub
		stopSelf();
	}

	
}
