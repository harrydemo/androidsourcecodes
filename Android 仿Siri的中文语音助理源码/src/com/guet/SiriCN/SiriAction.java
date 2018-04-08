/*
 *  http://www.guet.edu.cn/
 *  by hmg25 20111212
 *  Just For Learning
 */
package com.guet.SiriCN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SiriAction {
	enum VoiceAction{NONE,CALL,SMS,CONTACT,SEARCH,OPENAPP,PLAY}
	
	  private static final String mActionTemplate = "(.*)(action)(.*)?";
	  private static final String[] callSynonyms={"拨打","呼叫","call","打电话"};
	  private static final String[] openSynonyms={"打开","运行","启动"};
	  private static final String[] playSynonyms={"播放"};
	  private static final String[] ignorePhrase={"请","麻烦","给","请"};
	  private static final String[] searchSynonyms={"搜索","查询"};
	  public String[] parameter;
	  public VoiceAction action;
	  public  SiriAction(){
		  action=VoiceAction.NONE;
		  parameter=new String[2];
	  }
	  
	  
	public boolean checkCall(String msg)
	  {
       String callAction;
	    for(int i=0;i<callSynonyms.length;i++)
	    {
	      callAction=mActionTemplate;
	      callAction=callAction.replaceAll("action", callSynonyms[i]);
	      Matcher m = Pattern.compile(callAction).matcher(msg);
	      if(m.matches()){
	    	  action=VoiceAction.CALL;
	    	  parameter[0]=m.group(3);  
	    	  return true;
	      }	      
	    }
	    return false;
	  }
	
	public boolean checkOpen(String msg)
	  {
      String openAction;
	    for(int i=0;i<openSynonyms.length;i++)
	    {
	      openAction=mActionTemplate;
	      openAction=openAction.replaceAll("action", openSynonyms[i]);
	      Matcher m = Pattern.compile(openAction).matcher(msg);
	      if(m.matches()){  
	    	  action=VoiceAction.OPENAPP;
	    	  parameter[0]=m.group(3); 
	    	  return true;
	      }	      
	    }
	    return false;
	  }
	
	public boolean checkPlay(String msg)
	  {
         String playAction;
	    for(int i=0;i<playSynonyms.length;i++)
	    {
	    	playAction=mActionTemplate;
	    	playAction=playAction.replaceAll("action", playSynonyms[i]);
	      Matcher m = Pattern.compile(playAction).matcher(msg);
	      if(m.matches()){  
	    	  action=VoiceAction.PLAY;
	    	  parameter[0]=m.group(3);
	    	  return true;
	      }	      
	    }
	    return false;
	  }
	
	public boolean checkWebSearch(String msg)
	  {
       String searchAction;
	    for(int i=0;i<searchSynonyms.length;i++)
	    {
	    	searchAction=mActionTemplate;
	    	searchAction=searchAction.replaceAll("action", searchSynonyms[i]);
	        Matcher m = Pattern.compile(searchAction).matcher(msg);
	        if(m.matches()){  
	    	  action=VoiceAction.SEARCH;
	    	  parameter[0]= m.group(3);
	    	  parameter[1]= m.group(1);
	    	  return true;
	      }	      
	    }
	    return false;
	  }
	
	 public boolean doAction(String msg)
	  {  
		  for(int i=0;i<ignorePhrase.length;i++)
		   {
			  msg=msg.replace(ignorePhrase[i], "");
		   }
	       if(checkCall(msg))return true;
	       if(checkOpen(msg))return true;
	       if(checkPlay(msg))return true;
	       if(checkWebSearch(msg))return true;
	       return false;
	  }
    
	 
	
}
