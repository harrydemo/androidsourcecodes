package com.markupartist.android.widget;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.ch_linghu.fanfoudroid.R;

public class PullToRefreshListView extends ListView implements
		OnScrollListener, GestureDetector.OnGestureListener {

	private final int MAXHEIGHT = 20;
	private static final int TAP_TO_REFRESH = 1;
	private static final int PULL_TO_REFRESH = 2;
	private static final int RELEASE_TO_REFRESH = 3;
	private static final int REFRESHING = 4;
	// private static final int RELEASING = 5;//释放过程做动画用

	private static final String TAG = "PullToRefreshListView";

	private OnRefreshListener mOnRefreshListener;

	/**
	 * Listener that will receive notifications every time the list scrolls.
	 */
	private OnScrollListener mOnScrollListener;
	private LayoutInflater mInflater;

	private LinearLayout mRefreshView;
	private TextView mRefreshViewText;
	private ImageView mRefreshViewImage;
	private ProgressBar mRefreshViewProgress;
	private TextView mRefreshViewLastUpdated;

	private int mCurrentScrollState;
	private int mRefreshState;

	private RotateAnimation mFlipAnimation;
	private RotateAnimation mReverseFlipAnimation;

	private int mRefreshViewHeight;
	private int mRefreshOriginalTopPadding;
	private int mLastMotionY;
	private GestureDetector mDetector;
//	private int mPadding;

	public PullToRefreshListView(Context context) {
		super(context);
		init(context);
	}

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public PullToRefreshListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {

		GestureDetector localGestureDetector = new GestureDetector(this);
		this.mDetector = localGestureDetector;

		// Load all of the animations we need in code rather than through XML
		mFlipAnimation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mFlipAnimation.setInterpolator(new LinearInterpolator());
		mFlipAnimation.setDuration(200);
		mFlipAnimation.setFillAfter(true);
		mReverseFlipAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
		mReverseFlipAnimation.setDuration(200);
		mReverseFlipAnimation.setFillAfter(true);

		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		mRefreshView = (LinearLayout) mInflater.inflate(
				R.layout.pull_to_refresh_header, null);

		mRefreshViewText = (TextView) mRefreshView
				.findViewById(R.id.pull_to_refresh_text);
		mRefreshViewImage = (ImageView) mRefreshView
				.findViewById(R.id.pull_to_refresh_image);
		mRefreshViewProgress = (ProgressBar) mRefreshView
				.findViewById(R.id.pull_to_refresh_progress);
		mRefreshViewLastUpdated = (TextView) mRefreshView
				.findViewById(R.id.pull_to_refresh_updated_at);

		mRefreshViewImage.setMinimumHeight(50);
		mRefreshView.setOnClickListener(new OnClickRefreshListener());
		mRefreshOriginalTopPadding = mRefreshView.getPaddingTop();

		mRefreshState = TAP_TO_REFRESH;

		addHeaderView(mRefreshView);

		super.setOnScrollListener(this);

		measureView(mRefreshView);
		mRefreshViewHeight = mRefreshView.getMeasuredHeight();
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);

		setSelection(1);
	}

	/**
	 * Set the listener that will receive notifications every time the list
	 * scrolls.
	 * 
	 * @param l
	 *            The scroll listener.
	 */
	@Override
	public void setOnScrollListener(AbsListView.OnScrollListener l) {
		mOnScrollListener = l;
	}

	/**
	 * Register a callback to be invoked when this list should be refreshed.
	 * 
	 * @param onRefreshListener
	 *            The callback to run.
	 */
	public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
		mOnRefreshListener = onRefreshListener;
	}

	/**
	 * Set a text to represent when the list was last updated.
	 * 
	 * @param lastUpdated
	 *            Last updated at.
	 */
	public void setLastUpdated(CharSequence lastUpdated) {
		if (lastUpdated != null) {
			mRefreshViewLastUpdated.setVisibility(View.VISIBLE);
			mRefreshViewLastUpdated.setText(lastUpdated);
		} else {
			mRefreshViewLastUpdated.setVisibility(View.GONE);
		}
	}

	@Override
	/**
	 * TODO:此方法重写
	 */
	public boolean onTouchEvent(MotionEvent event) {

		GestureDetector localGestureDetector = this.mDetector;
		localGestureDetector.onTouchEvent(event);
		final int y = (int) event.getY();

		Log.d(TAG,
				String.format(
						"[onTouchEvent]event.Action=%d, currState=%d, refreshState=%d,y=%d",
						event.getAction(), mCurrentScrollState, mRefreshState,
						y));
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			if (!isVerticalScrollBarEnabled()) {
				setVerticalScrollBarEnabled(true);
			}
			if (getFirstVisiblePosition() == 0 && mRefreshState != REFRESHING) {
				if ((mRefreshView.getBottom() >= mRefreshViewHeight + MAXHEIGHT || mRefreshView
						.getTop() >= 0)) {
					// Initiate the refresh
					mRefreshState = REFRESHING;
					prepareForRefresh();
					onRefresh();
				} else if (mRefreshView.getBottom() < mRefreshViewHeight
						+ MAXHEIGHT
						|| mRefreshView.getTop() <= 0) {
					// Abort refresh and scroll down below the refresh view
					resetHeader();
					setSelection(1);
				}
			}
			break;
		case MotionEvent.ACTION_DOWN:

			mLastMotionY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			applyHeaderPadding(event);

			break;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * TODO:此方法重写
	 * @param ev
	 */
	private void applyHeaderPadding(MotionEvent ev) {
		final int historySize = ev.getHistorySize();

		Log.d(TAG, String.format(
				"[applyHeaderPadding]currState=%d, refreshState=%d",
				mCurrentScrollState, mRefreshState));
		// Workaround for getPointerCount() which is unavailable in 1.5
		// (it's always 1 in 1.5)
		int pointerCount = 1;
		try {
			Method method = MotionEvent.class.getMethod("getPointerCount");
			pointerCount = (Integer) method.invoke(ev);
		} catch (NoSuchMethodException e) {
			pointerCount = 1;
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (IllegalAccessException e) {
			System.err.println("unexpected " + e);
		} catch (InvocationTargetException e) {
			System.err.println("unexpected " + e);
		}
		if (mRefreshState == RELEASE_TO_REFRESH) {
//			this.offsetTopAndBottom(-mPadding);

			for (int h = 0; h < historySize; h++) {
				for (int p = 0; p < pointerCount; p++) {

					if (isVerticalFadingEdgeEnabled()) {
						setVerticalScrollBarEnabled(false);
					}

					int historicalY = 0;
					try {
						// For Android > 2.0
						Method method = MotionEvent.class.getMethod(
								"getHistoricalY", Integer.TYPE, Integer.TYPE);
						historicalY = ((Float) method.invoke(ev, p, h))
								.intValue();
					} catch (NoSuchMethodException e) {
						// For Android < 2.0
						historicalY = (int) (ev.getHistoricalY(h));
					} catch (IllegalArgumentException e) {
						throw e;
					} catch (IllegalAccessException e) {
						System.err.println("unexpected " + e);
					} catch (InvocationTargetException e) {
						System.err.println("unexpected " + e);
					}

					// Calculate the padding to apply, we divide by 1.7 to
					// simulate a more resistant effect during pull.
					int topPadding = (int) (((historicalY - mLastMotionY) - mRefreshViewHeight) / 1.7);
//					Log.d(TAG,
//							String.format(
//									"[applyHeaderPadding]historicalY=%d,topPadding=%d,mRefreshViewHeight=%d,mLastMotionY=%d",
//									historicalY, topPadding,
//									mRefreshViewHeight, mLastMotionY));
					mRefreshView.setPadding(mRefreshView.getPaddingLeft(),
							topPadding, mRefreshView.getPaddingRight(),
							mRefreshView.getPaddingBottom());
				}
			}
		}
	}

	/**
	 * Sets the header padding back to original size.
	 */
	private void resetHeaderPadding() {
		Log.d(TAG, String.format(
				"[resetHeaderPadding]currState=%d, refreshState=%d",
				mCurrentScrollState, mRefreshState));
		// invalidate();

		//this.mPadding=0;
		//this.offsetTopAndBottom(this.mPadding);
		
		mRefreshView.setPadding(mRefreshView.getPaddingLeft(),
				mRefreshOriginalTopPadding, mRefreshView.getPaddingRight(),
				mRefreshView.getPaddingBottom());
	}

	/**
	 * Resets the header to the original state.
	 */
	private void resetHeader() {
		Log.d(TAG, String.format("[resetHeader]currState=%d, refreshState=%d",
				mCurrentScrollState, mRefreshState));
		if (mRefreshState != TAP_TO_REFRESH) {
			mRefreshState = TAP_TO_REFRESH;

			resetHeaderPadding();

			// Set refresh view text to the pull label
			// mRefreshViewText.setText(R.string.pull_to_refresh_tap_label);//点击刷新是否有用
			mRefreshViewText.setText(R.string.pull_to_refresh_pull_label);
			// Replace refresh drawable with arrow drawable
			mRefreshViewImage
					.setImageResource(R.drawable.ic_pulltorefresh_arrow);
			// Clear the full rotation animation
			mRefreshViewImage.clearAnimation();
			// Hide progress bar and arrow.
			mRefreshViewImage.setVisibility(View.GONE);
			mRefreshViewProgress.setVisibility(View.GONE);
		}
	}

	private void measureView(View child) {
		Log.d(TAG, String.format("[measureView]currState=%d, refreshState=%d",
				mCurrentScrollState, mRefreshState));
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		Log.d(TAG, "List onScroll");
		
		
		if (mCurrentScrollState == SCROLL_STATE_FLING
				&& firstVisibleItem == 0 && mRefreshState != REFRESHING) {
			setSelection(1);
			mRefreshViewImage.setVisibility(View.INVISIBLE);
		}
		
		
		if (mOnScrollListener != null) {
			mOnScrollListener.onScroll(this, firstVisibleItem,
					visibleItemCount, totalItemCount);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		mCurrentScrollState = scrollState;

		if (mOnScrollListener != null) {
			mOnScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	public void prepareForRefresh() {
		resetHeaderPadding();

		mRefreshViewImage.setVisibility(View.GONE);
		// We need this hack, otherwise it will keep the previous drawable.
		mRefreshViewImage.setImageDrawable(null);
		mRefreshViewProgress.setVisibility(View.VISIBLE);

		// Set refresh view text to the refreshing label
		mRefreshViewText.setText(R.string.pull_to_refresh_refreshing_label);

		mRefreshState = REFRESHING;
	}

	public void onRefresh() {
		Log.d(TAG, "onRefresh");

		if (mOnRefreshListener != null) {
			mOnRefreshListener.onRefresh();
		}
	}

	/**
	 * Resets the list to a normal state after a refresh.
	 * 
	 * @param lastUpdated
	 *            Last updated at.
	 */
	public void onRefreshComplete(CharSequence lastUpdated) {
		setLastUpdated(lastUpdated);
		onRefreshComplete();
	}

	/**
	 * Resets the list to a normal state after a refresh.
	 */
	public void onRefreshComplete() {
		Log.d(TAG, "onRefreshComplete");

		resetHeader();

		// If refresh view is visible when loading completes, scroll down to
		// the next item.
		if (mRefreshView.getBottom() > 0) {
			invalidateViews();
			// setSelection(1);
		}
	}

	/**
	 * Invoked when the refresh view is clicked on. This is mainly used when
	 * there's only a few items in the list and it's not possible to drag the
	 * list.
	 */
	private class OnClickRefreshListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (mRefreshState != REFRESHING) {
				prepareForRefresh();
				onRefresh();
			}
		}

	}

	/**
	 * Interface definition for a callback to be invoked when list should be
	 * refreshed.
	 */
	public interface OnRefreshListener {
		/**
		 * Called when the list should be refreshed.
		 * <p>
		 * A call to {@link PullToRefreshListView #onRefreshComplete()} is
		 * expected to indicate that the refresh has completed.
		 */
		public void onRefresh();
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {

		int firstVisibleItem = this.getFirstVisiblePosition();

		// When the refresh view is completely visible, change the text to say
		// "Release to refresh..." and flip the arrow drawable.

		Log.d(TAG, String.format(
				"[OnScroll]first=%d, currState=%d, refreshState=%d",
				firstVisibleItem, mCurrentScrollState, mRefreshState));

		if (mCurrentScrollState == SCROLL_STATE_TOUCH_SCROLL
				&& mRefreshState != REFRESHING) {
			if (firstVisibleItem == 0) {
				mRefreshViewImage.setVisibility(View.VISIBLE);
//				Log.d(TAG,
//						String.format(
//								"getBottom=%d,refreshViewHeight=%d,getTop=%d,mRefreshOriginalTopPadding=%d",
//								mRefreshView.getBottom(), mRefreshViewHeight
//										+ MAXHEIGHT,
//								this.getTopPaddingOffset(),
//								mRefreshOriginalTopPadding));
				if ((mRefreshView.getBottom() >= mRefreshViewHeight + MAXHEIGHT || mRefreshView
						.getTop() >= 0) && mRefreshState != RELEASE_TO_REFRESH) {
					//this.mPadding+=distanceY;//备用
					mRefreshViewText
							.setText(R.string.pull_to_refresh_release_label);
					mRefreshViewImage.clearAnimation();
					mRefreshViewImage.startAnimation(mFlipAnimation);
					mRefreshState = RELEASE_TO_REFRESH;
				} else if (mRefreshView.getBottom() < mRefreshViewHeight
						+ MAXHEIGHT
						&& mRefreshState != PULL_TO_REFRESH) {
					mRefreshViewText
							.setText(R.string.pull_to_refresh_pull_label);
					if (mRefreshState != TAP_TO_REFRESH) {
						mRefreshViewImage.clearAnimation();
						mRefreshViewImage.startAnimation(mReverseFlipAnimation);
					}
					mRefreshState = PULL_TO_REFRESH;

				}
			} else {
				mRefreshViewImage.setVisibility(View.GONE);
				resetHeader();
			}
		} /*not execute
		else if (mCurrentScrollState == SCROLL_STATE_FLING
				&& firstVisibleItem == 0 && mRefreshState != REFRESHING) {
			setSelection(1);
		}*/

	

		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}
}
