/*
 *  http://www.guet.edu.cn/
 *  by hmg25 20111212
 *  Just For Learning
 */
package com.guet.SiriCN;

import java.io.File;
import java.lang.reflect.Method;

import android.content.Context;
import android.util.Log;


public class jtTTS {
	  private Class mClassTTS;
	  private Context mContext;
	  private Object mObjectTTS;
	  public jtTTS(Context contex){
	      try {
		  mContext = contex.createPackageContext("com.sinovoice.sharedtts", 3);		
	      mClassTTS =Class.forName("com.sinovoice.sharedtts.SharedTTS", true, mContext.getClassLoader());
	      Method  method = mClassTTS.getMethod("init",null);
	      method.invoke(null);	
	      method = mClassTTS.getMethod("getInstance", null);	
	      mObjectTTS = method.invoke(null);	 
	      method =mClassTTS.getMethod("setContext",Context.class);
	      method.invoke(mObjectTTS, mContext);
	      } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
	}
	
	  public int initTTSLib(int param)
	  {
	    try
	    {	     
	      Method method = mClassTTS.getMethod("setContext", Context.class);
	      method.invoke(mObjectTTS, mContext);  
	      method=mClassTTS.getMethod("initTTSLib",Integer.TYPE);
	      int j = ((Integer)method.invoke(mObjectTTS, Integer.valueOf(param))).intValue();
	      return j;
	    }
	    catch (Exception e)
	    {
	          
	        Log.e("hmg", "initTTSLib error ");
	        return -1;
	    }
	  }
	  
	  public void playText(String strMsg)
	  {
	    try
	    {
	      Method method = mClassTTS.getMethod("playText", String.class);
	      method.invoke(mObjectTTS,strMsg);
	      return;
	    }
	    catch (Exception e)
	    {
	    	  Log.e("hmg", "playText error ");
	    }
	  }
	  
	  public void playFile(File file, long l)
	  {
	    try
	    {
	      Class[] cls = new Class[2];
	      cls[0] = File.class;
	      cls[1] = Long.TYPE;
	      Method method = mClassTTS.getMethod("playFile", cls);     
	      Object[] obj = new Object[2];
	      obj[0] = file;
	      obj[1] = Long.valueOf(l);
	      method.invoke(mObjectTTS, obj);
	      return;
	    }
	    catch (Exception e)
	    {
	       Log.e("hmg", "playText error ");
	    }
	  }
	  
	  public int SetParam(int type, long param)
	  {
	    try
	    {
	      Class[] parmCls = new Class[2];
	      parmCls[0] = Integer.TYPE;
	      parmCls[1] =  Long.TYPE;
	      Method method = mClassTTS.getMethod("ttsSetParam",parmCls);
	      Object[] obj = new Object[2];
	      obj [0] = Integer.valueOf(type);
	      obj [1] =  Long.valueOf(param);
	      int j = ((Integer)method.invoke(mObjectTTS, obj)).intValue();
	      return j;
	    }
	    catch (Exception e)
	    {
	    	Log.e("hmg", "SetParam error ");
	    	return -1;
	    }
	  }

	  
	  public int ttsEnd()
	  {
	    try
	    {
	      Method method = mClassTTS.getMethod("ttsEnd",null);
	      int j = ((Integer)method.invoke(mObjectTTS,null)).intValue();
	      return j;
	    }
	    catch (Exception e)
	    {
	    	Log.e("hmg", "ttsEnd error ");
	    	return -1;
	    }
	  }
	  
	  public void ttsPause()
	  {
	    try
	    {
	      Method method = mClassTTS.getMethod("ttsPause", null);
	      method.invoke(mObjectTTS, null);
	      return;
	    }
	    catch (Exception e)
	    {
	    	Log.e("hmg", "ttsPause");
	    }
	  }

	  public void ttsResume()
	  {
	    try
	    {
	      Method method = mClassTTS.getMethod("ttsResume", null);
	      method.invoke(mObjectTTS, null);
	      return;
	    }
	    catch (Exception e)
	    {
	    	Log.e("hmg", "ttsResume");
	    }
	  }

	  public int ttsStop()
	  {
	    try
	    {
	      Method method = mClassTTS.getMethod("ttsStop", null);
	      int j = ((Integer)method.invoke(mObjectTTS, null)).intValue();
	      return j;
	    }
	    catch (Exception e)
	    {
	    	Log.e("hmg", "ttsStop");
	    	return -1;
	    }
	  }
}
