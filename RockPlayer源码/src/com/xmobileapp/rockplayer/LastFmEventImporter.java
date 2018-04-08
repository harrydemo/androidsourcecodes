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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.xmobileapp.rockplayer.R;
import org.apache.http.client.methods.HttpGet;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.xmobileapp.rockplayer.utils.RockOnPreferenceManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.app.Activity;

public class LastFmEventImporter{
	
	private final int MAX_EVENT_LIST_SIZE = 200;

	/*********************************
	 * 
	 * Definitions
	 *
	 *********************************/
	private final String LAST_FM_API_URL = 
		"http://ws.audioscrobbler.com/2.0/?method=artist.getevents&api_key=16b888d61aa3d51621f5d6d8a7396784";
	
	//&artist=Cher
	
	/*********************************
	 * 
	 * Global Vars
	 * 
	 *********************************/
	Context context;
	Cursor	artistCursor;
	double MIN_UPDATE_INTVL = 0;
	double SPREAD_INTVL = 0;
	LinkedList<ArtistEvent> artistEventList = new LinkedList<ArtistEvent>();
	EventLinkedListAdapter eventLinkedListAdapter = null;
	Location myLocation = null;
	double concertRadius;
	
	/*********************************
	 * 
	 * Constructor
	 * @param context
	 * 
	 *********************************/
	public LastFmEventImporter(Context context){
		this.context = context;
		
		Log.i("LASTFMEVENT", "creating-------------------------");
		/*
		 * Check for Internet Connection (Through whichever interface)
		 */
		ConnectivityManager connManager = (ConnectivityManager) 
											((RockPlayer) context).getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = connManager.getActiveNetworkInfo();
		/******* EMULATOR HACK - false condition needs to be removed *****/
		//if (false && (netInfo == null || !netInfo.isConnected())){
		if ((netInfo == null || !netInfo.isConnected())){
			Bundle data = new Bundle();
			data.putString("info", "No Internet Connection");
			Message msg = new Message();
			msg.setData(data);
			((RockPlayer) context).analyseConcertInfoHandler.sendMessage(msg);
			return;
		}
		
		/*
		 * Get location
		 */
		MIN_UPDATE_INTVL = 5*24*60*60*1000; // 5 days
		SPREAD_INTVL = 21*24*60*60*1000; // 21 days;
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		LocationManager locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		if(locManager.getBestProvider(criteria, true) != null)
			myLocation = locManager.getLastKnownLocation(locManager.getBestProvider(criteria, true));
		else {
			myLocation = new Location("gps");
			myLocation.setLatitude(47.100301);
			myLocation.setLongitude(-119.982465);
		}
		
        /*
         * Get preferred distance
         */
//		SharedPreferences prefs = ((Filex) context).getSharedPreferences(((Filex) context).PREFS_NAME, 0);
        RockOnPreferenceManager prefs = new RockOnPreferenceManager(((RockPlayer) context).FILEX_PREFERENCES_PATH);
		concertRadius = prefs.getLong("ConcertRadius", (long) (((RockPlayer) context).CONCERT_RADIUS_DEFAULT));

		
		//myLocation =  locManager.getLastKnownLocation(locManager.getBestProvider(Criteria.POWER_LOW, true));
		
//		try {
//			getArtistEvents();
//		} catch (SAXException e) {
//			e.printStackTrace();
//		} catch (ParserConfigurationException e) {
//			e.printStackTrace();
//		}
	}
	
	/*********************************
	 * 
	 * Get Artist Events
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 *
	 *********************************/
	public void getArtistEvents() throws SAXException, ParserConfigurationException{
		/*
		 * Initialize Artist Cursor
		 */
    	artistCursor = ((RockPlayer) context).contentResolver.query(
    			MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
    			((RockPlayer) context).ARTIST_COLS, // we should minimize the number of columns
				null,	// all albums 
				null,   // parameters to the previous parameter - which is null also 
				null	// sort order, SQLite-like
				);
    	
    	/*
    	 * Declare & Initialize some vars
    	 */
    	String artistName 					= null;
    	String artistNameFiltered			= null;
    	String artistConcertFileName		= null;
    	SAXParserFactory saxParserFactory	= SAXParserFactory.newInstance();
        SAXParser saxParser 				= saxParserFactory.newSAXParser();
        XMLReader xmlReader 				= saxParser.getXMLReader();
        XMLArtistEventHandler xmlHandler 	= new XMLArtistEventHandler();
        xmlHandler.myLocation				= this.myLocation;
        xmlReader.setContentHandler(xmlHandler);
     
        /*
         * Set Distance Limit
         */
        xmlHandler.MAX_DISTANCE = this.concertRadius;
		
        /*
         * Loop through the artists
         */
        artistCursor.moveToFirst();
        for(int i=0; i<artistCursor.getCount(); i++){
        	/*
        	 * Get artist name
        	 */
        	artistName = artistCursor.getString(
        					artistCursor.getColumnIndex(
        						MediaStore.Audio.Artists.ARTIST));
        	if(artistName.equals("<unknown>")){
    			artistCursor.moveToNext();
            	continue;
        	}
        	artistNameFiltered = filterString(artistName);
        	artistConcertFileName = ((RockPlayer) context).FILEX_CONCERT_PATH + validateFileName(artistName); 
        	
        	/*
        	 * UI feedback
        	 */
        	Bundle data = new Bundle();
        	data.putString("info", artistName);
        	Message msg = new Message();
        	msg.setData(data);
        	((RockPlayer) context).analyseConcertInfoHandler.sendMessage(msg);
        	
        	/*
        	 * If we dont have yet or info is too old, update the concert info of this artist
        	 */
        	if(hasConcertInfo(artistName) == false || concertInfoNeedsUpdate(artistName) == true){
        		Log.i("INET", "Getting concert info from LastFM");
        		if(hasConcertInfo(artistName) == false)
            		Log.i("INET", "Because there is no concert info yet");
        		if(concertInfoNeedsUpdate(artistName) == true)
            		Log.i("INET", "Because Info is too old");

        		File artistConcertFile = new File(artistConcertFileName);
        		if(!artistConcertFile.exists()){
        			try {
						artistConcertFile.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
        		}
				URL lastFmApiRequest;
				try {
					lastFmApiRequest = new URL(this.LAST_FM_API_URL+
											"&artist="+URLEncoder.encode(artistNameFiltered));
					BufferedInputStream bufferedURLStream =
										new BufferedInputStream(
												lastFmApiRequest.openStream());
					BufferedOutputStream bufferedFileWriter = 
										new BufferedOutputStream(new FileOutputStream(artistConcertFile));
					
					byte[] buf=new byte[1024];
				    int len;
				    while((len=bufferedURLStream.read(buf)) >= 0)
				    	bufferedFileWriter.write(buf,0,len);
				    bufferedURLStream.close();
				    bufferedFileWriter.close();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
        	/*
        	 * get event list from cached XML files
        	 */
        	File artistConcertFile = new File(artistConcertFileName);
    		if(artistConcertFile.exists() && artistConcertFile.length() > 0){
    			try {
					BufferedReader xmlFileReader = new BufferedReader(
														new InputStreamReader(
															new FileInputStream(artistConcertFile)));
					xmlHandler.resetList();
					xmlHandler.artist = artistName;
					xmlReader.parse(new InputSource(xmlFileReader));
					insertListInListByDate(xmlHandler.artistEventList);
    			} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (SAXException e){
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
    		}
    		
			artistCursor.moveToNext();
        }

        /*
         * Debug
         */
        ArtistEvent artistEventDebug = null;
        if(!artistEventList.isEmpty()){
	        artistEventList.getFirst();
	        ListIterator<ArtistEvent> listIterator = artistEventList.listIterator(0);
	        for(artistEventDebug = listIterator.next() ;listIterator.hasNext() ==  true; artistEventDebug = listIterator.next()){
	        	if(artistEventDebug != null)
	        		Log.i("DBG", artistEventDebug.date+" "+artistEventDebug.city);
	        	//	Log.i("DBG", artistEventDebug.date+" "+artistEventDebug.city+" "+artistEventDebug.artist);
	        	else
	        		Log.i("DBG", "NULL");
	        }
        }
        /*
         * Update Adapter
         */
        eventLinkedListAdapter = new EventLinkedListAdapter(context, R.layout.eventlist_item, artistEventList);
        if(eventLinkedListAdapter == null)
        	Log.i("NULL","NULL");
        ((RockPlayer) context).updateEventListHandler.sendEmptyMessage(0);
        
		/*
		 * Give feedback to the user
		 */
		Bundle data = new Bundle();
		Message msg = new Message();
    	data.putString("info", "Done!");
		msg.setData(data);
		((RockPlayer) context).analyseConcertInfoHandler.sendMessage(msg);
	}
    
	/*********************************************
	 * 
	 * hasConcertInfo
	 * @param artistName
	 * @return
	 * 
	 *********************************************/
	public boolean hasConcertInfo(String artistName){
    	String artistConcertFileName = ((RockPlayer) context).FILEX_CONCERT_PATH + validateFileName(artistName); 
    	File artistConcertFile = new File(artistConcertFileName);
    	if(artistConcertFile.exists())
    		return true;
    	else
    		return false;
	}
	
	/**********************************************
	 * 
	 * concertInfoNeedsUpdate
	 * @param artistName
	 * @return
	 *
	 **********************************************/
	public boolean concertInfoNeedsUpdate(String artistName){
    	String artistConcertFileName = ((RockPlayer) context).FILEX_CONCERT_PATH + validateFileName(artistName);
    	File artistConcertFile = new File(artistConcertFileName);
    	if(artistConcertFile.exists()){
    		Random randomNumber = new Random();
    		double threshold = Math.abs(randomNumber.nextGaussian()) * SPREAD_INTVL + MIN_UPDATE_INTVL;
    		Log.i("UPDATE", ""+System.currentTimeMillis()+"-"+artistConcertFile.lastModified()+">"+ threshold);
    		if(System.currentTimeMillis()-artistConcertFile.lastModified() > threshold)
    			return true;
    		else
    			return false;
    		
    	} else {
    		Log.i("UPDATE", "File does not exist yet");
    		return true;
    	}
	}
	
	/**********************************************
	 * 
	 * insertListInListByDate
	 * @param singleArtistEventList
	 * 
	 **********************************************/
	public void insertListInListByDate(LinkedList<ArtistEvent> singleArtistEventList){
		/*
		 * If this artist List is empty just return
		 */
		if(singleArtistEventList.isEmpty())
			return;
		
		/*
		 * If the big list is empty just add this one to it
		 */
		if(artistEventList.isEmpty()){
			artistEventList.addAll(singleArtistEventList);
			return;
		}
		
		/*
		 * Insert the items (normal case)
		 */
		ListIterator<ArtistEvent> artistEventListIterator = 
			artistEventList.listIterator(0);
		ListIterator<ArtistEvent> singleArtistEventListIterator =
			singleArtistEventList.listIterator(0);
		ArtistEvent artistEvent;
		ArtistEvent singleArtistEvent;
		while(singleArtistEventListIterator.hasNext()){
			/*
			 * Not yet at the end of the big list
			 */
			if(artistEventListIterator.hasNext()){
				singleArtistEvent = singleArtistEventListIterator.next();
				artistEvent = artistEventListIterator.next();
				while(singleArtistEvent.dateInMillis > artistEvent.dateInMillis){
					if(artistEventListIterator.hasNext())
						artistEvent = artistEventListIterator.next();
					else{
						if(artistEventListIterator.previousIndex() >= MAX_EVENT_LIST_SIZE){
							return;
						} else {
							break; // TODO: add missing items to the big list
						}
					}
				}
				artistEventListIterator.previous();
				artistEventListIterator.add(singleArtistEvent);
			}
			/*
			 * At the end of the big list (but not of the 'small' list
			 */
			else {
				if(artistEventListIterator.previousIndex() >= MAX_EVENT_LIST_SIZE)
					return;
				singleArtistEvent = singleArtistEventListIterator.next();
				artistEventListIterator.add(singleArtistEvent);
				artistEventListIterator.next();
			}
		}
		
		/*
		 * Keep the list size in check
		 */
		while(artistEventList.size() > MAX_EVENT_LIST_SIZE)
			artistEventList.removeLast();
	}
	
	/*********************************************
	 * 
	 * insertElementInFullList
	 * 
	 *********************************************/
	public void insertElementInFullList(ArtistEvent artistEvent){
		/*
		 * First Element
		 */
		if(artistEventList.isEmpty())
			artistEventList.add(artistEvent);
		else {
			Log.i("STARTCMP", "starting");
			ArtistEvent	artistEventItem;
			ListIterator<ArtistEvent> artistEventListIterator = 
				artistEventList.listIterator();
			while(artistEventListIterator.hasNext()){
				artistEventItem = artistEventListIterator.next();
				Log.i("CMPTIME", artistEvent.dateInMillis+"<"+artistEventItem.dateInMillis);
				if(artistEvent.dateInMillis < artistEventItem.dateInMillis){
					artistEventList.add(artistEventListIterator.nextIndex()-1, artistEvent);
					return;
				}
			}
			artistEventList.add(artistEventList.size(), artistEvent);
		}
	}
//		/*
//		 * initialize iterators
//		 */
//		ListIterator<ArtistEvent> singleArtistEventListIterator = singleArtistEventList.listIterator();
//		ListIterator<ArtistEvent> artistEventListIterator = this.artistEventList.listIterator();
//		
//		/*
//		 * if the full list of concerts is still empty
//		 */
//		if(artistEventList.isEmpty()){
//			artistEventList.add(singleArtistEventListIterator.next());
//			if(singleArtistEventListIterator.hasNext() == false)
//				return;
//		}
//		
//		/*
//		 * While there are elements in the main list go through it 
//		 */
//		ArtistEvent	artistEvent;
//		ArtistEvent singleArtistEvent;
//		singleArtistEvent = singleArtistEventListIterator.next();
//		while(artistEventListIterator.hasNext()){
//			artistEvent = artistEventListIterator.next();
//			/*
//			 * Check if we have elements to insert here
//			 */
//			while(singleArtistEvent.dateInMillis < artistEvent.dateInMillis){
//				artistEventListIterator.add(singleArtistEvent);
//				//Log.i("EVTADD", "adding concert from "+singleArtistEvent.artist+" at position "+(artistEventListIterator.nextIndex()-1));
//				if(singleArtistEventListIterator.hasNext()){
//					singleArtistEvent = singleArtistEventListIterator.next();	
//				} else {
//					/*
//					 * No more concerts to insert from this artist
//					 */
//					return;
//				}
//			}
//		}
//		/*
//		 * Reached end of full list of concerts
//		 * we need to append the concerts of this artist 
//		 * that have not been added yet
//		 */
//		artistEventListIterator.add(singleArtistEvent);
//		while(singleArtistEventListIterator.hasNext()){
//			singleArtistEvent = singleArtistEventListIterator.next();
//			artistEventListIterator.add(singleArtistEvent);
//		}
//	}
//		/*
//		 * check if this artist event list is not empty
//		 */
//		if(singleArtistEventList.isEmpty() == true)
//			return;
//		
////		if(artistEventList.isEmpty() == false)
////			artistEventList.getFirst();
////			artistEventList.listIterator(location)
//		ListIterator<ArtistEvent> artistEventListIterator = this.artistEventList.listIterator(0);
//		ListIterator<ArtistEvent> singleArtistEventListIterator = singleArtistEventList.listIterator(0);
//		int i = 0;
//
//		ArtistEvent	singleArtistEvent = singleArtistEventListIterator.next();
//		ArtistEvent artistEvent = null;
//		/*
//		 * main list is still empty
//		 */
//		if(artistEventList.isEmpty() == true){
//			artistEventListIterator.add(singleArtistEvent);
//			artistEvent = artistEventListIterator.previous();
//			singleArtistEvent = singleArtistEventListIterator.next();
//		} 
//		/*
//		 * main list is already initialized
//		 */
//		else {
//			artistEvent = artistEventListIterator.next();
//		}
//
//		/*
//		 * Insert new results in the main list
//		 */
//		String previousCity = "";
//		while(singleArtistEventListIterator.hasNext()){
//			if(singleArtistEvent.city == previousCity){
//				singleArtistEventListIterator.next();
//				continue;
//			}
//			previousCity = singleArtistEvent.city;
//			
//			/*
//			 * Look for the place in the full list based on date
//			 */
//			//while( dateAfter(singleArtistEvent.date, artistEvent.date) ){
//			while(singleArtistEvent.dateInMillis > artistEvent.dateInMillis){
//				if(artistEventListIterator.hasNext() == true)
//					artistEvent = artistEventListIterator.next();
//				else
//					break;
//			}
//			Log.i("ADD", "Adding item to list in position "+artistEventListIterator.nextIndex()+" "+singleArtistEvent.dateInMillis);
//			artistEventListIterator.add(singleArtistEvent);
//			singleArtistEvent = singleArtistEventListIterator.next();
//			Log.i("eVNT", "Added event in "+artistEvent.city);
//		}
	
	/********************************************
	 * 
	 * dateAfter
	 * 	method for comparing string dates
	 *  date format Sun, 06 Mar 2008
	 ********************************************/
	public boolean dateAfter(String varDateString, String fixedDateString){
		try {
			DateFormat dateFormatter = new SimpleDateFormat("E, dd MMM yyyy");
			//Log.i("DATE_", varDateString+" "+fixedDateString);
			Date varDate = dateFormatter.parse(varDateString);
			Date fixedDate = dateFormatter.parse(fixedDateString);
			if(varDate.after(fixedDate)){
				return true;
			} else {
				return false;
			}
			//Log.i("DATE", varDate.toString());
			//Log.i("DATE", fixedDate.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}
    	/*
    	 * Loop through the albums
    	 */
//    	albumCursor.moveToFirst();
//					 * Get album URL from Last.FM
//					 */
//        			String artistNameFiltered = filterString(artistName);
//        			String albumNameFiltered = filterString(albumName);
//					URL lastFmApiRequest = new URL(this.LAST_FM_API_URL+
//															"&artist="+URLEncoder.encode(artistNameFiltered)+
//															"&album="+URLEncoder.encode(albumNameFiltered));
//					BufferedReader in = 
//	                    new BufferedReader(new InputStreamReader(lastFmApiRequest.
//	                    											openStream()));
//					xmlReader.parse(new InputSource(in));
//					
//					if(xmlHandler.xlargeAlbumArt != null){
//						albumArtURL = xmlHandler.xlargeAlbumArt;
//					} else if (xmlHandler.largeAlbumArt != null) {
//						albumArtURL = xmlHandler.largeAlbumArt;
//					} else if (xmlHandler.mediumAlbumArt != null) {
//						albumArtURL = xmlHandler.mediumAlbumArt;
//					}
//					
//					/*
//					 * No Album Art available - get Artist art instead 
//					 */
//					if(albumArtURL == null){
//						albumCursor.moveToNext();
//						continue;
//					}
//					
//					/*
//					 * Retreive Album
//					 */
//					String fileName = ((Filex)context).FILEX_ALBUM_ART_PATH+
//										artistName+
//										" - "+
//										albumName+
//										".jpg";
//					validateFileName(fileName);
//					File albumArtFile = new File(fileName);
//					albumArtFile.createNewFile();
//					FileOutputStream albumArtFileStream = new FileOutputStream(albumArtFile);
//					
//					//URL albumArt = new URL(URLEncoder.encode(albumArtURL));
//					URL albumArt = new URL(albumArtURL);
//					InputStream albumArtURLStream = albumArt.openStream();
//					
//					byte buf[]=new byte[1024];
//				    int len;
//				    while((len=albumArtURLStream.read(buf))>=0)
//				    	albumArtFileStream.write(buf,0,len);
//				    
//				    albumArtFileStream.close();
//				    albumArtURLStream.close();
//				    
//					/*
//					 * reset xml handler
//					 */
//					xmlHandler.smallAlbumArt = null;
//					xmlHandler.mediumAlbumArt= null;
//					xmlHandler.largeAlbumArt = null;
//					xmlHandler.xlargeAlbumArt = null;
//
//	               
//				} catch (MalformedURLException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				} catch (SAXException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}        		 
//    		}
//		    /*
//		     * Create small album art
//		     */
//		    createSmallAlbumArt(artistName, albumName);
//		    
//    		albumCursor.moveToNext();
//    	}
//	}
//
//	/*********************************
//	 * 
//	 * albumHasArt
//	 *
//	 *********************************/
//	private String getAlbumArtPath(String artistName, String albumName){
//		/*
//		 * Check Art in the DB
//		 */
//    	String albumCoverPath = albumCursor.getString(
//									albumCursor.getColumnIndex(
//											MediaStore.Audio.Albums.ALBUM_ART));
//
//    	/*
//    	 * Check if we have downloaded it before
//    	 */
//    	if(albumCoverPath == null){
//    		String path = ((Filex)context).FILEX_ALBUM_ART_PATH+
//							artistName+
//							" - "+
//							albumName+
//							".jpg";
//    		validateFileName(path);
//    		File albumCoverFilePath = new File(path);
//    		if(albumCoverFilePath.exists() && albumCoverFilePath.length() > 0){
//    			albumCoverPath = albumCoverFilePath.getAbsolutePath();
//    		}
//    	}
//    	
//    	/*
//    	 * If both checks above have failed return false
//    	 */
//    	return albumCoverPath;
//	}
//	
//	/*******************************
//	 * 
//	 * createSmallAlbumArt
//	 * 
//	 *******************************/
//	private void createSmallAlbumArt(String artistName, String albumName){
//	    /*
//	     * If small art already exists just return
//	     */
//		String smallArtFileName = ((Filex)context).FILEX_SMALL_ALBUM_ART_PATH+
//									validateFileName(artistName)+
//	    							" - "+
//	    							validateFileName(albumName)+
//	    							".jpg";
//		Log.i("CREATE", smallArtFileName);
//	    File smallAlbumArtFile = new File(smallArtFileName);
//		if(smallAlbumArtFile.exists() && smallAlbumArtFile.length() > 0){
//			return;
//		}
//
//		/*
//		 * Get path for existing Album art
//		 */
//		String albumArtPath = getAlbumArtPath(artistName, albumName);
//
//		/*
//		 * If album has art file create the small thumb from it 
//		 * otherwise do it from the cdcover resource
//		 */
//		Bitmap smallBitmap = null;
//		if(albumArtPath != null){
//			Log.i("SCALEBM", albumArtPath);
//			smallBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(albumArtPath),
//													120, 120, false);
//		}
//		else
//			smallBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.albumart_mp_unknown);		
//	    try {
//			smallAlbumArtFile.createNewFile();
//		    FileOutputStream smallAlbumArtFileStream;
//			smallAlbumArtFileStream = new FileOutputStream(smallAlbumArtFile);
//		    smallBitmap.compress(Bitmap.CompressFormat.JPEG, 90, smallAlbumArtFileStream);
//			smallAlbumArtFileStream.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
	/*******************************
	 * 
	 * filterString
	 * 
	 *******************************/
	private String filterString(String original){
		String filtered = original;
		
		try{
			/* Remove anything within () or []*/
			int init = original.indexOf('(');
			int stop = original.indexOf(')', init);
			if(init != -1 && stop != -1){
				String addInfo= original.substring(init, stop+1);
				filtered = original.substring(0, init) +
							original.substring(stop+1, original.length());
				//filtered = original.replaceAll(addInfo, "");
			}
			init = filtered.indexOf('[');
			stop = filtered.indexOf(']', init);
			if(init != -1 && stop != -1){
				String addInfo= filtered.substring(init, stop+1);
				filtered = filtered.substring(0, init) +
							filtered.substring(stop+1, original.length());
				//filtered = original.replaceAll(addInfo, "");
			}
			
			/* Remove common album name garbage */
			filtered = filtered.replace("CD1", "");
			filtered = filtered.replace("CD2", "");
			filtered = filtered.replace("cd1", "");
			filtered = filtered.replace("cd2", "");
			
			/* Remove strange characters */
			filtered = filtered.replace(',', ' ');
			filtered = filtered.replace('.', ' ');
			filtered = filtered.replace('+', ' ');
			filtered = filtered.replace('/', ' ');
			filtered = filtered.replace('<', ' ');
			filtered = filtered.replace('>', ' ');
			filtered = filtered.replace('?', ' ');
			filtered = filtered.replace('!', ' ');
			filtered = filtered.replace('|', ' ');
			filtered = filtered.replace('#', ' ');
			filtered = filtered.replace('&', ' ');
			filtered = filtered.replace('%', ' ');
			
			Log.i("filter", filtered);
		} catch(Exception e){
			e.printStackTrace();
		}
		
		return filtered;
	}
	
	/*********************************
	 * 
	 * ValidateFilename
	 * 
	 *********************************/
	private String validateFileName(String fileName) {
		if(fileName == null)
			return "";
		fileName = fileName.replace('/', '_');
		fileName = fileName.replace('<', '_');
		fileName = fileName.replace('>', '_');
		fileName = fileName.replace(':', '_');
		fileName = fileName.replace('\'', '_');
		fileName = fileName.replace('?', '_');
		fileName = fileName.replace('"', '_');
		fileName = fileName.replace('|', '_');
		return fileName;
	}
}

