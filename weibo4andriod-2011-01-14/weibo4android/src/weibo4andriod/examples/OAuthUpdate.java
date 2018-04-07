/*
Copyright (c) 2007-2009, Yusuke Yamamoto
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the Yusuke Yamamoto nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY Yusuke Yamamoto ``AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Yusuke Yamamoto BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package weibo4andriod.examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import weibo4andriod.Weibo;
import weibo4andriod.WeiboException;
import weibo4andriod.http.AccessToken;
import weibo4andriod.http.RequestToken;
import weibo4andriod.util.BareBonesBrowserLaunch;

/**
 * Example application that uses OAuth method to acquire access to your account.<br>
 * This application illustrates how to use OAuth method with Weibo4J.<br>
 * Usage: java -DWeibo4j.oauth.consumerKey=[consumer key] -DWeibo4j.oauth.consumerSecret=[consumer secret] Weibo4j.examples.OAuthUpdate [message]
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class OAuthUpdate {
    /**
     * Usage: java -DWeibo4j.oauth.consumerKey=[consumer key] -DWeibo4j.oauth.consumerSecret=[consumer secret] Weibo4j.examples.OAuthUpdate [message]
     * @param args message
     */
    public static void main(String[] args) {
        try {
        	System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
        	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);


            Weibo weibo = new Weibo();
            // set callback url, desktop app please set to null
            // http://callback_url?oauth_token=xxx&oauth_verifier=xxx
            RequestToken requestToken = weibo.getOAuthRequestToken();

            System.out.println("Got request token.");
            System.out.println("Request token: "+ requestToken.getToken());
            System.out.println("Request token secret: "+ requestToken.getTokenSecret());
            AccessToken accessToken = null;

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (null == accessToken) {
                System.out.println("Open the following URL and grant access to your account:");
                System.out.println(requestToken.getAuthorizationURL());
                BareBonesBrowserLaunch.openURL(requestToken.getAuthorizationURL()+"&from=xweibo");
                System.out.print("Hit enter when it's done.[Enter]:");

                String pin = br.readLine();
                System.out.println("pin: " + br.toString());
                try{
                    accessToken = requestToken.getAccessToken(pin);
                } catch (WeiboException te) {
                    if(401 == te.getStatusCode()){
                        System.out.println("Unable to get the access token.");
                    }else{
                        te.printStackTrace();
                    }
                }
            }
            System.out.println("Got access token.");
            System.out.println("Access token: "+ accessToken.getToken());
            System.out.println("Access token secret: "+ accessToken.getTokenSecret());

           /* weibo.setToken(accessToken.getToken(), accessToken.getTokenSecret());

			Status status = weibo.updateStatus("test message6 ");
			System.out.println("Successfully updated the status to ["
					+ status.getText() + "].");

            try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
            System.exit(0);
        } catch (WeiboException te) {
            System.out.println("Failed to get timeline: " + te.getMessage());
            System.exit( -1);
        } catch (IOException ioe) {
            System.out.println("Failed to read the system input.");
            System.exit( -1);
        }
    }
}
