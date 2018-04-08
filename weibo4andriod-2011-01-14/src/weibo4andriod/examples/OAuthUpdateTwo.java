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

import weibo4andriod.Status;
import weibo4andriod.Weibo;
import weibo4andriod.WeiboException;


/**
 * Example application that uses OAuth method to acquire access to your account.<br>
 * This application illustrates how to use OAuth method with Weibo4J.<br>
 * Usage: java -DWeibo4j.oauth.consumerKey=[consumer key] -DWeibo4j.oauth.consumerSecret=[consumer secret] Weibo4j.examples.OAuthUpdate [message]
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class OAuthUpdateTwo {
    /**
     * Usage: java -DWeibo4j.oauth.consumerKey=[consumer key] -DWeibo4j.oauth.consumerSecret=[consumer secret] Weibo4j.examples.OAuthUpdate [message]
     * @param args message
     */
    public static void main(String[] args) {
        try {
        	if (args.length < 2) {
	            System.out.println(
	                "Usage: java weibo4j.examples.OAuthUpdateTwo token tokenSecret");
	            System.exit( -1);
	        }
        	
        	System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
        	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
        	
            Weibo weibo = new Weibo();
            
            /*
			 * 此处需要填写AccessToken的key和Secret，可以从OAuthUpdate的执行结果中拷贝过来
             */
            weibo.setToken("720228000", "f190c46a3017801ebefeca62bf36aac3");
            
            Status status = weibo.updateStatus("你好吗？");
            
            System.out.println("Successfully updated the status to [" + status.getText() + "].");
            System.exit(0);
        } catch (WeiboException te) {
            System.out.println("Failed to get timeline: " + te.getMessage());
            System.exit( -1);
        } catch (Exception ioe) {
            System.out.println("Failed to read the system input.");
            System.exit( -1);
        }
    }
}
