/*
 * [程序名称] Android 音乐播放器
 * [参考资料] http://code.google.com/p/rockon-android/ 
 * [开源协议] Apache License, Version 2.0 (http://www.apache.org/licenses/LICENSE-2.0)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xmobileapp.rockplayer;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLGoogleAlbumArtHandler extends DefaultHandler {

	public int MAX_IMAGES = 3;

	String imageDataTagBaseString = "tDataImage";

	boolean[] imageDataTag = { false, false, false };

	String[] albumArtUrl = { null, null, null };

	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		try {
			if (localName.equals("td")) {
				for (int i = 0; i < MAX_IMAGES; i++) {
					if (atts.getValue("id").equals(
							imageDataTagBaseString + Integer.toString(i))) {
						imageDataTag[i] = true;
					}
				}
			}
			if (localName.equals("img")) {
				for (int i = 0; i < MAX_IMAGES; i++) {
					if (imageDataTag[i]) {
						int urlCharIndex = 0;
						for (int j = 0; j < 3; j++) {
							urlCharIndex = atts.getValue("src").indexOf(':',
									urlCharIndex);
						}
						albumArtUrl[i] = new String(atts.getValue("src")
								.substring(urlCharIndex));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (localName.equals("td")) {
			for (int i = 0; i < MAX_IMAGES; i++) {
				imageDataTag[i] = false;
			}
		}
	}

	public void clear() {
		for (int i = 0; i < MAX_IMAGES; i++) {
			imageDataTag[i] = false;
			albumArtUrl[i] = null;
		}
	}
}
