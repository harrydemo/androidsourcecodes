package cn.com.mythos.android.Contents;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Log;

public class Utils {
	public static String _sdCardPath = getSDCardPath();
	public static String _rootTempPath = _sdCardPath + Contents.TEMPPATH;
	private final static String TAG = "Utils";
	public static int bmpWidth = 0;
	public static int bmpHeight = 0;
	public static double zoomBigscale;
	public static double zoomSmallscale;
	public static float scaleWidth = 1;
	public static float scaleHeight = 1;
	public static float scale = 1;
	public static int ScaleAngle = 0;
	public static int ScaleTimes = 0;
	public static String _bookMarks = Contents.BOOKMARKS;
	
	
	//判断SD卡是否存在,如果存在则继续加载内容,同时创建文件夹cartoonReader/temp/
	public static boolean createFile() {
		if(_sdCardPath != null) {
			String filePath = _rootTempPath;
			File file = new File(filePath);
			if(!file.exists()) {
				file.mkdirs();
			}
			file = null;
			return true;
		}
		return false;
	}
	
	//按照书签名称 删除指定书签
	public static void removeBookMarkByName(String bookMarkName) {
		String bookMark = "";
		String[] array = getFileRead(Contents.BOOKMARKS).split(";");
		if(array != null && array.length > 0) {
			for(int i = 0;i < array.length;i++) {
				if(array[i].indexOf(bookMarkName + "|") != -1) {
					array[i] = "";
				}
				if(!"".equals(array[i]) && array[i].length() > 0) {
					bookMark += array[i] + ";" + "\r\n";
				}
			}
			if(!"".equals(bookMark)) {
				bookMark = bookMark.substring(0, bookMark.length() - 1);
			}
			if(_sdCardPath != null) {
				String filePath = _rootTempPath + _bookMarks;
				File file = new File(filePath);
				if(file.exists()) {
					file.delete();
					file = null;
				}
				saveFile(_bookMarks, bookMark, true);
			}
		}
	}
	
	//删除全部书签
	public static void removeAllBookMarks() {
		if(_sdCardPath != null) {
			String filePath = _rootTempPath + _bookMarks;
			File file = new File(filePath);
			if(file.exists()) {
				file.delete();
				file = null;
			}
		}
	}
	
	//旋转图片的方法
	public static Bitmap imageRotate(String direction, int scaleAngle, String picPath) {
		Bitmap newBitmap = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inSampleSize = 2;
		Bitmap bitmap = BitmapFactory.decodeFile(picPath, opts);
		int widthOrig = bitmap.getWidth();
		int heightOrig = bitmap.getHeight();
		int newWidth = widthOrig * ScaleTimes;
		int newHeight = heightOrig * ScaleTimes;
		scaleWidth = ((float) newWidth) / widthOrig;
		scaleHeight = ((float) newHeight) / heightOrig;
		if(direction.equals("left")) {
			ScaleAngle--;
			if(ScaleAngle <= -45) {
				ScaleAngle = 45;
			}
		}
		if(direction.equals("right")) {
			ScaleAngle++;
			if(ScaleAngle >= 45) {
				ScaleAngle = 45;
			}
		}
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		matrix.postRotate(scaleAngle * ScaleAngle);
		newBitmap = Bitmap.createBitmap(bitmap, 0, 0, widthOrig, heightOrig, matrix, true);
		return newBitmap;
	}
	
	
	//缩小图片的方法
	public static Bitmap imageZoomSmall(String picPath, int disWidth, int disHeight) {
		Bitmap newBitmap = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inSampleSize = 2;
		Bitmap bitmap = BitmapFactory.decodeFile(picPath, opts);
		int widthOrig = bitmap.getWidth();
		int heightOrig = bitmap.getHeight();
		if (bmpWidth == 0 || bmpHeight == 0) {
			bmpWidth = bitmap.getWidth();
			bmpHeight = bitmap.getHeight();
		}
		if (bmpWidth >= widthOrig / 2 && bmpHeight >= heightOrig / 2) {
			zoomSmallscale = 0.8;
			scaleWidth = (float) zoomSmallscale * scaleWidth;
			scaleHeight = (float) zoomSmallscale * scaleHeight;
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			newBitmap = Bitmap.createBitmap(bitmap, 0, 0, widthOrig, heightOrig, matrix, true);
			bmpWidth=newBitmap.getWidth();
			bmpHeight=newBitmap.getHeight();
		}
		return newBitmap;
	}
	
	//放大图片的方法
	public static Bitmap imageZoomBig (String picPath, int disWidth, int disHeight) {
		Bitmap newBitmap = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inSampleSize = 2;
		Bitmap bitmap = BitmapFactory.decodeFile(picPath, opts);
		int widthOrig = bitmap.getWidth();
		int heightOrig = bitmap.getHeight();
		if (bmpWidth == 0 || bmpHeight == 0) {
			bmpWidth = bitmap.getWidth();
			bmpHeight = bitmap.getHeight();
		}
		if (bmpWidth <= 2 * disWidth && bmpHeight <= disHeight) {
			zoomBigscale = 1.25;
			scaleWidth = (float) zoomBigscale * scaleWidth;
			scaleHeight = (float) zoomBigscale * scaleHeight;
			scale = scaleHeight;
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			newBitmap = Bitmap.createBitmap(bitmap, 0, 0, widthOrig, heightOrig, matrix, true);
			bmpWidth=newBitmap.getWidth();
			bmpHeight=newBitmap.getHeight();
		}
		return newBitmap;
	}
	
	//重置缩放参数
	public static void resetBitmap() {
		zoomBigscale = 1.25;
		zoomSmallscale = 0.8;
		bmpWidth = 0;
		bmpHeight = 0;
		ScaleAngle = 0;
		ScaleTimes = 1;
		scaleWidth = 1;
		scaleHeight = 1;
	}
	
	//获取SDcard的方法
	public static String getSDCardPath() {
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File sdCard = Environment.getExternalStorageDirectory();
			String path = sdCard.getPath();
			return path;
		}
		return null;
	}
	
	//获取文件并按文件名称排序
	public static File[] getPicOrder(String path){
		File[] files = new File(path).listFiles();
		File temp;
		for(int i = 0; i < files.length;i++) {
			for(int j = 0;j < files.length - i -1;j++) {
				if(files[j].getName().compareTo(files[j + 1].getName()) > 0) {
					temp = files[j];
					files[j] = files[j + 1];
					files[j + 1] = temp;
				}
			}
		}
		return files;
	}
	
	//判断文件的合法性，即文件类型和文件大小是否满足要求
	public static boolean getFileExt(String fileName) {
		if(fileName != null && !"".equals(fileName)) {
			String fileExt = fileName.substring(fileName.indexOf(".") + 1, fileName.length());
			if("jpg".equalsIgnoreCase(fileExt) || "jpeg".equalsIgnoreCase(fileExt) || "png".equalsIgnoreCase(fileExt) || "gif".equalsIgnoreCase(fileExt) || "bmp".equals(fileExt)) {
				if(getFileSize(fileName) <= Contents.FILESIZE) {
					return true;
				}
			}
		}
		return false;
	}
	
	//获得文件大小的方法
	public static double getFileSize(String filePath) {
		double size = 0.0;
		File file = new File(filePath);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			size = fis.available()/1024;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return size;
	}
	
	//将返回的TXT保存到SDCard,其中：fileName=文件名,fileContent=文件内容,flag=是否在原文件内容后进行追加
	public static String saveFile(String fileName, String fileContent, boolean flag) {
		String filePath = "";
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		try {
			if(_sdCardPath != null) {
				filePath = _rootTempPath;
				try {
					filePath += fileName;
					File file = new File(filePath);
					if(!file.exists()) {
						if(flag) {
							fos = new FileOutputStream(file, true);
						}else {
							fos = new FileOutputStream(file);
						}
						osw = new OutputStreamWriter(fos);
						bw = new BufferedWriter(osw);
						bw.write(fileContent);
						bw.newLine();
						bw.flush();
					}else {
						if(flag) {
							fos = new FileOutputStream(file,true);
							osw = new OutputStreamWriter(fos);
							bw = new BufferedWriter(osw);
							bw.write("\r\n" + fileContent);
						}else {
							fos = new FileOutputStream(file);
							osw = new OutputStreamWriter(fos);
							bw = new BufferedWriter(osw);
							bw.write(fileContent);
						}
						bw.newLine();
						bw.flush();
					}
				}catch (Exception e) {
					Log.i(TAG, e.getMessage());
				}finally {
					if (bw != null) {
						bw.close();
					}
					if (osw != null) {
						osw.close();
					}
					if(fos != null) {
						fos.close();
					}
				}
			}
		} catch (Exception e) {
			Log.i(TAG, e.getMessage());
		}
		return filePath;
	}
	
	//读取txt中保存的信息
	public static String getFileRead(String filePath) {
		String content = "";
		String path = _rootTempPath;
		String line = "";
		File file = null;
		FileReader fr = null;
		BufferedReader bfr = null;

		if(_sdCardPath != null) {
			try {
				path += filePath;
				file = new File(path);
				if(file.exists()) {
					fr = new FileReader(file);
					bfr = new BufferedReader(fr);
					while((line = bfr.readLine()) != null) {
						content += line.trim();
					}
				}
			}catch (Exception e) {
				Log.i(TAG, e.getMessage());
			}finally {
				try {
					if (bfr != null) {
						bfr.close();
					}
					if (fr != null) {
						fr.close();
					}
				}catch (Exception e) {
					Log.i(TAG, e.getMessage());
				}
			}
		}
		return content;
	}
	
	//获取图片的路径
	public static String getImagePath(Map<String, String> imagePosition, LinkedList<String> imageList) {
		String picPath = null;
		if(imagePosition != null && imagePosition.size() > 0) {
			int position = Integer.parseInt(imagePosition.get("positionId"));
			if(imageList != null && imageList.size() > 0) {
				picPath = imageList.get(position);
			}
		}
		return picPath;
	}
	
	//获取图片位置
	public static String getImagePagePosition(Map<String, String> imagePosition, LinkedList<String> imageList) {
		String pagePosition = null;
		if(imagePosition != null && imagePosition.size() > 0) {
			int page = Integer.parseInt(imagePosition.get("positionId")) + 1;
			int totalPages = imageList.size();
			StringBuffer sbf = new StringBuffer();
			sbf.append(String.valueOf(page));
			sbf.append("/");
			sbf.append(String.valueOf(totalPages));
			pagePosition = sbf.toString();
			sbf = null;
		}
		return pagePosition;
	}
}
