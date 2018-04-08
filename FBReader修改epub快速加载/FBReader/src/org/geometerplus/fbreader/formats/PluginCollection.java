/*
 * Copyright (C) 2007-2011 Geometer Plus <contact@geometerplus.com>
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

package org.geometerplus.fbreader.formats;

import java.util.ArrayList;

import org.geometerplus.fbreader.formats.oeb.OEBPlugin;
import org.geometerplus.zlibrary.core.filesystem.ZLFile;
import org.geometerplus.zlibrary.core.options.ZLBooleanOption;
import org.geometerplus.zlibrary.core.options.ZLStringOption;

import com.nil.util.L;

public class PluginCollection {
	static {
		System.loadLibrary("NativeFormats");
	}

	private static PluginCollection ourInstance;

	private final ArrayList<FormatPlugin> myPlugins = new ArrayList<FormatPlugin>();
	private final ArrayList<FormatPlugin> myJavaPlugins = new ArrayList<FormatPlugin>();
	public ZLStringOption DefaultLanguageOption;
	public ZLStringOption DefaultEncodingOption;
	public ZLBooleanOption LanguageAutoDetectOption;

	public static PluginCollection Instance() {
		if (ourInstance == null) {
			ourInstance = new PluginCollection();
			//ourInstance.myJavaPlugins.add(new FB2Plugin());
			//ourInstance.myJavaPlugins.add(new MobipocketPlugin());
			ourInstance.myJavaPlugins.add(new OEBPlugin());

			ourInstance.myPlugins.addAll(ourInstance.myJavaPlugins);

			ourInstance.runTests();
		}
		return ourInstance;
	}

	public static void deleteInstance() {
		if (ourInstance != null) {
			ourInstance = null;
		}
	}

	private PluginCollection() {
		LanguageAutoDetectOption = new ZLBooleanOption("Format", "AutoDetect",
				true);
		DefaultLanguageOption = new ZLStringOption("Format", "DefaultLanguage",
				"en");
		DefaultEncodingOption = new ZLStringOption("Format", "DefaultEncoding",
				"windows-1252");
	}

	public FormatPlugin getPlugin(ZLFile file) {
		for (FormatPlugin plugin : myPlugins) {
			if (plugin.acceptsFile(file)) {
				return plugin;
			}
		}

		L.l("====================format====plugin==");
		final FormatPlugin plugin = getNativePlugin(file.getPath());
		if (plugin != null) {
			myPlugins.add(plugin);
		}
		return plugin;
	}

	/* package */FormatPlugin getJavaPlugin(ZLFile file) {
		for (FormatPlugin plugin : myJavaPlugins) {
			if (plugin.acceptsFile(file)) {
				return plugin;
			}
		}
		return null;
	}

	private native FormatPlugin getNativePlugin(String path);

	private native void free();

	// called from native code
	public String getDefaultLanguage() {
		return DefaultLanguageOption.getValue();
	}

	// called from native code
	public String getDefaultEncoding() {
		return DefaultEncodingOption.getValue();
	}

	// called from native code
	public boolean isLanguageAutoDetectEnabled() {
		return LanguageAutoDetectOption.getValue();
	}

	protected void finalize() throws Throwable {
		deleteInstance();
		myPlugins.clear();
		myJavaPlugins.clear();
		free();
		super.finalize();
	};

	private native void runTests();
}
