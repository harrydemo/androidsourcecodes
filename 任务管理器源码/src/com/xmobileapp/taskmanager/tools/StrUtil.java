/*
 * [程序名称] Android 任务管理器
 * [作者] xmobileapp团队
 * [参考资料] http://code.google.com/p/freetaskmanager/ 
 * [开源协议] GNU General Public License v2 (http://www.gnu.org/licenses/old-licenses/gpl-2.0.html)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 */

package com.xmobileapp.taskmanager.tools;

import java.util.Calendar;
import java.util.Random;

public final class StrUtil {
    
    /**
     * revert a string
     * @param from the string
     * @return reverted string
     */
    public static String revert(String from) {
        if (from == null || from.length() <= 1) return from;
        int len = from.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 1; i <= len; i++) {
            sb.append(from.charAt(len - i));
        }
        return sb.toString();
    }
    
    private static int ASCII_KEY_LENTH = 126 - 33;
    public static String generatePassword(int length) {
        StringBuilder sb = new StringBuilder(length);
        Random r = new Random();
        for (int i = 0; i < length; i++) {
            sb.append((char)(33 + r.nextInt(ASCII_KEY_LENTH)));
        }
        return sb.toString();
    }
    
    public static int parseInt(String s) {
        return parseInt(s, 0);
    }

    public static int parseInt(Object s) {
        return parseInt(s == null ? null : s.toString(), 0);
    }

    public static int parseInt(Object s, int iDefault) {
        if (s == null) return iDefault;
        if (s instanceof String) return parseInt((String) s, iDefault);
        return parseInt(s.toString(), iDefault);
    }

    public static int parseInt(String s, int iDefault) {
        return (int) parseLong(s, iDefault);
    }

    public static long parseLong(String s) {
        return parseLong(s, 0L);
    }

    public static long parseLong(String s, long iDefault) {
        if (s == null || s.equals("")) return iDefault;
        try {
            s = s.trim().replaceAll(",", "");
            int l = s.indexOf(".");
            if (l > 0) s = s.substring(0, l);
            return Long.parseLong(s);
        } catch (Exception e) {
            return iDefault;
        }
    }
    
    public static String formatShortDate(long time) {
        Calendar now = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        String ret;
        long diff = now.getTimeInMillis() / (1000 * 60 * 60 * 24) - time / (1000 * 60 * 60 * 24); 
        if (diff <= 0) {
             ret = String.format("%tR", cal);
        } else {
            ret = String.format("%tb %td", cal, cal);
        }
        return ret;
    }

}
