package com.renren.api.connect.android.example.xml;

import org.xml.sax.helpers.DefaultHandler;

/**
 * @author 李勇 2010-5-4
 */
public abstract class RestHandler extends DefaultHandler {
	public abstract Object getResult();
}
