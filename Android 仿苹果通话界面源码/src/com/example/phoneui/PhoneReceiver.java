package com.example.phoneui;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PhoneReceiver extends BroadcastReceiver {
	private static boolean incoming = false;
	private static boolean isanswer = false;
	//private static boolean isoutgoing = true;
	private WeakReference<Context> mApp;
	private WeakReference<Intent> mIntent;
	
	private static int num = 0;
	private static TelephonyManager manager;
	private static ITelephony mITelephony;
	private static WindowManager wm;
	//private static View outview;
	private static View inview;
	private static ImageButton decline;
	private static ImageButton answer;
	private static ImageButton speaker;
	private static LinearLayout lin1;
	private static LinearLayout lin2;
	private static RelativeLayout tools;
	private static Button endcall;
	//private static Button outendcall;
	private static itemShort onClick;
	private static itemTouch onTouch;
	private static WindowManager.LayoutParams inparams;
	//private static WindowManager.LayoutParams outparams;
	private static TextView name;
	private static TextView status;
	private static TextView slide;
	private static Chronometer time;
	private static String number;
	private TelephonyManager tm;
	
	@Override
	public void onReceive(Context context, Intent intent) {
	  // TODO Auto-generated method stub
		//Log.i("phone", "num");
		mApp = new WeakReference<Context>(context);
		mIntent = new WeakReference<Intent>(intent);
		final String action = mIntent.get().getAction(); 
		tm = (TelephonyManager) mApp.get().getSystemService(Service.TELEPHONY_SERVICE);
		start();
		if(action.equals(Intent.ACTION_NEW_OUTGOING_CALL))
		{
			 incoming = false;
			 number = mIntent.get().getStringExtra(Intent.EXTRA_PHONE_NUMBER); 
			 name.setText(getName(number));
			 status.setVisibility(View.VISIBLE);
			 time.setVisibility(View.GONE);
			 status.setText("正在拨号");
			 lin1.setVisibility(View.GONE);
			 lin2.setVisibility(View.VISIBLE);
		}
		else
		{
		   switch (tm.getCallState()) {
           case TelephonyManager.CALL_STATE_RINGING:
                   incoming = true;//标识当前是来电
                   isanswer = false;
                   number = mIntent.get().getStringExtra("incoming_number");
                   name.setText(getName(number));
                   status.setText("来电");
                   lin1.setVisibility(View.VISIBLE);
      			   lin2.setVisibility(View.GONE);
      			   status.setVisibility(View.VISIBLE);
    			   time.setVisibility(View.GONE);
    			   tools.setVisibility(View.GONE);
                   break;
           case TelephonyManager.CALL_STATE_OFFHOOK:  
                   if(incoming){
                	   isanswer = true;
                	   lin1.setVisibility(View.GONE);
                	   lin2.setVisibility(View.VISIBLE);
                	   status.setVisibility(View.GONE);
        			   time.setVisibility(View.VISIBLE);
        			   tools.setVisibility(View.VISIBLE);
                	   if(time != null)
           	   			{
           	   				time.setBase(SystemClock.elapsedRealtime());
           	   				time.start();
           	   			}
                   }
                   break;
           case TelephonyManager.CALL_STATE_IDLE: 
                   if(incoming){
                	   	lin1.setVisibility(View.GONE);
             	   		lin2.setVisibility(View.GONE);  
             	   		if(time != null)
             	   			time.stop();
                	   if(!isanswer)
                		   toNotic(mApp.get(),mIntent.get());
                   }
                   break;
           } 
		}
		if(incoming)
		{
			if(tm.getCallState() != TelephonyManager.CALL_STATE_IDLE)
			{
				if(num == 0)
				{
					//if(incomingFlag)
						wm.addView(inview, inparams);
					//else
						//wm.addView(outview, outparams);
				}
				else
				{
					//if(incomingFlag)
						wm.updateViewLayout(inview, inparams);
					//else
						//wm.updateViewLayout(outview, outparams);
				}
				num++;
			}
			else if(num > 0)
			{
				new Handler().postDelayed(new Runnable()
				{   
				    public void run() 
				    {
				    	//if(incomingFlag)
				    		wm.removeView(inview);
				    	//else
				    		//wm.removeView(outview);
				    }   
				 }, 1000);  
				num = 0;
			}
			
		}
	}
	/*void outing(){
		new Handler().postDelayed(new Runnable()
		{   
		    public void run() 
		    {
		    	if(status!=null && time!=null)
		    	{
		    		status.setVisibility(View.GONE);
 			   		time.setVisibility(View.VISIBLE);
 			   		time.setBase(SystemClock.elapsedRealtime());
 			   		time.start();
		    	}
		    }   
		 }, 4000); 
	}*/
	 public void toNotic(Context context,Intent intent){
		 intent.setClass(context,Notic.class);
		 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 context.startActivity(intent);
	 }
	 void start(){
		 	if(manager == null)
		 		manager = (TelephonyManager) mApp.get().getSystemService(Context.TELEPHONY_SERVICE);
	        if(wm == null)
	        	wm = (WindowManager) mApp.get().getSystemService(Context.WINDOW_SERVICE);  
	        /*if(outparams == null)
	        {
	        	outparams = new WindowManager.LayoutParams(); 
	        	outparams.type = WindowManager.LayoutParams.TYPE_PHONE;  
	        	outparams.gravity = Gravity.LEFT|Gravity.BOTTOM;
	        	outparams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; 
	        	outparams.width = backpx(220);//WindowManager.LayoutParams.WRAP_CONTENT;  
	        	outparams.height = backpx(166);//WindowManager.LayoutParams.WRAP_CONTENT;  
	        }*/
	        if(inparams == null)
	        {
	        	inparams = new WindowManager.LayoutParams(); 
	        	inparams.type = WindowManager.LayoutParams.TYPE_PHONE;  
	        	inparams.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; 
	        	inparams.width = WindowManager.LayoutParams.WRAP_CONTENT;  
	        	inparams.height = WindowManager.LayoutParams.WRAP_CONTENT;  
	        }
	        if(inview == null)
	        {
	        	LayoutInflater inflater = (LayoutInflater) mApp.get().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        	inview = inflater.inflate(R.layout.ui, null);
	        	answer = (ImageButton) inview.findViewById(R.id.answer);
		        endcall = (Button) inview.findViewById(R.id.endcall);
		        decline = (ImageButton) inview.findViewById(R.id.decline);
		        speaker = (ImageButton) inview.findViewById(R.id.speaker);
		        name = (TextView) inview.findViewById(R.id.name);
		        status = (TextView) inview.findViewById(R.id.statu);
		        slide = (TextView) inview.findViewById(R.id.slide);
		        time = (Chronometer) inview.findViewById(R.id.time);
		        lin1 = (LinearLayout) inview.findViewById(R.id.lin1);
		        lin2 = (LinearLayout) inview.findViewById(R.id.lin2);
		        tools = (RelativeLayout) inview.findViewById(R.id.tools);
		        onClick = new itemShort();
	        	onTouch = new itemTouch();
	        	speaker.setOnClickListener(onClick);
	        	endcall.setOnClickListener(onClick);
	        	answer.setOnTouchListener(onTouch);
	        	decline.setOnTouchListener(onTouch);
	        }
	        /*if(outview == null)
	        {
	        	LayoutInflater inflater = (LayoutInflater) mApp.get().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        	outview = inflater.inflate(R.layout.outgoing, null);
	        	outendcall = (Button) outview.findViewById(R.id.endcall);
	        	outendcall.setOnClickListener(onClick);
	        }*/
	 }
	 //切换扬声器
     void switchspeaker(){
         try{
         AudioManager audioManager = (AudioManager) mApp.get().getSystemService(Context.AUDIO_SERVICE);    
         //int currVolume = audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
         if(!audioManager.isSpeakerphoneOn())
         {
        	 audioManager.setMode(AudioManager.ROUTE_SPEAKER);
        	 audioManager.setSpeakerphoneOn(true);
        	 speaker.setImageResource(R.drawable.ic_in_call_touch_speaker_on);
         }
         else
         {
        	 audioManager.setSpeakerphoneOn(false);
        	 speaker.setImageResource(R.drawable.ic_in_call_touch_speaker_off);
         }
         audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL ),AudioManager.STREAM_VOICE_CALL);
        } catch (Exception e) {
            e.printStackTrace();
        }
     }
	 String getName(String address){
	    	String number = address.replace("+86","");
	    	String name = "";
	    	Cursor cursor = mApp.get().getContentResolver().query(Phone.CONTENT_URI,new String[]{Phone.DISPLAY_NAME}, Phone.NUMBER+"=?"+" OR "+Phone.NUMBER+"=?", new String[]{number,"+86"+number}, null);
	    	if(cursor.moveToFirst())
	    		name = cursor.getString(0);
	    	else
	    		name = number;
	    	cursor.close();
	    	return name;
	 }
	 class itemShort implements OnClickListener{
		 public void onClick(View v) {
				// TODO 自动生成的方法存根
			 switch(v.getId())
			 {
			 case R.id.endcall:toEnd();break;
			 case R.id.speaker:switchspeaker();break;
			 }
		 }
	 }
	 class itemTouch implements OnTouchListener{
	    	private int lastX;
	    	private int screenWidth;
	        private int screenHeight;
	        private ViewGroup.LayoutParams aparam;
	        private ViewGroup.LayoutParams dparam;
	        private DisplayMetrics dm;
	        itemTouch(){
	        	dm = mApp.get().getResources().getDisplayMetrics();
		        screenWidth=dm.widthPixels;
		        screenHeight=dm.heightPixels;
		        aparam=answer.getLayoutParams(); 
		        dparam=decline.getLayoutParams();
	        }
			public boolean onTouch(View v, MotionEvent event) {
				// TODO 自动生成的方法存根
				
				if(v.getId() == R.id.answer && decline.getVisibility() == View.VISIBLE)
				{
					decline.setVisibility(View.INVISIBLE);
					slide.setText("右滑接听");
				}
				if(v.getId() == R.id.decline && answer.getVisibility() == View.VISIBLE)
				{
					answer.setVisibility(View.INVISIBLE);
					slide.setText("左滑拒接");
				}
				int ea=event.getAction();
		          switch(ea){
		          case MotionEvent.ACTION_DOWN:           
		           
		           lastX=(int)event.getRawX();
		           //lastY=(int)event.getRawY();           
		           break;
		          case MotionEvent.ACTION_MOVE:
		           int dx=(int)event.getRawX()-lastX;
		           int dy=0;//(int)event.getRawY()-lastY;           
		           
		           int l=v.getLeft()+dx; 
		           int b=v.getBottom()+dy;
		           int r=v.getRight()+dx;
		           int t=v.getTop()+dy;
		           if(l<0){
		            l=0;    
		            r=l+v.getWidth();
		           }
		           
		           if(t<0){
		            t=0;
		            b=t+v.getHeight();
		           }
		           
		           if(r>screenWidth){
		            r=screenWidth;
		            l=r-v.getWidth();
		           }
		          
		           if(b>screenHeight){
		            b=screenHeight;
		            t=b-v.getHeight();
		           }
		           v.layout(l, t, r, b);
		           
		           lastX=(int)event.getRawX();
		           //lastY=(int)event.getRawY();
		           
		           v.postInvalidate();  
		           
		           break;
		          case MotionEvent.ACTION_UP:
		        	  if(v.getId() == R.id.answer)
		        		  v.setLayoutParams(aparam);
		        	  else
		        		  v.setLayoutParams(dparam);
		        	  if(v.getId() == R.id.answer)
						{
							decline.setVisibility(View.VISIBLE);
							if(lastX > screenWidth-backpx(60))
							{
								toAnswer();
								lin1.setVisibility(View.GONE);
							}
						}
						if(v.getId() == R.id.decline)
						{
							answer.setVisibility(View.VISIBLE);
							if(lastX < backpx(60))
							{
								isanswer = true;
								toEnd();
							lin1.setVisibility(View.GONE);
							}
						}
						slide.setText("移动滑块");
		           break;          
		          }
		          return false;
			}
			 int backpx(float dpValue) {  
			        final float scale = dm.density;  
			        return (int) (dpValue * scale + 0.5f);  
			 }
	    }
	void toAnswer(){
		try {    
	            Method getITelephonyMethod = TelephonyManager.class.getDeclaredMethod("getITelephony", (Class[]) null);    
	            getITelephonyMethod.setAccessible(true);    
	            mITelephony = (ITelephony) getITelephonyMethod.invoke(manager, (Object[]) null); 
	            answerRingingCall(mApp.get());
	        } catch (Exception e) {    
	            e.printStackTrace();    
	        }    
	}
	void toEnd(){
		try {    
			Method getITelephonyMethod = TelephonyManager.class.getDeclaredMethod("getITelephony", (Class[]) null);    
			getITelephonyMethod.setAccessible(true);    
			mITelephony = (ITelephony) getITelephonyMethod.invoke(manager, (Object[]) null);    
          //拒接来电
			mITelephony.endCall();
          
      } catch (Exception e) {    
          e.printStackTrace();    
      } 
	 }
	
	 private synchronized void answerRingingCall(Context context) {
	        try {  
	            Intent localIntent1 = new Intent(Intent.ACTION_HEADSET_PLUG);  
	            localIntent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);  
	            localIntent1.putExtra("state", 1);  
	            localIntent1.putExtra("microphone", 1);  
	            localIntent1.putExtra("name", "Headset");  
	            context.sendOrderedBroadcast(localIntent1,  
	                    "android.permission.CALL_PRIVILEGED");  
	            Intent localIntent2 = new Intent(Intent.ACTION_MEDIA_BUTTON);  
	            KeyEvent localKeyEvent1 = new KeyEvent(KeyEvent.ACTION_DOWN,  
	                    KeyEvent.KEYCODE_HEADSETHOOK);  
	            localIntent2.putExtra("android.intent.extra.KEY_EVENT",  
	                    localKeyEvent1);  
	            context.sendOrderedBroadcast(localIntent2,  
	                    "android.permission.CALL_PRIVILEGED");  
	            Intent localIntent3 = new Intent(Intent.ACTION_MEDIA_BUTTON);  
	            KeyEvent localKeyEvent2 = new KeyEvent(KeyEvent.ACTION_UP,  
	                    KeyEvent.KEYCODE_HEADSETHOOK);  
	            localIntent3.putExtra("android.intent.extra.KEY_EVENT",  
	                    localKeyEvent2);  
	            context.sendOrderedBroadcast(localIntent3,  
	                    "android.permission.CALL_PRIVILEGED");  
	            Intent localIntent4 = new Intent(Intent.ACTION_HEADSET_PLUG);  
	            localIntent4.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);  
	            localIntent4.putExtra("state", 0);  
	            localIntent4.putExtra("microphone", 1);  
	            localIntent4.putExtra("name", "Headset");  
	            context.sendOrderedBroadcast(localIntent4,  
	                    "android.permission.CALL_PRIVILEGED");  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	 	}
}
