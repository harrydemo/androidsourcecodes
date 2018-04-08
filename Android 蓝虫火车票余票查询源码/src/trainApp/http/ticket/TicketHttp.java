package trainApp.http.ticket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

public class TicketHttp {

	HttpURLConnection httpURLConnection;
	OutputStream httpOutputStream;
	
	private TicketHttp(){ 
		
	}
	
	public TicketHttp(String url) throws MalformedURLException, IOException{
		this(new URL(url));
	}
	public TicketHttp(URL url) throws IOException{
		this(url.openConnection());
	}
	public TicketHttp(URLConnection conn) throws ProtocolException{
		if(conn != null){
			this.httpURLConnection = (HttpURLConnection) conn;
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Accept", "application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
			httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpURLConnection.setRequestProperty("Origin", "http://dynamic.12306.cn");
			httpURLConnection.setRequestProperty("Referer","http://dynamic.12306.cn/TrainQuery/leftTicketByStation.jsp");
			httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/534.6 (KHTML, like Gecko) Chrome/7.0.503.0 Safari/534.6");
		
			httpURLConnection.setInstanceFollowRedirects(false);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(false);
		} else {
			this.httpOutputStream = null;
		}
	}
	
	protected void connect() throws IOException{
		if(null == this.httpOutputStream){
			this.httpOutputStream = this.httpURLConnection.getOutputStream();
		}
	}
	
	public void write(String postData) throws IOException{
		connect();
		OutputStreamWriter wr = new OutputStreamWriter(this.httpOutputStream);
	    wr.write(postData);
	    wr.flush();
	    wr.close();
	}
	
	public String post() throws IOException{
		BufferedReader rd = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
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
		return new TicketHttp(url).post(postData);
	}
	public static String post(String url, String postData) throws MalformedURLException, IOException{
		return new TicketHttp(url).post(postData);
	}
	
}
