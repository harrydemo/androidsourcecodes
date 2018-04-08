package org.abrantix.rockon.rockonnggl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class GoogleImagesFetcher{

	final String TAG = "GoogleImagesFetcher";
	
	private final String	GOOGLE_IMAGES_BASE_URL = "http://images.google.com/";
	private final String	GOOGLE_IMAGES_SEARCH_URL = GOOGLE_IMAGES_BASE_URL + "images?hl=en&source=hp&gbv=2&aq=f&oq=&aqi=&q=";
	private final String	GOOGLE_IMAGES_SEARCH_API_URL = "http://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8&safe=moderate&imgsz="+URLEncoder.encode("large|xlarge")+"&q="; // rsz = results per page [max=8]
//	private final String	GOOGLE_IMAGES_SEARCH_API_URL = "http://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8&safe=moderate&imgsz="+URLEncoder.encode("medium|large|xlarge")+"&q="; // rsz = results per page [max=8]
	
    GoogleImagesFetcher(){

    }
    
    private BufferedReader createReaderJSON(String artistName, String albumName){
		try{
	    	URL googleImagesRequest = new URL(
	    			this.GOOGLE_IMAGES_SEARCH_API_URL+
					URLEncoder.encode(artistName)+
					"+"+
					URLEncoder.encode(albumName));
	
			/* connection setup */
			BasicHttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params, 7500);
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(googleImagesRequest.toString());
			HttpResponse response; 
			 
			/* fetch content */
			response = httpClient.execute(httpGet);
		    HttpEntity entity = response.getEntity();
			
		    /* get the response */
		    return 
				new BufferedReader(new InputStreamReader(
						entity.getContent()));
//			entity.consumeContent();
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
    }
    
    private BufferedReader createReader(String artistName, String albumName){
		try{
	    	URL googleImagesRequest = new URL(
					this.GOOGLE_IMAGES_SEARCH_URL+
					URLEncoder.encode(artistName)+
					"+"+
					URLEncoder.encode(albumName));
	
			/* connection setup */
			BasicHttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params, 7500);
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(googleImagesRequest.toString());
			HttpResponse response; 
			 
			/* fetch content */
			response = httpClient.execute(httpGet);
		    HttpEntity entity = response.getEntity();
			
		    /* get the response */
		    return 
				new BufferedReader(new InputStreamReader(
						entity.getContent()));
//			entity.consumeContent();
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
    }
    
	public Bitmap fetch(String artistName, String albumName){
		
		try{
			/* parse response and fetch 1st image bigger than blahblah */
//			Bitmap bitmap = getFirstDecentImage(createReader(artistName, albumName));
			Bitmap bitmap = getFirstDecentImageJSON(createReaderJSON(artistName, albumName));
			
			return bitmap;
			
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public void fetch(String artistName, String albumName, int count, Handler newImageHandler){
//		getImages(
//				createReader(artistName, albumName),
//				count,
//				newImageHandler);
		getImagesJSON(
				createReaderJSON(artistName, albumName),
				count,
				newImageHandler);
	}
	
	
	Bitmap getFirstDecentImageJSON(BufferedReader in) {
		Bitmap bm = null;
		try {
			JSONObject obj = new JSONObject(in.readLine());
			JSONObject responseObj = obj.getJSONObject("responseData");
			JSONArray array = responseObj.getJSONArray("results");
			for(int i=0; i<Math.min(20, array.length()); i++) {
				JSONObject resultsObj = array.getJSONObject(i);
				String albumArtURL = resultsObj.getString("url");
				Log.i(TAG, "GIMAGE[URL]: "+albumArtURL);
				if(albumArtURL != null){
					try{
						Bitmap bitmap = AlbumArtUtils.fetchBitmap(albumArtURL);
						if(bitmap.getWidth() < Constants.REASONABLE_ALBUM_ART_SIZE){
							albumArtURL = null;
							Log.i("GIMAGES" , "createAlbumArt FAIL");
						} else {
							Log.i("GIMAGES" , "createAlbumArt WIN");
							return bitmap;
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
				} else {
					Log.i("GIMAGES" , "albumArt URL FAIL!");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bm;
	}
	
	Bitmap getImagesJSON(BufferedReader in, int count, Handler newImageHandler){
		Bitmap bm = null;
		try {
			int imagesFound = 0;
			JSONObject obj = new JSONObject(in.readLine());
			JSONObject responseObj = obj.getJSONObject("responseData");
			JSONArray array = responseObj.getJSONArray("results");
			for(int i=0; i<Math.min(20, array.length()); i++) {
				JSONObject resultsObj = array.getJSONObject(i);
				String albumArtURL = resultsObj.getString("url");
				Log.i(TAG, "GIMAGE[URL]: "+albumArtURL);
				if(albumArtURL != null){
					try{
						Bitmap bitmap = AlbumArtUtils.fetchBitmap(albumArtURL);
						if(bitmap.getWidth() < Constants.REASONABLE_ALBUM_ART_SIZE){
							albumArtURL = null;
							Log.i("GIMAGES" , "createAlbumArt FAIL");
						} else {
							Log.i("GIMAGES" , "createAlbumArt WIN");
							Message msg = new Message();
							msg.obj = bitmap;
							msg.what = imagesFound;
							newImageHandler.sendMessage(msg);
							imagesFound++;
							if(imagesFound >= count)
								return null;
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
				} else {
					Log.i("GIMAGES" , "albumArt URL FAIL!");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bm;
	}
	
	/** XXX - YUCK CODE WARNING */
	Bitmap getFirstDecentImage(BufferedReader in){
		
		/*
		 * Parse 1st decent image on the result page
		 */
		String line;
		int idxStart = 0;
		int idxStop;
		do{
			try{
				line = in.readLine();
			}catch(Exception e){
				e.printStackTrace();
				line = null;
			}
			if(line != null){
				Log.i("GIMAGES", line);
//				if(line.startsWith("<table")){
				if(line.contains("imgurl")){
					Log.i("GIMAGES", line);
					boolean found = false;
					int tries = 0;
					while(!found){
						tries++;
						if(tries > 12)
							break;
//						idxStart = line.indexOf("<a href=", idxStart);
						idxStart = line.indexOf("imgurl=", idxStart);
						if(idxStart == -1){
							try{
								line = in.readLine();
							}catch(Exception e){
								e.printStackTrace();
								line = null;
							}
							if(line == null)
								break;
							continue;
						}
						
						idxStart = line.indexOf("http://", idxStart);
						idxStop = line.indexOf("&imgrefurl=", idxStart);
						String albumArtURL = line.substring(idxStart, idxStop);
						Log.i("GIMAGE-RESULT", line.substring(idxStart, idxStop));
						
						try{
							if(albumArtURL != null){
//								if(createAlbumArt(artistName, albumName, albumArtURL) == null){
								Bitmap bitmap = AlbumArtUtils.fetchBitmap(albumArtURL);
								if(bitmap.getWidth() < Constants.REASONABLE_ALBUM_ART_SIZE){
									albumArtURL = null;
									found = false;
									Log.i("GIMAGES" , "createAlbumArt FAIL");
								} else {
									found = true;
									Log.i("GIMAGES" , "createAlbumArt WIN");
									return bitmap;
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
		
		return null;
	}
	
	
	/** XXX - YUCK CODE WARNING */
	Bitmap getImages(BufferedReader in, int count, Handler newImageHandler){
		
		/*
		 * Parse 1st decent image on the result page
		 */
		String line;
		int idxStart = 0;
		int idxStop;
		do{
			try{
				line = in.readLine();
			}catch(Exception e){
				e.printStackTrace();
				line = null;
			}
			if(line != null){
				Log.i("GIMAGES", line);
//				if(line.startsWith("<table")){
				if(line.contains("imgurl")){
					Log.i("GIMAGES", line);
					boolean found = false;
					int imagesFound = 0;
					int tries = 0;
					while(imagesFound < count){
						tries++;
						if(tries > count * 2)
							break;
//						idxStart = line.indexOf("<a href=", idxStart);
						idxStart = line.indexOf("imgurl=", idxStart);
						
						if(idxStart == -1){
							try{
								line = in.readLine();
							}catch(Exception e){
								e.printStackTrace();
								line = null;
							}
							if(line == null)
								break;
							continue;
						}
						
						idxStart = line.indexOf("http://", idxStart);
						idxStop = line.indexOf("&imgrefurl=", idxStart);
						String albumArtURL = line.substring(idxStart, idxStop);
						Log.i("GIMAGE-RESULT", line.substring(idxStart, idxStop));
						
						try{
							if(albumArtURL != null){
								Bitmap bitmap = AlbumArtUtils.fetchBitmap(albumArtURL);
								if(bitmap == null){
									albumArtURL = null;
									found = false;
									Log.i("GIMAGES" , "createAlbumArt FAIL");
								} else {
									Message msg = new Message();
									msg.obj = bitmap;
									msg.what = imagesFound;
									newImageHandler.sendMessage(msg);
									imagesFound++;
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
		
		return null;
	}
		
}
