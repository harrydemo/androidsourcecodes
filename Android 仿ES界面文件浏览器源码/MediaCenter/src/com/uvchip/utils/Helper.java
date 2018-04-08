
package com.uvchip.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.uvchip.files.FileItem;
import com.uvchip.files.FileItemForOperation;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;


public class Helper {
    
	public static List<FileItemForOperation> GetData(File folder){
        List<FileItemForOperation> mdata = new ArrayList<FileItemForOperation>();
        if (folder.exists()) {
            if (folder.isFile()) {
                return mdata;
            } else if (folder.isDirectory()) {
                File[] files = folder.listFiles();
                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        FileItem fileOrfolder = new FileItem();
                        String fileName = files[i].getName();
                        if(Helper.isHiden(fileName)){
                            break;
                        }
                        String extraName = fileName.substring(fileName.lastIndexOf(".") + 1);
                        extraName = extraName.toUpperCase();
                        boolean isDir = files[i].isDirectory();
                        if(isDir)extraName = "FOLDER";
                        fileOrfolder.setDirectory(isDir);
                        fileOrfolder.setFileName(fileName);
                        fileOrfolder.setExtraName(extraName);
                        fileOrfolder.setFilePath(files[i].getAbsolutePath());
                        fileOrfolder.setFileSize(files[i].length());
                        FileItemForOperation fileItem = new FileItemForOperation();
                        fileItem.setFileItem(fileOrfolder);
                        mdata.add(fileItem);
                    }
                }
            }
        }
        return mdata;
    }
	
	 public static boolean isHiden(String folder){
//	        String[] hidenFolders = MyApplication.hidenFolders;
//	        if(hidenFolders.length == 0)return false;
//	        for (String string : hidenFolders) {
//	            if(folder.equals(string)){
//	                return true;
//	            }
//	        }
	        return false;
	    }
    private static final String TAG = Helper.class.getCanonicalName();
    
    
    /**
     * 截取文件路径的最后文件名. 根目录返回 根目录 /, 如果文件名最后是/, 返回空字符串. / --> / /path --> /path
     * /path/1 --> 1 /path/1/ --> ""
     */
    public static String getFolderNameOfPath(String path) {
        if(path.endsWith("/"))
            path = path.substring(0, path.length()- 1);
        int index = path.lastIndexOf('/');
        if (index == -1 || index == 0)
            return path;
        return path.substring(index + 1);
    }
    /**
     * 获取文件的上级目录名称 ，根目录返回null
     * . /mnt/sdcard -->/mnt  /mnt/sdcard/1.txt -->/mnt/sdcard  /-->null
     * 
     */
    public static String getParentNameofPath(String path){
    	if(path.equals("/")){
    		return null;
    	}
    	if(path.endsWith("/")){
    		path = path.substring(0,path.length()-1);
    	}
    	int index = path.lastIndexOf('/');
    	if(path.length()== 1){
    		return path;
    	}
    	if(index==0){
    		return "/";
    	}
    	return path.substring(0,index);
    }
    
    public static void reverseList(List<?> list){
    	
    }

    /**
     * 将文件名后面,后缀前面加Str.
     */
    public static String getNameAppendStr(String path, String Str) {
        int i = path.lastIndexOf(".");
        if (i == -1 || i == 0)
            return path + Str;
        return path.substring(0, i) + Str + path.substring(i, path.length());
    }
    
    public static String newFolder(String currFolder,String newFolder){
        if(!currFolder.endsWith("/")){
            currFolder += "/";
        }
        return currFolder + newFolder;
    }
    /**
     * 验证文件名
     * @param str
     * @return
     */
    public static boolean validateFileName(String str) {
    	if(str.trim().length()==0){
    		return false;
    	}
        String strPattern = "[^/\\:*?\"<>|]+";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(str);
        return m.matches();
    }
    public static Integer[] ints = new Integer[]{0,0};
    /**
     * 递归获取文件夹里所有文件的总大小 BUG: 不能分辨链接文件
     */
    public static long getDirectorySize(File f) throws IOException {
        long size = 0;
        File flist[] = f.listFiles();
        if (flist == null)
            return f.length();
        int length = flist.length;
        for (int i = 0; i < length; i++) {
            if (flist[i].isDirectory()) {
                Log.i(TAG,"AbsolutePath=======>" + flist[i].getAbsolutePath());
                size = size + getDirectorySize(flist[i]);
                ints[0]++;
            } else {
                size = size + flist[i].length();
                ints[1]++;
            }
        }
        return size;
    }

    /**
     * BUG: 不能分辨链接文件
     */
    public static long getDirectorySize(String fp) throws IOException {
        long size = 0;
        File f = new File(fp);
        File flist[] = f.listFiles();
        if (flist == null)
            return f.length();
        int length = flist.length;
        for (int i = 0; i < length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getDirectorySize(flist[i]);
            } else {
                size = size + flist[i].length();
            }
        }
        return size;
    }
    /**
     * 格式化文件的大小描述
     * @param size 文件大小
     * @return
     */
    public static String formatFromSize(long size) {
        if(size == -1) return "";
        String suffix = " B";
        if (size >= 1024) {
            suffix = " KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = " MB";
                size /= 1024;
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffix != null)
            resultBuffer.append(suffix);
        return resultBuffer.toString();
    }
    /**
     * 读取配置文件
     */
    public String[] loadProperties(){
        String[] limitFolders;
        Properties properties = new Properties();
        try {
            properties.load( getClass().getResourceAsStream( "defaultConfig.properties" ) );
            String limitFolderStr = properties.getProperty("limitFolder");
            
            if(limitFolderStr.indexOf("|") > 0){
                limitFolders = limitFolderStr.split("|");
            }else
            {
                limitFolders = new String[]{limitFolderStr};
            }
            return limitFolders;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 重命名一个文件
     * @param fileItem 用于操作的文件
     * @param newName 新的文件名
     * @return
     */
    public static String[] reName(FileItem fileItem,String newName) {
        File tmpFile = new File(fileItem.getFilePath());
        String prePath = tmpFile.getParent();
        if(prePath != null)
        {
            if(!prePath.endsWith("/"))
                prePath += "/";
            if(newName.lastIndexOf(".") > 0)
                return new String[]{prePath + newName,newName};
            else{
                String extraName = fileItem.getExtraName();
                if(extraName.equals("FOLDER"))
                    return new String[]{prePath + newName,newName};
                else
                    return new String[]{prePath + newName + "." + extraName.toLowerCase(),
                    newName + "." + extraName.toLowerCase()};
            }
        }
        return new String[]{};
    }
    /**
     * 
     * @param 文件的位置
     * @return 文件的根位置  如  /mnt/sdcard/aaa ---> /mnt/sdcard /mnt/innerDisk/ ---> /mnt/innerDisk
     */
    public static String getRoot(String loc){
    	if(loc.startsWith("/mnt/sdcard")){
    		return "/mnt/sdcard";
    	}else if(loc.startsWith("/mnt/innerDisk")){
    		return "/mnt/innerDisk";
    	}else{
    		return "/mnt/usbDisk1";
    	}
    	//return loc;
    }
    /**
     * 用来得到没有安装上的应用程序的icon
     * @param context
     * @param apkPath
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Drawable showUninstallAPKIcon(Context context,String apkPath) {
        String PATH_PackageParser = "android.content.pm.PackageParser";
        String PATH_AssetManager = "android.content.res.AssetManager";
        try {
            // apk包的文件路径
            // 这是一个Package 解释器, 是隐藏的
            // 构造函数的参数只有一个, apk文件的路径
            // PackageParser packageParser = new PackageParser(apkPath);
            Class pkgParserCls = Class.forName(PATH_PackageParser);
            Class[] typeArgs = new Class[1];
            typeArgs[0] = String.class;
            Constructor pkgParserCt = pkgParserCls.getConstructor(typeArgs);
            Object[] valueArgs = new Object[1];
            valueArgs[0] = apkPath;
            Object pkgParser = pkgParserCt.newInstance(valueArgs);
            //Log.d("ANDROID_LAB", "pkgParser:" + pkgParser.toString());
            // 这个是与显示有关的, 里面涉及到一些像素显示等等, 我们使用默认的情况
            DisplayMetrics metrics = new DisplayMetrics();
            metrics.setToDefaults();
            // PackageParser.Package mPkgInfo = packageParser.parsePackage(new
            // File(apkPath), apkPath,
            // metrics, 0);
            typeArgs = new Class[4];
            typeArgs[0] = File.class;
            typeArgs[1] = String.class;
            typeArgs[2] = DisplayMetrics.class;
            typeArgs[3] = Integer.TYPE;
            Method pkgParser_parsePackageMtd = pkgParserCls.getDeclaredMethod("parsePackage",
                    typeArgs);
            valueArgs = new Object[4];
            valueArgs[0] = new File(apkPath);
            valueArgs[1] = apkPath;
            valueArgs[2] = metrics;
            valueArgs[3] = 0;
            Object pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser, valueArgs);
            // 应用程序信息包, 这个公开的, 不过有些函数, 变量没公开
            // ApplicationInfo info = mPkgInfo.applicationInfo;
            Field appInfoFld = pkgParserPkg.getClass().getDeclaredField("applicationInfo");
            ApplicationInfo info = (ApplicationInfo) appInfoFld.get(pkgParserPkg);
            // uid 输出为"-1"，原因是未安装，系统未分配其Uid。
            //Log.d("ANDROID_LAB", "pkg:" + info.packageName + " uid=" + info.uid);
            // Resources pRes = getResources();
            // AssetManager assmgr = new AssetManager();
            // assmgr.addAssetPath(apkPath);
            // Resources res = new Resources(assmgr, pRes.getDisplayMetrics(),
            // pRes.getConfiguration());
            Class assetMagCls = Class.forName(PATH_AssetManager);
            Constructor assetMagCt = assetMagCls.getConstructor((Class[]) null);
            Object assetMag = assetMagCt.newInstance((Object[]) null);
            typeArgs = new Class[1];
            typeArgs[0] = String.class;
            Method assetMag_addAssetPathMtd = assetMagCls.getDeclaredMethod("addAssetPath",
                    typeArgs);
            valueArgs = new Object[1];
            valueArgs[0] = apkPath;
            assetMag_addAssetPathMtd.invoke(assetMag, valueArgs);
            Resources res = context.getResources();
            typeArgs = new Class[3];
            typeArgs[0] = assetMag.getClass();
            typeArgs[1] = res.getDisplayMetrics().getClass();
            typeArgs[2] = res.getConfiguration().getClass();
            Constructor resCt = Resources.class.getConstructor(typeArgs);
            valueArgs = new Object[3];
            valueArgs[0] = assetMag;
            valueArgs[1] = res.getDisplayMetrics();
            valueArgs[2] = res.getConfiguration();
            res = (Resources) resCt.newInstance(valueArgs);
            //CharSequence label = null;
            //if (info.labelRes != 0) {
                //label = res.getText(info.labelRes);
            //}
            // if (label == null) {
            // label = (info.nonLocalizedLabel != null) ? info.nonLocalizedLabel
            // : info.packageName;
            // }
            //Log.d("ANDROID_LAB", "label=" + label);
            // 这里就是读取一个apk程序的图标
            if (info.icon != 0) {
                Drawable icon = res.getDrawable(info.icon);
                return icon;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
