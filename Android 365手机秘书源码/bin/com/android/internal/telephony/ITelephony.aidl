package com.android.internal.telephony;  
 /**  
  * Interface used to interact with the phone.  Mostly this is used by the  
  * TelephonyManager class.  A few places are still using this directly.  
  * Please clean them up if possible and use TelephonyManager instead.  
  * {@hide}  
  */ 

interface ITelephony {      
 /**      
  * End call or go to the Home screen
  * @return whether it hung up     
  */    
	boolean endCall();      
   
   /**      
    * Answer the currently-ringing call.      
    *      
    * If there's already a current active call, that call will be      
    * automatically put on hold.  If both lines are currently in use, the     
    * current active call will be ended.      
    *    
    * TODO: provide a flag to let the caller specify what policy to use     
    * if both lines are in use.  (The current behavior is hardwired to     
    * "answer incoming, end ongoing", which is how the CALL button      
    * is specced to behave.)      
    *      
    * TODO: this should be a oneway call (especially since it's called     
    * directly from the key queue thread).      
    */     
    void answerRingingCall(); 
    
    /**
     * Allow mobile data connections.
     */
    boolean enableDataConnectivity();

    /**
     * Disallow mobile data connections.
     */
    boolean disableDataConnectivity();

    /**
     * Report whether data connectivity is possible.
     */
    boolean isDataConnectivityPossible();
} 