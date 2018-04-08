package com.uvchip.mediacenter;

import java.util.HashMap;
import android.content.Context;
import android.graphics.Bitmap;

public class PreparedResource {
    //private final String TAG = PreparedResource.class.getCanonicalName();
	private final static String FILE_TYPE_DISK = "disk";
    private final static String FILE_TYPE_AAC = "aac";
    private final static String FILE_TYPE_BIN = "bin";
    private final static String FILE_TYPE_BMP = "bmp";
    private final static String FILE_TYPE_DOC = "doc";
    private final static String FILE_TYPE_DOCX = "docx";
    private final static String FILE_TYPE_PDF = "pdf";
    private final static String FILE_TYPE_PPT = "ppt";
    private final static String FILE_TYPE_TXT = "txt";
    private final static String FILE_TYPE_WAV = "wav";
    private final static String FILE_TYPE_WMA = "wma";
    private final static String FILE_TYPE_MP3 = "mp3";
    private final static String FILE_TYPE_XML = "xml";
    private final static String FILE_TYPE_HTML = "html";
    private final static String FILE_TYPE_APK = "apk";
    private final static String FILE_TYPE_ZIP = "zip";
    private final static String FILE_TYPE_RAR = "rar";
    private final static String FILE_TYPE_FOLDER = "folder";
    
    
    
    private HashMap<String, Integer> fileType = new HashMap<String, Integer>();
    private HashMap<String, Bitmap> apkIcon = new HashMap<String, Bitmap>();
    private HashMap<String, String> defaultVideoType = new HashMap<String,String>();
    private HashMap<String, String> defaultAudioType = new HashMap<String,String>();
    private HashMap<String, String> defaultImageType = new HashMap<String,String>();
    private Context mContext;
    public PreparedResource(Context context){
        this.mContext = context;
        fileType.put(FILE_TYPE_DISK, R.drawable.disk);
        fileType.put(FILE_TYPE_AAC, R.drawable.aac);
        fileType.put(FILE_TYPE_BIN, R.drawable.bin);
        fileType.put(FILE_TYPE_BMP, R.drawable.picture);
        fileType.put(FILE_TYPE_DOC, R.drawable.doc);
        fileType.put(FILE_TYPE_DOCX, R.drawable.doc);       //docx
        fileType.put(FILE_TYPE_PDF, R.drawable.pdf);
        fileType.put(FILE_TYPE_PPT, R.drawable.ppt);
        fileType.put(FILE_TYPE_TXT, R.drawable.txt);
        fileType.put(FILE_TYPE_WAV, R.drawable.wav);
        fileType.put(FILE_TYPE_WMA, R.drawable.wma);
        fileType.put(FILE_TYPE_MP3, R.drawable.mp3);
        fileType.put(FILE_TYPE_XML, R.drawable.xml);
        fileType.put(FILE_TYPE_HTML, R.drawable.all);       //html  
        fileType.put(FILE_TYPE_ZIP, R.drawable.zip);        //zip
        fileType.put(FILE_TYPE_RAR, R.drawable.zip);        //rar
        fileType.put(FILE_TYPE_FOLDER, R.drawable.folder);
        fileType.put(FILE_TYPE_APK, R.drawable.app_default_icon);
        loadDefaultMineType();
    }
    
    private void loadDefaultMineType(){
    	defaultVideoType.put("avi", "video/*");
    	defaultVideoType.put("flv", "video/*");
    	defaultVideoType.put("f4v", "video/*");
    	defaultVideoType.put("mpg", "video/*");
    	defaultVideoType.put("mp4", "video/*");
    	defaultVideoType.put("rmvb", "video/*");
    	defaultVideoType.put("rm", "video/*");
    	defaultVideoType.put("mkv", "video/*");
    	defaultVideoType.put("vob", "video/*");
    	defaultVideoType.put("ts", "video/*");
    	defaultVideoType.put("m2ts", "video/*");
    	defaultVideoType.put("m2p", "video/*");
    	defaultVideoType.put("wmv", "video/*");
    	defaultVideoType.put("asf", "video/*");
    	defaultVideoType.put("d2v", "video/*");
    	defaultVideoType.put("ogm", "video/*");
    	defaultVideoType.put("3gp", "video/*");
    	defaultVideoType.put("divx", "video/*");
    	defaultVideoType.put("mpeg", "video/*");
    	defaultVideoType.put("m4v", "video/*");
    	defaultVideoType.put("mov", "video/*");
    	defaultVideoType.put("tp", "video/*");
    	defaultVideoType.put("iso", "video/*");
    	defaultVideoType.put("rt", "video/*");
    	defaultVideoType.put("qt", "video/*");
    	defaultVideoType.put("ram", "video/*");
    	defaultVideoType.put("vod", "video/*");
    	defaultVideoType.put("dat", "video/*");
    	
    	defaultImageType.put("png", "image/*");
    	defaultImageType.put("jpg", "image/*");
    	defaultImageType.put("jpeg", "image/*");
    	defaultImageType.put("gif", "image/*");
    	defaultImageType.put("bmp", "image/*");
    	
    	defaultAudioType.put("mp3", "audio/*");
    	defaultAudioType.put("wav", "audio/*");
    	defaultAudioType.put("ogg", "audio/*");
    	defaultAudioType.put("wma", "audio/*");
    	defaultAudioType.put("wave", "audio/*");
    	defaultAudioType.put("midi", "audio/*");
    	defaultAudioType.put("mp2", "audio/*");
    	defaultAudioType.put("aac", "audio/*");
    	defaultAudioType.put("amr", "audio/*");
    	defaultAudioType.put("ape", "audio/*");
    	defaultAudioType.put("flac", "audio/*");
    	defaultAudioType.put("m4a", "audio/*");
    }
    
    public String getMineType(String str){
    	if(defaultAudioType.containsKey(str)){
    		return defaultAudioType.get(str);
    	} else if(defaultVideoType.containsKey(str)){
    		return defaultVideoType.get(str);
    	} else {
    		return defaultImageType.get(str);
    	}
    }
    
    public boolean isAudioFile(String key){
    	if(defaultAudioType.containsKey(key)){
    		return true;
    	}
    	return false;
    }
    public boolean isVideoFile(String key){
    	if(defaultVideoType.containsKey(key))
    		return true;
    	return false;
    }
    public boolean isImageFile(String key){
    	if(defaultImageType.containsKey(key)){
    		return true;
    	}
    	return false;
    }
        
    public int getBitMap(String key){       
        if(fileType.containsKey(key)){
        	return fileType.get(key);
        }else if (isImageFile(key)) {
			return R.drawable.picture;
		}else if (isVideoFile(key)) {
			return R.drawable.videofile;
		}else{
        	return R.drawable.all;
        }
    }
    public void recycle(){
        this.apkIcon.clear();
        this.fileType.clear();
    }
}
