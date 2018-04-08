/*
 * Copyright (C) 2007-2012 Geometer Plus <contact@geometerplus.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package org.geometerplus.zlibrary.ui.android.view;

import java.util.*;
import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import android.graphics.Typeface;

import org.geometerplus.zlibrary.core.util.ZLTTFInfoDetector;

import org.geometerplus.fbreader.Paths;

public final class AndroidFontUtil {
	private static Method ourFontCreationMethod;
	static {
		try {
			ourFontCreationMethod = Typeface.class.getMethod("createFromFile", File.class);
		} catch (NoSuchMethodException e) {
			ourFontCreationMethod = null;
		}
	}

	public static boolean areExternalFontsSupported() {
		return ourFontCreationMethod != null;
	}

	private static Typeface createFontFromFile(File file) {
		if (ourFontCreationMethod == null) {
			return null;
		}
		try {
			return (Typeface)ourFontCreationMethod.invoke(null, file);
		} catch (IllegalAccessException e) {
			return null;
		} catch (InvocationTargetException e) {
			return null;
		}
	}

	private static Map<String,File[]> ourFontMap;
	private static Set<File> ourFileSet;
	private static long ourTimeStamp;

	private static Map<String,File[]> getFontMap(boolean forceReload) {
		if (ourFontCreationMethod == null) {
			return Collections.emptyMap();
		}

		final long timeStamp = System.currentTimeMillis();
		if (forceReload && timeStamp < ourTimeStamp + 1000) {
			forceReload = false;
		}
		ourTimeStamp = timeStamp;
		if (ourFileSet == null || forceReload) {
			final HashSet<File> fileSet = new HashSet<File>();
			final FilenameFilter filter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					if (name.startsWith(".")) {
						return false;
					}
					final String lcName = name.toLowerCase();
					return lcName.endsWith(".ttf") || lcName.endsWith(".otf");
				}
			};
			final File[] fileList = new File(Paths.FontsDirectoryOption().getValue()).listFiles(filter);
			if (fileList != null) {
				fileSet.addAll(Arrays.asList(fileList));
			}
			if (!fileSet.equals(ourFileSet)) {
				ourFileSet = fileSet;
				ourFontMap = new ZLTTFInfoDetector().collectFonts(fileSet);
			}
		}
		return ourFontMap;
	}

	public static String realFontFamilyName(String fontFamily) {
		for (String name : getFontMap(false).keySet()) {
			if (name.equalsIgnoreCase(fontFamily)) {
				return name;
			}
		}
		if ("serif".equalsIgnoreCase(fontFamily) || "droid serif".equalsIgnoreCase(fontFamily)) {
			return "serif";
		}
		if ("sans-serif".equalsIgnoreCase(fontFamily) || "sans serif".equalsIgnoreCase(fontFamily) || "droid sans".equalsIgnoreCase(fontFamily)) {
			return "sans-serif";
		}
		if ("monospace".equalsIgnoreCase(fontFamily) || "droid mono".equalsIgnoreCase(fontFamily)) {
			return "monospace";
		}
		return "sans-serif";
	}

	public static void fillFamiliesList(ArrayList<String> families) {
		final TreeSet<String> familySet = new TreeSet<String>(getFontMap(true).keySet());
		familySet.add("Droid Sans");
		familySet.add("Droid Serif");
		familySet.add("Droid Mono");
		families.addAll(familySet);
	}

	private static final HashMap<String,Typeface[]> ourTypefaces = new HashMap<String,Typeface[]>();

	public static Typeface typeface(String family, boolean bold, boolean italic) {
		family = realFontFamilyName(family);
		final int style = (bold ? Typeface.BOLD : 0) | (italic ? Typeface.ITALIC : 0);
		Typeface[] typefaces = ourTypefaces.get(family);
		if (typefaces == null) {
			typefaces = new Typeface[4];
			ourTypefaces.put(family, typefaces);
		}
		Typeface tf = typefaces[style];
		if (tf == null) {
			File[] files = getFontMap(false).get(family);
			if (files != null) {
				try {
					if (files[style] != null) {
						tf = createFontFromFile(files[style]);
					} else {
						for (int i = 0; i < 4; ++i) {
							if (files[i] != null) {
								tf = (typefaces[i] != null) ?
									typefaces[i] : createFontFromFile(files[i]);
								typefaces[i] = tf;
								break;
							}
						}
					}
				} catch (Throwable e) {
				}
			}
			if (tf == null) {
				tf = Typeface.create(family, style);
			}
			typefaces[style] = tf;
		}
		return tf;
	}

	public static void clearFontCache() {
		ourTypefaces.clear();
		ourFileSet = null;
	}
}
