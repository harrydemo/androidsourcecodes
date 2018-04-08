package org.loon.framework.javase.game.core.resource;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;

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
public class RemoteRes extends DataRes implements Resource {

	public RemoteRes(String url) {
		this.path = url;
		this.name = url;
	}

	public InputStream getInputStream() {
		try {
			if (in != null) {
				return in;
			}
			return in = new URL(path).openStream();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getResourceName() {
		return name;
	}

	public URI getURI() {
		try {
			return new URL(path).toURI();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		RemoteRes other = (RemoteRes) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

}
