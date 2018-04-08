package com.xcontacts.ui;

import com.xcontacts.activities.R;
import com.xcontacts.utils.MyLog;
import com.xcontacts.utils.MyTextWachter;

import android.content.ContentValues;
import android.content.Context;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Website;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class WebsiteItem extends LinearLayout {

	/**
	 * 包裹相同类型信息的LinearLayout。如：包裹Phone信息的最外层的LinearLayout
	 */
	private LinearLayout mContainer;
	/**
	 * 包裹一行的LinearLayout
	 */
	private LinearLayout mItemContainer;
	private EditText etWebsite;
	private ImageButton ibDele;

	/**
	 * 编辑联系人时，我们需要每条信息在Data表中_id的值，用作更新条件
	 */
	private long dataId;
	/**
	 * 编辑联系人时，当某些信息被更新时，我们把改变后的信息存放在changes对象中
	 */
	private ContentValues changes = null;

	public WebsiteItem(Context context) {
		super(context);
		mItemContainer = this;
		mItemContainer.setOrientation(HORIZONTAL);
		mItemContainer.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		etWebsite = new EditText(context);
		etWebsite.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, 1));

		ibDele = new ImageButton(context);
		ibDele.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		ibDele.setBackgroundResource(R.drawable.ic_btn_round_minus);

		mItemContainer.addView(etWebsite);
		mItemContainer.addView(ibDele);
	}

	public void initViews(LinearLayout container, int arrayRes, int hintRes) {
		this.mContainer = container;
		etWebsite.setHint(hintRes);
		
		// 监听EditText中内容变化
		etWebsite.addTextChangedListener(new MyTextWachter(Website.URL,
				getChanges()));
		// 点击，删除这一行
		this.ibDele.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mContainer.removeView(mItemContainer);
				// 如果是编辑联系人时删除原来的信息，则应当在数据库中删除相关记录
				long dataId = getDataId();
				if (dataId > 0) {
					getContext().getContentResolver().delete(Data.CONTENT_URI,
							Data._ID + "=?",
							new String[] { String.valueOf(dataId) });
					MyLog.w("删除联系人的原有信息！删除条件是：Data._id = " + dataId);
				}
			}
		});
	}

	/**
	 * @return the dataId
	 */
	public long getDataId() {
		return dataId;
	}

	/**
	 * @param dataId
	 *            the dataId to set
	 */
	public void setDataId(long dataId) {
		this.dataId = dataId;
	}

	public ContentValues getChanges() {
		if (changes == null) {
			changes = new ContentValues();
		}
		return changes;
	}

	public void setContent(String content) {
		etWebsite.setText(content);
	}
}