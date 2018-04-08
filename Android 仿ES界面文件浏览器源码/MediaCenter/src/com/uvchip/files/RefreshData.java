package com.uvchip.files;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.os.Handler;

import com.uvchip.files.FileManager.FileFilter;
import com.uvchip.mediacenter.MApplication;
import com.uvchip.mediacenter.PreparedResource;


public class RefreshData {
    static final int FINISHED = 1;
    static final int QUERY_MATCH = 2;
    static final int QUERY_MATCH_FOR_FOLDER = 3;
    public static final int LOAD_APK_ICON_FINISHED = 4;
    Handler responsHandler;
    private boolean isFolder;
    File folder;
    Context mContext;
    FileFilter mFileFilter;
    private List<FileItemForOperation> mItems;
    private PreparedResource mPreResource;
    public static HashMap<String, String> usbIndex = new HashMap<String, String>();

    public RefreshData(Context context, Handler handler) {
        mContext = context;
        responsHandler = handler;
        mPreResource = MApplication.getInstance().getPreparedResource();
    }


    public void setFolder(File folder) {
        this.folder = folder;
    }

    RefreshDataThread thread;

    public void queryData(FileFilter filter) {
    	mFileFilter = filter;
        mItems = new ArrayList<FileItemForOperation>();
        thread = new RefreshDataThread();
        thread.setShouldStop(false);
        thread.start();
    }

    public List<FileItemForOperation> getItems() {
        return mItems;
    }

    public void stopGetData() {
        thread.setShouldStop(true);
    }

    class RefreshDataThread extends Thread {
        private boolean shouldStop;

        private List<File> mediaFiles = new ArrayList<File>();
        public void setShouldStop(boolean b) {
            shouldStop = b;
        }
        
        private void fetchMediaFiles(File folder){
        	if (folder.getAbsolutePath().split("/").length >= 6) {
                return;
            }
        	File[] files = null;
        	switch (mFileFilter) {
            case MUSIC:
                files = folder.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        File file = new File(dir + "/" + filename);
                        if (file.isDirectory()) {
                            return true;
                        }
                        return mPreResource.isAudioFile(filename.substring(
                                filename.lastIndexOf(".") + 1).toLowerCase());
                    }
                });
                break;
            case VIDEO:
                files = folder.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        File file = new File(dir + "/" + filename);
                        if (file.isDirectory()) {
                            return true;
                        }
                        return mPreResource.isVideoFile(filename.substring(
                                filename.lastIndexOf(".") + 1).toLowerCase());
                    }
                });
                break;
            case PICTURE:
                files = folder.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        File file = new File(dir + "/" + filename);
                        if (file.isDirectory()) {
                            return true;
                        }
                        return mPreResource.isImageFile(filename.substring(
                                filename.lastIndexOf(".") + 1).toLowerCase());
                    }
                });
                break;
        	}
        	if (files != null) {
	        	for (File file : files) {
					if (file.isFile()) {
						mediaFiles.add(file);
					}else {
						fetchMediaFiles(file);
					}
				}
        	}
        }
        public void run() {
            if (folder.exists()) {
                if (folder.isFile()) {
                    responsHandler.sendEmptyMessage(FINISHED);
                } else if (folder.isDirectory()) {
                    File[] files = null;
                    if (mFileFilter == FileFilter.ALL) {
                    	files = folder.listFiles();
					}else {
						fetchMediaFiles(folder);
						files = mediaFiles.toArray(new File[mediaFiles.size()]);
					}

                    if (files != null) {
                        Arrays.sort(files, FileManager.mComparator);
                        // QuickSorter.sort(files, com);
                        int i;
                        for (i = 0; i < files.length && !shouldStop; i++) {
                            FileItem fileOrfolder = new FileItem();
                            String fileName = files[i].getName();

                            String extraName = fileName.substring(fileName.lastIndexOf(".") + 1);
                            extraName = extraName.toLowerCase();
                            long size = files[i].length();
                            String path = files[i].getAbsolutePath();
                            boolean isDir = files[i].isDirectory();
                            if (isDir) {
                                extraName = "folder";
                                size = -1;
                                path += "/";
                            }
                            fileOrfolder.setDirectory(isDir);
                            fileOrfolder.setFileName(fileName);
                            fileOrfolder.setExtraName(extraName);
                            fileOrfolder.setFilePath(path);
                            fileOrfolder.setFileSize(size);
                            fileOrfolder.setCanRead(files[i].canRead());
                            fileOrfolder.setCanWrite(files[i].canWrite());
                            fileOrfolder.setHide(files[i].isHidden());
                            fileOrfolder.setIconId(mPreResource.getBitMap(extraName));
                            
                            FilePropertyAdapter propAdapter = new FilePropertyAdapter(mContext,fileOrfolder);
                            FileItemForOperation fileItem = new FileItemForOperation();
                            fileItem.setPropAdapter(propAdapter);
                            fileItem.setFileItem(fileOrfolder);
                            RefreshData.this.addFileItem(fileItem);
                            // 每搜索40个文件，刷新一下屏幕
                            if ((i + 1) % 20 == 0) {
                                if (isFolder) {
                                    responsHandler.sendEmptyMessage(QUERY_MATCH_FOR_FOLDER);
                                } else {
                                    responsHandler.sendEmptyMessage(QUERY_MATCH);
                                }
                            }
                            /*
                             * else{ //搜索整个文件夹完毕，刷新屏幕 if(i + 1 == files.length){
                             * if(isFolder){ responsHandler.sendEmptyMessage(
                             * QUERY_MATCH_FOR_FOLDER); }else{
                             * responsHandler.sendEmptyMessage(QUERY_MATCH); } }
                             * }
                             */
                        }

                        if (isFolder) {
                            responsHandler.sendEmptyMessage(QUERY_MATCH_FOR_FOLDER);
                        } else {
                            responsHandler.sendEmptyMessage(QUERY_MATCH);
                        }
                        responsHandler.sendEmptyMessage(FINISHED);
                    } else {

                        if (isFolder) {
                            responsHandler.sendEmptyMessage(QUERY_MATCH_FOR_FOLDER);
                        } else {
                            responsHandler.sendEmptyMessage(QUERY_MATCH);
                        }
                        responsHandler.sendEmptyMessage(FINISHED);
                    }
                }
            } else {
                responsHandler.sendEmptyMessage(QUERY_MATCH);
                responsHandler.sendEmptyMessage(FINISHED);
            }
        }
    }

    public void addFileItem(FileItemForOperation fileItem) {
        mItems.add(fileItem);
    }
    
}
