package org.loon.framework.javase.game.core.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * Copyright 2008 - 2011
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
abstract class DataRes {

	String path;

	String name;

	InputStream in;

	URI uri;

	public int hashCode() {
		return (name == null) ? super.hashCode() : name.hashCode();
	}

	public void dispose() {
		if (in != null) {
			try {
				in.close();
				in = null;
			} catch (IOException e) {
			}
		}
		if (uri != null) {
			uri = null;
		}
	}
}
