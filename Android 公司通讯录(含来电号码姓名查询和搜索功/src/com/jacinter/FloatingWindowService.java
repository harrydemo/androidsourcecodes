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
	//��������������ܣ�����������룬������ʾ�����Ĵ���
	public static final String OPERATION = "operation";
	private boolean isAdded = false; // �Ƿ�������������
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
		Log.e("PhoneService", "�����ѽ���");
        myManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        myManager.listen(new myManagerListener(), PhoneStateListener.LISTEN_CALL_STATE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.e("PhoneService", "����������");

	}

	public void onStart() {
		super.onCreate();
		
		}

	/**
	 * ����������
	 */
	private void createFloatView() {
		Log.e("PhoneService", "����׼������");

		
		btn_floatView = new Button(getApplicationContext());
        btn_floatView.setText(INCOMINGNUMBER);
        
        wm = (WindowManager) getApplicationContext()
        	.getSystemService(Context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();
        
        // ����window type   WindowManager.LayoutParams.
        params.type = 2010;
        /*
         * �������Ϊparams.type = WindowManager.LayoutParams.TYPE_PHONE;
         * ��ô���ȼ��ή��һЩ, ������֪ͨ�����ɼ�
         */
        
        params.format = PixelFormat.RGBA_8888; // ����ͼƬ��ʽ��Ч��Ϊ����͸��
        
        // ����Window flag
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                              | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        /*
         * �����flags���Ե�Ч����ͬ����������
         * ���������ɴ������������κ��¼�,ͬʱ��Ӱ�������¼���Ӧ��
        wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL
                               | LayoutParams.FLAG_NOT_FOCUSABLE
                               | LayoutParams.FLAG_NOT_TOUCHABLE;
         */
        
        // �����������ĳ��ÿ�
        params.width = 250;
        params.height = 100;
        sharedPreferences = this.getSharedPreferences("WindowPosition",MODE_PRIVATE);  
        editor = sharedPreferences.edit();  
        int PositionX = sharedPreferences.getInt("PositionX", 0); 
        int PositionY = sharedPreferences.getInt("PositionY", 0); 


        
        params.x=PositionX;
        params.y=PositionY;

        //������������ñ���ɫΪ͸��ɫ
        //btn_floatView.setBackgroundColor(Color.TRANSPARENT);
        // ������������Touch����
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
					// ����������λ��
			        wm.updateViewLayout(btn_floatView, params);
			        Log.d("PhoneService","��¼�ĺ�����Ϊ"+params.x);
			        editor.putInt("PositionX", params.x);
			        Log.d("PhoneService","��¼��������Ϊ"+params.y);
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
                    		Log.e("PhoneService", "�Ѿ�����������");
                            myHelper = new MySQLiteHelper(FloatingWindowService.this, "my.db", null, 2); 
                            SQLiteDatabase db = myHelper.getReadableDatabase();  
                            Cursor cursor = db.rawQuery("SELECT name,department FROM jacnamelist where telephone like ?", new String[]{"%"+incomingNumber+"%"});  
                            if( cursor != null ){
                                if( cursor.moveToFirst() ){
                            		//Toast.makeText(getApplicationContext(), cursor.getString(0), Toast.LENGTH_LONG).show();
                            		//���ô�������
                            		Log.e("PhoneService", "���������磬����Ϊ"+cursor.getString(0));
                            		int i=Integer.parseInt(cursor.getString(1));
                                    if (i>=0&&i<16){
                                    String[] str={"��˾�쵼","�ۺϹ���","����","����һ��","���۶���","��������","�ͻ�����","������","������","�г���","�ܾ���","�ͻ�����","����һ��","��������","��������","����һ��"};
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
                        	Log.e("PhoneService","ִ�е�OFFHOOK");

                        	if (isAdded==true){
                                wm.removeView(btn_floatView);  
                                isAdded = false;  
                                INCOMINGNUMBER=null;
                        		Log.e("PhoneService", "�绰�ж�");

                            	}
                        	break;
                        	
                        default :
                        	Log.e("PhoneService","ִ�е�Ĭ��default");
                        	if (isAdded==true){
                                wm.removeView(btn_floatView);  
                                isAdded = false;  
                                INCOMINGNUMBER=null;
                        		Log.e("PhoneService", "ִ�е��رմ���");
                            	}

                            break;
                }
                
                super.onCallStateChanged(state, incomingNumber);
        }

    }

	
}
