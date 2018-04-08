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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpClientConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.DefaultHttpClientConnection;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.AbstractHttpParams;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.DefaultedHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.xmobileapp.rockplayer.utils.RockOnPreferenceManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class LastFmAlbumArtImporter{
	
	/*********************************
	 * 
	 * Definitions
	 *
	 *********************************/
	private final String	LAST_FM_ALBUM_GETINFO_URL = "http://ws.audioscrobbler.com/2.0/?method=album.getinfo&api_key=16b888d61aa3d51621f5d6d8a7396784";
	private final String	LAST_FM_ALBUM_SEARCH_URL = "http://ws.audioscrobbler.com/2.0/?method=album.search&api_key=16b888d61aa3d51621f5d6d8a7396784";
	private final String	LAST_FM_ARTIST_GETINFO_URL = "http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&api_key=16b888d61aa3d51621f5d6d8a7396784";
	private final String	LAST_FM_ARTIST_SEARCH_URL = "http://ws.audioscrobbler.com/2.0/?method=artist.search&api_key=16b888d61aa3d51621f5d6d8a7396784";
	
	private final String	GOOGLE_IMAGES_BASE_URL = "http://images.google.com/";
	private final String	GOOGLE_IMAGES_SEARCH_URL = GOOGLE_IMAGES_BASE_URL + "images?q=";
//	private final String	GOOGLE_IMAGES_SEARCH_URL = GOOGLE_IMAGES_BASE_URL + "images?imgw=500&imgh=500&q=";
	//gbv=2&hl=en&sa=G&imgsz=&
	
    String					FILEX_FILENAME_EXTENSION = "";

	//&artist=Cher&album=Believe
	
	/*********************************
	 * 
	 * Global Vars
	 * 
	 *********************************/
	Context context;
	Cursor	albumCursor;
	boolean	USE_LAST_FM = false;
	boolean USE_GOOGLE_IMAGES = true;
	
	/*********************************
	 * 
	 * Constructor
	 * @param context
	 * 
	 *********************************/
	public LastFmAlbumArtImporter(Context context){
		this.context = context;
		try {
			/*
			 * Check for Internet Connection (Through whichever interface)
			 */
			ConnectivityManager connManager = (ConnectivityManager) 
												((RockPlayer) context).getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = connManager.getActiveNetworkInfo();
			/******* EMULATOR HACK - false condition needs to be removed *****/
			//if (false && (netInfo == null || !netInfo.isConnected())){
			if ((netInfo == null || netInfo.isConnected() == false)){
				Bundle data = new Bundle();
				data.putString("info", "No Internet Connection");
				Message msg = new Message();
				msg.setData(data);
				((RockPlayer) context).getAlbumArtHandler.sendMessage(msg);
				return;
			}
			
			/*
			 * Import Album Art from Last FM
			 */
			//getAlbumArt();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*********************************
	 * 
	 * Get AlbumArt
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 *
	 *********************************/
	public void getAlbumArt() throws SAXException, ParserConfigurationException{
		/*
		 * Initialize Album Cursor
		 */
    	albumCursor = ((RockPlayer) context).contentResolver.query(
    			MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
    			((RockPlayer) context).ALBUM_COLS, // we should minimize the number of columns
				null,	// all albums 
				null,   // parameters to the previous parameter - which is null also 
				null	// sort order, SQLite-like
				);
    	
    	/*
    	 * Declare & Initialize some vars
    	 */
    	String artistName 					= null;
    	String albumName 					= null;
    	SAXParserFactory saxParserFactory	= SAXParserFactory.newInstance();
        SAXParser saxParser 				= saxParserFactory.newSAXParser();
        XMLReader xmlReader 				= saxParser.getXMLReader();
        XMLAlbumArtHandler xmlHandler 		= new XMLAlbumArtHandler();
//        XMLGoogleAlbumArtHandler xmlGoogleHandler = new XMLGoogleAlbumArtHandler();
                
		/*
		 * Give feedback to the user
		 */
		Bundle data = new Bundle();
		Message msg = new Message();
		data.putString("info", "Looking for missing art...");
		msg.setData(data);
		((RockPlayer) context).getAlbumArtHandler.sendMessage(msg);
		
    	/*
    	 * Loop through the albums
    	 */
    	albumCursor.moveToFirst();
    	for(int i=0; i<albumCursor.getCount(); i++){
    		System.gc();
    		/*
    		 * Get Album Details
    		 */
    		artistName = albumCursor.getString(
    						albumCursor.getColumnIndex(
    								MediaStore.Audio.Albums.ARTIST));
    		albumName = albumCursor.getString(
							albumCursor.getColumnIndex(
									MediaStore.Audio.Albums.ALBUM));

    		
    		/*
    		 * If no Art is available fetch it
    		 */
    		if(getAlbumArtPath(artistName, albumName) == null){
        		Log.i("LastFM", "Album with no Art "+albumName);
        		try{
        			if((artistName.equals("<unknown>") && albumName.equals("<unknown>"))){
        				Log.i("ALBUMART", "Unknown album");
        				albumCursor.moveToNext();
        				continue;
        			}
        		} catch (Exception e) {
    				Log.i("ALBUMART", "Null album or artist");
    				albumCursor.moveToNext();
    				continue;        			
        		}
        			
    			/*
    			 * Give feedback to the user
    			 */
        		data = new Bundle();
        		msg = new Message();
    			data.putString("info", albumName);
    			msg.setData(data);
    			((RockPlayer) context).getAlbumArtHandler.sendMessage(msg);
    			
				String 	albumArtURL = null;
        		try {
					/*
					 * Get album URL from Last.FM
					 */
        			String artistNameFiltered = filterString(artistName);
        			String albumNameFiltered = filterString(albumName);
					

        			if(USE_GOOGLE_IMAGES){
//        				xmlReader.setContentHandler(xmlGoogleHandler);
        		                				
        				URL googleImagesRequest = new URL(
        						this.GOOGLE_IMAGES_SEARCH_URL+
								URLEncoder.encode(artistNameFiltered)+
								"+"+
								URLEncoder.encode(albumNameFiltered));
//        				Log.i("GOOGLEIMAGES", googleImagesRequest.toString());
						
//        				DefaultHttpClientConnection httpCon = createGoogleImageConnection(
//        						googleImagesRequest.toString());
        				/*
        				 * retreive URL
        				 */
    					BasicHttpParams params = new BasicHttpParams();
        				HttpConnectionParams.setConnectionTimeout(params, 10000);
        				DefaultHttpClient httpClient = new DefaultHttpClient();
        				
        		        // Get cookies from the login page (not the address same of the form post)
        		         HttpGet httpGet = new HttpGet(googleImagesRequest.toString());
        		         
        		         HttpResponse response; 
        				
						try{
							/*
							 * Get the page
							 */
        					response = httpClient.execute(httpGet);
        		            HttpEntity entity = response.getEntity();
							BufferedReader in = 
								new BufferedReader(new InputStreamReader(
										entity.getContent()));
							
							/*
							 * Parse 1st existing image on the result page
							 */
							String line;
							int idxStart = 0;
							int idxStop;
							do{
								line = in.readLine();
								if(line != null){
//									Log.i("GIMAGES", line);
									if(line.startsWith("<table")){
										boolean found = false;
										int tries = 0;
										while(!found){
											tries++;
											if(tries > 12)
												break;
											idxStart = line.indexOf("<a href=", idxStart);
								
											if(idxStart == -1){
												line = in.readLine();
												if(line == null)
													break;
												continue;
											}
											
											idxStart = line.indexOf("http://", idxStart);
											idxStop = line.indexOf("&imgrefurl=", idxStart);
											albumArtURL = line.substring(idxStart, idxStop);
											Log.i("GIMAGE", line.substring(idxStart, idxStop));
											
											try{
												//URL albumArt = new URL(URLEncoder.encode(albumArtURL));
//												URL albumArt = new URL(albumArtURL);
//												InputStream albumArtURLStream = albumArt.openStream();
//												albumArtURLStream.close();
												if(albumArtURL != null){
													if(createAlbumArt(artistName, albumName, albumArtURL) == null){
														albumArtURL = null;
														found = false;
														Log.i("GIMAGES" , "createAlbumArt FAIL");
													} else {
														found = true;
														Log.i("GIMAGES" , "createAlbumArt WIN");
													}
												} else {
													albumArtURL = null;
													found = false;
													Log.i("GIMAGES" , "albumArt URL FAIL!");
												}					
											} catch (Exception e){
												e.printStackTrace();
												albumArtURL = null;
												found = false;
											}
										}
										break;
									}
								}
							} while(line != null);
								
//							xmlReader.parse(new InputSource(in));
							
							entity.consumeContent();
							
//							for(int j = 0; j < xmlGoogleHandler.MAX_IMAGES; j++){
//								if(xmlGoogleHandler.albumArtUrl[j] != null){
//									albumArtURL = xmlGoogleHandler.albumArtUrl[j];
//									break;
//								}
//							}
							
							/*
							 * No luck with the duck
							 */
//							if(albumArtURL == null){
//								Log.i("GOOGLEIMAGES", "Absolutely no luck");
//								// mark this as a problematic album... 
//								// so we dont refetch it all the time
//							    createSmallAlbumArt(artistName, albumName, false);
//								albumCursor.moveToNext();
//								continue;
//							} else {
//								Log.i("GOOGLEIMAGES", albumArtURL);
//							}

							
							
							/*
							 * Clear up the Handler
							 */
//							xmlGoogleHandler.clear();
							
						} catch (MalformedURLException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (Exception e){
							e.printStackTrace();
						}
//						
//						/*
//						* No Album Art available
//						*  1. try going by the album name
//						*  2. get some artist pic and thats it
//						*/
//						if(albumArtURL == null){
//						Log.i("LASTFM", "Could not get album art immediately");
//						Log.i("LASTFM", "Trying sole album search");
//						
//						albumArtURL = getAlbumArtByAlbumName(albumName, artistName);
//						if(albumArtURL == null){
//						Log.i("LASTFM", "Trying to get artist Art");
//						albumArtURL = getArtistArt(artistName);
//						}
//						/*
//						* No luck with the duck
//						*/
//						if(albumArtURL == null){
//						Log.i("LASTFM", "Absolutely no luck");
//						// mark this as a problematic album... 
//						// so we dont refetch it all the time
//						createSmallAlbumArt(artistName, albumName, false);
//						albumCursor.moveToNext();
//						continue;
//						}
//						}
        			} 
        			
        			/*
        			 * If google images failed try last.fm
        			 */
        			if(albumArtURL == null){
        				xmlReader.setContentHandler(xmlHandler);
        				
        				URL lastFmApiRequest = new URL(this.LAST_FM_ALBUM_GETINFO_URL+
	        													"&artist="+URLEncoder.encode(artistNameFiltered)+
																"&album="+URLEncoder.encode(albumNameFiltered));
						try{
							BufferedReader in = 
			                    new BufferedReader(new InputStreamReader(lastFmApiRequest.
			                    											openStream()));
							xmlReader.parse(new InputSource(in));
							
							if(xmlHandler.xlargeAlbumArt != null){
								albumArtURL = xmlHandler.xlargeAlbumArt;
							} else if (xmlHandler.largeAlbumArt != null) {
								albumArtURL = xmlHandler.largeAlbumArt;
							} else if (xmlHandler.mediumAlbumArt != null) {
								albumArtURL = xmlHandler.mediumAlbumArt;
							}
						} catch (MalformedURLException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} 
							
						/*
						 * No Album Art available
						 *  1. try going by the album name
						 *  2. get some artist pic and thats it
						 */
						if(albumArtURL == null){
							Log.i("LASTFM", "Could not get album art immediately");
							Log.i("LASTFM", "Trying sole album search");
	
							albumArtURL = getAlbumArtByAlbumName(albumName, artistName);
							if(albumArtURL == null){
								Log.i("LASTFM", "Trying to get artist Art");
								albumArtURL = getArtistArt(artistName);
							}
							/*
							 * No luck with the duck
							 */
							if(albumArtURL == null){
								Log.i("LASTFM", "Absolutely no luck");
								// mark this as a problematic album... 
								// so we dont refetch it all the time
							    createSmallAlbumArt(artistName, albumName, false);
								albumCursor.moveToNext();
								continue;
							}
						}
						
						/* only reaches here if not FAIL */
						createAlbumArt(artistName, albumName, albumArtURL);
						
        			}

					/*
					 * reset xml handler
					 */
					xmlHandler.smallAlbumArt = null;
					xmlHandler.mediumAlbumArt= null;
					xmlHandler.largeAlbumArt = null;
					xmlHandler.xlargeAlbumArt = null;

	               
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NullPointerException e){
					e.printStackTrace();
				}
    		}
		    /*
		     * Create small album art
		     */
		    createSmallAlbumArt(artistName, albumName, true);
			
		    /*
			 * Give feedback to the user
			 */
			//Bundle data = new Bundle();
			//Message msg = new Message();
			//data.putString("info", "Creating Thumbnail");
			//msg.setData(data);
			//((Filex) context).getAlbumArtHandler.sendMessage(msg);
		    
    		albumCursor.moveToNext();
    	}
    	
		/*
		 * Give feedback to the user
		 */
		data = new Bundle();
		msg = new Message();
    	data.putString("info", "Done!");
		msg.setData(data);
		((RockPlayer) context).getAlbumArtHandler.sendMessage(msg);
    	
    	/*
    	 * Set the last import date on preferences
    	 */
//        SharedPreferences settings = ((Filex) this.context).getSharedPreferences(((Filex) this.context).PREFS_NAME, 0);
//        Editor editor = settings.edit();
//        editor.putLong("artImportDate", System.currentTimeMillis());
//        editor.commit();
        RockOnPreferenceManager settings = new RockOnPreferenceManager(((RockPlayer) context).FILEX_PREFERENCES_PATH);
        settings.putLong("artImportDate", System.currentTimeMillis());
        
        //settings.
    	//	long lastAlbumArtImportDate = settings.getLong("artImportDate", 0);
	}

	/*********************************
	 * 
	 * getGoogleSearchResponse
	 * 
	 *********************************/
	public HttpEntity getGoogleSearchResponse(String artistName, String albumName){
		try {
			/*
			 * Filter Strings
			 */
			String artistNameFiltered = filterString(artistName);
			String albumNameFiltered = filterString(albumName);
			
		            				
			URL googleImagesRequest = new URL(
					this.GOOGLE_IMAGES_SEARCH_URL+
					URLEncoder.encode(artistNameFiltered)+
					"+"+
					URLEncoder.encode(albumNameFiltered));

			/*
			 * retreive URL
			 */
			BasicHttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			DefaultHttpClient httpClient = new DefaultHttpClient();
			
	        // Get cookies from the login page (not the address same of the form post)
			HttpGet httpGet = new HttpGet(googleImagesRequest.toString());
	         
	        HttpResponse response; 
			
			try{
				/*
				 * Get the page
				 */
				response = httpClient.execute(httpGet);
	            HttpEntity entity = response.getEntity();
//				BufferedReader in = 
//					new BufferedReader(new InputStreamReader(
//							entity.getContent()));
	            return entity;
	            
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/*********************************
	 * 
	 * parseGoogleSearchResponse
	 * 
	 *********************************/
	public String parseGoogleSearchResponse(BufferedReader in, int index){
	
		try{
			Log.i("GOOGLE", "Parsing Google Search response "+index);
				/*
				 * Parse 1st existing image on the result page
				 */
				String line;
				String albumArtURL = null;
				int idxStart = 0;
				int idxStop;
				do{
					line = in.readLine();
					if(line != null){
						Log.i("GIMAGES", line);
						if(line.startsWith("<table")){
							boolean found = false;
							int tries = 0;
							while(true){
//								if(tries > 20)
//									break;
								idxStart = line.indexOf("<a href=", idxStart);
								
								if(idxStart == -1){
									line = in.readLine();
									if(line == null)
										return null;
									continue;
								}
								
								idxStart = line.indexOf("http://", idxStart);
								idxStop = line.indexOf("&imgrefurl=", idxStart);
								albumArtURL = line.substring(idxStart, idxStop);
								Log.i("GIMAGE", idxStart + " " + idxStop + line.substring(idxStart, idxStop));
								
								if(tries == index){
									return albumArtURL;
								}
								
								tries++;
							}
						}
					}
				} while(line != null);
				
//				entity.consumeContent();
						
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			
		return null; 
	}
	
	
	
	/*********************************
	 * 
	 * creatAlbumArt
	 * 
	 *********************************/
	public Bitmap createAlbumArt(String artistName, String albumName, String albumArtURL){
		String fileName = ((RockPlayer)context).FILEX_ALBUM_ART_PATH+
				validateFileName(artistName)+
				" - "+
				validateFileName(albumName)+
				FILEX_FILENAME_EXTENSION;
		validateFileName(fileName);
		File albumArtFile = new File(fileName);
		try{
			/*
			 * Retreive Album
			 */
						
			albumArtFile.createNewFile();
			FileOutputStream albumArtFileStream = new FileOutputStream(albumArtFile);
			
			/*
			 * retreive URL
			 */
			BasicHttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);
			DefaultHttpClient httpClient = new DefaultHttpClient(params);
	//		DefaultedHttpParams params = new DefaultedHttpParams(httpClient.getParams(),
	//				httpClient.getParams());
	//		httpClient.setParams(params);
	        HttpGet httpGet = new HttpGet(albumArtURL);
	        HttpResponse response;
	        HttpEntity entity ;
	        InputStream albumArtURLStream = null;
	        
	        response = httpClient.execute(httpGet);
	        entity = response.getEntity();
	//			BufferedReader in = 
	//				new BufferedReader(new InputStreamReader(
	//						entity.getContent()));
	       albumArtURLStream = entity.getContent();
			
	//		URL albumArt = new URL(albumArtURL);
	//		InputStream albumArtURLStream = albumArt.openStream();
			
	       
			byte buf[]=new byte[1024];
		    int len;
		    while((len=albumArtURLStream.read(buf))>=0)
		    	albumArtFileStream.write(buf,0,len);
		    
		    albumArtFileStream.close();
		    albumArtURLStream.close();
		    
		    /*
		     * Rescale/Crop the Bitmap
		     */
		    BitmapFactory.Options opts = new BitmapFactory.Options();
		    opts.inJustDecodeBounds = true;
		    Bitmap albumArtBitmap = BitmapFactory.decodeFile(fileName, opts);
		    if(opts.outHeight < 320 || opts.outWidth < 320)
		    	return null;
		    int MAX_DIM = 460;
		    int sampling = 1;
		    if(Math.min(opts.outWidth, opts.outHeight) > MAX_DIM){
		    	sampling = (int) Math.floor(Math.min(opts.outWidth, opts.outHeight)/MAX_DIM);
		    }
		    opts.inSampleSize = sampling;
		    opts.inJustDecodeBounds = false;
		    FileInputStream fileNameStream = new FileInputStream(fileName);
		    albumArtBitmap = BitmapFactory.decodeStream(fileNameStream, null, opts);
		    fileNameStream.close();
		    
		    if(albumArtBitmap != null){
			    int dimension = Math.min(480, Math.min(albumArtBitmap.getHeight(), albumArtBitmap.getWidth()));
			    Bitmap albumArtBitmapRescaled = Bitmap.createBitmap(albumArtBitmap, 
						(int)Math.floor((albumArtBitmap.getWidth()-dimension)/2),
						(int)Math.floor((albumArtBitmap.getHeight()-dimension)/2),
						(int)dimension,
						(int)dimension);
	
			    
			    FileOutputStream rescaledAlbumArtFileStream;
			    rescaledAlbumArtFileStream = new FileOutputStream(albumArtFile);
			    albumArtBitmapRescaled.compress(Bitmap.CompressFormat.JPEG, 96, rescaledAlbumArtFileStream);
			    rescaledAlbumArtFileStream.close();	
			    if(albumArtBitmapRescaled != null)
			    	albumArtBitmapRescaled.recycle();
			    
		    } else {
		    	albumArtFile.delete();
		    }
		    if(albumArtBitmap != null)
		    	albumArtBitmap.recycle();
		    return albumArtBitmap;
		} catch (Exception e) {
			e.printStackTrace();
			albumArtFile.delete();
			return null;
		} catch (Error err){
			err.printStackTrace();
			return null;
		}
	}
	
	/*********************************
	 * 
	 * creatAlbumArt
	 * 
	 *********************************/
	public Bitmap createAlbumArt(String artistName, String albumName, Bitmap bm){
		String fileName = ((RockPlayer)context).FILEX_ALBUM_ART_PATH+
				validateFileName(artistName)+
				" - "+
				validateFileName(albumName)+
				FILEX_FILENAME_EXTENSION;
		validateFileName(fileName);
		File albumArtFile = new File(fileName);
		
		try{
			/*
			 * Set file output
			 */
						
			albumArtFile.createNewFile();
			FileOutputStream albumArtFileStream = new FileOutputStream(albumArtFile);
			
			/*
		     * Rescale/Crop the Bitmap
		     */
			int MAX_SIZE = 480;
			float scaleFactor = (float) Math.max(
					1.0f,
					Math.min((float)bm.getWidth()/(float)MAX_SIZE, (float)bm.getHeight()/(float)MAX_SIZE));
			Log.i("CREATE", ""+scaleFactor);
		    Bitmap albumArtBitmap = Bitmap.createScaledBitmap(
		    		bm,
		    		(int)Math.round((float)bm.getWidth()/scaleFactor),
		    		(int)Math.round((float)bm.getHeight()/scaleFactor),
		    		false);
		    
		    if(albumArtBitmap != null){
			    int dimension = Math.min(480, Math.min(albumArtBitmap.getHeight(), albumArtBitmap.getWidth()));
			    Bitmap albumArtBitmapRescaled = Bitmap.createBitmap(albumArtBitmap, 
						(int)Math.floor((albumArtBitmap.getWidth()-dimension)/2),
						(int)Math.floor((albumArtBitmap.getHeight()-dimension)/2),
						(int)dimension,
						(int)dimension);
	
			    
			    FileOutputStream rescaledAlbumArtFileStream;
			    rescaledAlbumArtFileStream = new FileOutputStream(albumArtFile);
			    albumArtBitmapRescaled.compress(Bitmap.CompressFormat.JPEG, 96, rescaledAlbumArtFileStream);
			    rescaledAlbumArtFileStream.close();	
			    if(albumArtBitmapRescaled != null)
			    	albumArtBitmapRescaled.recycle();
			    
		    } else {
		    	albumArtFile.delete();
		    }
		    if(albumArtBitmap != null)
		    	albumArtBitmap.recycle();
		    return albumArtBitmap;
		} catch (Exception e) {
			e.printStackTrace();
			albumArtFile.delete();
			return null;
		} catch (Error err){
			err.printStackTrace();
			return null;
		}
		
	}
	
	/*********************************
	 * 
	 * getAlbumArtByAlbumName
	 * 
	 *********************************/
	private String getAlbumArtByAlbumName(String albumName, String artistName){
		try {
	    	SAXParserFactory saxParserFactory	= SAXParserFactory.newInstance();
	        SAXParser saxParser;
			saxParser = saxParserFactory.newSAXParser();
	        XMLReader xmlReader;
			xmlReader = saxParser.getXMLReader();
	        XMLAlbumSearchHandler xmlHandler 		= new XMLAlbumSearchHandler();
	        xmlReader.setContentHandler(xmlHandler);
			/*
			 * Get artist art from Last.FM
			 */
			String artistNameFiltered = filterString(artistName);
			String albumNameFiltered = filterString(albumName);
			URL lastFmApiRequest = new URL(this.LAST_FM_ALBUM_SEARCH_URL+
													"&album="+URLEncoder.encode(albumNameFiltered));
			BufferedReader in = 
	            new BufferedReader(new InputStreamReader(lastFmApiRequest.
	            											openStream()));
			xmlReader.parse(new InputSource(in));
			
			for(int i=0; i < xmlHandler.albumSearchList.size(); i++){
				AlbumSearch albumSearch = xmlHandler.albumSearchList.get(i);
				if(artistNameIsSimilarEnough(filterString(albumSearch.artistName), artistNameFiltered)){
					if(albumSearch.xlargeAlbumArt != null){
						return albumSearch.xlargeAlbumArt;
					} else if (albumSearch.largeAlbumArt != null) {
						return albumSearch.largeAlbumArt;
					} else if (albumSearch.mediumAlbumArt != null) {
						return albumSearch.mediumAlbumArt;
					}
				}
			}
			return null;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		} catch (SAXException e) {
			e.printStackTrace();
			return null;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*********************************
	 * 
	 * getArtistArt
	 * 
	 *********************************/
	private String getArtistArt(String artistName){
		try {
	    	SAXParserFactory saxParserFactory	= SAXParserFactory.newInstance();
	        SAXParser saxParser;
			saxParser = saxParserFactory.newSAXParser();
	        XMLReader xmlReader;
			xmlReader = saxParser.getXMLReader();
	        XMLArtistInfoHandler xmlHandler 		= new XMLArtistInfoHandler();
	        xmlReader.setContentHandler(xmlHandler);
			/*
			 * Get artist art from Last.FM
			 */
			String artistNameFiltered = filterString(artistName);
			URL lastFmApiRequest = new URL(this.LAST_FM_ARTIST_GETINFO_URL+
													"&artist="+URLEncoder.encode(artistNameFiltered));
			BufferedReader in = 
	            new BufferedReader(new InputStreamReader(lastFmApiRequest.
	            											openStream()));
			xmlReader.parse(new InputSource(in));
			
			if(xmlHandler.xlargeAlbumArt != null){
				return xmlHandler.xlargeAlbumArt;
			} else if (xmlHandler.largeAlbumArt != null) {
				return xmlHandler.largeAlbumArt;
			} else if (xmlHandler.mediumAlbumArt != null) {
				return xmlHandler.mediumAlbumArt;
			}

			return null;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		} catch (SAXException e) {
			e.printStackTrace();
			return null;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*********************************
	 * 
	 * albumHasArt
	 *
	 *********************************/
	private String getAlbumArtPath(String artistName, String albumName){
		
		String albumCoverPath = null;
		
		/*
    	 * 1. Check if we have downloaded it before
    	 */
//    	if(albumCoverPath == null){
    		String path = ((RockPlayer)context).FILEX_ALBUM_ART_PATH+
    						validateFileName(artistName)+
							" - "+
							validateFileName(albumName)+
							FILEX_FILENAME_EXTENSION;
    		File albumCoverFilePath = new File(path);
    		if(albumCoverFilePath.exists() && albumCoverFilePath.length() > 0){
    			albumCoverPath = albumCoverFilePath.getAbsolutePath();
    		}
//    	}
		
		/*
		 * 2. Check Art in the DB
		 */
    	if(albumCoverPath == null){
	    		albumCoverPath = albumCursor.getString(
	    				albumCursor.getColumnIndexOrThrow(
	    						MediaStore.Audio.Albums.ALBUM_ART));
	
	    	/* check if the embedded mp3 is valid (or big enough)*/
	    	if(albumCoverPath != null){
	    		Log.i("DBG", albumCoverPath);
		    	BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;
				Bitmap bmTmp = BitmapFactory.decodeFile(albumCoverPath, opts);
				if(opts == null || opts.outHeight < 320 || opts.outWidth < 320)
					albumCoverPath = null;
				if(bmTmp != null)
					bmTmp.recycle();
	    	}
    	}
    	
    	
    	
    	Log.i("DBG", ""+albumCoverPath);
    	
    	
    	/*
    	 * If both checks above have failed return false
    	 */
    	return albumCoverPath;
	}
	
	/*******************************
	 * 
	 * checkAlbumArtPathCustom
	 * 
	 *******************************/
	public String checkAlbumArtPathCustom(String artistName, String albumName){
    	
    	/*
    	 * Check if we have downloaded it before
    	 */
		String path = ((RockPlayer)context).FILEX_ALBUM_ART_PATH+
						validateFileName(artistName)+
						" - "+
						validateFileName(albumName)+
						FILEX_FILENAME_EXTENSION;
		File albumCoverFilePath = new File(path);
		if(albumCoverFilePath.exists() && albumCoverFilePath.length() > 0){
			return albumCoverFilePath.getAbsolutePath();
		} else {
			return null;
		}
    	
	}	
	
	/*******************************
	 * 
	 * checkAlbumArtEmbeddedSize
	 * 
	 *******************************/
	public String checkAlbumArtEmbeddedSize(String albumCoverPath){

    	/* check if the embedded mp3 is valid (or big enough)*/
    	if(albumCoverPath != null){
    		Log.i("DBG", albumCoverPath);
	    	BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			Bitmap bmTmp = BitmapFactory.decodeFile(albumCoverPath, opts);
			if(opts == null || opts.outHeight < 320 || opts.outWidth < 320)
				albumCoverPath = null;
			if(bmTmp != null)
				bmTmp.recycle();
    	}
    	
    	return albumCoverPath;
    	
	}
	
	/*******************************
	 * 
	 * createSmallAlbumArt
	 * 
	 *******************************/
	public void createSmallAlbumArt(String artistName, String albumName, boolean force){
	    /*
	     * If small art already exists just return
	     */
		String smallArtFileName = ((RockPlayer)context).FILEX_SMALL_ALBUM_ART_PATH+
									validateFileName(artistName)+
	    							" - "+
	    							validateFileName(albumName)+
	    							FILEX_FILENAME_EXTENSION;
		Log.i("CREATE", smallArtFileName);
	    File smallAlbumArtFile = new File(smallArtFileName);
		if(!force && smallAlbumArtFile.exists() && smallAlbumArtFile.length() > 0){
			return;
		}

		/*
		 * Get path for existing Album art
		 */
		String albumArtPath = getAlbumArtPath(artistName, albumName);

		/*
		 * If album has art file create the small thumb from it 
		 * otherwise do it from the cdcover resource
		 */
		Bitmap smallBitmap = null;
		if(albumArtPath != null){
			try{
				Log.i("SCALEBM", albumArtPath);
//				BitmapFactory.Options opts = new BitmapFactory.Options();
//				opts.inJustDecodeBounds = true;
//				Bitmap bmTmp = BitmapFactory.decodeFile(albumArtPath, opts);
//				Log.i("DBG", ""+opts.outWidth+" "+opts.outHeight);
//				bmTmp.recycle();
				
				FileInputStream albumArtPathStream  = new FileInputStream(albumArtPath);
				Bitmap bm = BitmapFactory.decodeStream(albumArtPathStream);
				smallBitmap = Bitmap.createScaledBitmap(bm,
														120, 120, false);
				albumArtPathStream.close();
				bm.recycle();
			} catch (Exception e) {
				// TODO: failed to get from library
				// 			show error
				//smallBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.albumart_mp_unknown);
				e.printStackTrace();
			} catch (Error err){
				err.printStackTrace();
			}
		}
		else{
			//smallBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.albumart_mp_unknown);
			return;
		}
					
	    
		try {
			if(smallAlbumArtFile != null)
				smallAlbumArtFile.createNewFile();
			else
				Log.i("DBG", "smallAlbumArtFile is null ?????");
			
		    FileOutputStream smallAlbumArtFileStream = null;
		    
		    if(smallAlbumArtFile != null)
		    	smallAlbumArtFileStream = new FileOutputStream(smallAlbumArtFile);
		    else
		    	Log.i("DBG", "smallAlbumArtFile is null ?????");
		    
		    if(smallBitmap != null && smallAlbumArtFileStream != null)
		    	smallBitmap.compress(Bitmap.CompressFormat.JPEG, 90, smallAlbumArtFileStream);
		    else
		    	Log.i("DBG", "smallBitmap or smallAlbumArtFileStream is null ?????");
		    
		    if(smallAlbumArtFileStream != null)
		    	smallAlbumArtFileStream.close();
		    else
		    	Log.i("DBG", "smallAlbumArtFileStream is null ?????");
		 
		    if(smallBitmap != null)
		    	smallBitmap.recycle();
		    
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
	
	/*******************************
	 * 
	 * isArtist
	 * 
	 *******************************/
	public boolean artistNameIsSimilarEnough(String artistNameFromSearch, String artistNameFiltered){
		/*
		 * First check if the separate words of our name 
		 * are contained in the result from the search
		 */
		// TODO:
		
		/*
		 * Then, do a blind search
		 */
		double SIMILARITY_THRESHOLD = 0.75;
		int COMPARISON_SEGMENT_LENGTH = 3;
		int matchedSegments = 0;
		for(int i=0; i < artistNameFiltered.length() - COMPARISON_SEGMENT_LENGTH; i++){
			if(artistNameFromSearch.contains(artistNameFiltered.substring(i, i+COMPARISON_SEGMENT_LENGTH))){
				matchedSegments++;
			}
			Log.i("SIMILARITY", artistNameFiltered.substring(i, i+COMPARISON_SEGMENT_LENGTH) +" in "+artistNameFromSearch);
		}
		Log.i("SIMILARITY", artistNameFromSearch+" ? "+artistNameFiltered);
		Log.i("SIMILARITY", ((double)matchedSegments)/(artistNameFiltered.length() - COMPARISON_SEGMENT_LENGTH)+" > "+SIMILARITY_THRESHOLD);
		if(((double)matchedSegments)/(artistNameFiltered.length() - COMPARISON_SEGMENT_LENGTH) > SIMILARITY_THRESHOLD)
			return true;
		else
			return false;
	}
	
//	/*******************************
//	 * 
//	 * createGoogleImageConnection
//	 * 
//	 *******************************/
//	public DefaultHttpClientConnection createGoogleImageConnection(String url){
//        
//		DefaultHttpClient httpClient = new DefaultHttpClient(); 
//        
//        // Get cookies from the login page (not the address same of the form post)
//         HttpGet httpGet = new HttpGet("https://secure.pragprog.com/login");
//         
//         HttpResponse response; 
//         
//         try {
//			response = httpClient.execute(httpGet);
//            HttpEntity entity = response.getEntity(); 
//            entity.
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
         
         
		//		Socket sock = new Socket();
//		DefaultHttpClientConnection conn = 
//			new DefaultHttpClientConnection();
//		
//		conn.bind(sock, )
//		
////		return null;
//		
//	}
	
	
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
							filtered.substring(stop+1, filtered.length());
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
			filtered = filtered.replace('|', ' ');
			filtered = filtered.replace('#', ' ');
			filtered = filtered.replace('&', ' ');
			filtered = filtered.replace('%', ' ');
			
			Log.i("filter", filtered);
	
			return filtered;
		} catch (StringIndexOutOfBoundsException e){
			e.printStackTrace();
			return original;
		}
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