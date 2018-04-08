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

import java.util.List;

import weibo4andriod.Comment;
import weibo4andriod.Status;
import weibo4andriod.Weibo;
import weibo4andriod.WeiboException;
import weibo4andriod.org.json.JSONException;
/**
 * <p>This is a code example of Weibo4J update API.<br>
 * Usage: java Weibo4j.examples.Update <i>WeiboID</i> <i>WeiboPassword</i> <i>text</i><br>
 * </p>
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class Update {
    /**
     * Main entry for this application.
     * @param args String[] WeiboID WeiboPassword StatusString
     */

    public static void main(String[] args)throws WeiboException{
        if (args.length < 3) {
            System.out.println(
                "Usage: java weibo4j.examples.Update ID Password text");
            System.exit( -1);
        }
        
        long l1 = System.currentTimeMillis();
        Weibo weibo = new Weibo(args[0], args[1]);
//        System.out.println(weibo.getRateLimitStatus());
        
        List<Status> statuses = weibo.getPublicTimeline();
        
        for (Status status : statuses) {
			System.out.println(status);
		}
        
        String msg = args[2] + new java.util.Date();
        
        // normal status
         Status status = weibo.updateStatus(args[2] + System.currentTimeMillis());
        // status with geocode
		try {
			status = weibo.updateStatus(msg, 40.7579, -73.985);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
        
        long l2 = System.currentTimeMillis();
        
        System.out.println("Successfully updated the status to [" + status.getText() + "].");
        System.out.println("Time elapsed: " + (l2 - l1));
        
        try {
			Thread.sleep(1000); // avoid flush server
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        // add a comment for the status
        long sid = status.getId();
        Comment cmt = weibo.updateComment("评论1 " + new java.util.Date(),  String.valueOf(sid), null);
        
        weibo.getComments(String.valueOf(sid));
        
        weibo.updateComment("评论2 " + new java.util.Date(),  String.valueOf(sid), null);

		try {
			Thread.sleep(1000); // avoid flush server
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Comment cmt2 = weibo.destroyComment(cmt.getId());
		System.out.println("delete " + cmt2);
    }
}