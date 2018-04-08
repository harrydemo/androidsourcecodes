package com.mzba.peng.util;

import android.content.Context;
import android.text.TextUtils;

/**
 * 
 * @author 06peng
 * 
 */
public class TextUtil {
	public static Context ctx_for_getResString;
	public static void setCtxForGetResString(Context ctx) {
		ctx_for_getResString = ctx;
	}
	/**
	 * 获取res字符串资源
	 * 
	 * @param ctx
	 * @param str
	 * @return
	 */
	public static String R(String str) {
		if (ctx_for_getResString == null)
			return str;
		String rs = "";
		int i = ctx_for_getResString.getResources().getIdentifier(str,
				"string", ctx_for_getResString.getPackageName());
		rs = ctx_for_getResString.getResources().getString(i);
		return rs;
	}
	
	public static String htmlEncode(String s) { 
        StringBuilder sb = new StringBuilder(); 
        char c; 
        for (int i = 0; i < s.length(); i++) { 
            c = s.charAt(i); 
            switch (c) { 
            case '<': 
                sb.append("&lt;"); //$NON-NLS-1$ 
                break; 
            case '>': 
                sb.append("&gt;"); //$NON-NLS-1$ 
                break; 
            case '&': 
                sb.append("&amp;"); //$NON-NLS-1$ 
                break; 
            case '\'': 
                sb.append("&apos;"); //$NON-NLS-1$ 
                break; 
            case '"': 
                sb.append("&quot;"); //$NON-NLS-1$ 
                break; 
            default: 
                sb.append(c); 
            } 
        } 
        return sb.toString(); 
    } 

	/**
	 * 检测英文，数字，下划线
	 * 
	 * @param Account
	 * @return
	 */
	public static boolean checkLoginAccount(String account) {
		boolean valid = true;
		if (TextUtils.isEmpty(account))
			return false;
		for (int i = 0; i < account.length(); i++) {
			char ch = account.charAt(i);
			if (Character.isLetterOrDigit(ch) || ch == '_') {
				continue;
			} else {
				valid = false;
				break;
			}
		}
		return valid;
	}

	public static boolean isValidEmail(String email) {
		if (TextUtils.isEmpty(email))
			return false;
		return email
				.matches("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");
	}
	
	public static boolean notEmpty(String str) {
		if (str != null && !str.equals("")) {
			return true;
		} else {
			return false;
		}
	}
}
