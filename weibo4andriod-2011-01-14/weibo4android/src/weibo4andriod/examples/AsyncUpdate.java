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

import weibo4andriod.AsyncWeibo;
import weibo4andriod.Status;
import weibo4andriod.WeiboAdapter;
import weibo4andriod.WeiboException;

/**
 * <p>This is a code example of Weibo4J async API.<br>
 * Usage: java Weibo4j.examples.AsyncUpdate <i>WeiboID</i> <i>WeiboPassword</i> <i>text</i><br>
 * </p>
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class AsyncUpdate {
    /**
     * Main entry for this application.
     * @param args String[] WeiboID WeiboPassword StatusString
     * @throws InterruptedException
     */

    static Object lock = new Object();
    public static void main(String[] args) throws InterruptedException {
        if (args.length < 3) {
            System.out.println(
                "Usage: java weibo4j.examples.AsyncUpdate ID Password text");
            System.exit( -1);
        }
        AsyncWeibo weibo = new AsyncWeibo(args[0], args[1]);
        weibo.updateStatusAsync(args[2], new WeiboAdapter() {
            @Override public void updated(Status status) {
                System.out.println("Successfully updated the status to [" +
                                   status.getText() + "].");
                synchronized (lock) {
                    lock.notify();
                }
            }

            @Override public void onException(WeiboException e, int method) {
                if (method == AsyncWeibo.UPDATE_STATUS) {
                    e.printStackTrace();
                    synchronized (lock) {
                        lock.notify();
                    }
                } else {
                    synchronized (lock) {
                        lock.notify();
                    }
                    throw new AssertionError("Should not happen");
                }
            }
        }
        );
        synchronized (lock) {
            lock.wait();
        }
    }

}
