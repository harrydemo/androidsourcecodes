package com.xcontacts.ui;

import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.view.View;

import com.xcontacts.activities.EditContactActivity;
import com.xcontacts.ui.model.AbstractItem;
import com.xcontacts.ui.model.AbstractItemsContainer;
import com.xcontacts.utils.MyLog;

public class PhoneItemsContainer extends AbstractItemsContainer {

	public PhoneItemsContainer(Context context) {
		super(context);
	}
	@Override
	public ArrayList<ContentProviderOperation> getItemsContentProviderOperation() {
		int count = mItemsContainer.getChildCount();
		if (count > 0) {
			MyLog.w("Phone:");
			ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
			for (int i = 0; i < count; i++) {
				AbstractItem item = (AbstractItem) mItemsContainer
						.getChildAt(i);
				if (EditContactActivity.isInsert()) {// 新建联系人
					MyLog.i("新建联系人");
					ContentProviderOperation.Builder builder = ContentProviderOperation
							.newInsert(Data.CONTENT_URI);
					// withValueBackReference()函数的使用请参考：
					// http://stackoverflow.com/questions/4655291/semantics-of-withvaluebackreference
					builder.withValueBackReference(Data.RAW_CONTACT_ID, 0);
					builder.withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
					builder.withValues(item.getChanges());
					builder.withYieldAllowed(true);
					ops.add(builder.build());
				} else {// 编辑联系人
					MyLog.i("编辑联系人");
					long dataId = item.getDataId();
					MyLog.d("dataId:" + dataId);
					if (dataId > 0) {// 编辑原有的信息
						MyLog.i("	编辑原有的信息");
						ContentProviderOperation.Builder editContactBuilder = ContentProviderOperation
								.newUpdate(Data.CONTENT_URI);
						// 更新时的条件
						editContactBuilder.withSelection(Data._ID + "=?",
								new String[] { String.valueOf(dataId) });
						editContactBuilder.withValues(item.getChanges());
						MyLog.d("ContentValues:" + item.getChanges().toString());
						editContactBuilder.withYieldAllowed(true);
						ops.add(editContactBuilder.build());
					} else {// 没有获得dataId，说明用户在编辑时又添加了信息
						MyLog.i("	新建信息");
						ContentProviderOperation.Builder builder = ContentProviderOperation
								.newInsert(Data.CONTENT_URI).withValue(
										Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
						builder.withValue(Data.RAW_CONTACT_ID,
								EditContactActivity.getRawContactId());
						MyLog.d("	rawContactId:"
								+ EditContactActivity.getRawContactId());
						builder.withValues(item.getChanges());
						builder.withYieldAllowed(true);
						ops.add(builder.build());
					}
				}
			}
			return ops;
		} else {
			return null;
		}
	}

	@Override
	public void initViews(int strRes, int typesRes, int hintRes) {
		int stringRes = strRes;
		final int itemTypesRes = typesRes;
		final int itemHintRes = hintRes;

		this.tvHeader.setHint(stringRes);

		this.ibAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addItem(itemTypesRes, itemHintRes);
			}
		});
	}

	public void addItem(int typesRes, int hintRes) {
		AbstractItem item = new PhoneItem(mContext);
		item.initViews(mItemsContainer, typesRes, hintRes);
		mItemsContainer.addView(item);
	}
}