/**
 *
 */
package weibo4andriod.examples;

import java.io.BufferedReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import weibo4andriod.Weibo;
import weibo4andriod.WeiboException;
import weibo4andriod.http.AccessToken;

/**
 * @author hezhou
 *
 */
public class XAuthTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
    	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
        Weibo weibo = new Weibo();
        try {
        	 String userId = "1377583044";
             String passWord = "s172721";
             AccessToken accessToken = weibo.getXAuthAccessToken(userId, passWord, "client_auth");
             System.out.println("Got access token.");
             System.out.println("Access token: "+ accessToken.getToken());
             System.out.println("Access token secret: "+ accessToken.getTokenSecret());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}

}
