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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.location.Location;
import android.location.LocationManager;


public class XMLArtistEventHandler extends DefaultHandler{

	LinkedList<ArtistEvent> artistEventList = new LinkedList<ArtistEvent>();
	double MAX_DISTANCE  = 100000000; // in meters
	
	boolean venueTag = false;
	boolean cityTag = false;
	boolean countryTag = false;
	boolean dateTag = false;
	boolean timeTag = false;
	boolean latitudeTag = false;
	boolean longitudeTag = false;
	
	String venue = null;
	String city = null;
	String country = null;
	String date = null;
	double dateInMillis = System.currentTimeMillis();
	String time = null;
	String artist = null; // configured externally
	String latitudeString = null;
	String longitudeString = null;
	
	DateFormat dateFormatter = new SimpleDateFormat("E, dd MMM yyyy");
	Location eventLocation = new Location(LocationManager.NETWORK_PROVIDER);
	Location myLocation = new Location(LocationManager.NETWORK_PROVIDER);
	double distance = MAX_DISTANCE + 1;

	
	@Override
    public void startElement(String namespaceURI, 
    							String localName,
    							String qName,
    							Attributes atts) 
	throws SAXException {
		//Log.i("XML", "ns "+namespaceURI+" ln "+localName+" qn "+qName);
		if(localName.equals("name")){
			venueTag = true;
		} else if(localName.equals("city")){
			cityTag = true;
		} else if(localName.equals("country")){
			countryTag = true;
		} else if(localName.equals("startDate")){
			dateTag = true;
		} else if(localName.equals("time")){
			timeTag = true;
		} else if(localName.equals("lat")){
			latitudeTag = true;
		} else if(localName.equals("long")){
			longitudeTag = true;
		}
	}
	
	@Override
	public void  endElement  (String uri, 
								String localName, 
								String qName)
	throws SAXException {
		//Log.i("XML", "ns "+uri+" ln "+localName+" qn "+qName);

		if(localName.equals("name")){
			venueTag = false;
		} else if(localName.equals("city")){
			cityTag = false;
		} else if(localName.equals("country")){
			countryTag = false;
		} else if(localName.equals("startDate")){
			dateTag = false;
		} else if(localName.equals("startTime")){
			timeTag = false;
		} else if(localName.equals("lat")){
			latitudeTag = false;
		} else if(localName.equals("long")){
			longitudeTag = false;
		} else if(localName.equals("event")){
			/*
			 * If event is too far away don't show it
			 */
			//Log.i("DBG", distanceTo()+" >? "+ MAX_DISTANCE);
			if(this.latitudeString == null ||
					this.longitudeString == null ||
					distanceTo() > MAX_DISTANCE){
				venue = null;
				city = null;
				country = null;
				date = null;
				time = null;
				latitudeString = null;
				longitudeString= null;
				return;
			}
			/*
			 * Save event info and add it to the list
			 */
			ArtistEvent artistEvent = new ArtistEvent();
			artistEvent.venue = venue;
			artistEvent.city = city;
			artistEvent.country = country;
			artistEvent.date = date;
			artistEvent.time = time;
			artistEvent.dateInMillis = dateInMillis;
			artistEvent.artist = artist;
			insertArtistEventByDate(artistEvent);
			venue = null;
			city = null;
			country = null;
			date = null;
			time = null;
			//artist = null;
		}
	}
	
	 @Override
	 public void characters(char ch[], int start, int length) {
		 if(venueTag){
			 this.venue = new String(ch, start, length);
		 }else if(cityTag){
			 this.city = new String(ch, start, length);
		 } else if(countryTag){
			 this.country = new String(ch, start, length);
		 } else if(dateTag){
			 this.date = new String(ch, start, length);
			 try {
				this.dateInMillis = this.dateFormatter.parse(this.date).getTime();
			} catch (ParseException e) {
				this.dateInMillis = System.currentTimeMillis() + (365*24*60*60*1000); // In one year
				e.printStackTrace();
			}
		 } else if(timeTag){
			 this.time = new String(ch, start, length);
		 } else if(latitudeTag){
			 //Log.i("XMLlatitude",  new String(ch, start, length));
			 this.latitudeString = new String(ch, start, length);
		 } else if(longitudeTag){
			 //Log.i("XMLlongitude",  new String(ch, start, length));
			 this.longitudeString = new String(ch, start, length);
		 }
	 }
	 
	 public void insertArtistEventByDate(ArtistEvent artistEvent){
		 // TODO: Add this sorted - but it should be already...
		 this.artistEventList.add(artistEvent);
	 }
	 
	 public double distanceTo(){
		 eventLocation.setLatitude(new Double(this.latitudeString));
		 eventLocation.setLongitude(new Double(longitudeString));
		 distance = myLocation.distanceTo(eventLocation);
		 //Log.i("DISTANCE", ""+distance);
		 return distance;
	 }
	 
	 public void resetList(){
		 artistEventList.clear();
		 artist = null;
	 }
}
