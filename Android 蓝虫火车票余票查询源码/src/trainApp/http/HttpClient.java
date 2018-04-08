package trainApp.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class HttpClient
{
	URLConnection conn = null;
	OutputStream os = null;
	
	private HttpClient(){}
	
	public HttpClient(String url) throws MalformedURLException, IOException{
		this(new URL(url));
	}
	public HttpClient(URL url) throws IOException{
		this(url.openConnection());
	}
	public HttpClient(URLConnection conn){
		this.conn = conn;
	    conn.setDoOutput(true);
	}
	
	protected void connect() throws IOException{
		if(null == os){
			os = conn.getOutputStream();
		}
	}
	
	public void write(String postData) throws IOException{
		connect();
		OutputStreamWriter wr = new OutputStreamWriter(os);
	    wr.write(postData);
	    wr.flush();
	    wr.close();
	}
	public String post() throws IOException{
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	    String line;
	    String msg = null;
	    while ((line = rd.readLine()) != null) {
	        // Process line...
	    	msg += line + "\n";
	    }
	    rd.close();
	    return msg;
	}
	public String post(String postData) throws IOException{
		write(postData);
		return post();
	}
	
	
	public static String post(URL url, String postData) throws IOException{
		return new HttpClient(url).post(postData);
	}
	
	public static String post(String url, String postData) throws MalformedURLException, IOException{
		return new HttpClient(url).post(postData);
	}
}
