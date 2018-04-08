package com.parabola.main;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 工具类，主要根据url读取图片返回流的方法 
 * @author Song Shi Chao
 *
 */
public class Request {
    public static InputStream HandlerData(String url) {
       InputStream inStream=null;

       try {
           URL feedUrl = new URL(url);
           URLConnection conn = feedUrl.openConnection();
           conn.setConnectTimeout(10 * 1000);
           inStream = conn.getInputStream();
       } catch (Exception e) {
           e.printStackTrace();
       }
       
       return inStream;
    }
}