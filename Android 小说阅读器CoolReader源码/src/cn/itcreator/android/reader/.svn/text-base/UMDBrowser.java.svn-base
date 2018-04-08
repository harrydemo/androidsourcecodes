/**
 * <This class for UMD browser .>
 *  Copyright (C) <2009>  <mingkg21,ACC http://androidos.cc/dev>
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
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery.LayoutParams;
import cn.itcreator.android.reader.paramter.Constant;
import cn.itcreator.android.reader.util.UMDFile;

/**
 * @author mingkg21
 * Date: 2009-4-8
 */
public class UMDBrowser extends Activity {

	/** Called when the activity is first created. */
	
	private static final String TAG = "UMDReader";
	
	private UMDFile umdFile;
	private int pageNum;
	private int pageIndex;
	
	private ListView catalogLV;
	private ImageSwitcher switcher;
	private List<Integer> thumbIds = new ArrayList<Integer>();
	private String _mFilePath = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_info);
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
		
        Log.i(TAG, "UMDBrowser start");
        parseUMD();
        
//        writeFile();
//        readFile();
        
        upateView();
    }

    @Override
    protected void onStop() {
    	// TODO Auto-generated method stub
    	super.onStop();
    	
    	try {
			String folderPath = "/sdcard/cr_temp";
//			File cr_temp = new File(folderPath);
//			if(cr_temp.exists()){
//				cr_temp.delete();
//			}
			remoeFile(folderPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void remoeFile(String path){
    	removeFile(new File(path));
    }
    
    private void removeFile(File path){
    	if(!path.exists()){
    		return;
    	}
    	if(path.isDirectory()){
    		File[] child = path.listFiles();
    		if(child != null && child.length != 0){
    			for(int i = 0; i < child.length; ++i){
    				removeFile(child[i]);
    				child[i].delete();
    			}
    		}
    	}
    	path.delete();
    }
    
    private void upateView(){
    	if(umdFile != null && umdFile.bookInfo != null){
    		ImageView cover = (ImageView)findViewById(R.id.book_cover_image);
    		cover.setImageBitmap(umdFile.getCoverImage());
    		cover.setAdjustViewBounds(true);
    		
    		TextView bookTitleTV = (TextView)findViewById(R.id.book_title);
    		bookTitleTV.setText(umdFile.bookInfo.title);
    		TextView bookAuthorTV = (TextView)findViewById(R.id.book_author);
    		bookAuthorTV.setText(umdFile.bookInfo.author);
    		TextView bookDateTV = (TextView)findViewById(R.id.book_date);
    		bookDateTV.setText(umdFile.bookInfo.getDate());
    		TextView bookGenderTV = (TextView)findViewById(R.id.book_gender);
    		bookGenderTV.setText(umdFile.bookInfo.gender);
    		TextView bookPublisherTV = (TextView)findViewById(R.id.book_publisher);
    		bookPublisherTV.setText(umdFile.bookInfo.publisher);
    		
    		catalogLV = (ListView)findViewById(R.id.book_catalog_list);
    		ArrayList<String> arrayList;
    		if(umdFile.getChapterSize() > 0){
    			arrayList = umdFile.getChapters();
    		}else{
    			arrayList = new ArrayList<String>();
    			arrayList.add(getString(R.string.openRead));
    		}
    		CatalogAdapter catalogAdapter = new CatalogAdapter(this, R.layout.catalog_row, arrayList);
    		catalogLV.setAdapter(catalogAdapter);
    		catalogLV.setOnItemClickListener(listener);
    	}
    }
    
    private OnItemClickListener listener = new OnItemClickListener(){

		public void onItemClick(AdapterView<?> arg0, View arg1, int index,
				long arg3) {
			// TODO Auto-generated method stub
			if(UMDFile.UMD_BOOK_TYPE_PICTURE == umdFile.bookInfo.type){
				showPictrueContent();
			}else{
				System.out.println("index " + index);
				openText(index);
			}
		}
    	
    };
    
    private void openText(int index){
    	setProgressBarIndeterminateVisibility(false);
    	String txtFilePath = "/sdcard/cr_temp/crbook" + index + ".txt";
    	System.out.println("file path " + txtFilePath);
    	Intent i = new Intent();
    	i.putExtra(Constant.FILE_PATH_KEY, txtFilePath);
		i.setClass(getApplicationContext(),	TxtActivity.class);
		startActivity(i);
		setProgressBarIndeterminateVisibility(true);
    }
    
	private void parseUMD(){
		umdFile = new UMDFile();
		umdFile.read(_mFilePath);
		pageNum = umdFile.getContentSize();
		System.out.println("pageNum " + pageNum);
		if(UMDFile.UMD_BOOK_TYPE_TEXT == umdFile.bookInfo.type){
			umdFile.writeFile(this);
		}
    }
	
	private int currentShowIndex;
	
	private void showPictrueContent(){
		currentShowIndex = 0;
		setContentView(R.layout.picturebrowser);
		switcher = (ImageSwitcher) findViewById(R.id.switcher);
		switcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
		switcher.setOutAnimation(AnimationUtils.loadAnimation(this,	android.R.anim.fade_out));
		fillImage();
		Gallery gallery = (Gallery) findViewById(R.id.gallery);
		switcher.setBackgroundDrawable(new BitmapDrawable(umdFile.getBitmap(pageIndex)));
		ImageAdapter ia = new ImageAdapter(this);
		gallery.setAdapter(ia);
		gallery.setOnItemSelectedListener(galleryListener);
	}
	
	private OnItemSelectedListener galleryListener = new OnItemSelectedListener(){

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			currentShowIndex = arg2;
			switcher.setBackgroundDrawable(new BitmapDrawable(umdFile.getBitmap(arg2)));
			System.gc();
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
//	private void changeImageList(){
//		if(pageIndex - 3 >= 0){
//			thumbIds.remove(0);
//		}
//		if(pageIndex + 2 < pageNum){
//			thumbIds.add(pageIndex + 2);
//		}
//	}
	
	private void fillImage(){
		System.out.println("fill Image");
		for(int i = 0; i < pageNum; ++i){
			thumbIds.add(i);
		}
	}
	
	public class ImageAdapter extends BaseAdapter {
		public ImageAdapter(Context c) {
			context = c;
		}

		public int getCount() {
			return thumbIds.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView i = new ImageView(context);
			System.out.println("postion " + position);
			Drawable drawable = new BitmapDrawable(umdFile.getBitmap(thumbIds.get(position)));
//			i.setImageBitmap(umdFile.getBitmap(thumbIds.get(position)));
			i.setImageDrawable(drawable);
			i.setAdjustViewBounds(true);
			if(currentShowIndex != position){
			}
			i.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			return i;
		}

		private Context context;

	}
}