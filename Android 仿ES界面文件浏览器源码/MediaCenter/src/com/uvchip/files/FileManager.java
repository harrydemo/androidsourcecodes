package com.uvchip.files;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

public class FileManager {
	public static final String HOME = "/mnt/";
    public static final String USB = HOME + "usbDisk";
    public static final String SD = HOME + "sdcard/";
    public static final String EXTSD = HOME + "ext_sd/";
    public static final String MEMORY = HOME + "innerDisk/";
    
	public enum FileFilter {MUSIC,VIDEO,PICTURE,ALL};
	private FileItemSet mData;
	private FileItemSet mDataForOperation;
	public enum FilesFor {COPY,CUT,DELETE,UNKOWN};
    private FilesFor mFilesFor = FilesFor.UNKOWN;
	
	private RefreshData queryThread;
	public static Comparator<File> mComparator; 
	public FileComparatorByName comp_name;
	public FileComparatorBySize comp_size;
	public FileComparatorByUpdateTime comp_update;

    public enum ViewMode {
        LISTVIEW, GRIDVIEW
    };



    private Context mContext;
    private String sdcardState = "";
    
    private OnFileSetUpdated mOnFileSetUpdated;
    private OnWhichOperation mOnWhichOperation;
    
    public static final boolean IS_SIMSDCARD = getSystemPro();

    static boolean getSystemPro() {
        String value = "";
        try {
            Class<?> classType = Class.forName("android.os.SystemProperties");
            Method getMethod = classType.getDeclaredMethod("get", new Class<?>[] {
                String.class
            });
            value = (String) getMethod.invoke(classType, new Object[] {
                "ro.uc13x.sim_sdcard"
            });

        } catch (Exception e) {
            e.printStackTrace();

        }
        return "simsdcard".equals(value);
    }
	

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RefreshData.FINISHED:
                    if (mOnFileSetUpdated != null) {
						mOnFileSetUpdated.queryFinished();
					}
                    break;
                case RefreshData.QUERY_MATCH:
                    // System.out.println(System.currentTimeMillis());
                    List<FileItemForOperation> items = new ArrayList<FileItemForOperation>();
                    items.addAll(queryThread.getItems());
                    // System.out.println(m_items.size());
                    // System.out.println("===================");
                    mData.setFileItems(items);
                    if (mOnFileSetUpdated != null) {
						mOnFileSetUpdated.queryMatched();
					}
                    return;
                case RefreshData.LOAD_APK_ICON_FINISHED:

                    return;
                default:
                    break;
            }
        }
    };
    
	public FileManager(Context context,FileItemSet data){
		mContext = context;
		initComparator();
		setSdcardState(Environment.getExternalStorageState());
		
        mData = data;
        mDataForOperation = new FileItemSet();
	}
	public void query(String path,FileFilter fileFilter){
        if(queryThread != null)
            queryThread.stopGetData();
        queryThread = new RefreshData(mContext,mHandler);
        queryThread.setFolder(new File(path));   
        queryThread.queryData(fileFilter);
	}
	public void setSdcardState(String sdcardState) {
        this.sdcardState = sdcardState;
    }
    public String getSdcardState() {
        return sdcardState;
    }
	private void initComparator() {
        comp_name = new FileComparatorByName();
        comp_size = new FileComparatorBySize();
        comp_update = new FileComparatorByUpdateTime();
        mComparator = comp_name;
    }
	public FileItemSet getDataForOperation(){
        return this.mDataForOperation;
    }
    public void resetDataForOperation(){
        mDataForOperation.clear();
        mFilesFor = FilesFor.UNKOWN;
        mOnWhichOperation.whichOperation(mFilesFor,0);
    }
    public void addFileItem(FileItemForOperation fileItem){
        if(!mDataForOperation.contains(fileItem)){
        	mDataForOperation.Add(fileItem);
        }
    }
    public void removeFileItem(FileItemForOperation fileItem){
        if(mDataForOperation.contains(fileItem)){
        	mDataForOperation.remove(fileItem);
        }
    }

    public void setFilesFor(FilesFor filesFor) {
        mFilesFor = filesFor;
        if(filesFor == FilesFor.COPY || filesFor == FilesFor.CUT){
            mOnWhichOperation.whichOperation(filesFor,this.mDataForOperation.size());
        }
    }
    public FilesFor getFilesFor() {
        return mFilesFor;
    }

    public boolean canOperation(){
        if(mDataForOperation.size() > 0 && mFilesFor != FilesFor.UNKOWN)
            return true;
        return false;
    }
    public void setOnWhichoperation(OnWhichOperation iWhichoperation) {
        mOnWhichOperation = iWhichoperation;
    }
    public OnWhichOperation getWhichoperation() {
        return mOnWhichOperation;
    }
    public interface OnWhichOperation{
        public void whichOperation(FilesFor filesFor,int size);
    }
	
	public void setOnFileSetUpdated(OnFileSetUpdated fileSetUpdated){
		mOnFileSetUpdated = fileSetUpdated;
	}
    
    public interface OnFileSetUpdated{
    	public void queryFinished();
    	public void queryMatched();
    }
    
}
