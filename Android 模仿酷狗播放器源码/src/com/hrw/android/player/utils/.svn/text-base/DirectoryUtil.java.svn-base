package com.hrw.android.player.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DirectoryUtil {
	private final static String MNT_SDCARD_ROOT = "/";
	public static List<String> fileNames = new ArrayList<String>();

	public static Set<String> mediaPaths = new HashSet<String>();

	public static File[] getDirectoryList() {
		File[] files = new File(MNT_SDCARD_ROOT).listFiles(new FileFilter() {
			@Override
			public boolean accept(final File pathname) {
				if (pathname.isDirectory()
						&& pathname.getPath().indexOf("/sdcard") != -1) {
					return true;
				} else {
					return false;
				}
			}

		});
		return files;

	}

	public static String[] getDirectoryNameList() {
		List<String> fileNames = new ArrayList<String>();
		File[] files = getDirectoryList();
		for (File file : files) {
			fileNames.add(file.getName());
		}
		return fileNames.toArray(new String[fileNames.size()]);
	}

	/**
	 * 扫描SD卡中所有文件夹
	 * 
	 * @param path
	 * @return
	 */
	public static String[] folderScan(String path) {
		File file = new File(path);
		if (file.isDirectory()) {
			File[] array = file.listFiles(new FileFilter() {
				@Override
				public boolean accept(final File pathname) {
					if (pathname.isDirectory()
							&& pathname.getPath().indexOf("/sdcard") != -1) {
						return true;
					} else {
						return false;
					}
				}

			});
			if (null != array) {
				for (int i = 0; i < array.length; i++) {
					File f = array[i];
					if (f.isDirectory()) {
						fileNames.add(f.getAbsolutePath());
						folderScan(f.getAbsolutePath());
					}
				}
			}
		}
		return fileNames.toArray(new String[fileNames.size()]);
	}

	/**
	 * 
	 * @param path
	 *            the path is the directory's path
	 * @return
	 */
	public static String[] MediaScan(String path) {
		// this file must be directory.
		File file = new File(path);
		File[] array = file.listFiles(new FileFilter() {
			@Override
			public boolean accept(final File pathname) {
				if (pathname.isDirectory()
						|| pathname.getPath().indexOf(".mp3") != -1) {
					return true;
				} else {
					return false;
				}
			}
		});

		if (null != array) {
			for (int i = 0; i < array.length; i++) {
				File f = array[i];
				if (f.isDirectory()) {
					// mediaPaths.add(f.getAbsolutePath());
					MediaScan(f.getAbsolutePath());
				} else {
					mediaPaths.add(f.getAbsolutePath());
				}

			}
		}
		// if (file.isDirectory()) {
		// File[] array = file.listFiles(new FileFilter() {
		// @Override
		// public boolean accept(final File pathname) {
		// if (!pathname.isDirectory()
		// && pathname.getPath().indexOf(".mp3") != -1) {
		// return true;
		// } else {
		// return false;
		// }
		// }
		//
		// });
		// if (null != array) {
		// for (int i = 0; i < array.length; i++) {
		// File f = array[i];
		// if (f.isDirectory()) {
		// fileNames.add(f.getAbsolutePath());
		// folderScan(f.getAbsolutePath());
		// }
		// }
		// }
		// }
		return mediaPaths.toArray(new String[mediaPaths.size()]);
	}

}
