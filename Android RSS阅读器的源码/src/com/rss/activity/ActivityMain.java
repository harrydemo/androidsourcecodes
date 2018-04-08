package com.rss.activity;

import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnKeyListener;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Selection;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.rss.data.RssFeed;
import com.rss.sax.RssHandler;

public class ActivityMain extends ListActivity {
	public final String RSS_URL = "http://www.naivix.com/hn/rss.xml";
	public final String TAG = "RssReader";
	private RssFeed feed;
	private Button addRss;
	private Button verifyRss;
	private Button quit;
	
	//new
	private List<Map<String, Object>> mList;
	
	private static final int MENU_ADD = Menu.FIRST;
	private static final int RSS_AND = MENU_ADD + 1;
	
	private Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);

       
        feed = getFeed(RSS_URL);       
        
        showListView();    
    }
    
    private RssFeed getFeed(String urlString) {
    	try {
    		URL url = new URL(urlString);
    		
    		SAXParserFactory factory = SAXParserFactory.newInstance();
    		SAXParser parser = factory.newSAXParser();
    		XMLReader xmlReader = parser.getXMLReader();
    		
    		RssHandler rssHandler = new RssHandler();
    		xmlReader.setContentHandler(rssHandler);
    		
    		InputSource is = new InputSource(url.openStream());
    		System.out.println(is.toString());
    		xmlReader.parse(is);
    		
    		return rssHandler.getFeed();
    	}catch (Exception e) {
			return null;
		}
    }

	@SuppressWarnings("unchecked")
	private void showListView() {
    	if(feed == null) {
    		setTitle("RSS�Ķ���");
    		return;
    	}
    	
    	mList = feed.getAllItemsForListView();    	
    	MyAdapter adapter = new MyAdapter(this);
    	setListAdapter(adapter);
    	setSelection(0);
	}
    
	//���menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_ADD, 0, R.string.add_rss);
		return super.onCreateOptionsMenu(menu);
	}		
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_ADD:
			Intent intent = new Intent();
			intent.setClass(ActivityMain.this, AddRss.class);
			startActivity(intent);
			return true;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}	

	class MyAdapter extends BaseAdapter  {
		private LayoutInflater mInflator;
		
		public MyAdapter(Context context) {
			this.mInflator = LayoutInflater.from(context);
		}
		
		public int getCount() {
			System.out.println("mList.size()=" + mList.size());
			return mList.size();
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewRss vRss = null;
			final int row = position;
			if(convertView == null) {
				vRss = new ViewRss();
				convertView = mInflator.inflate(R.layout.main2, null);
				
				vRss.title = (TextView)convertView.findViewById(R.id.title);
				vRss.pubdate = (TextView)convertView.findViewById(R.id.pubdate);				
				vRss.delBtn = (Button)convertView.findViewById(R.id.del_btn);
				
				convertView.setTag(vRss);
			} else {
				vRss = (ViewRss)convertView.getTag();
			}
			String pubDate =  (String) mList.get(position).get("pubDate"); 
			String title = (String)mList.get(position).get("title");
			vRss.title.setText(title);
			vRss.pubdate.setText(pubDate);					
				
			vRss.delBtn.setOnClickListener(new OnClickListener() {				
				public void onClick(View v) {
					delRssInfo();					
				}

				private void delRssInfo() {
					mList.remove(row);
					notifyDataSetChanged();					
				}

			});
			
			vRss.title.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					Intent itemintent = new Intent(ActivityMain.this, ActivityShowDescription.class);
					Bundle b = new Bundle();
					b.putString("title", feed.getItem(row).getTitle());
					b.putString("description", feed.getItem(row).getDescription());
					b.putString("link", feed.getItem(row).getLink());
					b.putString("pubdate", feed.getItem(row).getPubDate());
					
					System.out.println("description:" +  feed.getItem(row).getDescription());
					itemintent.putExtra("com.rss.data.RssFeed", b);
					startActivity(itemintent);
					
				}
			});
			return convertView;
		}
		
	}
	
	public final class ViewRss {
		public TextView title;
		public TextView pubdate;
		public Button delBtn;
	}
	
}