package com.feicong.Utils;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class RootUtils
{
	private static final String BOOT_START_PERMISSION = 
			 "android.permission.RECEIVE_BOOT_COMPLETED";			 			 
	public RootUtils(Context context) {
	}			 
	 /**
　　      * 获取Android开机启动列表
　　      */
	public static List<Map<String, Object>> FetchInstalledApps(Context mContext) {
		PackageManager pm = mContext.getPackageManager();
		List<ApplicationInfo> appInfo = pm.getInstalledApplications(0);
		Iterator<ApplicationInfo> appInfoIterator = appInfo.iterator();
		List<Map<String, Object>> appList = new ArrayList<Map<String, Object>>(appInfo.size());
 
		while (appInfoIterator.hasNext()) {
			ApplicationInfo app = appInfoIterator.next();
			int flag = pm.checkPermission(
					BOOT_START_PERMISSION, app.packageName);
			if (flag == PackageManager.PERMISSION_GRANTED) {
				Map<String, Object> appMap = new HashMap<String, Object>();
				String label = pm.getApplicationLabel(app).toString();
				Drawable icon = pm.getApplicationIcon(app);
				String desc = app.packageName;
				appMap.put("label", label);
				appMap.put("icon", icon);
				appMap.put("desc", desc);
            	appList.add(appMap);
			}
		}
		return appList;
	}
	
	public static void EnableApp(String packageName, boolean bEnable){
		String str;
		if(bEnable)
			str = "enable ";
		else
			str = "disable ";
		RootCommand("su -c \"pm " + str + packageName + "\"");
	}
	
	public static boolean hasRootPermission(){
		Process process = null;  
        DataOutputStream os = null;  
        try{  
            process = Runtime.getRuntime().exec("su");  
            os = new DataOutputStream(process.getOutputStream());  
            os.writeBytes("id\n");  
            os.writeBytes("exit\n");  
            os.flush();  
            process.waitFor();  
        } catch (Exception e){   
            return false;  
        } finally {
            try{  
                if (os != null) {  
                    os.close();  
                }  
                process.destroy();  
            } catch (Exception e) {  
            }  
        }  
		return true;
	}	
	/**
	 * 执行需要Root的命令
	 * @param command 执行的命令行
	 * @return 是否执行成功
	 */
	public static boolean RootCommand(String command){  
        Process process = null;  
        DataOutputStream os = null;  
        try{  
            process = Runtime.getRuntime().exec("su");  
            os = new DataOutputStream(process.getOutputStream());  
            os.writeBytes(command + "\n");  
            os.writeBytes("exit\n");  
            os.flush();  
            process.waitFor();  
        } catch (Exception e){  
            Log.d("*** DEBUG ***", "ROOT failed" + e.getMessage());  
            return false;  
        } finally {
            try{  
                if (os != null) {  
                    os.close();  
                }  
                process.destroy();  
            } catch (Exception e) {  
            }  
        }  
        Log.d("*** DEBUG ***", "Root succeed ");  
        return true;  
    } 
}
