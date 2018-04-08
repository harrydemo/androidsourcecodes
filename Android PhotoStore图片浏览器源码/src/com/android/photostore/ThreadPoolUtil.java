package com.android.photostore;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*****************Copyright (C), 2010-2015, FORYOU Tech. Co., Ltd.********************/
/**
 * @Filename: ThreadPoolUtil.java
 * @Author: wanghb 
 * @Email: wanghb@foryouge.com.cn
 * @CreateDate: 2011-6-25
 * @Description: description of the new class
 * @Others: comments
 * @ModifyHistory:
 */
public class ThreadPoolUtil {
	
	private static ExecutorService executorService;
	
	private static int maxThreadNum = 40;
	
	private static void initService(){
		if(executorService == null){
			executorService = Executors.newFixedThreadPool(maxThreadNum);
		}
	}
	
	/**
	 * @Description:Ö´ÐÐ
	 * @Author: wanghb 
	 * @Email: wanghb@foryouge.com.cn
	 * @param runnable
	 * @Others:
	 */
	public static void addRunnable(final Runnable runnable) {
		initService();
		executorService.execute(new Runnable() {
			public void run() {
				try {
					runnable.run();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});
	}

}
