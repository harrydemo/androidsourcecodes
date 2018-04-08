﻿package com.uvchip.files;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.util.Log;
import com.uvchip.files.FileManager.FilesFor;
import com.uvchip.utils.Helper;

public class FileOperationThreadManager {
	
	final boolean DEBUG = false;
	static final String TAG = FileOperationThreadManager.class.getCanonicalName();
	
	public static final int NEWFOLDER_SUCCEED = 10;
	public static final int NEWFOLDER_FAILED = 20;
	public static final int RENAME_SUCCEED          = 100;
    public static final int RENAME_FAILED           = 200;
    
	public static final String KEY_NEW_NAME         = "newname";
	public static final String KEY_NEW_PATH         = "newpath";
	public static final String KEY_CURR_NAME        = "curr_name";
	
    public static final int PASTE_SUCCEED           = 1000;
    public static final int PASTE_COMPLETED         = 2000;
    public static final int PASTE_PROGRESS_CHANGE   = 3000;
    public static final int PASTE_FAILED            = 4000;
    public static final int PASTE_CANCEL            = 5000;
    public static final int PASTE_PAUSE             = 6000;
    
    
    public static final int MOVE_SUCCEED			= 1100;
    public static final int MOVE_COMPLETED			= 1200;
    public static final int MOVE_PROGRESS_CHANGE	= 1300;
    public static final int MOVE_FAILED				= 1400;
    public static final int MOVE_CANCEL				= 1500;
    public static final int MOVE_PAUSE				= 1600;
    
    public static final int DELETE_COMPLETED        = 10000;
    public static final int DELETE_PROGRESS_CHANGE  = 20000;
    public static final int DELETE_FAILED           = 30000;
    public static final int DELETE_CANCEL           = 40000;
	
	public static final int FAILED_REASON_UNKOWN                 = 0;
	public static final int FAILED_REASON_FROM_FILE_NOT_EXIST    = 1;
	public static final int FAILED_REASON_READ_ONLY_FILE_SYSTEM  = 2;
	public static final int FAILED_REASON_INVALNAME              = 3;
	public static final int FAILED_REASON_FOLDER_HAS_EXIST       = 4;
	public static final int FAILED_REASON_FOLDER_LIMIT           = 5;
	public static final int FAILED_REASON_SAME_FOLDER            = 6;
	public static final int FAILED_REASON_GETSIZE_ERROR          = 7;
	public static final int FAILED_REASON_NO_SPACE_LEFT          = 8;
	public static final int FAILED_REASON_PASTE_NOT_ALLOWED 	 = 9;
	
	public static final int PAUSE_REASON_TO_FOLDER_HAS_EXIST     = 1;
	public static final int PAUSE_REASON_CANNOT_COVER            = 2;
    
	public static final int GETSIZE_COMPLETED                    = 100000;
	public static final int GETSIZE_ERROR              		  = 200000;
    
	public static final int GETTOTALNUM_COMPLETED					= 1000000;
	public static final int GETTOTALNUM_ERROR						= 2000000;
    
    public static final int LOADCAPACITY					= 12345;
    public static final int LOADCAPACITYOK					= 12346;
    /**
     * 粘贴操作的文件来源
     */
    private FilesFor filesFor = null;
    
    private boolean canceled = false;
    
    private boolean getSizeCompleted = false;
    
    private int pasted_rate;
    /**
     * 删除文件夹，里面的只读文件
     */
    private File rdOnlyFile; 
    
    /**
     * 是否剪切粘贴在同一级根目录下
     */
    public boolean cutInSameRoot = false;
    
    
    private List<CapacityItem> mDiscs;
       
    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what) {
                case PASTE_SUCCEED:
                    //如果是剪切文件，需要删除原来的文件
                    if(filesFor == FilesFor.CUT && mSrcToDir.get(currPosition) != null){
                    	//如果原文件不是一个目录，就删除
                    	if(!mSrcToDir.get(currPosition).getFileItem().isDirectory()){
                    	//	System.out.println("删除文件---->"+mSrcToDir.get(currPosition).getFileItem().getFileName());
                    		doDelete(mSrcToDir.get(currPosition).getFileItem());
                    	}
                    }
                    //先发送一次更改的消息
                    responseHandler.sendMessage(getProgressChangeMsg());
                    //粘贴成功后先检验是否到了最后一个
                    if(!mSrcToDir.containsKey(currPosition + 1)){
                        if(DEBUG)Log.i(TAG, "currPosition==========>" + (currPosition + 1));
                        if(filesFor == FilesFor.CUT){
                            for (FileItemForOperation operationFile : folders) {
                            	//递归删除第一层的目录
                                doDelete(operationFile.getFileItem());
                            }
                        }
                        responseHandler.sendEmptyMessage(PASTE_COMPLETED);
                    }
                    else{
                        if(!canceled){
                            currPosition++;
                            beginPaste(doitAsSame ? currOperationType : CopyOperation.UNKOWN);
                        }else{
                            responseHandler.sendEmptyMessage(PASTE_CANCEL);
                        }
                    }
                    break;
                case PASTE_FAILED:
                    responseHandler.sendMessage(responseMsg(PASTE_FAILED,FAILED_REASON_UNKOWN));
                    break;
                case PASTE_FAILED + FAILED_REASON_FROM_FILE_NOT_EXIST:
                    responseHandler.sendMessage(responseMsg(PASTE_FAILED,FAILED_REASON_FROM_FILE_NOT_EXIST));
                    break;
                case PASTE_FAILED + FAILED_REASON_READ_ONLY_FILE_SYSTEM:
                    Message tmpMsg = responseMsg(PASTE_FAILED,FAILED_REASON_READ_ONLY_FILE_SYSTEM,toFolder);
                    responseHandler.sendMessage(tmpMsg);
                    break;
                case PASTE_PAUSE + PAUSE_REASON_TO_FOLDER_HAS_EXIST:
                    responseHandler.sendMessage(responseMsg(PASTE_PAUSE,PAUSE_REASON_TO_FOLDER_HAS_EXIST));
                    break;
                case PASTE_PAUSE + PAUSE_REASON_CANNOT_COVER:
                    responseHandler.sendMessage(responseMsg(PASTE_PAUSE,PAUSE_REASON_CANNOT_COVER));
                    break;
                case PASTE_FAILED + FAILED_REASON_FOLDER_LIMIT:
                    responseHandler.sendMessage(responseMsg(PASTE_FAILED,FAILED_REASON_FOLDER_LIMIT));
                    break;
                case PASTE_FAILED + FAILED_REASON_SAME_FOLDER:
                    responseHandler.sendMessage(responseMsg(PASTE_FAILED,FAILED_REASON_SAME_FOLDER));
                    break;
                case PASTE_FAILED + FAILED_REASON_NO_SPACE_LEFT:
                    responseHandler.sendMessage(responseMsg(PASTE_FAILED,FAILED_REASON_NO_SPACE_LEFT));
                    break;
                case DELETE_FAILED:
                    responseHandler.sendMessage(responseMsg(DELETE_FAILED,FAILED_REASON_UNKOWN));
                    break;
                case GETSIZE_COMPLETED:
                    getSizeCompleted = true;
                    Message tmp = new Message();
                    tmp.what = GETSIZE_COMPLETED;
                    tmp.arg1 = totalFileNum;
                    responseHandler.sendMessage(tmp);
                    break;
                case GETSIZE_ERROR:
                    responseHandler.sendMessage(responseMsg(PASTE_FAILED,FAILED_REASON_GETSIZE_ERROR));
                    break;
                case MOVE_SUCCEED:
                	pasted_rate = 100*(currPosition+1)/totalFileNum;
                	responseHandler.sendMessage(getProgressChangeMsg());
                	//检验是否已经到达最后一个
                	if(currPosition+1==totalFileNum){
        				responseHandler.sendEmptyMessage(PASTE_COMPLETED);
                	}else{
                		if(!canceled){
                			currPosition++;
                			doMove(doitAsSame ? currOperationType : CopyOperation.UNKOWN);
                		}else{
        					responseHandler.sendEmptyMessage(PASTE_CANCEL);  
                		}
                	}
                	break;
                case MOVE_FAILED:
                	responseHandler.sendMessage(responseMsg(PASTE_FAILED,FAILED_REASON_UNKOWN));
                	break;
                case MOVE_FAILED + FAILED_REASON_READ_ONLY_FILE_SYSTEM:
                	Message tempMsg = responseMsg(PASTE_FAILED,FAILED_REASON_READ_ONLY_FILE_SYSTEM,mFiles.get(currPosition).getFileItem().getFilePath());
                	responseHandler.sendMessage(tempMsg);
                	break;
                case MOVE_PAUSE + PAUSE_REASON_CANNOT_COVER:
                	break;
                default:
                    break;
            }
        }
    };

	/**
	 * 回传处理结果的handler
	 */
	Handler responseHandler;
	FileItemForOperation mOperationFile;
	List<FileItemForOperation> mFiles;      //待处理的文件集合
    int currPosition = 0;                   //当前处理的文件
    String toFolder;                        //目标文件夹
    File toFolderF;
    boolean doitAsSame;                     //是否做相同的操作
    int delCount;
    
    List<FileItemForOperation> folders = new ArrayList<FileItemForOperation>();
    /**
     * 用来保存将要复制或剪切的文件，及对应目标文件夹
     */
    Hashtable<Integer, FileItemForOperation> mSrcToDir;
    
    private CopyOperation currOperationType;
    
    public static enum CopyOperation{COVER,JUMP,APPEND2,CANCEL,UNKOWN};
	
    
    
	public boolean isCanceled() {
		return canceled;
	}
	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
	public boolean isDoitAsSame() {
		return doitAsSame;
	}
	public void setDoitAsSame(boolean doitAsSame) {
		this.doitAsSame = doitAsSame;
	}
	public FilesFor getFilesFor() {
		return filesFor;
	}
	public void setFilesFor(FilesFor filesFor) {
		this.filesFor = filesFor;
	}
	
	public FileOperationThreadManager(){
		
	}
	
	public FileOperationThreadManager(List<CapacityItem> discs){
		mDiscs = discs;
	}
	
	public FileOperationThreadManager(Handler handler){
		responseHandler = handler;
	}
	public FileOperationThreadManager(FileItemForOperation operationFile,Handler handler){
		mOperationFile = operationFile;
		responseHandler = handler;
	}
	public FileOperationThreadManager(List<FileItemForOperation> list,Handler handler){
		this.responseHandler = handler;
		this.mFiles = list;
	}
	public FileOperationThreadManager(List<FileItemForOperation> files,String currFolder, Handler handler,FilesFor filesFor){
		this.filesFor = filesFor;
		this.responseHandler = handler;
		this.setToFolder(currFolder);
		setOperatingFiles(files);
	}
	
	/**
	 * 显示出磁盘容量
	 */
	public void showDiscCapacity(final Handler handler,final int rand){
		final File file = new File(FileManager.HOME);
		new Thread(){
			private int i = 0;
			public void run(){
				file.listFiles(new FileFilter() {	
					@Override
					public boolean accept(File pathname) {
						if(!canceled){
							if(pathname.getAbsolutePath().equals("/mnt/sdcard")
							        || pathname.getAbsolutePath().equals("/mnt/innerDisk")
							        || pathname.getAbsolutePath().startsWith("/mnt/usbDisk")
							        || pathname.getAbsolutePath().equals("/mnt/ext_sd")){
								mDiscs.add(new CapacityItem(pathname));
								handler.sendEmptyMessage(LOADCAPACITY);
								return true;
							}else{
								return false;
							}
						}else{
							return false;
						}
					}
				});
				handler.sendMessage(handler.obtainMessage(LOADCAPACITYOK, rand, 0));
			}
		}.start();
	}
	
	
	public void setOperatingFiles(List<FileItemForOperation> files){
        this.mFiles = files; 
        String filesParent = Helper.getParentNameofPath(files.get(0).getFileItem().getFilePath());
        if(filesFor == FilesFor.CUT){
            //先判断第一个from文件是否有写的权限，如果没有直接返回
              FileItemForOperation fileForOperation = files.get(0);
              FileItem fileItem = fileForOperation.getFileItem();
              File fileFrom = new File(fileItem.getFilePath());
              if(!fileFrom.canWrite()){
                  responseHandler.sendMessage(responseMsg(PASTE_FAILED, 
                          FAILED_REASON_READ_ONLY_FILE_SYSTEM,fileItem.getFilePath()));
                  return;
              }
              // 如果在同一个文件夹下面不允许移动文件操作
              if(toFolder.equals(filesParent)||toFolder.equals(filesParent+"/")){
              	responseHandler.sendMessage(responseMsg(PASTE_FAILED, FAILED_REASON_SAME_FOLDER));
          		return;
              }
              //在同一个磁盘上移动文件
              if(Helper.getRoot(toFolder).equals(Helper.getRoot(filesParent))){
            	  cutInSameRoot = true;
            	  for(int i = 0; i < files.size(); i++){
              		// 如果要移动到的文件夹是原文件夹的子文件夹 则不允许移动
                  	if(toFolder.startsWith(files.get(i).getFileItem().getFilePath())){
                  		responseHandler.sendMessage(responseMsg(PASTE_FAILED, FAILED_REASON_PASTE_NOT_ALLOWED));
                  		return;
                  	}
                  }
	     		  toFolderF = new File(toFolder);
	     		  if(!toFolderF.exists()){
	     		  	  toFolderF.mkdirs();
	     		  }
	     		  if(!toFolderF.canWrite()){
	     			  //发送消息  移动失败，toFolderF为只读文件
	     			  responseHandler.sendMessage(responseMsg(PASTE_FAILED, 
	                        FAILED_REASON_READ_ONLY_FILE_SYSTEM,toFolder));
	     			  return;
	     		  }
	     		 totalFileNum = mFiles.size();
            	 doMove(CopyOperation.UNKOWN);
            	 return;
              }
        }
        mSrcToDir = new Hashtable<Integer, FileItemForOperation>();
        for(int i = 0; i < files.size(); i++){
    		// 如果要复制到的文件夹是原文件夹的子文件夹 则不允许复制
        	if(toFolder.startsWith(files.get(i).getFileItem().getFilePath())){
        		responseHandler.sendMessage(responseMsg(PASTE_FAILED, FAILED_REASON_PASTE_NOT_ALLOWED));
        		return;
        	}
            FileItemForOperation fileItemForOperation = files.get(i);
            fileItemForOperation.setDirFolder(this.toFolder);
            mSrcToDir.put(i, fileItemForOperation);
        }
        getSize();
    }
	/**
     * 移动文件
     * @param type
     */
    public void doMove(CopyOperation type){
        if(DEBUG)Log.i(TAG, "paste currPosition===========>" + currPosition);
        currOperationType = type;
        new MoveThread().start();
    }
	/**
	 * 在同一级根目录下移动文件的操作
	 * @author dongli
	 *
	 */
	private class MoveThread extends Thread{
		
		@Override
		public void run() {
			FileItem fileItem = mFiles.get(currPosition).getFileItem();
			String oldFilePath = fileItem.getFilePath();
			File oldFile = new File(oldFilePath);
			if(!oldFile.canWrite()){
				//发送消息  移动失败，oldFile为只读文件
				mHandler.sendMessage(responseMsg(MOVE_FAILED, FAILED_REASON_READ_ONLY_FILE_SYSTEM));
                return;
			}
			String FileName = fileItem.getFileName();
			String newFilePath = toFolder+FileName;
			File newFile = new File(newFilePath);
			if(newFile.exists()){
				if( currOperationType == CopyOperation.COVER){
                    if(newFile.getAbsolutePath().equals(oldFile.getAbsolutePath())){
                    	responseHandler.sendMessage(responseMsg(PASTE_PAUSE,PAUSE_REASON_CANNOT_COVER));
                    	return;
                    }else{
                    	if(newFile.isFile()){
                    		
                    	}else{
                    		deleteFolderForCut(newFile);
                    	}
                    }
                }else if(currOperationType == CopyOperation.JUMP){
             		mHandler.sendEmptyMessage(MOVE_SUCCEED);
             		return;
                }else if(currOperationType == CopyOperation.APPEND2){
                    int tmp = 2;
                    newFile = new File(toFolder + Helper.getNameAppendStr(oldFile.getName(),"(" + tmp + ")"));
                    while (newFile.exists()) {
                        tmp++;
                        newFile = new File(toFolder + Helper.getNameAppendStr(oldFile.getName(),"(" + tmp + ")"));
                    }
                }else if(currOperationType == CopyOperation.CANCEL){
                    responseHandler.sendEmptyMessage(PASTE_CANCEL);
                    return;
                }else{
                	mHandler.sendEmptyMessage(PASTE_PAUSE + PAUSE_REASON_TO_FOLDER_HAS_EXIST);
                	return;
                }
			}
			//开始移动文件
			if(oldFile.renameTo(newFile)){
				mHandler.sendEmptyMessage(MOVE_SUCCEED);
			}else{
				mHandler.sendEmptyMessage(MOVE_FAILED);
			}
		}
	}
	
	public void setToFolder(String toFolder){
        this.toFolder = toFolder;
        this.toFolderF = new File(toFolder);
        if(!toFolder.endsWith("/"))
            this.toFolder = toFolder + "/";
    }
	
	/**
     * Manager提供的粘贴文件的接口
     * @param type 出现相同文件时的处理方式
     */
    public void beginPaste(CopyOperation type){
        if(filesFor == FilesFor.COPY) {
            doPaste(type);
    	} else if(filesFor == FilesFor.CUT){
          //先判断from文件夹是否有写的权限
            FileItemForOperation fileForOperation = mSrcToDir.get(currPosition);
            FileItem fileItem = fileForOperation.getFileItem();
            File fileFrom = new File(fileItem.getFilePath());
            if(!fileFrom.canWrite()){
                responseHandler.sendMessage(responseMsg(PASTE_FAILED, 
                        FAILED_REASON_READ_ONLY_FILE_SYSTEM,fileItem.getFilePath()));
                return;
            }
            //粘贴
            doPaste(type);
        }
    }
    
    /**
     * 粘贴
     * @param type
     */
    private void doPaste(CopyOperation type){
        if(DEBUG)Log.i(TAG, "paste currPosition===========>" + currPosition);
        currOperationType = type;
        FileItemForOperation fileForOperation = mSrcToDir.get(currPosition);
        if(fileForOperation != null){
            FileItem fileItem = fileForOperation.getFileItem();
            File fromFile = new File(fileItem.getFilePath());
            //如果复制的是文件夹
            if(fromFile.isDirectory()){
                if(!addFilesToTable(fileForOperation))
                    return;
                //将第一层加入到folders中去
                folders.add(mSrcToDir.get(currPosition));
           
                mHandler.sendEmptyMessage(PASTE_SUCCEED);
                return;
            }
            PasteFileThread thread = new PasteFileThread(fileForOperation);
            thread.setOperationType(type);
            thread.start();
        }
    }
    /**
     * 将文件夹中的文件添加到Hashtable中
     * @param folder 当前操作的文件夹
     */
    private boolean addFilesToTable(FileItemForOperation folder){
        String path = folder.getFileItem().getFilePath();
        String folderName = Helper.getFolderNameOfPath(path);
        List<FileItemForOperation> files = Helper.GetData(new File(path));
        int size = files.size();
        int tableSize = mSrcToDir.size();
        
        File tmpFile = new File(folder.getDirFolder() + folderName + "/");
        //检查目标路径是否存在,不存在就建立路径
        int tmp = 1;
        while (tmpFile.exists()) {
            tmp++;
            tmpFile = new File(folder.getDirFolder() + folderName + "(" + tmp + ")");
        }
        if(toFolderF.canWrite()){
            tmpFile.mkdir();
        }
        else{
            responseHandler.sendMessage(responseMsg(PASTE_FAILED, 
                    FAILED_REASON_READ_ONLY_FILE_SYSTEM,toFolderF.getName()));
            return false;
        }
        for(int i = 0; i < size; i++){
            FileItemForOperation fileItemForOperation = files.get(i);
            fileItemForOperation.setDirFolder(tmpFile.getAbsolutePath() + "/");
            mSrcToDir.put(tableSize + i, fileItemForOperation);
        }
        return true;
    }
    
	/**
     * 在剪切文件的时候 删除原文件
     * @param fileItem
     */
    private void doDelete(final FileItem fileItem){
        new Thread(){
            public void run(){
                try{
                    if(DEBUG)Log.i(TAG, "doDelete currPosition===========>" + currPosition);
                    File theFile = new File(fileItem.getFilePath());
                    if(theFile.canWrite()){
                        if(theFile.isDirectory()){
                            deleteFolderForCut(theFile);
                        }else{
                            theFile.delete();
                        }
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }.start();
    }
    
    private boolean deleteFolderForCut(File folder){
    	File[] files = folder.listFiles();
        if(files != null){
            for(int j = 0; j < files.length && !canceled; j++){
                File aFile = files[j];
                if(aFile.isDirectory()){
                   deleteFolderForCut(aFile); 	
                }else{
                    if(aFile.canWrite())
                        aFile.delete();
                }
            }
            if(!canceled){
            	folder.delete();
            }
        }else{
            folder.delete();
        }
        return canceled;
    }
	
	private Thread getSizeThread;
    private double totalSize = 0;
    private double hasPasted = 0;
    private int totalFileNum = 0;
    private long startTime;
    private boolean getSizeError = false;
    private boolean getNumError = false;
    public void getSize(){
        totalSize = 0;
        totalFileNum = 0;
        getSizeError = false;
        startTime = System.currentTimeMillis();
        getSizeThread = new Thread(){
            public void run(){
                for(int i = 0; i < mSrcToDir.size() && !getSizeError; i++){
                    File f = new File(mSrcToDir.get(i).getFileItem().getFilePath());
                    try {
                        getDirectorySize(f);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(getSizeError)
                    mHandler.sendEmptyMessage(GETSIZE_ERROR);
                else{
                    mHandler.sendEmptyMessage(GETSIZE_COMPLETED);
                }
            }
        };
        getSizeThread.start();
    }
    private void getDirectorySize(File f) throws IOException {
        if(System.currentTimeMillis() - startTime > 10000) {
            getSizeError = true;
            return;
        }
        if(f.isFile()){
            totalSize += f.length();
            totalFileNum += 1;
            return;
        }
        totalFileNum += 1;
        File flist[] = f.listFiles();
        if (flist == null){
            return;
        }
        int length = flist.length;
        for (int i = 0; i < length && !getSizeError; i++) {
            if (flist[i].isDirectory()) {
                getDirectorySize(flist[i]);
            } else {
                totalSize = totalSize + flist[i].length();
                totalFileNum = totalFileNum + 1;
            }
        }
    }

    public FilePropertyAdapter readProp(Context context,FileItemForOperation fileItemForOperation){
    	return new FilePropertyAdapter(context, fileItemForOperation.getFileItem());
    }
    
	public void newFolder(String currFolder, String newFolder) {
		newFolder = newFolder.trim();
		if(!Helper.validateFileName(newFolder)){
			responseHandler.sendMessage(responseMsg(NEWFOLDER_FAILED,FAILED_REASON_INVALNAME));
			return;
		}
		File curr = new File(currFolder);
		if(curr.canWrite()){
			String newPath = Helper.newFolder(currFolder, newFolder);
			File folder = new File(newPath);
			if(folder.exists()){
				responseHandler.sendMessage(responseMsg(NEWFOLDER_FAILED,FAILED_REASON_FOLDER_HAS_EXIST));
				return;
			}
			if(!folder.mkdir()){
				responseHandler.sendMessage(responseMsg(NEWFOLDER_FAILED,FAILED_REASON_NO_SPACE_LEFT));
				return;
			}
            Message msg = new Message();
            msg.what = NEWFOLDER_SUCCEED;
            Bundle b = new Bundle();
            b.putString(KEY_NEW_NAME, newFolder);
            b.putString(KEY_NEW_PATH, newPath);
            msg.setData(b);
            responseHandler.sendMessage(msg);
		}else{
			responseHandler.sendMessage(responseMsg(NEWFOLDER_FAILED,FAILED_REASON_READ_ONLY_FILE_SYSTEM,currFolder));
		}
	}

	public void rename(String newName){
		newName = newName.trim();
		if(!Helper.validateFileName(newName)){
			responseHandler.sendMessage(responseMsg(RENAME_FAILED,FAILED_REASON_INVALNAME));
            return;
		}
		File file = new File(mOperationFile.getFileItem().getFilePath());
		if(file.canWrite()){
			try{
                String[] pathAndName = Helper.reName(mOperationFile.getFileItem(), newName);
                String newPath = pathAndName[0];
                File tmp = new File(newPath);
                if(tmp.exists()){
                    responseHandler.sendMessage(responseMsg(RENAME_FAILED,FAILED_REASON_FOLDER_HAS_EXIST));
                    return;
                }
                file.renameTo(new File(newPath));
                mOperationFile.getFileItem().setFileName(newName);
                mOperationFile.getFileItem().setFilePath(newPath);
                Message msg = new Message();
                msg.what = RENAME_SUCCEED;
                Bundle b = new Bundle();
                b.putString(KEY_NEW_NAME, pathAndName[1]);
                b.putString(KEY_NEW_PATH, pathAndName[0]);
                msg.setData(b);
                responseHandler.sendMessage(msg);
            }catch(Exception ex){
                responseHandler.sendMessage(responseMsg(RENAME_FAILED,FAILED_REASON_UNKOWN));
                ex.printStackTrace();
            }
		}else{
			responseHandler.sendMessage(responseMsg(RENAME_FAILED,
	                    FAILED_REASON_READ_ONLY_FILE_SYSTEM,file.getName()));
		}
	}
	
	static final int GETNUMSUCCEED = 0;
	static final int GETNUMERROR = 1;
	static final int CANCELED = 2;
	
	public int getTotalFileNum(){
		startTime = System.currentTimeMillis();
		for(int i=0;i<mFiles.size();i++){
			if(canceled){
				return CANCELED;
			}
			if(getNumError){
				return GETNUMERROR;
			}
			FileItem fileItem = mFiles.get(i).getFileItem();
            File theFile = new File(fileItem.getFilePath());
            if(theFile.isDirectory()){
                getDirectFileNum(theFile);
            }
            totalFileNum++;
		}
		if(canceled){
			return CANCELED;
		}
		if(getNumError){
			return GETNUMERROR;
		}
		return GETNUMSUCCEED;
		
	}
	public void getDirectFileNum(File file){
		if(canceled)
			return;
		if(getNumError)
			return;
		if(System.currentTimeMillis() - startTime > 10000) {
            getNumError = true;
            return;
        }
        File flist[] = file.listFiles();
        if (flist == null){
            return;
        }
        int length = flist.length;
        for (int i = 0; i < length ; i++) {
            if (flist[i].isDirectory()) {
                getDirectFileNum(flist[i]);
            }
            totalFileNum++;
        }
	}
	
	/**
     * Manager提供的删除文件的接口
     */
	public void beginDelete(){
        new Thread(){
            public void run(){
            	int flag = getTotalFileNum();
            	if(flag==CANCELED){
            		responseHandler.sendEmptyMessage(DELETE_CANCEL);
            		return;
            	}else if(flag==GETNUMERROR){	
            		responseHandler.sendEmptyMessage(GETTOTALNUM_ERROR);
            		return;
            	}
            	responseHandler.sendMessage(responseMsg(FileOperationThreadManager.GETTOTALNUM_COMPLETED,totalFileNum));
            	
            	delCount = 0;
                try{
                    for(int i = 0; i < mFiles.size() && !canceled; i++){
                        FileItem fileItem = mFiles.get(i).getFileItem();
                        File theFile = new File(fileItem.getFilePath());
                        if(theFile.canWrite()){
                            if(theFile.isDirectory()){
                                if(deleteFolder(theFile)){
                                	if(rdOnlyFile != null){
                                		responseHandler.sendMessage(responseMsg(FileOperationThreadManager.DELETE_FAILED, FAILED_REASON_READ_ONLY_FILE_SYSTEM,rdOnlyFile.getAbsolutePath()));
                                		return;
                                	}
                                }
                            }else{
                            	theFile.delete();
                                delCount++;
                                int rate = (int)(delCount * 1.0 / totalFileNum * 100.0);
                                Message msg = new Message();
                                msg.what = DELETE_PROGRESS_CHANGE;
                                msg.arg1 = rate;
                                msg.arg2 = delCount;
                                responseHandler.sendMessage(msg);  
                            }
                        }else{
                            Message msg = responseMsg(DELETE_FAILED,
                                    FAILED_REASON_READ_ONLY_FILE_SYSTEM,fileItem.getFileName());
                            responseHandler.sendMessage(msg);
                            break;
                        }
                    }
                    if(delCount == totalFileNum){
                        responseHandler.sendEmptyMessage(DELETE_COMPLETED);
                    }else if(canceled){
                        responseHandler.sendEmptyMessage(DELETE_CANCEL);
                    }
                }catch(Exception ex){
                    responseHandler.sendEmptyMessage(DELETE_FAILED);
                    ex.printStackTrace();
                }
            }
        }.start();
    }
 

    /**
     * 递归删除文件夹及其中的文件
     * @param folder
     */
    private boolean deleteFolder(File folder){
        File[] files = folder.listFiles();
        if(files != null){
            for(int j = 0; j < files.length && !canceled; j++){
                File aFile = files[j];
                if(aFile.isDirectory()){
                	/*if(!deleteFolder(aFile)){
                		return false;
                	}*/
                	deleteFolder(aFile);
                }else{
                    if(aFile.canWrite()){
                        /*if(!aFile.delete()){
                        	return false;
                        }*/
                    	aFile.delete();
	                    delCount++;
	                    int rate = (int)(delCount * 1.0 / totalFileNum * 100.0);
	                    Message msg = new Message();
	                    msg.what = DELETE_PROGRESS_CHANGE;
	                    msg.arg1 = rate;
	                    msg.arg2 = delCount;
	                    responseHandler.sendMessage(msg);
                    }else{
                    	rdOnlyFile = aFile;
                    	canceled = true;
                    }
                }
            }
            if(!canceled){
            	folder.delete();
            	delCount++;
                int rate = (int)(delCount * 1.0 / totalFileNum * 100.0);
                Message msg = new Message();
                msg.what = DELETE_PROGRESS_CHANGE;
                msg.arg1 = rate;
                msg.arg2 = delCount;
                responseHandler.sendMessage(msg);
            }
        }else{
            folder.delete();
            delCount++;
            int rate = (int)(delCount * 1.0 / totalFileNum * 100.0);
            Message msg = new Message();
            msg.what = DELETE_PROGRESS_CHANGE;
            msg.arg1 = rate;
            msg.arg2 = delCount;
            responseHandler.sendMessage(msg);
        }
        return canceled;
    }
    
    
	
	public Message responseMsg(int what,int arg1){
		Message msg = Message.obtain();
		msg.what = what;
		msg.arg1 = arg1;
		return msg;
	}
	 private Message responseMsg(int what,int arg1,String data){
	        Message msg = new Message();
	        msg.what = what;
	        msg.arg1 = arg1;
	        Bundle b = new Bundle();
	        b.putString(KEY_CURR_NAME, data);
	        msg.setData(b);
	        return msg;
	 }
	 
	 private Message responseMsg(int what,int arg1,int arg2){
	    	Message msg = new Message();
	    	msg.what = what;
	    	msg.arg1 = 100*arg1/arg2;
	    	Bundle bundle = new Bundle();
	    	String currPos = arg1+"/"+arg2;
	    	String percentage = (100*arg1/arg2)+"%";
	    	bundle.putString("currPos", currPos);
	    	bundle.putString("percentage", percentage);
	    	msg.obj = bundle;
	    	return msg;
	    }
	 
	 private Message getProgressChangeMsg(){
	//	System.out.println(pasted_rate);
         Message msg = new Message();
         msg.what = PASTE_PROGRESS_CHANGE;
         msg.arg1 = pasted_rate;
         Bundle bundle = new Bundle();
	    	String currPos = currPosition+"/"+totalFileNum;
	    	String percentage = pasted_rate+"%";
	    
	    	bundle.putString("currPos", currPos);
	    	bundle.putString("percentage", percentage);
	    	msg.obj = bundle;
	    	return msg;
     }
	 
	 class PasteFileThread extends Thread{
        
        private CopyOperation operationType = CopyOperation.UNKOWN;
        FileItemForOperation mFileForOperation;
        public PasteFileThread(FileItemForOperation fileForOperation){
            this.mFileForOperation = fileForOperation;
        }
        @Override
        public void run(){
            int res = 0;
            res = pasteFile(mFileForOperation);
            mHandler.sendEmptyMessage(res);
        }
        public void setOperationType(CopyOperation operationType) {
            this.operationType = operationType;
        }
        public CopyOperation getOperationType() {
            return operationType;
        }
        
        private int pasteFile(FileItemForOperation fileForOperation){
            if(fileForOperation == null){
                responseHandler.sendEmptyMessage(PASTE_PROGRESS_CHANGE);
                return PASTE_SUCCEED;
            }
            String toFolder = fileForOperation.getDirFolder();
            FileItem fileItem = fileForOperation.getFileItem();
            File fromFile = new File(fileItem.getFilePath());
            int copyResult = PASTE_FAILED;
            //检查被复制的文件是否存在
            if(!fromFile.exists()){
                return PASTE_FAILED + FAILED_REASON_FROM_FILE_NOT_EXIST;
            }
            //检查被复制的文件是否被限制无法拷贝出
            if(!canPasteFrom(fromFile)){
                return PASTE_FAILED + FAILED_REASON_FOLDER_LIMIT;
            }
            File tmpFile = new File(toFolder);
            //检查目标路径是否存在,不存在就建立路径
            if(!tmpFile.exists())
          
                if(!tmpFile.mkdir()){
                	//空间不够，经历路径失败
                	return PASTE_FAILED + FAILED_REASON_NO_SPACE_LEFT;
                }
            //检查目标文件夹是否可写
            if(!tmpFile.canWrite()){
                return PASTE_FAILED + FAILED_REASON_READ_ONLY_FILE_SYSTEM;
            }
            tmpFile = new File(toFolder + fromFile.getName());
            double fileSize = fromFile.length();
            //检查粘贴的目录是否已经有同名的文件
            if(tmpFile.exists()){
          //  	Log.i(TAG,"=============>"+"存在同名的");
                if(operationType == CopyOperation.COVER){
                    if(tmpFile.getAbsolutePath().equals(fromFile.getAbsolutePath())){
                        return PASTE_PAUSE + PAUSE_REASON_CANNOT_COVER;
                    }else{
                    	System.out.println(tmpFile.delete());
                        Log.e(TAG,"===========temp.exist" + tmpFile.exists());
                    }	
                }
                else if(operationType == CopyOperation.JUMP){
                    //responseHandler.sendEmptyMessage(PASTE_PROGRESS_CHANGE);
                    pasted_rate = (int)((hasPasted + fileSize) / totalSize * 100.0);
                    responseHandler.sendMessage(getProgressChangeMsg());
                    return PASTE_SUCCEED;
                }
                else if(operationType == CopyOperation.APPEND2){
                    int tmp = 2;
                    tmpFile = new File(toFolder + Helper.getNameAppendStr(fromFile.getName(),"(" + tmp + ")"));
                    while (tmpFile.exists()) {
                        tmp++;
                        tmpFile = new File(toFolder + Helper.getNameAppendStr(fromFile.getName(),"(" + tmp + ")"));
                    }
                }
                else if(operationType == CopyOperation.CANCEL){
                    return PASTE_CANCEL;
                }
                else{
                    return PASTE_PAUSE + PAUSE_REASON_TO_FOLDER_HAS_EXIST;
                }
            }
            //copyFileLinuxCommand(fileItem.getFilePath(), toFolder);
            copyResult = copyFileJavaCommand(fromFile, tmpFile);
            return copyResult;
        }
        
        
        private int copyFileJavaCommand(File srcFile,File dirFile){
        //	Log.i(TAG,"===============>"+"开始复制");
            int res = 0;
            int byteread = 0;
            try{
        //    	Log.i(TAG, "dir file==========>" + dirFile.getParent());
                StatFs stat = null;
                if(dirFile.getAbsolutePath().startsWith("/mnt/sdcard")){
                	stat = new StatFs("/mnt/sdcard");
                } else if(dirFile.getAbsolutePath().startsWith("/mnt/ext_sd")){
                    stat = new StatFs("/mnt/ext_sd");
                } else{
                	stat = new StatFs("/mnt/innerDisk");
                }
                long left = (((long)stat.getAvailableBlocks())*stat.getBlockSize());
                Log.i(TAG, "left space============>" + left);
                if(srcFile.length() > left){
                    return PASTE_FAILED + FAILED_REASON_NO_SPACE_LEFT;
                }
                InputStream inStream = new FileInputStream(srcFile); // 读入原文件
                FileOutputStream outStream = new FileOutputStream(dirFile);
                byte[] buffer = new byte[1024 * 10];
                int tmpRate = 0;
                while ((byteread = inStream.read(buffer)) != -1 && !canceled) {
                    hasPasted += byteread; // 字节数 文件大小
                    outStream.write(buffer, 0, byteread);
                    pasted_rate = (int)(hasPasted / totalSize * 100.0);
                    if(pasted_rate >= tmpRate + 1){
                    	responseHandler.sendMessage(getProgressChangeMsg());
                        tmpRate = pasted_rate;
                    }
                }
                inStream.close();
                if(canceled){
                    res = PASTE_CANCEL;
                    dirFile.delete();
                    responseHandler.sendEmptyMessage(PASTE_CANCEL);
                }else{
                    res = PASTE_SUCCEED;
                 //	responseHandler.sendMessage(getProgressChangeMsg(0,1));
                }
            }catch(Exception ex){
            	if(DEBUG) Log.i(TAG,"EXCEPTION===>"+ex.getMessage());
                res = PASTE_FAILED;
                canceled = true;
                ex.printStackTrace();
            }
            operationType = CopyOperation.UNKOWN;
            return res;
        }
        
        private boolean canPasteFrom(File fromFile){
//            String fromPath = fromFile.getAbsolutePath();
//            String[] limitFolders = MyApplication.limitFolders;
//            if(limitFolders.length == 0)return true;
//            for (String string : limitFolders) {
//                if(fromPath.startsWith(string)){
//                    return false;
//                }
//            }
            return true;
        }
    } 
}
