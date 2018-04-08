package cn.ingenic.gabriel.filmwind;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/*
 * example for user-defined item for ListActivity
 * mail to wangjianshi_1983@163.com for discussion
 */
public class FilmWind extends ListActivity {
	public static final String TAG = "FilmWind";
	private boolean DBG = Parameters.DEBUG;
	private Resources mResources;
	private ListView mListView;
	private static final int DIALOG_SYNOPSIS = 1;
	private String mDispData;
	private int[] mImgIds;
	private int mCurImgId;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mResources = getResources();
        initAdapter();
    }
    
    private void initAdapter() {
    	String[] adapterItems = {"item_image", "item_name", "item_icon"};
    	TypedArray ta = mResources.obtainTypedArray(R.array.wind_list_image);
    	final int tal = ta.length();
    	mImgIds = new int[tal];
    	final String[] imgName = mResources.getStringArray(R.array.wind_list_name);
    	// get data for adapter
    	ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
    	for(int i=0, len=tal; i<len; i++) {
    		HashMap<String, Object> map = new HashMap<String, Object>();
    		mImgIds[i] = ta.getResourceId(i, R.drawable.wind_list_image_all);
    		map.put(adapterItems[0], mImgIds[i]);
            map.put(adapterItems[1], imgName[i]);  
            map.put(adapterItems[2], R.drawable.wind_list_icon);  
            listItem.add(map);
    	}
    	ta.recycle();

    	GabrielAdapter adapter = new GabrielAdapter(FilmWind.this, mAdpBtnClickHandler,
				listItem, 
				R.layout.wind_list_item, 
				adapterItems, 
				new int[] {R.id.item_image, R.id.item_name, R.id.item_icon});
    	setListAdapter(adapter);

    	mListView = getListView();
    	mListView.setOnItemClickListener(mOnItemClickListener);   
    }
    
    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			if(DBG) Log.d(TAG, "position0: " + position + " id: " + id);
			Intent intent = new Intent();
			intent.setClass(FilmWind.this, Introduction.class);
			Bundle bundle = new Bundle();
			bundle.putInt(Parameters.BUNDLE_CLICK_POSITION, position);
			intent.putExtras(bundle);
			startActivity(intent);
		}       	
    };
    
    public Handler mAdpBtnClickHandler = new Handler() {
    	public void handleMessage(Message msg) {
    		switch(msg.what) {
    			case Parameters.ADAPTER_BUTTON_CLICK_MSG:
    				int pos = msg.arg1;
    				if(DBG) Log.d(TAG, "pos: " + pos);
    				mDispData = mResources.getStringArray(R.array.wind_list_btn_pop)[pos];
    				mCurImgId = mImgIds[pos];
    				showDialog(DIALOG_SYNOPSIS);
    				break;
    		}
    	}
    };
    
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		super.onPrepareDialog(id, dialog);
		switch(id) {
			case DIALOG_SYNOPSIS:
				AlertDialog ad = (AlertDialog) dialog;
				ad.setIcon(mCurImgId);				
				ad.setMessage(mDispData);
			break;
		}
		if(DBG) Log.d(TAG, "onPrepareDialog");
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch(id) {
			case DIALOG_SYNOPSIS:
				if(DBG) Log.d(TAG, "onCreateDialog");
				return new AlertDialog.Builder(FilmWind.this)
				.setIcon(mCurImgId)
				.setTitle(R.string.dialog_button_title)
				.setMessage(mDispData)
				.setNeutralButton(R.string.dialog_button_neutral, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialogInterface, int arg1) {
						if(DBG) Log.d(TAG, "arg1: " + arg1);
						dialogInterface.dismiss();
					}					
				})
				.create();
		}
		return null;
	}
		
}