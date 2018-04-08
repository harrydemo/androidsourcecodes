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

public class XMLAlbumArtHandler extends DefaultHandler{

	boolean smallTag = false;
	boolean mediumTag = false;
	boolean largeTag = false;
	boolean xlargeTag = false;
	
	String smallAlbumArt = null;
	String mediumAlbumArt = null;
	String largeAlbumArt = null;
	String xlargeAlbumArt = null;
	
	@Override
    public void startElement(String namespaceURI, 
    							String localName,
    							String qName,
    							Attributes atts) 
	throws SAXException {
		if(localName.equals("image")){
			if(atts.getValue("size").equals("small")){
				smallTag = true;
			} else if(atts.getValue("size").equals("medium")){
				mediumTag = true;
			} else if(atts.getValue("size").equals("large")){
				largeTag = true;
			} else if(atts.getValue("size").equals("xlarge")){
				xlargeTag = true;
			}
		}
	}
	
	@Override
	public void  endElement  (String uri, 
								String localName, 
								String qName)
	throws SAXException {
		if(localName.equals("image")){
			smallTag = false;
			mediumTag = false;
			largeTag = false;
			xlargeTag = false;
		}
	}
	
	 @Override
	 public void characters(char ch[], int start, int length) {
		 if(smallTag){
			 this.smallAlbumArt = new String(ch, start, length);
		 }
		 if(mediumTag){
			 this.mediumAlbumArt = new String(ch, start, length);
		 }
		 if(largeTag){
			 this.largeAlbumArt = new String(ch, start, length);
		 }
		 if(xlargeTag){
			 this.xlargeAlbumArt = new String(ch, start, length);
		 }
	 }
}
