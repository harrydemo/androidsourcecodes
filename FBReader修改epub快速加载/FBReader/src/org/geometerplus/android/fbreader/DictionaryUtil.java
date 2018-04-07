/*
 * Copyright (C) 2010-2011 Geometer Plus <contact@geometerplus.com>
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

package org.geometerplus.android.fbreader;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.geometerplus.android.util.PackageUtil;
import org.geometerplus.android.util.UIUtil;
import org.geometerplus.zlibrary.core.filesystem.ZLFile;
import org.geometerplus.zlibrary.core.options.ZLStringOption;
import org.geometerplus.zlibrary.core.resources.ZLResource;
import org.geometerplus.zlibrary.core.xml.ZLStringMap;
import org.geometerplus.zlibrary.core.xml.ZLXMLReaderAdapter;
import org.geometerplus.zlibrary.text.view.ZLTextRegion;
import org.geometerplus.zlibrary.text.view.ZLTextWord;
import org.geometerplus.zlibrary.ui.android.library.ZLAndroidApplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.Gravity;

public abstract class DictionaryUtil {
	// Map: dictionary info -> hide if package is not installed
	private static LinkedHashMap<PackageInfo,Boolean> ourDictionaryInfos =
		new LinkedHashMap<PackageInfo,Boolean>();
	private static ZLStringOption ourSingleWordTranslatorOption;
	private static ZLStringOption ourMultiWordTranslatorOption;

	private static class InfoReader extends ZLXMLReaderAdapter {
		@Override
		public boolean dontCacheAttributeValues() {
			return true;
		}

		@Override
		public boolean startElementHandler(String tag, ZLStringMap attributes) {
			if ("dictionary".equals(tag)) {
				final String id = attributes.getValue("id");
				final String title = attributes.getValue("title");
				ourDictionaryInfos.put(new PackageInfo(
					id,
					attributes.getValue("package"),
					attributes.getValue("class"),
					title != null ? title : id,
					attributes.getValue("action"),
					attributes.getValue("dataKey"),
					attributes.getValue("pattern")
				), !"always".equals(attributes.getValue("list")));
			}
			return false;
		}
	}

	private interface ColorDict3 {
		String ACTION = "colordict.intent.action.SEARCH";
		String QUERY = "EXTRA_QUERY";
		String HEIGHT = "EXTRA_HEIGHT";
		String WIDTH = "EXTRA_WIDTH";
		String GRAVITY = "EXTRA_GRAVITY";
		String MARGIN_LEFT = "EXTRA_MARGIN_LEFT";
		String MARGIN_TOP = "EXTRA_MARGIN_TOP";
		String MARGIN_BOTTOM = "EXTRA_MARGIN_BOTTOM";
		String MARGIN_RIGHT = "EXTRA_MARGIN_RIGHT";
		String FULLSCREEN = "EXTRA_FULLSCREEN";
	}

	private static Map<PackageInfo,Boolean> infos() {
		if (ourDictionaryInfos.isEmpty()) {
			new InfoReader().read(ZLFile.createFileByPath("dictionaries.xml"));
		}
		return ourDictionaryInfos;
	}

	public static List<PackageInfo> dictionaryInfos(Context context) {
		final LinkedList<PackageInfo> list = new LinkedList<PackageInfo>();
		for (Map.Entry<PackageInfo,Boolean> entry : infos().entrySet()) {
			final PackageInfo info = entry.getKey();
			if (!entry.getValue() ||
				PackageUtil.canBeStarted(context, getDictionaryIntent(info, "test"), false)) {
				list.add(info);
			}
		}
		return list;
	}

	private static PackageInfo firstInfo() {
		for (Map.Entry<PackageInfo,Boolean> entry : infos().entrySet()) {
			if (!entry.getValue()) {
				return entry.getKey();
			}
		}
		throw new RuntimeException("There are no available dictionary infos");
	}

	public static ZLStringOption singleWordTranslatorOption() {
		if (ourSingleWordTranslatorOption == null) {
			ourSingleWordTranslatorOption = new ZLStringOption("Dictionary", "Id", firstInfo().Id);
		}
		return ourSingleWordTranslatorOption;
	}

	public static ZLStringOption multiWordTranslatorOption() {
		if (ourMultiWordTranslatorOption == null) {
			ourMultiWordTranslatorOption = new ZLStringOption("Translator", "Id", firstInfo().Id);
		}
		return ourMultiWordTranslatorOption;
	}

	private static PackageInfo getCurrentDictionaryInfo(boolean singleWord) {
		final ZLStringOption option = singleWord
			? singleWordTranslatorOption() : multiWordTranslatorOption();
		final String id = option.getValue();
		for (PackageInfo info : infos().keySet()) {
			if (info.Id.equals(id)) {
				return info;
			}
		}
		return firstInfo();
	}

	private static Intent getDictionaryIntent(String text, boolean singleWord) {
		return getDictionaryIntent(getCurrentDictionaryInfo(singleWord), text);
	}

	public static Intent getDictionaryIntent(PackageInfo dictionaryInfo, String text) {
		final Intent intent = new Intent(dictionaryInfo.IntentAction);
		if (dictionaryInfo.PackageName != null) {
			String cls = dictionaryInfo.ClassName;
			if (cls != null && cls.startsWith(".")) {
				cls = dictionaryInfo.PackageName + cls;
			}
			intent.setComponent(new ComponentName(
				dictionaryInfo.PackageName, cls
			));
		}
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		text = dictionaryInfo.IntentDataPattern.replace("%s", text);
		if (dictionaryInfo.IntentKey != null) {
			return intent.putExtra(dictionaryInfo.IntentKey, text);
		} else {
			return intent.setData(Uri.parse(text));
		}			
	}

	public static void openTextInDictionary(Activity activity, String text, boolean singleWord, int selectionTop, int selectionBottom) {
		if (singleWord) {
			int start = 0;
			int end = text.length();
			for (; start < end && !Character.isLetterOrDigit(text.charAt(start)); ++start);
			for (; start < end && !Character.isLetterOrDigit(text.charAt(end - 1)); --end);
			if (start == end) {
				return;
			}
			text = text.substring(start, end);
		}

		final PackageInfo info = getCurrentDictionaryInfo(singleWord);
		final Intent intent = getDictionaryIntent(info, text);
		try {
			if ("ColorDict".equals(info.Id)) {
				final DisplayMetrics metrics = new DisplayMetrics();
				activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
				final int screenHeight = metrics.heightPixels;
				final int topSpace = selectionTop;
				final int bottomSpace = metrics.heightPixels - selectionBottom;
				final boolean showAtBottom = bottomSpace >= topSpace;
				final int space = (showAtBottom ? bottomSpace : topSpace) - 20;
				final int maxHeight = Math.min(400, screenHeight * 2 / 3);
				final int minHeight = Math.min(200, screenHeight * 2 / 3);
				intent.putExtra(ColorDict3.HEIGHT, Math.max(minHeight, Math.min(maxHeight, space)));
				intent.putExtra(ColorDict3.GRAVITY, showAtBottom ? Gravity.BOTTOM : Gravity.TOP);
				final ZLAndroidApplication application = ZLAndroidApplication.Instance();
				intent.putExtra(ColorDict3.FULLSCREEN, !application.ShowStatusBarOption.getValue());
			}
			activity.startActivity(intent);
		} catch (ActivityNotFoundException e) {
			DictionaryUtil.installDictionaryIfNotInstalled(activity, singleWord);
		}
	}

	public static void openWordInDictionary(Activity activity, ZLTextWord word, ZLTextRegion region) { 
		openTextInDictionary(
			activity, word.toString(), true, region.getTop(), region.getBottom()
		);
	}

	public static void installDictionaryIfNotInstalled(final Activity activity, boolean singleWord) {
		if (PackageUtil.canBeStarted(activity, getDictionaryIntent("test", singleWord), false)) {
			return;
		}
		final PackageInfo dictionaryInfo = getCurrentDictionaryInfo(singleWord);

		final ZLResource dialogResource = ZLResource.resource("dialog");
		final ZLResource buttonResource = dialogResource.getResource("button");
		final ZLResource installResource = dialogResource.getResource("installDictionary");
		new AlertDialog.Builder(activity)
			.setTitle(installResource.getResource("title").getValue())
			.setMessage(installResource.getResource("message").getValue().replace("%s", dictionaryInfo.Title))
			.setIcon(0)
			.setPositiveButton(
				buttonResource.getResource("install").getValue(),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						installDictionary(activity, dictionaryInfo);
					}
				}
			)
			.setNegativeButton(buttonResource.getResource("skip").getValue(), null)
			.create().show();
	}

	private static void installDictionary(Activity activity, PackageInfo dictionaryInfo) {
		if (!PackageUtil.installFromMarket(activity, dictionaryInfo.PackageName)) {
			UIUtil.showErrorMessage(activity, "cannotRunAndroidMarket", dictionaryInfo.Title);
		}
	}
}
