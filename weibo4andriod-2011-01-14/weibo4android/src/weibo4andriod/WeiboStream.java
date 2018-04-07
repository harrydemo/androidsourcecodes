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
package weibo4andriod;

import weibo4andriod.http.PostParameter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A java reporesentation of the <a href="http://open.t.sina.com.cn/wiki/Streaming-API-Documentation">Weibo Streaming API</a>
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Weibo4J 2.0.4
 */
public class WeiboStream extends WeiboSupport {
    private final static boolean DEBUG = Configuration.getDebug();

    private StatusListener statusListener;
    private StreamHandlingThread handler = null;
    private int retryPerMinutes = 1;

    /**
     * Constructs a WeiboStream instance. UserID and password should be provided by either sinat4j.properties or system property.
     * since Weibo4J 2.0.10
     */
    public WeiboStream() {
        super();
    }

    public WeiboStream(String userId, String password) {
        super(userId, password);
    }

    public WeiboStream(String userId, String password, StatusListener listener) {
        super(userId, password);
        this.statusListener = listener;
    }

    /* Streaming API */
    /**
     * Starts listening on all public statuses. Available only to approved parties and requires a signed agreement to access. Please do not contact us about access to the firehose. If your service warrants access to it, we'll contact you.
     *
     * @param count Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @throws WeiboException when Weibo service or network is unavailable
     * @see weibo4andriod.StatusStream
     * @see <a href="http://open.t.sina.com.cn/wiki/Streaming-API-Documentation#firehose">Weibo API Wiki / Streaming API Documentation - firehose</a>
     * @since Weibo4J 2.0.4
     */
    public void firehose(int count) throws WeiboException {
        startHandler(new StreamHandlingThread(new Object[]{count}) {
            public StatusStream getStream() throws WeiboException {
                return getFirehoseStream((Integer) args[0]);
            }
        });
    }

    /**
     * Returns a status stream of all public statuses. Available only to approved parties and requires a signed agreement to access. Please do not contact us about access to the firehose. If your service warrants access to it, we'll contact you.
     *
     * @param count Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @return StatusStream
     * @throws WeiboException when Weibo service or network is unavailable
     * @see weibo4andriod.StatusStream
     * @see <a href="http://open.t.sina.com.cn/wiki/Streaming-API-Documentation#firehose">Weibo API Wiki / Streaming API Documentation - firehose</a>
     * @since Weibo4J 2.0.4
     */
    public StatusStream getFirehoseStream(int count) throws WeiboException {

        try {
            return new StatusStream(http.post(getStreamBaseURL() + "1/statuses/firehose.json"
                    , new PostParameter[]{new PostParameter("count"
                            , String.valueOf(count))}, true));
        } catch (IOException e) {
            throw new WeiboException(e);
        }
    }

    /**
     * Starts listening on all retweets. The retweet stream is not a generally available resource. Few applications require this level of access. Creative use of a combination of other resources and various access levels can satisfy nearly every application use case. As of 9/11/2009, the site-wide retweet feature has not yet launched, so there are currently few, if any, retweets on this stream.
     *
     * @throws WeiboException when Weibo service or network is unavailable
     * @see weibo4andriod.StatusStream
     * @see <a href="http://open.t.sina.com.cn/wiki/Streaming-API-Documentation#retweet">Weibo API Wiki / Streaming API Documentation - retweet</a>
     * @since Weibo4J 2.0.10
     */
    public void retweet() throws WeiboException {
        startHandler(new StreamHandlingThread(new Object[]{}) {
            public StatusStream getStream() throws WeiboException {
                return getRetweetStream();
            }
        });
    }

    /**
     * Returns a stream of all retweets. The retweet stream is not a generally available resource. Few applications require this level of access. Creative use of a combination of other resources and various access levels can satisfy nearly every application use case. As of 9/11/2009, the site-wide retweet feature has not yet launched, so there are currently few, if any, retweets on this stream.
     *
     * @return StatusStream
     * @throws WeiboException when Weibo service or network is unavailable
     * @see weibo4andriod.StatusStream
     * @see <a href="http://open.t.sina.com.cn/wiki/Streaming-API-Documentation#firehose">Weibo API Wiki / Streaming API Documentation - firehose</a>
     * @since Weibo4J 2.0.10
     */
    public StatusStream getRetweetStream() throws WeiboException {

        try {
            return new StatusStream(http.post(getStreamBaseURL() + "1/statuses/retweet.json"
                    , new PostParameter[]{}, true));
        } catch (IOException e) {
            throw new WeiboException(e);
        }
    }

    /**
     * Starts listening on random sample of all public statuses. The default access level provides a small proportion of the Firehose. The "Gardenhose" access level provides a proportion more suitable for data mining and research applications that desire a larger proportion to be statistically significant sample.
     *
     * @throws WeiboException when Weibo service or network is unavailable
     * @see weibo4andriod.StatusStream
     * @see <a href="http://open.t.sina.com.cn/wiki/Streaming-API-Documentation#sample">Weibo API Wiki / Streaming API Documentation - sample</a>
     * @since Weibo4J 2.0.10
     */
    public void sample() throws WeiboException {
        startHandler(new StreamHandlingThread(null) {
            public StatusStream getStream() throws WeiboException {
                return getSampleStream();
            }
        });
    }

    /**
     * Returns a stream of random sample of all public statuses. The default access level provides a small proportion of the Firehose. The "Gardenhose" access level provides a proportion more suitable for data mining and research applications that desire a larger proportion to be statistically significant sample.
     *
     * @return StatusStream
     * @throws WeiboException when Weibo service or network is unavailable
     * @see weibo4andriod.StatusStream
     * @see <a href="http://open.t.sina.com.cn/wiki/Streaming-API-Documentation#sample">Weibo API Wiki / Streaming API Documentation - sample</a>
     * @since Weibo4J 2.0.10
     */
    public StatusStream getSampleStream() throws WeiboException {
        try {
            return new StatusStream(http.get(getStreamBaseURL() + "1/statuses/sample.json"
                    , true));
        } catch (IOException e) {
            throw new WeiboException(e);
        }
    }


    /**
     * See birddog above. Allows following up to 200 users. Publicly available.
     *
     * @param count  Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @param follow Specifies the users, by ID, to receive public tweets from.
     * @param track Specifies keywords to track.
     * @throws WeiboException when Weibo service or network is unavailable
     * @see weibo4andriod.StatusStream
     * @see <a href="http://open.t.sina.com.cn/wiki/Streaming-API-Documentation#filter">Weibo API Wiki / Streaming API Documentation - filter</a>
     * @since Weibo4J 2.0.10
     */
    public void filter(int count, int[] follow, String[] track) throws WeiboException {
        startHandler(new StreamHandlingThread(new Object[]{count, follow, track}) {
            public StatusStream getStream() throws WeiboException {
                return getFilterStream((Integer) args[0],(int[]) args[1],(String[]) args[2]);
            }
        });
    }

    /**
     * Returns stream of public statuses that match one or more filter predicates. At least one predicate parameter, track or follow, must be specified. Both parameters may be specified which allows most clients to use a single connection to the Streaming API.
     *
     * @param follow Specifies the users, by ID, to receive public tweets from.
     * @return StatusStream
     * @throws WeiboException when Weibo service or network is unavailable
     * @see weibo4andriod.StatusStream
     * @see <a href="http://open.t.sina.com.cn/wiki/Streaming-API-Documentation#filter">Weibo API Wiki / Streaming API Documentation - filter</a>
     * @since Weibo4J 2.0.10
     */
    public StatusStream getFilterStream(int count, int[] follow, String[] track)
            throws WeiboException {
        List<PostParameter> postparams = new ArrayList<PostParameter>();
        postparams.add(new PostParameter("count", count));
        if(null != follow && follow.length > 0){
            postparams.add(new PostParameter("follow"
                            , toFollowString(follow)));
        }
        if(null != track && track.length > 0){
            postparams.add(new PostParameter("track"
                            , toTrackString(track)));
        }
        try {
            return new StatusStream(http.post(getStreamBaseURL() + "1/statuses/filter.json"
                    , postparams.toArray(new PostParameter[0]), true));
        } catch (IOException e) {
            throw new WeiboException(e);
        }
    }

    /**
     * Starts listening on a percentage of all public statuses, suitable for data mining and research applications that require a statistically significant sample. Available only to approved parties and requires a signed agreement to access.
     *
     * @throws WeiboException when Weibo service or network is unavailable
     * @see weibo4andriod.StatusStream
     * @see <a href="http://open.t.sina.com.cn/wiki/Streaming-API-Documentation#gardenhose">Weibo API Wiki / Streaming API Documentation - gardenhose</a>
     * @since Weibo4J 2.0.4
     * @deprecated use sample() instead
     */
    public void gardenhose() throws WeiboException {
        startHandler(new StreamHandlingThread(null) {
            public StatusStream getStream() throws WeiboException {
                return getGardenhoseStream();
            }
        });
    }

    /**
     * Returns a status stream for a percentage of all public statuses, suitable for data mining and research applications that require a statistically significant sample. Available only to approved parties and requires a signed agreement to access.
     *
     * @return StatusStream
     * @throws WeiboException when Weibo service or network is unavailable
     * @see weibo4andriod.StatusStream
     * @see <a href="http://open.t.sina.com.cn/wiki/Streaming-API-Documentation#gardenhose">Weibo API Wiki / Streaming API Documentation - gardenhose</a>
     * @since Weibo4J 2.0.4
     * @deprecated use sampleStream() instead
     */
    public StatusStream getGardenhoseStream() throws WeiboException {
        return getSampleStream();
    }

    /**
     * Starts listening on a percentage of all public statuses, suitable for small projects that don't require a statistically significant sample. Publicly available.
     *
     * @throws WeiboException when Weibo service or network is unavailable
     * @see weibo4andriod.StatusStream
     * @see <a href="http://open.t.sina.com.cn/wiki/Streaming-API-Documentation#spritzer">Weibo API Wiki / Streaming API Documentation - spritizer</a>
     * @since Weibo4J 2.0.4
     * @deprecated use sample() instead
     */
    public void spritzer() throws WeiboException {
        startHandler(new StreamHandlingThread(null) {
            public StatusStream getStream() throws WeiboException {
                return getSpritzerStream();
            }
        });
    }

    /**
     * Returns a status stream for a percentage of all public statuses, suitable for small projects that don't require a statistically significant sample. Publicly available.
     *
     * @return StatusStream
     * @throws WeiboException when Weibo service or network is unavailable
     * @see weibo4andriod.StatusStream
     * @see <a href="http://open.t.sina.com.cn/wiki/Streaming-API-Documentation#spritzer">Weibo API Wiki / Streaming API Documentation - spritizer</a>
     * @since Weibo4J 2.0.4
     * @deprecated use sampleStream() instead
     */
    public StatusStream getSpritzerStream() throws WeiboException {
        return getSampleStream();
    }

    /**
     * Starts listening on public statuses from a specified set of users, by ID. Requires use of the "follow" parameter, documented below. Allows following up to 200,000 users. Available only to approved parties and requires a signed agreement to access.
     *
     * @param count  Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @param follow Specifies the users, by ID, to receive public tweets from.
     * @throws WeiboException when Weibo service or network is unavailable
     * @see weibo4andriod.StatusStream
     * @see <a href="http://open.t.sina.com.cn/wiki/Streaming-API-Documentation#birddog">Weibo API Wiki / Streaming API Documentation - birddog</a>
     * @since Weibo4J 2.0.4
     * @deprecated use filter() instead
     */
    public void birddog(int count, int[] follow) throws WeiboException {
        startHandler(new StreamHandlingThread(new Object[]{count, follow}) {
            public StatusStream getStream() throws WeiboException {
                return getBirddogStream((Integer) args[0], (int[]) args[1]);
            }
        });
    }

    /**
     * Returns a status stream for public statuses from a specified set of users, by ID. Requires use of the "follow" parameter, documented below. Allows following up to 200,000 users. Available only to approved parties and requires a signed agreement to access.
     *
     * @param count  Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @param follow Specifies the users, by ID, to receive public tweets from.
     * @return StatusStream
     * @throws WeiboException when Weibo service or network is unavailable
     * @see weibo4andriod.StatusStream
     * @see <a href="http://open.t.sina.com.cn/wiki/Streaming-API-Documentation#birddog">Weibo API Wiki / Streaming API Documentation - birddog</a>
     * @since Weibo4J 2.0.4
     * @deprecated use filterStream() instead
     */
    public StatusStream getBirddogStream(int count, int[] follow) throws WeiboException {
        return getFilterStream(count, follow, null);
    }

    /**
     * See birddog above. Allows following up to 2,000 users.
     *
     * @param count  Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @param follow Specifies the users, by ID, to receive public tweets from.
     * @throws WeiboException when Weibo service or network is unavailable
     * @see weibo4andriod.StatusStream
     * @see <a href="http://open.t.sina.com.cn/wiki/Streaming-API-Documentation#firehose">Weibo API Wiki / Streaming API Documentation - shadow</a>
     * @since Weibo4J 2.0.4
     * @deprecated use filter() instead
     */
    public void shadow(int count, int[] follow) throws WeiboException {
        startHandler(new StreamHandlingThread(new Object[]{count, follow}) {
            public StatusStream getStream() throws WeiboException {
                return getShadowStream((Integer) args[0], (int[]) args[1]);
            }
        });
    }

    /**
     * See birddog above. Allows following up to 2,000 users.
     *
     * @param count  Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @param follow Specifies the users, by ID, to receive public tweets from.
     * @return StatusStream
     * @throws WeiboException when Weibo service or network is unavailable
     * @see weibo4andriod.StatusStream
     * @see <a href="http://open.t.sina.com.cn/wiki/Streaming-API-Documentation#firehose">Weibo API Wiki / Streaming API Documentation - shadow</a>
     * @since Weibo4J 2.0.4
     * @deprecated use filterStream() instead
     */
    public StatusStream getShadowStream(int count, int[] follow) throws WeiboException {
        return getFilterStream(count, follow, null);
    }

    /**
     * See birddog above. Allows following up to 200 users. Publicly available.
     *
     * @param follow Specifies the users, by ID, to receive public tweets from.
     * @throws WeiboException when Weibo service or network is unavailable
     * @see weibo4andriod.StatusStream
     * @see <a href="http://open.t.sina.com.cn/wiki/Streaming-API-Documentation#follow">Weibo API Wiki / Streaming API Documentation - follow</a>
     * @since Weibo4J 2.0.4
     * @deprecated use filter() instead
     */
    public void follow(int[] follow) throws WeiboException {
        startHandler(new StreamHandlingThread(new Object[]{follow}) {
            public StatusStream getStream() throws WeiboException {
                return getFollowStream((int[]) args[0]);
            }
        });
    }

    /**
     * See birddog above. Allows following up to 200 users. Publicly available.
     *
     * @param follow Specifies the users, by ID, to receive public tweets from.
     * @return StatusStream
     * @throws WeiboException when Weibo service or network is unavailable
     * @see weibo4andriod.StatusStream
     * @see <a href="http://open.t.sina.com.cn/wiki/Streaming-API-Documentation#follow">Weibo API Wiki / Streaming API Documentation - follow</a>
     * @since Weibo4J 2.0.4
     * @deprecated use filterStream() instead
     */
    public StatusStream getFollowStream(int[] follow) throws WeiboException {
        return getFilterStream(0, follow, null);
    }

    private String toFollowString(int[] follows) {
        StringBuffer buf = new StringBuffer(11 * follows.length);
        for (int follow : follows) {
            if (0 != buf.length()) {
                buf.append(",");
            }
            buf.append(follow);
        }
        return buf.toString();
    }

    /**
     * Returns public statuses that contain at least one of the specified keywords. 
     * Requires use of the "track" parameter, documented below. Allows following up 
     * to 20 keywords, subject to Track Limitations, described above. 
     * Predicate terms are case-insensitive logical ORs. Terms are exact-matched, 
     * and exact-matched ignoring punctuation. Publicly available.
     *
     * Examples: The predicate Weibo will match all public statuses with the 
     * following space delimited tokens in their text field: WEIBO, weibo, 
     * "Weibo", weibo., #weibo and @weibo. The following tokens will not be matched: 
     * WeiboTracker and http://open.t.sina.com.cn/,
     *
     * @throws WeiboException when Weibo service or network is unavailable
     * @see weibo4andriod.StatusStream
     * @see <a href="http://open.t.sina.com.cn/wiki/Streaming-API-Documentation#spritzer">Weibo API Wiki / Streaming API Documentation - spritizer</a>
     * @since Weibo4J 2.0.9
     * @deprecated use filter() instead
     */
    public void track(final String []keywords) throws WeiboException {
        startHandler(new StreamHandlingThread(null) {
            public StatusStream getStream() throws WeiboException {
                return getTrackStream(keywords);
            }
        });
    }

    /** @see #getTrackStream(String[])
     * @deprecated use filterStream() instead
     */
    public StatusStream getTrackStream(final String[] keywords) throws WeiboException {
        return getFilterStream(0, null, keywords);
    }
    
    
    private String toTrackString(final String[] keywords) {
        final StringBuffer buf = new StringBuffer(20 * keywords.length  * 4);
        for (String keyword : keywords) {
            if (0 != buf.length()) {
                buf.append(",");
            }
            buf.append(keyword);
        }
        return buf.toString();
    }
    
    private synchronized void startHandler(StreamHandlingThread handler) throws WeiboException {
        cleanup();
        if(null == statusListener){
            throw new IllegalStateException("StatusListener is not set.");
        }
        this.handler = handler;
        this.handler.start();
    }

    public synchronized void cleanup() {
        if (null != handler) {
            try {
                handler.close();
            } catch (IOException ignore) {
            }
        }
    }

    public StatusListener getStatusListener() {
        return statusListener;
    }

    public void setStatusListener(StatusListener statusListener) {
        this.statusListener = statusListener;
    }

    abstract class StreamHandlingThread extends Thread {
        StatusStream stream = null;
        Object[] args;
        private List<Long> retryHistory;
        private static final String NAME = "Weibo Stream Handling Thread";
        private boolean closed = false;

        StreamHandlingThread(Object[] args) {
            super(NAME + "[initializing]");
            this.args = args;
            retryHistory = new ArrayList<Long>(retryPerMinutes);
        }

        public void run() {
            Status status;
            while (!closed) {
                try {
                    // dispose outdated retry history
                    if(retryHistory.size() > 0){
                        if((System.currentTimeMillis() - retryHistory.get(0)) > 60000){
                            retryHistory.remove(0);
                        }
                    }
                    if(retryHistory.size() < retryPerMinutes){
                        // try establishing connection
                        setStatus("[establishing connection]");

                        while (!closed && null == stream) {
                            if (retryHistory.size() < retryPerMinutes) {
                                retryHistory.add(System.currentTimeMillis());
                                stream = getStream();
                            }
                        }
                    }else{
                        // exceeded retry limit, wait to a moment not to overload Weibo API
                        long timeToSleep = 60000 - (System.currentTimeMillis() - retryHistory.get(retryHistory.size() - 1));
                        setStatus("[retry limit reached. sleeping for " + (timeToSleep / 1000) + " secs]");
                        try {
                            Thread.sleep(timeToSleep);
                        } catch (InterruptedException ignore) {
                        }

                    }
                    if(null != stream){
                        // stream established
                        setStatus("[receiving stream]");
                        while (!closed && null != (status = stream.next())) {
                            log("received:", status.toString());
                            if (null != statusListener) {
                                statusListener.onStatus(status);
                            }
                        }
                    }
                } catch (WeiboException te) {
                    stream = null;
                    te.printStackTrace();
                    log(te.getMessage());
                    statusListener.onException(te);
                }
            }
        }

        public synchronized void close() throws IOException {
            setStatus("[disposing thread]");
            if (null != stream) {
                this.stream.close();
                closed = true;
            }
        }
        private void setStatus(String message){
            String actualMessage = NAME + message;
            setName(actualMessage);
            log(actualMessage);
        }

        abstract StatusStream getStream() throws WeiboException;

    }

    private String getStreamBaseURL() {
        return USE_SSL ? "https://stream.t.sina.com.cn/" : "http://stream.t.sina.com.cn/";
    }

    private void log(String message) {
        if (DEBUG) {
            System.out.println("[" + new java.util.Date() + "]" + message);
        }
    }

    private void log(String message, String message2) {
        if (DEBUG) {
            log(message + message2);
        }
    }
}
