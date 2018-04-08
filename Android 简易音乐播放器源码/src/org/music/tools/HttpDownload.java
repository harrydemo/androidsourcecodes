package org.music.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * �ļ�������,�漰���ļ�����/����ⲿ�֣��������ص������ļ����������������
 */
public class HttpDownload {
	public static  String downloadStringFromInternetFile(String urlStr) {
		StringBuffer sb = null;
		String line = null;
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader buffer = null;
		try {
			inputStream = getInputStreamFromUrl(urlStr);
			inputStreamReader = new InputStreamReader(inputStream, "GB2312");//��������ʾUTF-8�������дGB2312,��дGB2312�������ˡ���Ӵ����������
			// ʹ��IO����ȡ����
			buffer = new BufferedReader(inputStreamReader);
			sb = new StringBuffer();
			while ((line = buffer.readLine()) != null) {
				sb.append(line.trim());
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (buffer != null) {
					buffer.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (inputStreamReader != null) {
						inputStreamReader.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (inputStream != null) {
							inputStream.close();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

	/**
	 * �ú����������� -1�����������ļ����� 0�����������ļ��ɹ� 1�������ļ��Ѿ�����
	 */
	public int downLoadFile(String urlStr, String path, String finalFileName) {
		InputStream inputStream = null;
		try {
			if (FileUtil.isFileExist(finalFileName+".lrc", path)) {
				FileUtil.deleteFile(path, finalFileName+".lrc");
			}
			inputStream = getInputStreamFromUrl(urlStr);
			File resultFile = FileUtil.write2SDFromInput(path, finalFileName+".lrc",
					inputStream);
			if (resultFile == null) {
				return -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	/**
	 * ����URL�õ�������
	 */
	public static InputStream getInputStreamFromUrl(String urlStr)
			throws IOException {
		URL url = null;
		HttpURLConnection urlConn = null;
		url = new URL(urlStr);
		urlConn = (HttpURLConnection) url.openConnection();
		urlConn.setConnectTimeout(6000);
		urlConn.setReadTimeout(6000);
		urlConn.setRequestMethod("GET");
		return urlConn.getInputStream();
	}

}
