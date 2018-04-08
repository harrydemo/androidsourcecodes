/**
 * 
 */
package com.jacinter;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.content.Context;
import android.content.SharedPreferences;  
import android.graphics.PixelFormat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;

public class FloatingWindowService extends Service {
	//这个类有两个功能，监听来电号码，创建显示姓名的窗口
	public static final String OPERATION = "operation";
	private boolean isAdded = false; // 是否已增加悬浮窗
	private static WindowManager wm;
	private static WindowManager.LayoutParams params;
	private Button btn_floatView;
    private TelephonyManager myManager;
    private MySQLiteHelper myHelper;  
    private String INCOMINGNUMBER=null;
    private String INCOMINGDEPARTMENT=null;
    private SharedPreferences sharedPreferences;  
    private SharedPreferences.Editor editor;  

	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.e("PhoneService", "服务已建立");
        myManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        myManager.listen(new myManagerListener(), PhoneStateListener.LISTEN_CALL_STATE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.e("PhoneService", "服务已销毁");

	}

	public void onStart() {
		super.onCreate();
		
		}

	/**
	 * 创建悬浮窗
	 */
	private void createFloatView() {
		Log.e("PhoneService", "窗口准备创建");

		
		btn_floatView = new Button(getApplicationContext());
        btn_floatView.setText(INCOMINGNUMBER);
        
        wm = (WindowManager) getApplicationContext()
        	.getSystemService(Context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();
        
        // 设置window type   WindowManager.LayoutParams.
        params.type = 2010;
        /*
         * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE;
         * 那么优先级会降低一些, 即拉下通知栏不可见
         */
        
        params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
        
        // 设置Window flag
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                              | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        /*
         * 下面的flags属性的效果形同“锁定”。
         * 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
        wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL
                               | LayoutParams.FLAG_NOT_FOCUSABLE
                               | LayoutParams.FLAG_NOT_TOUCHABLE;
         */
        
        // 设置悬浮窗的长得宽
        params.width = 250;
        params.height = 100;
        sharedPreferences = this.getSharedPreferences("WindowPosition",MODE_PRIVATE);  
        editor = sharedPreferences.edit();  
        int PositionX = sharedPreferences.getInt("PositionX", 0); 
        int PositionY = sharedPreferences.getInt("PositionY", 0); 


        
        params.x=PositionX;
        params.y=PositionY;

        //下面这句是设置背景色为透明色
        //btn_floatView.setBackgroundColor(Color.TRANSPARENT);
        // 设置悬浮窗的Touch监听
        btn_floatView.setOnTouchListener(new OnTouchListener() {
        	int lastX, lastY;
        	int paramX, paramY;
        	
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					lastX = (int) event.getRawX();
					lastY = (int) event.getRawY();
					paramX = params.x;
					paramY = params.y;
					break;
				case MotionEvent.ACTION_MOVE:
					int dx = (int) event.getRawX() - lastX;
					int dy = (int) event.getRawY() - lastY;
					params.x = paramX + dx;
					params.y = paramY + dy;
					// 更新悬浮窗位置
			        wm.updateViewLayout(btn_floatView, params);
			        Log.d("PhoneService","记录的横坐标为"+params.x);
			        editor.putInt("PositionX", params.x);
			        Log.d("PhoneService","记录的纵坐标为"+params.y);
			        editor.putInt("PositionY", params.y);
			        editor.commit(); 
			        break;
				}
				return true;
			}
		});
        
        wm.addView(btn_floatView, params);
        isAdded = true;
	}
	


    private class myManagerListener extends PhoneStateListener{
        @Override
		public void onCallStateChanged(int state, String incomingNumber) {
                switch(state){
                        case TelephonyManager.CALL_STATE_RINGING :
                    		Log.e("PhoneService", "已经监听到来电");
                            myHelper = new MySQLiteHelper(FloatingWindowService.this, "my.db", null, 2); 
                            SQLiteDatabase db = myHelper.getReadableDatabase();  
                            Cursor cursor = db.rawQuery("SELECT name,department FROM jacnamelist where telephone like ?", new String[]{"%"+incomingNumber+"%"});  
                            if( cursor != null ){
                                if( cursor.moveToFirst() ){
                            		//Toast.makeText(getApplicationContext(), cursor.getString(0), Toast.LENGTH_LONG).show();
                            		//调用创建窗口
                            		Log.e("PhoneService", "监听到来电，号码为"+cursor.getString(0));
                            		int i=Integer.parseInt(cursor.getString(1));
                                    if (i>=0&&i<16){
                                    String[] str={"公司领导","综合管理部","财务部","销售一部","销售二部","销售三部","客户服务部","物流部","技术部","市场部","总经办","客户管理部","生产一部","生产二部","生产三部","制造一部"};
                            		INCOMINGDEPARTMENT=str[i];
                            		INCOMINGNUMBER=INCOMINGDEPARTMENT+"\n"+cursor.getString(0);
                                    }else{
                                		INCOMINGNUMBER=cursor.getString(0);
                                    }
                                    	
                                		
                                	createFloatView();
                                    isAdded = true;
                                   }
                              }
                             cursor.close();
                             db.close();
                        break;
                        case TelephonyManager.CALL_STATE_OFFHOOK :
                        	Log.e("PhoneService","执行到OFFHOOK");

                        	if (isAdded==true){
                                wm.removeView(btn_floatView);  
                                isAdded = false;  
                                INCOMINGNUMBER=null;
                        		Log.e("PhoneService", "电话中断");

                            	}
                        	break;
                        	
                        default :
                        	Log.e("PhoneService","执行到默认default");
                        	if (isAdded==true){
                                wm.removeView(btn_floatView);  
                                isAdded = false;  
                                INCOMINGNUMBER=null;
                        		Log.e("PhoneService", "执行到关闭窗口");
                            	}

                            break;
                }
                
                super.onCallStateChanged(state, incomingNumber);
        }

    }

	
}
