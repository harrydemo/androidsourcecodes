package com.pixtas.helpers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class RemoteFile {
	public static int getRemoteFileSize(String url){
		/*访问远程音频文件的大小*/
		URL myURL;
		int fileSize = 0;
		try {
			myURL = new URL(url);
			URLConnection conn;
			try {
				conn = myURL.openConnection();
				conn.connect();
				fileSize = conn.getContentLength();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return fileSize;
	}
}
