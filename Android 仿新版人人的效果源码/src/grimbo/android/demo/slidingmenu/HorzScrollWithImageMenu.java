package grimbo.android.demo.slidingmenu;

import java.util.ArrayList;
import grimbo.android.demo.adapter.PurpleAdapter;
import grimbo.android.demo.adapter.PurpleEntry;
import grimbo.android.demo.adapter.PurpleListener;
import grimbo.android.demo.adapter.SeparatedListAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class HorzScrollWithImageMenu extends Activity {
	public class MyGestureDetector extends SimpleOnGestureListener {
		
		@Override
		   public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
	       {
	           try
	           {
	               float touchLength = Math.abs(e1.getY() - e2.getY());

	               System.out.println("touchLength="+touchLength);
	               if (touchLength > SWIPE_MAX_OFF_PATH)
	                   return false;
//	               从右向左滑动
	               if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
	               {
	            	   System.out.println("SSSSSSSSSSSleftS");
	            	   int menuWidth = menu.getMeasuredWidth();
						menu.setVisibility(View.VISIBLE);
						int left = menuWidth;
						scrollView.smoothScrollTo(left, 0);
	               }
	               else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
	               {
	            	  
						
						int left = 0;
	        			scrollView.smoothScrollTo(left, 0);
	        			System.out.println("SSSSSSSSSSSleftS"+left);
	               }
	           }
	           catch (Exception e)
	           {
	               Log.e("detector", "excetpion:" + e.getMessage());
	           }
	           return false;
	       }
	}
	  @Override
	   public boolean onTouchEvent(MotionEvent event)
	   {
	       if (gestureDetector.onTouchEvent(event))
	           return true;
	       else
	           return false;
	   }
	
	MyHorizontalScrollView scrollView;
	View menu;
	View app;
	ImageView btnSlide;
	boolean menuOut = true;
	Handler handler = new Handler();
	int btnWidth;
	private PurpleAdapter mBrowseJamendoPurpleAdapter;
	private PurpleAdapter mMyLibraryPurpleAdapter;
	private ArrayList<PurpleEntry> browseListEntry;
	private ArrayList<PurpleEntry> libraryListEntry;
	private View mHomeListView;
	private GestureDetector gestureDetector;
	ListView.OnTouchListener gestureListener;
	 private static final int SWIPE_MIN_DISTANCE = 120;
	 private static final int SWIPE_MAX_OFF_PATH = 250;
	 private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LayoutInflater inflater = LayoutInflater.from(this);
		setContentView(inflater.inflate(R.layout.horz_scroll_with_image_menu,
				null));
		// MyHorizontalScrollView 自定义view
		scrollView = (MyHorizontalScrollView) findViewById(R.id.myScrollView);
		// @drawable/fb_menu黑色的那张图片
		menu = findViewById(R.id.menu);

		app = inflater.inflate(R.layout.horz_scroll_app, null);

		// 点击滑动 tabBar包括图片和textview
		ViewGroup tabBar = (ViewGroup) app.findViewById(R.id.tabBar);

		ListView listView = (ListView) app.findViewById(R.id.list);
		ViewUtils.initListView(this, listView, "Item ", 30,
				android.R.layout.simple_list_item_1);
		btnSlide = (ImageView) tabBar.findViewById(R.id.BtnSlide);
		
		listWindow();
		
		 gestureDetector = new GestureDetector(new MyGestureDetector());
		   gestureListener = new View.OnTouchListener()
		   {
		       public boolean onTouch(View v, MotionEvent event)
		       {
		           if (gestureDetector.onTouchEvent(event))
		           {
		               return true;
		           }
		           return false;
		       }
		   };
		
		btnSlide.setOnClickListener(new ClickListenerForScrolling(scrollView,
				menu));
		View transparent = new TextView(this);
		transparent.setBackgroundColor(android.R.color.transparent);
		final View[] children = new View[] { transparent, app };
		int scrollToViewIdx = 2;
		// scrollView = (MyHorizontalScrollView)
		// findViewById(R.id.myScrollView);
		// 显示为图片的旁边的一道
		scrollView.initViews(children, scrollToViewIdx,
				new SizeCallbackForMenu(btnSlide));
	}

	private void listWindow() {
		mHomeListView = findViewById(R.id.HomeListView);
		mBrowseJamendoPurpleAdapter = new PurpleAdapter(
				HorzScrollWithImageMenu.this);
		mMyLibraryPurpleAdapter = new PurpleAdapter(
				HorzScrollWithImageMenu.this);
		browseListEntry = new ArrayList<PurpleEntry>();
		libraryListEntry = new ArrayList<PurpleEntry>();
		browseListEntry.add(new PurpleEntry(R.drawable.desktop_list_newsfeed,
				R.string.search, new PurpleListener() {
					@Override
					public void performAction() {
						int menuWidth = menu.getMeasuredWidth();
						menu.setVisibility(View.VISIBLE);
						int left = menuWidth;
						scrollView.smoothScrollTo(left, 0);

					}
				}));
		browseListEntry.add(new PurpleEntry(R.drawable.desktop_list_message,
				R.string.message, new PurpleListener() {
					@Override
					public void performAction() {

					}
				}));
		browseListEntry.add(new PurpleEntry(R.drawable.desktop_list_message,
				R.string.most_listened, new PurpleListener() {
					@Override
					public void performAction() {

					}
				}));
		libraryListEntry.add(new PurpleEntry(R.drawable.desktop_list_message,
				R.string.download, new PurpleListener() {
					@Override
					public void performAction() {

					}
				}));

		mBrowseJamendoPurpleAdapter.setList(browseListEntry);
		mMyLibraryPurpleAdapter.setList(libraryListEntry);
		SeparatedListAdapter separatedAdapter = new SeparatedListAdapter(
				HorzScrollWithImageMenu.this);
		separatedAdapter.addSection(getString(R.string.browse_jamendo),
				mBrowseJamendoPurpleAdapter);
		separatedAdapter.addSection(getString(R.string.my_library),
				mMyLibraryPurpleAdapter);

		((ListView) mHomeListView).setAdapter(separatedAdapter);
		((ListView) mHomeListView)
				.setOnItemClickListener(mHomeItemClickListener);
	}

	private OnItemClickListener mHomeItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view,
				int index, long time) {
			try {
				PurpleListener listener = ((PurpleEntry) adapterView
						.getAdapter().getItem(index)).getListener();
				if (listener != null) {
					listener.performAction();
				}
			} catch (ClassCastException e) {

			}
		}
	};
	
}
