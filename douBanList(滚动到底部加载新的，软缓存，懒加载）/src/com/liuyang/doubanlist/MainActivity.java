package com.liuyang.doubanlist;

import java.io.IOException;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.liuyang.doubanlist.AsyncImageLoader.ImageCallback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private ListView lv_main_books;//listView对象
	private LinearLayout ll_loading;//控制显示正在加载的progress
	private List<NewBook> list;//要显示的列表
	private boolean isScrolling = false;//是否正在滚动
	private SubjectListAdapter adapter;//数据适配器
	private boolean isloading = false;//判断是否正在加载中
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		lv_main_books = (ListView) this.findViewById(R.id.lv_main_books);
		ll_loading = (LinearLayout) this.findViewById(R.id.ll_main_progress);
		list = new ArrayList<NewBook>();
		adapter = new SubjectListAdapter();
		//第一次加载数据
		getData();
		//lv_main_books的setOnScrollListener主要是实现里面的方法进而判断是否处于滚动和是否已经滚动到最底
		lv_main_books.setOnScrollListener(new OnScrollListener() {
			//三种不同状态
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_FLING:
					isScrolling = true;
					break;
				case OnScrollListener.SCROLL_STATE_IDLE:
					isScrolling = false;
					int startindex = lv_main_books.getFirstVisiblePosition();
					int count = lv_main_books.getChildCount();
					for (int i = 0; i < count; i++) {
						int currentpostion = startindex + i;
						final NewBook book = (NewBook) lv_main_books
								.getItemAtPosition(currentpostion);
						final View viewchildren = lv_main_books.getChildAt(i);
						ImageView iv_icon = (ImageView) viewchildren.findViewById(R.id.iv_icon);
						Drawable drawable = NetUtil.asyncImageLoader.loadDrawable(book.getBookPicturePath(),
								new ImageCallback() {
									public void imageLoaded(Drawable imageDrawable,
											String imageUrl) {
										ImageView imageViewByTag = (ImageView) lv_main_books
												.findViewWithTag(imageUrl);
										if (imageViewByTag != null) {
											imageViewByTag.setImageDrawable(imageDrawable);
										}
									}
								});

						if (drawable != null) {
							iv_icon.setImageDrawable(drawable);
						} else {
							iv_icon.setImageResource(R.drawable.ic_launcher);
						}
					}
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					isScrolling = true;
					break;
				}
			}
			//是否已到最底
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				 if (totalItemCount <= 0){
                     return;
             }
				if(firstVisibleItem+visibleItemCount ==totalItemCount ){
					if(isloading){
						return;
					}
					new AsyncTask<Void, String, List<NewBook>>() {
						protected List<NewBook> doInBackground(Void... params) {
							List<NewBook> listNewBooks = null;
							try {
								listNewBooks = NewBookDao.getAllNewBooks();

							} catch (Exception e) {
								publishProgress("获取新书失败,请稍后再试。。。");
								e.printStackTrace();
							}
							return listNewBooks;
						}

						protected void onPreExecute() {
							ll_loading.setVisibility(View.VISIBLE);
							isloading = true;
							super.onPreExecute();
						}

						protected void onPostExecute(List<NewBook> result) {
							list.addAll(result);
							ll_loading.setVisibility(View.GONE);
								System.out.println("更新adapter");
								adapter.notifyDataSetChanged();
							System.out.println("一共有" + list.size() + "本书");
							isloading = false;
							super.onPostExecute(result);
						}

						protected void onProgressUpdate(String... values) {
							Toast.makeText(MainActivity.this, values[0], Toast.LENGTH_SHORT)
									.show();
							super.onProgressUpdate(values);
						}

					}.execute();
				}
				
			}
		});
	}
	//获取第一次显示的数据
	private void getData() {
		new AsyncTask<Void, String, List<NewBook>>() {
			protected List<NewBook> doInBackground(Void... params) {
				List<NewBook> listNewBooks = null;
				try {
					listNewBooks = NewBookDao.getAllNewBooks();

				} catch (Exception e) {
					publishProgress("获取新书失败,请稍后再试。。。");
					e.printStackTrace();
				}
				return listNewBooks;
			}

			protected void onPreExecute() {
				ll_loading.setVisibility(View.VISIBLE);
				super.onPreExecute();
			}

			protected void onPostExecute(List<NewBook> result) {
				list=result;
				ll_loading.setVisibility(View.GONE);
					lv_main_books.setAdapter(adapter);
				System.out.println("一共有" + list.size() + "本书");
				super.onPostExecute(result);
			}

			protected void onProgressUpdate(String... values) {
				Toast.makeText(MainActivity.this, values[0], Toast.LENGTH_SHORT)
						.show();
				super.onProgressUpdate(values);
			}

		}.execute();
	}
	private class SubjectListAdapter extends BaseAdapter{
		
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View view = null;
			ViewCache viewCache;
			ViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				view = View.inflate(MainActivity.this, R.layout.main_item, null);
				viewCache = new ViewCache(view);
				view.setTag(R.id.tag_first,viewCache);
				viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
				viewHolder.tv_message = (TextView) view.findViewById(R.id.tv_message);
				viewHolder.tv_synopsis = (TextView) view.findViewById(R.id.tv_synopsis);
				view.setTag(R.id.tag_second,viewHolder);
			} else {
				view = convertView;
				viewHolder = (ViewHolder) view.getTag(R.id.tag_second);
				viewCache = (ViewCache) view.getTag(R.id.tag_first);
			}
			NewBook newBook = list.get(position);
			viewHolder.tv_name.setText(newBook.getBookName());
			viewHolder.tv_message.setText(newBook.getBookMessage());
			viewHolder.tv_synopsis.setText(newBook.getBookSynopsis());
			String imgUrl = newBook.getBookPicturePath();
			ImageView imgBook = viewCache.getImageView();
			imgBook.setTag(imgUrl);
			if(isScrolling){//滑动式加载本地的假图片
				imgBook.setImageResource(R.drawable.ic_launcher);
			}else{//静止时下载网上的真图片
				Drawable drawable = NetUtil.asyncImageLoader.loadDrawable(imgUrl,
						new ImageCallback() {
							public void imageLoaded(Drawable imageDrawable,
									String imageUrl) {
								ImageView imageViewByTag = (ImageView) lv_main_books
										.findViewWithTag(imageUrl);
								if (imageViewByTag != null) {
									imageViewByTag.setImageDrawable(imageDrawable);
								}
							}
						});

				if (drawable != null) {
					imgBook.setImageDrawable(drawable);
				} else {
					imgBook.setImageResource(R.drawable.ic_launcher);
				}
			}
			
			return view;
		}
		
	}
	static class ViewHolder {
		ImageView iv_icon;
		TextView tv_name;
		TextView tv_message;
		TextView tv_synopsis;

	}
	
}