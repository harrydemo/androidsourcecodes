package com.xmobileapp.android.weatherforecast.transport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.ksoap2.transport.ServiceConnection;

/**
 * Connection using apache HttpComponent
 */
public class AndroidServiceConnection implements ServiceConnection {
//	private HttpClientConnection connection;
//	private static ClientConnectionManager connectionManager = new SingleClientConnManager(getParams(), supportedSchemes);
	private HttpPost httppost;
    private java.io.ByteArrayOutputStream bufferStream = null;
    private OutputStreamWriter outputStreamWriter;
    private DefaultHttpClient httpclient;
    
    /**
     * Constructor taking the url to the endpoint for this soap communication
     * @param url the url to open the connection to.
     */
    public AndroidServiceConnection(String url) throws IOException {
    	httppost = new HttpPost(url);
    	httpclient = new DefaultHttpClient(); 
//    	HttpHost proxy = new HttpHost("210.51.54.165", 9090);  
//    	httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);  
//    	httpclient = (DefaultHttpClient) Utils.setProxy(httpclient, "210.51.54.165", 9090);
    }

    public void connect() throws IOException {
        
    }

    public void disconnect() {
        
    }

    public void setRequestProperty(String name, String value) {
    	httppost.setHeader(name, value);
    }

    public void setRequestMethod(String requestMethod) throws IOException {
        if (!requestMethod.toLowerCase().equals("post")) {
        	throw(new IOException("Only POST method is supported"));
        }
    }

    public OutputStream openOutputStream() throws IOException {
    	bufferStream = new java.io.ByteArrayOutputStream();
    	return bufferStream;
    }

//    public Writer openWriter() throws IOException {
//    	outputStreamWriter = new OutputStreamWriter("tuf-8");
//    	return bufferStream;
//    }
    
    public InputStream openInputStream() throws IOException {
    	
    	AbstractHttpEntity re = new ByteArrayEntity(bufferStream.toByteArray());
    	httppost.setEntity(re);
    	HttpResponse response = httpclient.execute(httppost);
    	HttpEntity entity = response.getEntity();  
	    System.out.println(response.getStatusLine());  
	    if (entity != null) {  
	      System.out.println("Response content length: " + entity.getContentLength());  
	    } 
	    return entity.getContent();
    }

    public InputStream getErrorStream() {
    	return null;
    }
    
}
