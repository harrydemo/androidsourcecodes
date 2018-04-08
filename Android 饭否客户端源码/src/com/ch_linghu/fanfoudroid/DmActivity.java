package com.ch_linghu.fanfoudroid;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ch_linghu.fanfoudroid.app.LazyImageLoader.ImageLoaderCallback;
import com.ch_linghu.fanfoudroid.app.Preferences;
import com.ch_linghu.fanfoudroid.data.Dm;
import com.ch_linghu.fanfoudroid.db.MessageTable;
import com.ch_linghu.fanfoudroid.db.TwitterDatabase;
import com.ch_linghu.fanfoudroid.fanfou.DirectMessage;
import com.ch_linghu.fanfoudroid.fanfou.Paging;
import com.ch_linghu.fanfoudroid.http.HttpException;
import com.ch_linghu.fanfoudroid.task.GenericTask;
import com.ch_linghu.fanfoudroid.task.TaskAdapter;
import com.ch_linghu.fanfoudroid.task.TaskListener;
import com.ch_linghu.fanfoudroid.task.TaskParams;
import com.ch_linghu.fanfoudroid.task.TaskResult;
import com.ch_linghu.fanfoudroid.ui.base.BaseActivity;
import com.ch_linghu.fanfoudroid.ui.base.Refreshable;
import com.ch_linghu.fanfoudroid.ui.module.Feedback;
import com.ch_linghu.fanfoudroid.ui.module.FeedbackFactory;
import com.ch_linghu.fanfoudroid.ui.module.FeedbackFactory.FeedbackType;
import com.ch_linghu.fanfoudroid.ui.module.NavBar;
import com.ch_linghu.fanfoudroid.ui.module.SimpleFeedback;
import com.ch_linghu.fanfoudroid.util.DateTimeHelper;
import com.ch_linghu.fanfoudroid.util.TextHelper;
import com.ch_linghu.fanfoudroid.R;

public class DmActivity extends BaseActivity implements Refreshable {

	private static final String TAG = "DmActivity";

	// Views.
	private ListView mTweetList;
	private Adapter mAdapter;
	private Adapter mInboxAdapter;
	private Adapter mSendboxAdapter;

	Button inbox;
	Button sendbox;
	Button newMsg;

	private int mDMType;
	private static final int DM_TYPE_ALL = 0;
	private static final int DM_TYPE_INBOX = 1;
	private static final int DM_TYPE_SENDBOX = 2;

	private TextView mProgressText;

	private NavBar mNavbar;
	private Feedback mFeedback;

	// Tasks.
	private GenericTask mRetrieveTask;
	private GenericTask mDeleteTask;

	private TaskListener mDeleteTaskListener = new TaskAdapter() {
		@Override
		public void onPreExecute(GenericTask task) {
			updateProgress(getString(R.string.page_status_deleting));
		}

		@Override
		public void onPostExecute(GenericTask task, TaskResult result) {
			if (result == TaskResult.AUTH_ERROR) {
				logout();
			} else if (result == TaskResult.OK) {
				mAdapter.refresh();
			} else {
				// Do nothing.
			}

			updateProgress("");
		}

		@Override
		public String getName() {
			return "DmDeleteTask";
		}
	};
	private TaskListener mRetrieveTaskListener = new TaskAdapter() {
		@Override
		public void onPreExecute(GenericTask task) {
			updateProgress(getString(R.string.page_status_refreshing));
		}

		@Override
		public void onProgressUpdate(GenericTask task, Object params) {
			draw();
		}

		@Override
		public void onPostExecute(GenericTask task, TaskResult result) {
			if (result == TaskResult.AUTH_ERROR) {
				logout();
			} else if (result == TaskResult.OK) {
				SharedPreferences.Editor editor = mPreferences.edit();
				editor.putLong(Preferences.LAST_DM_REFRESH_KEY,
						DateTimeHelper.getNowTime());
				editor.commit();
				draw();
				goTop();
			} else {
				// Do nothing.
			}

			updateProgress("");
		}

		@Override
		public String getName() {
			return "DmRetrieve";
		}
	};

	// Refresh data at startup if last refresh was this long ago or greater.
	private static final long REFRESH_THRESHOLD = 5 * 60 * 1000;
	private static final String EXTRA_USER = "user";
	private static final String LAUNCH_ACTION = "com.ch_linghu.fanfoudroid.DMS";

	public static Intent createIntent() {
		return createIntent("");
	}

	public static Intent createIntent(String user) {
		Intent intent = new Intent(LAUNCH_ACTION);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		if (!TextUtils.isEmpty(user)) {
			intent.putExtra(EXTRA_USER, user);
		}

		return intent;
	}

	@Override
	protected boolean _onCreate(Bundle savedInstanceState) {
		if (super._onCreate(savedInstanceState)) {
			setContentView(R.layout.dm);
			mNavbar = new NavBar(NavBar.HEADER_STYLE_HOME, this);
			mNavbar.setHeaderTitle("我的私信");

			mFeedback = FeedbackFactory.create(this, FeedbackType.PROGRESS);

			bindFooterButtonEvent();

			mTweetList = (ListView) findViewById(R.id.tweet_list);
			mProgressText = (TextView) findViewById(R.id.progress_text);

			TwitterDatabase db = getDb();
			// Mark all as read.
			db.markAllDmsRead();

			setupAdapter(); // Make sure call bindFooterButtonEvent first

			boolean shouldRetrieve = false;

			long lastRefreshTime = mPreferences.getLong(
					Preferences.LAST_DM_REFRESH_KEY, 0);
			long nowTime = DateTimeHelper.getNowTime();

			long diff = nowTime - lastRefreshTime;
			Log.d(TAG, "Last refresh was " + diff + " ms ago.");

			if (diff > REFRESH_THRESHOLD) {
				shouldRetrieve = true;
			} else if (isTrue(savedInstanceState, SIS_RUNNING_KEY)) {
				// Check to see if it was running a send or retrieve task.
				// It makes no sense to resend the send request (don't want
				// dupes)
				// so we instead retrieve (refresh) to see if the message has
				// posted.
				Log.d(TAG,
						"Was last running a retrieve or send task. Let's refresh.");
				shouldRetrieve = true;
			}

			if (shouldRetrieve) {
				doRetrieve();
			}

			// Want to be able to focus on the items with the trackball.
			// That way, we can navigate up and down by changing item focus.
			mTweetList.setItemsCanFocus(true);

			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkIsLogedIn();
	}

	private static final String SIS_RUNNING_KEY = "running";

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (mRetrieveTask != null
				&& mRetrieveTask.getStatus() == GenericTask.Status.RUNNING) {
			outState.putBoolean(SIS_RUNNING_KEY, true);
		}
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestroy.");

		if (mRetrieveTask != null
				&& mRetrieveTask.getStatus() == GenericTask.Status.RUNNING) {
			mRetrieveTask.cancel(true);
		}
		if (mDeleteTask != null
				&& mDeleteTask.getStatus() == GenericTask.Status.RUNNING) {
			mDeleteTask.cancel(true);
		}

		super.onDestroy();
	}

	// UI helpers.

	private void bindFooterButtonEvent() {
		inbox = (Button) findViewById(R.id.inbox);
		sendbox = (Button) findViewById(R.id.sendbox);
		newMsg = (Button) findViewById(R.id.new_message);

		inbox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mDMType != DM_TYPE_INBOX) {
					mDMType = DM_TYPE_INBOX;
					inbox.setEnabled(false);
					sendbox.setEnabled(true);
					mTweetList.setAdapter(mInboxAdapter);
					mInboxAdapter.refresh();
				}
			}
		});

		sendbox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mDMType != DM_TYPE_SENDBOX) {
					mDMType = DM_TYPE_SENDBOX;
					inbox.setEnabled(true);
					sendbox.setEnabled(false);
					mTweetList.setAdapter(mSendboxAdapter);
					mSendboxAdapter.refresh();
				}
			}
		});

		newMsg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(DmActivity.this, WriteDmActivity.class);
				intent.putExtra("reply_to_id", 0); // TODO: 传入实际的reply_to_id
				startActivity(intent);
			}
		});
	}

	private void setupAdapter() {
		Cursor cursor = getDb().fetchAllDms(-1);
		startManagingCursor(cursor);
		mAdapter = new Adapter(this, cursor);

		Cursor inboxCursor = getDb().fetchInboxDms();
		startManagingCursor(inboxCursor);
		mInboxAdapter = new Adapter(this, inboxCursor);

		Cursor sendboxCursor = getDb().fetchSendboxDms();
		startManagingCursor(sendboxCursor);
		mSendboxAdapter = new Adapter(this, sendboxCursor);

		mTweetList.setAdapter(mInboxAdapter);
		registerForContextMenu(mTweetList);

		inbox.setEnabled(false);
	}

	private class DmRetrieveTask extends GenericTask {
		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			List<DirectMessage> dmList;

			ArrayList<Dm> dms = new ArrayList<Dm>();

			TwitterDatabase db = getDb();
			// ImageManager imageManager = getImageManager();

			String maxId = db.fetchMaxDmId(false);

			HashSet<String> imageUrls = new HashSet<String>();

			try {
				if (maxId != null) {
					Paging paging = new Paging(maxId);
					dmList = getApi().getDirectMessages(paging);
				} else {
					dmList = getApi().getDirectMessages();
				}
			} catch (HttpException e) {
				Log.e(TAG, e.getMessage(), e);
				return TaskResult.IO_ERROR;
			}
			publishProgress(SimpleFeedback.calProgressBySize(40, 20, dmList));

			for (DirectMessage directMessage : dmList) {
				if (isCancelled()) {
					return TaskResult.CANCELLED;
				}

				Dm dm;

				dm = Dm.create(directMessage, false);
				dms.add(dm);
				imageUrls.add(dm.profileImageUrl);

				if (isCancelled()) {
					return TaskResult.CANCELLED;
				}
			}

			maxId = db.fetchMaxDmId(true);

			try {
				if (maxId != null) {
					Paging paging = new Paging(maxId);
					dmList = getApi().getSentDirectMessages(paging);
				} else {
					dmList = getApi().getSentDirectMessages();
				}
			} catch (HttpException e) {
				Log.e(TAG, e.getMessage(), e);
				return TaskResult.IO_ERROR;
			}

			for (DirectMessage directMessage : dmList) {
				if (isCancelled()) {
					return TaskResult.CANCELLED;
				}

				Dm dm;

				dm = Dm.create(directMessage, true);
				dms.add(dm);
				imageUrls.add(dm.profileImageUrl);

				if (isCancelled()) {
					return TaskResult.CANCELLED;
				}
			}

			db.addDms(dms, false);

			// if (isCancelled()) {
			// return TaskResult.CANCELLED;
			// }
			//
			// publishProgress(null);
			//
			// for (String imageUrl : imageUrls) {
			// if (!Utils.isEmpty(imageUrl)) {
			// // Fetch image to cache.
			// try {
			// imageManager.put(imageUrl);
			// } catch (IOException e) {
			// Log.e(TAG, e.getMessage(), e);
			// }
			// }
			//
			// if (isCancelled()) {
			// return TaskResult.CANCELLED;
			// }
			// }

			return TaskResult.OK;
		}
	}

	private static class Adapter extends CursorAdapter {

		public Adapter(Context context, Cursor cursor) {
			super(context, cursor);

			mInflater = LayoutInflater.from(context);

			// TODO: 可使用:
			// DM dm = MessageTable.parseCursor(cursor);
			mUserTextColumn = cursor
					.getColumnIndexOrThrow(MessageTable.FIELD_USER_SCREEN_NAME);
			mTextColumn = cursor.getColumnIndexOrThrow(MessageTable.FIELD_TEXT);
			mProfileImageUrlColumn = cursor
					.getColumnIndexOrThrow(MessageTable.FIELD_PROFILE_IMAGE_URL);
			mCreatedAtColumn = cursor
					.getColumnIndexOrThrow(MessageTable.FIELD_CREATED_AT);
			mIsSentColumn = cursor
					.getColumnIndexOrThrow(MessageTable.FIELD_IS_SENT);
		}

		private LayoutInflater mInflater;

		private int mUserTextColumn;
		private int mTextColumn;
		private int mProfileImageUrlColumn;
		private int mIsSentColumn;
		private int mCreatedAtColumn;

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			View view = mInflater.inflate(R.layout.direct_message, parent,
					false);

			ViewHolder holder = new ViewHolder();
			holder.userText = (TextView) view
					.findViewById(R.id.tweet_user_text);
			holder.tweetText = (TextView) view.findViewById(R.id.tweet_text);
			holder.profileImage = (ImageView) view
					.findViewById(R.id.profile_image);
			holder.metaText = (TextView) view
					.findViewById(R.id.tweet_meta_text);
			view.setTag(holder);

			return view;
		}

		class ViewHolder {
			public TextView userText;
			public TextView tweetText;
			public ImageView profileImage;
			public TextView metaText;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			ViewHolder holder = (ViewHolder) view.getTag();

			int isSent = cursor.getInt(mIsSentColumn);
			String user = cursor.getString(mUserTextColumn);

			if (0 == isSent) {
				holder.userText.setText(context
						.getString(R.string.direct_message_label_from_prefix)
						+ user);
			} else {
				holder.userText.setText(context
						.getString(R.string.direct_message_label_to_prefix)
						+ user);
			}

			TextHelper.setTweetText(holder.tweetText,
					cursor.getString(mTextColumn));

			String profileImageUrl = cursor.getString(mProfileImageUrlColumn);

			if (!TextUtils.isEmpty(profileImageUrl)) {
				holder.profileImage
						.setImageBitmap(TwitterApplication.mImageLoader.get(
								profileImageUrl, new ImageLoaderCallback() {

									@Override
									public void refresh(String url,
											Bitmap bitmap) {
										Adapter.this.refresh();
									}

								}));
			}

			try {
				holder.metaText.setText(DateTimeHelper
						.getRelativeDate(TwitterDatabase.DB_DATE_FORMATTER
								.parse(cursor.getString(mCreatedAtColumn))));
			} catch (ParseException e) {
				Log.w(TAG, "Invalid created at data.");
			}
		}

		public void refresh() {
			getCursor().requery();
		}

	}

	// Menu.

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// FIXME: 将刷新功能绑定到顶部的刷新按钮上，主菜单中的刷新选项已删除
		// case OPTIONS_MENU_ID_REFRESH:
		// doRetrieve();
		// return true;
		case OPTIONS_MENU_ID_TWEETS:
			launchActivity(TwitterActivity.createIntent(this));
			return true;
		case OPTIONS_MENU_ID_REPLIES:
			launchActivity(MentionActivity.createIntent(this));
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private static final int CONTEXT_REPLY_ID = 0;
	private static final int CONTEXT_DELETE_ID = 1;

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, CONTEXT_REPLY_ID, 0, R.string.cmenu_reply);
		menu.add(0, CONTEXT_DELETE_ID, 0, R.string.cmenu_delete);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		Cursor cursor = (Cursor) mAdapter.getItem(info.position);

		if (cursor == null) {
			Log.w(TAG, "Selected item not available.");
			return super.onContextItemSelected(item);
		}

		switch (item.getItemId()) {
		case CONTEXT_REPLY_ID:
			String user_id = cursor.getString(cursor
					.getColumnIndexOrThrow(MessageTable.FIELD_USER_ID));
			Intent intent = WriteDmActivity.createIntent(user_id);
			startActivity(intent);

			return true;
		case CONTEXT_DELETE_ID:
			int idIndex = cursor.getColumnIndexOrThrow(MessageTable._ID);
			String id = cursor.getString(idIndex);
			doDestroy(id);

			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private void doDestroy(String id) {
		Log.d(TAG, "Attempting delete.");

		if (mDeleteTask != null
				&& mDeleteTask.getStatus() == GenericTask.Status.RUNNING) {
			return;
		} else {
			mDeleteTask = new DmDeleteTask();
			mDeleteTask.setListener(mDeleteTaskListener);

			TaskParams params = new TaskParams();
			params.put("id", id);
			mDeleteTask.execute(params);
		}
	}

	private class DmDeleteTask extends GenericTask {

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			TaskParams param = params[0];
			try {
				String id = param.getString("id");
				DirectMessage directMessage = getApi().destroyDirectMessage(id);
				Dm.create(directMessage, false);
				getDb().deleteDm(id);
			} catch (HttpException e) {
				Log.e(TAG, e.getMessage(), e);
				return TaskResult.IO_ERROR;
			}

			if (isCancelled()) {
				return TaskResult.CANCELLED;
			}

			return TaskResult.OK;
		}
	}

	public void doRetrieve() {
		Log.d(TAG, "Attempting retrieve.");

		if (mRetrieveTask != null
				&& mRetrieveTask.getStatus() == GenericTask.Status.RUNNING) {
			return;
		} else {
			mRetrieveTask = new DmRetrieveTask();
			mRetrieveTask.setFeedback(mFeedback);
			mRetrieveTask.setListener(mRetrieveTaskListener);
			mRetrieveTask.execute();
		}
	}

	public void goTop() {
		mTweetList.setSelection(0);
	}

	public void draw() {
		mAdapter.refresh();
		mInboxAdapter.refresh();
		mSendboxAdapter.refresh();
	}

	private void updateProgress(String msg) {
		mProgressText.setText(msg);
	}
}
