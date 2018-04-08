package org.music.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;

public class FileUtil {

    /**
     * 得到当前外部存储设备的目录
     */
    private static String SDCardRoot = Environment
            .getExternalStorageDirectory().getAbsolutePath() + File.separator;

    /**
     * 在SD卡上创建文件
     * 
     * @throws IOException
     */
    public static File createFileInSDCard(String fileName, String dir)
            throws IOException {
        if (isFileExist(fileName, dir)) {
            deleteFile(dir, fileName);
        }
        File file = new File(SDCardRoot + dir + File.separator + fileName);
        file.createNewFile();
        return file;
    }

    /**
     * 在SD卡上创建目录
     * 
     * @param dirName
     */
    public static File creatSDDir(String dir) {
        File dirFile = new File(SDCardRoot + dir + File.separator);
        dirFile.mkdirs();
        return dirFile;
    }

    /**
     * 删除SD卡上的文件
     * 
     * @param dir
     *            文件夹目录(已包含根目录)
     * @param fileName
     *            文件名
     * @return
     * @throws IOException
     */
    public static boolean deleteFile(String dir, String fileName)
            throws IOException {
        File dirFile = new File(SDCardRoot + dir + File.separator + fileName);
        return dirFile.delete();
    }

    /**
     * 删除_data路径的文件
     * 
     * @param _data
     * @return
     */
    public static boolean deleteAnotherFile(String _data) throws IOException {
        File dirFile = new File(_data);
        return dirFile.delete();
    }

    /**
     * 判断SD卡上的文件是否存在
     */
    public static boolean isFileExist(String fileName, String path) {
        File file = new File(SDCardRoot + path + File.separator + fileName);
        return file.exists();
    }

    public static boolean _isFileExist(String fileName) {
        File file = new File(SDCardRoot + "mp3" + File.separator + fileName);
        return file.exists();
    }
    
    /**
     * 
     * @param path 路径加文件名加后缀
     * @return
     */
    public static boolean _isFileExist2(String path){
        File file = new File(path);
        return file.exists();
    }

    /**
     * 判断SD卡上的文件夹是否存在
     */
    public static boolean isFilePathExist(String path) {
        File file = new File(SDCardRoot + path);
        return file.exists();
    }

    public static String getFilePath(String fileName) {
        return SDCardRoot + "mp3" + File.separator + fileName;
    }

    public static void updateFileName(File file, String finalFileName) {
        String path = file.getAbsolutePath();
        String orgFileName = path.substring(0, path.lastIndexOf("/") + 1)
                + finalFileName;
        file.renameTo(new File(orgFileName));
    }

    /**
     * 将一个InputStream里面的数据写入到SD卡中
     */
    public static File write2SDFromInput(String path, String fileName,
            InputStream inPutStream) {
        File file = null;
        OutputStream outPutStream = null;
        try {
            creatSDDir(path);
            file = createFileInSDCard(fileName, path);
            outPutStream = new FileOutputStream(file);
            byte buffer[] = new byte[64];
            int temp;
            while ((temp = inPutStream.read(buffer)) != -1) {
                outPutStream.write(buffer, 0, temp);
            }
            outPutStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outPutStream != null) {
                    outPutStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 读取目录中的MP3文件的名字和大小
     */
    public static List<Mp3Info> getMp3Files(String path) {
        ArrayList<Mp3Info> mp3Infos = new ArrayList<Mp3Info>();
        File file = new File(SDCardRoot + File.separator + path);
        File[] files = file.listFiles();
        Mp3Info mp3Info = null;
        int length = files.length;
        for (int i = 0; i < length; i++) {
            if (files[i].getName().endsWith("mp3")) {
                mp3Info = new Mp3Info();
                mp3Info.setMp3Name(files[i].getName());
                mp3Info.setMp3Size(String.valueOf(files[i].length()));
                String[] temp = mp3Info.getMp3Name().split("\\.");
                String lrcName = temp[0] + ".lrc";
                mp3Info.setLrcName(lrcName);
                mp3Infos.add(mp3Info);
            }
        }
        return mp3Infos;
    }

}
