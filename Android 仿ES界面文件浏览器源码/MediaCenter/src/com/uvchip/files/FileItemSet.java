package com.uvchip.files;

import java.util.ArrayList;
import java.util.List;

public class FileItemSet {
    private List<FileItemForOperation> fileItems;
    public FileItemSet(){
        setFileItems(new ArrayList<FileItemForOperation>());
    }
    public void setFileItems(List<FileItemForOperation> fileItems) {
        this.fileItems = fileItems;
    }
    public List<FileItemForOperation> getFileItems() {
        return fileItems;
    }
    public void Add(FileItemForOperation fileItem){
        this.fileItems.add(fileItem);
    }
    public void remove(FileItemForOperation fileItem){
        this.fileItems.remove(fileItem);
    }
    public void clear(){
        this.fileItems.clear();
    }
    public void insertAt(int location,FileItemForOperation fileItem){
        this.fileItems.add(location, fileItem);
    }
    public boolean contains(FileItemForOperation fileItem){
        for (FileItemForOperation item : fileItems) {
            if(item.getFileItem().getFilePath().equals(fileItem.getFileItem().getFilePath()))
                return true;
        }
        return false;
    }
    public int size(){
        return this.fileItems.size();
    }
    
    
    public interface IFileItemSet{
        public void addFileItem(FileItemForOperation fileItem);
    }
}
