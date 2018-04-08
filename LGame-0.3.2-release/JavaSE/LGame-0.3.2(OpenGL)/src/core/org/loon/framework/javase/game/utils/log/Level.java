package org.loon.framework.javase.game.utils.log;

/**
 * Copyright 2008 - 2009
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * @project loonframework
 * @author chenpeng
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public class Level {

	// debug模式
	public static final Level DEBUG = new Level("Debug", 1);

	// info模式
	public static final Level INFO = new Level("Info", 2);

	// warn模式
	public static final Level WARN = new Level("Warn", 3);

	// error模式
	public static final Level ERROR = new Level("Error", 4);

	// ignore模式
	public static final Level IGNORE = new Level("Ignore", 5);

	String levelString;

	final int level;

	private Level(String levelString, int levelInt) {
		this.levelString = levelString;
		this.level = levelInt;
	}

	public String toString() {
		return levelString;
	}

	public int toType() {
		return level;
	}
}
