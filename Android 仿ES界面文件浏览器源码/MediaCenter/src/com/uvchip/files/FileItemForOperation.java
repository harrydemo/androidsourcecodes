package com.uvchip.files;



public class FileItemForOperation{
    public static final int SELECT_STATE_CUT = -1;
    public static final int SELECT_STATE_NOR = 0;
    public static final int SELECT_STATE_SEL = 1;

    private FileItem fileItem;

    private String dirFolder;

    private int selectState;

    private FilePropertyAdapter proAdapter;
    
    
    public FileItemForOperation(){}
    
    public FileItem getFileItem(){
        return fileItem;
    }
    public void setFileItem(FileItem fileItem){
        this.fileItem = fileItem;
    }
    
    public void setDirFolder(String dirFolder) {
        this.dirFolder = dirFolder;
    }
    public String getDirFolder() {
        return dirFolder;
    }
    public void setSelectState(int selectState) {
        this.selectState = selectState;
    }
    public int getSelectState() {
        return selectState;
    }

    public void setPropAdapter(FilePropertyAdapter proAdapter) {
        this.proAdapter = proAdapter;
    }

    public FilePropertyAdapter getPropAdapter() {
        return proAdapter;
    }
    
    
}
