/**
 * <This class for browser the html file.>
 *  Copyright (C) <2009>  <Wang XinFeng,ACC http://androidos.cc/dev>
 *
 *   This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
package cn.itcreator.android.reader;

import java.io.File;
import java.io.UnsupportedEncodingException;

import cn.itcreator.android.reader.io.ReadFileRandom;
import cn.itcreator.android.reader.paramter.Constant;
import cn.itcreator.android.reader.util.BytesEncodingDetect;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * This class for browser the html file
 * 
 * @author Wang XinFeng
 * @version 1.0
 * @since 2009-2-13
 */
public class HtmlBrowser extends Activity {
	private String _mFilePath = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.htmlbrowser);
		 Intent intent = getIntent();
			if(intent == null){
				finish();
				return;
			}
			Bundle bundle = intent.getExtras();
			if(bundle==null){
				finish();
				return;
			}
			_mFilePath = bundle.getString(Constant.FILE_PATH_KEY);
			if(_mFilePath==null || _mFilePath.equals("")){
				finish();
				return;
			}
			
		WebView wv = (WebView) findViewById(R.id.webview);

		ReadFileRandom readFileRandom = new ReadFileRandom(_mFilePath);
		File f = new File(_mFilePath);
		byte[] b = readFileRandom.readBytes(200);
		readFileRandom.close();
		readFileRandom.openNewStream();
		BytesEncodingDetect be = new BytesEncodingDetect();
		int ec = be.detectEncoding(b);
		String ecoding = BytesEncodingDetect.htmlname[ec];
		
		int fileLength = (int) f.length();
		b = readFileRandom.readBytes(fileLength);
		String data = "";
		try {
			data = new String(b,ecoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		wv.loadData(data, getString(R.string.html), ecoding);
		
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

		}
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

		}
		if (getResources().getConfiguration().keyboardHidden == Configuration.KEYBOARDHIDDEN_NO) {
			// ´ò¿ª¼üÅÌ
		}
		if (getResources().getConfiguration().keyboardHidden == Configuration.KEYBOARDHIDDEN_YES) {
			// ¹Ø±Õ¼üÅÌ
		}
	}
}
