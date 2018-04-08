package com.orange.gpstest;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.os.Message;

/**
 * This class represents Chrono Timer
 * @author Alan Sowamber
 */

public class ChronoTimer {
    Timer timer;
    int seconds;
    int chronoValue;
    IChronoEventListener listener;
    RemindHandler remindHandler;
    
    private static final int START=0x0;
    private static final int STOP=0x1;
    private static final int CHRONO=0x2;
    

    /*
     * Create the timer passing time value
     * @param int time in seconds
     */
    public ChronoTimer(int seconds) {
        timer = new Timer();
        this.seconds=seconds;
        chronoValue=0;
        this.listener=null;
        
        remindHandler=new RemindHandler();
	}
    
    public ChronoTimer(int seconds,IChronoEventListener listener) {
        timer = new Timer();
        this.seconds=seconds;
        chronoValue=0;
        this.listener=listener;
        
        remindHandler=new RemindHandler();
	}
    
    

    class RemindTask extends TimerTask {
    	
    	
        public void run() {
        	chronoValue++;
        	Message msg=new Message();
        	msg.arg1=CHRONO;
        	msg.arg2=chronoValue;
        	
        	remindHandler.sendMessage(msg);
        }
        
        
    }
    
    
    
    public void startChrono()
    {
    	chronoValue=0;
    	timer.cancel();
    	timer = new Timer();
    	timer.scheduleAtFixedRate(new RemindTask(),seconds*1000, seconds*1000);
    	
    	Message msg=new Message();
    	msg.arg1=START;
    	remindHandler.sendMessage(msg);
    	
    	
    	
    	
    }
    
    public void stopChrono()
    {
    	
    	timer.cancel();
    	
    	
    	Message msg=new Message();
    	msg.arg1=STOP;
    	remindHandler.sendMessage(msg);
    }
    
    class RemindHandler extends Handler{
    	
    	
    	public void handleMessage(Message msg){
    		switch(msg.arg1){
    		case START:
    			if(listener!=null)
    	    		listener.onChronoStart();
    			break;
    		case STOP:
    			if(listener!=null)
    	    		listener.onChronoStop();
    			break;
    		case CHRONO:
    			if(listener!=null)
    	    		listener.onChronoReached(msg.arg2);
    			break;
    		}
    		
    		
    	}
    	
    }
   

   
}

