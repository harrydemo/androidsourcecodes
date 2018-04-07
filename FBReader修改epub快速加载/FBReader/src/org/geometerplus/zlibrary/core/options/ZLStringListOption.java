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

package org.geometerplus.zlibrary.core.options;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ZLStringListOption extends ZLOption {
	private final List<String> myDefaultValue;
	private List<String> myValue;

	private static String listToString(List<String> list) {
		if (list == null || list.isEmpty()) {
			return "";
		}
		final StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (String s : list) {
			if (first) {
				first = false;
			} else {
				builder.append(",");
			}
			builder.append(s);
		}
		return builder.toString();
	}

	private static List<String> stringToList(String str) {
		if (str == null || "".equals(str)) {
			return Collections.emptyList();
		}
		return Arrays.asList(str.split(","));
	}

	public ZLStringListOption(String group, String optionName, List<String> defaultValue) {
		super(group, optionName);
		myDefaultValue = (defaultValue != null) ? defaultValue : Collections.<String>emptyList();
		myValue = myDefaultValue;
	}

	public List<String> getValue() {
		if (!myIsSynchronized) {
			final String value = getConfigValue(listToString(myDefaultValue));
			if (value != null) {
				myValue = stringToList(value);
			}
			myIsSynchronized = true;
		}
		return Collections.unmodifiableList(myValue);
	}

	public void setValue(List<String> value) {
		if (value == null) {
			value = Collections.emptyList();
		}
		if (myIsSynchronized && (myValue.equals(value))) {
			return;
		}
		myValue = new ArrayList<String>(value);
		if (value.equals(myDefaultValue)) {
			unsetConfigValue();
		} else {
			setConfigValue(listToString(value));
		}
		myIsSynchronized = true;
	}
}
