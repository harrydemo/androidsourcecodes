package com.xjf.filedialog;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.android.xjf.utils.Common;

/**
 * �������ļ���service. ��һ�߳�ʵ��
 * 
 * 1.51 (����ʵ��, su ���cp,�޷���ʾ���ƶԻ���,��Ϊcp��,ֱ�ӷ�����һ��.).
 *  root ʱ, ��cp����,
 *  ��root ʱ, ��java����
 * */
public class CopyFileService extends Service {
	
	private final static String tag = "FileDialog";
	
	private Handler handler = null;
	private Context context = null;
	private ArrayList<String> from = null;
	private String toParentPath = null;
	//private NotificationManager NM;
	private boolean destroyAfterCopy = false;
	private boolean destroyed;
	
	private boolean isCopying = false;
	//private String perString = "777";
	boolean isCancel = false;
	boolean isCut = false;
	
	ProgressDialog dialog ;
	
	private final static int BUFF_SIZE = 8192;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return cBinder;
	}
	
	private CopyFileBinder cBinder = new CopyFileBinder();
	
	public void setDestroyAfterCopy(boolean d) { destroyAfterCopy = d;}
	public boolean isDestroyAfterCopy() { return destroyAfterCopy;}
	
	public boolean isDestroyed() { return destroyed;}
	
	public void setHandler(Handler h) { handler = h;}
	//public void setPermission(String pre) { this.perString = pre;}
	public void setContext(Context c) { 
		context = c;
		fileManager = (FileManager) c;
	}
	
	
	public void setFrom(ArrayList<String> f) { from = f;}
	public void setToParentPath(String t) { toParentPath = t;}
	
	/** ��ȡ CopyFileBinder ʵ��   */
	public class CopyFileBinder extends Binder {
		public CopyFileService getService(){
			return CopyFileService.this;
		}
	}
	
	FileManager fileManager;
	
    @Override
    public void onCreate() {
    	//Log.d(tag, "Service onCreate");
    	destroyed = false;
    	//NM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    	linux = Runtime.getRuntime();
    }
    
    
    // onStart ->  thread.satrt -> startCopy
    // -> startDoCopy -> copyfile
    
    @Override
	public void onStart(Intent intent, int startid) {
    	//Log.d(tag, "Service onStart");
    	//destroyAfterCopy = true;
    	if (from == null)
    		return;

		copyThread = new Thread(new CopyThread(from, toParentPath));
		copyThread.start();
	}
    
    /**
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }
    /**/
    @Override
    public void onDestroy() {
    	//Log.d(tag, "Service onDestroy");
    	destroyed = true;
    }
	
	private long copyLength = 0;
	long copyedLength = 0;
	
	class CopyThread implements Runnable{
		ArrayList<String> from; 
		String toPath;
		public CopyThread(ArrayList<String> from, 
				String toPath) {
			this.from = from;
			this.toPath = toPath;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			startCopy(from, toPath);
		}
		
	}
	
	/**
	 *  /dev
	 *  /system
	 *  /data
	 *  /cache
	 *  /sdcard
	 *  /sqlite_stmt_journals
	 *  /mnt/asec ??
	 * */
	
	public static String getFirstPart(String path){
		int index = path.lastIndexOf("/");
		if (index == 0)
			return "/";
		if (index == -1)
			return "";
		return path.substring(0, index - 1);
	}

	public void cancelCopy() { 
		if (moveProcess != null) {
			moveProcess.destroy();
			moveProcess = null;
		}
		if (rootProcess != null) {
			rootProcess.destroy();
			doDelete();
			rootProcess = null;
		}
		isCancel = true;
		isCopying = false;
	}
	
	
	// startCopy -> thread.start -> startDoCopy -> copyfiles
	Runtime linux;
	public boolean root = false;
	private boolean deleAfterCopy = false;
	Thread copyThread;
	private ArrayList<String> tmpCutFiles = null;

	private Process rootProcess = null;
	private BufferedReader rootEBR = null;
	private DataOutputStream rootOS = null;
	Process moveProcess = null;
	@SuppressWarnings("unchecked")
	public void startCopy(ArrayList<String> from, 
								String toPath){
		if (isCopying)
			return;
		File dFile = new File(toPath);
		if (!root && !dFile.canWrite()) {
			doFailure();
			return;
		}
		String[] cmds = null;

		if (!dFile.isDirectory())
			return;
		deleAfterCopy = isCut;
		int ret = -1;
		checkFile = true;
		int count = from.size();
		DataOutputStream shell = null;
		BufferedReader err = null;
		/**
		 * �������ܲ�����mv��������
		 * */
		if (isCut) {
			handler.sendEmptyMessage(FileItemClickListener.HANDLER_SHOW_CUT_DIALOG);
			tmpCutFiles = (ArrayList<String>) from.clone();
		}
		if (isCut && (!root || dFile.canWrite())) {
			String fPath = null;
			try {
				File dst;
				for (int i = 0; i < count; i++) {
					fPath = from.get(i);
					toFile = toPath + "/" + Common.getPathName(fPath);

					dst = new File(toFile);
					if (dst.exists()) {
						ret = multFile(dst.getAbsolutePath());
						if (ret == FileItemClickListener.COPY_CANCEL) {
							doCancel();
							return;
						}
						if (ret == FileItemClickListener.COPY_SKIP) {
							tmpCutFiles.remove(fPath);
							continue;
						}
						dst = new File(toFile);
					}
					if (toFile.contains(fPath))
						continue;
					cmds = new String[] { "mv", fPath, toFile };
					moveProcess = linux.exec(cmds);
					ret = moveProcess.waitFor();
					if (ret != 0) {
						if (!root && !dFile.canWrite()) {
							doFailure();
							return;
						}
						startDoCopy(from, toPath);
						return;
					}
				}
				// success
				if (isCut) {
					deleAfterCopy = false;
					doSuccess();
					return;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (moveProcess != null)
					moveProcess.destroy();
			}
		} else {
			startDoCopy(from, toPath);
		}
	}
	
	//�Ƿ����ļ��Ѵ��� (��־)
	private boolean checkFile = false;
    
	
	private void doFinish(boolean success) {

		isCopying = false;
		allDoSame = false;
		if (destroyAfterCopy)
			this.stopSelf();
    	from = null;
    	toParentPath = null;
		moveProcess = null;
		if (success && !isCut) {
			handler.sendEmptyMessage(FileItemClickListener.HANDLER_COPY_FINISHED);
		} else if (success){
			handler.sendEmptyMessage(FileItemClickListener.HANDLER_CUT_FINISH);
		}
	}
	
	
	private void doFailure(){
		doFinish(false);
		handler.sendEmptyMessage(
				FileItemClickListener.HANDLER_COPY_FAILURE);
	}
	
	private void doCancel(){
		doFinish(false);
		handler.sendEmptyMessage(
				FileItemClickListener.HANDLER_COPY_CANCEL);
	}
	
	private void doSuccess() {
		if (deleAfterCopy){
			doDelete();
		}
		doFinish(true);
		try {
			if (out != null) {
				out.close();
			}
			if (in != null)
				in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	public String pasteToPath = "";
	public int selection = -1;
	public boolean allDoSame = false;
	public boolean isHidden = false;
	public boolean successOrCancel() { return isCancel;}
	
	public void setHidden(boolean b) { 
		isHidden = b;
	}


	boolean largeFile = false;
	
	private int perSize = 0;
	
	/**
	 * ��ɸ���, ����ļ�
	 */
    private void startDoCopy(ArrayList<String> from, 
    			String toPath){
    	
		int fSize = from.size();
		copyLength = 0;
		copyedLength = 0;
		isCopying = true;
		isCancel = false;
		allDoSame = false;
		selection = -1;
		Message msg;
		boolean result = false;

		try {
			// �����ļ��ܴ�С. toPath.contains("/sdcard")����Ϊ�ڱ��Ŀ¼�п�����������,,java������
			if (toPath.contains("/sdcard")) {
				for (int i = 0; i < fSize; i++) {
					copyLength += FileOperation.getDirectorySize(from.get(i));
				}
			} else {
				copyLength = 100;
			}
			
			// !cut
			if (!deleAfterCopy) {
				msg = handler
						.obtainMessage(FileItemClickListener.HANDLER_SHOW_COPY_PROGRESS_DIALOG);
				msg.arg1 = (int) copyLength;
				perSize = ((int) copyLength / 100);
				handler.sendMessage(msg);
			}
			int ret = 0;
			//root�û����� !canWrite && 
			
			if (root) {
				String fsPerm = fileManager.getCurrentDirPerm();
				if (fsPerm != null && fsPerm.equals(FileManager.Mounts.RO)) {
					Log.d(tag, "file system read only");
					return;
				}
				///

				rootProcess = fileManager.linux.shell.exec("su");
				rootOS = new DataOutputStream(rootProcess.getOutputStream());
				rootEBR = new BufferedReader(new InputStreamReader(rootProcess.getErrorStream()));
			} // root
			
			
			// ���ļ�������Ҫ,,ͳһ����
			for (int i = 0; i < fSize; i++) {
				String fr = from.get(i);
				toFile = toPath + "/" + Common.getPathName(fr);
				File fd = new File(toFile);
				if (checkFile) {
					if (fr.equals(toFile)) {
						toFile = Common.pathNameAppend(toFile, "(2)");
						while ((fd = new File(toFile)).exists()) {
							toFile = Common.pathNameAppend(toFile, "(2)");
						}
					}
				}

				File fs = new File(fr);
				// ���ƶԻ�����ʾ ��ǰ�����ļ�
				if (!isCut) {
					msg = handler.obtainMessage(FileItemClickListener.HANDLER_PROCESS_SET_MESSAGE);
					Bundle b = new Bundle();
					b.putString(FileItemClickListener.BUNDLE_FROM_PATH, fr);
					b.putString(FileItemClickListener.BUNDLE_TO_PATH, toFile);
					msg.setData(b);
					
					handler.sendMessage(msg);
				}
				//!canWrite && root
				if (root) {
					String rootCmd = "";
					if (fd.exists()) {
						ret = multFile(fd.getAbsolutePath());
						if (ret == FileItemClickListener.COPY_CANCEL) {
							doCancel();
							return;
						}
						if (ret == FileItemClickListener.COPY_SKIP) {
							if (isCut) {
								tmpCutFiles.remove(fr);
							}
							continue;
						}
						fd = new File(toFile);
					}
					/**/
					if (!isCut) {
						rootCmd = "cp -fpr \"" + fs.getAbsolutePath() + "\" \""
							+ fd.getAbsolutePath()
							+ "\"\n";
					} else {
						rootCmd = "mv -r \"" + fs.getAbsolutePath() + "\" \""
							+ fd.getAbsolutePath()
							+ "\"\n";
					}
					if (FileManager.D) Log.d(tag, rootCmd);
					rootOS.write(rootCmd.getBytes());
					
					if (rootEBR.ready()) {
						Log.e(tag, "rootEBR: " + rootEBR.readLine());
						// return;
					}
					/***/

				} else {
					// no root
					if (copyFile(fs, fd) == FileItemClickListener.COPY_CANCEL) {
						isCancel = true;
						doCancel();
						return;
					}
					handler.sendEmptyMessage(FileItemClickListener.HANDLER_REFRESH_LIST);
				}
				/**/
			} // for ()
			
			result = true;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			//Log.d(tag, "finally");
			if (rootProcess != null) {
				try {
					rootOS.writeBytes("exit\n");
					rootProcess.waitFor();
					if (FileManager.D) {
						Log.d(tag,  " s: " + rootOS.size());
						Log.d(tag, " " + rootProcess.waitFor()); 
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				rootProcess.destroy();
			}
			rootProcess = null;
			rootEBR = null;
			if (result) {
				doSuccess();
			} else {
				doFailure();
			}
			// Log.d(tag, "finish");
		}

	} // startDoCopy
    
    FileInputStream fIn = null;
	FileOutputStream fOut = null;
	BufferedInputStream in = null;
	BufferedOutputStream out = null;
	
	
	private final static int SUCCESS = 999;
	boolean doIsFile;
	
	/**
	 * �Ͳ㸴��.
	 * */
	private int copyFile(File src, File dst) throws InterruptedException {
		doIsFile = false;
		int ret = SUCCESS;
		Message msg;
		try {
			if (!root && dst.exists() && checkFile) {
				ret = multFile(dst.getAbsolutePath());
				if (ret == FileItemClickListener.COPY_CANCEL) {
					return FileItemClickListener.COPY_CANCEL;
				}
				if (ret == FileItemClickListener.COPY_SKIP) {
					return FileItemClickListener.COPY_SKIP;
				}
				dst = new File(toFile);
			}

			if (src.isFile()) {
				// �ļ�
				doIsFile = true;				
				fIn = new FileInputStream(src);
				fOut = new FileOutputStream(dst);
				in = new BufferedInputStream(fIn, BUFF_SIZE);
				out = new BufferedOutputStream(fOut, BUFF_SIZE);
				byte[] bytes = new byte[BUFF_SIZE];
				int length;
				int tmpSize = 0;
				while ((length = in.read(bytes)) != -1) {
					out.write(bytes, 0, length);
					copyedLength += length;
					tmpSize += length;
					/**/
					if (isCancel) {
						dst.delete();
						return (ret = FileItemClickListener.COPY_CANCEL);
					}
					if (!isHidden && (tmpSize >= perSize) && !deleAfterCopy) {
						msg = handler.obtainMessage(
								FileItemClickListener
								.HANDLER_INCREMENT_COPY_PROGRESS);
						msg.arg1 = tmpSize;
						tmpSize = 0;
						handler.sendMessage(msg);
					}
					/**/
				}
				out.flush();
			} else {
				// �ļ���
				//Log.d(tag, "folder");
				if (toFile.contains(src.getAbsolutePath()))
					return FileItemClickListener.COPY_SKIP;
				dst.mkdirs();
				File[] fFiles = src.listFiles();
								
				int count = fFiles.length;
				for (int i = 0; i < count; i++) {
					ret = copyFile(fFiles[i], new File(dst.getAbsoluteFile() + "/"
							+ fFiles[i].getName()));
					if ( ret == FileItemClickListener.COPY_CANCEL) {
						return FileItemClickListener.COPY_CANCEL;
					}
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (!doIsFile)
				return ret;
			try {
				if (fIn != null)
					fIn.close();
				if (fOut != null)
					fOut.close();
				return ret;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ret;
	}
    
	/** ���ڸ��Ƶ��ļ� */
    private String toFile = "";

    /**
     * ������ʱ,����ļ����ظ�ʱ���õ�.�������û�����,��ȡ�ʵ��Ĳ���.
     * ������, ����, �ϲ��ļ���, ����, ȡ��.
     * */
    private int multFile(String tof) throws InterruptedException{
		synchronized (context){
			if (!allDoSame){
				Message msg = handler.obtainMessage(
						FileItemClickListener.HANDLER_SHOW_COPY_WARN);
				Bundle bundle = new Bundle();
				bundle.putString(FileItemClickListener.BUNDLE_MULT_FILE_PATH, 
						tof);
				bundle.putBoolean(FileItemClickListener.BUNDLE_IS_FILE, doIsFile);
				msg.setData(bundle);
				handler.sendMessage(msg);
				context.wait();
			}
			
			switch (selection) {
			case FileItemClickListener.COPY_APPEND:
				toFile = Common.fileNameAppend(tof, "(2)");
				return FileItemClickListener.COPY_APPEND;
				
			case FileItemClickListener.COPY_REPLACED:
				toFile = tof;
				return FileItemClickListener.COPY_REPLACED;

			case FileItemClickListener.COPY_SKIP:
				return FileItemClickListener.COPY_SKIP;
				
			case FileItemClickListener.COPY_CANCEL:
			default:
				return FileItemClickListener.COPY_CANCEL;
			}
		}
    }
    
    private void doDelete(){
		BufferedReader br = null;
		//String fl = " ";
		Process p = null;
		int ret = -1;
		if (tmpCutFiles == null ||
				tmpCutFiles.isEmpty())
			return;
		int size = tmpCutFiles.size();
		try {
			if (root){ 
				/**��ǰ�޷�����,,��Ϊִ������ʱ,cp���ܻ�û�����.
				for (int i = 0; i < size; i++){
						fl = fl + "\"" + tmpCutFiles.get(i) + "\" "; 
				}
				p = linux.exec("su");
				DataOutputStream shell = new DataOutputStream(p.getOutputStream());
				String cmd = "rm -r \" + fl + "\n" + "exit\n";
				Log.d(tag, "@@cmd: " + cmd);
				shell.write(cmd.getBytes());
				shell.flush();
				shell.close();
				/**/
				// end root
			} else {
				tmpCutFiles.add(0, "rm");
				tmpCutFiles.add(1, "-r");
				p = linux.exec(
						tmpCutFiles.toArray(new String[size + 2]));
				tmpCutFiles = null;br = new BufferedReader(
						new InputStreamReader(p.getErrorStream()));
				ret = p.waitFor();
				if (ret != 0) 
					Log.d(tag, "Error(code = " + ret + "): " + br.readLine());
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			tmpCutFiles = null;
			if (p != null)
				p.destroy();
		}
	}
    
    
    
    

    
	
    
	
	/**
	try {
		Process p = null;
		if (root) {
			p = fileDialog.linux.shell.exec("su");
			shell = new DataOutputStream(p.getOutputStream());
			String cmd = " " ; //"rm -r " + fl + "\n" + "exit\n";
			String fPath = null;
			String tofile = null;
			err = new BufferedReader(new InputStreamReader
					(p.getErrorStream()));
			for (int i = 0; i < count; i++){
				fPath = from.get(i);
				tofile = toPath + "/" + Common.getPathName(fPath);
				//cmd += "echo  > \"" + tofile + "\"\n" ;
				cmd += "echo \"" + tofile + "\"\n" ;
			}
			cmd += "exit\n";
			Log.d(tag, "" + cmd);
			shell.write(cmd.getBytes());
			shell.flush();
			shell.close();
			
			
			ret = p.waitFor();
			if (ret != 0)
				Log.d(tag, "new: " + err.readLine());
			isCut = false;
			//end root mv 
		} else {

		}
		/**/
    

	/**
	Process p = null;
	try {
		p = ((FileDialog)context).linux.shell.exec("su");
		shell = new DataOutputStream(
				p.getOutputStream());
		err = new BufferedReader(new InputStreamReader
				(p.getErrorStream()));
		String cmd = ""; // "rm -r " + fl + "\n" + "exit\n";
		String fPath = null;
		
		for (int i = 0; i < count; i++) {
			fPath = from.get(i);
			cmd += "cp -r \"" + fPath + "\" \"" + toPath + "/\"\n";
		}
		
		cmd += "\nexit\n";
		shell.write(cmd.getBytes());
		shell.flush();
		shell.close();
		ret = p.waitFor();
		if (ret != 0){
			Log.d(tag, "" + err.readLine());
			doFailure();
		}else 
			doCopyFinish();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} finally{
		if (shell != null)
			try {
				shell.close();
				if ( err != null)
					err.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
} 
/**/
    
}
    
